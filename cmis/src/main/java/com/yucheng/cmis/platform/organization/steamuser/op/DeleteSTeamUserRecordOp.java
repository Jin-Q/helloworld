package com.yucheng.cmis.platform.organization.steamuser.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteSTeamUserRecordOp extends CMISOperation {

	private final String modelId = "STeamUser";
	


	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String mem_no = "";
		String memNo;
		String team_no="";
		int count = 0;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			mem_no = (String) context.getDataValue("mem_no");
			team_no = (String) context.getDataValue("team_no");
			Map pkMap = new HashMap();
			//批量删除
			String Mem[] = mem_no.split(",");
			for(int i=0;i<Mem.length;i++){
				memNo = Mem[i];
				pkMap.put("team_no",team_no);
				pkMap.put("mem_no",memNo);
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
