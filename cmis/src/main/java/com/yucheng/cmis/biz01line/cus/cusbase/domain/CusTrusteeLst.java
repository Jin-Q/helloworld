package com.yucheng.cmis.biz01line.cus.cusbase.domain;
/**
 * Title: CusTrusteeLst.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Fri Apr 17 10:21:49 CST 2009
 * @version：1.0
 */
public class CusTrusteeLst  implements com.yucheng.cmis.pub.CMISDomain{
	private String serno;
	private String cusId;
	private String cusName;
	private String consignorId;
	private String consignorBrId;
	private String trusteeId;
	private String trusteeBrId;
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
 	 * @return 返回 cusName
 	 */
	public String getCusName(){
		return cusName;
	}
	/**
 	 * @设置 cusName
 	 * @param cusName
 	 */
	public void setCusName(String cusName){
		this.cusName = cusName;
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