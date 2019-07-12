package com.yucheng.cmis.platform.organization.steammem.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class IntroTeamMemOp  extends CMISOperation {
	
	private final String modelId = "STeamMem";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String team_no = (String) context.getDataValue("team_no");
		String mem_no = (String) context.getDataValue("mem_no");
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			//定义字符串数组用来接收批量
				String Mem[] = mem_no.split(",");
				KeyedCollection kColl = new KeyedCollection(modelId);
				kColl.addDataField("team_no", team_no);
				kColl.addDataField("team_role", "01");
				kColl.addDataField("mem_no", "");
				for(int i=0;i<Mem.length;i++){
					 kColl.setDataValue("mem_no", Mem[i]);
					 dao.insert(kColl, connection);
				}
				context.addDataField("flag", "success");
				context.addDataField("msg", "已成功录入！");
			
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
