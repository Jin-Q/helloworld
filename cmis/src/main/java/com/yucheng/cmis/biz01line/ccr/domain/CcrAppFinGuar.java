package com.yucheng.cmis.biz01line.ccr.domain;

import com.yucheng.cmis.pub.CMISDomain;

/**
 * Title: CcrAppInfo.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Wed Mar 18 14:29:06 CST 2009
 * @version：1.0
 */
public class CcrAppFinGuar implements CMISDomain {
	private String serno;
	private String cusId;
	private String appBeginDate;
	private String appEndDate;
	private String inputId;
	private String startDate;
	private String expiringDate;
	private String autoGrade;
	private String inputBrId;
	private String finaBrId;
	private String approveStatus;
	private String cusType;
	private String managerId;
	private String managerBrId;
	private String modelNo;
	private String extrRatingUnit;
	private String extrRatingDate;
	private String extrRatingDateResult;
	private String bailMulti;
	private String guarType;
	private String isAuthorize;
	private String rateScore;
	private String autoScore;
	private String adjAutoGrade;
	private String ratingReason;
	private String regDate;
	private String fncYear;
	private String fncMonth;
	
	
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


	public String getFinaBrId() {
		return finaBrId;
	}


	public void setFinaBrId(String finaBrId) {
		this.finaBrId = finaBrId;
	}


	public String getApproveStatus() {
		return approveStatus;
	}


	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}


	public String getCusType() {
		return cusType;
	}


	public void setCusType(String cusType) {
		this.cusType = cusType;
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


	public String getModelNo() {
		return modelNo;
	}


	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}


	public String getExtrRatingUnit() {
		return extrRatingUnit;
	}


	public void setExtrRatingUnit(String extrRatingUnit) {
		this.extrRatingUnit = extrRatingUnit;
	}


	public String getExtrRatingDate() {
		return extrRatingDate;
	}


	public void setExtrRatingDate(String extrRatingDate) {
		this.extrRatingDate = extrRatingDate;
	}


	public String getExtrRatingDateResult() {
		return extrRatingDateResult;
	}


	public void setExtrRatingDateResult(String extrRatingDateResult) {
		this.extrRatingDateResult = extrRatingDateResult;
	}


	public String getBailMulti() {
		return bailMulti;
	}


	public void setBailMulti(String bailMulti) {
		this.bailMulti = bailMulti;
	}


	public String getGuarType() {
		return guarType;
	}


	public void setGuarType(String guarType) {
		this.guarType = guarType;
	}


	public String getIsAuthorize() {
		return isAuthorize;
	}


	public void setIsAuthorize(String isAuthorize) {
		this.isAuthorize = isAuthorize;
	}


	public String getRateScore() {
		return rateScore;
	}


	public void setRateScore(String rateScore) {
		this.rateScore = rateScore;
	}


	public String getAutoScore() {
		return autoScore;
	}


	public void setAutoScore(String autoScore) {
		this.autoScore = autoScore;
	}


	public String getAdjAutoGrade() {
		return adjAutoGrade;
	}


	public void setAdjAutoGrade(String adjAutoGrade) {
		this.adjAutoGrade = adjAutoGrade;
	}


	public String getRatingReason() {
		return ratingReason;
	}


	public void setRatingReason(String ratingReason) {
		this.ratingReason = ratingReason;
	}


	public String getRegDate() {
		return regDate;
	}


	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}


	public String getFncYear() {
		return fncYear;
	}


	public void setFncYear(String fncYear) {
		this.fncYear = fncYear;
	}


	public String getFncMonth() {
		return fncMonth;
	}


	public void setFncMonth(String fncMonth) {
		this.fncMonth = fncMonth;
	}


	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 
}