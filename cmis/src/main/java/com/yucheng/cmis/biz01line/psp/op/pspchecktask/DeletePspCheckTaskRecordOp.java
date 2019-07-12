package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeletePspCheckTaskRecordOp extends CMISOperation {

	private final String modelId = "PspCheckTask";
	

	private final String task_id_name = "task_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String task_id_value = null;
			try {
				task_id_value = (String)context.getDataValue(task_id_name);
			} catch (Exception e) {}
			if(task_id_value == null || task_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+task_id_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, task_id_value, connection);
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
