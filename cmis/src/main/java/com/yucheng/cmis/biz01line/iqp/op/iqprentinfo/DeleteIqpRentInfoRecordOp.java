package com.yucheng.cmis.biz01line.iqp.op.iqprentinfo;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpRentInfoRecordOp extends CMISOperation {

	private final String modelId = "IqpRentInfo";
	

	private final String rent_serno_name = "rent_serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String rent_serno_value = null;
			try {
				rent_serno_value = (String)context.getDataValue(rent_serno_name);
			} catch (Exception e) {}
			if(rent_serno_value == null || rent_serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+rent_serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, rent_serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag", "success");
			context.addDataField("message", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
			context.addDataField("message", ee.getMessage());
			try{
				connection.rollback();
			}catch(SQLException e){
				e.printStackTrace();
			}
			ee.printStackTrace();
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
