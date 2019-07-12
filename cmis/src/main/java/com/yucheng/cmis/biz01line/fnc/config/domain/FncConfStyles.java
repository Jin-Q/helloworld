package com.yucheng.cmis.biz01line.fnc.config.domain;


import java.util.ArrayList;
import java.util.List;

import com.yucheng.cmis.pub.CMISDomain;
  /**
 *@Classname	FncConfStyles.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-6 下午03:48:10  
 *@Copyright 	yuchengtech
 *@Author 		Yu
 *              报表样式信息(包含具体项目信息列表)
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncConfStyles implements CMISDomain {
	
	/**
	 * 对应FNC_CONF_STYLES里面相应的变量 样式表编号
	 */
	private String styleId;
	
	/**
	 * 对应数据库里面的哪一张表 例如FNC_Stat_BS
	 */
	private String fncName;
	
	/**
	 * 例如"资产负债表"
	 */
	private String fncConfDisName;
	
	/**
	 * 对应FNC_CONF_STYLES里面相应的变量
	 */
	private String fncConfTyp;
	
	/**
	 * 数据列数
	 */
	private int fncConfDataCol;      
	
	/**
	 * //分栏数
	 */
	private int fncConfCotes;           
	
	/**
	 * //用于存放'报表配置定义表'的信息
	 */
	private List<FncConfDefFormat> items;      

	private String headLeft;
	private String foodRight;
	private String foodCenter;
	private String foodLeft;
	private String headRight;
	private String headCenter;
	private String noInd;
	private String comInd;
	private String dataDec1;
	private String dataDec2;
	private String dataDec3;
	private String dataDec4;
	private String dataDec5;
	private String dataDec6;
	private String dataDec7;
	private String dataDec8;
	
	public String getDataDec3() {
		return dataDec3;
	}

	public void setDataDec3(String dataDec3) {
		this.dataDec3 = dataDec3;
	}

	public String getDataDec4() {
		return dataDec4;
	}

	public void setDataDec4(String dataDec4) {
		this.dataDec4 = dataDec4;
	}

	public String getDataDec5() {
		return dataDec5;
	}

	public void setDataDec5(String dataDec5) {
		this.dataDec5 = dataDec5;
	}

	public String getDataDec6() {
		return dataDec6;
	}

	public void setDataDec6(String dataDec6) {
		this.dataDec6 = dataDec6;
	}

	public String getDataDec7() {
		return dataDec7;
	}

	public void setDataDec7(String dataDec7) {
		this.dataDec7 = dataDec7;
	}

	public String getDataDec8() {
		return dataDec8;
	}

	public void setDataDec8(String dataDec8) {
		this.dataDec8 = dataDec8;
	}

	public String getDataDec1() {
		return dataDec1;
	}

	public void setDataDec1(String dataDec1) {
		this.dataDec1 = dataDec1;
	}

	public String getDataDec2() {
		return dataDec2;
	}

	public void setDataDec2(String dataDec2) {
		this.dataDec2 = dataDec2;
	}

	public String getNoInd() {
		return noInd;
	}

	public void setNoInd(String noInd) {
		this.noInd = noInd;
	}

	public String getComInd() {
		return comInd;
	}

	public void setComInd(String comInd) {
		this.comInd = comInd;
	}

	public int getFncConfCotes() {
		return fncConfCotes;
	}

	public void setFncConfCotes(int fncConfCotes) {
		this.fncConfCotes = fncConfCotes;
	}

	public int getFncConfDataCol() {
		return fncConfDataCol;
	}

	public void setFncConfDataCol(int fncConfDataCol) {
		this.fncConfDataCol = fncConfDataCol;
	}

	public List<FncConfDefFormat> getItems() {
		return items;
	}

	public void setItems(List<FncConfDefFormat> items) {
		this.items = items;
	}

	public String getFncConfDisName() {
		return fncConfDisName;
	}

	public void setFncConfDisName(String fncConfDisName) {
		this.fncConfDisName = fncConfDisName;
	}

	public String getFncConfTyp() {
		return fncConfTyp;
	}

	public void setFncConfTyp(String fncConfTyp) {
		this.fncConfTyp = fncConfTyp;
	}

	public String getFncName() {
		return fncName;
	}

	public void setFncName(String fncName) {
		this.fncName = fncName;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	
	

	
	
	
	public String getHeadLeft() {
		return headLeft;
	}

	public void setHeadLeft(String headLeft) {
		this.headLeft = headLeft;
	}

	public String getFoodRight() {
		return foodRight;
	}

	public void setFoodRight(String foodRight) {
		this.foodRight = foodRight;
	}

	public String getFoodCenter() {
		return foodCenter;
	}

	public void setFoodCenter(String foodCenter) {
		this.foodCenter = foodCenter;
	}

	public String getFoodLeft() {
		return foodLeft;
	}

	public void setFoodLeft(String foodLeft) {
		this.foodLeft = foodLeft;
	}

	public String getHeadRight() {
		return headRight;
	}

	public void setHeadRight(String headRight) {
		this.headRight = headRight;
	}

	public String getHeadCenter() {
		return headCenter;
	}

	public void setHeadCenter(String headCenter) {
		this.headCenter = headCenter;
	}


	
	
	public Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 
		
		FncConfStyles result = new FncConfStyles(); 
		
		result.setStyleId(this.getStyleId());
		result.setFncName(this.getFncName());
		result.setFncConfCotes(this.getFncConfCotes());
		result.setFncConfDataCol(this.getFncConfDataCol());
		result.setFncConfDisName(this.getFncConfDisName());
		result.setFncConfTyp(this.getFncConfTyp());
		result.setFoodCenter(foodCenter);
		result.setFoodLeft(foodLeft);
		result.setFoodRight(foodRight);
		result.setHeadCenter(headCenter);
		result.setHeadLeft(headLeft);
		result.setHeadRight(headRight);
		result.setComInd(comInd);
		result.setNoInd(noInd);
		result.setDataDec1(dataDec1);
		result.setDataDec2(dataDec2);
		result.setDataDec3(dataDec3);
		result.setDataDec4(dataDec4);
		result.setDataDec5(dataDec5);
		result.setDataDec6(dataDec6);
		result.setDataDec7(dataDec7);
		result.setDataDec8(dataDec8);
		
		List<FncConfDefFormat> list = new ArrayList<FncConfDefFormat>();
	    List<FncConfDefFormat> itemsList = this.getItems();
	    FncConfDefFormat fcdf=new FncConfDefFormat();
	    for(int i=0;i<itemsList.size();i++){
	    	fcdf=((FncConfDefFormat)itemsList.get(i)).clone();
	    	list.add(fcdf);
	    }
		result.setItems(list);

		return result; 

		} 

}
