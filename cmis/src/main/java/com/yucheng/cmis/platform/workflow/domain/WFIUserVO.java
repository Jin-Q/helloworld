package com.yucheng.cmis.platform.workflow.domain;

/**
 * 流程办理人员对象
 * 
 * @author liuhw  2013-6-14
 */
public class WFIUserVO {
	
	/**
	 * 用户ID
	 */
	private String userId = null;//用户ID
	
	/**
	 * 用户名称
	 */
	private String userName = null;//用户名称
	
	/**
	 * 机构ID
	 */
	private String orgId;
	
	/**
	 * 机构名称
	 */
	private String orgName;
	
	/**
	 * 中文名字全拼音(用于选择办理人员时搜索)
	 */
	private String chineseFull;
	
	/**
	 * 中文名字拼音首字母(用于选择办理人员时搜索)
	 */
	private String chineseHead;
	
	/**
	 * 其所在的用户列表是否是多选(用于选择办理人员时搜索)
	 */
	private String userIsmu;
	
	/**
	 * 用户ID
	 * @return 用户ID
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * 用户ID
	 * @param userId 用户ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 用户名称
	 * @return 用户名称
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * 用户名称
	 * @param userName 用户名称
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * @return the chineseFull
	 */
	public String getChineseFull() {
		return chineseFull;
	}

	/**
	 * @param chineseFull the chineseFull to set
	 */
	public void setChineseFull(String chineseFull) {
		this.chineseFull = chineseFull;
	}

	/**
	 * @return the chineseHead
	 */
	public String getChineseHead() {
		return chineseHead;
	}

	/**
	 * @param chineseHead the chineseHead to set
	 */
	public void setChineseHead(String chineseHead) {
		this.chineseHead = chineseHead;
	}

	/**
	 * @return the userIsmu
	 */
	public String getUserIsmu() {
		return userIsmu;
	}

	/**
	 * @param userIsmu the userIsmu to set
	 */
	public void setUserIsmu(String userIsmu) {
		this.userIsmu = userIsmu;
	}

}
