package com.yucheng.cmis.biz01line.lmt.op.lmtintbnkapp.lmtintbnkappdetails;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;	

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteLmtIntbnkAppLmtIntbnkAppDetailsRecordOp extends CMISOperation {
	
	private final String modelId = "LmtIntbnkAppDetails";
	
	private final String serno_name = "serno";
	private final String crd_item_id_name = "crd_item_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			String crd_item_id_value = null;
			try {
				crd_item_id_value = (String)context.getDataValue(crd_item_id_name);
			} catch (Exception e) {}
			if(crd_item_id_value == null || crd_item_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+crd_item_id_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("serno",serno_value);
			pkMap.put("crd_item_id",crd_item_id_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Record Count: " + count);
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
