package com.yucheng.cmis.biz01line.iqp.op.iqpbailinfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpBailInfoRecordOp extends CMISOperation {

	private final String modelId = "IqpBailInfo";
	

	private final String serno_name = "serno";
	private final String bail_acct_no_name = "bail_acct_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);


			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String bail_acct_no_value = null;
			try {
				bail_acct_no_value = (String)context.getDataValue(bail_acct_no_name);
			} catch (Exception e) {}
			if(bail_acct_no_value == null || bail_acct_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+bail_acct_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, bail_acct_no_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag", "success");
			//context.addDataField("serno", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
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
