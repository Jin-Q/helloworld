package com.yucheng.cmis.platform.permission.resource.op;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.operation.CMISOperation;

public class CMISDeleteResourceOperation extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		String sql = null;
		Connection connection = null;
		PreparedStatement state = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			connection = this.getConnection(context);
			String resourceId = null;
			try {
				resourceId = (String)context.getDataValue("s_resource__resourceid");
			} catch (Exception e) {}
			if(resourceId == null)
				throw new CMISException("请指定所要删除的资源编号!");
			
			//找到当前资源以及其子资源
			sql = "select resourceid from s_resource start with resourceid=? connect by prior resourceid=parentid";
			state = connection.prepareStatement(sql);
			state.setObject(1, resourceId);
			rs = state.executeQuery();
			stmt = connection.createStatement();
			while(rs.next()){
				resourceId = rs.getString(1);
				sql = "delete from s_roleright where resourceid='" + resourceId + "'";
				stmt.addBatch(sql);
				sql = "delete from s_resourceaction where resourceid='" + resourceId + "'";
				stmt.addBatch(sql);
				sql = "delete from s_resource where resourceid='" + resourceId + "'";
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
		}catch(EMPException el){
			throw el;
		}catch(Exception e){
			throw new EMPException(e);
		}finally{
			if(stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {}
			}
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
		
		return "0";
	}

	@Override
	public void initialize() {

	}

}
