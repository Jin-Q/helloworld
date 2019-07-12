package com.yucheng.cmis.biz01line.iqp.op.iqpbatchbillrel;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIqpBatchBillRelDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpBatchBillRel";
	

	private final String batch_no_name = "batch_no";
	private final String porder_no_name = "porder_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String batch_no_value = null;
			try {
				batch_no_value = (String)context.getDataValue(batch_no_name);
			} catch (Exception e) {}
			if(batch_no_value == null || batch_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+batch_no_name+"] cannot be null!");

			String porder_no_value = null;
			try {
				porder_no_value = (String)context.getDataValue(porder_no_name);
			} catch (Exception e) {}
			if(porder_no_value == null || porder_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+porder_no_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("batch_no",batch_no_value);
			pkMap.put("porder_no",porder_no_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
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
