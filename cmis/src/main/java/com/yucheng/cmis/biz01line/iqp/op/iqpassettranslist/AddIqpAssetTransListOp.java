package com.yucheng.cmis.biz01line.iqp.op.iqpassettranslist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddIqpAssetTransListOp  extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno="";
		try{
			connection = this.getConnection(context);
		    serno =(String)context.getDataValue("serno");
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
