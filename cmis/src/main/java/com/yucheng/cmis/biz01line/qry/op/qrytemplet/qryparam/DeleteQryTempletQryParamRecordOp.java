package com.yucheng.cmis.biz01line.qry.op.qrytemplet.qryparam;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;	

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteQryTempletQryParamRecordOp extends CMISOperation {
	
	private final String modelId = "QryParam";
	
	private final String temp_no_name = "temp_no";
	private final String param_no_name = "param_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			
			String temp_no_value = null;
			try {
				temp_no_value = (String)context.getDataValue(temp_no_name);
			} catch (Exception e) {}
			if(temp_no_value == null || temp_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+temp_no_name+"] cannot be null!");
			String param_no_value = null;
			try {
				param_no_value = (String)context.getDataValue(param_no_name);
			} catch (Exception e) {}
			if(param_no_value == null || param_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+param_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("temp_no",temp_no_value);
			pkMap.put("param_no",param_no_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
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
		return "0";
	}
}
