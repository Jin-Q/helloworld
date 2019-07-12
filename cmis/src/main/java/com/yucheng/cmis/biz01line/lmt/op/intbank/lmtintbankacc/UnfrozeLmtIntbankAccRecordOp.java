package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankacc;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UnfrozeLmtIntbankAccRecordOp extends CMISOperation {
	 
	private final String modelId = "LmtIntbankAcc";

 
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			String froze_amt=(String)kColl.getDataValue("froze_amt");//冻结的额度
			String unfroze_amt = (String)kColl.getDataValue("unfroze_amt");//输入的解冻额度
			if(!froze_amt.equals("")&&!unfroze_amt.equals("")){
				float froze = Float.parseFloat(froze_amt);
				float unfroze = Float.parseFloat(unfroze_amt);				
				froze = froze - unfroze;
				kColl.setDataValue("froze_amt", froze);
			}else{
				kColl.setDataValue("froze_amt", "0");
			}			 
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Record Count: " + count);
			}
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
