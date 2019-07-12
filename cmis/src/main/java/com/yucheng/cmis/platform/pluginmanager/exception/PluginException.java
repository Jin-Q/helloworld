package com.yucheng.cmis.platform.pluginmanager.exception;

/**
 * 插件管理异常
 * 
 * @time 2013-04-19
 * @author yuhq
 * @version 3.0
 * @since 3.0
 *
 */
public class PluginException extends Exception{

	public PluginException(){
		super();
	}
	
	public PluginException(String str){
		super(str);
	}
	
	public PluginException(Throwable e){
		super(e);
	}
	
	public PluginException(String src, Throwable e){
		super(src, e);
	}
	
}
