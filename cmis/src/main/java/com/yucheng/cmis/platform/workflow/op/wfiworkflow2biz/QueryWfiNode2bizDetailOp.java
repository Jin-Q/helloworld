package com.yucheng.cmis.platform.workflow.op.wfiworkflow2biz;

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

public class QueryWfiNode2bizDetailOp  extends CMISOperation {
	
	private final String modelId = "WfiNode2biz";
	

	private final String pk1_name = "pk1";
	private final String nodeid_name = "nodeid";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String pk1_value = null;
			try {
				pk1_value = (String)context.getDataValue(pk1_name);
			} catch (Exception e) {}
			if(pk1_value == null || pk1_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk1_name+"] cannot be null!");

			String nodeid_value = null;
			try {
				nodeid_value = (String)context.getDataValue(nodeid_name);
			} catch (Exception e) {}
			if(nodeid_value == null || nodeid_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+nodeid_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("pk1",pk1_value);
			pkMap.put("nodeid",nodeid_value);
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
