package com.yucheng.cmis.biz01line.fnc.config.domain;


import com.yucheng.cmis.pub.CMISDomain;
  /**
 *@Classname	FncConfItems.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-6 下午03:47:30  
 *@Copyright 	yuchengtech
 *@Author 		Yu
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncConfItems implements CMISDomain{
	

	private String itemId;
	
	private String itemName;
	
	private String fncConfTyp;
	
	private String fncNoFlg;
	
	private String remark;
	
	private String formula;
	
	private String itemUnit;
	
	private double data1;
	
	private double data2;
	
	private double data3;
	
	private double datadill1;

	private double datadill2;
	
	private String dec1;
	
	private String dec2;
	

	
	
	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public double getData3() {
		return data3;
	}

	public void setData3(double data3) {
		this.data3 = data3;
	}

	public double getDatadill1() {
		return datadill1;
	}

	public void setDatadill1(double datadill1) {
		this.datadill1 = datadill1;
	}

	public double getDatadill2() {
		return datadill2;
	}

	public void setDatadill2(double datadill2) {
		this.datadill2 = datadill2;
	}

	public String getDec1() {
		return dec1;
	}

	public void setDec1(String dec1) {
		this.dec1 = dec1;
	}

	public String getDec2() {
		return dec2;
	}

	public void setDec2(String dec2) {
		this.dec2 = dec2;
	}

	public double getData1() {
		return data1;
	}

	public void setData1(double data1) {
		this.data1 = data1;
	}

	public double getData2() {
		return data2;
	}

	public void setData2(double data2) {
		this.data2 = data2;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getFncConfTyp() {
		return fncConfTyp;
	}

	public void setFncConfTyp(String fncConfTyp) {
		this.fncConfTyp = fncConfTyp;
	}

	public String getFncNoFlg() {
		return fncNoFlg;
	}

	public void setFncNoFlg(String fncNoFlg) {
		this.fncNoFlg = fncNoFlg;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Object clone() throws CloneNotSupportedException { 
		FncConfItems result = new FncConfItems(); 
		result.setData1(this.getData1());
		result.setData2(this.getData2());
		result.setData3(this.getData3());
		result.setDec1(this.getDec1());
		result.setDec2(this.getDec2());
		result.setDatadill1(this.getDatadill1());
		result.setDatadill2(this.getDatadill2());
		result.setFncConfTyp(this.getFncConfTyp());
		result.setFncNoFlg(this.getFncNoFlg());
		result.setFormula(this.getFormula());
		result.setItemId(this.getItemId());
		result.setItemName(this.getItemName());
		result.setRemark(this.getRemark());
		result.setItemUnit(this.getItemUnit());
		result.setFormula(this.getFormula());
		return result; 

		} 


}
