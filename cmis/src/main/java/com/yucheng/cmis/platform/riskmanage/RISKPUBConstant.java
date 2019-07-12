package com.yucheng.cmis.platform.riskmanage;

import java.util.HashMap;

/**
 * 此类的作用是为方便应用中常量的管理，提供更加有效的常量存储机制，方便应用系统中类的调用，避免 程序中过多的出现硬编码。
 * 
 *
 */
public class RISKPUBConstant {
	/**
	 * 风险拦截检查结果
	 */
	public static   HashMap<String,String> WFI_RISKINSPECT_RESULT=new HashMap<String,String>();
	static{
    	/*
    	 * '1':'通过', '2':'不通过',
    	 * */
		WFI_RISKINSPECT_RESULT.put("通过","1");
		WFI_RISKINSPECT_RESULT.put("不通过","2");
    	
    }
	  /** 风险拦截检查结果  ———— 通过*/
	public static final String WFI_RISKINSPECT_RESULT_PASS = "1";

    /** 风险拦截检查结果  ———— 不通过*/
	public static final String WFI_RISKINSPECT_RESULT_DENY = "2";
	 /** 风险拦截检查结果  ———— 忽略*/
	public static final String WFI_RISKINSPECT_RESULT_CANCEL = "3";
    /** 风险拦截检查结果  ———— 异常*/
	public static final String WFI_RISKINSPECT_RESULT_EXCEPTION= "5";
}
