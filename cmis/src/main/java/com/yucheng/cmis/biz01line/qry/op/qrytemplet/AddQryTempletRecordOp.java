package com.yucheng.cmis.biz01line.qry.op.qrytemplet;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddQryTempletRecordOp extends CMISOperation {
	
	private final String modelId = "QryTemplet";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			/** 生成序列号  */
			String tempNo="";
			tempNo = CMISSequenceService4JXXD.querySequenceFromDB("QRY_NO", "all", connection, context);
			
			kColl.setDataValue("temp_no", tempNo);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
			context.addDataField("flag", "success");
			context.addDataField("tempNo", tempNo);
			context.addDataField("message", "success");
		}catch(Exception e){
			context.addDataField("flag", "error");
			context.addDataField("temp_no", "");
			context.addDataField("message", "失败原因："+e.getMessage());			
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
