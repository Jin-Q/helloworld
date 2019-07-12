package com.yucheng.cmis.biz01line.core.sholidayregister;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class SPubNullOp extends CMISOperation {

	/*** 用于补充新增操作op缺失 ***/
	public String doExecute(Context context) throws EMPException {
		return "0";
	}

}
