package com.yucheng.cmis.biz01line.prd.prdtools.tree;

import java.sql.Connection;

import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

/**
 * 通过客户所属业务条线，查询业务条线下产品目录信息
 * 以及产品目录下的产品信息，封装成json返回
 * 实时查询数据库读取信息
 * @author Pansq
 */
public class CMISShowPrdTreeDetails extends CMISOperation {
	private static final String CATALOGTABLENAME="prd_catalog";//产品目录表
	private static final String PRDTABLENAME="prd_basicinfo";//产品信息表
	@Override
	public String doExecute(Context context) throws EMPException {
		String parentNodeId = null;
		Connection connection = null;
		IndexedCollection iColl = null;
		String jsonStr = null;
		String bizline = "";
		connection = this.getConnection(context);
		try{
			bizline = (String) context.getDataValue("bizline");
			parentNodeId = (String) context.getDataValue("parentNodeId");
		}catch(Exception e){
		}
		PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
		.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
		/** 初始加载遍历业务线下所有产品目录，以及目录下挂接的产品 */
		IndexedCollection allIColl = null;
		try {
			if(parentNodeId == null || parentNodeId == ""){
				iColl = ppsc.getCatalogICollByParentId(CATALOGTABLENAME, parentNodeId);
				/** 获取跟节点和一级节点信息，包含产品信息 */
				jsonStr = this.getRootAndFristNode(connection, iColl, ppsc,bizline);
			}else {
				allIColl = ppsc.getNextCatalogByBizline(bizline,parentNodeId);
				
				/** 通过传递的节点ID获得当前节点下一节点信息 */
				jsonStr = this.getNextTreeJson(bizline, allIColl, ppsc);
			}    
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			context.addDataField(CMISConstance.ATTR_RET_JSONDATANAME, jsonStr);
		} catch (DuplicatedDataNameException e) {
			context.setDataValue(CMISConstance.ATTR_RET_JSONDATANAME, jsonStr);
		}
		return "0";
	}
	/**
	 * 将返回的IndexedCollection解析成需要返回的格式的JSON
	 * @param nodeIColl
	 * @param ppsc
	 * @return
	 * @throws Exception
	 */
	public String getNextTreeJson(String bizline, IndexedCollection nodeIColl, PrdPolcySchemeComponent ppsc) throws Exception {
		StringBuffer buff = new StringBuffer();
		buff.append("[");
		for(int j=0;j<nodeIColl.size();j++){
			KeyedCollection nodeKColl = (KeyedCollection)nodeIColl.get(j);
			String nodeid = (String)nodeKColl.getDataValue("catalogid");
			String nodename = (String)nodeKColl.getDataValue("catalogname");
			String nodeilevel = (String)nodeKColl.getDataValue("cataloglevel");
			buff.append("{id:\"").append(nodeid).append("\"");
			buff.append(",label:\"").append(nodename).append("\"");
			buff.append(",dynamic:\"").append("true").append("\"");
			buff.append(",locate:\"").append(nodeilevel).append("\"");
			buff.append(",checked:\"").append("false").append("\"");
			boolean isLeaf = ppsc.isLeaf(bizline, nodeid);
			if(isLeaf){
				/** 有子节点*/
				buff.append(",leaf:\"").append("false").append("\"");
			}else {
				/** 无子节点*/
				buff.append(",leaf:\"").append("true").append("\"");
			}
			buff.append("}");
			if(j < nodeIColl.size() -1){
				buff.append(",");
			}
		}
		buff.append("]");
		return buff.toString();
	}
	/**
	 * 通过目录节点ID获得目录节点下的子目录节点信息
	 * @throws InvalidArgumentException 
	 * @throws ObjectNotFoundException 
	 */
	public String getChildNodeJsonByNodeId(String bizline, String nodeId, IndexedCollection nodeIColl, PrdPolcySchemeComponent ppsc) throws Exception{
		StringBuffer buff = new StringBuffer();
		for(int j=0;j<nodeIColl.size();j++){
			KeyedCollection nodeKColl = (KeyedCollection)nodeIColl.get(j);
			String nodeid = (String)nodeKColl.getDataValue("catalogid");
			String nodename = (String)nodeKColl.getDataValue("catalogname");
			String nodeilevel = (String)nodeKColl.getDataValue("cataloglevel");
			/** 判断下一级目录下是否存在业务线产品 */
			if(ppsc.isLeaf(bizline, nodeid)){
				buff.append("{id:\"").append(nodeid).append("\"");
				buff.append(",label:\"").append(nodename).append("\"");
				buff.append(",dynamic:\"").append("true").append("\"");
				buff.append(",locate:\"").append(nodeilevel).append("\"");
				buff.append(",checked:\"").append("false").append("\"");
				buff.append(",leaf:\"").append("false").append("\"");
				buff.append("}");
				if(j < nodeIColl.size()-1){
					KeyedCollection kHelp = (KeyedCollection)nodeIColl.get(j+1);
					String noidHelp = (String)kHelp.getDataValue("catalogid");
					if(ppsc.isLeaf(bizline, noidHelp)){
						buff.append(",");
					}
				}
			}
		}
		return buff.toString();
	}
	
