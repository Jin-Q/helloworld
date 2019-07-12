package com.yucheng.cmis.biz01line.cus.op.cusother.cusfixhistory;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class QueryCusFixHistoryDetailOp  extends CMISOperation {
	
	private final String modelId = "CusFixHistory";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cusId = (String) context.getDataValue("cus_id");
			
			CusBaseComponent baseComp = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(PUBConstant.CUSBASE, context, connection);
			CusBase cusBase = baseComp.getCusBase(cusId);
			
			KeyedCollection kColl = new KeyedCollection(modelId);
			
			kColl.addDataField("cus_id", cusId);
			kColl.addDataField("cus_name",cusBase.getCusName());
			kColl.addDataField("update_id", context.getDataValue(CMISConstance.ATTR_CURRENTUSERID));
			kColl.addDataField("input_br_id", context.getDataValue(CMISConstance.ATTR_ORGID));
			
			this.putDataElement2Context(kColl, context);
			
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
