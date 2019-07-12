package com.yucheng.cmis.pub;

import java.util.HashMap;
import java.util.ResourceBundle;

import com.ecc.emp.core.Context;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.util.ResourceUtils;
import com.yucheng.cmis.pub.util.XMLFileUtil;

/**
 * Agent工厂类
 * 
 * @Classname com.yucheng.cmis.pub.CMISAgentFactory.java
 * @author wqgang
 * @Since 2009-3-24 上午08:39:17
 * @Copyright yuchengtech
 * @version 1.0
 */
public class CMISAgentFactory {
	/**
	 * 工厂类实例
	 */
	private static CMISAgentFactory instance = new CMISAgentFactory();
	/**
	 *业务代理例表 其KEY对应组件ID，其值为对应组件的配置信息（格式仍为HashMap）
	 */
	private static HashMap agentTable = null;
	/**
	 * 配置文件(XML)agentid元素数据
	 */
	public static final String CONFIGKEY_AGENTID = "agentid";
	/**
	 * 配置文件(XML)classname元素数据
	 */
	public static final String CONFIGKEY_CLASSNAME = "classname";
	/**
	 * 配置文件(XML)describe元素数据
	 */
	public static final String CONFIGKEY_DESCRIBE = "describe";

	/**
	 * <p>
	 * 业务代理工厂初始化
	 * </p>
	 * <p>
	 * 从配置业务代理配置文件(xml)加载所有代理的配置信息 配置文件路径src/main/config/com/yucheng/cmis/config
	 * </p>
	 * 
	 * @throws AgentException
	 *             Agent异常
	 */
	public static void init() throws AgentException {

		try {
			XMLFileUtil xmlFileUtil = new XMLFileUtil();

			ResourceBundle res = ResourceBundle.getBundle("cmis");
			String dir = res.getString("component.config.file.dir");
			String AGENT_CONFIG_FILM_DIR = ResourceUtils.getFile(dir)
					.getAbsolutePath();
			agentTable = (HashMap) xmlFileUtil
					.readAgentFromXMLFile(AGENT_CONFIG_FILM_DIR);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AgentException(CMISMessage.MESSAGEDEFAULT, ex
					.getMessage());
		}
	}

