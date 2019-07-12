package com.yucheng.cmis.pub;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @Classname com.yucheng.cmis.pub.FncNumber.java
 * @author wqgang
 * @Since 2009-4-22 下午02:06:21 
 * @Copyright yuchengtech
 * @version 1.0
 */
public class FncNumber {
	
	/**
	 * 得到当前系统日期 
	 * 格式:yyyyMMdd
	 * @return
	 */
	public static String getSysDate(){
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		String strDate=sdf.format(date);
		return strDate;
	}
	/**
	 * 计算季报
	 * @param month
	 * @return
	 */
	public static String getJibao(String month){
		String postfix = null;
		int mm = Integer.parseInt(month);
		switch(mm){
			case 1:
			case 2:
			case 3:
			postfix = "_Q1";
			break;
			case 4:
			case 5:
			case 6:
			postfix = "_Q2";
			break;
			case 7:
			case 8:
			case 9:
			postfix = "_Q3";
			break;
			case 10:
			case 11:
			case 12:
			postfix = "_Q4";
			break;
			
			default: break;
		}
		return postfix;
	}
	/**
	 * 计算半年报
	 * @return
	 */
	public static String getBanNianBao(String month){
		String postfix = null;
		int mm = Integer.parseInt(month);
		switch(mm){
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			postfix = "_Y1";
			break;
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			postfix = "_Y2";
			break;
			
			default: break;
		}
		return postfix;
	}
	/**
	 * 计算年报
	 * @return
	 */
	public static String getNianBao(String month){
		String postfix = null;
		int mm = Integer.parseInt(month);
		switch(mm){
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			postfix = "_Y";
			break;
			
			default: break;
		}
		return postfix;
	}
}
