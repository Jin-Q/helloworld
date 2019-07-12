package com.yucheng.cmis.biz01line.ccr.op.ccrbizmodel;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteCcrBizModelRecordOp extends CMISOperation {

	private final String modelId = "CcrBizModel";
	

	private final String com_cll_typ_name = "com_cll_typ";
	private final String com_opt_scale_name = "com_opt_scale";
	private final String index_no_name = "index_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String com_cll_typ_value = null;
			try {
				com_cll_typ_value = (String)context.getDataValue(com_cll_typ_name);
			} catch (Exception e) {}
			if(com_cll_typ_value == null || com_cll_typ_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+com_cll_typ_name+"] cannot be null!");
				
			String com_opt_scale_value = null;
			try {
				com_opt_scale_value = (String)context.getDataValue(com_opt_scale_name);
			} catch (Exception e) {}
			if(com_opt_scale_value == null || com_opt_scale_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+com_opt_scale_name+"] cannot be null!");
				
			String index_no_value = null;
			try {
				index_no_value = (String)context.getDataValue(index_no_name);
			} catch (Exception e) {}
			if(index_no_value == null || index_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+index_no_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("com_cll_typ",com_cll_typ_value);
			pkMap.put("com_opt_scale",com_opt_scale_value);
			pkMap.put("index_no",index_no_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
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
