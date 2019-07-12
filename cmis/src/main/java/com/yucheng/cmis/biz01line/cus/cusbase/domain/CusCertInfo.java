package com.yucheng.cmis.biz01line.cus.cusbase.domain;


public class CusCertInfo implements com.yucheng.cmis.pub.CMISDomain {
	
	private String cusId;
	private String certTyp;
	private String certcode;
	private String crtDate;
	private String crtEndDate;
	private String inputCli;
	private String inputOrg;
	private String inputDate;
	
	
	
	
	public String getCusId() {
		return cusId;
	}




	public void setCusId(String cusId) {
		this.cusId = cusId;
	}




	public String getCertTyp() {
		return certTyp;
	}




	public void setCertTyp(String certTyp) {
		this.certTyp = certTyp;
	}




	public String getCertcode() {
		return certcode;
	}




	public void setCertcode(String certcode) {
		this.certcode = certcode;
	}




	public String getCrtDate() {
		return crtDate;
	}




	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}




	public String getCrtEndDate() {
		return crtEndDate;
	}




	public void setCrtEndDate(String crtEndDate) {
		this.crtEndDate = crtEndDate;
	}




	public String getInputCli() {
		return inputCli;
	}




	public void setInputCli(String inputCli) {
		this.inputCli = inputCli;
	}




	public String getInputOrg() {
		return inputOrg;
	}




	public void setInputOrg(String inputOrg) {
		this.inputOrg = inputOrg;
	}




	public String getInputDate() {
		return inputDate;
	}




	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}




	public/* protected */Object clone() throws CloneNotSupportedException {

		// call父类的clone方法

		Object result = super.clone();

		// TODO: 定制clone数据

		return result;

	}
	
}
