package com.yucheng.cmis.biz01line.prd.op.prdpolcyscheme;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DelPrdPolcySchemeFirstOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection conn = null;
		try {
			conn = this.getConnection(context);
			String doId = (String)context.getDataValue("doId");//操作标识
			String schemeid = (String)context.getDataValue("schemeid");
			String flowId = (String)context.getDataValue("flowid");
			String status = (String)context.getDataValue("effective");
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, conn);
			//ppsc.updatePrdSpaceRel(doId, schemeid, flowId, flowNode, polcyCode, status);
			ppsc.doPrdSpaceRel(doId, schemeid, flowId, status);
			
			//对返回的IColl进行封装，一个流程只显示一条记录
			IndexedCollection iColl = new IndexedCollection();
			iColl = ppsc.getFlowIdBySchemeId(schemeid);
			iColl.setName("PrdSchemeSpaceRel");
			this.putDataElement2Context(iColl, context);
			if(context.containsKey("schemeid")){
				context.setDataValue("schemeid", schemeid);
			}else {
				context.addDataField("schemeid", schemeid);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				this.releaseConnection(context, conn);
		}
		
		return null;
	}

}
