package com.yucheng.cmis.platform.organization.msi;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.platform.organization.exception.OrganizationException;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * 
 * 组织机构管理模块对外提供的缓存服务
 * <p>
 * 	提供机构、用户、岗位的ID-NAME的缓存服务
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
@ModualService(serviceId="organizationCacheServices",serviceDesc="组织机构管理模块对外提供的缓存服务接口",
		modualId="organization",modualName="组织机构管理模块",className="com.yucheng.cmis.standard.platform.organization.interfaces.OrganizationCacheServiceInterface")
public interface OrganizationCacheServiceInterface {

	
	/**
	 * 将iColl中的机构码转为机构名称
	 * <p>在IndexedCollection中新增key=args[i]_displayname,value=args[i]的机构名称的DataField
	 * 
	 * @param iColl IndexedCollection
	 * @param args iColl中放机构号的key
	 * @throws OrganizationException 异常
	 */
	@MethodService(method="addOrgName",desc="将iColl中的机构码转为机构名称",
					inParam={
						@MethodParam(paramName="iColl",paramDesc="IndexedCollection"),
						@MethodParam(paramName="args",paramDesc="iColl中放机构号的key")
					},
					outParam=@MethodParam(paramName="iColl",paramDesc="IndexedCollection"))
	public IndexedCollection addOrgName(IndexedCollection iColl, String[] args)throws OrganizationException;
	
	
	/**
	 * 
	 * 将kColl中的机构码转为机构名称
	 * <p>在KeyedCollection中新增key=args[i]_displayname,value=args[i]的机构名称的DataField
	 * 
	 * @param iColl IndexedCollection
	 * @param args iColl中放机构号的key
	 * @throws OrganizationException 异常
	 */
	@MethodService(method="addOrgName",desc="将kColl中的机构码转为机构名称",
					inParam={
						@MethodParam(paramName="kColl",paramDesc="KeyedCollection"),
						@MethodParam(paramName="args",paramDesc="kColl中放机构号的key")
					},
					outParam=@MethodParam(paramName="kColl",paramDesc="KeyedCollection"))
	public KeyedCollection addOrgName(KeyedCollection kColl, String[] args)throws OrganizationException;
	
	
	/**
	 * 将iColl中的用户码转为用户名称
	 * <p>在IndexedCollection中新增key=args[i]_displayname,value=args[i]的用户名称的DataField
	 * 
	 * @param iColl IndexedCollection
	 * @param args iColl中放用户码的key
	 * @throws OrganizationException 异常
	 */
	@MethodService(method="addUserName",desc="将iColl中的用户码转为用户名称",
					inParam={
						@MethodParam(paramName="iColl",paramDesc="IndexedCollection"),
						@MethodParam(paramName="args",paramDesc="kColl中放用户码的key")
					},
					outParam=@MethodParam(paramName="iColl",paramDesc="IndexedCollection"))
	public IndexedCollection addUserName(IndexedCollection iColl, String[] args)throws OrganizationException;
	
	/**
	 * 将kColl中的用户码转为用户名称
	 * <p>在KeyedCollection中新增key=args[i]_displayname,value=args[i]的用户名称的DataField
	 * 
	 * @param iColl IndexedCollection
	 * @param args iColl中放用户码的key
	 * @throws OrganizationException 异常
	 */
	@MethodService(method="addUserName",desc="将kColl中的用户码转为用户名称",
					inParam={
						@MethodParam(paramName="kColl",paramDesc="KeyedCollection"),
						@MethodParam(paramName="args",paramDesc="kColl中放用户码的key")
					},
					outParam=@MethodParam(paramName="kColl",paramDesc="KeyedCollection"))
	public KeyedCollection addUserName(KeyedCollection kColl, String[] args) throws OrganizationException;
	
	
	/**
	 * 将iColl中的岗位码转为岗位名称
	 * 
	 * 在IndexedCollection中新增key=args[i]_displayname,value=args[i]的岗位名称的DataField
	 * 
	 * @param iColl IndexedCollection
	 * @param args iColl中放岗位码的key
	 * @throws OrganizationException 异常
	 */
	@MethodService(method="addDutyName",desc="将iColl中的岗位码转为岗位名称",
					inParam={
						@MethodParam(paramName="iColl",paramDesc="IndexedCollection"),
						@MethodParam(paramName="args",paramDesc="kColl中放用户码的key")
					},
					outParam=@MethodParam(paramName="iColl",paramDesc="IndexedCollection"))
	public IndexedCollection addDutyName(IndexedCollection iColl, String[] args)throws OrganizationException;
	
	/**
	 * 将kColl中的岗位码转为岗位名称
	 * 
	 * 在KeyedCollection中新增key=args[i]_displayname,value=args[i]的岗位名称的DataField
	 * 
	 * @param iColl IndexedCollection
	 * @param args iColl中放岗位码的key
	 * @throws OrganizationException 异常
	 */
	@MethodService(method="addDutyName",desc="将kColl中的岗位码转为岗位名称",
					inParam={
						@MethodParam(paramName="kColl",paramDesc="KeyedCollection"),
						@MethodParam(paramName="args",paramDesc="kColl中放用户码的key")
					},
					outParam=@MethodParam(paramName="kColl",paramDesc="KeyedCollection"))
	public KeyedCollection addDutyName(KeyedCollection kColl, String[] args)throws OrganizationException;
	
	/**
	 * 根据用户码获得用户名
	 * @param userId 用户码
	 * @return 用户名
	 * @throws OrganizationException
	 */
	@MethodService(method="getUserName",desc="根据用户码获得用户名",
					inParam=@MethodParam(paramName="userId",paramDesc="用户码"),
					outParam=@MethodParam(paramName="String",paramDesc="用户名"))
	public String getUserName(String userId)throws OrganizationException;
	
	
	/**
	 * 根据机构码获得用户名
	 * @param userId 机构码
	 * @return 机构名
	 * @throws OrganizationException
	 */
	@MethodService(method="getOrgName",desc="根据机构码获得用户名",
					inParam=@MethodParam(paramName="orgId",paramDesc="机构码"),
					outParam=@MethodParam(paramName="String",paramDesc="机构名"))
	public String getOrgName(String orgId)throws OrganizationException;
	
	
	/**
	 * 根据岗位码获得用户名
	 * @param userId 岗位码
	 * @return 岗位名
	 * @throws OrganizationException
	 */
	@MethodService(method="getDutyName",desc="根据岗位码获得用户名",
					inParam=@MethodParam(paramName="dutyId",paramDesc="岗位码"),
					outParam=@MethodParam(paramName="String",paramDesc="岗位名"))
	public String getDutyName(String dutyId)throws OrganizationException;
	
	
}
