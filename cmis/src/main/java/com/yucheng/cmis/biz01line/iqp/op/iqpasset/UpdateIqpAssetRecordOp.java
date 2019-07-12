package com.yucheng.cmis.biz01line.iqp.op.iqpasset;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateIqpAssetRecordOp extends CMISOperation {
	private final String modelId = "IqpAsset";
	private final String relmodelId = "IqpAssetRel";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);

			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			context.addDataField("flag", "success");
			
//			String asset_no = (String) kColl.getDataValue("asset_no");
//			String condition = " where asset_no = '"+asset_no+"'";
//			IndexedCollection iColl = dao.queryList(relmodelId, condition, connection);
//			BigDecimal asset_total_amt = new BigDecimal("0");
//			BigDecimal takeover_total_amt = new BigDecimal("0");
//			for(int i=0;i<iColl.size();i++){
//				KeyedCollection kc = (KeyedCollection) iColl.get(i);
//				BigDecimal loan_amt = new BigDecimal(kc.getDataValue("loan_amt")+"");
//				asset_total_amt.add(loan_amt);
//				BigDecimal takeover_amt = new BigDecimal(kc.getDataValue("takeover_amt")+"");
//				takeover_total_amt.add(takeover_amt);
//			}
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
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
