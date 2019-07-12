package com.yucheng.cmis.platform.pluginmanager.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.pluginmanager.PluginFileManager;
import com.yucheng.cmis.pub.util.CMISPropertyManager;

/**
 * <pre>
 * 	Title:导出插件OP
 * </pre>
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
public class ExportPluginOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			//工程的绝对路径
			String projectPath = CMISPropertyManager.getInstance().getCmisProjectPath();
			//插件包目录
			String exportPath = (String)context.getDataValue("export_path");
			//插件ID
			String pluginModualId = (String)context.getDataValue("plugin_modual_id");
			
			PluginFileManager plugManager = new PluginFileManager(projectPath, null);
			//导出插件
			boolean flag = plugManager.exportPlugin(pluginModualId, exportPath);
			
			if(flag){
				context.addDataField("flag", "success");
				context.addDataField("message", "插件["+pluginModualId+"]导出成功!");
			}else{
				context.addDataField("flag", "error");
				context.addDataField("message", "插件["+pluginModualId+"]导出失败!");
			}
			
		}catch (EMPException ee) {
			ee.printStackTrace();
			context.addDataField("flag", "error");
			context.addDataField("message", "插件导出失败!");
		} catch(Exception e){
			e.printStackTrace();
			context.addDataField("flag", "error");
			context.addDataField("message", "插件导出失败!");
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
