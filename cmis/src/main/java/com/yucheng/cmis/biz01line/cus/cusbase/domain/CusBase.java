package com.yucheng.cmis.biz01line.cus.cusbase.domain;

/**
 * Title: CusBase.java Description:
 * 
 * @author��echow heyc@yuchengtech.com
 * @create date��Thu Mar 12 08:54:33 CST 2009
 * @version��1.0
 */
public class CusBase implements com.yucheng.cmis.pub.CMISDomain {
	private String cusId;
	private String cusName;
	private String cusShortName;
	private String cusType;
	private String certType;
	private String certCode;
	private String openDate;
	private String cusCountry;
	private String loanCardFlg;
	private String loanCardId;
	private String loanCardPwd;
	private String loanCardEffFlg;
	private String loanCardAuditDt;
	private String cusCrdGrade;
	private String cusCrdDt;
	private String cusStatus;
	private String mainBrId;
	private String custMgr;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String belgLine;
	private String guarCrdGrade;
	private String hxCusId;

	public String getHxCusId() {
		return hxCusId;
	}


	public void setHxCusId(String hxCusId) {
		this.hxCusId = hxCusId;
	}
	
	public String getCusId() {
		return cusId;
	}


	public void setCusId(String cusId) {
		this.cusId = cusId;
	}


	public String getCusName() {
		return cusName;
	}


	public void setCusName(String cusName) {
		this.cusName = cusName;
	}


	public String getCusShortName() {
		return cusShortName;
	}


	public void setCusShortName(String cusShortName) {
		this.cusShortName = cusShortName;
	}


	public String getCusType() {
		return cusType;
	}


	public void setCusType(String cusType) {
		this.cusType = cusType;
	}


	public String getCertType() {
		return certType;
	}


	public void setCertType(String certType) {
		this.certType = certType;
	}


	public String getCertCode() {
		return certCode;
	}


	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}


	public String getOpenDate() {
		return openDate;
	}


	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}


	public String getCusCountry() {
		return cusCountry;
	}


	public void setCusCountry(String cusCountry) {
		this.cusCountry = cusCountry;
	}


	public String getLoanCardFlg() {
		return loanCardFlg;
	}


	public void setLoanCardFlg(String loanCardFlg) {
		this.loanCardFlg = loanCardFlg;
	}


	public String getLoanCardId() {
		return loanCardId;
	}


	public void setLoanCardId(String loanCardId) {
		this.loanCardId = loanCardId;
	}


	public String getLoanCardPwd() {
		return loanCardPwd;
	}


	public void setLoanCardPwd(String loanCardPwd) {
		this.loanCardPwd = loanCardPwd;
	}


	public String getLoanCardEffFlg() {
		return loanCardEffFlg;
	}


	public void setLoanCardEffFlg(String loanCardEffFlg) {
		this.loanCardEffFlg = loanCardEffFlg;
	}


	public String getLoanCardAuditDt() {
		return loanCardAuditDt;
	}


	public void setLoanCardAuditDt(String loanCardAuditDt) {
		this.loanCardAuditDt = loanCardAuditDt;
	}


	public String getCusCrdGrade() {
		return cusCrdGrade;
	}


	public void setCusCrdGrade(String cusCrdGrade) {
		this.cusCrdGrade = cusCrdGrade;
	}


	public String getCusCrdDt() {
		return cusCrdDt;
	}


	public void setCusCrdDt(String cusCrdDt) {
		this.cusCrdDt = cusCrdDt;
	}


	public String getCusStatus() {
		return cusStatus;
	}


	public void setCusStatus(String cusStatus) {
		this.cusStatus = cusStatus;
	}


	public String getMainBrId() {
		return mainBrId;
	}


	public void setMainBrId(String mainBrId) {
		this.mainBrId = mainBrId;
	}


	public String getCustMgr() {
		return custMgr;
	}


	public void setCustMgr(String custMgr) {
		this.custMgr = custMgr;
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


	public String getBelgLine() {
		return belgLine;
	}


	public void setBelgLine(String belgLine) {
		this.belgLine = belgLine;
	}


	public String getGuarCrdGrade() {
		return guarCrdGrade;
	}


	public void setGuarCrdGrade(String guarCrdGrade) {
		this.guarCrdGrade = guarCrdGrade;
	}


	public/* protected */Object clone() throws CloneNotSupportedException {

		// call父类的clone方法

		Object result = super.clone();

		// TODO: 定制clone数据

		return result;

	}
}