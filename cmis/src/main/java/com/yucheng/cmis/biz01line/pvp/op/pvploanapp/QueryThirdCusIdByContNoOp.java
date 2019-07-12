package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class QueryThirdCusIdByContNoOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cusId = "";
		try{
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement("PvpLoanApp"); 
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0){
				throw new EMPJDBCException("The values cannot be empty!");
			}
			String contNo = (String) kColl.getDataValue("cont_no");//获取合同编号
			KeyedCollection relateCusInfo = (KeyedCollection)SqlClient.queryFirst("getThirdCusIdByContNo", contNo, null, connection);
			if(relateCusInfo!= null && relateCusInfo.size()> 0){
				cusId = relateCusInfo.getDataValue("cus_id").toString().trim();	
				if(cusId !=null && !"".equals(cusId)){
					context.addDataField("flag", "success");
					context.addDataField("msg","");
					context.addDataField("cusId", cusId);
					EMPLog.log("QueryThirdCusIdByContNoOp", EMPLog.INFO, 0, "【第三方客户码】获取成功...", null);
				}			
			}else{
				context.addDataField("flag", "failed");
				context.addDataField("msg", "额度信息未关联第三方授信额度!");
				context.addDataField("cusId", "");
				EMPLog.log("QueryThirdCusIdByContNoOp", EMPLog.INFO, 0, "【第三方客户码】无此第三方客户码...", null);
			}
				
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
