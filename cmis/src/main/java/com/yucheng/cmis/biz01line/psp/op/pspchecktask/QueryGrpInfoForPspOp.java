package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2015-5-21
*@description TODO  需求编号：XD150504034 贷后管理常规检查任务改造
*@version v1.0
*
 */
public class QueryGrpInfoForPspOp extends CMISOperation {
	private final String modelId = "PspCheckAnaly";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String task_id = null;
		String op=null;
		try{
			connection = this.getConnection(context);
			try {
				task_id = (String) context.getDataValue("task_id");
				op = (String) context.getDataValue("op");
			} catch (Exception e) {
            	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "获取不到任务编号", null);
            	throw new EMPException(e);
            }
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, task_id, connection);
			kColl.setDataValue("task_id", task_id);
			this.putDataElement2Context(kColl, context);
			if("view".equals(op)){
				return "1";
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
