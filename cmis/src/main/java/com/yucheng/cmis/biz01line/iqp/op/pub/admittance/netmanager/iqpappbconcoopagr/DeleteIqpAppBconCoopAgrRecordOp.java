package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpappbconcoopagr;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpAppBconCoopAgrRecordOp extends CMISOperation {

	private final String modelId = "IqpAppBconCoopAgr";
	

	private final String coop_agr_no_name = "coop_agr_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String coop_agr_no_value = null;
			String serno = null;
			try {
				coop_agr_no_value = (String)context.getDataValue(coop_agr_no_name);
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(coop_agr_no_value == null || coop_agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+coop_agr_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			HashMap<String,String> mapIqp = new HashMap<String,String>();
			mapIqp.put("serno", serno);
			mapIqp.put("coop_agr_no", coop_agr_no_value);
			int count=dao.deleteByPks(modelId, mapIqp, connection);
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
