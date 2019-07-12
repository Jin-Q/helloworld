package com.yucheng.cmis.biz01line.arp.op.arpbusidebtinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.arp.component.ArpPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class AddArpBusiDebtInfoRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "ArpBusiDebtInfo";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
			
			KeyedCollection kCollpar = new KeyedCollection();
			kCollpar.addDataField("serno", kColl.getDataValue("serno"));
			ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",context,connection);
			KeyedCollection kcoll = cmisComponent.delReturnSql("getArpBusiSum", kCollpar);
			System.out.println();
			//获取几个金额字段的累加和
			KeyedCollection kCollR = (KeyedCollection) kcoll.getDataValue("results");
			kCollR.addDataField("serno", kColl.getDataValue("serno"));
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
