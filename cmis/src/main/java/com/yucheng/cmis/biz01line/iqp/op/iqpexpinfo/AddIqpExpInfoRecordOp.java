package com.yucheng.cmis.biz01line.iqp.op.iqpexpinfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddIqpExpInfoRecordOp extends CMISOperation {

	private final String modelId = "IqpExpInfo";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert[" + modelId
						+ "] cannot be empty!");

			TableModelDAO dao = this.getTableModelDAO(context);
			String po_no = kColl.getDataValue("po_no").toString();
			String invc_no = kColl.getDataValue("invc_no").toString();
			String express_no = kColl.getDataValue("express_no").toString();
			Map pkMap = new HashMap();
			//pkMap.put("po_no", po_no);
			pkMap.put("invc_no", invc_no);
			pkMap.put("express_no", express_no);
			KeyedCollection kCollQuery = dao.queryDetail(modelId, pkMap,
					connection);
			if (kCollQuery != null
					&&kCollQuery.getDataValue("invc_no") != null) {
				context.addDataField("flag", "exist");
				return "0";
			}
			kColl.setDataValue("input_id", context
					.getDataValue("currentUserId"));
			kColl.setDataValue("input_br_id", context.getDataValue("organNo"));
			kColl.setDataValue("input_date", context.getDataValue("OPENDAY"));
			dao.insert(kColl, connection);
			context.addDataField("flag", "success");
		} catch (EMPException ee) {
			context.addDataField("flag", "fail");
			throw ee;
		} catch (Exception e) {
			context.addDataField("flag", "fail");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
