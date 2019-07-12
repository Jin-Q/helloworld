package com.yucheng.cmis.platform.organization.steamorg.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QuerySTeamOrgListOp extends CMISOperation {


	private final String modelId = "STeamOrg";
	

	public String doExecute(Context context) throws EMPException {
		String team_no = "";
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			team_no = (String) context.getDataValue("team_no");
			//拼接where 条件对成员信息进行过滤
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			List list = new ArrayList();
			IndexedCollection iColl = dao.queryList(modelId, list,"where team_no ='"+team_no+"'",pageInfo,connection);
			iColl.setName("STeamOrgList");
			//成员名称翻译
			String[] args=new String[] { "team_org_id" };
			String[] modelIds=new String[]{"SOrg"};
			String[] modelForeign=new String[]{"organno "};
			String[] fieldName=new String[]{"organname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
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
