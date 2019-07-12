package com.yucheng.cmis.biz01line.lmt.domain.bizarea;

import com.yucheng.cmis.pub.CMISDomain;

public class LmtAgrBizAreaComn implements CMISDomain {

	private String bizAreaNo;
	private String shopType;
	private String mainPrd;
	private String operModel;
	private String otherCond;
	private String serno;

	public String getBizAreaNo() {
		return bizAreaNo;
	}


	public void setBizAreaNo(String bizAreaNo) {
		this.bizAreaNo = bizAreaNo;
	}


	public String getShopType() {
		return shopType;
	}


	public void setShopType(String shopType) {
		this.shopType = shopType;
	}


	public String getMainPrd() {
		return mainPrd;
	}


	public void setMainPrd(String mainPrd) {
		this.mainPrd = mainPrd;
	}


	public String getOperModel() {
		return operModel;
	}


	public void setOperModel(String operModel) {
		this.operModel = operModel;
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
