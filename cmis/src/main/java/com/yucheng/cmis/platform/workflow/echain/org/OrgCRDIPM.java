package com.yucheng.cmis.platform.workflow.echain.org;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ecc.echain.db.DbControl;
import com.ecc.echain.org.OrgIF;
import com.ecc.echain.org.model.DepModel;
import com.ecc.echain.org.model.GroupModel;
import com.ecc.echain.org.model.OrgModel;
import com.ecc.echain.org.model.RoleModel;
import com.ecc.echain.org.model.UserModel;
import com.ecc.echain.util.WfPropertyManager;
import com.ecc.echain.workflow.cache.OUCache;
import com.ecc.echain.workflow.model.WFClient;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.domains.SRole;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.exception.OrganizationException;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.MD5;

/**
 * 
 * <p>
 * 信贷系统流程接入机构用户实现
 * </p>
 * 
 * @author liuhw
 * 
 */
public class OrgCRDIPM implements OrgIF {

	private static OrganizationServiceInterface orgMsi = null;
	static {
		try {
			orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory
					.getInstance().getModualServiceById("organizationServices",
							"organization");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回所有的一级机构（暂不用）
	 * 
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放OrgModel
	 */
	public List getAllBaseOrgs(Connection con) {
		List list = new ArrayList();
		OrgModel orgModel = null;
		orgModel = new OrgModel();
		orgModel.setOrgid(WorkFlowConstance.WFI_CMIS_ORGID);
		orgModel.setOrgname("信贷系统缺省机构");
		orgModel.setOrglevel(2 - 1);
		orgModel.setSuporgid("");
		list.add(orgModel);
		return list;
	}

	// 返回组织中所有群组(岗位)
	public List getAllGroups(String orgid, Connection cn) {
		List list = new ArrayList();
		try {
			List<SDuty> dutys = orgMsi.getAllDutys(cn);
			if (dutys != null && dutys.size() > 0) {
				for (int i = 0; i < dutys.size(); i++) {
					SDuty duty = dutys.get(i);
					GroupModel groupmodel = toGroupModel(duty);
					list.add(groupmodel);
				}
			}
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		return list;
	}

	// 返回所有的组织机构
	public List getAllOrgs(Connection con) {
		List list = new ArrayList();
		List<SOrg> orgList;
		try {
			orgList = orgMsi.getAllOrgs(con);
			if (orgList != null && orgList.size() > 0) {
				for (int i = 0; i < orgList.size(); i++) {
					SOrg org = orgList.get(i);
					OrgModel orgModel = toOrgModel(org);
					list.add(orgModel);
				}
			}
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return list;
	}

	// 返回组织中所有角色
	public List getAllRoles(String orgid, Connection cn) {
		List list = new ArrayList();
		try {
			List<SRole> roleList = orgMsi.getAllRoles(cn);
			if (roleList != null && roleList.size() > 0) {
				for (SRole role : roleList) {
					RoleModel model = toRoleModel(role);
					list.add(model);
				}
			}
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		return list;
	}

	// 返回部门底下所有的子部门（不含子部门），暂不实现
	public List getAllSubDepsByDep(String orgid, String depid, Connection cn) {
		/*
		 * List list = new ArrayList(); DepModel model; //
		 * if(WfPropertyManager.getInstance().isOUCache){ // List
		 * al=OUCache.getInstance().getAllDepModel(); // for(int
		 * i=0;i<al.size();i++){ // model=(DepModel)al.get(i); //
		 * if(model.getSupdepid().equals(depid)) // list.add(model); // } //
		 * return list; // } Vector vecRow, vecData; String SqlStr = "";
		 * DbControl db = DbControl.getInstance(); try { SqlStr =
		 * "select ORGANNO,ORGANNAME from S_ORG where SUPORGANNO=" + depid + "";
		 * vecData = db.performQuery(SqlStr, cn); for (int i = 0; i <
		 * vecData.size(); i++) { vecRow = (Vector) vecData.elementAt(i); model
		 * = new DepModel(); model.setOrgid((String) vecRow.elementAt(0));
		 * model.setDepname((String) vecRow.elementAt(1));
		 * model.setOrgid(orgid); model.setSupdepid(depid); list.add(model); } }
		 * catch (Exception e) { e.printStackTrace(); } return list;
		 */
		return null;
	}

	// 返回部门底下所有的子部门（含子部门）
	public List getAllSubDepsByDepIncSubDep(String orgid, String depid,
			Connection cn) {
		return getAllSubDepsByDep(orgid, depid, cn);
	}

	// 返回组织底下所有的部门，暂不实现
	public List getAllSubDepsByOrg(String orgid, Connection cn) {
		// if(WfPropertyManager.getInstance().isOUCache)
		// return OUCache.getInstance().getAllDepModel();
		/*
		 * List list = new ArrayList(); Vector vecRow, vecData; String SqlStr =
		 * ""; DbControl db = DbControl.getInstance(); try { SqlStr =
		 * "select ORGANNO,ORGANNAME,SUPORGANNO from S_ORG"; vecData =
		 * db.performQuery(SqlStr, cn); DepModel model; for (int i = 0; i <
		 * vecData.size(); i++) { vecRow = (Vector) vecData.elementAt(i); model
		 * = new DepModel(); model.setOrgid((String) vecRow.elementAt(0));
		 * model.setDepname((String) vecRow.elementAt(1));
		 * model.setOrgid(orgid); if (vecRow.elementAt(2) == null ||
		 * vecRow.elementAt(2).toString().equals("") ||
		 * vecRow.elementAt(2).toString().equals("null") ||
		 * vecRow.elementAt(2).toString().equals("-1") ||
		 * vecRow.elementAt(2).toString().equals("0")) model.setSupdepid(null);
		 * else model.setSupdepid((String) vecRow.elementAt(2));
		 * list.add(model); } } catch (Exception e) { e.printStackTrace(); }
		 * return list;
		 */
		return null;
	}

	/**
	 * 返回机构下所有的子机构（包含多级子机构）
	 * 
	 * @param orgid
	 *            ：父机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放OrgModel
	 */
	public List getAllSubOrgs(String orgid, Connection cn) {

		List list = new ArrayList();
		try {
			List<SOrg> orgList = orgMsi.getAllSubOrgs(orgid, cn);
			if (orgList != null && orgList.size() > 0) {
				for (SOrg org : orgList) {
					OrgModel orgModel = toOrgModel(org);
					list.add(orgModel);
				}
			}
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		return list;
	}

	// 返回部门底下所有的用户（不含子部门） 暂不实现
	public List getAllUsersByDep(String orgid, String depid, Connection cn) {
		/*
		 * List list = new ArrayList(); UserModel model; Vector vecRow, vecData;
		 * String SqlStr = ""; DbControl db = DbControl.getInstance(); try {
		 * SqlStr =
		 * "select a.ACTORNO,b.ACTORNAME from S_DEPTUSER a,S_USER b where a.ACTORNO=b.ACTORNO and a.ORGANNO='"
		 * + depid + "'"; vecData = db.performQuery(SqlStr, cn); for (int i = 0;
		 * i < vecData.size(); i++) { vecRow = (Vector) vecData.elementAt(i);
		 * model = new UserModel(); model.setOrgid((String)
		 * vecRow.elementAt(0)); model.setUsername((String)
		 * vecRow.elementAt(1)); model.setDepid(depid); model.setOrgid(orgid);
		 * list.add(model); } } catch (Exception e) { e.printStackTrace(); }
		 * return list;
		 */
		return null;
	}

	// 返回部门底下所有的用户（含子部门）
	public List getAllUsersByDepIncSubDep(String orgid, String depid,
			Connection cn) {
		return getAllUsersByDep(orgid, depid, cn);
	}

	// 返回组织底下所有的用户
	public List getAllUsersByOrg(String orgid, Connection cn) {
		List list = new ArrayList();
		try {
			List<SUser> userList = orgMsi.getUsersByOrgId(orgid, cn);
			if (userList != null) {
				for (SUser user : userList) {
					UserModel model = toUserModel(user);
					list.add(model);
				}
			}
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		return list;
	}

	// 返回部门主管（暂不用）
	public List getDepDirector(String orgid, String depid, Connection cn) {
		List list = new ArrayList();
		return list;
	}

	// 返回部门领导（正职、副职）（暂不用）
	public List getDepLeaders(String orgid, String depid, Connection cn) {
		List list = new ArrayList();
		return list;
	}

	// 输入部门ＰＫ取得部门对象（暂不用）
	public DepModel getDepWithPK(String orgid, String depid, Connection cn) {
		// if(WfPropertyManager.getInstance().isOUCache){
		// return (DepModel)OUCache.getInstance().hmDMCache.get(depid);
		// }
		DepModel model = new DepModel();
		Vector vecRow, vecData;
		String SqlStr = "";
		DbControl db = DbControl.getInstance();
		try {
			SqlStr = "select ORGANNO,ORGANNAME,SUPORGANNO from S_ORG where ORGANNO="
					+ depid;
			vecData = db.performQuery(SqlStr, cn);
			if (vecData.size() > 0) {
				vecRow = (Vector) vecData.elementAt(0);
				model.setOrgid((String) vecRow.elementAt(0));
				model.setOrgid(orgid);
				model.setDepname((String) vecRow.elementAt(1));
				model.setSupdepid((String) vecRow.elementAt(2));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	// 获取用户授权信息（暂不用）
	public String getGrantor(String orgid, String userid, String AppID,
			Connection cn) {
		return null;
	}

	// 输入群组id取得群组对象
	public GroupModel getGroupWithPK(String orgid, String groupid, Connection cn) {
		if (WfPropertyManager.getInstance().isOUCache) {
			return (GroupModel) OUCache.getInstance().hmGMCache.get(groupid);
		}
		GroupModel model = new GroupModel();
		Vector vecRow, vecData;
		String SqlStr = "";
		DbControl db = DbControl.getInstance();
		try {
			SqlStr = "SELECT DUTYNO,DUTYNAME FROM S_DUTY WHERE DUTYNO='"
					+ groupid + "'";
			vecData = db.performQuery(SqlStr, cn);
			if (vecData.size() > 0) {
				vecRow = (Vector) vecData.elementAt(0);
				model.setGroupid((String) vecRow.elementAt(0));
				model.setGroupname((String) vecRow.elementAt(1));
				model.setOrgid(WorkFlowConstance.WFI_CMIS_ORGID);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	// 取得下级办理人
	public List getJuniorUsers(String orgid, String userid, Connection cn) {
		List list = new ArrayList();
		return list;
	}

	// 返回组织管理员
	public List getOrgLeaders(String orgid, Connection cn) {
		List list = new ArrayList();
		return list;
	}

	public OrgModel getOrgWithPK(String orgid, Connection cn) {
		OrgModel model = new OrgModel();
		try {
			SOrg org = orgMsi.getOrgByOrgId(orgid, cn);
			model = toOrgModel(org);
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return model;
	}

	// 返回用户拥有的角色
	public List getRolesByUser(String orgid, String userid, Connection cn) {
		List list = new ArrayList();
		try {
			List<SRole> roleList = orgMsi.getRolesByUserId(userid, cn);
			if (roleList != null) {
				for (SRole role : roleList) {
					RoleModel model = toRoleModel(role);
					list.add(model);
				}
			}
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		return list;
	}

	public RoleModel getRoleWithPK(String orgid, String roleid, Connection cn) {
		if (WfPropertyManager.getInstance().isOUCache) {
			return (RoleModel) OUCache.getInstance().hmRMCache.get(roleid);
		}
		RoleModel model = new RoleModel();
		Vector vecRow, vecData;
		String SqlStr = "";
		DbControl db = DbControl.getInstance();
		try {
			SqlStr = "select ROLENO,ROLENAME from S_ROLE where ROLENO='"
					+ roleid + "'";
			vecData = db.performQuery(SqlStr, cn);
			if (vecData.size() > 0) {
				vecRow = (Vector) vecData.elementAt(0);
				model.setRoleid((String) vecRow.elementAt(0));
				model.setRolename((String) vecRow.elementAt(1));
				model.setOrgid(WorkFlowConstance.WFI_CMIS_ORGID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	// 取得同部门人员（暂不用）
	public List getSameDepUsers(String orgid, String userid, Connection cn) {
		List list = new ArrayList();
		UserModel model;
		Vector vecRow, vecData;
		String SqlStr = "";
		DbControl db = DbControl.getInstance();
		try {
			// 首先取得用户所在的部门
			SqlStr = "select ORGANNO from S_DEPTUSER where ACTORNO='" + userid
					+ "'";
			vecData = db.performQuery(SqlStr, cn);
			if (vecData.size() < 1)
				return list;
			vecRow = (Vector) vecData.elementAt(0);
			String depid = (String) vecRow.elementAt(0);
			SqlStr = "select a.ACTORNO,b.ACTORNAME from S_DEPTUSER a,S_USER b where a.ACTORNO=b.ACTORNO and a.ORGANNO='"
					+ depid + "'";
			vecData = db.performQuery(SqlStr, cn);
			for (int i = 0; i < vecData.size(); i++) {
				vecRow = (Vector) vecData.elementAt(i);
				model = new UserModel();
				model.setOrgid((String) vecRow.elementAt(0));
				model.setUsername((String) vecRow.elementAt(1));
				model.setDepid(depid);
				model.setOrgid(orgid);
				list.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 取得上一级用户(按机构级别)
	public List getSuperiorUsers(String orgid, String userid, Connection cn) {
		List list = new ArrayList();
		try {
			SOrg supOrg = orgMsi.getSupOrg(orgid, cn);
			String supOrgId = supOrg.getOrganno();
			if (supOrgId != null && !supOrgId.equals(orgid)) {
				List<SUser> userList = orgMsi.getUsersByOrgId(supOrgId, cn);
				if (userList != null) {
					for (SUser user : userList) {
						UserModel model = toUserModel(user);
						list.add(model);
					}
				}
			}
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		return list;
	}

	// 返回群组(岗位)下用户
	public List getUsersByGroup(String orgid, String groupid, Connection cn) {
		List list = new ArrayList();
		try {
			List<SUser> userList = orgMsi.getUsersByDutyId(groupid, cn);
			if (userList != null) {
				for (SUser user : userList) {
					list.add(toUserModel(user));
				}
			}
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		return list;
	}

	// 返回角色下用户
	public List getUsersByRole(String orgid, String roleid, Connection cn) {
		List list = new ArrayList();
		try {
			List<SUser> userList = orgMsi.getUsersByRoleId(roleid, cn);
			if (userList != null) {
				for (SUser user : userList) {
					UserModel userModel = toUserModel(user);
					list.add(userModel);
				}
			}
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		return list;
	}

	public UserModel getUserWithPK(String orgid, String userid, Connection cn) {
		if (WfPropertyManager.getInstance().isOUCache) {
			UserModel um = (UserModel) OUCache.getInstance().hmUMCache
					.get(userid);
			return um;
		}
		UserModel model = null;
		try {
			SUser user = orgMsi.getUserByUserId(userid, cn);
			model = toUserModel(user);
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		return model;
	}

	// 登录验证
	public UserModel isValidUser(String orgid, String userid, String password,
			Connection cn) {
		UserModel model = null;
		SUser user = null;
		try {
			user = orgMsi.getUserByUserId(userid, cn);
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		if (user != null) {
			MD5 m = new MD5();
			password = m.getMD5ofStr(userid + password);
			if (password.equals(user.getPassword())) {
				model = new UserModel();
				model.setUserid(userid);
				model.setUsername(user.getActorname());
				model.setEmail(user.getUsermail());
				model.setMobile(user.getTelnum());
				model.setOrgid(orgid);
				model.setUserstatus(Integer.valueOf(user.getState()));
				model.setAdminflag(true);
				model.setWfadminflag(true);
				model.setSaflag(true);
				model.setPassword(password);
			}
		}
		return model;
	}

	/**
	 * 获取用户邮件地址
	 * 
	 * @return String 邮件地址
	 */
	public String getUserEmail(String orgid, String userid, Connection cn) {
		String strResult = "";
		if (WfPropertyManager.getInstance().isOUCache) {
			StringTokenizer st = new StringTokenizer(userid, ";");
			String key;
			UserModel um;
			while (st.hasMoreElements()) {
				key = (String) st.nextElement();
				um = (UserModel) OUCache.getInstance().hmUMCache.get(key);
				strResult += ";" + um.getEmail();
			}
			if (strResult.length() > 2)
				strResult = strResult.substring(1);
			return strResult;
		}
		UserModel userModel = getUserModel(userid, cn);
		strResult = userModel.getEmail();
		return strResult;
	}

	// 加载组织用户到缓存中,根据项目需要决定是否实现
	public void loadOUCache(OUCache oucache, Connection con) {
		oucache.hmOMCache.clear();// 首先清空hmOMCache的内容
		oucache.hmRMCache.clear();
		oucache.hmGMCache.clear();
		oucache.hmUMCache.clear();
		List<OrgModel> olist = this.getAllOrgs(con);
		for (OrgModel model : olist) {
			oucache.hmOMCache.put(model.getOrgid(), model);
		}
		List<RoleModel> rlist = this.getAllRoles(con);
		for (RoleModel model : rlist) {
			oucache.hmRMCache.put(model.getRoleid(), model);
		}
		List<GroupModel> glist = this.getAllGroups(con);
		for (GroupModel model : glist) {
			oucache.hmGMCache.put(model.getGroupid(), model);
		}
		try {
			List<SUser> userList = orgMsi.getAllUsers(con);
			for (SUser user : userList) {
				UserModel userModel = toUserModel(user);
				oucache.hmUMCache.put(userModel.getUserid(), userModel);
			}
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回系统中所有的全局群组/岗位（不属于某一个机构的群组/岗位）
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放GroupModel
	 */
	public List getAllBaseGroups(Connection con) {
		return getAllGroups(con);
	}

	/**
	 * 返回系统中所有全局角色（不属于某一个机构的角色）
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放RoleModel
	 */
	public List getAllBaseRoles(Connection con) {
		return getAllRoles(con);
	}

	/**
	 * 返回机构下所有的部门（含多级子部门）
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放DepModel
	 */
	public List getAllDepsByOrg(String orgid, Connection con) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 返回系统中所有的群组（岗位）
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放GroupModel
	 */
	public List getAllGroups(Connection con) {
		List list = new ArrayList();
		try {
			List<SDuty> dutys = orgMsi.getAllDutys(con);
			if (dutys != null && dutys.size() > 0) {
				for (int i = 0; i < dutys.size(); i++) {
					SDuty duty = dutys.get(i);
					GroupModel groupmodel = toGroupModel(duty);
					list.add(groupmodel);
				}
			}
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		return list;
	}

	/**
	 * 返回机构下的群组（岗位）
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放GroupModel
	 */
	public List getAllGroups(String orgid, String depid, Connection con) {
		return getAllGroups(orgid, con);
	}

	/**
	 * 返回系统中所有角色
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放RoleModel
	 */
	public List getAllRoles(Connection con) {
		return getAllRoles(null, con);
	}

	/**
	 * 返回机构下所有角色
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放RoleModel
	 */
	public List getAllRoles(String orgid, String depid, Connection con) {
		return getAllRoles(null, con);
	}

	/**
	 * 取所有流程客户端
	 * 
	 * @param status
	 *            客户端状态
	 * @param con
	 *            数据库连接
	 * @return List list中放WFClient对象
	 */
	public List getAllWFClient(String status, Connection con) {
		DbControl db = DbControl.getInstance();
		String strSql = "";
		List<WFClient> list = new ArrayList<WFClient>();
		WFClient client = null;
		boolean isClose = false;
		try {
			if (con == null) {
				con = db.getConnection();
				isClose = true;
			}
			strSql = "select pkey,ClientSign,ClientName,IP,Type,InvokeType,Status,Remark from WF_CLIENT ";
			if (status != null && status.length() > 0)
				strSql += " where status='" + status + "' ";
			Vector<Vector> vec = db.performQuery(strSql, con);
			if (vec != null && vec.size() > 0) {
				for (Iterator iterator = vec.iterator(); iterator.hasNext();) {
					client = new WFClient();
					Vector vecRow = (Vector) iterator.next();
					client.setPkey((String) vecRow.elementAt(0));
					client.setClientSign((String) vecRow.elementAt(1));
					client.setClientName((String) vecRow.elementAt(2));
					client.setIP((String) vecRow.elementAt(3));
					client.setType((String) vecRow.elementAt(4));
					client.setInvokeType((String) vecRow.elementAt(5));
					client.setStatus((String) vecRow.elementAt(6));
					client.setRemark((String) vecRow.elementAt(7));

					list.add(client);
				}
			} else {
				client = new WFClient();
				client.setPkey("");
				client.setClientSign(WorkFlowConstance.WFI_CMIS_SYSID);
				client.setClientName("信贷管理系统");
				client.setIP("*.*.*.*");
				client.setType("");
				client.setInvokeType("1;2");
				client.setStatus("0");
				client.setRemark("");
				list.add(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (isClose)
					DbControl.getInstance().freeConnection(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 返回部门主管（正职）
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param depid
	 *            ：部门id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放UserModel
	 */
	public List getDepDirectors(String orgid, String depid, Connection con) {
		return null;
	}

	/**
	 * 返回部门对象
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param depid
	 *            ：部门id
	 * @param Connection
	 *            con：数据库连接
	 * @return DepModel
	 */
	public DepModel getDepModel(String orgid, String depid, Connection con) {
		return null;
	}

	/**
	 * <p>
	 * 找到部门所属机构
	 * 
	 * <p>
	 * 逻辑：根根据orgid向上递归找到所有的上级的【机构】，并排序(升序)，取第一条记录
	 * 
	 * <p>
	 * 注意：如果orgid是机构，则返回orgid；如果是部门，则返回其所属机构
	 * 
	 * @param orgid
	 *            机构ID
	 * @param con
	 *            数据库连接
	 * @return List lt;UserModelgt;
	 */
	public String getDeptOrgid(String orgid, Connection con) {
		return null;
	}

	/**
	 * 返回机构下所有的直属部门（一级部门）
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放DepModel
	 */
	public List getDirectDepsByOrg(String orgid, Connection con) {
		return null;
	}

	/**
	 * 返回部门下所有的直属子部门（不含多级子部门）
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param depid
	 *            ：父部门id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放DepModel
	 */
	public List getDirectSubDepsByDep(String orgid, String depid, Connection con) {
		return null;
	}

	/**
	 * 返回机构下所有的直属子机构（不含多级子机构）
	 * 
	 * @param orgid
	 *            ：父机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放OrgModel
	 */
	public List getDirectSubOrgs(String orgid, Connection con) {
		List list = new ArrayList();
		try {
			List<SOrg> orgList = orgMsi.getDirectSubOrgs(orgid, con);
			if (orgList != null) {
				for (SOrg org : orgList) {
					OrgModel orgModel = toOrgModel(org);
					list.add(orgModel);
				}
			}
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 返回部门下所有直属用户（不含子部门）
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param depid
	 *            ：部门id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放UserModel
	 */
	public List getDirectUsersByDep(String orgid, String depid, Connection con) {
		return null;
	}

	/**
	 * 返回机构下所有直属用户（直接属于机构而不属于部门的）
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放UserModel
	 */
	public List getDirectUsersByOrg(String orgid, Connection con) {
		List list = new ArrayList();
		try {
			List<SUser> userList = orgMsi.getUsersByOrgId(orgid, con);
			if (userList != null) {
				for (SUser user : userList) {
					UserModel userModel = toUserModel(user);
					list.add(userModel);
				}
			}
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 返回用户拥有的群组（岗位）
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param userid
	 *            ：用户id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放GroupModel
	 */
	public List getGroupByUser(String orgid, String userid, Connection con) {
		List list = new ArrayList();
		try {
			List<SDuty> dutyList = orgMsi.getDutysByUserId(userid, con);
			if (dutyList != null) {
				for (SDuty duty : dutyList) {
					GroupModel groupModel = toGroupModel(duty);
					list.add(groupModel);
				}
			}
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 取得群组（岗位）对象
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param groupid
	 *            ：工作组id
	 * @param Connection
	 *            con：数据库连接
	 * @return GroupModel
	 */
	public GroupModel getGroupModel(String orgid, String groupid, Connection con) {
		return getGroupWithPK(orgid, groupid, con);
	}

	/**
	 * 返回机构主管（正职） (项目组根据机构下主管岗位来实现)
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return List里面放UserModel
	 */
	public List getOrgDirectors(String orgid, Connection con) {
		return null;
	}

	/**
	 * 取得用户所在的机构
	 * 
	 * @param userid
	 *            ：用户id
	 * @param Connection
	 *            con：数据库连接
	 * @return String orgid，如果找不到则返回null
	 */
	public String getOrgIdByUser(String userid, Connection con) {
		String orgid = null;
		//会签匿名用户、项目池用户，机构之间返回null
		if(WorkFlowConstance.SIGN_USER.equals(userid) || userid.startsWith("T."))
			return orgid;
		try {
			SUser user = orgMsi.getUserByUserId(userid, con);
			orgid = user.getOrgid();
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return orgid;
	}

	/**
	 * 返回机构对象
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return 上级机构OrgModel
	 */
	public OrgModel getOrgModel(String orgid, Connection con) {
		return getOrgWithPK(orgid, con);
	}

	/**
	 * 返回部门所属的上级部门
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param depid
	 *            ：部门id
	 * @param Connection
	 *            con：数据库连接
	 * @return DepModel，如果是一级部门，则返回null
	 */
	public DepModel getParentDep(String orgid, String depid, Connection con) {
		return null;
	}

	/**
	 * 返回机构所属的上级机构
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param Connection
	 *            con：数据库连接
	 * @return 上级机构OrgModel
	 */
	public OrgModel getParentOrg(String orgid, Connection con) {
		OrgModel supOrgModel = null;
		try {
			SOrg supOrg = orgMsi.getSupOrg(orgid, con);
			supOrgModel = toOrgModel(supOrg);
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return supOrgModel;
	}

	/**
	 * 取得角色对象
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param roleid
	 *            ：角色id
	 * @param Connection
	 *            con：数据库连接
	 * @return RoleModel
	 */
	public RoleModel getRoleModel(String orgid, String roleid, Connection con) {
		return getRoleWithPK(orgid, roleid, con);
	}

	/**
	 * 根据角色名称模糊查询角色
	 * 
	 * @param orgid
	 *            机构号
	 * @param roleName
	 *            角色名称
	 * @param con
	 *            Connection con：数据库连接
	 * @return List里面放RoleModel
	 */
	public List getRolesByName(String orgid, String roleName, Connection con) {
		List list = new ArrayList();
		try {
			List<SRole> roleList = orgMsi.getRolesByName(roleName, con);
			if (roleList != null) {
				for (SRole role : roleList) {
					RoleModel roleModel = toRoleModel(role);
					list.add(roleModel);
				}
			}
		} catch (OrganizationException e1) {
			e1.printStackTrace();
		}
		return list;
	}

	/**
	 * 返回根机构
	 * 
	 * @param Connection
	 *            con：数据库连接
	 * @return OrgModel
	 */
	public OrgModel getRootOrg(Connection con) {
		OrgModel rootOrgModel = null;
		try {
			SOrg rootOrg = orgMsi.getRootOrg(con);
			rootOrgModel = toOrgModel(rootOrg);
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return rootOrgModel;
	}

	/**
	 * 获取orgid的所有上级机构(同一机构线上)
	 * 
	 * @param orgid
	 *            流程发起机构
	 * @param con
	 *            数据库连接
	 * @return List中放机构号
	 */
	public List getSameOrgLine(String orgid, Connection con) {
		List list = new ArrayList();
		try {
			List<SOrg> orgList = orgMsi.getSameOrgLine(orgid, con);
			if (orgid != null) {
				for (SOrg org : orgList) {
					String orgidTmp = org.getOrganno();
					list.add(orgidTmp);
				}
			}
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 取同一机构线上的所有用户 根据orgid往上找其所有上级机构的用户
	 * 
	 * @param orgid
	 *            机构号
	 * @param con
	 *            数据库连接
	 * @return List，里面放UserModel
	 */
	public List getSameOrgLineUsers(String orgid, Connection con) {
		List list = new ArrayList();
		List supOrgs = getSameOrgLine(orgid, con);
		for (int i = 0; i < supOrgs.size(); i++) {
			String orgidTmp = (String) supOrgs.get(i);
			try {
				List<SUser> userList = orgMsi.getUsersByOrgId(orgidTmp, con);
				if (userList != null) {
					for (SUser user : userList) {
						UserModel userModel = toUserModel(user);
						list.add(userModel);
					}
				}
			} catch (OrganizationException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 取得同机构人员
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param userid
	 *            ：用户id
	 * @param Connection
	 *            con：数据库连接
	 * @return List，里面放UserModel
	 */
	public List getSameOrgUsers(String orgid, String userid, Connection con) {
		List list = new ArrayList();
		try {
			if (orgid == null || orgid.trim().equals("")) {
			//始终重新查询
				SUser userTmp = orgMsi.getUserByUserId(userid, con);
				orgid = userTmp.getOrgid();//这个根本查不到
			}
			List<SUser> userList = orgMsi.getUsersByOrgId(orgid, con);
			if (userList != null) {
				for (SUser user : userList) {
					UserModel userModel = toUserModel(user);
					list.add(userModel);
				}
			}
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 取本级及上级办理人员
	 * 
	 * @param orgid
	 *            机构号
	 * @param con
	 *            数据库连接
	 * @return List lt;UserModelgt;
	 */
	public List getSampeAndUpOrgUsers(String orgid, Connection con) {
		List list = new ArrayList();
		list.addAll(getSameOrgUsers(orgid, null, con));
		list.addAll(getSuperiorUsers(orgid, null, con));
		return list;
	}

	/**
	 * 取同部门或是机构的办理人员，不对传入的orgid是机构还部门校验 (暂不用)
	 * 
	 * @param orgid
	 *            机构号
	 * @param con
	 *            数据库连接
	 * @return
	 */
	public List getSampeOrgOrDeptUsers(String orgid, Connection con) {
		return null;
	}

	/**
	 * 取当前orgid下的所有部门包括子部门ID (暂不实现)
	 * <p>
	 * 注：不含其下机构
	 * 
	 * @param orgid
	 *            机构号 (可以是部门)
	 * @param con
	 *            数据库连接
	 * @return List lt;UserModelgt;
	 */
	public List getSubDeptByOrgid(String orgid, Connection con) {
		return null;
	}

	/**
	 * 取上级及上上级机构下所有办理人员
	 * 
	 * @param orgid
	 *            机构号
	 * @param con
	 *            数据库连接
	 * @return List lt;UserModelgt;
	 */
	public List getUpAndUpUpOrgUsers(String orgid, Connection con) {
		List list = new ArrayList();
		try {
			SOrg supOrg = orgMsi.getSupOrg(orgid, con);
			String supOrgNo = supOrg.getOrganno();
			List<SUser> supUserList = orgMsi.getUsersByOrgId(supOrgNo, con);
			if (supUserList != null) {
				for (SUser user : supUserList) {
					UserModel userModel = toUserModel(user);
					list.add(userModel);
				}
			}
			SOrg supSupOrg = orgMsi.getSupOrg(supOrgNo, con);
			String supSupOrgNo = supSupOrg.getOrganno();
			List<SUser> supSupUserList = orgMsi.getUsersByOrgId(supSupOrgNo,
					con);
			if (supSupUserList != null) {
				for (SUser user : supSupUserList) {
					UserModel userModel = toUserModel(user);
					list.add(userModel);
				}
			}
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 取当前机构的上级的下级机构的所有用户
	 * 
	 * @param orgid
	 *            机构号
	 * @param con
	 *            数据库连接
	 * @return List，里面放UserModel
	 */
	public List getUpDownOrgUsers(String orgid, Connection con) {
		List list = new ArrayList();
		try {
			SOrg supOrg = orgMsi.getSupOrg(orgid, con);
			String supOrgNo = supOrg.getOrganno();
			List<SUser> supUserList = orgMsi.getUsersByOrgId(supOrgNo, con);
			if (supUserList != null) {
				for (SUser user : supUserList) {
					UserModel userModel = toUserModel(user);
					list.add(userModel);
				}
			}
			List<SOrg> subOrgList = orgMsi.getDirectSubOrgs(orgid, con);
			if (subOrgList != null) {
				for (SOrg org : subOrgList) {
					String orgidTmp = org.getOrganno();
					List<SUser> subUserList = orgMsi.getUsersByOrgId(orgidTmp,
							con);
					if (subUserList != null) {
						for (SUser user : subUserList) {
							UserModel userModel = toUserModel(user);
							list.add(userModel);
						}
					}
				}
			}
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 取当前机构的上上级的下级机构的所有用户(不含当前机构)
	 * 
	 * @param orgid
	 *            机构号
	 * @param con
	 *            数据库连接
	 * @return List，里面放UserModel
	 */
	public List getUpUpOrgDownOrgUsers(String orgid, Connection con) {
		List list = new ArrayList();
		try {
			SOrg supOrg = orgMsi.getSupOrg(orgid, con);
			SOrg supSupOrg = orgMsi.getSupOrg(supOrg.getOrganno(), con);
			if (supSupOrg != null) {
				List<SOrg> subOrgList = orgMsi.getDirectSubOrgs(
						supSupOrg.getOrganno(), con);
				if (subOrgList != null) {
					for (SOrg subOrg : subOrgList) {
						List<SUser> userList = orgMsi.getUsersByOrgId(
								subOrg.getOrganno(), con);
						if (userList != null) {
							for (SUser user : userList) {
								UserModel userModel = toUserModel(user);
								list.add(userModel);
							}
						}
					}
				}
			}
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 取当前机构的上上级机构的所有用户
	 * 
	 * @param orgid
	 *            机构号
	 * @param con
	 *            数据库连接
	 * @return List，里面放UserModel
	 */
	public List getUpUpOrgUsers(String orgid, Connection con) {
		List list = new ArrayList();
		try {
			SOrg supOrg = orgMsi.getSupOrg(orgid, con);
			String supOrgNo = supOrg.getOrganno();
			SOrg supSupOrg = orgMsi.getSupOrg(supOrgNo, con);
			String supSupOrgNo = supSupOrg.getOrganno();
			List<SUser> supSupUserList = orgMsi.getUsersByOrgId(supSupOrgNo,
					con);
			if (supSupUserList != null) {
				for (SUser user : supSupUserList) {
					UserModel userModel = toUserModel(user);
					list.add(userModel);
				}
			}
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 取得用户对象
	 * 
	 * @param orgid
	 *            ：机构id
	 * @param userid
	 *            ：用户id
	 * @param Connection
	 *            con：数据库连接
	 * @return UserModel
	 */
	public UserModel getUserModel(String orgid, String userid, Connection con) {
		return getUserModel(userid, con);
	}

	public UserModel getUserModel(String userid, Connection con) {
		UserModel userModel = null;
		try {
			SUser user = orgMsi.getUserByUserId(userid, con);
			userModel = toUserModel(user);
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return userModel;
	}

	/**
	 * 根据用户名称模糊匹配所有用户
	 * 
	 * @param name
	 *            客户名称
	 * @param con
	 *            数据库连接
	 * @return List&lt;UserModel&gt;
	 */
	public List<UserModel> queryUserModelsByName(String name, Connection con) {
		List<UserModel> list = new ArrayList<UserModel>();
		try {
			List<SUser> userList = orgMsi.getUsersByName(name, con);
			for (SUser user : userList) {
				UserModel userModel = toUserModel(user);
				list.add(userModel);
			}
		} catch (OrganizationException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 信贷SUser转换UserModel
	 * 
	 * @param user
	 * @return
	 */
	private UserModel toUserModel(SUser user) {
		UserModel userModel = new UserModel();
		userModel.setUserid(user.getActorno());
		userModel.setUsername(user.getActorname());
		userModel.setEmail(user.getUsermail());
		userModel.setMobile(user.getTelnum());
		userModel.setOrgid(user.getOrgid());
		return userModel;
	}

	/**
	 * 信贷SOrg转换OrgModel
	 * 
	 * @param user
	 * @return
	 */
	private OrgModel toOrgModel(SOrg org) {
		OrgModel orgModel = new OrgModel();
		String orgId = org.getOrganno();
		String supOrgId = org.getSuporganno();
		if (orgId.equals(supOrgId)) {
			supOrgId = null;
		}
		orgModel.setOrgid(orgId);
		orgModel.setOrgname(org.getOrganname());
		orgModel.setSuporgid(supOrgId);
		int orgLevel = org.getOrganlevel();
		orgModel.setOrglevel(orgLevel);
		return orgModel;
	}

	/**
	 * 信贷SRole转换RoleModel
	 * 
	 * @param user
	 * @return
	 */
	private RoleModel toRoleModel(SRole role) {
		RoleModel roleModel = new RoleModel();
		roleModel.setRoleid(role.getRoleno());
		roleModel.setRolename(role.getRolename());
		roleModel.setOrgid(WorkFlowConstance.WFI_CMIS_ORGID);
		return roleModel;
	}

	/**
	 * 信贷SDuty转换GroupModel
	 * 
	 * @param user
	 * @return
	 */
	private GroupModel toGroupModel(SDuty duty) {
		GroupModel groupModel = new GroupModel();
		groupModel.setGroupid(duty.getDutyno());
		groupModel.setGroupname(duty.getDutyname());
		groupModel.setOrgid(WorkFlowConstance.WFI_CMIS_ORGID);
		return groupModel;
	}
}
