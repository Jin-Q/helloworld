package com.yucheng.cmis.pub;

/**
 * <p>信贷开发平台常量类</p>
 * <p>描述：该类主要用于信贷开发平台常量的维护，项目实施过程中尽量不要修改该类</p>
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
public class CMISConstant {
	
	/**
	 * 信贷业务开发平台模块初始化机制
	 */
	public final static String CMIS_INITIALIZER = "CMIS_MOD_INIT";
	
	/**
	 * <p>使用档板</p>
	 * <p>描述：在调用模块服务时，动态的决定是调用档板服务还是模块实现的服务</p>
	 */
	public final static String MODUAL_CONFIG_TYPE_01="1";
	
	/**
	 * <p>不使用档板</p>
	 * <p>描述：在调用模块服务时，动态的决定是调用档板服务还是模块实现的服务</p>
	 */
	public final static String MODUAL_CONFIG_TYPE_02="2";
	
	
	/**
	 * <p>模块服务后缀</p>
	 * <p>描述：在使用档板时，在标准的服务ID后加上后缀，以便系统能准备定位到档板服务</p>
	 */
	public final static String BAFFLE_SUFFIX = "Baffle";

	/**
	 * <p>页面服务Action</p>
	 * <p>描述：在调用页面服务时统一通过该Action中转，以便调用档板还是实现的服务</p>
	 */
	public final static String JSP_SERVICE_ID = "getModualJSPService.do";
	
	/**
	 * <p>页面服务Action中模块ID的参数名</p>
	 * <p>描述：在调用页面服务时统一通过该Action中转，该常量是模块ID的参数名， 如:getModualJSPService.do?modualId=xxxxx</p>
	 */
	public final static String JSP_SERVICE_MODUAL_PARAM_NAME = "modualId";
	
	/**
	 * <p>资源权限管理模块ID</p>
	 */
	public final static String PERMISSION_MODUAL_ID = "permission";
	/**
	 * <p>组织机构管理模块ID</p>
	 */
	public final static String ORGANIZATION_MODUAL_ID = "organization";
	
}
