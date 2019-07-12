package com.yucheng.cmis.biz01line.psp.op.pspbatchtaskrel;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
*@author lisj
*@time 2015-1-13
*@description 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量）
*@version v1.0
*
 */
public class QuerySubPspCheckTaskListPopOp extends CMISOperation {

	private final String modelId = "PspCheckTask";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String managerId = "";
			String batch_task_type ="";
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement("PspBatchTask");
			} catch (Exception e) {}
			try{
				managerId = (String)context.getDataValue("manager_id");		
			}catch(Exception e){
				throw new Exception("主管客户经理ID获取失败!");
			}
			try{
				batch_task_type = (String)context.getDataValue("batch_task_type");
			}catch(Exception e){
				throw new Exception("批量任务类型获取失败!");
			}
            String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
		    if("".equals(conditionStr) || conditionStr == null){
		    	if(batch_task_type.equals("01")){
		    		conditionStr = "where manager_id ='"+managerId+"' and task_type='07' "
		    				+" and task_id not in (select sub_task_id from psp_batch_task_rel) and approve_status='000'"
		    				+"and not exists (select 1 from psp_check_task_rel"
		    				+" where psp_check_task.task_id = psp_check_task_rel.task_id "
		    				+" and exists (select 1 from acc_view where psp_check_task_rel.bill_no = acc_view.bill_no"
		    				+" and acc_view.five_class in ('20', '30', '40', '50')))"
		    				+" order by task_id desc";//房地产/汽车类
		    	}else if(batch_task_type.equals("02")){
		    		conditionStr = "where manager_id ='"+managerId+"' and task_type='08' " 
					    		+" and task_id not in (select sub_task_id from psp_batch_task_rel) and approve_status='000'"
			    				+"and not exists (select 1 from psp_check_task_rel"
			    				+" where psp_check_task.task_id = psp_check_task_rel.task_id "
			    				+" and exists (select 1 from acc_view where psp_check_task_rel.bill_no = acc_view.bill_no"
			    				+" and acc_view.five_class in ('20', '30', '40', '50')))"
			    				+" order by task_id desc";//融资100W以下类
		    	}
		    }else{	
		    	if(batch_task_type.equals("01")){
		    		conditionStr += "and manager_id ='"+managerId+"' and task_type='07' "
			    		+" and task_id not in (select sub_task_id from psp_batch_task_rel) and approve_status='000'"
	    				+"and not exists (select 1 from psp_check_task_rel"
	    				+" where psp_check_task.task_id = psp_check_task_rel.task_id "
	    				+" and exists (select 1 from acc_view where psp_check_task_rel.bill_no = acc_view.bill_no"
	    				+" and acc_view.five_class in ('20', '30', '40', '50')))"
	    				+" order by task_id desc";
		    	}else if(batch_task_type.equals("02")){
		    		conditionStr += "and manager_id ='"+managerId+"' and task_type='08' " 
			    		+" and task_id not in (select sub_task_id from psp_batch_task_rel) and approve_status='000'"
	    				+"and not exists (select 1 from psp_check_task_rel"
	    				+" where psp_check_task.task_id = psp_check_task_rel.task_id "
	    				+" and exists (select 1 from acc_view where psp_check_task_rel.bill_no = acc_view.bill_no"
	    				+" and acc_view.five_class in ('20', '30', '40', '50')))"
	    				+" order by task_id desc";
		    	}
		    }
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));	
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>(); 
			list.add("task_id");
			list.add("cus_id");
			list.add("check_type");
			list.add("task_create_date");
			list.add("task_request_time");
			list.add("qnt");
			list.add("loan_totl_amt");
			list.add("loan_balance");
			list.add("check_freq");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName("PspBatchTaskList");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			String[] args=new String[] {"cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
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
