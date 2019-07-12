package com.yucheng.cmis.biz01line.ind.domain;

import com.yucheng.cmis.pub.CMISDomain;



/**
 * Title: IndGroupIndex.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Tue Mar 10 15:48:44 CST 2009
 * @version：1.0
 */
public class IndGrpIndexDomain implements CMISDomain {
	private String groupNo;
	private String indexNo;
	private String indStdScore;
	private float weight;
	private String ruleClasspath;
	private String category;
	private String subCategory;
	private String disProperty;
	private String indDisType;
	private int seqNo;
	private String fullScore;
	private String scoreWay;
	/**
 	 * @return 返回 groupNo
 	 */
	public String getGroupNo(){
		return groupNo;
	}
	/**
 	 * @设置 groupNo
 	 * @param groupNo
 	 */
	public void setGroupNo(String groupNo){
		this.groupNo = groupNo;
	}
	/**
 	 * @return 返回 indexNo
 	 */
	public String getIndexNo(){
		return indexNo;
	}
	/**
 	 * @设置 indexNo
 	 * @param indexNo
 	 */
	public void setIndexNo(String indexNo){
		this.indexNo = indexNo;
	}
	/**
 	 * @return 返回 indStdScore
 	 */
	public String getIndStdScore(){
		return indStdScore;
	}
	/**
 	 * @设置 indStdScore
 	 * @param indStdScore
 	 */
	public void setIndStdScore(String indStdScore){
		this.indStdScore = indStdScore;
	}
	/**
 	 * @return 返回 weight
 	 */
	public float getWeight(){
		return weight;
	}
	/**
 	 * @设置 weight
 	 * @param weight
 	 */
	public void setWeight(float weight){
		this.weight = weight;
	}
	/**
 	 * @return 返回 ruleClasspath
 	 */
	public String getRuleClasspath(){
		return ruleClasspath;
	}
	/**
 	 * @设置 ruleClasspath
 	 * @param ruleClasspath
 	 */
	public void setRuleClasspath(String ruleClasspath){
		this.ruleClasspath = ruleClasspath;
	}
	/**
 	 * @return 返回 category
 	 */
	public String getCategory(){
		return category;
	}
	/**
 	 * @设置 category
 	 * @param category
 	 */
	public void setCategory(String category){
		this.category = category;
	}
	/**
 	 * @return 返回 subCategory
 	 */
	public String getSubCategory(){
		return subCategory;
	}
	/**
 	 * @设置 subCategory
 	 * @param subCategory
 	 */
	public void setSubCategory(String subCategory){
		this.subCategory = subCategory;
	}
	/**
 	 * @return 返回 disProperty
 	 */
	public String getDisProperty(){
		return disProperty;
	}
	/**
 	 * @设置 disProperty
 	 * @param disProperty
 	 */
	public void setDisProperty(String disProperty){
		this.disProperty = disProperty;
	}
	/**
 	 * @return 返回 indDisType
 	 */
	public String getIndDisType(){
		return indDisType;
	}
	/**
 	 * @设置 indDisType
 	 * @param indDisType
 	 */
	public void setIndDisType(String indDisType){
		this.indDisType = indDisType;
	}
	/**
 	 * @return 返回 seqNo
 	 */
	public int getSeqNo(){
		return seqNo;
	}
	/**
 	 * @设置 seqNo
 	 * @param seqNo
 	 */
	public void setSeqNo(int seqNo){
		this.seqNo = seqNo;
	}
	/**
 	 * @return 返回 fullScore
 	 */
	public String getFullScore(){
		return fullScore;
	}
	/**
 	 * @设置 fullScore
 	 * @param fullScore
 	 */
	public void setFullScore(String fullScore){
		this.fullScore = fullScore;
	}
	/**
 	 * @return 返回 scoreWay
 	 */
	public String getScoreWay(){
		return scoreWay;
	}
	/**
 	 * @设置 scoreWay
 	 * @param scoreWay
 	 */
	public void setScoreWay(String scoreWay){
		this.scoreWay = scoreWay;
	}
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
