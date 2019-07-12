package com.yucheng.cmis.biz01line.qry.domain;
/**
 * Title: QryTemplet.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Fri Jul 03 15:09:35 CST 2009
 * @version：1.0
 */
public class QryTemplet  implements com.yucheng.cmis.pub.CMISDomain{
	private String tempNo;
	private String tempName;
	private String organlevel;
	private String templetType;
	private String tempPattern;
	private String classpath;
	private String tempEnable;
	private String querySql;
	private String jspFileName;
	private String orderId;
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
 	 * @return 返回 tempName
 	 */
	public String getTempName(){
		return tempName;
	}
	/**
 	 * @设置 tempName
 	 * @param tempName
 	 */
	public void setTempName(String tempName){
		this.tempName = tempName.trim();
	}
	/**
 	 * @return 返回 organlevel
 	 */
	public String getOrganlevel(){
		return organlevel;
	}
	/**
 	 * @设置 organlevel
 	 * @param organlevel
 	 */
	public void setOrganlevel(String organlevel){
		this.organlevel = organlevel;
	}
	/**
 	 * @return 返回 templetType
 	 */
	public String getTempletType(){
		return templetType;
	}
	/**
 	 * @设置 templetType
 	 * @param templetType
 	 */
	public void setTempletType(String templetType){
		this.templetType = templetType;
	}
	/**
 	 * @return 返回 tempPattern
 	 */
	public String getTempPattern(){
		return tempPattern;
	}
	/**
 	 * @设置 tempPattern
 	 * @param tempPattern
 	 */
	public void setTempPattern(String tempPattern){
		this.tempPattern = tempPattern;
	}
	/**
 	 * @return 返回 classpath
 	 */
	public String getClasspath(){
		return classpath;
	}
	/**
 	 * @设置 classpath
 	 * @param classpath
 	 */
	public void setClasspath(String classpath){
		this.classpath = classpath;
	}
	/**
 	 * @return 返回 tempEnable
 	 */
	public String getTempEnable(){
		return tempEnable;
	}
	/**
 	 * @设置 tempEnable
 	 * @param tempEnable
 	 */
	public void setTempEnable(String tempEnable){
		this.tempEnable = tempEnable;
	}
	/**
 	 * @return 返回 querySql
 	 */
	public String getQuerySql(){
		return querySql;
	}
	/**
 	 * @设置 querySql
 	 * @param querySql
 	 */
	public void setQuerySql(String querySql){
		this.querySql = querySql;
	}
	/**
 	 * @return 返回 jspFileName
 	 */
	public String getJspFileName(){
		return jspFileName;
	}
	/**
 	 * @设置 jspFileName
 	 * @param jspFileName
 	 */
	public void setJspFileName(String jspFileName){
		this.jspFileName = jspFileName;
	}
	/**
 	 * @return 返回 orderId
 	 */
	public String getOrderId(){
		return orderId;
	}
	/**
 	 * @设置 orderId
 	 * @param orderId
 	 */
	public void setOrderId(String orderId){
		this.orderId = orderId;
	}
}