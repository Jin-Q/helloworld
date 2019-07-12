package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpmemmana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpMemManaRecordOp extends CMISOperation {

	private final String modelId = "IqpMemMana";
	

	private final String pk1_name = "pk1";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String pk1_value = null;
			try {
				pk1_value = (String)context.getDataValue(pk1_name);//获取成员客户号
			} catch (Exception e) {}
			if(pk1_value == null || pk1_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk1_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
            KeyedCollection kColl=dao.queryAllDetail(modelId, pk1_value, connection);
            kColl.setDataValue("status", "3");
            dao.update(kColl, connection);//更改成员客户的状态为待退网
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
