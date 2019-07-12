package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIUserVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>获取转办用户
 * @author liuhw
 *
 */
public class GetWfChangeUserOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
			
		String currentUserId = null;
		String nodeId = null;
		String instanceId = null;
		Connection connection = null;
		WorkflowServiceInterface wfi = null;
		try {
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			nodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NODEID);
			connection = this.getConnection(context);
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			List<WFIUserVO> list = wfi.getChangeUser(instanceId, nodeId, currentUserId, connection);
			context.put(WorkFlowConstance.WF_NEXT_USER_LIST, list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
