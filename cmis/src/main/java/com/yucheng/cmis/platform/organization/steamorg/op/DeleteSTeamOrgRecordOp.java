package com.yucheng.cmis.platform.organization.steamorg.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteSTeamOrgRecordOp extends CMISOperation {

	private final String modelId = "STeamOrg";
	


	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String team_org = "";
		String memNo;
		String team_no="";
		int count = 0;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			team_org = (String) context.getDataValue("team_org");
			team_no = (String) context.getDataValue("team_no");
			Map pkMap = new HashMap();
			//批量删除
			String team_orgs[] = team_org.split(",");
			for(int i=0;i<team_orgs.length;i++){
				team_org = team_orgs[i];
				pkMap.put("team_no",team_no);
				pkMap.put("team_org_id",team_org);
				count=dao.deleteByPks(modelId, pkMap, connection);
				if(count!=1){
					throw new EMPException("Remove Failed! Records :"+count);
				}
			}
			
			context.addDataField("flag","success");
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
