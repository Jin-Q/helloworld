package com.yucheng.cmis.biz01line.cus.cusindiv.ops.holdchrem;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetCusHoldChremRecordOp extends CMISOperation {
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		KeyedCollection kColl = null;
		try{
			connection = this.getConnection(context);
			kColl = new KeyedCollection("CusHoldChrem");

		    String serno = CMISSequenceService4JXXD.querySequenceFromDB("HOLDCHR", "fromDate", connection, context);
			
		    String cusid = (String)context.getDataValue("CusHoldChrem.cus_id");
		    
		    String inputbrid = (String)context.getDataValue("organNo");
		    String inputid = (String)context.getDataValue("currentUserId");
		    
		    kColl.addDataField("cus_id", cusid);
		    kColl.addDataField("input_br_id", inputbrid);
		    kColl.addDataField("input_id", inputid);
			kColl.addDataField("serno", serno);
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			SInfoUtils.addSOrgName(kColl, new String[]{ "input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{ "input_id"});
			
			this.putDataElement2Context(kColl, context);
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
