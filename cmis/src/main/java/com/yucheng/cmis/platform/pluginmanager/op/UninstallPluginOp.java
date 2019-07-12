package com.yucheng.cmis.platform.pluginmanager.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.permission.msi.PermissionServiceInterface;
import com.yucheng.cmis.platform.pluginmanager.PluginFileManager;
import com.yucheng.cmis.platform.pluginmanager.domain.PluginRegVO;
import com.yucheng.cmis.platform.pluginmanager.exception.PluginException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.exception.AsynException;
import com.yucheng.cmis.pub.util.CMISPropertyManager;

/**
 * 卸载插件OP
 * @author yuhq
 *
 */
public class UninstallPluginOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			//工程的绝对路径
			String projectPath = CMISPropertyManager.getInstance().getCmisProjectPath();
			
			//插件包目录
			String pluginId = (String)context.getDataValue("plugin_modual_id");
			PluginFileManager plugLoad = new PluginFileManager(projectPath, null);
			//卸载指定插件
			PluginRegVO vo = plugLoad.uninstallPlugIn(pluginId, connection);
			
			context.addDataField("flag", "success");
			context.addDataField("message", "插件"+vo.getPluginModualName()+"卸载成功");
			
			//生成用户权限文件
			this.generatePermisFIle(context, connection, "admin");
			
		}catch (PluginException e) {
			throw new AsynException("卸载失败!\n"+e.getMessage().substring("com.yucheng.cmis.platform.pluginmanager.exception.PluginException".length()+1));
		}catch (EMPException ee) {
			throw new AsynException("卸载失败!\n"+ee.getMessage());
		} catch(Exception e){
			throw new AsynException("卸载失败!\n"+e.getMessage());
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

	
	/**
	 * 生成用户权限文件
	 * @param context
	 * @param connection
	 * @param actorNo
	 * @throws EMPException
	 */
	public void generatePermisFIle(Context context, Connection connection, String actorNo) throws EMPException{
		try {
			//调用资愿权限模块对外提供生成的用户权限文件服务
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PermissionServiceInterface perSer =  (PermissionServiceInterface)serviceJndi.getModualServiceById("permissionServices", CMISConstance.PERMISSION_MODUAL_ID);
			perSer.generatePermissionFile(actorNo, connection);
		
		} catch (Exception e) {
			throw new EMPException(e);
		}
	}
	
}
