package com.yucheng.cmis.biz01line.cus.cuscom.domain;

import com.yucheng.cmis.pub.CMISDomain;

//他行交易－他行担保
public class CusOBisAssure  implements CMISDomain{
	private CusCom cuscom;

	private String cusId;

	private int seq;

	private String gtyTyp;

	private double gtyAmt;

	private double gtyBlc;

	private String gtyStrDt;

	private String gtyEndDt;

	private String gtyBusBch;

	private String gtyBusBchDec;

	private String validFlg;

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

	public double getGtyAmt() {
		return gtyAmt;
	}

	public void setGtyAmt(double gtyAmt) {
		this.gtyAmt = gtyAmt;
	}

	public double getGtyBlc() {
		return gtyBlc;
	}

	public void setGtyBlc(double gtyBlc) {
		this.gtyBlc = gtyBlc;
	}

	public String getGtyBusBch() {
		return gtyBusBch;
	}

	public void setGtyBusBch(String gtyBusBch) {
		this.gtyBusBch = gtyBusBch;
	}

	public String getGtyBusBchDec() {
		return gtyBusBchDec;
	}

	public void setGtyBusBchDec(String gtyBusBchDec) {
		this.gtyBusBchDec = gtyBusBchDec;
	}

	public String getGtyEndDt() {
		return gtyEndDt;
	}

	public void setGtyEndDt(String gtyEndDt) {
		this.gtyEndDt = gtyEndDt;
	}

	public String getGtyStrDt() {
		return gtyStrDt;
	}

	public void setGtyStrDt(String gtyStrDt) {
		this.gtyStrDt = gtyStrDt;
	}

	public String getGtyTyp() {
		return gtyTyp;
	}

	public void setGtyTyp(String gtyTyp) {
		this.gtyTyp = gtyTyp;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getValidFlg() {
		return validFlg;
	}

	public void setValidFlg(String validFlg) {
		this.validFlg = validFlg;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
