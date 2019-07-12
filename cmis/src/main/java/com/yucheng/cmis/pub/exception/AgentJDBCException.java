package com.yucheng.cmis.pub.exception;

/**
 *@Classname	AgentJDBCException.java
 *@Version 1.0	
 *@Since   1.0 	Oct 13, 2008 4:35:32 PM  
 *@Copyright 	yuchengtech
 *@Author 		Eric
 *@Description：本类主要处理所有在代理层抛出的sql异常
 *@Lastmodified 
 *@Author
 */
public class AgentJDBCException extends AgentException{
	
	

	private static final long serialVersionUID = 1L;

	public AgentJDBCException() {
		super();
	}

	public AgentJDBCException(String message) {
		super(message);
		this.setShowMessage(message);
	}
	
	/**
	 * 最常用的代理层异常方法生成在抛出方法
	 * @param errorCode	出错编码
	 * @param message	错误信息（如果在配置文件中没找到出错编码时，取的值）
	 */
	public AgentJDBCException(String errorCode, String message) {
		super(errorCode, message);
		this.setShowMessage(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AgentJDBCException(String message, Throwable cause) {
		super(message, cause);
		this.setShowMessage(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AgentJDBCException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		this.setShowMessage(message);
	}

	/**
	 * @param cause
	 */
	public AgentJDBCException(Throwable cause) {
		super(cause);
	}
}
