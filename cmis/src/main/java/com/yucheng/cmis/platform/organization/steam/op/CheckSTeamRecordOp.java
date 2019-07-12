package com.yucheng.cmis.platform.organization.steam.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckSTeamRecordOp extends CMISOperation {
	

	private final String modelId = "STeam";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String team_no = null;
			try {
				team_no = (String)context.getDataValue("team_no");
			} catch (Exception e) {}
		
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection ic = dao.queryList(modelId, "where team_no='"+team_no+"'", connection);
			if(ic.size()!=0){
				context.addDataField("flag","fail");
			}else{
				context.addDataField("flag","success");
			}
			//异步操作标志
			
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
