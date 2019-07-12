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
 * 重置会办参与人
 * @author liuhw
 *
 */
public class ResetGatherProcessorOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		String currentGatherUserList = null;
		String gatherInstanceId = null;
		String currentUserId = null;
		Connection connection = null;
		try {
			currentGatherUserList = (String) context.getDataValue("currentGatherUserList");
			gatherInstanceId = (String) context.getDataValue("instanceID");
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			connection = this.getConnection(context);
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			WFIVO wfiVo = wfi.wfResetGatherProcessor(gatherInstanceId, currentGatherUserList, currentUserId, connection);
			context.put("WFIVO", wfiVo);			
		} catch (Exception e) {
			EMPLog.log("ResetGatherProcessorOp", EMPLog.ERROR, EMPLog.ERROR, "重置会办参与人出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
