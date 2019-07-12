package com.yucheng.cmis.platform.riskmanage.op.prdpreventrisk.prdpvriskscene;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.riskmanage.component.RiskManageComponent;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class QueryPrdPreventRiskPrdPvRiskSceneDetailOp  extends CMISOperation {

	private final String sceneModel = "PrdPvRiskScene";
	private final String relModel = "PrdPvRiskItemRel";
	private final String itemModel = "PrdPvRiskItem";
	private boolean updateCheck = false;

	/**
	 * 流程节点查看页面
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.sceneModel, context, connection);
			}
			String prevent_id = (String)context.getDataValue("prevent_id");
			String wfid = (String)context.getDataValue("wfid");//此处为流程标识
			
			if(prevent_id == null || prevent_id.trim().length() == 0){
				throw new EMPException("获取方案编号失败！");
			}
			if(wfid == null || wfid.trim().length() == 0){
				throw new EMPException("获取流程标识失败！");
			}
			
			String conndition = " where prevent_id='"+prevent_id+"' and wfid='"+wfid+"' ";
			
			/** 通过流程字典转换流程标识为流程ID */
			WorkflowServiceInterface wfi = null;
			String wfId = "";
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			String orgid = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			List<WFIVO> wfivoList = wfi.getWFNameList(currentUserId, orgid, null, connection);
			
			for(WFIVO vo : wfivoList) {
				String wfSign = vo.getWfSign();
				if(wfid.equals(wfSign)){
					wfId = vo.getWfId();
				}
			}
			if(wfId != null && wfId.trim().length() != 0){
				/** 通过流程字典项封装生成流程节点字典项,由于产品模块加载时已经将流程节点封装成字典项，规则为：流程名_FLOWNODE_TYPE，所以此处直接引用 */
				IndexedCollection nodeIColl = (IndexedCollection)((KeyedCollection)context.getDataElement("dictColl")).getDataElement(wfId+"_FLOWNODE_TYPE");
				nodeIColl.setName("FLOW_SCENE_ID");
				this.putDataElement2Context(nodeIColl, context);
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			
			/** 设置场景基本信息 */
			KeyedCollection retrunKColl = new KeyedCollection();
			retrunKColl.addDataField("prevent_id", prevent_id);
			retrunKColl.addDataField("wfid", wfId);
			retrunKColl.addDataField("scene_id", "");
			retrunKColl.setName("PrdPvRiskScene");
			this.putDataElement2Context(retrunKColl, context);
			
			/** 
			 * 通过方案编号以及流程编号查询
			 * 1.查询关联中可配置的拦截信息
			 * 2.查询场景中已经配置的拦截信息
			 * 3.统一封装返回
			 */
			IndexedCollection returnIColl = new IndexedCollection();
			/** 查询出流程配置的不同的拦截方案 */
			//IndexedCollection sceneIColl = dao.queryList(sceneModel, conndition, connection);
			RiskManageComponent riskManageComponent = (RiskManageComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("RiskManage", context, connection);
			IndexedCollection sceneIColl = riskManageComponent.doSqlExecute("select distinct(item_id),prevent_id,wfid,item_name from prd_pv_risk_scene "+conndition,connection);
			String itemList = "";
			if(sceneIColl != null && sceneIColl.size() > 0){
				for(int i=0;i<sceneIColl.size();i++){
					KeyedCollection senKColl = new KeyedCollection();
					KeyedCollection insertKColl = (KeyedCollection)sceneIColl.get(i);
					String item_id = (String)insertKColl.getDataValue("item_id");
					KeyedCollection itemKColl = dao.queryDetail(itemModel, item_id, connection);
					senKColl.addDataField("item_id", item_id);
					senKColl.addDataField("item_name", insertKColl.getDataValue("item_name"));
					senKColl.addDataField("used_ind", itemKColl.getDataValue("used_ind"));
					senKColl.addDataField("risk_level", "1");
					senKColl.addDataField("is_check", "1");
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
					relKColl.addDataField("item_id", item_id);
					relKColl.addDataField("item_name", itemKColl.getDataValue("item_name"));
					relKColl.addDataField("used_ind", itemKColl.getDataValue("used_ind"));
					relKColl.addDataField("risk_level", "");//未选中的默认为""
					relKColl.addDataField("is_check", "");
					returnIColl.add(relKColl);
				}
			}
			returnIColl.setName("PrdPvRiskSceneList");
			this.putDataElement2Context(returnIColl, context);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
