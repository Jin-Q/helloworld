package com.yucheng.cmis.biz01line.ccr.domain;

import com.yucheng.cmis.pub.CMISDomain;

/**
 * Title: CcrBizModel.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Thu May 14 10:55:27 CST 2009
 * @version：1.0
 */
public class CcrBizModel   implements CMISDomain{
	private String comCllTyp;
	private String comOptScale;
	private String indexNo;
	private String indexName;
	private double excellentScore;
	private double goodScore;
	private double averageScore;
	private double lowerScore;
	private double worseScore;
	private double worstScore;
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
 	 * @return 返回 comCllTyp
 	 */
	public String getComCllTyp(){
		return comCllTyp;
	}
	/**
 	 * @设置 comCllTyp
 	 * @param comCllTyp
 	 */
	public void setComCllTyp(String comCllTyp){
		this.comCllTyp = comCllTyp;
	}
	/**
 	 * @return 返回 comOptScale
 	 */
	public String getComOptScale(){
		return comOptScale;
	}
	/**
 	 * @设置 comOptScale
 	 * @param comOptScale
 	 */
	public void setComOptScale(String comOptScale){
		this.comOptScale = comOptScale;
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
 	 * @return 返回 indexName
 	 */
	public String getIndexName(){
		return indexName;
	}
	/**
 	 * @设置 indexName
 	 * @param indexName
 	 */
	public void setIndexName(String indexName){
		this.indexName = indexName;
	}
	/**
 	 * @return 返回 excellentScore
 	 */
	public double getExcellentScore(){
		return excellentScore;
	}
	/**
 	 * @设置 excellentScore
 	 * @param excellentScore
 	 */
	public void setExcellentScore(double excellentScore){
		this.excellentScore = excellentScore;
	}
	/**
 	 * @return 返回 goodScore
 	 */
	public double getGoodScore(){
		return goodScore;
	}
	/**
 	 * @设置 goodScore
 	 * @param goodScore
 	 */
	public void setGoodScore(double goodScore){
		this.goodScore = goodScore;
	}
	/**
 	 * @return 返回 averageScore
 	 */
	public double getAverageScore(){
		return averageScore;
	}
	/**
 	 * @设置 averageScore
 	 * @param averageScore
 	 */
	public void setAverageScore(double averageScore){
		this.averageScore = averageScore;
	}
	/**
 	 * @return 返回 lowerScore
 	 */
	public double getLowerScore(){
		return lowerScore;
	}
	/**
 	 * @设置 lowerScore
 	 * @param lowerScore
 	 */
	public void setLowerScore(double lowerScore){
		this.lowerScore = lowerScore;
	}
	/**
 	 * @return 返回 worseScore
 	 */
	public double getWorseScore(){
		return worseScore;
	}
	/**
 	 * @设置 worseScore
 	 * @param worseScore
 	 */
	public void setWorseScore(double worseScore){
		this.worseScore = worseScore;
	}
	/**
 	 * @return 返回 worstScore
 	 */
	public double getWorstScore(){
		return worstScore;
	}
	/**
 	 * @设置 worstScore
 	 * @param worstScore
 	 */
	public void setWorstScore(double worstScore){
		this.worstScore = worstScore;
	}


}