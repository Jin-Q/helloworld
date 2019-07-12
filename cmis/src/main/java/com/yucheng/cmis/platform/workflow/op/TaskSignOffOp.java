package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>实例放回项目池</p>
 * @author liuhw
 *
 */
public class TaskSignOffOp extends CMISOperation {

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
			WFIVO wfiVo = wfi.taskSignOff(instanceId, currentUserId, connection);
			if(wfiVo.getSign() == WFIVO.SIGN_SUCCESS) {
				context.put("flag", WFIVO.SIGN_SUCCESS);
			} else {
				context.put("flag", wfiVo.getMessage());
			}
			
		} catch (Exception e) {
			EMPLog.log("SignInWorkFlowOp", EMPLog.ERROR, EMPLog.ERROR, "流程实例签收出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
