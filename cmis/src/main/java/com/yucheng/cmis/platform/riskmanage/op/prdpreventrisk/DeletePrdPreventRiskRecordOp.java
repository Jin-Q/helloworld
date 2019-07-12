package com.yucheng.cmis.platform.riskmanage.op.prdpreventrisk;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.riskmanage.component.RiskManageComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeletePrdPreventRiskRecordOp extends CMISOperation {
	
	private final String modelId = "PrdPreventRisk";
	private final String relModel = "PrdPvRiskItemRel";
	private final String senModel = "PrdPvRiskScene";
	private final String prevent_id_name = "prevent_id";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String prevent_id_value = null;
			try {
				prevent_id_value = (String)context.getDataValue(prevent_id_name);
			} catch (Exception e) {}
			if(prevent_id_value == null || prevent_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+prevent_id_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, prevent_id_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Record Count: " + count);
			}
			RiskManageComponent riskManageComponent = (RiskManageComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("RiskManage", context, connection);
			riskManageComponent.doDeleteSqlExecute("delete from prd_pv_risk_item_rel where prevent_id='"+prevent_id_value+"'", connection);
			riskManageComponent.doDeleteSqlExecute("delete from prd_pv_risk_scene where prevent_id='"+prevent_id_value+"'", connection);
			
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
