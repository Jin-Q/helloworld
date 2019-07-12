package com.yucheng.cmis.biz01line.acc.op.acctrantrustcompany;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2014-12-31
*@description 需求编号：【XD141204082】新增台账信托贷款贷款明细，获取上一付款日日期
*@version 
*
 */
public class PrepareAccTranTrustCompanyInfoOp extends CMISOperation {
	private final String modelId = "AccTranTrustCompany";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String cont_no_value = "";
			String bill_no_value = "";
			String last_pay_date ="";
			try {
				bill_no_value = (String)context.getDataValue("bill_no");
				cont_no_value = (String)context.getDataValue("cont_no");
			} catch (Exception e) {}
			if(bill_no_value == null || bill_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+bill_no_value+"] cannot be null!");
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of ["+cont_no_value+"] cannot be null!");
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition ="WHERE CONT_NO ='"+cont_no_value+"' AND BILL_NO ='"+bill_no_value
						+"' AND RECLAIM_MODE ='PI' AND TRADE_STATUS ='1' ORDER BY TRAN_DATE DESC";
			IndexedCollection iColl = dao.queryList(modelId, null, condition, connection);
			
			if(iColl!=null && iColl.size() >0){
				KeyedCollection kColl = (KeyedCollection) iColl.get(0);
				last_pay_date = kColl.getDataValue("tran_date").toString().trim();
				context.addDataField("last_pay_date", last_pay_date);
			}else{
				KeyedCollection accTranTC = dao.queryDetail("AccLoan", bill_no_value, connection);
				last_pay_date = accTranTC.getDataValue("distr_date").toString().trim();
				context.addDataField("last_pay_date", last_pay_date);
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
