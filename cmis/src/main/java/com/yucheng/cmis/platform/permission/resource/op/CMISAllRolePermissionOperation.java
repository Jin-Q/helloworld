package com.yucheng.cmis.platform.permission.resource.op;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.permission.PermissionContents;
import com.yucheng.cmis.platform.permission.resource.component.CMISRolePermissionComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISConstant;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>
 * 	生成所有用户的资源权限文件
 * 	<ul>
 * 		备注：修改为通过接口的方式来访问用户信息
 * </ul>
 * </p>
 * @author yuhq
 *
 */
public class CMISAllRolePermissionOperation extends CMISOperation {
	
	private String op;

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			CMISRolePermissionComponent service = (CMISRolePermissionComponent)CMISComponentFactory.getComponentFactoryInstance()
														.getComponentInstance(PermissionContents.ROLE_PERMISSION_COMPONENT_ID, context, connection);
			
			//调用组织机构管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			OrganizationServiceInterface orgService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", CMISConstant.ORGANIZATION_MODUAL_ID);
			List<SUser> userList = orgService.getAllUsers(connection);
			
			//逐个生成资源文件
			for (int i = 0; i < userList.size(); i++) {
				String actorno = userList.get(i).getActorno();
				service.generatePermissionFile(actorno, connection);
			}
		} catch (EMPException e) {
			throw e;
		} catch(Exception e1){
			throw new EMPException(e1);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

	public void initialize() {
		// TODO Auto-generated method stub

	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}
}
