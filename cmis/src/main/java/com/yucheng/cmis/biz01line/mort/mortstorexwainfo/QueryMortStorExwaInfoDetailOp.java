package com.yucheng.cmis.biz01line.mort.mortstorexwainfo;

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

public class QueryMortStorExwaInfoDetailOp  extends CMISOperation {
	
	private final String modelId = "MortStorExwaInfo";
	private final String modelId1 = "MortStorExwaDetail";
	private final String serno_name = "serno";
	
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
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection ic = dao.queryList(modelId1,"where serno = '"+serno_value+"'", connection);
			KeyedCollection kCollDetail = null;
			String warrantNoStr = "";
			String warrantTypeNoStr = "";
			for(int i=0;i<ic.size();i++){
				kCollDetail = (KeyedCollection) ic.get(i);
				warrantNoStr = warrantNoStr+(String) kCollDetail.getDataValue("warrant_no")+",";
				warrantTypeNoStr = warrantTypeNoStr+(String) kCollDetail.getDataValue("warrant_type")+",";
			}
			context.addDataField("warrantNoStr",warrantNoStr);
			context.addDataField("warrantTypeNoStr",warrantTypeNoStr);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			//机构的翻译
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
			SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "manager_id" });
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
