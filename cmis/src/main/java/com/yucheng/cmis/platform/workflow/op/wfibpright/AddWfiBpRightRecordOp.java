package com.yucheng.cmis.platform.workflow.op.wfibpright;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class AddWfiBpRightRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "WfiBpRight";
	
	/**
	 * bussiness logic operation
	 */
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
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
//			TableModelDAO dao = this.getTableModelDAO(context);
//			String condition = "where approve_org='9350583002'";
//			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
//			for(int i=0;i<iColl.size();i++){
//				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
//				kColl.put("pk1", "");
//				kColl.put("approve_org", "9350583003");//洪濑
//				dao.insert(kColl, connection);
//			}
			context.put("flag", PUBConstant.SUCCESS);
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
