package com.yucheng.cmis.biz01line.cus.cusindiv.domain;
/**
 * Title: CusIndivFamLby.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat May 09 15:01:55 CST 2009
 * @version：1.0
 */
public class CusIndivFamLby  implements com.yucheng.cmis.pub.CMISDomain{
	private String cusId;
	private String indivDebtId;
	private String indivDebtTyp;
	private String indivDebtDesc;
	private String indivCreditor;
	private String indivDebtCur;
	private double indivDebtAmt;
	private String indivDebtStrDt;
	private String indivDebtEndDt;
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
 	 * @return 返回 indivDebtId
 	 */
	public String getIndivDebtId(){
		return indivDebtId;
	}
	/**
 	 * @设置 indivDebtId
 	 * @param indivDebtId
 	 */
	public void setIndivDebtId(String indivDebtId){
		this.indivDebtId = indivDebtId;
	}
	/**
 	 * @return 返回 indivDebtTyp
 	 */
	public String getIndivDebtTyp(){
		return indivDebtTyp;
	}
	/**
 	 * @设置 indivDebtTyp
 	 * @param indivDebtTyp
 	 */
	public void setIndivDebtTyp(String indivDebtTyp){
		this.indivDebtTyp = indivDebtTyp;
	}
	/**
 	 * @return 返回 indivDebtDesc
 	 */
	public String getIndivDebtDesc(){
		return indivDebtDesc;
	}
	/**
 	 * @设置 indivDebtDesc
 	 * @param indivDebtDesc
 	 */
	public void setIndivDebtDesc(String indivDebtDesc){
		this.indivDebtDesc = indivDebtDesc;
	}
	/**
 	 * @return 返回 indivCreditor
 	 */
	public String getIndivCreditor(){
		return indivCreditor;
	}
	/**
 	 * @设置 indivCreditor
 	 * @param indivCreditor
 	 */
	public void setIndivCreditor(String indivCreditor){
		this.indivCreditor = indivCreditor;
	}
	/**
 	 * @return 返回 indivDebtCur
 	 */
	public String getIndivDebtCur(){
		return indivDebtCur;
	}
	/**
 	 * @设置 indivDebtCur
 	 * @param indivDebtCur
 	 */
	public void setIndivDebtCur(String indivDebtCur){
		this.indivDebtCur = indivDebtCur;
	}
	/**
 	 * @return 返回 indivDebtAmt
 	 */
	public double getIndivDebtAmt(){
		return indivDebtAmt;
	}
	/**
 	 * @设置 indivDebtAmt
 	 * @param indivDebtAmt
 	 */
	public void setIndivDebtAmt(double indivDebtAmt){
		this.indivDebtAmt = indivDebtAmt;
	}
	/**
 	 * @return 返回 indivDebtStrDt
 	 */
	public String getIndivDebtStrDt(){
		return indivDebtStrDt;
	}
	/**
 	 * @设置 indivDebtStrDt
 	 * @param indivDebtStrDt
 	 */
	public void setIndivDebtStrDt(String indivDebtStrDt){
		this.indivDebtStrDt = indivDebtStrDt;
	}
	/**
 	 * @return 返回 indivDebtEndDt
 	 */
	public String getIndivDebtEndDt(){
		return indivDebtEndDt;
	}
	/**
 	 * @设置 indivDebtEndDt
 	 * @param indivDebtEndDt
 	 */
	public void setIndivDebtEndDt(String indivDebtEndDt){
		this.indivDebtEndDt = indivDebtEndDt;
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