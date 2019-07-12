package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtsiglmt;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class AddLmtSigLmtRecordOp extends CMISOperation {
	

	private final String modelId = "LmtSigLmt";
	

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
			
			String cus_id_value = (String)kColl.getDataValue("cus_id");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//若该客户在授信中不存在'997','998',那么就可以再进行授信了。
			IndexedCollection iCollLmt = dao.queryList("LmtSigLmt", "where cus_id='"+cus_id_value+"' and approve_status not in ('997','998')", connection);
			if(iCollLmt.size()>0){
				context.addDataField("flag", PUBConstant.FAIL);
			}else{
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
