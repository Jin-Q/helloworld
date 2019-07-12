package com.yucheng.cmis.biz01line.lmt.op.lmtagrfinguar;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteLmtAgrFinGuarRecordOp extends CMISOperation {

	private final String modelId = "LmtAgrFinGuar";
	

	private final String agr_no_name = "agr_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String agr_no_value = null;
			try {
				agr_no_value = (String)context.getDataValue(agr_no_name);
			} catch (Exception e) {}
			if(agr_no_value == null || agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+agr_no_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, agr_no_value, connection);
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
