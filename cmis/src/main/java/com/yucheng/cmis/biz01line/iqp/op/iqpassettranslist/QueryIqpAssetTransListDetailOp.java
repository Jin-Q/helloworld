package com.yucheng.cmis.biz01line.iqp.op.iqpassettranslist;

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
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpAssetTransListDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpAssetTransList";
	private final String appmodelId = "IqpAssetRegiApp";

	private final String serno_name = "serno";
	private final String bill_no_name = "bill_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			String bill_no_value = null;
			try {
				bill_no_value = (String)context.getDataValue(bill_no_name);
			} catch (Exception e) {}
			if(bill_no_value == null || bill_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+bill_no_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("serno",serno_value);
			pkMap.put("bill_no",bill_no_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			
			String condition = " where serno = (select serno from iqp_asset_regi where asset_status = '01' and bill_no = '"+bill_no_value+"')";
			KeyedCollection appkcoll = dao.queryFirst(appmodelId, null, condition, connection);
			appkcoll.put("serno", serno_value);
			appkcoll.put("bill_no", bill_no_value);
			appkcoll.put("trans_rate", kColl.getDataValue("trans_rate"));
			appkcoll.put("trans_amt", kColl.getDataValue("trans_amt"));
			appkcoll.setName(modelId);
			
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(appkcoll, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			this.putDataElement2Context(appkcoll, context);
			
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
