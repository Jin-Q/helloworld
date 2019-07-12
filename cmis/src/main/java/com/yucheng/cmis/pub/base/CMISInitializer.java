package com.yucheng.cmis.pub.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Map;
import java.sql.Connection;
import javax.sql.DataSource;

import com.ecc.emp.component.factory.ComponentFactory;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.component.xml.FormatParser;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.dbmodel.service.pkgenerator.PkGeneratorSet;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.web.servlet.Initializer;
import com.yucheng.cmis.accesscontroll.PermissionAccessController;
import com.yucheng.cmis.base.BusinessInitializer;
import com.yucheng.cmis.base.CMISComponetHelper;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISDataDicService;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.log.LogLoader;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.platform.permission.record.ConfigLoader;


/**
 * CMIS项目中用于初始化准备工作的接口实现类。
 * <p>
 * 初始化的工作有：
 * <p>
 * 1、表模型的加载<br>
 * 2、表模型操作的服务类的初始化<br>
 * 3、记录级权限操作的服务类的初始化<br>
 * 4、字典数据的加载<br>
 * 5、树形字典数据的加载<br>
 * 6、如果定义了相应的业务初始化接口的实现类，则执行相应的业务初始化
 * 
 * @author liubq
 *
 */
public class CMISInitializer implements Initializer {

	/**
	 * 具体项目中用于扩展业务的初始化工作的接口
	 */
	BusinessInitializer businessInitializer;
	
