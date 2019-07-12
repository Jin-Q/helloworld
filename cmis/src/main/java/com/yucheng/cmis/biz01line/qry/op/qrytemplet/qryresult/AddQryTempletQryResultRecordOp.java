package com.yucheng.cmis.biz01line.qry.op.qrytemplet.qryresult;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddQryTempletQryResultRecordOp extends CMISOperation {
	 
	private final String modelId = "QryResult";

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
			
			/**
			 * 生成序列号
			 */
//			CMISSequenceService sequenceService = (CMISSequenceService)context.getService("sequenceService");
			String resultNo="";
//			tempNo = sequenceService.getSequence("QRY_NO", "ALL", context,connection);
			resultNo = CMISSequenceService4JXXD.querySequenceFromDB("QRY_NO", "all", connection, context);
			
			kColl.setDataValue("result_no", resultNo);
						
			TableModelDAO dao = this.getTableModelDAO(context);
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
