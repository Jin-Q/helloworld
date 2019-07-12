package com.yucheng.cmis.pub;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import com.yucheng.cmis.pub.util.CMISPropertyManager;
import com.yucheng.cmis.pub.util.ResourceUtils;
import com.yucheng.cmis.pub.util.XMLFileUtil;

/**
 * <p>
 * 	模块、插件对外提供服务统一管理类
 * 
 * 	<ul>
 * 		描述：
 * 			<li>统一管理各模块、插件对外提供的服务</li>
 * 			<li>能动态的决定是调用档板类服务还是模块实现类服务</li>
 * 			<li>能展示所有注册的服务</li>
 * 			<li>记录所有调用模块以便知道模块之间的依赖关系，以方便的知道在后续的模块升级改造时对系统的影响</li>
 * 	</ul>
 * </p>
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
public class CMISModualServiceFactory {

	private static CMISModualServiceFactory instance = new CMISModualServiceFactory();
	private static Map modualServiceMap;
	public final static String ID = "id";
	public final static String DESCRIBE = "describe";
	public final static String COMPROPERTY = "comproperty";
	public final static String CLASSNAME = "classname";
	public final static String MODUALNAME="modualName";
	
	//私有的构造方法
	private CMISModualServiceFactory(){}
	
	/**
	 * 获得模块服务工厂实例
	 * @return
	 */
	public static CMISModualServiceFactory getInstance(){
		return instance;
	}
	
	/**
	 * 	解析配置文件需要注册的服务
	 * <p>
	 * 	在系统启动的时候被调用
	 * </p>
	 * 
	 * @throws Exception
	 */
	public static void init() throws Exception{
		XMLFileUtil xmlFileUtil = new XMLFileUtil();
		//ResourceBundle res = ResourceBundle.getBundle("cmis");
		String dir = CMISPropertyManager.getInstance().getModualserviceConfigFileDir();
		String CONFIG_FILM_DIR = ResourceUtils.getFile(dir).getAbsolutePath();
		modualServiceMap = (HashMap) xmlFileUtil.readModualServiceFromXMLFile(CONFIG_FILM_DIR);
	}
	
	/**
	 * 取实际服务类
	 * 
	 * @param serviceId 服务ID
	 * @param invokeModualName 调用模块名称
	 * 
	 * @retun CMISModualService 服务提供者
	 */
	private CMISModualService loadModualService(String serviceId, String modualId) throws Exception {
		CMISModualService service = null;
		if(modualServiceMap==null || modualServiceMap.size()==0)
			return null;
		
		if(serviceId==null || serviceId.length()<=0)
			return null;
		
		Map configMap = (Map)modualServiceMap.get(serviceId);
		if(configMap!=null && configMap.size()>0){
			String id = (String)configMap.get(ID);
			String className = (String)configMap.get(CLASSNAME);
			String describe = (String)configMap.get(DESCRIBE);
			String comproperty = (String)configMap.get(COMPROPERTY);
			String modualName = (String)configMap.get(MODUALNAME);
			
			service = (CMISModualService)Class.forName(className).newInstance();
			service.setModualName(modualName);
			service.setId(id);
			service.setDescribe(describe);
			service.setComproperty(comproperty);
			service.setClassName(className);
		} else
			throw new Exception("服务ID为："+serviceId+"的服务未注册！请在src/main/com/yucheng/cmis/config/下注册");
		
		return service;
	}
	
	/**
	 * <p>
	 * 	根据服务ID取得服务
	 * 	
	 * 	<ul>
	 * 		逻辑：
	 * 			<li>根据cmis.properties是否需要取档板实现类,档板命名是serviceId+Baffle</li>
	 * 			<li>返回该模块的MSI实现</li>
	 * 	</ul>
	 * 
	 * </p>
	 * 
	 * @param serviceId 服务ID
	 * @param modualId 模块ID
	 * @return CMISModualService 服务实例
	 */
	public CMISModualService getModualServiceById(String serviceId, String modualId) throws Exception {

		if (modualServiceMap == null || modualServiceMap.size() <= 0) {
			throw new Exception("Modual Service服务加载失败！");
		}
		
		//取配置信息，是否使用档板
		String value = CMISConstant.MODUAL_CONFIG_TYPE_02;
		try {
			value = CMISPropertyManager.getInstance().getPropertyValue(modualId);
		} catch (MissingResourceException e) {
			System.err.println(modualId+"模块未设置是否使用档板，默认不使用");
		}
		
		//使用档板服务，在标准的服务ID后加上后缀，以便系统能准备定位到档板服务
		if(value!=null && value.equals(CMISConstant.MODUAL_CONFIG_TYPE_01)){
			return loadModualService(serviceId+CMISConstant.BAFFLE_SUFFIX, modualId);
		}
		else{
			return loadModualService(serviceId, modualId);
		}
	}
	
}
