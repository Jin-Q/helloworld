package com.yucheng.cmis.biz01line.ind.domain;

import com.yucheng.cmis.pub.CMISDomain;

 

/**
 * Title: IndLibDomain.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Tue Mar 10 15:44:30 CST 2009
 * @version：1.0
 */
public class IndLibDomain implements CMISDomain {
	private String indexNo;
	private String indexName;
	private String parIndexNo;
	private String indexProperty;
	private String indexType;
	private String inputType;
	private String inputClasspath;
	private String exeCycle;
	private String indexLevel;
	private String fncIndexRpt;
	
	public String getFncIndexRpt() {
		return fncIndexRpt;
	}
	public void setFncIndexRpt(String fncIndexRpt) {
		this.fncIndexRpt = fncIndexRpt;
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
 	 * @return 返回 parIndexNo
 	 */
	public String getParIndexNo(){
		return parIndexNo;
	}
	/**
 	 * @设置 parIndexNo
 	 * @param parIndexNo
 	 */
	public void setParIndexNo(String parIndexNo){
		this.parIndexNo = parIndexNo;
	}
	/**
 	 * @return 返回 indexProperty
 	 */
	public String getIndexProperty(){
		return indexProperty;
	}
	/**
 	 * @设置 indexProperty
 	 * @param indexProperty
 	 */
	public void setIndexProperty(String indexProperty){
		this.indexProperty = indexProperty;
	}
	/**
 	 * @return 返回 indexType
 	 */
	public String getIndexType(){
		return indexType;
	}
	/**
 	 * @设置 indexType
 	 * @param indexType
 	 */
	public void setIndexType(String indexType){
		this.indexType = indexType;
	}
	/**
 	 * @return 返回 inputType
 	 */
	public String getInputType(){
		return inputType;
	}
	/**
 	 * @设置 inputType
 	 * @param inputType
 	 */
	public void setInputType(String inputType){
		this.inputType = inputType;
	}
	/**
 	 * @return 返回 inputClasspath
 	 */
	public String getInputClasspath(){
		return inputClasspath;
	}
	/**
 	 * @设置 inputClasspath
 	 * @param inputClasspath
 	 */
	public void setInputClasspath(String inputClasspath){
		this.inputClasspath = inputClasspath;
	}
	/**
 	 * @return 返回 exeCycle
 	 */
	public String getExeCycle(){
		return exeCycle;
	}
	/**
 	 * @设置 exeCycle
 	 * @param exeCycle
 	 */
	public void setExeCycle(String exeCycle){
		this.exeCycle = exeCycle;
	}
	/**
 	 * @return 返回 indexLevel
 	 */
	public String getIndexLevel(){
		return indexLevel;
	}
	/**
 	 * @设置 indexLevel
 	 * @param indexLevel
 	 */
	public void setIndexLevel(String indexLevel){
		this.indexLevel = indexLevel;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
