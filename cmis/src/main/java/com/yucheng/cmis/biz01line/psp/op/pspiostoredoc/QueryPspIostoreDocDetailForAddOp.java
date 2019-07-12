package com.yucheng.cmis.biz01line.psp.op.pspiostoredoc;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2015-1-21
*@description 需求编号：【XD141230092】 贷后管理需求（首次检查、零售常规）
*@version v1.0
*
 */
public class QueryPspIostoreDocDetailForAddOp  extends CMISOperation {
	
	//private final String modelId = "PspIostoreDoc";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String cus_id = null;
			String task_id = null;
			try {
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}
			if(cus_id == null || cus_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id+"] cannot be null!");
			
			try {
				task_id = (String)context.getDataValue("task_id");
			} catch (Exception e) {}
			if(task_id == null || task_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+task_id+"] cannot be null!");
			
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection PCT = dao.queryDetail("PspCheckTask", task_id, connection);
			String check_freq = (String) PCT.getDataValue("check_freq");
			context.addDataField("check_freq", check_freq);

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
