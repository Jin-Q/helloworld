package com.yucheng.cmis.biz01line.iqp.op.iqpguarantinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateIqpGuarantInfoRecordOp extends CMISOperation {
	

	private final String modelId = "IqpGuarantInfo";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
            String serno = (String)kColl.getDataValue("serno");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kc = dao.queryDetail(modelId, serno, connection);
			String kSerno = (String)kc.getDataValue("serno");
			if(kSerno != null && kSerno != ""){
				dao.update(kColl, connection);
			} else {
				dao.insert(kColl, connection);
			}
			context.addDataField("flag", "success");

		}catch (EMPException ee) {
			context.addDataField("flag", "failed");   
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
