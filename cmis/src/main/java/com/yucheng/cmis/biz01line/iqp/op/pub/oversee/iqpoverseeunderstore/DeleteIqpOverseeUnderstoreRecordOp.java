package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpoverseeunderstore;

import java.net.URLDecoder;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpOverseeUnderstoreRecordOp extends CMISOperation {

	private final String modelId = "IqpOverseeUnderstore";
	

	private final String store_id_name = "store_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String store_id_value = null;
			try {
				store_id_value = (String)context.getDataValue(store_id_name);
			} catch (Exception e) {}
			if(store_id_value == null || store_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+store_id_name+"] cannot be null!");
				
			//中文转码
			store_id_value = URLDecoder.decode(store_id_value,"UTF-8");

			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, store_id_value, connection);
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
