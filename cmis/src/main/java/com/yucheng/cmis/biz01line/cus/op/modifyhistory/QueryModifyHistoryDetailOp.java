package com.yucheng.cmis.biz01line.cus.op.modifyhistory;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cuscom.component.ModifyHistoryComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryModifyHistoryDetailOp extends CMISOperation {

	private final String modelId = "ModifyHistory";

	private final String keyIdName = "key_id";

	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);

			if (this.updateCheck) {

				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context,
						connection);
			}

			String keyIdValue = null;
			try {
				keyIdValue = (String) context.getDataValue(keyIdName);
			} catch (Exception e) {
			}
			if (keyIdValue == null || keyIdValue.length() == 0)
				throw new EMPJDBCException("The value of pk[" + keyIdName
						+ "] cannot be null!");

			int size = 15;
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			ModifyHistoryComponent component = (ModifyHistoryComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							PUBConstant.MODIFY_HISTORY_COMPONENT,context, connection);
			IndexedCollection iColl = component.getDetailKColl(keyIdValue);
			if (iColl != null) {
				this.putDataElement2Context(iColl, context);
			}
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
