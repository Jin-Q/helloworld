package com.yucheng.cmis.biz01line.cus.cusindiv.domain;
/**
 * Title: CusIndivAss.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat May 09 15:01:55 CST 2009
 * @version：1.0
 */
public class CusIndivAss  implements com.yucheng.cmis.pub.CMISDomain{
	private String cusId;
	private String indivAssId;
	private String indivAssTpy;
	private String indivAssName;
	private String indivAssPlr;
	private double indivAssNum;
	private double indivAss;
	private String indivAssDes;
	private String remark;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String lastUpdId;
	private String lastUpdDate;
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
 	 * @return 返回 indivAssId
 	 */
	public String getIndivAssId(){
		return indivAssId;
	}
	/**
 	 * @设置 indivAssId
 	 * @param indivAssId
 	 */
	public void setIndivAssId(String indivAssId){
		this.indivAssId = indivAssId;
	}
	/**
 	 * @return 返回 indivAssTpy
 	 */
	public String getIndivAssTpy(){
		return indivAssTpy;
	}
	/**
 	 * @设置 indivAssTpy
 	 * @param indivAssTpy
 	 */
	public void setIndivAssTpy(String indivAssTpy){
		this.indivAssTpy = indivAssTpy;
	}
	/**
 	 * @return 返回 indivAssName
 	 */
	public String getIndivAssName(){
		return indivAssName;
	}
	/**
 	 * @设置 indivAssName
 	 * @param indivAssName
 	 */
	public void setIndivAssName(String indivAssName){
		this.indivAssName = indivAssName;
	}
	/**
 	 * @return 返回 indivAssPlr
 	 */
	public String getIndivAssPlr(){
		return indivAssPlr;
	}
	/**
 	 * @设置 indivAssPlr
 	 * @param indivAssPlr
 	 */
	public void setIndivAssPlr(String indivAssPlr){
		this.indivAssPlr = indivAssPlr;
	}
	/**
 	 * @return 返回 indivAssNum
 	 */
	public double getIndivAssNum(){
		return indivAssNum;
	}
	/**
 	 * @设置 indivAssNum
 	 * @param indivAssNum
 	 */
	public void setIndivAssNum(double indivAssNum){
		this.indivAssNum = indivAssNum;
	}
	/**
 	 * @return 返回 indivAss
 	 */
	public double getIndivAss(){
		return indivAss;
	}
	/**
 	 * @设置 indivAss
 	 * @param indivAss
 	 */
	public void setIndivAss(double indivAss){
		this.indivAss = indivAss;
	}
	/**
 	 * @return 返回 indivAssDes
 	 */
	public String getIndivAssDes(){
		return indivAssDes;
	}
	/**
 	 * @设置 indivAssDes
 	 * @param indivAssDes
 	 */
	public void setIndivAssDes(String indivAssDes){
		this.indivAssDes = indivAssDes;
	}
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
 	 * @return 返回 inputId
 	 */
	public String getInputId(){
		return inputId;
	}
	/**
 	 * @设置 inputId
 	 * @param inputId
 	 */
	public void setInputId(String inputId){
		this.inputId = inputId;
	}
	/**
 	 * @return 返回 inputBrId
 	 */
	public String getInputBrId(){
		return inputBrId;
	}
	/**
 	 * @设置 inputBrId
 	 * @param inputBrId
 	 */
	public void setInputBrId(String inputBrId){
		this.inputBrId = inputBrId;
	}
	/**
 	 * @return 返回 inputDate
 	 */
	public String getInputDate(){
		return inputDate;
	}
	/**
 	 * @设置 inputDate
 	 * @param inputDate
 	 */
	public void setInputDate(String inputDate){
		this.inputDate = inputDate;
	}
	/**
 	 * @return 返回 lastUpdId
 	 */
	public String getLastUpdId(){
		return lastUpdId;
	}
	/**
 	 * @设置 lastUpdId
 	 * @param lastUpdId
 	 */
	public void setLastUpdId(String lastUpdId){
		this.lastUpdId = lastUpdId;
	}
	/**
 	 * @return 返回 lastUpdDate
 	 */
	public String getLastUpdDate(){
		return lastUpdDate;
	}
	/**
 	 * @设置 lastUpdDate
 	 * @param lastUpdDate
 	 */
	public void setLastUpdDate(String lastUpdDate){
		this.lastUpdDate = lastUpdDate;
	}
}