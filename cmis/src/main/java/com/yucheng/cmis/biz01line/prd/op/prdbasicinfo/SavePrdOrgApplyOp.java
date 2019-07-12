package com.yucheng.cmis.biz01line.prd.op.prdbasicinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class SavePrdOrgApplyOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		// 新增产品适用机构记录
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String prdId = "";
			if(context.containsKey("prdId")){
				prdId = (String)context.getDataValue("prdId");
			}
			IndexedCollection iColl = (IndexedCollection)context.getDataElement("PrdOrgApplyList");
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
			ppsc.doPrdOrgApplyByIColl(prdId, iColl);
			
			context.addDataField("flag", "success");
			
		} catch (Exception e) {
			context.addDataField("flag", "");
			e.printStackTrace();
		} finally {
			if(connection != null){
				this.releaseConnection(context, connection);
			}
		}
		return null;
	}

}
