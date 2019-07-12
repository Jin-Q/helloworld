package com.yucheng.cmis.biz01line.lmt.domain.bizarea;

import com.yucheng.cmis.pub.CMISDomain;

public class LmtAgrBizAreaSupmk implements CMISDomain {

	private String bizAreaNo;
	private String supmkSerno;
	private String operTrade;
	private String operModel;
	private String tradeRank;
	private String providYear;
	private String netAsset;
	private String otherCond;

	public String getBizAreaNo() {
		return bizAreaNo;
	}

	public void setBizAreaNo(String bizAreaNo) {
		this.bizAreaNo = bizAreaNo;
	}

	public String getSupmkSerno() {
		return supmkSerno;
	}

	public void setSupmkSerno(String supmkSerno) {
		this.supmkSerno = supmkSerno;
	}

	public String getOperTrade() {
		return operTrade;
	}

	public void setOperTrade(String operTrade) {
		this.operTrade = operTrade;
	}

	public String getOperModel() {
		return operModel;
	}

	public void setOperModel(String operModel) {
		this.operModel = operModel;
	}

	public String getTradeRank() {
		return tradeRank;
	}

	public void setTradeRank(String tradeRank) {
		this.tradeRank = tradeRank;
	}

	public String getProvidYear() {
		return providYear;
	}

	public void setProvidYear(String providYear) {
		this.providYear = providYear;
	}

	public String getNetAsset() {
		return netAsset;
	}

	public void setNetAsset(String netAsset) {
		this.netAsset = netAsset;
	}

	public String getOtherCond() {
		return otherCond;
	}

	public void setOtherCond(String otherCond) {
		this.otherCond = otherCond;
	}

	public Object clone() throws CloneNotSupportedException {
        Object result = super.clone();
        return result;
    } 
}
