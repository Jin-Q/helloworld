package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpapppsalecontgood;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateIqpAppPsaleContGoodRecordOp extends CMISOperation {
	

	private final String modelId = "IqpAppPsaleContGood";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
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
            context.put("flag", "success");
            context.put("msg", "");
		}catch (EMPException ee) {
			context.put("flag", "error");
	        context.put("msg", ee.getMessage());
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
