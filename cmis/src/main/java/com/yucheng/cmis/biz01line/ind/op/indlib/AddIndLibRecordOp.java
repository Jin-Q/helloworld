package com.yucheng.cmis.biz01line.ind.op.indlib;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddIndLibRecordOp extends CMISOperation {
	
	private static final Logger logger = Logger.getLogger(AddIndLibRecordOp.class);
	
	//扄1�7要操作的表模垄1�7
	private final String modelId = "IndLib";
	
	/**
	 * 业务逻辑执行的具体实现方泄1�7
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e)
			{
				 logger.error(e.getMessage(), e);
			}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//新增丄1�7条记彄1�7
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
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
