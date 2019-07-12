package com.yucheng.cmis.biz01line.cus.cuscom.domain;

/**
 * Title: CusComRelInvest.java Description:
 * 
 * @author：echow heyc@yuchengtech.com
 * @create date：Sat May 09 15:05:20 CST 2009
 * @version：1.0
 */
public class CusComRelInvest implements com.yucheng.cmis.pub.CMISDomain {
	private String cusId;
	private String comInvTyp;
	private String comWprmCode;
	private String comInvCurTyp;
	private double comInvAmt;
	private String comInvApp;
	private double comInvPerc;
	private String comInvDesc;
	private String comInvDt;
	private String remark;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String lastUpdId;
	private String lastUpdDate;
	private String cusIdRel;

	public String getCusIdRel() {
		return cusIdRel;
	}

	public void setCusIdRel(String cusIdRel) {
		this.cusIdRel = cusIdRel;
	}

	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		Object result = super.clone();
		// TODO: 定制clone数据
		return result;
	}

	/**
	 * @return 返回 cusId
	 */
	public String getCusId() {
		return cusId;
	}

	/**
	 * @设置 cusId
	 * @param cusId
	 */
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	/**
	 * @return 返回 comInvTyp
	 */
	public String getComInvTyp() {
		return comInvTyp;
	}

	/**
	 * @设置 comInvTyp
	 * @param comInvTyp
	 */
	public void setComInvTyp(String comInvTyp) {
		this.comInvTyp = comInvTyp;
	}

	/**
	 * @return 返回 comWprmCode
	 */
	public String getComWprmCode() {
		return comWprmCode;
	}

	/**
	 * @设置 comWprmCode
	 * @param comWprmCode
	 */
	public void setComWprmCode(String comWprmCode) {
		this.comWprmCode = comWprmCode;
	}

	/**
	 * @return 返回 comInvCurTyp
	 */
	public String getComInvCurTyp() {
		return comInvCurTyp;
	}

	/**
	 * @设置 comInvCurTyp
	 * @param comInvCurTyp
	 */
	public void setComInvCurTyp(String comInvCurTyp) {
		this.comInvCurTyp = comInvCurTyp;
	}

	/**
	 * @return 返回 comInvAmt
	 */
	public double getComInvAmt() {
		return comInvAmt;
	}

	/**
	 * @设置 comInvAmt
	 * @param comInvAmt
	 */
	public void setComInvAmt(double comInvAmt) {
		this.comInvAmt = comInvAmt;
	}

	/**
	 * @return 返回 comInvApp
	 */
	public String getComInvApp() {
		return comInvApp;
	}

	/**
	 * @设置 comInvApp
	 * @param comInvApp
	 */
	public void setComInvApp(String comInvApp) {
		this.comInvApp = comInvApp;
	}

	/**
	 * @return 返回 comInvPerc
	 */
	public double getComInvPerc() {
		return comInvPerc;
	}

	/**
	 * @设置 comInvPerc
	 * @param comInvPerc
	 */
	public void setComInvPerc(double comInvPerc) {
		this.comInvPerc = comInvPerc;
	}

	/**
	 * @return 返回 comInvDesc
	 */
	public String getComInvDesc() {
		return comInvDesc;
	}

	/**
	 * @设置 comInvDesc
	 * @param comInvDesc
	 */
	public void setComInvDesc(String comInvDesc) {
		this.comInvDesc = comInvDesc;
	}

	/**
	 * @return 返回 comInvDt
	 */
	public String getComInvDt() {
		return comInvDt;
	}

	/**
	 * @设置 comInvDt
	 * @param comInvDt
	 */
	public void setComInvDt(String comInvDt) {
		this.comInvDt = comInvDt;
	}

	/**
	 * @return 返回 remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @设置 remark
	 * @param remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return 返回 inputId
	 */
	public String getInputId() {
		return inputId;
	}

	/**
	 * @设置 inputId
	 * @param inputId
	 */
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}

	/**
	 * @return 返回 inputBrId
	 */
	public String getInputBrId() {
		return inputBrId;
	}

	/**
	 * @设置 inputBrId
	 * @param inputBrId
	 */
	public void setInputBrId(String inputBrId) {
		this.inputBrId = inputBrId;
	}

	/**
	 * @return 返回 inputDate
	 */
	public String getInputDate() {
		return inputDate;
	}

	/**
	 * @设置 inputDate
	 * @param inputDate
	 */
	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	/**
	 * @return 返回 lastUpdId
	 */
	public String getLastUpdId() {
		return lastUpdId;
	}

	/**
	 * @设置 lastUpdId
	 * @param lastUpdId
	 */
	public void setLastUpdId(String lastUpdId) {
		this.lastUpdId = lastUpdId;
	}

	/**
	 * @return 返回 lastUpdDate
	 */
	public String getLastUpdDate() {
		return lastUpdDate;
	}

	/**
	 * @设置 lastUpdDate
	 * @param lastUpdDate
	 */
	public void setLastUpdDate(String lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}
}