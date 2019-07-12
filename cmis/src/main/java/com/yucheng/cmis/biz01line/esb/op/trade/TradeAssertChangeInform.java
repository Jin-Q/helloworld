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
 * 资产变动通知
 * @author Pansq
 * 根据交易类型判断是资产买入或者卖出
 * 资产买入：1.根据借据号更新贷款台账的台账状态为正常，
 * 			2.根据借据号更新资产转让台账的台账状态为正常，转让日期为交易日期
 * 资产卖出: 1.根据原借据号更新贷款台账的台账状态为核销 
 *          2.根据借据编号查询贷款台账获取到对应的合同编号,再通过合同编号获取所有借据，若合同下借据已经全部结清且合同余额为0则注销(900)合同（合同状态，注销日期）,否则不做处理
 *          3.根据借据号更新资产转让台账的台账状态为正常，转让日期为交易日期
 * 根据借据号更新授权表的状态为授权已确认
 *          
 */
public class TradeAssertChangeInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			String ORI_DUEBILL_NO = (String)reqBody.getDataValue("ORI_DUEBILL_NO");//原借据号
			BigDecimal TRAN_AMT = BigDecimalUtil.replaceNull(reqBody.getDataValue("TRAN_AMT"));//交易金额
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("TRAN_DATE"));//交易日期
			String TRAN_TYPE = (String)reqBody.getDataValue("TRAN_TYPE");//交易类型
			String BRANCH_ID = (String)reqBody.getDataValue("BRANCH_ID");//机构代码
			Map param = new HashMap();
			param.put("status", "1");
			param.put("tran_date", TRAN_DATE);
			Map paramForLoan = new HashMap();
			paramForLoan.put("tran_date", TRAN_DATE);
			
			if(TRAN_TYPE.equals("01")){//买入
				//根据借据号更新贷款台账为正常（资产买入并没有在贷款台账生成新的借据）
//				paramForLoan.put("status", "1");
//				SqlClient.update("updateAccLoanStatusAndBalanceOnly", DUEBILL_NO, paramForLoan, null, connection);
				//更新资产转让台账为正常
				SqlClient.update("updateAccAssetStatus", DUEBILL_NO, param, null, connection);
				//同时更新accloan台账状态为正常
				SqlClient.update("updateAccLoanStatus", DUEBILL_NO, "1", null, connection);
			}else if(TRAN_TYPE.equals("02")){//卖出未结清
				//先判断转让台账是否来自垫款台账
				BigDecimal pad_count = (BigDecimal)SqlClient.queryFirst("queryLoanBillFromAccPad", ORI_DUEBILL_NO, null, connection);
				int pad = Integer.parseInt(pad_count.toString());
				if(pad==0){//不是来自垫款，来自普通贷款
					//更新余额
					SqlClient.update("updateAccLoanAmt", ORI_DUEBILL_NO, TRAN_AMT, null, connection);
					
					//卖出成功后需要修改卖出资产登记状态
					SqlClient.update("updateIqpAverageAssetByBillNo", ORI_DUEBILL_NO, "3", null, connection);
				}else{//来自垫款
					//更新余额
					SqlClient.update("updateAccPadAmt", ORI_DUEBILL_NO, TRAN_AMT, null, connection);
					
					//卖出成功后需要修改卖出资产登记状态
					SqlClient.update("updateIqpAverageAssetByBillNo", ORI_DUEBILL_NO, "3", null, connection);
				}
				
				//判断收息方式
				String interest_type = (String)SqlClient.queryFirst("queryInterestTypeByBillNo", DUEBILL_NO, null, connection);
				if(interest_type==null){
					throw new Exception("获取不到收息方式！");
				}
				if(interest_type.equals("1")){//自主收息：更新转让台账为核销
					//更新资产转让台账为核销
					param.put("status", "9");
					SqlClient.update("updateAccAssetStatus", DUEBILL_NO, param, null, connection);
				}else{//代收：更新转让台账为正常
					//更新资产转让台账为正常
					SqlClient.update("updateAccAssetStatus", DUEBILL_NO, param, null, connection);
				}
			}else{//卖出结清
				//先判断转让台账是否来自垫款台账
				BigDecimal pad_count = (BigDecimal)SqlClient.queryFirst("queryLoanBillFromAccPad", ORI_DUEBILL_NO, null, connection);
				int pad = Integer.parseInt(pad_count.toString());
				if(pad==0){//不是来自垫款，来自普通贷款
					//根据原借据编号更新贷款台账为核销
					paramForLoan.put("status", "9");
					paramForLoan.put("balance", 0);
					//核销贷款台账
					SqlClient.update("AccLoanOver", ORI_DUEBILL_NO, paramForLoan, null, connection);//贷款台账
					//根据借据号查询合同下是否有未结清借据
					BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryLoanBillFromCont", ORI_DUEBILL_NO, null, connection);
					int i = Integer.parseInt(count.toString());
					if(i==0){//合同下无未结清借据，接下来判断合同余额是否为0
						//获取合同余额进行判断
						BigDecimal contBalance = (BigDecimal)SqlClient.queryFirst("queryContBalance", ORI_DUEBILL_NO, null, connection);
						if(contBalance == null){
							contBalance = new BigDecimal(0);
						}
						if(Integer.parseInt(contBalance.toString()) == 0){//合同余额为0,执行注销合同操作
							Map paramForCont = new HashMap();
							paramForCont.put("status", "900");
							paramForCont.put("tran_date", TRAN_DATE);
							SqlClient.update("cancelCtrLoanContByBillNo", ORI_DUEBILL_NO, paramForCont, null, connection);
							//解除合同和担保合同关系
							SqlClient.update("cancelGrtLoanGur", ORI_DUEBILL_NO, null, null, connection);
						}
					}
					
					//卖出成功后需要修改卖出资产登记状态
					SqlClient.update("updateIqpAverageAssetByBillNo", ORI_DUEBILL_NO, "3", null, connection);
				}else{//来自垫款
					//根据原借据编号更新贷款台账为核销
					paramForLoan.put("status", "9");
					paramForLoan.put("balance", 0);
					//核销垫款台账
					SqlClient.update("AccLoanOverForDK", ORI_DUEBILL_NO, paramForLoan, null, connection);//垫款台账
					
					//同时核销原台账
					Map param1 = new HashMap();
					param1.put("status", "9");
					param1.put("tran_date", TRAN_DATE);
					SqlClient.update("AccDrftOver", ORI_DUEBILL_NO, param1, null, connection);//贴现
					SqlClient.update("updateAccAccpStatus", ORI_DUEBILL_NO, param1, null, connection);//银票
					SqlClient.update("AccLoanOver", ORI_DUEBILL_NO, paramForLoan, null, connection);//保函
					
					//根据借据号查询合同下是否有未结清借据
					BigDecimal count_dk = (BigDecimal)SqlClient.queryFirst("queryLoanBillFromContForDK", ORI_DUEBILL_NO, null, connection);
					int j = Integer.parseInt(count_dk.toString());
					if(j==0){//合同下无未结清借据，接下来判断合同余额是否为0
						//获取合同余额进行判断
						BigDecimal contBalance = (BigDecimal)SqlClient.queryFirst("queryContBalanceForDK", ORI_DUEBILL_NO, null, connection);
						if(contBalance == null){
							contBalance = new BigDecimal(0);
						}
						if(Integer.parseInt(contBalance.toString()) == 0){//合同余额为0,执行注销合同操作
							Map paramForCont = new HashMap();
							paramForCont.put("status", "900");
							paramForCont.put("tran_date", TRAN_DATE);
							SqlClient.update("cancelCtrLoanContByBillNoForDK", ORI_DUEBILL_NO, paramForCont, null, connection);
							//解除合同和担保合同关系
							SqlClient.update("cancelGrtLoanGurForDK", ORI_DUEBILL_NO, null, null, connection);
						}
					}
					
					//卖出成功后需要修改卖出资产登记状态
					SqlClient.update("updateIqpAverageAssetByBillNo", ORI_DUEBILL_NO, "3", null, connection);
				}
				
				//判断收息方式
				String interest_type = (String)SqlClient.queryFirst("queryInterestTypeByBillNo", DUEBILL_NO, null, connection);
				if(interest_type==null){
					throw new Exception("获取不到收息方式！");
				}
				if(interest_type.equals("1")){//自主收息：更新转让台账为核销
					//更新资产转让台账为核销
					param.put("status", "9");
					SqlClient.update("updateAccAssetStatus", DUEBILL_NO, param, null, connection);
				}else{//代收：更新转让台账为正常
					//更新资产转让台账为正常
					SqlClient.update("updateAccAssetStatus", DUEBILL_NO, param, null, connection);
				}
				
			}
			
			//判断批次包下是否存在未办理的业务，全部办结则修改批次包为已办结
			BigDecimal acccount = (BigDecimal)SqlClient.queryFirst("queryAccAssetstrsfStsByBillNo", DUEBILL_NO, null, connection);
			int j = Integer.parseInt(acccount.toString());
			if(j==0){
				//根据借据号找到批次号更新资产批次包状态
				SqlClient.update("updateIqpAssetByBillNo", DUEBILL_NO, "03", null, connection);
			}
			//根据借据号更新授权表的状态为授权已确认
			SqlClient.update("updatePvpAuthorizeStatus", DUEBILL_NO, "04", null, connection);
			
			EMPLog.log("TradeAssertChangeInform", EMPLog.INFO, 0, "【资产变动通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【资产变动通知】,业务逻辑处理异常！借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeAssertChangeInform", EMPLog.ERROR, 0, "【资产变动通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
