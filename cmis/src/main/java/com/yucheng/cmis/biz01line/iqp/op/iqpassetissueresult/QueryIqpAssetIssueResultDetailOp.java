package com.yucheng.cmis.biz01line.iqp.op.iqpassetissueresult;

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

public class QueryIqpAssetIssueResultDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpAssetIssueResult";
	

	private final String serno_name = "serno";
	private final String prd_id_name = "prd_id";
	
	
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

			String prd_id_value = null;
			try {
				prd_id_value = (String)context.getDataValue(prd_id_name);
			} catch (Exception e) {}
			if(prd_id_value == null || prd_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+prd_id_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("serno",serno_value);
			pkMap.put("prd_id",prd_id_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			
			String[] args=new String[] {"prd_id" };
			String[] modelIds=new String[]{"IqpAssetPrdInfo"};
			String[]modelForeign=new String[]{"prd_id"};
			String[] fieldName=new String[]{"prd_short_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
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
