package com.yucheng.cmis.biz01line.iqp.op.togetherrqstr;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckTogetherRqstrRecordOp extends CMISOperation {
	
	private final String modelId = "IqpTogetherRqstr";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			context.addDataField("flag", "success");
			String serno="";
			String cus_id="";
			String flag="success";
			try {
				cus_id = (String)context.getDataValue("cus_id");
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(cus_id == null || cus_id == "")
				throw new EMPJDBCException("The values to cus_id cannot be empty!");
			String conditionStr = "where serno='"+serno+"'and cus_id='"+cus_id+"'";
			
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String cusIdHave = (String)kColl.getDataValue("cus_id");
				if(cusIdHave.equals(cus_id)){
				   flag="error";
				}
			}
			context.setDataValue("flag", flag); 
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
