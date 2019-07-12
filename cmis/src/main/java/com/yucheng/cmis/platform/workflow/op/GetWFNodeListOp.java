package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFINodeVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>获取当前流程所有流程节点</p>
 * @author liuhw
 *
 */
public class GetWFNodeListOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		WorkflowServiceInterface wfi = null;
		Connection connection = null;
		String instanceId = null;
		try {
			connection = this.getConnection(context);
			instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			String wfId = null; //传入null，根据实例号获取wfid
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			List<WFINodeVO> list = wfi.getWFNodeList(instanceId, wfId, connection);
			context.put(WorkFlowConstance.WF_NEXT_NODE_LIST, list);
			
		} catch (Exception e) {
			EMPLog.log("GetWFNodeListOp", EMPLog.ERROR, EMPLog.ERROR, "获取当前流程所有流程节点出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
