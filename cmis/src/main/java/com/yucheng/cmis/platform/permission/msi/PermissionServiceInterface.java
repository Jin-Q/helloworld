package com.yucheng.cmis.platform.permission.msi;

import java.sql.Connection;

import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * 
 * <p>
 * 	资源权限模块对外提供的服务接口
 * </P
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 */
@ModualService(serviceId="permissionServices",serviceDesc="资源权限模块对外提供的服务接口",
				modualId="permission",modualName="资源权限模块",className="com.yucheng.cmis.platform.permission.msi.PermissionServiceInterface")
public interface PermissionServiceInterface {

	/**
	 * <p>
	 *  生成用户的资源权限文件
	 * </p>
	 * 
	 * @param actorno 用户号
	 * @param con 数据库连接
	 * @throws Exception 
	 */
	@MethodService(method="generatePermissionFile",desc="生成用户的资源权限文件")
	public void generatePermissionFile(String actorNo, Connection con) throws Exception;
	
}
