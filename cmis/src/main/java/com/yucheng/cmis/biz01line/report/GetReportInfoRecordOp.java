package com.yucheng.cmis.biz01line.report;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class GetReportInfoRecordOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String rep_flag = null;
			String serno = null;
			String guaranty_no = null;
			
			/**取得Tab参数*/
			if(context.containsKey("rep_flag")){
				rep_flag = (String)context.getDataValue("rep_flag");  
			}
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");  
			}
			if(context.containsKey("guaranty_no")){
				guaranty_no = (String)context.getDataValue("guaranty_no");  
			}
			
			context.put("rep_flag", rep_flag);
			context.put("serno", serno);
			context.put("guaranty_no", guaranty_no);
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
