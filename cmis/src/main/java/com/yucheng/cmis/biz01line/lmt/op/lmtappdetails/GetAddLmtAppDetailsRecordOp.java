package com.yucheng.cmis.biz01line.lmt.op.lmtappdetails;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;

public class GetAddLmtAppDetailsRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAppDetails";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = new KeyedCollection(modelId);
			
			context.addDataField("action", "addLmtAppDetailsRecord.do");
			this.putDataElement2Context(kColl, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
