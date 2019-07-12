package com.yucheng.cmis.platform.workflow.domain;

/**
 * 流程意见值对象
 * 
 * @author liuhw 2013-6-17
 */
public class WFICommentVO {

	/**
	 * 意见ID
	 */
	private String commentId;
	
	/**
	 * 实例ID
	 */
	private String instanceId;
	
	/**
	 * 节点ID
	 */
	private String nodeId;
	
	/**
	 * 节点名称
	 */
	private String nodeName;
	
	/**
	 * 意见人Id
	 */
	private String userId;
	
	/**
	 * 意见人名称
	 */
	private String userName;
	
	/**
	 * 意见标识
	 */
	private String commentSign;
	
	/**
	 * 意见标识名称（用户前台显示）
	 */
	private String commentSignName;
	
	/**
	 * 意见内容
	 */
	private String commentContent;
	
	/**
	 * 意见时间
	 */
	private String commentTime;
	
	/**
	 * 意见级别
	 */
	private int commentLevel;
	
	/**
	 * 机构ID
	 */
	private String orgId;

	/**
	 * @return 意见ID
	 */
	public String getCommentId() {
		return commentId;
	}

	/**
	 * @param 意见ID
	 */
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	/**
	 * @return 实例ID
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * @param 实例ID
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/**
	 * @return 节点ID
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * @param 节点ID
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * @return 节点名称
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param 节点名称
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * @return 意见人Id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param 意见人Id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return 意见人名称
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param 意见人名称
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return 意见标识
	 */
	public String getCommentSign() {
		return commentSign;
	}

	/**
	 * @param 意见标识
	 */
	public void setCommentSign(String commentSign) {
		this.commentSign = commentSign;
	}

	/**
	 * @return 意见标识名称
	 */
	public String getCommentSignName() {
		return commentSignName;
	}

	/**
	 * @param commentSignName 意见标识名称 to set
	 */
	public void setCommentSignName(String commentSignName) {
		this.commentSignName = commentSignName;
	}

	/**
	 * @return 意见内容
	 */
	public String getCommentContent() {
		return commentContent;
	}

	/**
	 * @param 意见内容
	 */
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	/**
	 * @return 意见时间
	 */
	public String getCommentTime() {
		return commentTime;
	}

	/**
	 * @param 意见时间
	 */
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	/**
	 * @return 意见级别
	 */
	public int getCommentLevel() {
		return commentLevel;
	}

	/**
	 * @param 意见级别
	 */
	public void setCommentLevel(int commentLevel) {
		this.commentLevel = commentLevel;
	}

	/**
	 * @return 机构ID
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @param 机构ID
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
