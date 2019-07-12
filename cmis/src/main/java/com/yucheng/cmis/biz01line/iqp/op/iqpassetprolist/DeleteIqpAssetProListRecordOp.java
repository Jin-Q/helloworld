package com.yucheng.cmis.biz01line.iqp.op.iqpassetprolist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteIqpAssetProListRecordOp extends CMISOperation {

	private final String modelId = "IqpAssetProList";
	

	private final String serno_name = "serno";
	private final String bill_no_name = "bill_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



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
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
			//同时更新申请表
			SqlClient.update("updateIqpAssetProApp", serno_value, null, null, connection);

			/*根据流水号查询该批次证劵化资产清单中单笔资产到期最晚时间       2014-08-21   王青   start*/
			String conditionStr = " where bill_no = '"+bill_no_value+"'";
			KeyedCollection kCollAccLoan = dao.queryFirst("IqpAssetProList", null, conditionStr, connection);
			String cont_no = (String) kCollAccLoan.getDataValue("cont_no");
			conditionStr = " where cont_no = '"+cont_no+"'";
			IndexedCollection iColl = dao.queryList("AccLoan", null, conditionStr, connection);
			String end_date = "";
			for(int i = 0;i<iColl.size();i++){
				KeyedCollection kCollList = (KeyedCollection) iColl.get(i);
				String end_date_t = (String) kCollList.getDataValue("end_date");
				int result = end_date.compareTo(end_date_t);
				if(result < 0){
					end_date = end_date_t;
				}
			}
			conditionStr = " where serno = '"+serno_value+"'";
			KeyedCollection kCollApp = dao.queryFirst("IqpAssetProApp", null, conditionStr, connection);
			kCollApp.put("end_date", end_date);
			dao.update(kCollApp, connection);
			/*根据流水号查询该批次证劵化资产清单中单笔资产到期最晚时间       2014-08-21   王青   end*/
			
			
			context.addDataField("flag", "success");
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
