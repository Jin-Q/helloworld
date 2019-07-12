package com.yucheng.cmis.biz01line.fnc.config.domain;


import com.yucheng.cmis.pub.CMISDomain;
/**
 * 用于封装查询数据的items
 * @Classname com.yucheng.cmis.fnc.config.domain.FncConfItems4Query.java
 * @author wqgang
 * @Since 2009-4-21 上午09:42:34 
 * @Copyright yuchengtech
 * @version 1.0
 */
public class FncConfItems4Query extends FncConfItems  implements CMISDomain{
	

	private String itemId;
	
	private String itemName;
	
	private String fncConfTyp;
	
	private String fncNoFlg;
	
	private String remark;
	
	private double data1;
	
	private double data2;
	
	private double data3;
	
	//存放趋势比较值
	private double datadill1;
	//存放行业比较值
	private double datadill2;
	//存放趋势比较分析描述
	private String dec1;
	//存放行业比较分析描述
	private String dec2;
	
	//存放趋势比较结果的描述
	private String datadilldesc1;
	//存放行业比较的结果描述
	private String datadilldesc2;
	
	
	private String range;
	
	private String formula;
	
	private String itemUnit;
	
	/**
	 * 用于处理N年
	 */
	private double []data;
	
	private String []dec;
	
	private double []datadill;
	
	public String getRange() {
		return range;
	}



	public void setRange(String range) {
		this.range = range;
	}


	public String getDatadilldesc1() {
		return datadilldesc1;
	}



	public void setDatadilldesc1(String datadilldesc1) {
		this.datadilldesc1 = datadilldesc1;
	}



	public String getDatadilldesc2() {
		return datadilldesc2;
	}



	public void setDatadilldesc2(String datadilldesc2) {
		this.datadilldesc2 = datadilldesc2;
	}



	public String getFormula() {
		return formula;
	}



	public void setFormula(String formula) {
		this.formula = formula;
	}



	public String getItemUnit() {
		return itemUnit;
	}



	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
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



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
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




	public double[] getData() {
		return data;
	}




	public void setData(double[] data) {
		this.data = data;
	}




	public String[] getDec() {
		return dec;
	}




	public void setDec(String[] dec) {
		this.dec = dec;
	}

	public double[] getDatadill() {
		return datadill;
	}



	public void setDatadill(double[] datadill) {
		this.datadill = datadill;
	}



	public Object clone() throws CloneNotSupportedException { 
		FncConfItems4Query result = new FncConfItems4Query(); 
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
		result.setData(this.data);
		result.setDec(this.dec);
		result.setRange(this.range);
		result.setItemUnit(this.getItemUnit());
		
		result.setDatadill(this.datadill);
		return result; 

		} 


}
