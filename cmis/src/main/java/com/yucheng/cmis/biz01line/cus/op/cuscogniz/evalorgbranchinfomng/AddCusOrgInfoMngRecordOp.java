package com.yucheng.cmis.biz01line.cus.op.cuscogniz.evalorgbranchinfomng;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddCusOrgInfoMngRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusOrgInfoMng";
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
			
			String cus_id = kColl.getDataValue("cus_id").toString();
			KeyedCollection kCollCusCom = dao.queryDetail("CusBase", cus_id, connection);			
			String manager_br_id = kCollCusCom.getDataValue("main_br_id").toString();
			String serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", manager_br_id, connection, context);
			kColl.setDataValue("serno", serno);
			
			dao.insert(kColl, connection);
			context.addDataField("flag", PUBConstant.SUCCESS);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
