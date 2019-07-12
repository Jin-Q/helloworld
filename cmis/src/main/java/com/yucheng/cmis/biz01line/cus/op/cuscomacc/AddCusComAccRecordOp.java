package com.yucheng.cmis.biz01line.cus.op.cuscomacc;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddCusComAccRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusComAcc";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			context.addDataField("cus_id", "");
			context.addDataField("flag", "");
			connection = this.getConnection(context);
			String cus_id = "";
			String acc_no = "";
			KeyedCollection kColl = null;
			KeyedCollection kCollExists = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				cus_id = (String) kColl.getDataValue("cus_id");
				acc_no = (String) kColl.getDataValue("acc_no");
				
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			if(cus_id == null || acc_no == null || acc_no.trim().length()<1 || cus_id.trim().length()<1)
				return "0";
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			kCollExists = dao.queryFirst(modelId, null, " where cus_id = '"+cus_id+"' and acc_no = '"+acc_no+"'", connection);
			cus_id = (String) kCollExists.getDataValue("cus_id");
			
			if(cus_id == null || cus_id.trim().length()<0)
			{
				dao.insert(kColl, connection);
				context.setDataValue("cus_id", kColl.getDataValue("cus_id"));
				context.setDataValue("flag", "保存成功");
			}else{
				context.setDataValue("flag", "已存在的账户账号");
			}
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
