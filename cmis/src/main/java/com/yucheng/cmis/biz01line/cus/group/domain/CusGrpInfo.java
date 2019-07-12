package com.yucheng.cmis.biz01line.cus.group.domain;
/**
 * Title: CusGrpInfo.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Tue May 26 17:43:19 CST 2009
 * @version：1.0
 */
public class CusGrpInfo  implements com.yucheng.cmis.pub.CMISDomain{
	private String grpNo;
	private String grpName;
	private String parentCusId;
	private String grpFinanceType;
	private String grpDetail;
	private String managerBrId;
	private String managerId;
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
	public String getInputBrId() {
		return inputBrId;
	}
	public void setInputBrId(String inputBrId) {
		this.inputBrId = inputBrId;
	}
	public String getManagerBrId() {
		return managerBrId;
	}
	public void setManagerBrId(String managerBrId) {
		this.managerBrId = managerBrId;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getInputId() {
		return inputId;
	}
	public void setInputId(String inputId) {
		this.inputId = inputId;
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
 	 * @return 返回 grpName
 	 */
	public String getGrpName(){
		return grpName;
	}
	/**
 	 * @设置 grpName
 	 * @param grpName
 	 */
	public void setGrpName(String grpName){
		this.grpName = grpName;
	}
	/**
 	 * @return 返回 parentCusId
 	 */
	public String getParentCusId(){
		return parentCusId;
	}
	/**
 	 * @设置 parentCusId
 	 * @param parentCusId
 	 */
	public void setParentCusId(String parentCusId){
		this.parentCusId = parentCusId;
	}
	/**
 	 * @return 返回 grpFinanceType
 	 */
	public String getGrpFinanceType(){
		return grpFinanceType;
	}
	/**
 	 * @设置 grpFinanceType
 	 * @param grpFinanceType
 	 */
	public void setGrpFinanceType(String grpFinanceType){
		this.grpFinanceType = grpFinanceType;
	}
	/**
 	 * @return 返回 grpDetail
 	 */
	public String getGrpDetail(){
		return grpDetail;
	}
	/**
 	 * @设置 grpDetail
 	 * @param grpDetail
 	 */
	public void setGrpDetail(String grpDetail){
		this.grpDetail = grpDetail;
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