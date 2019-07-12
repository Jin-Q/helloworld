package com.yucheng.cmis.biz01line.lmt.domain.bizarea;

import com.yucheng.cmis.pub.CMISDomain;

public class LmtAgrBizAreaCore implements CMISDomain {

	private String BizAreaNo;
	private String coreConType;
	private String yearSaleAmt;
	private String foreDebtBal;
	private String coopYear;
	private String otherCond;
	private String serno;

	public String getBizAreaNo() {
		return BizAreaNo;
	}

	public void setBizAreaNo(String BizAreaNo) {
		this.BizAreaNo = BizAreaNo;
	}

	public String getCoreConType() {
		return coreConType;
	}

	public void setCoreConType(String coreConType) {
		this.coreConType = coreConType;
	}

	public String getYearSaleAmt() {
		return yearSaleAmt;
	}

	public void setYearSaleAmt(String yearSaleAmt) {
		this.yearSaleAmt = yearSaleAmt;
	}

	public String getForeDebtBal() {
		return foreDebtBal;
	}

	public void setForeDebtBal(String foreDebtBal) {
		this.foreDebtBal = foreDebtBal;
	}

	public String getCoopYear() {
		return coopYear;
	}

	public void setCoopYear(String coopYear) {
		this.coopYear = coopYear;
	}

	public String getOtherCond() {
		return otherCond;
	}

	public void setOtherCond(String otherCond) {
		this.otherCond = otherCond;
	}

	public String getSerno() {
		return serno;
	}

	public void setSerno(String serno) {
		this.serno = serno;
	}

	public Object clone() throws CloneNotSupportedException {
        Object result = super.clone();
        return result;
    } 
	
}
