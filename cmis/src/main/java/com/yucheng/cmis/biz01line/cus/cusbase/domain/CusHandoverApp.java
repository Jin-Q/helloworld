package com.yucheng.cmis.biz01line.cus.cusbase.domain;
/**
 * Title: CusHandoverApp.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Fri Apr 17 10:21:49 CST 2009
 * @version：1.0
 */
public class CusHandoverApp  implements com.yucheng.cmis.pub.CMISDomain{
	private String serno;
	private String orgType;
	private String handoverScope;
	private String handoverMode;
	private String areaCode;
	private String areaName;
	private String handoverId;
	private String handoverBrId;
	private String superviseId;
	private String superviseBrId;
	private String receiverId;
	private String receiverBrId;
	private String handoverDetail;
	private String superviseDate;
	private String receiveDate;
	private String approve_status;
	private String inputId;
	private String inputBrId;
	private String inputDate;
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
 	 * @return 返回 orgType
 	 */
	public String getOrgType(){
		return orgType;
	}
	/**
 	 * @设置 orgType
 	 * @param orgType
 	 */
	public void setOrgType(String orgType){
		this.orgType = orgType;
	}
	/**
 	 * @return 返回 handoverScope
 	 */
	public String getHandoverScope(){
		return handoverScope;
	}
	/**
 	 * @设置 handoverScope
 	 * @param handoverScope
 	 */
	public void setHandoverScope(String handoverScope){
		this.handoverScope = handoverScope;
	}
	/**
 	 * @return 返回 handoverMode
 	 */
	public String getHandoverMode(){
		return handoverMode;
	}
	/**
 	 * @设置 handoverMode
 	 * @param handoverMode
 	 */
	public void setHandoverMode(String handoverMode){
		this.handoverMode = handoverMode;
	}
	/**
 	 * @return 返回 areaCode
 	 */
	public String getAreaCode(){
		return areaCode;
	}
	/**
 	 * @设置 areaCode
 	 * @param areaCode
 	 */
	public void setAreaCode(String areaCode){
		this.areaCode = areaCode;
	}
	/**
 	 * @return 返回 areaName
 	 */
	public String getAreaName(){
		return areaName;
	}
	/**
 	 * @设置 areaName
 	 * @param areaName
 	 */
	public void setAreaName(String areaName){
		this.areaName = areaName;
	}
	/**
 	 * @return 返回 handoverId
 	 */
	public String getHandoverId(){
		return handoverId;
	}
	/**
 	 * @设置 handoverId
 	 * @param handoverId
 	 */
	public void setHandoverId(String handoverId){
		this.handoverId = handoverId;
	}
	/**
 	 * @return 返回 handoverBrId
 	 */
	public String getHandoverBrId(){
		return handoverBrId;
	}
	/**
 	 * @设置 handoverBrId
 	 * @param handoverBrId
 	 */
	public void setHandoverBrId(String handoverBrId){
		this.handoverBrId = handoverBrId;
	}
	/**
 	 * @return 返回 superviseId
 	 */
	public String getSuperviseId(){
		return superviseId;
	}
	/**
 	 * @设置 superviseId
 	 * @param superviseId
 	 */
	public void setSuperviseId(String superviseId){
		this.superviseId = superviseId;
	}
	/**
 	 * @return 返回 superviseBrId
 	 */
	public String getSuperviseBrId(){
		return superviseBrId;
	}
	/**
 	 * @设置 superviseBrId
 	 * @param superviseBrId
 	 */
	public void setSuperviseBrId(String superviseBrId){
		this.superviseBrId = superviseBrId;
	}
	/**
 	 * @return 返回 receiverId
 	 */
	public String getReceiverId(){
		return receiverId;
	}
	/**
 	 * @设置 receiverId
 	 * @param receiverId
 	 */
	public void setReceiverId(String receiverId){
		this.receiverId = receiverId;
	}
	/**
 	 * @return 返回 receiverBrId
 	 */
	public String getReceiverBrId(){
		return receiverBrId;
	}
	/**
 	 * @设置 receiverBrId
 	 * @param receiverBrId
 	 */
	public void setReceiverBrId(String receiverBrId){
		this.receiverBrId = receiverBrId;
	}
	/**
 	 * @return 返回 handoverDetail
 	 */
	public String getHandoverDetail(){
		return handoverDetail;
	}
	/**
 	 * @设置 handoverDetail
 	 * @param handoverDetail
 	 */
	public void setHandoverDetail(String handoverDetail){
		this.handoverDetail = handoverDetail;
	}
	/**
 	 * @return 返回 superviseDate
 	 */
	public String getSuperviseDate(){
		return superviseDate;
	}
	/**
 	 * @设置 superviseDate
 	 * @param superviseDate
 	 */
	public void setSuperviseDate(String superviseDate){
		this.superviseDate = superviseDate;
	}
	/**
 	 * @return 返回 receiveDate
 	 */
	public String getReceiveDate(){
		return receiveDate;
	}
	/**
 	 * @设置 receiveDate
 	 * @param receiveDate
 	 */
	public void setReceiveDate(String receiveDate){
		this.receiveDate = receiveDate;
	}
	
	public String getApprove_status() {
		return approve_status;
	}
	public void setApprove_status(String approve_status) {
		this.approve_status = approve_status;
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
}