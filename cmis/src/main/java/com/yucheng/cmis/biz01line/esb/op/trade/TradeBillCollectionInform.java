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
/**
 * 票据托收通知
 * @author Pansq
 * 根据业务品种区分：
 * 贴现业务：根据借据号更新票据流水台账状态为核销，核销日期为交易日期，
 * 			根据汇票号码更新票据明细为核销
 *           同时查询同时查询对应协议下业务是否都已经结清且合同余额为0，注销协议（合同状态，注销日期）
 *           （商票贴现是通过商票扣款时去结清的，即通过银票到期扣款交易）
 * 贸易融资业务：根据托收金额更新贷款台账表的贷款余额，若余额为0就直接结清更新台账表的状态为核销，核销日期为当前日期
 * 				同时查询对应协议下业务是否都已经结清且合同余额为0，同时注销协议（合同状态，注销日期）
 * 				若余额不为0，则只更新贷款余额为原贷款余额-托收金额
 * 
 */
public class TradeBillCollectionInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		// TODO Auto-generated method stub
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			String BILL_NO = (String)reqBody.getDataValue("BILL_NO");//汇票号码
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("TRAN_DATE"));;//交易日期
			String TRAN_AMT = reqBody.getDataValue("TRAN_AMT").toString();//交易金额
			String BUSS_TYPE = (String)reqBody.getDataValue("BUSS_TYPE");//业务类型
			String BRANCH_ID = (String)reqBody.getDataValue("BRANCH_ID");//机构代码
			BigDecimal amt = new BigDecimal(TRAN_AMT);//转化托收金额为bigdecimal类型
			if(BUSS_TYPE.equals("01")){//贴现
				String bill_type = (String)SqlClient.queryFirst("queryBillTypeByPorderNo", BILL_NO, null, connection);
				if("100".equals(bill_type)){//银票
					Map paramForDrft = new HashMap();
					paramForDrft.put("status", "9");
					paramForDrft.put("tran_date", TRAN_DATE);
					//根据借据号核销票据流水台账
					SqlClient.update("AccDrftOver", DUEBILL_NO, paramForDrft, null, connection);
					//根据汇票号码更新票据状态
					 SqlClient.update("updateBillStateByPorderNo", BILL_NO, "04", null, connection);
					//查询合同下是否有存在未结清的票据流水
					 BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryDrftFromCont", DUEBILL_NO, null, connection);
					 int i = Integer.parseInt(count.toString());
					 if(i==0){//合同下无未结清借据，接下来判断合同余额是否为0
							BigDecimal contBalance = (BigDecimal)SqlClient.queryFirst("queryContBalanceForDrft", DUEBILL_NO, null, connection);
							if(contBalance == null){
								contBalance = new BigDecimal(0);
							}
							if(Integer.parseInt(contBalance.toString()) == 0){//合同余额为0,执行注销合同操作
								Map paramForCont = new HashMap();
								paramForCont.put("status", "900");
								paramForCont.put("tran_date", TRAN_DATE);
								SqlClient.update("cancelCtrLoanContByBillNoForDrft", DUEBILL_NO, paramForCont, null, connection);
								//解除合同和担保合同关系
								SqlClient.update("cancelGrtLoanGurForDrft", DUEBILL_NO, null, null, connection);
							}
						}
				}else{
					//商票贴现不是垫款，也直接结清
					String status = (String)SqlClient.queryFirst("queryStatusByPorderNo", BILL_NO, null, connection);
					if(!status.equals("07")){
						//票据状态不为垫款，则可以结清直贴台账
						String ORI_DUEBILL_NO = (String)SqlClient.queryFirst("queryBillNoFromAccDrftByPorderNo", BILL_NO, null, connection);//商票直贴的借据号
						Map paramForDrft = new HashMap();
						paramForDrft.put("status", "9");
						paramForDrft.put("tran_date", TRAN_DATE);
						//根据借据号核销票据流水台账
						SqlClient.update("AccDrftOver", ORI_DUEBILL_NO, paramForDrft, null, connection);
						//根据汇票号码更新票据状态
						SqlClient.update("updateBillStateByPorderNo", BILL_NO, "04", null, connection);
						//查询合同下是否有存在未结清的票据流水
						 BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryDrftFromCont", ORI_DUEBILL_NO, null, connection);
						 int i = Integer.parseInt(count.toString());
						 if(i==0){//合同下无未结清借据，接下来判断合同余额是否为0
							 BigDecimal contBalance = (BigDecimal)SqlClient.queryFirst("queryContBalanceForDrft", ORI_DUEBILL_NO, null, connection);
							 if(contBalance == null){
								 contBalance = new BigDecimal(0);
							 }
							 if(Integer.parseInt(contBalance.toString()) == 0){//合同余额为0,执行注销合同操作
								 Map paramForCont = new HashMap();
								 paramForCont.put("status", "900");
								 paramForCont.put("tran_date", TRAN_DATE);
								 SqlClient.update("cancelCtrLoanContByBillNoForDrft", ORI_DUEBILL_NO, paramForCont, null, connection);
								 //解除合同和担保合同关系
								 SqlClient.update("cancelGrtLoanGurForDrft", ORI_DUEBILL_NO, null, null, connection);
							 }
						}
					}
				}
				
			}else if(BUSS_TYPE.equals("05")){//转贴现
				Map paramForDrft = new HashMap();
				paramForDrft.put("status", "9");
				paramForDrft.put("tran_date", TRAN_DATE);
				//根据借据号核销票据流水台账
				SqlClient.update("AccDrftOver", DUEBILL_NO, paramForDrft, null, connection);
				//根据汇票号码更新票据状态
				 SqlClient.update("updateBillStateByPorderNo", BILL_NO, "04", null, connection);
				//查询合同下是否有存在未结清的票据流水
				 BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryDrftFromCont", DUEBILL_NO, null, connection);
				 int i = Integer.parseInt(count.toString());
				 if(i==0){//合同下无未结清借据则注销合同
							Map paramForCont = new HashMap();
							paramForCont.put("status", "900");
							paramForCont.put("tran_date", TRAN_DATE);
							SqlClient.update("cancelCtrRpddscntContByBillNoForDrft", DUEBILL_NO, paramForCont, null, connection);
							//解除合同和担保合同关系
							SqlClient.update("cancelGrtLoanGurForDrft", DUEBILL_NO, null, null, connection);
					}
				
				 //判断是否需要结清商票直贴
				 String bill_type = (String)SqlClient.queryFirst("queryBillTypeByPorderNo", BILL_NO, null, connection);
				 if("200".equals(bill_type)){//商票
					 String status = (String)SqlClient.queryFirst("queryStatusByPorderNo", BILL_NO, null, connection);
					 if(!status.equals("07")){
							//票据状态不为垫款，则可以结清直贴台账
							String ORI_DUEBILL_NO = (String)SqlClient.queryFirst("queryBillNoFromAccDrftByPorderNo", BILL_NO, null, connection);//商票直贴的借据号
							Map paramForDrft1 = new HashMap();
							paramForDrft1.put("status", "9");
							paramForDrft1.put("tran_date", TRAN_DATE);
							//根据借据号核销票据流水台账
							SqlClient.update("AccDrftOver", ORI_DUEBILL_NO, paramForDrft1, null, connection);
							//根据汇票号码更新票据状态
							SqlClient.update("updateBillStateByPorderNo", BILL_NO, "04", null, connection);
							//查询合同下是否有存在未结清的票据流水
							 BigDecimal count1 = (BigDecimal)SqlClient.queryFirst("queryDrftFromCont", ORI_DUEBILL_NO, null, connection);
							 int j = Integer.parseInt(count1.toString());
							 if(j==0){//合同下无未结清借据，接下来判断合同余额是否为0
								 BigDecimal contBalance = (BigDecimal)SqlClient.queryFirst("queryContBalanceForDrft", ORI_DUEBILL_NO, null, connection);
								 if(contBalance == null){
									 contBalance = new BigDecimal(0);
								 }
								 if(Integer.parseInt(contBalance.toString()) == 0){//合同余额为0,执行注销合同操作
									 Map paramForCont = new HashMap();
									 paramForCont.put("status", "900");
									 paramForCont.put("tran_date", TRAN_DATE);
									 SqlClient.update("cancelCtrLoanContByBillNoForDrft", ORI_DUEBILL_NO, paramForCont, null, connection);
									 //解除合同和担保合同关系
									 SqlClient.update("cancelGrtLoanGurForDrft", ORI_DUEBILL_NO, null, null, connection);
								 }
							}
						}
				 }
			}else{//贸易融资
				//通过借据号查询贷款台账信息
				KeyedCollection info = (KeyedCollection)SqlClient.queryFirst("queryAccLoanByBillNo", DUEBILL_NO, null, connection);
				if(info != null){
					BigDecimal loanBalance = (BigDecimal)info.getDataValue("loan_balance"); 
					if(loanBalance.compareTo(amt)==0){//贷款余额等于托收金额则更新贷款台账余额为0，核销贷款台账
						Map paramForLoan = new HashMap();
						paramForLoan.put("status", "9");
						paramForLoan.put("balance", "0");
						paramForLoan.put("tran_date", TRAN_DATE);
						//核销贷款台账
						SqlClient.update("AccLoanOver", DUEBILL_NO, paramForLoan, null, connection);
						//根据借据号查询合同下是否有未结清借据
						BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryLoanBillFromCont", DUEBILL_NO, null, connection);
						int i = Integer.parseInt(count.toString());
						if(i==0){//合同下无未结清借据，接下来判断合同余额是否为0
							BigDecimal contBalance = (BigDecimal)SqlClient.queryFirst("queryContBalance", DUEBILL_NO, null, connection);
							if(contBalance == null){
								contBalance = new BigDecimal(0);
							}
							if(Integer.parseInt(contBalance.toString()) == 0){//合同余额为0,执行注销合同操作
								Map paramForCont = new HashMap();
								paramForCont.put("status", "900");
								paramForCont.put("tran_date", TRAN_DATE);
								SqlClient.update("cancelCtrLoanContByBillNo", DUEBILL_NO, paramForCont, null, connection);
							}
						}
					}else{//托收金额不等于贷款余额，更新贷款余额为原贷款余额-托收金额，台账状态为原台账状态
						BigDecimal newbalance = loanBalance.subtract(amt);//贷款余额-托收金额
						String status = (String)info.getDataValue("acc_status");//为了复用sql语句获取原台账状态进行更新
						Map param = new HashMap();
						param.put("status", status);
						param.put("balance", newbalance);
						SqlClient.update("updateAccLoanStatusAndBalanceOnly", DUEBILL_NO, param, null, connection);
						//解除合同和担保合同关系
						SqlClient.update("cancelGrtLoanGur", DUEBILL_NO, null, null, connection);
					}
				}
			}
			EMPLog.log("TradeBillCollectionInform", EMPLog.INFO, 0, "【票据托收通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【票据托收通知】,业务逻辑处理异常！借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeBillCollectionInform", EMPLog.ERROR, 0, "【票据托收通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
