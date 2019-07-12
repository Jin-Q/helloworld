package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO;
import com.yucheng.cmis.platform.workflow.domain.WfiWorkflow2biz;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>获取流程实例详情</p>
 * 执行以下处理：<br>
 * <li>1.调用流程接入接口获取流程实例信息；
 * <li>2.根据流程关联业务配置解析申请信息URL、审批变更页面;
 * @author liuhw
 *
 */
public class GetInstancePropOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		String retValue = "view";
		Connection connection = null;
		String instanceId = null;
		String applType = null;
		String currentUserId = null;
		String nodeId = null;
		String appUrl = null; //申请信息URL
		String bizUrl = null; //审批变更业务要素修改JSP页面URL
		String preventList = null;
		WorkflowServiceInterface wfi = null;
		WFIComponent wfiComponent = null;
		try {
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			applType = (String) context.getDataValue(WorkFlowConstance.ATTR_APPLTYPE);
			nodeId = (String) context.get(WorkFlowConstance.ATTR_NODEID);
			connection = this.getConnection(context);
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			//获取实例信息
			WFIInstanceVO instanceVO = wfi.getInstanceInfo(instanceId, currentUserId, nodeId, connection);
			/**
			 * 根据当前登录人是否当前节点办理人或者节点待签收，判断返回页面
			 */
			String nodeUserId = instanceVO.getCurrentNodeUser();
			String wfStatus = instanceVO.getWfStatus(); //工作流状态0：流转中；1：流程结束；2：流程挂起；3：流程异常中止；4：预结束；5：流程过期办理
			String nodeStatus = instanceVO.getNodeStatus();
			if((nodeId!=null&&!nodeId.equals("")) && ((nodeUserId!=null && (nodeUserId.equals(currentUserId)||
					nodeUserId.equals(currentUserId+";")||
					nodeUserId.startsWith(currentUserId+";")||
					nodeUserId.contains(";"+currentUserId+";")||
					nodeUserId.endsWith(";"+currentUserId)||
					nodeUserId.endsWith(";"+currentUserId+";")) && 
					(wfStatus.equals("0")||wfStatus.equals("5"))) || nodeStatus.equals("3"))) {
				retValue = "approve";
			}
			
			wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
			KeyedCollection wfiJoin = null;
			if("1".equals(wfStatus) || "3".equals(wfStatus)) {
				wfiJoin = wfiComponent.queryWfiJoinHis(instanceId);
			} else {
				wfiJoin = wfiComponent.queryWfiJoin(instanceId);
			}
			String wfiStatus = (String) wfiJoin.getDataValue("wfi_status"); //流程审批状态
			String sceneScope = WorkFlowConstance.WFI_2BIZ_SCOPE_ALL;
			if(WorkFlowConstance.WFI_STATUS_PASS.equals(wfiStatus)) {
				sceneScope = WorkFlowConstance.WFI_2BIZ_SCOPE_PASS;
			} else if(WorkFlowConstance.WFI_STATUS_DENIAL.equals(wfiStatus)) {
				sceneScope = WorkFlowConstance.WFI_2BIZ_SCOPE_DENIAL;
			} else if(WorkFlowConstance.WFI_STATUS_APPROVE.equals(wfiStatus)) {
				sceneScope = WorkFlowConstance.WFI_2BIZ_SCOPE_APPROVE;
			}
			WfiWorkflow2biz wf2bizConf = wfiComponent.getWf2bizConf(applType, nodeId, sceneScope);
			appUrl = wf2bizConf.getAppUrl();
			bizUrl = wf2bizConf.getBizUrl();
			if(appUrl==null || "".equals(appUrl.trim())) {
				throw new EMPException("申请类型["+applType+"]获取业务详情URL失败。请确定是否设置好流程关联业务配置！");
			}
			if(bizUrl!=null && bizUrl.trim().length()>0) {
				//获取已经保存的审批变更，用于回显
				String condition = "WHERE INSTANCEID='"+instanceId+"' AND NODEID='"+nodeId+"' AND INPUT_ID='"+currentUserId+"'";
				TableModelDAO dao = this.getTableModelDAO(context);
				IndexedCollection icoll = dao.queryList("WfiBizVarRecord", condition, connection);
				KeyedCollection varRecordKcoll = new KeyedCollection("WfiBizVarRecord");
				for(int i=0; i<icoll.size(); i++) {
					KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
					String varKey = (String) kcoll.getDataValue("var_key");
					String varValue = (String) kcoll.getDataValue("var_value");
					varRecordKcoll.put(varKey, varValue);				
				}
				this.putDataElement2Context(varRecordKcoll, context);
			}
			
			preventList = wf2bizConf.getPreventList(); //风险拦截列表
			
			/**
			 * 参数处理。参数形式为pkVal=${pk_value}，pkVal为url中的参数名称，pk_value则为context中某个属性值或者接入表wfi_join的某个字段名称
			 */
			appUrl = wfiComponent.processURLParam(appUrl, null, wfiJoin);
			bizUrl = wfiComponent.processURLParam(bizUrl, null, wfiJoin);
			
			context.put("wfAppUrl", appUrl);
			context.put("wfBizUrl", bizUrl);
			context.put("preventList", preventList);
			context.put("wfiInstanceVO", instanceVO);
			
			//用于前台表单赋值
			context.put(WorkFlowConstance.ATTR_INSTANCEID, instanceId);
			context.put("wfSign", instanceVO.getWfSign());
			context.put("applType", applType);
			context.put("modelId", wfiJoin.getDataValue("table_name"));
			context.put("statusName", wfiJoin.getDataValue("status_name"));
			context.put("pkVal", wfiJoin.getDataValue("pk_value"));
			context.put("pkCol", wfiJoin.getDataValue("pk_col"));
			context.put("mainInstanceId", instanceVO.getMainInstanceId());
			context.put("instanceId", instanceVO.getInstanceId());
			context.put("nodeId", (nodeId!=null&&!nodeId.equals("null")&&!nodeId.equals(""))?nodeId : instanceVO.getNodeId());
			if("approve".equals(retValue)) {
				context.put(WorkFlowConstance.ATTR_NODEID, nodeId);
				context.put("isProcessed", instanceVO.getIsProcessed());
				context.put("isDraft", instanceVO.getIsdraft());
				context.put("scene", wfi.getWFNodeExtProperty(nodeId, WorkFlowConstance.NODE_EXT_SCENE));
				context.put("mainNodeId", instanceVO.getMainNodeId());
				context.put("nodeName", instanceVO.getNodeName());
			}
			
		} catch (Exception e) {
			EMPLog.log("GetInstanceInfoOp", EMPLog.ERROR, EMPLog.ERROR, "获取流程实例详情出错。异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return retValue;
	}

}
