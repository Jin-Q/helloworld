package com.yucheng.cmis.biz01line.mort.cargo.mortcargorepllist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteMortCargoReplListRecordOp extends CMISOperation {

	private final String modelId = "MortCargoReplList";
	

	private final String cargo_id_name = "cargo_id";
	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String cargo_id_value = null;
			try {
				cargo_id_value = (String)context.getDataValue(cargo_id_name);
			} catch (Exception e) {}
			if(cargo_id_value == null || cargo_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cargo_id_name+"] cannot be null!");
				
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cargo_id",cargo_id_value);
			pkMap.put("serno",serno_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.put("flag","success");
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
