package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.exception.AsynException;

public class AddRscTaskInfoSubOp extends CMISOperation {
	private final String modelId = "RscTaskInfoSub";
	/**
	 * <p>
	 * <h2>简述</h2>
	 *    <ol>新增记录</ol>
	 * <h2>功能描述</h2>
	 * 		<ol>无</ol>
	 * </p>
	 * @param context EMP上下文
	 * @return String
	 * @throws EMPException
	 */ 
	@Override
	public String doExecute(Context context) throws EMPException {
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		Connection connection = null;
		String conditionStr = "";
		
		try {
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//新增数据
			int result = dao.insert(kColl, connection);
			//将处理结果放入Context中以便前端获取
			if(result>0){
				context.addDataField("flag", "success");
			} else {
				context.addDataField("flag", "failed");
			}
		} catch (EMPException ee) {
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
