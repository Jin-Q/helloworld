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
 * 垫款还款通知
 * @author Pansq
 * 说明：
 * 根据结清标志判断，
 * 已结清：根据借据编号,更新垫款台账的核销日期，垫款余额为0，更新垫款台账状态为核销，
 * 未结清：根据借据编号,更新垫款台账的垫款余额为借据余额
 *			
 */
public class TradePadRepayInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			String BUSS_TYPE = (String)reqBody.getDataValue("BUSS_TYPE");//业务类型
			String TRAN_AMT = reqBody.getDataValue("TRAN_AMT").toString();//交易金额
			String DUEBILL_BALANCE = reqBody.getDataValue("DUEBILL_BALANCE").toString();//借据余额
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("TRAN_DATE"));//交易日期
			String CANCEL_FLAG = (String)reqBody.getDataValue("CANCEL_FLAG");//撤销标志
			String DUEBILL_STATUS = (String)reqBody.getDataValue("DUEBILL_STATUS");//借据状态
			String BRANCH_ID = (String)reqBody.getDataValue("BRANCH_ID");//机构代码
			/** 判断业务类型：
			 * 1-银承垫款
			 * 2-保函垫款
			 * 3-信用证垫款
			 * 4-保理垫款
			 * 5-外汇保函垫款
			 * 
			 * */
			//查询垫款台账对应的业务借据号
			String pad_bill_no = (String)SqlClient.queryFirst("queryPadBillNo", DUEBILL_NO, null, connection);
			if("SETL".equals(DUEBILL_STATUS)){//结清
				//核销垫款台账
				Map param = new HashMap();
				param.put("status", "9");
				param.put("balance", DUEBILL_BALANCE);
				param.put("tran_date", TRAN_DATE);
				SqlClient.update("AccPadOver", DUEBILL_NO, param, null, connection);
				//根据业务类型找到垫款源更新台账状态为结清（仅需要处理贴现垫款）
				Map param1 = new HashMap();
				param1.put("status", "9");
				param1.put("tran_date", TRAN_DATE);
				if(BUSS_TYPE.equals("DISC")){//贴现
					SqlClient.update("AccDrftOver", DUEBILL_NO, param1, null, connection);

					//根据汇票号码更新票据状态
					SqlClient.update("updateBillStatusForPad", DUEBILL_NO, "04", null, connection);
					
					//根据借据号查询合同下是否有未结清借据
					BigDecimal count_dk = (BigDecimal)SqlClient.queryFirst("queryLoanBillFromContForDK", DUEBILL_NO, null, connection);
					int j = Integer.parseInt(count_dk.toString());
					if(j==0){//合同下无未结清借据，接下来判断合同余额是否为0
						//获取合同余额进行判断
						BigDecimal contBalance = (BigDecimal)SqlClient.queryFirst("queryContBalanceForDK", DUEBILL_NO, null, connection);
						if(contBalance == null){
							contBalance = new BigDecimal(0);
						}
						if(Integer.parseInt(contBalance.toString()) == 0){//合同余额为0,执行注销合同操作
							Map paramForCont = new HashMap();
							paramForCont.put("status", "900");
							paramForCont.put("tran_date", TRAN_DATE);
							SqlClient.update("cancelCtrLoanContByBillNoForDK", DUEBILL_NO, paramForCont, null, connection);
							//解除合同和担保合同关系
							SqlClient.update("cancelGrtLoanGurForDK", DUEBILL_NO, null, null, connection);
						}
					}
//				else if(BUSS_TYPE.equals("ACCP")){//银票
//					SqlClient.update("updateAccAccpStatus", DUEBILL_NO, param1, null, connection);
				}else if(BUSS_TYPE.equals("GUTR")){//保函：境内保函需要改成核销，外汇保函不用核销后续需要付汇交易
					KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryAccLoanByBillNo", DUEBILL_NO, null, connection);
					if(kColl!=null){
						String prd_id = (String) kColl.getDataValue("prd_id");
						if("400021".equals(prd_id)){
							SqlClient.update("AccLoanOver", DUEBILL_NO, param, null, connection);
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
						}
					}
				}
				
			}else{//未结清
				SqlClient.update("updateAccPadAmt", DUEBILL_NO, DUEBILL_BALANCE, null, connection);
			}
			EMPLog.log("TradePadRepayInform", EMPLog.INFO, 0, "【垫款还款通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【垫款还款通知】,业务处理失败！借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradePadRepayInform", EMPLog.ERROR, 0, "【垫款还款通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
