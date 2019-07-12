package com.yucheng.cmis.platform.riskmanage.op.prdpreventrisk.prdpvriskscene;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.riskmanage.component.RiskManageComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class QueryPrdPvRiskSceneListOp extends CMISOperation {
	private static final String scenModel = "PrdPvRiskScene";
	private static final String itemModel = "PrdPvRiskItem";
	private static final String relModel = "PrdPvRiskItemRel";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String prevent_id = "";
			if(context.containsKey("prevent_id")){
				prevent_id = (String)context.getDataValue("prevent_id");
			}
			if(prevent_id == null || prevent_id.trim().length() == 0){
				throw new EMPException("获取风险拦截方案ID失败！");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			String conndition = " where prevent_id = '"+prevent_id+"'";
			/** 
			 * 查询场景中是否存在配置的方案信息 
			 * 1.取出配置过的拦截方案信息
			 * 2.取出关联表中配置的未在拦截场景中配置的信息
			 * 3.将两部风合并作为己过返回
			 * */
			IndexedCollection returnIColl = new IndexedCollection();
			
			RiskManageComponent riskManageComponent = (RiskManageComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("RiskManage", context, connection);
			IndexedCollection sceneIColl = riskManageComponent.doSqlExecute("select distinct(item_id),prevent_id,wfid,risk_level,item_name from prd_pv_risk_scene "+conndition,connection);
			String itemList = "";
			if(sceneIColl != null && sceneIColl.size() > 0){
				for(int i=0;i<sceneIColl.size();i++){
					KeyedCollection senKColl = new KeyedCollection();
					KeyedCollection insertKColl = (KeyedCollection)sceneIColl.get(i);
					String item_id = (String)insertKColl.getDataValue("item_id");
					senKColl.addDataField("prevent_id", prevent_id);
					senKColl.addDataField("item_id", item_id);
					senKColl.addDataField("item_name", insertKColl.getDataValue("item_name"));
					senKColl.addDataField("risk_level", insertKColl.getDataValue("risk_level"));
					returnIColl.add(senKColl);
					itemList = itemList + "'"+item_id+"',";
				}
				itemList = "("+itemList.substring(0, itemList.length()-1)+")";
			}
			
			String itemSql = "";
			if(itemList.trim().length() > 0){
				itemSql = " and item_id not in "+ itemList;
			}
			IndexedCollection relIColl = dao.queryList(relModel, " where prevent_id = '"+prevent_id+"' " +itemSql, connection);
			if(relIColl != null && relIColl.size() > 0){
				for(int i=0;i<relIColl.size();i++){
					KeyedCollection relKColl = new KeyedCollection();
					KeyedCollection insertKColl = (KeyedCollection)relIColl.get(i);
					String item_id = (String)insertKColl.getDataValue("item_id");
					KeyedCollection itemKColl = dao.queryDetail(itemModel, item_id, connection);
					relKColl.addDataField("prevent_id", prevent_id);
					relKColl.addDataField("item_id", item_id);
					relKColl.addDataField("item_name", itemKColl.getDataValue("item_name"));
					relKColl.addDataField("risk_level", "1");//未选中的默认为提示
					returnIColl.add(relKColl);
				}
			}
			returnIColl.setName("PrdPvRiskSceneList");
			this.putDataElement2Context(returnIColl, context);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
