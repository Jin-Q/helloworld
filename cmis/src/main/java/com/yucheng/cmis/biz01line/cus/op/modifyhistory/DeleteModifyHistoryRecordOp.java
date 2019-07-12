package com.yucheng.cmis.biz01line.cus.op.modifyhistory;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteModifyHistoryRecordOp extends CMISOperation {

	private final String modelId = "ModifyHistory";
	

	private final String key_id_name = "key_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String key_id_value = null;
			try {
				key_id_value = (String)context.getDataValue(key_id_name);
			} catch (Exception e) {}
			if(key_id_value == null || key_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+key_id_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, key_id_value, connection);
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
