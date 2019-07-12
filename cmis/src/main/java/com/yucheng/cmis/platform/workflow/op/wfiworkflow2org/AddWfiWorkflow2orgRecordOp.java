package com.yucheng.cmis.platform.workflow.op.wfiworkflow2org;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddWfiWorkflow2orgRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "WfiWorkflow2org";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//根据机构+申请类型检验配置唯一性
			String orgId = (String) kColl.getDataValue("org_id");
			String applType = (String) kColl.getDataValue("appl_type");
			String condition = "WHERE org_id='"+orgId+"' AND appl_type='"+applType+"'";
			IndexedCollection icoll = dao.queryList(modelId, condition, connection);
			if(icoll!=null && icoll.size() > 0) {
				throw new EMPException("根据机构+申请类型+流程标识检验配置唯一性不通过！");
			}
			dao.insert(kColl, connection);
			
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
