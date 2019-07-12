package com.yucheng.cmis.platform.organization.sdept.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddSDeptRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SDept";
	
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
			
			//机构码
			String depno_value = (String)kColl.getDataValue("depno");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//检查待新增部是否存在于数据库中 at 2010-11-2 10:40:35
			KeyedCollection existKColl = dao.queryDetail(modelId, depno_value, connection);
			if(existKColl != null && existKColl.containsKey("depno") && existKColl.getDataValue("depno") != null){
				flag = "exist";
			}
			//新增一条记录
			else{
				dao.insert(kColl, connection);
				flag = "sucess";
			}
			
			context.addDataField("flag", flag);
			context.addDataField("deptno", depno_value); 
			
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
