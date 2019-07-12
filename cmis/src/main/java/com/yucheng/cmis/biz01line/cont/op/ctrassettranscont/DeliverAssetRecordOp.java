package com.yucheng.cmis.biz01line.cont.op.ctrassettranscont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class DeliverAssetRecordOp extends CMISOperation {
	

	private final String modelId = "CtrAssetTransCont";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			
//			KeyedCollection kColl = null;
//			try {
//				kColl = (KeyedCollection)context.getDataElement(modelId);
//			} catch (Exception e) {}
//			if(kColl == null || kColl.size() == 0)
//				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			String cont_no = (String) context.getDataValue("cont_no");
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, cont_no, connection);
			
			//更新交割日期
			kColl.put("deliver_date", context.getDataValue("OPENDAY").toString());
			
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
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
