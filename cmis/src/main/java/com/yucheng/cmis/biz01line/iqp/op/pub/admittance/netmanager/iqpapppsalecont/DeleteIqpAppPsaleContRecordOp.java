package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpapppsalecont;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpAppPsaleContRecordOp extends CMISOperation {

	private final String modelId = "IqpAppPsaleCont";
	

	private final String psale_cont_name = "psale_cont";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String psale_cont_value = null;
			String serno = null;
			try {
				psale_cont_value = (String)context.getDataValue(psale_cont_name);
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(psale_cont_value == null || psale_cont_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+psale_cont_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("psale_cont", psale_cont_value);
			map.put("serno", serno);
			int count=dao.deleteByPks(modelId, map, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
