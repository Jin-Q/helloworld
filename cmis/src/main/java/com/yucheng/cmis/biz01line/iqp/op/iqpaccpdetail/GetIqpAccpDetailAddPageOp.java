package com.yucheng.cmis.biz01line.iqp.op.iqpaccpdetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class GetIqpAccpDetailAddPageOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String serno = "";
			String op = "";
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
