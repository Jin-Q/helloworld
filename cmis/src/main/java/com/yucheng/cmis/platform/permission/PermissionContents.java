package com.yucheng.cmis.platform.permission;

/**
 * <p>
 * 	permission模块的常量类
 * <p>
 * 
 * @author yuhq
 * @version 1.0
 */
public class PermissionContents {

	/**
	 * 系统资源组件ID
	 */
	public static final String RESOURCE_COMPONENT_ID="cmisResource";
	
	/**
	 * 角色资源权限组件
	 */
	public static final String ROLE_PERMISSION_COMPONENT_ID = "cmisRolePermisson";
	/*
	 * 超级管理员
	 */
	public static final String CMIS_SUPER_ADMIN="admin";
	
	/**
	 *记录集权限类型 
	 *
	 **/
	public static final String READ = "READ"; //只读
	public static final String WRITE = "WRITE"; //可写
}
