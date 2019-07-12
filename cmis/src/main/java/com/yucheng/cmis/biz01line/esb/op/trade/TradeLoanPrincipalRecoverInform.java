package com.yucheng.cmis.biz01line.esb.op.trade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 	贷款本金回收通知
 *  服务码：30220005
 *  场景码：03
 * @author huangtao
 * @date 2019-04-12
 * -----------------------------------------------------------------------------------------
 * 	业务逻辑：
 *	根据核心传入参数【NOTICE_TYPE 通知类型】 01--正常回收、02--提前回收、03--逾期回收、04--呆账回收
 *	进行判断走哪个处理分支
 * =========================================================================================
 *	01：正常回收 则进一步判断是否是已结清借据	
 * 		已结清：更新台账状态为核销，核销日期为交易日期，同时根据借据编号查询贷款台账获取到对应的合同编号,再通过合同编号获取所有借据
 *         若合同下借据已经全部结清且合同余额为0则注销(900)合同（合同状态，注销日期）,否则不做处理
 *         若合同注销则同时根据合同编号解除担保合同和业务的关系
 * 		未结清：更新贷款台账余额为借据余额
 * =========================================================================================
 * 	02：提前回收 则进一步判断是否是已结清借据
 * 		已结清：更新台账状态为核销，核销日期为交易日期，同时根据借据编号查询贷款台账获取到对应的合同编号,再通过合同编号获取所有借据
 *         若合同下借据已经全部结清且合同余额为0则注销(900)合同（合同状态，注销日期）,否则不做处理
 *         若合同注销则同时根据合同编号解除担保合同和业务的关系
 * 		未结清：更新贷款台账余额为借据余额
 * =========================================================================================
 * 	03：逾期回收  则进一步判断是否是已结清借据
 * 		已结清：更新台账状态为核销，核销日期为交易日期，同时根据借据编号查询贷款台账获取到对应的合同编号,再通过合同编号获取所有借据
 *         若合同下借据已经全部结清且合同余额为0则注销(900)合同（合同状态，注销日期）,否则不做处理
 *         若合同注销则同时根据合同编号解除担保合同和业务的关系
 * 		未结清：更新贷款台账余额为借据余额
 * =========================================================================================
 * 	04：呆账回收
 * 		TODO 待补充
 */
public class TradeLoanPrincipalRecoverInform extends ESBTranService {

