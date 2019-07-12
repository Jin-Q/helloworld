package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddIqpCargoOverseeReRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpCargoOverseeRe";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			KeyedCollection kColl = new KeyedCollection(modelId);	
			//生成押品编号
			String guarantyNo = CMISSequenceService4JXXD.querySequenceFromDB("MT", "fromDate", connection, context);
			kColl.addDataField("guaranty_no", guarantyNo);
			this.putDataElement2Context(kColl, context);
		} catch (EMPException ee) {
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
