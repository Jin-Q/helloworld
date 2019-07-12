package com.yucheng.cmis.platform.userparam;

/**
 * <p>
 * <h2>简述</h2>
 *      <ol>用户个人参数管理模块常量类</ol>
 * <h2>功能描述</h2>
 *      <ol>无.</ol>
 * <h2>修改历史</h2>
 *      <ol>无.</ol>
 * </p>
 * @author liuhw
 * @time 2014-02-12
 * @version 1.0
 */

public class UserparamConstance {

	/** 用户个人参数管理模块ID */
	public static final String MODULEID = "userparamModule";

	/** 用户个人参数管理模块服务ID */
	public static final String MODULE_SERVICEID = "userparamService";

	/** 用户布局设置存放在context中的名称 */
	public static final String ATTR_USERPARAM_LAYOUT = "userparamLayout";

	/** 用户肤色设置存放在context中的名称 */
	public static final String ATTR_USERPARAM_SKIN = "userparamSkin";

	/** 用户肤色设置存放在context中的名称 */
	public static final String ATTR_USERPARAM_BG_IMAGE = "userparamBgImage";

	/** 用户肤色设置存放在context中的名称 */
	public static final String ATTR_USERPARAM_RESOLUTION = "userparamResolution";

	/** 用户管理密码最长必须变更天数（默认90天） */
	public static final int PWD_OVERDUE_DAYS = 90;
	/**
	* 用户语言组件注册服务ID
	*/
	public static final String S_USR_LANG_ID = "sUsrLangComponent";

	/**
	 * 私有构造器 
	 */
	private UserparamConstance() {
		super();
	}

}
