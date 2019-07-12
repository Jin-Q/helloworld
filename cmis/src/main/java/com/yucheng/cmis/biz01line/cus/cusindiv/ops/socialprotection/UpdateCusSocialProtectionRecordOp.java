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

public class UpdateCusSocialProtectionRecordOp extends CMISOperation {
	

	private final String modelId = "CusSocialProtection";
	
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
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			String serno = (String)kColl.getDataValue("serno");
			
			String provid_fund_id = (String)kColl.getDataValue("provid_fund_id");//公积金账号
			String ent_provid_fund_id = (String)kColl.getDataValue("ent_provid_fund_id");//单位公积金账号
			
			String condition= "where provid_fund_id ='"+provid_fund_id+"' and ent_provid_fund_id = '"+ent_provid_fund_id+"' and serno ='"+serno+"'";
			String conditionStr = "where provid_fund_id ='"+provid_fund_id+"' and ent_provid_fund_id = '"+ent_provid_fund_id+"'";
			IndexedCollection ic = dao.queryList(modelId, condition, connection);
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			
			if(ic.size()>0){//存在相同流水号的数据
				if(iColl.size()>1){
					context.addDataField("flag", PUBConstant.EXISTS);
				}else{
					int count=dao.update(kColl, connection);
					if(count!=1){
						throw new EMPException("Update Failed! Record Count: " + count);
					}
					context.addDataField("flag", PUBConstant.SUCCESS);
				}
			}else{
				if(iColl.size()>0){
					context.addDataField("flag", PUBConstant.EXISTS);
				}else{
					int count=dao.update(kColl, connection);
					if(count!=1){
						throw new EMPException("Update Failed! Record Count: " + count);
					}
					context.addDataField("flag", PUBConstant.SUCCESS);
				}
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
