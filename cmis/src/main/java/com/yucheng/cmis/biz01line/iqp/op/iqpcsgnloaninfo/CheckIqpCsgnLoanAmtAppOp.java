package com.yucheng.cmis.biz01line.iqp.op.iqpcsgnloaninfo;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class CheckIqpCsgnLoanAmtAppOp extends CMISOperation {
	

	private final String modelIdSub = "IqpCsgnLoanInfo";
	private final String modelId = "IqpLoanApp";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = null;
			BigDecimal csgn_amt = null;
			try {
				serno = (String)context.getDataValue("serno");
				csgn_amt = new BigDecimal(context.getDataValue("csgn_amt")+"");
			} catch (Exception e) {}
			if(serno == null || "".equals(serno))
				throw new EMPJDBCException("The values to serno cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kc = dao.queryDetail(modelId, serno, connection);
			String kSerno = (String)kc.getDataValue("serno");
			if(kSerno != null && kSerno != ""){
				BigDecimal applyAmt = BigDecimalUtil.replaceNull(kc.getDataValue("apply_amount"));
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kc.getDataValue("exchange_rate"));
				
				if((csgn_amt.compareTo(applyAmt.multiply(exchange_rate)))==0){
					context.addDataField("flag", "success");
				}else{
					context.addDataField("flag", "error");
				}
				
			} else {
				throw new EMPJDBCException("业务申请基本信息取值失败!");
			}
			
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
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
