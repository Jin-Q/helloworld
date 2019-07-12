package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpcommoprovider;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddIqpCommoProviderOp extends CMISOperation {
	
	private final String modelId = "IqpCommoProvider";
	
	private boolean updateCheck = true;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			KeyedCollection kColl = new KeyedCollection("IqpCommoProvider");

			kColl.addDataField("status", "01"); //初始状态为“已登记”
			kColl.addDataField("input_id",context.getDataValue("currentUserId"));
			kColl.addDataField("input_br_id",context.getDataValue("organNo"));
			kColl.addDataField("input_date",context.getDataValue("OPENDAY"));
			
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "input_id"});
			
			context.addDataField("operate", "addIqpCommoProviderRecord.do");
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
