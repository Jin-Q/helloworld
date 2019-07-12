package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>检查当前节点是否发起过异步子流程</p>
 * 处理逻辑：<br>
 * <li>1.如果存在异步子流程，并且子流程已经提交审批，则进行提示，是否仍然发起子流程
 * <li>2.如果存在异步子流程，但是子流程没有发生提交，则直接删除，无需提醒
 * @author liuhw
 *
 */
public class CheckAsynSubWorkFlowOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		WFIComponent wfiComponent = null;
		WorkflowServiceInterface wfi = null;
		try {
			connection = this.getConnection(context);
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			String instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			String subWfsign = (String) context.getDataValue("subWfSign");
			wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
			IndexedCollection subIcoll = wfiComponent.querySubWfiJoinList(instanceId);
			String flag = "0"; //0没有在途子流程，无需提示；1有在途子流程，提示；
			if(subIcoll!=null && subIcoll.size()>0) {
				wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
				for(Object obj : subIcoll) {
					KeyedCollection kcoll = (KeyedCollection) obj;
					String subInstanceId = (String) kcoll.getDataValue("instanceid");
					if(subInstanceId.equals(instanceId)) {
						continue;  //同步子流程无需检查
					}
					WFIInstanceVO ivo = wfi.getInstanceInfo(subInstanceId, currentUserId, null, connection);
					String wfSignTmp = ivo.getWfSign();
					String preNodeId = ivo.getPreNodeId();
					if(WorkFlowConstance.ATTR_BEGIN_NODEID.equals(preNodeId)) {  //前一节点为开始节点，直接删除
						Map paramMap = new HashMap();
						paramMap.put(WorkFlowConstance.ATTR_EMPCONTEXT, context);
						//added by yangzy 20150818 实例删除日志 start
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"DelWorkFlowOp-PART2",null);
						//added by yangzy 20150818 实例删除日志 end
						wfi.wfDelInstance(subInstanceId, currentUserId, paramMap, connection);
					} else if(wfSignTmp.equals(subWfsign)){
						flag = "1";
					}
				}
			}
			context.put("flag", flag);
		} catch (Exception e) {
			EMPLog.log("CheckAsynSubWorkFlowOp", EMPLog.ERROR, EMPLog.ERROR, "检查当前节点是否发起过异步子流程出错！异常信息："+e.getMessage());
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
