package com.yucheng.cmis.biz01line.prd.op.prdlibormaintain;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeletePrdLiborMaintainRecordOp extends CMISOperation {

	private final String modelId = "PrdLiborMaintain";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String pk_ids = null;
			int count=0;
			try {
				pk_ids = (String)context.getDataValue("pk_ids");
			} catch (Exception e) {}
			if(pk_ids == null || pk_ids.length() == 0)
				throw new EMPJDBCException("The value of pk pk_id cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			for(int i=0;i<(pk_ids.split(",")).length;i++){
				count=dao.deleteByPk(modelId, pk_ids.split(",")[i], connection);
			}
			context.addDataField("flag","success");
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
