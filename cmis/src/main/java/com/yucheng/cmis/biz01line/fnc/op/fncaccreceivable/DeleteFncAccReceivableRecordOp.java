package com.yucheng.cmis.biz01line.fnc.op.fncaccreceivable;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteFncAccReceivableRecordOp extends CMISOperation {

	private final String modelId = "FncAccReceivable";
	

	private final String pk_id_name = "pk_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);


			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String pk_id_value = null;
			try {
				pk_id_value = (String)context.getDataValue(pk_id_name);
			} catch (Exception e) {}
			if(pk_id_value == null || pk_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, pk_id_value, connection);
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
