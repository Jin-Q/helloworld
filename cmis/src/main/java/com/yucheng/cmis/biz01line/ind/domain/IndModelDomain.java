
package com.yucheng.cmis.biz01line.ind.domain;

import com.yucheng.cmis.pub.CMISDomain;


/**
 * Title: IndModel.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Tue Mar 10 09:54:20 CST 2009
 * @version：1.0
 */
public class IndModelDomain implements CMISDomain {
	private String remark;
	private String modelNo;
	private String modelName;
	private String ratingRules;
	private String inputId;
	private String inputDate;
	private String inputBrId;
	private String ceilingLimit;
	private String lowerLimit;
	/**
 	 * @return 返回 remark
 	 */
	public String getRemark(){
		return remark;
	}
	/**
 	 * @设置 remark
 	 * @param remark
 	 */
	public void setRemark(String remark){
		this.remark = remark;
	}
	/**
 	 * @return 返回 modelNo
 	 */
	public String getModelNo(){
		return modelNo;
	}
	/**
 	 * @设置 modelNo
 	 * @param modelNo
 	 */
	public void setModelNo(String modelNo){
		this.modelNo = modelNo;
	}
	/**
 	 * @return 返回 modelName
 	 */
	public String getModelName(){
		return modelName;
	}
	/**
 	 * @设置 modelName
 	 * @param modelName
 	 */
	public void setModelName(String modelName){
		this.modelName = modelName;
	}
	/**
 	 * @return 返回 ratingRules
 	 */
	public String getRatingRules(){
		return ratingRules;
	}
	/**
 	 * @设置 ratingRules
 	 * @param ratingRules
 	 */
	public void setRatingRules(String ratingRules){
		this.ratingRules = ratingRules;
	}
	
	public String getInputId() {
		return inputId;
	}
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}
	public String getInputDate() {
		return inputDate;
	}
	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}
	public String getInputBrId() {
		return inputBrId;
	}
	public void setInputBrId(String inputBrId) {
		this.inputBrId = inputBrId;
	}
	
	
	public String getCeilingLimit() {
		return ceilingLimit;
	}
	public void setCeilingLimit(String ceilingLimit) {
		this.ceilingLimit = ceilingLimit;
	}
	public String getLowerLimit() {
		return lowerLimit;
	}
	public void setLowerLimit(String lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}