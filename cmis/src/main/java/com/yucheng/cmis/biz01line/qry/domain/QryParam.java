package com.yucheng.cmis.biz01line.qry.domain;
/**
 * Title: QryParam.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Fri Jul 03 15:09:35 CST 2009
 * @version：1.0
 */
public class QryParam  implements com.yucheng.cmis.pub.CMISDomain{
	private String tempNo;
	private String paramNo;
	private String cnname;
	private String enname;
	private String paramType;
	private String paramDicNo;
	private String orderid;
	/**
	 * opttype和qerySql,popname是param_dict中的字段,为了方便,一起查出来使用本domain保存
	 */
	private String opttype;//标签名称
	private String querySql;//字典查询sql语句
	private String popname;//查询用pop框查询的名字.
	
	public String getPopname() {
		return popname;
	}
	public void setPopname(String popname) {
		this.popname = popname.trim();
	}
	public String getOpttype() {
		return opttype;
	}
	public void setOpttype(String opttype) {
		this.opttype = opttype.trim();
	}
	public String getQuerySql() {
		return querySql;
	}
	public void setQuerySql(String querySql) {
		this.querySql = querySql;
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
 	 * @return 返回 paramNo
 	 */
	public String getParamNo(){
		return paramNo;
	}
	/**
 	 * @设置 paramNo
 	 * @param paramNo
 	 */
	public void setParamNo(String paramNo){
		this.paramNo = paramNo;
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
		this.cnname = cnname.trim();
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
		this.enname = enname.trim();
	}
	/**
 	 * @return 返回 paramType
 	 */
	public String getParamType(){
		return paramType;
	}
	/**
 	 * @设置 paramType
 	 * @param paramType
 	 */
	public void setParamType(String paramType){
		this.paramType = paramType;
	}
	/**
 	 * @return 返回 paramDicNo
 	 */
	public String getParamDicNo(){
		return paramDicNo;
	}
	/**
 	 * @设置 paramDicNo
 	 * @param paramDicNo
 	 */
	public void setParamDicNo(String paramDicNo){
		this.paramDicNo = paramDicNo;
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
		this.orderid = orderid.trim();
	}
}