package com.yucheng.cmis.pub.base;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class CMISUserSignOnNextOperation extends CMISOperation {
	
	/**
	 * 查询出系统当前登录用户的可操作人员列表，以及为了适应泉州银行一个用户多机构
	 * 所进行的选择登录机构的改造,前端展示页面大致样式为：
	 * ------用户名--------用户归属（托管、本身）-------所属机构---------
	 */
	public String doExecute(Context context) throws EMPException {
		String returnResult = "";
		Connection connection = null;
		OrganizationServiceInterface orgMsi = null;
		try {
			/*if(context==null||context.getParent()==null){
			 	throw new SessionException("登陆超时!");
		 	}
			HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
			request.getSession().setAttribute("sessionManager", context.getDataValue("sessionManager"));
			request.getSession().setAttribute(EMPConstance.ATTR_CONTEXT, context);*/
			
			connection = this.getConnection(context);
			orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			
			String userId = (String)context.getDataValue("userId");
			String userName = "";
			String orgNo = "";
			String orgName = "";
			
			if(userId == null || userId.trim().length() == 0){
				throw new EMPException("获取用户ID失败，请检查登录！");
			}
			SUser user = orgMsi.getUserByUserId(userId, connection);
			if(user != null){
				userName = user.getActorname();
				//orgNo = user.getOrgid();
			}
		
			TableModelDAO dao = this.getTableModelDAO(context);
			
			/**查询配置皮肤信息，若没有配置则默认赋值01*****************/
			IndexedCollection iColltmp = dao.queryList("PubSkin", "where actorno='"+userId+"'", connection);
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
			
			IndexedCollection sDeptUserIColl = dao.queryList("SDeptuser", " where actorno = '"+userId+"'", connection);
			if(sDeptUserIColl == null || sDeptUserIColl.size() == 0){
				throw new EMPException("用户"+userId+"未设置所属机构信息，请确认！");
			}
			/** 为返回做封装预留 */
			IndexedCollection returnIColl = new IndexedCollection();
			
			KeyedCollection retrunKColl = new KeyedCollection();
			retrunKColl.setName("UserLogin");
			retrunKColl.addDataField("userId", userId);
			retrunKColl.addDataField("userName", userName);
			
			if(sDeptUserIColl.size() == 1){
				/** 只存在单个机构，单个选择直接进入主页面 */
				KeyedCollection sDeptUserKColl = (KeyedCollection)sDeptUserIColl.get(0);
				orgNo = (String)sDeptUserKColl.getDataValue("organno");
				SOrg curOrg = orgMsi.getOrgByOrgId(orgNo, connection);
				orgName = curOrg.getOrganname();
				
				retrunKColl.addDataField("orgNo", orgNo);
				retrunKColl.addDataField("orgName", orgName);
				
				HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
				if(context.getParent().containsKey(CMISConstance.ATTR_ORGID)){
					context.getParent().setAttribute(CMISConstance.ATTR_ORGID, orgNo);
					context.getParent().put(CMISConstance.ATTR_ORGID, orgNo);
					
					request.getSession().setAttribute(WorkFlowConstance.ATTR_ORGID, orgNo);//流程登录需要将机构设置到SESSION中  2014-06-07 唐顺岩
				}else {
					context.getParent().put(CMISConstance.ATTR_ORGID, orgNo);
					request.getSession().setAttribute(WorkFlowConstance.ATTR_ORGID, orgNo);  //流程登录需要将机构设置到SESSION中  2014-06-07 唐顺岩
				}
				if(context.getParent().containsKey(CMISConstance.ATTR_ORGNAME)){
					context.getParent().setAttribute(CMISConstance.ATTR_ORGNAME, orgName); 
				}else {
					context.getParent().put(CMISConstance.ATTR_ORGNAME, orgName);
				}
				returnResult = "main";
			}else {
				/** 构造机构字典项，所属机构则为字典项配置 ，封装返回字典项 STD_ORG_HELP_TYPE*/
				IndexedCollection dicIColl = new IndexedCollection("STD_ORG_HELP_TYPE");
				for(int i=0;i<sDeptUserIColl.size();i++){
					KeyedCollection sDeptUserKColl = (KeyedCollection)sDeptUserIColl.get(i);
					KeyedCollection dicKColl = new KeyedCollection();
					orgNo = (String)sDeptUserKColl.getDataValue("organno");
					dicKColl.addDataField("enname", orgNo);
					SOrg curOrg = orgMsi.getOrgByOrgId(orgNo, connection);
					orgName = curOrg.getOrganname();
					dicKColl.addDataField("cnname", orgName);
					dicIColl.add(dicKColl);
				}
				
				this.putDataElement2Context(dicIColl, context);
				returnResult = "next";
			}
			
			retrunKColl.setName("UserLogList");
			returnIColl.add(retrunKColl);
			returnIColl.setName("UserLoginList");
			this.putDataElement2Context(retrunKColl, context);
			this.putDataElement2Context(returnIColl, context);
			
		} catch (Exception e) {
			throw new EMPException("用户未设置所属机构信息，请确认！"+e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		return returnResult;
	}

}
