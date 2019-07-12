package com.yucheng.cmis.biz01line.fnc.master.domain;


import com.yucheng.cmis.pub.CMISDomain;


/**
 *@Classname	FncStatSoe.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 3:04:57 PM  
 *@Copyright 	yuchengtech
 *@Author 		Eric
 *@Description：公司客户所有者权益变动信息
 *@Lastmodified 
 *@Author
 */

public class FncStatSoe implements CMISDomain{
   /** 客户代码*/
   private java.lang.String cusId;
   
   /** 报表年 */
   private java.lang.String statYear;
   
   /** 项目编号*/
   private java.lang.String statItemId;
   
   /** 一月数值*/
   private double statInitAmt1;
   
   /** 二月数值*/
   private double statInitAmt2;
   
   /** 三月数值*/
   private double statInitAmt3;
   
   /** 四月数值*/
   private double statInitAmt4;
   
   /** 五月数值*/
   private double statInitAmt5;
   
   /** 六月数值*/
   private double statInitAmt6;
   
   /** 七月数值*/
   private double statInitAmt7;
   
   /** 八月数值*/
   private double statInitAmt8;
   
   /** 九月数值*/
   private double statInitAmt9;
   
   /** 十月数值*/
   private double statInitAmt10;
   
   /** 十一月数值*/
   private double statInitAmt11;
   
   /** 十二月数值*/
   private double statInitAmt12;
   
   /** 一季度数值*/
   private double statInitAmtQ1;
   
   /** 二季度数值*/
   private double statInitAmtQ2;
   
   /** 三季度数值*/
   private double statInitAmtQ3;
   
   /** 四季度数值*/
   private double statInitAmtQ4;
   
   /** 上半年数值 */
   private double statInitAmtY1;
   
   /** 下半年数值*/
   private double statInitAmtY2;
   
   /** 年数值*/
   private double statInitAmtY;

   /**
	   * 报表口径STAT_STYLE
	   * 09-06-27 新增联合主键
	   * 1 本部报表
	   * 2 联合报表
	   * 9 未知
	   */
	 private String statStyle;
	 
	 
   
   
   public String getStatStyle() {
	return statStyle;
}


public void setStatStyle(String statStyle) {
	this.statStyle = statStyle;
}


public java.lang.String getCusId() {
	return cusId;
}


public void setCusId(java.lang.String cusId) {
	this.cusId = cusId;
}


public java.lang.String getStatYear() {
	return statYear;
}


public void setStatYear(java.lang.String statYear) {
	this.statYear = statYear;
}


public java.lang.String getStatItemId() {
	return statItemId;
}


public void setStatItemId(java.lang.String statItemId) {
	this.statItemId = statItemId;
}


public double getStatInitAmt1() {
	return statInitAmt1;
}


public void setStatInitAmt1(double statInitAmt1) {
	this.statInitAmt1 = statInitAmt1;
}


public double getStatInitAmt2() {
	return statInitAmt2;
}


public void setStatInitAmt2(double statInitAmt2) {
	this.statInitAmt2 = statInitAmt2;
}


public double getStatInitAmt3() {
	return statInitAmt3;
}


public void setStatInitAmt3(double statInitAmt3) {
	this.statInitAmt3 = statInitAmt3;
}


public double getStatInitAmt4() {
	return statInitAmt4;
}


public void setStatInitAmt4(double statInitAmt4) {
	this.statInitAmt4 = statInitAmt4;
}


public double getStatInitAmt5() {
	return statInitAmt5;
}


public void setStatInitAmt5(double statInitAmt5) {
	this.statInitAmt5 = statInitAmt5;
}


public double getStatInitAmt6() {
	return statInitAmt6;
}


public void setStatInitAmt6(double statInitAmt6) {
	this.statInitAmt6 = statInitAmt6;
}


public double getStatInitAmt7() {
	return statInitAmt7;
}


public void setStatInitAmt7(double statInitAmt7) {
	this.statInitAmt7 = statInitAmt7;
}


public double getStatInitAmt8() {
	return statInitAmt8;
}


public void setStatInitAmt8(double statInitAmt8) {
	this.statInitAmt8 = statInitAmt8;
}


public double getStatInitAmt9() {
	return statInitAmt9;
}


public void setStatInitAmt9(double statInitAmt9) {
	this.statInitAmt9 = statInitAmt9;
}


public double getStatInitAmt10() {
	return statInitAmt10;
}


public void setStatInitAmt10(double statInitAmt10) {
	this.statInitAmt10 = statInitAmt10;
}


public double getStatInitAmt11() {
	return statInitAmt11;
}


public void setStatInitAmt11(double statInitAmt11) {
	this.statInitAmt11 = statInitAmt11;
}


public double getStatInitAmt12() {
	return statInitAmt12;
}


public void setStatInitAmt12(double statInitAmt12) {
	this.statInitAmt12 = statInitAmt12;
}


public double getStatInitAmtQ1() {
	return statInitAmtQ1;
}


public void setStatInitAmtQ1(double statInitAmtQ1) {
	this.statInitAmtQ1 = statInitAmtQ1;
}


public double getStatInitAmtQ2() {
	return statInitAmtQ2;
}


public void setStatInitAmtQ2(double statInitAmtQ2) {
	this.statInitAmtQ2 = statInitAmtQ2;
}


public double getStatInitAmtQ3() {
	return statInitAmtQ3;
}


public void setStatInitAmtQ3(double statInitAmtQ3) {
	this.statInitAmtQ3 = statInitAmtQ3;
}


public double getStatInitAmtQ4() {
	return statInitAmtQ4;
}


public void setStatInitAmtQ4(double statInitAmtQ4) {
	this.statInitAmtQ4 = statInitAmtQ4;
}


public double getStatInitAmtY1() {
	return statInitAmtY1;
}


public void setStatInitAmtY1(double statInitAmtY1) {
	this.statInitAmtY1 = statInitAmtY1;
}


public double getStatInitAmtY2() {
	return statInitAmtY2;
}


public void setStatInitAmtY2(double statInitAmtY2) {
	this.statInitAmtY2 = statInitAmtY2;
}


public double getStatInitAmtY() {
	return statInitAmtY;
}


public void setStatInitAmtY(double statInitAmtY) {
	this.statInitAmtY = statInitAmtY;
}


public/*protected*/ Object clone() throws CloneNotSupportedException { 

	// call父类的clone方法 

	Object result = super.clone(); 



	//TODO: 定制clone数据 

	return result; 

	} 

}