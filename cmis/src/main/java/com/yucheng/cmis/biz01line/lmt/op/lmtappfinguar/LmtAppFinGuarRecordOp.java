package com.yucheng.cmis.biz01line.lmt.op.lmtappfinguar;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class LmtAppFinGuarRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAppFinGuar";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = new KeyedCollection();
			kColl.setName(modelId);
			kColl.addDataField("input_id",context.getDataValue("currentUserId"));
			kColl.addDataField("input_br_id",context.getDataValue("organNo"));
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "input_id"});
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
