package com.yucheng.cmis.biz01line.iqp.op.togetherrqstr;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteTogetherRqstrRecordOp extends CMISOperation {

	private final String modelId = "IqpTogetherRqstr";
	

	private final String serno_name = "serno";
	private final String cusid = "cus_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String serno_value = null;
			String cus_id = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				cus_id = (String)context.getDataValue(cusid);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			String condition ="where serno='"+serno_value+"' and cus_id='"+cus_id+"'";	


			TableModelDAO dao = this.getTableModelDAO(context);
			Map map = new HashMap();
			map.put("serno", serno_value);
			map.put("cus_id", cus_id);
			int count = dao.deleteByPks(modelId, map, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
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
