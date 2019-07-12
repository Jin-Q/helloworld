package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.TimeUtil;
/**
 * 
*@author lisj
*@time 2015-5-19
*@description TODO 需求编号：XD150504034 贷后管理常规检查任务改造（否决操作）
*@version v1.0
*
 */
public class RejectPspCheckTaskRecordOp extends CMISOperation {
	
	private final String modelId = "PspCheckTask";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);	
			String task_id ="";
			/**modified by yezm 2015-7-23 需求编号：XD150625045 贷后管理常规检查任务改造（记录否决操作log） begin**/
			String currentUserId ="";
			String approve_status ="";
			try {
				task_id = (String)context.getDataValue("task_id");
				currentUserId = (String)context.getDataValue("currentUserId");
				
			} catch (Exception e) {}		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection  kColl = dao.queryDetail(modelId, task_id, connection);
			approve_status = kColl.getDataValue("approve_status").toString().trim();
			KeyedCollection rblKColl = null;
			rblKColl = new KeyedCollection("PspRejectBatchLog");
			rblKColl.put("task_id", task_id);
			rblKColl.put("input_id", currentUserId);
			rblKColl.put("approve_status_ori", approve_status);
			rblKColl.put("update_type", "否决");
			rblKColl.put("input_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
			
			kColl.put("approve_status", "998");//无条件更改审批状态为【否决】
			
			dao.insert(rblKColl, connection);
			/**modified by yezm 2015-7-23 需求编号：XD150625045 贷后管理常规检查任务改造（记录否决操作log） end**/
			dao.update(kColl, connection);
			context.put("flag", PUBConstant.SUCCESS);
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