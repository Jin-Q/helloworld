package com.yucheng.cmis.biz01line.cus.cusindiv.ops.socialprotection;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class AddCusSocialProtectionRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusSocialProtection";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			String provid_fund_id = (String)kColl.getDataValue("provid_fund_id");//公积金账号
			String ent_provid_fund_id = (String)kColl.getDataValue("ent_provid_fund_id");//单位公积金账号
			
			String conditionStr = "where provid_fund_id ='"+provid_fund_id+"' and ent_provid_fund_id = '"+ent_provid_fund_id+"'";
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			if(iColl.size()>0){
				context.addDataField("flag", PUBConstant.EXISTS);
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
