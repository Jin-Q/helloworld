package com.yucheng.cmis.biz01line.cus.cusbase.domain;
/**
 * Title: CusTrusteeLog.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Fri Apr 17 10:21:49 CST 2009
 * @version：1.0
 */
public class CusTrusteeLog  implements com.yucheng.cmis.pub.CMISDomain{
	private String serno;
	private String orgType;
	private String trusteeScope;
	private String areaCode;
	private String areaName;
	private String consignorId;
	private String consignorBrId;
	private String superviseId;
	private String superviseBrId;
	private String trusteeId;
	private String trusteeBrId;
	private String trusteeDetail;
	private String trusteeDate;
	private String retractDate;
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
 	 * @return 返回 trusteeScope
 	 */
	public String getTrusteeScope(){
		return trusteeScope;
	}
	/**
 	 * @设置 trusteeScope
 	 * @param trusteeScope
 	 */
	public void setTrusteeScope(String trusteeScope){
		this.trusteeScope = trusteeScope;
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
 	 * @return 返回 consignorId
 	 */
	public String getConsignorId(){
		return consignorId;
	}
	/**
 	 * @设置 consignorId
 	 * @param consignorId
 	 */
	public void setConsignorId(String consignorId){
		this.consignorId = consignorId;
	}
	/**
 	 * @return 返回 consignorBrId
 	 */
	public String getConsignorBrId(){
		return consignorBrId;
	}
	/**
 	 * @设置 consignorBrId
 	 * @param consignorBrId
 	 */
	public void setConsignorBrId(String consignorBrId){
		this.consignorBrId = consignorBrId;
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
 	 * @return 返回 trusteeId
 	 */
	public String getTrusteeId(){
		return trusteeId;
	}
	/**
 	 * @设置 trusteeId
 	 * @param trusteeId
 	 */
	public void setTrusteeId(String trusteeId){
		this.trusteeId = trusteeId;
	}
	/**
 	 * @return 返回 trusteeBrId
 	 */
	public String getTrusteeBrId(){
		return trusteeBrId;
	}
	/**
 	 * @设置 trusteeBrId
 	 * @param trusteeBrId
 	 */
	public void setTrusteeBrId(String trusteeBrId){
		this.trusteeBrId = trusteeBrId;
	}
	/**
 	 * @return 返回 trusteeDetail
 	 */
	public String getTrusteeDetail(){
		return trusteeDetail;
	}
	/**
 	 * @设置 trusteeDetail
 	 * @param trusteeDetail
 	 */
	public void setTrusteeDetail(String trusteeDetail){
		this.trusteeDetail = trusteeDetail;
	}
	/**
 	 * @return 返回 trusteeDate
 	 */
	public String getTrusteeDate(){
		return trusteeDate;
	}
	/**
 	 * @设置 trusteeDate
 	 * @param trusteeDate
 	 */
	public void setTrusteeDate(String trusteeDate){
		this.trusteeDate = trusteeDate;
	}
	/**
 	 * @return 返回 retractDate
 	 */
	public String getRetractDate(){
		return retractDate;
	}
	/**
 	 * @设置 retractDate
 	 * @param retractDate
 	 */
	public void setRetractDate(String retractDate){
		this.retractDate = retractDate;
	}
}