package com.ecc.emp.component.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import java.io.File;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ecc.emp.component.ComponentNotDefinedException;
import com.ecc.emp.component.xml.ServletContextParser;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.jmx.support.EMPMBeanExportor;
import com.ecc.emp.log.EMPLog;

/**
 * MVC模型实例化工厂
 * 该工厂对象从MVC模型配置文件实例化MVC模型，用来描述Jsp页面跳转逻辑和业务逻辑处理之间的关联关系。
 * 
 * 修改：使加载action文件时支持多级子目录，新增方法loadAllActions()
 * modify by yuhq at 2013年05月16日 星期四 13时35分28秒
 * 
 * MVC模型配置文件实例：
 * <pre>
 * &lt;action id="addAccount" type="normal" checkSession="false">
 *		&lt;jspView id="addAccount.jsp" url="addAccount.jsp"/>
 *		&lt;refFlow flowId="accountManager" op="addAccount">
 *			&lt;transition dest="showCustomerInfo.jsp" condition="$retValue='success'"/>
 *			&lt;transition dest="addAccount.jsp" condition="$retValue='failed'"/>
 *		&lt;/refFlow>
 *	&lt;/action>
 *	</pre>
 * Copyright (c) 2000, 2006 e-Channels Corporation
 * @author zhongmc
 * @version 1.0.0
 * @since 2006-11-3
 * @lastmodified 2006-11-3
 */
public class ServletContextFactory extends ComponentFactory {

	/**
	 * 用于保存已实例化的MVC Context, Portlet 使用
	 */
	private static Map contexts = new HashMap();
	
	/**
	 * 定义独立的action配置文件缺省目录和cache对象
	 */
//	final private String ACTION_DEFINE_DIRC_NAME="actions";
	
	private String actionSubDir = "actions";
	/**
	 * 独立定义的MVC配置文档对象集合
	 */
	private ArrayList actionDocs;

//	final private String JMX_RMI_CLIENT_MANAGER_ID="jmxRMIClientManager";
	/*pengjizhou finished*/

	
	/**
	 * 初始化方法
	 * 设置专用解析器对象：ServletContextParser。
	 * @see com.ecc.emp.component.xml.ServletContextParser类
	 */
	public ServletContextFactory() {
		super();
		
		ServletContextParser parser = new ServletContextParser();
		parser.setComponentFactory( this );
		this.setComponentParser( parser );
	}

	
	/**
	 * 增加一个ServletContext对象定义
	 * @param ctxName
	 * @param ctx
	 */	
	static public void addContext(String ctxName, Object ctx )
	{
		contexts.put( ctxName, ctx );
	}
	
	/**
	 * 得到一个指定名称ServletContext对象
	 * @param ctxName
	 * @return
	 */
	static public Object getContext(String ctxName )
	{
		return contexts.get( ctxName );
	}
	
	/**
	 * 除去一个指定名称的ServletContext对象
	 * @param ctxName
	 */
	static public void removeContext(String ctxName )
	{
		contexts.remove( ctxName );
	}
	
	/**
	 * 将目录下的action定义文件对象存储到actionDocs集合中
	 * @author pengjizhou
	 * @param doc
	 */
	private void addActionDocument(Document doc){
		if(actionDocs==null)
			actionDocs=new ArrayList();
		actionDocs.add(doc);
	}
	
	  /**
	    * 从外部文件实例化新的组件工厂.
	    * @param name
	    * @param fileName
	    * @return com.ecc.emp.component.factory.ComponentFactory
	    */
	   public ComponentFactory initializeComponentFactory(String name, String fileName) 
	   {
		   this.setFileName(fileName);
		   this.setName(name); 
		  //实例化公共ServletContext配置文件信息
		   try{
			   document = loadXMLDocument( fileName );
			   ComponentFactory.addComponentFactory( name, this );
		   }catch(Exception e)
		   {
			   EMPLog.log( EMPConstance.EMP_COMP_FACTORY, EMPLog.ERROR, 0, "ComponentFactory [" + name + "]failed to initialize from [" + fileName + "]!", e);
		   }
		   //实例化每个MVC Controller信息，通过对目录内所有文件进行实例化获得每个ACTION CONTROLLER的定义
		   int index=fileName.lastIndexOf("/");
		   String actionDirName=fileName.substring(0,index+1)+ actionSubDir; //this.ACTION_DEFINE_DIRC_NAME;
		   
		   this.loadAllActions(name, actionDirName);
//		   File dir=new File(actionDirName);
//		   if(dir.exists()&&dir.isDirectory())
//		   {
//			   String[] files=dir.list();
//			   if(files!=null&&files.length>0 )
//			   {
//				   for(int i=0;i<files.length;i++)
//				   {
//					   String actionFileName=actionDirName+"/"+files[i];
//					   try{
//						   Document tempDocument = loadXMLDocument( actionFileName );
//						   this.addActionDocument(tempDocument);
//					   }catch(Exception e)
//					   {
//						   EMPLog.log( EMPConstance.EMP_COMP_FACTORY, EMPLog.ERROR, 0, "ComponentFactory [" + name + "]failed to initialize from [" + fileName + "]!", e);
//					   }
//				   }
//			   }
//		   }
		   return this;
	   }
	   
