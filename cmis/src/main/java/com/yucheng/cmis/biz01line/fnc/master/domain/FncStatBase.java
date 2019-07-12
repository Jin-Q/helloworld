package com.yucheng.cmis.biz01line.fnc.master.domain;


import com.yucheng.cmis.pub.CMISDomain;


/**
 *@Classname	FncStatBase.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 3:08:38 PM  
 *@Copyright 	yuchengtech
 *@Author 		Eric
 *@Description： 公司客户报表信息  
 *				 该类中可能会包含多张报表的基础信息
 *@Lastmodified 
 *@Author
 */

public class FncStatBase implements CMISDomain {
	/** 客户代码*/
	   private String cusId;
	   
	   /** 客户名称 **/
	   private String cusName;
	   
	   /** 报表周期类型*/
	   private String statPrdStyle;
	   
	   /** 报表期间*/
	   private String statPrd;
	   
	   /** 资产样式编号 */
	   private String statBsStyleId;
	   
	   /** 损益表编号*/
	   private String statPlStyleId;
	   
	   /** 现金流量表编号 */
	   private String statCfStyleId;
	   
	   /** 财务指标表编号*/
	   private String statFiStyleId;
	   
	   /** 所有者权益变动表编号*/
	   private String statSoeStyleId;
	   
	   /** 财务简表编号*/
	   private String statSlStyleId;
	   
	   /** 保留 */
	   private String styleId1;
	   
	   /** 保留1*/
	   private String styleId2;
	   
	   /** 状态*/
	   private String stateFlg;
	   
	   /** 是否新报表*/
	   private String statIsNrpt;
	   
	   /** 报表口径 */
	   private String statStyle;
	   
	   /** 是否经过审计*/
	   private String statIsAudit;
	   
	   /** 审计单位*/
	   private String statAdtEntr;
	   
	   /** 审计结论*/
	   private String statAdtConc;
	   
	   /** 是否经过调整*/
	   private String statIsAdjt;
	   
	   /** 财务报表调整原因*/
	   private String statAdjRsn;
	   
	   /** 编辑人 */
	   private String lastUpdId;
	   
	   /** 登记机构*/
	   private String inputBrId;
	   
	   /** 登记人*/
	   private String inputId;
	   
	   /** 登记日期*/
	   private String inputDate;
	   
	   /** 更新时间*/
	   private String lastUpdDate;
	
	   /** 报表类型*/
	   private String fncType;
	/**
	 * @return the cusId
	 */
	public String getCusId() {
		return cusId;
	}
	
	/**
	 * @param cusId the cusId to set
	 */
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	
	/**
	 * @return the statAdjRsn
	 */
	public String getStatAdjRsn() {
		return statAdjRsn;
	}
	
	/**
	 * @param statAdjRsn the statAdjRsn to set
	 */
	public void setStatAdjRsn(String statAdjRsn) {
		this.statAdjRsn = statAdjRsn;
	}
	
	/**
	 * @return the statAdtConc
	 */
	public String getStatAdtConc() {
		return statAdtConc;
	}
	
	/**
	 * @param statAdtConc the statAdtConc to set
	 */
	public void setStatAdtConc(String statAdtConc) {
		this.statAdtConc = statAdtConc;
	}
	
	/**
	 * @return the statAdtEntr
	 */
	public String getStatAdtEntr() {
		return statAdtEntr;
	}
	
	/**
	 * @param statAdtEntr the statAdtEntr to set
	 */
	public void setStatAdtEntr(String statAdtEntr) {
		this.statAdtEntr = statAdtEntr;
	}
	
	/**
	 * @return the statBsStyleId
	 */
	public String getStatBsStyleId() {
		return statBsStyleId;
	}
	
	/**
	 * @param statBsStyleId the statBsStyleId to set
	 */
	public void setStatBsStyleId(String statBsStyleId) {
		this.statBsStyleId = statBsStyleId;
	}
	
	/**
	 * @return the statCfStyleId
	 */
	public String getStatCfStyleId() {
		return statCfStyleId;
	}
	
	/**
	 * @param statCfStyleId the statCfStyleId to set
	 */
	public void setStatCfStyleId(String statCfStyleId) {
		this.statCfStyleId = statCfStyleId;
	}

	
	/**
	 * @return the stateFlg
	 */
	public String getStateFlg() {
		return stateFlg;
	}
	
	/**
	 * @param stateFlg the stateFlg to set
	 */
	public void setStateFlg(String stateFlg) {
		this.stateFlg = stateFlg;
	}
	
