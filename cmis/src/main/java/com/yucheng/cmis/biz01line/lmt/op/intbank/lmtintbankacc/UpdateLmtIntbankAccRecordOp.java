package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankacc;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateLmtIntbankAccRecordOp extends CMISOperation {
	 
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
			TableModelDAO dao = this.getTableModelDAO(context);
			String forze_amt = kColl.getDataValue("froze_amt").toString();//当前输入的冻结金额
			String serno  = kColl.getDataValue("serno").toString();
			KeyedCollection kCollList = dao.queryAllDetail(modelId, serno, connection);
			String Lsamt =(String)kCollList.getDataValue("froze_amt");//原有已经被冻结金额
			if(!"".equals(Lsamt) && Lsamt!=null)
			{
				float forze = Float.parseFloat(forze_amt);
				float amt = Float.parseFloat(Lsamt);
				forze = forze+amt;
				kColl.setDataValue("froze_amt", forze);
			}
			
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
