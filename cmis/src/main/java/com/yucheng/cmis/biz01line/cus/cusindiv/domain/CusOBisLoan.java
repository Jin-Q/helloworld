package com.yucheng.cmis.biz01line.cus.cusindiv.domain;

import com.yucheng.cmis.pub.CMISDomain;

//他行交易－他行贷款
public class CusOBisLoan  implements CMISDomain{

	private CusIndiv cusIndiv;

	private String cusId;

	private double seq;

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

	private double extendTm;

	private double refinanceTm;

	private String loanForm4;

	private String loanForm5;

	private String blawsuit;

	private String validFlg;
	
	private double contRate;

	public String getBlawsuit() {
		return blawsuit;
	}

	public void setBlawsuit(String blawsuit) {
		this.blawsuit = blawsuit;
	}

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

	public CusIndiv getCusIndiv() {
		return cusIndiv;
	}

	public void setCusIndiv(CusIndiv cusIndiv) {
		this.cusIndiv = cusIndiv;
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

	public double getExtendTm() {
		return extendTm;
	}

	public void setExtendTm(double extendTm) {
		this.extendTm = extendTm;
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

	public double getRefinanceTm() {
		return refinanceTm;
	}

	public void setRefinanceTm(double refinanceTm) {
		this.refinanceTm = refinanceTm;
	}

	public double getSeq() {
		return seq;
	}

	public void setSeq(double seq) {
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

	public double getContRate() {
		return contRate;
	}

	public void setContRate(double contRate) {
		this.contRate = contRate;
	} 

}
