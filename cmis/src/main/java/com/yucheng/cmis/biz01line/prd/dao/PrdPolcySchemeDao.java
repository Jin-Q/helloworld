package com.yucheng.cmis.biz01line.prd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * CMISDao层不直接写数据库操作，通过调用命名SQL实现数据库操作
 * @author Pansq
 * @create 20130718
 * 待完善，将业务逻辑抽离至CMISComponent层
 */
public class PrdPolcySchemeDao extends CMISDao {

	/**
	 * @关闭输入输出流
	 * @param pstmt
	 * @param rs
	 */
	public void closeResultAndPreparedStatement(PreparedStatement pstmt,ResultSet rs){
		if(rs != null){
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(pstmt != null){
			try {
				pstmt.close();
				pstmt = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 根据政策值获取已配置流程
	 * @param schemeId 政策ID
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getFlowIdBySchemeId(String schemeId) throws Exception {
		IndexedCollection iColl = new IndexedCollection(schemeId);
		IndexedCollection resultIColl = null;
		try {
			resultIColl = SqlClient.queryList4IColl("getFlowIdBySchemeId", schemeId, this.getConnection());
			for(int i=0;i<resultIColl.size();i++){
				KeyedCollection kColl = new KeyedCollection("PrdSchemeSpaceRel");
				KeyedCollection kCollResult = null;
				kCollResult = (KeyedCollection)resultIColl.get(i);
				String flowid = ""; 
				String effective = "";
				String schemeid = "";
				try {
					flowid = (String)kCollResult.getDataValue("flowid");
					effective = (String)kCollResult.getDataValue("effective");
					schemeid = (String)kCollResult.getDataValue("schemeid");
					if(flowid == null || flowid == "" || flowid == "null"){
						resultIColl.remove(i);
					}else {
						IndexedCollection ic = (IndexedCollection)((KeyedCollection)this.getContext().getDataElement("dictColl")).getDataElement(PRDConstant.FLOW_TYPE);
						for(int j=0;j<ic.size();j++){
							KeyedCollection c = (KeyedCollection)ic.get(j);
							if(flowid.equals(c.getDataValue("enname"))){
								kColl.addDataField("flowid",flowid);
								kColl.addDataField("flowname", c.getDataValue("cnname"));
								kColl.addDataField("effective", effective);
								kColl.addDataField("schemeid", schemeid);
							}
						}
						iColl.add(kColl);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return iColl;
	}
	/**
	 * 根据政策值、流程值获取已配置流程节点
	 * @param schemeId 政策ID
	 * @param flowId 流程ID
	 * @return String
	 * @throws Exception
	 */
	public String getFlowNodeBySchemeIdAndFlowId(String schemeId, String flowId) throws Exception {
		String selectFlowNode = "";
		KeyedCollection kc = new KeyedCollection();
		try {
			kc.addDataField("schemeid", schemeId);
			kc.addDataField("flowid", flowId);
			
			IndexedCollection iColl = SqlClient.queryList4IColl("getFlowNodeBySchemeIdAndFlowId", kc, this.getConnection());
			/**
			 * 对IColl循环遍历取数据
			 */
			String flowNode = "";
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				flowNode += (String)kColl.getDataValue("flownode")+",";
			}
			selectFlowNode = flowNode.substring(0,flowNode.length()-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectFlowNode;
	}
	/**
	 * 正对政策关联表信息进行新增操作，修改原纪录
	 * 插入规则：政策编号、流程编号、流程节点（需要拆分出多条记录）
	 * @param kColl
	 */
	public void insertPrdSchemeSpaceRel(String ifSelect, KeyedCollection kColl) throws Exception {
		try {
			String flowValue = (String)kColl.getDataValue("flowValue");
			String flowNodeValue = (String)kColl.getDataValue("flowNodeValue");
			String schemecode = (String)kColl.getDataValue("schemecode");
			String schemeid = (String)kColl.getDataValue("schemeid");
			String schemetype = (String)kColl.getDataValue("schemetype");
			/*拆分流程节点，以，号拆分*/
			String flowNodeArr[] = flowNodeValue.split(",");
			for(int i=0;i<flowNodeArr.length;i++){
				/** 删除原有拦截方案信息 */
				KeyedCollection delKColl = new KeyedCollection("delKColl");
				delKColl.addDataField("schemeid", schemeid);
				delKColl.addDataField("flowid", flowValue);
				delKColl.addDataField("flownode", flowNodeArr[i]);
				delKColl.addDataField("schemecode", schemecode);
				int delKCollResult = SqlClient.delete("deleteSchemeRel", delKColl, this.getConnection());
				
				/** 新增新的拦截方案 */
				Map<String, String> insertMap = new HashedMap();
				insertMap.put("schemeid", schemeid);
				insertMap.put("flowid", flowValue);
				insertMap.put("flownode", flowNodeArr[i]);
				insertMap.put("schemecode", schemecode);
				if(ifSelect.equals("1")){
					insertMap.put("status", schemetype);
				}else {
					insertMap.put("status", "");
				}
				int insertKCollResult = SqlClient.insert("insertSchemeRel", insertMap, this.getConnection());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	/**
	 * 根据操作ID对政策关联场景进行操作
	 * @param doId 操作ID,包括场景打开、场景关闭、场景删除
	 * @param schemeId 政策ID
	 * @param flowId 流程ID
	 * @param flowNode 流程节点
	 * @param polcyCode 政策代码
	 * @param status 审核状态
	 */
	public void updatePrdSpaceRel(String doId, String schemeId, String flowId,String flowNode, String polcyCode, String status) throws Exception {
		try {
			
			KeyedCollection delKColl = new KeyedCollection("delKColl");
			delKColl.addDataField("schemeid", schemeId);
			delKColl.addDataField("flowid", flowId);
			delKColl.addDataField("flownode", flowNode);
			delKColl.addDataField("schemecode", polcyCode);
			
			Map<String, String> deleteMap = new HashedMap();
			deleteMap.put("schemeid", schemeId);
			deleteMap.put("flowid", flowId);
			deleteMap.put("flownode", flowNode);
			deleteMap.put("schemecode", polcyCode);
			if("delete".equals(doId)){
				//删除关联场景
				//int delKCollResult = SqlClient.delete("deleteSchemeRelMap", delKColl, this.getConnection());
				int delMapResult = SqlClient.executeUpd("deleteSchemeRelMap", null, deleteMap, null, this.getConnection());
			}else if("start".equals(doId)){
				//关闭关联场景
				deleteMap.put("effective", "1");
				int updKCollResult = SqlClient.executeUpd("updateSchemeRelStatus", null, deleteMap, null, this.getConnection());
			}else if("close".equals(doId)){
				//打开关联场景
				deleteMap.put("effective", "2");
				int updKCollResult = SqlClient.executeUpd("updateSchemeRelStatus", null, deleteMap, null, this.getConnection());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据政策方案ID、流程ID、流程节点ID获得节点配置的政策策略
	 * @param schemeId 政策方案ID
	 * @param flowId 流程ID
	 * @param nodeId 流程节点ID
	 * @return IndexedCollection
	 * @throws ComponentException
	 */
	public IndexedCollection getPlocyListBySchemeIdAndFlowIdAndNodeId(String schemeId, String flowId, String nodeId) throws Exception {
		IndexedCollection iColl = new IndexedCollection();
		try {
			KeyedCollection paramKColl = new KeyedCollection();
			paramKColl.addDataField("schemeid", schemeId);
			paramKColl.addDataField("flowid", flowId);
			paramKColl.addDataField("flownode", nodeId);
			
			IndexedCollection resultIColl = SqlClient.queryList4IColl("getPlocyListBySchemeIdAndFlowIdAndNodeId", paramKColl, this.getConnection());
			/** 对返回IndexedCollection解析分析是否为选中节点 */
			for(int i=0;i<resultIColl.size();i++){
				KeyedCollection kHelp = (KeyedCollection)resultIColl.get(i);
				KeyedCollection kColl = new KeyedCollection();
				kColl.addDataField("schemecode", kHelp.getDataValue("schemecode"));
				kColl.addDataField("schemedesc", kHelp.getDataValue("schemedesc"));
				kColl.addDataField("schemetype", kHelp.getDataValue("status"));
				if(kHelp.getDataValue("status") != "" && kHelp.getDataValue("status") != null){
					kColl.addDataField("ifSelect", "1");//节点默认为未选择
				}
				iColl.add(kColl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iColl;
	}
	/**
	 * 产品配置模块【根据产品ID获取产品项下关联的机构】
	 * @param prdId 产品ID
	 * @return IndexedCollection
	 * @throws ComponentException
	 */
	public IndexedCollection getPrdOrgApplyICollByPrdId(String prdId) throws Exception {
		IndexedCollection iColl = new IndexedCollection();
		try {
			iColl = SqlClient.queryList4IColl("getPrdOrgApplyICollByPrdId", prdId, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iColl;
	}
	/**
	 * 产品配置关联机构设置，遍历iColl中的kColl,根据kColl中的操作参数进行相关才做
	 * @param iColl 产品适用机构iColl
	 * @param prdId 产品编号
	 * @throws ComponentException
	 */
	public void doPrdOrgApplyByIColl(String prdId, IndexedCollection iColl) throws Exception {
		try {
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String optype = (String)kColl.getDataValue("optType");
				Map<String,String> insertMap = new HashedMap();
				insertMap.put("prd_id", prdId);
				insertMap.put("org_id", (String)kColl.getDataValue("org_id"));
				insertMap.put("apply_range", (String)kColl.getDataValue("apply_range"));
				insertMap.put("apply_type", (String)kColl.getDataValue("apply_type"));
				
				if("add".equals(optype)||"".equals(optype)){
					/** 判断该条数据是否已经存在，存在则修改原数据 */
					IndexedCollection queryIColl = SqlClient.queryList4IColl("selectPrdOrgApplyByIColl", insertMap, this.getConnection());
					if(queryIColl.size() != 0){
						Map<String,String> param = new HashedMap();
						param.put("prd_id", prdId);
						param.put("org_id", (String)kColl.getDataValue("org_id"));
						Map<String,String> value = new HashedMap();
						value.put("apply_range", (String)kColl.getDataValue("apply_range"));
						value.put("apply_type", (String)kColl.getDataValue("apply_type"));
						int updateResult = SqlClient.executeUpd("updatePrdOrgApplyByIColl", param, value, null, this.getConnection());
					}else {
						/** 新增操作*/
						int insertResult = SqlClient.insert("insertPrdOrgApplyByIColl", insertMap, this.getConnection());
					}
				}else if("del".equals(optype)){//删除操作
					int deleteResult = SqlClient.executeUpd("deletePrdOrgApplyByIColl", insertMap, null, null, this.getConnection());
				}else {
					//新增后又删除，该条记录作废，不做任何处理,此处标出制作逻辑说明
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 产品配置模块【根据政策ID、流程ID获取政策方案项下流程中所有政策信息】
	 * @param schemeId
	 * @param flowId
	 * @return
	 * @throws ComponentException
	 */
	public IndexedCollection getPlocyListBySchemeIdAndFlowId(String schemeId, String flowId) throws Exception {
		IndexedCollection iColl = new IndexedCollection();
		try {
			IndexedCollection iCollResult = SqlClient.queryList4IColl("getPlocyListBySchemeIdAndFlowId", schemeId, this.getConnection());
			for(int i=0;i<iCollResult.size();i++){
				KeyedCollection returnKColl = new KeyedCollection();
				KeyedCollection kColl = (KeyedCollection)iCollResult.get(i);
				returnKColl.addDataField("schemecode", kColl.getDataValue("schemecode"));
				returnKColl.addDataField("schemedesc", kColl.getDataValue("schemedesc"));
				returnKColl.addDataField("schemetype", "");
				returnKColl.addDataField("ifSelect", "");
				iColl.add(returnKColl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iColl;
	}
	/**
	 * 根据操作ID对政策关联场景进行操作
	 * @param doId 操作ID,包括场景打开、场景关闭、场景删除
	 * @param schemeId 政策ID
	 * @param flowId 流程ID
	 * @param status 审核状态
	 */
	public void doPrdSpaceRel(String doId, String schemeId, String flowId, String status) throws Exception {
		try {
			Map<String, String> deleteMap = new HashedMap();
			deleteMap.put("schemeid", schemeId);
			deleteMap.put("flowid", flowId);
			if("delete".equals(doId)){
				//删除关联场景
				int delMapResult = SqlClient.executeUpd("deleteSchemeRel1", deleteMap, null, null, this.getConnection());
			}else if("start".equals(doId)){
				//关闭关联场景
				int updKCollResult = SqlClient.executeUpd("updateSchemeRelStatus1", deleteMap, "1", null, this.getConnection());
			}else if("close".equals(doId)){
				//打开关联场景
				int updKCollResult = SqlClient.executeUpd("updateSchemeRelStatus1", deleteMap, "2", null, this.getConnection());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 *  产品配置模块【根据政策方案ID,查询条件获取政策方案下未配置关联的政策资料】
	 * @param schemeId 政策方案ID
	 * @return IndexedCollection
	 */
	public IndexedCollection getPrdPlocyBySchemeId(String schemeId,String conditionStr) throws Exception {
		IndexedCollection iColl = new IndexedCollection();
		Connection conn = this.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM PRD_PLOCY WHERE SCHEMECODE NOT IN (SELECT DISTINCT(PLOCYCODE) FROM PRD_SCHEME_SPACE_REL WHERE SCHEMEID=?)"+conditionStr;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, schemeId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				KeyedCollection kColl = new KeyedCollection("PrdPlocy");
				kColl.addDataField("schemecode", (String)rs.getString("schemecode"));
				kColl.addDataField("schemedesc", (String)rs.getString("schemedesc"));
				kColl.addDataField("ifwarrant", (String)rs.getString("ifwarrant"));
				kColl.addDataField("schemetype", (String)rs.getString("schemetype"));
				kColl.addDataField("inputid", (String)rs.getString("inputid"));
				kColl.addDataField("inputdate", (String)rs.getString("inputdate"));
				kColl.addDataField("orgid", (String)rs.getString("orgid"));
				iColl.add(kColl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeResultAndPreparedStatement(pstmt, rs);
		}
		return iColl;
	}
	/**
	 * 产品配置模块【根据政策方案ID和获取的资料列表建立两者关联关系】
	 * @param schemeId 政策方案ID
	 * @param schemeCode 资料代码
	 */
	public void connPrdPlocyWithPrdScheme(String schemeId, String schemeCode) throws Exception {
		try {
			Map <String,String> insertMap = new HashedMap();
			insertMap.put("schemeid", schemeId);
			insertMap.put("plocycode", schemeCode);
			int result = SqlClient.insert("connPrdPlocyWithPrdScheme", insertMap, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 产品配置模块【根据政策方案ID获取政策关联表中已关联的政策资料】
	 * @param schemeId
	 * @return IndexedCollection
	 */
	public IndexedCollection getPlocyListBySchemeId(String schemeId) throws Exception {
		IndexedCollection iColl = new IndexedCollection();
		try {
			IndexedCollection iCollResult = SqlClient.queryList4IColl("getPlocyListBySchemeIdAndFlowId", schemeId, this.getConnection());
			for(int i=0;i<iCollResult.size();i++){
				KeyedCollection returnKColl = new KeyedCollection();
				KeyedCollection kColl = (KeyedCollection)iCollResult.get(i);
				returnKColl.addDataField("schemecode", kColl.getDataValue("schemecode"));
				returnKColl.addDataField("schemedesc", kColl.getDataValue("schemedesc"));
				returnKColl.addDataField("schemetype", "");
				returnKColl.addDataField("ifSelect", "");
				iColl.add(returnKColl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iColl;
	}
	/**
	 * 往政策场景关联表中插入关联信息
	 * @param ifSelect 选择标志
	 * @param kColl
	 */
	public void updatePrdSchemeSpaceRel(String ifSelect, KeyedCollection kColl) throws Exception {
		
		try {
			String flowValue = (String)kColl.getDataValue("flowValue");
			String flowNodeValue = (String)kColl.getDataValue("flowNodeValue");
			String schemecode = (String)kColl.getDataValue("schemecode");
			String schemeid = (String)kColl.getDataValue("schemeid");
			String schemetype = (String)kColl.getDataValue("schemetype");
			/*拆分流程节点，以，号拆分*/
			String flowNodeArr[] = flowNodeValue.split(",");
			for(int i=0;i<flowNodeArr.length;i++){
				Map<String,String> paramMap = new HashedMap();
				paramMap.put("schemeid", schemeid);
				paramMap.put("plocycode", schemecode);
				paramMap.put("flowid", flowValue);
				paramMap.put("flownode", flowNodeArr[i]);
				IndexedCollection queryIColl = SqlClient.queryList4IColl("selectPrdSchemeSpaceRel", paramMap, this.getConnection());
				if(queryIColl.size() != 0){
					/** 更新操作 */
					Map<String,String> valueMap = new HashedMap();
					if(ifSelect.equals("1")){
						valueMap.put("status", schemetype);
					}else {
						valueMap.put("status", "");
					}
					int updateResult = SqlClient.executeUpd("updatePrdSchemeSpaceRel", paramMap, valueMap, null, this.getConnection());
				}else {
					/** 新增操作 */
					if(ifSelect.equals("1")){
						paramMap.put("status", schemetype);
					}else {
						paramMap.put("status", "");
					}
					int insertResult = SqlClient.insert("insertPrdSchemeSpaceRel", paramMap, this.getConnection());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 通过政策ID获取政策资料列表数据，特定字段可增加
	 * @param schemeId 方案ID
	 * @return IndexedCollection
	 * @throws ComponentException
	 */
	public IndexedCollection getPlocyRelListBySchemeId(String schemeId) throws Exception {
		IndexedCollection iColl = null;
		
		try {
			iColl = SqlClient.queryList4IColl("getPlocyRelListBySchemeId", schemeId, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iColl;
	}
	/**
	 * 移除政策方案和政策资料的关联关系，同步清理政策下所有节点信息
	 * @param schemeId 方案ID
	 * @param schemeCode 资料ID
	 */
	public void delPrdPlocyWithPrdScheme(String schemeId, String schemeCode) throws Exception {
		try {
			Map<String,String> paramMap = new HashedMap();
			paramMap.put("schemeid", schemeId);
			paramMap.put("schemecode", schemeCode);
			int delMapResult = SqlClient.executeUpd("delPrdPlocyWithPrdScheme", paramMap, null, null, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据资源表中上级资源ID获得改资源下所有资源列表
	 * @param parentId 上一级资源ID
	 * @return IndexedCollection
	 */
	public IndexedCollection getTabResourceListByParentId(String parentId) throws Exception {
		IndexedCollection iColl = null;
		try {
			iColl = SqlClient.queryList4IColl("getTabResourceListByParentId", null, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iColl;
	}
	public IndexedCollection getICollByModelIdAndConndition(String tableName,
			String conndition, PageInfo pageInfo) throws Exception {
		/**
		 * 取分页参数作为查询依据
		 */
		int pageSize = pageInfo.pageSize;//每页记录数
		int pageIdx = pageInfo.pageIdx;//当前页数标签
		int beginIdx = (pageIdx-1 <= 0 ? 0:pageIdx-1)*pageSize+1;
		int endIdx = (pageIdx == 0 ? 1:pageIdx)*pageSize;
		pageInfo.beginIdx = beginIdx;
		pageInfo.endIdx = endIdx;
		IndexedCollection iColl = new IndexedCollection();
		Connection conn = this.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM (SELECT rownum row_num, t.* from (SELECT * FROM "+ tableName +" " + conndition+") t) WHERE row_num BETWEEN "+beginIdx+" AND "+endIdx;
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs != null){
				ResultSetMetaData rsm = rs.getMetaData();
		        int column = rsm.getColumnCount();
				while(rs.next()){
					KeyedCollection rowKcoll=new KeyedCollection("PrdPlocyList");
	              	for (int i = 1; i <= column; i++) {
	              		String key = rsm.getColumnName(i);
	              		key=key.toLowerCase();
	              		Object value = rs.getObject(i);
	              		rowKcoll.addDataField(key, value);
	              	}
	              	iColl.add(rowKcoll);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeResultAndPreparedStatement(pstmt, rs);
		}
		return iColl;
	}
	public IndexedCollection getCatalogICollByParentId(String tableName, String parentId) throws Exception {
		IndexedCollection iColl = new IndexedCollection();
		Connection conn = this.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "";
			if(parentId == null || parentId ==""){
				/** 获取上一目录为空则表示为最顶级目录 */
				sql = "select * from "+tableName+" where supcatalogid is null";
			} else {
				/** 根据上级目录获得上级目录下的子目录 */
				sql = "select * from "+tableName+" where supcatalogid= '"+ parentId+"'";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				KeyedCollection kColl = new KeyedCollection("catalogTree");
				String catalogid = (String)rs.getString("catalogid");
				String catalogname = (String)rs.getString("catalogname");
				String cataloglevel = (String)rs.getString("cataloglevel");
				String supcatalogid = (String)rs.getString("supcatalogid");
				
				kColl.addDataField("catalogid", catalogid);
				kColl.addDataField("catalogname", catalogname);
				kColl.addDataField("cataloglevel", cataloglevel);
				kColl.addDataField("supcatalogid", supcatalogid);
				iColl.add(kColl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeResultAndPreparedStatement(pstmt, rs);
		}
		return iColl;
	}
	/**
	 * 通过产品上级目录检索产品表中的产品信息
	 * @param parentCatalogId 上级目录ID
	 * @return IndexedCollection
	 */
	public IndexedCollection getPrdByParentCatalogId(String parentCatalogId) throws Exception {
		IndexedCollection IColl = null;
		try {
			IColl = SqlClient.queryList4IColl("getPrdByParentCatalogId", parentCatalogId, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return IColl;
	}
	/**
	 * 通过业务线查询节点下下一节点信息
	 * @param catalogid 上一目录ID
	 * @return IndexedCollection
	 */
	public IndexedCollection getNextCatalogByCatalogId(String catalogid) throws Exception {
		IndexedCollection nextIColl = null;
		try {
			nextIColl = SqlClient.queryList4IColl("getNextCatalogByCatalogId", catalogid, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nextIColl;
	}
	/**
	 * 通过目录ID获得目录下的产品信息
	 * @param catalogid 目录ID
	 * @return IndexedCollection
	 */
	public IndexedCollection queryPrdBasicinfoByCatalog(Map<String,String> paramMap) throws Exception {
		IndexedCollection nextIColl = null;
		try {
			nextIColl = SqlClient.queryList4IColl("queryPrdBasicinfoByCatalog", paramMap, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nextIColl;
	}
	/**
	 * 通过产品编号获得产品信息
	 * @param prdId 产品编号
	 * @param connection
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getPrdBasicinfoByPrdId(String prdId, Connection connection) throws Exception {
		KeyedCollection kc = null;
		try {
			kc = (KeyedCollection)SqlClient.queryFirst("getPrdBasicinfoByPrdId4KColl", prdId, null, connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kc;
	}
	/**
	 * 通过资源ID获取资源操作表中的操作权限
	 * @param resourceid 资源ID
	 * @return
	 * @throws Exception
	 */
	public IndexedCollection getActionByResourceId(String resourceid) throws Exception {
		IndexedCollection ic = null;
		try {
			ic = (IndexedCollection)SqlClient.queryList4IColl("getActionByResourceId", resourceid, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ic;
	}
	/**
	 * 通过业务品种、币种、期限类型、期限获取利率信息
	 * @param prdId 业务品种
	 * @param currType 币种
	 * @param termM 期限
	 * @return 利率以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getRate(String prdId, String currType, int termM,Connection connection) throws Exception {
		KeyedCollection returnKColl = null;
		try {
			Map paramMap = new HashMap();
			paramMap.put("prdid", prdId);
			paramMap.put("curtype", currType);
			paramMap.put("termM", termM);
			returnKColl = (KeyedCollection)SqlClient.queryFirst("getRate", paramMap, null, connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnKColl;
	}
	
	/**
     * 通过币种和利率种类获取Libor基准年利率
     * @param currType 币种
     * @param irType   利率种类
     * @param connection   数据库连接
     * @return returnKColl 
     * @throws Exception 
     */
	public KeyedCollection getLiborRate(String currType,String openDay, String irType,Connection connection) throws Exception {
		KeyedCollection returnKColl = null;
		try {
			Map paramMap = new HashMap();
			paramMap.put("currType", currType);
			paramMap.put("irType", irType);
			paramMap.put("openDay", openDay);
			returnKColl = (KeyedCollection)SqlClient.queryFirst("getLiborRate", paramMap, null, connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnKColl;
	}
}
