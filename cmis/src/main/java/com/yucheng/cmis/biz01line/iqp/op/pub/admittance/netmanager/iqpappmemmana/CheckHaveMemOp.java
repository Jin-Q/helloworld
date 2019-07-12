package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpappmemmana;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckHaveMemOp  extends CMISOperation {
	private final String modelId = "IqpAppMemMana";

	private final String serno_name = "serno";
	private final String cus_id_name = "cus_id";
	
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = null;
			String serno = null;
			try {
				serno = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno == null || serno.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			try {
				cus_id = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id == null || cus_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("serno",serno);
			pkMap.put("mem_cus_id",cus_id);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			String selectSerno = (String)kColl.getDataValue("serno");
			if(selectSerno == null || "".equals(selectSerno)){
				context.addDataField("flag", "success");
			}else{
				context.addDataField("flag", "error");
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
