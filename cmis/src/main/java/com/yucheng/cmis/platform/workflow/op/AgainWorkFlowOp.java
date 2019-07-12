package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>流程实例收回</p>
 * @author liuhw
 *
 */
public class AgainWorkFlowOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		WorkflowServiceInterface wfi = null;
		Connection connection = null;
		String currentUserId = null;
		String instanceId = null;
		try {
			connection = this.getConnection(context);
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			WFIInstanceVO instanceVO = wfi.getInstanceInfo(instanceId, currentUserId, null, connection);
			String wfCurUserId = instanceVO.getCurrentNodeUser();
			String wfCurNodeId = instanceVO.getNodeId();
			String wfCurNodeName = instanceVO.getNodeName();
			String wfNodeStartTime = instanceVO.getNodeStartTime();
			String wfNodeStartDate = wfNodeStartTime.substring(0, 10);
			context.put("wfCurUserId", wfCurUserId);
			context.put("wfCurNodeId", wfCurNodeId);
			context.put("wfCurNodeName", wfCurNodeName);
			context.put("wfNodeStartDate", wfNodeStartDate);
			Map paramMap = new HashMap();
			paramMap.put(WorkFlowConstance.ATTR_EMPCONTEXT, context);
			WFIVO wfiVo = wfi.wfTakeBack(instanceId, currentUserId, paramMap, connection);
			if(wfiVo.getSign() == WFIVO.SIGN_SUCCESS) {
				context.put("flag", WFIVO.SIGN_SUCCESS);
			} else {
				context.put("flag", wfiVo.getMessage());
			}
			
		} catch (Exception e) {
			EMPLog.log("AgainWorkFlowOp", EMPLog.ERROR, EMPLog.ERROR, "流程实例收回出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
