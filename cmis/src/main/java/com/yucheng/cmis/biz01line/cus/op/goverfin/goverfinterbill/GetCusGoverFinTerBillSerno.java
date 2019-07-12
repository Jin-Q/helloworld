package com.yucheng.cmis.biz01line.cus.op.goverfin.goverfinterbill;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class GetCusGoverFinTerBillSerno extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			KeyedCollection kColl = new KeyedCollection("CusGoverFinTerBill");
			connection = this.getConnection(context);
			kColl.addDataField("manager_id", (String)context.getDataValue("manager_id"));
			kColl.addDataField("manager_br_id", (String)context.getDataValue("manager_br_id"));
			kColl.addDataField("input_id", context.getDataValue("currentUserId"));
			kColl.addDataField("input_br_id", context.getDataValue("organNo"));
			
			CusBaseComponent cusBaseComp = (CusBaseComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusBase", context, connection);
			String cus_id = (String) context.getDataValue("cus_id");
			CusBase cusBase = cusBaseComp.getCusBase(cus_id);
			String cus_name = cusBase.getCusName();
			kColl.addDataField("cus_name", cus_name);

			this.putDataElement2Context(kColl, context);
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
