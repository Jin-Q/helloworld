package com.yucheng.cmis.biz01line.psp.op.pspcheckitem;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeletePspCheckItemRecordOp extends CMISOperation {

	private final String modelId = "PspCheckItem";
	

	private final String item_id_name = "item_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String item_id_value = null;
			try {
				item_id_value = (String)context.getDataValue(item_id_name);
			} catch (Exception e) {}
			if(item_id_value == null || item_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+item_id_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, item_id_value, connection);
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
