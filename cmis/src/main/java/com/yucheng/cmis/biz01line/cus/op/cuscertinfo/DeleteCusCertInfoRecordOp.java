package com.yucheng.cmis.biz01line.cus.op.cuscertinfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteCusCertInfoRecordOp extends CMISOperation {

	private final String modelId = "CusCertInfo";
	
	private final String cus_id_name = "cus_id";
	private final String cert_typ_name = "cert_typ";
	private final String cert_code_name = "cert_code";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
				
			String cus_id_value = null;
			String cert_typ_value = null;
			String cert_code_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
				cert_typ_value = (String)context.getDataValue(cert_typ_name);
				cert_code_value = (String)context.getDataValue(cert_code_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
			if(cert_typ_value == null || cert_typ_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cert_typ_name+"] cannot be null!");
			if(cert_code_value == null || cert_code_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cert_code_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cus_id",cus_id_value);
			pkMap.put("cert_typ",cert_typ_value);
			pkMap.put("cert_code", cert_code_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
			flag = "删除成功";
		}catch (EMPException ee) {
			flag = "删除失败";
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag",flag);
		return "0";
	}
}
