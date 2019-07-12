package com.yucheng.cmis.platform.organization.steamuser.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateSTeamUserRecordOp extends CMISOperation {
	

	private final String modelId = "STeamUser";
	

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
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			String team_role=TagUtil.replaceNull4String(kColl.getDataValue("team_role"));
			if("00".equals(team_role)){
				String mem_no=TagUtil.replaceNull4String(kColl.getDataValue("mem_no"));
				String team_no=TagUtil.replaceNull4String(kColl.getDataValue("team_no"));
				IndexedCollection users=dao.queryList(modelId,"where team_no='"+team_no+"' and mem_no<>'"+mem_no+"' and team_role='00' ", connection);
				if(users!=null&&users.size()>0){
					context.addDataField("flag","error");
					context.addDataField("msg","修改失败,该团队已存在团队长！");
					return "0";
				}
				
			}
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			//异步操作标志
			context.addDataField("flag","success");
			context.addDataField("msg","");
		}catch (EMPException ee) {
			context.addDataField("flag","error");
			context.addDataField("msg","修改出错！");
			throw ee;
		} catch(Exception e){
			context.addDataField("flag","error");
			context.addDataField("msg","修改出错！");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
