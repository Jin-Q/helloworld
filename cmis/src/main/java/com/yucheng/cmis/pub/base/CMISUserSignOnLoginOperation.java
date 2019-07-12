package com.yucheng.cmis.pub.base;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class CMISUserSignOnLoginOperation extends CMISOperation {

	/**
	 * 选择当前登录人员信息确认，以及更新当前登录人员的相关信息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		OrganizationServiceInterface orgMsi = null;
		try {
			connection = this.getConnection(context);
			
			String orgNo = (String)context.getDataValue("orgNo");
			String loginuserid = (String)context.getDataValue("loginuserid");
			String currentUserId = (String)context.getDataValue("currentUserId");
			orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			
			if(orgNo == null || orgNo.trim().length() == 0){
				throw new EMPException("获取用户登录ID失败，请检查选择登录页面信息是否正确！");
			}
			
			/**查询配置皮肤信息，若没有配置则默认赋值01*****************/
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColltmp = dao.queryList("PubSkin", "where actorno='"+currentUserId+"'", connection);
			if(iColltmp.size()>0){
				KeyedCollection kColltmp = (KeyedCollection)iColltmp.get(0);
				String skinId = (String)kColltmp.getDataValue("skinid");
				if(skinId==null||"".equals(skinId)){
					skinId = "01";
				}
				context.getParent().put("SkinId", skinId);
			}else{
				context.getParent().put("SkinId", "01");
			}
			/************************************/
			
			SOrg curOrg = orgMsi.getOrgByOrgId(orgNo, connection);
			if(loginuserid.equals(currentUserId)){
				if(context.getParent().containsKey("loginorgid")){
					context.getParent().setDataValue("loginorgid", curOrg.getOrganno());
				}else {
					context.getParent().put("loginorgid", curOrg.getOrganno());
				}
				if(context.getParent().containsKey("loginorgname")){
					context.getParent().setDataValue("loginorgname", curOrg.getOrganname());
				}else {
					context.getParent().put("loginorgname", curOrg.getOrganname());
				}
			}
			if(context.getParent().containsKey(CMISConstance.ATTR_ORGID)){
				context.getParent().setDataValue(CMISConstance.ATTR_ORGID, curOrg.getOrganno());
			}else {
				context.getParent().put(CMISConstance.ATTR_ORGID, curOrg.getOrganno());
			}
			if(context.getParent().containsKey(CMISConstance.ATTR_ORGNAME)){
				context.getParent().setDataValue(CMISConstance.ATTR_ORGNAME, curOrg.getOrganname());
			}else {
				context.getParent().put(CMISConstance.ATTR_ORGNAME, curOrg.getOrganname());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return "0";
	}

}
