package com.yucheng.cmis.biz01line.psp.op.pspcatitemrel;

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

public class QueryPspCatItemRelDetailOp  extends CMISOperation {
	
	private final String modelId = "PspCatItemRel";
	

	private final String catalog_id_name = "catalog_id";
	private final String item_id_name = "item_id";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String catalog_id_value = null;
			try {
				catalog_id_value = (String)context.getDataValue(catalog_id_name);
			} catch (Exception e) {}
			if(catalog_id_value == null || catalog_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+catalog_id_name+"] cannot be null!");

			String item_id_value = null;
			try {
				item_id_value = (String)context.getDataValue(item_id_name);
			} catch (Exception e) {}
			if(item_id_value == null || item_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+item_id_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("catalog_id",catalog_id_value);
			pkMap.put("item_id",item_id_value);
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
