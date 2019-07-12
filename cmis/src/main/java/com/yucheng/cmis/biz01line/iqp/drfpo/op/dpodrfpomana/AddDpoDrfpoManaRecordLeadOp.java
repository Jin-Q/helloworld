package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpodrfpomana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddDpoDrfpoManaRecordLeadOp extends CMISOperation {
	
	private final String modelId = "IqpDrfpoMana";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String drfpo_no ="";
		try{
			connection = this.getConnection(context);
			//从context中取出sequenceService
			drfpo_no = CMISSequenceService4JXXD.querySequenceFromDB("PJC", "all", connection, context);
			KeyedCollection kColl = new KeyedCollection(modelId);
			kColl.addDataField("drfpo_no", drfpo_no);
			kColl.addDataField("input_id",context.getDataValue("currentUserId"));
			kColl.addDataField("input_br_id",context.getDataValue("organNo"));
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
			kColl.addDataField("input_date", context.getDataValue("OPENDAY"));
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
