package com.yucheng.cmis.biz01line.cus.cusindiv.domain;

import com.yucheng.cmis.pub.CMISDomain;

public class CusIndivInvtEnterprise implements CMISDomain {
	private CusIndiv cusIndiv;

	private String cusId;

	private String indivInvtComId;

	private String indivInvtComName;

	private String indivRegNo;

	private String indivInvtLcd;

	private double indivInvtAmt;

	private String indivInvtDt;

	private double indivInvPert;

	private String crtUsrId;

	private String crtDt;

	private String remark;

	private String lastUpdTm;

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

	public CusIndiv getCusIndiv() {
		return cusIndiv;
	}

	public void setCusIndiv(CusIndiv cusIndiv) {
		this.cusIndiv = cusIndiv;
	}

	public double getIndivInvPert() {
		return indivInvPert;
	}

	public void setIndivInvPert(double indivInvPert) {
		this.indivInvPert = indivInvPert;
	}

	public double getIndivInvtAmt() {
		return indivInvtAmt;
	}

	public void setIndivInvtAmt(double indivInvtAmt) {
		this.indivInvtAmt = indivInvtAmt;
	}

	public String getIndivInvtComId() {
		return indivInvtComId;
	}

	public void setIndivInvtComId(String indivInvtComId) {
		this.indivInvtComId = indivInvtComId;
	}

	public String getIndivInvtComName() {
		return indivInvtComName;
	}

	public void setIndivInvtComName(String indivInvtComName) {
		this.indivInvtComName = indivInvtComName;
	}

	public String getIndivInvtDt() {
		return indivInvtDt;
	}

	public void setIndivInvtDt(String indivInvtDt) {
		this.indivInvtDt = indivInvtDt;
	}

	public String getIndivInvtLcd() {
		return indivInvtLcd;
	}

	public void setIndivInvtLcd(String indivInvtLcd) {
		this.indivInvtLcd = indivInvtLcd;
	}

	public String getIndivRegNo() {
		return indivRegNo;
	}

	public void setIndivRegNo(String indivRegNo) {
		this.indivRegNo = indivRegNo;
	}

	public String getLastUpdTm() {
		return lastUpdTm;
	}

	public void setLastUpdTm(String lastUpdTm) {
		this.lastUpdTm = lastUpdTm;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
