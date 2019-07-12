package com.yucheng.cmis.pub.util;

/**
 * <p>Title:Sheet工作表的java对象模型</p>
 * <p>Copyright:yucheng Copyright (c) 2008</p>
 * <p>Company: yuchengtech</p>
 * @author ljy
 * @version 1.0
 */
public class SheetVO {
	public String sheetname;
	public int rownum;//记录行数
	public int colnum;//列数
	public CellVO[][] cells;//存放了CellVO的二维数组
}
