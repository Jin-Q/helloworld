package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WFIGatherCommentVO;
import com.yucheng.cmis.platform.workflow.domain.WFIGatherInstanceVO;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>获取会办实例信息</p>
 * @author liuhw
 *
 */
public class GetGatherInstanceInfoOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		String currentUserId = null;
		String gatherInstanceId = null;
		String mainNodeId = null;
		String mainInstanceId = null;
		WorkflowServiceInterface wfi = null;
		WFIComponent wfiComponent = null;
		String appUrl = null; //申请信息URL
		int checkFlag = 0;  //校验会办参与人意见是否全部提交；0全部提交，其他否
		Map actionMap = new HashMap();  //权限设置
		try {
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			gatherInstanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			mainNodeId = (String) context.getDataValue("mainNodeId");
			mainInstanceId = (String) context.getDataValue("mainInstandeId");
			connection = this.getConnection(context);
			//获取会办实例
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			WFIGatherInstanceVO gatherInstance = wfi.getGatherInstanceInfo(gatherInstanceId, currentUserId, connection);
			//取流程意见
			List<WFIGatherCommentVO> gatherWfComment = wfi.getGatherComment(gatherInstanceId, currentUserId, "0", connection);
			//取会办意见
			List<WFIGatherCommentVO> gatherComment = wfi.getGatherComment(gatherInstanceId, currentUserId, "1", connection);
			//校验会办参与人意见是否全部提交
			WFIVO wfivo = wfi.wfCheckIsFinishGather(gatherInstanceId, currentUserId, connection);
			checkFlag = wfivo.getSign();
			//获取主流程业务信息页面
			/**
			 * 获取业务详情URL、审批变更URL、审批意见历史查看URL。四个组合条件匹配，优先级依次从高到低；一旦匹配成功，立即返回并停止。四个组合条件如下：
			 * 1.申请类型 + 节点ID + 范围【审批中111】
			 * 2.申请类型 + 节点ID + 范围【所有999】
			 * 3.申请类型 + 范围【审批中111】
			 * 4.申请类型 + 范围【所有999】
			 */
			wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
			KeyedCollection kcollWJ = wfiComponent.queryWfiJoin(mainInstanceId);
			String applType = (String) kcollWJ.getDataValue("appl_type");
			KeyedCollection kcollUrl = wfiComponent.getWf2bizConfByNode(applType, WorkFlowConstance.WFI_2BIZ_SCOPE_APPROVE, mainNodeId);
			boolean fromNode = false; //是否从节点字表获取URL
			if(kcollUrl==null || kcollUrl.getDataValue("appl_type")==null) {
				kcollUrl = wfiComponent.getWf2bizConfByNode(applType, WorkFlowConstance.WFI_2BIZ_SCOPE_ALL, mainNodeId);
			} else {
				fromNode = true;
			}
			if(kcollUrl==null || kcollUrl.getDataValue("appl_type")==null) {
				kcollUrl = wfiComponent.getWf2bizConfByApplType(applType, WorkFlowConstance.WFI_2BIZ_SCOPE_APPROVE);
			} else {
				fromNode = true;
			}
			if(kcollUrl==null || kcollUrl.getDataValue("appl_type")==null) {
				kcollUrl = wfiComponent.getWf2bizConfByApplType(applType, WorkFlowConstance.WFI_2BIZ_SCOPE_ALL);
			}
			if(kcollUrl==null || kcollUrl.getDataValue("appl_type")==null) {
				throw new EMPException("申请类型["+applType+"]获取业务详情URL、审批变更URL、审批意见历史查看URL失败。请确定是否设置好流程关联业务配置！");
			}
			if(fromNode) {
				appUrl = (String) kcollUrl.getDataValue("node_app_url");
				//当节点关联配置取值失败，取主表
				if(appUrl==null || "".equals(appUrl.trim())) {
					appUrl = (String) kcollUrl.getDataValue("wf_app_url");
				}
			} else {
				appUrl = (String) kcollUrl.getDataValue("wf_app_url");
			}
			if(appUrl==null || "".equals(appUrl.trim())) {
				throw new EMPException("申请类型["+applType+"]获取业务详情URL失败。请确定是否设置好流程关联业务配置！");
			}
			/**
			 * 参数处理。参数形式为pkVal=${pk_value}，pkVal为url中的参数名称，pk_value则为context中某个属性值或者接入表wfi_join的某个字段名称
			 */
			appUrl = wfiComponent.processURLParam(appUrl, null, kcollWJ);
			
			//权限设置
			String gatherStartUserID = gatherInstance.getGatherStartUserId();
			String gatherEndUserID = gatherInstance.getGatherEndUserId();
			String currentGatherUserList = gatherInstance.getCurrentGatherUserList()==null?"":gatherInstance.getCurrentGatherUserList();
			String currentGatherProcessors = gatherInstance.getCurrentGatherProcessors();
			String gatherEndTime = gatherInstance.getGatherEndTime();
			if(currentGatherUserList.contains(currentUserId)){
				//提交会办：会办发起人，没有权限
				if(currentUserId!=null && currentGatherUserList.contains(currentUserId+";") && gatherEndTime==null )
					actionMap.put("submitGather","true");
				//发起新会办：当前会办的发起人或是汇总人不能发起会办
				if(currentUserId!=null && !(currentUserId.equals(gatherStartUserID)||currentUserId.equals(gatherEndUserID)) && gatherEndTime==null)
					actionMap.put("newGather","true");
				//转办会办：当前会办的发起人或是汇总人不能 提交会办
				if(currentUserId!=null && !(currentUserId.equals(gatherStartUserID)||currentUserId.equals(gatherEndUserID)) && gatherEndTime==null)
					actionMap.put("changeGather","true");
			}
			//重置会办参与人：当前会办发起人才有权限
			if(currentUserId!=null && currentUserId.equals(gatherStartUserID) && (currentGatherProcessors==null||currentGatherProcessors.equals("")) && gatherEndTime==null)
				actionMap.put("resetGather","true");
			//结束会办：当前会办汇总人才有权限
			if(currentUserId!=null && currentUserId.equals(gatherEndUserID) && gatherEndTime==null)
				actionMap.put("endGather","true");
			
			context.put("gatherInstance", gatherInstance);
			context.put("gatherWfComment", gatherWfComment);
			context.put("gatherComment", gatherComment);
			context.put("checkFlag", checkFlag);
			context.put("appUrl", appUrl);
			context.put("actionMap", actionMap);
			
		} catch (Exception e) {
			EMPLog.log("GetGatherInstanceInfoOp", EMPLog.ERROR, EMPLog.ERROR, "获取会办实例信息出错。异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
