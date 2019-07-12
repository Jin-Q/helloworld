package com.yucheng.cmis.platform.workflow.domain;

/**
 * 流程会办实例值对象
 * @author liuhw 2013-6-28
 */
public class WFIGatherInstanceVO {
	
	/**
	 * 业务流水号
	 */
	private String bizSeqNo;

	/**
	 * 会办实例号
	 */
	private String instanceId;
	
	/**
	 * 上一级会办实例号
	 */
	private String beforeInstanceId;
	
	/**
	 * 主流程实例号
	 */
	private String mainInstanceId;
	
	/**
	 * 主流程节点ID
	 */
	private String mainNodeId;
	
	/**
	 * 主流程节点名称
	 */
	private String mainNodeName;
	
	/**
	 * 主流程名称
	 */
	private String mainJobName;
	
	/**
	 * 会办发起人ID
	 */
	private String gatherStartUserId;
	
	/**
	 * 会办发起人名称
	 */
	private String gatherStartUserName;
	
	/**
	 * 会办汇总人ID
	 */
	private String gatherEndUserId;
	
	/**
	 * 会办汇总人名称
	 */
	private String gatherEndUserName;
	
	/**
	 * 会办办理人员列表
	 */
	private String currentGatherUserList;
	
	/**
	 * 会办主题
	 */
	private String gatherTitle;
	
	/**
	 * 会办描述
	 */
	private String gatherDesc;
	
	/**
	 * 会办参与人ID列表
	 */
	private String allProcessor;
	
	/**
	 * 会办参与人名称列表
	 */
	private String allProcessorName;
	
	/**
	 * 会办已办人员列表
	 */
	private String currentGatherProcessors;
	
	/**
	 * 会办发起时间
	 */
	private String gatherStartTime;
	
	/**
	 * 会办结束时间
	 */
	private String gatherEndTime;
	
	/**
	 * 意见VO
	 */
	private WFIGatherCommentVO gatherCommentVO;
	

	/**
	 * @return 业务流水号
	 */
	public String getBizSeqNo() {
		return bizSeqNo;
	}

	/**
	 * @param 业务流水号
	 */
	public void setBizSeqNo(String bizSeqNo) {
		this.bizSeqNo = bizSeqNo;
	}

	/**
	 * @return 会办实例号
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * @param 会办实例号
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/**
	 * @return 上一级会办实例号
	 */
	public String getBeforeInstanceId() {
		return beforeInstanceId;
	}

	/**
	 * @param 上一级会办实例号
	 */
	public void setBeforeInstanceId(String beforeInstanceId) {
		this.beforeInstanceId = beforeInstanceId;
	}

	/**
	 * @return 主流程实例号
	 */
	public String getMainInstanceId() {
		return mainInstanceId;
	}

	/**
	 * @param 主流程实例号
	 */
	public void setMainInstanceId(String mainInstanceId) {
		this.mainInstanceId = mainInstanceId;
	}

	/**
	 * @return 主流程节点ID
	 */
	public String getMainNodeId() {
		return mainNodeId;
	}

	/**
	 * @param 主流程节点ID
	 */
	public void setMainNodeId(String mainNodeId) {
		this.mainNodeId = mainNodeId;
	}

	/**
	 * @return 主流程节点名称
	 */
	public String getMainNodeName() {
		return mainNodeName;
	}

	/**
	 * @param 主流程节点名称
	 */
	public void setMainNodeName(String mainNodeName) {
		this.mainNodeName = mainNodeName;
	}

	/**
	 * @return 主流程名称
	 */
	public String getMainJobName() {
		return mainJobName;
	}

	/**
	 * @param 主流程名称
	 */
	public void setMainJobName(String mainJobName) {
		this.mainJobName = mainJobName;
	}

	/**
	 * @return 会办发起人ID
	 */
	public String getGatherStartUserId() {
		return gatherStartUserId;
	}

	/**
	 * @param 会办发起人ID
	 */
	public void setGatherStartUserId(String gatherStartUserId) {
		this.gatherStartUserId = gatherStartUserId;
	}

	/**
	 * @return 会办发起人名称
	 */
	public String getGatherStartUserName() {
		return gatherStartUserName;
	}

	/**
	 * @param 会办发起人名称
	 */
	public void setGatherStartUserName(String gatherStartUserName) {
		this.gatherStartUserName = gatherStartUserName;
	}

	/**
	 * @return 会办汇总人ID
	 */
	public String getGatherEndUserId() {
		return gatherEndUserId;
	}

	/**
	 * @param 会办汇总人ID
	 */
	public void setGatherEndUserId(String gatherEndUserId) {
		this.gatherEndUserId = gatherEndUserId;
	}

	/**
	 * @return 会办汇总人名称
	 */
	public String getGatherEndUserName() {
		return gatherEndUserName;
	}

	/**
	 * @param 会办汇总人名称
	 */
	public void setGatherEndUserName(String gatherEndUserName) {
		this.gatherEndUserName = gatherEndUserName;
	}

	/**
	 * @return 会办办理人员列表
	 */
	public String getCurrentGatherUserList() {
		return currentGatherUserList;
	}

	/**
	 * @param 会办办理人员列表
	 */
	public void setCurrentGatherUserList(String currentGatherUserList) {
		this.currentGatherUserList = currentGatherUserList;
	}

	/**
	 * @return 会办主题
	 */
	public String getGatherTitle() {
		return gatherTitle;
	}

	/**
	 * @param 会办主题
	 */
	public void setGatherTitle(String gatherTitle) {
		this.gatherTitle = gatherTitle;
	}

	/**
	 * @return 会办描述
	 */
	public String getGatherDesc() {
		return gatherDesc;
	}

	/**
	 * @param 会办描述
	 */
	public void setGatherDesc(String gatherDesc) {
		this.gatherDesc = gatherDesc;
	}

	/**
	 * @return 会办参与人ID列表
	 */
	public String getAllProcessor() {
		return allProcessor;
	}

	/**
	 * @param 会办参与人ID列表
	 */
	public void setAllProcessor(String allProcessor) {
		this.allProcessor = allProcessor;
	}

	/**
	 * @return 会办参与人名称列表
	 */
	public String getAllProcessorName() {
		return allProcessorName;
	}

	/**
	 * @param 会办参与人名称列表
	 */
	public void setAllProcessorName(String allProcessorName) {
		this.allProcessorName = allProcessorName;
	}

	/**
	 * @return 会办已办人员列表
	 */
	public String getCurrentGatherProcessors() {
		return currentGatherProcessors;
	}

	/**
	 * @param 会办已办人员列表
	 */
	public void setCurrentGatherProcessors(String currentGatherProcessors) {
		this.currentGatherProcessors = currentGatherProcessors;
	}

	/**
	 * @return 会办发起时间
	 */
	public String getGatherStartTime() {
		return gatherStartTime;
	}

	/**
	 * @param 会办发起时间
	 */
	public void setGatherStartTime(String gatherStartTime) {
		this.gatherStartTime = gatherStartTime;
	}

	/**
	 * @return 会办结束时间
	 */
	public String getGatherEndTime() {
		return gatherEndTime;
	}

	/**
	 * @param 会办结束时间
	 */
	public void setGatherEndTime(String gatherEndTime) {
		this.gatherEndTime = gatherEndTime;
	}

	/**
	 * @return 意见VO
	 */
	public WFIGatherCommentVO getGatherCommentVO() {
		return gatherCommentVO;
	}

	/**
	 * @param 意见VO
	 */
	public void setGatherCommentVO(WFIGatherCommentVO gatherCommentVO) {
		this.gatherCommentVO = gatherCommentVO;
	}
	
}
