package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpapppsalecont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpAppPsaleContRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpAppPsaleCont";
	
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
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String psale_cont = CMISSequenceService4JXXD.querySequenceFromDB("PC", "all", connection, context);
			kColl.put("psale_cont", psale_cont);
			kColl.put("status", "1");
			kColl.put("cont_status", "1");
			dao.insert(kColl, connection);
			context.addDataField("flag", "success");
			context.addDataField("psale_cont", psale_cont);
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
