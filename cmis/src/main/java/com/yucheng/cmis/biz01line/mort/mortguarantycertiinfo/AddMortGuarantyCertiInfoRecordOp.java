package com.yucheng.cmis.biz01line.mort.mortguarantycertiinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddMortGuarantyCertiInfoRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortGuarantyCertiInfo";
	
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
			//生成核心流水号
			String hxSerno = CMISSequenceService4JXXD.querySequenceFromDB("HXSERNO", "fromDate", connection, context);
			if(kColl.containsKey("guaranty_no")){
				hxSerno = (String)kColl.getDataValue("guaranty_no") + hxSerno;
			}else{
				throw new EMPJDBCException("押品编号为空！");
			}
			kColl.put("hx_serno", hxSerno);
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			context.addDataField("flag","success");
			
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
