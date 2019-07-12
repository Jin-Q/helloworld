package com.yucheng.cmis.biz01line.ccr.domain;

import java.math.BigDecimal;

/**
 * Title: CcrAppDetail.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Thu Jul 09 16:34:44 CST 2009
 * @version：1.0
 */
public class CcrAppDetail  implements com.yucheng.cmis.pub.CMISDomain{
	private String serno;
	private String modelNo;
	private String oRatingUnit;
	private String oRatingResult;
	private String oRatingDate;
	private BigDecimal autoScore;
	private String autoGrade;
	private String adjustedGrade;
	private String reason;
	private String fncYear;  //会计年份
	private String fncMonth; //会计月份
	private String allScore;		//评级总分
	private String limit_grade;		//限定等级、
	private String limit_reason;	//限定原因
	private String reason_show;
	private String reason_show0;
	private String reason_show1;
	private String reason_show2;
	private String bailMulti;
	private String guarType;
	private String is_authorize;
	private String congniz_fn_dt;
	private String congniz_reason;
	private String statPrdStyle;//报表周期类型
	private String lat_app_end_date;
	private String last_adjusted_grade;
	public String getLat_app_end_date() {
		return lat_app_end_date;
	}


	public void setLat_app_end_date(String lat_app_end_date) {
		this.lat_app_end_date = lat_app_end_date;
	}


	public String getLast_adjusted_grade() {
		return last_adjusted_grade;
	}


	public void setLast_adjusted_grade(String last_adjusted_grade) {
		this.last_adjusted_grade = last_adjusted_grade;
	}


	public String getReason_show0() {
		return reason_show0;
	}


	public void setReason_show0(String reason_show0) {
		this.reason_show0 = reason_show0;
	}


	public String getReason_show1() {
		return reason_show1;
	}


	public void setReason_show1(String reason_show1) {
		this.reason_show1 = reason_show1;
	}


	public String getReason_show2() {
		return reason_show2;
	}


	public void setReason_show2(String reason_show2) {
		this.reason_show2 = reason_show2;
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


	public String getStatPrdStyle() {
		return statPrdStyle;
	}


	public void setStatPrdStyle(String statPrdStyle) {
		this.statPrdStyle = statPrdStyle;
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


	public String getSerno() {
		return serno;
	}


	public void setSerno(String serno) {
		this.serno = serno;
	}


	public String getModelNo() {
		return modelNo;
	}


	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}


	public String getORatingUnit() {
		return oRatingUnit;
	}


	public void setORatingUnit(String ratingUnit) {
		oRatingUnit = ratingUnit;
	}


	public String getORatingResult() {
		return oRatingResult;
	}


	public void setORatingResult(String ratingResult) {
		oRatingResult = ratingResult;
	}


	public String getORatingDate() {
		return oRatingDate;
	}


	public void setORatingDate(String ratingDate) {
		oRatingDate = ratingDate;
	}


	public BigDecimal getAutoScore() {
		return autoScore;
	}


	public void setAutoScore(BigDecimal autoScore) {
		this.autoScore = autoScore;
	}


	public String getAutoGrade() {
		return autoGrade;
	}


	public void setAutoGrade(String autoGrade) {
		this.autoGrade = autoGrade;
	}


	public String getAdjustedGrade() {
		return adjustedGrade;
	}


	public void setAdjustedGrade(String adjustedGrade) {
		this.adjustedGrade = adjustedGrade;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
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


	public String getAllScore() {
		return allScore;
	}


	public void setAllScore(String allScore) {
		this.allScore = allScore;
	}


	public String getLimit_grade() {
		return limit_grade;
	}


	public void setLimit_grade(String limit_grade) {
		this.limit_grade = limit_grade;
	}


	public String getLimit_reason() {
		return limit_reason;
	}


	public void setLimit_reason(String limit_reason) {
		this.limit_reason = limit_reason;
	}


	public String getReason_show() {
		return reason_show;
	}


	public void setReason_show(String reason_show) {
		this.reason_show = reason_show;
	}

	public String getIs_authorize() {
		return is_authorize;
	}


	public void setIs_authorize(String is_authorize) {
		this.is_authorize = is_authorize;
	}


	public String getCongniz_fn_dt() {
		return congniz_fn_dt;
	}


	public void setCongniz_fn_dt(String congniz_fn_dt) {
		this.congniz_fn_dt = congniz_fn_dt;
	}


	public String getCongniz_reason() {
		return congniz_reason;
	}


	public void setCongniz_reason(String congniz_reason) {
		this.congniz_reason = congniz_reason;
	}
	

}