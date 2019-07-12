package com.yucheng.cmis.biz01line.fnc.master.domain;


import com.yucheng.cmis.pub.CMISDomain;

/**
 *@Classname	FncStatCfs.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 3:07:23 PM  
 *@Copyright 	yuchengtech
 *@Author 		Eric
 *@Description：公司客户现金流量项目信息
 *@Lastmodified 
 *@Author
 */

public class FncStatCfs implements CMISDomain {
   /** 客户代码*/
   private java.lang.String cusId;
   
   /** 报表年*/
   private java.lang.String statYear;
   
   /** 项目编号 */
   private java.lang.String statItemId;
   
   /** 一月数值*/
   private double statInitAmt1;
   
   /** 二月数值 */
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
   
   /** 二季度数值 */
   private double statInitAmtQ2;
   
   /** 三季度数值*/
   private double statInitAmtQ3;
   
   /** 四季度数值*/
   private double statInitAmtQ4;
   
   /** 上半年数值*/
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

	/**
	 * @return the cusId
	 */
	public java.lang.String getCusId() {
		return cusId;
	}
	
	/**
	 * @param cusId the cusId to set
	 */
	public void setCusId(java.lang.String cusId) {
		this.cusId = cusId;
	}
	
	/**
	 * @return the statInitAmt1
	 */
	public double getStatInitAmt1() {
		return statInitAmt1;
	}
	
	/**
	 * @param statInitAmt1 the statInitAmt1 to set
	 */
	public void setStatInitAmt1(double statInitAmt1) {
		this.statInitAmt1 = statInitAmt1;
	}
	
	/**
	 * @return the statInitAmt10
	 */
	public double getStatInitAmt10() {
		return statInitAmt10;
	}
	
	/**
	 * @param statInitAmt10 the statInitAmt10 to set
	 */
	public void setStatInitAmt10(double statInitAmt10) {
		this.statInitAmt10 = statInitAmt10;
	}
	
	/**
	 * @return the statInitAmt11
	 */
	public double getStatInitAmt11() {
		return statInitAmt11;
	}
	
	/**
	 * @param statInitAmt11 the statInitAmt11 to set
	 */
	public void setStatInitAmt11(double statInitAmt11) {
		this.statInitAmt11 = statInitAmt11;
	}
	
	/**
	 * @return the statInitAmt12
	 */
	public double getStatInitAmt12() {
		return statInitAmt12;
	}
	
	/**
	 * @param statInitAmt12 the statInitAmt12 to set
	 */
	public void setStatInitAmt12(double statInitAmt12) {
		this.statInitAmt12 = statInitAmt12;
	}
	
	/**
	 * @return the statInitAmt2
	 */
	public double getStatInitAmt2() {
		return statInitAmt2;
	}
	
	/**
	 * @param statInitAmt2 the statInitAmt2 to set
	 */
	public void setStatInitAmt2(double statInitAmt2) {
		this.statInitAmt2 = statInitAmt2;
	}
	
	/**
	 * @return the statInitAmt3
	 */
	public double getStatInitAmt3() {
		return statInitAmt3;
	}
	
	/**
	 * @param statInitAmt3 the statInitAmt3 to set
	 */
	public void setStatInitAmt3(double statInitAmt3) {
		this.statInitAmt3 = statInitAmt3;
	}
	
	/**
	 * @return the statInitAmt4
	 */
	public double getStatInitAmt4() {
		return statInitAmt4;
	}
	
	/**
	 * @param statInitAmt4 the statInitAmt4 to set
	 */
	public void setStatInitAmt4(double statInitAmt4) {
		this.statInitAmt4 = statInitAmt4;
	}
	
	/**
	 * @return the statInitAmt5
	 */
	public double getStatInitAmt5() {
		return statInitAmt5;
	}
	
	/**
	 * @param statInitAmt5 the statInitAmt5 to set
	 */
	public void setStatInitAmt5(double statInitAmt5) {
		this.statInitAmt5 = statInitAmt5;
	}
	
	/**
	 * @return the statInitAmt6
	 */
	public double getStatInitAmt6() {
		return statInitAmt6;
	}
	
	/**
	 * @param statInitAmt6 the statInitAmt6 to set
	 */
	public void setStatInitAmt6(double statInitAmt6) {
		this.statInitAmt6 = statInitAmt6;
	}
	
