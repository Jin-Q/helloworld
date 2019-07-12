package com.yucheng.cmis.biz01line.fnc.op.fncloan;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddFncLoanRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "FncLoan";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		// 更新kcoll
		String currentUserId = (String)context.getDataValue(
				"currentUserId");
		String currentSorg = (String) context.getDataValue(
				"organNo");
		String currentDate = (String) context.getDataValue(PUBConstant.OPENDAY);
		
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//add a record
			kColl.put("input_id",currentUserId);
			kColl.put("input_br_id",currentSorg);
			kColl.put("input_date",currentDate);
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"input_id","last_upd_id"});
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
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
