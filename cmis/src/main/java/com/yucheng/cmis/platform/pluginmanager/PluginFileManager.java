package com.yucheng.cmis.platform.pluginmanager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yucheng.cmis.platform.pluginmanager.domain.PluginRegVO;
import com.yucheng.cmis.platform.pluginmanager.exception.PluginException;
import com.yucheng.cmis.platform.pluginmanager.util.FileUtil;
import com.yucheng.cmis.platform.pluginmanager.util.SQLParsing;
import com.yucheng.cmis.pub.util.CMISPropertyManager;

/**
 * <h3>装载插件包下工程文件类</h3>
 * <p>
 * 		1、考虑JAR包和接入第三方应用时一些文件位置的特殊要求（如：eChain.properties必需要放到src下的，studio必须要放到
 * 	WebContent下)JAR在加入时，添加校验，校验当前工程下是否有（按名称来校验，不校验版本），添加扩展字段：该字段放一特
 *  殊要求的文件目录，以';'分隔;
 *		 添加一个字段，记录在安装时的警告信息,如JAR包冲突等; 2013-7-11 10:50:45	
 * </p>	
 * 
 * <p>	2、卸载时备份工程文件 2013-8-20 10:01:50</p>
 * 
 * <p>
 * 		3、添加升级功能：升级时覆盖原工程文件并执行数据库脚本。TODO 升级时可能会删除原工程文件；升级时如果需要删除或是修改
 * 	前一版本的数据库表或数据，可以直接通过数据库脚本来完成。2013-8-29 10:17:58
 * </p>
 * 
 * 
 * @time 2013-04-19
 * @author yuhq
 * @version 1.0
 * @since 1.0
 */
public class PluginFileManager {
	private static final String MODUAL_NAME = "插件管理模块";
	private String projectPath = null;//项目工程目录
	private String pluginPath = null;//插件目录
	private String javaBasePath = "src/main/java/com/yucheng/cmis";//目路检索时使用
	private String serviceBasePath = "src/main/config/com/yucheng/cmis/config";
	private String sqlBasePath = "src/main/config/sql";
	private String jspBasePath = "WebContent/WEB-INF/mvcs/CMISMvc";
	private String actionBasePath = "WebContent/WEB-INF/mvcs/CMISMvc/actions";
	private String tableBasePath = "WebContent/WEB-INF/tables";
	private String initializerBasePath = "src/main/config/initializer";
	private String jsBasePath = "WebContent/scripts";
	private String imagBasePath = "WebContent/images";
	private String cssBasePath = "WebContent/styles";
	private String libBasePath = "WebContent/WEB-INF/lib";
	
	//SQL执行异常的时候，将SQL写入到文件中　TODO　该文件需要从配置文件中取
	private static final String PLUGIN_EXCEPTION_FILE = "/home/yuhq/workspace/plugin_exception_file.sql";
	
	private boolean uninstallPart = true;//是否部分卸载
	private String 	backSqlFolder = null;//备份卸载数据库脚本目录
	
	
	/**
	 * 构造方法
	 * @param projectPath　项目工程目录
	 * @param pluginPath　插件目录
	 */
	public PluginFileManager(String projectPath, String pluginPath){
		this.projectPath = projectPath;
		this.pluginPath = pluginPath;
		this.javaBasePath = this.projectPath+"/"+this.javaBasePath;
		this.serviceBasePath = this.projectPath+"/"+this.serviceBasePath;
		this.sqlBasePath = this.projectPath+"/"+this.sqlBasePath;
		this.actionBasePath = this.projectPath+"/"+this.actionBasePath;
		this.jspBasePath = this.projectPath+"/"+this.jspBasePath;
		this.tableBasePath = this.projectPath+"/"+this.tableBasePath;
		this.jsBasePath = this.projectPath+"/"+this.jsBasePath;
		this.imagBasePath = this.projectPath+"/"+this.imagBasePath;
		this.cssBasePath = this.projectPath+"/"+this.cssBasePath;
		this.libBasePath = this.projectPath+"/"+this.libBasePath;
		this.initializerBasePath = this.projectPath+"/"+this.initializerBasePath;
		
	}

	/**
	 * <p>
	 * 	安装插件
	 * 	<ul>
	 * 		逻辑：
	 * 			<li>校验插件包的完整性</li>
	 * 			<li>校验项目中是否已经安装过该插件</li>
	 * 			<li>插件注册</li>
	 * 			<li>安装插件之工程文件</li>
	 * 			<li>安装插件之插件依赖数据库</li>
	 * 			<li>更新安装信息到注册表中</li>
	 * 	<ul>
	 * </p>
	 *  
	 * @param forceLoad　true--强制安装插件；false--不强制安装插件
	 * @param con 数据库连接
	 * @return PluginRegVO 插件注册信息
	 */
	public PluginRegVO installPlugin(boolean forceLoad, Connection con)throws PluginException{
		try {
			
			
			//校验插件包的完整性
			boolean isIntact = this.checkPluginIntact();
			if(!isIntact)
				throw new PluginException("插件安装包不完整，安装中止!");
			
			//解析插件安装包下的注册文件
			PluginRegVO vo = this.parsPluginRegVOFromRegFile();
			
			//校验项目中是否已经安装过该插件
			boolean isExist = this.checkPluginExist(vo.getPluginModualId(), con);
			if(isExist && !forceLoad)
				throw new PluginException("项目中已经存在该插件，请先卸载再安装!");
				
			//插件注册
			this.regPluginInfo(vo, con);
			
			//重新解析一次，因在regPluginInfo方法中vo被修正了
			vo = this.parsPluginRegVOFromRegFile();
			
			//安装插件之工程文件
			String installMsg = this.installPluginResource(vo,forceLoad);
			
			//安装插件之插件依赖数据库
			this.installPluginDataBase(vo,forceLoad, con);
			
			//更新安装信息到注册表中
			this.updateInstallMsg(vo.getPluginModualId(), installMsg, false, con);
			
			return vo;
		} catch (Exception e) {
			throw new PluginException(e);
		}
		
	}
	
	/**
	 * <p>
	 * 	升级(更新)插件
	 * 	<ul>
	 * 		逻辑：
	 * 			<li>校验插件包的完整性</li>
	 * 			<li>校验是否已经安装过该插件</li>
	 * 			<li>安装插件之工程文件</li>
	 * 			<li>安装插件之插件依赖数据库</li>
	 * 			<li>更新插件注册表</li>
	 * 	<ul>
	 * </p>
	 * 
	 * @param con 数据库连接
	 * @return 插件注册信息
	 * @throws PluginException 异常
	 */
	public PluginRegVO updatePlugin(Connection con)throws PluginException{
		try {
			//校验插件包的完整性
			boolean isIntact = this.checkPluginIntact();
			if(!isIntact)
				throw new PluginException("插件安装包不完整，安装中止!");
			
			//解析插件安装包下的注册文件
			PluginRegVO vo = this.parsPluginRegVOFromRegFile();
			
			//校验项目中是否已经安装过该插件
			boolean isExist = this.checkPluginExist(vo.getPluginModualId(), con);
			if(!isExist)
				throw new PluginException("不存在该插件，升级操作中止!");
			
			//安装插件之工程文件,覆盖以前的工程文件
			String installMsg = this.installPluginResource(vo,true);
			
			//升级数据库
			this.installPluginDataBase(vo,true, con);
			
			//更新插件注册表
			upateRegPluginInfo(vo, con);
			
			//更新安装信息到注册表中
			this.updateInstallMsg(vo.getPluginModualId(), installMsg, false, con);
			
			return vo;
		} catch (Exception e) {
			throw new PluginException(e);
		}
	}
	
	/**
	 * <p>
	 * 	卸载插件
	 * 	<ul>
	 * 		逻辑：
	 * 			<li>删除插件的注删信息</li>
	 *  		<li>卸载插件之工程文件</li>
	 *   		<li>卸载插件之插件依赖数据库</li>
	 *   		<li>卸载时支持完全卸载：该插件所有代码都删除；部份卸载：除该插件的MSI、Domains、异常、档板外，其余所有代码都删除</li>
	 *   		<li>不卸载模块下的msi(msi下的msiimple会删除)、domain、exception、baffleplate、常量类：模块ID+Constance</li>
	 *   		<li>卸载前备份工程文件</li>
	 * 	</ul>
	 * 	注：暂时不考虑将卸载的插件备份，因为项目实际的开发过程肯定会有SVN，即使误操作还可以通过SVN恢复;
	 * 		JAR不卸载，因为JAR文件特殊的安装规则，使得JAR文件不可删除
	 * </p>
	 * 
	 * @parma pluginModualId 待卸载的插件ID
	 * @param con　数据连接
	 * @return PluginRegVO 插件注册信息
	 */
	public PluginRegVO uninstallPlugIn(String pluginModualId, Connection con)throws PluginException{
		try {
			
			
			//得到该模块的注册表信息
			PluginRegVO vo = this.parsPluginRegVOFromDB(pluginModualId, con);
			
			//备份工程文件到指定目录下，目录名为pluginid_yyyyMMddHHmmss
			this.backUpPlugin(vo);
			
			//删除插件的注册信息
			this.deletePlugInfo(vo, con);
			
			//卸载插件之插件依赖数据库
			this.uninstallPluginDataBase(vo, con);
			
			//卸载插件之工程文件,文件操作放在数据库操作之后，因为文件操作没有事务控制
			this.uninstallPluginResource(vo);
			
			
			return vo;
		} catch (Exception e) {
			throw new PluginException(e);
		}
	}
	
