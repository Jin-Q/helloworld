package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpdepotagr;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpDepotAgrRecordOp extends CMISOperation {

	private final String modelId = "IqpDepotAgr";
	

	private final String depot_agr_no_name = "depot_agr_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String depot_agr_no_value = null;
			try {
				depot_agr_no_value = (String)context.getDataValue(depot_agr_no_name);
			} catch (Exception e) {}
			if(depot_agr_no_value == null || depot_agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+depot_agr_no_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, depot_agr_no_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag", "success");
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
