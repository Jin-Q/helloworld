package com.yucheng.cmis.biz01line.qry.domain;
/**
 * Title: QryResult.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Fri May 04 18:19:54 CST 2012
 * @version：1.0
 */
public class QryResult  implements com.yucheng.cmis.pub.CMISDomain{
	private String tempNo;
	private String resultNo;
	private String cnname;
	private String enname;
	private String enname2;
	private String resultType;
	private String resultTitle;
	private String orderid;
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
 	 * @return 返回 tempNo
 	 */
	public String getTempNo(){
		return tempNo;
	}
	/**
 	 * @设置 tempNo
 	 * @param tempNo
 	 */
	public void setTempNo(String tempNo){
		this.tempNo = tempNo;
	}
	/**
 	 * @return 返回 resultNo
 	 */
	public String getResultNo(){
		return resultNo;
	}
	/**
 	 * @设置 resultNo
 	 * @param resultNo
 	 */
	public void setResultNo(String resultNo){
		this.resultNo = resultNo;
	}
	/**
 	 * @return 返回 cnname
 	 */
	public String getCnname(){
		return cnname;
	}
	/**
 	 * @设置 cnname
 	 * @param cnname
 	 */
	public void setCnname(String cnname){
		this.cnname = cnname;
	}
	/**
 	 * @return 返回 enname
 	 */
	public String getEnname(){
		return enname;
	}
	/**
 	 * @设置 enname
 	 * @param enname
 	 */
	public void setEnname(String enname){
		this.enname = enname;
	}
	/**
 	 * @return 返回 enname2
 	 */
	public String getEnname2(){
		return enname2;
	}
	/**
 	 * @设置 enname2
 	 * @param enname2
 	 */
	public void setEnname2(String enname2){
		this.enname2 = enname2;
	}
	/**
 	 * @return 返回 resultType
 	 */
	public String getResultType(){
		return resultType;
	}
	/**
 	 * @设置 resultType
 	 * @param resultType
 	 */
	public void setResultType(String resultType){
		this.resultType = resultType;
	}
	/**
 	 * @return 返回 resultTitle
 	 */
	public String getResultTitle(){
		return resultTitle;
	}
	/**
 	 * @设置 resultTitle
 	 * @param resultTitle
 	 */
	public void setResultTitle(String resultTitle){
		this.resultTitle = resultTitle;
	}
	/**
 	 * @return 返回 orderid
 	 */
	public String getOrderid(){
		return orderid;
	}
	/**
 	 * @设置 orderid
 	 * @param orderid
 	 */
	public void setOrderid(String orderid){
		this.orderid = orderid;
	}
}