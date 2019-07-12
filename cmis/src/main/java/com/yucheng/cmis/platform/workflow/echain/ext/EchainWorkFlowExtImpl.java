package com.yucheng.cmis.platform.workflow.echain.ext;

import java.sql.Connection;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.ecc.echain.db.DbControl;
import com.ecc.echain.ext.AppExtIF;
import com.ecc.echain.util.Field;
import com.ecc.echain.workflow.engine.EVO;
import com.ecc.echain.workflow.model.GatherVO;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO;
import com.yucheng.cmis.platform.workflow.domain.WfiBizConfigVO;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.exception.WFIException;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.TimeUtil;

/**
 * echain工作流扩展实现类
 * @author liuhw
 */

public class EchainWorkFlowExtImpl implements AppExtIF{
	
	/**
	 * 信贷流程引擎服务接入接口
	 */
	private WorkflowServiceInterface wfi = null;
	/**
	 * echain引擎与业务处理是否作同一事务处理标识。是true，否false；
	 */
	private boolean SAME_TRANSACTION = false;
	
	public EchainWorkFlowExtImpl() throws Exception {
		try {
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log("EchainWorkFlowExtImpl", EMPLog.ERROR, EMPLog.ERROR, "流程引擎接入实现类实例化失败！");
			throw e;
		}
	}
	
	/**
	 * 实例初始化前执行
	 */
	public Object beforeInit(EVO evo, Map hm) throws Exception {
		return null;
	}

	/**
	 * 实例初始后执行
	 */
	public Object afterInit(EVO evo, Map hm) throws Exception {
		return null;
	}

	/**
	 * 实例保存后执行
	 */
	public Object afterSave(EVO evo, Map hm) throws Exception {
		return null;
	}

	/**
	 * 流程提交前执行
	 */
	public Object beforeSubmit(EVO evo, Map hm) throws Exception {
		return null;
	}

