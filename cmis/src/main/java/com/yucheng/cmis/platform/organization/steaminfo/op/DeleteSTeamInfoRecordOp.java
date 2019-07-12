package com.yucheng.cmis.platform.organization.steaminfo.op;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteSTeamInfoRecordOp extends CMISOperation {

	private final String modelId = "STeamInfo";
	

	private final String team_no_name = "team_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);


			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String team_no_value = null;
			try {
				team_no_value = (String)context.getDataValue(team_no_name);
			} catch (Exception e) {}
			if(team_no_value == null || team_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+team_no_name+"] cannot be null!");
				
			//中文转码
			team_no_value = URLDecoder.decode(team_no_value,"UTF-8");

			TableModelDAO dao = this.getTableModelDAO(context);
			//级联删除团队成员信息
			Map<String,String> refFields = new HashMap<String,String>();
			refFields.put("team_no", team_no_value);
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			lmtComponent.deleteByField("STeamMem", refFields);
			int count=dao.deleteByPk(modelId, team_no_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
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
