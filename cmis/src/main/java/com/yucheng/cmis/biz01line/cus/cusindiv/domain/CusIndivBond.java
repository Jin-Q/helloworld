package com.yucheng.cmis.biz01line.cus.cusindiv.domain;
/**
 * Title: CusIndivBond.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat May 09 15:01:55 CST 2009
 * @version：1.0
 */
public class CusIndivBond  implements com.yucheng.cmis.pub.CMISDomain{
	private String cusId;
	private String indivBondId;
	private String indivBondTyp;
	private String indivBondName;
	private double indivBondEvaAmt;
	private String indivBondPub;
	private String indivBondStrDt;
	private String indivBondEndDt;
	private String indivBondStatus;
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
 	 * @return 返回 indivBondId
 	 */
	public String getIndivBondId(){
		return indivBondId;
	}
	/**
 	 * @设置 indivBondId
 	 * @param indivBondId
 	 */
	public void setIndivBondId(String indivBondId){
		this.indivBondId = indivBondId;
	}
	/**
 	 * @return 返回 indivBondTyp
 	 */
	public String getIndivBondTyp(){
		return indivBondTyp;
	}
	/**
 	 * @设置 indivBondTyp
 	 * @param indivBondTyp
 	 */
	public void setIndivBondTyp(String indivBondTyp){
		this.indivBondTyp = indivBondTyp;
	}
	/**
 	 * @return 返回 indivBondName
 	 */
	public String getIndivBondName(){
		return indivBondName;
	}
	/**
 	 * @设置 indivBondName
 	 * @param indivBondName
 	 */
	public void setIndivBondName(String indivBondName){
		this.indivBondName = indivBondName;
	}
	/**
 	 * @return 返回 indivBondEvaAmt
 	 */
	public double getIndivBondEvaAmt(){
		return indivBondEvaAmt;
	}
	/**
 	 * @设置 indivBondEvaAmt
 	 * @param indivBondEvaAmt
 	 */
	public void setIndivBondEvaAmt(double indivBondEvaAmt){
		this.indivBondEvaAmt = indivBondEvaAmt;
	}
	/**
 	 * @return 返回 indivBondPub
 	 */
	public String getIndivBondPub(){
		return indivBondPub;
	}
	/**
 	 * @设置 indivBondPub
 	 * @param indivBondPub
 	 */
	public void setIndivBondPub(String indivBondPub){
		this.indivBondPub = indivBondPub;
	}
	/**
 	 * @return 返回 indivBondStrDt
 	 */
	public String getIndivBondStrDt(){
		return indivBondStrDt;
	}
	/**
 	 * @设置 indivBondStrDt
 	 * @param indivBondStrDt
 	 */
	public void setIndivBondStrDt(String indivBondStrDt){
		this.indivBondStrDt = indivBondStrDt;
	}
	/**
 	 * @return 返回 indivBondEndDt
 	 */
	public String getIndivBondEndDt(){
		return indivBondEndDt;
	}
	/**
 	 * @设置 indivBondEndDt
 	 * @param indivBondEndDt
 	 */
	public void setIndivBondEndDt(String indivBondEndDt){
		this.indivBondEndDt = indivBondEndDt;
	}
	/**
 	 * @return 返回 indivBondStatus
 	 */
	public String getIndivBondStatus(){
		return indivBondStatus;
	}
	/**
 	 * @设置 indivBondStatus
 	 * @param indivBondStatus
 	 */
	public void setIndivBondStatus(String indivBondStatus){
		this.indivBondStatus = indivBondStatus;
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