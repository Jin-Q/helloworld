package com.yucheng.cmis.biz01line.fnc.master.domain;


import com.yucheng.cmis.pub.CMISDomain;


/**
 *@Classname	FncStatBs.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 3:08:03 PM  
 *@Copyright 	yuchengtech
 *@Author 		Eric
 *@Description：公司客户资产负债项目信息
 *@Lastmodified 
 *@Author
 */

public class FncStatBs implements CMISDomain {
   /** 客户代码*/
	private String cusId;
   
   /** 报表年*/
	private String statYear;
   
   /** 项目编号*/
	private String statItemId;
   
   /** 一月数值*/
	private double statInitAmt1;
   
   /** 一月期末数*/
	private double statEndAmt1;
   
   /** 二月数值*/
	private double statInitAmt2;
   
   /** 二月期末数 */
	private double statEndAmt2;
   
   /** 三月数值*/
	private double statInitAmt3;
   
   /** 三月期末数*/
	private double statEndAmt3;
   
   /** 四月数值*/
	private double statInitAmt4;
   
   /** 四月期末数*/
	private double statEndAmt4;
   
   /** 五月数值 */
	private double statInitAmt5;
   
   /** 五月期末数 */
	private double statEndAmt5;
   
   /** 六月数值 */
	private double statInitAmt6;
   
   /** 六月期末数*/
	private double statEndAmt6;
   
   /** 七月数值*/
	private double statInitAmt7;
   
   /** 七月期末数*/
	private double statEndAmt7;
   
   /** 八月数值*/
	private double statInitAmt8;
   
   /** 八月期末数 */
	private double statEndAmt8;
   
   /** 九月数值*/
	private double statInitAmt9;
   
   /** 九月期末数*/
	private double statEndAmt9;
   
   /** 十月数值 */
	private double statInitAmt10;
   
   /** 十月期末数*/
	private double statEndAmt10;
   
   /** 十一月数值*/
	private double statInitAmt11;
   
   /** 十一月期末数*/
   private double statEndAmt11;
   
   /** 十二月数值*/
   private double statInitAmt12;
   
   /** 十二月期末数*/
   private double statEndAmt12;
   
   /** 一季度数值*/
   private double statInitAmtQ1;
   
   /** 一季度期末数*/
   private double statEndAmtQ1;
   
   /** 二季度数值*/
   private double statInitAmtQ2;
   
   /** 二季度期末数*/
   private double statEndAmtQ2;
   
   /** 三季度数值*/
   private double statInitAmtQ3;
   
   /** 三季度期末数*/
   private double statEndAmtQ3;
   
   /** 四季度数值*/
   private double statInitAmtQ4;
   
   /** 四季度期末数*/
   private double statEndAmtQ4;
   
   /** 上半年数值*/
   private double statInitAmtY1;
   
   /** 上半年期末数*/
   private double statEndAmtY1;
   
   /** 下半年数值 */
   private double statInitAmtY2;
   
   /** 下半年期末数*/
   private double statEndAmtY2;
   
   /** 年数值*/
   private double statInitAmtY;
   
   /** 年期末数*/
   private double statEndAmtY;
   
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





