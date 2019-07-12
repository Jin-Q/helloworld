package com.yucheng.cmis.biz01line.mort.mortguarantysuddeninfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteMortGuarantySuddenInfoRecordOp extends CMISOperation {

	private final String modelId = "MortGuarantySuddenInfo";
	

	private final String accident_no_name = "accident_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);


			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String accident_no_value = null;
			try {
				accident_no_value = (String)context.getDataValue(accident_no_name);
			} catch (Exception e) {}
			if(accident_no_value == null || accident_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+accident_no_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, accident_no_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag", "success");
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
