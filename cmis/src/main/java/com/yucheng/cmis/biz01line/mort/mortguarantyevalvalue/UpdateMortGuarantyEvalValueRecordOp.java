package com.yucheng.cmis.biz01line.mort.mortguarantyevalvalue;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateMortGuarantyEvalValueRecordOp extends CMISOperation {
	

	private final String modelId = "MortGuarantyEvalValue";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection mortGuarantyBaseInfo = null;
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				mortGuarantyBaseInfo =(KeyedCollection) context.getDataElement("MortGuarantyBaseInfo");
				mortGuarantyBaseInfo.addDataField("guaranty_no", (String)kColl.getDataValue("guaranty_no"));
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			count=dao.update(mortGuarantyBaseInfo, connection);
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
