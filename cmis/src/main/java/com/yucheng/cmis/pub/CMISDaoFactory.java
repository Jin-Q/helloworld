package com.yucheng.cmis.pub;

import java.util.HashMap;
import java.util.ResourceBundle;

import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;
import com.yucheng.cmis.pub.util.ResourceUtils;
import com.yucheng.cmis.pub.util.XMLFileUtil;



public class CMISDaoFactory {
	/** 业务组件工厂实例*/
	private static CMISDaoFactory instance = new CMISDaoFactory();
	/** 业务组件例表 其KEY对应组件ID，其值为对应组件的配置信息（格式仍为HashMap） */
	private static HashMap daoTable = new HashMap();
	
	public static final String CONFIGKEY_MODULEID = "moduleid";
	public static final String CONFIGKEY_CLASSNAME = "classname";
	public static final String CONFIGKEY_DESCRIBE = "describe";
	
	/**
	 * <p>业务组件工厂初始化</p>
	 * <p>从配置业务组件配置文件(daocfg.xml)加载所有组件的配置信息</p>
	 * @throws Exception
	 * @todo 需实现从配置文件中读，现只放测试用的配置数据 
	 */
	public static void init() throws DaoException{
	/*	if(daoTable == null){
			daoTable = new HashMap();
		}
	
		*/
		try {
			XMLFileUtil xmlFileUtil = new XMLFileUtil();

			ResourceBundle res = ResourceBundle.getBundle("cmis");
			String dir = res.getString("component.config.file.dir");
			String DAO_CONFIG_FILM_DIR = ResourceUtils.getFile(dir)
					.getAbsolutePath();
			daoTable = (HashMap) xmlFileUtil
					.readDaoFromXMLFile(DAO_CONFIG_FILM_DIR);
			// agentTable =
			// (HashMap)xmlFileUtil.readAgentFromXMLFile(PUBConstant.COMPONENT_CONFIG_FILM_DIR);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				throw new AgentException(CMISMessage.MESSAGEDEFAULT, ex
						.getMessage());
			} catch (AgentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * <p>由业务组件ID获得业务组件实例</p>
	 * <p>依据配置业务组件的配置实例化组件，并将配置信息设置到实例出的组件中</p>
	 * @param comId 业务组件ID
	 * @return 业务组件实例
	 */
	public CMISDao getDaoInstance(String comId) throws DaoException {
		
		if(daoTable == null || daoTable.size() <= 0){
			throw new DaoException("业务组件列表尚未初始化，请先调用初始化方法后再使用getdaoInstance方法");	
		}
		
		if(comId == null || comId.trim().equals("")){
			throw new DaoException("业务组件编号为空， 无法实例化业务组件");
		}
		CMISDao dao = null;
		try {
		    HashMap daoCfg = (HashMap)daoTable.get(comId);
		    if(daoCfg != null && daoCfg.size() > 0){
		    	
		    	String st_classname = (String)daoCfg.get(CONFIGKEY_CLASSNAME);
		    	String st_describe  = (String)daoCfg.get(CONFIGKEY_DESCRIBE);
		        
		    	if(st_classname != null && !st_classname.trim().equals("")){
		    	   /** 实例化组件*/
		    	   dao = (CMISDao)Class.forName(st_classname).newInstance();
		    	   /** 设置组件基本信息*/
		    	   dao.setId(comId);
		    	   dao.setDescribe(st_describe);
		    	   dao.setParameter(daoCfg);
		    	   
		    	} else {
		    	   throw new DaoException("业务组件[" + comId + "]没有配置实现类（" + CONFIGKEY_CLASSNAME + "）， 无法实例化业务组件");
		    	}
		    } else {
		    	throw new DaoException("业务组件[" + comId + "]尚未配置，无法实例化");
		    }
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DaoException("初始化业务组件[" + comId + "]失败，无权访问类" + e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DaoException("初始化业务组件[" + comId + "]失败，对应的实现类不存在" + e.getMessage());
		}
		return dao;
		
	}
	
	/** 
	 * <p>取得业务组件工厂实例</p>
	 * @return 业务组件工厂实例
	 */
	public static CMISDaoFactory getDaoFactoryInstance() {
	   if(instance != null){
	     return instance;
	   } else {
		   return new CMISDaoFactory();
	   }
	}
}
