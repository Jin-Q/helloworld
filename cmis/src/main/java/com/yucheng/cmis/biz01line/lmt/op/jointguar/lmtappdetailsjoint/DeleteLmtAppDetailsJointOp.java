package com.yucheng.cmis.biz01line.lmt.op.jointguar.lmtappdetailsjoint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteLmtAppDetailsJointOp extends CMISOperation {

	private final String modelId = "LmtAppDetails";

	private final String limit_code_name = "limit_code";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String limit_code_value = null;
			try {
				limit_code_value = (String)context.getDataValue(limit_code_name);
			} catch (Exception e) {}
			if(limit_code_value == null || limit_code_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+limit_code_name+"] cannot be null!");

			
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, limit_code_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
			context.addDataField("flag","success");
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
