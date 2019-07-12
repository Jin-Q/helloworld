package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

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
 * <p>流程退回</p>
 * 根据isDraft控制是否退回第一个节点；1退回第一个节点，否则退回上一节点（物理上）
 * @author liuhw
 *
 */
public class ReturnBackWorkFlowOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		String userId = null;  //当前登录人
        String instanceId = null;  //实例号
        String isDraft = null; //根据isDraft控制是否退回第一个节点；1退回第一个节点，否则退回上一节点（物理上）
        WorkflowServiceInterface wfi;
        Connection connection = null;
        try {
        	connection = this.getConnection(context);
        	userId = (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
            instanceId = (String)context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
            isDraft = (String) context.getDataValue("isDraft");
        	wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
            Map paramMap = new HashMap();
            paramMap.put(WorkFlowConstance.ATTR_EMPCONTEXT, context);
            WFIVO wfivo = wfi.wfReturnBack(instanceId, userId, isDraft, paramMap, connection);
            context.put(WorkFlowConstance.WFVO_RET_NAME, wfivo);
        } catch (Exception e) {
			EMPLog.log("ReturnBackWorkFlowOp", EMPLog.ERROR, EMPLog.ERROR, "流程退回出错；实例号：【"+instanceId+"】，当前用户【"+userId+"】。异常信息："+e.getMessage());
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
