package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>删除流程实例</p>
 * @author liuhw
 *
 */
public class DelWorkFlowOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		WorkflowServiceInterface wfi = null;
		boolean result = false;
		try {
			String instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			connection = this.getConnection(context);
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			Map paramMap = new HashMap();
			paramMap.put(WorkFlowConstance.ATTR_EMPCONTEXT, context);
			//added by yangzy 20150818 实例删除日志 start
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"DelWorkFlowOp-PART1",null);
			//added by yangzy 20150818 实例删除日志 end
			wfi.wfDelInstance(instanceId, currentUserId, paramMap, connection);
			result = true;
			if(result)
				context.put("flag", "0");
			else
				context.put("flag", "1");
			
		} catch (Exception e) {
			EMPLog.log("DelWorkFlowOp", EMPLog.ERROR, EMPLog.ERROR, "删除流程实例出错！异常信息为："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