	/**
	 * <p>流程提交后执行处理</p>
	 * 需要执行的操作有：<br>
	 * <li>1.更新业务流程审批状态；
	 * <li>2.更新流程接入表审批状态；
	 * <li>3.调用流程审批中执行业务处理逻辑处理；
	 */
	public Object afterSubmit(EVO evo, Map hm) throws Exception {
		
		Context context = (Context) evo.paramMap.get(WorkFlowConstance.ATTR_EMPCONTEXT);
		String instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
		String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
		WFIInstanceVO instanceVo = wfi.getInstanceInfo(instanceId, currentUserId, null, evo.getConnection());
		if(instanceVo.getMainInstanceId()!=null && !instanceVo.getMainInstanceId().equals("") && !instanceVo.getMainInstanceId().equals("null")) { //子流程提交，暂时采用忽略后处理
			return null;
		}
		//通过流程实例 号去取表名和业务ID
		DbControl db = DbControl.getInstance();
		Vector vecFields = new Vector();
		//更新接入表审批状态
		vecFields.addElement(new Field("WFI_STATUS", WorkFlowConstance.WFI_STATUS_APPROVE));
		db.doUpdate("WFI_JOIN", vecFields, "INSTANCEID='"+instanceId+"'", evo.getConnection());
		String nodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NODEID);
		//节点是否处理业务逻辑 0.不处理;1.处理
		String isProcess = wfi.getWFNodeExtProperty(nodeId, WorkFlowConstance.NODE_EXT_IS_PROCESS);
		if(isProcess!=null && isProcess.equals("1")){
			WfiMsgQueue wfiMsgQueue=this.sendMsg2Queue(evo);
			this.handleBiz(evo, wfiMsgQueue);
		}
		/* added by yangzy 2014/11/10 流程抄送改造，流程通过后添加抄送人员 start */
		String strSql =  " SELECT LISTAGG(EXV100, ';') WITHIN                                         "
						+"  GROUP(                                                                    "
						+"  ORDER BY INSTANCEID) AS EXV100                                            "
						+"   FROM (SELECT DISTINCT A.INSTANCEID, A.EXV100                             "
						+"           FROM (SELECT INSTANCEID,                                         "
						+"                        SUBSTR(EXV100,                                      "
						+"                               INSTR(EXV100, ';', 1, LV) + 1,               "
						+"                               INSTR(EXV100, ';', 1, LV + 1) -              "
						+"                               INSTR(EXV100, ';', 1, LV) - 1) AS EXV100     "
						+"                   FROM (SELECT WF.INSTANCEID,                              "
						+"                                ';' || WF.EXV100 || ';' AS EXV100,          "
						+"                                LENGTH(WF.EXV100) -                         "
						+"                                LENGTH(REPLACE(WF.EXV100, ';', '')) AS LEN, "
						+"                                LV                                          "
						+"                           FROM (select INSTANCEID, EXV100                  "
						+"                                   from WF_INSTANCE_WHOLE_PROPERTY          "
						+"                                  where INSTANCEID = '"+instanceId+"') WF,  "
						+"                                (SELECT LEVEL AS LV                         "
						+"                                   FROM DUAL                                "
						+"                                 CONNECT BY LEVEL < 300) DL                 "
						+"                          WHERE WF.EXV100 IS NOT NULL)                      "
						+"                  WHERE LEN >= LV - 1) A) B                                 ";
		Vector vecWfiApproveCount = db.performQuery(strSql, evo.getConnection());
		String annouceUserStr = "";
		String annouceUser = "";
		if (vecWfiApproveCount != null && vecWfiApproveCount.size() > 0) {
			Vector vecRow = (Vector) vecWfiApproveCount.get(0);
			if(vecRow.elementAt(0)!=null&&!"null".equals(vecRow.elementAt(0))&&!"".equals(vecRow.elementAt(0))){
				annouceUserStr = (String) vecRow.elementAt(0);
			}
		}
		if(context.containsKey("nextAnnouceUser")&&context.getDataValue("nextAnnouceUser")!=null&&!"".equals(context.getDataValue("nextAnnouceUser"))){
			annouceUser = (String) context.getDataValue("nextAnnouceUser");
			if(!"".equals(annouceUserStr)){
				annouceUserStr+=";"+annouceUser;
			}else{
				annouceUserStr+=annouceUser;
			}
		}
		Vector vecFields1 = new Vector();
		vecFields1.addElement(new Field("EXV100", annouceUserStr));
		db.doUpdate("WF_INSTANCE_WHOLE_PROPERTY", vecFields1, "INSTANCEID='"+instanceId+"'", evo.getConnection());
		/* added by yangzy 2014/11/10 流程抄送改造，流程通过后添加抄送人员 end */
		//主键，字段名，业务表审批状态字段名
		String pkCol = (String) context.getDataValue("pkCol");
		String pkVal = (String) context.getDataValue("pkVal");
		String statusName = (String) context.getDataValue("statusName");
		String modelId = (String) context.getDataValue("modelId");
		TableModelLoader model = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
		//物理表名
		String tableName = model.getTableModel(modelId).getDbTableName();
		vecFields.clear();
		//更新业务表流程审批状态（将更新业务表审批状态放到最后业务处理接口完成后做，防止出现表死锁）
		vecFields.addElement(new Field(statusName, WorkFlowConstance.WFI_STATUS_APPROVE));
		db.doUpdate(tableName, vecFields, pkCol+"='"+pkVal+"'", evo.getConnection());
		return null;
	}
	
	/**
	 * <p>流程退回后执行的处理</p>
	 * <li>1.如果退回到第一个节点，则更新业务表审批状态为打回（退回操作也使用打回状态）
	 * <li>2.如果退回到第一个节点，更新接入表审批状态为打回
	 * <li>3.如果退回到第一个节点，调用业务处理接口
	 */
	public Object afterReturnBack(EVO evo, Map hm) throws Exception {
		Context context = (Context) evo.paramMap.get(WorkFlowConstance.ATTR_EMPCONTEXT);
		String instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
		String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
		WFIInstanceVO instanceVo = wfi.getInstanceInfo(instanceId, currentUserId, null, evo.getConnection());
		if(instanceVo.getMainInstanceId()!=null && !instanceVo.getMainInstanceId().equals("") && !instanceVo.getMainInstanceId().equals("null")) { //子流程提交，暂时采用忽略后处理
			return null;
		}
		String nextNodeId = evo.getNextNodeID();
		boolean isFirstNode = (Boolean) wfi.getWFNodeProperty(nextNodeId, "isFirstNode");
		if(isFirstNode) {
			DbControl db = DbControl.getInstance();
			Vector vecFields = new Vector();
			vecFields.addElement(new Field("WFI_STATUS", WorkFlowConstance.WFI_STATUS_BACK));
			db.doUpdate("WFI_JOIN", vecFields, "INSTANCEID='"+instanceId+"'", evo.getConnection());
			WfiMsgQueue wfiMsgQueue=this.sendMsg2Queue(evo);
			this.handleBiz(evo, wfiMsgQueue);
			
			String pkCol = (String) context.getDataValue("pkCol");
			String pkVal = (String) context.getDataValue("pkVal");
			String statusName = (String) context.getDataValue("statusName");
			String modelId = (String) context.getDataValue("modelId");
			TableModelLoader model = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
			String tableName = model.getTableModel(modelId).getDbTableName();
			vecFields.clear();
			vecFields.addElement(new Field(statusName, WorkFlowConstance.WFI_STATUS_BACK));
			db.doUpdate(tableName, vecFields, pkCol+"='"+pkVal+"'", evo.getConnection());
		}
		return null;
	}
	
	/**
	 * <p>打回后执行的处理</p>
	 * <li>1.如果退回到第一个节点，则更新业务表审批状态为打回
	 * <li>2.如果退回到第一个节点，更新接入表审批状态为打回
	 * <li>3.如果退回到第一个节点，调用业务处理接口
	 */
	public Object afterCallBack(EVO evo, Map hm) throws Exception {
		Context context = (Context) evo.paramMap.get(WorkFlowConstance.ATTR_EMPCONTEXT);
		String instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
		String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
		WFIInstanceVO instanceVo = wfi.getInstanceInfo(instanceId, currentUserId, null, evo.getConnection());
		if(instanceVo.getMainInstanceId()!=null && !instanceVo.getMainInstanceId().equals("") && !instanceVo.getMainInstanceId().equals("null")) { //子流程提交，暂时采用忽略后处理
			return null;
		}
		String nextNodeId = evo.getNextNodeID();
		boolean isFirstNode = (Boolean) wfi.getWFNodeProperty(nextNodeId, "isFirstNode");
		if(isFirstNode) {
			
			DbControl db = DbControl.getInstance();
			Vector vecFields = new Vector();
			vecFields.addElement(new Field("WFI_STATUS", WorkFlowConstance.WFI_STATUS_BACK));
			db.doUpdate("WFI_JOIN", vecFields, "INSTANCEID='"+instanceId+"'", evo.getConnection());
			WfiMsgQueue wfiMsgQueue=this.sendMsg2Queue(evo);
			this.handleBiz(evo, wfiMsgQueue);
			
			TableModelLoader model = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
			String pkCol = (String) context.getDataValue("pkCol");
			String pkVal = (String) context.getDataValue("pkVal");
			String statusName = (String) context.getDataValue("statusName");
			String modelId = (String) context.getDataValue("modelId");
			String tableName = model.getTableModel(modelId).getDbTableName();
			vecFields.clear();
			vecFields.addElement(new Field(statusName, WorkFlowConstance.WFI_STATUS_BACK));
			db.doUpdate(tableName, vecFields, pkCol+"='"+pkVal+"'", evo.getConnection());
		}
		//连续打回后后处理，第一次打回选择原办理人办理，第二次逐级提交的Bug
		String callModel = evo.getCallBackModel() ;
		if(callModel!=null&&"1".equals(callModel)){
			Vector vecFields = new Vector();
			vecFields.addElement(new Field("nextnodeid",""));
			vecFields.addElement(new Field("nextnodename",""));
			vecFields.addElement(new Field("nextnodeuser",""));
			DbControl db = DbControl.getInstance();
			db.doUpdate("wf_instance_node_property", vecFields, "instanceid='"+evo.getInstanceID()+"'", evo.getConnection());
		}
		return null;
	}
	
	/**
	 * <p>流程跳转后执行的处理<p/>
	 * <li>1.如果跳转到第一个节点，则更新业务表审批状态为打回
	 * <li>2.如果跳转到第一个节点，更新接入表审批状态为打回
	 * <li>3.如果跳转到第一个节点，调用业务处理接口
	 */
	public Object afterJump(EVO evo, Map map) throws Exception {
		Context context = (Context) evo.paramMap.get(WorkFlowConstance.ATTR_EMPCONTEXT);
		String instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
		String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
		String commentSign = (String) context.getDataValue(WorkFlowConstance.ATTR_WFI_RESULT);
		String wfiStatus = WorkFlowConstance.WFI_STATUS_BACK;
		if(WorkFlowConstance.WFI_RESULT_AGAIN_FIRST.equals(commentSign)) { //追回
			wfiStatus = WorkFlowConstance.WFI_STATUS_AGAIN_FIRST;
		}
		WFIInstanceVO instanceVo = wfi.getInstanceInfo(instanceId, currentUserId, null, evo.getConnection());
		if(instanceVo.getMainInstanceId()!=null && !instanceVo.getMainInstanceId().equals("") && !instanceVo.getMainInstanceId().equals("null")) { //子流程提交，暂时采用忽略后处理
			return null;
		}
		String nextNodeId = evo.getNextNodeID();
		boolean isFirstNode = (Boolean) wfi.getWFNodeProperty(nextNodeId, "isFirstNode");
		if(isFirstNode) {
			DbControl db = DbControl.getInstance();
			Vector vecFields = new Vector();
			vecFields.addElement(new Field("WFI_STATUS", wfiStatus));
			db.doUpdate("WFI_JOIN", vecFields, "INSTANCEID='"+instanceId+"'", evo.getConnection());
			WfiMsgQueue wfiMsgQueue=this.sendMsg2Queue(evo);
			this.handleBiz(evo, wfiMsgQueue);
			
			TableModelLoader model = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
			String pkCol = (String) context.getDataValue("pkCol");
			String pkVal = (String) context.getDataValue("pkVal");
			String statusName = (String) context.getDataValue("statusName");
			String modelId = (String) context.getDataValue("modelId");
			String tableName = model.getTableModel(modelId).getDbTableName();
			vecFields.clear();
			vecFields.addElement(new Field(statusName, wfiStatus));
			db.doUpdate(tableName, vecFields, pkCol+"='"+pkVal+"'", evo.getConnection());
		}
		return null;
	}
	
	/**
	 * <p>收回重办后执行才处理</p>
	 */
	public Object afterTakeBack(EVO evo, Map hm) throws Exception {
		Context context = (Context) evo.paramMap.get(WorkFlowConstance.ATTR_EMPCONTEXT);
//		String instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
		String instanceId = evo.getInstanceID();
		String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
		WFIInstanceVO instanceVo = wfi.getInstanceInfo(instanceId, currentUserId, null, evo.getConnection());
		if(instanceVo.getMainInstanceId()!=null && !instanceVo.getMainInstanceId().equals("") && !instanceVo.getMainInstanceId().equals("null")) { //子流程提交，暂时采用忽略后处理
			return null;
		}
//		DbControl db = DbControl.getInstance();
//		//泉州需求：检查是否审贷中心（均衡分配审批环节）的上一节点拿回，如果是则设置拿回再提交的下一办理人为第一次均衡分配的人员
//		String applType = (String) context.getDataValue("applType");
//		String preWfCurUserId = (String) context.getDataValue("wfCurUserId");
//		preWfCurUserId = preWfCurUserId.replace(";", "");
//		String preWfCurNodeId = (String) context.getDataValue("wfCurNodeId");
//		String preWfCurNodeName = (String) context.getDataValue("wfCurNodeName");
//		String preWfNodeStartDate = (String) context.getDataValue("wfNodeStartDate");
//		String strSql = "select actorno from wfi_approve_count where appl_type='"+applType+"' " +
//				"and nodeid='"+preWfCurNodeId+"' and actorno='"+preWfCurUserId+"' and approve_date='"+preWfNodeStartDate+"'";
//		Vector vecWfiApproveCount = db.performQuery(strSql, evo.getConnection());
//		if(vecWfiApproveCount.size() > 0) {
//			Vector vecFields = new Vector();
//			vecFields.addElement(new Field("NEXTNODEID", preWfCurNodeId));
//			vecFields.addElement(new Field("NEXTNODENAME", preWfCurNodeName));
//			vecFields.addElement(new Field("NEXTNODEUSER", preWfCurUserId));
//			db.doUpdate("WF_INSTANCE_NODE_PROPERTY", vecFields, "INSTANCEID='"+instanceId+"'", evo.getConnection());
//		}
//		
//		String nextNodeId = evo.getNextNodeID();
//		boolean isFirstNode = (Boolean) wfi.getWFNodeProperty(nextNodeId, "isFirstNode");
//		if(isFirstNode) {
//			Vector vecFields = new Vector();
//			vecFields.addElement(new Field("WFI_STATUS", WorkFlowConstance.WFI_STATUS_AGAIN));
//			db.doUpdate("WFI_JOIN", vecFields, "INSTANCEID='"+instanceId+"'", evo.getConnection());
//			WfiMsgQueue wfiMsgQueue=this.sendMsg2Queue(evo);
//			this.handleBiz(evo, wfiMsgQueue);
//			
//			TableModelLoader model = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
//			String pkCol = (String) context.getDataValue("pkCol");
//			String pkVal = (String) context.getDataValue("pkVal");
//			String statusName = (String) context.getDataValue("statusName");
//			String modelId = (String) context.getDataValue("modelId");
//			String tableName = model.getTableModel(modelId).getDbTableName();
//			vecFields.clear();
//			vecFields.addElement(new Field(statusName, WorkFlowConstance.WFI_STATUS_AGAIN));
//			db.doUpdate(tableName, vecFields, pkCol+"='"+pkVal+"'", evo.getConnection());
//		}
		return null;
	}
	
	/**
	 * <p>流程结束后执行的处理</p>
	 * <li>1.更新业务表审批状态
	 * <li>2.更新接入表审批状态
	 * <li>3.调用业务处理接口
	 */
	public Object afterEnd(EVO evo, Map hm) throws Exception {
		Context context = (Context) evo.paramMap.get(WorkFlowConstance.ATTR_EMPCONTEXT);
		String instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
		String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
		DbControl db = DbControl.getInstance();
		/* added by yangzy 2014/11/10 流程抄送改造，流程通过后添加抄送人员 start */
		String strSql =  " SELECT LISTAGG(EXV100, ';') WITHIN                                         "
						+"  GROUP(                                                                    "
						+"  ORDER BY INSTANCEID) AS EXV100                                            "
						+"   FROM (SELECT DISTINCT A.INSTANCEID, A.EXV100                             "
						+"           FROM (SELECT INSTANCEID,                                         "
						+"                        SUBSTR(EXV100,                                      "
						+"                               INSTR(EXV100, ';', 1, LV) + 1,               "
						+"                               INSTR(EXV100, ';', 1, LV + 1) -              "
						+"                               INSTR(EXV100, ';', 1, LV) - 1) AS EXV100     "
						+"                   FROM (SELECT WF.INSTANCEID,                              "
						+"                                ';' || WF.EXV100 || ';' AS EXV100,          "
						+"                                LENGTH(WF.EXV100) -                         "
						+"                                LENGTH(REPLACE(WF.EXV100, ';', '')) AS LEN, "
						+"                                LV                                          "
						+"                           FROM (select INSTANCEID, EXV100                  "
						+"                                   from WF_INSTANCE_END                     "
						+"                                  where INSTANCEID = '"+instanceId+"') WF,  "
						+"                                (SELECT LEVEL AS LV                         "
						+"                                   FROM DUAL                                "
						+"                                 CONNECT BY LEVEL < 300) DL                 "
						+"                          WHERE WF.EXV100 IS NOT NULL)                      "
						+"                  WHERE LEN >= LV - 1) A) B                                 ";
		Vector vecWfiApproveCount = db.performQuery(strSql, evo.getConnection());
		String annouceUserStr = "";
		String annouceUser = "";
		if (vecWfiApproveCount != null && vecWfiApproveCount.size() > 0) {
			Vector vecRow = (Vector) vecWfiApproveCount.get(0);
			if(vecRow.elementAt(0)!=null&&!"null".equals(vecRow.elementAt(0))&&!"".equals(vecRow.elementAt(0))){
				annouceUserStr = (String) vecRow.elementAt(0);
			}
		}
		if(context.containsKey("nextAnnouceUser")&&context.getDataValue("nextAnnouceUser")!=null&&!"".equals(context.getDataValue("nextAnnouceUser"))){
			annouceUser = (String) context.getDataValue("nextAnnouceUser");
			if(!"".equals(annouceUserStr)){
				annouceUserStr+=";"+annouceUser;
			}else{
				annouceUserStr+=annouceUser;
			}
		}
		Vector vecFields1 = new Vector();
		vecFields1.addElement(new Field("EXV100", annouceUserStr));
		db.doUpdate("WF_INSTANCE_END", vecFields1, "INSTANCEID='"+instanceId+"'", evo.getConnection());
		/* added by yangzy 2014/11/10 流程抄送改造，流程通过后添加抄送人员 end */
		WFIInstanceVO instanceVo = wfi.getInstanceInfo(instanceId, currentUserId, null, evo.getConnection());
		if(instanceVo.getMainInstanceId()!=null && !instanceVo.getMainInstanceId().equals("") && !instanceVo.getMainInstanceId().equals("null")) { //子流程提交，暂时采用忽略后处理
			return null;
		}
		
		String wfiResult = (String) context.getDataValue(WorkFlowConstance.ATTR_WFI_RESULT);
		String wfiStatus = WorkFlowConstance.WFI_STATUS_PASS;
		if(wfiResult.equals(WorkFlowConstance.WFI_RESULT_DISAGREE)) {  //否决
			wfiStatus = WorkFlowConstance.WFI_STATUS_DENIAL;
		}
		
		Vector vecFields = new Vector();
		//更新wfi_join中的状态
		String orgNum = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
		vecFields.addElement(new Field("WFI_STATUS", wfiStatus));
		vecFields.addElement(new Field("WFI_END_ORG", orgNum)); 
		db.doUpdate("WFI_JOIN", vecFields, "INSTANCEID='"+evo.getInstanceID()+"'", evo.getConnection());
		WfiMsgQueue wfiMsgQueue=this.sendMsg2Queue(evo);
		this.handleBiz(evo, wfiMsgQueue);
		
		TableModelLoader model = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
		String modelId = (String) context.get("modelId"); //业务表名
		String tableName = model.getTableModel(modelId).getDbTableName();
		String pkCol = (String) context.get("pkCol");  //主键，业务ID
		String pkVal = (String) context.get("pkVal");  //主键，字段名
		String statusName = (String) context.getDataValue("statusName"); //业务审批状态字段名
		//更新业务审批状态
		vecFields.clear();
		vecFields.addElement(new Field(statusName, wfiStatus));
		db.doUpdate(tableName, vecFields, pkCol+"='"+pkVal+"'", evo.getConnection());
		return null;
	}
	
	/**
	 * 实例签收后执行的处理
	 */
	public Object afterSignIn(EVO evo, Map hm) throws Exception {
		return null;
	}

	/**
	 * 实例撤销签收后执行的处理
	 */
	public Object afterSignOff(EVO evo, Map hm) throws Exception {
		return null;
	}

	/**
	 * 流程转办后执行
	 */
	public Object afterChange(EVO evo, Map hm) throws Exception {
		return null;
	}

	/**
	 * 流程撤办后执行
	 */
	public Object afterCancel(EVO evo, Map hm) throws Exception {
		
		return null;
	}

	/**
	 * 流程挂起后执行
	 */
	public Object afterHang(EVO evo, Map hm) throws Exception {

		return null;
	}

	/**
	 * 流程唤醒后执行
	 */
	public Object afterWake(EVO evo, Map hm) throws Exception {
		
		return null;
	}

	/**
	 * 实例删除后执行
	 */
	public Object afterDelete(EVO evo, Map hm) throws Exception {
		//modified by yangzy 2015/08/13 流程删除实例改造 start
		String delBiz = (String) evo.paramMap.get("delBiz"); //true删除业务时删除流程实例信息，否则是流程实例管理删除实例（需要恢复业务状态）
		if(!"true".equals(delBiz)) {
			//更新业务申请表状态为“待发起”
			Context context = (Context) evo.paramMap.get(WorkFlowConstance.ATTR_EMPCONTEXT);
			TableModelLoader model = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
			String modelId = (String) context.get("modelId"); //业务表名
			String tableName = model.getTableModel(modelId).getDbTableName();
			String pkCol = (String) context.get("pkCol");  //主键，业务ID
			String pkVal = (String) context.get("pkVal");  //主键，字段名
			String statusName = (String) context.getDataValue("statusName"); //业务审批状态字段名
			
			HttpServletRequest request = (HttpServletRequest)context.getDataValue(EMPConstance.SERVLET_REQUEST);
			String userIP = request.getRemoteAddr();
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			String instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			
			DbControl db = DbControl.getInstance();
			Vector vecFields = new Vector();
			//更新业务审批状态
			vecFields.addElement(new Field(statusName, WorkFlowConstance.WFI_STATUS_INIT));
			db.doUpdate(tableName, vecFields, pkCol+"='"+pkVal+"'", evo.getConnection());
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"DelWorkFlowOp-删除流程实例,操作ID:"+currentUserId+",登入IP:"+userIP+",流程编号:"+instanceId+",业务编号:"+pkVal+",业务类型:"+modelId+",标识:delWfi",null);
		}
		//modified by yangzy 2015/08/13 流程删除实例改造 end
		return null;
	}

	/**
	 *  取消虚似办结后执行（不用）
	 */
	public Object afterCancelVirEnd(EVO evo, Map map) throws Exception {
		return null;
	}

	/**
	 * 会办结束后执行
	 */
	public Object endGather(GatherVO gathervo, Map map) throws Exception {
		return null;
	}

	/**
	 * 获取流程实例后执行
	 */
	public Object getInstanceInfo(EVO evo, Map map) throws Exception {
		return null;
	}

	/**
	 * 发起会办后执行
	 */
	public Object startGather(GatherVO gathervo, Map map) throws Exception {
		return null;
	}

	/**
	 * <p>发起子流程后处理</p>
	 * 暂时不能从evo中获取EMPContext（没有设置）
	 */
	public Object startSubFlowAfter(EVO evo, Map map) throws Exception {
		return null;
	}

	/**
	 * 发起子流程前执行（echain引擎暂未支持）
	 */
	public Object startSubFlowBefore(EVO evo, Map map) throws Exception {

		return null;
	}

	/**
	 * 会办提交给会办汇总人后执行
	 */
	public Object submitGather(GatherVO gathervo, Map map) throws Exception {
		return null;
	}
	
	
	/**
	 * <p>发送消息至队列中</p>
	 * 目前只有在拿回到第一个节点、打回到第一个节点、流程结束、节点配置审批中处理才发送流程消息。如果每步流转操作都需要发送消息，则以下设置流程审批状态逻辑需做调整。
	 * @param evo 
	 * @param wfsign
	 * @param result
	 * @param tableNm
	 * @param pkCol
	 * @param pkVal
	 * @throws Exception
	 */
	private WfiMsgQueue sendMsg2Queue(EVO evo) throws Exception {

		Context context = (Context) evo.paramMap.get(WorkFlowConstance.ATTR_EMPCONTEXT);
		String instanceId = evo.getInstanceID();
		String nodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NODEID);
		String applType = (String) context.getDataValue(WorkFlowConstance.ATTR_APPLTYPE);
		//审批结论（意见标识）
		String wfiResult = (String) context.getDataValue(WorkFlowConstance.ATTR_WFI_RESULT);
		String wfSign = evo.getWFSign();
		String scene = wfi.getWFNodeExtProperty(nodeId, WorkFlowConstance.NODE_EXT_SCENE);
		String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
		String orgId = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
		String tabModelId = (String) context.getDataValue("modelId");
		String pkCol = (String) context.getDataValue("pkCol");
		String pkVal = (String) context.getDataValue("pkVal");
		String wfiStatus = WorkFlowConstance.WFI_STATUS_APPROVE;
		//设置流程审批状态
		String nextNodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NEXTNODEID);
		if(nextNodeId==null || nextNodeId.equals("null") || nextNodeId.equals("")) {
			nextNodeId = evo.getNextNodeID();
		}
		if(nextNodeId!=null && nextNodeId.contains("e")) {  //结束节点
			if(wfiResult.equals(WorkFlowConstance.WFI_RESULT_AGREE)) {
				wfiStatus = WorkFlowConstance.WFI_STATUS_PASS;
			} else {
				wfiStatus = WorkFlowConstance.WFI_STATUS_DENIAL;
			}
		} else {
			boolean isFirst = (Boolean) WorkFlowUtil.getWFNodeProperty(nextNodeId, "isFirstNode");
			if(isFirst) {
				if(wfiResult.equals(WorkFlowConstance.WFI_RESULT_AGAIN)) {
					wfiStatus = WorkFlowConstance.WFI_STATUS_AGAIN;
				} else if(wfiResult.equals(WorkFlowConstance.WFI_RESULT_CALLBACK) || wfiResult.equals(WorkFlowConstance.WFI_RESULT_RETURNBACK) ||wfiResult.equals(WorkFlowConstance.WFI_RESULT_JUMP)) {
					wfiStatus = WorkFlowConstance.WFI_STATUS_BACK;
				}
			}
		}
		
		UNIDGenerator unid = new UNIDGenerator();
		WfiMsgQueue wfiMsgQueue=new WfiMsgQueue();
		wfiMsgQueue.setMsgid(unid.getUNID());
		wfiMsgQueue.setWfsign(wfSign);
		wfiMsgQueue.setNodeid(nodeId);
		wfiMsgQueue.setInstanceid(instanceId);
		wfiMsgQueue.setScene(scene);
		wfiMsgQueue.setApplType(applType);
		wfiMsgQueue.setTableName(tabModelId);
		wfiMsgQueue.setPkCol(pkCol);
		wfiMsgQueue.setPkValue(pkVal);
		wfiMsgQueue.setUserId(currentUserId);
		wfiMsgQueue.setOrgId(orgId);
		wfiMsgQueue.setOpstatus(WorkFlowConstance.WFI_MSG_OPSTATUS_INIT);
		wfiMsgQueue.setWfiResult(wfiResult);
		wfiMsgQueue.setWfiStatus(wfiStatus);
		String optime = TimeUtil.getDateTime("yyyy-MM-dd");
		wfiMsgQueue.setOptime(optime);
		
		Vector vecFields = new Vector();
		vecFields.addElement(new Field("MSGID",wfiMsgQueue.getMsgid()));
		vecFields.addElement(new Field("APPL_TYPE",wfiMsgQueue.getApplType()));
		vecFields.addElement(new Field("INSTANCEID",wfiMsgQueue.getInstanceid()));
		vecFields.addElement(new Field("WFSIGN",wfiMsgQueue.getWfsign()));
		vecFields.addElement(new Field("NODEID",wfiMsgQueue.getNodeid()));
		vecFields.addElement(new Field("SCENE",wfiMsgQueue.getScene()));
		vecFields.addElement(new Field("TABLE_NAME",wfiMsgQueue.getTableName()));
		vecFields.addElement(new Field("PK_COL",wfiMsgQueue.getPkCol()));
		vecFields.addElement(new Field("PK_VALUE",wfiMsgQueue.getPkValue()));
		vecFields.addElement(new Field("USER_ID",wfiMsgQueue.getUserId()));
		vecFields.addElement(new Field("ORG_ID",wfiMsgQueue.getOrgId()));
		vecFields.addElement(new Field("WFI_RESULT",wfiMsgQueue.getWfiResult()));
		vecFields.addElement(new Field("WFI_STATUS",wfiMsgQueue.getWfiStatus()));
		vecFields.addElement(new Field("OPSTATUS",wfiMsgQueue.getOpstatus()));
		vecFields.addElement(new Field("OPTIME",wfiMsgQueue.getOptime()));
		DbControl db = DbControl.getInstance();
		db.doInsert("WFI_MSG_QUEUE", vecFields, evo.getConnection());
		return  wfiMsgQueue;
	}
	
	/**
	 * <p>
	 * <h2>业务处理</h2>
	 * 根据流程消息处理业务逻辑。处理成功，关闭消息；当发生异常时：
	 * <li>如果设置不在同一事务中，则流转成功，业务到异常消息模块单独处理，将当前消息处理设置为“异常”；
	 * <li>如果系统设置审批流转与业务处理同一事务，则抛出异常进行回滚(除非流程消息队列单独数据库连接操作，否则不产生流程消息)；
	 * </p>
	 * @param evo
	 * @param wfiMsgQueue
	 * @throws Exception
	 */
	private void handleBiz(EVO evo, WfiMsgQueue wfiMsgQueue) throws Exception {
		
		Context context = (Context) evo.paramMap.get(WorkFlowConstance.ATTR_EMPCONTEXT);
		String nodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NODEID);
		String applType = (String) context.getDataValue(WorkFlowConstance.ATTR_APPLTYPE);
		//审批结论（意见标识）
		String wfiResult = (String) context.getDataValue(WorkFlowConstance.ATTR_WFI_RESULT);
		String tabModelId = (String) context.get("modelId");
		//流程审批状态
		String wfiStatus = wfiMsgQueue.getWfiStatus();
		// 主键，业务ID
		String pkVal = (String) context.get("pkVal");
		String instanceId = evo.getInstanceID();
		WfiBizConfigVO bizConfigVO = WorkFlowUtil.getBizInterfaceId(wfiMsgQueue.getApplType());
		String interfaceId = bizConfigVO.getBizInterfaceId();
		int endFlag = 0;//1标识审批结束，其他标识审批中处理
		if(WorkFlowConstance.WFI_STATUS_PASS.equals(wfiStatus) || WorkFlowConstance.WFI_STATUS_DENIAL.equals(wfiStatus)) {
			endFlag = 1;
		} else {
			endFlag = 2;
		}
		boolean success = false;  //业务处理是否成功，true成功
		Connection connection = null;
		if(WorkFlowConstance.WFI_BIZIF_BLANK.equals(interfaceId)){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "没有配置流程后业务处理接口，将不做任何业务处理！业务主表模型["+tabModelId+"，申请类型["+applType+"]");
			success = true;
		} else {
			//如果是同一事务处理，则从evo里获取；否则重新获取新的连接
			if(SAME_TRANSACTION) {
				connection = evo.getConnection();
			} else {
				connection = DbControl.getInstance().getConnection();
				connection.setAutoCommit(false);
			}
			BIZProcessInterface wfiBizComp = (BIZProcessInterface) CMISComponentFactory .getComponentFactoryInstance().getComponentInterface(interfaceId, context, connection);
			try {
				/** 开始处理业务逻辑 */
				if(endFlag == 1) {
					if(WorkFlowConstance.WFI_STATUS_PASS.equals(wfiStatus)) {
						wfiBizComp.executeAtWFAgree(wfiMsgQueue);
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "审批已通过，业务处理成功！申请流水号："+pkVal);
					} else {
						wfiBizComp.executeAtWFDisagree(wfiMsgQueue);
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "审批被否决，业务处理完毕！申请流水号："+pkVal);
					}
				} else { //endFlag = 2;
					if(WorkFlowConstance.WFI_STATUS_AGAIN.equals(wfiStatus)) {
						wfiBizComp.executeAtTakeback(wfiMsgQueue);
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "审批被拿回，业务处理完毕！申请流水号："+pkVal);
					} else if(WorkFlowConstance.WFI_STATUS_BACK.equals(wfiStatus)) {
						wfiBizComp.executeAtCallback(wfiMsgQueue);
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "审批被打回，业务处理完毕！申请流水号："+pkVal);
					} else {
						//当前办理节点是否配置了需要业务处理
						String isProcess = wfi.getWFNodeExtProperty(evo.getNodeID(), WorkFlowConstance.NODE_EXT_IS_PROCESS);
						if (WorkFlowConstance.WFI_RESULT_AGREE.equals(wfiResult) && isProcess != null && isProcess.equals("1")) { //同意且节点需做业务逻辑处理
							wfiBizComp.executeAtWFAppProcess(wfiMsgQueue);
							EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "已提交下一审批人员，业务处理成功！申请流水号："+pkVal);
						}
					}
				}
				success = true;
			} catch (Exception e) {
				if(SAME_TRANSACTION || endFlag==2) { //同一事务或流程审批中出现业务异常，则抛出异常。流程审批中始终抛出异常目的是为了控制审批中业务异常后需要业务正常处理完成再继续流程流转，以保证审批的正常进行
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"业务逻辑处理失败，流程引擎审批回滚。异常信息："+e.getMessage(), e);
					throw new WFIException("业务逻辑处理失败，流程引擎审批回滚。流程实例号:"+instanceId+ "，业务流水号："+pkVal+"，异常信息：" + e.getMessage());
				} else {
					connection.rollback();
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"业务逻辑处理失败，业务将需要人工进行异常处理。流程实例号:" + instanceId +  "，业务流水号："+pkVal+"\n" + e.getMessage());
					context.put("errorMsg", "业务逻辑处理失败，业务将需要人工进行异常处理。业务流水号："+pkVal+"\n");
					wfiMsgQueue.setOpstatus(WorkFlowConstance.WFI_MSG_OPSTATUS_ERROR);
					this.updateWFMessage(wfiMsgQueue, evo.getConnection());
				}
			}
		}
		try {
			if(success) {
				//更新消息状态
				wfiMsgQueue.setOpstatus(WorkFlowConstance.WFI_MSG_OPSTATUS_END);
				this.updateWFMessage(wfiMsgQueue, evo.getConnection());
			}
			if(endFlag == 1) {
				//迁移数据到历史表
				WFIComponent wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, evo.getConnection());
				wfiComponent.transDataWfi(evo.getInstanceID(), evo.getConnection());
			}
			//如果业务处理是独立事务，则需要审批流程本身相关操作成功后，业务处理才提交；否则做回滚处理
			if(connection!=null && !SAME_TRANSACTION && success)
				connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if(connection!=null && !SAME_TRANSACTION) {
				connection.rollback();//如果业务处理是独立事务，也需回滚
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "业务处理成功，但更新流程业务处理消息或迁移数据到历史失败，业务处理回滚！申请流水号："+pkVal);
			}
			throw new WFIException("更新流程业务处理消息或迁移数据到历史失败。异常信息：" + e.getMessage());
		} finally {
			if(!SAME_TRANSACTION && connection!=null) {
				try {
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private boolean updateWFMessage(WfiMsgQueue wfiMsgQueue, Connection connection) throws Exception {
		boolean result = false;
		DbControl db = DbControl.getInstance();
		Vector vecFields = new Vector();
		/*
		vecFields.addElement(new Field("APPL_TYPE",wfiMsgQueue.getApplType()));
		vecFields.addElement(new Field("INSTANCEID",wfiMsgQueue.getInstanceId()));
		vecFields.addElement(new Field("WFSIGN",wfiMsgQueue.getWfSign()));
		vecFields.addElement(new Field("NODEID",wfiMsgQueue.getNodeId()));
		vecFields.addElement(new Field("SCENE",wfiMsgQueue.getScene()));
		vecFields.addElement(new Field("TABLE_NAME",wfiMsgQueue.getTableName()));
		vecFields.addElement(new Field("PK_COL",wfiMsgQueue.getPkCol()));
		vecFields.addElement(new Field("PK_VALUE",wfiMsgQueue.getPkValue()));
		vecFields.addElement(new Field("USER_ID",wfiMsgQueue.getUserId()));
		vecFields.addElement(new Field("ORG_ID",wfiMsgQueue.getOrgId()));
		vecFields.addElement(new Field("WFI_RESULT",wfiMsgQueue.getWfiResult()));
		vecFields.addElement(new Field("OPTIME",wfiMsgQueue.getOptime()));
		*/
		vecFields.addElement(new Field("OPSTATUS",wfiMsgQueue.getOpstatus()));
		String optime = TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
		vecFields.addElement(new Field("OPTIME",optime));
		db.doUpdate("WFI_MSG_QUEUE", vecFields, "MSGID='"+wfiMsgQueue.getMsgid()+"'", connection);
		result = true;
		return result;
	}
}
