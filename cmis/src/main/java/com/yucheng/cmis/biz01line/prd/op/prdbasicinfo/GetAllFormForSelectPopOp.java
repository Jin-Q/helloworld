package com.yucheng.cmis.biz01line.prd.op.prdbasicinfo;

import java.io.File;
import java.net.URL;
import java.sql.Connection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class GetAllFormForSelectPopOp extends CMISOperation {

	//private static final String MODEL_XML_PATH = "F:/WorkSpaces/XDWork/cmis/WebContent/WEB-INF/tables/";
	String floderPath = "";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			IndexedCollection iColl = new IndexedCollection();  //从缓存中读出来的总记录集合,特殊场合适用
			IndexedCollection iCollPage = new IndexedCollection();//条件查询时，符合条件的记录集合
			IndexedCollection iCollForPage = new IndexedCollection();//分页时，展示在页面上的记录集合
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String flag ="";
			try {
				flag = (String)context.getDataValue("flag");
				queryData = (KeyedCollection)context.getDataElement("Model");
			} catch (Exception e) {}
			
			/** 读取所有表模型的XML，以模型ID、模型名称为一条记录封装到IColl中 */
			/*File dir = new File(MODEL_XML_PATH);
			if(!dir.isDirectory()) {
				throw new Exception("表模型XML文件路径"+MODEL_XML_PATH+"获取错误！");
			}
			String[] files = dir.list();
			
			for(int i=0;i<files.length;i++){
				if(files[i].endsWith(".xml")){
					this.readXML2IColl(MODEL_XML_PATH+"/"+files[i], iColl);
				}else if(files[i].indexOf(".") == -1){
					*//** 处理文件夹 *//*
					//MODEL_XML_PATH += files[i]+"/";
					//this.recurrenceFolder(MODEL_XML_PATH, iColl);					
				}
			}*/
			/*取table文件路径
			ResourceBundle res = ResourceBundle.getBundle("cmis");
	        String ProjectPath = res.getString("cmisProjectPath");
			String MODEL_XML_PATH = ProjectPath+"/WEB-INF/tables/";*/
			URL url =  Thread.currentThread().getContextClassLoader().getResource("");
			String MODEL_XML_PATH = url.getPath().replace("/classes", "")+"tables/";
			floderPath = MODEL_XML_PATH;
			this.recurrenceFolder(floderPath, iColl,flag);
			//分页，通过获取pageInfo信息，手动给iColl分割显示
			int size = 10;
			int RecordSize = 0;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			pageInfo.setRecordSize(String.valueOf(iColl.size()));
			//输入查询条件时，分情况对iColl进行循环遍历查询
			if(queryData!=null){
				if(queryData.getDataValue("prdname")!="" && queryData.getDataValue("prdid")==""){
					String prdname = (String)queryData.getDataValue("prdname");
					prdname= prdname.trim();
					//输入查询条件时，查询符合条件的记录总数
					for(int i=0;i<iColl.size();i++){	
						KeyedCollection kColl = (KeyedCollection)iColl.get(i);
						String prdnameIColl = (String)kColl.getDataValue("modelName");
							if(prdnameIColl.contains(prdname)){
								iCollPage.addDataElement(kColl);
						    }
					}
					//页面展示集合
					for(int i=(pageInfo.pageIdx*pageInfo.pageSize-pageInfo.pageSize);i<pageInfo.pageSize*pageInfo.pageIdx;i++){
						if(iCollPage.size()>i){
							   KeyedCollection kColl = (KeyedCollection)iCollPage.get(i);
							   iCollForPage.addDataElement(kColl);
						}
					}
					//输入查询条件时，总记录数相应改变
					pageInfo.setRecordSize(String.valueOf(iCollPage.size()));
				}else if(queryData.getDataValue("prdname")=="" && queryData.getDataValue("prdid")!=""){
					String prdid = (String)queryData.getDataValue("prdid");
					prdid = prdid.trim();
					for(int i=0;i<iColl.size();i++){						
						KeyedCollection kColl = (KeyedCollection)iColl.get(i);
						String prdidIColl = (String)kColl.getDataValue("modelId");
						if(prdidIColl.contains(prdid)){
							iCollPage.addDataElement(kColl);
						}
					}
					for(int i=(pageInfo.pageIdx*pageInfo.pageSize-pageInfo.pageSize);i<pageInfo.pageSize*pageInfo.pageIdx;i++){
						if(iCollPage.size()>i){
							   KeyedCollection kColl = (KeyedCollection)iCollPage.get(i);
							   iCollForPage.addDataElement(kColl);
						}
					}
					//输入查询条件时，总记录数相应改变
					pageInfo.setRecordSize(String.valueOf(iCollPage.size()));
				}else if(queryData.getDataValue("prdname")!="" && queryData.getDataValue("prdid")!=""){
					String prdname = (String)queryData.getDataValue("prdname");
					String prdid = (String)queryData.getDataValue("prdid");
					prdname = prdname.trim();
					prdid = prdid.trim();
					for(int i=0;i<iColl.size();i++){						
						KeyedCollection kColl = (KeyedCollection)iColl.get(i);
						String prdnameIColl = (String)kColl.getDataValue("modelName");
						String prdidIColl = (String)kColl.getDataValue("modelId");
						if(prdidIColl.contains(prdid) && prdnameIColl.contains(prdname)){
							iCollPage.addDataElement(kColl);
						}
					}
					for(int i=(pageInfo.pageIdx*pageInfo.pageSize-pageInfo.pageSize);i<pageInfo.pageSize*pageInfo.pageIdx;i++){
						if(iCollPage.size()>i){
							   KeyedCollection kColl = (KeyedCollection)iCollPage.get(i);
							   iCollForPage.addDataElement(kColl);
						}
					}
					//输入查询条件时，总记录数相应改变
					pageInfo.setRecordSize(String.valueOf(iCollPage.size()));
				}else if(queryData.getDataValue("prdname")=="" && queryData.getDataValue("prdid")==""){
					for(int i=(pageInfo.pageIdx*pageInfo.pageSize-pageInfo.pageSize);i<pageInfo.pageSize*pageInfo.pageIdx;i++){
					    if(iColl.size()>i){
						  KeyedCollection kColl = (KeyedCollection) iColl.get(i);
						  iCollForPage.addDataElement(kColl);
						}
				    }
				}
				
			}else{
				for(int i=(pageInfo.pageIdx*pageInfo.pageSize-pageInfo.pageSize);i<pageInfo.pageSize*pageInfo.pageIdx;i++){
					 if(iColl.size()>i){
					   KeyedCollection kColl = (KeyedCollection) iColl.get(i);
					   iCollForPage.addDataElement(kColl); 
					 }
			    }
			} 
			iCollForPage.setName("modelList");			
			this.putDataElement2Context(iCollForPage, context);						
			TableModelUtil.parsePageInfo(context, pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}
	
	/**
	 * 根据XML文件路径读取表模型XML文件，将模型ID,模型名称存入IColl中
	 * @param filePath XML文件路径
	 * @param iColl IndexedCollection
	 * @throws Exception
	 */
	public void readXML2IColl(String filePath, IndexedCollection iColl,String flag) throws Exception{
		/** 处理XML文件 */
		DocumentBuilderFactory doBuilderFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder doBuilder = doBuilderFactory.newDocumentBuilder();
		Document doc =  doBuilder.parse(filePath);
		
		NodeList nodeFlowList = doc.getElementsByTagName("TableModel");
		for(int j=0;j<nodeFlowList.getLength();j++){
			Node node = nodeFlowList.item(j);
			String modelId = node.getAttributes().getNamedItem("id").getNodeValue();
			String modelName = node.getAttributes().getNamedItem("cnname").getNodeValue();
			KeyedCollection kColl = new KeyedCollection();
			if(modelId.startsWith(flag)){ 
				kColl.addDataField("modelId", modelId);
				kColl.addDataField("modelName", modelName);
				iColl.add(kColl);
			}
		}
	}
	
	/**
	 * 根据文件路径，递归查询文件目录下子目录，查询出所有.xml文件
	 * @param path 文件路径
	 * @param iColl 表模型存入的IndexedCollection
	 */
	public void recurrenceFolder(String path, IndexedCollection iColl,String flag) throws Exception {
		File dir = new File(path);
		if(!dir.isDirectory()) {
			throw new Exception("表模型XML文件路径"+path+"获取错误！");
		}
		String[] files = dir.list();
		for(int i=0;i<files.length;i++){
			if(files[i].endsWith(".xml")){
				/** 处理XML文件 */
				this.readXML2IColl(dir.getPath() +"/"+files[i], iColl,flag);
				/*DocumentBuilderFactory doBuilderFactory= DocumentBuilderFactory.newInstance();
				DocumentBuilder doBuilder = doBuilderFactory.newDocumentBuilder();
				Document doc =  doBuilder.parse(dir.getPath() +"\\"+files[i]);
				
				NodeList nodeFlowList = doc.getElementsByTagName("TableModel");
				for(int j=0;j<nodeFlowList.getLength();j++){
					Node node = nodeFlowList.item(j);
					String modelId = node.getAttributes().getNamedItem("id").getNodeValue();
					String modelName = node.getAttributes().getNamedItem("cnname").getNodeValue();
					KeyedCollection kColl = new KeyedCollection();
					kColl.addDataField("modelId", modelId);
					kColl.addDataField("modelName", modelName);
					iColl.add(kColl);
				}*/
			}else if(files[i].indexOf(".") == -1){
				/** 处理文件夹 */
				path = dir.getPath() +"/"+ files[i]+"/";
				recurrenceFolder(path, iColl, flag);
			}
		}
	}

}
