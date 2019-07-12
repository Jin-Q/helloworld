package com.yucheng.cmis.biz01line.cus.cusindiv.domain;
/**
 * Title: CusIndivSocRel.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Mon May 18 15:49:06 CST 2009
 * @version：1.0
 */
public class CusIndivSocRel  implements com.yucheng.cmis.pub.CMISDomain{
	private String cusId;
	//姓名
	private String indivRelCusName;
	private String indivRelCertTyp;
	private String indivRlCertCode;
	private String indivSex;
	private String indivCusRel;
	private String indivFamilyFlg;
	private double indivRlYIncm;
	private String indivRelJob;
	private String indivRelComName;
	private String indivRelDuty;
	private String indivRelInd;
	private String indivRelPhn;
	private String remark;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String lastUpdId;
	private String lastUpdDate;
	private String cusIdRel;
	public String getCusIdRel() {
		return cusIdRel;
	}
	public void setCusIdRel(String cusIdRel) {
		this.cusIdRel = cusIdRel;
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
 	 * @return 返回 indivRelCusName
 	 */
	public String getIndivRelCusName(){
		return indivRelCusName;
	}
	/**
 	 * @设置 indivRelCusName
 	 * @param indivRelCusName
 	 */
	public void setIndivRelCusName(String indivRelCusName){
		this.indivRelCusName = indivRelCusName;
	}
	/**
 	 * @return 返回 indivRelCertTyp
 	 */
	public String getIndivRelCertTyp(){
		return indivRelCertTyp;
	}
	/**
 	 * @设置 indivRelCertTyp
 	 * @param indivRelCertTyp
 	 */
	public void setIndivRelCertTyp(String indivRelCertTyp){
		this.indivRelCertTyp = indivRelCertTyp;
	}
	/**
 	 * @return 返回 indivRlCertCode
 	 */
	public String getIndivRlCertCode(){
		return indivRlCertCode;
	}
	/**
 	 * @设置 indivRlCertCode
 	 * @param indivRlCertCode
 	 */
	public void setIndivRlCertCode(String indivRlCertCode){
		this.indivRlCertCode = indivRlCertCode;
	}
	/**
 	 * @return 返回 indivSex
 	 */
	public String getIndivSex(){
		return indivSex;
	}
	/**
 	 * @设置 indivSex
 	 * @param indivSex
 	 */
	public void setIndivSex(String indivSex){
		this.indivSex = indivSex;
	}
	/**
 	 * @return 返回 indivCusRel
 	 */
	public String getIndivCusRel(){
		return indivCusRel;
	}
	/**
 	 * @设置 indivCusRel
 	 * @param indivCusRel
 	 */
	public void setIndivCusRel(String indivCusRel){
		this.indivCusRel = indivCusRel;
	}
	/**
 	 * @return 返回 indivFamilyFlg
 	 */
	public String getIndivFamilyFlg(){
		return indivFamilyFlg;
	}
	/**
 	 * @设置 indivFamilyFlg
 	 * @param indivFamilyFlg
 	 */
	public void setIndivFamilyFlg(String indivFamilyFlg){
		this.indivFamilyFlg = indivFamilyFlg;
	}
	/**
 	 * @return 返回 indivRlYIncm
 	 */
	public double getIndivRlYIncm(){
		return indivRlYIncm;
	}
	/**
 	 * @设置 indivRlYIncm
 	 * @param indivRlYIncm
 	 */
	public void setIndivRlYIncm(double indivRlYIncm){
		this.indivRlYIncm = indivRlYIncm;
	}
	/**
 	 * @return 返回 indivRelJob
 	 */
	public String getIndivRelJob(){
		return indivRelJob;
	}
	/**
 	 * @设置 indivRelJob
 	 * @param indivRelJob
 	 */
	public void setIndivRelJob(String indivRelJob){
		this.indivRelJob = indivRelJob;
	}
	/**
 	 * @return 返回 indivRelComName
 	 */
	public String getIndivRelComName(){
		return indivRelComName;
	}
	/**
 	 * @设置 indivRelComName
 	 * @param indivRelComName
 	 */
	public void setIndivRelComName(String indivRelComName){
		this.indivRelComName = indivRelComName;
	}
	/**
 	 * @return 返回 indivRelDuty
 	 */
	public String getIndivRelDuty(){
		return indivRelDuty;
	}
	/**
 	 * @设置 indivRelDuty
 	 * @param indivRelDuty
 	 */
	public void setIndivRelDuty(String indivRelDuty){
		this.indivRelDuty = indivRelDuty;
	}
	/**
 	 * @return 返回 indivRelInd
 	 */
	public String getIndivRelInd(){
		return indivRelInd;
	}
	/**
 	 * @设置 indivRelInd
 	 * @param indivRelInd
 	 */
	public void setIndivRelInd(String indivRelInd){
		this.indivRelInd = indivRelInd;
	}
	/**
 	 * @return 返回 indivRelPhn
 	 */
	public String getIndivRelPhn(){
		return indivRelPhn;
	}
	/**
 	 * @设置 indivRelPhn
 	 * @param indivRelPhn
 	 */
	public void setIndivRelPhn(String indivRelPhn){
		this.indivRelPhn = indivRelPhn;
	}
	/**
 	 * @return 返回 remark
 	 */
	public String getRemark(){
		return remark;
	}
	/**
 	 * @设置 remark
 	 * @param remark
 	 */
	public void setRemark(String remark){
		this.remark = remark;
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