	/**
	 * <p>
	 * 	导出插件
	 *  目前只导出工程文件，不考虑数据库
	 * 	<ul>
	 * 		逻辑：
	 * 			<li>根据modualId在工程中自动检索java、jsp、action、js等相关文件的位置</li>
	 *  		<li>根据第一步检索的信息自动写插件安装包的注册文件</li>
	 *   		<li>导出文件到指定目录下</li>
	 * 	</ul>
	 * </p>
	 * @param pluginModualId 需要导出的模块
	 * @param path 导出的路径
	 * @return
	 * @throws PluginException
	 */
	public boolean exportPlugin(String pluginModualId,String path)throws PluginException{
		FileUtil fileUtil = FileUtil.getInstance();
		BufferedWriter write = null;
		String javaRelPath = null;//java相对路径
		String jspRelPath = null;//jsp相对路径
		String serviceRelPath = null;//服务注册文件相对路径
		String sqlRelPath = null;//命名SQL相对路径
		String actionRelPath = null;//action配置文件相对路径
		String tableRelPath = null;//table配置文件相对路径
		String initializerPath = null;//模块初始化配置文件相对路径
		String jsRelPath = null;//JS文件相对路径
		String imagRelPath = null;//IMAG文件相对路径
		String cssRelPath = null;//CSS文件相对路径
		try {
			//第一步：－－－－－－－－－－－－－－自动检索工程中该插件的所有文件目录，以目录为单位导出插件－－－－－－－－－－－－－－－
			PluginRegVO vo = autoSearchPlugin(pluginModualId);
			
			//第二步：－－－－－－－－－－－－－－导出工程文件到插件安装包下－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
			//校验path目录是否存在，如果存在，则将插件导到path+pluginModualId+yyyyMMddHHmmss下
			File file = new File(path);
			if(!file.isDirectory())
				throw new PluginException("插件导出失败，"+path+"不是一个目录！");
			
			String exportFolder = path+"/"+pluginModualId+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
			//创建导出插件目录
			file = new File(exportFolder);
			file.mkdir();

			//导出JAVA文件
			if(vo.getResJavaPath()!=null){
				javaRelPath = "resource/"+vo.getResJavaPath().substring(this.projectPath.length()+1);
				//JAVA文件实际导出目录
				String javaNewPath = exportFolder+"/"+javaRelPath;
				fileUtil.copyFolder(vo.getResJavaPath(), javaNewPath);
			}
				
			//JSP文件实际导出目录
			if(vo.getResJspPath()!=null){
				jspRelPath = "resource/"+vo.getResJspPath().substring(this.projectPath.length()+1);
				String jspNewPath = exportFolder+"/"+jspRelPath;
				//导出JSP文件
				fileUtil.copyFolder(vo.getResJspPath(), jspNewPath);
			}
			
			//导出Service配置文件
			if(vo.getResServicePath()!=null){
				serviceRelPath = "resource/"+vo.getResServicePath().substring(this.projectPath.length()+1);
				//Service配置文件实际导出目录
				String serviceNewPath = exportFolder+"/"+serviceRelPath;
				fileUtil.copyFolder(vo.getResServicePath(), serviceNewPath);
			}
			
			//导出命名SQL配置文件
			if(vo.getResSqlPath()!=null){
				sqlRelPath = "resource/"+vo.getResSqlPath().substring(this.projectPath.length()+1);
				//命名SQL配置文件实际导出目录
				String sqlNewPath = exportFolder+"/"+sqlRelPath;
				fileUtil.copyFolder(vo.getResSqlPath(), sqlNewPath);
			}
			
			//导出Action配置文件
			if(vo.getResActionPath()!=null){
				actionRelPath = "resource/"+vo.getResActionPath().substring(this.projectPath.length()+1);
				//Action配置文件实际导出目录
				String actionNewPath = exportFolder+"/"+actionRelPath;
				fileUtil.copyFolder(vo.getResActionPath(), actionNewPath);
			}
			
			//导出Jtable配置文件
			if(vo.getResTablePath()!=null){
				tableRelPath = "resource/"+vo.getResTablePath().substring(this.projectPath.length()+1);
				//table配置文件实际导出目录
				String tableNewPath = exportFolder+"/"+tableRelPath;
				fileUtil.copyFolder(vo.getResTablePath(), tableNewPath);
			}
			
			//导出模块初始化配置文件
			if(vo.getResInitializerPath()!=null){
				initializerPath = "resource/"+vo.getResInitializerPath().substring(this.projectPath.length()+1);
				//模块初始化配置文件实际导出目录
				String initializerNewPath = exportFolder+"/"+initializerPath;
				fileUtil.copyFolder(vo.getResInitializerPath(), initializerNewPath);
			}
			
			//导出JS文件
			if(vo.getResJsPath()!=null){
				jsRelPath = "resource/"+vo.getResJsPath().substring(this.projectPath.length()+1);
				//JS文件实际导出目录
				String jsNewPath = exportFolder+"/"+jsRelPath;
				fileUtil.copyFolder(vo.getResJsPath(), jsNewPath);
			}
			
			//导出Imag文件
			if(vo.getResImagPath()!=null){
				imagRelPath = "resource/"+vo.getResImagPath().substring(this.projectPath.length()+1);
				//Imag文件实际导出目录
				String imagNewPath = exportFolder+"/"+imagRelPath;
				fileUtil.copyFolder(vo.getResImagPath(), imagNewPath);
			}
			
			//导出css文件
			if(vo.getResCssPath()!=null){
				cssRelPath = "resource/"+vo.getResCssPath().substring(this.projectPath.length()+1);
				//css文件实际导出目录
				String cssNewPath = exportFolder+"/"+cssRelPath;
				fileUtil.copyFolder(vo.getResCssPath(), cssNewPath);
			}
			
			
			//第三步：－－－－－－－－－－－－－－生成插件包下的数据库目录和空的SQL脚本文件（目前不自动导出数据库脚本）－－－－－－－－－
			file = new File(exportFolder+"/database/Oracle");
			file.mkdirs();
			//缺省创建Oracle目录下的三个空文件
			file = new File(exportFolder+"/database/Oracle/create_table.sql");
			file.createNewFile();
			file = new File(exportFolder+"/database/Oracle/init_data.sql");
			file.createNewFile();
			file = new File(exportFolder+"/database/Oracle/uninstall_db.sql");
			file.createNewFile();
			file = new File(exportFolder+"/database/DB2");
			file.mkdirs();
			file = new File(exportFolder+"/database/Informix");
			file.mkdirs();
			
			
			//第四步：－－－－－－－－－－－－－－写注册文件－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
			//写注册文件 TODO 如果系统的注册表中有该插件的信息，可以直接读注册表信息写到这里。
			write = new BufferedWriter(new FileWriter(new File(exportFolder+"/ini_cmis_"+pluginModualId+".reg")));
			write.write("#这是"+pluginModualId+"的的注册表文件");
			write.newLine();
			write.write("#插件／模块名称");
			write.newLine();
			write.write("pluginModualId="+(pluginModualId==null?"":pluginModualId));
			write.newLine();
			write.write("pluginModualName="+(pluginModualId==null?"":pluginModualId));
			write.newLine();
			write.write("#插件版本");
			write.newLine();
			write.write("pluginVersion=V1.0-R01");
			write.newLine();
			write.newLine();
			write.write("#工程文件相对路径");
			write.newLine();
			write.write("resourcePath=resource");
			write.newLine();
			write.write("#数据库文件相对目录");
			write.newLine();
			write.write("dbPath=database");
			write.newLine();
			write.newLine();
			write.write("#java文件相对路径，到插件／模块名即可");
			write.newLine();
			write.write("resJavaPath="+(javaRelPath==null?"":javaRelPath));
			write.newLine();
			write.write("#jsp文件相对路径，到插件／模块名即可");
			write.newLine();
			write.write("resJspPath="+(jspRelPath==null?"":jspRelPath));
			write.newLine();
			write.write("#服务注册文件相对路径,到插件／模块名即可");
			write.newLine();
			write.write("resServicePath="+(serviceRelPath==null?"":serviceRelPath));
			write.newLine();
			write.write("#命名SQL配置文件相对路径，到插件／模块名即可");
			write.newLine();
			write.write("resSqlPath="+(sqlRelPath==null?"":sqlRelPath));
			write.newLine();
			write.write("#table文件相对路径，到插件／模块名即可");
			write.newLine();
			write.write("resTablePath="+(tableRelPath==null?"":tableRelPath));
			write.newLine();
			write.write("#action文件相对路径，到插件／模块名即可");
			write.newLine();
			write.write("resActionPath="+(actionRelPath==null?"":actionRelPath));
			write.newLine();
			write.write("#js文件相对路径,到插件／模块名即可");
			write.newLine();
			write.write("resJsPath="+(jsRelPath==null?"":jsRelPath));
			write.newLine();
			write.write("#imag文件相对路径,到插件／模块名即可");
			write.newLine();
			write.write("resImagPath="+(imagRelPath==null?"":imagRelPath));
			write.newLine();
			write.write("#css文件相对路径,到插件／模块名即可");
			write.newLine();
			write.write("resCssPath="+(cssRelPath==null?"":cssRelPath));
			write.newLine();
			write.write("#模块初始化配置文件相对路径,到插件／模块名即可");
			write.newLine();
			write.write("resInitializerPath="+(initializerPath==null?"":initializerPath));
			write.newLine();
			write.write("#jar文件相对路径,到插件／模块名即可");
			write.newLine();
			write.write("resLibPath=");
			write.newLine();
			write.write("#扩展文件相对路径,到插件／模块名即可");
			write.newLine();
			write.write("resExtPath=");
			write.newLine();
			write.newLine();
			write.write("#数据库文件路径");
			write.newLine();
			write.write("#建表语句相对路径");
			write.newLine();
			write.write("dbCreateTableName=database/Oracle/create_table.sql");
			write.newLine();
			write.write("#初始化数据相对路径");
			write.newLine();
			write.write("dbInitDataName=database/Oracle/init_data.sql");
			write.newLine();
			write.write("#卸载数据相对路径");
			write.newLine();
			write.write("dbUninstallSql=database/Oracle/uninstall_db.sql");
			
		} catch (PluginException e) {
			throw new PluginException(e);
		} catch (Exception e){
			throw new PluginException(e);
		} finally{
			if(write!=null)
				try {
					write.close();
				} catch (IOException e) {
					throw new PluginException(e);
				}
		}
		
		return true;
	}
	
