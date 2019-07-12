package com.yucheng.cmis.biz01line.iqp.op.iqpexpinfo;

import java.net.URLDecoder;
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

public class QueryIqpExpInfoDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpExpInfo";
	

	private final String po_no_name = "po_no";
	private final String express_no_name = "express_no";
	private final String invc_no_name = "invc_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String po_no_value = null;
			try {
				po_no_value = (String)context.getDataValue(po_no_name);
			} catch (Exception e) {}
			if(po_no_value == null || po_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+po_no_name+"] cannot be null!");

			String express_no_value = null;
			try {
				express_no_value = (String)context.getDataValue(express_no_name);
			} catch (Exception e) {}
			if(express_no_value == null || express_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+express_no_name+"] cannot be null!");

			String invc_no_value = null;
			try {
				invc_no_value = (String)context.getDataValue(invc_no_name);
			} catch (Exception e) {}
			if(invc_no_value == null || invc_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+invc_no_name+"] cannot be null!");

			//中文转码
			express_no_value = URLDecoder.decode(express_no_value,"UTF-8");
			invc_no_value = URLDecoder.decode(invc_no_value,"UTF-8");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("po_no",po_no_value);
			pkMap.put("express_no",express_no_value);
			pkMap.put("invc_no",invc_no_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
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
