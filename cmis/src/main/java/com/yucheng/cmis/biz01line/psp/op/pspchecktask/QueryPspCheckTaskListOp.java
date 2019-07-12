package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;

import javax.sql.DataSource;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPspCheckTaskListOp extends CMISOperation {


	private final String modelId = "PspCheckTask";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			//获取当前登录人工号
			String currentUserId=(String)context.getDataValue("currentUserId");
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			String roleNoList=(String)context.getDataValue(CMISConstance.ATTR_ROLENO_LIST);
			if(conditionStr.equals("")){
				conditionStr = " where approve_status != '997' and ( manager_id = '"+currentUserId+"' or (instr('"+roleNoList+"','9011')>0 and manager_id = 'OCS01')) ";
			}else{
				conditionStr = conditionStr + " and approve_status != '997' and ( manager_id = '"+currentUserId+"' or (instr('"+roleNoList+"','9011')>0 and manager_id = 'OCS01')) ";
			}
			conditionStr = conditionStr+"order by task_id desc";
			
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			/**add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  begin**/			
			String task_type = (String) queryData.getDataValue("task_type");
			if(task_type!=null && !"".equals(task_type) &&(task_type.equals("07") || task_type.equals("08"))){
				for(int i=0;i<iColl.size();i++){
					KeyedCollection temp = (KeyedCollection) iColl.get(i);
					String sub_task_id = (String) temp.getDataValue("task_id");
					DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
					//查询贷后任务是否存在批量任务中，如果有则取批量任务的审批状态为基准
					String conditionSelect ="select p1.approve_status from psp_check_task p1 where p1.task_id in "
											+"(select p2.major_task_id from psp_batch_task_rel p2 where p2.sub_task_id = '"+sub_task_id+"') and p1.approve_status !='997'";
					IndexedCollection PspBatchTaskRel = TableModelUtil.buildPageData(null, dataSource, conditionSelect);
					if(PspBatchTaskRel!=null && PspBatchTaskRel.size()>0){
					 KeyedCollection tempRel = (KeyedCollection) PspBatchTaskRel.get(0);
					 temp.setDataValue("approve_status", tempRel.getDataValue("approve_status").toString());
					}
				}
			}
			/**add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  end**/
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"manager_id"});

			String[] args=new String[] {"cus_id","grp_no" };
			String[] modelIds=new String[]{"CusBase","CusGrpInfo"};
			String[]modelForeign=new String[]{"cus_id","grp_no"};
			String[] fieldName=new String[]{"cus_name","grp_name"};
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
