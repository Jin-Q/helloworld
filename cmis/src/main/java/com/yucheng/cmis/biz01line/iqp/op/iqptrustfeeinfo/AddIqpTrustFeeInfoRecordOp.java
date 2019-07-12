package com.yucheng.cmis.biz01line.iqp.op.iqptrustfeeinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2014-12-29
*@description 需求编号：【XD141204082】关于信托贷款业务需求调整
*@version v1.0
*
 */
public class AddIqpTrustFeeInfoRecordOp extends CMISOperation {
	
	private final String modelId = "IqpTrustFeeInfo";
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

			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			context.addDataField("msg", "success");
			context.addDataField("sernoStr", kColl.getDataValue("serno").toString());
		}catch (EMPException ee) {
			context.addDataField("msg", "failed");
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
