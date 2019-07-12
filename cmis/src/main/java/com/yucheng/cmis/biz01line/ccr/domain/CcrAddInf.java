package com.yucheng.cmis.biz01line.ccr.domain;

import com.yucheng.cmis.pub.CMISDomain;

public class CcrAddInf implements CMISDomain {

	public String getSerno() {
		return serno;
	}
	public void setSerno(String serno) {
		this.serno = serno;
	}	
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public String getCusType() {
		return cusType;
	}
	public void setCusType(String cusType) {
		this.cusType = cusType;
	}	
	public String getModelNo() {
		return modelNo;
	}
	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}	
	public String getAppBeginDate() {
		return appBeginDate;
	}
	public void setAppBeginDate(String appBeginDate) {
		this.appBeginDate = appBeginDate;
	}	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getExpiringDate() {
		return expiringDate;
	}
	public void setExpiringDate(String expiringDate) {
		this.expiringDate = expiringDate;
	}
	public String getInputBrId() {
		return inputBrId;
	}
	public void setInputBrId(String inputBrId) {
		this.inputBrId = inputBrId;
	}
	public String getFinaBrId() {
		return finaBrId;
	}
	public void setFinaBrId(String finaBrId) {
		this.finaBrId = finaBrId;
	}

	public String getInputId() {
		return inputId;
	}
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getManagerBrId() {
		return managerBrId;
	}
	public void setManagerBrId(String managerBrId) {
		this.managerBrId = managerBrId;
	}

	private String serno;
	private String cusId;
	private String cusName;
	private String cusType;
	private String modelNo;
	private String modelName;
	private String appBeginDate;
	private String startDate;
	private String expiringDate;
	private String inputId;
	private String managerId;
	private String managerBrId;

	private String inputBrId;
	private String finaBrId;
	private String approveStatus;
	private String lastGrade;
	private String lastRatingDate;
	
	public String getLastGrade() {
		return lastGrade;
	}
	public void setLastGrade(String lastGrade) {
		this.lastGrade = lastGrade;
	}
	public String getLastRatingDate() {
		return lastRatingDate;
	}
	public void setLastRatingDate(String lastRatingDate) {
		this.lastRatingDate = lastRatingDate;
	}
	public String getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
