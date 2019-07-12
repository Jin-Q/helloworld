package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFINodeVO;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>流程退回任一步（打回）</p>
 * @author liuhw
 * added by yangzy 2014/11/25 XD140718026_新信贷系统授信进度查询改造，流程管理员无条件打回
 */
public class CallBackWorkFlowUnlimitOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		WorkflowServiceInterface wfi = null;
		Connection connection = null;
		String currentUserId = null;
		String instanceId = null;
		String nodeId = null;
		String nextNodeId = null;
		String nextNodeUser = null;
		String callBackModel = null;
		String nodeUser = null;
		try {
			connection = this.getConnection(context);
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			nodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NODEID);
			nextNodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NEXTNODEID);
			nextNodeUser = (String) context.getDataValue(WorkFlowConstance.ATTR_NEXTNODEUSER);
			callBackModel = (String) context.getDataValue("callBackModel");
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			Map paramMap = new HashMap();
			paramMap.put(WorkFlowConstance.ATTR_EMPCONTEXT, context);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection icoll = dao.queryList("WfiWorklistTodo", " where INSTANCEID = '"+instanceId+"' ", connection);
			if(icoll!=null&&icoll.size()>0){
				KeyedCollection kc = (KeyedCollection) icoll.get(0);
				if(kc!=null&&kc.getDataValue("currentnodeuser")!=null&&!"".equals(kc.getDataValue("currentnodeuser"))){
					nodeUser = (String) kc.getDataValue("currentnodeuser");
					nodeUser = nodeUser.replace(";", "");
				}
			}
			
			WFIVO wfiVo = wfi.wfCallBack(instanceId, nodeId, nodeUser, nextNodeId, nextNodeUser, callBackModel, paramMap, connection);
			context.put(WorkFlowConstance.WFVO_RET_NAME, wfiVo);
			
		} catch (Exception e) {
			EMPLog.log("CallBackWorkFlowOp", EMPLog.ERROR, EMPLog.ERROR, "流程退回任一步（打回）出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
