package com.yucheng.cmis.platform.organization.initializer;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.BusinessInitializer;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.pub.dao.SqlClient;

/**
 * 组织机构在系统加载时需要初始化的信息类
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
public class OrganizationInitializer implements BusinessInitializer{
	public static Map<String, String> usersMap = new HashMap<String, String>();
	public static Map<String, String> orgsMap = new HashMap<String, String>();
	public static Map<String, String> rolesMap = new HashMap<String, String>();
	public static Map<String, String> dutysMap = new HashMap<String, String>();
	public static boolean isInit = false;
	
	/**
	 * 加载所有组织机构的ID-NAME信息
	 */
	public void initialize(Context rootCtx, Connection connection)
			throws Exception {
		
		EMPLog.log(CMISConstance.CMIS_INITIALIZER, EMPLog.DEBUG, 0, "organization模块初始化操作：加载机构、岗位、用户缓存信息...");
		
		//加载用户信息
		List<SUser> userList = (List<SUser>)SqlClient.queryList("queryAllUsers", "", connection);
		for (int i = 0; i < userList.size(); i++) {
			SUser user = userList.get(i);
			usersMap.put(user.getActorno(), user.getActorname());
		}
		
		//加载机构信息
		List<SOrg> orgList = (List<SOrg>)SqlClient.queryList("queryAllOrgs", "", connection);
		for (int i = 0; i < orgList.size(); i++) {
			SOrg org = orgList.get(i);
			orgsMap.put(org.getOrganno(), org.getOrganname());
		}
		
		//加载岗位信息
		List<SDuty> dutyList = (List<SDuty>)SqlClient.queryList("queryAllDutys", "", connection);
		for (int i = 0; i < dutyList.size(); i++) {
			SDuty duty = dutyList.get(i);
			dutysMap.put(duty.getDutyno(),duty.getDutyname());
		}
		
		isInit = true;
	}
	
	
	/**
	 * 添加机构码、机构名缓存
	 * @param orgId
	 * @param orgName
	 */
	public static void addAndUpdateOrgMapInfo(String orgId, String orgName){
		orgsMap.put(orgId,orgName);
	}
	
	/**
	 * 删除机构码、机构名缓存
	 * @param orgId
	 * @param orgName
	 */
	public static void removeOrgMapInfo(String orgId){
		orgsMap.remove(orgId);
	}
	
	/**
	 * 添加用户码、用户名缓存
	 * @param userId
	 * @param userName
	 */
	public static void addAndUpdateUserMapInfo(String userId, String userName){
		usersMap.put(userId, userName);
	}
	
	/**
	 * 删除用户缓存
	 * @param userId
	 */
	public static void removeUserMapInfo(String userId){
		usersMap.remove(userId);
	}
	
	/**
	 * 添加岗位码、岗位名缓存
	 * @param dutyId
	 * @param dutyName
	 */
	public static void addAndUpdateDutyMapInfo(String dutyId, String dutyName){
		dutysMap.put(dutyId, dutyName);
	}
	
	/**
	 * 删除岗位缓存
	 * @param dutyId
	 */
	public static void removeDutyMapInfo(String dutyId){
		dutysMap.remove(dutyId);
	}

}
