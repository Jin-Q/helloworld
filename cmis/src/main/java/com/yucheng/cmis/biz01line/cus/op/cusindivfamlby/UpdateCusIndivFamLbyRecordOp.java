package com.yucheng.cmis.biz01line.cus.op.cusindivfamlby;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateCusIndivFamLbyRecordOp extends CMISOperation {
	
	private final String modelId = "CusIndivFamLby";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			 kColl.setDataValue("last_upd_id", context.getDataValue("currentUserId"));
			 kColl.setDataValue("last_upd_date", context.getDataValue("OPENDAY"));

			TableModelDAO dao = this.getTableModelDAO(context);

			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}

			flag = "修改成功";
		}catch (EMPException ee) {
			flag = "修改失败";
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag",flag);
		return "0";
	}
}
