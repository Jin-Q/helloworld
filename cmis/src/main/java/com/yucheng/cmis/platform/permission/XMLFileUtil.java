package com.yucheng.cmis.platform.permission;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ecc.emp.log.EMPLog;

/**
 *@Classname	XMLFileUtil.java
 *@Version 1.0	
 *@Since   1.0 	Sep 18, 2008 4:55:38 PM  
 *@Copyright 	yuchengtech
 *@Author 		Eric
 *@Description：此类主要负责对业务主线配置文件XML文件的读写
 *@Lastmodified dec 7,2008 
 *@Author	    eirc
 */
public class XMLFileUtil{
	/*
	 * 存储所有需要解析的文件列表
	 */
	private ArrayList<String> al = new ArrayList<String>();
	
	private String m_fileType = "_cfg.xml";		  //文件类型	
	
	/*
	 * 组件配置文件的xml标签
	 */
	public final String 	BUSINESSLINES = "businessLines";
	
	public static final String 	BUSINESSLINES_CFG_FILE = "businesslines.xml";
	public static final String     DEFAULT_BUSINESSLINE_ID = "BL_DEFAULT";
	
	/**
	 * 从业务主线配置文件中读出业务主线配置信息
	 * @param dir 配置文件目录
	 * @return MAP格式的业务主线配置信息
	 * @throws Exception
	 */
	public Map readBusinessLinesFormXMLFile(String dir) throws Exception {
		DocumentBuilderFactory doBuilderFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder doBuilder = doBuilderFactory.newDocumentBuilder();
		String path = dir + "/platform/permission/" +BUSINESSLINES_CFG_FILE;
		File file= new File(path);
		Document doc =  doBuilder.parse(file);
		doc.getDocumentElement().normalize();
		HashMap ALLBLConfig = new HashMap();
		//取得所有有效的业务主线配置
		NodeList BLList = doc.getElementsByTagName(BUSINESSLINES);
		for(int i=0; i<BLList.getLength(); i++){
			Node BLNode = BLList.item(i);
			Element BLElement  = (Element)BLNode;
			String activity = BLElement.getAttribute("activity");
			if(activity != null && activity.trim().toLowerCase().equals("true")){//只加载有效的
			    String id = BLElement.getAttribute("id");
			    String name = BLElement.getAttribute("name");
			    String basepackage = BLElement.getAttribute("basepackage");
			    HashMap BLConfig = new HashMap();
			    BLConfig.put("id", id);
			    BLConfig.put("name", name);
			    BLConfig.put("basepackage", basepackage);
			    ALLBLConfig.put(id, BLConfig);
			    
			}
		}
		
		return ALLBLConfig;
	}
	
	static public void main(String[] arg) {
		XMLFileUtil x = new XMLFileUtil();
		try {
			HashMap v = (HashMap)x.readBusinessLinesFormXMLFile("E:/CMIS/project3/cmis-main/src/main/config/com/yucheng/cmis/config/");
			
			System.err.println(v.toString());
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}