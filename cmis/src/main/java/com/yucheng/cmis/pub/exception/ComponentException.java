package com.yucheng.cmis.pub.exception;

import com.yucheng.cmis.base.CMISException;

public class ComponentException extends CMISException {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1759207994449934353L;
	
	
	public ComponentException() {
		super();
	}

	public ComponentException(String message) {
		super(message);
		this.setShowMessage(message);
	}
	/**
	 * 最常用的组件层异常方法生成在抛出方法
	 * @param errorCode	出错编码
	 * @param message	错误信息（如果在配置文件中没找到出错编码时，取的值）
	 */
	public ComponentException(String errorCode, String message) {
		super(errorCode, message);
		this.setShowMessage(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ComponentException(String message, Throwable cause) {
		super(message, cause);
		this.setShowMessage(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ComponentException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		this.setShowMessage(message);
	}

	/**
	 * @param cause
	 */
	public ComponentException(Throwable cause) {
		super(cause);
	}
   
}
