package com.yucheng.cmis.pub.exception;

 
import com.yucheng.cmis.base.CMISException;

/**
 * 定义异步提交时候，throw exception
 * 
 * @author rendongxie
 * @time 2010-5-5
 */
public class AsynException extends CMISException{
	public AsynException() {
		super();
	}

	public AsynException(String message) {
		super(message);
		this.setShowMessage(message);
	}
	/**
	 * 最常用的组件层异常方法生成在抛出方法
	 * @param errorCode	出错编码
	 * @param message	错误信息（如果在配置文件中没找到出错编码时，取的值）
	 */
	public AsynException(String errorCode, String message) {
		super(errorCode, message);
		this.setShowMessage(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AsynException(String message, Throwable cause) {
		super(message, cause);
		this.setShowMessage(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AsynException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		this.setShowMessage(message);
	}

	/**
	 * @param cause
	 */
	public AsynException(Throwable cause) {
		super(cause);
	}
   
}
