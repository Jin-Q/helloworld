package com.yucheng.cmis.platform.workflow.op.wfiworkflow2org;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateWfiWorkflow2orgRecordOp extends CMISOperation {
	

	private final String modelId = "WfiWorkflow2org";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			String wf2orgId = (String) kColl.getDataValue("wf2org_id");
			//根据机构+申请类型检验配置唯一性
			String orgId = (String) kColl.getDataValue("org_id");
			String applType = (String) kColl.getDataValue("appl_type");
			String condition = "WHERE org_id='"+orgId+"' AND appl_type='"+applType+"' AND wf2org_id!='"+wf2orgId+"'";
			IndexedCollection icoll = dao.queryList(modelId, condition, connection);
			if(icoll!=null && icoll.size() > 0) {
				throw new EMPException("根据机构+申请类型检验配置唯一性不通过！");
			}
			
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}

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
