package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.TimeUtil;

/**
 * <p>异常业务重新请求处理</p>
 * @author liuhw
 *
 */
public class ProcBizExcetionOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		String msgId = null;
		Connection connection = null;
		WorkflowServiceInterface wfi = null;
		try {
			msgId = (String) context.getDataValue("msgid");
		} catch (Exception e) {
			throw new EMPException("异常业务重新请求处理失败！原因：获取参数[msgid]失败。");
		}
		try {
			connection = this.getConnection(context);
			WFIComponent wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
			boolean his = false;  //是否历史表记录
			WfiMsgQueue msgQueue = wfiComponent.queryWfiMsgQueueById(msgId);
			if(msgQueue==null || msgQueue.getMsgid()==null || msgQueue.getMsgid().equals("")) {
				msgQueue = wfiComponent.queryWfiMsgQueueHisById(msgId);
				his = true;
			}
			String applType = msgQueue.getApplType();
			String bizIfName = WorkFlowUtil.getBizInterfaceId(applType).getBizInterfaceId();
			String wfiStatus = msgQueue.getWfiStatus();
			String wfiResult = msgQueue.getWfiResult();
			String instanceId = msgQueue.getInstanceid();
			String nodeId = msgQueue.getNodeid();
			String pkVal = msgQueue.getPkValue();
			KeyedCollection kcollResult = new KeyedCollection("result");
			if(!bizIfName.equals(WorkFlowConstance.WFI_BIZIF_BLANK)) {
				wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
				BIZProcessInterface wfiBizComp = (BIZProcessInterface) CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(bizIfName, context, connection);
			
				if(WorkFlowConstance.WFI_STATUS_PASS.equals(wfiStatus) || WorkFlowConstance.WFI_STATUS_DENIAL.equals(wfiStatus)) {
					if(WorkFlowConstance.WFI_STATUS_PASS.equals(wfiStatus)) {
						wfiBizComp.executeAtWFAgree(msgQueue);
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "审批通过，异常业务处理成功！申请流水号："+pkVal);
					} else {
						wfiBizComp.executeAtWFDisagree(msgQueue);
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "审批被否决，异常业务处理完毕！申请流水号："+pkVal);
					}
					//迁移数据到历史表
					wfiComponent.transDataWfi(instanceId, connection);
					his = true; //标识被迁移到his表中
				} else {
					if(WorkFlowConstance.WFI_STATUS_AGAIN.equals(wfiStatus)) {
						wfiBizComp.executeAtTakeback(msgQueue);
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "审批被拿回，异常业务处理完毕！申请流水号："+pkVal);
						
					} else if(WorkFlowConstance.WFI_STATUS_BACK.equals(wfiStatus)) {
						wfiBizComp.executeAtCallback(msgQueue);
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "审批被打回，异常业务处理完毕！申请流水号："+pkVal);
						
					} else {
						//当前办理节点是否配置了需要业务处理
						String isProcess = wfi.getWFNodeExtProperty(nodeId, WorkFlowConstance.NODE_EXT_IS_PROCESS);
						if (WorkFlowConstance.WFI_RESULT_AGREE.equals(wfiResult) && isProcess != null && isProcess.equals("1")) { //同意且节点需做业务逻辑处理
							wfiBizComp.executeAtWFAppProcess(msgQueue);
							EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "审批中，异常业务处理成功！申请流水号："+pkVal);
						}
					}
				}
				kcollResult.put("flag", "1");
				kcollResult.put("msg", "操作成功！业务处理接口调用成功，消息将关闭！");
			} else {
				kcollResult.put("flag", "1");
				kcollResult.put("msg", "没有配置业务处理接口，默认处理成功，消息将关闭！");
			}
			msgQueue.setOpstatus(WorkFlowConstance.WFI_MSG_OPSTATUS_END);
			String optime = TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
			msgQueue.setOptime(optime);
			//关闭消息
			wfiComponent.updateWfiMesQueue(msgQueue, his);
			this.putDataElement2Context(kcollResult, context);
			
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {}
			KeyedCollection kcollResult = new KeyedCollection("result");
			e.printStackTrace();
			//EMPLog.log("ProcBizExcetionOp", EMPLog.ERROR, EMPLog.ERROR, "异常业务重新请求处理出错！异常信息为："+e.getMessage(), e);
			kcollResult.put("flag", "fail");
			kcollResult.put("msg", e.getMessage());
			this.putDataElement2Context(kcollResult, context);
			//throw new EMPException("异常业务重新请求处理出错！异常信息为："+e.getMessage());
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}

		return "normal";
	}

}
