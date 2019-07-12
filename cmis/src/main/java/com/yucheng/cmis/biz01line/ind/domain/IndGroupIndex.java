package com.yucheng.cmis.biz01line.ind.domain;

import com.yucheng.cmis.pub.CMISDomain;

 

/**
 * Title: IndGroup.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：
 * @version：1.0
 */
public class IndGroupIndex implements CMISDomain {
	private String groupNo;
	private String indexNo;
	private String indStdScore;
	private String weight;
	private String ruleClasspath;
	private String category;
	private String subCategory;
	private String disProperty;
	private String indDisType;
	private String seqNo;
	private String indexName;
	private String fullScore;
	private String scoreWay;
	private double referenceValue;	//参照值
	private double limitValue;		//极限值
	private String memo;			//评分标准
	private String limitFlag;		//极限值符号 1 大于 2 小于
	private String memoCal;			//评级计算标准
	
	
	public String getMemoCal() {
		return memoCal;
	}



	public void setMemoCal(String memoCal) {
		this.memoCal = memoCal;
	}



	public String getGroupNo() {
		return groupNo;
	}



	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}



	public String getIndexNo() {
		return indexNo;
	}



	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
	}



	public String getIndStdScore() {
		return indStdScore;
	}



	public void setIndStdScore(String indStdScore) {
		this.indStdScore = indStdScore;
	}



	public String getWeight() {
		return weight;
	}



	public void setWeight(String weight) {
		this.weight = weight;
	}



	public String getRuleClasspath() {
		return ruleClasspath;
	}



	public void setRuleClasspath(String ruleClasspath) {
		this.ruleClasspath = ruleClasspath;
	}



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
	}



	public String getSubCategory() {
		return subCategory;
	}



	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}



	public String getDisProperty() {
		return disProperty;
	}



	public void setDisProperty(String disProperty) {
		this.disProperty = disProperty;
	}



	public String getIndDisType() {
		return indDisType;
	}



	public void setIndDisType(String indDisType) {
		this.indDisType = indDisType;
	}



	public String getSeqNo() {
		return seqNo;
	}



	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}



	public String getIndexName() {
		return indexName;
	}



	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}



	public String getFullScore() {
		return fullScore;
	}



	public void setFullScore(String fullScore) {
		this.fullScore = fullScore;
	}



	public String getScoreWay() {
		return scoreWay;
	}



	public void setScoreWay(String scoreWay) {
		this.scoreWay = scoreWay;
	}
	
	


	public double getLimitValue() {
		return limitValue;
	}



	public void setLimit_value(double limitValue) {
		this.limitValue = limitValue;
	}



	public String getMemo() {
		return memo;
	}



	public void setMemo(String memo) {
		this.memo = memo;
	}



	public double getReferenceValue() {
		return referenceValue;
	}



	public void setReference_value(double referenceValue) {
		this.referenceValue = referenceValue;
	}

	

	public String getLimitFlag() {
		return limitFlag;
	}



	public void setLimitFlag(String limitFlag) {
		this.limitFlag = limitFlag;
	}



	public void setLimitValue(double limitValue) {
		this.limitValue = limitValue;
	}



	public void setReferenceValue(double referenceValue) {
		this.referenceValue = referenceValue;
	}



	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}