	/**
	 * <p>
	 * 	自动检索pluginModualId插件依赖的工程文件目录，返回文件全路径
	 * </p>
	 *  
	 *  <ul>自动检索依赖于工程基础的目录结构规则，如JAVA文件放在com.yucheng.cmis下，JSP文件放在/WebContent/mvcs/CMISMvc/下</ul>
	 *  <ul>自动检索不会检索该插件依赖的JAR和扩展文件/文件夹，需要手工导出</ul>
	 *  
	 * 
	 * @param modualId 模块ID
	 * @return PluginRegVO
	 * @throws PluginException
	 */
	private PluginRegVO autoSearchPlugin(String pluginModualId)throws PluginException{
		PluginRegVO vo = new PluginRegVO();
		try {
			FileUtil fileUtil = FileUtil.getInstance();
			List<String> retList = new ArrayList<String>();
			//检索JAVA文件,java文件放在projectPath/src/java/com/yucheng/cmis/下,且不在bafflelate和msi下
			fileUtil.searchFolder(this.javaBasePath, pluginModualId, new String[]{"baffleplate","msi"}, retList);
			if(retList.size()>1)
				throw new PluginException("在"+javaBasePath+"目录下检索到多个目标目录"+pluginModualId+"，请检查");
			String javaPath = retList.size()==1?retList.get(0):null;
			
			//检索服务注册配置文件目录
			retList = new ArrayList<String>();
			fileUtil.searchFolder(this.serviceBasePath, pluginModualId, null, retList);
			if(retList.size()>1)
				throw new PluginException("在"+this.serviceBasePath+"目录下检索到多个目标目录"+pluginModualId+"，请检查");
			String servicePath = retList.size()==1?retList.get(0):null;
			
			//检索命名SQL配置文件目录
			retList = new ArrayList<String>();
			fileUtil.searchFolder(this.sqlBasePath, pluginModualId, null, retList);
			if(retList.size()>1)
				throw new PluginException("在"+this.sqlBasePath+"目录下检索到多个目标目录"+pluginModualId+"，请检查");
			String sqlPath = retList.size()==1?retList.get(0):null;
			
			//检索JSP文件目录
			retList = new ArrayList<String>();
			fileUtil.searchFolder(this.jspBasePath, pluginModualId, new String[]{"actions"}, retList);
			if(retList.size()>1)
				throw new PluginException("在"+this.jspBasePath+"目录下检索到多个目标目录"+pluginModualId+"，请检查");
			String jspPath = retList.size()==1?retList.get(0):null;
			
			//检索Action配置文件目录
			retList = new ArrayList<String>();
			fileUtil.searchFolder(this.actionBasePath, pluginModualId, null, retList);
			if(retList.size()>1)
				throw new PluginException("在"+this.actionBasePath+"目录下检索到多个目标目录"+pluginModualId+"，请检查");
			String actionPath = retList.size()==1?retList.get(0):null;
			
			//检索table配置文件目录
			retList = new ArrayList<String>();
			fileUtil.searchFolder(this.tableBasePath, pluginModualId, null, retList);
			if(retList.size()>1)
				throw new PluginException("在"+this.tableBasePath+"目录下检索到多个目标目录"+pluginModualId+"，请检查");
			String tablePath = retList.size()==1?retList.get(0):null;
			
			//检索模块初始化配置文件目录
			retList = new ArrayList<String>();
			fileUtil.searchFolder(this.initializerBasePath, pluginModualId, null, retList);
			if(retList.size()>1)
				throw new PluginException("在"+this.initializerBasePath+"目录下检索到多个目标目录"+pluginModualId+"，请检查");
			String initializerPath = retList.size()==1?retList.get(0):null;
			
			//检索JS文件目录
			retList = new ArrayList<String>();
			fileUtil.searchFolder(this.jsBasePath, pluginModualId, null, retList);
			if(retList.size()>1)
				throw new PluginException("在"+this.jsBasePath+"目录下检索到多个目标目录"+pluginModualId+"，请检查");
			String jsPath = retList.size()==1?retList.get(0):null;
			
			//检索imag文件目录
			retList = new ArrayList<String>();
			fileUtil.searchFolder(this.imagBasePath, pluginModualId, null, retList);
			if(retList.size()>1)
				throw new PluginException("在"+this.imagBasePath+"目录下检索到多个目标目录"+pluginModualId+"，请检查");
			String imagPath = retList.size()==1?retList.get(0):null;
			
			
			//检索css文件目录
			retList = new ArrayList<String>();
			fileUtil.searchFolder(this.cssBasePath, pluginModualId, null, retList);
			if(retList.size()>1)
				throw new PluginException("在"+this.cssBasePath+"目录下检索到多个目标目录"+pluginModualId+"，请检查");
			String cssPath = retList.size()==1?retList.get(0):null;
			
			vo.setResJavaPath(javaPath);
			vo.setResServicePath(servicePath);
			vo.setResSqlPath(sqlPath);
			vo.setResJspPath(jspPath);
			vo.setResActionPath(actionPath);
			vo.setResTablePath(tablePath);
			vo.setResInitializerPath(initializerPath);
			vo.setResJsPath(jsPath);
			vo.setResImagPath(imagPath);
			vo.setResCssPath(cssPath);
			
			
		} catch (Exception e) {
			throw new PluginException(e);
		}
		
		return vo;
	}
	
	
	
	/**
	 * <p>
	 * 	校验插件包的完整性
	 * 	<ul>逻辑：
	 * 		<li>校验插件包下是否有*.reg文件</li>
	 * 		<li>根据插件包下的*.reg文件中的信息来验证插件包是否完整</li>
	 * </p>
	 * @return true-插件包完整；false-插件包不完整
	 */
	protected boolean checkPluginIntact()throws PluginException{
		
		return true;
	}
	
	/**
	 * <p>
	 * 	校验项目中是否已经安装过该插件
	 * 	<ul>逻辑：
	 * 		<li>校验注册表是否已经存在</li>
	 * 	</ul>
	 * 　<ul>场景：该方法一般是在安装插件时被调用</ul>
	 * </p>
	 * 
	 * <p>
	 * 	注：注册表中不存在该插件，则认为未安装过该插件；不考虑工程是否已存在
	 * </p>
	 * 
	 * @param pluginModualId 插件ID
	 * @return true--存在; false--不存在
	 */
	protected boolean checkPluginExist(String pluginModualId, Connection con)throws PluginException{
		try {
			if(pluginModualId ==null || pluginModualId.trim().length()==0) 
				throw new PluginException("【校验项目中是否已经安装过该插件】步骤出错，插件ID为空...");
			
			PluginRegVO dbVO = parsPluginRegVOFromDB(pluginModualId, con);
			if(dbVO == null || dbVO.getPluginModualId()==null)
				return false;
			
			else
				return true;
			
		} catch (Exception e) {
			throw new PluginException("【校验项目中是否已经安装过该插件】步骤出错:"+e);
		}
	}
	
