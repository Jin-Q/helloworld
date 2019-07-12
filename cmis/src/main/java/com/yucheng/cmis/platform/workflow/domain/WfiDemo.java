package com.yucheng.cmis.platform.workflow.domain;

import com.yucheng.cmis.pub.CMISDomain;

public class WfiDemo implements CMISDomain {

	/** 申请流水号 */
	private String serno;
	
	/** 客户ID */
	private String cusId;
	
	/** 客户名称 */
	private String cusName;
	
	/** 审批状态 */
	private String approveStatus;
	
	
	/**
	 * @return 申请流水号
	 */
	public String getSerno() {
		return serno;
	}



	/**
	 * @param serno 申请流水号 to set
	 */
	public void setSerno(String serno) {
		this.serno = serno;
	}



	/**
	 * @return 客户ID
	 */
	public String getCusId() {
		return cusId;
	}



	/**
	 * @param cusId 客户ID to set
	 */
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}



	/**
	 * @return 客户名称
	 */
	public String getCusName() {
		return cusName;
	}



	/**
	 * @param cusName 客户名称 to set
	 */
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}



	/**
	 * @return the approveStatus
	 */
	public String getApproveStatus() {
		return approveStatus;
	}



	/**
	 * @param approveStatus the approveStatus to set
	 */
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}



	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone()throws CloneNotSupportedException{
		Object result = super.clone();
		//TODO: 定制clone数据
		return result;
	}
}
