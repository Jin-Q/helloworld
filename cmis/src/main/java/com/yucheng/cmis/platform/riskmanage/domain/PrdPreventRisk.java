package com.yucheng.cmis.platform.riskmanage.domain;

import com.yucheng.cmis.pub.CMISDomain;

public class PrdPreventRisk implements CMISDomain{
	
	
	private String preventId;
	private String preventDesc;
	private String usedInd;
	private String memo;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	
	
	public String getPreventId() {
		return preventId;
	}


	public void setPreventId(String preventId) {
		this.preventId = preventId;
	}


	public String getPreventDesc() {
		return preventDesc;
	}


	public void setPreventDesc(String preventDesc) {
		this.preventDesc = preventDesc;
	}


	public String getUsedInd() {
		return usedInd;
	}


	public void setUsedInd(String usedInd) {
		this.usedInd = usedInd;
	}


	public String getMemo() {
		return memo;
	}


	public void setMemo(String memo) {
		this.memo = memo;
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
