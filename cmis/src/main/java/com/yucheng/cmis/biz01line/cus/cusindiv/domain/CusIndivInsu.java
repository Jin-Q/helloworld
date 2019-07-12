package com.yucheng.cmis.biz01line.cus.cusindiv.domain;
/**
 * Title: CusIndivInsu.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat May 09 15:01:55 CST 2009
 * @version：1.0
 */
public class CusIndivInsu  implements com.yucheng.cmis.pub.CMISDomain{
	private String cusId;
	private String indivInsId;
	private String indivInsCvg;
	private String indivInsCom;
	private String indivInsTyp;
	private String policyholders;
	private String beneficiaries;
	private String indivInsSub;
	private double indivInsVal;
	private String indivInsUnd;
	private double indivInsFee;
	private double indivInsTotAmt;
	private String indivInsStrDt;
	private String indivInsEndDt;
	private double indivInsAmt;
	private String indivInsStatus;
	private String remark;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String lastUpdId;
	private String lastUpdDate;
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
 	 * @return 返回 indivInsId
 	 */
	public String getIndivInsId(){
		return indivInsId;
	}
	/**
 	 * @设置 indivInsId
 	 * @param indivInsId
 	 */
	public void setIndivInsId(String indivInsId){
		this.indivInsId = indivInsId;
	}
	/**
 	 * @return 返回 indivInsCvg
 	 */
	public String getIndivInsCvg(){
		return indivInsCvg;
	}
	/**
 	 * @设置 indivInsCvg
 	 * @param indivInsCvg
 	 */
	public void setIndivInsCvg(String indivInsCvg){
		this.indivInsCvg = indivInsCvg;
	}
	/**
 	 * @return 返回 indivInsCom
 	 */
	public String getIndivInsCom(){
		return indivInsCom;
	}
	/**
 	 * @设置 indivInsCom
 	 * @param indivInsCom
 	 */
	public void setIndivInsCom(String indivInsCom){
		this.indivInsCom = indivInsCom;
	}
	/**
 	 * @return 返回 indivInsTyp
 	 */
	public String getIndivInsTyp(){
		return indivInsTyp;
	}
	/**
 	 * @设置 indivInsTyp
 	 * @param indivInsTyp
 	 */
	public void setIndivInsTyp(String indivInsTyp){
		this.indivInsTyp = indivInsTyp;
	}
	/**
 	 * @return 返回 policyholders
 	 */
	public String getPolicyholders(){
		return policyholders;
	}
	/**
 	 * @设置 policyholders
 	 * @param policyholders
 	 */
	public void setPolicyholders(String policyholders){
		this.policyholders = policyholders;
	}
	/**
 	 * @return 返回 beneficiaries
 	 */
	public String getBeneficiaries(){
		return beneficiaries;
	}
	/**
 	 * @设置 beneficiaries
 	 * @param beneficiaries
 	 */
	public void setBeneficiaries(String beneficiaries){
		this.beneficiaries = beneficiaries;
	}
	/**
 	 * @return 返回 indivInsSub
 	 */
	public String getIndivInsSub(){
		return indivInsSub;
	}
	/**
 	 * @设置 indivInsSub
 	 * @param indivInsSub
 	 */
	public void setIndivInsSub(String indivInsSub){
		this.indivInsSub = indivInsSub;
	}
	/**
 	 * @return 返回 indivInsVal
 	 */
	public double getIndivInsVal(){
		return indivInsVal;
	}
	/**
 	 * @设置 indivInsVal
 	 * @param indivInsVal
 	 */
	public void setIndivInsVal(double indivInsVal){
		this.indivInsVal = indivInsVal;
	}
	/**
 	 * @return 返回 indivInsUnd
 	 */
	public String getIndivInsUnd(){
		return indivInsUnd;
	}
	/**
 	 * @设置 indivInsUnd
 	 * @param indivInsUnd
 	 */
	public void setIndivInsUnd(String indivInsUnd){
		this.indivInsUnd = indivInsUnd;
	}
	/**
 	 * @return 返回 indivInsFee
 	 */
	public double getIndivInsFee(){
		return indivInsFee;
	}
	/**
 	 * @设置 indivInsFee
 	 * @param indivInsFee
 	 */
	public void setIndivInsFee(double indivInsFee){
		this.indivInsFee = indivInsFee;
	}
	/**
 	 * @return 返回 indivInsTotAmt
 	 */
	public double getIndivInsTotAmt(){
		return indivInsTotAmt;
	}
	/**
 	 * @设置 indivInsTotAmt
 	 * @param indivInsTotAmt
 	 */
	public void setIndivInsTotAmt(double indivInsTotAmt){
		this.indivInsTotAmt = indivInsTotAmt;
	}
	/**
 	 * @return 返回 indivInsStrDt
 	 */
	public String getIndivInsStrDt(){
		return indivInsStrDt;
	}
	/**
 	 * @设置 indivInsStrDt
 	 * @param indivInsStrDt
 	 */
	public void setIndivInsStrDt(String indivInsStrDt){
		this.indivInsStrDt = indivInsStrDt;
	}
	/**
 	 * @return 返回 indivInsEndDt
 	 */
	public String getIndivInsEndDt(){
		return indivInsEndDt;
	}
	/**
 	 * @设置 indivInsEndDt
 	 * @param indivInsEndDt
 	 */
	public void setIndivInsEndDt(String indivInsEndDt){
		this.indivInsEndDt = indivInsEndDt;
	}
	/**
 	 * @return 返回 indivInsAmt
 	 */
	public double getIndivInsAmt(){
		return indivInsAmt;
	}
	/**
 	 * @设置 indivInsAmt
 	 * @param indivInsAmt
 	 */
	public void setIndivInsAmt(double indivInsAmt){
		this.indivInsAmt = indivInsAmt;
	}
	/**
 	 * @return 返回 indivInsStatus
 	 */
	public String getIndivInsStatus(){
		return indivInsStatus;
	}
	/**
 	 * @设置 indivInsStatus
 	 * @param indivInsStatus
 	 */
	public void setIndivInsStatus(String indivInsStatus){
		this.indivInsStatus = indivInsStatus;
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