package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateRLmtGuarContRecordOp extends CMISOperation {
	
	private final String modelId = "RLmtGuarCont";
	private final String modelIdApp = "RLmtAppGuarCont";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			KeyedCollection kCollApp = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			
			try {
				kCollApp = (KeyedCollection)context.getDataElement(modelIdApp);
			} catch (Exception e) {}
			
			if((kColl == null || kColl.size() == 0)&&(kCollApp == null || kCollApp.size() == 0))
				throw new EMPJDBCException("The values to update["+modelId+"]["+modelIdApp+"] cannot be empty!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			int count = 0;
			if(kColl == null || kColl.size() == 0){
				count=dao.update(kCollApp, connection);
			}else{
				count=dao.update(kColl, connection);
			}
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			context.put("flag", PUBConstant.SUCCESS);
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
