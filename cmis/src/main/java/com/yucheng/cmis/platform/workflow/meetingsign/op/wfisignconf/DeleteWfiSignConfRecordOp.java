package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisignconf;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteWfiSignConfRecordOp extends CMISOperation {

	private final String modelId = "WfiSignConf";
	

	private final String sign_id_name = "sign_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String sign_id_value = null;
			try {
				sign_id_value = (String)context.getDataValue(sign_id_name);
			} catch (Exception e) {}
			if(sign_id_value == null || sign_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+sign_id_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, sign_id_value, connection);
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
