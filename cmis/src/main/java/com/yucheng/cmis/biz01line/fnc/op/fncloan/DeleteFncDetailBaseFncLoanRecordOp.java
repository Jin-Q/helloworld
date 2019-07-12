package com.yucheng.cmis.biz01line.fnc.op.fncloan;


import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;	

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteFncDetailBaseFncLoanRecordOp extends CMISOperation {
	
	private final String modelId = "FncLoan";
	
	private final String seq_name = "seq";
	private final String cus_id_name = "cus_id";
	private final String fnc_ym_name = "fnc_ym";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			
			String seq_value = null;
			try {
				seq_value = (String)context.getDataValue(seq_name);
			} catch (Exception e) {}
			if(seq_value == null || seq_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+seq_name+"] cannot be null!");
			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
			String fnc_ym_value = null;
			try {
				fnc_ym_value = (String)context.getDataValue(fnc_ym_name);
			} catch (Exception e) {}
			if(fnc_ym_value == null || fnc_ym_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+fnc_ym_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("seq",seq_value);
			pkMap.put("cus_id",cus_id_value);
			pkMap.put("fnc_ym",fnc_ym_value);
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
