package com.yucheng.cmis.biz01line.cus.cuscom.domain;

/**
 * Title: CusComManager.java Description:
 * 
 * @author：echow heyc@yuchengtech.com
 * @create date：Tue Jan 12 11:09:00 CST 2010
 * @version：1.0
 */
public class CusComManager implements com.yucheng.cmis.pub.CMISDomain {
	private String cusId;
	private String comMrgTyp;
	private String inputId;
	private String inputBrId;
	private String inputDate;
	private String lastUpdId;
	private String lastUpdDate;
	private String signInitDate;
	private String signEndDate;
	private String cusIdRel;
	private String powerOrg;
	private String comRelateType;

	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		Object result = super.clone();
		// TODO: 定制clone数据
		return result;
	}

	/**
	 * @return 返回 cusId
	 */
	public String getCusId() {
		return cusId;
	}

	/**
	 * @设置 cusId
	 * @param cusId
	 */
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	/**
	 * @return 返回 comMrgTyp
	 */
	public String getComMrgTyp() {
		return comMrgTyp;
	}

	/**
	 * @设置 comMrgTyp
	 * @param comMrgTyp
	 */
	public void setComMrgTyp(String comMrgTyp) {
		this.comMrgTyp = comMrgTyp;
	}

	/**
	 * @return 返回 inputId
	 */
	public String getInputId() {
		return inputId;
	}

	/**
	 * @设置 inputId
	 * @param inputId
	 */
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}

	/**
	 * @return 返回 inputBrId
	 */
	public String getInputBrId() {
		return inputBrId;
	}

	/**
	 * @设置 inputBrId
	 * @param inputBrId
	 */
	public void setInputBrId(String inputBrId) {
		this.inputBrId = inputBrId;
	}

	/**
	 * @return 返回 inputDate
	 */
	public String getInputDate() {
		return inputDate;
	}

	/**
	 * @设置 inputDate
	 * @param inputDate
	 */
	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	/**
	 * @return 返回 lastUpdId
	 */
	public String getLastUpdId() {
		return lastUpdId;
	}

	/**
	 * @设置 lastUpdId
	 * @param lastUpdId
	 */
	public void setLastUpdId(String lastUpdId) {
		this.lastUpdId = lastUpdId;
	}

	/**
	 * @return 返回 lastUpdDate
	 */
	public String getLastUpdDate() {
		return lastUpdDate;
	}

	/**
	 * @设置 lastUpdDate
	 * @param lastUpdDate
	 */
	public void setLastUpdDate(String lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	/**
	 * @return 返回 signInitDate
	 */
	public String getSignInitDate() {
		return signInitDate;
	}

	/**
	 * @设置 signInitDate
	 * @param signInitDate
	 */
	public void setSignInitDate(String signInitDate) {
		this.signInitDate = signInitDate;
	}

	/**
	 * @return 返回 signEndDate
	 */
	public String getSignEndDate() {
		return signEndDate;
	}

	/**
	 * @设置 signEndDate
	 * @param signEndDate
	 */
	public void setSignEndDate(String signEndDate) {
		this.signEndDate = signEndDate;
	}

	/**
	 * @return 返回 cusIdRel
	 */
	public String getCusIdRel() {
		return cusIdRel;
	}

	/**
	 * @设置 cusIdRel
	 * @param cusIdRel
	 */
	public void setCusIdRel(String cusIdRel) {
		this.cusIdRel = cusIdRel;
	}

	public String getPowerOrg() {
		return powerOrg;
	}

	public void setPowerOrg(String powerOrg) {
		this.powerOrg = powerOrg;
	}

	public String getComRelateType() {
		return comRelateType;
	}

	public void setComRelateType(String comRelateType) {
		this.comRelateType = comRelateType;
	}
}