package com.yucheng.cmis.biz01line.fnc.config.domain;


import com.yucheng.cmis.pub.CMISDomain;

  /**
 *@Classname	FncConfDefFormat.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-6 下午08:19:33  
 *@Copyright 	yuchengtech
 *@Author 		Yu
 *@Description：代表一个item的具体信息
 *@Lastmodified  2009 3 30 xuyp 加注释 
 *@Author
 */
public class FncConfDefFormat implements CMISDomain{
	
	private String styleId;         	//报表样式编号
	
	private String itemId;         		//项目编号
	
	private String itemName;
	
	private int appendRow;
	
	/**
	 * //顺序编号  就是行号
	 */
	private int fncConfOrder;      		//顺序编号
	
	/**
	 * //栏位
	 */
	private int fncConfCotes;       	//栏位
	
	/**
	 * //行次标识 是否显示行号
	 */
	private String fncConfRowFlg;   	//行次标识
	
	private int fncConfIndent;       	//层次
	
	private String fncConfPrefix;    	//前缀
	
	/**
	 * 0 常量项
	 * 1 手工输入项
	 * 2 自动计算
	 * 3 标题项
	 * 具体参见s_dic中的memo = ‘财务报表项目编辑方式’
	 */
	private String fncItemEditTyp;      //项目编辑方式
	
	private double fncConfDispAmt;    //显示数值
	
	private String fncConfChkFrm;   //检查公式
	
	private String fncConfCalFrm;     //计算公式
	
	private int fncCnfAppRow;        //追加行数
	
	private String fncConfDispTpy;      //默认现实类型
	
	/**
	 * 期初
	 */
	private double data1;                //用于存放数据（对已经存在的报表而言）  该字段不用于对数据库插入
	
	/**
	 * 期末
	 */
	private double data2;                //同上
	
	private double average;         //新增加的行业标准值
	


	private double[] dataA;			//所有者权益变动表用
	
	private double[] dataB;

	
	public double[] getDataA() {
		return dataA;
	}

	public void setDataA(double[] dataA) {
		this.dataA = dataA;
	}

	public double[] getDataB() {
		return dataB;
	}

	public void setDataB(double[] dataB) {
		this.dataB = dataB;
	}


	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
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



	public double getFncConfDispAmt() {
		return fncConfDispAmt;
	}

	public void setFncConfDispAmt(double fncConfDispAmt) {
		this.fncConfDispAmt = fncConfDispAmt;
	}

	public String getFncConfChkFrm() {
		return fncConfChkFrm;
	}

	public void setFncConfChkFrm(String fncConfChkFrm) {
		this.fncConfChkFrm = fncConfChkFrm;
	}

	public String getFncConfCalFrm() {
		return fncConfCalFrm;
	}

	public void setFncConfCalFrm(String fncConfCalFrm) {
		this.fncConfCalFrm = fncConfCalFrm;
	}

	public int getFncCnfAppRow() {
		return fncCnfAppRow;
	}

	public void setFncCnfAppRow(int fncCnfAppRow) {
		this.fncCnfAppRow = fncCnfAppRow;
	}

	public String getFncConfDispTpy() {
		return fncConfDispTpy;
	}

	public void setFncConfDispTpy(String fncConfDispTpy) {
		this.fncConfDispTpy = fncConfDispTpy;
	}

	public int getFncConfCotes() {
		return fncConfCotes;
	}

	public void setFncConfCotes(int fncConfCotes) {
		this.fncConfCotes = fncConfCotes;
	}


	public int getFncConfIndent() {
		return fncConfIndent;
	}

	public void setFncConfIndent(int fncConfIndent) {
		this.fncConfIndent = fncConfIndent;
	}

	public int getFncConfOrder() {
		return fncConfOrder;
	}

	public void setFncConfOrder(int fncConfOrder) {
		this.fncConfOrder = fncConfOrder;
	}

	public String getFncConfPrefix() {
		return fncConfPrefix;
	}

	public void setFncConfPrefix(String fncConfPrefix) {
		this.fncConfPrefix = fncConfPrefix;
	}

	public String getFncConfRowFlg() {
		return fncConfRowFlg;
	}

	public void setFncConfRowFlg(String fncConfRowFlg) {
		this.fncConfRowFlg = fncConfRowFlg;
	}

	public String getFncItemEditTyp() {
		return fncItemEditTyp;
	}

	public void setFncItemEditTyp(String fncItemEditTyp) {
		this.fncItemEditTyp = fncItemEditTyp;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public int getAppendRow() {
		return appendRow;
	}

	public void setAppendRow(int appendRow) {
		this.appendRow = appendRow;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	
	public FncConfDefFormat clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		FncConfDefFormat result = new FncConfDefFormat(); 
		result.setAppendRow(this.getAppendRow());
		result.setAverage(this.getAverage());
		result.setData1(this.getData1());
		result.setData2(this.getData2());
		
		result.setFncCnfAppRow(this.getAppendRow());
		result.setFncConfCalFrm(this.getFncConfCalFrm());
		result.setFncConfChkFrm(this.getFncConfChkFrm());
		result.setFncConfCotes(this.getFncConfCotes());
		
		result.setFncConfDispAmt(this.getFncConfDispAmt());
		result.setFncConfDispTpy(this.getFncConfDispTpy());
		result.setFncConfIndent(this.getFncConfIndent());
		result.setFncConfOrder(this.getFncConfOrder());
		
		result.setFncConfPrefix(this.getFncConfPrefix());
		result.setFncConfRowFlg(this.getFncConfRowFlg());
		result.setFncItemEditTyp(this.getFncItemEditTyp());
		result.setItemId(this.getItemId());
		
		result.setItemName(this.getItemName());
		result.setStyleId(this.getStyleId());
		
		result.setDataA(dataA);
		result.setDataB(dataB);


		//TODO: 定制clone数据 

		return result; 

		} 

}
