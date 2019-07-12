package com.yucheng.cmis.biz01line.fnc.detail.domain;


import com.yucheng.cmis.pub.CMISDomain;


/**
 *@Classname	FncAssure.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 5:09:37 PM  
 *@Copyright 	yuchengtech
 *@Author 		Eric
 *@Description：主要对外担保及表外业务明细信息
 *@Lastmodified 
 *@Author
 */
public class FncAssure implements CMISDomain{
	
   /** 客户代码 */
   public  String cusId;
   
   /** 年月 */
   public  String fncYm;
   
   /** 报表周期类型1*/
   public  String fncTyp;
   
   /** 序号*/
   public int seq;
   
   /** 借款类别 */
   public  String fncLoanTyp;
   
   /** 机构名称 */
   public  String fncBchName;
   
   /** 金额*/
   public double fncAmt;
   
   /** 余额*/
   public double fncBlc;
   
   /** 敞口金额*/
   public double fncOpenAmt;
   
   /** 备注*/
   public  String remark;
   
   /** 登记机构*/
   public  String regBchId;
   
   /** 登记人*/
   public  String crtUsrId;
   
   /** 日期*/
   public  String crtDt;
   
   /** 更新时间*/
   public  String lastUpdTm;
   
   /** 当前编辑人*/
   public  String editUpdUsrId;
   
   /** 主办机构*/
   public  String txBchId;
   
   /** 管理机构*/
   public  String mngBchId;
   
   

	/**
	 * @return the crtDt
	 */
	public  String getCrtDt() {
		return crtDt;
	}
	
	/**
	 * @param crtDt the crtDt to set
	 */
	public void setCrtDt( String crtDt) {
		this.crtDt = crtDt;
	}
	
	/**
	 * @return the crtUsrId
	 */
	public  String getCrtUsrId() {
		return crtUsrId;
	}
	
	/**
	 * @param crtUsrId the crtUsrId to set
	 */
	public void setCrtUsrId( String crtUsrId) {
		this.crtUsrId = crtUsrId;
	}
	
	/**
	 * @return the cusId
	 */
	public  String getCusId() {
		return cusId;
	}
	
	/**
	 * @param cusId the cusId to set
	 */
	public void setCusId( String cusId) {
		this.cusId = cusId;
	}
	
	/**
	 * @return the editUpdUsrId
	 */
	public  String getEditUpdUsrId() {
		return editUpdUsrId;
	}
	
	/**
	 * @param editUpdUsrId the editUpdUsrId to set
	 */
	public void setEditUpdUsrId( String editUpdUsrId) {
		this.editUpdUsrId = editUpdUsrId;
	}
	
	/**
	 * @return the fncAmt
	 */
	public double getFncAmt() {
		return fncAmt;
	}
	
	/**
	 * @param fncAmt the fncAmt to set
	 */
	public void setFncAmt(double fncAmt) {
		this.fncAmt = fncAmt;
	}
	
	/**
	 * @return the fncBchName
	 */
	public  String getFncBchName() {
		return fncBchName;
	}
	
	/**
	 * @param fncBchName the fncBchName to set
	 */
	public void setFncBchName( String fncBchName) {
		this.fncBchName = fncBchName;
	}
	
	/**
	 * @return the fncBlc
	 */
	public double getFncBlc() {
		return fncBlc;
	}
	
	/**
	 * @param fncBlc the fncBlc to set
	 */
	public void setFncBlc(double fncBlc) {
		this.fncBlc = fncBlc;
	}
	
	/**
	 * @return the fncLoanTyp
	 */
	public  String getFncLoanTyp() {
		return fncLoanTyp;
	}
	
	/**
	 * @param fncLoanTyp the fncLoanTyp to set
	 */
	public void setFncLoanTyp( String fncLoanTyp) {
		this.fncLoanTyp = fncLoanTyp;
	}
	
	/**
	 * @return the fncOpenAmt
	 */
	public double getFncOpenAmt() {
		return fncOpenAmt;
	}
	
	/**
	 * @param fncOpenAmt the fncOpenAmt to set
	 */
	public void setFncOpenAmt(double fncOpenAmt) {
		this.fncOpenAmt = fncOpenAmt;
	}
	
	/**
	 * @return the fncTyp
	 */
	public  String getFncTyp() {
		return fncTyp;
	}
	
	/**
	 * @param fncTyp the fncTyp to set
	 */
	public void setFncTyp( String fncTyp) {
		this.fncTyp = fncTyp;
	}
	
	/**
	 * @return the fncYm
	 */
	public  String getFncYm() {
		return fncYm;
	}
	
	/**
	 * @param fncYm the fncYm to set
	 */
	public void setFncYm( String fncYm) {
		this.fncYm = fncYm;
	}
	
	/**
	 * @return the lastUpdTm
	 */
	public  String getLastUpdTm() {
		return lastUpdTm;
	}
	
	/**
	 * @param lastUpdTm the lastUpdTm to set
	 */
	public void setLastUpdTm( String lastUpdTm) {
		this.lastUpdTm = lastUpdTm;
	}
	
	/**
	 * @return the mngBchId
	 */
	public  String getMngBchId() {
		return mngBchId;
	}
	
	/**
	 * @param mngBchId the mngBchId to set
	 */
	public void setMngBchId( String mngBchId) {
		this.mngBchId = mngBchId;
	}
	
	/**
	 * @return the regBchId
	 */
	public  String getRegBchId() {
		return regBchId;
	}
	
	/**
	 * @param regBchId the regBchId to set
	 */
	public void setRegBchId( String regBchId) {
		this.regBchId = regBchId;
	}
	
	/**
	 * @return the remark
	 */
	public  String getRemark() {
		return remark;
	}
	
	/**
	 * @param remark the remark to set
	 */
	public void setRemark( String remark) {
		this.remark = remark;
	}
	
	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}
	
	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	/**
	 * @return the txBchId
	 */
	public  String getTxBchId() {
		return txBchId;
	}
	
	/**
	 * @param txBchId the txBchId to set
	 */
	public void setTxBchId( String txBchId) {
		this.txBchId = txBchId;
	}
	   
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 


}