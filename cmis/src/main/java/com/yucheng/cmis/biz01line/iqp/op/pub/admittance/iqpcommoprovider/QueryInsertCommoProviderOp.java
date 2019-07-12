package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpcommoprovider;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryInsertCommoProviderOp extends CMISOperation {

	private final String modelId = "IqpCommoProvider";
	

	private final String mort_catalog_no_name = "mort_catalog_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String mort_catalog_no_value = null;
			try {
				mort_catalog_no_value = (String)context.getDataValue(mort_catalog_no_name);
			} catch (Exception e) {}
			if(mort_catalog_no_value == null || mort_catalog_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+mort_catalog_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kc = dao.queryDetail(modelId, mort_catalog_no_value, connection);
			kc.setDataValue("status", "02");
			dao.update(kc, connection);
			
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
