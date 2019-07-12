package com.yucheng.cmis.biz01line.iqp.op.iqpratechangeapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpRateChangeAppRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpRateChangeApp";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			kColl.put("serno", serno);
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
			context.addDataField("serno", serno);
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
			context.addDataField("msg", "新增失败,原因:"+ee.getMessage());
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
