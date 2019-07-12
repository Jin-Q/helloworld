package com.yucheng.cmis.platform.msiview.exception;

/**
 * MSI View模块的异常
 * 
 * @time 2013-04-19
 * @author yuhq
 * @version 3.0
 * @since 3.0
 *
 */
public class MSIViewException extends Exception{

	public MSIViewException(){
		super();
	}
	
	public MSIViewException(String str){
		super(str);
	}
	
	public MSIViewException(Throwable e){
		super(e);
	}
	
	public MSIViewException(String src, Throwable e){
		super(src, e);
	}
	
}