	/**
	 * <p>
	 * 	修正PluginRegVO，将安装包中的相对路径resourcePath修正掉
	 * </p>
	 * 
	 * <p>适用场景：安装或是升级插件时，从插件安装包中解析到的文件相对路径中含有插件包的信息：resourcePath</p>
	 * @param vo 插件注册信息VO
	 * @return 修正后插件注册信息VO
	 */
	private PluginRegVO fixPluginRegVO(PluginRegVO vo){
		//修改PluginRegVO中的路径，原始路径含插件目录内的相对路径，这里需要将其截除，变成相对工程项目的路径
		String resourcePath = vo.getResourcePath();
		
		//java文件相对路径修正
		String resJavaPath = vo.getResJavaPath();
		if(resJavaPath!=null && resJavaPath.length()>0){
			resJavaPath = resJavaPath.substring(resourcePath.length()+1);
			vo.setResJavaPath(resJavaPath);
		}
		
		//jsp文件相对路径修正
		String resJspPath = vo.getResJspPath();
		if(resJspPath!=null && resJspPath.length()>0){
			resJspPath = resJspPath.substring(resourcePath.length()+1);
			vo.setResJspPath(resJspPath);
		}
		
		//table文件相对路径修正
		String resTablePath = vo.getResTablePath();
		if(resTablePath!=null && resTablePath.length()>0){
			resTablePath = resTablePath.substring(resourcePath.length()+1);
			vo.setResTablePath(resTablePath);
		}
			
		
		//action文件相对路径修正
		String resActionPath = vo.getResActionPath();
		if(resActionPath!=null && resActionPath.length()>0){
			resActionPath = resActionPath.substring(resourcePath.length()+1);
			vo.setResActionPath(resActionPath);
		}
			
		
		//service文件相对路径修正
		String resServicePath = vo.getResServicePath();
		if(resServicePath!=null && resServicePath.length()>0){
			resServicePath = resServicePath.substring(resourcePath.length()+1);
			vo.setResServicePath(resServicePath);
		}
		
		//命名SQL文件相对路径修正
		String resSqlPath = vo.getResSqlPath();
		if(resSqlPath!=null && resSqlPath.length()>0){
			resSqlPath = resSqlPath.substring(resourcePath.length()+1);
			vo.setResSqlPath(resSqlPath);
		}
			
		
		//JS文件相对路径修正
		String resJsPath = vo.getResJsPath();
		if(resJsPath!=null && resJsPath.length()>0){
			resJsPath = resJsPath.substring(resourcePath.length()+1);
			vo.setResJsPath(resJsPath);
		}
			
		
		//imag文件相对路径修正
		String resImagPath = vo.getResImagPath();
		if(resImagPath!=null && resImagPath.length()>0){
			resImagPath = resImagPath.substring(resourcePath.length()+1);
			vo.setResImagPath(resImagPath);
		}
			
		
		//css文件相对路径修正
		String resCssPath = vo.getResCssPath();
		if(resCssPath!=null && resCssPath.length()>0){
			resCssPath = resCssPath.substring(resourcePath.length()+1);
			vo.setResCssPath(resCssPath);
		}
			
		
		//ext扩展文件路径修正
		String resExtPath = vo.getResExtPath();
		if(resExtPath!=null && resExtPath.length()>0){
			String[] resExtPaths = resExtPath.split(";");
			resExtPath = "";
			for (int i = 0; i < resExtPaths.length; i++) {
				resExtPath+= resExtPaths[i].substring(resourcePath.length()+1)+";";
			}
			if(resExtPath.endsWith(";"))
				resExtPath = resExtPath.substring(0,resExtPath.length()-1);
			
			vo.setResExtPath(resExtPath);
		}
		
		//初始化配置文件路径修正
		String resInitializerPath = vo.getResInitializerPath();
		if(resInitializerPath!=null && resInitializerPath.length()>0){
			resInitializerPath = resInitializerPath.substring(resourcePath.length()+1);
			vo.setResInitializerPath(resInitializerPath);
		}
		
		return vo;
	}
	
	/**
	 * <p>
	 * 	更新插件注册信息
	 * 
	 * 	<ul>逻辑：
	 * 		<li>修正VO中的相对路径</li>
	 * 		<li>更新数据库</li>
	 * 	</ul>
	 * </p>
	 * @param vo 插件的主册信息VO
	 * @param con 数据库连接
	 * @throws PluginException
	 */
	protected void upateRegPluginInfo(PluginRegVO vo, Connection con) throws PluginException{
		Statement stmt = null;
		try {
			
			StringBuffer sb = new StringBuffer();
			//sb.append("update S_PLUGIN_REG "); 
			
			sb.append(" set ");
			
			if(vo.getPluginModualName()!=null && vo.getPluginModualName().length()>0) sb.append(" plugin_modual_name='"+vo.getPluginModualName()+"', ");
			
			if(vo.getResourcePath()!=null && vo.getResourcePath().length()>0) sb.append(" resource_path='"+vo.getResourcePath()+"', ");
			
			if(vo.getDbPath()!=null && vo.getDbPath().length()>0) sb.append(" db_path='"+vo.getDbPath()+"', ");
			
			if(vo.getResJspPath()!=null && vo.getResJspPath().length()>0) sb.append(" res_jsp_path='"+vo.getResJspPath()+"', ");
			
			if(vo.getResTablePath()!=null && vo.getResTablePath().length()>0) sb.append(" res_table_path='"+vo.getResTablePath()+"', ");
			
			if(vo.getResActionPath()!=null && vo.getResActionPath().length()>0) sb.append(" res_action_path='"+vo.getResActionPath()+"', ");
			
			if(vo.getResServicePath()!=null && vo.getResServicePath().length()>0) sb.append(" res_service_path='"+vo.getResServicePath()+"', ");
			
			if(vo.getResSqlPath()!=null && vo.getResSqlPath().length()>0) sb.append(" res_sql_path='"+vo.getResSqlPath()+"', ");
			
			if(vo.getResJsPath()!=null && vo.getResJsPath().length()>0) sb.append(" res_js_path='"+vo.getResJsPath()+"', ");
			
			if(vo.getResImagPath()!=null && vo.getResImagPath().length()>0) sb.append(" res_imag_path='"+vo.getResImagPath()+"', ");
			
			if(vo.getResCssPath()!=null && vo.getResCssPath().length()>0) sb.append(" res_css_path='"+vo.getResCssPath()+"', ");
			
			if(vo.getPluginVersion()!=null && vo.getPluginVersion().length()>0) sb.append(" plugin_version='"+vo.getPluginVersion()+"', ");
			
			if(vo.getResExtPath()!=null && vo.getResExtPath().length()>0) sb.append(" res_ext_path='"+vo.getResExtPath()+"', ");
			
			if(vo.getResInitializerPath()!=null && vo.getResInitializerPath().length()>0) sb.append(" res_Initializer_Path='"+vo.getResInitializerPath()+"', ");
			
			sb.append(" install_date='"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"', ");
			
			FileUtil fileUtil = FileUtil.getInstance();
			//复制数据库卸载文件到"cmis.properties中指定目录/插件名/目录/"下
			String dbUninstallPath = vo.getDbUninstallPath();
			String backDbUninstallPath = null;//
			if(dbUninstallPath!=null && dbUninstallPath.length()>0){
				String backSqlFolder = this.getBackSqlFolder()+"/"+vo.getPluginModualId();
				//如果目录不存在，则新建
				fileUtil.newFolder(backSqlFolder+"/");
				//插件包下的sql文件全路径
				dbUninstallPath = pluginPath+"/"+dbUninstallPath;
				//sql文件名
				String sqlName = dbUninstallPath.substring(dbUninstallPath.lastIndexOf("/"));
				//备份的全路径sql文件名
				backDbUninstallPath = backSqlFolder+"/"+sqlName;
			}
			
			if(backDbUninstallPath!=null && backDbUninstallPath.length()>0)
				sb.append(" db_uninstall_sql='"+backDbUninstallPath+"', ");
			
			String strSql = sb.toString();
			//说明有需要更新的项
			if(strSql.endsWith(",")){
				strSql = strSql.substring(0, strSql.length()-1);
				strSql = "update S_PLUGIN_REG " +strSql +" where plugin_modual_id='"+vo.getPluginModualId()+"'";
			}else
				return;
			
			//更新操作
			stmt = con.createStatement();
			int i = stmt.executeUpdate(strSql);
			if(i==0) 
				throw new PluginException("插件注册表数据更新失败，更新记录数为"+i+",插件ID："+vo.getPluginModualId());
			
		} catch (Exception e) {
			throw new PluginException("插件注册表数据更新失败,插件ID："+vo.getPluginModualId()+","+e);
		} finally{
			try {
				if(stmt!=null) stmt.close();
			} catch (Exception e2) {
				throw new PluginException("插件注册表数据更新失败,插件ID："+vo.getPluginModualId()+","+e2);
			}
		}
	}
	
