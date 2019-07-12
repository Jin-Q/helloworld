package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteMortCommenOwnerOp extends CMISOperation {

	private final String modelId = "MortCommenOwner";
	

	private final String commen_owner_no_name = "commen_owner_no";
	private final String guaranty_no_name = "guaranty_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);


			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String commen_owner_no_value = null;
			try {
				commen_owner_no_value = (String)context.getDataValue(commen_owner_no_name);
			} catch (Exception e) {}
			if(commen_owner_no_value == null || commen_owner_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+commen_owner_no_name+"] cannot be null!");
				
			String guaranty_no_value = null;
			try {
				guaranty_no_value = (String)context.getDataValue(guaranty_no_name);
			} catch (Exception e) {}
			if(guaranty_no_value == null || guaranty_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+guaranty_no_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("commen_owner_no",commen_owner_no_value);
			pkMap.put("guaranty_no",guaranty_no_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				if(context.containsKey("delet")){
					context.setDataValue("delet", "false");
				}else{
					context.addDataField("delet", "false");
				}
			}else{
				if(context.containsKey("delet")){
					context.setDataValue("delet", "true");
				}else{
					context.addDataField("delet", "true");
				}
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
