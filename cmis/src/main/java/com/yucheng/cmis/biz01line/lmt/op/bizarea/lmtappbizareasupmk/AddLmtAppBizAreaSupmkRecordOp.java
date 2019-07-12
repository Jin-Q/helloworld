package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappbizareasupmk;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddLmtAppBizAreaSupmkRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAppBizAreaSupmk";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = new KeyedCollection(modelId);
			
			String serno = (String) context.getDataValue( "serno" );
			
			String supmk_serno = CMISSequenceService4JXXD.querySequenceFromDB("bizAreaComnSerno", "fromDate", connection, context);
			
			kColl.addDataField("serno", serno);
			kColl.addDataField("supmk_serno", supmk_serno);
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
			context.addDataField("flag", "suc");
			
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
