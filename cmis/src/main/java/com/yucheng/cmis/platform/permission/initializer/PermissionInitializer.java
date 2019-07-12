package com.yucheng.cmis.platform.permission.initializer;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.BusinessInitializer;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.permission.domain.SRowright;
import com.yucheng.cmis.platform.permission.resource.component.SInfoQuery;
import com.yucheng.cmis.pub.CMISConstant;
import com.yucheng.cmis.pub.dao.SqlClient;

/**
 * 组织机构在系统加载时需要初始化的信息类
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
public class PermissionInitializer implements BusinessInitializer{
	public static Map<String, SRowright> SROWRIGHTMAP = null; //s_rowright记录集权限
	public static boolean isInit = false;
	
	/**
	 * 加载所有组织机构的ID-NAME信息
	 */
	public void initialize(Context rootCtx, Connection connection)
			throws Exception {
		
		EMPLog.log(CMISConstant.CMIS_INITIALIZER, EMPLog.DEBUG, 0, "permission模块初始化操作：记录集权限缓存信息...");
		
		SInfoQuery SInfoQuery = new SInfoQuery();
		SROWRIGHTMAP =SInfoQuery.initSrowright(rootCtx, connection);
		
		isInit = true;
	}

}