	@Override
	public KeyedCollection doExecute(KeyedCollection kColl, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		String NOTICE_TYPE = "";
		try {
			KeyedCollection reqBody = (KeyedCollection)kColl.get("BODY");
			NOTICE_TYPE = (String)reqBody.getDataValue("NtcTp");//通知类型
			
			if(StringUtils.isBlank(NOTICE_TYPE)){ // 健壮性判断
				throw new CMISException("通知类型字段【NOTICE_TYPE】为空！");
			}
			
			DUEBILL_NO = (String)reqBody.getDataValue("DblNo");//借据号
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("AcctTxnDt"));//交易日期
			String DUEBILL_BALANCE = reqBody.getDataValue("DbllBal").toString();//借据余额
			String DUEBILL_STATUS = (String)reqBody.getDataValue("DbllSt");//借据状态
			String BRANCH_ID = (String)reqBody.getDataValue("TxnInstCd");//机构代码
			String RECYCLE_TYPE = null;    
			String TRAN_AMT = null;         
			String OWE_INT = null;          
			String OUTTABLE_OWE_INT = null; 
			String NOR_RECE_INTEREST = null;
			String REPAY_INTEREST = null;   
			String BUSS_TYPE = null;   
			String RECYCLE_CORPUS = null;   
			
			HashMap<String,Object> param = new HashMap<String,Object>();
			
			/** 根据通知类型路由对应处理分支 */
			switch (NOTICE_TYPE) {
			
			case "01": /**========================正常回收=========================*/
				
				RECYCLE_TYPE = (String)reqBody.getDataValue("RecMd");//回收方式
				TRAN_AMT = reqBody.getDataValue("TxnAmt").toString();//交易金额
				OWE_INT = reqBody.getDataValue("BalShetOwInt").toString();//欠息
				OUTTABLE_OWE_INT = reqBody.getDataValue("OBSOwInt").toString();//表外欠息
				NOR_RECE_INTEREST = reqBody.getDataValue("ComRcvblInt").toString();//正常应收利息累计
				REPAY_INTEREST = reqBody.getDataValue("ActRepymtInt").toString();//实还利息
				
				if("SETL".equals(DUEBILL_STATUS)){
					// 已结清需要的参数
					param.put("status", "9");
					param.put("balance", DUEBILL_BALANCE);
					param.put("tran_date", TRAN_DATE);
					updateAccAndContrForSetl(connection, DUEBILL_NO,DUEBILL_STATUS,param);
				}else{
					// 未结清，状态保持正常，贷款余额更新为借据余额
					param.put("balance", DUEBILL_BALANCE);
					param.put("inner_owe_int", OWE_INT);
					param.put("out_owe_int", OUTTABLE_OWE_INT);
					param.put("rec_int_accum", NOR_RECE_INTEREST);
					param.put("recv_int_accum", REPAY_INTEREST);
					SqlClient.update("updateAccLoanStatusAndBalanceForInt", DUEBILL_NO, param, null, connection);
				}
				
				break;
				
			case "02": /**========================提前回收=========================*/
				
				TRAN_AMT = reqBody.getDataValue("TxnAmt").toString();//交易金额
				OWE_INT = reqBody.getDataValue("BalShetOwInt").toString();//欠息
				OUTTABLE_OWE_INT = reqBody.getDataValue("OBSOwInt").toString();//表外欠息
				NOR_RECE_INTEREST = reqBody.getDataValue("ComRcvblInt").toString();//正常应收利息累计
				REPAY_INTEREST = reqBody.getDataValue("ActRepymtInt").toString();//实还利息
				String REPAY_TYPE = (String)reqBody.getDataValue("RepymtMd");//还款方式默认为全部提前还款 FS
				
				if("SETL".equals(DUEBILL_STATUS)){
					// 已结清需要的参数
					param.put("status", "9");
					param.put("balance", DUEBILL_BALANCE);
					param.put("tran_date", TRAN_DATE);
					updateAccAndContrForSetl(connection, DUEBILL_NO,DUEBILL_STATUS,param);
				}else{
					// 未结清，状态保持正常，贷款余额更新为借据余额
					param.put("status", "1");
					param.put("balance", DUEBILL_BALANCE);
					SqlClient.update("updateAccLoanStatusAndBalanceOnly", DUEBILL_NO, param, null, connection);
				}
				
				break;
				
			case "03": /**========================逾期回收=========================*/
				
				TRAN_AMT = reqBody.getDataValue("TxnAmt").toString();//交易金额
				OWE_INT = reqBody.getDataValue("BalShetOwInt").toString();//欠息
				OUTTABLE_OWE_INT = reqBody.getDataValue("OBSOwInt").toString();//表外欠息
				NOR_RECE_INTEREST = reqBody.getDataValue("ComRcvblInt").toString();//正常应收利息累计
				REPAY_INTEREST = reqBody.getDataValue("ActRepymtInt").toString();//实还利息

				if("SETL".equals(DUEBILL_STATUS)){
					// 已结清需要的参数
					param.put("status", "9");
					param.put("balance", DUEBILL_BALANCE);
					param.put("tran_date", TRAN_DATE);
					updateAccAndContrForSetl(connection, DUEBILL_NO,DUEBILL_STATUS,param);
				}else{
					// 未结清，状态保持正常，贷款余额更新为借据余额
					param.put("status", "1");
					param.put("balance", DUEBILL_BALANCE);
					SqlClient.update("updateAccLoanStatusAndBalanceOnly", DUEBILL_NO, param, null, connection);
				}
				
				break;
				
			case "04": /**========================呆账回收=========================*/
				
				RECYCLE_CORPUS = reqBody.getDataValue("RecPnpAmt").toString(); //回收本金
				DUEBILL_NO = (String)reqBody.getDataValue("DblNo"); //借据号
				TRAN_AMT = reqBody.getDataValue("TxnAmt").toString();//交易金额
				BigDecimal BD_RECYCLE_CORPUS = new BigDecimal(RECYCLE_CORPUS);
				BigDecimal BD_TRAN_AMT = new BigDecimal(TRAN_AMT);
				String WRITEOFF_INT_BAL = BD_RECYCLE_CORPUS.subtract(BD_TRAN_AMT).toString();
				param.put("writeoff_cap_bal", RECYCLE_CORPUS);	//核销本金
				param.put("writeoff_int_bal", WRITEOFF_INT_BAL);	//核销利息
				SqlClient.update("setBadRepayAcc", DUEBILL_NO, param , null, connection);
				
				break;
				
			default:
				break;
			}
			//恢复循环贷款额度
			repayRecycleLoanBalance(connection,DUEBILL_NO);
			retKColl.put("RetCd", "000000"); 
			retKColl.put("RetInf", "【贷款本金回收通知】,交易成功");
			EMPLog.log("TradeLoanRecoverInfrom", EMPLog.INFO, 0, "【贷款本金回收通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.put("RetCd", "999999");
			retKColl.put("RetInf", "【贷款本金回收通知】,业务处理失败！借据号为:"+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeLoanRecoverInfrom", EMPLog.ERROR, 0, "【贷款本金回收通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

	private void updateAccAndContrForSetl(Connection connection, String DUEBILL_NO,String DUEBILL_STATUS,Map<String,Object> param) throws ObjectNotFoundException, InvalidArgumentException, SQLException {
		//核销贷款台账
		SqlClient.update("AccLoanOver", DUEBILL_NO, param, null, connection);
		KeyedCollection ctrLoanContKcoll = (KeyedCollection)SqlClient.queryFirst("queryContByBillNo", DUEBILL_NO, null, connection);
		String prdId = (String)ctrLoanContKcoll.getDataValue("prd_id");
		//根据借据号查询合同下是否有未结清借据
		if(!"100039".equals(prdId)){
		BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryLoanBillFromCont", DUEBILL_NO, null, connection);
		int i = Integer.parseInt(count.toString());
		if(i==0){//合同下无未结清借据，接下来判断合同余额是否为0
			BigDecimal contBalance = (BigDecimal)SqlClient.queryFirst("queryContBalance", DUEBILL_NO, null, connection);
			if(contBalance == null){
				contBalance = new BigDecimal(0);
			}
			if(Integer.parseInt(contBalance.toString()) == 0){//合同余额为0,执行注销合同操作
				Map<String,Object> paramForCont = new HashMap<String,Object>();
				paramForCont.put("status", "900");
				paramForCont.put("tran_date", param.get("tran_date"));
				SqlClient.update("cancelCtrLoanContByBillNo", DUEBILL_NO, paramForCont, null, connection);
				//解除合同和担保合同关系
				SqlClient.update("cancelGrtLoanGur", DUEBILL_NO, null, null, connection);
			}
		}
		}
	}
	
	//循环贷款还款恢复额度,重新计算所有的余额
	private void repayRecycleLoanBalance(Connection connection,String DUEBILL_NO) throws ObjectNotFoundException, InvalidArgumentException, SQLException {
		 SqlClient.update("updateCtrLoanContBalanceRepay", DUEBILL_NO, "", null, connection);
	}
	
}
