package com.yucheng.cmis.platform.pluginmanager.domain;

/**
 * 插件注册信息VO
 * 
 * @time 2013-4-19
 * @author yuhq
 * @version 3.0
 * @since 3.0
 *
 */
public class PluginRegVO {

	private String pluginModualId = null;
	private String pluginModualName = null;
	private String resJavaPath = null;
	private String resJspPath = null;
	private String resTablePath = null;
	private String resActionPath = null;
	private String resServicePath = null;
	private String resInitializerPath = null;
	private String resSqlPath = null;
	private String resJsPath = null;
	private String resImagPath = null;
	private String resCssPath = null;
	private String resLibPath = null;
	private String resExtPath = null;
	private String dbCreateTableName = null;
	private String dbInitDataName = null;
	private String resourcePath = null;
	private String dbPath = null;
	private String dbUninstallPath = null;
	private String pluginVersion = null;
	private String installDate = null;
	private String pluginStatus = null;
	private String pluginMemo = null;
	
	/**
	 * 插件ＩＤ
	 * @return　插件ＩＤ
	 */
	public String getPluginModualId() {
		return pluginModualId;
	}

	/**
	 * 插件ＩＤ
	 * @param id 插件ＩＤ
	 */
	public void setPluginModualId(String pluginModualId) {
		this.pluginModualId = pluginModualId;
	}

	/**
	 * 插件名称
	 * @return　插件名称
	 */
	public String getPluginModualName() {
		return pluginModualName;
	}
	
	/**
	 * 插件名称
	 * @param pluginModualName 插件名称
	 */
	public void setPluginModualName(String pluginModualName) {
		this.pluginModualName = pluginModualName;
	}
	
	/**
	 * java文件相对路径
	 * 到pluginModualName即可,如：resource/src/main/java/com/yucheng/cmis/platform/base/organization
	 * @return　java文件相对路径
	 */
	public String getResJavaPath() {
		return resJavaPath;
	}
	
	/**
	 * java文件相对路径
	 * 到pluginModualName即可,如：resource/src/main/java/com/yucheng/cmis/platform/base/organization
	 * @param resJavaPath java文件相对路径
	 */
	public void setResJavaPath(String resJavaPath) {
		this.resJavaPath = resJavaPath;
	}
	
	/**
	 * jsp文件相对路径，到pluginModualName即可
	 * @return jsp文件相对路径 
	 */
	public String getResJspPath() {
		return resJspPath;
	}
	
	/**
	 * jsp文件相对路径，到pluginModualName即可
	 * @param resJspPath jsp文件相对路径
	 */
	public void setResJspPath(String resJspPath) {
		this.resJspPath = resJspPath;
	}
	
	/**
	 * table文件相对路径，到pluginModualName即可
	 * @return table文件相对路径
	 */
	public String getResTablePath() {
		return resTablePath;
	}
	
	/**
	 * table文件相对路径，到pluginModualName即可
	 * @param resTablePath table文件相对路径
	 */
	public void setResTablePath(String resTablePath) {
		this.resTablePath = resTablePath;
	}
	
	/**
	 * action文件相对路径，到pluginModualName即可
	 * @return action文件相对路径 
	 */
	public String getResActionPath() {
		return resActionPath;
	}
	
	/**
	 * action文件相对路径，到pluginModualName即可
	 * @param resActionPath action文件相对路径 
	 */
	public void setResActionPath(String resActionPath) {
		this.resActionPath = resActionPath;
	}
	
	/**
	 * 建表语句的相对路径＋名称，如database/Oracle/create_table.sql
	 * @return 建表语句的相对路径＋名称
	 */
	public String getDbCreateTableName() {
		return dbCreateTableName;
	}
	
	/**
	 * 建表语句的相对路径＋名称，如database/Oracle/create_table.sql
	 * @param dbCreateTableName 建表语句的相对路径＋名称
	 */
	public void setDbCreateTableName(String dbCreateTableName) {
		this.dbCreateTableName = dbCreateTableName;
	}
	
	/**
	 * 初始化数据相对路径+名称，如database/Oracle/init_sql.sql
	 * @return　初始化数据相对路径+名称
	 */
	public String getDbInitDataName() {
		return dbInitDataName;
	}
	
	/**
	 * 初始化数据相对路径+名称
	 * @param dbInitDataName 初始化数据相对路径+名称
	 */
	public void setDbInitDataName(String dbInitDataName) {
		this.dbInitDataName = dbInitDataName;
	}

	/**
	 * 工程文件相对路径，包启src和WebContent的相对路径
	 * @return　工程文件相对路径
	 */
	public String getResourcePath() {
		return resourcePath;
	}

