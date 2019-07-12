package com.yucheng.cmis.platform.organization.msi.msiimple;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.domains.SRole;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.exception.OrganizationException;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISModualService;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.dao.SqlClient;

/**
 * <p>
 * 		组织机构管理模块对外提供的服务接口实现类
 * </p>
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
public class OrganizationServiceImple extends CMISModualService implements OrganizationServiceInterface{

	
	/**
	 * 取系统中所有定义的角色
	 * 
	 * @param con 数据库连接
	 * @return 所有角色集合
	 */
	public List<SRole> getAllRoles(Connection con) throws OrganizationException{
		List<SRole> retList = new ArrayList<SRole>();
		try {
			retList = (List<SRole>)SqlClient.queryList("queryAllRoles", "", con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		}
		
		return retList;
	}

	/**
	 * 获得该用户拥有的角色集合
	 * @param userId 用户号
	 * @param con 数据库连接
	 * @return 该用户拥有角色集合
	 */
	public List<SRole> getRolesByUserId(String userId, Connection con)throws OrganizationException{
		List<SRole> retList = new ArrayList<SRole>();
		try {
			retList = (List<SRole>)SqlClient.queryList("queryRolesByUserId", userId, con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		} 
		
		return retList;
	}
	
	/**
	 * 获得特定机构特定岗位下所有用户集合
	 * @param con 数据库连接
	 * @return 获得系统中所有用户集合
	 */
	public List<SUser> getUsersByOrgIdAndDutyId(String orgId,String dutyId, Connection con) throws OrganizationException{
		List<SUser> retList = new ArrayList<SUser>();
		try {
			HashMap map= new HashMap();
			map.put("orgId", orgId);
			map.put("dutyId", dutyId);
			retList = (List<SUser>)SqlClient.queryList("queryUsersByOrgIdAndDutyId", map, con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		} 
		
		return retList;
	}

	
	/**
	 * 获得系统中所有用户集合
	 * @param con 数据库连接
	 * @return 获得系统中所有用户集合
	 */
	public List<SUser> getAllUsers(Connection con) throws OrganizationException{
		List<SUser> retList = new ArrayList<SUser>();
		try {
			retList = (List<SUser>)SqlClient.queryList("queryAllUsers", "", con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		} 
		
		return retList;
	}

	/**
	 * 获得该机构下所有用户集合
	 * @param orgId 机构号
	 * @param con 数据库连接
	 * @return 该机构下所有用户集合
	 */
	public List<SUser> getUsersByOrgId(String orgId, Connection con) throws OrganizationException{
		List<SUser> retList = new ArrayList<SUser>();
		try {
			retList = (List<SUser>)SqlClient.queryList("queryUsersByOrgId", orgId, con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		} 
		
		return retList;
	}

	/**
	 * 获得该机构及其下属机构下所有用户集合
	 * @param orgId 机构号
	 * @param con 数据库连接
	 * @return 该机构及其下属机构下所有用户集合
	 */
	public List<SUser> getOrgAndSubOrgUsers(String orgId, Connection con) throws OrganizationException{
		List<SUser> retList = new ArrayList<SUser>();
		try {
			retList = (List<SUser>)SqlClient.queryList("queryOrgAndSubOrgUsers", "%"+orgId+"%", con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		} 
		return retList;
	}

	/**
	 * 获得系统中所有机构集合
	 * @param con 数据库连接
	 * @return 系统中所有机构集合
	 * @throws Exception
	 */
	public List<SOrg> getAllOrgs(Connection con) throws OrganizationException{
		List<SOrg> retList = new ArrayList<SOrg>();
		try {
			retList = (List<SOrg>)SqlClient.queryList("queryAllOrgs", null, con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		} 
		return retList;
	}

	/**
	 * 根据机构号获得该机构信息
	 * @param orgId 机构号
	 * @param con 数据库连接
	 * @return SOrg
	 * @throws Exception
	 */
	public SOrg getOrgByOrgId(String orgId, Connection con) throws OrganizationException{
		SOrg retOrg = null;
		try {
			retOrg = (SOrg)SqlClient.queryFirst("queryOrgByOrgId", orgId, null, con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		} 
		return retOrg;
	}

	/**
	 * 获得系统中所有岗位集合
	 * @param con 数据库连接
	 * @return 系统中所有岗位集合
	 * @throws OrganizationException
	 */
	public List<SDuty> getAllDutys(Connection con) throws OrganizationException {
		List<SDuty> retList = new ArrayList<SDuty>();
		try {
			retList = (List<SDuty>)SqlClient.queryList("queryAllDutys", null, con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		} 
		return retList;
	}

	/**
	 * 获得该用户拥有的所有岗位
	 * @param userId 用户号
	 * @param con 数据库连接
	 * @return 该用户拥有的所有岗位
	 * @throws OrganizationException
	 */
	public List<SDuty> getDutysByUserId(String userId, Connection con) throws OrganizationException {
		List<SDuty> retList = new ArrayList<SDuty>();
		try {
			retList = (List<SDuty>)SqlClient.queryList("queryDutysByuserId", userId, con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		} 
		return retList;
	}

	/**
	 * 根据用户号获取该用户信息
	 * @param userId 用户号
	 * @param con 数据库连接
	 * @return 用户信息
	 * @throws OrganizationException
	 */
	public SUser getUserByUserId(String userId, Connection con) throws OrganizationException {
		SUser user = null;
		try {
			user = (SUser)SqlClient.queryFirst("queryUsersByUserId", userId, null, con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		} 
		return user;
	}

	/**
	 * 获得该岗位下所有用户集合
	 * @param dutyId 岗位号
	 * @param con 数据库连接
	 * @return 该岗位下所有用户集合
	 * @throws OrganizationException
	 */
	public List<SUser> getUsersByDutyId(String dutyId, Connection con) throws OrganizationException {
		List<SUser> retList = new ArrayList<SUser>();
		try {
			retList = (List<SUser>)SqlClient.queryList("queryUsersByDutyId", dutyId, con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		} 
		return retList;
	}

	/**
	 * 获得该角色下所有用户集合
	 * @param roleId  角色号
	 * @param con 数据库连接
	 * @return 该角色下所有用户集合
	 * @throws OrganizationException
	 */
	public List<SUser> getUsersByRoleId(String roleId, Connection con) throws OrganizationException {
		List<SUser> retList = new ArrayList<SUser>();
		try {
			retList = (List<SUser>)SqlClient.queryList("queryUsersByRoleId", roleId, con);
		} catch (Exception e) {
			throw new OrganizationException(e);
		} 
		return retList;
	}
	
	public SOrg getRootOrg(Connection connection) throws OrganizationException{
		SOrg rootOrg = new SOrg();
		List<SOrg> orgs = getAllOrgs(connection);
		for(SOrg org : orgs) {
			String orgId = org.getOrganno();
			String supOrgId = org.getSuporganno();
			//如果某机构的上级机构号为空或上级机构号与本机构号相同，则认为此机构是根机构
			if(supOrgId==null||supOrgId.trim().equals("")||supOrgId.trim().equals("")||orgId.equals(supOrgId)) {
				rootOrg = org;
				break;
			}
		}
		return rootOrg;
	}
	
	public List<SOrg> getDirectSubOrgs(String orgId, Connection connection) throws OrganizationException {
		List<SOrg> orgs = new ArrayList<SOrg>();
		try {
			orgs = (List<SOrg>)SqlClient.queryList("queryDirectSubOrgs", orgId, connection);
		} catch (Exception e) {
			throw new OrganizationException(e);
		}
		return orgs;		
	}

	public SOrg getSupOrg(String orgId, Connection connection) throws OrganizationException {
		SOrg supOrg = new SOrg();
		String sqlId = "getSupOrganNo";
		try {
			String supOrgId = (String) SqlClient.queryFirst(sqlId, orgId, null, connection);
			supOrg = getOrgByOrgId(supOrgId, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new OrganizationException(e);
		}
		return supOrg;
	}
	
	public List<SOrg> getAllSubOrgs(String orgId, Connection connection) throws OrganizationException {
		List<SOrg> orgList = new ArrayList<SOrg>();
		String sqlId = "getAllSubOrgs";
		try {
			orgList = (List<SOrg>) SqlClient.queryList(sqlId, orgId, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new OrganizationException(e);
		}
		return orgList;		
	}
	
	public List<SOrg> getSameOrgLine(String orgId, Connection connection) throws OrganizationException {
		List<SOrg> orgList = new ArrayList<SOrg>();
		String sqlId = "getSameOrgLine";
		try {
			orgList = (List<SOrg>) SqlClient.queryList(sqlId, orgId, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new OrganizationException(e);
		}
		return orgList;		
	}
	
	public List<SRole> getRolesByName(String roleName, Connection connection) throws OrganizationException {
		List<SRole> roleList = new ArrayList<SRole>();
		String sqlId = "getRolesByName";
		try {
			roleList = (List<SRole>) SqlClient.queryList(sqlId, "%"+roleName+"%", connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new OrganizationException(e);
		}
		return roleList;
	}
	
	public List<SUser> getUsersByName(String userName, Connection connection) throws OrganizationException {
		List<SUser> userList = new ArrayList<SUser>();
		String sqlId = "getUsersByName";
		try {
			userList = (List<SUser>) SqlClient.queryList(sqlId, "%"+userName+"%", connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new OrganizationException(e);
		}
		return userList;		
	}
	
	/**
	 * 根据用户码获取该用户所有机构信息
	 * @param actorNo 用户码
	 * @param connection 数据库连接
	 * @return List&lt;SUser>&gt;
	 * @throws OrganizationException
	 */
	public List<SOrg> getDeptOrgByActno(String actorNo, Connection connection) throws OrganizationException{
		List<SOrg> orgList = new ArrayList<SOrg>();
		String sqlId = "getDeptOrgByActno";
		try {
			orgList = (List<SOrg>) SqlClient.queryList(sqlId, actorNo, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new OrganizationException(e);
		}
		return orgList;
	}
	/**
	 * 根据用户码获取该用户托管用户集合
	 * @param userId 用户码
	 * @param connection 数据库连接
	 * @return cusTrustList;
	 * @throws OrganizationException
	 */
	public IndexedCollection getCusTrusteeByUserId(String userId, Connection connection) throws OrganizationException{
		IndexedCollection cusTrustList = new IndexedCollection();
		String sqlId = "getCusTrusteeByUserId";
		try {
			cusTrustList = SqlClient.queryList4IColl(sqlId, userId, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new OrganizationException(e);
		}
		return cusTrustList;
	}
}
