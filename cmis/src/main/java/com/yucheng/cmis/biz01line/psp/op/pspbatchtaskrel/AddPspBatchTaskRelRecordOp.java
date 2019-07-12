package com.yucheng.cmis.biz01line.psp.op.pspbatchtaskrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2015-1-13
*@description 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量）
*@version v1.0
*
 */
public class AddPspBatchTaskRelRecordOp extends CMISOperation {
	
	private final String modelId = "PspBatchTaskRel";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);
			String major_task_id = kColl.getDataValue("major_task_id").toString();
			String condition = "where task_id='"+major_task_id+"'";
			IndexedCollection iColl = dao.queryList("PspCheckTask", null, condition, connection);
			if(iColl.size()==0){
				KeyedCollection PCT = new KeyedCollection();
				PCT.setName("PspCheckTask");
				PCT.put("task_id", kColl.getDataValue("major_task_id").toString());
				PCT.put("check_type", "02");
				PCT.put("manager_id",kColl.getDataValue("manager_id").toString());
				PCT.put("manager_br_id",kColl.getDataValue("manager_br_id").toString());
				PCT.put("task_type",kColl.getDataValue("task_type").toString());
				PCT.put("batch_task_type", kColl.getDataValue("batch_task_type").toString());
				PCT.put("input_id",kColl.getDataValue("input_id").toString());
				PCT.put("input_br_id",kColl.getDataValue("input_br_id").toString());
				PCT.put("approve_status","000");
				dao.insert(PCT, connection);
			}
			this.putDataElement2Context(kColl, context);
			
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
