package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisignvote;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.msi.OrganizationCacheServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryWfiSignVoteListOp extends CMISOperation {


	private final String modelId = "WfiSignVote";
	private final String modelId1="WfiSignTask";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String serno = "";
			if(queryData!=null && queryData.containsKey("serno")){//业务编号单独处理
				serno = (String)queryData.getDataValue("serno");
				queryData.remove("serno");
			}
			String currentuserid=(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);//获取当前登录人
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			 if (conditionStr == null || conditionStr.trim().equals(""))
                 conditionStr = " where 1=1 ";
			StringBuffer conditionBuffer = new StringBuffer("");
			conditionBuffer.append(" and sv_exe_user='").append(currentuserid).append("' ");//任务执行人只能看到自己的任务
			if(serno!=null && !"".equals(serno)){
				conditionBuffer.append(" and st_task_id in (select st_task_id from wfi_sign_task where serno='"+serno+"') ");
			}
			conditionStr =conditionStr+conditionBuffer.toString()+"order by sv_vote_id desc";
									
			
			int size = 20;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
		    List list = new ArrayList();
			list.add("sv_vote_id");
			list.add("sv_exe_user");
			list.add("sv_result");
			list.add("sv_status");
			list.add("sv_start_time");
			list.add("sv_end_time");
			list.add("sv_request_time");
			list.add("st_task_id");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			//根据WfiSignVote中的投票id得到任务id，然后根据任务id，查询WfiSignTask中的 相关字段
			for(Iterator<KeyedCollection> iter=iColl.iterator();iter.hasNext();){
				KeyedCollection kColl=iter.next();
				String stTaskzId=(String)kColl.getDataValue("st_task_id");
				IndexedCollection iColl1=dao.queryList(modelId1, " where st_task_id='"+stTaskzId+"'" ,connection);
				  for(Iterator<KeyedCollection> iter1=iColl1.iterator();iter1.hasNext();){
					 KeyedCollection kColl1=iter1.next();
					 String serNO=(String)kColl1.getDataValue("serno");
					 String bizType=(String)kColl1.getDataValue("biz_type");
					 String stTaskName=(String)kColl1.getDataValue("st_task_name");
					 
					 kColl.addDataField("serno", serNO);
					 kColl.addDataField("biz_type", bizType);
					 kColl.addDataField("st_task_name", stTaskName);
				 }
				
			}
			
			OrganizationCacheServiceInterface orgCacheMsi = (OrganizationCacheServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationCacheServices", "organization");
			orgCacheMsi.addUserName(iColl, new String[]{"sv_exe_user"});
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			
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
