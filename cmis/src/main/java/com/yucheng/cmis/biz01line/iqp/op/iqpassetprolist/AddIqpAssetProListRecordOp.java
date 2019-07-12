package com.yucheng.cmis.biz01line.iqp.op.iqpassetprolist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class AddIqpAssetProListRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpAssetProList";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
			//同时更新申请表
			String serno = (String) kColl.getDataValue("serno");
			SqlClient.update("updateIqpAssetProApp", serno, null, null, connection);
			
			
			/*根据流水号查询该批次证劵化资产清单中单笔资产到期最晚时间       2014-08-21   王青   start*/
			String cont_no = (String) kColl.getDataValue("cont_no");
			String conditionStr = " where cont_no = '"+cont_no+"'";
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
			conditionStr = " where serno = '"+serno+"'";
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
