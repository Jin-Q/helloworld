package com.yucheng.cmis.biz01line.prd.op.prdbasicinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class GetPrdOrgApplyListByPrdPkOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			IndexedCollection prdOrgApplyIColl = null;
			String prdId = "";
			String query = "";
			if(context.containsKey("prdId")){
				prdId = (String)context.getDataValue("prdId");
			}
			if(context.containsKey("query")){
				query = (String)context.getDataValue("query");
			}
			
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
			prdOrgApplyIColl = ppsc.getPrdOrgApplyICollByPrdId(prdId);
			prdOrgApplyIColl.setName("PrdOrgApplyList");
			this.putDataElement2Context(prdOrgApplyIColl, context);

			if(context.containsKey("prdId")){
				context.setDataValue("prdId", prdId);
			}else {
				context.addDataField("prdId", prdId);
			}
			if(context.containsKey("query")){
				context.setDataValue("query", query);
			}else {
				context.addDataField("query", query);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connection != null){
				this.releaseConnection(context, connection);
			}
		}
		
		return null;
	}

}
