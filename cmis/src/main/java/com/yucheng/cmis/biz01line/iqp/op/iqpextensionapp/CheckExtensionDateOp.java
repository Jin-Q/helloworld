package com.yucheng.cmis.biz01line.iqp.op.iqpextensionapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.component.CatalogManaComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class CheckExtensionDateOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {

		Connection conn = null;
		try {
			conn = this.getConnection(context);
			String type = (String)context.getDataValue("type");
			KeyedCollection kcoll = new KeyedCollection();
			
			if(type.equals("CheckExtensionDate")){
				kcoll.addDataField("bill_no", context.getDataValue("value"));
				CatalogManaComponent cmisComponent = (CatalogManaComponent)CMISComponentFactory.
				getComponentFactoryInstance().getComponentInstance("CatalogManaComponent", context, conn);
				kcoll = cmisComponent.excuteSql(type, kcoll);
			}
			
			context.addDataField("term_type", kcoll.getDataValue("term_type"));
			context.addDataField("cont_term", kcoll.getDataValue("cont_term"));
			context.addDataField("extension_date", kcoll.getDataValue("extension_date"));
			context.addDataField("date_type", kcoll.getDataValue("date_type"));
		}catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				this.releaseConnection(context, conn);
		}
		return "0";
	}
}