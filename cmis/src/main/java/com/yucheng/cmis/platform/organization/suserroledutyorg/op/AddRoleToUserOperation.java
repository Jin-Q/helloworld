package com.yucheng.cmis.platform.organization.suserroledutyorg.op;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddRoleToUserOperation extends CMISOperation{
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		Statement statement = null;	
		Statement batchStatement = null;
		try{
			connection = this.getConnection(context);
			statement = connection.createStatement();
			batchStatement = connection.createStatement();
			String actorNo = (String)context.getDataValue("actorno");
			String roleno = (String)context.getDataValue("roleno");//多值，逗号","分割
			String states = (String)context.getDataValue("state");//true：添加；false：删除
			if(actorNo == null || roleno == null || states == null)
				throw new EMPException("The update RoleRight data is null!");
			String[] roleNoList = roleno.split(",");
			String[] stateList = states.split(",");
			for (int i = 0; i < roleNoList.length; i++) {
				String roleId = roleNoList[i];
				String state = stateList[i];
				String sql = "select roleno from S_ROLEUSER where ACTORNO='"+actorNo+"' and ROLENO='"+roleId+"'";
				ResultSet rs = statement.executeQuery(sql);
				System.out.println("=====================================");				
				if(rs.next()){//已存在该条记录
					if("false".equals(state))
					{
						sql = "delete from S_ROLEUSER where ACTORNO='"+actorNo+"' and ROLENO='"+roleId+"'";
						System.out.println(sql);
						batchStatement.addBatch(sql);
					}
				}else{//记录不存在
					if("true".equals(state))
						sql = "insert into S_ROLEUSER(ACTORNO,ROLENO,STATE) values('"+actorNo+"','"+roleId+"',1)";
					System.out.println(sql);
					batchStatement.addBatch(sql);
				}
				rs.close();
				rs = null;
			}
			statement.close();
			statement = null;
			batchStatement.executeBatch();
		
			batchStatement.close();
			batchStatement = null;
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception se1) {
				}
			}
			if (batchStatement != null) {
				try {
					batchStatement.close();
				} catch (Exception se1) {
				}
			}
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
	public void initialize() {}
}
