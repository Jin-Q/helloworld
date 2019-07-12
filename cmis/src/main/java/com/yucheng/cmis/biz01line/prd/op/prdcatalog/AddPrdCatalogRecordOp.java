package com.yucheng.cmis.biz01line.prd.op.prdcatalog;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddPrdCatalogRecordOp extends CMISOperation {
	
	private final String modelId = "PrdCatalog";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				kColl.addDataField("catalogtype", "STD_ZB_CATALOG_TYPE");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			/**
			 * 获取产品目录编号生成规则：
			 * PRD+年月日+（3为编号）
			 */
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("PRDSQ", "fromDate", connection, context);
			kColl.setDataValue("catalogid", serno);
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
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
