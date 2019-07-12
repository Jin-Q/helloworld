package com.yucheng.cmis.biz01line.cus.op.cusblkcheckinapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateCusBlkCheckinappRecordOp extends CMISOperation {

	private final String modelId = "CusBlkCheckinapp";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//String black_level = (String)kColl.getDataValue("black_level");
			String cus_id = (String)kColl.getDataValue("cus_id");
			String conditionStr ="where cus_id='"+cus_id+"' and status='002'";
			KeyedCollection kc = dao.queryFirst("CusBlkList", null, conditionStr, connection);
			//String black_levelTemp = (String)kc.getDataValue("black_level");
			
			if(kc!=null&&kc.getDataValue("cus_id")!=null&&!"".equals(kc.getDataValue("cus_id"))){
				context.addDataField("flag", PUBConstant.FAIL);
			}else{
				int count=dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("Update Failed! Record Count: " + count);
				}
				context.addDataField("flag", PUBConstant.SUCCESS);
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
