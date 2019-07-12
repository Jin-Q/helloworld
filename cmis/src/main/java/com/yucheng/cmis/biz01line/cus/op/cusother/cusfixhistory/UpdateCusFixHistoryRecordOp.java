package com.yucheng.cmis.biz01line.cus.op.cusother.cusfixhistory;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.util.MD5;

public class UpdateCusFixHistoryRecordOp extends CMISOperation {
	

	private final String modelId = "CusFixHistory";
	private final String modelIdA = "CusFixAuthorize";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String flag = "";
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			String inputId = (String) kColl.getDataValue("auth_id");
			String checkCode = (String) kColl.getDataValue("checkcode");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//校验
			String openDay = (String) context.getDataValue(PUBConstant.OPENDAY);
			checkCode = MD5.encode(checkCode).toUpperCase();
			kColl.setDataValue("checkcode", checkCode);
			String condition = " WHERE auth_id = '" + inputId + "' and checkcode = '" + checkCode + "' " +
					"and '"+openDay+"' >= startdate and '"+openDay+"' <= enddate and status='1'";
			IndexedCollection iColl = dao.queryList(modelIdA, condition, connection);
			if(iColl.size() > 0){
				flag = "suc";
			}else{
				flag = "fail";
			}
			//保存
			int count = dao.insert(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			context.addDataField("flag",flag);
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
