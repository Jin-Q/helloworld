package com.yucheng.cmis.biz01line.psp.op.pspzxqkloan;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
//贷后管理系统改造（常规检查）    XD141222090     modefied by zhaoxp  2014-12-26
public class QueryPspZxqkLoanForAddOp  extends CMISOperation {
	
	private final String modelId = "PspZxqkLoan";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			
			String cus_id = null;
			try {
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}
			if(cus_id == null || cus_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id+"] cannot be null!");

			
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = " where cus_id = '"+cus_id+"' order by task_id desc ";
			KeyedCollection kColl = dao.queryFirst(modelId, null, condition, connection);
			
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
