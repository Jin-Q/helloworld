package com.yucheng.cmis.biz01line.prd.op.prdbasicinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddPrdBasicinfoRecordOp extends CMISOperation {
	
	private final String modelId = "PrdBasicinfo";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			context.addDataField("flag", "failed");
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			/**
			 * 生成产品编号，生成规则：
			 * 产品编号设置：两位标识位+4位编号
			 * 贷款类：10
			 * 银承：20
			 * 贴现：30
			 * 保函：40 
			 * 贸易融资：50 
			 * 资产转让：60 
			 * 信用证：70 
			 * 保理：80
			 */
			String prdKind = (String)kColl.getDataValue("prdkind");
			String serno = prdKind+CMISSequenceService4JXXD.querySequenceFromDB(prdKind, "fromDate", connection, context);
			kColl.addDataField("prdid", serno);
		
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			context.setDataValue("flag", "success");
			context.addDataField("prdid", serno);
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
