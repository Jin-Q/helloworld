package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.domain.WfiSignTask;
import com.yucheng.cmis.platform.workflow.meetingsign.component.WfiSignComponent;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

/**
 * <p>流程提交</p>
 * @author liuhw
 *
 */
public class SubmitWorkFlowOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		String retValue = "normal";
		String userId = null;  //当前登录人
        String instanceId = null;  //实例号
        String nodeId;  //流程当前节点ID
        String nextNodeID;  //流程下一节点ID
        String nextNodeUser;  //流程下一办理人
        String nextAnnouceUser;  //抄送人员
        String entrustModel;  //委托代办模式：0代办人办理，1原办理人代人都可以办理，2原办理人办理
        String orgid;  //当前机构
        
        WorkflowServiceInterface wfi;
        Map paramMap;
        Connection connection = null;
        try {
        	connection = this.getConnection(context);
        	userId = (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
            instanceId = (String)context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
        	wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
            nodeId = (String)context.getDataValue(WorkFlowConstance.ATTR_NODEID);
            nextNodeID = (String)context.getDataValue(WorkFlowConstance.ATTR_NEXTNODEID);
            nextNodeUser = (String)context.getDataValue(WorkFlowConstance.ATTR_NEXTNODEUSER);
            nextAnnouceUser = (String)context.getDataValue(WorkFlowConstance.ATTR_NEXTANNOUCEUSER);
            entrustModel = (String) context.getDataValue("entrustModel");
            orgid = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
            paramMap = new HashMap();
            paramMap.put(WorkFlowConstance.ATTR_EMPCONTEXT, context);  //设置emp.context（必须）
            WFIVO wfiVo = wfi.wfCompleteJob(instanceId, nodeId, userId, nextNodeID, nextNodeUser, nextAnnouceUser, entrustModel, paramMap, orgid, connection);
            /** 流程提交时若该节点为发起节点则删除流程中审批变更信息 2014-3-10 */
            if(nodeId!=null&&!"".equals(nodeId)){
				boolean isFirstNode = (Boolean) wfi.getWFNodeProperty(nodeId, "isFirstNode");
				if(isFirstNode){
					SqlClient.delete("delWfiBizVarByInstanceId", instanceId, connection);
					/** added by yangzy 20150908 需求编号：【XD150303015】普通贷款出账，展期出账流程提交时，回收放款退回修改权限 start*/
		            String wfSign = (String) context.getDataValue("wfSign");
		            String pkVal = (String) context.getDataValue("pkVal");
		        	if(wfSign!=null&&"wfi_pvp_extension".equals(wfSign)){
		        		SqlClient.executeUpd("updatePvpExtensionModifyRight", pkVal, "0", null , connection);
		        	}else if(wfSign!=null&&"wfi_pvp_loan_app".equals(wfSign)){
		        		SqlClient.executeUpd("updatePvpLoanModifyRight", pkVal, "0", null , connection);
		        	}
		            /** added by yangzy 20150908 需求编号：【XD150303015】普通贷款出账，展期出账流程提交时，回收放款退回修改权限 end*/
				}
			}
            String subWfType = (String) context.getDataValue("subWfType");
            if("2".equals(subWfType)) { //异步子流程，暂时不考虑异步子流程的第二个节点是会签节点（如果是则需进行会签初始化，并且也要考虑前台jsp页面显示）
            	if(wfiVo.getSign() == WFIVO.SIGN_SUCCESS) {
            		context.put("flag", WFIVO.SIGN_SUCCESS);
            	} else {
            		context.put("flag", wfiVo.getMessage());
            	}
            	retValue = "syn";
            } else {
            	String signConfig = "0";
            	if(nextNodeID.indexOf("@")==-1 && nextNodeID.indexOf("e")==-1) { //目前只考虑后续有且只有一个节点才认为可能是会签节点
            		signConfig = wfi.getWFNodeExtProperty(nextNodeID, WorkFlowConstance.NODE_EXT_PROPERTY_SIGNCONFIG);
            	}
            	if("0".equals(signConfig)) { //非会签节点
            		context.put(WorkFlowConstance.WFVO_RET_NAME, wfiVo);
            	} else { //签节点处理，会签初始化
            		WfiSignComponent wfiSignComp = (WfiSignComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID4SIGN, context, connection);
            		WfiSignTask wfiSignTask = wfiSignComp.initSignTask(instanceId);
            		context.put("st_task_id", wfiSignTask.getStTaskId());
        			retValue = "sign";
            	}
            }
            
        } catch (Exception e) {
			EMPLog.log("SubmitWorkFlowOp", EMPLog.ERROR, EMPLog.ERROR, "提交流程出错；实例号：【"+instanceId+"】，当前用户【"+userId+"】。异常信息："+e.getMessage());
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return retValue;
		
	}

}
