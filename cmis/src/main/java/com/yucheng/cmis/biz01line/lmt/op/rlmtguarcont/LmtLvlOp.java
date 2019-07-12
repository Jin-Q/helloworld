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

public class LmtLvlOp extends CMISOperation {
	private final String modelId = "RLmtAppGuarCont";    

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String limit_code = "";
			String limit_code_up = "";
			String guar_cont_no = "";
			String guar_cont_no_up = "";
			String serno = "";
			String serno_up = "";
			try {
				limit_code = (String)context.getDataValue("limit_code");
				guar_cont_no = (String)context.getDataValue("guar_cont_no");
				serno = (String)context.getDataValue("serno");
				limit_code_up = (String)context.getDataValue("limit_code_up");
				guar_cont_no_up = (String)context.getDataValue("guar_cont_no_up"); 
				serno_up = (String)context.getDataValue("serno_up"); 
			} catch (Exception e) {}
			if(serno_up == null || "".equals(serno_up) || serno == null || "".equals(serno) || limit_code == null ||  "".equals(limit_code) || limit_code_up == null || "".equals(limit_code_up) || guar_cont_no == null || "".equals(guar_cont_no) || guar_cont_no_up == null || "".equals(guar_cont_no_up))
				throw new EMPJDBCException("The values cannot be empty!"); 
			
			TableModelDAO dao = this.getTableModelDAO(context); 
			Map<String,String> map = new HashMap<String,String>();
			Map<String,String> mapUp = new HashMap<String,String>();
			map.put("limit_code", limit_code);
			map.put("serno", serno);
			map.put("guar_cont_no", guar_cont_no);
			mapUp.put("limit_code", limit_code_up); 
			mapUp.put("serno", serno_up);
			mapUp.put("guar_cont_no", guar_cont_no_up);
  /**担保等级互换**/ 
			KeyedCollection kColl = dao.queryDetail(modelId, map, connection);
			KeyedCollection upKColl = dao.queryDetail(modelId, mapUp, connection);
			String guar_lvl = (String)kColl.getDataValue("guar_lvl");  
			String up_guar_lvl = (String)upKColl.getDataValue("guar_lvl");
			kColl.setDataValue("guar_lvl", up_guar_lvl);
			upKColl.setDataValue("guar_lvl", guar_lvl);
  /**保存互换后的数据**/			
			int count=dao.update(kColl, connection);
			int upCount=dao.update(upKColl, connection);
			if(count!=1 && upCount!=1){ 
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
