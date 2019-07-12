package com.yucheng.cmis.biz01line.cus.cuscom.domain;
/**
 * Title: CusComAptitude.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat May 09 15:05:20 CST 2009
 * @version：1.0
 */
public class CusComAptitude  implements com.yucheng.cmis.pub.CMISDomain{
	private String cusId;
	private String comAptTyp;
	private String comAptName;
	private String comAptCode;
	private String comAptDec;
	private String comAptCls;
	private String regBchId;
	private String crtDate;
	private String comAptExpired;
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
 	 * @return 返回 comAptTyp
 	 */
	public String getComAptTyp(){
		return comAptTyp;
	}
	/**
 	 * @设置 comAptTyp
 	 * @param comAptTyp
 	 */
	public void setComAptTyp(String comAptTyp){
		this.comAptTyp = comAptTyp;
	}
	/**
 	 * @return 返回 comAptName
 	 */
	public String getComAptName(){
		return comAptName;
	}
	/**
 	 * @设置 comAptName
 	 * @param comAptName
 	 */
	public void setComAptName(String comAptName){
		this.comAptName = comAptName;
	}
	/**
 	 * @return 返回 comAptCode
 	 */
	public String getComAptCode(){
		return comAptCode;
	}
	/**
 	 * @设置 comAptCode
 	 * @param comAptCode
 	 */
	public void setComAptCode(String comAptCode){
		this.comAptCode = comAptCode;
	}
	/**
 	 * @return 返回 comAptDec
 	 */
	public String getComAptDec(){
		return comAptDec;
	}
	/**
 	 * @设置 comAptDec
 	 * @param comAptDec
 	 */
	public void setComAptDec(String comAptDec){
		this.comAptDec = comAptDec;
	}
	/**
 	 * @return 返回 comAptCls
 	 */
	public String getComAptCls(){
		return comAptCls;
	}
	/**
 	 * @设置 comAptCls
 	 * @param comAptCls
 	 */
	public void setComAptCls(String comAptCls){
		this.comAptCls = comAptCls;
	}
	/**
 	 * @return 返回 regBchId
 	 */
	public String getRegBchId(){
		return regBchId;
	}
	/**
 	 * @设置 regBchId
 	 * @param regBchId
 	 */
	public void setRegBchId(String regBchId){
		this.regBchId = regBchId;
	}
	/**
 	 * @return 返回 crtDate
 	 */
	public String getCrtDate(){
		return crtDate;
	}
	/**
 	 * @设置 crtDate
 	 * @param crtDate
 	 */
	public void setCrtDate(String crtDate){
		this.crtDate = crtDate;
	}
	/**
 	 * @return 返回 comAptExpired
 	 */
	public String getComAptExpired(){
		return comAptExpired;
	}
	/**
 	 * @设置 comAptExpired
 	 * @param comAptExpired
 	 */
	public void setComAptExpired(String comAptExpired){
		this.comAptExpired = comAptExpired;
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