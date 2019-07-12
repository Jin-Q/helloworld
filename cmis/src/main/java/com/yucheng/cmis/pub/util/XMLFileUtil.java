package com.yucheng.cmis.pub.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 *@Description：此类主要负责对XML文件的读写
 *@Lastmodified dec 7,2008 
 *@Author	    eirc
 */
public class XMLFileUtil{
	/*
	 * 存储所有需要解析的文件列表
	 */
	private static ArrayList<String> al = new ArrayList<String>();
	
	private String m_fileType = ".xml";		  //文件类型	
	
	/*
	 * 组件配置文件的xml标签
	 */
	public final String  	BUILDINGELEMENT ="buildingElement";
	public final String  	COMPONENT = "COMPONENT";
	public final String  	AGENT = "AGENT";
	public final String  	DAO = "DAO";
	public final String  	INTERFACE = "INTERFACE";
	public final String 	MODUALSERVICE = "MODUALSERVICE";
	public final String 	ID = "id";
	public final String 	DESCRIBE = "describe";
	public final String 	COMPROPERTY = "comproperty";
	public final String 	CLASSNAME = "classname";
	public final String 	MODUALNAME="modualName";
	
	/**
	 * 从组件配置文件中取出所有的组件信息，存放到一个MAP中,
	 * 能解析的文件结构
	 * <cmis>
	 * 		<dddd>
	 * 			<xxx></xxx>
	 * 			<xxx></xxx>
	 * 			<ddd></ddd>
	 * 		<\dddd>
	 * 		<dddd>...</ddd>
	 * </cmis>
	 * @param path 文件路径
	 * @return 组件信息 结构（）
	 * @throws Exception
	 */
	public Map readComponentFromXMLFile(String dir) throws Exception{
		
		this.searchFiles(dir,true);
		
		HashMap<String, HashMap<String, String>> infoMap = new HashMap<String, HashMap<String, String>>();
		
		//构件解析器
		DocumentBuilderFactory doBuilderFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder doBuilder = doBuilderFactory.newDocumentBuilder();
		
		for(int k=0; k<al.size(); k++){
			String path = al.get(k);
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "start parser component file :  path");
			File file= new File(path);
			Document doc =  doBuilder.parse(file);
			
			//取得根目录
			doc.getDocumentElement().normalize();
			
			//取得所有构件列表
			NodeList buildingList = doc.getElementsByTagName(BUILDINGELEMENT);
			Element ele = (Element)buildingList.item(0);
		
			for(int s=0; s<buildingList.getLength(); s++){
				//得到单个构件节点
				Node buildNode = buildingList.item(s);
				Element buildElement = (Element)buildNode;
				
				//得到这个构建下所有的component
				NodeList componentList = buildElement.getElementsByTagName(COMPONENT);
				
				/*
				 * 取出每个component节点信息，存入一个map
				 */
				for(int i=0; i<componentList.getLength(); i++){
					//创建单个component信息存储容器
					HashMap<String, String> componentMap = new HashMap<String, String>();
					
					Node componentNode = componentList.item(i);
					Element componentElement  = (Element)componentNode;
					String componentId = componentElement.getAttribute(ID);
					String componentDescribe = componentElement.getAttribute(DESCRIBE);
					String componentComproperty = componentElement.getAttribute(COMPROPERTY);
					
					componentMap.put(DESCRIBE, componentDescribe);
					componentMap.put(COMPROPERTY, componentComproperty);
					
					NodeList classnameList = componentElement.getChildNodes();
					String classname = ((Node)classnameList.item(0)).getNodeValue().trim();
					componentMap.put(CLASSNAME, classname);
					
					infoMap.put(componentId, componentMap);
					
				}
	
			}
		}
		
