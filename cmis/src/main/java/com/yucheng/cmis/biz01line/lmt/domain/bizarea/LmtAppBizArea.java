package com.yucheng.cmis.biz01line.lmt.domain.bizarea;

import com.yucheng.cmis.pub.CMISDomain;

public class LmtAppBizArea implements CMISDomain {
	private String serno;
	private String bizAreaName;
	private String bizAreaType;
	private String shareRange;
	
	
	private String belgOrg;
	private String coopGuarType;
	
	private String curType;
	private String lmtTotlAmt;
	private String singleMaxAmt;
	
	private String managerId;
	private String managerBrId;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	
	private String appDate;
	private String overDate;
	private String teamType;
	private String Team;
	private String approveStatus;
	


	public String getSerno() {
		return serno;
	}



	public void setSerno(String serno) {
		this.serno = serno;
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



	public String getAppDate() {
		return appDate;
	}



	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}



	public String getOverDate() {
		return overDate;
	}



	public void setOverDate(String overDate) {
		this.overDate = overDate;
	}



	public String getTeamType() {
		return teamType;
	}



	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}



	public String getTeam() {
		return Team;
	}



	public void setTeam(String team) {
		Team = team;
	}



	public String getApproveStatus() {
		return approveStatus;
	}



	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}



	public Object clone() throws CloneNotSupportedException {
        Object result = super.clone();
        return result;
    } 
}
