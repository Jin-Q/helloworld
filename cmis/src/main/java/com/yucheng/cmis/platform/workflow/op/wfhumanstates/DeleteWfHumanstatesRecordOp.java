package com.yucheng.cmis.platform.workflow.op.wfhumanstates;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteWfHumanstatesRecordOp extends CMISOperation {

	private final String modelId = "WfHumanstates";
	

	private final String pkey_name = "pkey";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String pkey_value = null;
			try {
				pkey_value = (String)context.getDataValue(pkey_name);
			} catch (Exception e) {}
			if(pkey_value == null || pkey_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pkey_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, pkey_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
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
