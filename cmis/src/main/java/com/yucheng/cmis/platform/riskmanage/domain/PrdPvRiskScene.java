package com.yucheng.cmis.platform.riskmanage.domain;

import com.yucheng.cmis.pub.CMISDomain;

public class PrdPvRiskScene implements CMISDomain{
	
	 private String sceneId;
	 private String preventId;
	 private String itemId;
	 private String wfid;
	 private String riskLevel;
	 private String itemName;
	 
	 
	public String getSceneId() {
		return sceneId;
	}


	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}


	public String getPreventId() {
		return preventId;
	}


	public void setPreventId(String preventId) {
		this.preventId = preventId;
	}


	public String getItemId() {
		return itemId;
	}


	public void setItemId(String itemId) {
		this.itemId = itemId;
	}


	public String getWfid() {
		return wfid;
	}


	public void setWfid(String wfid) {
		this.wfid = wfid;
	}


	public String getRiskLevel() {
		return riskLevel;
	}


	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
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
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

    	// call父类的clone方法 

    	Object result = super.clone(); 



    	//TODO: 定制clone数据 

    	return result; 

    	} 

}
