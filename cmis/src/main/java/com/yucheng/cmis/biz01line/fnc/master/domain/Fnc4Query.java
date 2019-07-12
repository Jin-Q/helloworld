package com.yucheng.cmis.biz01line.fnc.master.domain;


import com.yucheng.cmis.pub.CMISDomain;



/**
 * 用于查询客户财报的条件封装对象
 * @Classname com.yucheng.cmis.fnc.master.domain.FncStatBase4Query.java
 * @author wqgang
 * @Since 2009-4-9 下午02:49:25 
 * @Copyright yuchengtech
 * @version 1.0
 */

public class Fnc4Query implements CMISDomain {
	 
	/** 客户代码*/
	  private String cusId;
	  /**
	   * 科目ID
	   */
	  private String itemId;
	  /**
	   * 数据归属日期
	   * 格式为
	   * yyyyMMdd
	   */
	  private String vDate; 
	  /**
	   * 财报类型
	   * 01:资产负债表
       * 02:损益表
       * 03:现金流量
       * 04:财务指标
       * 05.所有者权益变动表
       * 06财务简表
	   */
	  private String fncType;
	  
	 /**
	  *  报表周期类型
	  *  1:月报
	  *  2:季报
	  *	 3:半年报
	  *	 4:年报
	  */
	  
	  public String  termType;
	 
	  /**
	   * 期初期末类型
	   * 
	   */
	  private String  iEType; 
	  
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


	public String getIEType() {
		return iEType;
	}


	public void setIEType(String type) {
		iEType = type;
	}


	public String getCusId() {
		return cusId;
	}


	public void setCusId(String cusId) {
		this.cusId = cusId;
	}


	public String getItemId() {
		return itemId;
	}


	public void setItemId(String itemId) {
		this.itemId = itemId;
	}





	public String getVDate() {
		return vDate;
	}


	public void setVDate(String date) {
		vDate = date;
	}


	public String getFncType() {
		return fncType;
	}


	public void setFncType(String fncType) {
		this.fncType = fncType;
	}


	public String getTermType() {
		return termType;
	}


	public void setTermType(String termType) {
		this.termType = termType;
	}


	public Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		return result; 

		} 


}