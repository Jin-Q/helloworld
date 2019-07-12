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

/**
 * <p>获取流程实例列表(流程实例管理)</p>
 * @author liuhw
 *
 */
public class GetWfInstanceListOp extends CMISOperation {

	private final String modelId = "WfiWorkList";
	private final String modelIdTodo = "WfiWorklistTodo";
	private final String modelIdEnd = "WfiWorklistEnd";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		
		String flag = null;
		String modelIdDb = null;
		try {
			flag = (String) context.getDataValue("flag");
			if(flag.equals("1") && !"2".equals(flag)) {
				modelIdDb = modelIdTodo;
			} else if(flag.equals("2")) {
				modelIdDb = modelIdEnd;
			} else {
				throw new EMPException("获取参数[flag]失败，非法值！");
			}
		} catch (Exception e) {
			throw new EMPException("获取参数[flag]失败！");
		}
		KeyedCollection kcollQuery = null;
		try {
			kcollQuery = (KeyedCollection) context.getDataElement(modelId);
		} catch (Exception e) {
		}
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String condition = TableModelUtil.getQueryCondition(modelIdDb, kcollQuery, context, false, false, false);
			condition += " ORDER BY WFSTARTTIME DESC";
			TableModelDAO dao = this.getTableModelDAO(context);
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
			List fields = new ArrayList();
			if(flag.equals("1")) {
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
			} else {
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
			}
			IndexedCollection icoll = dao.queryList(modelIdDb, fields, condition, pageInfo, connection);
			icoll.setName(modelId+"List");
			this.putDataElement2Context(icoll, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
		} catch (Exception e) {
			EMPLog.log("GetWfInstanceListOp", EMPLog.ERROR, EMPLog.ERROR, "获取流程实例列表出错。异常信息："+e.getMessage());
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return modelIdDb;
	}

}
