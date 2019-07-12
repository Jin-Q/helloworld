package com.yucheng.cmis.platform.riskmanage.domain;

import com.yucheng.cmis.pub.CMISDomain;

public class PrdPvRiskItem implements CMISDomain{
	
	private String itemId;
	private String itemName;
	private String itemDesc;
	private String itemRules;
	private String linkUrl;
	private String usedInd;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	
	
	public String getItemId() {
		return itemId;
	}


	public void setItemId(String itemId) {
		this.itemId = itemId;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public String getItemDesc() {
		return itemDesc;
	}


	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}


	public String getItemRules() {
		return itemRules;
	}


	public void setItemRules(String itemRules) {
		this.itemRules = itemRules;
	}


	public String getLinkUrl() {
		return linkUrl;
	}


	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}


	public String getUsedInd() {
		return usedInd;
	}


	public void setUsedInd(String usedInd) {
		this.usedInd = usedInd;
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


	/**
	 * clone
	 * @return obj
	 * @throws CloneNotSupportedException
	 */
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

    	// call父类的clone方法 

    	Object result = super.clone(); 



    	//TODO: 定制clone数据 

    	return result; 

    	} 
}
