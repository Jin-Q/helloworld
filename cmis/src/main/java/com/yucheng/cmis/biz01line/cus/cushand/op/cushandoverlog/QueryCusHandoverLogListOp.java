package com.yucheng.cmis.biz01line.cus.cushand.op.cushandoverlog;

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
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusHandoverLogListOp extends CMISOperation {

	private final String modelId = "CusHandoverLog";

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection) context.getDataElement(this.modelId);
			} catch (Exception e) {}

//			String cusId = (String) context.getDataValue("currentUserId");
//			StringBuffer sb = new StringBuffer();
//			sb.append(" ( handover_id='").append(cusId).append(
//					"' or supervise_id='").append(cusId).append(
//					"' or receiver_id='").append(cusId).append("' ) ");

			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
//			if (conditionStr.length()==0) {
//				conditionStr = " where ";
//			} else {
//				conditionStr += " and ";
//			}
			conditionStr += " order by serno desc";
			int size = 10;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));

			TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);

			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("handover_date");
			list.add("handover_scope");
			list.add("handover_mode");
			list.add("handover_br_id");
			list.add("handover_id");
			list.add("receiver_br_id");
			list.add("receiver_id");
//			list.add("supervise_br_id");
//			list.add("supervise_id");
			list.add("pk_id");
			System.out.println(conditionStr+ "lilxtest");
			IndexedCollection iColl = dao.queryList(modelId, list, conditionStr, pageInfo, connection);
			iColl.setName(iColl.getName() + "List");

			SInfoUtils.addSOrgName(iColl, new String[] { "handover_br_id", "receiver_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "handover_id", "receiver_id" });

			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
