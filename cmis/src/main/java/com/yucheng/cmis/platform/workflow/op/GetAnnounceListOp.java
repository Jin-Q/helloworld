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
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.base.CMISConstance;

/**
 * <p>获取抄送事项列表</p>
 * @author liuhw
 *
 */
public class GetAnnounceListOp extends CMISOperation {

	private final static String modelId = "WfiWorklistTodo";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		KeyedCollection queryData = null;
		String condition = null;
		try {
			queryData = (KeyedCollection) context.getDataElement(modelId);
			condition = TableModelUtil.getQueryCondition(modelId, queryData, context, false, false, false);
			condition = StringUtil.transConditionStr(condition, "cus_name");
		} catch (Exception e) {
		}
		try {
			connection = this.getConnection(context);
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
			List<String> fields = new ArrayList<String>();
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
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			//非拟稿||已经办理  && 流转中||过期办理  && 办理人员LIKE
			//(此处可能存在一个bug（一般不会），当出现用户号如800001、80000等情况时，所以尽量避免存在诸如此类的用户ID)
			String condition2 = "(BDRAFT='0' OR ISPROCESSED='1') AND ANNOUNCEUSER IS NOT NULL AND (WFSTATUS='0' OR WFSTATUS='5')" + 
						" AND ANNOUNCEUSER LIKE '%"+currentUserId+";%'";
			if(condition==null || condition.trim().equals("")) {
				condition = " WHERE " + condition2;
			} else {
				condition += " AND " + condition2;
			}
			condition += " ORDER BY NODESTARTTIME DESC";
			IndexedCollection icoll = dao.queryList(modelId, fields, condition, pageInfo, connection);
			icoll.setName(modelId+"List");
			WorkFlowUtil.addUserName4WF(icoll, new String[]{"currentnodeuser"});
			TableModelUtil.parsePageInfo(context, pageInfo);
			this.putDataElement2Context(icoll, context);
			
		} catch (Exception e) {
			EMPLog.log("GetAnnounceListOp", EMPLog.ERROR, EMPLog.ERROR, "获取抄送事项列表出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
