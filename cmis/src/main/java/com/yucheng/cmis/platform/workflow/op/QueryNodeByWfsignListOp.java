package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFINodeVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>根据流程标识查找节点列表</p>
 * @author liuhw
 *
 */
public class QueryNodeByWfsignListOp extends CMISOperation {

	private final String modelIdVir = "WorkFlowNode";
	@Override
	public String doExecute(Context context) throws EMPException {
		
		WorkflowServiceInterface wfi = null;
		Connection connection = null;
		String wfsign = null;
		KeyedCollection queryData = null;
		try {
			queryData = (KeyedCollection) context.getDataElement(modelIdVir);
		} catch (Exception e) {
		}
		try {
			connection = this.getConnection(context);
			wfsign = (String) context.getDataValue("wfsign");
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			String wfId = (String) wfi.getWFPropertyByWfSign(wfsign, "wfid");
			List<WFINodeVO> list = wfi.getWFNodeList(null, wfId, connection);
			IndexedCollection icoll = new IndexedCollection(modelIdVir+"List");
			if(list!=null && list.size()>=0) {
				for(WFINodeVO node : list) {
					String nodeId = node.getNodeId();
					String nodeName = node.getNodeName();
					boolean isExist1 = false;
					boolean isExist2 = false;
					try {
						isExist1 = nodeId.contains((String)queryData.getDataValue("nodeid"));
					} catch (Exception e) {
						isExist1 = true;
					}
					try {
						isExist2 = nodeName.contains((String)queryData.getDataValue("nodename"));
					} catch (Exception e) {
						isExist2 = true;
					}
					if(isExist1 && isExist2) {
						KeyedCollection kcoll = new KeyedCollection(modelIdVir);
						kcoll.put("nodeid", nodeId);
						kcoll.put("nodename", nodeName);
						icoll.add(kcoll);
					}
				}
			}
			this.putDataElement2Context(icoll, context);
			
		} catch (Exception e) {
			EMPLog.log("QueryNodeByWfsignListOp", EMPLog.ERROR, EMPLog.ERROR, "根据流程标识查找节点列表出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
