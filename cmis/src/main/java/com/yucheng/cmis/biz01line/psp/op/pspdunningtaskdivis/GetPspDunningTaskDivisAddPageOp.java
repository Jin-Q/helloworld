package com.yucheng.cmis.biz01line.psp.op.pspdunningtaskdivis;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class GetPspDunningTaskDivisAddPageOp  extends CMISOperation {
	
	private final String modelId = "PspDunningTaskDivis";
	
//	private final String serno_name = "serno";
	
//	private boolean updateCheck = true;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
//			if(this.updateCheck){
//				RecordRestrict recordRestrict = this.getRecordRestrict(context);
//				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
//			}
			
//			String serno_value = null;
//			try {
//				serno_value = (String)context.getDataValue(serno_name);
//			} catch (Exception e) {}
//			if(serno_value == null || serno_value.length() == 0)
//				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
//
//			TableModelDAO dao = this.getTableModelDAO(context);
//			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
//			
//			String[] args=new String[] { "cus_id" };
//			String[] modelIds=new String[]{"CusBase"};
//			String[] modelForeign=new String[]{"cus_id"};
//			String[] fieldName=new String[]{"cus_name"};
//			//详细信息翻译时调用			
//			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
//			SInfoUtils.addSOrgName(kColl, new String[] { "exe_br_id","divis_br_id","input_br_id" });
//			SInfoUtils.addUSerName(kColl, new String[] { "exe_id","divis_id","input_id" });
//			this.putDataElement2Context(kColl, context);
			
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
