package com.yucheng.cmis.platform.workflow.exception;

/**
 * 信贷与流程引擎接入模块的异常信息
 * 
 * @author yuhq
 *
 */
public class WFIException extends Exception{

	private static final long serialVersionUID = 7806298044751666319L;

	public WFIException() {
		super();
	}

	public WFIException(String msg) {
		super(msg);
	}

	public WFIException(Throwable nestedThrowable) {
		// this.nestedThrowable = nestedThrowable;
		super(nestedThrowable);
	}

	public WFIException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
		// this.nestedThrowable = nestedThrowable;
	}
}
