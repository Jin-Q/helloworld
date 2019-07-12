package com.yucheng.cmis.biz01line.acc.op.acctrantrustcompany;

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

public class CheckAccTranTrustAmtOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "AccTranTrustCompany";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String list_type ="";
			String serno ="";
			String bill_no ="";
			String oprate ="";
			BigDecimal amt = new BigDecimal(0.00);
			try {
				 list_type = (String)context.getDataValue("list_type");//回收方式
				 bill_no = (String)context.getDataValue("bill_no");
				 oprate = (String)context.getDataValue("oprate");
				 serno = (String)context.getDataValue("serno");
			} catch (Exception e) {} 
			if(list_type == null || "".equals(list_type) || bill_no == null || "".equals(bill_no))
				throw new EMPJDBCException("The values reclaim_mode,bill_no cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = "";
			if("add".equals(oprate)){
				condition="where bill_no='"+bill_no+"' and list_type='"+list_type+"'";
			}else{
				condition="where bill_no='"+bill_no+"' and list_type='"+list_type+"' and serno<>'"+serno+"'";
			}
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			for(Iterator iterator = iColl.iterator();iterator.hasNext();){
				KeyedCollection kColl = (KeyedCollection)iterator.next();
				amt = amt.add(BigDecimalUtil.replaceNull(kColl.getDataValue("tran_amt")));
			}
			context.put("amt", amt);
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