	/**
	 * <p>
	 * 由业务代理ID获得业务代理实例
	 * </p>
	 * <p>
	 * 依据配置业务代理的配置实例化组件，并将配置信息设置到实例出的组件中
	 * </p>
	 * 
	 * @param agentId
	 *            业务代理ID
	 * @param context
	 *            EMP 结构
	 * @return 业务代理实例
	 * @throws AgentException
	 *             Agent异常
	 */
	/**
	 * <p>
	 * 由业务代理ID获得业务代理实例
	 * </p>
	 * <p>
	 * 依据配置业务代理的配置实例化组件，并将配置信息设置到实例出的组件中
	 * </p>
	 * 
	 * @param comId
	 *            业务代理ID
	 * @param context
	 *            EMP 结构
	 * @return 业务代理实例
	 * @throws AgentException
	 *             Agent异常
	 */
	public CMISAgent getAgentInstance(String agentId, Context context)
			throws AgentException {

		if (agentTable == null || agentTable.size() <= 0) {
			throw new AgentException(CMISMessage.MESSAGEDEFAULT,
					"业务代理列表尚未初始化，请先调用初始化方法后再使用getAgentInstance方法");
		}

		if (agentId == null || agentId.trim().equals("")) {
			throw new AgentException(CMISMessage.MESSAGEDEFAULT,
					"业务代理编号为空， 无法实例化业务代理");
		}
		CMISAgent agent = null;
		try {
			HashMap agentCfg = (HashMap) agentTable.get(agentId);
			if (agentCfg != null && agentCfg.size() > 0) {

				String st_classname = (String) agentCfg
						.get(CONFIGKEY_CLASSNAME);
				String st_describe = (String) agentCfg.get(CONFIGKEY_DESCRIBE);

				if (st_classname != null && !st_classname.trim().equals("")) {

					agent = (CMISAgent) Class.forName(st_classname)
							.newInstance();

					agent.setId(agentId);
					agent.setDescribe(st_describe);
					agent.setContext(context);
				} else {
					throw new AgentException(CMISMessage.MESSAGEDEFAULT,
							"业务代理[" + agentId + "]没有配置实现类（"
									+ CONFIGKEY_CLASSNAME + "）， 无法实例化业务代理");
				}
			} else {
				throw new AgentException(CMISMessage.MESSAGEDEFAULT, "业务代理["
						+ agentId + "]尚未配置，无法实例化");
			}
		} catch (InstantiationException e) {

			e.printStackTrace();
			throw new AgentException(e.getMessage());
		} catch (IllegalAccessException e) {

			e.printStackTrace();
			throw new AgentException(CMISMessage.MESSAGEDEFAULT, "初始化业务代理["
					+ agentId + "]失败，无权限访问," + e.getMessage());
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
			throw new AgentException(CMISMessage.MESSAGEDEFAULT, "初始化业务代理["
					+ agentId + "]失败，类不存在," + e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AgentException(CMISMessage.MESSAGEDEFAULT, "初始化业务代理["
					+ agentId + "]失败," + ex.getMessage());
		}
		return agent;

	}

	/**
	 * <p>
	 * 由业务代理ID获得业务代理实例
	 * </p>
	 * <p>
	 * 依据配置业务代理的配置实例化组件，并将配置信息设置到实例出的组件中
	 * </p>
	 * 
	 * @param comId
	 *            业务代理ID
	 * @return 业务代理实例
	 * @throws AgentException
	 *             Agent异常
	 */
	public CMISAgent getAgentInstance(String agentId) throws AgentException {

		if (agentTable == null || agentTable.size() <= 0) {
			throw new AgentException(CMISMessage.MESSAGEDEFAULT,
					"业务代理列表尚未初始化，请先调用初始化方法后再使用getAgentInstance方法");
		}

		if (agentId == null || agentId.trim().equals("")) {
			throw new AgentException(CMISMessage.MESSAGEDEFAULT,
					"业务代理编号为空， 无法实例化业务代理");
		}
		CMISAgent agent = null;
		try {
			HashMap agentCfg = (HashMap) agentTable.get(agentId);
			if (agentCfg != null && agentCfg.size() > 0) {

				String st_classname = (String) agentCfg
						.get(CONFIGKEY_CLASSNAME);
				String st_describe = (String) agentCfg.get(CONFIGKEY_DESCRIBE);

				if (st_classname != null && !st_classname.trim().equals("")) {

					agent = (CMISAgent) Class.forName(st_classname)
							.newInstance();

					agent.setId(agentId);
					agent.setDescribe(st_describe);

				} else {
					throw new AgentException(CMISMessage.MESSAGEDEFAULT,
							"业务代理[" + agentId + "]没有配置实现类（"
									+ CONFIGKEY_CLASSNAME + "）， 无法实例化业务代理");
				}
			} else {
				throw new AgentException(CMISMessage.MESSAGEDEFAULT, "业务代理["
						+ agentId + "]尚未配置，无法实例化");
			}
		} catch (InstantiationException e) {

			e.printStackTrace();
			throw new AgentException(e.getMessage());
		} catch (IllegalAccessException e) {

			e.printStackTrace();
			throw new AgentException(CMISMessage.MESSAGEDEFAULT, "初始化业务代理["
					+ agentId + "]失败，无权限访问," + e.getMessage());
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
			throw new AgentException(CMISMessage.MESSAGEDEFAULT, "初始化业务代理["
					+ agentId + "]失败，类不存在," + e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AgentException(CMISMessage.MESSAGEDEFAULT, "初始化业务代理["
					+ agentId + "]失败," + ex.getMessage());
		}
		return agent;

	}

	/**
	 * <p>
	 * 取得业务代理工厂实例
	 * </p>
	 * 
	 * @return 业务代理工厂实例
	 */
	public static CMISAgentFactory getAgentFactoryInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new CMISAgentFactory();
		}
	}
}
