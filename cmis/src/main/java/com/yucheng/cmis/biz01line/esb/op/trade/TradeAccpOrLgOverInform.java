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
 * 银票/保函到期解付核销通知
 * @author Pansq
 * 根据业务类型区分：
 * 银票：
 *     1.核销银票台账
 *     2.根据借据编号查询银票台账获取到对应的合同编号,再通过合同编号获取所有借据，若合同下借据已经全部结清且合同余额为0则注销(900)合同（合同状态，注销日期）,否则不做处理
 * 保函：
 *     1.核销贷款台账
       2.根据借据编号查询贷款台账获取到对应的合同编号,再通过合同编号获取所有借据，若合同下借据已经全部结清且合同余额为0则注销(900)合同（合同状态，注销日期）,否则不做处理
 * 
 */
public class TradeAccpOrLgOverInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			String BUSS_TYPE = (String)reqBody.getDataValue("BUSS_TYPE");//业务类型
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("TRAN_DATE"));//交易日期
			String BILL_NO = (String)reqBody.getDataValue("BILL_NO");//汇票号码
			//String DUEBILL_STATUS = (String)reqBody.getDataValue("DUEBILL_STATUS");//借据状态
			String BRANCH_ID = (String)reqBody.getDataValue("BRANCH_ID");//机构代码
			
			Map param = new HashMap();
			//判断业务类型
			if("01".equals(BUSS_TYPE)){//银票
				//如果存在未结清垫款，不能核销台账
				BigDecimal pad_count = (BigDecimal)SqlClient.queryFirst("queryCountFromAccPad", DUEBILL_NO, null, connection);
				int pad = Integer.parseInt(pad_count.toString());
				if(pad==0){//不存在未结清的垫款
					//核销银票台账
					Map paramForAccp = new HashMap();
					paramForAccp.put("status", "9");
					paramForAccp.put("tran_date", TRAN_DATE);
					SqlClient.update("updateAccAccpStatus", DUEBILL_NO, paramForAccp, null, connection);
					//根据借据号查询合同下是否有未结清借据
					BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryAccpBillFromCont", DUEBILL_NO, null, connection);
					int i = Integer.parseInt(count.toString());
					if(i==0){//合同下无未结清借据，接下来判断合同余额是否为0
						BigDecimal contBalance = (BigDecimal)SqlClient.queryFirst("queryContBalanceForAccp", DUEBILL_NO, null, connection);
						if(contBalance == null){
							contBalance = new BigDecimal(0);
						}
						if(Integer.parseInt(contBalance.toString()) == 0){//合同余额为0,执行注销合同操作
							Map paramForCont = new HashMap();
							paramForCont.put("status", "900");
							paramForCont.put("tran_date", TRAN_DATE);
							SqlClient.update("cancelCtrLoanContByBillNoAccp", DUEBILL_NO, paramForCont, null, connection);
							//解除合同和担保合同关系
							SqlClient.update("cancelGrtLoanGurForAccp", DUEBILL_NO, null, null, connection);
						}
					}
				}
				
			}else {//保函
				//核销贷款台账
				Map paramForLoan = new HashMap();
				paramForLoan.put("status", "9");
				paramForLoan.put("balance", 0);
				paramForLoan.put("tran_date", TRAN_DATE);
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
						//解除合同和担保合同关系
						SqlClient.update("cancelGrtLoanGur", DUEBILL_NO, null, null, connection);
					}
				}
			}
			EMPLog.log("TradeAccpOrLgOverInform", EMPLog.INFO, 0, "【银票/保函到期解付核销通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【银票/保函到期解付核销通知】 ,业务处理失败！借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeAccpOrLgOverInform", EMPLog.ERROR, 0, "【银票/保函到期解付核销通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
