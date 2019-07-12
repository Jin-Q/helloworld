package com.yucheng.cmis.biz01line.iqp.op.iqpbksyndic;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIqpBksyndicDetailOp extends CMISOperation {

	private final String modelId = "IqpBksyndic";

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String condition="";

			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, serno_value, connection);
			this.putDataElement2Context(kColl, context);

			condition=" where serno='"+serno_value+"'";
			IndexedCollection iColl_IqpBksyndicInfo = dao.queryList("IqpBksyndicInfo",condition, connection);
			this.putDataElement2Context(iColl_IqpBksyndicInfo, context);
			
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
