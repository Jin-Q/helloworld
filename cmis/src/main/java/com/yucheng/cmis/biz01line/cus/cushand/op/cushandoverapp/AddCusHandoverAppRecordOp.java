package com.yucheng.cmis.biz01line.cus.cushand.op.cushandoverapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
/**
 * 客户移交新增保存操作op
 * @Version bsbcmis
 * @author wuming 2012-3-19 
 * Description:
 */
public class AddCusHandoverAppRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusHandoverApp";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = ""; 
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			kColl.setDataValue("serno", serno);
			dao.insert(kColl, connection);
			context.addDataField("flag", "0");
			
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
