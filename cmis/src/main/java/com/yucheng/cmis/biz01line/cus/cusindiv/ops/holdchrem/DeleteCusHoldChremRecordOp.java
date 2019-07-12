package com.yucheng.cmis.biz01line.cus.cusindiv.ops.holdchrem;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteCusHoldChremRecordOp extends CMISOperation {

	private final String modelId = "CusHoldChrem";
	

	private final String cus_id_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, cus_id_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag", PUBConstant.SUCCESS);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
