package com.yucheng.cmis.biz01line.psp.op.pspsitecheckcom;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
//贷后管理系统改造（常规检查）    XD141222090     modefied by zhaoxp  2014-12-24 
public class DeletePspSitecheckComRecordOp extends CMISOperation {

	private final String modelId = "PspSitecheckCom";
	

	private final String pk_id_name = "pk_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String pk_id = null;
			try {
				pk_id= (String)context.getDataValue(pk_id_name);
			} catch (Exception e) {}
			if(pk_id == null || pk_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, pk_id, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
			context.addDataField("flag", "success");
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
