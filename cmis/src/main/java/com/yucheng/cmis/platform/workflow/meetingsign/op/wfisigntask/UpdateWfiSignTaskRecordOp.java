package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisigntask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateWfiSignTaskRecordOp extends CMISOperation {

	private final String modelId = "WfiSignTask";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		boolean success = false;
		try {
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update[" + modelId
						+ "] cannot be empty!");

			TableModelDAO dao = this.getTableModelDAO(context);
			int count = dao.update(kColl, connection);
			if (count == 1) {
				success = true;
			}
		} catch (EMPException ee) {
			ee.printStackTrace();
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} finally {
			context.addDataField("success", success);
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
