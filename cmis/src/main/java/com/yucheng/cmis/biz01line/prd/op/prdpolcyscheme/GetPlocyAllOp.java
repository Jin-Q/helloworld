package com.yucheng.cmis.biz01line.prd.op.prdpolcyscheme;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class GetPlocyAllOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		//查询已选政策资料进行配置
		Connection conn = this.getConnection(context);
		try {
			String schemeid = (String)context.getDataValue("schemeId");
			//查询出已选流程封装到IndexedCollection中
			IndexedCollection flowIColl = null;
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, conn);
			flowIColl = ppsc.getFlowIdBySchemeId(schemeid);
			flowIColl.setName("SelectFlowListBySchemeId");
			this.putDataElement2Context(flowIColl, context);
			
			//查询政策资料关联信息，封装到IndexedCollection中,注意只能传入单个节点，在前段页面控制
			String flowId = "";
			String nodeId = "";
			if(context.containsKey("flowValue")){
				flowId = (String)context.getDataValue("flowValue");	
			}
			if(context.containsKey("flownode")){
				nodeId = (String)context.getDataValue("flownode");
				/*if(nodeId.indexOf(",") != -1){
					throw new Exception("传入的节点值只能为一个，请选择一个节点！");
				}*/
			}
			IndexedCollection plocyIColl = null;
			//plocyIColl = ppsc.getPlocyListBySchemeIdAndFlowIdAndNodeId(schemeid, flowId, nodeId);
			plocyIColl = ppsc.getPlocyListBySchemeId(schemeid);
			plocyIColl.setName("PrdCatalogList");
			this.putDataElement2Context(plocyIColl, context);
			
			if(context.containsKey("schemeId")){
				context.setDataValue("schemeId", schemeid);
			}else {
				context.addDataField("schemeId", schemeid);
			}
			if(context.containsKey("flowValue")){
				context.setDataValue("flowValue", flowId);
			}else {
				context.addDataField("flowValue", flowId);
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
