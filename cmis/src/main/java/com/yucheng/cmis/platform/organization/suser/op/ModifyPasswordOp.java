package com.yucheng.cmis.platform.organization.suser.op;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.MD5;
import com.yucheng.cmis.pub.exception.ComponentException;

public class ModifyPasswordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SUser";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String actorno_value = null; //当前编辑用户
			try {
				actorno_value = (String)(context.getDataValue("currentUserId"));
			} catch (Exception e) {

				e.printStackTrace();
				EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "取您的用户代码失败！");
				throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"取您的用户代码失败！请重新操作！");
			}
						
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, actorno_value, connection);
			
			String password=(String)kColl.getDataValue("password");
			kColl.setDataValue("password", "");
			kColl.addDataField("passwordhiddenold", password);
			this.putDataElement2Context(kColl, context);
			
	
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
