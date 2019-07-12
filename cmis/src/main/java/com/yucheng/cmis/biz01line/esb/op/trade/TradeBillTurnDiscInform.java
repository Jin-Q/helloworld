package com.yucheng.cmis.biz01line.esb.op.trade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 票据转贴现放款通知
 * @author Pansq
 * 说明：
 * 更新转贴现对应的票据流水台账状态为核销
 * 更新票据状态为核销
 * 更新票据批次包状态为已办结
 * 根据借据号更新授权表的状态为授权已确认
 * 
 * 特殊处理：
 * 1、后续的返售和回购操作没有授权，只进行票据状态更新
 * 2、核心柜面，买入、内转、回购是按合同做的，所以根据合同去更新数据。
 *             卖出、返售是按借据做的，所以根据借据号和票号去更新数据。
 * 3、商票贴现直贴不能核销，需要等到商票扣款时去核销
 * 
 */
public class TradeBillTurnDiscInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String CONTRACT_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			String DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			CONTRACT_NO = (String)reqBody.getDataValue("CONTRACT_NO");//合同号（内部转贴现需要）
			String BILL_NO = (String)reqBody.getDataValue("BILL_NO");//汇票号码
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("TRAN_DATE"));//交易日期
			String BRANCH_ID = (String)reqBody.getDataValue("BRANCH_ID");//机构代码
			
			String dscnt_type = (String)SqlClient.queryFirst("queryAccDrftByContNo", CONTRACT_NO, null, connection);
			
			HashMap hm = new HashMap();
			hm.put("status", "9");
			hm.put("tran_date", TRAN_DATE);
			
			Boolean flag = false;//是否为返售和回购操作，true为是，false为否
			if(dscnt_type.equals("02")){//买入返售：判断是否是返售操作，返售和回购操作没有授权，只进行票据状态更新
				//根据票据状态判断是，买入返售还是返售
				String status = (String)SqlClient.queryFirst("queryStatusByPorderNo", BILL_NO, null, connection);
				if(status!=null&&!"".equals(status)){
					if(status.equals("02")){//持有票据说明是返售操作
						//根据汇票号和台账状态更新原票据流水台账为核销
						SqlClient.update("AccDrftOverByPorderNo", BILL_NO, hm, null, connection);
						//根据汇票号码更新票据状态
						SqlClient.update("updateBillStateByPorderNo", BILL_NO, "04", null, connection);
						//根据借据号更新票据流水台帐状态为核销
						SqlClient.update("AccDrftOver", DUEBILL_NO, hm, null, connection);
					    flag=true;
					}else{
						//根据汇票号码更新票据状态
						SqlClient.update("updateBillStateByContNo", CONTRACT_NO, "02", null, connection);
						//根据借据号更新票据流水台帐状态
					    SqlClient.update("updateAccDrftStatusByContNo", CONTRACT_NO, "1", null, connection);
					}
				}else{
					//根据汇票号码更新票据状态
					SqlClient.update("updateBillStateByContNo", CONTRACT_NO, "02", null, connection);
					//根据借据号更新票据流水台帐状态
				    SqlClient.update("updateAccDrftStatusByContNo", CONTRACT_NO, "1", null, connection);
				}
				
			}else if(dscnt_type.equals("04")){//卖出回购：判断是否是回购操作，返售和回购操作没有授权，只进行票据状态更新
				//根据票据状态判断是，卖出回购还是回购
				String status = (String)SqlClient.queryFirst("queryStatusByPorderNo", BILL_NO, null, connection);
				if(status.equals("05")){//质押状态说明是回购操作
					//根据汇票号码更新票据状态
					SqlClient.update("updateBillStateByPorderNo", BILL_NO, "02", null, connection);
					//根据借据号更新票据流水台帐状态
//					SqlClient.update("AccDrftOver", DUEBILL_NO, hm, null, connection);回购后仍为正常
				    flag=true;
				}else{
					//根据汇票号和台账状态更新原票据流水台账为核销
					SqlClient.update("AccDrftOverByPorderNo", BILL_NO, hm, null, connection);
					//根据汇票号码更新票据状态
					SqlClient.update("updateBillStateByPorderNo", BILL_NO, "05", null, connection);//卖出后改成质押状态
					//根据借据号更新票据流水台帐状态为核销
//					SqlClient.update("updateAccDrftStatus", DUEBILL_NO, "9", null, connection);
					SqlClient.update("updateAccDrftStatus", DUEBILL_NO, "1", null, connection);
				}
			}else if(dscnt_type.equals("01")){//转贴现买入更新票据状态为持有
				//根据汇票号码更新票据状态
				 SqlClient.update("updateBillStateByContNo", CONTRACT_NO, "02", null, connection);
				//根据借据号更新票据流水台帐状态
			    SqlClient.update("updateAccDrftStatusByContNo", CONTRACT_NO, "1", null, connection);
			}else if(dscnt_type.equals("03")){//转贴现卖出，根据票据类型
				String bill_type = (String)SqlClient.queryFirst("queryBillTypeByPorderNo", BILL_NO, null, connection);
				if("100".equals(bill_type)){//银票
					//根据汇票号和台账状态更新原票据流水台账为核销
					SqlClient.update("AccDrftOverByPorderNo", BILL_NO, hm, null, connection);
					//根据汇票号码更新票据状态
					 SqlClient.update("updateBillStateByPorderNo", BILL_NO, "04", null, connection);
					//根据借据号更新票据流水台帐状态为核销
					 SqlClient.update("AccDrftOver", DUEBILL_NO, hm, null, connection);
				}else{//商票
					//判断是否做过直贴，直贴为我行商贴不能结清，买入商贴可以直接结清
					BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryCountDiscAccDrftByPorderNo", BILL_NO, null, connection);
					int j = Integer.parseInt(count.toString());
					if(j==1){
						//根据汇票号和台账状态更新原票据流水台账为核销
						SqlClient.update("AccDrftOverByPorderNo", BILL_NO, hm, null, connection);
						//根据汇票号码更新票据状态
						SqlClient.update("updateBillStateByPorderNo", BILL_NO, "08", null, connection);//改成转贴现卖断状态  2014-07-15 确认新增转贴现卖断状态，我行商票使用
						//根据借据号更新票据流水台帐状态为正常
						SqlClient.update("AccDrftOver", DUEBILL_NO, hm, null, connection);
					}else{
						//根据汇票号和台账状态更新原票据流水台账为核销
						SqlClient.update("AccDrftOverByPorderNo", BILL_NO, hm, null, connection);
						//根据汇票号码更新票据状态
						SqlClient.update("updateBillStateByPorderNo", BILL_NO, "04", null, connection);
						//根据借据号更新票据流水台帐状态为核销
						SqlClient.update("AccDrftOver", DUEBILL_NO, hm, null, connection);
					}
				}
				
			}else if(dscnt_type.equals("06")){//再贴更新原票据台账状态为核销，票据状态为核销
				 
				String bill_type = (String)SqlClient.queryFirst("queryBillTypeByPorderNo", BILL_NO, null, connection);
				if("100".equals(bill_type)){//银票
					//根据汇票号和台账状态更新原票据流水台账为核销
					SqlClient.update("AccDrftOverByPorderNo", BILL_NO, hm, null, connection);
					//根据汇票号码更新票据状态
					 SqlClient.update("updateBillStateByPorderNo", BILL_NO, "04", null, connection);
					//根据借据号更新票据流水台帐状态为核销
					 SqlClient.update("AccDrftOver", DUEBILL_NO, hm, null, connection);
				}else{//商票
					//判断是否做过直贴，直贴为我行商贴不能结清，买入商贴可以直接结清
					BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryCountDiscAccDrftByPorderNo", BILL_NO, null, connection);
					int j = Integer.parseInt(count.toString());
					if(j==1){
						//根据汇票号和台账状态更新原票据流水台账为核销
						SqlClient.update("AccDrftOverByPorderNo", BILL_NO, hm, null, connection);
						//根据汇票号码更新票据状态
						SqlClient.update("updateBillStateByPorderNo", BILL_NO, "05", null, connection);//改成质押状态
						//根据借据号更新票据流水台帐状态为正常
						SqlClient.update("AccDrftOver", DUEBILL_NO, hm, null, connection);
					}else{
						//根据汇票号和台账状态更新原票据流水台账为核销
						SqlClient.update("AccDrftOverByPorderNo", BILL_NO, hm, null, connection);
						//根据汇票号码更新票据状态
						SqlClient.update("updateBillStateByPorderNo", BILL_NO, "04", null, connection);
						//根据借据号更新票据流水台帐状态为核销
						SqlClient.update("AccDrftOver", DUEBILL_NO, hm, null, connection);
					}
				}
				 
			}else if(dscnt_type.equals("05")){//内部转贴现更新原票据流水台账为核销，更新票据流水台账为正常
				//根据合同号更新原票据流水台账为核销
				SqlClient.update("AccDrftOverByContNo", CONTRACT_NO, hm, null, connection);
				//根据合同号更新票据流水台帐状态
				SqlClient.update("updateAccDrftStatusByContNo", CONTRACT_NO, "1", null, connection);
			}
			
			if(!flag){
				//判断批次包下是否存在未办理的业务，全部办结则修改批次包为已办结
				BigDecimal acccount = (BigDecimal)SqlClient.queryFirst("queryAccDrftStsByBillNo", DUEBILL_NO, null, connection);
				int j = Integer.parseInt(acccount.toString());
				if(j==0){
					 //根据合同号更新票据批次包状态
					 SqlClient.update("updateBatchMngByContNo", CONTRACT_NO, "03", null, connection);
				 }
				//根据合同号更新授权表的状态为授权已确认
				SqlClient.update("updatePvpAuthorizeStatusByContNo", CONTRACT_NO, "04", null, connection);
			}
			
			//获取未结清的合同，对满足结清条件的合同进行结清
			IndexedCollection ctrloanic = (IndexedCollection)SqlClient.queryList4IColl("queryCtrLoanContByContNoForJQ", CONTRACT_NO, connection);
			for(int a=0;a<ctrloanic.size();a++){
				KeyedCollection ctrloankc = (KeyedCollection) ctrloanic.get(a);
				String cont_no = (String) ctrloankc.getDataValue("cont_no");
				//查询合同下是否有存在未结清的票据流水
				BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryDrftFromContByContNo", cont_no, null, connection);
				int i = Integer.parseInt(count.toString());
				if(i==0){//合同下无未结清借据，接下来判断合同余额是否为0
					BigDecimal contBalance = (BigDecimal)SqlClient.queryFirst("queryContBalanceForDrftByContNo", cont_no, null, connection);
					if(contBalance == null){
						contBalance = new BigDecimal(0);
					}
					if(Integer.parseInt(contBalance.toString()) == 0){//合同余额为0,执行注销合同操作
						Map paramForCont = new HashMap();
						paramForCont.put("status", "900");
						paramForCont.put("tran_date", TRAN_DATE);
						SqlClient.update("cancelCtrLoanContByContNoForDrft", cont_no, paramForCont, null, connection);
						//解除合同和担保合同关系
						SqlClient.update("cancelGrtLoanGurForDrftByContNo", cont_no, null, null, connection);
					}
				}
			}
			IndexedCollection ctrrpddic = (IndexedCollection)SqlClient.queryList4IColl("queryCtrRpddscntContByContNoForJQ", CONTRACT_NO, connection);
			for(int b=0;b<ctrrpddic.size();b++){
				KeyedCollection ctrrpddkc = (KeyedCollection) ctrrpddic.get(b);
				String cont_no = (String) ctrrpddkc.getDataValue("cont_no");
				//查询合同下是否有存在未结清的票据流水
				BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryDrftFromContByContNo", cont_no, null, connection);
				int i = Integer.parseInt(count.toString());
				if(i==0){//合同下无未结清借据则注销合同
					Map paramForCont = new HashMap();
					paramForCont.put("status", "900");
					paramForCont.put("tran_date", TRAN_DATE);
					SqlClient.update("cancelCtrRpddscntContByContNoForDrft", cont_no, paramForCont, null, connection);
					//解除合同和担保合同关系
					SqlClient.update("cancelGrtLoanGurForDrftByContNo", cont_no, null, null, connection);
				}
			}
			 
			EMPLog.log("TradeBillTurnDiscInform", EMPLog.INFO, 0, "【票据转贴现放款通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【票据转贴现放款通知】,业务逻辑处理异常！合同号为："+CONTRACT_NO);
			e.printStackTrace();
			EMPLog.log("TradeBillTurnDiscInform", EMPLog.ERROR, 0, "【票据转贴现放款通知】交易处理失败，借据号为："+CONTRACT_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
