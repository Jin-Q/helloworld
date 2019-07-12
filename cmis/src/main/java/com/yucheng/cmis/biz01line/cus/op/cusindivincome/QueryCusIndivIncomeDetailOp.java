package com.yucheng.cmis.biz01line.cus.op.cusindivincome;

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

public class QueryCusIndivIncomeDetailOp  extends CMISOperation {
	
	private final String modelId = "CusIndivIncome";

	private final String cus_id_name = "cus_id";
	private final String indiv_sur_year_name = "indiv_sur_year";
	private final String indiv_deposits_name = "indiv_deposits";
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");

			String indiv_sur_year_value = null;
			try {
				indiv_sur_year_value = (String)context.getDataValue(indiv_sur_year_name);
			} catch (Exception e) {}
			if(indiv_sur_year_value == null || indiv_sur_year_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+indiv_sur_year_name+"] cannot be null!");

			String indiv_deposits_value = null;
			try {
				indiv_deposits_value = (String)context.getDataValue(indiv_deposits_name);
			} catch (Exception e) {}
			if(indiv_deposits_value == null || indiv_deposits_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+indiv_deposits_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cus_id",cus_id_value);
			pkMap.put("indiv_sur_year",indiv_sur_year_value);
			pkMap.put("indiv_deposits",indiv_deposits_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
            SInfoUtils.addUSerName(kColl, new String[]{"input_id", "last_upd_id"});
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
