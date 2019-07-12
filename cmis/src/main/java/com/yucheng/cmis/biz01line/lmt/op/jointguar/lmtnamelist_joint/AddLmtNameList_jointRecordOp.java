package com.yucheng.cmis.biz01line.lmt.op.jointguar.lmtnamelist_joint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class AddLmtNameList_jointRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAppNameList";
	
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
			if(kColl==null||kColl.size()==0){
				throw new Exception("The values to insert["+modelId+"] cannot be empty!");
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);

			kColl.setDataValue("sub_type", "03");
			dao.insert(kColl, connection);
			context.addDataField("flag", "suc");
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
