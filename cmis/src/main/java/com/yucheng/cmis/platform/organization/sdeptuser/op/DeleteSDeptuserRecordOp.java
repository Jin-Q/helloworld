package com.yucheng.cmis.platform.organization.sdeptuser.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteSDeptuserRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SDeptuser";
	
	//所要操作的表模型的主键
	private final String organno_name = "organno";
	private final String actorno_name = "actorno";
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			//删除一条特定的记录


			//获得删除需要的主键信息
			String organno_value = null;
			try {
				organno_value = (String)context.getDataValue(organno_name);
			} catch (Exception e) {}
			if(organno_value == null || organno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+organno_name+"] cannot be null!");
				
			String actorno_value = null;
			try {
				actorno_value = (String)context.getDataValue(actorno_name);
			} catch (Exception e) {}
			if(actorno_value == null || actorno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+actorno_name+"] cannot be null!");
				

			//删除指定记录
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("organno",organno_value);
			pkMap.put("actorno",actorno_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("删除数据失败！操作影响了"+count+"条记录");
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
