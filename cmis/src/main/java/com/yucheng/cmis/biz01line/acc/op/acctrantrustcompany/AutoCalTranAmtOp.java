package com.yucheng.cmis.biz01line.acc.op.acctrantrustcompany;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
*@author lisj
*@time 2014-12-30
*@description 需求编号：【XD141204082】当款项明细为【安排错合费】、【贷款管理费】时，交易金额自动计算
*@version v1.0
*
 */
public class AutoCalTranAmtOp extends CMISOperation {

//private final String modelId = "AccTranTrustCompany";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String bill_no ="";
			String cont_no="";
			String tran_date = "";// AM/LM费用付款日
			String last_pay_date ="";// AM/LM费用上一付款日
			String rm_value="";
			BigDecimal tran_amt = new BigDecimal(0.00);//交易金额
			BigDecimal loan_amt = new BigDecimal(0.00);//台账贷款金额
			BigDecimal loanBalance = new BigDecimal(0.00); //台账贷款余额
			try {
				bill_no = (String)context.getDataValue("bill_no");
				cont_no = (String)context.getDataValue("cont_no");
				tran_date = (String)context.getDataValue("tran_date");
				last_pay_date = (String)context.getDataValue("last_pay_date");
				rm_value = (String)context.getDataValue("rm_value");
			} catch (Exception e) {}
			if(bill_no == null || bill_no.length() == 0)
				throw new EMPJDBCException("借据号["+bill_no+"] 不能为空值！");
			if(cont_no == null || cont_no.length() == 0)
				throw new EMPJDBCException("合同编号 ["+cont_no+"]  不能为空值！");
			if(tran_date == null || tran_date.length() == 0)
				throw new EMPJDBCException("付款日 ["+tran_date+"]  不能为空值！");
			if(last_pay_date == null || last_pay_date.length() == 0)
				throw new EMPJDBCException("上一付款日["+last_pay_date+"]  不能为空值！");
			if(rm_value == null || rm_value.length() == 0)
				throw new EMPJDBCException("款项明细不能为空值！");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);

			KeyedCollection accTranTC = dao.queryDetail("AccLoan", bill_no, connection);
			loan_amt = BigDecimalUtil.replaceNull(accTranTC.getDataValue("loan_amt"));//台账贷款金额
			String conditionSelect ="select p1.* from iqp_trust_fee_info p1, ctr_loan_cont p2"
									+" where p2.serno = p1.serno and p2.cont_no = '"+cont_no+"'";
			IndexedCollection trustFI = TableModelUtil.buildPageData(null, dataSource, conditionSelect);
			KeyedCollection trustFeeInfo = new KeyedCollection();
			BigDecimal am_fee_rate = new BigDecimal(0);
			BigDecimal lm_fee_rate = new BigDecimal(0);
			//注意：存量的信托合同不存在安排撮合费率或贷款管理费率的情况
			if(trustFI!=null && trustFI.size() >0){
				trustFeeInfo = (KeyedCollection) trustFI.get(0);
				am_fee_rate = BigDecimalUtil.replaceNull(trustFeeInfo.getDataValue("am_fee_rate"));//安排撮合费率（年）
				lm_fee_rate = BigDecimalUtil.replaceNull(trustFeeInfo.getDataValue("lm_fee_rate"));//贷款管理费率（年）
			}
			 BigDecimal sumAmt = new BigDecimal(0.00);
			//交易明细存在款项明细为【归还本金】时，进行分段计算
			String condiStrSegOne ="select * from acc_tran_trust_company p1 where to_date(p1.tran_date, 'yyyy-mm-dd') <= to_date('"+last_pay_date+"', 'yyyy-mm-dd')"
									  +" and p1.reclaim_mode = 'PP' "
									  +" and p1.trade_status = '1'  "
									  +" and p1.bill_no ='"+bill_no+"' order by p1.tran_date asc";
			String condiStrSegTwo ="select * from acc_tran_trust_company p1                                                  "
									+" where to_date('"+last_pay_date+"', 'yyyy-mm-dd') < to_date(p1.tran_date, 'yyyy-mm-dd')" 
									+"     and to_date(p1.tran_date, 'yyyy-mm-dd') < to_date('"+tran_date+"', 'yyyy-mm-dd')  "
									+"			 and p1.reclaim_mode = 'PP'                                                  "
									+"			 and p1.trade_status = '1'                                                   "
									+"			 and p1.bill_no = '"+bill_no+"' order by p1.tran_date asc                    ";
			IndexedCollection accTTC4PPSegOne = TableModelUtil.buildPageData(null, dataSource, condiStrSegOne);
			IndexedCollection accTTC4PPSegTwo= TableModelUtil.buildPageData(null, dataSource, condiStrSegTwo);
				if(accTTC4PPSegOne!=null && accTTC4PPSegOne.size() >0){
					for(int i=0; i < accTTC4PPSegOne.size(); i++){
						KeyedCollection temp = (KeyedCollection) accTTC4PPSegOne.get(0);
						sumAmt =  sumAmt.add(BigDecimalUtil.replaceNull(temp.getDataValue("tran_amt")));
					}
				}
				loanBalance = loan_amt.subtract(sumAmt);//贷款余额
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(accTTC4PPSegTwo!=null && accTTC4PPSegTwo.size()>0){
				 BigDecimal nextLoanBalance = loanBalance;
				 Date startDate = new Date();
				 Date endDate = new Date();
				 Date preDate = new Date();
				 for(int j=0; j <= accTTC4PPSegTwo.size(); j++){
					 KeyedCollection tempSegTwo = new KeyedCollection();
					 if(j<accTTC4PPSegTwo.size()){
						 tempSegTwo = (KeyedCollection) accTTC4PPSegTwo.get(j);
					 }
					 if(j-1 < 0){
						 //段头节点
						 startDate = sdf.parse(last_pay_date);
					 }else{
						 startDate = preDate;
					 }
					 if(j==accTTC4PPSegTwo.size()){
						 endDate = sdf.parse(tran_date);
					 }else{
						 endDate = sdf.parse(tempSegTwo.getDataValue("tran_date").toString());
					 }
					 long segTime = ( endDate.getTime()-startDate.getTime());
					 BigDecimal days = new BigDecimal((segTime/(1000*60*60*24)));
					 BigDecimal tran_amt_temp = new BigDecimal(0.00);
					 if("AM".equals(rm_value)){
						 tran_amt_temp = (nextLoanBalance.multiply(days).multiply(am_fee_rate)).divide(new BigDecimal(360),2,BigDecimal.ROUND_HALF_EVEN);
					 }else if("LM".equals(rm_value)){
						 tran_amt_temp = (nextLoanBalance.multiply(days).multiply(lm_fee_rate)).divide(new BigDecimal(360),2,BigDecimal.ROUND_HALF_EVEN);
					 }
					 tran_amt=tran_amt.add(tran_amt_temp);
					 if(j<accTTC4PPSegTwo.size()){
						 preDate = sdf.parse(tempSegTwo.getDataValue("tran_date").toString());
						 nextLoanBalance=nextLoanBalance.subtract(BigDecimalUtil.replaceNull(tempSegTwo.getDataValue("tran_amt")));
					 }
				 }
				 context.addDataField("tranAmt", tran_amt);
			}else{
				 Date td =sdf.parse(tran_date);
				 Date lpd = sdf.parse(last_pay_date);
				 long segTime = ( td.getTime()-lpd.getTime());
				 BigDecimal days = new BigDecimal((segTime/(1000*60*60*24)));
				 if("AM".equals(rm_value)){
				 		tran_amt = ((loanBalance.multiply(am_fee_rate)).multiply(days)).divide(new BigDecimal(360),2,BigDecimal.ROUND_HALF_EVEN);
				 	}else if("LM".equals(rm_value)){
				 		tran_amt = ((loanBalance.multiply(lm_fee_rate)).multiply(days)).divide(new BigDecimal(360),2,BigDecimal.ROUND_HALF_EVEN);
				 }
				 context.addDataField("tranAmt", tran_amt);
			}
		 	context.addDataField("msg", "success");
			
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
