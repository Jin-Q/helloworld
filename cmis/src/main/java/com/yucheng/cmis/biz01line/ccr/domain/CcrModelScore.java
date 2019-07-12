package com.yucheng.cmis.biz01line.ccr.domain;

import com.yucheng.cmis.pub.CMISDomain;

/**
 * Title: CcrModelScore.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Wed Mar 18 11:19:39 CST 2009
 * @version：1.0
 */
public class CcrModelScore implements CMISDomain{
	private String modelNo;
	private String modelName;
	private String serno;
	private String cusId;
	private String ccrDate;
	private String natureScore;
	private java.math.BigDecimal quantityScore;
	private String limitScore;
	private String allScore;
	private String scoringManager;
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
 	 * @return 返回 serno
 	 */
	public String getSerno(){
		return serno;
	}
	/**
 	 * @设置 serno
 	 * @param serno
 	 */
	public void setSerno(String serno){
		this.serno = serno;
	}
	/**
 	 * @设置 cusId
 	 * @param cusId
 	 */	
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}	
	/**
 	 * @return 返回 ccrDate
 	 */
	public String getCcrDate(){
		return ccrDate;
	}
	/**
 	 * @设置 ccrDate
 	 * @param ccrDate
 	 */
	public void setCcrDate(String ccrDate){
		this.ccrDate = ccrDate;
	}
	/**
 	 * @return 返回 natureScore
 	 */
	public String getNatureScore(){
		return natureScore;
	}
	/**
 	 * @设置 natureScore
 	 * @param natureScore
 	 */
	public void setNatureScore(String natureScore){
		this.natureScore = natureScore;
	}
	/**
 	 * @return 返回 quantityScore
 	 */
	public java.math.BigDecimal getQuantityScore(){
		return quantityScore;
	}
	/**
 	 * @设置 quantityScore
 	 * @param quantityScore
 	 */
	public void setQuantityScore(java.math.BigDecimal quantityScore){
		this.quantityScore = quantityScore;
	}
	/**
 	 * @return 返回 limitScore
 	 */
	public String getLimitScore(){
		return limitScore;
	}
	/**
 	 * @设置 limitScore
 	 * @param limitScore
 	 */
	public void setLimitScore(String limitScore){
		this.limitScore = limitScore;
	}
	/**
 	 * @return 返回 allScore
 	 */
	public String getAllScore(){
		return allScore;
	}
	/**
 	 * @设置 allScore
 	 * @param allScore
 	 */
	public void setAllScore(String allScore){
		this.allScore = allScore;
	}
	/**
 	 * @return 返回 scoringManager
 	 */
	public String getScoringManager(){
		return scoringManager;
	}
	/**
 	 * @设置 scoringManager
 	 * @param scoringManager
 	 */
	public void setScoringManager(String scoringManager){
		this.scoringManager = scoringManager;
	}
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 


}