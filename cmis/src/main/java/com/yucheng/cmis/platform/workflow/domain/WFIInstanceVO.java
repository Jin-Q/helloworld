package com.yucheng.cmis.platform.workflow.domain;

import java.util.List;
import java.util.Map;

/**
 * 信贷流程实例对象
 * @author liuhw 2013-6-17
 *
 */
public class WFIInstanceVO {
	
	/**
	 * 流程实例号
	 */
	private String instanceId;
	
	/**
	 * 主流程实例号
	 */
	private String mainInstanceId;
	
	/**
	 * 当前用户ID
	 */
	private String currentUserId;
	
	/**
	 * 流程ID
	 */
	private String wfId;
	
	/**
	 * 流程标识
	 */
	private String wfSign;
	
	/**
	 * 流程名称
	 */
	private String wfName;
	
	/**
	 * 应用模块ID，与echainForms.xml的app[id]对应
	 */
	private String appId;
	
	/**
	 * 应用模块名称
	 */
	private String appName;
	
	/**
	 * 流程工作名称
	 */
	private String wfJobName;
	
	/**
	 * 流程应用数据标识
	 */
	private String appSign;
	
	/**
	 * 流程审批状态
	 * <p>0：审批中；1：同意；2：不同意；3：部分同意；4：不明确
	 */
	private String spStatus;
	
	/**
	 * 前一办理节点ID
	 */
	private String preNodeId;
	
	/**
	 * 前一办理节点名称
	 */
	private String preNodeName;
	
	/**
	 * 当前办理节点ID
	 */
	private String nodeId;
	
	/**
	 * 当前办理节点名称
	 */
	private String nodeName;
	
	/**
	 * 主流程节点ID
	 */
	private String mainNodeId;
	
	/**
	 * 主流程节点名称
	 */
	private String mainNodeName;
	
	/**
	 * 工作流状态
	 * <p>0：流转中；1：流程结束；2：流程挂起；3：流程异常中止；4：预结束；5：流程过期办理
	 */
	private String wfStatus;
	
	/**
	 * 节点状态
	 * <p>0：正常办理；1：催办；2：办理结束；3：待签收；4：拿回；5：退回；6：挂起；7：打回；8：审批协助；9：取消办结；10：虚似办结
	 */
	private String nodeStatus;
	
	/**
	 * 流程表单ID
	 */
	private String formId;
	
	/**
	 * 节点表单流水号
	 */
	private String formFlow;
	
	/**
	 * 当前节点办理人
	 */
	private String currentNodeUser;
	
	/**
	 * 流程发起人
	 */
	private String author;
	
	/**
	 * 前一节点办理人
	 */
	private String preUser;
	
	/**
	 * 节点开始时间
	 */
	private String nodeStartTime;
	
	/**
	 * 节点办理时限
	 */
	private String nodePlanEndTime;
	
	/**
	 * 是否拟稿状态
	 * <p>1：拟稿状态；0：流转中
	 */
	private String isdraft;
	
	/**
	 * 节点是否已经办理过
	 * <p>0：未开始办理；1：节点已经办理过
	 */
	private String isProcessed;
	
	/**
	 * 节点表单字段List
	 */
	private List<WFIFormFieldVO> fieldList;
	
	/**
	 * 节点高级操作
	 */
	private WFIFormActionVO formActionVO;
	
	/**
	 * 流程表单数据
	 */
	private Map formData;
	
	/**
	 * 流程实例意见（默认存放用于回显的那条记录）
	 */
	private List<WFICommentVO> commentList;

	/**
	 * @return the instanceId
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * @param instanceId the instanceId to set
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/**
	 * @return the currentUserId
	 */
	public String getCurrentUserId() {
		return currentUserId;
	}

	/**
	 * @param currentUserId the currentUserId to set
	 */
	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	/**
	 * @return the wfId
	 */
	public String getWfId() {
		return wfId;
	}

	/**
	 * @param wfId the wfId to set
	 */
	public void setWfId(String wfId) {
		this.wfId = wfId;
	}

	/**
	 * @return the wfSign
	 */
	public String getWfSign() {
		return wfSign;
	}

	/**
	 * @param wfSign the wfSign to set
	 */
	public void setWfSign(String wfSign) {
		this.wfSign = wfSign;
	}

	/**
	 * @return the wfName
	 */
	public String getWfName() {
		return wfName;
	}

