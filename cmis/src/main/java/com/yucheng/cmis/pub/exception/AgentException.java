package com.yucheng.cmis.pub.exception;


public class AgentException extends ComponentException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3976526164455463435L;

	public AgentException() {
		super();
	}

	public AgentException(String message) {
		super(message);
		this.setShowMessage(message);
	}
	
	/**
	 * 最常用的代理层异常方法生成在抛出方法
	 * @param errorCode	出错编码
	 * @param message	错误信息（如果在配置文件中没找到出错编码时，取的值）
	 */
	public AgentException(String errorCode, String message) {
		super(errorCode, message);
		this.setShowMessage(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AgentException(String message, Throwable cause) {
		super(message, cause);
		this.setShowMessage(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AgentException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		this.setShowMessage(message);
	}

	/**
	 * @param cause
	 */
	public AgentException(Throwable cause) {
		super(cause);
	}

    
}