		return infoMap;
	}
	
	/**
	 * 从组件配置文件中取出所有的代理信息，存放到一个MAP中,
	 * 能解析的文件结构
	 * <cmis>
	 * 		<dddd>
	 * 			<xxx></xxx>
	 * 			<ddd></ddd>
	 * 		<\dddd>
	 * 		<dddd>...</ddd>
	 * </cmis>
	 * @param path 文件路径
	 * @return 组件信息 结构（）
	 * @throws Exception
	 */
	public Map readAgentFromXMLFile(String dir) throws Exception{
		
		this.searchFiles(dir,true);
		
		HashMap<String, HashMap<String, String>> infoMap = new HashMap<String, HashMap<String, String>>();
		
		//构件解析器
		DocumentBuilderFactory doBuilderFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder doBuilder = doBuilderFactory.newDocumentBuilder();
		
		for(int k=0; k<al.size(); k++){
			String path = al.get(k);
			
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "start parser agent file :  path");
			
			File file= new File(path);
			Document doc =  doBuilder.parse(file);
			
			//取得根目录
			doc.getDocumentElement().normalize();
			
			//取得所有构件列表
			NodeList buildingList = doc.getElementsByTagName(BUILDINGELEMENT);
			
			for(int s=0; s<buildingList.getLength(); s++){
				//得到单个构件节点
				Node buildNode = buildingList.item(s);
				Element buildElement = (Element)buildNode;
				
				//得到这个构件下所有的agent
				NodeList agentList = buildElement.getElementsByTagName(AGENT);
				/*
				 * 取出每个agent节点信息，存入一个map
				 */
				for(int i=0; i<agentList.getLength(); i++){
					//创建单个agent信息存储容器
					HashMap<String, String> agentMap = new HashMap<String, String>();
					
					Node agentNode = agentList.item(i);
					Element agentElement  = (Element)agentNode;
					String agentId = agentElement.getAttribute(ID);
					String agentDescribe = agentElement.getAttribute(DESCRIBE);
					String agentComproperty = agentElement.getAttribute(COMPROPERTY);
					
					agentMap.put(DESCRIBE, agentDescribe);
					agentMap.put(COMPROPERTY, agentComproperty);
					
					NodeList classnameList_agent = agentElement.getChildNodes();
					String classname_agent = ((Node)classnameList_agent.item(0)).getNodeValue().trim();
					agentMap.put(CLASSNAME, classname_agent);
					
					infoMap.put(agentId, agentMap);
				}
	
			}
		}
		
		return infoMap;
	}
	
	/**
	 * 从组件配置文件中取出所有的代理信息，存放到一个MAP中,
	 * 能解析的文件结构
	 * <cmis>
	 * 		<dddd>
	 * 			<xxx></xxx>
	 * 			<ddd></ddd>
	 * 		<\dddd>
	 * 		<dddd>...</ddd>
	 * </cmis>
	 * @param path 文件路径
	 * @return 组件信息 结构（）
	 * @throws Exception
	 */
	public Map readDaoFromXMLFile(String dir) throws Exception{
		
		this.searchFiles(dir,true);
		
		HashMap<String, HashMap<String, String>> infoMap = new HashMap<String, HashMap<String, String>>();
		
		//构件解析器
		DocumentBuilderFactory doBuilderFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder doBuilder = doBuilderFactory.newDocumentBuilder();
		
		for(int k=0; k<al.size(); k++){
			String path = al.get(k);
			
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "start parser dao file :  path");
			
			File file= new File(path);
			Document doc =  doBuilder.parse(file);
			
			//取得根目录
			doc.getDocumentElement().normalize();
			
			//取得所有构件列表
			NodeList buildingList = doc.getElementsByTagName(BUILDINGELEMENT);
			
			for(int s=0; s<buildingList.getLength(); s++){
				//得到单个构件节点
				Node buildNode = buildingList.item(s);
				Element buildElement = (Element)buildNode;
				
				//得到这个构件下所有的agent
				NodeList agentList = buildElement.getElementsByTagName(DAO);
				/*
				 * 取出每个agent节点信息，存入一个map
				 */
				for(int i=0; i<agentList.getLength(); i++){
					//创建单个agent信息存储容器
					HashMap<String, String> agentMap = new HashMap<String, String>();
					
					Node agentNode = agentList.item(i);
					Element agentElement  = (Element)agentNode;
					String agentId = agentElement.getAttribute(ID);
					String agentDescribe = agentElement.getAttribute(DESCRIBE);
					String agentComproperty = agentElement.getAttribute(COMPROPERTY);
					
					agentMap.put(DESCRIBE, agentDescribe);
					agentMap.put(COMPROPERTY, agentComproperty);
					
					NodeList classnameList_agent = agentElement.getChildNodes();
					String classname_agent = ((Node)classnameList_agent.item(0)).getNodeValue().trim();
					agentMap.put(CLASSNAME, classname_agent);
					
					infoMap.put(agentId, agentMap);
				}
	
			}
		}
		
		return infoMap;
	}	
	
	/**
	 * 解析某个目录下所有文件得到需要的文件的绝对路径
	 * <p>
	 * 	注：如果已经有配置文件被加载过了，则不会再加载。
	 * 
	 * @param dir 文件路径  如： c:\work
	 * @param reLoad true-强制重新加载，false-不重复加载
	 * @return 文件路径集合
	 * @throws Exception
	 */
    private ArrayList searchFiles(String dir,boolean reLoad) throws Exception {
        //说明加载过了，不需要重复加载
    	if(al.size()>0 && !reLoad)
        	return al;
		
    	al.clear();
    	
    	doSearchFiles(dir);
    	
        return al;
        
    }

   /**
    * 解析某个目录下所有文件得到需要的文件的绝对路径
    * <p>
    * 	注：总是会解析该目录下的配置文件路径
    * 
    * @param dir 文件路径  如： c:\work
    * @return 文件路径集合
    */
    private void doSearchFiles(String dir){
    	ArrayList<String> retList = new ArrayList<String>();
    	
    	File root = new File(dir);
		//得到该文件夹下的所有类型文件名称
		File[] filesOrDirs = root.listFiles();
   
		for (int i = 0; i < filesOrDirs.length; i++){
			/*
			* 判断该文件是否是文件夹，是则继续读取，不是则读取该目录
			*/
			 if (filesOrDirs[i].isDirectory()){
			    	
				 doSearchFiles(filesOrDirs[i].getAbsolutePath());
			        
			  } else {
				  
			    //得到文件名
			    String fileName = filesOrDirs[i].getName();
			    
			    /*
			    * 匹配文件名，看看该文件是否是需要找的文件
			    */
				//boolean IsValidfileEnd   = m_fileType.equals(fileName.substring(fileName.length()-4, fileName.length()));
			    boolean IsValidfileEnd = fileName.endsWith(m_fileType);
				    	
				//完成匹配的把该文件路径存起来
				if(IsValidfileEnd){      
					al.add(filesOrDirs[i].getAbsolutePath()) ;  
				}	
			 }
		}
		
    }
    
    
	public Map readInterfaceFromXMLFile(String dir) throws Exception{
		
		this.searchFiles(dir,true);
		
		HashMap<String, HashMap<String, String>> infoMap = new HashMap<String, HashMap<String, String>>();
		
		//构件解析器
		DocumentBuilderFactory doBuilderFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder doBuilder = doBuilderFactory.newDocumentBuilder();
		
		for(int k=0; k<al.size(); k++){
			String path = al.get(k);
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "start parser interface file :  path");
			File file= new File(path);
			Document doc =  doBuilder.parse(file);
			
			//取得根目录
			doc.getDocumentElement().normalize();
			
			//取得所有构件列表
			NodeList buildingList = doc.getElementsByTagName(BUILDINGELEMENT);
		
			for(int s=0; s<buildingList.getLength(); s++){
				//得到单个构件节点
				Node buildNode = buildingList.item(s);
				Element buildElement = (Element)buildNode;
				
				//得到这个构建下所有的interface
				NodeList interfaceList = buildElement.getElementsByTagName(INTERFACE);
				
				/*
				 * 取出每个interface节点信息，存入一个map
				 */
				for(int i=0; i<interfaceList.getLength(); i++){
					//创建单个interface信息存储容器
					HashMap<String, String> interfaceMap = new HashMap<String, String>();
					
					Node interfaceNode = interfaceList.item(i);
					Element interfaceElement  = (Element)interfaceNode;
					String interfaceId = interfaceElement.getAttribute(ID);
					String interfaceDescribe = interfaceElement.getAttribute(DESCRIBE);
					String interfaceComproperty = interfaceElement.getAttribute(COMPROPERTY);
					
					interfaceMap.put(DESCRIBE, interfaceDescribe);
					interfaceMap.put(COMPROPERTY, interfaceComproperty);
					
					NodeList classnameList = interfaceElement.getChildNodes();
					String classname = ((Node)classnameList.item(0)).getNodeValue().trim();
					interfaceMap.put(CLASSNAME, classname);
					
					infoMap.put(interfaceId, interfaceMap);
					
				}
	
			}
		}
		
		return infoMap;
	}    
	
	/**
	 * <p>
	 * 	解析xml文件中的MODUALSERVICE配置文件
	 * </p>
	 * 
	 * 
	 * @author yuhq
	 * @since 3.9
	 * @param dir　配置文件目录
	 * @return　Map
	 * @throws Exception
	 */
	public Map readModualServiceFromXMLFile(String dir) throws Exception{
		
		this.searchFiles(dir,true);
		
		HashMap<String, HashMap<String, String>> infoMap = new HashMap<String, HashMap<String, String>>();
		
		//构件解析器
		DocumentBuilderFactory doBuilderFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder doBuilder = doBuilderFactory.newDocumentBuilder();
		
		for(int k=0; k<al.size(); k++){
			String path = al.get(k);
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "start parser modual service file :  "+path);
			File file= new File(path);
			Document doc =  doBuilder.parse(file);
			
			//取得根目录
			doc.getDocumentElement().normalize();
			
			//取得所有构件列表
			NodeList buildingList = doc.getElementsByTagName(BUILDINGELEMENT);
			Element ele = (Element)buildingList.item(0);

			String modualName = null;//模块名称
			if(ele!=null){
				 modualName = ele.getAttribute("modualName");
			}
			
			for(int s=0; s<buildingList.getLength(); s++){
				//得到单个构件节点
				Node buildNode = buildingList.item(s);
				Element buildElement = (Element)buildNode;
				
				//得到这个构建下所有的modualService
				NodeList modualServiceList = buildElement.getElementsByTagName(MODUALSERVICE);
				/*
				 * 取出每个modualService节点信息，存入一个map
				 */
				for(int i=0; i<modualServiceList.getLength(); i++){
					//创建单个modualService信息存储容器
					HashMap<String, String> serviceModualMap = new HashMap<String, String>();
					
					Node modualServiceNode = modualServiceList.item(i);
					Element modualServiceElement  = (Element)modualServiceNode;
					String serviceId = modualServiceElement.getAttribute(ID);
					String serviceDescribe = modualServiceElement.getAttribute(DESCRIBE);
					String serviceComproperty = modualServiceElement.getAttribute(COMPROPERTY);
					
					serviceModualMap.put(MODUALNAME, modualName);
					serviceModualMap.put(DESCRIBE, serviceDescribe);
					serviceModualMap.put(COMPROPERTY, serviceComproperty);
					
					NodeList classnameList = modualServiceElement.getChildNodes();
					String classname = ((Node)classnameList.item(0)).getNodeValue().trim();
					serviceModualMap.put(CLASSNAME, classname);
					
					infoMap.put(serviceId, serviceModualMap);
				}
	
			}
		}
		
		return infoMap;
	}    
	
	/**
	 * 加载各模块的初始化配置文件
	 * 
	 * @param dir 配置文件的根目路
	 * @return 初始化类的集合key:类名，value：模块名
	 */
	public Map<String, String> loadModualInitializerCfgFiles(String dir) throws Exception{
		Map<String,String> retMap = new HashMap<String,String>();
		if(dir == null || dir.equals("")) return retMap;
		
		//配置文件的绝对路径集合
		List<String> fileList = this.searchFiles(dir,true);
		
		//构件解析器
		DocumentBuilderFactory doBuilderFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder doBuilder = doBuilderFactory.newDocumentBuilder();
		
		for (int i = 0; i < fileList.size(); i++) {
			String path = fileList.get(i);

			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "start parser interface file :  "+path);
			File file= new File(path);
			Document doc =  doBuilder.parse(file);
			
			//取得根目录
			doc.getDocumentElement().normalize();
			
			//取得所有构件列表
			NodeList buildingList = doc.getElementsByTagName(BUILDINGELEMENT);
			Element ele = (Element)buildingList.item(0);
			String modualName = ele.getAttribute("modualName");//模块名称
			
			for(int s=0; s<buildingList.getLength(); s++){
				//得到单个构件节点
				Node buildNode = buildingList.item(s);
				Element buildElement = (Element)buildNode;
				
				//得到这个构建下所有的initializer
				NodeList initializerList = buildElement.getElementsByTagName("initializer");
				/*
				 * 取出每个modualService节点信息，存入一个map
				 */
				for(int k=0; k<initializerList.getLength(); k++){
					Node initializerNode = initializerList.item(k);
					Element initializerElement  = (Element)initializerNode;
					//得到配置的class
					String className = initializerElement.getAttribute("class");
					retMap.put(className, modualName);
				}
			}
		}
		return retMap;
	}
	
    
}