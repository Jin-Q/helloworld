package com.yucheng.cmis.platform.organization.msi;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.domains.SRole;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.exception.OrganizationException;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * <p>
 * 		组织机构管理模块对外提供的服务接口
 * </p>
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */ 
@ModualService(serviceId="organizationServices",serviceDesc="组织机构管理模块对外提供的常用服务接口",
				modualId="organization",modualName="组织机构管理模块",className="com.yucheng.cmis.standard.platform.organization.interfaces.OrganizationServiceInterface")
public interface OrganizationServiceInterface {

	/**
	 * 取系统中所有定义的角色
	 * 
	 * @param con 数据库连接
	 * @return 所有角色集合
	 */
	@MethodService(method="getAllRoles",desc="取系统中所有定义的角色",
					inParam={@MethodParam(paramName="con",paramDesc="数据库连接")},
					outParam={@MethodParam(paramName="List<SRole>",paramDesc="所有角色集合")})
	public List<SRole> getAllRoles(Connection con) throws OrganizationException ;
	
	/**
	 * 获得该用户拥有的角色集合
	 * @param userId 用户号
	 * @param con 数据库连接
	 * @return 该用户拥有角色集合
	 */
	@MethodService(method="getRolesByUserId",desc="获得该用户拥有的角色集合",
					inParam={	
								@MethodParam(paramName="userId",paramDesc="用户号"),
								@MethodParam(paramName="con",paramDesc="数据库连接")},
					outParam={@MethodParam(paramName="List<SRole>",paramDesc="该用户拥有角色集合")})
	public List<SRole> getRolesByUserId(String userId, Connection con) throws OrganizationException ;
	
	/**
	 * 获得系统中所有用户集合
	 * @param con 数据库连接
	 * @return 系统中所有用户集合
	 */
	@MethodService(method="getAllUsers",desc="获得系统中所有用户集合",
					inParam={@MethodParam(paramName="con",paramDesc="数据库连接")},
					outParam={@MethodParam(paramName="List<SUser>",paramDesc="系统中所有用户集合")})
	public List<SUser> getAllUsers(Connection con) throws OrganizationException ;
	
	/**
	 * 根据用户号获取该用户信息
	 * @param userId 用户号
	 * @param con 数据库连接
	 * @return 用户信息
	 * @throws OrganizationException
	 */
	@MethodService(method="getUserByUserId",desc="根据用户号获取该用户信息",
				inParam={	
					@MethodParam(paramName="userId",paramDesc="用户号"),
					@MethodParam(paramName="con",paramDesc="数据库连接")},
				outParam={@MethodParam(paramName="SUser",paramDesc="用户信息")})
	public SUser getUserByUserId(String userId, Connection con) throws OrganizationException;
	
	
	/**
	 * 获得该角色下所有用户集合
	 * @param roleId  角色号
	 * @param con 数据库连接
	 * @return 该角色下所有用户集合
	 * @throws OrganizationException
	 */
	@MethodService(method="getUsersByRoleId",desc="获得该角色下所有用户集合",
				inParam={	
					@MethodParam(paramName="roleId",paramDesc="角色号"),
					@MethodParam(paramName="con",paramDesc="数据库连接")},
				outParam={@MethodParam(paramName="List<SUser>",paramDesc="该角色下所有用户集合")})
	public List<SUser> getUsersByRoleId(String roleId,Connection con) throws OrganizationException;
	
	/**
	 * 获得该岗位下所有用户集合
	 * @param dutyId 岗位号
	 * @param con 数据库连接
	 * @return 该岗位下所有用户集合
	 * @throws OrganizationException
	 */
	@MethodService(method="getUsersByDeptId",desc="获得该岗位下所有用户集合",
				inParam={	
					@MethodParam(paramName="dutyId",paramDesc="岗位号"),
					@MethodParam(paramName="con",paramDesc="数据库连接")},
				outParam={@MethodParam(paramName="List<SUser>",paramDesc="该岗位下所有用户集合")})
	public List<SUser> getUsersByDutyId(String dutyId, Connection con) throws OrganizationException;
	
