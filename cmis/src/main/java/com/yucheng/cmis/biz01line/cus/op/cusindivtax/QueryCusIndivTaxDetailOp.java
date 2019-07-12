package com.yucheng.cmis.biz01line.cus.op.cusindivtax;

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

public class QueryCusIndivTaxDetailOp  extends CMISOperation {
	
	private final String modelId = "CusIndivTax";

	private final String cus_id_name = "cus_id";
	private final String indiv_taxes_name = "indiv_taxes";
	private final String serno_name = "serno";
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

			String indiv_taxes_value = null;
			try {
				indiv_taxes_value = (String)context.getDataValue(indiv_taxes_name);
			} catch (Exception e) {}
			if(indiv_taxes_value == null || indiv_taxes_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+indiv_taxes_name+"] cannot be null!");

			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cus_id",cus_id_value);
			pkMap.put("indiv_taxes",indiv_taxes_value);
			pkMap.put("serno",serno_value);
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
