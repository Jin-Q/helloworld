package com.yucheng.cmis.platform.organization.steam.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteSTeamRecordOp extends CMISOperation {

	private final String modelId = "STeam";
	

	private final String team_no_name = "team_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String team_no_value = null;
			try {
				team_no_value = (String)context.getDataValue(team_no_name);
			} catch (Exception e) {}
			if(team_no_value == null || team_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+team_no_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, team_no_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			String delUserSql=" DELETE FROM S_Team_User t WHERE t.team_no='"+team_no_value+"' ";
			String delOrgSql=" DELETE FROM S_Team_Org t WHERE t.team_no='"+team_no_value+"' ";
			SqlClient.deleteBySql(delUserSql, connection);
			SqlClient.deleteBySql(delOrgSql, connection);
			context.addDataField("flag", "success");
			context.addDataField("msg", "已级联删除团队下的成员信息！");
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
