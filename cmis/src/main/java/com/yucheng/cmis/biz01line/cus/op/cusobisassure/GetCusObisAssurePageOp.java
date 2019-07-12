package com.yucheng.cmis.biz01line.cus.op.cusobisassure;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;

import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class GetCusObisAssurePageOp extends CMISOperation {
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		KeyedCollection kColl = new KeyedCollection("CusObisAssure");
		String seqNo = ""; 
		try{
			connection = this.getConnection(context);
      
            seqNo = CMISSequenceService4JXXD.querySequenceFromDB("CUS", "all", 
	        		connection, context);	
		}catch (EMPException ee) {
		
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		kColl.addDataField("seq", seqNo);
		this.putDataElement2Context(kColl, context);
		return "0";
	}
}
