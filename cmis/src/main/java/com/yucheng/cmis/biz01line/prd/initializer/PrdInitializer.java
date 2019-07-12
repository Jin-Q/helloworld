package com.yucheng.cmis.biz01line.prd.initializer;

import java.io.File;
import java.sql.Connection;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.BusinessInitializer;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.dic.CMISDataDicService;
/**
 * 产品配置模块在系统加载时所需初始化的操作类
 * @author Pansq
 * @version V1.0
 */
public class PrdInitializer implements BusinessInitializer {
	public static boolean isInit = false;
	public static String ATTR_ENNAME = "enname";
	public static String ATTR_CNNAME = "cnname";
	
	public void initialize(Context rootCtx, Connection connection)
			throws Exception {
		//泉州银行产品树形数据导入
		/*CMISProductTreeDicService treePrdService = (CMISProductTreeDicService)rootCtx.getService(PRDConstant.ATTR_TREEPRDSERVICE);
		if(treePrdService != null){
			treePrdService.loadDicData(rootCtx, connection);
			EMPLog.log(EMPConstance.EMP_CORE, EMPLog.INFO, 0, "Initial the PrdInitializer success!");
		}else{
			EMPLog.log(EMPConstance.EMP_CORE, EMPLog.WARNING, 0, "There is no PrdInitializer in rootContext!");
		}*/
		/** 虚拟化流程字典导入 */
		KeyedCollection dictColl = null;
		try {
			dictColl = (KeyedCollection)rootCtx.getDataElement(CMISConstance.ATTR_DICTDATANAME);
		} catch (ObjectNotFoundException e) {
			dictColl = new KeyedCollection();
			dictColl.setName(CMISConstance.ATTR_DICTDATANAME);
			rootCtx.addDataElement(dictColl);
		}
		/**
		 * 泉州银行流程生成字典项加载机制,包括流程字典项、流程节点字典项
		 * @author pansq
		 */
		/** 获取流程文件配置路径，如果存在studio文件，并且配置了流程路径，则取流程路径，否则在cmis中配置也可以 */
		String flowXmlPath = "";
		ResourceBundle resStudion = ResourceBundle.getBundle("echain");
		if(resStudion == null){
			ResourceBundle res = ResourceBundle.getBundle("cmis");
			flowXmlPath  = res.getString(PRDConstant.CMISSTUDIOPATH);
			EMPLog.log(EMPConstance.EMP_CORE, EMPLog.INFO, 0, "------------PrdInitializer.java获取流程xml文件路径为cmis.properties中路径-----------");
		}else {
			flowXmlPath = resStudion.getString(PRDConstant.ECHAINSTUDIOPATH);
			EMPLog.log(EMPConstance.EMP_CORE, EMPLog.INFO, 0, "------------PrdInitializer.java获取流程xml文件路径为echain.properties中路径-----------");
		}
		flowXmlPath += "processes/issue/" ; 
		
		String flowDicNameRule = PRDConstant.FLOW_TYPE;
		File file = new File(flowXmlPath);
		if(!file.isDirectory()) {
			throw new Exception("获取流程配置xml文件路径"+flowXmlPath+"获取错误！");
		}
		String[] files = file.list();
		/*构件流程节点字典项,命名规则：FLOW_TYPE*/
		for(int i=0;i<files.length;i++){
			if(files[i].endsWith(".xml")){
				DocumentBuilderFactory doBuilderFactory= DocumentBuilderFactory.newInstance();
				DocumentBuilder doBuilder = doBuilderFactory.newDocumentBuilder();
				Document doc =  doBuilder.parse(flowXmlPath+files[i]);
				
				NodeList nodeFlowList = doc.getElementsByTagName("wf");
				for(int j=0;j<nodeFlowList.getLength();j++){
					Node node = nodeFlowList.item(j);
					//获取流程状态判断当前xml流程是否有效，状态为A则表示流程有效
					String status = node.getAttributes().getNamedItem("status").getNodeValue();
					if("A".equals(status)){
						String nodeId = node.getAttributes().getNamedItem("id").getNodeValue();
						String nodeName = node.getAttributes().getNamedItem("name").getNodeValue();
						
						//取得具体的字典列表(若没有，则新建出一个类型)
						IndexedCollection iColl = null;
						if(dictColl.containsKey(flowDicNameRule)){
							iColl = (IndexedCollection)dictColl.getDataElement(flowDicNameRule);
						}else{
							iColl = new IndexedCollection();
							iColl.setName(flowDicNameRule);
							dictColl.addDataElement(iColl);
							KeyedCollection kColl = new KeyedCollection();
							kColl.addDataField(CMISDataDicService.ATTR_ENNAME, null);
							kColl.addDataField(CMISDataDicService.ATTR_CNNAME, null);
							iColl.setDataElement(kColl);
						}
					
						KeyedCollection kColl = (KeyedCollection)iColl.getDataElement().clone();
						kColl.setName(nodeId);
						kColl.setDataValue(ATTR_ENNAME, nodeId);
						kColl.setDataValue(ATTR_CNNAME, nodeName);
						iColl.addDataElement(kColl);
					}
				}
			}
		}
		EMPLog.log(EMPConstance.EMP_CORE, EMPLog.INFO, 0, "------------PrdInitializer.java开始加载流程字典项FLOW_TYPE-----------");
		/*构件流程节点字典项,命名规则：流程名_FLOWNODE_TYPE*/
		for(int i=0;i<files.length;i++){
			if(files[i].endsWith(".xml")){
				DocumentBuilderFactory doBuilderFactory= DocumentBuilderFactory.newInstance();
				DocumentBuilder doBuilder = doBuilderFactory.newDocumentBuilder();
				Document doc =  doBuilder.parse(flowXmlPath+files[i]);
				
				NodeList nodeList = doc.getElementsByTagName("node");
				String fileName = files[i].substring(0, files[i].indexOf(".xml"));
				//对获得的文件节点进行处理
				for(int j=0;j<nodeList.getLength();j++){
					Node node = nodeList.item(j);
					String nodeId = node.getAttributes().getNamedItem("id").getNodeValue();
					String nodeName = node.getAttributes().getNamedItem("name").getNodeValue();
					String nodeType = node.getAttributes().getNamedItem("type").getNodeValue();
					if(nodeType.equals("A")){
						//构件字典项type
						flowDicNameRule = fileName+"_FLOWNODE_TYPE";
						
						//取得具体的字典列表(若没有，则新建出一个类型)
						IndexedCollection iColl = null;
						if(dictColl.containsKey(flowDicNameRule)){
							iColl = (IndexedCollection)dictColl.getDataElement(flowDicNameRule);
						}else{
							iColl = new IndexedCollection();
							iColl.setName(flowDicNameRule);
							dictColl.addDataElement(iColl);
							KeyedCollection kColl = new KeyedCollection();
							kColl.addDataField(CMISDataDicService.ATTR_ENNAME, null);
							kColl.addDataField(CMISDataDicService.ATTR_CNNAME, null);
							iColl.setDataElement(kColl);
						}
					
						KeyedCollection kColl = (KeyedCollection)iColl.getDataElement().clone();
						kColl.setName(nodeId);
						kColl.setDataValue(ATTR_ENNAME, nodeId);
						kColl.setDataValue(ATTR_CNNAME, nodeName);
						iColl.addDataElement(kColl);
					}
				}
			}
		}
		EMPLog.log(EMPConstance.EMP_CORE, EMPLog.INFO, 0, "------------PrdInitializer.java加载流程节点字典项成功，命名规则为：流程ID_FLOW_TYPE------------");
		isInit = true;
	}

}
