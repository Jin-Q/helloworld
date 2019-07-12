package com.yucheng.cmis.biz01line.cus.cuscom.domain;


/**
 * Title: CusComRelApital.java
 * Description:
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat May 09 15:05:20 CST 2009
 * @version：1.0
 */
public class CusComRelApital implements com.yucheng.cmis.pub.CMISDomain{
	private String cusId;
	private String invtTyp;
	private String regCode;
	private String curType;
	private double invtAmt;
	private double invtPerc;
	private String comInvtDesc;
	private String invDate;
	private String remark;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String lastUpdId;
	private String lastUpdDate;
	private String status;
	private String cusStatus;
	private String cusIdRel;
	private String invtType;
	private String relaType;
	
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
	
	
	public String getCusIdRel() {
		return cusIdRel;
	}


	public void setCusIdRel(String cusIdRel) {
		this.cusIdRel = cusIdRel;
	}


	/**
 	 * @return 返回 status
 	 */
	public String getStatus(){
		return status;
	}
	/**
 	 * @设置 status
 	 * @param status
 	 */
	public void setStatus(String status){
		this.status = status;
	}
	/**
 	 * @return 返回 cusStatus
 	 */
	public String getCusStatus(){
		return cusStatus;
	}
	/**
 	 * @设置 cusStatus
 	 * @param cusStatus
 	 */
	public void setCusStatus(String cusStatus){
		this.cusStatus = cusStatus;
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
 	 * @return 返回 invtTyp
 	 */
	public String getInvtTyp(){
		return invtTyp;
	}
	/**
 	 * @设置 invtTyp
 	 * @param invtTyp
 	 */
	public void setInvtTyp(String invtTyp){
		this.invtTyp = invtTyp;
	}
	/**
 	 * @return 返回 regCode
 	 */
	public String getRegCode(){
		return regCode;
	}
	/**
 	 * @设置 regCode
 	 * @param regCode
 	 */
	public void setRegCode(String regCode){
		this.regCode = regCode;
	}
	/**
 	 * @return 返回 curType
 	 */
	public String getCurType(){
		return curType;
	}
	/**
 	 * @设置 curType
 	 * @param curType
 	 */
	public void setCurType(String curType){
		this.curType = curType;
	}
	/**
 	 * @return 返回 invtAmt
 	 */
	public double getInvtAmt(){
		return invtAmt;
	}
	/**
 	 * @设置 invtAmt
 	 * @param invtAmt
 	 */
	public void setInvtAmt(double invtAmt){
		this.invtAmt = invtAmt;
	}

	/**
 	 * @return 返回 invtPerc
 	 */
	public double getInvtPerc(){
		return invtPerc;
	}

	/**
         * @设置 invtPerc
         * @param invtPerc
         */
	public void setInvtPerc(double invtPerc){
		this.invtPerc = invtPerc;
	}
	/**
 	 * @return 返回 comInvtDesc
 	 */
	public String getComInvtDesc(){
		return comInvtDesc;
	}
	/**
 	 * @设置 comInvtDesc
 	 * @param comInvtDesc
 	 */
	public void setComInvtDesc(String comInvtDesc){
		this.comInvtDesc = comInvtDesc;
	}
	/**
 	 * @return 返回 invDate
 	 */
	public String getInvDate(){
		return invDate;
	}
	/**
 	 * @设置 invDate
 	 * @param invDate
 	 */
	public void setInvDate(String invDate){
		this.invDate = invDate;
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
	 * @return 返回invtType
	 */
	public String getInvtType() {
		return invtType;
	}

	/**
	 * @设置invtType
	 * @param invtType
	 */
	public void setInvtType(String invtType) {
		this.invtType = invtType;
	}


	public String getRelaType() {
		return relaType;
	}


	public void setRelaType(String relaType) {
		this.relaType = relaType;
	}
}