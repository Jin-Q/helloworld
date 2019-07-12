package com.yucheng.cmis.biz01line.iqp.op.iqptrustfeeinfo;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 
*@author lisj
*@time 2015-10-13 
*@description 需求编号：XD150409029 信贷保函及资产模块改造需求
*@version v1.0
*
 */
public class CalBaileePayAmtOp extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String serno ="";
			//String mm_charge_term ="";//委托管理费计费周期
			BigDecimal mm_fee_rate = new BigDecimal(0);//委托管理费率（年）
			BigDecimal bailee_pay_ratio = new BigDecimal(0);//信托计划受托人支付比例
			try {
				serno = (String)context.getDataValue("serno");
				mm_fee_rate = BigDecimalUtil.replaceNull((String)context.getDataValue("mm_fee_rate"));
				bailee_pay_ratio = BigDecimalUtil.replaceNull((String)context.getDataValue("bailee_pay_ratio"));
				//mm_charge_term = (String)context.getDataValue("mm_charge_term");
			} catch (Exception e) {}
			if(serno == null || serno.length() == 0)
				throw new EMPJDBCException("流水号["+serno+"] 不能为空值！");

			//if(mm_charge_term == null || mm_charge_term.length() == 0)
				//throw new EMPJDBCException("委托管理费计费周期 ["+mm_charge_term+"]  不能为空值！");	
			TableModelDAO dao = this.getTableModelDAO(context);
			BigDecimal baileePayAmt = new BigDecimal(0.00);
			KeyedCollection kColl4ILA = dao.queryDetail("IqpLoanApp", serno, connection);
			KeyedCollection kColl4ILAS = dao.queryDetail("IqpLoanAppSub", serno, connection);
			if(kColl4ILA!=null && kColl4ILAS!=null && !"".equals(kColl4ILAS.getDataValue("apply_term"))){
				BigDecimal apply_term  = BigDecimalUtil.replaceNull((String)kColl4ILAS.getDataValue("apply_term"));
				String term_type = (String) kColl4ILAS.getDataValue("term_type");
				BigDecimal apply_amount = BigDecimalUtil.replaceNull((String) kColl4ILA.getDataValue("apply_amount"));
				BigDecimal terms = new BigDecimal(0);
				//将期限转化为以年为单位
				if("001".equals(term_type)){
					terms = apply_term;
				}else if("002".equals(term_type)){
					terms = apply_term.divide(new BigDecimal(12),2,BigDecimal.ROUND_HALF_EVEN);
				}else if("003".equals(term_type)){
					terms = apply_term.divide(new BigDecimal(365),2,BigDecimal.ROUND_HALF_EVEN);
				}
				baileePayAmt = ((apply_amount.multiply(bailee_pay_ratio)).multiply(terms)).multiply(mm_fee_rate).setScale(2, BigDecimal.ROUND_HALF_UP);;
				context.addDataField("baileePayAmt", baileePayAmt);
			 	context.addDataField("msg", "success");
			}else{
				context.addDataField("msg", "failed");	
			}	
		}catch (EMPException ee) {
		 	context.addDataField("msg", "failed");
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
