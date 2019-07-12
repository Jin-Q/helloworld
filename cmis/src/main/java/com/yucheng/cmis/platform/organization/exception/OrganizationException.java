package com.yucheng.cmis.platform.organization.exception;

/**
 * 组织机构管理模块异常
 * 
 * @time 2013-04-19
 * @author yuhq
 * @version 3.0
 * @since 3.0
 *
 */
public class OrganizationException extends Exception{

	public OrganizationException(){
		super();
	}
	
	public OrganizationException(String str){
		super(str);
	}
	
	public OrganizationException(Throwable e){
		super(e);
	}
	
	public OrganizationException(String src, Throwable e){
		super(src, e);
	}
	
}
