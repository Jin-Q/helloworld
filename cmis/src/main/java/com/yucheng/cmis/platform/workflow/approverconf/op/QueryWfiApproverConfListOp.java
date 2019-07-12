package com.yucheng.cmis.platform.workflow.approverconf.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryWfiApproverConfListOp extends CMISOperation {


	private final String modelId = "WfiApproverConf";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"order by confid desc";
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "20");
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo, connection);
			iColl.setName(iColl.getName()+"List");
			for(int i=0; i<iColl.size(); i++) {
				KeyedCollection kcoll = (KeyedCollection) iColl.get(i);
				kcoll.addDataField("wfsign_displayname", WorkFlowUtil.getWFPropertyByWfSign((String)kcoll.getDataValue("wfsign"), "WFName"));
				kcoll.addDataField("nodeid_displayname", WorkFlowUtil.getWFNodeProperty((String)kcoll.getDataValue("nodeid"), "NodeName"));
			}
			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
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
