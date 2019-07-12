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
 * <p>流程挂起</p>
 * @author liuhw
 *
 */
public class HangWorkFlowOp extends CMISOperation {

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
			Map paramMap = new HashMap();
			paramMap.put(WorkFlowConstance.ATTR_EMPCONTEXT, context);
			WFIVO wfiVo = wfi.wfHang(instanceId, currentUserId, paramMap, connection);
			context.put(WorkFlowConstance.WFVO_RET_NAME, wfiVo);
			
		} catch (Exception e) {
			EMPLog.log("HangWorkFlowOp", EMPLog.ERROR, EMPLog.ERROR, "流程挂起出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
