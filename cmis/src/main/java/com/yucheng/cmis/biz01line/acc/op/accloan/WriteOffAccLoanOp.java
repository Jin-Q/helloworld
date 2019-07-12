package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class WriteOffAccLoanOp extends CMISOperation {

	private final String modelId = "AccLoan";
	private final String modelIdAccTranTrustCompany = "AccTranTrustCompany";
	private final String serno_name = "bill_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String serno_value = null;
			String cont_no = null;
			BigDecimal loan_balance = new BigDecimal(0);
			BigDecimal tran_amt = new BigDecimal(0);
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			//取到贷款余额
			loan_balance = BigDecimalUtil.replaceNull(kColl.getDataValue("loan_balance"));
			cont_no = (String)kColl.getDataValue("cont_no");
			//统计交易流水回收金额
			String condition = "where bill_no='"+serno_value+"' and list_type='1'";
			IndexedCollection iCollTran = dao.queryList(modelIdAccTranTrustCompany, condition, connection);
			for(Iterator iterator = iCollTran.iterator();iterator.hasNext();){
				KeyedCollection kCollTran = (KeyedCollection)iterator.next();
				tran_amt = tran_amt.add(BigDecimalUtil.replaceNull(kCollTran.getDataValue("tran_amt")));
			}
		    if(tran_amt.compareTo(loan_balance)>=0){
		    	kColl.put("acc_status", "9");
		    	kColl.put("loan_balance", "0");
		    	dao.update(kColl, connection);
		    	context.put("flag", "success");
		    	context.put("amt", 0.00);
		    }else{
		    	context.put("amt", loan_balance.subtract(tran_amt));
		    	context.put("flag", "error");
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
