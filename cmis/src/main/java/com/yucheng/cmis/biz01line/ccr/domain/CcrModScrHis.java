package com.yucheng.cmis.biz01line.ccr.domain;
/**
 * Title: CcrModScrHis.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat Apr 18 09:05:13 CST 2009
 * @version：1.0
 */
public class CcrModScrHis  implements com.yucheng.cmis.pub.CMISDomain{
	private String modelNo;
	private String modelName;
	private String serno;
	private String cusId;
	private String ccrDate;
	private String natureScore;
	private double quantityScore;
	private String limitScore;
	private String allScore;
	private String scoringManager;
	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone()throws CloneNotSupportedException{
		Object result = super.clone();
		//TODO: 定制clone数据
		return result;
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
 	 * @return 返回 cusId
 	 */
	public String getCusId(){
		return cusId;
	}
	/**
 	 * @设置 cusId
 	 * @param cusId
 	 */
	public void setCusId(String cusId){
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
	public double getQuantityScore(){
		return quantityScore;
	}
	/**
 	 * @设置 quantityScore
 	 * @param quantityScore
 	 */
	public void setQuantityScore(double quantityScore){
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
}