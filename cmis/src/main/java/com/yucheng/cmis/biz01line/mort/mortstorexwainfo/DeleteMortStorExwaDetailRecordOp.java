package com.yucheng.cmis.biz01line.mort.mortstorexwainfo;

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
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteMortStorExwaDetailRecordOp extends CMISOperation {

	private final String modelId = "MortStorExwaDetail";
	

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String serno_value = null;
			String warrant_no = null;
			String warrant_type = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				warrant_no = URLDecoder.decode((String) context.getDataValue("warrant_no"),"UTF-8");
				warrant_type = URLDecoder.decode((String) context.getDataValue("warrant_type"),"UTF-8");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> map = new HashMap<String,String>();
			map.put("serno",serno_value);
			map.put("warrant_no",warrant_no);
			map.put("warrant_type",warrant_type);
			int count=dao.deleteByPks(modelId, map, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			/***剔除出库申请中权证信息将状态改为“在库” add by tangzf 2014.04.25********/
			KeyedCollection kCollParam = new KeyedCollection();
			kCollParam.put("warrant_no", warrant_no);
			kCollParam.put("warrant_type",warrant_type);
			SqlClient.update("updateMortCertiStatus", kCollParam, "3", null, connection);
			context.addDataField("flag","success");
			
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
