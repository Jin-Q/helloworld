/**
 * 
 */
package com.yucheng.cmis.platform.riskmanage.domain;

import com.yucheng.cmis.pub.CMISDomain;

/**
 * 风险拦截应用DOMAIN
 * @author zhangming
 * 
 */
public class IqpPvRiskResult implements CMISDomain {
	
	private String resultId;
	private String serno;
	private String wfid;
	private String nodeId;
	private String itemId;
	private String riskLevel;
	private String passState;
	private String itemName;
	private String itemDesc;
	private String linkUrl;
	
	

	public String getLinkUrl() {
		return linkUrl;
	}



	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}



	public String getItemDesc() {
		return itemDesc;
	}



	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}



	public String getResultId() {
		return resultId;
	}



	public void setResultId(String resultId) {
		this.resultId = resultId;
	}



	public String getSerno() {
		return serno;
	}



	public void setSerno(String serno) {
		this.serno = serno;
	}



	public String getWfid() {
		return wfid;
	}



	public void setWfid(String wfid) {
		this.wfid = wfid;
	}



	public String getNodeId() {
		return nodeId;
	}



	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}



	public String getItemId() {
		return itemId;
	}



	public void setItemId(String itemId) {
		this.itemId = itemId;
	}



	public String getRiskLevel() {
		return riskLevel;
	}



	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}



	public String getPassState() {
		return passState;
	}



	public void setPassState(String passState) {
		this.passState = passState;
	}



	public String getItemName() {
		return itemName;
	}



	public void setItemName(String itemName) {
		this.itemName = itemName;
	}



	/**
	 * clone
	 * @return obj
	 * @throws CloneNotSupportedException
	 */
	public/* protected */Object clone() throws CloneNotSupportedException {
		// call父类的clone方法
		Object result = super.clone();
		// TODO: 定制clone数据
		return result;
	}
}
