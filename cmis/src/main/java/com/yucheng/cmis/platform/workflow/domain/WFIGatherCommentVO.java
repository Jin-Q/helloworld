package com.yucheng.cmis.platform.workflow.domain;

/**
 * <p>流程会办意见</p>
 * @author liuhw 2013-6-28
 */
public class WFIGatherCommentVO {

	/**
	 * 操作记录ID
	 */
	private String actionId;
	/**
	 * 会办实例号
	 */
	private String gatherInstaceId;
	
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
	 * 办理人ID
	 */
	private String transActor;
	
	/**
	 * 办理人名称
	 */
	private String transActorName;
	
	/**
	 * 办理时间
	 */
	private String actTime;
	
	/**
	 * 意见级别：0-所有人可见，其它-部门内部可见
	 */
	private String commentLevel;
	
	/**
	 * 意见类别：0-流程意见 1-会办意见
	 */
	private String commentType;
	
	/**
	 * 意见内容
	 */
	private String suggest;
	
	/**
	 * 执行操作
	 */
	private String actionName;
	
	/**
	 * 备注
	 */
	private String memo;
	
	/**
	 * 下一办理人ID
	 */
	private String nextActorId;
	
	/**
	 * @return 操作记录ID
	 */
	public String getActionId() {
		return actionId;
	}

	/**
	 * @param 操作记录ID
	 */
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return 会办实例号
	 */
	public String getGatherInstaceId() {
		return gatherInstaceId;
	}

	/**
	 * @param 会办实例号
	 */
	public void setGatherInstaceId(String gatherInstaceId) {
		this.gatherInstaceId = gatherInstaceId;
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
	 * @return 办理人ID
	 */
	public String getTransActor() {
		return transActor;
	}

	/**
	 * @param 办理人ID
	 */
	public void setTransActor(String transActor) {
		this.transActor = transActor;
	}

	/**
	 * @return 办理人名称
	 */
	public String getTransActorName() {
		return transActorName;
	}

	/**
	 * @param 办理人名称
	 */
	public void setTransActorName(String transActorName) {
		this.transActorName = transActorName;
	}

	/**
	 * @return 办理时间
	 */
	public String getActTime() {
		return actTime;
	}

	/**
	 * @param 办理时间
	 */
	public void setActTime(String actTime) {
		this.actTime = actTime;
	}

	/**
	 * @return 意见级别：0-所有人可见，其它-部门内部可见
	 */
	public String getCommentLevel() {
		return commentLevel;
	}

	/**
	 * @param 意见级别：0-所有人可见，其它-部门内部可见
	 */
	public void setCommentLevel(String commentLevel) {
		this.commentLevel = commentLevel;
	}

	/**
	 * @return 意见类别：0-流程意见 1-会办意见
	 */
	public String getCommentType() {
		return commentType;
	}

	/**
	 * @param 意见类别：0-流程意见 1-会办意见
	 */
	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	/**
	 * @return 意见内容
	 */
	public String getSuggest() {
		return suggest;
	}

	/**
	 * @param 意见内容
	 */
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	/**
	 * @return 执行操作
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param 执行操作
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @return 下一办理人ID
	 */
	public String getNextActorId() {
		return nextActorId;
	}

	/**
	 * @param 备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @param 下一办理人ID
	 */
	public void setNextActorId(String nextActorId) {
		this.nextActorId = nextActorId;
	}
	
}
