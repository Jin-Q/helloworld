package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WfiWorkflow2biz;
import com.yucheng.cmis.pub.CMISComponentFactory;

/**
 * 根据实例号、节点、办理人、时间获取某一环节审批变更详细
 * @author liuhw
 *
 */
public class GetWfiBizVarDetailOp extends CMISOperation {

	private final String modelId = "WfiBizVarRecord";
	private final String modelIdHis = "WfiBizVarRecordHis";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		
		String instanceId, nodeId, userId, opTime;
		try {
			instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			nodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NODEID);
			userId = (String) context.getDataValue("userId");
			opTime = (String) context.getDataValue("opTime");
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("根据实例号、节点、办理人、时间获取某一环节审批变更详细，获取参数失败！");
		}
		String condition = "WHERE INSTANCEID='"+instanceId+"' AND NODEID='"+nodeId+"' AND OP_TIME='"+opTime+"' AND INPUT_ID='"+userId+"'";
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection icoll = dao.queryList(modelId, condition, connection);
			if(icoll==null || icoll.size()<=0) {
				icoll = dao.queryList(modelIdHis, condition, connection);
			}
			if(icoll==null || icoll.size()<=0) {
				throw new EMPException("获取审批变更详细出错，请确定输入的参数是否正确。");
			}
			KeyedCollection varRecordKcoll = new KeyedCollection("WfiBizVarRecord");
			for(int i=0; i<icoll.size(); i++) {
				KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
				String varKey = (String) kcoll.getDataValue("var_key");
				String varValue = (String) kcoll.getDataValue("var_value");
				varRecordKcoll.put(varKey, varValue);				
			}
			this.putDataElement2Context(varRecordKcoll, context);
			//获取变更页面地址
			WFIComponent wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
			KeyedCollection kcoll = wfiComponent.queryWfiJoin(instanceId);
			if(kcoll==null||kcoll.getDataValue("instanceid")==null||kcoll.getDataValue("instanceid").equals("null")) {
				kcoll = wfiComponent.queryWfiJoinHis(instanceId);
			}
			String applType = (String) kcoll.getDataValue("appl_type");
			WfiWorkflow2biz wf2bizConf = wfiComponent.getWf2bizConf(applType, nodeId, WorkFlowConstance.WFI_2BIZ_SCOPE_ALL);
			String bizUrl = wf2bizConf.getBizUrl();
			context.put("wfBizUrl", bizUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
