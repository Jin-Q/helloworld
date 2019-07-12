package com.yucheng.cmis.biz01line.cus.cushand.op.cushandovercfg.cushandoverdetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryCusHandoverCfgCusHandoverDetailDetailOp  extends CMISOperation {

	private final String modelId = "CusHandoverDetail";


	private final String sub_serno_name = "sub_serno";
	

	private boolean updateCheck = false;


	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String sub_serno_value = null;
			try {
				sub_serno_value = (String)context.getDataValue(sub_serno_name);
			} catch (Exception e) {}
			if(sub_serno_value == null || sub_serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+sub_serno_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, sub_serno_value, connection);
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
