package com.yucheng.cmis.biz01line.psp.op.pspcapusemonitor;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddPspCapUseMonitorRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "PspCapUseMonitor";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			String task_id = "";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			try {
				task_id = (String)context.getDataValue("task_id");
			} catch (Exception e) {}
			if(task_id == null || task_id.length() == 0)
				throw new EMPJDBCException("贷后任务编号不能为空!");
			//add a record
			kColl.put("task_id", task_id);
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
			context.addDataField("flag", "success");
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
