package com.yucheng.cmis.pub.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.domains.SRole;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.MD5;

/**
 * 用户登陆验证 成功返回success，失败返回fail
 * 
 * <p>
 * 	 添加首次登陆需要修改密码，且每三个月需要修改一次密码
 * </p>
 * 
 */
public class CMISUserCheckOperation extends CMISOperation {
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		OrganizationServiceInterface orgMsi = null;
		PreparedStatement datepstmt = null;
		PreparedStatement rolepstmt = null;
		ResultSet daters = null;
		ResultSet rolers = null;
		try {
			HttpServletRequest request1=super.getHttpServletRequest(context);
			String userId = (String) context.getDataValue("currentUserId");
			String loginuserid = (String) context.getDataValue("loginuserid");
			//String password = (String) context.getDataValue("password");
			//处理加密
			//MD5 m = new MD5();
			//password = m.getMD5ofStr(userId + password);
			connection = this.getConnection(context);
			orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			SUser user = orgMsi.getUserByUserId(userId, connection);
			//if(user!=null && userId.equals(user.getActorno()) && password.equals(user.getPassword())) {
				//String orgid = user.getOrgid();
				//SOrg curOrg = orgMsi.getOrgByOrgId(orgid, connection);
				List<SDuty> dutyList = orgMsi.getDutysByUserId(userId, connection);
				List<SRole> roleList = orgMsi.getRolesByUserId(userId, connection);
				String dutynos = "", dutynames = "";
				String rolenos = "", rolenames = "";
				String dutys = "", roles = "";
				
				if(dutyList!=null && dutyList.size()>0) {
					for(SDuty duty : dutyList) {
						dutynos += duty.getDutyno() + ",";
						dutynames += duty.getDutyname() + ",";
						dutys += ("'" + duty.getDutyno() + "',");
					}
					dutys = dutys.substring(0, dutys.length()-1);
					dutynos = dutynos.substring(0, dutynos.length()-1);
					dutynames = dutynames.substring(0, dutynames.length()-1);
				}
				if(roleList!=null && roleList.size()>0) {
					for(SRole role : roleList) {
						rolenos += role.getRoleno() + ",";
						rolenames += role.getRolename() + ",";
						roles += ("'" + role.getRoleno() + "',");
					}
					roles = roles.substring(0, roles.length()-1);
					rolenos = rolenos.substring(0, rolenos.length()-1);
					rolenames = rolenames.substring(0, rolenames.length()-1);
				}
				
				//首次登陆修改密码，每三个月需要修改一次密码
				//根据密码失效日期，如果为空，则是首次登陆；否则和当前日期比较，如果超过三个月，需修改
				//TODO
				//获取系统时间
				String openday = "";
				String lastOpenDay = "";
				String dateSql = "SELECT OPENDAY,LAST_OPENDAY FROM PUB_SYS_INFO ";
				datepstmt = connection.prepareStatement(dateSql);
				daters = datepstmt.executeQuery();
				if(daters.next()){
					openday = daters.getString(1);
					lastOpenDay = daters.getString(2);
				}
				/** 设置登录人员所属业务条线 */
				String bizlineList = "";
				String roleSql =  "SELECT distinct memo FROM S_ROLE WHERE roleno in ('" 
					 + rolenos.replaceAll(",", "','")+ "')  and memo <>'BL_DEFAULT'";
				rolepstmt = connection.prepareStatement(roleSql);
				rolers = rolepstmt.executeQuery();
				while(rolers.next()){
					bizlineList += rolers.getString(1) + ",";
				}
				if(bizlineList.length() > 1){
					bizlineList = bizlineList.substring(0, bizlineList.length()-1);
				}
				//设置session
				context.getParent().put("dutys", dutys);
				context.getParent().put("roles", roles);
				context.getParent().put(CMISConstance.BIZLINELIST, bizlineList);
				context.getParent().put(CMISConstance.OPENDAY, openday);
				context.getParent().put(CMISConstance.LAST_OPENDAY, lastOpenDay);
				context.getParent().put(CMISConstance.ATTR_CURRENTUSERID, userId);
				context.getParent().put(CMISConstance.ATTR_CURRENTUSERNAME, user.getActorname());
				//context.getParent().put(CMISConstance.ATTR_ORGID, curOrg.getOrganno());
				//context.getParent().put(CMISConstance.ATTR_ORGNAME, curOrg.getOrganname());
				//context.getParent().put(CMISConstance.ATTR_ARTI_ORGANNO, curOrg.getArtiOrganno());
				context.getParent().put(CMISConstance.ATTR_DUTYNO_LIST, dutynos);
				context.getParent().put(CMISConstance.ATTR_DUTYNAME_LIST, dutynames);
				context.getParent().put(CMISConstance.ATTR_ROLENO_LIST, rolenos);
				context.getParent().put(CMISConstance.ATTR_ROLENAME_LIST, rolenames);
				
				List<SRole> roleList1 = orgMsi.getRolesByUserId(loginuserid, connection);
				String rolenames1 = "";
				if(roleList1!=null && roleList1.size()>0) {
					for(SRole role1 : roleList1) {
						rolenames1 += role1.getRolename() + ",";
					}
					
					rolenames1 = rolenames1.substring(0, rolenames1.length()-1);
				}else{
					throw new EMPException("用户【"+loginuserid+"】未配置角色，请联系管理员！");
				}
				SUser user1 = orgMsi.getUserByUserId(loginuserid, connection);
				//String orgid1 = user1.getOrgid();
				//SOrg curOrg1 = orgMsi.getOrgByOrgId(orgid1, connection);
				context.getParent().put("loginuserid", loginuserid);
				context.getParent().put("loginusername", user1.getActorname());
				//context.getParent().put("loginorgid", curOrg1.getOrganno());
				//context.getParent().put("loginorgname", curOrg1.getOrganname());
				context.getParent().put("loginRoleNameList", rolenames1);
				
				HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
				request.getSession().setAttribute(EMPConstance.ATTR_CONTEXT, context);
				request.getSession().setAttribute("sessionManager", context.getDataValue("sessionManager"));
				// 暂用于工作流操作
				context.getParent().put(WorkFlowConstance.ATTR_SYSID, WorkFlowConstance.WFI_CMIS_SYSID);
				//用于工作流定义自动登录
				request.getSession().setAttribute(WorkFlowConstance.ATTR_USERID, userId);
				//request.getSession().setAttribute(WorkFlowConstance.ATTR_ORGID, curOrg.getOrganno());
				
				//IndexedCollection iCollCusTrustee = orgMsi.getCusTrusteeByUserId(userId, connection);
				//if(iCollCusTrustee!=null&&iCollCusTrustee.size()>0){
					
				//}else{
					return "success";
				//}
			//}
			
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			try {
				if(daters != null){
					daters.close();
					daters = null;
				}
				if(datepstmt != null){
					datepstmt.close();
					datepstmt = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		//return "fail";
	}
	
	public void initialize() {
	}
}
