package com.yucheng.cmis.platform.permission.resource.component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.accesscontroll.PermissionAccessController;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.organization.domains.SRole;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.permission.domain.CMISResource;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISConstant;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.CMISJSONUtil;

/**
 * 
 * <p>
 * 	权限文件生成类
 * 	
 * 	<ul>备注：将组织机构管理模块S_ROLEUSER表的直接访问，改为通过服务接口的方式访问</ul>
 * </p>
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
public class CMISRolePermissionComponent extends CMISComponent{
	
	//用于判断菜单是否可见的操作
	public static String ATTR_VISIT = "visit";
	
	/**
	 * 生成特定用户(actorNo)的权限文件和访问控制文件
	 * @param context
	 * @param actorNo
	 * @param connection
	 * @throws EMPException
	 */
	public void generatePermissionFile(String actorNo, Connection connection) throws EMPException{
		EMPLog.log(CMISConstance.CMIS_PERMISSION, EMPLog.DEBUG, 0, "generate the PermissionFile of actorNo["+actorNo+"]!");
		
		//PermissionAccessController accessController = (PermissionAccessController)context.getService(CMISConstance.ATTR_PERMISSIONACCESS);
		
		//菜单操作权限文件的内容(.json文件)
		StringBuffer buf = new StringBuffer();
		
		//访问控制权限文件的内容(.xml文件)
		StringBuffer xmlBuffer = new StringBuffer();
		
		buf.append("{menuTree:{id:'root',children:[");
		
		String sql = "";
		Statement statement = null;
		ResultSet rs = null;
		HashMap resourcesMap = new HashMap();
		List rootResource = new LinkedList();
		String roleList = "";//用户角色列表
		try{
			
			//调用组织机构管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			OrganizationServiceInterface orgService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", CMISConstant.ORGANIZATION_MODUAL_ID);
			List<SRole> userRoles = orgService.getRolesByUserId(actorNo, connection);
			//将角色拼成('','')方便后面的SQL查询
			roleList = "(";
			for (int i = 0; i < userRoles.size(); i++) {
				SRole role = userRoles.get(i);
				roleList += "'"+role.getRoleno()+"',";
			}
			if(roleList.endsWith(","))roleList = roleList.substring(0, roleList.length()-1);
			roleList += ")";
			
			if(roleList.equals("()")) roleList = "('')";
			
			//获得特定用户的所有有权限的资源
			sql = "select distinct s.RESOURCEID,s.CNNAME,s.PARENTID,s.SYSTEMPK,s.URL,s.ORDERID from S_RESOURCE s,S_ROLERIGHT r where r.ROLENO in "+roleList+" and r.RESOURCEID=s.RESOURCEID and r.ACTID='"+ATTR_VISIT+"' order by s.ORDERID";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()){
				String resourceId = rs.getString(1);
				String cnName = rs.getString(2);
				CMISResource resource = (CMISResource)resourcesMap.get(resourceId);
				if(resource == null){
					resource = new CMISResource();
					resourcesMap.put(resourceId, resource);
				}
				resource.resourceId = resourceId;
				resource.cnName = cnName;
				resource.url = rs.getString(5);
				
				//将当前资源的权限设置到访问权限控制文件中
				if(resource.url != null && PermissionAccessController.accessController != null){
					int idx = resource.url.indexOf(".do");
					if(idx != -1){
						String actId = resource.url.substring(0, idx);
						if(PermissionAccessController.accessController.isCheckPermission(actId))
							xmlBuffer.append("<action id=\"" + actId + "\"/>");
					}
				}
				
				String parentId = rs.getString(3);
				if(parentId == null || "".equals(parentId.trim())){
					rootResource.add(resource);
				}else{
					CMISResource parent = (CMISResource)resourcesMap.get(parentId);
					if(parent == null){
						parent = new CMISResource();
						resourcesMap.put(parentId, parent);
					}
					parent.addCMISResource(resource);
				}
			}
			Iterator iterator = rootResource.iterator();
	        boolean hasNext = iterator.hasNext();
	        while (hasNext) {
	        	CMISResource resource = (CMISResource)iterator.next();
	            hasNext = iterator.hasNext();
	            buf.append(resource.toJSONString());
	            if (hasNext)
	                buf.append(",");
	        }
			buf.append("]}");
		}catch(Exception e){
			EMPLog.log(CMISConstance.CMIS_PERMISSION, EMPLog.ERROR, 0, "generatePermissionFile error!", e);
			throw new EMPException(e);
		}finally{
			if(rs != null){
				try{
					rs.close();
				} catch (SQLException se1) {}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException se1) {}
			}
		}
		
		
		buf.append(",operations:{");
		String resourceId = null;
		resourcesMap = new HashMap();
		try{
			//获得特定用户的所有有权限的操作
			sql = "select distinct a.RESOURCEID, a.ACTID, a.DESCR, a.CONFIRMMSG from S_ROLERIGHT r, S_RESOURCEACTION a where r.ROLENO in "+roleList+" and r.RESOURCEID=a.RESOURCEID and r.ACTID=a.ACTID and a.ACTID <> '"+ATTR_VISIT+"' order by a.RESOURCEID";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			boolean hasOperation = false;
			while(rs.next()){
				hasOperation = true;
				String newResourceId = rs.getString(1);
				String actId = rs.getString(2);
				String descr = rs.getString(3);
				String confirmMsg = rs.getString(4);
				if(confirmMsg == null)
					confirmMsg = "";
				
				if(resourceId == null){
					resourceId = newResourceId;
					buf.append(CMISJSONUtil.normalizeString(resourceId)).append(":{");
				}else if(resourceId != null && resourceId.equals(newResourceId)){
					buf.append(",");
				} else{
					resourceId = newResourceId;
					buf.append("},").append(CMISJSONUtil.normalizeString(resourceId)).append(":{");
				}
				
				buf.append(CMISJSONUtil.normalizeString(actId)).append(":");
				buf.append("{label:'").append(CMISJSONUtil.normalizeString(descr));
				buf.append("',tip:'',confirm:'").append(CMISJSONUtil.normalizeString(confirmMsg));
				buf.append("',type:'list_single',defaultPriv:'true',defaultOp:'true'}");
				
				//访问控制权限文件的内容
				if(PermissionAccessController.accessController != null && PermissionAccessController.accessController.isCheckPermission(actId))
					xmlBuffer.append("<action id=\"" + actId + "\"/>");
			}
			if(hasOperation)
				buf.append("}");
		}catch(Exception e){
			EMPLog.log(CMISConstance.CMIS_PERMISSION, EMPLog.ERROR, 0, "generatePermissionFile error!", e);
			throw new EMPException(e);
		}finally{
			if(rs != null){
				try{
					rs.close();
				} catch (SQLException se1) {}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException se1) {}
			}
		}
		
		buf.append("}}");
		
		//将buf中的信息写入相应用户的权限文件中
		this.writeToUserPermissionFile(buf, actorNo);
		this.writeToAccessFile(xmlBuffer, actorNo);
		
	}
	
	/**
	 * 当用户权限文件的内容写入文件中
	 * @param buf
	 * @param actorNo
	 * @throws EMPException
	 */
	private void writeToUserPermissionFile(StringBuffer buf, String actorNo) throws EMPException{
				
		String fileName = CMISConstance.PERMISSIONFILE_PATH + "/client/" + actorNo + ".json"; 
		
		FileOutputStream fo = null;
		Writer out = null;
		try {
			File file = new File(fileName);
			if (!file.exists())
				file.createNewFile();
			
			fo = new FileOutputStream(fileName, false);
			out = new BufferedWriter(new OutputStreamWriter(fo, "UTF-8")); 
			out.write(buf.toString());
		} catch (Exception e) {
			EMPLog.log(CMISConstance.CMIS_PERMISSION, EMPLog.ERROR, 0, "writeToUserPermissionFile error!", e);
			throw new EMPException(e);
		} finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {}
			}
			if (fo != null){
				try {
					fo.close();
				} catch (IOException e) {}
			}
		}
	}
	
	/**
	 * 将用户的访问控制文件写到文件中
	 * @param accessList
	 * @param actorNo
	 * @throws EMPException
	 */
	private void writeToAccessFile(StringBuffer buf, String actorNo)throws EMPException {
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<?xml version='1.0' encoding='UTF-8' ?>");
		xmlBuffer.append("<permission.xml>");

		xmlBuffer.append("<classMap><map id=\"user\" class=\"com.yucheng.cmis.accesscontroll.UserObject\"/><map id=\"action\" class=\"com.yucheng.cmis.accesscontroll.ActionObject\"/></classMap>");
		xmlBuffer.append("<user id=\"" + actorNo + "\">");

		xmlBuffer.append(buf);

		xmlBuffer.append("</user>");
		xmlBuffer.append("</permission.xml>");

		// 将DOM文件内容写入XML文件中
		String fileName = CMISConstance.PERMISSIONFILE_PATH + "/server/" + actorNo + ".xml";
		
		FileOutputStream fo = null;
		Writer out = null;
		try {
			File file = new File(fileName);
			if (!file.exists())
				file.createNewFile();
			
			fo = new FileOutputStream(fileName, false);
			out = new BufferedWriter(new OutputStreamWriter(fo, "UTF-8"));
			out.write(xmlBuffer.toString());
		} catch (Exception e) {
			EMPLog.log(CMISConstance.CMIS_PERMISSION, EMPLog.ERROR, 0, "writeToAccessFile error!", e);
			throw new EMPException(e);
		} finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {}
			}
			if (fo != null){
				try {
					fo.close();
				} catch (IOException e) {}
			}
		}
	}
}
