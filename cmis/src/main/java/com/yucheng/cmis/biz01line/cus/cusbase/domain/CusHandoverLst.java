package com.yucheng.cmis.biz01line.cus.cusbase.domain;
/**
 * Title: CusHandoverLst.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Fri Apr 17 10:21:49 CST 2009
 * @version：1.0
 */
public class CusHandoverLst  implements com.yucheng.cmis.pub.CMISDomain{
	private String businessCode;
	private String businessDetail;
	private String serno;
	private String handoverType;
	private String cusId;
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
 	 * @return 返回 businessCode
 	 */
	public String getBusinessCode(){
		return businessCode;
	}
	/**
 	 * @设置 businessCode
 	 * @param businessCode
 	 */
	public void setBusinessCode(String businessCode){
		this.businessCode = businessCode;
	}
	/**
 	 * @return 返回 businessDetail
 	 */
	public String getBusinessDetail(){
		return businessDetail;
	}
	/**
 	 * @设置 businessDetail
 	 * @param businessDetail
 	 */
	public void setBusinessDetail(String businessDetail){
		this.businessDetail = businessDetail;
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
 	 * @return 返回 handoverType
 	 */
	public String getHandoverType(){
		return handoverType;
	}
	/**
 	 * @设置 handoverType
 	 * @param handoverType
 	 */
	public void setHandoverType(String handoverType){
		this.handoverType = handoverType;
	}
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
}