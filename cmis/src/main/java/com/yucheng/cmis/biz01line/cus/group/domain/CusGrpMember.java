package com.yucheng.cmis.biz01line.cus.group.domain;
/**
 * Title: CusGrpMember.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Tue May 26 17:43:19 CST 2009
 * @version：1.0
 */
public class CusGrpMember  implements com.yucheng.cmis.pub.CMISDomain{
	private String grpNo;
	private String cusId;
	private String grpCorreType;
	private String grpCorreDetail;
	private String inputId;
	private String inputDate;
	private String inputBrId;
	private String genType;
	public String getGenType() {
		return genType;
	}
	public void setGenType(String genType) {
		this.genType = genType;
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
 	 * @return 返回 grpNo
 	 */
	public String getGrpNo(){
		return grpNo;
	}
	/**
 	 * @设置 grpNo
 	 * @param grpNo
 	 */
	public void setGrpNo(String grpNo){
		this.grpNo = grpNo;
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
 	 * @return 返回 grpCorreType
 	 */
	public String getGrpCorreType(){
		return grpCorreType;
	}
	/**
 	 * @设置 grpCorreType
 	 * @param grpCorreType
 	 */
	public void setGrpCorreType(String grpCorreType){
		this.grpCorreType = grpCorreType;
	}
	/**
 	 * @return 返回 grpCorreDetail
 	 */
	public String getGrpCorreDetail(){
		return grpCorreDetail;
	}
	/**
 	 * @设置 grpCorreDetail
 	 * @param grpCorreDetail
 	 */
	public void setGrpCorreDetail(String grpCorreDetail){
		this.grpCorreDetail = grpCorreDetail;
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
	public String getInputId() {
		return inputId;
	}
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}
}