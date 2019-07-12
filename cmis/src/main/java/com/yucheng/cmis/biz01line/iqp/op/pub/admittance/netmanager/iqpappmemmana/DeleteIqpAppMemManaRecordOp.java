package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpappmemmana;

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

public class DeleteIqpAppMemManaRecordOp extends CMISOperation {

	private final String modelId = "IqpAppMemMana";
	private final String modelIdMemMana = "IqpMemMana";
	

	private final String serno_name = "serno";
	private final String mem_cus_id_name = "mem_cus_id";

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
				
			String mem_cus_id_value = null;
			try {
				mem_cus_id_value = (String)context.getDataValue(mem_cus_id_name);
			} catch (Exception e) {}
			if(mem_cus_id_value == null || mem_cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+mem_cus_id_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("serno",serno_value);
			pkMap.put("mem_cus_id",mem_cus_id_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			//成员变更状态，如果入网成员或 原有成员,则修改为退网成员,反之则为撤销退网操作
			String status = (String)kColl.getDataValue("status");
			if("1".equals(status) || "3".equals(status)){
				kColl.setDataValue("status", "2");
				context.addDataField("message", "backNet");
			}else if("2".equals(status)){
				//判断成员退网操作前是入网成员还是原有成员
				String condit = "where mem_cus_id="+mem_cus_id_value;
				IndexedCollection iColl = dao.queryList(modelIdMemMana, condit, connection);
				if(iColl.size()>0 && iColl != null){
					kColl.setDataValue("status", "3");
				}else{
					kColl.setDataValue("status", "1");
				}
				context.addDataField("message", "desNet");
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
