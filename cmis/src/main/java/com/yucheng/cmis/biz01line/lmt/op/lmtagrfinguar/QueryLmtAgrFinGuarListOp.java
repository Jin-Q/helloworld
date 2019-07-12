package com.yucheng.cmis.biz01line.lmt.op.lmtagrfinguar;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAgrFinGuarListOp extends CMISOperation {

	private final String modelId = "LmtAgrFinGuar";

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection) context .getDataElement(this.modelId);
			} catch (Exception e) {
			}

			String conditionStr = TableModelUtil.getQueryCondition( this.modelId, queryData, context, false, false, false)+ "order by agr_no desc";

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 10;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));

			TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);

			IndexedCollection iColl = dao.queryList(modelId, null, conditionStr, pageInfo, connection);
			iColl.setName(iColl.getName() + "List");
			this.putDataElement2Context(iColl, context);

			SInfoUtils.addUSerName(iColl, new String[] { "input_id" });
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id" });

			String[] args = new String[] { "cus_id" };
			String[] modelIds = new String[] { "CusBase" };
			String[] modelForeign = new String[] { "cus_id" };
			String[] fieldName = new String[] { "cus_name" };
			// 详细信息翻译时调用
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);

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
