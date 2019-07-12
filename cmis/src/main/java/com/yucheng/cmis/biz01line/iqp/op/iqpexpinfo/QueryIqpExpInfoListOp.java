package com.yucheng.cmis.biz01line.iqp.op.iqpexpinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpExpInfoListOp extends CMISOperation {

	private final String modelId = "IqpExpInfo";

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String poNo = context.getDataValue("po_no").toString();
			String conditionStr = " where po_no='" + poNo + "'";
			TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,
					conditionStr, connection);
			iColl.setName(iColl.getName() + "List");
			this.putDataElement2Context(iColl, context);

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
