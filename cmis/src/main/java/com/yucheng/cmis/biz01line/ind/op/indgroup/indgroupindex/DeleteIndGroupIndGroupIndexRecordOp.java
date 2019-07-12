package com.yucheng.cmis.biz01line.ind.op.indgroup.indgroupindex;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;	

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIndGroupIndGroupIndexRecordOp extends CMISOperation {
	
	  private static final Logger logger = Logger.getLogger(DeleteIndGroupIndGroupIndexRecordOp.class);
	
	//扄1�7要操作的表模垄1�7
	private final String modelId = "IndGroupIndex";
	
	//扄1�7要操作的表模型的主键
	private final String group_no_name = "group_no";
	private final String index_no_name = "index_no";

	/**
	 * 业务逻辑执行的具体实现方泄1�7
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			//删除丄1�7条特定的记录
			
			//获得删除霄1�7要的主键信息
			String group_no_value = null;
			try {
				group_no_value = (String)context.getDataValue(group_no_name);
			} catch (Exception e) 
			{
				logger.error(e.getMessage(), e);
			}
			if(group_no_value == null || group_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+group_no_name+"] cannot be null!");
			String index_no_value = null;
			try {
				index_no_value = (String)context.getDataValue(index_no_name);
			} catch (Exception e) 
			{
				logger.error(e.getMessage(), e);
			}
			if(index_no_value == null || index_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+index_no_name+"] cannot be null!");

			//删除指定记录
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("group_no",group_no_value);
			pkMap.put("index_no",index_no_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
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
