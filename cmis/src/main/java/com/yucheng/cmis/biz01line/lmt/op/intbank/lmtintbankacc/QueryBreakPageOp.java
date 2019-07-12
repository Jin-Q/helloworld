package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankacc;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryBreakPageOp extends CMISOperation {
	private final String modelId = "LmtIntbankAcc";

	@Override
	public String doExecute(Context context) throws EMPException {
		KeyedCollection kColl = null;
		try{
			kColl = new KeyedCollection(modelId);
			kColl.addDataField("break_date", "");
			this.putDataElement2Context(kColl, context);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