	/**
	 * <p>
	 * 	插件注册
	 * 	<ul>逻辑：
	 * 		<li>将插件包中的注册文件信息写到数据库S_PLUGIN_REG表中,在写入工程文件的相对路径时需要截除resourcePath，以保留实际上项目工程中的相对路径，
	 * 			以便插件卸载的时候使用</li>
	 * 		<li>备份卸载的SQL文件到指定目录/插件名/文件名，在卸载数据库文件时使用</li>
	 * </ur>
	 * </p>
	 * 
	 * @param PluginRegVO 插件的主册信息VO
	 * @param con　数据库连接
	 */
	protected void regPluginInfo(PluginRegVO vo, Connection con)throws PluginException{
		PreparedStatement pst = null;
		
		try {
			if(vo==null || vo.getPluginModualName()==null) return ;
			FileUtil fileUtil = FileUtil.getInstance();
			//插入注册表SQL
			String strSql = "insert into S_PLUGIN_REG"
								+ "(plugin_modual_id,plugin_modual_name,resource_path,db_path,res_java_path," //5个字段
								+ "res_jsp_path,res_table_path,res_action_path,res_service_path,res_sql_path,"//５个字段
								+ "res_js_path,res_imag_path,res_css_path,db_uninstall_sql,install_date,"     //5个字段
								+ "plugin_status,plugin_version,res_ext_path,plugin_memo,res_Initializer_Path) " //5个字段
							+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			//修正VO
			vo = fixPluginRegVO(vo);
			
			
			//复制数据库卸载文件到"cmis.properties中指定目录/插件名/目录/"下
			String dbUninstallPath = vo.getDbUninstallPath();
			String backDbUninstallPath = null;//
			if(dbUninstallPath!=null && dbUninstallPath.length()>0){
				String backSqlFolder = this.getBackSqlFolder()+"/"+vo.getPluginModualId();
				//如果目录不存在，则新建
				fileUtil.newFolder(backSqlFolder+"/");
				//插件包下的sql文件全路径
				dbUninstallPath = pluginPath+"/"+dbUninstallPath;
				//sql文件名
				String sqlName = dbUninstallPath.substring(dbUninstallPath.lastIndexOf("/"));
				//备份的全路径sql文件名
				backDbUninstallPath = backSqlFolder+"/"+sqlName;
				
			}
			
			//注册表数据写入
			pst = con.prepareStatement(strSql);
			pst.setString(1, vo.getPluginModualId());
			pst.setString(2, vo.getPluginModualName());
			pst.setString(3, vo.getResourcePath());
			pst.setString(4, vo.getDbPath());
			pst.setString(5, vo.getResJavaPath());
			pst.setString(6, vo.getResJspPath());
			pst.setString(7, vo.getResTablePath());
			pst.setString(8, vo.getResActionPath());
			pst.setString(9, vo.getResServicePath());
			pst.setString(10, vo.getResSqlPath());
			pst.setString(11, vo.getResJsPath());
			pst.setString(12, vo.getResImagPath());
			pst.setString(13, vo.getResCssPath());
			pst.setString(14, backDbUninstallPath);
			pst.setString(15, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			pst.setString(16,"010");//启用状态
			pst.setString(17, vo.getPluginVersion());
			pst.setString(18, vo.getResExtPath());
			pst.setString(19, vo.getPluginMemo());
			pst.setString(20, vo.getResInitializerPath());
			
			pst.execute();
			
			//复制文件,放在这里是因为数据库操作失败后，可以回滚，但是复制文件还没有很好的回滚机制
			fileUtil.copyFile(dbUninstallPath, backDbUninstallPath);
			
		} catch (Exception e) {
			throw new PluginException("插件注册表数据写入失败",e);
		} finally{
			try {
				if(pst!=null) pst.close();
			} catch (Exception e2) {
				throw new PluginException("插件注册表数据写入失败",e2);
			}
			
		}
	}
	
	/**
	 * <p>
	 * 	删除插件的注删信息
	 * 	<ul>逻辑：删除S_PLUGIN_REG表中该插件模块的信息</ul>
	 *  注：该方法一般与删除插件功能一起使用
	 * </p>
	 * 
	 * @param PluginRegVO 插件VO
	 * @param con 数据库连接
	 */
	protected void deletePlugInfo(PluginRegVO vo,Connection con)throws PluginException{
		Statement stmt = null;
		String plugidModualId = null;
		try {
			if(vo==null) return ;
			plugidModualId = vo.getPluginModualId();
			String strSql = "delete from S_PLUGIN_REG where plugin_modual_id='"+plugidModualId+"'";
			
			stmt = con.createStatement();
			
			stmt.execute(strSql);
			
		} catch (Exception e) {
			throw new PluginException("删除插件的注删信息失败，插件ID："+plugidModualId);
		}
	}
	
	/**
	 * <p>
	 * 安装插件之工程文件
	 * <ul>逻辑：
	 * 		<li>解析*.reg文件中的注册信息</li>
	 * 		<li>校验项目是否已有该插件（工程文件），如果有根据forceLoad是否强制装载</li>
	 * 		<li>根据注册信息将插件包中的工程文件复制到项目中</li>
	 * 		<li>JAR文件复制使用指定规则</li>
	 * </ul>
	 * </p>
	 * 
	 * @param PluginRegVO 插件注册信息VO
	 * @param forceLoad　true-强制加载；false-非强制加载
	 * @return 安装信息
	 */
	protected String installPluginResource(PluginRegVO regVO,boolean forceLoad)throws PluginException{
		StringBuffer retMsg = new StringBuffer();//安装信息
		try {
			//校验项目是否已有该插件
			//TODO
			
			//根据注册信息将插件包中的工程文件复制到项目中
			String resourcePath = regVO.getResourcePath();
			FileUtil fileUtil = FileUtil.getInstance();
			
			//复制JAVA文件
			if(regVO.getResJavaPath()!=null && regVO.getResJavaPath().length()>0){
				String javaPath = regVO.getResJavaPath().substring(resourcePath.length()+1);
				fileUtil.copyFolder(this.pluginPath+"/"+regVO.getResJavaPath(), this.projectPath+"/"+javaPath);
			}
			
			//复制JSP文件
			if(regVO.getResJspPath()!=null && regVO.getResJspPath().length()>0){
				String jspPath = regVO.getResJspPath().substring(resourcePath.length()+1);
				fileUtil.copyFolder(this.pluginPath+"/"+regVO.getResJspPath(), this.projectPath+"/"+jspPath);
			}
			
			//复制Table文件
			if(regVO.getResTablePath()!=null && regVO.getResTablePath().length()>0){
				String tablePath = regVO.getResTablePath().substring(resourcePath.length()+1);
				fileUtil.copyFolder(this.pluginPath+"/"+regVO.getResTablePath(), this.projectPath+"/"+tablePath);
			}
		
			//复制Action文件
			if(regVO.getResActionPath()!=null && regVO.getResActionPath().length()>0){
				String actionPath = regVO.getResActionPath().substring(resourcePath.length()+1);
				fileUtil.copyFolder(this.pluginPath+"/"+regVO.getResActionPath(), this.projectPath+"/"+actionPath);
			}
			
			//复制services文件
			if(regVO.getResServicePath()!=null && regVO.getResServicePath().length()>0){
				String servicePath = regVO.getResServicePath().substring(resourcePath.length()+1);
				fileUtil.copyFolder(this.pluginPath+"/"+regVO.getResServicePath(), this.projectPath+"/"+servicePath);
			}
			
			//复制SQL文件
			if(regVO.getResSqlPath()!=null && regVO.getResSqlPath().length()>0){
				String sqlPath = regVO.getResSqlPath().substring(resourcePath.length()+1);
				fileUtil.copyFolder(this.pluginPath+"/"+regVO.getResSqlPath(), this.projectPath+"/"+sqlPath);
			}
			
			//复制JS文件
			if(regVO.getResJsPath()!=null && regVO.getResJsPath().length()>0){
				String jsPath = regVO.getResJsPath().substring(resourcePath.length()+1);
				fileUtil.copyFolder(this.pluginPath+"/"+regVO.getResJsPath(), this.projectPath+"/"+jsPath);
			}
			
			//复制imag文件
			if(regVO.getResImagPath()!=null && regVO.getResImagPath().length()>0){
				String imagPath = regVO.getResImagPath().substring(resourcePath.length()+1);
				fileUtil.copyFolder(this.pluginPath+"/"+regVO.getResImagPath(), this.projectPath+"/"+imagPath);
			}
			
			//复制CSS文件
			if(regVO.getResCssPath()!=null && regVO.getResCssPath().length()>0){
				String cssPath = regVO.getResCssPath().substring(resourcePath.length()+1);
				fileUtil.copyFolder(this.pluginPath+"/"+regVO.getResCssPath(), this.projectPath+"/"+cssPath);
			}
			
			//复制模块初始化文件
			if(regVO.getResInitializerPath()!=null && regVO.getResInitializerPath().length()>0){
				String initializerPath = regVO.getResInitializerPath().substring(resourcePath.length()+1);
				fileUtil.copyFolder(this.pluginPath+"/"+regVO.getResInitializerPath(), this.projectPath+"/"+initializerPath);
			}
			
			//复制Lib文件
			String libStr = installLibResource(regVO, forceLoad);
			retMsg.append(libStr);
			
			//复制扩展文件信息
			installExtResource(regVO, forceLoad);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new PluginException(e);
		}
		
		return retMsg.toString();
	}
	
	/**
	 * <p>
	 * 复制插件的Lib文件
	 * <ul>逻辑：
	 * 		<li>校验工程下是否有同名的JAR包，如果没有，则复制，如果有，则不复制；</li>
	 * 		<li>JAR匹配：只匹配名字，不匹配版本，即：工程中有一个版本较底或较高版本的JAR，也不会复制或覆盖
	 * 			因为JAR的命名没有同一规则，所以只匹配名字比较困难；这里的使用的规则是：
	 * 				1、commons-codec-1.3.jar，类似这种命名的jar包，'-'最后以数字的则认为其是版本号
	 * 				2、匹配'数字 . -'.jar，则认为其是版本号如：emp2.2.101012.jar，认为2.2.101012是版本号
	 * 		</li>
	 * 
	 * 		<li>因为JAR的复制规则，使插件在卸载时不能删除JAR，因为该JAR已经被其它已安装插件所使用</li>
	 * 
	 * </ul>
	 * </p>
	 * @param PluginRegVO 插件注册信息VO
	 * @param forceLoad　true-强制加载；false-非强制加载
	 * @return 安装信息，这里记录JAR包冲突的信息
	 */
	protected String installLibResource(PluginRegVO regVO,boolean forceLoad){
		if(regVO.getResLibPath() == null || regVO.getResLibPath().length()==0)
			return null;
		
		StringBuffer sb = new StringBuffer();
		FileUtil fileUtil = FileUtil.getInstance();
		
		String resourcePath = regVO.getResourcePath();
		String libPath = regVO.getResLibPath().substring(resourcePath.length()+1);
		String pluginLibPath = this.pluginPath+"/"+regVO.getResLibPath();//插件的Lib目录绝对位置
		String projectLibPath = this.projectPath+"/"+libPath;//工程中的Lib目录绝对位置
		
		File file = new File(pluginLibPath);
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				String name = files[i].getName();
				
				//根据规则来取得JAR的名字（不含版本）
				String jarName = getLibMathName(name);
				
				File plibFile = new File(projectLibPath);
				File[] plFiles = plibFile.listFiles();
				
				boolean flag = false;
				for (int j = 0; j < plFiles.length; j++) {
					//工程中已包含该JAR包
					if(plFiles[j].getName().startsWith(jarName)){
						flag = true;
						sb.append("工程中含有该JAR："+plFiles[j]+",忽略插件包中的JAR："+name+"\n");
					}
				}
				
				if(!flag){
					flag = false;
					//复制该JAR
					fileUtil.copyFile(files[i].getPath(), projectLibPath+"/"+files[i].getName());
				}
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * <p>
	 * 	复制插件的扩展文件
	 * 	<ul>
	 * 		接入第三方应用时一些文件位置的特殊要求（如：eChain.properties必需要放到src下的，studio必须要放到WebContent下)
	 *		JAR在加入时，添加校验，校验当前工程下是否有（按名称来校验，不校验版本），添加扩展字段：该字段放一特殊要求的文件
	 *		目录，以;分隔
	 *  </ul>
	 * </p>
	 *  
	 * @param PluginRegVO 插件注册信息VO
	 * @param forceLoad　true-强制加载；false-非强制加载
	 * @return 安装信息，这里记录JAR包冲突的信息
	 */
	protected void installExtResource(PluginRegVO regVO,boolean forceLoad){
		if(regVO.getResExtPath() == null || regVO.getResExtPath().length() == 0) return ;
		
		FileUtil fileUtil = FileUtil.getInstance();
		String resourcePath = regVO.getResourcePath();
		String extPath = regVO.getResExtPath();
		
		String[] extPaths = extPath.split(";");
		for (int i = 0; i < extPaths.length; i++) {
			String fileName = this.pluginPath+"/"+extPaths[i];
			File tmpFile = new File(fileName);
			//复制目录
			if(tmpFile.isDirectory()){
				String tmpPath = extPaths[i].substring(resourcePath.length()+1);
				fileUtil.copyFolder(fileName, this.projectPath+"/"+tmpPath);
			}
			//复制文件
			else {
				String tmpPath = extPaths[i].substring(resourcePath.length()+1);
				fileUtil.copyFile(fileName, this.projectPath+"/"+tmpPath);
			}
		}
	}
	
	
	
	/**
	 * 匹配JAR,得返回JAR的名字，不含版本号
	 * @param name JAR的文件名
	 * @return  JAR的名字，不含版本号
	 */
	private String getLibMathName(String name){
		String retStr = null;
		//匹配emp2.2.101012.jar
		String reg1 = "([a-zA-Z]+)([0-9\\.]+).jar";
		//匹配commons-codec-1.3.jar
		String reg2 = "(\\S+)-([0-9\\.]+).jar";
		
		Pattern p = Pattern.compile(reg1);
		Matcher m = p.matcher(name);
		while(m.find()){
			retStr = m.group(1);
		}
		if(retStr!=null) return retStr;
		
		p = Pattern.compile(reg2);
		m = p.matcher(name);
		while(m.find()){
			retStr = m.group(1);
		}
		if(retStr!=null) return retStr;
		return name;
	}
	
	
	/**
	 * <p>
	 * 安装插件之插件依赖数据库
	 * <ul>逻辑：
	 * 		<li>解析*.reg文件中的注册信息</li>
	 * 		<li>校验项目是否已有该插件（数据库注册表），如果有根据forceLoad是否强制装载</li>
	 * 		<li>根据注册信息执行插件包中的数据库脚本</li>
	 * </ul>
	 * </p>
	 * 
	 * @param PluginRegVO 插件注册信息VO
	 * @param forceLoad　true-强制加载；false-非强制加载
	 * @param con 数据库连接
	 */
	protected void installPluginDataBase(PluginRegVO plugVO,boolean forceLoad,Connection con)throws PluginException{
		try {
			if(con==null) return ;
			
			//创表SQL执行
			String createTableName = plugVO.getDbCreateTableName();
			if(createTableName!=null && createTableName.length()>0){
				List<String> sqlList = SQLParsing.sqlParsingFromFile(this.pluginPath+"/"+createTableName);
				this.executeSQL(sqlList, con);
			}
			
			//初始化数据
			String initDataName = plugVO.getDbInitDataName();
			if(initDataName!=null && initDataName.length()>0){
				List<String> sqlList = SQLParsing.sqlParsingFromFile(this.pluginPath+"/"+initDataName);
				this.executeSQL(sqlList, con);
			}
			
		} catch (Exception e) {
			throw new PluginException("安装插件之插件依赖数据库出错！",e); 
		}
	}
	
	/**
	 * <p>
	 * 卸载插件之工程文件
	 * <ul>逻辑：
	 * 		<li>根据插件的注册信息来删除工程中的相关代码</li>
	 * 		<li>JAVA:不卸载模块下的msi(msi下的msiimple会删除)、domain、exception、baffleplate、常量类：模块ID+Constance</li>
	 * 		<li>备份代码指定目录，备份策略：以yyyyMMddHHssmm格式新建文件夹，以工程的结构备份所有代码(不包括数据库)</li>
	 * </ul>
	 * </p>
	 * @param PluginRegVO 卸载插件VO
	 */
	protected void uninstallPluginResource(PluginRegVO vo)throws PluginException{
		try {
			if(vo==null) return ;
			String resJavaPath = vo.getResJavaPath();
			String resJspPath = vo.getResJspPath();
			String resTablePath = vo.getResTablePath();
			String resActionath = vo.getResActionPath();
			String resServicePath = vo.getResServicePath();
			String resSqlPath = vo.getResSqlPath();
			String resJsPath = vo.getResJsPath();
			String resImagPath = vo.getResImagPath();
			String resCssPath = vo.getResCssPath();
			String resExtPath = vo.getResExtPath();
			String resInitializerPath = vo.getResInitializerPath();
			
			FileUtil fileUtil = FileUtil.getInstance();
			//清除JAVA文件
			if(resJavaPath!=null && resJavaPath.length()>0){
				//不删除的目录
				List<String> exclude = new ArrayList<String>();
				exclude.add("msi");
				exclude.add("domain");
				exclude.add("exception");
				exclude.add("baffleplate");
				exclude.add(vo.getPluginModualId()+"Constance.java");
				
				//删除操作
				fileUtil.delFolderExclude(this.projectPath+"/"+resJavaPath, exclude);
				
				//删除msi/msiimple
				fileUtil.delFolder(this.projectPath+"/"+resJavaPath+"/msi/msiimple");
			}
				
			//清除JSP文件
			if(resJspPath!=null && resJspPath.length()>0)
				fileUtil.delFolder(this.projectPath+"/"+resJspPath);
			//清除Table配置文件
			if(resTablePath!=null && resTablePath.length()>0)
				fileUtil.delFolder(this.projectPath+"/"+resTablePath);
			//清除Action配置文件
			if(resActionath!=null && resActionath.length()>0)
				fileUtil.delFolder(this.projectPath+"/"+resActionath);
			//清除服务注册文件
			if(resServicePath!=null && resServicePath.length()>0)
				fileUtil.delFolder(this.projectPath+"/"+resServicePath);
			//清除命名SQL文件
			if(resSqlPath!=null && resSqlPath.length()>0)
				fileUtil.delFolder(this.projectPath+"/"+resSqlPath);
			//清除JS文件
			if(resJsPath!=null && resJsPath.length()>0)
				fileUtil.delFolder(this.projectPath+"/"+resJsPath);
			//清除imag文件
			if(resImagPath!=null && resImagPath.length()>0)
				fileUtil.delFolder(this.projectPath+"/"+resImagPath);
			//清除CSS文件
			if(resCssPath!=null && resCssPath.length()>0)
				fileUtil.delFolder(this.projectPath+"/"+resCssPath);
			//清除模块初始化配置文件
			if(resInitializerPath!=null && resInitializerPath.length()>0)
				fileUtil.delFolder(this.projectPath+"/"+resInitializerPath);
			
			//清除扩展文件
			if(resExtPath!=null && resExtPath.length()>0){
				String[] extFiles = resExtPath.split(";"); 
				for (int i = 0; i < extFiles.length; i++) {
					File extFile = new File(this.projectPath+"/"+extFiles[i]);
					//文件夹删除
					if(extFile.isDirectory())
						fileUtil.delFolder(this.projectPath+"/"+extFiles[i]);
					//文件删除
					else if(extFile.isFile())
						fileUtil.delFile(this.projectPath+"/"+extFiles[i]);
				}
			}
			
		} catch (Exception e) {
			throw new PluginException("卸载插件（删除工程文件）出错!",e);
		}
	}
	
	/**
	 * <p>
	 * 卸载插件之插件依赖数据库
	 * <ul>逻辑：
	 * 		<li>读取注册表S_PLUGIN_REG中注册信息</li>
	 * 		<li>删除注册信息，还是备份ＴＯＤＯ　没想好</li>
	 * </ul>
	 * </p>
	 * @param PluginRegVO 卸载插件VO
	 * @param con 数据库连接
	 */
	protected void uninstallPluginDataBase(PluginRegVO vo, Connection con)throws PluginException{
		try {
			if(vo == null) return ;
			String uninstallSqlPath = vo.getDbUninstallPath();
			if(uninstallSqlPath==null || uninstallSqlPath.length()==0){
				System.out.println("卸载插["+vo.getPluginModualName()+"]时，未找到需要清理的数据库脚本，跳过该步");
				return ;
			}
				
			//解析需要执行的SQL
			List<String> sqlList = SQLParsing.sqlParsingFromFile(uninstallSqlPath);
			this.executeSQL(sqlList, con);
			
		} catch (Exception e) {
			throw new PluginException("卸载插件（清除数据库）出错!",e);
		}
	}
	
	/**
	 * <p>
	 * 	根据路径读取插件下的*.reg文件中的配置信息
	 * </p>
	 * 
	 * @return PluginRegVO　插件注册信息
	 */
	public PluginRegVO parsPluginRegVOFromRegFile()throws PluginException{
		String regFile = null;//插件包下的注册文件
		PluginRegVO plugVO = new PluginRegVO();
		try {
			File file = new File(this.pluginPath);
			if(!file.isDirectory())
				throw new PluginException(this.pluginPath+"不是插件包根目录");
			
			//找到注册文件
			String[] files = file.list();
			for (int i = 0; i < files.length; i++) {
				if(files[i].endsWith(".reg"))
					regFile = files[i];
			}
			
			//解析注册文件
			Properties prop = new Properties();
			prop.load(new FileInputStream(new File(this.pluginPath+"/"+regFile)));
			String pluginModualId = prop.getProperty("pluginModualId");
			String pluginModualName = prop.getProperty("pluginModualName");
			
			if(pluginModualId==null || pluginModualId.length()==0 || pluginModualName==null || pluginModualName.length()==0)
				throw new PluginException("解析插件注册文件出错!插件ID、插件名称为空!");
			
			plugVO.setPluginModualId(pluginModualId);
			plugVO.setPluginModualName(new String(pluginModualName.getBytes("ISO-8859-1"),PluginManagerConstance.PLUGIN_CHARSET));
			plugVO.setResJavaPath(prop.getProperty("resJavaPath"));
			plugVO.setResJspPath(prop.getProperty("resJspPath"));
			plugVO.setResTablePath(prop.getProperty("resTablePath"));
			plugVO.setResActionPath(prop.getProperty("resActionPath"));
			plugVO.setResServicePath(prop.getProperty("resServicePath"));
			plugVO.setResSqlPath(prop.getProperty("resSqlPath"));
			plugVO.setResJsPath(prop.getProperty("resJsPath"));
			plugVO.setResImagPath(prop.getProperty("resImagPath"));
			plugVO.setResCssPath(prop.getProperty("resCssPath"));
			plugVO.setResLibPath(prop.getProperty("resLibPath"));
			plugVO.setResExtPath(prop.getProperty("resExtPath"));
			plugVO.setResInitializerPath(prop.getProperty("resInitializerPath"));
			plugVO.setDbCreateTableName(prop.getProperty("dbCreateTableName"));
			plugVO.setDbInitDataName(prop.getProperty("dbInitDataName"));
			plugVO.setResourcePath(prop.getProperty("resourcePath"));
			plugVO.setDbPath(prop.getProperty("dbPath"));
			plugVO.setDbUninstallPath(prop.getProperty("dbUninstallSql"));
			plugVO.setPluginVersion(prop.getProperty("pluginVersion"));
			
		} catch (Exception e) {
			throw new PluginException("解析插件注册文件出错!插件目录："+this.pluginPath,e);
		}
		
		return plugVO;
	}
	
	/**
	 * <p>
	 * 	备份指定插件
	 * 
	 * 	备份指定插件到指定目录，目录命名为：pluginid/pluginid_yyyyMMddHHmmss
	 * </p>
	 * @param regVO 插件注册信息
	 */
	protected void backUpPlugin(PluginRegVO regVO) throws PluginException{
		//插件备份目录
		String backUpFolder = CMISPropertyManager.getInstance().getPluginUninstallBackUpPath();
		if(backUpFolder==null) throw new PluginException("插件备份目录未设置...");
		
		FileUtil fileUtil = FileUtil.getInstance();
		
		//插件的备份目录 = 指定目录/pluginId/pluginid_yyyyMMddHHmmss
		String pluginBackUp = backUpFolder+"/"+regVO.getPluginModualId()+"/"+regVO.getPluginModualId()+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		//新建
		fileUtil.newFolder(pluginBackUp);
		
		
		//备份JAVA文件
		String javaPath = regVO.getResJavaPath();
		if(javaPath!=null && javaPath.length()>0){
			fileUtil.copyFolder(this.projectPath+"/"+javaPath, pluginBackUp+"/"+javaPath);
		}
		
		//备份JSP文件
		String jspPath = regVO.getResJspPath();
		if(jspPath!=null && jspPath.length()>0){
			fileUtil.copyFolder(this.projectPath+"/"+jspPath, pluginBackUp+"/"+jspPath);
		}
		
		//备份Table文件
		String tablePath = regVO.getResTablePath();
		if(tablePath!=null && tablePath.length()>0){
			fileUtil.copyFolder(this.projectPath+"/"+tablePath, pluginBackUp+"/"+tablePath);
		}
	
		//备份Action文件
		String actionPath = regVO.getResActionPath();
		if(actionPath!=null && actionPath.length()>0){
			fileUtil.copyFolder(this.projectPath+"/"+actionPath, pluginBackUp+"/"+actionPath);
		}
		
		//备份services文件
		String servicePath = regVO.getResServicePath();
		if(servicePath!=null && servicePath.length()>0){
			
			fileUtil.copyFolder(this.projectPath+"/"+servicePath, pluginBackUp+"/"+servicePath);
		}
		
		//备份SQL文件
		String sqlPath = regVO.getResSqlPath();
		if(sqlPath!=null && sqlPath.length()>0){
			fileUtil.copyFolder(this.projectPath+"/"+sqlPath, pluginBackUp+"/"+sqlPath);
		}
		
		//备份JS文件
		String jsPath = regVO.getResJsPath();
		if(jsPath!=null && jsPath.length()>0){
			
			fileUtil.copyFolder(this.projectPath+"/"+jsPath, pluginBackUp+"/"+jsPath);
		}
		
		//备份imag文件
		String imagPath = regVO.getResImagPath();
		if(imagPath!=null && imagPath.length()>0){
			fileUtil.copyFolder(this.projectPath+"/"+imagPath, pluginBackUp+"/"+imagPath);
		}
		
		//备份CSS文件
		String cssPath = regVO.getResCssPath();
		if(cssPath!=null && cssPath.length()>0){
			fileUtil.copyFolder(this.projectPath+"/"+cssPath, pluginBackUp+"/"+cssPath);
		}
		
		//备份模块初始化文件
		String initializerPath = regVO.getResInitializerPath();
		if(initializerPath!=null && initializerPath.length()>0){
			fileUtil.copyFolder(this.projectPath+"/"+initializerPath, pluginBackUp+"/"+initializerPath);
		}
		
		//备份扩展文件信息
		String extPath = regVO.getResExtPath();
		if(extPath!=null && extPath.length()>0){
			String[] extPaths = extPath.split(";");
			for (int i = 0; i < extPaths.length; i++) {
				String fileName = this.projectPath+"/"+extPaths[i];
				File tmpFile = new File(fileName);
				//复制目录
				if(tmpFile.isDirectory()){
					fileUtil.copyFolder(fileName, pluginBackUp+"/"+extPaths[i]);
				}
				//复制文件
				else {
					fileUtil.copyFile(fileName, pluginBackUp+"/"+extPaths[i]);
				}
			}
		}
	}
	
	/**
	 * <p>
	 * 	根据数据库来读插件注册信息
	 * </p>
	 * 
	 * @param pluginModualName 插件名称
	 * @param con　数据库连接
	 * @return PluginRegVO　插件注册信息
	 */
	protected PluginRegVO parsPluginRegVOFromDB(String pluginModualId, Connection con)throws PluginException{
		PluginRegVO plugVO = new PluginRegVO();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if(pluginModualId==null || pluginModualId.length()<=0) return null;
			
			String strSql = "select resource_path,db_path,res_java_path,res_jsp_path, res_table_path,"	  //5个字段
					+ "			res_action_path,res_service_path,res_sql_path,res_js_path,res_imag_path, "//5个字段
					+ "			res_css_path,db_uninstall_sql,plugin_modual_name,res_ext_path, res_Initializer_Path" //5个字段	
					+ "		 from S_PLUGIN_REG where plugin_modual_id='"+pluginModualId+"'";
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSql);
			if(rs.next()){
				plugVO.setPluginModualId(pluginModualId);
				plugVO.setResourcePath(rs.getString(1));
				plugVO.setDbPath(rs.getString(2));
				plugVO.setResJavaPath(rs.getString(3));
				plugVO.setResJspPath(rs.getString(4));
				plugVO.setResTablePath(rs.getString(5));
				plugVO.setResActionPath(rs.getString(6));
				plugVO.setResServicePath(rs.getString(7));
				plugVO.setResSqlPath(rs.getString(8));
				plugVO.setResJsPath(rs.getString(9));
				plugVO.setResImagPath(rs.getString(10));
				plugVO.setResCssPath(rs.getString(11));
				plugVO.setDbUninstallPath(rs.getString(12));
				plugVO.setPluginModualName(rs.getString(13));
				plugVO.setResExtPath(rs.getString(14));
				plugVO.setResInitializerPath(rs.getString(15));
			}
			
		} catch (Exception e) {
			throw new PluginException("解析插件注册文件出错!插件ID："+pluginModualId,e);
		} finally{
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
			} catch (Exception e2) {
				throw new PluginException("解析插件注册文件出错!插件ID："+pluginModualId,e2);
			}
			
			
		}
		
		return plugVO;
	}
	
	
	/**
	 * <p>
	 * 	执行SQL
	 * 	<ul>
	 * 		逻辑：因为每个插件在执行ＳＱＬ的时候，极有可能表已存在或是主键冲突，所以这里在执行某一条ＳＱＬ出异常的时候
	 * 			不会中断程序，而是会将导制异常的ＳＱＬ写到文件中
	 * 	</ul>
	 * </p>
	 * 
	 * @param sqlList　ＳＱＬ的集合
	 * @param con　数据加连接
	 * @throws PluginException　异常
	 */
	private void executeSQL(List<String> sqlList, Connection con) throws PluginException{
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			
			for (int i = 0; i < sqlList.size(); i++) {
				String sql = sqlList.get(i);
				try {
					System.out.println(sql);
					stmt.execute(sql);
				} catch (Exception e) {
					//写文件到PLUGIN_EXCEPTION_FILE文件中
					//注意，创建失败的的表和数据，在卸装的时候是否要过滤掉，怎样过滤？
					//TODO
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			throw new PluginException("执行SQL出错！",e); 
		} finally{
			try {
				if (stmt != null) stmt.close();
			} catch (Exception e2) {
				throw new PluginException("执行SQL出错！", e2);
			}
			
		}
	}
	
	/**
	 * <p>
	 * 	更新插件安装信息:支持是否append模式,append模式，不覆盖以前安装信息；非append模式:覆盖以前安装信息
	 * 
	 * </p>
	 * @param pluginId 插件ID
	 * @param msg 安装信息
	 * @param isAppend true：append模式，不覆盖以前安装信息；false:覆盖以前安装信息
	 * @param con 数据库连接
	 */
	private void updateInstallMsg(String pluginId,String msg, boolean isAppend,Connection con) throws PluginException{
		if(msg==null || msg.trim().length()==0) return;
		
		PreparedStatement pst = null;
		String bMsg = "";//之前的安装信息
		try {
			if(isAppend){
				bMsg = this.queryInstallMsg(pluginId, con);
				bMsg = bMsg.equals("")?bMsg:bMsg+";";
			}
			
			String sqlStr = "update S_Plugin_Reg set plugin_memo=? where plugin_modual_id='"+pluginId+"'";
			pst = con.prepareStatement(sqlStr);
			pst.setString(1, bMsg+msg);
			
			pst.executeUpdate();
		} catch (Exception e) {
			throw new PluginException(e);
		} finally{
			try {
				if(pst!=null) pst.close();
			} catch (Exception e2) {
				throw new PluginException(e2);
			}
		}
	}
	
	/**
	 * <p>
	 * 	查询插件安装信息
	 * </p>
	 * @param pluginId 插件ID
	 * @param con 数据库连接
	 * @throws PluginException 异常
	 */
	private String queryInstallMsg(String pluginId, Connection con) throws PluginException{
		Statement stmt = null;
		ResultSet rs = null;
		String msg = null;
		try {
			String sqlStr = "select plugin_memo from S_Plugin_Reg where plugin_modual_id="+pluginId+"'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlStr);
			
			if(rs.next())
				msg = rs.getString(1);
			
		} catch (Exception e) {
			throw new PluginException(e);
		} finally{
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
			} catch (Exception e2) {
				throw new PluginException(e2);
			}
		}
		
		return msg==null?"":msg;
	}
	
	
	/**
	 * 完全卸载：该插件所有代码都删除；部份卸载：除该插件的MSI、Domains、异常、档板外，其余所有代码都删除
	 * @return 是否部份卸载插件
	 */
	public boolean isUninstallPart() {
		return uninstallPart;
	}

	/**
	 * 完全卸载：该插件所有代码都删除；部份卸载：除该插件的MSI、Domains、异常、档板外，其余所有代码都删除
	 * @param uninstallPart 是否部份卸载插件
	 */
	public void setUninstallPart(boolean uninstallPart) {
		this.uninstallPart = uninstallPart;
	}
	
	/**
	 * 备份卸载数据库脚本目录
	 * <p>默认是工程目录/backup
	 * @return 备份卸载数据库脚本目录
	 */
	public String getBackSqlFolder() {
		if(backSqlFolder==null || backSqlFolder.trim().length()<=0)
			return this.projectPath+"/backup";
		return backSqlFolder;
	}

	/**
	 * 备份卸载数据库脚本目录
	 * @param backSqlFolder 备份卸载数据库脚本目录
	 */
	public void setBackSqlFolder(String backSqlFolder) {
		this.backSqlFolder = backSqlFolder;
	}

	public static void main(String[] args) throws Exception {
//		String plugPath = "/home/yuhq/mydoc/产品研发/组织机构插件/organization/";
//		String projectPath = "/home/yuhq/workspace/TestSpace/yc_csd";
//		
//		PluginFileLoad load = new PluginFileLoad(projectPath,plugPath);
//		//加载工程文件
//		PluginRegVO vo = load.parsPluginRegVOFromRegFile();
//		load.installPluginResource(vo,true);
		
		//测试卸载插件－－－工程文件
//		PluginRegVO vo = new PluginRegVO();
//		vo.setResJavaPath("src/main/java/com/yucheng/cmis/platform/base/organization");
//		vo.setResJspPath("WebContent/WEB-INF/mvcs/CMISMvc/platform/base/organization");
//		vo.setResTablePath("WebContent/WEB-INF/tables/platform/base/organization");
//		vo.setResActionPath("WebContent/WEB-INF/mvcs/CMISMvc/actions/platform/base/organization");
//		
//		load.uninstallPluginResource(vo);
		
		String projectPath = "E:/eclipse_workspace/CMIS5/cmis";
		String pluginPath = "E:/信贷研发/2013/信贷业务开发平台/插件/导出的插件/organization20130821141955";

		PluginFileManager load = new PluginFileManager(projectPath, pluginPath);
		Connection con = load.getConnection();
		
//		//加载插件
//		load.installPlugin(true, con);
		//卸载插件
//		load.uninstallPlugIn("organization", con);
		
		
		
//		PluginRegVO vo = load.autoSearchPlugin("flashcharts");
//		System.out.println(vo.getResActionPath()+"\n"+
//						   vo.getResCssPath()+"\n"+
//						   vo.getResImagPath()+"\n"+
//						   vo.getResJavaPath()+"\n"+
//						   vo.getResJsPath()+"\n"+
//						   vo.getResJspPath()+"\n"+
//						   vo.getResServicePath()+"\n"+
//						   vo.getResSqlPath()+"\n"+
//						   vo.getResTablePath());
//		
		load.exportPlugin("pluginmanager", "E:/开发记录/信贷研发/2013/信贷业务开发平台/插件/导出的插件");
												
										   
//		
//		String serviceRelPath = null;
//		System.out.println("resServicePath="+serviceRelPath==null?"":serviceRelPath);
		
		con.close();
		
//		Properties pro = System.getProperties();
//		Enumeration e = pro.keys();
//		while(e.hasMoreElements()){
//			String key = (String)e.nextElement();
//			System.out.println(key+" : "+pro.getProperty(key));
//		}
		
	}
	
	
	private Connection getConnection(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			String url = "jdbc:oracle:thin:@localhost:1521:cmis"; // orcl为数据库的SID
			String user="cmis";
			String password="cmis";
			Connection conn= DriverManager.getConnection(url,user,password); 
			
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
