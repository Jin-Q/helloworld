package com.yucheng.cmis.pub.dao.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.yucheng.cmis.base.CMISComponetHelper;
import com.yucheng.cmis.util.CommonUtil;

public class SqlConfigLoader {

	public static final String TAG_RECORD_START = "SQL";
	public static final String TAG_SELECT = "SELECT";
	public static final String TAG_UPDATE = "UPDATE";
	public static final String TAG_DELETE = "DELETE";
	public static final String TAG_INSERT = "INSERT";
	public static final String TAG_OPTCONDITION = "OPT_CONDITION";
	
	public void addConfig(Document doc){
		
		if(doc == null){
			return ;
		}
		
		NodeList sqlNode = doc.getElementsByTagName(TAG_RECORD_START);
		for (int i=0;i<sqlNode.getLength();i++){
			String id = sqlNode.item(i).getAttributes().getNamedItem("id").getNodeValue();
			
			Node pClassNode = sqlNode.item(i).getAttributes().getNamedItem("parameterClass");
			String parameterClass =pClassNode!=null?pClassNode.getNodeValue():"";
			
			Node vClassNode = sqlNode.item(i).getAttributes().getNamedItem("valueClass");
			String valueClass =vClassNode!=null?vClassNode.getNodeValue():"";
			
			Node rClassNode = sqlNode.item(i).getAttributes().getNamedItem("resultClass");
			String resultClass = rClassNode!=null?rClassNode.getNodeValue():"";
			
			Node rFirstNode = sqlNode.item(i).getAttributes().getNamedItem("onlyReturnFirst");
			String onlyReturnFirst = rFirstNode!=null?rFirstNode.getNodeValue():"";
			
			String sql = "";
			String sqlType = "";
			String updTableName = "";
			boolean canUpdateAll = false;
			
			Document subdoc = sqlNode.item(i).getOwnerDocument();
			NodeList selectNode = subdoc.getElementsByTagName(TAG_SELECT);
			if(selectNode != null && selectNode.getLength() > 0){
				sql = selectNode.item(0).getTextContent();
				sqlType = TAG_SELECT;
				System.err.println(sql);
			}
			
			NodeList updateNode = subdoc.getElementsByTagName(TAG_UPDATE);
			if(updateNode != null && updateNode.getLength() > 0){
				sql = updateNode.item(0).getTextContent();
				Node tableNmNode = updateNode.item(0).getAttributes().getNamedItem("updTableName");
				updTableName = tableNmNode != null ? tableNmNode.getNodeValue():"";
				
				Node updateAllNode = updateNode.item(0).getAttributes().getNamedItem("canUpdateAll");
				canUpdateAll = updateAllNode != null ? Boolean.parseBoolean(updateAllNode.getNodeValue()):false;
				
				sqlType = TAG_UPDATE;
				System.err.println(sql);
			}
			
			NodeList deleteNode = subdoc.getElementsByTagName(TAG_DELETE);
			if(deleteNode != null && deleteNode.getLength() > 0){
				sql = deleteNode.item(0).getTextContent();
				sqlType = TAG_DELETE;
				System.err.println(sql);
			}
			
			NodeList addNode = subdoc.getElementsByTagName(TAG_INSERT);
			if(addNode != null && addNode.getLength() > 0){
				sql = addNode.item(0).getTextContent();
				Node tableNmNode = addNode.item(0).getAttributes().getNamedItem("updTableName");
				updTableName = tableNmNode != null ? tableNmNode.getNodeValue():"";
				sqlType = TAG_INSERT;
				System.err.println(sql);
			}
			
			if(sql == null || sql.trim().equals("")){
				if(updTableName == null || updTableName.trim().equals("")){
					System.err.println("SQLID:" + id + "中没有配置SQL，无法加载该配置！！");
					continue;
				}else{
					///使用了自动SQL
					sql = "";
				}
			}
			
			NodeList optCondiNode = subdoc.getElementsByTagName(TAG_OPTCONDITION);
			HashMap<String,String> optCondition = null;
			if(optCondiNode != null && optCondiNode.getLength() > 0){
				optCondition = new HashMap<String,String>();
				for(int k=0; k<optCondiNode.getLength(); k++){
					String condiId = optCondiNode.item(k).getAttributes().getNamedItem("id").getNodeValue();
					String condiRel = optCondiNode.item(k).getAttributes().getNamedItem("relationType").getNodeValue();
					String condi = optCondiNode.item(k).getTextContent();
					
					if(condi != null)
					   optCondition.put(condiId, condiRel + " (" + condi.trim() + ")");
					System.err.println(condiId + "  " + condiRel + "  " + condi.trim());
				}
			}
			
			System.err.println(id + "  " + parameterClass + "  " + resultClass + "  " + onlyReturnFirst);
			
			SqlConfig sqlCfg = new  SqlConfig();
			sqlCfg.setSqlid(id);
			sqlCfg.setParameterClass(parameterClass);
			sqlCfg.setValueClass(valueClass);
			sqlCfg.setResultClass(resultClass);
			
			sqlCfg.setOnlyReturnFirst(Boolean.parseBoolean(onlyReturnFirst));
			sqlCfg.setOptCondition(optCondition);
		    sqlCfg.setSql(sql.trim());
		    sqlCfg.setSqlType(sqlType);
		    sqlCfg.setUpdTableName(updTableName);
		    sqlCfg.setCanUpdateAll(canUpdateAll);
		    
			SqlConfigBuffer.setSqlConfig(id, sqlCfg);
		}
		
	}
	
