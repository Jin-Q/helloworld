package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpoverseecusinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpOverseeCusinfoRecordOp extends CMISOperation {

	private final String modelId = "IqpOverseeCusinfo";
	

	private final String cusinfo_id_name = "cusinfo_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String cusinfo_id_value = null;
			try {
				cusinfo_id_value = (String)context.getDataValue(cusinfo_id_name);
			} catch (Exception e) {}
			if(cusinfo_id_value == null || cusinfo_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cusinfo_id_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, cusinfo_id_value, connection);
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
