package com.yucheng.cmis.platform.organization.sduty.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.initializer.OrganizationInitializer;

public class DeleteSDutyRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SDuty";
	
	//所要操作的表模型的主键
	private final String dutyno_name = "dutyno";
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			//删除一条特定的记录


			//获得删除需要的主键信息
			String dutyno_value = null;
			try {
				dutyno_value = (String)context.getDataValue(dutyno_name);
			} catch (Exception e) {}
			if(dutyno_value == null || dutyno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+dutyno_name+"] cannot be null!");
				

			//删除指定记录
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, dutyno_value, connection);
			if(count!=1){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "删除数据失败！操作影响了"+count+"条记录");
				return null;
			}
			
			OrganizationInitializer.removeDutyMapInfo(dutyno_value);
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
