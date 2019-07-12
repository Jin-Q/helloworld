package com.yucheng.cmis.biz01line.cus.cuscom.domain;

import com.yucheng.cmis.pub.CMISDomain;

//他行交易－他行存款
public class CusOBisDeposit  implements CMISDomain{
	private CusCom cuscom;

	private String cusId;

	private int seq;

	private String cusTyp;

	private String orgName;

	private String accNo;

	private String accCurTyp;

	private String accTyp;

	private String accCls;

	private String accSt;

	private double accBlc;

	private String crtUsrId;

	private String crtDt;

	public double getAccBlc() {
		return accBlc;
	}

	public void setAccBlc(double accBlc) {
		this.accBlc = accBlc;
	}

	public String getAccCls() {
		return accCls;
	}

	public void setAccCls(String accCls) {
		this.accCls = accCls;
	}

	public String getAccCurTyp() {
		return accCurTyp;
	}

	public void setAccCurTyp(String accCurTyp) {
		this.accCurTyp = accCurTyp;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getAccSt() {
		return accSt;
	}

	public void setAccSt(String accSt) {
		this.accSt = accSt;
	}

	public String getAccTyp() {
		return accTyp;
	}

	public void setAccTyp(String accTyp) {
		this.accTyp = accTyp;
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

	public CusCom getCuscom() {
		return cuscom;
	}

	public void setCuscom(CusCom cuscom) {
		this.cuscom = cuscom;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getCusTyp() {
		return cusTyp;
	}

	public void setCusTyp(String cusTyp) {
		this.cusTyp = cusTyp;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
