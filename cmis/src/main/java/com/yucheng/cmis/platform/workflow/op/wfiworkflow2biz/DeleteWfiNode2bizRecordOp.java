package com.yucheng.cmis.platform.workflow.op.wfiworkflow2biz;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteWfiNode2bizRecordOp extends CMISOperation {

	private final String modelId = "WfiNode2biz";
	

	private final String pk1_name = "pk1";
	private final String nodeid_name = "nodeid";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



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
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.put("flag", count);
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
