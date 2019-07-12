package com.yucheng.cmis.biz01line.prd.prdtools.tree;

import java.sql.Connection;

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

public class CMISShowTreePrd extends CMISOperation {
	private static final String TABLENAME="prd_catalog";
	@Override
	public String doExecute(Context context) throws EMPException {
		CMISProductTreeDicService services = (CMISProductTreeDicService)context.getService("treePrdService");
		String parentNodeId = null;
		Connection connection = null;
		IndexedCollection iColl = null;
		try {
			connection = this.getConnection(context);
			try{
				parentNodeId = (String) context.getDataValue("parentNodeId");
			}catch(Exception e){
			}
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
			try {
				iColl = ppsc.getCatalogICollByParentId(TABLENAME, parentNodeId);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			String jsonStr = null;
			
			if(parentNodeId == null || parentNodeId == ""){
				try {
					jsonStr = this.getRootAndFristNode(connection, iColl, ppsc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				try {
					jsonStr = this.getChildNodeJsonByNodeId(connection, parentNodeId, iColl, ppsc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			try {
				context.addDataField(CMISConstance.ATTR_RET_JSONDATANAME, jsonStr);
			} catch (DuplicatedDataNameException e) {
				context.setDataValue(CMISConstance.ATTR_RET_JSONDATANAME, jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return "0";
	}

	/**
	 * 通过节点ID获得节点下的子节点信息
	 * @throws InvalidArgumentException 
	 * @throws ObjectNotFoundException 
	 */
	public String getChildNodeJsonByNodeId(Connection connection, String nodeId, IndexedCollection nodeIColl, PrdPolcySchemeComponent ppsc) throws Exception{
		StringBuffer buff = new StringBuffer();
		buff.append("[");
		for(int j=0;j<nodeIColl.size();j++){
			KeyedCollection nodeKColl = (KeyedCollection)nodeIColl.get(j);
			String nodeid = (String)nodeKColl.getDataValue("catalogid");
			String nodename = (String)nodeKColl.getDataValue("catalogname");
			String nodeilevel = (String)nodeKColl.getDataValue("cataloglevel");
			//String nodesupid = (String)nodeKColl.getDataValue("supcatalogid");
			buff.append("{id:\"").append(nodeid).append("\"");
			buff.append(",label:\"").append(nodename).append("\"");
			buff.append(",dynamic:\"").append("true").append("\"");
			buff.append(",locate:\"").append(nodeilevel).append("\"");
			buff.append(",checked:\"").append("false").append("\"");
			
			IndexedCollection nodeICollChild = ppsc.getCatalogICollByParentId(TABLENAME, nodeid);
			if(nodeICollChild.size() != 0){
				/** 有子节点*/
				buff.append(",leaf:\"").append("false").append("\"");
			}else {
				/** 无子节点*/
				buff.append(",leaf:\"").append("true").append("\"");
			}
			buff.append("}");
			if(j < nodeIColl.size() -1 ){
				buff.append(",");
			}
		}
		buff.append("]");
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
	public String getRootAndFristNode(Connection connection, IndexedCollection iColl, PrdPolcySchemeComponent ppsc) throws Exception {
		StringBuffer buff = new StringBuffer();
		try {
			if(iColl.size() == 0){
				throw new Exception("无根节点信息，请检查该目录节点是否存在");
			} else {
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					String catalogid = (String)kColl.getDataValue("catalogid");
					String catalogname = (String)kColl.getDataValue("catalogname");
					String cataloglevel = (String)kColl.getDataValue("cataloglevel");
					if(cataloglevel == "null" || cataloglevel == null){
						cataloglevel= "";
					}
					//String supcatalogid = (String)kColl.getDataValue("supcatalogid");
					/**
					 * 封装根目录
					 */
					buff.append("{id:\"").append(catalogid).append("\"");
					buff.append(",label:\"").append(catalogname).append("\"");
					buff.append(",dynamic:\"").append("true").append("\"");
					buff.append(",locate:\"").append(cataloglevel).append("\"");
					buff.append(",checked:\"").append("false").append("\"");
					/**
					 * 封装一级目录
					 */
					IndexedCollection nodeIColl = ppsc.getCatalogICollByParentId(TABLENAME, catalogid);
					if(nodeIColl.size() != 0){
						/** 存在子节点 */
						buff.append(",leaf:\"").append("false").append("\"");
						/** 添加子节点*/
						buff.append(",children:");
						String childString = this.getChildNodeJsonByNodeId(connection, catalogid, nodeIColl, ppsc);
						buff.append(childString);
					}else {
						/** 无子节点 */
						buff.append(",leaf:\"").append("true").append("\"");
					}
					buff.append("}");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buff.toString();
	}
}
