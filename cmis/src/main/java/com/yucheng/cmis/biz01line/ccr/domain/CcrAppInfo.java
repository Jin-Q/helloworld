package com.yucheng.cmis.biz01line.ccr.domain;

import com.yucheng.cmis.pub.CMISDomain;

/**
 * Title: CcrAppInfo.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Wed Mar 18 14:29:06 CST 2009
 * @version：1.0
 */
public class CcrAppInfo implements CMISDomain {
	private String serno;
	private String cusId;
	private String appBeginDate;
	private String appEndDate;
	private String inputId;
	private String startDate;
	private String expiringDate;
	private String autoGrade;
	private String inputBrId;
	private String approveStatus;
	private String managerId;
	private String managerBrId;
	private String flag;
	private String regDate;
	private String lmtSerno;
	

	public String getRegDate() {
		return regDate;
	}


	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}


	public String getFlag() {
		return flag;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}

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




	public String getAppBeginDate() {
		return appBeginDate;
	}




	public void setAppBeginDate(String appBeginDate) {
		this.appBeginDate = appBeginDate;
	}




	public String getAppEndDate() {
		return appEndDate;
	}




	public void setAppEndDate(String appEndDate) {
		this.appEndDate = appEndDate;
	}




	public String getInputId() {
		return inputId;
	}




	public void setInputId(String inputId) {
		this.inputId = inputId;
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




	public String getAutoGrade() {
		return autoGrade;
	}




	public void setAutoGrade(String autoGrade) {
		this.autoGrade = autoGrade;
	}




	public String getInputBrId() {
		return inputBrId;
	}




	public void setInputBrId(String inputBrId) {
		this.inputBrId = inputBrId;
	}




	public String getApproveStatus() {
		return approveStatus;
	}




	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
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




	public String getLmtSerno() {
		return lmtSerno;
	}


	public void setLmtSerno(String lmtSerno) {
		this.lmtSerno = lmtSerno;
	}


	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 
}