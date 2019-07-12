package com.yucheng.cmis.biz01line.ccr.op.ccrratdirect;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddCcrRatDirectRecordDetailOp  extends CMISOperation{
	private final String modelId = "CcrAppInfo";
	
	public String doExecute(Context context) throws EMPException {
			
		Connection connection = null;
		String serNo = ""; 
		String day = ""; 
		try{
			connection = this.getConnection(context);
			
			KeyedCollection ccr = new KeyedCollection(modelId);
	
			//serNo=CMISSequenceService4JXXD.querySequenceFromDB("CREDIT", "fromDate", 
				//	connection, context);
			
            day = this.getOpenDay(context);
            
            ccr.addDataField("serno", serNo);
//			ccr.addDataField("manager_id", context.getDataValue(PUBConstant.currentUserId));
			ccr.addDataField("input_id", context.getDataValue(PUBConstant.currentUserId));
			ccr.addDataField("input_br_id", context.getDataValue(PUBConstant.organNo));

			ccr.addDataField("input_date", day);
			
            SInfoUtils.addUSerName(ccr, new String[] { "input_id" });
            SInfoUtils.addSOrgName(ccr, new String[] { "input_br_id" });
            
            this.putDataElement2Context(ccr, context);
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
