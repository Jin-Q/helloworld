package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpapppsalecontgood;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpAppPsaleContGoodRecordOp extends CMISOperation {

	private final String modelId = "IqpAppPsaleContGood";
	

	private final String psale_cont_name = "psale_cont";
	private final String commo_name_name = "commo_name";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String psale_cont_value = null;
			try {
				psale_cont_value = (String)context.getDataValue(psale_cont_name);
			} catch (Exception e) {}
			if(psale_cont_value == null || psale_cont_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+psale_cont_name+"] cannot be null!");
				
			String commo_name_value = null;
			try {
				commo_name_value = (String)context.getDataValue(commo_name_name);
			} catch (Exception e) {}
			if(commo_name_value == null || commo_name_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+commo_name_name+"] cannot be null!");
			
			String serno = null;
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(commo_name_value == null || commo_name_value.length() == 0)
				throw new EMPJDBCException("The value of serno cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("psale_cont",psale_cont_value);
			pkMap.put("commo_name",commo_name_value);
			pkMap.put("serno",serno);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.put("flag", "success");
			context.put("msg", "");
		}catch (EMPException ee) {
			context.put("flag", "error");
			context.put("msg", ee.getMessage());
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
