package com.yucheng.cmis.biz01line.psp.op.pspappaltsignal;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPspAppAltSignalListOp extends CMISOperation {

	private final String modelId = "PspAppAltSignal";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
			String task_id = null;
			String cus_id = null;
			try {
				task_id = (String)context.getDataValue("task_id");
				cus_id = (String)context.getDataValue("cus_id");
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			if(task_id==null||"".equals(task_id)){
				throw new Exception("[task_id] 为空！");
			}
			if(cus_id==null||"".equals(cus_id)){
				throw new Exception("[cus_id] 为空");
			}
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if(conditionStr==null || "".equals(conditionStr)){
				conditionStr = " where task_id = '"+task_id+"' order by pk_id desc";
			}else{
				conditionStr += " and task_id = '"+task_id+"' order by pk_id desc";
			}
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			
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
