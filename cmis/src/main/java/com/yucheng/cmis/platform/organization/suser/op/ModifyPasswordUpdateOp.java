package com.yucheng.cmis.platform.organization.suser.op;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.MD5;

public class ModifyPasswordUpdateOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SUser";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			//更新一条指定的记录
			
			KeyedCollection kColl = null;
			String actorno = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			 
				
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
		
			//处理加密
			
			String password=(String)kColl.getDataValue("password");
			String user=(String)kColl.getDataValue("actorno");
			/* MD5 m = new MD5();
			 password=m.getMD5ofStr(user+password);
			 kColl.setDataValue("password", password);
			*/
			MD5.md5Str2Kcol(kColl, user,password);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("修改数据失败！操作影响了"+count+"条记录");
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
