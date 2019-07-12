package com.yucheng.cmis.biz01line.qry.op.qrytemplet;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateQryTempletRecordOp extends CMISOperation {
	 
	private final String modelId = "QryTemplet";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			//String condition="";
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0){
				context.addDataField("flag","fild");
				context.addDataField("msg","要保存的模型数据为空。");
				return "0";
			}
			 
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Record Count: " + count);
			}

			context.addDataField("flag", "success");
			context.addDataField("message", "success");
		}catch(Exception e){
			context.addDataField("flag", "error");
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