	/**
	 * @return the statInitAmt7
	 */
	public double getStatInitAmt7() {
		return statInitAmt7;
	}
	
	/**
	 * @param statInitAmt7 the statInitAmt7 to set
	 */
	public void setStatInitAmt7(double statInitAmt7) {
		this.statInitAmt7 = statInitAmt7;
	}
	
	/**
	 * @return the statInitAmt8
	 */
	public double getStatInitAmt8() {
		return statInitAmt8;
	}
	
	/**
	 * @param statInitAmt8 the statInitAmt8 to set
	 */
	public void setStatInitAmt8(double statInitAmt8) {
		this.statInitAmt8 = statInitAmt8;
	}
	
	/**
	 * @return the statInitAmt9
	 */
	public double getStatInitAmt9() {
		return statInitAmt9;
	}
	
	/**
	 * @param statInitAmt9 the statInitAmt9 to set
	 */
	public void setStatInitAmt9(double statInitAmt9) {
		this.statInitAmt9 = statInitAmt9;
	}
	
	/**
	 * @return the statInitAmtQ1
	 */
	public double getStatInitAmtQ1() {
		return statInitAmtQ1;
	}
	
	/**
	 * @param statInitAmtQ1 the statInitAmtQ1 to set
	 */
	public void setStatInitAmtQ1(double statInitAmtQ1) {
		this.statInitAmtQ1 = statInitAmtQ1;
	}
	
	/**
	 * @return the statInitAmtQ2
	 */
	public double getStatInitAmtQ2() {
		return statInitAmtQ2;
	}
	
	/**
	 * @param statInitAmtQ2 the statInitAmtQ2 to set
	 */
	public void setStatInitAmtQ2(double statInitAmtQ2) {
		this.statInitAmtQ2 = statInitAmtQ2;
	}
	
	/**
	 * @return the statInitAmtQ3
	 */
	public double getStatInitAmtQ3() {
		return statInitAmtQ3;
	}
	
	/**
	 * @param statInitAmtQ3 the statInitAmtQ3 to set
	 */
	public void setStatInitAmtQ3(double statInitAmtQ3) {
		this.statInitAmtQ3 = statInitAmtQ3;
	}
	
	/**
	 * @return the statInitAmtQ4
	 */
	public double getStatInitAmtQ4() {
		return statInitAmtQ4;
	}
	
	/**
	 * @param statInitAmtQ4 the statInitAmtQ4 to set
	 */
	public void setStatInitAmtQ4(double statInitAmtQ4) {
		this.statInitAmtQ4 = statInitAmtQ4;
	}
	
	/**
	 * @return the statInitAmtY
	 */
	public double getStatInitAmtY() {
		return statInitAmtY;
	}
	
	/**
	 * @param statInitAmtY the statInitAmtY to set
	 */
	public void setStatInitAmtY(double statInitAmtY) {
		this.statInitAmtY = statInitAmtY;
	}
	
	/**
	 * @return the statInitAmtY1
	 */
	public double getStatInitAmtY1() {
		return statInitAmtY1;
	}
	
	/**
	 * @param statInitAmtY1 the statInitAmtY1 to set
	 */
	public void setStatInitAmtY1(double statInitAmtY1) {
		this.statInitAmtY1 = statInitAmtY1;
	}
	
	/**
	 * @return the statInitAmtY2
	 */
	public double getStatInitAmtY2() {
		return statInitAmtY2;
	}
	
	/**
	 * @param statInitAmtY2 the statInitAmtY2 to set
	 */
	public void setStatInitAmtY2(double statInitAmtY2) {
		this.statInitAmtY2 = statInitAmtY2;
	}
	
	/**
	 * @return the statItemId
	 */
	public java.lang.String getStatItemId() {
		return statItemId;
	}
	
	/**
	 * @param statItemId the statItemId to set
	 */
	public void setStatItemId(java.lang.String statItemId) {
		this.statItemId = statItemId;
	}
	
	/**
	 * @return the statYear
	 */
	public java.lang.String getStatYear() {
		return statYear;
	}
	
	/**
	 * @param statYear the statYear to set
	 */
	public void setStatYear(java.lang.String statYear) {
		this.statYear = statYear;
	}
   
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 


}