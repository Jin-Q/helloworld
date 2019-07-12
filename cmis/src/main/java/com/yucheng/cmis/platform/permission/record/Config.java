package com.yucheng.cmis.platform.permission.record;

import java.util.HashMap;

public class Config {

	/** 记录级权限模板配置缓存*/
	public static HashMap<String,String> permissionTemplate = null;
	
	/** 记录级权限模板配置————权限字段名————机构*/
	public static HashMap<String,String> permissionBCHField = null;

	/** 记录级权限模板配置————权限字段名————用户*/
	public static HashMap<String,String> permissionUSRField = null;

	
	static {
		permissionTemplate = new HashMap<String,String>();
		
		permissionBCHField = new HashMap<String,String>();
		
		permissionUSRField = new HashMap<String,String>();
	}
}
