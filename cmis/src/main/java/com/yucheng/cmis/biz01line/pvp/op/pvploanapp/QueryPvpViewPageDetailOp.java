package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryPvpViewPageDetailOp  extends CMISOperation {
	
	private final String modelId = "PvpLoanApp";
	private final String serno_name = "pvpSerno";
		
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		
		try{
			connection = this.getConnection(context);
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeQueryRestrict(this.modelId, null, context, connection);

			String serno_value = null;
			try {
				if(context.containsKey(serno_name)){  
					serno_value = (String)context.getDataValue(serno_name);
				}	
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");	
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			String prd_id = (String)kColl.getDataValue("prd_id");
			String cont_no = (String)kColl.getDataValue("cont_no");
			context.addDataField("prd_id", prd_id);
			context.addDataField("cont_no", cont_no);
			
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
