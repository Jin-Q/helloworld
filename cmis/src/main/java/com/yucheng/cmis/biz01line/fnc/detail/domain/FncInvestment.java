package com.yucheng.cmis.biz01line.fnc.detail.domain;


import com.yucheng.cmis.pub.CMISDomain;

/**
 *@Classname	FncInvestment.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-7 下午02:11:57  
 *@Copyright 	2008 yucheng Co. Ltd.
 *@Author 		biwq
 *@Description：
 *@Lastmodified 
 *@Author
 */

public class FncInvestment implements CMISDomain{
	/** 客户代码*/
	private String cusId;
	/** 年月*/
	private String fncYm;
	/** 报表周期类型*/
	private String fncTyp;
	/** 序号*/
	private double seq;
	/** 投向*/
	private String fncInvtToward;
	/** 投入金额*/
	private double fncInvtAmt;
	/** 现价值*/
	private double fncCurVal;
	/** 上年投资收益*/
	private double fncLastInvtIncm;
	/** 平均投资收益*/
	private double fncAvgInvtIncm;
	/** 计价方法*/
	private String fncPriceTyp;
	/** 投资日期*/
	private String fncInvtDt;
	/** 登记机构*/
	private String regBchId;
	/** 登记人*/
	private String crtUsrId;
	/** 日期*/
	private String crtDt;
	/** 更新时间*/
	private String lastUpdTm;
	/** 当前编辑人*/
	private String editUpdUsrId;
	/** 主办机构*/
	private String txBchId;
	/** 管理机构*/
	private String mngBchId;
	
	private String fncInvtType;
	
	
	
	public String getFncInvtType() {
		return fncInvtType;
	}
	public void setFncInvtType(String fncInvtType) {
		this.fncInvtType = fncInvtType;
	}
	public String getCrtDt() {
		return crtDt;
	}
	public void setCrtDt(String crtDt) {
		this.crtDt = crtDt;
	}
	public String getCrtUsrId() {
		return crtUsrId;
	}
	public void setCrtUsrId(String crtUsrId) {
		this.crtUsrId = crtUsrId;
	}
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	public String getEditUpdUsrId() {
		return editUpdUsrId;
	}
	public void setEditUpdUsrId(String editUpdUsrId) {
		this.editUpdUsrId = editUpdUsrId;
	}
	public double getFncAvgInvtIncm() {
		return fncAvgInvtIncm;
	}
	public void setFncAvgInvtIncm(double fncAvgInvtIncm) {
		this.fncAvgInvtIncm = fncAvgInvtIncm;
	}
	public double getFncCurVal() {
		return fncCurVal;
	}
	public void setFncCurVal(double fncCurVal) {
		this.fncCurVal = fncCurVal;
	}
	public double getFncInvtAmt() {
		return fncInvtAmt;
	}
	public void setFncInvtAmt(double fncInvtAmt) {
		this.fncInvtAmt = fncInvtAmt;
	}
	public String getFncInvtDt() {
		return fncInvtDt;
	}
	public void setFncInvtDt(String fncInvtDt) {
		this.fncInvtDt = fncInvtDt;
	}
	public String getFncInvtToward() {
		return fncInvtToward;
	}
	public void setFncInvtToward(String fncInvtToward) {
		this.fncInvtToward = fncInvtToward;
	}
	public double getFncLastInvtIncm() {
		return fncLastInvtIncm;
	}
	public void setFncLastInvtIncm(double fncLastInvtIncm) {
		this.fncLastInvtIncm = fncLastInvtIncm;
	}
	public String getFncPriceTyp() {
		return fncPriceTyp;
	}
	public void setFncPriceTyp(String fncPriceTyp) {
		this.fncPriceTyp = fncPriceTyp;
	}
	public String getFncTyp() {
		return fncTyp;
	}
	public void setFncTyp(String fncTyp) {
		this.fncTyp = fncTyp;
	}
	public String getFncYm() {
		return fncYm;
	}
	public void setFncYm(String fncYm) {
		this.fncYm = fncYm;
	}
	public String getLastUpdTm() {
		return lastUpdTm;
	}
	public void setLastUpdTm(String lastUpdTm) {
		this.lastUpdTm = lastUpdTm;
	}
	public String getMngBchId() {
		return mngBchId;
	}
	public void setMngBchId(String mngBchId) {
		this.mngBchId = mngBchId;
	}
	public String getRegBchId() {
		return regBchId;
	}
	public void setRegBchId(String regBchId) {
		this.regBchId = regBchId;
	}
	public double getSeq() {
		return seq;
	}
	public void setSeq(double seq) {
		this.seq = seq;
	}
	public String getTxBchId() {
		return txBchId;
	}
	public void setTxBchId(String txBchId) {
		this.txBchId = txBchId;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
