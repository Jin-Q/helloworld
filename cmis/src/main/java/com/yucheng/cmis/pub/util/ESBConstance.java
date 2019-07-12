package com.yucheng.cmis.pub.util;
/**
 * 
 * <p>
 * <h2>简述</h2>
 * 		<ol>ESB常量类</ol>
 * <h2>功能描述</h2>
 * 		<ol>请添加功能详细的描述</ol>
 * <h2>修改历史</h2>
 *    <ol>如有修改，添加修改历史</ol>
 * </p>
 * @author 杨锦华
 * @2016-4-19
 * @version 1.0
 */
public class ESBConstance {
	
	/** ESB配置文件**/
	public final static String AFE_SERVICE_CODE = "AfeCd";
	public final static String AFE_SERVICE_SERNO = "ChanlSn";
	public final static String APP_SERVICE_HEAD = "APP_HEAD";
	public final static String SYS_SERVICE_HEAD = "SYS_HEAD";
	public final static String SERVICE_BODY = "BODY";
	public final static String AFE_SERVICE_REQUEST = "REQ";
	public final static String AFE_SERVICE_RESPONSE = "RSP";
	/** 请求报文的请求内容根标签开头符：<head>**/
	public final static String SERVICE_START = "<service>";
	/** 请求报文的请求内容根标签结束符：<head>**/
	public final static String SERVICE_END = "</service>";
	/** 请求报文头的请求内容根标签开头符：<head>**/
	public final static String SYS_HEAD_START = "<SYS_HEAD>";
	/** 请求报文头的请求内容根标签结束符：<head>**/
	public final static String SYS_HEAD_END = "</SYS_HEAD>";
	/** 请求报文头的请求内容根标签开头符：<head>**/
	public final static String APP_HEAD_START = "<APP_HEAD>";
	/** 请求报文头的请求内容根标签结束符：<head>**/
	public final static String APP_HEAD_END = "</APP_HEAD>";
	/** 请求报文体的请求内容根标签开头符：<body>**/
	public final static String BODY_START = "<BODY>";
	/** 请求报文体的请求内容根标签结束符：<body>**/
	public final static String BODY_END = "</BODY>";
	/** AFE交易成功响应码**/
	public final static String SUCESSE_CODE = "00000000000000";
	/** AFE交易失败响应码**/
	public final static String AFE_EXCETION_CODE = "99999";
	/** AFE加密密钥:IM.rz_01_zz_efde.zak**/
	public static final String MAC_KEY_NAME_REQ = "IM.rz_01_zz_efde.zak";
	public static final String MAC_KEY_NAME_RSP = "IM.rz_01_zz_efde.zak";
	/** AFE加密字段**/
	public static final String MAC_NAME_REQ = "Org:Oprtr:ChanlDt:ChanlCd:AfeCd";
	public static final String MAC_NAME_RSP = "SvcSysRtrCd5:AfeRtrCd5";
	/** AFE零售信贷系统渠道号 **/
	public static final String CHANLCD = "B1000000";
	
	public final static String ESB_CONFIG_FILE_NAME = "Soap.properties";
	/**空字符串**/
	public final static String EMPTY_STR = "";
	/**xml标签结束符的开头：</ **/
	public final static String LEFT_END_STR = "</";
	/**xml标签结束符：>**/
	public final static char RIGHT_C = '>';
	/**xml标签开始符：<**/
	public final static char LEFT_C = '<';
	/**ESB日志打印ID**/
	public final static String ESB_LOG_ID = "ESBAppender";
	public final static String AFE_SERVICE_HEAD = "head";
	public final static String AFE_SERVICE_BODY = "body";
	/** 请求报文的请求内容根标签开头符：<head>**/
	public final static String AFE_SERVICE_START = "<cfx>";
	/** 请求报文的请求内容根标签结束符：<head>**/
	public final static String AFE_SERVICE_END = "</cfx>";
	/** 请求报文头的请求内容根标签开头符：<head>**/
	public final static String AFE_HEAD_START = "<head>";
	/** 请求报文头的请求内容根标签结束符：<head>**/
	public final static String AFE_HEAD_END = "</head>";
	/** 请求报文体的请求内容根标签开头符：<body>**/
	public final static String AFE_BODY_START = "<body>";
	/** 请求报文体的请求内容根标签结束符：<body>**/
	public final static String AFE_BODY_END = "</body>";
	/** AFE交易成功响应码**/
	public final static String AFE_SUCESSE_CODE = "00000";
}
