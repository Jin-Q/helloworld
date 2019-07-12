package com.yucheng.cmis.platform.workflow.domain;

public class WfiWorkflow2biz implements com.yucheng.cmis.pub.CMISDomain {

	/**
	 * 配置主键
	 */
	private String pk1;

	/**
	 * 申请类型
	 */
	private String applType;

	/**
	 * 流程标识
	 */
	private String wfsign;

	/**
	 * 流程名称
	 */
	private String wfname;

	/**
	 * 申请业务信息页面
	 */
	private String appUrl;

	/**
	 * 业务要素修改页面
	 */
	private String bizUrl;

	/**
	 * 配置适用范围
	 */
	private String sceneScope;

	/**
	 * 风险拦截配置
	 */
	private String preventList;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * @return 配置主键
	 */
	public String getPk1() {
		return pk1;
	}

	/**
	 * @param pk1 配置主键 to set
	 */
	public void setPk1(String pk1) {
		this.pk1 = pk1;
	}

	/**
	 * @return 申请类型
	 */
	public String getApplType() {
		return applType;
	}

	/**
	 * @param applType 申请类型 to set
	 */
	public void setApplType(String applType) {
		this.applType = applType;
	}

	/**
	 * @return 流程标识
	 */
	public String getWfsign() {
		return wfsign;
	}

	/**
	 * @param wfsign 流程标识 to set
	 */
	public void setWfsign(String wfsign) {
		this.wfsign = wfsign;
	}

	/**
	 * @return 流程名称
	 */
	public String getWfname() {
		return wfname;
	}

	/**
	 * @param wfname 流程名称 to set
	 */
	public void setWfname(String wfname) {
		this.wfname = wfname;
	}

	/**
	 * @return 申请业务信息页面
	 */
	public String getAppUrl() {
		return appUrl;
	}

	/**
	 * @param appUrl 申请业务信息页面 to set
	 */
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	/**
	 * @return 业务要素修改页面
	 */
	public String getBizUrl() {
		return bizUrl;
	}

	/**
	 * @param bizUrl 业务要素修改页面 to set
	 */
	public void setBizUrl(String bizUrl) {
		this.bizUrl = bizUrl;
	}

	/**
	 * @return 配置适用范围
	 */
	public String getSceneScope() {
		return sceneScope;
	}

	/**
	 * @param sceneScope 配置适用范围 to set
	 */
	public void setSceneScope(String sceneScope) {
		this.sceneScope = sceneScope;
	}

	/**
	 * @return 风险拦截配置
	 */
	public String getPreventList() {
		return preventList;
	}

	/**
	 * @param preventList 风险拦截配置 to set
	 */
	public void setPreventList(String preventList) {
		this.preventList = preventList;
	}

	/**
	 * @return 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark 备注 to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		Object result = super.clone();
		// TODO: 定制clone数据
		return result;
	}
}
