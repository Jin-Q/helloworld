package com.yucheng.cmis.biz01line.cus.op.modifyhistory;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryModifyHistoryListOp extends CMISOperation {

	private final String modelId = "ModifyHistory";

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			String cusId = "";
			try {
				cusId = (String) context.getDataValue("cus_id");
				queryData = (KeyedCollection) context
						.getDataElement(this.modelId);
			} catch (Exception e) {
			}

			String conditionStr = TableModelUtil.getQueryCondition(
					this.modelId, queryData, context, false, false, false);
			if (!"".equals(cusId)) {
				conditionStr += " where cus_id='" + cusId
						+ "' order by modify_time desc";
			}

			int size = 10;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one",
					String.valueOf(size));

			TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,
					conditionStr, pageInfo, connection);
			iColl.setName(iColl.getName() + "List");
			SInfoUtils.addSOrgName(iColl, new String[]{"modify_user_br_id"});
			SInfoUtils.addUSerName(iColl, new String[]{"modify_user_id"});
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