	/**
	 * @return the statFiStyleId
	 */
	public String getStatFiStyleId() {
		return statFiStyleId;
	}
	
	/**
	 * @param statFiStyleId the statFiStyleId to set
	 */
	public void setStatFiStyleId(String statFiStyleId) {
		this.statFiStyleId = statFiStyleId;
	}
	
	/**
	 * @return the statIsAdjt
	 */
	public String getStatIsAdjt() {
		return statIsAdjt;
	}
	
	/**
	 * @param statIsAdjt the statIsAdjt to set
	 */
	public void setStatIsAdjt(String statIsAdjt) {
		this.statIsAdjt = statIsAdjt;
	}
	
	/**
	 * @return the statIsAudit
	 */
	public String getStatIsAudit() {
		return statIsAudit;
	}
	
	/**
	 * @param statIsAudit the statIsAudit to set
	 */
	public void setStatIsAudit(String statIsAudit) {
		this.statIsAudit = statIsAudit;
	}
	
	/**
	 * @return the statIsNrpt
	 */
	public String getStatIsNrpt() {
		return statIsNrpt;
	}
	
	/**
	 * @param statIsNrpt the statIsNrpt to set
	 */
	public void setStatIsNrpt(String statIsNrpt) {
		this.statIsNrpt = statIsNrpt;
	}
	
	/**
	 * @return the statPlStyleId
	 */
	public String getStatPlStyleId() {
		return statPlStyleId;
	}
	
	/**
	 * @param statPlStyleId the statPlStyleId to set
	 */
	public void setStatPlStyleId(String statPlStyleId) {
		this.statPlStyleId = statPlStyleId;
	}
	
	/**
	 * @return the statPrd
	 */
	public String getStatPrd() {
		return statPrd;
	}
	
	/**
	 * @param statPrd the statPrd to set
	 */
	public void setStatPrd(String statPrd) {
		this.statPrd = statPrd;
	}
	
	/**
	 * @return the statPrdStyle
	 */
	public String getStatPrdStyle() {
		return statPrdStyle;
	}
	
	/**
	 * @param statPrdStyle the statPrdStyle to set
	 */
	public void setStatPrdStyle(String statPrdStyle) {
		this.statPrdStyle = statPrdStyle;
	}
	
	/**
	 * @return the statSlStyleId
	 */
	public String getStatSlStyleId() {
		return statSlStyleId;
	}
	
	/**
	 * @param statSlStyleId the statSlStyleId to set
	 */
	public void setStatSlStyleId(String statSlStyleId) {
		this.statSlStyleId = statSlStyleId;
	}
	
	/**
	 * @return the statSoeStyleId
	 */
	public String getStatSoeStyleId() {
		return statSoeStyleId;
	}
	
	/**
	 * @param statSoeStyleId the statSoeStyleId to set
	 */
	public void setStatSoeStyleId(String statSoeStyleId) {
		this.statSoeStyleId = statSoeStyleId;
	}
	
	/**
	 * @return the statStyle
	 */
	public String getStatStyle() {
		return statStyle;
	}
	
	/**
	 * @param statStyle the statStyle to set
	 */
	public void setStatStyle(String statStyle) {
		this.statStyle = statStyle;
	}
	
	/**
	 * @return the styleId1
	 */
	public String getStyleId1() {
		return styleId1;
	}
	
	/**
	 * @param styleId1 the styleId1 to set
	 */
	public void setStyleId1(String styleId1) {
		this.styleId1 = styleId1;
	}
	
	/**
	 * @return the styleId2
	 */
	public String getStyleId2() {
		return styleId2;
	}
	
	/**
	 * @param styleId2 the styleId2 to set
	 */
	public void setStyleId2(String styleId2) {
		this.styleId2 = styleId2;
	}

   
	public String getLastUpdId() {
		return lastUpdId;
	}

	public void setLastUpdId(String lastUpdId) {
		this.lastUpdId = lastUpdId;
	}

	public String getInputBrId() {
		return inputBrId;
	}

	public void setInputBrId(String inputBrId) {
		this.inputBrId = inputBrId;
	}

	public String getInputId() {
		return inputId;
	}

	public void setInputId(String inputId) {
		this.inputId = inputId;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public String getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(String lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	
	
	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getFncType() {
		return fncType;
	}

	public void setFncType(String fncType) {
		this.fncType = fncType;
	}

	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 


}