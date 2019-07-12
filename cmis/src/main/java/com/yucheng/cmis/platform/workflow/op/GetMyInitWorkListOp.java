package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.msi.OrganizationCacheServiceInterface;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.base.CMISConstance;

/**
 * <p>获取我发起的任务列表</p>
 * @author liuhw
 *
 */
public class GetMyInitWorkListOp extends CMISOperation {

	private final static String modelIdToDo = "WfiWorklistTodo";
	private final static String modelIdEnd = "WfiWorklistEnd";
	
	@Override
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		String type = null; //获取类型：todo未办结，end已办结
		try {
			try {
				type = (String) context.getDataValue("type");
				if(!type.equals("todo") && !type.equals("end")) {
					throw new EMPException("获取我发起的任务列表失败，原因是参数[type]传递不正确！");
				}
			} catch (Exception e) {
				throw new EMPException("获取我发起的任务列表失败，原因是参数[type]获取出错！");
			}
			connection = this.getConnection(context);
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			KeyedCollection queryData = (KeyedCollection) context.get(type.equals("todo")?modelIdToDo:modelIdEnd);
			String conditionStr = TableModelUtil.getQueryCondition(type.equals("todo")?modelIdToDo:modelIdEnd, queryData, context, false, false, false);
			if(conditionStr==null || "".equals(conditionStr)) {//2015-01-30 新增主管客户经理查看进度 Edited By FCL 
				if("todo".equals(type)){conditionStr = " WHERE (AUTHOR='"+currentUserId+"' OR CUST_MGR='"+currentUserId+"')";}else{conditionStr = " WHERE AUTHOR='"+currentUserId+"' ";}
			} else {
				if("todo".equals(type)){conditionStr = conditionStr+" AND (AUTHOR='"+currentUserId+"' OR CUST_MGR='"+currentUserId+"') ";}else{conditionStr = conditionStr+" AND AUTHOR='"+currentUserId+"' ";}
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
			IndexedCollection iColl = null;
			if(type.equals("todo")) {
				List fields = new ArrayList();
				fields.add("instanceid");
				fields.add("maininstanceid");
				fields.add("wfid");
				fields.add("wfname");
				fields.add("wfsign");
				fields.add("wfjobname");
				fields.add("wfstarttime");
				fields.add("author");
				fields.add("prenodeid");
				fields.add("prenodename");
				fields.add("nodeid");
				fields.add("nodename");
				fields.add("nodestatus");
				fields.add("nodestarttime");
				fields.add("nodeaccepttime");
				fields.add("nodeplanendtime");
				fields.add("currentnodeuser");
				fields.add("currentnodeusers");
				fields.add("originalusers");
				fields.add("table_name");
				fields.add("pk_value");
				fields.add("cus_id");
				fields.add("cus_name");
				fields.add("appl_type");
				fields.add("wfi_status");
				iColl = dao.queryList(modelIdToDo, fields, conditionStr, pageInfo, connection);
				iColl.setName("WfiWorklistTodoList");
			} else {
				List fields = new ArrayList();
				fields.add("instanceid");
				fields.add("maininstanceid");
				fields.add("wfid");
				fields.add("wfname");
				fields.add("wfsign");
				fields.add("wfjobname");
				fields.add("wfstarttime");
				fields.add("wfendtime");
				fields.add("author");
				fields.add("spstatus");
				fields.add("lastuser");
				fields.add("costtimes");
				fields.add("table_name");
				fields.add("pk_value");
				fields.add("cus_id");
				fields.add("cus_name");
				fields.add("appl_type");
				fields.add("wfi_status");
				iColl = dao.queryList(modelIdEnd, fields, conditionStr, pageInfo, connection);
				iColl.setName("WfiWorklistEndList");
			}
			WorkFlowUtil.addUserName4WF(iColl, new String[]{"currentnodeuser","lastuser"});
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
		} catch (Exception e) {
			EMPLog.log("GetMyInitWorkListOp", EMPLog.ERROR, EMPLog.ERROR, "获取我发起的任务列表出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);			
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return type;
	}

}
