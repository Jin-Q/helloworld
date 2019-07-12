package com.yucheng.cmis.platform.workflow.op;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * 调用流程引擎服务重新加载流程缓存
 * @author liuhw
 *
 */
public class ReloadWfCacheOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		try {
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			wfi.loadWorkFlowCache(true);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		}
		context.put("flag", "0");
		return null;
	}

}
