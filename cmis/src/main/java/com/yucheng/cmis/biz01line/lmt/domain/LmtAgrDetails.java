package com.yucheng.cmis.biz01line.lmt.domain;

import com.yucheng.cmis.pub.CMISDomain;

/**
 * Title: LmtAgrDetails.java
 * Description: 
 * @author��echow	heyc@yuchengtech.com
 * @create date��Fri Apr 17 11:44:04 CST 2009
 * @version��1.0
 */
public class LmtAgrDetails implements CMISDomain{
	private String agrNo;
	private String subType;
	private String cusId;
	private String coreCorpCusId;
	private String coreCorpDuty;
	private String bailRate;
	private String limitType;
	private String limitCode;
	private String limitName;
	private String prdId;
	private String curType;
	private String crdAmt;
	private String frozeAmt;
	private String enableAmt;
	private String guarType;
	private String termType;
	private String term;
	private String isAdjTerm;
	private String isPreCrd;
	private String startDate;
	private String endDate;
	private String lmtStatus;
	private String cusType;
	private String bizAreaNo;


	public String getSubType() {
		return subType;
	}


	public String getAgrNo() {
		return agrNo;
	}


	public void setAgrNo(String agrNo) {
		this.agrNo = agrNo;
	}


	public String getPrdId() {
		return prdId;
	}


	public void setPrdId(String prdId) {
		this.prdId = prdId;
	}


	public String getLmtStatus() {
		return lmtStatus;
	}


	public void setLmtStatus(String lmtStatus) {
		this.lmtStatus = lmtStatus;
	}


	public String getCusType() {
		return cusType;
	}


	public void setCusType(String cusType) {
		this.cusType = cusType;
	}


	public String getBizAreaNo() {
		return bizAreaNo;
	}


	public void setBizAreaNo(String bizAreaNo) {
		this.bizAreaNo = bizAreaNo;
	}


	public void setSubType(String subType) {
		this.subType = subType;
	}


	public String getCusId() {
		return cusId;
	}


	public void setCusId(String cusId) {
		this.cusId = cusId;
	}


	public String getCoreCorpCusId() {
		return coreCorpCusId;
	}


	public void setCoreCorpCusId(String coreCorpCusId) {
		this.coreCorpCusId = coreCorpCusId;
	}


	public String getCoreCorpDuty() {
		return coreCorpDuty;
	}


	public void setCoreCorpDuty(String coreCorpDuty) {
		this.coreCorpDuty = coreCorpDuty;
	}


	public String getBailRate() {
		return bailRate;
	}


	public void setBailRate(String bailRate) {
		this.bailRate = bailRate;
	}


	public String getLimitType() {
		return limitType;
	}


	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}


	public String getLimitCode() {
		return limitCode;
	}


	public void setLimitCode(String limitCode) {
		this.limitCode = limitCode;
	}


	public String getLimitName() {
		return limitName;
	}


	public void setLimitName(String limitName) {
		this.limitName = limitName;
	}
	

	public String getCurType() {
		return curType;
	}


	public void setCurType(String curType) {
		this.curType = curType;
	}


	public String getCrdAmt() {
		return crdAmt;
	}


	public void setCrdAmt(String crdAmt) {
		this.crdAmt = crdAmt;
	}


	public String getFrozeAmt() {
		return frozeAmt;
	}


	public void setFrozeAmt(String frozeAmt) {
		this.frozeAmt = frozeAmt;
	}


	public String getEnableAmt() {
		return enableAmt;
	}


	public void setEnableAmt(String enableAmt) {
		this.enableAmt = enableAmt;
	}


	public String getGuarType() {
		return guarType;
	}


	public void setGuarType(String guarType) {
		this.guarType = guarType;
	}


	public String getTermType() {
		return termType;
	}


	public void setTermType(String termType) {
		this.termType = termType;
	}


	public String getTerm() {
		return term;
	}


	public void setTerm(String term) {
		this.term = term;
	}


	public String getIsAdjTerm() {
		return isAdjTerm;
	}


	public void setIsAdjTerm(String isAdjTerm) {
		this.isAdjTerm = isAdjTerm;
	}


	public String getIsPreCrd() {
		return isPreCrd;
	}


	public void setIsPreCrd(String isPreCrd) {
		this.isPreCrd = isPreCrd;
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


	public Object clone() throws CloneNotSupportedException {
            Object result = super.clone();
            return result;
        } 
}