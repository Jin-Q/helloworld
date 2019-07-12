package com.yucheng.cmis.platform.msiview.initializer;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.BusinessInitializer;
import com.yucheng.cmis.platform.msiview.ViewAllModualServices;
import com.yucheng.cmis.pub.CMISConstant;
import com.yucheng.cmis.pub.util.CMISPropertyManager;

/**
 * 系统启动时，加载MSI服务信息,解析各模块注册的服务并保证到数据中
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
public class MsiviewInitializer implements BusinessInitializer{

	public void initialize(Context rootCtx, Connection connection)throws Exception {
		
		ViewAllModualServices ms = new ViewAllModualServices();
		String projectPath = CMISPropertyManager.getInstance().getCmisProjectPath();
		
		EMPLog.log(CMISConstant.CMIS_INITIALIZER, EMPLog.DEBUG, 0, "msiview模块初始化操作：解析各模块注册的服务接口...");
		//加载MSI服务到数据库
		ms.loadMSI2DataBase(projectPath+"/src/main/java/com/yucheng/", connection);
		
	}

}
