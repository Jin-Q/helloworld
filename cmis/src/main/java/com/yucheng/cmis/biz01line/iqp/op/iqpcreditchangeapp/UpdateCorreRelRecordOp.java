package com.yucheng.cmis.biz01line.iqp.op.iqpcreditchangeapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateCorreRelRecordOp extends CMISOperation {
	

	private final String modelId = "GrtLoanRGur";       
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null; 
		try{
			connection = this.getConnection(context);
			String pk_id = null;
			String corre_rel = null;
			try { 
				pk_id = (String)context.getDataValue("pk_id");
				corre_rel = (String)context.getDataValue("corre_rel");
			} catch (Exception e) {}
			if(pk_id == null || "".equals(pk_id) || corre_rel == null || "".equals(corre_rel))
				throw new EMPJDBCException("The values pk_id or corre_rel cannot be empty!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, pk_id, connection);
			kColl.setDataValue("corre_rel", corre_rel);
			int count=dao.update(kColl, connection);   
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
            context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "error");    
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
