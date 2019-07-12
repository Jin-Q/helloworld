package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpappmortdetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpAppMortDetailRecordOp extends CMISOperation {

	private final String modelId = "IqpAppMortDetail";
	

	private final String catalog_no_name = "catalog_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String catalog_no_value = null;
			try {
				catalog_no_value = (String)context.getDataValue(catalog_no_name);
			} catch (Exception e) {}
			if(catalog_no_value == null || catalog_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+catalog_no_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, catalog_no_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag","success");
			
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
