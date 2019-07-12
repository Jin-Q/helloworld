package com.yucheng.cmis.biz01line.prd.op.prdcatalog;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class GetPrdCatalogAddPageOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		KeyedCollection kColl = new KeyedCollection("PrdCatalog");
		try{
			connection = this.getConnection(context);
			/**
			 * 设置默认登记人员、组织机构、录入时间
			 */
			String inputId = (String)context.getDataValue(PUBConstant.currentUserId);
			String inputDate = (String)context.getDataValue(PUBConstant.OPENDAY);
			String orgId = (String)context.getDataValue(PUBConstant.organNo);
			
			kColl.addDataField("inputid", inputId);
			kColl.addDataField("inputdate", inputDate);
			kColl.addDataField("orgid", orgId);
			this.putDataElement2Context(kColl, context);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
