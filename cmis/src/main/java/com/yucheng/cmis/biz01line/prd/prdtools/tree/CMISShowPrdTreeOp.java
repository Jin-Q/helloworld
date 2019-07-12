package com.yucheng.cmis.biz01line.prd.prdtools.tree;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
/**
 * 产品树查询优化，支持多条线返回，需指定目录顶层ID
 * @author Pansq
 */
public class CMISShowPrdTreeOp extends CMISOperation {
	public static final String FATHER_CATALOG_ID = "PRD20120802187";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String bizLine = "";//业务条线
		String jsonStr = null;
		try {
			connection = this.getConnection(context);
			bizLine = (String) context.getDataValue("bizline");
			
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
			
			/** 初始加载遍历业务线下所有产品目录，以及目录下挂接的产品，加载全部 */
			KeyedCollection allKColl = ppsc.getShowPrdTreeByFatherCatalogId(FATHER_CATALOG_ID, bizLine, connection);
			System.out.println(allKColl.toString());
			jsonStr = this.getJsonStrByReturnIColl(connection, allKColl, ppsc, bizLine, context);
			//jsonStr = (jsonStr.substring(0,jsonStr.length()-3)+"]}");
			context.addDataField(CMISConstance.ATTR_RET_JSONDATANAME, jsonStr);
			System.out.println(jsonStr);
			
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}
	
