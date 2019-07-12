package com.yucheng.cmis.platform.workflow.op.taskpool;

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

public class DeleteWfTaskpoolRecordOp extends CMISOperation {

	private final String modelId = "WfTaskpool";
	

	private final String tpid_name = "tpid";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String tpid_value = null;
			try {
				tpid_value = (String)context.getDataValue(tpid_name);
			} catch (Exception e) {}
			if(tpid_value == null || tpid_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+tpid_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			//删除关联
			IndexedCollection icoll = dao.queryList("WfTaskpoolUser", "WHERE TPID='"+tpid_value+"'", connection);
			if(icoll!=null && icoll.size()>0) {
				Map pkMap = new HashMap();
				for(int i=0; i<icoll.size(); i++) {
					KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
					pkMap.put("tpid", kcoll.getDataValue("tpid"));
					pkMap.put("userid", kcoll.getDataValue("userid"));
					int count = dao.deleteAllByPks("WfTaskpoolUser", pkMap, connection);
				}
			}

			int count=dao.deleteByPk(modelId, tpid_value, connection);
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
