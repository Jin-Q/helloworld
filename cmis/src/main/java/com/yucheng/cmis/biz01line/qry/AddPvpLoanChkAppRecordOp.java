package com.yucheng.cmis.biz01line.qry;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService;

public class AddPvpLoanChkAppRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "PvpLoanChkApp";
	
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
			CMISSequenceService sequenceService = (CMISSequenceService)  context.getService("sequenceService");
			String no = sequenceService.getSequence("CONT","ALL", context, connection);
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			kColl.put("serno", no);
			kColl.put("prd_pk", "0");
			dao.insert(kColl, connection);
			
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
