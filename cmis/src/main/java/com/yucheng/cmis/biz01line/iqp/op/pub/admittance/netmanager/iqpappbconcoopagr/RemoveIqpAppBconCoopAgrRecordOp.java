package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpappbconcoopagr;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class RemoveIqpAppBconCoopAgrRecordOp extends CMISOperation {

	private final String modelId = "IqpAppBconCoopAgr";
	

	private final String serno_name = "serno";
	private final String coop_agr_no_name = "coop_agr_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				
			String coop_agr_no_value = null;
			try {
				coop_agr_no_value = (String)context.getDataValue(coop_agr_no_name);
			} catch (Exception e) {}
			if(coop_agr_no_value == null || coop_agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+coop_agr_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("serno",serno_value);
			pkMap.put("coop_agr_no",coop_agr_no_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			//
			String status = (String)kColl.getDataValue("status");
			if("0".equals(status)){
				kColl.setDataValue("status", "1");
				context.addDataField("msg", "success");//失效变成生效
			}else if("1".equals(status)){
				kColl.setDataValue("status", "0");
				context.addDataField("msg", "error");//生效变成失效
			}
			dao.update(kColl, connection);
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
