package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankacc;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

   /**
    * 设定日期进行终止授信
    * 
    * */

public class BreakLmtIntbankAccRecordOp extends CMISOperation {
	

	private final String modelId = "LmtIntbankAcc";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			String break_date = (String)kColl.getDataValue("break_date");//获取终止日期
			String conditionStr = "where start_date <='"+break_date+"' and lmt_status = '10'";
			IndexedCollection ic = dao.queryList(modelId, conditionStr, connection);
			if(ic != null && ic.size()>0){
				for(int i=0;i<ic.size();i++){
					KeyedCollection kc = new KeyedCollection();
					kc=(KeyedCollection) ic.get(i);
					kc.setDataValue("lmt_status", "30");
					dao.update(kc, connection);
				}
				context.addDataField("flag", PUBConstant.SUCCESS);
			}else{
				context.addDataField("flag", PUBConstant.FAIL);
			}
			
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
