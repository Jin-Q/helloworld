package com.yucheng.cmis.platform.permission.record;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.dbmodel.service.RecordRestrict;

/**
 * <p>读取记录级权限控制模板文件</p>
 * 模板配置文件格式为：
 * <TEMPLATE>
 *    <MODEL modelId='表模型ID1' BCHField='权限控制字段--机构' USRField='权限控制字段--用户' >
 *       <QUERY duty='岗位ID1,岗位ID2,...岗位IDn'>查询记录级权限模板类名</QUERY>
 *       <QUERY duty='岗位ID1,岗位ID2,...'>查询记录级权限模板类名</QUERY>
 *       ...
 *       <UPDATE duty='岗位ID1,岗位ID2,...岗位IDn'>修改记录级权限模板类名</UPDATE>
 *       <UPDATE duty='岗位ID1'>修改记录级权限模板类名</UPDATE>
 *       ...
 *       <DELETE duty='岗位ID1,岗位ID2,...岗位IDn'>删除记录级权限模板类名</DELETE>
 *       <DELETE>删除记录级权限模板类名</DELETE>
 *       ...
 *    </MODEL>
 *      ... ...
 * </TEMPLATE>
 * @author Administrator
 *
 */

public class ConfigLoader {

	private static final String filename = "datapermission.xml";
	private static final String TAG_RECORD_START = "MODEL";
	
	public static void loadPermissionConfig(){
		
		ResourceBundle res = ResourceBundle.getBundle("cmis");
        String FileRootPath = res.getString("permission.file.path");
        String pathString = "";
        if(FileRootPath != null && FileRootPath.toLowerCase().startsWith("classpath:")) {
	        URL url =  Thread.currentThread().getContextClassLoader().getResource("");
	        pathString = url.getPath();
        }else{
        	pathString = FileRootPath;
        }
        
        //System.err.println("datapermission.file.path = " + FileRootPath + "  " + pathString);
          
		String fileNm = pathString + "/" + filename;
		EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "记录级权限配置文件加载" + fileNm);
		
	    java.io.FileReader fr = null;
        java.io.BufferedReader br = null;
        
		try {
			
			DocumentBuilderFactory doBuilderFactory= DocumentBuilderFactory.newInstance();	
			DocumentBuilder doBuilder = doBuilderFactory.newDocumentBuilder();
			
	    	File xmlfile = new java.io.File(fileNm);
		    fr = new java.io.FileReader(xmlfile);
		    br = new java.io.BufferedReader(fr);
		    /** 一次处理一笔数据 */
		    String st_line = "";
		    StringBuffer stb_onerec = new StringBuffer();
			while(br.ready() && (st_line = br.readLine()) != null){
				if(st_line.trim().startsWith("<" + TAG_RECORD_START + " ")){
					/**data begin*/
					stb_onerec.delete(0, stb_onerec.length()); ///清空已有数据
				}
				/** 读取并加载数据至BUFFER中*/
				stb_onerec.append(st_line);
				if(st_line.trim().startsWith("</" + TAG_RECORD_START + ">") 
						|| st_line.trim().endsWith("</" + TAG_RECORD_START + ">")){
			    	java.io.StringReader sr = new java.io.StringReader(stb_onerec.toString());
			    	InputSource is_xml = new InputSource(sr);
					Document doc = doBuilder.parse(is_xml);
					
					loadxml2Sys(doc);
					sr.close();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fr.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "记录级权限加载结束");
	}
	
	private static void loadxml2Sys(Document doc){
		
		try {

			NodeList modelList = doc.getElementsByTagName("MODEL");
			//System.err.println(">>>" + modelList.getLength());
			if(modelList != null && modelList.getLength() > 0){
				Node modelNode = modelList.item(0);
				
				if(modelNode == null){
					EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "记录级权限配置为空，无需加载");
					return ;
				}
				
				String modelId = ((Element)modelNode).getAttribute("modelId");
				String BCHField = ((Element)modelNode).getAttribute("BCHField");//机构字段
				String USRField = ((Element)modelNode).getAttribute("USRField");//用户字段
				
				Config.permissionBCHField.put(modelId, BCHField);
				Config.permissionUSRField.put(modelId, USRField);
				//System.err.println(modelNode.getOwnerDocument());
				
				NodeList queryNodeList = modelNode.getOwnerDocument().getElementsByTagName("QUERY");
				
				if(queryNodeList != null && queryNodeList.getLength() > 0){
					for(int i=0; i<queryNodeList.getLength(); i++){
						Node subNode = queryNodeList.item(i);
						String duty = ((Element)subNode).getAttribute("duty");
						String st_template = subNode.getTextContent();
						//System.err.println("" + duty);

						load2buf(RecordRestrict.QUERY_RESTRICT,modelId,duty,st_template);
					}
				}else{
					EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "表模型【" + modelId + "】无查询记录权限控制");
				}
				
				NodeList deleteNodeList = modelNode.getOwnerDocument().getElementsByTagName("DELETE");
				
				if(deleteNodeList != null && deleteNodeList.getLength() > 0){
					for(int i=0; i<deleteNodeList.getLength(); i++){
						Node subNode = deleteNodeList.item(i);
						String duty = ((Element)subNode).getAttribute("duty");
						String st_template = subNode.getTextContent();
						//System.err.println("" + duty);
						load2buf(RecordRestrict.DELETE_RESTRICT,modelId,duty,st_template);
					}
				}else{
					EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "表模型【" + modelId + "】无删除记录权限控制");
				}
				NodeList updateNodeList = modelNode.getOwnerDocument().getElementsByTagName("UPDATE");
				
				if(updateNodeList != null && updateNodeList.getLength() > 0){
					for(int i=0; i<updateNodeList.getLength(); i++){
						Node subNode = updateNodeList.item(i);
						String duty = ((Element)subNode).getAttribute("duty");
						String st_template = subNode.getTextContent();
						//System.err.println("" + duty);
						load2buf(RecordRestrict.UPDATE_RESTRICT,modelId,duty,st_template);
					}
				}else{
					EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "表模型【" + modelId + "】无修改记录权限控制");
				}
				
				EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "成功加载表模型【" + modelId + "】记录级权限配置");
			}else{
				EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.ERROR, 0, "记录级权限配置为空");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	}
	
	private static void load2buf(String restrict, String modelId, String duty, String st_template){
    	if(duty != null && !duty.trim().equals("")){
    		duty += ",";
    		String[] _dutyList = duty.split(",");
    		for(int du=0; du<_dutyList.length; du++){
    		   if(_dutyList[du] != null && !_dutyList[du].trim().equals("")){
    		      Config.permissionTemplate.put(modelId + restrict + _dutyList[du], st_template);
    		   }
    		}
    	} else {
	        Config.permissionTemplate.put(modelId + restrict, st_template);
    	}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConfigLoader.loadPermissionConfig();
		
		for(Iterator itr=Config.permissionTemplate.keySet().iterator(); itr.hasNext();){
			String st_key = (String)itr.next();
			System.err.println(st_key + "   " + Config.permissionTemplate.get(st_key));
		}
		
		for(Iterator itr=Config.permissionBCHField.keySet().iterator(); itr.hasNext();){
			String st_key = (String)itr.next();
			System.err.println(st_key + "   " + Config.permissionBCHField.get(st_key));
		}
		
		for(Iterator itr=Config.permissionUSRField.keySet().iterator(); itr.hasNext();){
			String st_key = (String)itr.next();
			System.err.println(st_key + "   " + Config.permissionUSRField.get(st_key));
		}
		

	}

}
