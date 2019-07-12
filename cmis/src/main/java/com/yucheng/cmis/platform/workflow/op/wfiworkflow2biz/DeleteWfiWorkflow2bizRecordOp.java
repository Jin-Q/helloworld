package com.yucheng.cmis.platform.workflow.op.wfiworkflow2biz;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteWfiWorkflow2bizRecordOp extends CMISOperation {

	private final String modelId = "WfiWorkflow2biz";
	private final String modelIdNode = "WfiNode2biz";

	private final String pk1_name = "pk1";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String pk1_value = null;
			try {
				pk1_value = (String)context.getDataValue(pk1_name);
			} catch (Exception e) {}
			if(pk1_value == null || pk1_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk1_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			//删除子表记录
			IndexedCollection icoll = dao.queryList(modelIdNode, "WHERE PK1='"+pk1_value+"'", connection);
			if(icoll!=null && icoll.size()>0) {
				Map pkMap = new HashMap();
				for(Object o : icoll) {
					KeyedCollection kcoll = (KeyedCollection) o;
					pkMap.put("pk1", pk1_value);
					pkMap.put("nodeid", kcoll.getDataValue("nodeid"));
					dao.deleteByPks(modelIdNode, pkMap, connection);
				}
			}
			
			int count=dao.deleteByPk(modelId, pk1_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
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
