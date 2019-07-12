package com.yucheng.cmis.pub.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 	模块服务接口注解类
 * 	主要用于接口方法的描述
 * </p>
 * 
 * @author yuhq
 * @version 1.0
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodService {

	public static enum MethodType{JSP, JAVA};
	
	/**
	 * 方法名
	 * @return 方法名
	 */
	String method();
	
	/**
	 * 方法描述
	 * @return 方法描述
	 */
	String desc();
	
	/**
	 * 输入参数,缺省是空
	 * @return 输入参数
	 */
	MethodParam[] inParam() default @MethodParam(paramName="",paramDesc="");
	
	/**
	 * 输出参数,缺省是空
	 * @return 输出参数
	 */
	MethodParam[] outParam() default @MethodParam(paramName="",paramDesc="");
	
	/**
	 * 调用示例 ,缺省是空
	 * @return 调用示例 
	 */
	String example() default "";
	
	/**
	 * 方法类型，JAVA or JSP,缺省是：MethodType.JAVA
	 * @return 枚举：MethodType.JAVA MethodType.JSP
	 */
	MethodType methodType() default MethodType.JAVA;
}
