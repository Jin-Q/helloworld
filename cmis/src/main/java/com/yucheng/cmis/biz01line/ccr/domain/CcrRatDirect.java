package com.yucheng.cmis.biz01line.ccr.domain;
/**
 *@Classname	CcrRatDirect.java
 *@Version 1.0	
 *@Since   1.0 	Mar 1, 2010 
 *@Copyright 	yuchengtech
 *@Author 		eric
 *@Description：
 *@Lastmodified 
 *@Author	    
 */
public class CcrRatDirect implements com.yucheng.cmis.pub.CMISDomain{
	private String serno;
	private String cusId;
	private String cusName;
	private String cusType;
	private String grade;
	private String score;
	private String memo;
	private String managerId;
	private String managerBrId;
	private String inputId;
	private String inputDate;
	private String endDate;
	private String approveStatus;
	private String comScaleCcr;
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

	public String getInputId() {
		return inputId;
	}

	public void setInputId(String inputId) {
		this.inputId = inputId;
	}

	public String getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
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
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getInputDate() {
		return inputDate;
	}
	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getSerno() {
		return serno;
	}
	public void setSerno(String serno) {
		this.serno = serno;
	}
	
	/**
 	 * @return 返回 comScaleCcr
 	 */
	public String getComScaleCcr(){
		return comScaleCcr;
	}
	/**
 	 * @设置 comScaleCcr
 	 * @param comScaleCcr
 	 */
	public void setComScaleCcr(String comScaleCcr){
		this.comScaleCcr = comScaleCcr;
	}
	
}
