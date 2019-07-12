package com.yucheng.cmis.biz01line.qry.op.qrytemplet.qryresult;

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

public class QueryQryTempletQryResultDetailOp  extends CMISOperation {

	private final String modelId = "QryResult";


	private final String temp_no_name = "temp_no";
	private final String result_no_name = "result_no";
	

	private boolean updateCheck = false;


	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String temp_no_value = null;
			try {
				temp_no_value = (String)context.getDataValue(temp_no_name);
			} catch (Exception e) {}
			if(temp_no_value == null || temp_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+temp_no_name+"] cannot be null!");
			String result_no_value = null;
			try {
				result_no_value = (String)context.getDataValue(result_no_name);
			} catch (Exception e) {}
			if(result_no_value == null || result_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+result_no_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("temp_no",temp_no_value);
			pkMap.put("result_no",result_no_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			
			//翻译参数选项字典编号
			String[] args = new String[] { "result_title","link_temp_no" };
			String[] modelIds = new String[] { "QryParamDic","QryTemplet" };
			String[] modelForeign = new String[] { "param_dic_no","temp_no" };
			String[] fieldName = new String[] { "name","temp_name" };
			// 详细信息翻译时调用
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
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
