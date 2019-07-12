package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpmortcatalogmana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.component.CatalogManaComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

/**
 *查询押品目录管理下的所有目录，封装成json返回
 * 实时查询数据库读取信息
 * @author 唐顺岩
 * @date 2013-08-23
 */
public class ShowCatalogManaTreeDetailsOp extends CMISOperation {
	private static final String CATALOGTABLENAME="IQP_MORT_CATALOG_MANA";//产品目录表
	
	@Override
	public String doExecute(Context context) throws EMPException {
		String parentNodeId = null;
		Connection connection = null;
		IndexedCollection iColl = null;
		String jsonStr = null;
		String serno = null;
		String value = null;
		connection = this.getConnection(context);
		try{
			serno = (String) context.getDataValue("serno");
			value = (String) context.getDataValue("value");
			parentNodeId = (String) context.getDataValue("parentNodeId");
			
		}catch(Exception e){
		}
		CatalogManaComponent catalogManaComponent = (CatalogManaComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CatalogManaComponent", context, connection);
		try {
			if(context.containsKey("type") && "Y".equalsIgnoreCase(context.getDataValue("type").toString())){
				iColl = catalogManaComponent.getCatalogICollByParentId(CATALOGTABLENAME, "ALL",null,null);
				jsonStr = this.getRootAndFristNode(iColl);
			}else if(context.containsKey("value") && "N".equalsIgnoreCase(context.getDataValue("value").toString())){//add by xiaod
				if(parentNodeId == null || parentNodeId == ""){
					iColl = catalogManaComponent.getCatalogICollByParentId(CATALOGTABLENAME, parentNodeId,serno,value);
					/** 获取根节点和一级目录信息，递归一级目录下所有子目录  */
					jsonStr = this.getAllNodeBySupCatalogNO(iColl, catalogManaComponent,serno,value);
				}
			}else if(serno=="null"||serno==null||"null".equals(serno)){
				iColl = catalogManaComponent.getCatalogICollByParentId(CATALOGTABLENAME, parentNodeId,serno,value);
				/** 获取根节点和一级目录信息，递归一级目录下所有子目录  */
				jsonStr = this.getAllNodeBySupCatalogNO(iColl, catalogManaComponent,null,null);
			}else{
				iColl = catalogManaComponent.getCatalogICollByParentId(CATALOGTABLENAME, parentNodeId,serno,value);
				/** 获取根节点和一级目录信息，递归一级目录下所有子目录  */
				jsonStr = this.getAllNodeBySupCatalogNO(iColl, catalogManaComponent,serno,value);
			}
			
		} catch (Exception e1) {
			throw new EMPException("查询押品目录管理下的所有目录错误，错误描述："+e1.getMessage());
		}
		if(context.containsKey(CMISConstance.ATTR_RET_JSONDATANAME)){
			context.setDataValue(CMISConstance.ATTR_RET_JSONDATANAME, jsonStr);
		}else{
			context.addDataField(CMISConstance.ATTR_RET_JSONDATANAME, jsonStr);
		}
		return "0";
	}
	
	/**
	 * 根据一级目录信息，拼装树形JSON，递归出下面所有子目录
	 * @param iColl 一级目录信息
	 * @param catalogManaComponent 
	 * @return 整个目录结构的JSON串
	 * @throws EMPException
	 */
	public String getAllNodeBySupCatalogNO(IndexedCollection iColl, CatalogManaComponent catalogManaComponent,String serno,String value) throws EMPException {
		StringBuffer buff = new StringBuffer();
		try {
			if(iColl.size() == 0){
				throw new Exception("无根节点信息，请检查该目录根节点是否存在");
			} else {
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					String catalogno = (String)kColl.getDataValue("catalog_no");
					String catalog_name = (String)kColl.getDataValue("catalog_name");
					String catalogpath = (String)kColl.getDataValue("catalog_path");
					if(catalogpath == "null" || catalogpath == null){
						catalogpath= catalogno;
					}
					
					/** 封装根目录 */
					buff.append("{id:\"").append(catalogno).append("\"");
					buff.append(",label:\"").append(catalog_name).append("\"");
					buff.append(",dynamic:\"").append("false").append("\"");   //是否点击树形的+号时实时查询数据库
					buff.append(",locate:\"").append(catalogpath).append("\"");
					buff.append(",checked:\"").append("false").append("\"");
					IndexedCollection nodeIColl = new IndexedCollection();
					boolean hasleaf=false;//是否存在子节点
					if(!"null".equals(serno)){
						hasleaf=catalogManaComponent.isLeaf(catalogno,serno);
					}else if("N".equals(value)){
						hasleaf=catalogManaComponent.isLeafNoValue(catalogno);
					}else{
						hasleaf=catalogManaComponent.isLeaf(catalogno);
					}
					if(hasleaf){
						if(null!=serno||"null"!=serno||!"null".equals(serno)){
							/** 获取下一级目录信息 (押品准入时)*/
							nodeIColl = catalogManaComponent.getCatalogICollByParentId(CATALOGTABLENAME, catalogno,serno,value);
						}else if(value!=null&&"N".equals(value)){
							/** 获取下一级目录信息 */
							nodeIColl = catalogManaComponent.getCatalogICollByParentId(CATALOGTABLENAME, catalogno,serno,value);
						}
						
						/** 存在子节点 */
						buff.append(",leaf:\"").append("false").append("\"");
						buff.append(",children:");
						buff.append("[");
						String childString = this.getAllNodeBySupCatalogNO(nodeIColl, catalogManaComponent,serno,value); //如果存在子节点递归调用自己
						buff.append(childString);
						buff.append("]");
					}else {
						/** 无子节点 */
						buff.append(",leaf:\"").append("true").append("\"");  //不管有无子节点都展示
					}
					buff.append("}");
					if(i < iColl.size() -1){
						buff.append(",");
					}
				}
			}
			
		} catch (Exception e) {
			throw new EMPException("查询根节点及一级目录字符串错误，错误描述："+e.getMessage());
		}
		return buff.toString();
	}
	
