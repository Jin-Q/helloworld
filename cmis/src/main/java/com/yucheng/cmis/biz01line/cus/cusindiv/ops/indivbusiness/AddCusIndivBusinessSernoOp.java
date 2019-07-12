package com.yucheng.cmis.biz01line.cus.cusindiv.ops.indivbusiness;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddCusIndivBusinessSernoOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusIndivBusiness";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = new KeyedCollection(modelId);
			
			String serNo=CMISSequenceService4JXXD.querySequenceFromDB("CINDBIZ", "fromDate", 
					connection, context);
			kColl.addDataField("cus_id", (String)context.getDataValue("cus_id"));
			kColl.addDataField("serno", serNo);
			
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
