package com.yucheng.cmis.platform.organization.steam.op;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class UpdateSTeamRecordOp extends CMISOperation {
	private final String modelId = "STeam";
	private final String userModelId = "STeamUser";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			String team_no=(String) kColl.getDataValue("team_no");
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection oldKColl=dao.queryDetail(modelId, team_no, connection);
			String oldStatus=(String) oldKColl.getDataValue("status");
			String newStatus=(String) kColl.getDataValue("status");
			String oldOrg=(String) oldKColl.getDataValue("team_org_id");
			String newOrg=(String) kColl.getDataValue("team_org_id");
			
			if(oldOrg.equals(newOrg)){
				if("2".equals(oldStatus)&&"1".equals(newStatus)){//无效改为有效
					IndexedCollection iColl=dao.queryList(userModelId,"where team_no='"+team_no+"' and mem_no in (SELECT t.mem_no  FROM S_Team_User t,s_team p WHERE t.team_no=p.team_no and p.status='1') ", connection);
					if(iColl!=null&&iColl.size()>0){
						String teamNos="";
						for(int i=0;i<iColl.size();i++){
							KeyedCollection kcoll=(KeyedCollection) iColl.get(i);
							String mem_no=(String) kcoll.getDataValue("mem_no");
							teamNos+=mem_no+",";
						}
						context.addDataField("flag", "failure");
						context.addDataField("msg", "修改数据失败！团队成员："+teamNos.subSequence(0, teamNos.length()-1)+"已在其他的有效团队存在！");	
					}
				}
				
			}else{
				String delUserSql=" DELETE FROM S_Team_User t WHERE t.team_no='"+team_no+"' ";
				String delOrgSql=" DELETE FROM S_Team_Org t WHERE t.team_no='"+team_no+"' ";
				SqlClient.deleteBySql(delUserSql, connection);
				SqlClient.deleteBySql(delOrgSql, connection);
			}
			
			if(!context.containsKey("flag")){
				int count=dao.update(kColl, connection);
				if(count!=1){
					context.addDataField("flag", "failure");
					context.addDataField("msg", "修改数据失败！操作影响了"+count+"条记录");
					throw new EMPException("Update Failed! Record Count: " + count);
				}
				context.addDataField("flag", "success");
				context.addDataField("msg", "");
			}
			
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "修改数据失败！失败原因："+ee.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", "failure");
			context.addDataField("msg", "修改数据失败！失败原因："+e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
