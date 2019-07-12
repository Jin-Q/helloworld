package com.yucheng.cmis.biz01line.cus.cushand.op.cushandoverapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryCusHandoverAppDetailOp  extends CMISOperation {
	
	private final String modelId = "CusHandoverApp";
	
	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String reflag=null;
		try{
			connection = this.getConnection(context);
		
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			
			SInfoUtils.addSOrgName(kColl, new String[]{"handover_br_id", "receiver_br_id", "supervise_br_id", "input_br_id","manager_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"handover_id", "receiver_id", "supervise_id", "input_id","manager_id"});
			
			this.putDataElement2Context(kColl, context);
			
			reflag = (String)context.getDataValue("update");
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return reflag;
	}
}
