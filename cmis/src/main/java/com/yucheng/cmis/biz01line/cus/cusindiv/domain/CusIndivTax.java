package com.yucheng.cmis.biz01line.cus.cusindiv.domain;
/**
 * Title: CusIndivTax.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat May 09 15:01:55 CST 2009
 * @version：1.0
 */
public class CusIndivTax  implements com.yucheng.cmis.pub.CMISDomain{
	private String serno;
	private String cusId;
	private String indivTaxes;
	private double indivTaxAmt;
	private double indivTaxPayAmt;
	private String indivTaxDt;
	private String indivTaxFlg;
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
 	 * @return 返回 indivTaxes
 	 */
	public String getIndivTaxes(){
		return indivTaxes;
	}
	/**
 	 * @设置 indivTaxes
 	 * @param indivTaxes
 	 */
	public void setIndivTaxes(String indivTaxes){
		this.indivTaxes = indivTaxes;
	}
	/**
 	 * @return 返回 indivTaxAmt
 	 */
	public double getIndivTaxAmt(){
		return indivTaxAmt;
	}
	/**
 	 * @设置 indivTaxAmt
 	 * @param indivTaxAmt
 	 */
	public void setIndivTaxAmt(double indivTaxAmt){
		this.indivTaxAmt = indivTaxAmt;
	}
	/**
 	 * @return 返回 indivTaxPayAmt
 	 */
	public double getIndivTaxPayAmt(){
		return indivTaxPayAmt;
	}
	/**
 	 * @设置 indivTaxPayAmt
 	 * @param indivTaxPayAmt
 	 */
	public void setIndivTaxPayAmt(double indivTaxPayAmt){
		this.indivTaxPayAmt = indivTaxPayAmt;
	}
	/**
 	 * @return 返回 indivTaxDt
 	 */
	public String getIndivTaxDt(){
		return indivTaxDt;
	}
	/**
 	 * @设置 indivTaxDt
 	 * @param indivTaxDt
 	 */
	public void setIndivTaxDt(String indivTaxDt){
		this.indivTaxDt = indivTaxDt;
	}
	/**
 	 * @return 返回 indivTaxFlg
 	 */
	public String getIndivTaxFlg(){
		return indivTaxFlg;
	}
	/**
 	 * @设置 indivTaxFlg
 	 * @param indivTaxFlg
 	 */
	public void setIndivTaxFlg(String indivTaxFlg){
		this.indivTaxFlg = indivTaxFlg;
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