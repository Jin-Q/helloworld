package com.yucheng.cmis.biz01line.iqp.op.iqpequipinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIqpEquipInfoDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpEquipInfo";

	private final String equip_pk_name = "equip_pk";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String equip_pk_value = null;
			try {
				equip_pk_value = (String)context.getDataValue(equip_pk_name);
			} catch (Exception e) {}
			if(equip_pk_value == null || equip_pk_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+equip_pk_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, equip_pk_value, connection);
			this.putDataElement2Context(kColl, context);
			
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
