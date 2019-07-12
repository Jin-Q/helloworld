package com.yucheng.cmis.biz01line.cus.cuscom.domain;
/**
 * Title: CusComFinaStock.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat May 09 15:05:20 CST 2009
 * @version：1.0
 */
public class CusComFinaStock  implements com.yucheng.cmis.pub.CMISDomain{
	private String cusId;
	private String comStkCode;
	private String comStkName;
	private String comStkMrkDt;
	private String comStkMrkPlace;
	private String comStkMrkBrs;
	private double comStkInitAmr;
	private double comStkCurAmt;
	private double comStkEvaAmt;
	private String comStkCapQnt;
	private String comStkCurQnt;
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
 	 * @return 返回 comStkCode
 	 */
	public String getComStkCode(){
		return comStkCode;
	}
	/**
 	 * @设置 comStkCode
 	 * @param comStkCode
 	 */
	public void setComStkCode(String comStkCode){
		this.comStkCode = comStkCode;
	}
	/**
 	 * @return 返回 comStkName
 	 */
	public String getComStkName(){
		return comStkName;
	}
	/**
 	 * @设置 comStkName
 	 * @param comStkName
 	 */
	public void setComStkName(String comStkName){
		this.comStkName = comStkName;
	}
	/**
 	 * @return 返回 comStkMrkDt
 	 */
	public String getComStkMrkDt(){
		return comStkMrkDt;
	}
	/**
 	 * @设置 comStkMrkDt
 	 * @param comStkMrkDt
 	 */
	public void setComStkMrkDt(String comStkMrkDt){
		this.comStkMrkDt = comStkMrkDt;
	}
	/**
 	 * @return 返回 comStkMrkPlace
 	 */
	public String getComStkMrkPlace(){
		return comStkMrkPlace;
	}
	/**
 	 * @设置 comStkMrkPlace
 	 * @param comStkMrkPlace
 	 */
	public void setComStkMrkPlace(String comStkMrkPlace){
		this.comStkMrkPlace = comStkMrkPlace;
	}
	/**
 	 * @return 返回 comStkMrkBrs
 	 */
	public String getComStkMrkBrs(){
		return comStkMrkBrs;
	}
	/**
 	 * @设置 comStkMrkBrs
 	 * @param comStkMrkBrs
 	 */
	public void setComStkMrkBrs(String comStkMrkBrs){
		this.comStkMrkBrs = comStkMrkBrs;
	}
	/**
 	 * @return 返回 comStkInitAmr
 	 */
	public double getComStkInitAmr(){
		return comStkInitAmr;
	}
	/**
 	 * @设置 comStkInitAmr
 	 * @param comStkInitAmr
 	 */
	public void setComStkInitAmr(double comStkInitAmr){
		this.comStkInitAmr = comStkInitAmr;
	}
	/**
 	 * @return 返回 comStkCurAmt
 	 */
	public double getComStkCurAmt(){
		return comStkCurAmt;
	}
	/**
 	 * @设置 comStkCurAmt
 	 * @param comStkCurAmt
 	 */
	public void setComStkCurAmt(double comStkCurAmt){
		this.comStkCurAmt = comStkCurAmt;
	}
	/**
 	 * @return 返回 comStkEvaAmt
 	 */
	public double getComStkEvaAmt(){
		return comStkEvaAmt;
	}
	/**
 	 * @设置 comStkEvaAmt
 	 * @param comStkEvaAmt
 	 */
	public void setComStkEvaAmt(double comStkEvaAmt){
		this.comStkEvaAmt = comStkEvaAmt;
	}
	/**
 	 * @return 返回 comStkCapQnt
 	 */
	public String getComStkCapQnt(){
		return comStkCapQnt;
	}
	/**
 	 * @设置 comStkCapQnt
 	 * @param comStkCapQnt
 	 */
	public void setComStkCapQnt(String comStkCapQnt){
		this.comStkCapQnt = comStkCapQnt;
	}
	/**
 	 * @return 返回 comStkCurQnt
 	 */
	public String getComStkCurQnt(){
		return comStkCurQnt;
	}
	/**
 	 * @设置 comStkCurQnt
 	 * @param comStkCurQnt
 	 */
	public void setComStkCurQnt(String comStkCurQnt){
		this.comStkCurQnt = comStkCurQnt;
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