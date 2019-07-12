package com.yucheng.cmis.biz01line.esb.op.trade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 贸易融资表外业务修改成功通知
 * @author liqh
 *  根据借据号查询改证申请表获取最新的一条记录，获取修改后的金额，修改后的保证金比例
 * 	更新贷款台账表的贷款金额，贷款余额为修改后信用证金额，如果原到期日与修改后的到期日不一直则展期次数+1，贷款到期日为信用证到期日
 * 	更新合同表的合同金额为修改后信用证金额，保证金比例为修改后的保证金比例
 * 	根据借据号更新授权表的状态为授权已确认
 * 
 */
public class TradeMyrzbwywxgInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			String BUSS_KIND = (String)reqBody.getDataValue("BUSS_KIND");//借据号
			KeyedCollection kColl = null;
			if(BUSS_KIND.equals("01")){//信用证
				//查询出改证申请信息
				kColl = (KeyedCollection)SqlClient.queryFirst("queryCreditChangeByBillNo", DUEBILL_NO, null, connection);
				if(kColl != null){
					BigDecimal new_apply_amt = (BigDecimal)kColl.getDataValue("new_apply_amt");//修改后的金额
					BigDecimal apply_amt = (BigDecimal)kColl.getDataValue("cont_amt");//修改前的金额
					String serno = (String)kColl.getDataValue("serno");//申请流水号
					String new_end_date = (String)kColl.getDataValue("new_end_date");//新效期
					String new_cur_type = (String)kColl.getDataValue("new_cur_type");//新币种
					//计算差额
					BigDecimal dif_amt = new_apply_amt.subtract(apply_amt);
					BigDecimal new_security_rate = (BigDecimal)kColl.getDataValue("new_security_rate");//修改后的保证金比例
					BigDecimal new_floodact_perc = (BigDecimal)kColl.getDataValue("new_floodact_perc");//修改后溢装比例
					BigDecimal floodact_perc = (BigDecimal)kColl.getDataValue("floodact_perc");//修改前溢装比例
					/** added by yangzy 2015/07/20 需求：XD150407026， 更新存量外币业务的汇率 start **/
					BigDecimal new_exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("new_exchange_rate"));//修改后汇率
					BigDecimal new_security_exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("new_security_exchange_rate"));//修改后保证金汇率
					/** added by yangzy 2015/07/20 需求：XD150407026， 更新存量外币业务的汇率 end **/
					//通过借据号查询出贷款台账信息获取展期次数
					//根据借据号查询贷款台账表
					KeyedCollection result = (KeyedCollection)SqlClient.queryFirst("queryAccLoanByBillNo", DUEBILL_NO, null, connection);
					if(result != null){
						BigDecimal count = (BigDecimal)result.getDataValue("post_count");
						BigDecimal balance = (BigDecimal)result.getDataValue("loan_balance");
						BigDecimal loan_amt = (BigDecimal)result.getDataValue("loan_amt");
						
						//先计算出还款金额
						BigDecimal hk = loan_amt.subtract(balance);
						BigDecimal new_loan_amt = new_apply_amt.multiply(new_floodact_perc.add(BigDecimalUtil.replaceNull(1.00)));//新贷款金额
						BigDecimal new_balance = new_loan_amt.subtract(hk);//新贷款余额
						
						int i = Integer.parseInt(count.toString());
						String end_date = (String)result.getDataValue("end_date");
						Map param = new HashMap();
						param.put("end_date", end_date);//原到期日
						//原到期日与新到期日如果不一致展期次数+1
						if(!end_date.equals(new_end_date)){
							param.put("count", i+1);//展期次数	
						}
						param.put("new_end_date", new_end_date);//新效期
						param.put("amt", new_loan_amt);//贷款金额
						param.put("balance", new_balance);//贷款余额
						param.put("cur_type", new_cur_type);//新币种
						//更新贷款台账表
						SqlClient.update("updateAccLoanForCreditxg", DUEBILL_NO, param, null, connection);
						//更新合同表
						Map paramForCont = new HashMap();
						paramForCont.put("serno", serno);//改证申请流水号
						paramForCont.put("amt", new_apply_amt);//贷款金额
						paramForCont.put("rate", new_security_rate);//保证金比例
						paramForCont.put("cont_cur_type", new_cur_type);//新币种
						/** added by yangzy 2015/07/20 需求：XD150407026， 更新存量外币业务的汇率 start **/
						paramForCont.put("new_exchange_rate", new_exchange_rate);//修改后汇率
						paramForCont.put("new_security_exchange_rate", new_security_exchange_rate);//修改后保证金汇率
						/** added by yangzy 2015/07/20 需求：XD150407026， 更新存量外币业务的汇率 end **/
						SqlClient.update("updateCtrLoanContForCredit", DUEBILL_NO, paramForCont, null, connection);
					}
					
				}
				}else if(BUSS_KIND.equals("02")){//保函
					//查询出保函修改申请信息
					kColl = (KeyedCollection)SqlClient.queryFirst("queryGuarantChangeByBillNo", DUEBILL_NO, null, connection);
					if(kColl != null){
						BigDecimal new_cont_amt = (BigDecimal)kColl.getDataValue("new_cont_amt");//修改后的金额
						BigDecimal cont_amt = (BigDecimal)kColl.getDataValue("cont_amt");//修改前的金额
						String serno = (String)kColl.getDataValue("serno");//申请流水号
						//计算差额
						BigDecimal dif_amt = new_cont_amt.subtract(cont_amt);
						BigDecimal new_security_rate = (BigDecimal)kColl.getDataValue("new_security_rate");//修改后的保证金比例
						String cont_end_date = (String)kColl.getDataValue("new_cont_end_date"); //到期日
						String new_cur_type = (String)kColl.getDataValue("new_cur_type");//新币种
						/** added by yangzy 2015/07/20 需求：XD150407026， 更新存量外币业务的汇率 start **/
						BigDecimal new_exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("new_exchange_rate"));//修改后汇率
						BigDecimal new_security_exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("new_security_exchange_rate"));//修改后保证金汇率						
						/** added by yangzy 2015/07/20 需求：XD150407026， 更新存量外币业务的汇率 end **/
						//通过借据号查询出贷款台账信息获取展期次数
						//根据借据号查询贷款台账表
						KeyedCollection result = (KeyedCollection)SqlClient.queryFirst("queryAccLoanByBillNo", DUEBILL_NO, null, connection);
						if(result != null){
							BigDecimal count = (BigDecimal)result.getDataValue("post_count");
							BigDecimal balance = (BigDecimal)result.getDataValue("loan_balance");
							int i = Integer.parseInt(count.toString());
							String end_date = (String)result.getDataValue("end_date");
							Map param = new HashMap();
							param.put("end_date", end_date);//原到期日
							//原到期日与新到期日如果不一致展期次数+1
							if(!end_date.equals(cont_end_date)){
								param.put("count", i+1);//展期次数	
							}
							param.put("new_end_date", cont_end_date);//新到期日
							param.put("amt", new_cont_amt);//贷款金额
							param.put("balance", balance.add(dif_amt));//贷款余额
							param.put("cur_type", new_cur_type);//币种
							//更新贷款台账表
							SqlClient.update("updateAccLoanForCreditxg", DUEBILL_NO, param, null, connection);
							//更新合同表
							Map paramForCont = new HashMap();
							paramForCont.put("serno", serno);//保函修改申请流水号
							paramForCont.put("amt", new_cont_amt);//贷款金额
							paramForCont.put("rate", new_security_rate);//保证金比例
							paramForCont.put("cont_cur_type", new_cur_type);//新币种
							/** added by yangzy 2015/07/20 需求：XD150407026， 更新存量外币业务的汇率 start **/
							paramForCont.put("new_exchange_rate", new_exchange_rate);//修改后汇率
							paramForCont.put("new_security_exchange_rate", new_security_exchange_rate);//修改后保证金汇率
							/** added by yangzy 2015/07/20 需求：XD150407026， 更新存量外币业务的汇率 end **/
							SqlClient.update("updateCtrLoanContForCredit", DUEBILL_NO, paramForCont, null, connection);
							
							//更新保函明细表到期日期
							SqlClient.update("updateEndDateByBillNo", DUEBILL_NO, cont_end_date, null, connection);
						}
						
					}
				}
			
			//根据借据号更新授权表的状态为授权已确认
			SqlClient.update("updatePvpAuthorizeStatus", DUEBILL_NO, "04", null, connection);
			
			EMPLog.log("TradeMyrzbwywxgInform", EMPLog.INFO, 0, "【贸易融资表外业务修改成功通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【贸易融资表外业务修改成功通知】,业务处理失败！借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeMyrzbwywxgInform", EMPLog.ERROR, 0, "【贸易融资表外业务修改成功通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
