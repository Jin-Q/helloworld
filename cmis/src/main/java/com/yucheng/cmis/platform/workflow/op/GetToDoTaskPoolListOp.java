package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * 获取指定项目池下的任务列表
 * @author liuhw
 *
 */
public class GetToDoTaskPoolListOp extends CMISOperation {

	private final static String modelId = "WfiWorklistTodo";
	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		KeyedCollection queryData = null;
		String condition = null;
		WorkflowServiceInterface wfi = null;
		try {
			connection = this.getConnection(context);
			String tpid = null;
			try {
				tpid = (String) context.getDataValue("tpid");
				String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
				//检查获取的项目池是否当前用户下权限，防非法修改url
				wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
				Vector tps = wfi.queryUserTaskPool(currentUserId, connection);
				boolean isVal = false;
				if(tps!=null && tps.size()>0) {
					for(Object obj : tps) {
						Vector vecRow = (Vector) obj;
						String tpidTmp = (String) vecRow.elementAt(0);
						if(tpid.equals(tpidTmp)) {
							isVal = true;
							break;
						}
					}
				}
				if(!isVal) {
					return null;
				}
				
			} catch (Exception e) {
				throw new EMPException("获取指定项目池下的任务列表失败，原因参数[tpid]获取出错！");
			}
			try {
				queryData = (KeyedCollection) context.getDataElement(modelId);
				condition = TableModelUtil.getQueryCondition(modelId, queryData, context, false, false, false);
			} catch (Exception e) {
			}
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
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
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition2 = "CURRENTNODEUSER='T."+tpid+";' AND " +
					" (WFSTATUS='0' OR WFSTATUS='5') ";
			if(condition==null || condition.trim().equals("")) {
				condition = " WHERE " + condition2;
			} else {
				condition += " AND " + condition2;
			}
			IndexedCollection icoll = dao.queryList(modelId, fields, condition, pageInfo, connection);
			icoll.setName(modelId+"List");
			TableModelUtil.parsePageInfo(context, pageInfo);
			this.putDataElement2Context(icoll, context);
			
		} catch (Exception e) {
			EMPLog.log("GetToDoTaskPoolListOp", EMPLog.ERROR, EMPLog.ERROR, "获取指定项目池下的任务列表出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
