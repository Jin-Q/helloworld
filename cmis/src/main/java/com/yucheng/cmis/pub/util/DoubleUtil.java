package com.yucheng.cmis.pub.util;

import java.text.DecimalFormat;

public class DoubleUtil {
	
	/** 如果Double类型数字很大，会显示为科学计数法。此方法是为了防止此种情况！*/
	public static String change(double val){
		String dbval = "0.0";
		DecimalFormat df = new DecimalFormat("0.00"); 
        dbval=df.format(val); 
        return dbval;
	}
}
