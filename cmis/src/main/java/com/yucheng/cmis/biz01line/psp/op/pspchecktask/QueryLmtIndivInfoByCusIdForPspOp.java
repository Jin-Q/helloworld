package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class QueryLmtIndivInfoByCusIdForPspOp extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String cus_id = null;
		KeyedCollection kColl = new KeyedCollection("LmtIndivInfo");
		try{
			connection = this.getConnection(context);
			
			cus_id = (String) context.getDataValue("cus_id");
			
			//获取授信循环额度信息
			KeyedCollection lmtCirKColl = (KeyedCollection) SqlClient.queryFirst("queryLmtCirCrdAmtByCusId", cus_id, null, connection);
			
			//获取授信一次性额度信息
			KeyedCollection lmtOnceKColl = (KeyedCollection) SqlClient.queryFirst("queryLmtOnceCrdAmtByCusId", cus_id, null, connection);
			
			//获取贷款信息
			KeyedCollection accKColl = (KeyedCollection) SqlClient.queryFirst("queryAccInfoByCusId", cus_id, null, connection);
			
			if(lmtCirKColl!=null){
				kColl.addDataField("cir_crd_amt", lmtCirKColl.getDataValue("cir_crd_amt"));
			}
			if(lmtOnceKColl!=null){
				kColl.addDataField("once_crd_amt", lmtOnceKColl.getDataValue("once_crd_amt"));
			}
			if(accKColl!=null){
				kColl.addDataField("loan_amt", accKColl.getDataValue("loan_amt"));
				kColl.addDataField("loan_balance", accKColl.getDataValue("loan_balance"));
				kColl.addDataField("owe_int", accKColl.getDataValue("owe_int"));
			}
			
			this.putDataElement2Context(kColl, context);
			
			
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
