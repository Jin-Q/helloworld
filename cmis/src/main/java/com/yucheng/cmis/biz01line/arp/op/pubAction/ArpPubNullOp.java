package com.yucheng.cmis.biz01line.arp.op.pubAction;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class ArpPubNullOp extends CMISOperation {

	/*** 用于补充新增操作op缺失 ***/
	public String doExecute(Context context) throws EMPException {
		return "0";
	}

}
