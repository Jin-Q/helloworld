package com.yucheng.cmis.biz01line.psp.op.pspbatchtaskrel;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
*@author lisj
*@time 2015-1-13
*@description 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量）
*@version v1.0
*
 */
public class ImportPspBatchTaskRelOp extends CMISOperation {
	private static final String relModel = "PspBatchTaskRel";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String manager_id = (String)context.getDataValue("manager_id");
			String major_task_id = (String)context.getDataValue("major_task_id");
			String subTaskIdStr = (String)context.getDataValue("subTaskIdStr");
			String batch_task_type = (String)context.getDataValue("batch_task_type");
			if(manager_id == null || manager_id.trim().length() == 0){
				throw new EMPException("引入子任务【主管客户经理】ID获取失败！");
			}
			if(major_task_id == null || major_task_id.trim().length() == 0){
				throw new EMPException("引入子任务【主任务编号】ID获取失败！");
			}
			if(batch_task_type == null || batch_task_type.trim().length() == 0){
				throw new EMPException("引入子任务【批量任务类型】获取失败！");
			}
			if(subTaskIdStr == null || subTaskIdStr.trim().length() == 0){
				throw new EMPException("引入子任务【任务编号字符串】获取失败！");
			}
			String sub_task_id ="";
        	String[]subTaskIdList = subTaskIdStr.split(",");
			TableModelDAO dao = this.getTableModelDAO(context);
			//查询子任务的任务起始日期是否一致
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			String conditionSelect ="select distinct t.task_create_date, t.task_request_time "
								+"  from (select p1.task_create_date, p1.task_request_time   "
								+"          from psp_check_task p1                           "
								+"         where instr('"+subTaskIdStr+"', p1.task_id) > 0  "
								+"        union all                                          "
								+"        select p2.task_create_date, p2.task_request_time   "
								+"          from psp_batch_task_rel p2						 "
								+"         where p2.major_task_id = '"+major_task_id+"') t	 ";
			IndexedCollection taskTimeList = TableModelUtil.buildPageData(null, dataSource, conditionSelect);
			if(taskTimeList!=null && taskTimeList.size() == 1){
				KeyedCollection PCT = (KeyedCollection) taskTimeList.get(0);
				PCT.put("task_id", major_task_id);
				PCT.setName("PspCheckTask");
				//将引入的贷后子任务起始日赋值给批量任务的任务起始日
				dao.update(PCT, connection);
	        	for(int i=0;i<subTaskIdList.length;i++){
	        		sub_task_id =subTaskIdList[i];  		
	        		KeyedCollection temp = dao.queryDetail("PspCheckTask", sub_task_id, connection);
	    			KeyedCollection relKColl = new KeyedCollection();
	        		relKColl.put("major_task_id", major_task_id);
	        		relKColl.put("sub_task_id",sub_task_id);
	        		relKColl.put("cus_id", temp.getDataValue("cus_id"));
	        		relKColl.put("check_type", temp.getDataValue("check_type"));
	        		relKColl.put("task_create_date", temp.getDataValue("task_create_date"));
	        		relKColl.put("task_request_time", temp.getDataValue("task_request_time"));
	        		relKColl.put("qnt", temp.getDataValue("qnt"));
	        		relKColl.put("loan_totl_amt", temp.getDataValue("loan_totl_amt"));
	        		relKColl.put("loan_balance", temp.getDataValue("loan_balance"));
	        		relKColl.put("manager_id", manager_id);
	        		relKColl.put("manager_br_id", temp.getDataValue("manager_br_id"));
	        		relKColl.put("task_type", temp.getDataValue("task_type"));
	        		relKColl.put("check_freq", temp.getDataValue("check_freq"));
	        		relKColl.put("batch_task_type", batch_task_type);
	    			relKColl.setName(relModel);
	        		dao.insert(relKColl, connection);
	        	}
				context.addDataField("flag", "success");
			}else{
				context.addDataField("flag", "timeDiff");
			}
		} catch (Exception e) {
			context.addDataField("flag", "falied");
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
