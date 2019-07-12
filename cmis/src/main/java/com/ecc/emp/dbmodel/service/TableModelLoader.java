package com.ecc.emp.dbmodel.service;

import java.io.File;
import java.util.HashMap;

import com.ecc.emp.component.factory.ComponentFactory;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelParser;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.service.EMPService;

/**
 * <h3>表模型加载器</h3>
 * 
 * <p>
 * 		<ul>２.2版本默认读/WebConet/WEB-INF/mvcs/tables/目录下的.xml文件.</ul>
 * 		<ul>３.0版本系统可以加载tables目录下的子目录的xml文件，支持多级子目录。插件／模块化设计需要</ul>
 * </p>
 * 
 * @time 2013-04-19
 * @author yuhq 
 * @version 1.0 
 *
 */
public class TableModelLoader extends EMPService {

	private HashMap modelBase = new HashMap(); 
	
	public void addTableModel(TableModel model){
		this.modelBase.put(model.getId(), model);
	}
	
	public TableModel getTableModel(String modelId){
		return (TableModel) this.modelBase.get(modelId);
	}
	
	public HashMap getAllModels(){
		return this.modelBase;
	}
	/**
	 * 实现init回调方法，进行service初始化。
	 * 
	 * @param factoryName
	 * 
	 */
	public void initialize(String factoryName) {
		ComponentFactory factory = EMPFlowComponentFactory.getComponentFactory(factoryName);
		
		String webInfPath = ((EMPFlowComponentFactory) factory).getRootPath();
		webInfPath = webInfPath.substring(0, webInfPath.indexOf("bizs"));
		String tableModelPath = webInfPath + "/tables/";
		
		File dir = new File(tableModelPath);
		if (!dir.exists() || !dir.isDirectory())
			return;

		String[] files = dir.list();
		if (files == null || files.length < 1)
			return;

		//循环加载tables目录下的所有子目录
		loadMultiFolderTableModels(tableModelPath);
		
		EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "Load all the tableModel file success!");
		
	}
	
	/**
	 * 加载指定目录下及其子目录所有的xml文件
	 * @param path　目录
	 */
	private void loadMultiFolderTableModels(String path){
		File dir = new File(path);
		
		String[] files = dir.list();
		if (files == null || files.length < 1)
			return;
		
		//加载该目录下所有xml文件
		loadSingleFolderTableModels(path);
		
		//加载该目录下的子目录
		for (int i = 0; i < files.length; i++) {
			String subPath = path+files[i];
			File subDir = new File(subPath);
			if(subDir.isDirectory())
				loadMultiFolderTableModels(subPath+"/");
		}
		
		
	}
	
	/**
	 * 加载某一个目录下的所有xml文件，该目录下的子目录忽略
	 * 
	 * @param path
	 */
	private void loadSingleFolderTableModels(String path){
		File dir = new File(path);
		if (!dir.exists() || !dir.isDirectory())
			return;

		String[] files = dir.list();
		if (files == null || files.length < 1)
			return;
		
		ComponentFactory tableFactory = new ComponentFactory();
		TableModelParser parser = new TableModelParser();
		tableFactory.setComponentParser(parser);

		for (int i = 0; i < files.length; i++) {
			if (!files[i].endsWith(".xml"))
				continue;
			String fileName = path + files[i];
			tableFactory.initializeComponentFactory("table", fileName);
			ComponentFactory.removeComponentFactory("table");
			try {
				String componentName = files[i].substring(0, files[i].lastIndexOf(".xml"));
				Object obj = tableFactory.getComponent(componentName);
				if (obj instanceof TableModel) {
					TableModel tableModel = (TableModel) obj;
					//tableModel.initializeOperationDef();
					this.addTableModel(tableModel);
					EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "Add tableModel[" + tableModel.getId()
							+ "] to TableModelService.");
				}
			} catch (Exception e) {
				EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.ERROR, 0, "failed to load the tableModel file !", e);
			}
		}
	}
	
	/**
	 * 调用IOC装载所有数据表模型定义。
	 * 
	 * @param tableModelPath
	 * 
	 */
	public void initTableModels(String tableModelPath) {
		File dir = new File(tableModelPath);
		if (!dir.exists() || !dir.isDirectory())
			return;

		String[] files = dir.list();
		if (files == null || files.length < 1)
			return;

		ComponentFactory tableFactory = new ComponentFactory();
		TableModelParser parser = new TableModelParser();
		tableFactory.setComponentParser(parser);

		for (int i = 0; i < files.length; i++) {
			if (!files[i].endsWith(".xml"))
				continue;
			String fileName = tableModelPath + files[i];
			tableFactory.initializeComponentFactory("table", fileName);
			ComponentFactory.removeComponentFactory("table");
			try {
				String componentName = files[i].substring(0, files[i].lastIndexOf(".xml"));
				Object obj = tableFactory.getComponent(componentName);
				if (obj instanceof TableModel) {
					TableModel tableModel = (TableModel) obj;
					this.addTableModel(tableModel);
					EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "Add tableModel[" + tableModel.getId()
							+ "] to TableModelService.");
				}
			} catch (Exception e) {
				EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.ERROR, 0, "failed to load the tableModel file !", e);
			}
		}
		
		EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "Load all the tableModel file success!");
	}
	
}
