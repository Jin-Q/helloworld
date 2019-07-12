package com.yucheng.cmis.pub;

import java.sql.Connection;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfTemplate;
/**
 * 
 * @Classname com.yucheng.cmis.pub.FNCFactory.java
 * @author wqgang
 * @Since 2009-4-22 下午02:06:57 
 * @Copyright yuchengtech
 * @version 1.0
 */
public class FNCFactory {
	
	/** 报表工厂实例化*/
	private static FNCFactory instance = new FNCFactory();

	/**
	 * 报表样式列表 
	 * KEY   :styleID
	 * Value :FncConfStyles对象
	 */
	private static Map<String,FncConfStyles> fncMap = null;
	private static Map<String,FncConfTemplate> fncTemplateMap = null;
	public static final String CONFIGKEY_MODULEID = "moduleid";
	public static final String CONFIGKEY_CLASSNAME = "classname";
	public static final String CONFIGKEY_DESCRIBE = "describe";
	
	/**
	 * <p>报表工厂初始化</p>
	 * <p>从数据库中读取报表样式列表的所有信息</p>
	 * @throws Exception
	 * @todo 将读取到的信息存放到HashMap中 
	 */
	public static void init(Context context, Connection conn){
		try{
			FNCQuery fncQuery = new FNCQuery();
			fncMap = fncQuery.getAllListFromDB(context, conn);
			fncTemplateMap=fncQuery.getTemplateFromDB(context, conn);
		}catch(Exception ex){
			ex.printStackTrace();
			//throw new ComponentException("报表工厂初始化失败，" + ex.getMessage());
		}
	}
	
	/**
	 * 根据报表样式编号从数据库中得到想对应的FncConfTemplate对象
	 * @param fncId
	 * @return
	 * @throws EMPException
	 */
	public static FncConfTemplate getTemplateInstance(String fncId) throws EMPException{

		if(fncTemplateMap == null || fncTemplateMap.size() <= 0){
			throw  new EMPException("报表工厂尚未初始化，请先调用初始化方法后再使用getFNCInstance方法");	
		}
		if(fncId == null || fncId.trim().equals("")){
			throw new EMPException("财报模板编号为空， 无法实例化");
		}
		
		FncConfTemplate fncConfTemplate = null;
		try {
			fncConfTemplate = (FncConfTemplate)fncTemplateMap.get(fncId);
		}catch(Exception e){
			throw new EMPException("不存在财报模板编号为"+fncId+"对象；" + e.toString());
		}
		return fncConfTemplate;
	}
	
	
	
	/**
	 * 根据报表样式编号从数据库中得到想对应的FncConfStyles对象,包括这张报表对应的所有item项目信息
	 * @param styleId
	 * @return
	 * @throws EMPException
	 */
	public static FncConfStyles getFNCInstance(String styleId) throws EMPException{

		if(fncMap == null || fncMap.size() <= 0){
			throw  new EMPException("报表工厂尚未初始化，请先调用初始化方法后再使用getFNCInstance方法");	
		}
		if(styleId == null || styleId.trim().equals("")){
			throw new EMPException("报表样式编号为空， 无法实例化");
		}
		
		FncConfStyles fncConfStyles = null;
		try {
			fncConfStyles = (FncConfStyles)fncMap.get(styleId);
		}catch(Exception e){
			throw new EMPException("不存在样式编号为"+styleId+"对象；" + e.toString());
		}
		//System.out.println(styleId+"      ///////////   "+fncConfStyles);
		//System.out.println(fncConfStyles.getStyleId() + " +++++++ " + fncConfStyles.getFncConfDisName());
		return fncConfStyles;
	}
	
	/** 
	 * <p>取得业务组件工厂实例</p>
	 * @return 业务组件工厂实例
	 */
	public static FNCFactory getFNCFactoryInstance() {
	   if(instance != null){
	     return instance;
	   } else {
		   return new FNCFactory();
	   }
	}

}
