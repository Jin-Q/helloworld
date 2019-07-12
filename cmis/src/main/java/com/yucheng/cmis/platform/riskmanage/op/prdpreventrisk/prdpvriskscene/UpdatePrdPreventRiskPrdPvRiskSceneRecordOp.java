package com.yucheng.cmis.platform.riskmanage.op.prdpreventrisk.prdpvriskscene;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.riskmanage.component.RiskManageComponent;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class UpdatePrdPreventRiskPrdPvRiskSceneRecordOp extends CMISOperation {
	private final String sceneModel = "PrdPvRiskScene";
	private final String itemModel = "PrdPvRiskItem";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			/** 获取场景ID、流程、流程节点 */
			String prevent_id = (String)context.getDataValue("prevent_id");
			String wfid = (String)context.getDataValue("wfid");
			String scene_id = (String)context.getDataValue("scene_id");
			if(prevent_id == null || prevent_id.trim().length() == 0){
				throw new EMPException("获取场景ID失败！");
			}
			if(wfid == null || wfid.trim().length() == 0){
				throw new EMPException("获取流程ID失败！");
			}
			if(scene_id == null || scene_id.trim().length() == 0){
				throw new EMPException("获取流程节点ID失败！");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			
			/** 获取拦截项目集合、拦截级别集合，全部以,好分隔 */
			String itemList = (String)context.getDataValue("itemList");
			String levelList = (String)context.getDataValue("levelList");
			if(itemList != null && itemList.trim().length() > 0){
				WorkflowServiceInterface wfi = null;
				String wfSign = "";
				String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
				String orgid = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
				wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
				List<WFIVO> wfivoList = wfi.getWFNameList(currentUserId, orgid, null, connection);
				
				for(WFIVO vo : wfivoList) {
					String wfId = vo.getWfId();
					if(wfId.equals(wfid)){
						wfSign = vo.getWfSign();
					}
				}
				String item = itemList.substring(0, itemList.length()-1);
				String level = levelList.substring(0, levelList.length()-1);
				if(item != null && item.length() > 0){
					/** 新增之前删除原来记录数 */
					RiskManageComponent riskManageComponent = (RiskManageComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("RiskManage", context, connection);
					riskManageComponent.doDeleteSqlExecute("delete from prd_pv_risk_scene where prevent_id='"+prevent_id+"' and wfid='"+wfSign+"' and scene_id='"+scene_id+"'", connection);
					
					String itemArr[] = item.split(",");
					String levelArr[] = level.split(",");
					String sceneArr[] = scene_id.split(",");
					for(int i=0;i<sceneArr.length;i++){
						String scene_id_value = sceneArr[i];
						/** 通过item_id,查询item_name */
						for(int j=0;j<itemArr.length;j++){
							String item_id = itemArr[j];
							String risk_level = levelArr[j];
							if(risk_level == null){
								risk_level = "";
							}
							KeyedCollection itemKColl = dao.queryDetail(itemModel, item_id, connection);
							String item_name = (String)itemKColl.getDataValue("item_name");
							KeyedCollection insertKColl = new KeyedCollection();
							insertKColl.addDataField("prevent_id", prevent_id);
							insertKColl.addDataField("wfid", wfSign);
							insertKColl.addDataField("scene_id", scene_id_value);
							insertKColl.addDataField("item_id", item_id);
							insertKColl.addDataField("item_name", item_name);
							insertKColl.addDataField("risk_level", risk_level);
							insertKColl.setName(sceneModel);
							dao.insert(insertKColl, connection);
						}
					}
				}
			}
			context.addDataField("flag", "success");
		}catch(Exception e){
			context.addDataField("flag", "failed");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
