package com.yucheng.cmis.biz01line.prd.op.prdpolcyscheme;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class GetFlowNodeDicByFlowDic extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection conn = null;
		try {
			conn = this.getConnection(context);
			/**获取前端传递的流程值*/
			String flowValue = (String)context.getDataValue("flowValue");
			/**获取前端传递的政策值*/
			String schemeId = (String)context.getDataValue("schemeId");
			/**根据政策值获取已配置流程*/
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, conn);
			IndexedCollection SelectFlowListBySchemeId = ppsc.getFlowIdBySchemeId(schemeId);
			SelectFlowListBySchemeId.setName("SelectFlowListBySchemeId");
			this.putDataElement2Context(SelectFlowListBySchemeId, context);
			/**根据政策值、流程值获取已配置流程节点*/
			String flownode = ppsc.getFlowNodeBySchemeIdAndFlowId(schemeId, flowValue);
			if(context.containsKey("flownode")){
				context.setDataValue("flownode", flownode);
			}else {
				context.addDataField("flownode", flownode);
			}
			
			IndexedCollection plocyIColl = null;
			//plocyIColl = ppsc.getPlocyListBySchemeIdAndFlowIdAndNodeId(schemeid, flowId, nodeId);
			plocyIColl = ppsc.getPlocyListBySchemeId(schemeId);
			plocyIColl.setName("PrdCatalogList");
			this.putDataElement2Context(plocyIColl, context);
			
			context.addDataField("flag", "success");
			if(context.containsKey("flowValue")){
				context.setDataValue("flowValue", flowValue);
			}else {
				context.addDataField("flowValue", flowValue);
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
