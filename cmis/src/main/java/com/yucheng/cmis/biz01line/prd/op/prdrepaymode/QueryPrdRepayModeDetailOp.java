package com.yucheng.cmis.biz01line.prd.op.prdrepaymode;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryPrdRepayModeDetailOp  extends CMISOperation {
	
	private final String modelId = "PrdRepayMode";
	

	private final String repay_mode_id_name = "repay_mode_id";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String repay_mode_id_value = null;
			try {
				repay_mode_id_value = (String)context.getDataValue(repay_mode_id_name);
			} catch (Exception e) {}
			if(repay_mode_id_value == null || repay_mode_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+repay_mode_id_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, repay_mode_id_value, connection);
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
