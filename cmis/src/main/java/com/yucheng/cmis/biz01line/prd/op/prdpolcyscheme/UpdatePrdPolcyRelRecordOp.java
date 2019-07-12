package com.yucheng.cmis.biz01line.prd.op.prdpolcyscheme;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdatePrdPolcyRelRecordOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		//根据流程ID、政策ID修改该政策下所有节点信息
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String schemeid = (String)context.getDataValue("schemeid");
			String flowid = "";
			if(context.containsKey("flowId")){
				flowid = (String)context.getDataValue("flowId");
			}else {
				flowid = (String)context.getDataValue("flowid");
			}
			
			String flownode = null;
			if(context.containsKey("flownode")){
				flownode = (String)context.getDataValue("flownode");
			}
			//String plocycode = (String)context.getDataValue("plocycode");
			if(context.containsKey("schemeId")){
				context.setDataValue("schemeId", schemeid);
			}else {
				context.addDataField("schemeId",schemeid);
			}
			if(context.containsKey("flowValue")){
				context.setDataValue("flowValue", flowid);
			}else {
				context.addDataField("flowValue",flowid);
			}
			if(context.containsKey("flownode")){
				context.setDataValue("flownode", flownode);
			}else {
				context.addDataField("flownode",flownode);
			}
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
			IndexedCollection plocyIColl = null;
			if(flownode == null){
				plocyIColl = ppsc.getPlocyListBySchemeIdAndFlowId(schemeid, flowid);
			}else {
				plocyIColl = ppsc.getPlocyListBySchemeIdAndFlowIdAndNodeId(schemeid, flowid, flownode);
			}
			if(plocyIColl.size() == 0){
				plocyIColl = ppsc.getPlocyListBySchemeIdAndFlowId(schemeid, flowid);
			}
			
			plocyIColl.setName("PrdCatalogList");
			this.putDataElement2Context(plocyIColl, context);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
