package com.yucheng.cmis.biz01line.cus.cuscom.domain;
/**
 * Title: CusComFinaBond.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat May 09 15:05:20 CST 2009
 * @version：1.0
 */
public class CusComFinaBond  implements com.yucheng.cmis.pub.CMISDomain{
	private String comBondCurTyp;
	private double comBondAmt;
	private double comBondRate;
	private String comBondMrkFlg;
	private String comBondMrkPlace;
	private String comBondMrkBrs;
	private String comBondCrtInfo;
	private String comBondCrtOrg;
	private double comBondEvaAmt;
	private String remark;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String lastUpdId;
	private String lastUpdDate;
	private String cusId;
	private double seq;
	private String comBondPubDt;
	private String comBondName;
	private String comBondTyp;
	private double comBondTrm;
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
 	 * @return 返回 comBondCurTyp
 	 */
	public String getComBondCurTyp(){
		return comBondCurTyp;
	}
	/**
 	 * @设置 comBondCurTyp
 	 * @param comBondCurTyp
 	 */
	public void setComBondCurTyp(String comBondCurTyp){
		this.comBondCurTyp = comBondCurTyp;
	}
	/**
 	 * @return 返回 comBondAmt
 	 */
	public double getComBondAmt(){
		return comBondAmt;
	}
	/**
 	 * @设置 comBondAmt
 	 * @param comBondAmt
 	 */
	public void setComBondAmt(double comBondAmt){
		this.comBondAmt = comBondAmt;
	}
	/**
 	 * @return 返回 comBondRate
 	 */
	public double getComBondRate(){
		return comBondRate;
	}
	/**
 	 * @设置 comBondRate
 	 * @param comBondRate
 	 */
	public void setComBondRate(double comBondRate){
		this.comBondRate = comBondRate;
	}
	/**
 	 * @return 返回 comBondMrkFlg
 	 */
	public String getComBondMrkFlg(){
		return comBondMrkFlg;
	}
	/**
 	 * @设置 comBondMrkFlg
 	 * @param comBondMrkFlg
 	 */
	public void setComBondMrkFlg(String comBondMrkFlg){
		this.comBondMrkFlg = comBondMrkFlg;
	}
	/**
 	 * @return 返回 comBondMrkPlace
 	 */
	public String getComBondMrkPlace(){
		return comBondMrkPlace;
	}
	/**
 	 * @设置 comBondMrkPlace
 	 * @param comBondMrkPlace
 	 */
	public void setComBondMrkPlace(String comBondMrkPlace){
		this.comBondMrkPlace = comBondMrkPlace;
	}
	/**
 	 * @return 返回 comBondMrkBrs
 	 */
	public String getComBondMrkBrs(){
		return comBondMrkBrs;
	}
	/**
 	 * @设置 comBondMrkBrs
 	 * @param comBondMrkBrs
 	 */
	public void setComBondMrkBrs(String comBondMrkBrs){
		this.comBondMrkBrs = comBondMrkBrs;
	}
	/**
 	 * @return 返回 comBondCrtInfo
 	 */
	public String getComBondCrtInfo(){
		return comBondCrtInfo;
	}
	/**
 	 * @设置 comBondCrtInfo
 	 * @param comBondCrtInfo
 	 */
	public void setComBondCrtInfo(String comBondCrtInfo){
		this.comBondCrtInfo = comBondCrtInfo;
	}
	/**
 	 * @return 返回 comBondCrtOrg
 	 */
	public String getComBondCrtOrg(){
		return comBondCrtOrg;
	}
	/**
 	 * @设置 comBondCrtOrg
 	 * @param comBondCrtOrg
 	 */
	public void setComBondCrtOrg(String comBondCrtOrg){
		this.comBondCrtOrg = comBondCrtOrg;
	}
	/**
 	 * @return 返回 comBondEvaAmt
 	 */
	public double getComBondEvaAmt(){
		return comBondEvaAmt;
	}
	/**
 	 * @设置 comBondEvaAmt
 	 * @param comBondEvaAmt
 	 */
	public void setComBondEvaAmt(double comBondEvaAmt){
		this.comBondEvaAmt = comBondEvaAmt;
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
 	 * @return 返回 comBondPubDt
 	 */
	public String getComBondPubDt(){
		return comBondPubDt;
	}
	/**
 	 * @设置 comBondPubDt
 	 * @param comBondPubDt
 	 */
	public void setComBondPubDt(String comBondPubDt){
		this.comBondPubDt = comBondPubDt;
	}
	/**
 	 * @return 返回 comBondName
 	 */
	public String getComBondName(){
		return comBondName;
	}
	/**
 	 * @设置 comBondName
 	 * @param comBondName
 	 */
	public void setComBondName(String comBondName){
		this.comBondName = comBondName;
	}
	/**
 	 * @return 返回 comBondTyp
 	 */
	public String getComBondTyp(){
		return comBondTyp;
	}
	/**
 	 * @设置 comBondTyp
 	 * @param comBondTyp
 	 */
	public void setComBondTyp(String comBondTyp){
		this.comBondTyp = comBondTyp;
	}
	/**
 	 * @return 返回 comBondTrm
 	 */
	public double getComBondTrm(){
		return comBondTrm;
	}
	/**
 	 * @设置 comBondTrm
 	 * @param comBondTrm
 	 */
	public void setComBondTrm(double comBondTrm){
		this.comBondTrm = comBondTrm;
	}
}