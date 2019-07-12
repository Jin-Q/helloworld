package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpappdepotagr;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpAppDepotAgrDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpAppDepotAgr";

	private final String depot_agr_no_name = "depot_agr_no";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String depot_agr_no_value = null;
			String serno = null;
			try {
				depot_agr_no_value = (String)context.getDataValue(depot_agr_no_name);
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(depot_agr_no_value == null || depot_agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+depot_agr_no_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("serno", serno);
			map.put("depot_agr_no", depot_agr_no_value);
			KeyedCollection kColl = dao.queryDetail(modelId, map, connection);
			//详细信息翻译时调用			
			String[] args=new String[] { "cus_id" };
		    String[] modelIds=new String[]{"CusBase"};
		    String[] modelForeign=new String[]{"cus_id"};
		    String[] fieldName=new String[]{"cus_name"};
            SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			//翻译登记人、登记机构相关信息
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
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
