package com.yucheng.cmis.pub.util;


import java.sql.Connection;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.domains.SRowright;
import com.yucheng.cmis.platform.organization.domains.SUser;
/**
 * 
 * @Classname com.yucheng.cmis.biz01line.fnc.interfaces.FNCFactory.java
 * @author wqgang
 * @Since 2009-4-22 下午02:06:57 
 * @Copyright yuchengtech
 * @version 1.0
 */
public class SInfoFactory { 
	
	/** 工厂实例化*/
	private static SInfoFactory instance = new SInfoFactory();

	
	public static Map<String, SOrg> getSOrgMap() {
		return sOrgMap;
	}

	public static Map<String, SUser> getSUserMap() {
		return sUserMap;
	}

	public static Map<String,SDuty> getSDutyMap() {
		return sDutyMap;
	}
	private static Map<String,SOrg> sOrgMap = null;
	private static Map<String,SDuty> sDutyMap = null;
	private static Map<String,SUser> sUserMap = null;
	public static Map<String, SRowright> SROWRIGHTMAP = null; //s_resource资源定义表
	public static final String CONFIGKEY_MODULEID = "moduleid";
	public static final String CONFIGKEY_CLASSNAME = "classname";
	public static final String CONFIGKEY_DESCRIBE = "describe";
	
	/**
	 * <p>系统工厂初始化</p>
	 * <p>从数据库中系统的所有信息</p>
	 * @throws Exception
	 * @todo 将读取到的信息存放到HashMap中 
	 */
	public static void init(Context context, Connection conn){
		try{
			SInfoQuery SInfoQuery = new SInfoQuery();
			sOrgMap = SInfoQuery.getSOrg(context, conn);
			sUserMap=SInfoQuery.getSUser(context, conn);
			sDutyMap=SInfoQuery.getSDuty(context, conn);
		}catch(Exception ex){
			ex.printStackTrace();
			//throw new ComponentException("报表工厂初始化失败，" + ex.getMessage());
		}
	}
	
	/**
	 * 根据报用户编号从数据库中得到想对应的用户对象
	 * @param fncId
	 * @return
	 * @throws EMPException
	 */
	public static SUser getSUserInstance(String actorno) throws EMPException{

		if(sUserMap == null || sUserMap.size() <= 0){
			throw  new EMPException("用户工厂尚未初始化，请先调用初始化方法后再使用getInstance方法");	
		}
		if(actorno == null || actorno.trim().equals("")){
			throw new EMPException("用户编号为空， 无法实例化");
		}
		
		SUser sUser = null;
		try {
			sUser = (SUser)sUserMap.get(actorno);
		}catch(Exception e){
			throw new EMPException("不存用户编号为"+actorno+"对象；" + e.toString());
		}
		return sUser;
	}
	
	
	
	/**
	 * 根据报机构编号从数据库中得到想对应的FncConfStyles对象,包括这张报表对应的所有item项目信息
	 * @param styleId
	 * @return
	 * @throws EMPException
	 */
	public static SOrg getSorgInstance(String organno) throws EMPException{

		if(sOrgMap == null || sOrgMap.size() <= 0){
			throw  new EMPException("机构工厂尚未初始化，请先调用初始化方法后再使用getInstance方法");	
		}
		if(organno == null || organno.trim().equals("")){
			throw new EMPException("机构编号为空， 无法实例化");
		}
		
		SOrg sOrg = null;
		try {
			sOrg= (SOrg)sOrgMap.get(organno);
		}catch(Exception e){
			throw new EMPException("不存在机构编号为"+organno+"对象；" + e.toString());
		}
		//System.out.println(styleId+"      ///////////   "+fncConfStyles);
		//System.out.println(fncConfStyles.getStyleId() + " +++++++ " + fncConfStyles.getFncConfDisName());
		return sOrg;
	}
	
	/**
	 * 岗位
	 * @param dutyNo
	 * @return
	 * @throws EMPException
	 */
	public static SDuty getSdutyInstance(String dutyNo) throws EMPException{

		if(sDutyMap == null || sDutyMap.size() <= 0){
			throw  new EMPException("机构工厂尚未初始化，请先调用初始化方法后再使用getInstance方法");	
		}
		if(dutyNo == null || dutyNo.trim().equals("")){
			throw new EMPException("岗位编号为空， 无法实例化");
		}
		
		SDuty sDuty = null;
		try {
			sDuty= (SDuty)sDutyMap.get(dutyNo);
		}catch(Exception e){
			throw new EMPException("不存在岗位编号为"+dutyNo+"对象；" + e.toString());
		}
		return sDuty;
	}
	
	/** 
	 * <p>取得业务组件工厂实例</p>
	 * @return 业务组件工厂实例
	 */
	public static SInfoFactory getSysInfoFactoryInstance() {
	   if(instance != null){
	     return instance;
	   } else {
		   return new SInfoFactory();
	   }
	}

	public static void initSrowright(Context context,Connection connection){ 
		SInfoQuery SInfoQuery = new SInfoQuery();
		SROWRIGHTMAP =SInfoQuery.initSrowright(context, connection);
	}
	
	

	/**
	 * 根据报用户编号从数据库中得到想对应的用户对象
	 * @param fncId
	 * @return
	 * @throws EMPException
	 */
	public static SRowright getSrowrightInstance(String resActorRight) throws EMPException{

		if(resActorRight == null || resActorRight.trim().equals("")){
			throw new EMPException("资源ID和用户权限组合 为空， 无法实例化");
		}
		SRowright sRowright=null;
		if(SROWRIGHTMAP != null && SROWRIGHTMAP.size() > 0){
			sRowright=SROWRIGHTMAP.get(resActorRight) ;
		}
		
		
		 
		return sRowright;
	}
	
	/**
	 * 根据报用户编号从数据库中得到想对应的用户对象
	 * @param fncId
	 * @return
	 * @throws EMPException
	 *//*
	public static String getSresourceInstance(String menuId) throws EMPException{

		if(menuId == null || menuId.trim().equals("")){
			throw new EMPException("资源ID为空， 无法实例化");
		}
		String restrict=null;
		if(SRESOURCEMAP != null && SRESOURCEMAP.size() > 0){
			restrict=SRESOURCEMAP.get(menuId) ;
		}
		
		
		 
		return restrict;
	}*/
	
}