	public String getCusId() {
	return cusId;
}





public void setCusId(String cusId) {
	this.cusId = cusId;
}





public String getStatYear() {
	return statYear;
}





public void setStatYear(String statYear) {
	this.statYear = statYear;
}





public String getStatItemId() {
	return statItemId;
}





public void setStatItemId(String statItemId) {
	this.statItemId = statItemId;
}





public double getStatInitAmt1() {
	return statInitAmt1;
}





public void setStatInitAmt1(double statInitAmt1) {
	this.statInitAmt1 = statInitAmt1;
}





public double getStatEndAmt1() {
	return statEndAmt1;
}





public void setStatEndAmt1(double statEndAmt1) {
	this.statEndAmt1 = statEndAmt1;
}





public double getStatInitAmt2() {
	return statInitAmt2;
}





public void setStatInitAmt2(double statInitAmt2) {
	this.statInitAmt2 = statInitAmt2;
}





public double getStatEndAmt2() {
	return statEndAmt2;
}





public void setStatEndAmt2(double statEndAmt2) {
	this.statEndAmt2 = statEndAmt2;
}





public double getStatInitAmt3() {
	return statInitAmt3;
}





public void setStatInitAmt3(double statInitAmt3) {
	this.statInitAmt3 = statInitAmt3;
}





public double getStatEndAmt3() {
	return statEndAmt3;
}





public void setStatEndAmt3(double statEndAmt3) {
	this.statEndAmt3 = statEndAmt3;
}





public double getStatInitAmt4() {
	return statInitAmt4;
}





public void setStatInitAmt4(double statInitAmt4) {
	this.statInitAmt4 = statInitAmt4;
}





public double getStatEndAmt4() {
	return statEndAmt4;
}





public void setStatEndAmt4(double statEndAmt4) {
	this.statEndAmt4 = statEndAmt4;
}





public double getStatInitAmt5() {
	return statInitAmt5;
}





public void setStatInitAmt5(double statInitAmt5) {
	this.statInitAmt5 = statInitAmt5;
}





public double getStatEndAmt5() {
	return statEndAmt5;
}





public void setStatEndAmt5(double statEndAmt5) {
	this.statEndAmt5 = statEndAmt5;
}





public double getStatInitAmt6() {
	return statInitAmt6;
}





public void setStatInitAmt6(double statInitAmt6) {
	this.statInitAmt6 = statInitAmt6;
}





public double getStatEndAmt6() {
	return statEndAmt6;
}





public void setStatEndAmt6(double statEndAmt6) {
	this.statEndAmt6 = statEndAmt6;
}





public double getStatInitAmt7() {
	return statInitAmt7;
}





public void setStatInitAmt7(double statInitAmt7) {
	this.statInitAmt7 = statInitAmt7;
}





public double getStatEndAmt7() {
	return statEndAmt7;
}





public void setStatEndAmt7(double statEndAmt7) {
	this.statEndAmt7 = statEndAmt7;
}





public double getStatInitAmt8() {
	return statInitAmt8;
}





public void setStatInitAmt8(double statInitAmt8) {
	this.statInitAmt8 = statInitAmt8;
}





public double getStatEndAmt8() {
	return statEndAmt8;
}





public void setStatEndAmt8(double statEndAmt8) {
	this.statEndAmt8 = statEndAmt8;
}





public double getStatInitAmt9() {
	return statInitAmt9;
}





public void setStatInitAmt9(double statInitAmt9) {
	this.statInitAmt9 = statInitAmt9;
}





public double getStatEndAmt9() {
	return statEndAmt9;
}





public void setStatEndAmt9(double statEndAmt9) {
	this.statEndAmt9 = statEndAmt9;
}





public double getStatInitAmt10() {
	return statInitAmt10;
}





public void setStatInitAmt10(double statInitAmt10) {
	this.statInitAmt10 = statInitAmt10;
}





public double getStatEndAmt10() {
	return statEndAmt10;
}





public void setStatEndAmt10(double statEndAmt10) {
	this.statEndAmt10 = statEndAmt10;
}





public double getStatInitAmt11() {
	return statInitAmt11;
}





public void setStatInitAmt11(double statInitAmt11) {
	this.statInitAmt11 = statInitAmt11;
}





public double getStatEndAmt11() {
	return statEndAmt11;
}





public void setStatEndAmt11(double statEndAmt11) {
	this.statEndAmt11 = statEndAmt11;
}





public double getStatInitAmt12() {
	return statInitAmt12;
}





public void setStatInitAmt12(double statInitAmt12) {
	this.statInitAmt12 = statInitAmt12;
}





public double getStatEndAmt12() {
	return statEndAmt12;
}





public void setStatEndAmt12(double statEndAmt12) {
	this.statEndAmt12 = statEndAmt12;
}





public double getStatInitAmtQ1() {
	return statInitAmtQ1;
}





public void setStatInitAmtQ1(double statInitAmtQ1) {
	this.statInitAmtQ1 = statInitAmtQ1;
}





public double getStatEndAmtQ1() {
	return statEndAmtQ1;
}





public void setStatEndAmtQ1(double statEndAmtQ1) {
	this.statEndAmtQ1 = statEndAmtQ1;
}





public double getStatInitAmtQ2() {
	return statInitAmtQ2;
}





public void setStatInitAmtQ2(double statInitAmtQ2) {
	this.statInitAmtQ2 = statInitAmtQ2;
}





public double getStatEndAmtQ2() {
	return statEndAmtQ2;
}





public void setStatEndAmtQ2(double statEndAmtQ2) {
	this.statEndAmtQ2 = statEndAmtQ2;
}





public double getStatInitAmtQ3() {
	return statInitAmtQ3;
}





public void setStatInitAmtQ3(double statInitAmtQ3) {
	this.statInitAmtQ3 = statInitAmtQ3;
}





public double getStatEndAmtQ3() {
	return statEndAmtQ3;
}





public void setStatEndAmtQ3(double statEndAmtQ3) {
	this.statEndAmtQ3 = statEndAmtQ3;
}





public double getStatInitAmtQ4() {
	return statInitAmtQ4;
}





public void setStatInitAmtQ4(double statInitAmtQ4) {
	this.statInitAmtQ4 = statInitAmtQ4;
}





public double getStatEndAmtQ4() {
	return statEndAmtQ4;
}





public void setStatEndAmtQ4(double statEndAmtQ4) {
	this.statEndAmtQ4 = statEndAmtQ4;
}





public double getStatInitAmtY1() {
	return statInitAmtY1;
}





public void setStatInitAmtY1(double statInitAmtY1) {
	this.statInitAmtY1 = statInitAmtY1;
}





public double getStatEndAmtY1() {
	return statEndAmtY1;
}





public void setStatEndAmtY1(double statEndAmtY1) {
	this.statEndAmtY1 = statEndAmtY1;
}





public double getStatInitAmtY2() {
	return statInitAmtY2;
}





public void setStatInitAmtY2(double statInitAmtY2) {
	this.statInitAmtY2 = statInitAmtY2;
}





public double getStatEndAmtY2() {
	return statEndAmtY2;
}





public void setStatEndAmtY2(double statEndAmtY2) {
	this.statEndAmtY2 = statEndAmtY2;
}





public double getStatInitAmtY() {
	return statInitAmtY;
}





public void setStatInitAmtY(double statInitAmtY) {
	this.statInitAmtY = statInitAmtY;
}





public double getStatEndAmtY() {
	return statEndAmtY;
}





public void setStatEndAmtY(double statEndAmtY) {
	this.statEndAmtY = statEndAmtY;
}





	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

   
}