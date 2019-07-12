package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIGatherCommentVO;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * 会办审批操作，包括交办、结束会办
 * @author liuhw
 *
 */
public class SubmitGatherOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		String actionType = null;
		try {
			actionType = (String) context.getDataValue("actionType");
		} catch (Exception e) {
			throw new EMPException("操作失败。原因是获取参数[actionType]出错！");
		}
		Connection connection = null;
		String instanceID = null;
		String currentUserID = null;
		String suggestType = null;
		String suggest = null;
		String suggestControl = null;
		WorkflowServiceInterface wfi = null;
		try {
			connection = this.getConnection(context);
			currentUserID = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			instanceID = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			suggestType = (String) context.getDataValue("suggest_type");//意见类别
			suggest = (String) context.getDataValue("suggest");//流程意见
			suggestControl = (String) context.get("suggestControl");//其它部门是否可见
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			WFIVO wfivo = new WFIVO();
			wfivo.setMessage("未执行任何操作!");
			//意见VO
			WFIGatherCommentVO commentVO = new WFIGatherCommentVO();
			commentVO.setSuggest(suggest);
			commentVO.setCommentType(suggestType);
			commentVO.setCommentLevel(suggestControl==null?"1":suggestControl);//0-其它部门可见，1-本部门可见
			if(actionType.equals("changeGather")) { //交办
				String nextUserID = (String) context.getDataValue("nextUserId");//下一办理人
//				String gatherDesc = (String) context.getDataValue("gatherDesc");
				wfivo = wfi.wfChangeGather(instanceID, nextUserID, currentUserID, commentVO, connection);
				
			} else if(actionType.equals("gatherSubmit")) {  //提交汇总人
				wfivo = wfi.wfCompleteGather(instanceID, currentUserID, commentVO, connection);
				
			} else if(actionType.equals("endGather")) {  //结束会办
				wfivo = wfi.wfFinishGatherJob(instanceID, currentUserID, commentVO, connection);
			}
			KeyedCollection kcoll = new KeyedCollection("result");
			kcoll.put("sign", wfivo.getSign());
			kcoll.put("msg", wfivo.getMessage());
			this.putDataElement2Context(kcoll, context);
		} catch (Exception e) {
			EMPLog.log("SubmitGatherOp", EMPLog.ERROR, EMPLog.ERROR, "会办审批操作出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
