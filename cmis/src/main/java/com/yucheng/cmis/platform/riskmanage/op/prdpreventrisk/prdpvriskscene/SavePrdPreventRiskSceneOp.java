package com.yucheng.cmis.platform.riskmanage.op.prdpreventrisk.prdpvriskscene;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.riskmanage.component.RiskManageComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class SavePrdPreventRiskSceneOp extends CMISOperation {
	private static final String modelId = "PrdPvRiskScene";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String prevent_id = (String)context.getDataValue("prevent_id");
			String itemArr = (String)context.getDataValue("itemArr");
			String levelArr = (String)context.getDataValue("levelArr");
			
			if(levelArr != null && levelArr.trim().length() > 0){
				/** 删除原有拦截场景配置 */
				RiskManageComponent riskManageComponent = (RiskManageComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("RiskManage", context, connection);
				riskManageComponent.doDeleteSqlExecute("delete from prd_pv_risk_scene where prevent_id='"+prevent_id+"'", connection);
				
				levelArr = levelArr.substring(0, levelArr.length()-1);
				TableModelDAO dao = this.getTableModelDAO(context);
				String itemArray[] = itemArr.split(",");
				String levelArray[] = levelArr.split(",");
				for(int i=0;i<itemArray.length;i++){
					String item_id = itemArray[i];
					String risk_level = levelArray[i];
					if(risk_level == null){
						risk_level = "";
					}
					/** 通过项目编号获取项目信息 */
					KeyedCollection sceneKColl = new KeyedCollection();
					KeyedCollection itemKColl = dao.queryDetail("PrdPvRiskItem", item_id, connection);
					sceneKColl.addDataField("prevent_id", prevent_id);
					sceneKColl.addDataField("item_id", item_id);
					sceneKColl.addDataField("scene_id", "000");
					sceneKColl.addDataField("wfid", "000");
					sceneKColl.addDataField("risk_level", risk_level);
					sceneKColl.addDataField("item_name", itemKColl.getDataValue("item_name"));
					sceneKColl.setName("PrdPvRiskScene");
					dao.insert(sceneKColl, connection);
				}
			}
			context.addDataField("flag", "success");
		} catch (Exception e) {
			context.addDataField("flag", "failed");
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
