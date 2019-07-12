package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpcoreconnet;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpCoreConNetRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpCoreConNet";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			String serno_value = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);//业务流水号
			String net_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("NET", "all", connection, context);//网络协议编号
			kColl.setDataValue("net_agr_no", net_agr_no);
			kColl.setDataValue("serno", serno_value);
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			context.addDataField("flag", PUBConstant.SUCCESS);
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
