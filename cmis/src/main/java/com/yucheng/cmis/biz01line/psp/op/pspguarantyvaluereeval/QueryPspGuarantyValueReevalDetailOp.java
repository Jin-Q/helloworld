package com.yucheng.cmis.biz01line.psp.op.pspguarantyvaluereeval;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class QueryPspGuarantyValueReevalDetailOp  extends CMISOperation {
	
	private final String modelId = "PspGuarantyValueReeval";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		KeyedCollection kColl = null;
		try{
			connection = this.getConnection(context);
			String guaranty_no = (String) context.getDataValue("guaranty_no");
			String task_id = (String) context.getDataValue("task_id");
			
			Map paramMap = new HashMap();
			paramMap.put("task_id", task_id);
			paramMap.put("guaranty_no", guaranty_no);
			
			kColl = (KeyedCollection) SqlClient.queryFirst("queryPspGuarantyReeval", paramMap, null, connection);
			if(kColl==null){
				kColl = new KeyedCollection(modelId);
			}
			kColl.setName(modelId);
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
