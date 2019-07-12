package com.yucheng.cmis.biz01line.mort.mortaccountsreceivable;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddMortAccountsReceivableRecordLeadOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortAccountsReceivable";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String debt_id = "";
		String guaranty_no = "";
		try {
			guaranty_no = (String) context.getDataValue("guaranty_no");
			KeyedCollection kColl = new KeyedCollection(modelId);
			debt_id = CMISSequenceService4JXXD.querySequenceFromDB("MT", "fromDate", connection, context);
		    kColl.addDataField("debt_id", debt_id);
		    kColl.addDataField("guaranty_no", guaranty_no);
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
