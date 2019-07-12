package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2015-5-21
*@description TODO  需求编号：XD150504034 贷后管理常规检查任务改造
*@version v1.0
*
 */
public class UpdateGrpInfoRecordOp extends CMISOperation {
	

	private final String modelId = "PspCheckAnaly";
	

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
			String task_id = kColl.getDataValue("task_id").toString();
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColl  =dao.queryList(modelId, "where task_id ='"+task_id+"'", connection);
			if(iColl!=null && iColl.size()>0){
				int count=dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("Update Failed! Record Count: " + count);
				}
			}else{
				dao.insert(kColl, connection);
			}

			context.addDataField("flag", "success");
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
