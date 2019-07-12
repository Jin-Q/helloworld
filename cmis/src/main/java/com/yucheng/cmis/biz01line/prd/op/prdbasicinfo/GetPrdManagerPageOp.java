package com.yucheng.cmis.biz01line.prd.op.prdbasicinfo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class GetPrdManagerPageOp extends CMISOperation {
	private final String modelId ="SUser";

	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection queryData =new KeyedCollection();
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
            RecordRestrict recordRestrict = this.getRecordRestrict(context);
            conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
			int size = 5;		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			List list = new ArrayList();
			list.add("actorname");
			list.add("actorno");
			list.add("orgid");
			list.add("state");
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
		
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