	/**
	 * 通过上级目录遍历该目录下的产品
	 * @param parentCatalogId 上级目录
	 * @param ppsc
	 * @return
	 * @throws Exception
	 */
	public String getPrdNodeJsonByParentCatalog(String bizline, String parentCatalogId, PrdPolcySchemeComponent ppsc) throws Exception {
		StringBuffer buff = new StringBuffer();
		/** 根据业务线、上级目录的ID取得产品表中使用该上级目录的产品信息 */
		IndexedCollection nextIColl = null;
		nextIColl = ppsc.getNextPrdByBizlineAndCatalog(bizline,parentCatalogId);
		if(nextIColl != null && nextIColl.size() > 0){
			for(int i=0;i<nextIColl.size();i++){
				KeyedCollection nodeKColl = (KeyedCollection)nextIColl.get(i);
				String nodeid = (String)nodeKColl.getDataValue("catalogid");
				String nodename = (String)nodeKColl.getDataValue("catalogname");
				String nodeilevel = (String)nodeKColl.getDataValue("cataloglevel");
				if(nodeilevel == null){
					nodeilevel = "";
				}
				buff.append("{id:\"").append(nodeid).append("\"");
				buff.append(",label:\"").append(nodename).append("\"");
				buff.append(",dynamic:\"").append("true").append("\"");
				buff.append(",locate:\"").append(nodeilevel).append("\"");
				buff.append(",checked:\"").append("false").append("\"");
				/** 产品配置默认无子节点*/
				buff.append(",leaf:\"").append("true").append("\"");
				buff.append("}");
				if(i < nextIColl.size() -1 ){
					buff.append(",");
				}
			}
		}
		return buff.toString();
	}
	/**
	 * 返回根节点和一级目录字符串
	 * @param connection
	 * @param iColl
	 * @param ppsc
	 * @return
	 * @throws Exception
	 */
	public String getRootAndFristNode(Connection connection, IndexedCollection iColl, PrdPolcySchemeComponent ppsc, String bizline) throws Exception {
		StringBuffer buff = new StringBuffer();
		try {
			if(iColl.size() == 0){
				throw new Exception("无根节点信息，请检查该目录根节点是否存在");
			} else {
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					String catalogid = (String)kColl.getDataValue("catalogid");
					String catalogname = (String)kColl.getDataValue("catalogname");
					String cataloglevel = (String)kColl.getDataValue("cataloglevel");
					if(cataloglevel == "null" || cataloglevel == null){
						cataloglevel= catalogid;
					}
					/**
					 * 封装根目录,判断业务条线
					 */
					EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
					Context context = factory.getContextNamed(factory.getRootContextName());
					KeyedCollection dictColl = null;
					dictColl = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
					IndexedCollection dicIColl = (IndexedCollection)dictColl.getDataElement("STD_ZB_PRDLINE");
					String catName = "";
					if(bizline.indexOf(",") != -1){
						String[] bizlines = bizline.split(","); 
						for(int j=0;j<bizlines.length;j++){
							for(int k=0;k<dicIColl.size();k++){
								KeyedCollection kc = (KeyedCollection)dicIColl.get(k);
								String enname = (String)kc.getDataValue("enname");
								String cnname = (String)kc.getDataValue("cnname");
								if(bizlines[j].equals(enname)){
									catName += cnname + "\\";
								}
							}
						}
						catName = catName.substring(0,catName.length()-1);
					}else {
						for(int k=0;k<dicIColl.size();k++){
							KeyedCollection kc = (KeyedCollection)dicIColl.get(k);
							String enname = (String)kc.getDataValue("enname");
							String cnname = (String)kc.getDataValue("cnname");
							if(bizline.equals(enname)){
								catName = cnname;
							}
						}
					}
					/** 封装根目录 */
					buff.append("{id:\"").append(catalogid).append("\"");
					buff.append(",label:\"").append(catName).append("\"");
					buff.append(",dynamic:\"").append("true").append("\"");
					buff.append(",locate:\"").append(cataloglevel).append("\"");
					buff.append(",checked:\"").append("false").append("\"");
					if(ppsc.isLeaf(bizline, catalogid)){
						/** 获取下一级目录信息 */
						IndexedCollection nodeIColl = ppsc.getCatalogICollByParentId(CATALOGTABLENAME, catalogid);
						/** 存在子节点 */
						buff.append(",leaf:\"").append("false").append("\"");
						buff.append(",children:");
						buff.append("[");
						/** 获取业务线下一目录信息 */
						String childString = this.getChildNodeJsonByNodeId(bizline,catalogid, nodeIColl, ppsc);
						buff.append(childString);
						/** 获取业务线下一产品信息 */
						String childPrd = this.getPrdNodeJsonByParentCatalog(bizline, catalogid, ppsc);
						if(childPrd.length() > 0){
							buff.append(",");
							buff.append(childPrd);
						}
						buff.append("]");
					}else {
						/** 无子节点 */
						buff.append(",leaf:\"").append("true").append("\"");
					}
					/**
					 * 封装一级目录
					 */
					/*IndexedCollection nodeIColl = ppsc.getCatalogICollByParentId(CATALOGTABLENAME, catalogid);
					if(nodeIColl.size() != 0){
						*//** 存在子节点 *//*
						buff.append(",leaf:\"").append("false").append("\"");
						*//** 添加子节点*//*
						buff.append(",children:");
						buff.append("[");
						String childString = this.getChildNodeJsonByNodeId(catalogid, nodeIColl, ppsc);
						buff.append(childString);
						buff.append(this.getPrdNodeJsonByParentCatalog(catalogid, ppsc));
						buff.append("]");
					}else {
						*//** 无子节点 *//*
						buff.append(",leaf:\"").append("true").append("\"");
					}*/
					buff.append("}");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buff.toString();
	}
}
