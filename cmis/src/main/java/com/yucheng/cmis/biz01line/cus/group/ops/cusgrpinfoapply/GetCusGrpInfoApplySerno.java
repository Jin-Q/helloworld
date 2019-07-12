package com.yucheng.cmis.biz01line.cus.group.ops.cusgrpinfoapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class GetCusGrpInfoApplySerno extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			String serno = "";
			KeyedCollection kColl = new KeyedCollection("CusGrpInfoApply");
			connection = this.getConnection(context);
			serno = CMISSequenceService4JXXD.querySequenceFromDB("CUSGRP","fromDate", connection, context);
			kColl.addDataField("serno", serno);
			this.putDataElement2Context(kColl, context);
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
