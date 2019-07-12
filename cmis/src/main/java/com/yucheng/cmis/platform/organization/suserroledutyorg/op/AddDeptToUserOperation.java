package com.yucheng.cmis.platform.organization.suserroledutyorg.op;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddDeptToUserOperation extends CMISOperation{
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
			String organNo = (String)context.getDataValue("organno");//多值，逗号","分割
			String actorNo = (String)context.getDataValue("actorno");
			String states = (String)context.getDataValue("state");//true：添加；false：删除
			if(organNo == null || actorNo == null || states == null)
				throw new EMPException("The update DutyUser data is null!");
			String[] organNoList = organNo.split(",");
			String[] stateList = states.split(",");
			if(organNoList.length != stateList.length)
				throw new EMPException("The update UserRole data imcomplete!");
			
			for (int i = 0; i < organNoList.length; i++) {
				String orgId = organNoList[i];
				String state = stateList[i];
				String sql = "select organno from S_DEPTUSER where ACTORNO='"+actorNo+"' and ORGANNO='"+orgId+"'";
				ResultSet rs = statement.executeQuery(sql);
				//已存在该条记录
				if(rs.next()){
					if("false".equals(state))
					{
						sql = "delete from S_DEPTUSER where ACTORNO='"+actorNo+"' and ORGANNO='"+orgId+"'";
						batchStatement.addBatch(sql);
						
					}
				}else{
					if("true".equals(state))
						sql = "insert into S_DEPTUSER(ACTORNO,ORGANNO,STATE) values('"+actorNo+"','"+orgId+"',1)";
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
