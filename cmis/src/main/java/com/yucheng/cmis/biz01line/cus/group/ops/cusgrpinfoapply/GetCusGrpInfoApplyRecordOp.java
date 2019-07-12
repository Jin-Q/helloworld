package com.yucheng.cmis.biz01line.cus.group.ops.cusgrpinfoapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class GetCusGrpInfoApplyRecordOp extends CMISOperation{

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		KeyedCollection kColl = null;
		String grp_no = "";
		String serno = "";
		try{
	
			connection = this.getConnection(context);
			kColl = new KeyedCollection("CusGrpInfoApply");

		    grp_no = CMISSequenceService4JXXD.querySequenceFromDB("CUSGRP", "fromDate", 
	        		connection, context);
		    
		    serno = CMISSequenceService4JXXD.querySequenceFromDB("GLKHSQ", "fromDate", 
	        		connection, context);
			
		    String mainBrId = (String)context.getDataValue("organNo");
		    String currentUserName = (String)context.getDataValue("currentUserId");
		    
		    if(!kColl.containsKey("main_br_id")) {
		    	kColl.addDataField("main_br_id", mainBrId);
		    	kColl.addDataField("cus_manager", currentUserName);
		    	kColl.addDataField("input_br_id", mainBrId);
		    	kColl.addDataField("input_user_id", currentUserName);
		    }
		    
			kColl.addDataField("grp_no", grp_no);
			kColl.addDataField("serno", serno);
			
			SInfoUtils.addSOrgName(kColl, new String[]{"main_br_id", "input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"cus_manager", "input_user_id"});
			
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
