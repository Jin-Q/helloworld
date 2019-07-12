package com.yucheng.cmis.pub.util;

import org.apache.poi.hssf.util.HSSFColor;

/**
 * <p>Title:Excel文件cell单元格的java对象模型</p>
 * <p>Copyright:yucheng Copyright (c) 2008</p>
 * <p>Company: yuchengtech</p>
 * @author ljy
 * @version 1.0
 */
public class CellVO {
	
	//定义单元格类型常量
	public static final int CELL_TYPE_NUMERIC = 0;
	public static final int CELL_TYPE_STRING = 1;
	public static final int CELL_TYPE_FORMULA = 2;
	public static final int CELL_TYPE_BLANK = 3;
	public static final int CELL_TYPE_BOOLEAN = 4;
	public static final int CELL_TYPE_ERROR = 5;
	
	//定义单元格背景色
	public static final int CELL_YELLOW = HSSFColor.YELLOW.index;			//黄色
	public static final int LIGHT_TURQUOISE= HSSFColor.LIGHT_TURQUOISE.index;
	
	public int celltype=1;//单元格类型
	public int cellrownum;//单元格所在行数，从0开始
	public int cellcolnum;//单元格所在列数，从0开始
	public short cellbgcolor = 0;//单元格背景色
	public Object cellvalue;//单元格的值
	public String celloriginalvalue;//单元格原始值
	
	public int cellwidth = 0; //单元格 宽度(统一)
	
	public String toString(){
		return "cells["+cellrownum+"]["+cellcolnum+"]={celltype="+celltype+";cellvalue="+cellvalue+"}";
	}
}
