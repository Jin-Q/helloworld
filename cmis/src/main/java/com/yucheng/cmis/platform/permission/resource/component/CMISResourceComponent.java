package com.yucheng.cmis.platform.permission.resource.component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.permission.domain.CMISAct;
import com.yucheng.cmis.platform.permission.domain.CMISResource;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.util.CMISJSONUtil;

public class CMISResourceComponent extends CMISComponent {
	
	/**
	 * 根据数据库中的数据生成资源定义中的资源树(以JSON字符串返回)
	 * @param connection
	 * @return
	 */
	public String buildResourceJSONTree(Connection connection) throws Exception{
		StringBuffer buf = new StringBuffer();
		buf.append("{id:'root',label:'资源操作定义',children:[");
		String sql = "select RESOURCEID,CNNAME,PARENTID,SYSTEMPK from S_RESOURCE order by ORDERID";
		HashMap resourcesMap = new HashMap();
		List rootResource = new LinkedList();
		
		PreparedStatement state = null;
		ResultSet rs = null;
		try {
			state = connection.prepareStatement(sql);
			rs = state.executeQuery();
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
		} catch (SQLException e) {
			EMPLog.log(CMISConstance.CMIS_PERMISSION, EMPLog.ERROR, 0, "buildResourceJSONTree error!", e);
			throw e;
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {}
			}
			if(state != null){
				try {
					state.close();
				} catch (SQLException e) {}
			}
		}
		return buf.toString();
	}
	
	/**
	 * 得到指定角色、指定资源的权限信息(其中resourceId若为空，则查询指定角色的全部权限信息)
	 * @param roleNo
	 * @param resourceId
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public String getRolePermissionJSONTree(String roleNo, String resourceId, Connection connection) throws Exception{
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		
		String sql = null;
		if(resourceId != null && !"".equals(resourceId))
			sql = "select RESOURCEID,ACTID from S_ROLERIGHT where ROLENO=? and RESOURCEID=?";
		else
			sql = "select RESOURCEID,ACTID from S_ROLERIGHT where ROLENO=?";
		
		HashMap resourcesMap = new HashMap();
		PreparedStatement state = null;
		ResultSet rs = null;
		try {
			state = connection.prepareStatement(sql);
			
			state.setString(1, roleNo);
			if(resourceId != null && !"".equals(resourceId))
				state.setString(2, resourceId);
			
			rs = state.executeQuery();
			while(rs.next()){
				resourceId = rs.getString(1);
				List acts = (List)resourcesMap.get(resourceId);
				if(resourcesMap.get(resourceId) == null){
					acts = new LinkedList();
					resourcesMap.put(resourceId, acts);
				}
				acts.add(rs.getString(2));
			}
		} catch (SQLException e) {
			EMPLog.log(CMISConstance.CMIS_PERMISSION, EMPLog.ERROR, 0, "getRolePermissionJSONTree error for roleNo["+roleNo+"] and resourceId["+resourceId+"]!", e);
			throw e;
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {}
			}
			if(state != null){
				try {
					state.close();
				} catch (SQLException e) {}
			}
		}
		Object[] resourceIds = resourcesMap.keySet().toArray();
		for (int i = 0; i < resourceIds.length; i++) {
			resourceId = (String) resourceIds[i];
			buf.append("{resourceid:'").append(CMISJSONUtil.normalizeString(resourceId));
			buf.append("',operations:{");
			List acts = (List) resourcesMap.get(resourceId);
			Iterator iterator = acts.iterator();
			boolean hasNext = iterator.hasNext();
			while (hasNext) {
				String actId = (String) iterator.next();
				hasNext = iterator.hasNext();
				buf.append(CMISJSONUtil.normalizeString(actId) + ":true");
				if (hasNext)
					buf.append(",");
			}
			buf.append("}},");
		}
		if(resourceIds.length > 0)
			buf.deleteCharAt(buf.length()-1);
		buf.append("]");
		return buf.toString();
	}
	
	/**
	 * 生成角色权限定义页面所需要的JSON字符串
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public String buildResourceActJSONTree(Connection connection) throws Exception{
		StringBuffer buf = new StringBuffer();
		buf.append("{id:'root',label:'资源操作定义',children:[");
		String sql = "select s.RESOURCEID,s.CNNAME,a.ACTID,a.DESCR,s.PARENTID,s.SYSTEMPK from S_RESOURCE s,S_RESOURCEACTION a where s.RESOURCEID=a.RESOURCEID order by s.ORDERID";
		HashMap resourcesMap = new HashMap();
		List rootResource = new ArrayList();
		
		PreparedStatement state = null;
		ResultSet rs = null;
		try {
			state = connection.prepareStatement(sql);
			rs = state.executeQuery();
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
				
				String parentId = rs.getString(5);
				if(parentId == null || "".equals(parentId.trim())){
					if(!rootResource.contains(resource))//若rootResource中未包括当前资源，则加入
						rootResource.add(resource);
				}else{
					CMISResource parent = (CMISResource)resourcesMap.get(parentId);
					if(parent == null){
						parent = new CMISResource();
						resourcesMap.put(parentId, parent);
					}
					CMISResource child = parent.getChild(resourceId);
					if(child == null){
						parent.addCMISResource(resource);
					}
				}

				CMISAct act = new CMISAct();
				act.actId = rs.getString(3);
				act.descr = rs.getString(4);
				resource.addCMISAct(act);
			}
			for(int i=0;i<rootResource.size();i++){
				CMISResource resource = (CMISResource)rootResource.get(i);
				buf.append(resource.toJSONString());
				if(i < rootResource.size() -1)
					buf.append(",");
			}
			buf.append("]}");
		} catch (SQLException e) {
			EMPLog.log(CMISConstance.CMIS_PERMISSION, EMPLog.ERROR, 0, "buildResourceActJSONTree error!", e);
			throw e;
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {}
			}
			if(state != null){
				try {
					state.close();
				} catch (SQLException e) {}
			}
		}
		return buf.toString();
	}
	
	/**
	 * 删除资源
	 * @param context
	 * @param connection
	 */
	public void removeResource(String resourceId, Connection cn) throws Exception{
		List list=new ArrayList();
		list.add(resourceId);
		findChildResource(resourceId,list,cn);//查找所有的关连下级资源
		removeDBResource(cn,list);		
	}	
	
	/*
	 * 删除数据库资源
	 * @param context
	 * @param connection
	 */
	public void removeDBResource(Connection connection,List resources) throws Exception{
		
			Statement state = null;
		try{
			state = connection.createStatement();
			
			for (int i = 0; i < resources.size(); i++) {
				String resourceId = (String)resources.get(i);
				String sqlStr = "delete from S_RESOURCE where RESOURCEID='" + resourceId + "'";//资源表
				state.execute(sqlStr);
				sqlStr = "delete from S_ROLERIGHT where RESOURCEID='" + resourceId + "'";//角色权限表
				state.execute(sqlStr);
				sqlStr = "delete from S_RESOURCEACTION where RESOURCEID='" + resourceId + "'";//资源操作表
				state.execute(sqlStr);
			}
			state.close();
			state = null;		
		}catch(Exception e){
			throw e;
		}finally{
			if (state != null) {
				try {
					state.close();
				} catch (SQLException se1) {
				}
			}
		}
	}
	/*
	 * 将此资源所有节点放入list中
	 * @param resource
	 * @param list
	 */
	public void findChildResource(String resourceId,List list,Connection cn)throws Exception{
		System.out.println("[findChildResource],resourceId="+resourceId);
		Statement state = null;
		ResultSet rs=null;
		String resid;
		try{
			state = cn.createStatement();
			rs=state.executeQuery("select RESOURCEID from S_RESOURCE where PARENTID='"+resourceId+"'");
			while(rs.next()){
				resid=rs.getString(1);
				list.add(resid);
				this.findChildResource(resid, list, cn);
			}
			rs.close();
			rs=null;
			state.close();
			state = null;
		}catch(Exception e){
			throw e;
		}finally{
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se1) {}
			}
			if (state != null) {
				try {
					state.close();
				} catch (SQLException se1) {}
			}			
		}
	}
}
