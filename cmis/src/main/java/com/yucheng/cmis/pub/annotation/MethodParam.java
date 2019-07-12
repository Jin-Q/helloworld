package com.yucheng.cmis.pub.annotation;

/**
 * <pre>
 * Title:描述方法的输入输出参数的注解
 * Des:
 * </pre>
 * @author yuhq
 * @version 1.0
 *
 */
public @interface MethodParam {

	/**
	 * 参数名
	 * @return 参数名
	 */
	String paramName();
	
	/**
	 * 参数描述
	 * @return 参数描述
	 */
	String paramDesc();
}
