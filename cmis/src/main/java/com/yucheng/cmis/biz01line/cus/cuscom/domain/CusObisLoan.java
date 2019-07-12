package com.yucheng.cmis.biz01line.cus.cuscom.domain;

import com.yucheng.cmis.pub.CMISDomain;

//他行交易－他行贷款
public class CusObisLoan  implements CMISDomain{

	private CusCom cuscom;

	private String cusId;

	private int seq;

	private String cusTyp;

	private String loanTyp;

	private String orgName;

	private String contNo;

	private String loanNo;

	private String contCurTyp;

	private double contAmt;

	private double loanBlc;

	private double interestBlcIsd;

	private double interestBlcOsd;

	private double gtyPerc;

	private String gtyMainTyp;

	private String loanStrDt;

	private String loanEndDt;

	private int extendTm;

	private int refinanceTm;

	private String loanForm4;

	private String loanForm5;

	private String lawSuitFlg;

	private String validFlg;

	private double contRate;

	public double getContAmt() {
		return contAmt;
	}

	public void setContAmt(double contAmt) {
		this.contAmt = contAmt;
	}

	public String getContCurTyp() {
		return contCurTyp;
	}

	public void setContCurTyp(String contCurTyp) {
		this.contCurTyp = contCurTyp;
	}

	public String getContNo() {
		return contNo;
	}

	public void setContNo(String contNo) {
		this.contNo = contNo;
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

	

	public String getGtyMainTyp() {
		return gtyMainTyp;
	}

	public void setGtyMainTyp(String gtyMainTyp) {
		this.gtyMainTyp = gtyMainTyp;
	}

	public double getGtyPerc() {
		return gtyPerc;
	}

	public void setGtyPerc(double gtyPerc) {
		this.gtyPerc = gtyPerc;
	}

	public double getInterestBlcIsd() {
		return interestBlcIsd;
	}

	public void setInterestBlcIsd(double interestBlcIsd) {
		this.interestBlcIsd = interestBlcIsd;
	}

	public double getInterestBlcOsd() {
		return interestBlcOsd;
	}

	public void setInterestBlcOsd(double interestBlcOsd) {
		this.interestBlcOsd = interestBlcOsd;
	}

	public double getLoanBlc() {
		return loanBlc;
	}

	public void setLoanBlc(double loanBlc) {
		this.loanBlc = loanBlc;
	}

	public String getLoanEndDt() {
		return loanEndDt;
	}

	public void setLoanEndDt(String loanEndDt) {
		this.loanEndDt = loanEndDt;
	}

	public String getLoanForm4() {
		return loanForm4;
	}

	public void setLoanForm4(String loanForm4) {
		this.loanForm4 = loanForm4;
	}

	public String getLoanForm5() {
		return loanForm5;
	}

	public void setLoanForm5(String loanForm5) {
		this.loanForm5 = loanForm5;
	}

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public String getLoanStrDt() {
		return loanStrDt;
	}

	public void setLoanStrDt(String loanStrDt) {
		this.loanStrDt = loanStrDt;
	}

	public String getLoanTyp() {
		return loanTyp;
	}

	public void setLoanTyp(String loanTyp) {
		this.loanTyp = loanTyp;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	
	

	public int getExtendTm() {
		return extendTm;
	}

	public void setExtendTm(int extendTm) {
		this.extendTm = extendTm;
	}

	public int getRefinanceTm() {
		return refinanceTm;
	}

	public void setRefinanceTm(int refinanceTm) {
		this.refinanceTm = refinanceTm;
	}

	public int getSeq() {
		return seq;
	}

	public String getValidFlg() {
		return validFlg;
	}

	public void setValidFlg(String validFlg) {
		this.validFlg = validFlg;
	}

	public String getLawSuitFlg() {
		return lawSuitFlg;
	}

	public void setLawSuitFlg(String lawSuitFlg) {
		this.lawSuitFlg = lawSuitFlg;
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

	public double getContRate() {
		return contRate;
	}

	public void setContRate(double contRate) {
		this.contRate = contRate;
	} 

}
