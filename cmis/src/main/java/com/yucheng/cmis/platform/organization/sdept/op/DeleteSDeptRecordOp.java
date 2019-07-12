package com.yucheng.cmis.platform.organization.sdept.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteSDeptRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SDept";
	
	//所要操作的表模型的主键
	private final String depno_name = "depno";
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			//删除一条特定的记录


			//获得删除需要的主键信息
			String depno_value = null;
			try {
				depno_value = (String)context.getDataValue(depno_name);
			} catch (Exception e) {}
			if(depno_value == null || depno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+depno_name+"] cannot be null!");
				

			//删除指定记录
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, depno_value, connection);
			if(count!=1){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "删除数据失败！操作影响了"+count+"条记录");
				return null;
			}
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "删除失败！失败原因："+ee.getMessage());
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
