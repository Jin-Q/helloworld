package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;
import java.text.SimpleDateFormat;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
/**
 * 
*@author lisj
*@time 2015-5-19
*@description TODO 需求编号：XD150504034 贷后管理常规检查任务改造（任务展期操作）
*@version v1.0
*
 */
public class DeferPspCheckTaskRecordOp extends CMISOperation {
	
	private final String modelId = "PspCheckTask";
	
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
			String task_id = (String)kColl.getDataValue("task_id");//任务号
			String task_request_time = (String)kColl.getDataValue("task_request_time");//任务要求完成时间
			KeyedCollection temp = dao.queryDetail(modelId, task_id, connection);
			String task_create_time  = (String) temp.getDataValue("task_create_date");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//检验任务完成日期不得早于任务生成日期
			if((sdf.parse(task_request_time)).before(sdf.parse(task_create_time))){
				context.put("flag", "wrongdate");
			}else{
				temp.setDataValue("task_request_time", task_request_time);
				dao.update(temp, connection);
				context.put("flag", PUBConstant.SUCCESS);
			}
		}catch (EMPException ee) {
			context.put("flag", PUBConstant.FAIL);
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