	/**
	 * 加载系统中所有Action文件，且支持多级子目录
	 * 
	 * @param path
	 *            Action文件目录
	 * @author yuhq
	 * @time 2013年05月16日 星期四 13时35分28秒
	 */
	private void loadAllActions(String ComponentFactoryName,String path) {
		File file = new File(path);
		if (!file.isDirectory())
			return;
		File[] fileList = file.listFiles();

		for (int i = 0; i < fileList.length; i++) {
			File tmpFile = fileList[i];
			if(tmpFile.isDirectory())
				loadAllActions(ComponentFactoryName,tmpFile.getPath());
			else{
				try {
					Document tempDocument = loadXMLDocument(tmpFile.getPath());
					this.addActionDocument(tempDocument);
				} catch (Exception e) {
					 EMPLog.log( EMPConstance.EMP_COMP_FACTORY, EMPLog.ERROR, 0, "ComponentFactory [" + ComponentFactoryName + "]failed to initialize from [" + tmpFile.getName() + "]!", e);
				}
				
			}
				
		}

	}
	   
		/**
		 * 根据xml文件对象实例化组件对象
		 * @param bean
		 * @param doc
		 * @throws Exception
		 */
		private void parseTheComponent(Object bean,Document doc) throws Exception
		{
			Element element = doc.getDocumentElement();
			   NodeList nodeList = element.getChildNodes();
			   
			   for( int i=0; i<nodeList.getLength(); i++ )
			   {
				   Node node = nodeList.item( i );
				   if( "classMap".equals( node.getNodeName() ))
					   continue;
				   if( "mbeanExportor".equals( node.getNodeName()))
					   continue;
				   
				   if( node.getNodeType() == Node.ELEMENT_NODE )
				   {
					   Object aBean = this.getComponentParser().parseTheElement(doc, node );
					   
					   if( aBean != null )
					   {
						   this.getComponentParser().addComponentToBean(bean, aBean, node.getNodeName(), this.getNodeAttributeValue("name", node));
						   
						   String id = getNodeAttributeValue("id", node );
						   if( id != null && !"false".equals(getNodeAttributeValue("singleton", node ) ))	//如果是唯一部署方式，则添加到Cache中
								   componentCache.put(id, aBean); 
						   
					   }
				   }
			   }
		}

	/*pengjizhou finished*/

	/**
	 * 将XML定义中的所有内容解析到bean中，Bean为EMPRequestServlet
	 * @param bean
	 */
	public void parseTheContext(Object bean)throws Exception
	{
//		if( this.document == null )
//			return;
//		
//		   Element element = document.getDocumentElement();
//		   NodeList nodeList = element.getChildNodes();
//		   
//		   for( int i=0; i<nodeList.getLength(); i++ )
//		   {
//			   Node node = nodeList.item( i );
//			   if( node.getNodeType() == Node.ELEMENT_NODE )
//			   {
//				   Object aBean = this.getComponentParser().parseTheElement(document, node );
//				   this.getComponentParser().addComponentToBean(bean, aBean, node.getNodeName());
//			   }
//		   }
		/*
		 * pengjizhou modify
		 * 修改该parser方法，增加了独立action定义对象的parser处理
		 */
		if( this.document == null )
			return;
		
		if( this.findElementNode(document, "mbeanExportor", MBEAN_EXPORTOR) != null )
		{
		
			mbeanExportor = (EMPMBeanExportor)this.getComponent( MBEAN_EXPORTOR);
		}
		

		parseTheComponent(bean,this.document);
		   
		//实例化每一个action定义对象
		if(actionDocs!=null&&actionDocs.size()>0){
			for(int i=0;i<actionDocs.size();i++){
				Document tempDoc=(Document)actionDocs.get(i);
				if(tempDoc!=null)
					parseTheComponent(bean,tempDoc);
			}
		}
		/*pengjizhou finished*/
	}
	
	
	
	   /**
	    * 从组件工厂取得一个组件定义实例
	    * @param name
	    * @return java.lang.Object
	    * @throws com.ecc.emp.component.ComponentNotDefinedException
	    */
	   public Object getComponent(Document document, String name ) throws ComponentNotDefinedException, Exception 
	   {
		  
		   if( document == this.document )		//common document define
			   return super.getComponent( name );
		   
	   
		   Node aNode = this.findElementNode( document, name );
		   
		   if( aNode == null )
		   {
			   return super.getComponent( name );	//try to find it from common file define
		   }
		   
		   
		   try{
			   if( componentParser != null )
			   {
				   Object bean =  componentParser.parseTheElement( document, aNode );
				   return bean;
			   }
		   }catch(Exception e)
		   {
			   throw e;
		   }
		   return null;
	   }
	   
	   /**
	    * 从XML Document 中查找指定ID的节点(ElementNode)
	    * 首先从指定的Document中查找，如果找不到，则从公公的Document中查找
	    * @param doc
	    * @param id
	    * @return org.w3c.dom.Node
	    */
	   public Node findElementNode(Document doc, String id) 
	   {
		   Element element = doc.getDocumentElement();
		   NodeList nodeList = element.getChildNodes();
		   
		   for( int i=0; i<nodeList.getLength(); i++ )
		   {
			   Node node = nodeList.item( i );
			   if( node.getNodeType() == Node.ELEMENT_NODE )
			   {
				   if( id.equals( getNodeAttributeValue("id", node )))
					   return node;
			   }
		   }
		   
		   if( doc != this.document && this.document != null )
		   {

			   element = this.document.getDocumentElement();
			   nodeList = element.getChildNodes();
			   
			   for( int i=0; i<nodeList.getLength(); i++ )
			   {
				   Node node = nodeList.item( i );
				   if( node.getNodeType() == Node.ELEMENT_NODE )
				   {
					   if( id.equals( getNodeAttributeValue("id", node )))
						   return node;
				   }
			   }
		   }
	    return null;
	   }


	public String getActionSubDir() {
		return actionSubDir;
	}


	public void setActionSubDir(String actionSubDir) {
		this.actionSubDir = actionSubDir;
	}
	   
	   
	
}
