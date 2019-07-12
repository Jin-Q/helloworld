package com.yucheng.cmis.biz01line.ind.domain;

import com.yucheng.cmis.pub.CMISDomain;

 
/**
 * Title: IndOpt.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Tue Mar 10 15:48:44 CST 2009
 * @version：1.0
 */
public class IndOptDomain implements CMISDomain {
	private String indexNo;
	private String indDesc;
	private String indexValue;
	private double valueScore;
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
 	 * @return 返回 indDesc
 	 */
	public String getIndDesc(){
		return indDesc;
	}
	/**
 	 * @设置 indDesc
 	 * @param indDesc
 	 */
	public void setIndDesc(String indDesc){
		this.indDesc = indDesc;
	}
	/**
 	 * @return 返回 indexValue
 	 */
	public String getIndexValue(){
		return indexValue;
	}
	/**
 	 * @设置 indexValue
 	 * @param indexValue
 	 */
	public void setIndexValue(String indexValue){
		this.indexValue = indexValue;
	}
	/**
 	 * @return 返回 valueScore
 	 */
	public double getValueScore(){
		return valueScore;
	}
	/**
 	 * @设置 valueScore
 	 * @param valueScore
 	 */
	public void setValueScore(double valueScore){
		this.valueScore = valueScore;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
