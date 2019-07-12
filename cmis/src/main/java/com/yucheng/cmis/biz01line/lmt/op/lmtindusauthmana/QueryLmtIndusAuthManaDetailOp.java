package com.yucheng.cmis.biz01line.lmt.op.lmtindusauthmana;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryLmtIndusAuthManaDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtIndusAuthMana";
	

	private final String agr_no_name = "agr_no";
	private final String input_br_id_name = "input_br_id";
	private final String guar_type_name = "guar_type";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String agr_no_value = null;
			try {
				agr_no_value = (String)context.getDataValue(agr_no_name);
			} catch (Exception e) {}
			if(agr_no_value == null || agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+agr_no_name+"] cannot be null!");

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
			pkMap.put("agr_no",agr_no_value);
			pkMap.put("input_br_id",input_br_id_value);
			pkMap.put("guar_type",guar_type_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
			this.putDataElement2Context(kColl, context);
			
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
