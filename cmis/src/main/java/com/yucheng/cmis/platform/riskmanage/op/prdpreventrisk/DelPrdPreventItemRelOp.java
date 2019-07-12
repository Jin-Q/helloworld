package com.yucheng.cmis.platform.riskmanage.op.prdpreventrisk;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.riskmanage.component.RiskManageComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DelPrdPreventItemRelOp extends CMISOperation {
	private static final String relModel = "PrdPvRiskItemRel";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String prevent_id = (String)context.getDataValue("prevent_id");
			if(prevent_id == null || prevent_id.trim().length() == 0){
				throw new EMPException("删除拦截项获取拦截方案ID失败！");
			}
			String relArr = (String)context.getDataValue("relArr");
			if(relArr == null || relArr.trim().length() == 0){
				throw new EMPException("删除拦截项获取拦截项ID失败！");
			}
			RiskManageComponent riskManageComponent = (RiskManageComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("RiskManage", context, connection);
			TableModelDAO dao = this.getTableModelDAO(context);
			String itemIdArr[] = relArr.split(",");
			for(int i=0;i<itemIdArr.length;i++){
				String item_id = itemIdArr[i];
				Map<String, String> param = new HashMap();
				param.put("prevent_id", prevent_id);
				param.put("item_id", item_id);
				dao.deleteByPks(relModel, param, connection);
				riskManageComponent.doDeleteSqlExecute("delete from prd_pv_risk_item_rel where prevent_id='"+prevent_id+"' and item_id='"+item_id+"'", connection);
				riskManageComponent.doDeleteSqlExecute("delete from prd_pv_risk_scene where prevent_id='"+prevent_id+"' and item_id='"+item_id+"'", connection);
			}
			context.addDataField("flag", "success");
		} catch (Exception e) {
			context.addDataField("flag", "falied");
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
