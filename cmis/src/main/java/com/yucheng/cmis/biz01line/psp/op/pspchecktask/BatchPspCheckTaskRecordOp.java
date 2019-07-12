package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.TimeUtil;
/**
 * 
*@author yezm
*@time 2015-7-23
*@description TODO 需求编号：XD150625045 贷后管理常规检查任务改造 （否决恢复操作）
*@version v1.0
*
 */ 
public class BatchPspCheckTaskRecordOp extends CMISOperation {


		private final String modelId = "PspCheckTask";

		public String doExecute(Context context) throws EMPException {
			Connection connection = null;
			try{
				connection = this.getConnection(context);	
				String task_id ="";
				String currentUserId ="";
				String approve_status ="";
				try {
					task_id = (String)context.getDataValue("task_id");
					currentUserId = (String)context.getDataValue("currentUserId");
					
				} catch (Exception e) {}
				TableModelDAO dao = this.getTableModelDAO(context);
				//String condition=" where task_id in (select pk_value from wfi_join_his where  wfi_status='998' and pk_value ='"+task_id+"' )";
				//IndexedCollection iColl = dao.queryList("PspRejectBatchLog", condition, connection);
				String condition=" where  wfi_status='998' and pk_value ='"+task_id+"'";
				IndexedCollection iColl = dao.queryList("WfiJoinHis", condition, connection);
				if(iColl.size()>0){
					context.put("flag", "warning");
				}else{
				String conditionStr = " where task_id='"+task_id+"' order by task_log_id desc ";
				KeyedCollection  kCollLog = dao.queryFirst("PspRejectBatchLog", null, conditionStr, connection);
				KeyedCollection  kColl = dao.queryDetail(modelId, task_id, connection);
				approve_status = (String) kCollLog.getDataValue("approve_status_ori");
				KeyedCollection rblKColl = null;
				rblKColl = new KeyedCollection("PspRejectBatchLog");
				rblKColl.put("task_id", task_id);
				rblKColl.put("input_id", currentUserId);
				rblKColl.put("approve_status_ori", approve_status);
				rblKColl.put("update_type", "启用");
				rblKColl.put("input_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
				
				kColl.put("approve_status", approve_status);//非流程否决的状态改为【启用】
				
				dao.insert(rblKColl, connection);
				dao.update(kColl, connection);
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
