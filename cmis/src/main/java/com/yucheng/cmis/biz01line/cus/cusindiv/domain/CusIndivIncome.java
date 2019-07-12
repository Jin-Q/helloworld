package com.yucheng.cmis.biz01line.cus.cusindiv.domain;
/**
 * Title: CusIndivIncome.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat May 09 15:01:55 CST 2009
 * @version：1.0
 */
public class CusIndivIncome  implements com.yucheng.cmis.pub.CMISDomain{
	private String indivSurYear;
	private double indivAnnIncm;
	private String remark;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String lastUpdId;
	private String lastUpdDate;
	private String cusId;
	private String indivDeposits;
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
 	 * @return 返回 indivSurYear
 	 */
	public String getIndivSurYear(){
		return indivSurYear;
	}
	/**
 	 * @设置 indivSurYear
 	 * @param indivSurYear
 	 */
	public void setIndivSurYear(String indivSurYear){
		this.indivSurYear = indivSurYear;
	}
	/**
 	 * @return 返回 indivAnnIncm
 	 */
	public double getIndivAnnIncm(){
		return indivAnnIncm;
	}
	/**
 	 * @设置 indivAnnIncm
 	 * @param indivAnnIncm
 	 */
	public void setIndivAnnIncm(double indivAnnIncm){
		this.indivAnnIncm = indivAnnIncm;
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
 	 * @return 返回 indivDeposits
 	 */
	public String getIndivDeposits(){
		return indivDeposits;
	}
	/**
 	 * @设置 indivDeposits
 	 * @param indivDeposits
 	 */
	public void setIndivDeposits(String indivDeposits){
		this.indivDeposits = indivDeposits;
	}
}