package com.yucheng.cmis.pub.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.domains.SRole;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 *跨域集成访问验证 成功返回success，失败返回fail
 * 
 * @author lisj
 * @time 2014-12-17 15:56:01
 * @description 需求编号：【FX140619013】 十二级系统查看客户信息功能（系统通用跨域访问方法）
 * @version v1.0
 */
public class DynamicLinkSystemOp extends CMISOperation {
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	private static final Logger logger = Logger.getLogger(CMISUserCheckOperation.class);
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		HttpServletRequest request=super.getHttpServletRequest(context);
		OrganizationServiceInterface orgMsi = null;
		PreparedStatement cusBLpstmt = null;
		ResultSet cusBL = null;
		String currentUserId = "";
		String cus_id = "";
		String cus_bline ="";//客户所属条线
		String queryType = "";//跨域查询类型
		String returnResult = "success";
		try {
			connection = this.getConnection(context);
					
			currentUserId = request.getParameter("currentUserId");
			queryType = request.getParameter("query_type");
			if(currentUserId ==  null || currentUserId.equals("")){
				returnResult = "failed";
			}
			if(queryType ==  null || queryType.equals("")){
				returnResult = "failed";
			}
			logger.info("*********************************************");
		    logger.info("***************currentUserId:" + currentUserId);

		    orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			SUser user = orgMsi.getUserByUserId(currentUserId, connection);
			List<SDuty> dutyList = orgMsi.getDutysByUserId(currentUserId, connection);
			List<SRole> roleList = orgMsi.getRolesByUserId(currentUserId, connection);
			String dutynos = "", dutynames = "";
			String rolenos = "", rolenames = "";
			String dutys = "", roles = "";
			//获取登录人员岗位，角色
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
			
			//跨域查询类型为CUSINFO（查询客户基本信息）
			if(queryType.equals("CUSINFO")){
				cus_id = request.getParameter("cus_id");
				//获取客户所属业务条线
				String cusBLSql =  "select c.BELG_LINE from cus_base c where c.cus_id='"+cus_id+"'";
				cusBLpstmt = connection.prepareStatement(cusBLSql);
				cusBL = cusBLpstmt.executeQuery();
				if(cusBL.next()){
					cus_bline = cusBL.getString(1);
				}
			}
			context.getParent().put("currentUserId", currentUserId);
			context.getParent().put("loginuserid", currentUserId);
			context.getParent().put("loginusername", user.getActorname());
			context.getParent().put("loginRoleNameList", rolenames);
			context.getParent().put("cus_bline", cus_bline);
			
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			try {
				if(cusBLpstmt != null){
					cusBLpstmt.close();
					cusBLpstmt = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return returnResult;
	}
}
