package com.yucheng.cmis.pub.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
	//+
	public static double add(double d1,double d2,int scale,int roundMode){
		double d=0;
		BigDecimal bd1=new BigDecimal(Double.toString(d1));
		BigDecimal bd2=new BigDecimal(Double.toString(d2));
		d=bd1.add(bd2).setScale(scale, roundMode).doubleValue();
		return d;
	}
	
    //-
	public static double sub(double d1,double d2,int scale,int roundMode){
		double d=0;
		BigDecimal bd1=new BigDecimal(Double.toString(d1));
		BigDecimal bd2=new BigDecimal(Double.toString(d2));
		d=bd1.subtract(bd2).setScale(scale, roundMode).doubleValue();
		return d;
	} 
	
    //*
	public static double mul(double d1,double d2,int scale,int roundMode){
		double d=0;
		BigDecimal bd1=new BigDecimal(Double.toString(d1));
		BigDecimal bd2=new BigDecimal(Double.toString(d2));
		d=bd1.multiply(bd2).setScale(scale, roundMode).doubleValue();
		return d;
	}
	
	 /* ��/ */
	public static double div(double d1,double d2,int scale,int roundMode) throws Exception{
		double d=0;
		BigDecimal bd1=new BigDecimal(Double.toString(d1));
		BigDecimal bd2=new BigDecimal(Double.toString(d2));
		//d=bd1.divide(bd2).setScale(scale, roundMode).doubleValue();
		d=bd1.divide(bd2, scale, roundMode).doubleValue();
		return d;
	}
	
	public static BigDecimal replaceNull(Object obj) throws Exception{
		if(obj == null || "".equals(obj) || "null".equals(obj)){   
			return new BigDecimal(0);
		}else{
			return new BigDecimal(obj.toString());
		}
	}
}
