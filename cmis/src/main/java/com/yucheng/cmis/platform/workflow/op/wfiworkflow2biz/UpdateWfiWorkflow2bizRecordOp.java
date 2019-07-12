package com.yucheng.cmis.platform.workflow.op.wfiworkflow2biz;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateWfiWorkflow2bizRecordOp extends CMISOperation {
	

	private final String modelId = "WfiWorkflow2biz";
	

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
			
			//根据申请类型+流程标识+配置适用范围校验配置唯一性
			String applType = (String) kColl.getDataValue("appl_type");
			String wfSign = (String) kColl.getDataValue("wfsign");
			String sceneScope = (String) kColl.getDataValue("scene_scope");
			String pk1 = (String) kColl.getDataValue("pk1");
			String condition = "WHERE APPL_TYPE='"+applType+"' AND WFSIGN='"+wfSign+"' AND SCENE_SCOPE='"+sceneScope+"' AND PK1!='"+pk1+"' ";
			IndexedCollection icoll = dao.queryList(modelId, condition, connection);
			if(icoll!=null && icoll.size()>0) {
				context.put("flag", "根据申请类型+流程标识+配置适用范围校验唯一性不通过！");
			} else {
				int count=dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("Update Failed! Record Count: " + count);
				}
				context.put("flag", count);
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
