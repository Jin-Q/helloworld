package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtbatchcorre;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteLmtBatchCorreRecordOp extends CMISOperation {

	private final String modelId = "LmtBatchCorre";
	

	private final String batch_cus_no_name = "batch_cus_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);


			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String batch_cus_no_value = null;
			try {
				batch_cus_no_value = (String)context.getDataValue(batch_cus_no_name);
			} catch (Exception e) {}
			if(batch_cus_no_value == null || batch_cus_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+batch_cus_no_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, batch_cus_no_value, connection);
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
