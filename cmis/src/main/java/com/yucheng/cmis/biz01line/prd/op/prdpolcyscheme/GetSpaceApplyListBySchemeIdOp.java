package com.yucheng.cmis.biz01line.prd.op.prdpolcyscheme;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class GetSpaceApplyListBySchemeIdOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		/**
		 * 通过传递的政策资料方案编号查询关联的场景，支持多场景
		 * 分别将场景传递到IColl中
		 */
		Connection conn = this.getConnection(context);
		try {
			String schemeId = (String)context.getDataValue("schemeId");
			
			/*分流程节点展示时放开此注释
			 * List list = new ArrayList<String>();
			list.add("schemeid");
			list.add("plocycode");
			list.add("flowid");
			list.add("flownode");
			list.add("effective");
			
			IndexedCollection iColl = new IndexedCollection();
			TableModelDAO dao = this.getTableModelDAO(context);
			iColl = dao.queryList("PrdSchemeSpaceRel", list, " where schemeid = '"+schemeId +"' ", conn);
			将流程节点转换为节点名称
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kc = (KeyedCollection)iColl.get(i);
				IndexedCollection ic = (IndexedCollection)((KeyedCollection)context.getDataElement("dictColl")).getDataElement((String)kc.getDataValue("flowid")+"_FLOWNODE_TYPE");
				String flowNode = null;
				for(int j=0;j<ic.size();j++){
					KeyedCollection kcj = (KeyedCollection)ic.get(j);
					if(((String)kcj.getDataValue("enname")).equals((String)kc.getDataValue("flownode"))){
						kc.addDataField("flowNodeName", (String)kcj.getDataValue("cnname"));
					}
				}
			}*/
			
			//对返回的IColl进行封装，一个流程只显示一条记录
			IndexedCollection iColl = new IndexedCollection();
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, conn);
			iColl = ppsc.getFlowIdBySchemeId(schemeId);
			iColl.setName("PrdSchemeSpaceRel");
			
			this.putDataElement2Context(iColl, context);
			context.addDataField("schemeid", schemeId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (conn != null)
				this.releaseConnection(context, conn);
		}
		return null;
	}

}
