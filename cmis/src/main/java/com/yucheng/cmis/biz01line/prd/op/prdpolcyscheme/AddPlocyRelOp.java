package com.yucheng.cmis.biz01line.prd.op.prdpolcyscheme;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddPlocyRelOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection conn = this.getConnection(context);
		try {
			String flowValue = (String)context.getDataValue("flowValue");
			String flowNodeValue = "";
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				this.releaseConnection(context, conn);
		}
		return null;
	}

}
