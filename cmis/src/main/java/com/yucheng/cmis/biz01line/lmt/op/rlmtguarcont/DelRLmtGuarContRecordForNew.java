package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DelRLmtGuarContRecordForNew extends CMISOperation {

	private final String modelId = "RLmtGuarCont";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String limit_code = null;
			String guar_cont_no = null;
			String guar_lvl = null;
			try { 
				limit_code = (String)context.getDataValue("limit_code");
				guar_cont_no = (String)context.getDataValue("guar_cont_no");
				guar_lvl = (String)context.getDataValue("guar_lvl"); 
			} catch (Exception e) {} 
			if(limit_code == null || limit_code.length() == 0 || guar_cont_no == null || guar_cont_no.length() == 0)
				throw new EMPJDBCException("The value of pk cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			/**删除授信担保合同数据后，下面一条授信担保合同的等级相应-1*/  
			Map<String,String> updateMap = new HashMap<String,String>(); 
			updateMap.put("LIMIT_CODE", limit_code);
			updateMap.put("GUAR_LVL", guar_lvl); 
         	int rels = SqlClient.update("updateRLmtGuarLvlZGENotApp", updateMap, null, null, connection);
			if(rels < 0){ 
				throw new EMPJDBCException("update failed!");  
			}
			
			Map<String, String> pkMap = new HashMap<String, String>();
			pkMap.put("limit_code", limit_code);
			pkMap.put("guar_cont_no", guar_cont_no);
			int countRel=dao.deleteByPks(modelId, pkMap, connection);
			if(countRel == 1){ 
				context.addDataField("flag", "success"); 
			}      
			
		}catch (EMPException ee) {
			context.addDataField("flag", "error"); 
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
