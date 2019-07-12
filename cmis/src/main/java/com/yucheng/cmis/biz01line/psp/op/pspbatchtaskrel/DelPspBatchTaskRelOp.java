package com.yucheng.cmis.biz01line.psp.op.pspbatchtaskrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 
*@author lisj
*@time 2015-1-13
*@description 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量）
*@version v1.0
*
 */
public class DelPspBatchTaskRelOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String manager_id = (String)context.getDataValue("manager_id");
			String subTaskIdStr = (String)context.getDataValue("subTaskIdStr");
			if(manager_id == null || manager_id.trim().length() == 0){
				throw new EMPException("引入子任务【主管客户经理】ID获取失败！");
			}
			if(subTaskIdStr == null || subTaskIdStr.trim().length() == 0){
				throw new EMPException("引入子任务【任务编号字符串】获取失败！");
			}
			String sub_task_id ="";
        	String[]subTaskIdList = subTaskIdStr.split(",");
        	for(int i=0;i<subTaskIdList.length;i++){
        		sub_task_id =subTaskIdList[i];
				String conditionStr ="delete from psp_batch_task_rel p " +
						"where p.sub_task_id='"+sub_task_id+"' and p.manager_id='"+manager_id+"'";
				
				SqlClient.deleteBySql(conditionStr, connection);
        	}
			context.addDataField("flag", "success");
		} catch (Exception e) {
			context.addDataField("flag", "falied");
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
