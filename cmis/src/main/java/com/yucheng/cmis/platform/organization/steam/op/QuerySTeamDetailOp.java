package com.yucheng.cmis.platform.organization.steam.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QuerySTeamDetailOp  extends CMISOperation {
	
	private final String modelId = "STeam";
	

	private final String team_no_name = "team_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String team_no_value = null;
			try {
				team_no_value = (String)context.getDataValue(team_no_name);
			} catch (Exception e) {}
			if(team_no_value == null || team_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+team_no_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, team_no_value, connection);
			this.putDataElement2Context(kColl, context);
			SInfoUtils.addSOrgName(kColl, new String[]{"team_org_id","input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
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
