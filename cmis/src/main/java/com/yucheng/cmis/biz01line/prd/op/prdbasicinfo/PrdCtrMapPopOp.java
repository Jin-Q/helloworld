package com.yucheng.cmis.biz01line.prd.op.prdbasicinfo;

import java.io.IOException;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.log.EMPLog;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 泉州银行产品配置映射处理类
 * @author Pansq
 * @create 2013-07-10
 */
public class PrdCtrMapPopOp extends CMISOperation {
	private static final String APPFORMDIC = "PRD_APP_DIC_TYPE";//构造申请表单字典项
	private static final String CONTFORMDIC = "PRD_CONT_DIC_TYPE";//构造合同表单字典项 
	@Override
	public String doExecute(Context context) throws EMPException {
		/**
		 * 处理逻辑：
		 * 1.构造选择的申请表单字典项
		 * 2.构造选择的合同表单字典项
		 * 3.获取系统变量字典项
		 * 4.对原表单映射进行处理显示
		 */
		try {
			/** 从表模型获取选择的申请表单、合同表单的表模型ID */
			String appForm = "";
			String contForm = "";
			String contmapping = "";
			appForm = (String)context.getDataValue("loanform");
			contForm = (String)context.getDataValue("contform");
			//contmapping = (String)context.getDataValue("contmapping");
			if(appForm == null || appForm == ""){
				throw new EMPException("获取申请表单信息失败！");
			}
			if(contForm == null || contForm == ""){
				throw new EMPException("获取合同表单信息失败！");
			}
			/** 加载表模型服务，获取表模型 */
			TableModelLoader modelLoader = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
			
			TableModel appFormModel = modelLoader.getTableModel(appForm);
			TableModel contFormModel = modelLoader.getTableModel(contForm);
			if(appFormModel == null){
				throw new EMPException("表模型"+appForm+"未配置！");
			}
			if(contFormModel == null){
				throw new EMPException("表模型"+contForm+"未配置！");
			}
			/** 构造申请表单字典项 */
			IndexedCollection appDicIColl = new IndexedCollection(APPFORMDIC); 
			this.addModelFiledDic(appForm, appDicIColl, context);
			/** 构造合同表单字典项 */
			IndexedCollection contDicIColl = new IndexedCollection(CONTFORMDIC);
			this.addModelFiledDic(contForm, contDicIColl, context);
			/** 获取系统变量字典项 */
			IndexedCollection iColSysVar = (IndexedCollection)context.getDataElement("dictColl.STD_ZB_SYS_VAR");
			/** 判断原合同映射是否有值，有值需要将值转换为select选项下的option显示出来 */
			KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("getPrdBasicinfoByPrdId4PrdBasicinfo", (String)context.getDataValue("prdid"), null, this.getConnection(context));
			contmapping = (String)kColl.getDataValue("contmapping");
			
			if(contmapping != null && contmapping.trim().length() != 0){
				IndexedCollection iColMap = this.changeMAPXML2Option(contmapping, appDicIColl, contDicIColl, iColSysVar);
				iColMap.setName("STD_ZB_MAP");
				this.putDataElement2Context(iColMap, context);
			}
			this.putDataElement2Context(appDicIColl, context);
			this.putDataElement2Context(contDicIColl, context);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * 通过表模型构建表模型字典项，包括主子表、主从表
	 * @param modelId 表模型
	 * @param iColDic 返回IndexedCollection
	 * @param context 上下文
	 * @throws Exception
	 */
	public void addModelFiledDic(String modelId,IndexedCollection dicIColl,Context context) throws Exception{
		TableModelLoader modelLoader = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
		TableModel tableModel = modelLoader.getTableModel(modelId);
		int fllowFlag = 1;//从表循环个数标识
		int sonFlag = 1;//子表循环个数标识
		if(tableModel == null ) {
			throw new EMPException("模型"+modelId+"未配置");
		}
		/**
		 * 遍历表模型下所有字段,存入dicIColl中，主表显示模式为【字段名+【主】】用于标识主子、主从表
		 */
		Iterator iterator = null;
		iterator = tableModel.getModelFields().keySet().iterator();
		while (iterator.hasNext()) {
			String fieldId = (String) iterator.next();
			TableModelField field = tableModel.getModelField(fieldId);
			KeyedCollection kFiled = new KeyedCollection();
			kFiled.addDataField("enname", fieldId);
			kFiled.addDataField("cnname",field.getCnname()+"[主]");
			dicIColl.addDataElement(kFiled);
		}
		/**
		 * 获取主表下从表的所有字段属性，放入dicIColl，从表显示模式为【字段名+【子】】用于标识主子、主从表
		 * 主子表支持多层循环嵌套，以递归方式解析
		 */
		iterator = tableModel.getOne2OneModels().keySet().iterator();
		while (iterator.hasNext()) {
			String fllowId = (String) iterator.next();
			/*One2OneRef oneRef = (One2OneRef)tableModel .getOne2OneRef(refId);
			TableModel refModel = modelLoader.getTableModel(oneRef.getRefModelId());*/
			TableModel fllowModel = modelLoader.getTableModel(fllowId);
			if(fllowModel == null ){//判断从表表模型是否存在
				throw new EMPException("表模型【"+tableModel.getCnname()+"="+modelId+"】的从表模型【"+fllowId+"】未配置！");
			}
			Iterator itFllow = fllowModel.getModelFields().keySet().iterator();
			while (itFllow.hasNext()) {
				String fllowFieldId = (String) itFllow.next();
				TableModelField field = fllowModel.getModelField(fllowFieldId);
				KeyedCollection kFiled = new KeyedCollection();
				kFiled.addDataField("enname", fllowId+"."+fllowFieldId);
				kFiled.addDataField("cnname", field.getCnname()+"[从"+fllowModel.getCnname()+fllowFlag+"]");
				dicIColl.addDataElement(kFiled);
			}
			fllowFlag ++ ;
		}
		
		/**
		 * 获取主表下子表的所有字段，放入dicIColl，子表显示模式为【字段名+【子】】用于标识主子、主从表
		 */
		iterator = tableModel.getOne2MultiModels().keySet().iterator();
		while(iterator.hasNext()){
			String sonId = (String)iterator.next();
			TableModel sonModel = modelLoader.getTableModel(sonId);
			if(sonModel == null){//子表表模型存在检查
				throw new EMPException("表模型【"+tableModel.getCnname()+"="+modelId+"】的子表模型【"+sonId+"】未配置！");
			}
			Iterator itSon = sonModel.getModelFields().keySet().iterator();
			while(itSon.hasNext()){
				String sonFiledId = (String)itSon.next();
				TableModelField field = sonModel.getModelField(sonFiledId);
				KeyedCollection kFiled = new KeyedCollection();
				kFiled.addDataField("enname", sonId+"."+sonFiledId);
				kFiled.addDataField("cnname", field.getCnname()+"[子"+sonFlag+"]");
				dicIColl.addDataElement(kFiled);
			}
			sonFlag++;
		}
		
	}
	
	/**
	   * 将表间映射XML转换成页面上select的option
	   * @param xml String
	   * @param coF Collection
	   * @param coT Collection
	   * @return Collection
	   */
	public IndexedCollection changeMAPXML2Option(String parserXML,IndexedCollection icoFromDic,IndexedCollection icoToDic,IndexedCollection iclSys) throws Exception {
		IndexedCollection iColReturn = new IndexedCollection();
		if(parserXML != null && !parserXML.trim().equals("")){
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, parserXML);
			parserXML = parserXML.replaceAll("~","\"");
			parserXML =parserXML.replaceAll("&lt;(.+?)&gt;", "<$1>");
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, parserXML);

			DOMParser parser = new DOMParser();
			java.io.StringReader reader = new java.io.StringReader(parserXML);
			try {
		        parser.parse(new InputSource(reader));
		        Document doc = parser.getDocument();
		        NodeList mList = doc.getElementsByTagName("m"); 
		        for(int i = 0; i < mList.getLength(); i++){
		        	String st_f = "";
		        	String st_t = "";
		        	if(mList.item(i).getAttributes().getNamedItem("f") != null){
		        		st_f = (String) mList.item(i).getAttributes().getNamedItem("f").
		        		getNodeValue();
		        	}
		            if (mList.item(i).getAttributes().getNamedItem("t") != null) {
		              st_t = (String) mList.item(i).getAttributes().getNamedItem("t").
		                  getNodeValue();
		            }
		            if(st_f != null && !st_f.trim().equals("") && st_t != null && !st_f.trim().equals("")){
		            	KeyedCollection kColTmp = new KeyedCollection();
		            	String st_f_name = null;
		            	if(st_f.indexOf("sys:") == 0){
		            		st_f_name = this.getCnname(st_f,iclSys); 
		            	}else{
		            		st_f_name = this.getCnname(st_f,icoFromDic);
		            	}
		            	String st_t_name = this.getCnname(st_t,icoToDic);
		            	kColTmp.addDataField("enname","<m t=~" + st_t + "~ f=~" + st_f + "~/>");
		            	kColTmp.addDataField("cnname",st_f_name + "=" + st_t_name);
		            	iColReturn.addDataElement(kColTmp);
		            }
		        }
			}catch(IOException ex) {
			} finally {
				reader.close();
			}
		}
		return iColReturn;
	}
	/**
	 * 获取指定的 enname 过滤 字典 iCol的中文名
	 * @param map 查找的 enname
	 * @param iCol 字典 iCol
	 * @return
	 * @throws Exception
	 */
	private String getCnname(String map,IndexedCollection iCol) throws Exception {
		if(iCol==null) return map;
	    String enname = null;
	    String cnname = null;
	    KeyedCollection kColTmp = null; 
	    for(Iterator it = iCol.iterator(); it.hasNext();) {
	    	kColTmp = (KeyedCollection)it.next();
	    	enname = (String)kColTmp.getDataValue("enname");
	    	if(map.trim().equals(enname.trim())) {
	    		cnname = (String)kColTmp.getDataValue("cnname");
	    		break;
	    	}
	    }
	    if(cnname==null || cnname.trim().equals("")) {
	    	cnname = map+"[未找到]";
	    }
	    return cnname;
	}
	
}
