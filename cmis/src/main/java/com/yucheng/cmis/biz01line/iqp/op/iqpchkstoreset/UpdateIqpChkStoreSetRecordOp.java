package com.yucheng.cmis.biz01line.iqp.op.iqpchkstoreset;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateIqpChkStoreSetRecordOp extends CMISOperation {
	

	private final String modelId = "IqpChkStoreSet";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "success";
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}

		}catch (EMPException ee) {
			flag = "error";
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			context.addDataField("flag", flag);
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
