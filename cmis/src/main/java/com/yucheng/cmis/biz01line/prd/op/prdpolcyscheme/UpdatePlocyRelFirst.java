package com.yucheng.cmis.biz01line.prd.op.prdpolcyscheme;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdatePlocyRelFirst extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String flowValue = (String)context.getDataValue("flowValue");
			String flowNodeValue = (String)context.getDataValue("flowNodeValue");
			String schemecode = (String)context.getDataValue("schemecode");			
			String schemeid = (String)context.getDataValue("schemeId");	
			String schemetype = (String)context.getDataValue("schemetype");	
			String ifSelect = (String)context.getDataValue("ifSelect");
			
			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("flowValue", flowValue);
			kColl.addDataField("flowNodeValue", flowNodeValue);
			kColl.addDataField("schemecode", schemecode);
			kColl.addDataField("schemeid", schemeid);
			kColl.addDataField("schemetype", schemetype);
			
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
			
			//ppsc.insertPrdSchemeSpaceRel(ifSelect,kColl);
			ppsc.updatePrdSchemeSpaceRel(ifSelect, kColl);
			
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}	
		return null;
	}

}
