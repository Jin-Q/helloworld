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
 * 根据流程意见获取某一环节审批变更详细
 * 获取到的最新一次操作的流程意见ID 泉州需要所做的新增 把业务信息变更和对应的流程意见关联起来保存到业务变更的表中
 * @author cyg
 *
 */
public class GetWfiBizVarDetailByCommentIdOp extends CMISOperation {

	private final String modelId = "WfiBizVarRecord";
	private final String modelIdHis = "WfiBizVarRecordHis";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		
		String instanceId, nodeId, commentId;
		try {
			instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			nodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NODEID);
			commentId = (String) context.getDataValue("commentid");
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("根据流程意见ID获取某一环节审批变更详细，获取参数失败！");
		}
		String condition = "WHERE COMMENTID='"+commentId+"' AND NODEID='"+nodeId+"'";
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
				String varOldValue = (String) kcoll.getDataValue("var_old_value");
				varRecordKcoll.put(varKey, varValue);
				varRecordKcoll.put(varKey+"_old", varOldValue);
			}
			this.putDataElement2Context(varRecordKcoll, context);
			//获取变更页面地址
			WFIComponent wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
			KeyedCollection kcoll = wfiComponent.queryWfiJoin(instanceId);
			if(kcoll==null||kcoll.getDataValue("instanceid")==null||kcoll.getDataValue("instanceid").equals("null")) {
				kcoll = wfiComponent.queryWfiJoinHis(instanceId);
			}
			String applType = (String) kcoll.getDataValue("appl_type");
			String wfsign = (String) kcoll.getDataValue("wfsign");
			WfiWorkflow2biz wf2bizConf = wfiComponent.getWf2bizConfBySign(applType, wfsign, nodeId, WorkFlowConstance.WFI_2BIZ_SCOPE_ALL);
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
