package com.yucheng.cmis.biz01line.cus.op.cusblklist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddCusBlkListRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusBlkListTemp";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			serno = CMISSequenceService4JXXD.querySequenceFromDB("CUS", "all", connection, context);
			kColl.setDataValue("status", "001");
			kColl.setDataValue("serno", serno);
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String cert_type = (String)kColl.getDataValue("cert_type");
			String cert_code = (String)kColl.getDataValue("cert_code");
			
			String conditionBase = "where cert_type ='"+cert_type+"' and cert_code ='"+cert_code+"'";
			IndexedCollection iCollBase = dao.queryList("CusBase", conditionBase, connection);
			if(iCollBase.size()>0){
				context.addDataField("flag", PUBConstant.EXISTS);
				return "";
			}
			String conditionStr = "where cert_type ='"+cert_type+"' and cert_code ='"+cert_code+"' and status in('001','002','004')";
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			if(iColl.size()>0){
				context.addDataField("flag", PUBConstant.FAIL);
			}else{
				dao.insert(kColl, connection);
				context.addDataField("flag", PUBConstant.SUCCESS);
			}
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
