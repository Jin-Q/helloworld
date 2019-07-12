package com.yucheng.cmis.biz01line.iqp.op.iqpaccaccp;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;	

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpAccAccpListOp extends CMISOperation {
	

	private final String modelId = "IqpAccAccp";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}

		String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
								+"order by serno desc";
			
			int size = 10;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("opac_org");
			list.add("is_elec_bill");
			list.add("bill_qty");
			list.add("actp_org_name");
			list.add("loan_use");
			list.add("serno");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
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
