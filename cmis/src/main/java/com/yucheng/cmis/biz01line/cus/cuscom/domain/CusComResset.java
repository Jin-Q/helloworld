package com.yucheng.cmis.biz01line.cus.cuscom.domain;
/**
 * Title: CusComResset.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat May 09 15:05:20 CST 2009
 * @version：1.0
 */
public class CusComResset  implements com.yucheng.cmis.pub.CMISDomain{
	private String cusId;
	private double seq;
	private String comAssTyp;
	private String comAssName;
	private double comAssFatArea;
	private double comAssRegArea;
	private double comAssNumber;
	private String comAssBuyDate;
	private double comAssOriAmt;
	private double comAssEvaAmt;
	private String comAssCollInfo;
	private double comAssRpn;
	private String remark;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String lastUpdId;
	private String lastUpdDate;
	private double zmje;
	/*
	 * zmje 账面金额.add by wak 2009-12-15
	 */
	public double getZmje() {
		return zmje;
	}
	public void setZmje(double zmje) {
		this.zmje = zmje;
	}
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
 	 * @return 返回 seq
 	 */
	public double getSeq(){
		return seq;
	}
	/**
 	 * @设置 seq
 	 * @param seq
 	 */
	public void setSeq(double seq){
		this.seq = seq;
	}
	/**
 	 * @return 返回 comAssTyp
 	 */
	public String getComAssTyp(){
		return comAssTyp;
	}
	/**
 	 * @设置 comAssTyp
 	 * @param comAssTyp
 	 */
	public void setComAssTyp(String comAssTyp){
		this.comAssTyp = comAssTyp;
	}
	/**
 	 * @return 返回 comAssName
 	 */
	public String getComAssName(){
		return comAssName;
	}
	/**
 	 * @设置 comAssName
 	 * @param comAssName
 	 */
	public void setComAssName(String comAssName){
		this.comAssName = comAssName;
	}
	/**
 	 * @return 返回 comAssFatArea
 	 */
	public double getComAssFatArea(){
		return comAssFatArea;
	}
	/**
 	 * @设置 comAssFatArea
 	 * @param comAssFatArea
 	 */
	public void setComAssFatArea(double comAssFatArea){
		this.comAssFatArea = comAssFatArea;
	}
	/**
 	 * @return 返回 comAssRegArea
 	 */
	public double getComAssRegArea(){
		return comAssRegArea;
	}
	/**
 	 * @设置 comAssRegArea
 	 * @param comAssRegArea
 	 */
	public void setComAssRegArea(double comAssRegArea){
		this.comAssRegArea = comAssRegArea;
	}
	/**
 	 * @return 返回 comAssNumber
 	 */
	public double getComAssNumber(){
		return comAssNumber;
	}
	/**
 	 * @设置 comAssNumber
 	 * @param comAssNumber
 	 */
	public void setComAssNumber(double comAssNumber){
		this.comAssNumber = comAssNumber;
	}
	/**
 	 * @return 返回 comAssBuyDate
 	 */
	public String getComAssBuyDate(){
		return comAssBuyDate;
	}
	/**
 	 * @设置 comAssBuyDate
 	 * @param comAssBuyDate
 	 */
	public void setComAssBuyDate(String comAssBuyDate){
		this.comAssBuyDate = comAssBuyDate;
	}
	/**
 	 * @return 返回 comAssOriAmt
 	 */
	public double getComAssOriAmt(){
		return comAssOriAmt;
	}
	/**
 	 * @设置 comAssOriAmt
 	 * @param comAssOriAmt
 	 */
	public void setComAssOriAmt(double comAssOriAmt){
		this.comAssOriAmt = comAssOriAmt;
	}
	/**
 	 * @return 返回 comAssEvaAmt
 	 */
	public double getComAssEvaAmt(){
		return comAssEvaAmt;
	}
	/**
 	 * @设置 comAssEvaAmt
 	 * @param comAssEvaAmt
 	 */
	public void setComAssEvaAmt(double comAssEvaAmt){
		this.comAssEvaAmt = comAssEvaAmt;
	}
	/**
 	 * @return 返回 comAssCollInfo
 	 */
	public String getComAssCollInfo(){
		return comAssCollInfo;
	}
	/**
 	 * @设置 comAssCollInfo
 	 * @param comAssCollInfo
 	 */
	public void setComAssCollInfo(String comAssCollInfo){
		this.comAssCollInfo = comAssCollInfo;
	}
	/**
 	 * @return 返回 comAssRpn
 	 */
	public double getComAssRpn(){
		return comAssRpn;
	}
	/**
 	 * @设置 comAssRpn
 	 * @param comAssRpn
 	 */
	public void setComAssRpn(double comAssRpn){
		this.comAssRpn = comAssRpn;
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