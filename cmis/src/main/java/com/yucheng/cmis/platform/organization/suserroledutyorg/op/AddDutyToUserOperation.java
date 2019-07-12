package com.yucheng.cmis.platform.organization.suserroledutyorg.op;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddDutyToUserOperation extends CMISOperation{
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
			String dutyNo = (String)context.getDataValue("dutyno");//多值，逗号","分割
			String states = (String)context.getDataValue("state");//true：添加；false：删除
			if(actorNo == null || dutyNo == null || states == null)
				throw new EMPException("The update RoleRight data is null!");
			String[] dutyNoList = dutyNo.split(",");
			String[] stateList = states.split(",");
			for (int i = 0; i < dutyNoList.length; i++) {
				String dutyId = dutyNoList[i];
				String state = stateList[i];
				String sql = "select dutyno from S_DUTYUSER where ACTORNO='"+actorNo+"' and DUTYNO='"+dutyId+"'";
				ResultSet rs = statement.executeQuery(sql);
				
				//已存在该条记录
				if(rs.next()){
					if("false".equals(state))
					{
						sql = "delete from S_DUTYUSER where ACTORNO='"+actorNo+"' and DUTYNO='"+dutyId+"'";
						
						batchStatement.addBatch(sql);
						
					}
				}else{
					if("true".equals(state))
						sql = "insert into S_DUTYUSER(ACTORNO,DUTYNO,STATE) values('"+actorNo+"','"+dutyId+"',1)";
					batchStatement.addBatch(sql);
				}
				rs.close();
				rs = null;
			}
			statement.close();
			statement = null;
			int[] results = batchStatement.executeBatch();
			
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
