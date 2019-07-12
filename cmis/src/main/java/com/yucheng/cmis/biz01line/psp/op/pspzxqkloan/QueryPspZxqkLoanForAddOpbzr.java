package com.yucheng.cmis.biz01line.psp.op.pspzxqkloan;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
//贷后管理系统改造（常规检查）    XD141222090     modefied by zhaoxp  2015-01-22
public class QueryPspZxqkLoanForAddOpbzr  extends CMISOperation {
	
	private final String modelId = "PspZxqkLoan";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			
			String task_id = null;
			String cus_id_bzr = null;
			
			try {
				task_id = (String)context.getDataValue("task_id");
				cus_id_bzr = (String)context.getDataValue("cus_id_bzr");
			} catch (Exception e) {}
			if(task_id == null || task_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+task_id+"] cannot be null!");
			if(cus_id_bzr == null || cus_id_bzr.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_bzr+"] cannot be null!");
				
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String conditionStr = " where task_id = '"+task_id+"' and cus_id_bzr = '"+cus_id_bzr+"'";
			
			KeyedCollection kCollStr = dao.queryFirst(modelId, null, conditionStr, connection);
			if(kCollStr==null||kCollStr.size()<1||kCollStr.getDataValue("cus_id") == null||"".equals(kCollStr.getDataValue("cus_id"))){
				kCollStr.setDataValue("cus_id_bzr", cus_id_bzr);
			}
			
			String[] args=new String[] {"cus_id_bzr"};
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"}; 
			String[] fieldName=new String[]{"cus_name"};
			String[] resultName = new String[] { "cus_id_displayname"};
		    //详细信息翻译时调用	
			SystemTransUtils.dealPointName(kCollStr, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			this.putDataElement2Context(kCollStr, context);
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
