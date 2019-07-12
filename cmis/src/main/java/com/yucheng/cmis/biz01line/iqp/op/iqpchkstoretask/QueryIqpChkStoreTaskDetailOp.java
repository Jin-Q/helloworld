package com.yucheng.cmis.biz01line.iqp.op.iqpchkstoretask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpChkStoreTaskDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpChkStoreTask";
	

	private final String task_id_name = "task_id";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			
			String task_id_value = null;
			try {
				task_id_value = (String)context.getDataValue(task_id_name);
			} catch (Exception e) {}
			if(task_id_value == null || task_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+task_id_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, task_id_value, connection);
			
			
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"manager_id","input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id"});
			this.putDataElement2Context(kColl, context); 
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
