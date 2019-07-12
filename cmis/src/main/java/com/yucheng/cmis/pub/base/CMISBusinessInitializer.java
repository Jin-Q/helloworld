package com.yucheng.cmis.pub.base;

import java.net.URL;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ecc.emp.core.Context;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.BusinessInitializer;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.dao.config.SqlConfigLoader;
import com.yucheng.cmis.pub.CMISAgentFactory;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISConstant;
import com.yucheng.cmis.pub.CMISDaoFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.FNCFactory;
import com.yucheng.cmis.pub.FNCItemsFactory;
import com.yucheng.cmis.pub.util.SInfoFactory;
import com.yucheng.cmis.pub.util.CMISPropertyManager;
import com.yucheng.cmis.pub.util.ResourceUtils;
import com.yucheng.cmis.pub.util.SetSysInfo;
import com.yucheng.cmis.pub.util.XMLFileUtil;


/**
 * 
 * 信贷业务开发平台对各模块的初始化操作统一管理
 * 
 * <p>
 * 		废弃以前的初始化操作的硬编码方式，各模块根据需要可以配置自己的初始化操作。<br>
 * </p>
 * 
 * @author yuhq
 * @version 1.0
 *
 */
public class CMISBusinessInitializer implements BusinessInitializer {

   //private static final Logger log = Logger.getLogger(CMISBusinessInitializer.class);

	/**
	 * 解析各模块的初始化配置文件，并执行各模块配置的初始化类
	 * <p>
	 * 	注：各模块的初始化类需要实现BusinessInitializer
	 * @param rootCtx
	 * @param connection
	 * @throws Exception 
	 */
	private void loadModualInitializerCfg(Context rootCtx, Connection connection) throws Exception{
		//配置文件根目录
		String dir = CMISPropertyManager.getInstance().getModualInitializerConfigFileDir();
		dir = ResourceUtils.getFile(dir).getAbsolutePath();
		
		XMLFileUtil xmlFile = new XMLFileUtil();
		Map<String,String> classMap = xmlFile.loadModualInitializerCfgFiles(dir);
		
		EMPLog.log(CMISConstant.CMIS_INITIALIZER, EMPLog.DEBUG, 0, "模块初始化机制被调用,开始解析并执行各模块的初始化操作...");
		
		for (Iterator it = classMap.keySet().iterator(); it.hasNext();) {
			String className = (String) it.next();
			String moudalName = classMap.get(className);
			try {
				BusinessInitializer obj = (BusinessInitializer)Class.forName(className).newInstance();
				obj.initialize(rootCtx, connection);
				EMPLog.log(CMISConstant.CMIS_INITIALIZER, EMPLog.DEBUG, 0, "执行"+moudalName+"的初始化类："+className+" 成功!");
			} catch (Exception e) {
				EMPLog.log(CMISConstant.CMIS_INITIALIZER, EMPLog.ERROR, 0, "执行"+moudalName+"的初始化类："+className+" 失败!",e);
			}
		}
	}
   
	
	
	public void initialize(Context rootCtx, Connection connection) throws Exception {

        try {
        	//系统权限文件的路径，这里初始化，其它地方通过CMISConstance.PERMISSIONFILE_PATH来访问
        	ResourceBundle res = ResourceBundle.getBundle("cmis");
            String FileRootPath = res.getString("permission.file.path");
            if(FileRootPath != null && FileRootPath.toLowerCase().startsWith("classpath:")) {
    	        URL url =  Thread.currentThread().getContextClassLoader().getResource("");
    	        CMISConstance.PERMISSIONFILE_PATH = url.getPath().replace("/classes", "")+FileRootPath.replace("classpath:", "");
            }else{
            	CMISConstance.PERMISSIONFILE_PATH = FileRootPath;
            }
    		//加载SQL配置
    		SqlConfigLoader sc = new SqlConfigLoader();
    		sc.loadSqlConfig();
        	
        	//组件配置初始化
            CMISComponentFactory.init();
    		CMISAgentFactory.init();
    		CMISDaoFactory.init();
    		CMISModualServiceFactory.init();
    		
    		//执行各模块配置的初始化类
    		this.loadModualInitializerCfg(rootCtx, connection);
    		
    		
    		
    	    		//指标选项初始化
//    		IndexInit.init(rootCtx, connection);
    		//设置系统当前日期 
    		SetSysInfo.init(rootCtx, connection);
    		
    		//初始化财报
    		FNCFactory.init(rootCtx, connection);
    		FNCItemsFactory.init(rootCtx, connection);
//    		//财务分析描述数据
//    		FnaAnlyConfigFactory.init(rootCtx, connection);
//    		//财报分析样式
//    		FnaTmptDefFactory.init(rootCtx, connection);
//    		
//    		//行业指标
//    		IndustryStdRelFactory.init(rootCtx, connection);
//    		//系统信息加载
    		SInfoFactory.init(rootCtx, connection);
    		
    		
        } catch (MissingResourceException e) {
            throw new IllegalStateException("cmis.properties配置出错！", e);
        }
    }

}