	/**
	 * 根据一级目录信息，拼装树形JSON，递归出下面所有子目录
	 * @param iColl 一级目录信息
	 * @param catalogManaComponent 
	 * @return 整个目录结构的JSON串
	 * @throws EMPException
	 */
	public String getRootAndFristNode(IndexedCollection iColl) throws EMPException {
		StringBuffer buff = new StringBuffer();
		try {
			if(iColl.size() == 0){
				throw new Exception("无根节点信息，请检查该目录根节点是否存在");
			} else {
				buff.append("{id:\"").append("Z090100").append("\"");
				buff.append(",label:\"").append("货物质押").append("\"");
				buff.append(",dynamic:\"").append("false").append("\"");   //是否点击树形的+号时实时查询数据库
				buff.append(",locate:\"").append("").append("\"");
				buff.append(",checked:\"").append("false").append("\"");
				buff.append(",leaf:\"").append("true").append("\"");
				buff.append(",children:");
				buff.append("[");
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					String catalogno = (String)kColl.getDataValue("catalog_no");
					String catalog_name = (String)kColl.getDataValue("catalog_name");
					String catalogpath = (String)kColl.getDataValue("catalog_path");
					if(catalogpath == "null" || catalogpath == null){
						catalogpath= catalogno;
					}
					
					/** 封装根目录 */
					buff.append("{id:\"").append(catalogno).append("\"");
					buff.append(",label:\"").append(catalog_name).append("\"");
					buff.append(",dynamic:\"").append("false").append("\"");   //是否点击树形的+号时实时查询数据库
					buff.append(",locate:\"").append(catalogpath).append("\"");
					buff.append(",checked:\"").append("false").append("\"");
					buff.append(",leaf:\"").append("true").append("\"");  //不管有无子节点都展示
					buff.append("}");
					
					if(i < iColl.size() -1){
						buff.append(",");
					}
				}
				buff.append("]");
				buff.append("}");
			}
			
		} catch (Exception e) {
			throw new EMPException("查询根节点及一级目录字符串错误，错误描述："+e.getMessage());
		}
		return buff.toString();
	}
}