	/**
	 * 获得该机构下所有用户集合
	 * @param orgId 机构号
	 * @param con 数据库连接
	 * @return 该机构下所有用户集合
	 */
	@MethodService(method="getUsersByOrgId",desc="获得该机构下所有用户集合",
				inParam={	
					@MethodParam(paramName="orgId",paramDesc="机构号"),
					@MethodParam(paramName="con",paramDesc="数据库连接")},
				outParam={@MethodParam(paramName="List<SUser>",paramDesc="该机构下所有用户集合")})
	public List<SUser> getUsersByOrgId(String orgId, Connection con) throws OrganizationException ;
	
	
	/**
	 * 获得特定机构特定岗位下所有用户集合
	 * @param orgId 机构号
	 * @param dutyId 岗位号
	 * @param con 数据库连接
	 * @return 该岗位下所有用户集合
	 * @throws OrganizationException
	 */
	@MethodService(method="getUsersByOrgIdAndDutyId",desc="获得特定机构特定岗位下所有用户集合",
				inParam={	
			        @MethodParam(paramName="orgId",paramDesc="岗位号"),
					@MethodParam(paramName="dutyId",paramDesc="岗位号"),
					@MethodParam(paramName="con",paramDesc="数据库连接")},
				outParam={@MethodParam(paramName="List<SUser>",paramDesc="该特定机构特定岗位下所有用户集合")})
	public List<SUser> getUsersByOrgIdAndDutyId(String orgId,String dutyId, Connection con) throws OrganizationException;
	
	/**
	 * 获得该机构及其下属机构下所有用户集合
	 * @param orgId 机构号
	 * @param con 数据库连接
	 * @return 该机构及其下属机构下所有用户集合
	 */
	@MethodService(method="getOrgAndSubOrgUsers",desc="获得该机构及其下属机构下所有用户集合",
				inParam={	
					@MethodParam(paramName="orgId",paramDesc="机构号"),
					@MethodParam(paramName="con",paramDesc="数据库连接")},
				outParam={@MethodParam(paramName="List<SUser>",paramDesc="该机构及其下属机构下所有用户集合")})
	public List<SUser> getOrgAndSubOrgUsers(String orgId, Connection con) throws OrganizationException ;
	
	/**
	 * 获得系统中所有机构集合
	 * @param con 数据库连接
	 * @return 系统中所有机构集合
	 * @throws Exception
	 */
	@MethodService(method="getAllOrgs",desc="获得系统中所有机构集合",
				inParam={	
					@MethodParam(paramName="con",paramDesc="数据库连接")},
				outParam={@MethodParam(paramName="List<SOrg>",paramDesc="系统中所有机构集合")})
	public List<SOrg> getAllOrgs(Connection con) throws OrganizationException;
	
	/**
	 * 根据机构号获得该机构信息
	 * @param orgId 机构号
	 * @param con 数据库连接
	 * @return SOrg 机构信息
	 * @throws Exception
	 */
	@MethodService(method="getOrgByOrgId",desc="根据机构号获得该机构信息",
				inParam={	
					@MethodParam(paramName="orgId",paramDesc="机构号"),
					@MethodParam(paramName="con",paramDesc="数据库连接")},
				outParam={@MethodParam(paramName="SOrg",paramDesc="机构信息")})
	public SOrg getOrgByOrgId(String orgId, Connection con) throws OrganizationException;
	
	/**
	 * 获得系统中所有岗位集合
	 * @param con 数据库连接
	 * @return 系统中所有岗位集合
	 * @throws OrganizationException
	 */
	@MethodService(method="getAllDutys",desc="获得系统中所有岗位集合",
				inParam={	
					@MethodParam(paramName="con",paramDesc="数据库连接")},
				outParam={@MethodParam(paramName="List<SDuty>",paramDesc="系统中所有岗位集合")})
	public List<SDuty> getAllDutys(Connection con) throws OrganizationException;
	
	/**
	 * 获得该用户拥有的所有岗位
	 * @param userId 用户号
	 * @param con 数据库连接
	 * @return 该用户拥有的所有岗位
	 * @throws OrganizationException
	 */
	@MethodService(method="getDutysByUserId",desc="获得该用户拥有的所有岗位",
				inParam={	
					@MethodParam(paramName="userId",paramDesc="用户号"),
					@MethodParam(paramName="con",paramDesc="数据库连接")},
				outParam={@MethodParam(paramName="List<SDuty>",paramDesc="该用户拥有的所有岗位")})
	public List<SDuty> getDutysByUserId(String userId, Connection con) throws OrganizationException;
	
