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
 * <p>实例化会办</p>
 * @author liuhw
 *
 */
public class StartGatherOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		
		String mainInstanceID = null;//主流程实例
		String beforeInstanceID = null;//上级会办流程实例
		String mainNodeID = null;//主流程节点ID
		String bizSeqNo = null;//主流程业务流水号
		String gatherStartUserID = null;//发起人
		String gatherEndUserID = null;//汇总人
		String currentGatherUserList = null;//参与人
		String gatherTitle = null;//主题
		String gatherDesc = null;//描述
		WorkflowServiceInterface wfi = null;
		Connection connection = null;
		String currentUserId = null;
		try {
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			gatherTitle = (String) context.getDataValue("gatherTitle");
			gatherDesc = (String) context.getDataValue("gatherDesc");
			currentGatherUserList = (String) context.getDataValue("currentGatherUserListID");
			gatherEndUserID = (String) context.getDataValue("gatherEndUserID");
			gatherStartUserID = (String) context.getDataValue("gatherStartUserID");
			bizSeqNo = (String) context.getDataValue("bizSeqNo");
			mainInstanceID = (String) context.getDataValue("mainInstanceID");
			beforeInstanceID = (String) context.getDataValue("beforeInstanceID");
			mainNodeID = (String) context.getDataValue("mainNodeID");
			connection = this.getConnection(context);
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			WFIVO wfivo = wfi.initializeGather(bizSeqNo, gatherTitle, gatherDesc, gatherStartUserID, currentGatherUserList, gatherEndUserID, beforeInstanceID, mainInstanceID, mainNodeID, connection);
			if(wfivo.getSign() == wfivo.SIGN_SUCCESS) {
				context.put("flag", wfivo.getSign());
			} else {
				context.put("flag", wfivo.getMessage());
			}
		} catch (Exception e) {
			EMPLog.log("StartGatherOp", EMPLog.ERROR, EMPLog.ERROR, "实例化会办出错。异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
