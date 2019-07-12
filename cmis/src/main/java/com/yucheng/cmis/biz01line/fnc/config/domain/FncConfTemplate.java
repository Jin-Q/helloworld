package com.yucheng.cmis.biz01line.fnc.config.domain;


import com.yucheng.cmis.pub.CMISDomain;
  /**
 *@Classname	FncConfTemplate.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-6 下午08:13:15  
 *@Copyright 	yuchengtech
 *@Author 		Yu
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncConfTemplate implements CMISDomain {
	
	private String fncId;
	
	private String fncName;
	
	private String fncBsStyleId;
	
	private String fncPlStyleId;
	
	private String fncCfStyleId;
	
	private String fncFiStyleId;
	
	private String fncRiStyleId;
	
	private String fncSmpStyleId;
	
	private String fncStyleId1;
	
	private String fncStyleId2;

	private String noInd;
	
	public String getNoInd() {
		return noInd;
	}

	public void setNoInd(String noInd) {
		this.noInd = noInd;
	}

	public String getComInd() {
		return comInd;
	}

	public void setComInd(String comInd) {
		this.comInd = comInd;
	}

	private String comInd;
	
	public String getFncBsStyleId() {
		return fncBsStyleId;
	}

	public void setFncBsStyleId(String fncBsStyleId) {
		this.fncBsStyleId = fncBsStyleId;
	}

	public String getFncCfStyleId() {
		return fncCfStyleId;
	}

	public void setFncCfStyleId(String fncCfStyleId) {
		this.fncCfStyleId = fncCfStyleId;
	}

	public String getFncFiStyleId() {
		return fncFiStyleId;
	}

	public void setFncFiStyleId(String fncFiStyleId) {
		this.fncFiStyleId = fncFiStyleId;
	}

	public String getFncId() {
		return fncId;
	}

	public void setFncId(String fncId) {
		this.fncId = fncId;
	}

	public String getFncName() {
		return fncName;
	}

	public void setFncName(String fncName) {
		this.fncName = fncName;
	}

	public String getFncPlStyleId() {
		return fncPlStyleId;
	}

	public void setFncPlStyleId(String fncPlStyleId) {
		this.fncPlStyleId = fncPlStyleId;
	}

	public String getFncRiStyleId() {
		return fncRiStyleId;
	}

	public void setFncRiStyleId(String fncRiStyleId) {
		this.fncRiStyleId = fncRiStyleId;
	}

	public String getFncSmpStyleId() {
		return fncSmpStyleId;
	}

	public void setFncSmpStyleId(String fncSmpStyleId) {
		this.fncSmpStyleId = fncSmpStyleId;
	}

	public String getFncStyleId1() {
		return fncStyleId1;
	}

	public void setFncStyleId1(String fncStyleId1) {
		this.fncStyleId1 = fncStyleId1;
	}

	public String getFncStyleId2() {
		return fncStyleId2;
	}

	public void setFncStyleId2(String fncStyleId2) {
		this.fncStyleId2 = fncStyleId2;
	}

	public/*protected*/ Object clone() throws CloneNotSupportedException { 



		FncConfTemplate result = new FncConfTemplate(); 

		result.setFncBsStyleId(fncBsStyleId);
		result.setFncCfStyleId(fncCfStyleId);
		result.setFncFiStyleId(fncFiStyleId);
		result.setFncId(fncId);
		result.setFncName(fncName);
		result.setFncPlStyleId(fncPlStyleId);
		result.setFncRiStyleId(fncRiStyleId);
		result.setFncSmpStyleId(fncSmpStyleId);
		result.setFncStyleId1(fncStyleId1);
		result.setFncStyleId2(fncStyleId2);
		result.setComInd(comInd);
		result.setNoInd(noInd);
		return result; 

		} 

	

}
