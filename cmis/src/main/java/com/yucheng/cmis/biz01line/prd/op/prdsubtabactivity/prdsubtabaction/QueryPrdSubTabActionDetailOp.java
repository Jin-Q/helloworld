package com.yucheng.cmis.biz01line.prd.op.prdsubtabactivity.prdsubtabaction;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryPrdSubTabActionDetailOp  extends CMISOperation {
	
	private final String modelId = "PrdSubTabAction";
	private final String pkid_name = "pkid";
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String pkid_value = null;
			try {
				pkid_value = (String)context.getDataValue(pkid_name);
			} catch (Exception e) {}
			if(pkid_value == null || pkid_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pkid_name+"] cannot be null!");

		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, pkid_value, connection);
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
