package com.yucheng.cmis.biz01line.iqp.op.iqpassetprdeval;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIqpAssetPrdEvalDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpAssetPrdEval";
	

	private final String prd_id_name = "prd_id";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String prd_id_value = null;
			try {
				prd_id_value = (String)context.getDataValue(prd_id_name);
			} catch (Exception e) {}
			if(prd_id_value == null || prd_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+prd_id_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, prd_id_value, connection);
			
			if(kColl.getDataValue(prd_id_name)==null){
				kColl.setDataValue(prd_id_name, prd_id_value);
			}
			
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
