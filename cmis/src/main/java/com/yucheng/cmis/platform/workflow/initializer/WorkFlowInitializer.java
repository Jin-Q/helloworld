package com.yucheng.cmis.platform.workflow.initializer;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.yucheng.cmis.base.BusinessInitializer;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>工作流引擎接入初始化缓存<p>
 * @author liuhw
 *
 */
public class WorkFlowInitializer implements BusinessInitializer {

	public void initialize(Context rootCtx, Connection connection)
			throws Exception {
		
		WorkflowServiceInterface wfi = null;
		wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
		wfi.loadWorkFlowCache(false);
		
	}

}
