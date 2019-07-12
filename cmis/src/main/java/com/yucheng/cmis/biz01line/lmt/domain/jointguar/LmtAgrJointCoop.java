package com.yucheng.cmis.biz01line.lmt.domain.jointguar;

import com.yucheng.cmis.pub.CMISDomain;

public class LmtAgrJointCoop implements CMISDomain{
	private String serno;
	private String agrNo;
	private String cusId;
	private String coopType;
	private String shareRange;
	private String belgOrg;
	
//	private String 	appDate;
//	private String overDate;
	private String isBizAreaJoint;
	private String bizAreaNo;
	private String curType;
	private String lmtTotlAmt;
	private String sigleMaxAmt;
	private String startDate;
	private String endDate;
	private String agrState;
//	private String teamType;
//	private String team;
	
	private String managerId;
	private String managerBrId;
	private String inputId;
	private String inputBrId;
	private String inputDate;
//	private String approve_status;
	
	public Object clone() throws CloneNotSupportedException {
        Object result = super.clone();
        return result;
    }

	public String getSerno() {
		return serno;
	}

	public void setSerno(String serno) {
		this.serno = serno;
	}

	public String getAgrNo() {
		return agrNo;
	}

	public void setAgrNo(String agrNo) {
		this.agrNo = agrNo;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getCoopType() {
		return coopType;
	}

	public void setCoopType(String coopType) {
		this.coopType = coopType;
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

	public String getIsBizAreaJoint() {
		return isBizAreaJoint;
	}

	public void setIsBizAreaJoint(String isBizAreaJoint) {
		this.isBizAreaJoint = isBizAreaJoint;
	}

	public String getBizAreaNo() {
		return bizAreaNo;
	}

	public void setBizAreaNo(String bizAreaNo) {
		this.bizAreaNo = bizAreaNo;
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

	public String getSigleMaxAmt() {
		return sigleMaxAmt;
	}

	public void setSigleMaxAmt(String sigleMaxAmt) {
		this.sigleMaxAmt = sigleMaxAmt;
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

	public String getAgrState() {
		return agrState;
	}

	public void setAgrState(String agrState) {
		this.agrState = agrState;
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
}
