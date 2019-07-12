package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteRLmtGuarInfoRecordOp extends CMISOperation {

	private final String modelId = "RLmtGuarInfo";
	
	private final String limit_code = "limit_code";
	private final String guaranty_no_name = "guaranty_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);
			String limit_code_value = null;
			try {
				limit_code_value = (String)context.getDataValue(limit_code);
			} catch (Exception e) {}
			if(limit_code_value == null || limit_code_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+limit_code+"] cannot be null!");
				
			String guaranty_no_value = null;
			try {
				guaranty_no_value = (String)context.getDataValue(guaranty_no_name);
			} catch (Exception e) {}
			if(guaranty_no_value == null || guaranty_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+guaranty_no_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("limit_code",limit_code_value);
			pkMap.put("guaranty_no",guaranty_no_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag", "suc");
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
