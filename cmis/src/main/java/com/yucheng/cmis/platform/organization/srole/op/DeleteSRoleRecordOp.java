package com.yucheng.cmis.platform.organization.srole.op;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteSRoleRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SRole";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String deleteRoleRightSql="deleteRoleRight";
		String deleteRoleUserSql="deleteRoleUser";
		try{
			connection = this.getConnection(context);
			//获得删除需要的主键信息
			String roleNos = null;
			try {
				roleNos = (String)context.getDataValue("roleNos");
			} catch (Exception e) {}
			if(roleNos == null || roleNos.length() == 0){
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, modelId+":获取角色主键失败");
				throw new EMPJDBCException("The value of pk roleNos cannot be null!");
			}

			String[] roleNoArray=roleNos.split(",");
			/**
			 * 删除角色、角色权限（多条）、角色用户（多条）
			 */
			TableModelDAO dao = this.getTableModelDAO(context);
			for(String roleNo:roleNoArray){
				//删除角色
				int count=dao.deleteByPk(modelId, roleNo, connection);
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, modelId+":删除"+count+"条角色 记录！");
				if(count!=1){
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, modelId+":删除角色失败！");
					throw new EMPException("删除数据失败！操作影响了"+count+"条记录");
				}
				//删除角色权限
				int cnt1=SqlClient.delete(deleteRoleRightSql, roleNo, connection);
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, modelId+":删除"+cnt1+"条角色权限 记录！");
				//删除角色用户
				int cnt2=SqlClient.delete(deleteRoleUserSql, roleNo, connection);
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, modelId+":删除"+cnt2+"条角色用户 记录！");
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (Exception ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "删除失败！失败原因："+ee.getMessage());
			ee.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
