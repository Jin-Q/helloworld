package com.yucheng.cmis.biz01line.iqp.op.iqpbatchmng;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class GetIqpBatchMngAddPageOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String batchNo = "";
			batchNo = CMISSequenceService4JXXD.querySequenceFromDB("PJC", "all", connection, context);
			context.addDataField("batchno", batchNo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
