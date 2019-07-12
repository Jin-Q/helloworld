package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpappdesbuyplan;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpAppDesbuyPlanRecordOp extends CMISOperation {

	private final String modelId = "IqpAppDesbuyPlan";
	

	private final String desgoods_plan_no_name = "desgoods_plan_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String desgoods_plan_no_value = null;
			String serno = null;
			try {
				desgoods_plan_no_value = (String)context.getDataValue(desgoods_plan_no_name);
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(desgoods_plan_no_value == null || desgoods_plan_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+desgoods_plan_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("desgoods_plan_no", desgoods_plan_no_value);
			map.put("serno", serno);
			int count=dao.deleteByPks(modelId, map, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag", "success");
			
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
