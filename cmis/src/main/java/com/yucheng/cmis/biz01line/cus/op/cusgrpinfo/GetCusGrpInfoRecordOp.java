package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class GetCusGrpInfoRecordOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		KeyedCollection kColl = null;
		String grp_no = "";
		try{
	
			connection = this.getConnection(context);
			kColl = new KeyedCollection("CusGrpInfo");

		    grp_no = CMISSequenceService4JXXD.querySequenceFromDB("CUSGRP", "fromDate", 
	        		connection, context);
			
		    String mainBrId = (String)context.getDataValue("organNo");
		    String currentUserName = (String)context.getDataValue("currentUserId");
		    
		    if(!kColl.containsKey("main_br_id")) {
		    	kColl.addDataField("input_br_id", mainBrId);
		    	kColl.addDataField("input_user_id", currentUserName);
		    }
		    
			kColl.addDataField("grp_no", grp_no);
			
			SInfoUtils.addSOrgName(kColl, new String[]{ "input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{ "input_user_id"});
			
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
