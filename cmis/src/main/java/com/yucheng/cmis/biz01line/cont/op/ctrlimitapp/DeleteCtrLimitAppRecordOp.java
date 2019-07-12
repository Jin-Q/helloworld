package com.yucheng.cmis.biz01line.cont.op.ctrlimitapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteCtrLimitAppRecordOp extends CMISOperation {

	private final String modelId = "CtrLimitApp";
	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String serno_value = null;
			String appType = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				appType = (String)context.getDataValue("app_type");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
			SqlClient.delete("deleteCtrLimitLmtRelBySerno", serno_value, connection);
			
			/** 删除从表信息，临时表信息,如果是变更，则删除变更临时表信息 *//*
			if("01".equals(appType)){
				SqlClient.delete("deleteCtrLimitLmtRelBySerno", serno_value, connection);
			}else {
				SqlClient.delete("deleteCtrLimitLmtRelTempBySerno", serno_value, connection);
			}*/
			/** 根据业务流水号删除客户经理绩效表信息 */
			SqlClient.delete("deleteCusManagerBySerno", serno_value, connection);
			
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
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
