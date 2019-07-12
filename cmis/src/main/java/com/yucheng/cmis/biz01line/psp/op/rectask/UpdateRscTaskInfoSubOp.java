package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateRscTaskInfoSubOp  extends CMISOperation {
	private final String modelId = "RscTaskInfo";
	/**
	 * <p>
	 * <h2>简述</h2>
	 *    <ol>更新记录</ol>
	 * <h2>功能描述</h2>
	 * 		<ol>无</ol>
	 * </p>
	* @param context EMP上下文
	 * @return String
	 * @throws EMPException
	 */
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

		    KeyedCollection kColl = null;
		    try {
			    kColl = (KeyedCollection)context.getDataElement(modelId);
		    } catch (Exception e) {}
		    if(kColl == null || kColl.size() == 0)
			    throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
		    
		    TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){ 
				context.addDataField("flag", "failed");
			} else {
				context.addDataField("flag", "success");
			}
		    
		}catch(Exception e){ 
			context.addDataField("flag","failed");
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
