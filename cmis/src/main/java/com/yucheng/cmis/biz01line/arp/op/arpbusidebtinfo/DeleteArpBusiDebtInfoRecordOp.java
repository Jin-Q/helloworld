package com.yucheng.cmis.biz01line.arp.op.arpbusidebtinfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.arp.component.ArpPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteArpBusiDebtInfoRecordOp extends CMISOperation {

	private final String modelId = "ArpBusiDebtInfo";
	

	private final String serno_name = "serno";
	private final String bill_no_name = "bill_no";

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
				
			String bill_no_value = null;
			try {
				bill_no_value = (String)context.getDataValue(bill_no_name);
			} catch (Exception e) {}
			if(bill_no_value == null || bill_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+bill_no_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("serno",serno_value);
			pkMap.put("bill_no",bill_no_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			KeyedCollection kCollpar = new KeyedCollection();
			kCollpar.addDataField("serno",serno_value);
			ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",context,connection);
			KeyedCollection kcoll = cmisComponent.delReturnSql("getArpBusiSum", kCollpar);
			System.out.println();
			//获取几个金额字段的累加和
			KeyedCollection kCollR = (KeyedCollection) kcoll.getDataValue("results");
			kCollR.addDataField("serno",serno_value);
			kCollR.setName("ArpCollDebtApp");
			dao.update(kCollR, connection);
			context.addDataField("flag","success");
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
