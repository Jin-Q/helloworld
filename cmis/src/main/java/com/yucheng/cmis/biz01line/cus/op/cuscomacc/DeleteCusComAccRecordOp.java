package com.yucheng.cmis.biz01line.cus.op.cuscomacc;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteCusComAccRecordOp extends CMISOperation {

	private final String modelId = "CusComAcc";
	

	private final String cus_id_name = "cus_id";
	private final String acc_no_name = "acc_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
				
			String acc_no_value = null;
			try {
				acc_no_value = (String)context.getDataValue(acc_no_name);
			} catch (Exception e) {}
			if(acc_no_value == null || acc_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+acc_no_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cus_id",cus_id_value);
			pkMap.put("acc_no",acc_no_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				context.addDataField("flag", "");
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag", "删除成功");
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
