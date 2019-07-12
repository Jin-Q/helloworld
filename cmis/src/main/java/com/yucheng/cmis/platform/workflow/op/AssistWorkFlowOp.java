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
 * 协助办理
 * @author liuhw
 *
 */
public class AssistWorkFlowOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		String userId = null;  //当前登录人
        String instanceId = null;  //实例号
        String nodeId;  //流程当前节点ID
        String nextNodeUser;  //流程下一办理人
        WorkflowServiceInterface wfi;
        Connection connection = null;
        try {
        	connection = this.getConnection(context);
        	userId = (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
            instanceId = (String)context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
            nodeId = (String)context.getDataValue(WorkFlowConstance.ATTR_NODEID);
            nextNodeUser = (String)context.getDataValue(WorkFlowConstance.ATTR_NEXTNODEUSER);
            wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
            WFIVO wfiVo = wfi.wfAssist(instanceId, nodeId, userId, nextNodeUser, connection);
            context.put(WorkFlowConstance.WFVO_RET_NAME, wfiVo);
            
        } catch (Exception e) {
			EMPLog.log("AssistWorkFlowOp", EMPLog.ERROR, EMPLog.ERROR, "协助办理出错；实例号：【"+instanceId+"】，当前用户【"+userId+"】。异常信息："+e.getMessage());
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
