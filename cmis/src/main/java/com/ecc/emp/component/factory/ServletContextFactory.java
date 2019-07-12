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
 * MVCģ��ʵ��������
 * �ù��������MVCģ�������ļ�ʵ����MVCģ�ͣ���������Jspҳ����ת�߼���ҵ���߼�����֮��Ĺ�����ϵ��
 * 
 * �޸ģ�ʹ����action�ļ�ʱ֧�ֶ༶��Ŀ¼����������loadAllActions()
 * modify by yuhq at 2013��05��16�� ������ 13ʱ35��28��
 * 
 * MVCģ�������ļ�ʵ����
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
	 * ���ڱ�����ʵ������MVC Context, Portlet ʹ��
	 */
	private static Map contexts = new HashMap();
	
	/**
	 * ���������action�����ļ�ȱʡĿ¼��cache����
	 */
//	final private String ACTION_DEFINE_DIRC_NAME="actions";
	
	private String actionSubDir = "actions";
	/**
	 * ���������MVC�����ĵ����󼯺�
	 */
	private ArrayList actionDocs;

//	final private String JMX_RMI_CLIENT_MANAGER_ID="jmxRMIClientManager";
	/*pengjizhou finished*/

	
	/**
	 * ��ʼ������
	 * ����ר�ý���������ServletContextParser��
	 * @see com.ecc.emp.component.xml.ServletContextParser��
	 */
	public ServletContextFactory() {
		super();
		
		ServletContextParser parser = new ServletContextParser();
		parser.setComponentFactory( this );
		this.setComponentParser( parser );
	}

	
	/**
	 * ����һ��ServletContext������
	 * @param ctxName
	 * @param ctx
	 */	
	static public void addContext(String ctxName, Object ctx )
	{
		contexts.put( ctxName, ctx );
	}
	
	/**
	 * �õ�һ��ָ������ServletContext����
	 * @param ctxName
	 * @return
	 */
	static public Object getContext(String ctxName )
	{
		return contexts.get( ctxName );
	}
	
	/**
	 * ��ȥһ��ָ�����Ƶ�ServletContext����
	 * @param ctxName
	 */
	static public void removeContext(String ctxName )
	{
		contexts.remove( ctxName );
	}
	
	/**
	 * ��Ŀ¼�µ�action�����ļ�����洢��actionDocs������
	 * @author pengjizhou
	 * @param doc
	 */
	private void addActionDocument(Document doc){
		if(actionDocs==null)
			actionDocs=new ArrayList();
		actionDocs.add(doc);
	}
	
	  /**
	    * ���ⲿ�ļ�ʵ�����µ��������.
	    * @param name
	    * @param fileName
	    * @return com.ecc.emp.component.factory.ComponentFactory
	    */
	   public ComponentFactory initializeComponentFactory(String name, String fileName) 
	   {
		   this.setFileName(fileName);
		   this.setName(name); 
		  //ʵ��������ServletContext�����ļ���Ϣ
		   try{
			   document = loadXMLDocument( fileName );
			   ComponentFactory.addComponentFactory( name, this );
		   }catch(Exception e)
		   {
			   EMPLog.log( EMPConstance.EMP_COMP_FACTORY, EMPLog.ERROR, 0, "ComponentFactory [" + name + "]failed to initialize from [" + fileName + "]!", e);
		   }
		   //ʵ����ÿ��MVC Controller��Ϣ��ͨ����Ŀ¼�������ļ�����ʵ�������ÿ��ACTION CONTROLLER�Ķ���
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
	 * ����ϵͳ������Action�ļ�����֧�ֶ༶��Ŀ¼
	 * 
	 * @param path
	 *            Action�ļ�Ŀ¼
	 * @author yuhq
	 * @time 2013��05��16�� ������ 13ʱ35��28��
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
		 * ����xml�ļ�����ʵ�����������
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
						   if( id != null && !"false".equals(getNodeAttributeValue("singleton", node ) ))	//�����Ψһ����ʽ������ӵ�Cache��
								   componentCache.put(id, aBean); 
						   
					   }
				   }
			   }
		}

	/*pengjizhou finished*/

	/**
	 * ��XML�����е��������ݽ�����bean�У�BeanΪEMPRequestServlet
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
		 * �޸ĸ�parser�����������˶���action��������parser����
		 */
		if( this.document == null )
			return;
		
		if( this.findElementNode(document, "mbeanExportor", MBEAN_EXPORTOR) != null )
		{
		
			mbeanExportor = (EMPMBeanExportor)this.getComponent( MBEAN_EXPORTOR);
		}
		

		parseTheComponent(bean,this.document);
		   
		//ʵ����ÿһ��action�������
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
	    * ���������ȡ��һ���������ʵ��
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
	    * ��XML Document �в���ָ��ID�Ľڵ�(ElementNode)
	    * ���ȴ�ָ����Document�в��ң�����Ҳ�������ӹ�����Document�в���
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
