package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankbatchlist.lmtbatchcorre;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteLmtIntbankBatchListLmtBatchCorreRecordOp extends CMISOperation {
	
	private final String modelId = "LmtBatchCorre";
	
	private final String batch_cus_no_name = "batch_cus_no";
	private final String cus_id_name ="cus_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			
			String batch_cus_no_value = null;
			String cus_id_value = null;
			try {
				batch_cus_no_value = (String)context.getDataValue(batch_cus_no_name);
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(batch_cus_no_value == null || batch_cus_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+batch_cus_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkmap  = new HashMap<String,String>();
			pkmap.put("cus_id",cus_id_value);
			pkmap.put("batch_cus_no", batch_cus_no_value);
			
			int count=dao.deleteAllByPks(modelId, pkmap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Record Count: " + count);
			}
			context.addDataField("flag", "success");
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
