package com.yucheng.cmis.biz01line.psp.op.pspcheckcatalog;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeletePspCheckCatalogRecordOp extends CMISOperation {
	private final String modelId = "PspCheckCatalog";
	private final String catalog_id_name = "catalog_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String catalog_id_value = null;
			try {
				catalog_id_value = (String)context.getDataValue(catalog_id_name);
			} catch (Exception e) {}
			if(catalog_id_value == null || catalog_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+catalog_id_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, catalog_id_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			//dao.deleteAllByPks("PspCatItemRel", pk_values, connection);
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
