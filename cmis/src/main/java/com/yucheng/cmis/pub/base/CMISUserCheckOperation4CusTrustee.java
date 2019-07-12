package com.yucheng.cmis.pub.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
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
public class CMISUserCheckOperation4CusTrustee extends CMISOperation {
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
			String password = (String) context.getDataValue("password");
			if(StringUtils.isEmpty(password)){
				return "fail";
			}
			//处理加密
			MD5 m = new MD5();
			password = m.getMD5ofStr(userId + password);
			connection = this.getConnection(context);
			orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			SUser user = orgMsi.getUserByUserId(userId, connection);
			if(user!=null && userId.equals(user.getActorno()) && password.equals(user.getPassword())) {
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
				
				context.getParent().put("loginuserid", userId);
				context.getParent().put("loginusername", user.getActorname());
				//context.getParent().put("loginorgid", curOrg.getOrganno());
				//context.getParent().put("loginorgname", curOrg.getOrganname());
				context.getParent().put("loginRoleNameList", rolenames);
				IndexedCollection iCollCusTrustee = orgMsi.getCusTrusteeByUserId(userId, connection);
				if(iCollCusTrustee!=null&&iCollCusTrustee.size()>0){
			   //对托管客户进行时间判断      modefied  by  zhaoxp  2014-11-04
					IndexedCollection cusTrustee =new IndexedCollection();
					for(int i=0;i<iCollCusTrustee.size();i++){
						KeyedCollection kc = (KeyedCollection)iCollCusTrustee.get(i);
						
						String trustee_date = (String)kc.getDataValue("trustee_date");
						if(trustee_date==null||trustee_date.equals("")){
							continue;
						}
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Calendar cal = Calendar.getInstance();
						Calendar cal2 = Calendar.getInstance();
						cal.setTime(sdf.parse(trustee_date));
						cal2.setTime(sdf.parse(openday));
						if(!cal.after(cal2)){
							cusTrustee.addDataElement(kc);
						}
					}
					context.getParent().addDataField("consignor", cusTrustee);
					return "success";
				}else{
					return "main";
				}
			}
			
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
		return "fail";
	}
	
	public void initialize() {
	}
}
