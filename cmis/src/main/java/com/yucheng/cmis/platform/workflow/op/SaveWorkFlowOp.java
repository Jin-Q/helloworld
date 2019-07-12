package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.PkGeneratorSet;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.domain.WfiBizConfigVO;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.TimeUtil;

/**
 * <p>保存流程</p>
 * 实现以下保存功能：<br>
 * <li>1.保存流程意见；
 * <li>2.保存审批变更信息；
 * <li>3.保存业务需要设置的流程变量到流程表单数据中
 * @author liuhw
 *
 */
public class SaveWorkFlowOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		String instanceId = null;
		String currentUserId = null, orgId = null;
		String loginuserid = null;
		Map paramMap = null;
		Connection connection = null;
		WorkflowServiceInterface wfi = null;
		try {
			connection = this.getConnection(context);
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			loginuserid = (String) context.getDataValue("loginuserid");
			orgId = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
			instanceId = (String) context.getDataValue("instanceId");
			String commentContent = (String) context.getDataValue("commentContent");
			String nodeId = (String) context.getDataValue("nodeId");
			
			if(commentContent!=null && !commentContent.trim().equals("")) {
				String commentSign = (String) context.getDataValue("commentSign");
//				if(commentSign==null || commentSign.trim().equals("")) {
//					commentSign = WorkFlowConstance.WFI_RESULT_AGREE;
//				}
				//保存意见
				/* added by yangzy 2014/12/03 需求：XD140718026_新信贷系统授信进度查询改造 start  */
				if(commentSign!=null&&!"".equals(commentSign)&&"30".equals(commentSign)){
					TableModelDAO dao = this.getTableModelDAO(context);
					String currentUserName = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME);
					commentContent = commentContent.replace("somebody", currentUserName);
					IndexedCollection icoll = dao.queryList("WfiWorklistTodo", " where INSTANCEID = '"+instanceId+"' ", connection);
					if(icoll!=null&&icoll.size()>0){
						KeyedCollection kc = (KeyedCollection) icoll.get(0);
						if(kc!=null&&kc.getDataValue("currentnodeuser")!=null&&!"".equals(kc.getDataValue("currentnodeuser"))){
							currentUserId = (String) kc.getDataValue("currentnodeuser");
							currentUserId = currentUserId.replace(";", "");
						}
					}
				}
				/* added by yangzy 2014/12/03 需求：XD140718026_新信贷系统授信进度查询改造 end  */
	            if(loginuserid!=null&&!loginuserid.equals(currentUserId)){
	            	String loginusername = context.getDataValue("loginusername")+"(受托于"+context.getDataValue("currentUserName")+"办理)";
	            	wfi.saveWFComment4CusTrustee(null, instanceId, nodeId, currentUserId,loginusername, commentSign, commentContent, orgId, connection);
	            }else{
	            	wfi.saveWFComment(null, instanceId, nodeId, currentUserId, commentSign, commentContent, orgId, connection);
	            }
	            /* added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 start  */
				if(context.containsKey("commentContentOther")){
					WFIComponent wfiComponent1 = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
					String commentid = wfiComponent1.getCurrentCommId(instanceId, currentUserId,connection, nodeId);
					String commentContentOther = (String) context.getDataValue("commentContentOther");
					String timeStr = TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
					String condition = "WHERE INSTANCEID='"+instanceId+"' AND NODEID='"+nodeId+"' AND INPUT_ID='"+currentUserId+"' AND COMMENTID='"+commentid+"'";
					TableModelDAO dao = this.getTableModelDAO(context);
					IndexedCollection icoll = dao.queryList("WfiBizCommentRecord", condition, connection);
					if(icoll!=null&&icoll.size()>0){
						KeyedCollection kcoll = (KeyedCollection) icoll.get(0);
						if(kcoll!=null){
							kcoll.put("commentcontent", commentContentOther);
							dao.update(kcoll, connection);
						}
					}else{
						if(commentContent!=null && !commentContent.trim().equals("")) {
							PkGeneratorSet pkservice = (PkGeneratorSet) context.getService(CMISConstance.ATTR_PRIMARYKEYSERVICE);
							UNIDGenerator pk = (UNIDGenerator) pkservice.getGenerator("UNID");
							Map<String,String> insertMap = new HashedMap();
							insertMap.put("pk1", pk.getUNID());
							insertMap.put("instanceid", instanceId);
							insertMap.put("nodeid", nodeId);
							insertMap.put("commentcontent", commentContentOther);
							insertMap.put("input_id", currentUserId);
							insertMap.put("input_br_id", orgId);
							insertMap.put("commentid", commentid);
							insertMap.put("op_time", timeStr);
							SqlClient.insert("insertWfiBizCommentRecord", insertMap, connection);
						}
					}
				}
				/* added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 end  */
			}
			
			boolean firstNode = (Boolean) wfi.getWFNodeProperty(nodeId, "isFirstNode");
			KeyedCollection varVal = null;
			KeyedCollection varType = null;
			KeyedCollection varDisp = null;
			KeyedCollection varNm = null;
			try {
				//只有不是第一个节点才可能出现审批变更要素的保存
				if(!firstNode) {
					WFIComponent wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
					if(context.containsKey("WfiBizVarRecord")) {
						varVal =(KeyedCollection) context.getDataElement("WfiBizVarRecord");
						varType = (KeyedCollection)context.getDataElement("WfiVarType");
						varDisp = (KeyedCollection)context.getDataElement("WfiVarDisp");
						varNm = (KeyedCollection)context.getDataElement("WfiVarName");
						//获取到的最新一次操作的流程意见ID 泉州需要所做的新增 把业务信息变更和对应的流程意见关联起来保存到业务变更的表中
						String commentid = wfiComponent.getCurrentCommId(instanceId, currentUserId, connection, nodeId); 
						//先清除当前暂存的历史记录
						wfiComponent.clearWfiVariable(instanceId, nodeId, currentUserId, commentid);
						//保存（暂存）
						//旧版wfiComponent.saveBizVariable(varVal, varNm, varType, varDisp);
						wfiComponent.saveBizVariable(varVal, varNm, varType, varDisp,commentid);
					}
					//保存时存储commentid
					if(context.containsKey("isSpecial")&&context.getDataValue("isSpecial")!=null&&!"".equals(context.getDataValue("isSpecial"))){
						String commentid = wfiComponent.getCurrentCommId(instanceId, currentUserId,connection, nodeId);
						KeyedCollection kCollPara = new KeyedCollection();
						kCollPara.put("instanceid", instanceId);
						kCollPara.put("nodeid", nodeId);
						SqlClient.update("updateWfiBizVarRecord", kCollPara, commentid, null, connection);
					}
				}
			} catch (Exception e) {
				EMPLog.log("SaveWorkFlowOp", EMPLog.WARNING, EMPLog.WARNING, "试图保存审批变更信息出错！异常信息："+e.getMessage(), e);
			}
			
			//调用流程审批业务处理接口设置业务数据至流程变量之中
			String applType = (String) context.getDataValue("applType");
			WfiBizConfigVO bizConfigVO = WorkFlowUtil.getBizInterfaceId(applType);
			String bizIf = bizConfigVO.getBizInterfaceId();
			String tabModelId = (String) context.getDataValue("modelId");
			String pkVal = (String) context.getDataValue("pkVal");
			
			if(!bizIf.equals(WorkFlowConstance.WFI_BIZIF_BLANK)) {
				BIZProcessInterface wfiBizComp = (BIZProcessInterface)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(bizIf, context, connection);
				paramMap = wfiBizComp.putBizData2WfVar(tabModelId, pkVal, varVal);
			} else {
				EMPLog.log("SaveWorkFlowOp", EMPLog.WARNING, EMPLog.WARNING, "申请类型["+applType+"]没有配置流程审批业务处理接口，将不做任何业务逻辑处理！");
			}
			if(paramMap == null) {
				paramMap = new HashMap();
			}
			//默认将EMPContext内容保存到流程表单中
			paramMap.put(WorkFlowConstance.ATTR_EMPCONTEXT, context);
			WFIVO wfivo = wfi.wfSaveJob(instanceId, currentUserId, orgId, paramMap, connection);
			int sign = wfivo.getSign();
			if(sign == WFIVO.SIGN_SUCCESS) {
				context.put("flag", sign);
			} else {
				context.put("flag", wfivo.getMessage());
			}
			
		} catch (Exception e) {
			EMPLog.log("SaveWorkFlowOp", EMPLog.ERROR, EMPLog.ERROR, "保存流程出错！异常信息为："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}
	
}