	/**
	 * @param wfName the wfName to set
	 */
	public void setWfName(String wfName) {
		this.wfName = wfName;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the wfJobName
	 */
	public String getWfJobName() {
		return wfJobName;
	}

	/**
	 * @param wfJobName the wfJobName to set
	 */
	public void setWfJobName(String wfJobName) {
		this.wfJobName = wfJobName;
	}

	/**
	 * @return the appSign
	 */
	public String getAppSign() {
		return appSign;
	}

	/**
	 * @param appSign the appSign to set
	 */
	public void setAppSign(String appSign) {
		this.appSign = appSign;
	}

	/**
	 * @return the spStatus
	 */
	public String getSpStatus() {
		return spStatus;
	}

	/**
	 * @param spStatus the spStatus to set
	 */
	public void setSpStatus(String spStatus) {
		this.spStatus = spStatus;
	}

	/**
	 * @return the preNodeId
	 */
	public String getPreNodeId() {
		return preNodeId;
	}

	/**
	 * @param preNodeId the preNodeId to set
	 */
	public void setPreNodeId(String preNodeId) {
		this.preNodeId = preNodeId;
	}

	/**
	 * @return the preNodeName
	 */
	public String getPreNodeName() {
		return preNodeName;
	}

	/**
	 * @param preNodeName the preNodeName to set
	 */
	public void setPreNodeName(String preNodeName) {
		this.preNodeName = preNodeName;
	}

	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * @return the wfStatus
	 */
	public String getWfStatus() {
		return wfStatus;
	}

	/**
	 * @param wfStatus the wfStatus to set
	 */
	public void setWfStatus(String wfStatus) {
		this.wfStatus = wfStatus;
	}

	/**
	 * @return the nodeStatus
	 */
	public String getNodeStatus() {
		return nodeStatus;
	}

	/**
	 * @param nodeStatus the nodeStatus to set
	 */
	public void setNodeStatus(String nodeStatus) {
		this.nodeStatus = nodeStatus;
	}

	/**
	 * @return the formId
	 */
	public String getFormId() {
		return formId;
	}

	/**
	 * @param formId the formId to set
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/**
	 * @return the formFlow
	 */
	public String getFormFlow() {
		return formFlow;
	}

	/**
	 * @param formFlow the formFlow to set
	 */
	public void setFormFlow(String formFlow) {
		this.formFlow = formFlow;
	}

	/**
	 * @return the currentNodeUser
	 */
	public String getCurrentNodeUser() {
		return currentNodeUser;
	}

	/**
	 * @param currentNodeUser the currentNodeUser to set
	 */
	public void setCurrentNodeUser(String currentNodeUser) {
		this.currentNodeUser = currentNodeUser;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the preUser
	 */
	public String getPreUser() {
		return preUser;
	}

	/**
	 * @param preUser the preUser to set
	 */
	public void setPreUser(String preUser) {
		this.preUser = preUser;
	}

	/**
	 * @return the nodeStartTime
	 */
	public String getNodeStartTime() {
		return nodeStartTime;
	}

	/**
	 * @param nodeStartTime the nodeStartTime to set
	 */
	public void setNodeStartTime(String nodeStartTime) {
		this.nodeStartTime = nodeStartTime;
	}

	/**
	 * @return the nodePlanEndTime
	 */
	public String getNodePlanEndTime() {
		return nodePlanEndTime;
	}

	/**
	 * @param nodePlanEndTime the nodePlanEndTime to set
	 */
	public void setNodePlanEndTime(String nodePlanEndTime) {
		this.nodePlanEndTime = nodePlanEndTime;
	}

	/**
	 * @return the isdraft
	 */
	public String getIsdraft() {
		return isdraft;
	}

	/**
	 * @param isdraft the isdraft to set
	 */
	public void setIsdraft(String isdraft) {
		this.isdraft = isdraft;
	}

	/**
	 * @return the isProcessed
	 */
	public String getIsProcessed() {
		return isProcessed;
	}

	/**
	 * @param isProcessed the isProcessed to set
	 */
	public void setIsProcessed(String isProcessed) {
		this.isProcessed = isProcessed;
	}

	/**
	 * @return the fieldList
	 */
	public List<WFIFormFieldVO> getFieldList() {
		return fieldList;
	}

	/**
	 * @param fieldList the fieldList to set
	 */
	public void setFieldList(List<WFIFormFieldVO> fieldList) {
		this.fieldList = fieldList;
	}

	/**
	 * @return the formActionVO
	 */
	public WFIFormActionVO getFormActionVO() {
		return formActionVO;
	}

	/**
	 * @param formActionVO the formActionVO to set
	 */
	public void setFormActionVO(WFIFormActionVO formActionVO) {
		this.formActionVO = formActionVO;
	}

	/**
	 * @return the formData
	 */
	public Map getFormData() {
		return formData;
	}

	/**
	 * @param formData the formData to set
	 */
	public void setFormData(Map formData) {
		this.formData = formData;
	}

	/**
	 * @return the commentList
	 */
	public List<WFICommentVO> getCommentList() {
		return commentList;
	}

	/**
	 * @param commentList the commentList to set
	 */
	public void setCommentList(List<WFICommentVO> commentList) {
		this.commentList = commentList;
	}

	/**
	 * @return the mainInstanceId
	 */
	public String getMainInstanceId() {
		return mainInstanceId;
	}

	/**
	 * @param mainInstanceId the mainInstanceId to set
	 */
	public void setMainInstanceId(String mainInstanceId) {
		this.mainInstanceId = mainInstanceId;
	}

	/**
	 * @return the mainNodeId
	 */
	public String getMainNodeId() {
		return mainNodeId;
	}

	/**
	 * @param mainNodeId the mainNodeId to set
	 */
	public void setMainNodeId(String mainNodeId) {
		this.mainNodeId = mainNodeId;
	}

	/**
	 * @return the mainNodeName
	 */
	public String getMainNodeName() {
		return mainNodeName;
	}

	/**
	 * @param mainNodeName the mainNodeName to set
	 */
	public void setMainNodeName(String mainNodeName) {
		this.mainNodeName = mainNodeName;
	}
	
}
