package com.yucheng.cmis.pub.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.easycon.busi.element.UniKeyValueObject;
import com.easycon.portal.client.PortalClient;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.domains.SRole;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.MD5;

/**
 *统一平台登录验证 成功返回success，失败返回fail
 * 
 * @author 
 * 
 */
public class UniteAttestationOp extends CMISOperation {
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	private static final Logger logger = Logger.getLogger(CMISUserCheckOperation.class);
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		HttpServletRequest request=super.getHttpServletRequest(context);
		PreparedStatement state_sys = null;
		ResultSet rs_sys = null;
		OrganizationServiceInterface orgMsi = null;
		PreparedStatement datepstmt = null;
		PreparedStatement rolepstmt = null;
		ResultSet daters = null;
		ResultSet rolers = null;
		String userId = "";
		String returnResult = "";
		try {
			connection = this.getConnection(context);
			
			state_sys =connection.prepareStatement("select nvl(count(*),0) num from pub_sys_info where trim(progress)!='0'");
			rs_sys = state_sys.executeQuery();
			if(rs_sys.next()){
				int flag =rs_sys.getInt(1);
				if(flag>0){
					returnResult = "fail_sys";
				}
			}
			
			String appLoginKey = request.getParameter("AppLoginKey");
			if(appLoginKey ==  null || appLoginKey.equals("")){
				returnResult = "fail";
				//throw new Exception( "认证码为空，登陆失败，请重新登录。");
			}
			logger.info("*********************************************");
		    logger.info("***************AppLoginKey:" + appLoginKey);
		    //从cmis.properties中读取地址
		    ResourceBundle res = ResourceBundle.getBundle("cmis");
			String uniteIp = res.getString("UNITEIP");
			String unitePort = res.getString("UNITEPORT");
			
			PortalClient.initializeInstance(uniteIp, Integer.parseInt(unitePort));
			UniKeyValueObject uko = PortalClient.doRequestLoginInfo(appLoginKey);
			logger.info("**********发送交易完成***********************************");
			logger.info(uko.toString());
			if(logger.isDebugEnabled()){
				logger.info("交易码：" + uko.getIntValue("JYM") + "--应答码:"
					+ uko.getValue("YDM") + "--应答信息：" + uko.getValue("YDXX")
					+ "--用户id：" + uko.getValue("USERID") + "机构码--"
					+ uko.getValue("DEPTID"));
			}
			if ("00".equals(uko.getValue("YDM"))) {
				userId = uko.getValue("userId");
			} else {
				returnResult = "fail";
				//PackUtil.backErrorPack("" + uko.getIntValue("JYM"), "登陆失败");
	            //throw new BadAppLoginKeyException(messages.getMessage("SwitchUserProcessingFilter.disabled", "登陆失败，认证码不可用，AppLoginKey is disabled"));
				//throw new Exception("认证码不可用，登陆失败，请重新登录。");
			}
			UniKeyValueObject uko1 = PortalClient.doRequestPortalIndexPage();
			if ("00".equals(uko1.getValue("YDM"))) {
				if(logger.isDebugEnabled()){
					logger.info("得到的退出系统url是：-------" + uko1.getValue("PORTALURL"));
				}
			} 
			//userId = "admin";
			orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			SUser user = orgMsi.getUserByUserId(userId, connection);
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
			context.getParent().put("currentUserId", userId);
			context.getParent().put("loginuserid", userId);
			context.getParent().put("loginusername", user.getActorname());
			//context.getParent().put("loginorgid", curOrg.getOrganno());
			//context.getParent().put("loginorgname", curOrg.getOrganname());
			context.getParent().put("loginRoleNameList", rolenames);
			IndexedCollection iCollCusTrustee = orgMsi.getCusTrusteeByUserId(userId, connection);
			if(iCollCusTrustee!=null&&iCollCusTrustee.size()>0){
				context.getParent().addDataField("consignor", iCollCusTrustee);
				returnResult = "success";
			}else{
				returnResult = "main";
			}
			
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			try {
				if (state_sys != null) {
					state_sys.close();
					state_sys = null;
				}
				if (rs_sys != null) {
					rs_sys.close();
					rs_sys = null;
				}
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
		return returnResult;
	}
}
