package com.yucheng.cmis.biz01line.ind.op.indmodel;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIndModelRecordOp extends CMISOperation {
	
	private static final Logger logger = Logger.getLogger(DeleteIndModelRecordOp.class);
	
	//扄1�7要操作的表模垄1�7
	private final String modelId = "IndModel";
	
	//扄1�7要操作的表模型的主键
	private final String model_no_name = "model_no";
	
	/**
	 * 业务逻辑执行的具体实现方泄1�7
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			
			//获得删除霄1�7要的主键信息
			String model_no_value = null;
			try {
				model_no_value = (String)context.getDataValue(model_no_name);
			} catch (Exception e) 
			{
				logger.error(e.getMessage(), e);
			}
			if(model_no_value == null || model_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+model_no_name+"] cannot be null!");


			//删除指定记录
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, model_no_value, connection);
			if(count!=1){
				throw new EMPException("删除数据失败！操作影响了"+count+"条记彄1�7");
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
