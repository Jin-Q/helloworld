package com.yucheng.cmis.platform.workflow.approverconf.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISDao;

public class DeleteWfiApproverConfRecordOp extends CMISOperation {

	private final String modelId = "WfiApproverConf";
	

	private final String confid_name = "confid";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String confid_value = null;
			try {
				confid_value = (String)context.getDataValue(confid_name);
			} catch (Exception e) {}
			if(confid_value == null || confid_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+confid_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			//删除配置用户记录
			
			CMISDao cmisDao = new CMISDao();
			cmisDao.setContext(context);
			cmisDao.setConnection(connection);
			Map fieldMap = new HashMap();
			fieldMap.put("confid", confid_value);
			cmisDao.deleteByField("WfiApproverUser", fieldMap);
			
			int count=dao.deleteByPk(modelId, confid_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.put("flag", 1);
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
