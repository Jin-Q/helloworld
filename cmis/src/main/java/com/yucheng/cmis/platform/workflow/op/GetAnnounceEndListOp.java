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
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;

/**
 * <p>获取已办结抄送事项列表</p>
 * @author lisj
 * @time 2014-12-16 15:07:28
 * @description 需求编号：【XD140808038】新信贷系统抄送事项的需求变更
 */
public class GetAnnounceEndListOp extends CMISOperation {

	private final static String modelId = "WfiWorklistEnd";
	
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
		    fields.add("wfendtime");
		    fields.add("spstatus");
		    fields.add("lastuser");
		    fields.add("costtimes");
		    fields.add("table_name");
		    fields.add("pk_value");
		    fields.add("cus_id");
		    fields.add("cus_name");
		    fields.add("appl_type");
		    fields.add("wfi_status");
		    fields.add("prd_name");
		    fields.add("amt");
		    fields.add("author");
		    fields.add("orgid");
			TableModelDAO dao = this.getTableModelDAO(context);
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			String condition2 = "instanceid in  (select instanceid from wf_instance_end where instr(exv100,'"+currentUserId+"') >0)";
			if(condition==null || condition.trim().equals("")) {
				condition = " WHERE " + condition2;
			} else {
				condition += " AND " + condition2;
			}
			condition += " ORDER BY WFSTARTTIME DESC";
			IndexedCollection icoll = dao.queryList(modelId, fields, condition, pageInfo, connection);
			icoll.setName(modelId+"List");
			SInfoUtils.addUSerName(icoll, new String[]{"author","currentnodeuser"});
			SInfoUtils.addSOrgName(icoll, new String[]{"orgid"});
			TableModelUtil.parsePageInfo(context, pageInfo);
			this.putDataElement2Context(icoll, context);
			
		} catch (Exception e) {
			EMPLog.log("GetAnnounceEndListOp", EMPLog.ERROR, EMPLog.ERROR, "获取抄送已办结事项列表出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
