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
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class RemoveIqpAppMemManaRecordOp extends CMISOperation {

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
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			int res = cmisComponent.removeIqpAppMem(pkMap, connection);			
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
