package com.yucheng.cmis.biz01line.psp.op.pspschcatrel;

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

public class QueryPspSchCatRelDetailOp  extends CMISOperation {
	
	private final String modelId = "PspSchCatRel";
	

	private final String scheme_id_name = "scheme_id";
	private final String catalog_id_name = "catalog_id";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String scheme_id_value = null;
			try {
				scheme_id_value = (String)context.getDataValue(scheme_id_name);
			} catch (Exception e) {}
			if(scheme_id_value == null || scheme_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+scheme_id_name+"] cannot be null!");

			String catalog_id_value = null;
			try {
				catalog_id_value = (String)context.getDataValue(catalog_id_name);
			} catch (Exception e) {}
			if(catalog_id_value == null || catalog_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+catalog_id_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("scheme_id",scheme_id_value);
			pkMap.put("catalog_id",catalog_id_value);
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
