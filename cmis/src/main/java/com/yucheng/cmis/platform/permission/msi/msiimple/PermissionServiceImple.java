package com.yucheng.cmis.platform.permission.msi.msiimple;

import java.sql.Connection;

import com.yucheng.cmis.platform.permission.msi.PermissionServiceInterface;
import com.yucheng.cmis.platform.permission.resource.component.CMISRolePermissionComponent;
import com.yucheng.cmis.pub.CMISModualService;

/**
 * <p>
 * 	资源权限模块对外提供服务实现类
 * </p>
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
public class PermissionServiceImple extends CMISModualService implements PermissionServiceInterface{

	/**
	 * <p>
	 *  生成用户的资源权限文件
	 * </p>
	 * 
	 * @param actorno 用户号
	 * @param con 数据库连接
	 */
	public void generatePermissionFile(String actorNo, Connection con) throws Exception {
		CMISRolePermissionComponent service = new CMISRolePermissionComponent();
		service.generatePermissionFile(actorNo, con);
		
	}
	
}
