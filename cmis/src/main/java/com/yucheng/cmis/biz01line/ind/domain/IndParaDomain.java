package com.yucheng.cmis.biz01line.ind.domain;

import com.yucheng.cmis.pub.CMISDomain;

 
public class IndParaDomain implements CMISDomain { 
	private String indexNo;
	private String paraEnname;
	private String paraCnname;
	private String paraValType;
	private String paraValWay;
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
 	 * @return 返回 paraEnname
 	 */
	public String getParaEnname(){
		return paraEnname;
	}
	/**
 	 * @设置 paraEnname
 	 * @param paraEnname
 	 */
	public void setParaEnname(String paraEnname){
		this.paraEnname = paraEnname;
	}
	/**
 	 * @return 返回 paraCnname
 	 */
	public String getParaCnname(){
		return paraCnname;
	}
	/**
 	 * @设置 paraCnname
 	 * @param paraCnname
 	 */
	public void setParaCnname(String paraCnname){
		this.paraCnname = paraCnname;
	}
	/**
 	 * @return 返回 paraValType
 	 */
	public String getParaValType(){
		return paraValType;
	}
	/**
 	 * @设置 paraValType
 	 * @param paraValType
 	 */
	public void setParaValType(String paraValType){
		this.paraValType = paraValType;
	}
	/**
 	 * @return 返回 paraValWay
 	 */
	public String getParaValWay(){
		return paraValWay;
	}
	/**
 	 * @设置 paraValWay
 	 * @param paraValWay
 	 */
	public void setParaValWay(String paraValWay){
		this.paraValWay = paraValWay;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}