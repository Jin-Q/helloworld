package com.yucheng.cmis.platform.organization.srole.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddSRoleRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SRole";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String roleno_value = (String)kColl.getDataValue("roleno");
			
			//校验待新增角色是否已经存在于数据库中
			KeyedCollection existKColl = dao.queryDetail(modelId, roleno_value, connection);
			if(existKColl != null && existKColl.containsKey("roleno") && existKColl.getDataValue("roleno") != null){
				flag = "exist";
			}
			//新增一条记录
			else {
				dao.insert(kColl, connection);
				flag = "sucess";
			}
			
			context.addDataField("flag", flag);
			context.addDataField("roleno", roleno_value);
			
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