	/**
	 * 工程文件相对路径,包启src和WebContent的相对路径
	 * @param resourcePath 工程文件相对路径
	 */
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	
	/**
	 * 服务注册文件路径
	 * @return 服务注册文件路径
	 */
	public String getResServicePath() {
		return resServicePath;
	}

	/**
	 * 服务注册文件路径
	 * @param resServicePath 服务注册文件路径
	 */
	public void setResServicePath(String resServicePath) {
		this.resServicePath = resServicePath;
	}
	
	/**
	 * 模块初始化配置文件
	 * @return 模块初始化配置文件
	 */
	public String getResInitializerPath() {
		return resInitializerPath;
	}

	/**
	 * 模块初始化配置文件
	 * @param resInitializerPath 模块初始化配置文件
	 */
	public void setResInitializerPath(String resInitializerPath) {
		this.resInitializerPath = resInitializerPath;
	}

	/**
	 * 命名SQL配置文件路径
	 * @return 命名SQL配置文件路径
	 */
	public String getResSqlPath() {
		return resSqlPath;
	}

	/**
	 * 命名SQL配置文件路径
	 * @param resSqlPath 命名SQL配置文件路径
	 */
	public void setResSqlPath(String resSqlPath) {
		this.resSqlPath = resSqlPath;
	}
	
	/**
	 * JS文件路径
	 * @return JS文件路径
	 */
	public String getResJsPath() {
		return resJsPath;
	}
	
	/**
	 * JS文件路径
	 * @param resJsPath JS文件路径
	 */
	public void setResJsPath(String resJsPath) {
		this.resJsPath = resJsPath;
	}

	/**
	 * Image文件路径
	 * @return Image文件路径
	 */
	public String getResImagPath() {
		return resImagPath;
	}
	
	/**
	 * Image文件路径
	 * @param resImagPath Image文件路径
	 */
	public void setResImagPath(String resImagPath) {
		this.resImagPath = resImagPath;
	}

	/**
	 * CSS样式文件路径
	 * @return CSS样式文件路径
	 */
	public String getResCssPath() {
		return resCssPath;
	}

	/**
	 * CSS样式文件路径 
	 * @param resCssPath CSS样式文件路径
	 */
	public void setResCssPath(String resCssPath) {
		this.resCssPath = resCssPath;
	}

	/**
	 * 插件依赖JAR包路径
	 * @return  插件依赖JAR包路径
	 */
	public String getResLibPath() {
		return resLibPath;
	}

	/**
	 * 插件依赖JAR包路径
	 * @param resLibPath 插件依赖JAR包路径
	 */
	public void setResLibPath(String resLibPath) {
		this.resLibPath = resLibPath;
	}
	
	/**
	 *  扩展文件或文件夹路径
	 * @return 扩展文件或文件夹路径
	 */
	public String getResExtPath() {
		return resExtPath;
	}

	/**
	 * 扩展文件或文件夹路径
	 * @param resExtPath 扩展文件或文件夹路径
	 */
	public void setResExtPath(String resExtPath) {
		this.resExtPath = resExtPath;
	}

	/**
	 * 数据库文件相对路径
	 * @return　数据库文件相对路径
	 */
	public String getDbPath() {
		return dbPath;
	}

	/**
	 * 数据库文件相对路径
	 * @param dbPath 数据库文件相对路径
	 */
	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	/**
	 * 卸载数据文件相对路径
	 * @return　卸载数据文件相对路径
	 */
	public String getDbUninstallPath() {
		return dbUninstallPath;
	}
	
	/**
	 * 卸载数据文件相对路径
	 * @param dbUninstallPath 卸载数据文件相对路径
	 */
	public void setDbUninstallPath(String dbUninstallPath) {
		this.dbUninstallPath = dbUninstallPath;
	}

	/**
	 * 插件版本
	 * @return 插件版本
	 */
	public String getPluginVersion() {
		return pluginVersion;
	}

	/**
	 * 插件版本
	 * @param pluginVersion 插件版本
	 */
	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	/**
	 * 安装时间
	 * @return 安装时间
	 */
	public String getInstallDate() {
		return installDate;
	}

	/**
	 * 安装时间
	 * @param installDate 安装时间
	 */
	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}
	
	/**
	 * 插件状态
	 * @return 插件状态
	 */
	public String getPluginStatus() {
		return pluginStatus;
	}

	/**
	 * 插件状态
	 * @param pluginStatus 插件状态
	 */
	public void setPluginStatus(String pluginStatus) {
		this.pluginStatus = pluginStatus;
	}

	/**
	 * 插件安装备忘 
	 * @return 插件安装备忘 
	 */
	public String getPluginMemo() {
		return pluginMemo;
	}

	/**
	 * 插件安装备忘 
	 * @param pluginMemo 插件安装备忘 
	 */
	public void setPluginMemo(String pluginMemo) {
		this.pluginMemo = pluginMemo;
	}
	
	
	
}
