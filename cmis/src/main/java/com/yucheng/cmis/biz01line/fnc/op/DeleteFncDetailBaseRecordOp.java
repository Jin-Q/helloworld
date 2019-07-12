package com.yucheng.cmis.biz01line.fnc.op;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteFncDetailBaseRecordOp extends CMISOperation {
	
	private final String modelId = "FncDetailBase";

	private final String pk_name = "pk";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String pk_value = null;
		String cus_id = null;
		try{
			connection = this.getConnection(context);
			
			
			
			try {
				pk_value = (String)context.getDataValue(pk_name);
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}
			if(pk_value == null || pk_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_name+"] cannot be null!");


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, pk_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Record Count: " + count);
			}
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.setDataValue("cus_id", cus_id);
		return "0";
	}
}
