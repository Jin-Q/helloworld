package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFINodeVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>获取下一节点列表</p>
 * @author liuhw
 */
public class GetNextNodeListOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String instanceId, nodeId, currentUserId;
		try {
			instanceId = (String) context.getDataValue("instanceId");
			nodeId = (String) context.getDataValue("nodeId");
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			connection = this.getConnection(context);
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			List<WFINodeVO> al = wfi.getNextNodeList(instanceId, currentUserId, nodeId, connection);
			String nodeRouterType = (String) wfi.getWFNodeProperty(nodeId, WorkFlowConstance.NODE_PROPERTY_ROUTERTYPE);
			/**
			 * 已经指定审批结论为否决，则只返回结束节点
			 */
			String commentSign = (String) context.get(WorkFlowConstance.ATTR_WFI_RESULT);
			if(WorkFlowConstance.WFI_RESULT_DISAGREE.equals(commentSign)) {
				nodeRouterType = "0";
				List<WFINodeVO> nodeListTmp = new ArrayList<WFINodeVO>();
				for(WFINodeVO wfiNode : al) {
					String nodeIdTmp = wfiNode.getNodeId();
					if(nodeIdTmp.indexOf("e") != -1) {
						nodeListTmp.add(wfiNode);
						break;
					}
				}
				al = nodeListTmp;
			}
			String nodeName = (String) wfi.getWFNodeProperty(nodeId, "nodename");
			context.put("CurrentNodeName", nodeName);
			context.put(WorkFlowConstance.WF_NEXT_NODE_LIST, al);
			context.put(WorkFlowConstance.NODE_PROPERTY_ROUTERTYPE, nodeRouterType);
			
		} catch (Exception e) {
			EMPLog.log("GetNextNodeListOp", EMPLog.ERROR, EMPLog.ERROR, "获取流程下一节点列表出错。异常信息为："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return "0";
	}

}