	/**
	 * 获取根机构信息
	 * @param connection 数据库连接
	 * @return 根机构信息
	 */
	@MethodService(method="getRootOrg",desc="获取根机构信息",
			inParam={@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="SOrg",paramDesc="根机构对象")})
	public SOrg getRootOrg(Connection connection) throws OrganizationException;
	
	/**
	 * 返回机构下所有的直属子机构（不含多级子机构）
	 * @param orgId 父机构id
	 * @param connection 数据库连接
	 * @return 机构下所有的直属子机构
	 */
	@MethodService(method="getDirectSubOrgs",desc="返回机构下所有的直属子机构（不含多级子机构）",
			inParam={@MethodParam(paramName="orgId",paramDesc="父机构id"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="sOrgList",paramDesc="机构下所有的直属子机构")})
	public List<SOrg> getDirectSubOrgs(String orgId, Connection connection) throws OrganizationException;
	
	/**
	 * <p>根据机构号获取上级机构</p>
	 * @param orgId 机构号
	 * @return 上级机构
	 */
	@MethodService(method="getSupOrg",desc="根据机构号获取上级机构",
			inParam={@MethodParam(paramName="orgId",paramDesc="机构id"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="supOrg",paramDesc="上级机构")})
	public SOrg getSupOrg(String orgId, Connection connection) throws OrganizationException;
	
	/**
	 * <p>返回机构下所有的子机构（包含多级子机构）</p>
	 * @param orgId 机构号
	 * @param connection 数据库连接
	 * @return List<SOrg>
	 * @throws OrganizationException
	 */
	@MethodService(method="getAllSubOrgs",desc="返回机构下所有的子机构（包含多级子机构）",
			inParam={@MethodParam(paramName="orgId",paramDesc="机构id"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List<SOrg>",paramDesc="所有的子机构")})
	public List<SOrg> getAllSubOrgs(String orgId, Connection connection) throws OrganizationException;
	
	/**
	 * 获取orgId的所有上级机构(同一机构线上)
	 * @param orgId 机构Id
	 * @param connection 数据库连接
	 * @return List中放机构号
	 * @throws OrganizationException
	 */
	@MethodService(method="getSameOrgLine",desc="获取orgId的所有上级机构(同一机构线上)",
			inParam={@MethodParam(paramName="orgId",paramDesc="机构id"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List<SOrg>",paramDesc="同一机构线上的机构")})
	public List<SOrg> getSameOrgLine(String orgId, Connection connection) throws OrganizationException;
	
	/**
	 * 根据角色名称模糊查询角色
	 * @param roleName 模糊角色名称
	 * @param connection 数据库连接
	 * @return List&lt;SRole&gt;
	 * @throws OrganizationException
	 */
	@MethodService(method="getRolesByName",desc="根据角色名称模糊查询角色",
			inParam={@MethodParam(paramName="roleName",paramDesc="模糊角色名称"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List<SRole>",paramDesc="角色list")})
	public List<SRole> getRolesByName(String roleName, Connection connection) throws OrganizationException;
	
	/**
	 * 根据用户名称模糊匹配所有用户
	 * @param userName 模糊用户名称
	 * @param connection 数据库连接
	 * @return List&lt;SUser>&gt;
	 * @throws OrganizationException
	 */
	@MethodService(method="getUsersByName",desc="根据用户名称模糊匹配所有用户",
			inParam={@MethodParam(paramName="userName",paramDesc="模糊用户名称"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List<SUser>",paramDesc="用户list")})
	public List<SUser> getUsersByName(String userName, Connection connection) throws OrganizationException;
	
	
	/**
	 * 根据用户码获取该用户所有机构信息
	 * @param actorNo 用户码
	 * @param connection 数据库连接
	 * @return List&lt;SUser>&gt;
	 * @throws OrganizationException
	 */
	@MethodService(method="getDeptOrgByActno",desc="根据用户码获取该用户所有机构信息",
			inParam={@MethodParam(paramName="actorNo",paramDesc="用户码"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List<SUser>",paramDesc="用户list")})
	public List<SOrg> getDeptOrgByActno(String actorNo, Connection connection) throws OrganizationException;
	/**
	 * 获得该用户拥有的托管用户集合
	 * @param userId 用户号
	 * @param con 数据库连接
	 * @return 该用户拥有托管用户集合
	 */
	@MethodService(method="getCusTrusteeByUserId",desc="获得该用户拥有的托管用户集合",
					inParam={	
								@MethodParam(paramName="userId",paramDesc="用户号"),
								@MethodParam(paramName="con",paramDesc="数据库连接")},
					outParam={@MethodParam(paramName="IndexedCollection",paramDesc="该用户拥有的托管用户集合")})
	public IndexedCollection getCusTrusteeByUserId(String userId, Connection con) throws OrganizationException ;
}
