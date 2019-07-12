package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>发起子流程（子流程初始化）</p>
 * @author liuhw
 *
 */
public class SubmitSubWorkFlowOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		String retValue = "normal";
		String userId = null;  //当前登录人
        String mainInstanceId = null;  //实例号
        String mainNodeId = null;  //流程当前节点ID
        String subWfType = null;  //子流程类型0.不允许;1.用户选择同步子流;2.用户选择异步子流;3.系统指定同步子流;4.系统指定异步子流
        String subWfSign = null;  //子流程标识
        String applType = null;
        String orgId;  //当前机构
        String sysId;  //系统ID
        WorkflowServiceInterface wfi;
        Connection connection = null;
        try {
        	connection = this.getConnection(context);
        	userId = (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
        	orgId = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
        	sysId = (String)context.getDataValue(WorkFlowConstance.ATTR_SYSID);  //系统ID
        	mainInstanceId = (String)context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
        	mainNodeId = (String)context.getDataValue(WorkFlowConstance.ATTR_NODEID);
        	subWfType = (String) context.getDataValue("subWfType");
        	subWfSign = (String) context.getDataValue("subWfSign");
        	applType = (String) context.getDataValue(WorkFlowConstance.ATTR_APPLTYPE);
        	wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
            WFIVO wfivo = null;
            if(subWfType.equals("1")) {
            	wfivo = wfi.synSubFlowSetSubmit(mainInstanceId, mainNodeId, subWfSign, userId, orgId, sysId, connection);
            } else {
            	wfivo = wfi.asynSubFlowSetSubmit(mainInstanceId, mainNodeId, subWfSign, userId, applType, orgId, sysId, connection);
            }
            if(wfivo.getSign() == WFIVO.SIGN_SUCCESS) {
            	WFIComponent wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
        		KeyedCollection wfiJoin = wfiComponent.queryWfiJoin(mainInstanceId);
        		wfiJoin.setDataValue("main_instanceid", mainInstanceId);
        		TableModelDAO dao = this.getTableModelDAO(context);
            	if(subWfType.equals("1")) {
            		context.put("instanceId", mainInstanceId);
            		dao.update(wfiJoin, connection);
            	} else {
            		//与接入表建立关联
            		wfiJoin.setDataValue("instanceid", wfivo.getInstanceId());
            		wfiJoin.setDataValue("wfi_status", WorkFlowConstance.WFI_STATUS_INIT);
            		dao.insert(wfiJoin, connection);
            		context.put("instanceId", wfivo.getInstanceId());
            	}
            	
            	String subNextNodeId = (String) wfi.getWFPropertyByWfSign(subWfSign, "WFFirstNodeDocID");
            	context.put("nodeId", subNextNodeId);
            } else {
            	context.put("instanceId", "");
            	context.put("nodeId", "");
            }
            
        } catch (Exception e) {
			EMPLog.log("SubmitSubWorkFlowOp", EMPLog.ERROR, EMPLog.ERROR, "发起子流程出错；实例号：【"+mainInstanceId+"】，当前用户【"+userId+"】。异常信息："+e.getMessage());
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return retValue;
	}

}
