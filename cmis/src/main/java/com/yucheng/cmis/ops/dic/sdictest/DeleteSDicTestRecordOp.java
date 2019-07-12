package com.yucheng.cmis.ops.dic.sdictest;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteSDicTestRecordOp extends CMISOperation {

	private final String modelId = "SDicTest";
	

	private final String enname_name = "enname";
	private final String opttype_name = "opttype";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String enname_value = null;
			try {
				enname_value = (String)context.getDataValue(enname_name);
			} catch (Exception e) {}
			if(enname_value == null || enname_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+enname_name+"] cannot be null!");
				
			String opttype_value = null;
			try {
				opttype_value = (String)context.getDataValue(opttype_name);
			} catch (Exception e) {}
			if(opttype_value == null || opttype_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+opttype_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("enname",enname_value);
			pkMap.put("opttype",opttype_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
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
