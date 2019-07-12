package com.yucheng.cmis.biz01line.psp.op.pspcheckcatalog;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddPspCheckCatalogRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "PspCheckCatalog";
	
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
			
			UNIDGenerator unidGenerator = new UNIDGenerator();
			kColl.setDataValue("catalog_id", unidGenerator.getUNID());
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			context.addDataField("flag", "success");
			context.addDataField("catlogId", kColl.getDataValue("catalog_id"));
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
			context.addDataField("catlogId", "");
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
