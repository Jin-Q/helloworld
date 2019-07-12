package com.yucheng.cmis.pub;


import java.sql.Connection;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.ecc.emp.core.Context;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.ResourceUtils;
import com.yucheng.cmis.pub.util.XMLFileUtil;

public class CMISComponentFactory {
	/** 业务组件工厂实例 */
	private static CMISComponentFactory instance = new CMISComponentFactory();
	/** 业务组件例表 其KEY对应组件ID，其值为对应组件的配置信息（格式仍为HashMap） */
	private static HashMap componentTable = null;
	private static HashMap interfaceTable = null;

	public static final String CONFIGKEY_MODULEID = "moduleid";
	public static final String CONFIGKEY_CLASSNAME = "classname";
	public static final String CONFIGKEY_DESCRIBE = "describe";

	/**
	 * <p>
	 * 业务组件工厂初始化
	 * </p>
	 * <p>
	 * 从配置业务组件配置文件(componentcfg.xml)加载所有组件的配置信息
	 * </p>
	 * 
	 * @throws Exception
	 * @todo 需实现从配置文件中读，现只放测试用的配置数据
	 */
	public static void init() throws ComponentException {

		try {
			XMLFileUtil xmlFileUtil = new XMLFileUtil();

			ResourceBundle res = ResourceBundle.getBundle("cmis");
			String dir = res.getString("component.config.file.dir");
			String CONFIG_FILM_DIR = ResourceUtils.getFile(dir)
					.getAbsolutePath();

			componentTable = (HashMap) xmlFileUtil
					.readComponentFromXMLFile(CONFIG_FILM_DIR);

			interfaceTable = (HashMap) xmlFileUtil
					.readInterfaceFromXMLFile(CONFIG_FILM_DIR);
			/*
			 * componentTable = (HashMap) xmlFileUtil
			 * .readComponentFromXMLFile(PUBConstant.COMPONENT_CONFIG_FILM_DIR);
			 * interfaceTable = (HashMap) xmlFileUtil
			 * .readInterfaceFromXMLFile(PUBConstant.COMPONENT_CONFIG_FILM_DIR);
			 */

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"初始化组件配置失败，" + ex.getMessage());
		}
	}

	/**
	 * <p>
	 * 由业务组件ID获得业务组件实例
	 * </p>
	 * <p>
	 * 依据配置业务组件的配置实例化组件，并将配置信息设置到实例出的组件中
	 * </p>
	 * 
	 * @param comId
	 *            业务组件ID
	 * @return 业务组件实例
	 */
	private CMISComponent getComponentInstance(String comId)
			throws ComponentException {

		if (componentTable == null || componentTable.size() <= 0) {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"业务组件列表尚未初始化，请先调用初始化方法后再使用getComponentInstance方法");
		}

		if (comId == null || comId.trim().equals("")) {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"业务组件编号为空， 无法实例化业务组件");
		}
		CMISComponent component = null;
		try {
			HashMap componentCfg = (HashMap) componentTable.get(comId);
			if (componentCfg != null && componentCfg.size() > 0) {

				String st_classname = (String) componentCfg
						.get(CONFIGKEY_CLASSNAME);
				String st_describe = (String) componentCfg
						.get(CONFIGKEY_DESCRIBE);

				if (st_classname != null && !st_classname.trim().equals("")) {
					/** 实例化组件 */
					component = (CMISComponent) Class.forName(st_classname)
							.newInstance();
					/** 设置组件基本信息 */
					component.setId(comId);
					component.setDescribe(st_describe);
					component.setParameter(componentCfg);

				} else {
					throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
							"业务组件[" + comId + "]没有配置实现类（" + CONFIGKEY_CLASSNAME
									+ "）， 无法实例化业务组件");
				}
			} else {
				throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
						"业务组件[" + comId + "]尚未配置，无法实例化");
			}
		} catch (InstantiationException e) {
			
			e.printStackTrace();
			throw new ComponentException(e.getMessage());
		} catch (IllegalAccessException e) {
		
			e.printStackTrace();
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT, "初始化业务组件["
					+ comId + "]失败，无权访问类" + e.getMessage());
		} catch (ClassNotFoundException e) {
		
			e.printStackTrace();
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT, "初始化业务组件["
					+ comId + "]失败，对应的实现类不存在" + e.getMessage());
		}
		return component;

	}

	/**
	 * <p>
	 * 由业务组件ID获得业务组件实例
	 * </p>
	 * <p>
	 * 依据配置业务组件的配置实例化组件，并将配置信息设置到实例出的组件中，该方法只允许在op中使用
	 * </p>
	 * 
	 * @param comId
	 *            业务组件ID
	 * @param context
	 *            EMP 结构
	 * @return 业务组件实例
	 */
	public CMISComponent getComponentInstance(String comId, Context context,Connection connection)
			throws ComponentException {

		CMISComponent component = null;
		component=this.getComponentInstance(comId, context, true,connection);

		return component;

	}
	/**
	 * <p>
	 * 由业务组件ID获得业务组件实例
	 * </p>
	 * <p>
	 * 依据配置业务组件的配置实例化组件，并将配置信息设置到实例出的组件中
	 * </p>
	 * 
	 * @param comId
	 *            业务组件ID
	 * @param context
	 *            EMP 结构
	 * @param setConnInd
	 *            是否设置conn           
	 * @return 业务组件实例
	 */
	public CMISComponent getComponentInstance(String comId, Context context,
			boolean setConnInd,Connection connection) throws ComponentException {

		if (componentTable == null || componentTable.size() <= 0) {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"业务组件列表尚未初始化，请先调用初始化方法后再使用getComponentInstance方法");
		}

		if (comId == null || comId.trim().equals("")) {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"业务组件编号为空， 无法实例化业务组件");
		}
		
		CMISComponent component = null;
		try {
			HashMap componentCfg = (HashMap) componentTable.get(comId);
			if (componentCfg != null && componentCfg.size() > 0) {

				String st_classname = (String) componentCfg
						.get(CONFIGKEY_CLASSNAME);
				String st_describe = (String) componentCfg
						.get(CONFIGKEY_DESCRIBE);

				if (st_classname != null && !st_classname.trim().equals("")) {
					/** 实例化组件 */
					component = (CMISComponent) Class.forName(st_classname)
							.newInstance();
					/** 设置组件基本信息 */
					component.setId(comId);
					component.setDescribe(st_describe);
					component.setParameter(componentCfg);
					component.setContext(context);
					if(setConnInd==true){
					
						component.setConnection(connection);//component.getConnectionFromContext());
					}
					
				} else {
					throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
							"业务组件[" + comId + "]没有配置实现类（" + CONFIGKEY_CLASSNAME
									+ "）， 无法实例化业务组件");
				}
			} else {
				throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
						"业务组件[" + comId + "]尚未配置，无法实例化");
			}
		} catch (InstantiationException e) {
		
			e.printStackTrace();
			throw new ComponentException(e.getMessage());
		} catch (IllegalAccessException e) {
		
			e.printStackTrace();
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT, "初始化业务组件["
					+ comId + "]失败，无权访问类" + e.getMessage());
		} catch (ClassNotFoundException e) {
		
			e.printStackTrace();
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT, "初始化业务组件["
					+ comId + "]失败，对应的实现类不存在" + e.getMessage());
		}
		return component;

	}

	/**
	 * <p>
	 * 由业务组件接口ID获得业务组件接口实例
	 * </p>
	 * <p>
	 * 依据配置业务组件的配置实例化组件接口，并将配置信息设置到实例出的组件接口中
	 * </p>
	 * 
	 * @param comId
	 *            业务组件接口ID
	 * @param context
	 *            EMP 结构
	 * @return 业务组件接口实例
	 */
	public CMISComponent getComponentInterface(String interfaceId,
			Context context,Connection connection) throws ComponentException {

		CMISComponent component = null;
		component=this.getComponentInterface(interfaceId, context, true,connection);
		return component;

	}
	
	
	/**
	 * <p>
	 * 由业务组件接口ID获得业务组件接口实例
	 * </p>
	 * <p>
	 * 依据配置业务组件的配置实例化组件接口，并将配置信息设置到实例出的组件接口中
	 * </p>
	 * 
	 * @param comId
	 *            业务组件接口ID
	 * @param context
	 *            EMP 结构
	 * @param setConnInd
	 *            是否设置conn   
	 * @return 业务组件接口实例
	 */
	public CMISComponent getComponentInterface(String interfaceId,
			Context context,boolean setConnInd,Connection connection) throws ComponentException {

		if (interfaceTable == null || interfaceTable.size() <= 0) {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"业务组件接口列表尚未初始化，请先调用初始化方法后再使用getComponentInterface方法");
		}

		if (interfaceId == null || interfaceId.trim().equals("")) {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"业务组件接口编号为空， 无法实例化业务组件接口");
		}
		CMISComponent component = null;
		try {
			HashMap componentCfg = (HashMap) interfaceTable.get(interfaceId);
			if (componentCfg != null && componentCfg.size() > 0) {

				String st_classname = (String) componentCfg
						.get(CONFIGKEY_CLASSNAME);
				String st_describe = (String) componentCfg
						.get(CONFIGKEY_DESCRIBE);

				if (st_classname != null && !st_classname.trim().equals("")) {
					/** 实例化组件接口 */
					component = (CMISComponent) Class.forName(st_classname)
							.newInstance();
					/** 设置组件接口基本信息 */
					component.setId(interfaceId);
					component.setDescribe(st_describe);
					component.setParameter(componentCfg);
					component.setContext(context);
					
					if(setConnInd==true){
						component.setConnection(connection);//component.getConnectionFromContext());
					}
 

				} else {
					throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
							"业务组件接口[" + interfaceId + "]没有配置实现类（"
									+ CONFIGKEY_CLASSNAME + "）， 无法实例化业务组件接口");
				}
			} else {
				throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
						"业务组件接口[" + interfaceId + "]尚未配置，无法实例化");
			}
		} catch (InstantiationException e) {
		
			e.printStackTrace();
			throw new ComponentException(e.getMessage());
		} catch (IllegalAccessException e) {
		
			e.printStackTrace();
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"初始化业务组件接口[" + interfaceId + "]失败，无权访问类" + e.getMessage());
		} catch (ClassNotFoundException e) {
		
			e.printStackTrace();
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"初始化业务组件接口[" + interfaceId + "]失败，对应的实现类不存在"
							+ e.getMessage());
		}
		return component;

	}

	/**
	 * <p>
	 * 取得业务组件工厂实例
	 * </p>
	 * 
	 * @return 业务组件工厂实例
	 */
	public static CMISComponentFactory getComponentFactoryInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new CMISComponentFactory();
		}
	}

	/**
	 * 利用类名实例化类工厂
	 * 
	 * @param className
	 *            类名
	 * @return 类工厂
	 * @throws ComponentException
	 */
	public CMISComponent getComponentByClassName(String className,
			Context context,Connection connection) throws ComponentException {

		if (className == null || "".equals(className)) {
			throw new ComponentException("component类不能为空");
		}
		CMISComponent component = null;

		/** 实例化组件接口 */
		try {
			component = (CMISComponent) Class.forName(className).newInstance();
		} catch (InstantiationException e) {
		
			e.printStackTrace();
		} catch (IllegalAccessException e) {
		
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"初始化component[" + className + "]失败，无权访问类" + e.getMessage());
		} catch (ClassNotFoundException e) {
		
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"初始化component[" + className + "]失败，对应的实现类不存在"
							+ e.getMessage());
		}
		/** 设置组件接口基本信息 */
		component.setId("className");
		component.setDescribe("className");
		component.setParameter(null);
		component.setContext(context);

		return component;
	}

	/**
	 * 获取接口实例引用旧连接
	 * 
	 * @param interfaceId
	 * @return
	 * @throws ComponentException
	 */
	public CMISComponent getComponentInterfaceWithOldConnection(
			String interfaceId, Context context,Connection connection) throws ComponentException {

		if (interfaceTable == null || interfaceTable.size() <= 0) {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"业务组件接口列表尚未初始化，请先调用初始化方法后再使用getComponentInterface方法");
		}

		if (interfaceId == null || interfaceId.trim().equals("")) {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"业务组件接口编号为空， 无法实例化业务组件接口");
		}
		CMISComponent component = null;
		try {
			HashMap componentCfg = (HashMap) interfaceTable.get(interfaceId);
			if (componentCfg != null && componentCfg.size() > 0) {

				String st_classname = (String) componentCfg
						.get(CONFIGKEY_CLASSNAME);
				String st_describe = (String) componentCfg
						.get(CONFIGKEY_DESCRIBE);

				if (st_classname != null && !st_classname.trim().equals("")) {
					/** 实例化组件接口 */
					component = (CMISComponent) Class.forName(st_classname)
							.newInstance();
					/** 设置组件接口基本信息 */
					component.setId(interfaceId);
					component.setDescribe(st_describe);
					component.setParameter(componentCfg);
					component.setContext(context);
					component.setConnection(connection);
 

				} else {
					throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
							"业务组件接口[" + interfaceId + "]没有配置实现类（"
									+ CONFIGKEY_CLASSNAME + "）， 无法实例化业务组件接口");
				}
			} else {
				throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
						"业务组件接口[" + interfaceId + "]尚未配置，无法实例化");
			}
		} catch (InstantiationException e) {
		
			e.printStackTrace();
			throw new ComponentException(e.getMessage());
		} catch (IllegalAccessException e) {
		
			e.printStackTrace();
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"初始化业务组件接口[" + interfaceId + "]失败，无权访问类" + e.getMessage());
		} catch (ClassNotFoundException e) {
		
			e.printStackTrace();
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"初始化业务组件接口[" + interfaceId + "]失败，对应的实现类不存在"
							+ e.getMessage());
		}
		return component;

	}

	public CMISComponent getComponentInstanceWithOldCon(String comId,
			Context context,Connection connection) throws ComponentException {

		if (componentTable == null || componentTable.size() <= 0) {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"业务组件列表尚未初始化，请先调用初始化方法后再使用getComponentInstance方法");
		}

		if (comId == null || comId.trim().equals("")) {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
					"业务组件编号为空， 无法实例化业务组件");
		}
		CMISComponent component = null;
		try {
			HashMap componentCfg = (HashMap) componentTable.get(comId);
			if (componentCfg != null && componentCfg.size() > 0) {

				String st_classname = (String) componentCfg
						.get(CONFIGKEY_CLASSNAME);
				String st_describe = (String) componentCfg
						.get(CONFIGKEY_DESCRIBE);

				if (st_classname != null && !st_classname.trim().equals("")) {
					/** 实例化组件 */
					component = (CMISComponent) Class.forName(st_classname)
							.newInstance();
					/** 设置组件基本信息 */
					component.setId(comId);
					component.setDescribe(st_describe);
					component.setParameter(componentCfg);
					component.setContext(context);
					component.setConnection(connection);

				} else {
					throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
							"业务组件[" + comId + "]没有配置实现类（" + CONFIGKEY_CLASSNAME
									+ "）， 无法实例化业务组件");
				}
			} else {
				throw new ComponentException(CMISMessage.MESSAGEDEFAULT,
						"业务组件[" + comId + "]尚未配置，无法实例化");
			}
		} catch (InstantiationException e) {
		
			e.printStackTrace();
			throw new ComponentException(e.getMessage());
		} catch (IllegalAccessException e) {
		
			e.printStackTrace();
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT, "初始化业务组件["
					+ comId + "]失败，无权访问类" + e.getMessage());
		} catch (ClassNotFoundException e) {
		
			e.printStackTrace();
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT, "初始化业务组件["
					+ comId + "]失败，对应的实现类不存在" + e.getMessage());
		}
		return component;

	}
}
