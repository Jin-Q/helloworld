package com.yucheng.cmis.platform.organization.steamorg.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QuerySTeamOrgDetailOp  extends CMISOperation {
	
	private final String modelId = "STeamOrg";
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String team_org_id = (String) context.getDataValue("team_org_id");
			String team_no = (String) context.getDataValue("team_no");
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("team_no",team_no);
			pkMap.put("team_org_id",team_org_id);
			
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			//成员名称翻译
			String[] args=new String[] { "team_org_id","team_no" };
			String[] modelIds=new String[]{"SOrg","STeam"};
			String[] modelForeign=new String[]{"organno ","team_no"};
			String[] fieldName=new String[]{"organname","team_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			this.putDataElement2Context(kColl, context);
			
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
