package com.yucheng.cmis.biz01line.cus.cusbase.domain;
/**
 * Title: CusEvent.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat Jun 27 10:42:09 CST 2009
 * @version：1.0
 */
public class CusEvent  implements com.yucheng.cmis.pub.CMISDomain{
	private String serno;
	private String cusId;
	private String eventDt;
	private String eventName;
	private String eventTyp;
	private String eventImpDeg;
	private String eventDesc;
	private double eventAmt;
	private String eventExpsCom;
	private String eventBankFlg;
	private String eventBchName;
	private String logoutDate;
	private String logoutReason;
	private String status;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String logoutId;
	private String logoutBrId;
	private String lastUpdId;
	private String lastUpdDate;
	private String eventClassify;
	public String getEventClassify() {
		return eventClassify;
	}
	public void setEventClassify(String eventClassify) {
		this.eventClassify = eventClassify;
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
 	 * @return 返回 serno
 	 */
	public String getSerno(){
		return serno;
	}
	/**
 	 * @设置 serno
 	 * @param serno
 	 */
	public void setSerno(String serno){
		this.serno = serno;
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
 	 * @return 返回 eventDt
 	 */
	public String getEventDt(){
		return eventDt;
	}
	/**
 	 * @设置 eventDt
 	 * @param eventDt
 	 */
	public void setEventDt(String eventDt){
		this.eventDt = eventDt;
	}
	/**
 	 * @return 返回 eventName
 	 */
	public String getEventName(){
		return eventName;
	}
	/**
 	 * @设置 eventName
 	 * @param eventName
 	 */
	public void setEventName(String eventName){
		this.eventName = eventName;
	}
	/**
 	 * @return 返回 eventTyp
 	 */
	public String getEventTyp(){
		return eventTyp;
	}
	/**
 	 * @设置 eventTyp
 	 * @param eventTyp
 	 */
	public void setEventTyp(String eventTyp){
		this.eventTyp = eventTyp;
	}
	/**
 	 * @return 返回 eventImpDeg
 	 */
	public String getEventImpDeg(){
		return eventImpDeg;
	}
	/**
 	 * @设置 eventImpDeg
 	 * @param eventImpDeg
 	 */
	public void setEventImpDeg(String eventImpDeg){
		this.eventImpDeg = eventImpDeg;
	}
	/**
 	 * @return 返回 eventDesc
 	 */
	public String getEventDesc(){
		return eventDesc;
	}
	/**
 	 * @设置 eventDesc
 	 * @param eventDesc
 	 */
	public void setEventDesc(String eventDesc){
		this.eventDesc = eventDesc;
	}
	/**
 	 * @return 返回 eventAmt
 	 */
	public double getEventAmt(){
		return eventAmt;
	}
	/**
 	 * @设置 eventAmt
 	 * @param eventAmt
 	 */
	public void setEventAmt(double eventAmt){
		this.eventAmt = eventAmt;
	}
	/**
 	 * @return 返回 eventExpsCom
 	 */
	public String getEventExpsCom(){
		return eventExpsCom;
	}
	/**
 	 * @设置 eventExpsCom
 	 * @param eventExpsCom
 	 */
	public void setEventExpsCom(String eventExpsCom){
		this.eventExpsCom = eventExpsCom;
	}
	/**
 	 * @return 返回 eventBankFlg
 	 */
	public String getEventBankFlg(){
		return eventBankFlg;
	}
	/**
 	 * @设置 eventBankFlg
 	 * @param eventBankFlg
 	 */
	public void setEventBankFlg(String eventBankFlg){
		this.eventBankFlg = eventBankFlg;
	}
	/**
 	 * @return 返回 eventBchName
 	 */
	public String getEventBchName(){
		return eventBchName;
	}
	/**
 	 * @设置 eventBchName
 	 * @param eventBchName
 	 */
	public void setEventBchName(String eventBchName){
		this.eventBchName = eventBchName;
	}
	/**
 	 * @return 返回 logoutDate
 	 */
	public String getLogoutDate(){
		return logoutDate;
	}
	/**
 	 * @设置 logoutDate
 	 * @param logoutDate
 	 */
	public void setLogoutDate(String logoutDate){
		this.logoutDate = logoutDate;
	}
	/**
 	 * @return 返回 logoutReason
 	 */
	public String getLogoutReason(){
		return logoutReason;
	}
	/**
 	 * @设置 logoutReason
 	 * @param logoutReason
 	 */
	public void setLogoutReason(String logoutReason){
		this.logoutReason = logoutReason;
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
 	 * @return 返回 logoutId
 	 */
	public String getLogoutId(){
		return logoutId;
	}
	/**
 	 * @设置 logoutId
 	 * @param logoutId
 	 */
	public void setLogoutId(String logoutId){
		this.logoutId = logoutId;
	}
	/**
 	 * @return 返回 logoutBrId
 	 */
	public String getLogoutBrId(){
		return logoutBrId;
	}
	/**
 	 * @设置 logoutBrId
 	 * @param logoutBrId
 	 */
	public void setLogoutBrId(String logoutBrId){
		this.logoutBrId = logoutBrId;
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