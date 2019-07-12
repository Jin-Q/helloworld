package com.yucheng.cmis.biz01line.lmt.op.lmtappfinsubpay;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddLmtAppFinSubpayRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAppFinSubpay";
	
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
			
			//生成流水号
			String main_br_id = (String) context.getDataValue("organNo");
			String serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ","all", main_br_id, connection, context);
//			String cus_id = kColl.getDataValue("cus_id").toString();
//			KeyedCollection kCollTemp = new KeyedCollection(modelId);
//			kCollTemp = dao.queryFirst(modelId, null, "where cus_id ='" + cus_id + "'", connection);
//			if(kCollTemp==null||kCollTemp.getDataValue("serno") == null || kCollTemp.getDataValue("serno").equals("")){
//				kColl.setDataValue("serno", serno);
//				dao.insert(kColl, connection);
//				context.addDataField("serno", serno);
//			}else{
//				kColl.setDataValue("serno", kCollTemp.getDataValue("serno"));
//				dao.update(kColl, connection);
//				context.addDataField("serno", kCollTemp.getDataValue("serno"));
//			}
			
			kColl.setDataValue("serno", serno);
			dao.insert(kColl, connection);
			context.addDataField("serno", serno);
			context.addDataField("flag", "success");
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