	public void initialize(EMPFlowComponentFactory factory) throws Exception {

		//获得根资源rootContext
		Context rootCtx = factory.getContextNamed(factory.getRootContextName());

		//获得表模型的根路径
		String bizRootPath = (factory).getRootPath();
		String webInfPath = bizRootPath.substring(0, bizRootPath.indexOf("bizs"));
		try{
			rootCtx.addDataField("webInfPath", webInfPath);
		}
		catch(Exception e){
			rootCtx.setDataValue("webInfPath", webInfPath);
		}
		String tableModelPath = webInfPath + "/tables/";
		
		//将所有表模型加载到TableModelLoader中
		TableModelLoader modelLoader = (TableModelLoader)rootCtx.getService(CMISConstance.ATTR_TABLEMODELLOADER);
		modelLoader.initTableModels(tableModelPath);	
		
		//设置表模型服务中的表模型加载类TableModelLoader
		TableModelDAO dao = (TableModelDAO)rootCtx.getService(CMISConstance.ATTR_TABLEMODELDAO);
		dao.setTableModelLoader(modelLoader);
		
		//设置表模型服务中的主键生成器服务类
		PkGeneratorSet pkGeneratorSet = (PkGeneratorSet)rootCtx.getService(CMISConstance.ATTR_PRIMARYKEYSERVICE);
		dao.setPkGeneratorSet(pkGeneratorSet);
		
		//设置记录级权限服务中的表模型服务类
		RecordRestrict recordRestrict = (RecordRestrict)rootCtx.getService(CMISConstance.ATTR_RECORDRESTRICT);
		recordRestrict.setTableModelDao(dao);
		
		//加载所有的错误码信息
		String messageFilePath = webInfPath + "/commons/messageManager.xml";
		CMISMessageManager.initiateMessage(messageFilePath);
		
		//加载访问控制权限的配置信息
		PermissionAccessController accessController = PermissionAccessController.accessController;
		if(accessController != null){
			String accessFilePath = webInfPath + "/commons/permissionAccess.xml";;
			accessController.setAccessFileName(accessFilePath);
			PermissionAccessController.initializeAccess();
		}
		
		// 用于流程接口整合
		CMISComponetHelper.setComponentFactory(factory);
		
		DataSource dataSource = (DataSource)rootCtx.getService(CMISConstance.ATTR_DATASOURCE);
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection(dataSource);
			
			//字典数据导入
			CMISDataDicService dicService = (CMISDataDicService)rootCtx.getService(CMISConstance.ATTR_DICSERVICE);
			if(dicService != null){
				dicService.loadDicData(rootCtx, connection);
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.INFO, 0, "Initial the CMISDataDicService success!");
			}else
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.WARNING, 0, "There is no CMISDataDicService in rootContext!");
			
			//树形字典数据导入
			CMISTreeDicService treeDicService = (CMISTreeDicService)rootCtx.getService(CMISConstance.ATTR_TREEDICSERVICE);
			if(treeDicService != null){
				treeDicService.loadDicData(rootCtx, connection);
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.INFO, 0, "Initial the CMISTreeDicService success!");
			}else{
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.WARNING, 0, "There is no CMISTreeDicService in rootContext!");
			}
			
			//泉州银行产品树形数据导入,改入模块化初始化加载
			/*CMISProductTreeDicService treePrdService = (CMISProductTreeDicService)rootCtx.getService(CMISConstance.ATTR_TREEPRDSERVICE);
			if(treePrdService != null){
				treePrdService.loadDicData(rootCtx, connection);
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.INFO, 0, "Initial the CMISProductTreeDicService success!");
			}else{
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.WARNING, 0, "There is no CMISProductTreeDicService in rootContext!");
			}*/
			
                        //财报表样式初始化
			/*FNCFactory fncFactory = (FNCFactory)rootCtx.getService(CMISConstance.ATTR_RPTSERVICE);
			if(fncFactory != null){
				fncFactory.loadFNCInfo(rootCtx, connection);
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.INFO, 0, "Initial the FNCFactory success!");
			}else{
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.WARNING, 0, "There is no FNCFactory in rootContext!");
			}*/


			//调用业务的初始化接口
			if(this.businessInitializer != null){
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.INFO, 0, "Initial the businessInitializer... ");
				this.businessInitializer.initialize(rootCtx, connection);
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.INFO, 0, "Initial the businessInitializer success!");
			}else{
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.WARNING, 0, "There is no businessInitializer!");
			}
			
			//加载资源列表
			LogLoader.getResource(connection);
			
		} catch(Exception e1){
			EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0,e1.getMessage());
			throw e1;
		} finally {
			if (connection != null)
				ConnectionManager.releaseConnection(dataSource, connection);
		}
		
		/** 交易报文格式配置初始化*/
		EMPLog.log(EMPConstance.EMP_CORE, EMPLog.INFO, 0,"交易报文格式配置初始化...");
		
		ComponentFactory formatFactory = new ComponentFactory();
		FormatParser parser = new FormatParser();
		formatFactory.setComponentParser(parser);
		parser.setComponentFactory(factory);
		cpFormatFile(factory.getRootPath());
		String fileName = factory.getRootPath() + "newFormats.xml";//具体的文件地址，根目录是WEB-INF/biz/
		
		formatFactory.initializeComponentFactory("formats", fileName);
		ComponentFactory.removeComponentFactory("formats");
		try {
			Object obj = formatFactory.getRootComponent();
			if(obj instanceof Map){
				rootCtx.setFormats((Map)obj);
			}
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.ERROR, 0, "failed to load the format file !", e);
		}	
		 
		/** 加载记录级权限配置 */
		//ConfigLoader.loadPermissionConfig();
		
	}
	
	public BusinessInitializer getBusinessInitializer() {
		return businessInitializer;
	}

	public void setBusinessInitializer(BusinessInitializer businessInitializer) {
		this.businessInitializer = businessInitializer;
	}
	
	private boolean cpFormatFile(String rootpath){
		boolean boReturn = true;
 
		FileReader fRead = null;
		FileWriter fWriter = null;
		BufferedReader bRead = null;
		BufferedWriter bWrite = null;
		File srcFile = new File(rootpath + "formats.xml");
		File newFile = new File(rootpath + "newFormats.xml");
		try {
 
		   fRead   = new FileReader(srcFile);
		   fWriter = new FileWriter(newFile);
		   
		   bRead = new BufferedReader(fRead);
		   bWrite = new BufferedWriter(fWriter);
		   
		   String line = bRead.readLine();
		   while(line != null){
			   
			   EMPLog.log(EMPConstance.EMP_CORE, EMPLog.DEBUG, 0,line);
			   if(line.trim().startsWith("<formats.xml>")){
				   line = "<formats.xml class=\"java.util.HashMap\">";
			   }
			   bWrite.write(line);
			   bWrite.newLine();
			   line = bRead.readLine();
		   }
		   boReturn = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			boReturn = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			boReturn = false;
		} finally {
			if(bRead != null){
				try {
					bRead.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(fRead != null){
				try {
					fRead.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(bWrite != null){
				try {
					bWrite.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(fWriter != null){
				try {
					fWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return boReturn;
	}
}
