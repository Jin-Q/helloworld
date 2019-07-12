package com.yucheng.cmis.biz01line.lmt.domain.bizarea;

import com.yucheng.cmis.pub.CMISDomain;

public class LmtAppJoinBack implements CMISDomain {
	private String bizAreaNo;
	private String serno;
	private String appFlag ;
	private String bizAreaName;
	private String bizAreaType;
	private String shareRange;
	private String belgOrg;
	private String coopGuarType;
	private String curType;
	private String lmtTotlAmt;
	private String singleMaxAmt;
	private String startDate;
	private String endDate;
	private String managerId;
	private String managerBrId;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String approveStatus;
	
	public String getBizAreaNo() {
		return bizAreaNo;
	}

	public void setBizAreaNo(String bizAreaNo) {
		this.bizAreaNo = bizAreaNo;
	}

	public String getSerno() {
		return serno;
	}

	public void setSerno(String serno) {
		this.serno = serno;
	}

	public String getAppFlag() {
		return appFlag;
	}

	public void setAppFlag(String appFlag) {
		this.appFlag = appFlag;
	}

	public String getBizAreaName() {
		return bizAreaName;
	}

	public void setBizAreaName(String bizAreaName) {
		this.bizAreaName = bizAreaName;
	}

	public String getBizAreaType() {
		return bizAreaType;
	}

	public void setBizAreaType(String bizAreaType) {
		this.bizAreaType = bizAreaType;
	}

	public String getShareRange() {
		return shareRange;
	}

	public void setShareRange(String shareRange) {
		this.shareRange = shareRange;
	}

	public String getBelgOrg() {
		return belgOrg;
	}

	public void setBelgOrg(String belgOrg) {
		this.belgOrg = belgOrg;
	}

	public String getCoopGuarType() {
		return coopGuarType;
	}

	public void setCoopGuarType(String coopGuarType) {
		this.coopGuarType = coopGuarType;
	}

	public String getCurType() {
		return curType;
	}

	public void setCurType(String curType) {
		this.curType = curType;
	}

	public String getLmtTotlAmt() {
		return lmtTotlAmt;
	}

	public void setLmtTotlAmt(String lmtTotlAmt) {
		this.lmtTotlAmt = lmtTotlAmt;
	}

	public String getSingleMaxAmt() {
		return singleMaxAmt;
	}

	public void setSingleMaxAmt(String singleMaxAmt) {
		this.singleMaxAmt = singleMaxAmt;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public String getInputBrId() {
		return inputBrId;
	}

	public void setInputBrId(String inputBrId) {
		this.inputBrId = inputBrId;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getApproveStatus() {
		return approveStatus;
	} 

	public Object clone() throws CloneNotSupportedException {
        Object result = super.clone();
        return result;
    }


}
