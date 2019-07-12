package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>管理员重置节点办理人</p>
 * @author liuhw
 *
 */
public class ResetNodeUserOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try {
			String instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			String nodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NODEID);
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			String resetNodeUser = (String) context.getDataValue("ext");
			connection = this.getConnection(context);
			WorkflowServiceInterface wfi = null;
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			WFIVO wvo = wfi.resetCurrentNodeUser(instanceId, nodeId, resetNodeUser, currentUserId, null, connection);
			if(wvo.getSign() == WFIVO.SIGN_SUCCESS) {
				context.put("flag", WFIVO.SIGN_SUCCESS);
			} else {
				context.put("flag", wvo.getMessage());
			}
		} catch (Exception e) {
			EMPLog.log("ResetNodeUserOp", EMPLog.ERROR, EMPLog.ERROR, "重置节点办理人出错！异常信息为："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
