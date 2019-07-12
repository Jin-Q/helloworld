package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateCorreRelRecord4LmtOp extends CMISOperation {
	
	private final String modelId = "RLmtAppGuarCont";       
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String guar_cont_no = null;
			String limit_code = null;
			String corre_rel = null; 
			String serno = null;
			try { 
				guar_cont_no = (String)context.getDataValue("guar_cont_no");
				limit_code = (String)context.getDataValue("limit_code");
				corre_rel = (String)context.getDataValue("corre_rel");
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(limit_code == null || "".equals(limit_code) || corre_rel == null || "".equals(corre_rel))
				throw new EMPJDBCException("The values pk_id or corre_rel cannot be empty!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			
			Map<String,String> updateMap = new HashMap<String,String>(); 
			updateMap.put("guar_cont_no", guar_cont_no); 
			updateMap.put("limit_code", limit_code); 
			updateMap.put("serno", serno); 
			KeyedCollection kColl = dao.queryDetail(modelId, updateMap, connection);
			kColl.setDataValue("corre_rel", corre_rel);
			int count=dao.update(kColl, connection);   
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
            context.addDataField("flag", "success");
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
