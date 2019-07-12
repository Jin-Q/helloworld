package com.yucheng.cmis.biz01line.lmt.op.lmtindusauthapply;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteLmtIndusAuthApplyRecordOp extends CMISOperation {

	private final String modelId = "LmtIndusAuthApply";
	

	private final String serno_name = "serno";
	private final String input_br_id_name = "input_br_id";
	private final String guar_type_name = "guar_type";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				
			String input_br_id_value = null;
			try {
				input_br_id_value = (String)context.getDataValue(input_br_id_name);
			} catch (Exception e) {}
			if(input_br_id_value == null || input_br_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+input_br_id_name+"] cannot be null!");
				
			String guar_type_value = null;
			try {
				guar_type_value = (String)context.getDataValue(guar_type_name);
			} catch (Exception e) {}
			if(guar_type_value == null || guar_type_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+guar_type_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("serno",serno_value);
			pkMap.put("input_br_id",input_br_id_value);
			pkMap.put("guar_type",guar_type_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag", PUBConstant.SUCCESS);
			
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
