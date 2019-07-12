package com.yucheng.cmis.biz01line.lmt.op.lmtintbnkapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.exception.AsynException;

public class DeleteLmtIntbnkAppRecordOp extends CMISOperation {
	
	private final String modelId = "LmtIntbnkApp";

	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = CMISMessage.SUCCESS;
		try{
			connection = this.getConnection(context);
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
//			if(1==1){
//				throw new EMPException("删除异常操作演示");
//			}
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Record Count: " + count);
			}
		} catch(Exception e){
			throw new AsynException(e.getMessage());
		} finally {
			context.put("flag", flag);
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
