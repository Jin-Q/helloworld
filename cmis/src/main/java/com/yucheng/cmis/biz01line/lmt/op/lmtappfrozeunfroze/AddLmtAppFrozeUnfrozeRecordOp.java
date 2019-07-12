package com.yucheng.cmis.biz01line.lmt.op.lmtappfrozeunfroze;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddLmtAppFrozeUnfrozeRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAppFrozeUnfroze";
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
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			
			if(kColl.containsKey("prd_id_displayname")){
				kColl.remove("prd_id_displayname");				
			}
			SInfoUtils.getPrdPopName(kColl, "prd_id", connection);  //翻译产品
			kColl.put("serno", serno);
			dao.insert(kColl, connection);
			context.put("flag","success");
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
