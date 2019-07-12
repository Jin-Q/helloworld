package com.yucheng.cmis.biz01line.iqp.op.iqpextensionapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.component.CatalogManaComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class CheckUniqueOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {

		Connection conn = null;
		try {
			conn = this.getConnection(context);
			String type = (String)context.getDataValue("type");
			String records = "0";
			
			KeyedCollection kcoll = new KeyedCollection();
			if(type.equals("ExtensionAppCheck") || type.equals("ExtensionAgrCheck")){
				kcoll.addDataField("value", context.getDataValue("value"));
				CatalogManaComponent cmisComponent = (CatalogManaComponent)CMISComponentFactory.
				getComponentFactoryInstance().getComponentInstance("CatalogManaComponent", context, conn);
				kcoll = cmisComponent.excuteSql(type, kcoll);
			}
			
			records = kcoll.getDataValue("result").toString();
			if(records.equals("0")){
				context.addDataField("flag", "success");
			}else{
				context.addDataField("flag", "failue");
			}	
		}catch (EMPException ee) {
			context.addDataField("flag", "failue");
			throw ee;
		} catch (Exception e) {
			context.addDataField("flag", "failue");
			e.printStackTrace();
		} finally {
			if (conn != null)
				this.releaseConnection(context, conn);
		}
		return "0";
	}

}