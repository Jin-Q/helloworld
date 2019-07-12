package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpappoverseeorg;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryOverseeOrgTableOp extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		String reportId = (String)context.getDataValue("reportId");
		String report[] = reportId.split("\\$");
		context.setDataValue("reportId", report[0]);
		return reportId;
	}
}