	public boolean doDigestSQLFile(String filename)throws Exception {
		boolean boReturn = false;
		System.err.println("开始处理处文件" + filename);
	    File file = null;	    
	    java.io.FileReader fr = null;
        java.io.BufferedReader br = null;
        
        try {
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder builder=factory.newDocumentBuilder();
        	
	    	file = new java.io.File(filename);
		    fr = new java.io.FileReader(file);
		    br = new java.io.BufferedReader(fr);
		    
		    String st_line = "";
		    StringBuffer stb_onerec = new StringBuffer();
		    /** 一次处理一笔数据 */
			while(br.ready() && (st_line = br.readLine()) != null){
				if(st_line.trim().startsWith("<" + TAG_RECORD_START + " ")){
					/**app data begin*/
					stb_onerec.delete(0, stb_onerec.length()); ///清空已有数据
				}
				/** 读取并加载数据至BUFFER中*/
				stb_onerec.append(st_line);
				
				if(st_line.trim().startsWith("</" + TAG_RECORD_START + ">") 
						|| st_line.trim().endsWith("</" + TAG_RECORD_START + ">")){
					/**app data end*/
					/**开始 调用解释与存储 */
			    	java.io.StringReader sr = new java.io.StringReader(stb_onerec.toString());
			    	InputSource is_xml = new InputSource(sr);
					Document doc = builder.parse(is_xml);
					
					addConfig(doc);
					
					sr.close();
					boReturn = true;
				}
	  	  }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			boReturn = false;
		} catch (IOException e) {
			e.printStackTrace();
			boReturn = false;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			boReturn = false;
		} catch (SAXException e) {
			e.printStackTrace();
			boReturn = false;
		} finally{
			try {
				fr.close();
				br.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		
		return boReturn;
	}
	
	public void loadSqlConfig(){
		try{
			String path = CommonUtil.getClassesPath()+ "/sql/";
			System.out.println("getClassesPath：" + CommonUtil.getClassesPath());
			loadSqlConfig(path);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载path目录所有的.sql.xml文件，支持path下有多层子目录
	 * 
	 * @author yuhq
	 * @time 2013年05月16日 星期四 09时00分05秒 
	 * @param path
	 * @throws Exception
	 */
	private void loadSqlConfig(String path)throws Exception{
		try {
			File f = new File(path);
			File[] fList = f.listFiles();
			for(int n=0; n<fList.length; n++){
				if(fList[n].getName().endsWith(".sql.xml")){
					System.err.println("加载SQL配置文件：" + fList[n].getAbsolutePath());
					this.doDigestSQLFile(fList[n].getAbsolutePath());
				}
				//目录则循环加载
				if(fList[n].isDirectory())
					loadSqlConfig(fList[n].getPath());
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SqlConfigLoader sc = new SqlConfigLoader();
		sc.loadSqlConfig();

	}

}
