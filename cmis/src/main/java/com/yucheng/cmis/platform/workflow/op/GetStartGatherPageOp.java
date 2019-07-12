package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIGatherInstanceVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>获取发起会办信息</p>
 * @author liuhw
 *
 */
public class GetStartGatherPageOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		String currentUserId = null;
//		String mainInstanceId = null; //主流程实例ID
		String beforeInstanceId = null;
		String mainNodeId = null;
		String mainNodeName = null;
//		String gatherTitle = "";
//		String gatherDesc = "";
		WorkflowServiceInterface wfi = null;
		Connection connection = null;
		try {
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
//			mainInstanceId = (String) context.getDataValue("instanceId");
			beforeInstanceId = (String) context.get("beforeInstanceId");
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			mainNodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NODEID);
			mainNodeName = (String) wfi.getWFNodeProperty(mainNodeId, "NodeName");
			context.put("mainNodeName", mainNodeName);
			if(beforeInstanceId!=null && !beforeInstanceId.equals("")){
				connection = this.getConnection(context);
				WFIGatherInstanceVO gatherInstanceVO = wfi.getGatherInstanceInfo(beforeInstanceId, currentUserId, connection);
				context.put("gahterInstanceIdVO", gatherInstanceVO);
			}
			
		} catch (Exception e) {
			EMPLog.log("GetStartGatherPageOp", EMPLog.ERROR, EMPLog.ERROR, "获取发起会办信息出错。异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
