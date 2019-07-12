package com.yucheng.cmis.biz01line.arp.op.arplawlawsuitdtmana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.arp.component.ArpPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateArpLawLawsuitDtmanaRecordOp extends CMISOperation {
	

	private final String modelId = "ArpLawLawsuitDtmana";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}

			setLawsuitFee(kColl, connection, context);
			context.addDataField("flag", PUBConstant.SUCCESS);
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
	
	/*** 重新合计诉讼费用 ***/
	public void setLawsuitFee(KeyedCollection kColl , Connection connection,Context context) throws EMPException {		
		KeyedCollection trans_kcoll = new KeyedCollection("TransValue");
		ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
		.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",context,connection);
		trans_kcoll.addDataField("case_no", kColl.getDataValue("case_no"));
		cmisComponent.delExecuteSql("manaLawsuitFee", trans_kcoll);
	}
}
