package com.yucheng.cmis.platform.organization.steamorg.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class IntroSTeamOrgOp  extends CMISOperation {
	
	private final String modelId = "STeamOrg";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String team_no = (String) context.getDataValue("team_no");
		String team_org = (String) context.getDataValue("team_org");
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			//定义字符串数组用来接收批量
				String Org[] = team_org.split(",");
				KeyedCollection kColl = new KeyedCollection(modelId);
				kColl.addDataField("team_no", team_no);
				kColl.addDataField("team_org_id", "");
				
				for(int i=0;i<Org.length;i++){
					IndexedCollection iColl= dao.queryList(modelId," where team_no='"+team_no+"' and team_org_id='"+Org[i]+"'", connection);
					if(iColl!=null&&iColl.size()>0){
						continue;
					}
					 kColl.setDataValue("team_org_id", Org[i]);
					 dao.insert(kColl, connection);
				}
				context.addDataField("flag", "success");
				context.addDataField("msg", "已成功录入！");
			
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "录入成员失败！失败原因："+ee.getMessage());
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
