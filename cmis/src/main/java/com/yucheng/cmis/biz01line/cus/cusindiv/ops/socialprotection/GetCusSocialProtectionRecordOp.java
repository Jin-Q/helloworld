package com.yucheng.cmis.biz01line.cus.cusindiv.ops.socialprotection;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class GetCusSocialProtectionRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusSocialProtection";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		KeyedCollection kColl = null;
		try{
			connection = this.getConnection(context);
			kColl = new KeyedCollection(modelId);

		    String serno = CMISSequenceService4JXXD.querySequenceFromDB("SOCPRO", "fromDate", connection, context);
			
		    String cusid = (String)context.getDataValue("CusSocialProtection.cus_id");
		    CusBaseComponent cusBaseComponent = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					PUBConstant.CUSBASE,context,connection);
		    String cusname = cusBaseComponent.getCusBase(cusid).getCusName();
		    
		    String inputbrid = (String)context.getDataValue("organNo");
		    String inputid = (String)context.getDataValue("currentUserId");
		    
		    kColl.addDataField("cus_id", cusid);
		    kColl.addDataField("cus_name", cusname);
		    kColl.addDataField("input_br_id", inputbrid);
		    kColl.addDataField("input_id", inputid);
			kColl.addDataField("serno", serno);
			
//			SInfoUtils.addSOrgName(kColl, new String[]{ "input_br_id"});
//			SInfoUtils.addUSerName(kColl, new String[]{ "input_id"});
			
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
