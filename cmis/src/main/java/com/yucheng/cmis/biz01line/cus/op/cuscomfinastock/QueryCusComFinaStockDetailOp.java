package com.yucheng.cmis.biz01line.cus.op.cuscomfinastock;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryCusComFinaStockDetailOp  extends CMISOperation {

	private final String modelId = "CusComFinaStock";
	
	private final String cus_id_name = "cus_id";
	private final String com_stk_code_name = "com_stk_code";
	
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");

			String com_stk_code_value = null;
			try {
				com_stk_code_value = (String)context.getDataValue(com_stk_code_name);
			} catch (Exception e) {}
			if(com_stk_code_value == null || com_stk_code_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+com_stk_code_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cus_id",cus_id_value);
			pkMap.put("com_stk_code",com_stk_code_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			IndexedCollection  icoll = new IndexedCollection();
			icoll.add(pkMap);
			
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