	/**
	 * 将返回的IndexedCollection转换成需要的json串
	 * @param connection 数据库连接
	 * @param kColl 返回的KeyedCollection
	 * @param ppsc
	 * @return json字符串
	 * @throws Exception
	 */
	public String getJsonStrByReturnIColl(Connection connection, KeyedCollection kColl, PrdPolcySchemeComponent ppsc, String bizline, Context context) throws Exception {
		StringBuffer buff = new StringBuffer();
		System.out.println(kColl.toString());
		try {
			if(kColl == null){
				throw new Exception("无根节点信息，请检查该目录根节点是否存在");
			} else {
				/** 获取目录详细信息 */
				String catalogid = FATHER_CATALOG_ID;
				String cataloglevel = "";
				if(cataloglevel == null){
					cataloglevel = "";
				}
				/** 封装根目录 */
				buff.append("{id:\"").append(catalogid).append("\"");
				buff.append(",label:\"").append(this.getBizLineName(bizline, context)).append("\"");
				buff.append(",dynamic:\"").append("true").append("\"");
				buff.append(",locate:\"").append(cataloglevel).append("\"");
				buff.append(",checked:\"").append("false").append("\"");
				
				/** 获取下级目录信息 */
				IndexedCollection prdCatalogIColl = (IndexedCollection)kColl.getDataElement("PrdCatalogList");
				
				/** 获取目录产品信息 */
				IndexedCollection prdBasicInfoIColl = (IndexedCollection)kColl.getDataElement("PrdBasicInfoList");
				
				/** 判断是否存在子节点 */
				if(ppsc.isLeaf(bizline, catalogid)){
					/** 存在子节点 */
					buff.append(",leaf:\"").append("false").append("\"");
					buff.append(",children:");
					buff.append("[");
					/** 添加目录信息 */
					StringBuffer catBuffer = new StringBuffer();
					catBuffer.append(this.getJsonStrByCatalogIColl(prdCatalogIColl, bizline, context, ppsc, connection));
					buff.append(catBuffer);
					/** 添加产品信息 */
					StringBuffer prdBuffer = new StringBuffer();
					prdBuffer.append(this.getJsonStrByPrdIColl(prdBasicInfoIColl));
					if(catBuffer != null && catBuffer.length() > 0 && prdBuffer != null && prdBuffer.length() > 0){
						buff.append(",");
					}
					buff.append(prdBuffer);
					
					buff.append("]");
				}else {
					/** 无子节点 */
					buff.append(",leaf:\"").append("true").append("\"");
				}
				buff.append("}");
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buff.toString();
	}

	/**
	 * 计息出目录信息
	 * @param prdCatalogIColl 目录集合
	 * @param bizline 业务条线
	 * @param context 上下文
	 * @param ppsc
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public String getJsonStrByCatalogIColl(IndexedCollection prdCatalogIColl, String bizline, Context context, PrdPolcySchemeComponent ppsc, Connection connection) throws Exception {
		StringBuffer buff = new StringBuffer();
		int length = 0;
		if(prdCatalogIColl == null || prdCatalogIColl.size() == 0){
			buff.append("");
		}else {
			try {
				for(int i=0;i<prdCatalogIColl.size();i++){
					KeyedCollection kColl = (KeyedCollection)prdCatalogIColl.get(i);
					/** 获取目录详细信息 */
					String catalogid = (String)kColl.getDataValue("catalogid");
					String cataloglevel = (String)kColl.getDataValue("cataloglevel");
					if(cataloglevel == null){
						cataloglevel = "";
					}
					KeyedCollection kc = (KeyedCollection)kColl.getDataElement("KCollList");
					if(kc != null && kc.size() > 0){
						length++;
						/** 封装根目录 */
						buff.append("{id:\"").append(catalogid).append("\"");
						buff.append(",label:\"").append((String)kColl.getDataValue("catalogname")).append("\"");
						buff.append(",dynamic:\"").append("true").append("\"");
						buff.append(",locate:\"").append(cataloglevel).append("\"");
						buff.append(",checked:\"").append("false").append("\"");
						
						
						/** 获取下级目录信息 */
						IndexedCollection prdNextCatalogIColl = (IndexedCollection)kc.getDataElement("PrdCatalogList");
						/** 获取目录产品信息 */
						IndexedCollection prdBasicInfoIColl = (IndexedCollection)kc.getDataElement("PrdBasicInfoList");
						
						/** 判断是否存在子节点 */
						if(ppsc.isLeaf(bizline, catalogid)){
							/** 存在子节点 */
							buff.append(",leaf:\"").append("false").append("\"");
							buff.append(",children:");
							buff.append("[");
							/** 添加目录信息 */
							StringBuffer catBuffer = new StringBuffer();
							catBuffer.append(this.getJsonStrByCatalogIColl(prdNextCatalogIColl, bizline, context, ppsc, connection));
							buff.append(catBuffer);
							/** 添加产品信息 */
							StringBuffer prdBuffer = new StringBuffer();
							prdBuffer.append(this.getJsonStrByPrdIColl(prdBasicInfoIColl));
							if(catBuffer != null && catBuffer.length() > 0 && prdBuffer != null && prdBuffer.length() > 0){
								buff.append(",");
							}
							buff.append(prdBuffer);
							
							buff.append("]");
						}else {
							/** 无子节点 */
							buff.append(",leaf:\"").append("true").append("\"");
						}
						buff.append("}");
						
						if(i<prdCatalogIColl.size()-1){
							buff.append(",");
						}
					}else {
						buff.append("");
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return buff.toString();
	}
	
	/**
	 * 解析出产品信息
	 * @param prdBasicInfoIColl 产品集合
	 * @return String产品json串
	 * @throws Exception
	 */
	public String getJsonStrByPrdIColl(IndexedCollection prdBasicInfoIColl) throws Exception {
		StringBuffer buff = new StringBuffer();
		if(prdBasicInfoIColl != null && prdBasicInfoIColl.size() > 0){
			for(int i=0;i<prdBasicInfoIColl.size();i++){
				KeyedCollection nodeKColl = (KeyedCollection)prdBasicInfoIColl.get(i);
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
				if(i < prdBasicInfoIColl.size() -1){
					buff.append(",");
				}
			}
		}else {
			buff.append("");
		}
		return buff.toString();
	}
	/**
	 * 将业务条线转换为可读的条线名称
	 * @param bizline 业务条线代码
	 * @param context 上下文
	 * @return String
	 * @throws EMPException
	 */
	public String getBizLineName(String bizline,Context context) throws EMPException{
		/** 转换最顶层目录的字典名称 */
        KeyedCollection dictColl = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
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
						catName += cnname + "/";
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
		return catName;
	}
}
