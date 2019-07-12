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
 * 银票、保函到期扣款通知
 * @author Pansq
 * 根据业务类型区分：
 * 银承：
 * 		结清： 银票台账状态置为7已扣款，
 *      未結清：銀票台賬狀態置为6垫款，更新墊款金額和轉墊款日期，同時生成墊款台賬
 * 境内保函：
 *      结清：贷款台账状态置为7已扣款
 *      未結清：貸款台賬狀態置为6垫款，更新贷款余额为0，更新墊款金額和轉墊款日期，同時生成墊款台賬
 * 信用证：
 *      垫款通知：置为6垫款，不更新余额，更新墊款金額和轉墊款日期，同時生成墊款台賬
 * 外汇保函：
 *      垫款通知：置为6垫款，不更新余额，更新墊款金額和轉墊款日期，同時生成墊款台賬
 * 商贴垫款：
 *      垫款通知：置为6垫款，不更新余额，更新墊款金額和轉墊款日期，同時生成墊款台賬
 */

public class TradeAccpOrLgRepayInform extends TranService {
	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			String openday  = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			String BUSS_TYPE = (String)reqBody.getDataValue("BUSS_TYPE");//业务类型：01银承，02保函，03信用证垫款，04 保函垫款，05商贴垫款
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("TRAN_DATE"));//交易日期
			String BRANCH_ID = (String)reqBody.getDataValue("BRANCH_ID");//机构代码
			String TRANS_ADV_CASH_FLAG = (String)reqBody.getDataValue("TRANS_ADV_CASH_FLAG");//转垫款标记
			String ADVANCE_CASH_SEQ_NO = (String)reqBody.getDataValue("ADVANCE_CASH_SEQ_NO");//垫款业务流水号
			String ADVANCE_CASH_DUEBILL_NO = (String)reqBody.getDataValue("ADVANCE_CASH_DUEBILL_NO");//垫款借据编号
			String DATE = reqBody.getDataValue("DATE").toString();//自然日期
			String YEAR = (String)reqBody.getDataValue("YEAR");//自然年
			String MONTH = (String)reqBody.getDataValue("MONTH");//自然月
			String PRODUCT_NO = (String)reqBody.getDataValue("PRODUCT_NO");//产品编号
			String CONTRACT_NO = (String)reqBody.getDataValue("CONTRACT_NO");//合同号
			String CLIENT_NO = (String)reqBody.getDataValue("CLIENT_NO");//客户号
			String CCY = (String)reqBody.getDataValue("CCY");//币种
			String ADVANCE_CASH_AMT = (String)reqBody.getDataValue("ADVANCE_CASH_AMT").toString();//垫款金额
			String ADVANCE_CASH_STATUS = (String)reqBody.getDataValue("ADVANCE_CASH_STATUS");//垫款状态
			
			/**借据状态
			 NBAP: 未放款
			 ACTV: 已发放
			 SETL: 已结清 
			 * */
			Map param = new HashMap();
			Map paramForAcc = new HashMap();
			//判断业务类型
			if("01".equals(BUSS_TYPE)){//银票
				if("SETL".equals(TRANS_ADV_CASH_FLAG)){//银票且无转垫款，银票台账状态置为已扣款
					param.put("status", "7");
					SqlClient.update("updateAccAccpStatusAndPorderNo", DUEBILL_NO, param, null, connection);
				}else{//未结清银票台账状态置为垫款,更新轉墊款日期，墊款金額，同時生成墊款台賬信息
					param.put("status", "6");
					param.put("amt", ADVANCE_CASH_AMT);
					param.put("day", openday);
					SqlClient.update("updateAccAccpForPad", DUEBILL_NO, param, null, connection);
					//通過借據號查詢銀票台賬信息
					  KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryAccpByBillNoForPad", DUEBILL_NO, null, connection);
					  if(kColl!=null){
							String fina_br_id = (String)kColl.getDataValue("fina_br_id");
							String manager_br_id = (String)kColl.getDataValue("manager_br_id");
							String prd_id = (String) kColl.getDataValue("prd_id");
							String cont_no = (String) kColl.getDataValue("cont_no");
							paramForAcc.put("serno", ADVANCE_CASH_DUEBILL_NO);
							paramForAcc.put("acc_day", DATE);
							paramForAcc.put("acc_year", YEAR);
							paramForAcc.put("acc_mon", MONTH);
							paramForAcc.put("prd_id", prd_id);
							paramForAcc.put("cont_no", cont_no);
							paramForAcc.put("bill_no", ADVANCE_CASH_DUEBILL_NO);
							paramForAcc.put("cus_id", CLIENT_NO);
							paramForAcc.put("pad_type", "01");
							paramForAcc.put("pad_bill_no", DUEBILL_NO);
							paramForAcc.put("pad_cur_type", CCY);
							paramForAcc.put("pad_amt", ADVANCE_CASH_AMT);
							paramForAcc.put("pad_date", TRAN_DATE);
							paramForAcc.put("pad_bal", ADVANCE_CASH_AMT);
							paramForAcc.put("separate_date", "");
							paramForAcc.put("writeoff_date", "");
							/* modified by yangzy 2014/09/29 当日产生垫款，五级分类为关注 begin */
							paramForAcc.put("five_class", "20");
							/* modified by yangzy 2014/09/29 当日产生垫款，五级分类为关注 end */
							paramForAcc.put("twelve_cls_flg", "");
							paramForAcc.put("manager_br_id", manager_br_id);
							paramForAcc.put("fina_br_id", fina_br_id);
							paramForAcc.put("overdue_balance", ADVANCE_CASH_AMT);
							paramForAcc.put("accp_status", "1");
							SqlClient.insert("insertAccPad", paramForAcc, connection);
						}
				}
			}else if("02".equals(BUSS_TYPE)){//保函
				if("SETL".equals(TRANS_ADV_CASH_FLAG)){//保函且无转垫款，贷款台账状态置为结清
					Map paramForOver = new HashMap();
					paramForOver.put("status", "9");
					paramForOver.put("balance", 0.0);
					paramForOver.put("tran_date", TRAN_DATE);
					SqlClient.update("AccLoanOver", DUEBILL_NO, paramForOver, null, connection);
					
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
							//解除合同和担保合同关系
							SqlClient.update("cancelGrtLoanGur", DUEBILL_NO, null, null, connection);
						}
					}
					
				}else{//保函未结清，贷款台账状态置为垫款,轉墊款日期為當前日期，同時生成墊款台賬信息
					Map paramForPad = new HashMap();
					paramForPad.put("status", "6");
					paramForPad.put("day", openday);
					paramForPad.put("balance", 0.0);
					SqlClient.update("updateAccLoanStatusForGutrPad", DUEBILL_NO, paramForPad, null, connection);
					//通過借據號查詢貸款台賬信息
					  KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryAccLoanByBillNo", DUEBILL_NO, null, connection);
					  if(kColl!=null){
							String fina_br_id = (String)kColl.getDataValue("fina_br_id");
							String manager_br_id = (String)kColl.getDataValue("manager_br_id");
							String prd_id = (String) kColl.getDataValue("prd_id");
							String cont_no = (String) kColl.getDataValue("cont_no");
							paramForAcc.put("serno", ADVANCE_CASH_DUEBILL_NO);
							paramForAcc.put("acc_day", DATE);
							paramForAcc.put("acc_year", YEAR);
							paramForAcc.put("acc_mon", MONTH);
							paramForAcc.put("prd_id", prd_id);
							paramForAcc.put("cont_no", cont_no);
							paramForAcc.put("bill_no", ADVANCE_CASH_DUEBILL_NO);
							paramForAcc.put("cus_id", CLIENT_NO);
							paramForAcc.put("pad_type", "02");
							paramForAcc.put("pad_bill_no", DUEBILL_NO);
							paramForAcc.put("pad_cur_type", CCY);
							paramForAcc.put("pad_amt", ADVANCE_CASH_AMT);
							paramForAcc.put("pad_date", TRAN_DATE);
							paramForAcc.put("pad_bal", ADVANCE_CASH_AMT);
							paramForAcc.put("separate_date", "");
							paramForAcc.put("writeoff_date", "");
							/* modified by yangzy 2014/09/29 当日产生垫款，五级分类为关注 begin */
							paramForAcc.put("five_class", "20");
							/* modified by yangzy 2014/09/29 当日产生垫款，五级分类为关注 end */
							paramForAcc.put("twelve_cls_flg", "");
							paramForAcc.put("manager_br_id", manager_br_id);
							paramForAcc.put("fina_br_id", fina_br_id);
							paramForAcc.put("accp_status", "1");
							SqlClient.insert("insertAccPad", paramForAcc, connection);
						}
				}
			}else if("03".equals(BUSS_TYPE)){//信用证垫款
				Map paramForPad = new HashMap();
				paramForPad.put("status", "6");
				paramForPad.put("day", openday);
				SqlClient.update("updateAccLoanStatusForPad", DUEBILL_NO, paramForPad, null, connection);
				//通過借據號查詢貸款台賬信息
				  KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryAccLoanByBillNo", DUEBILL_NO, null, connection);
				  if(kColl!=null){
						String fina_br_id = (String)kColl.getDataValue("fina_br_id");
						String manager_br_id = (String)kColl.getDataValue("manager_br_id");
						String prd_id = (String) kColl.getDataValue("prd_id");
						String cont_no = (String) kColl.getDataValue("cont_no");
						paramForAcc.put("serno", ADVANCE_CASH_DUEBILL_NO);
						paramForAcc.put("acc_day", DATE);
						paramForAcc.put("acc_year", YEAR);
						paramForAcc.put("acc_mon", MONTH);
						paramForAcc.put("prd_id", prd_id);
						paramForAcc.put("cont_no", cont_no);
						paramForAcc.put("bill_no", ADVANCE_CASH_DUEBILL_NO);
						paramForAcc.put("cus_id", CLIENT_NO);
						if(prd_id!=null&&"700020".equals(prd_id)){
							paramForAcc.put("pad_type", "04");
						}else{
							paramForAcc.put("pad_type", "05");
						}
						paramForAcc.put("pad_bill_no", DUEBILL_NO);
						paramForAcc.put("pad_cur_type", CCY);
						paramForAcc.put("pad_amt", ADVANCE_CASH_AMT);
						paramForAcc.put("pad_date", TRAN_DATE);
						paramForAcc.put("pad_bal", ADVANCE_CASH_AMT);
						paramForAcc.put("separate_date", "");
						paramForAcc.put("writeoff_date", "");
						/* modified by yangzy 2014/09/29 当日产生垫款，五级分类为关注 begin */
						paramForAcc.put("five_class", "20");
						/* modified by yangzy 2014/09/29 当日产生垫款，五级分类为关注 end */
						paramForAcc.put("twelve_cls_flg", "");
						paramForAcc.put("manager_br_id", manager_br_id);
						paramForAcc.put("fina_br_id", fina_br_id);
						paramForAcc.put("accp_status", "1");
						SqlClient.insert("insertAccPad", paramForAcc, connection);
					}
			}else if("04".equals(BUSS_TYPE)){//外汇保函垫款
				Map paramForPad = new HashMap();
				paramForPad.put("status", "6");
				paramForPad.put("day", openday);
				SqlClient.update("updateAccLoanStatusForPad", DUEBILL_NO, paramForPad, null, connection);
				//通過借據號查詢貸款台賬信息
				  KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryAccLoanByBillNo", DUEBILL_NO, null, connection);
				  if(kColl!=null){
						String fina_br_id = (String)kColl.getDataValue("fina_br_id");
						String manager_br_id = (String)kColl.getDataValue("manager_br_id");
						String cont_no = (String) kColl.getDataValue("cont_no");
						String prd_id = (String) kColl.getDataValue("prd_id");
						paramForAcc.put("serno", ADVANCE_CASH_DUEBILL_NO);
						paramForAcc.put("acc_day", DATE);
						paramForAcc.put("acc_year", YEAR);
						paramForAcc.put("acc_mon", MONTH);
						paramForAcc.put("prd_id", prd_id);
						paramForAcc.put("cont_no", cont_no);
						paramForAcc.put("bill_no", ADVANCE_CASH_DUEBILL_NO);
						paramForAcc.put("cus_id", CLIENT_NO);
						paramForAcc.put("pad_type", "03");
						paramForAcc.put("pad_bill_no", DUEBILL_NO);
						paramForAcc.put("pad_cur_type", CCY);
						paramForAcc.put("pad_amt", ADVANCE_CASH_AMT);
						paramForAcc.put("pad_date", TRAN_DATE);
						paramForAcc.put("pad_bal", ADVANCE_CASH_AMT);
						paramForAcc.put("separate_date", "");
						paramForAcc.put("writeoff_date", "");
						/* modified by yangzy 2014/09/29 当日产生垫款，五级分类为关注 begin */
						paramForAcc.put("five_class", "20");
						/* modified by yangzy 2014/09/29 当日产生垫款，五级分类为关注 end */
						paramForAcc.put("twelve_cls_flg", "");
						paramForAcc.put("manager_br_id", manager_br_id);
						paramForAcc.put("fina_br_id", fina_br_id);
						paramForAcc.put("accp_status", "1");
						SqlClient.insert("insertAccPad", paramForAcc, connection);
					}
			}else if("05".equals(BUSS_TYPE)){//商贴垫款
				if("SETL".equals(TRANS_ADV_CASH_FLAG)){//无转垫款，结清商票贴现
					BigDecimal count_rp = (BigDecimal)SqlClient.queryFirst("queryRpddscntFromDrftByBillNo", DUEBILL_NO, null, connection);
					int j = Integer.parseInt(count_rp.toString());
					if(j!=0){//发生过转贴，可以结清直贴
						Map paramForDrft = new HashMap();
						paramForDrft.put("status", "9");
						paramForDrft.put("tran_date", TRAN_DATE);
						//根据借据号核销票据流水台账
						SqlClient.update("AccDrftOver", DUEBILL_NO, paramForDrft, null, connection);
						//根据汇票号码更新票据状态
						BigDecimal count_rps = (BigDecimal)SqlClient.queryFirst("queryRpddscntFromDrftByBillNoForStatus", DUEBILL_NO, null, connection);
						int k = Integer.parseInt(count_rps.toString());
						if(k==0){//如果贴现结清，需要修改票据状态
							SqlClient.update("updateBillStatusForPad", DUEBILL_NO, "04", null, connection);
						}
						
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
					}
					
				}else{//产生垫款
					Map paramForPad = new HashMap();
					paramForPad.put("status", "6");
					paramForPad.put("day", openday);
					SqlClient.update("updateAccDrftStatusForPad", DUEBILL_NO, paramForPad, null, connection);
					
					//同时更新票据状态
					SqlClient.update("updateBillStatusForPad", DUEBILL_NO, "07", null, connection);
					
					//通過借據號查詢貸款台賬信息
					  KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryDrftByBillNoForPad", DUEBILL_NO, null, connection);
					  if(kColl!=null){
							String fina_br_id = (String)kColl.getDataValue("fina_br_id");
							String manager_br_id = (String)kColl.getDataValue("manager_br_id");
							String prd_id = (String) kColl.getDataValue("prd_id");
							String cont_no = (String) kColl.getDataValue("cont_no");
							paramForAcc.put("serno", ADVANCE_CASH_DUEBILL_NO);
							paramForAcc.put("acc_day", DATE);
							paramForAcc.put("acc_year", YEAR);
							paramForAcc.put("acc_mon", MONTH);
							paramForAcc.put("prd_id", prd_id);
							paramForAcc.put("cont_no", cont_no);
							paramForAcc.put("bill_no", ADVANCE_CASH_DUEBILL_NO);
							paramForAcc.put("cus_id", CLIENT_NO);
							paramForAcc.put("pad_type", "99");
							paramForAcc.put("pad_bill_no", DUEBILL_NO);
							paramForAcc.put("pad_cur_type", CCY);
							paramForAcc.put("pad_amt", ADVANCE_CASH_AMT);
							paramForAcc.put("pad_date", TRAN_DATE);
							paramForAcc.put("pad_bal", ADVANCE_CASH_AMT);
							paramForAcc.put("separate_date", "");
							paramForAcc.put("writeoff_date", "");
							/* modified by yangzy 2014/09/29 当日产生垫款，五级分类为关注 begin */
							paramForAcc.put("five_class", "20");
							/* modified by yangzy 2014/09/29 当日产生垫款，五级分类为关注 end */
							paramForAcc.put("twelve_cls_flg", "");
							paramForAcc.put("manager_br_id", manager_br_id);
							paramForAcc.put("fina_br_id", fina_br_id);
							paramForAcc.put("accp_status", "1");
							SqlClient.insert("insertAccPad", paramForAcc, connection);
						}
				}
			}
			EMPLog.log("TradeAccpOrLgRepayInform", EMPLog.INFO, 0, "【银票、保函到期扣款通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【银票、保函到期扣款通知】 ,业务处理失败！借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeAccpOrLgRepayInform", EMPLog.ERROR, 0, "【银票、保函到期扣款通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}
}
