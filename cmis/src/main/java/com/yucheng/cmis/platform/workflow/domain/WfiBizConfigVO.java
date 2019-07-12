package com.yucheng.cmis.platform.workflow.domain;

/**
 * <p>信贷系统中与流程审批关联的业务处理接口配置</p>
 * @author liuhw
 */

public class WfiBizConfigVO {
	
	/**
	 * 业务处理接口ID
	 */
	private String bizInterfaceId;
	
	/**
	 * 申请类型
	 */
	private String applType;

	/**
	 * 描述
	 */
	private String desc;

	/**
	 * @return 业务处理接口ID
	 */
	public String getBizInterfaceId() {
		return bizInterfaceId;
	}

	/**
	 * @param 业务处理接口ID
	 */
	public void setBizInterfaceId(String bizInterfaceId) {
		this.bizInterfaceId = bizInterfaceId;
	}

	/**
	 * @return 申请类型
	 */
	public String getApplType() {
		return applType;
	}

	/**
	 * @param 申请类型
	 */
	public void setApplType(String applType) {
		this.applType = applType;
	}

	/**
	 * @return 描述
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param 描述
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
