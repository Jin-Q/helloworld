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
 * 提前还款通知 
 * @author Pansq
 * 根据借据状态判断：
 * 已结清：更新台账状态为核销，核销日期为交易日期，同时根据借据编号查询贷款台账获取到对应的合同编号,再通过合同编号获取所有借据
 *         若合同下借据已经全部结清且合同余额为0则注销(900)合同（合同状态，注销日期）,否则不做处理
 *         若合同注销则同时根据合同编号解除担保合同和业务的关系
 * 未结清：更新贷款台账余额为借据余额
 * modified by yangzy 2015/04/16 该交易已失效
 */
public class TradeAheadBackLoanInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			String TRAN_AMT = reqBody.getDataValue("TRAN_AMT").toString();//交易金额
			String DUEBILL_BALANCE = reqBody.getDataValue("DUEBILL_BALANCE").toString();//借据余额
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("TRAN_DATE"));//交易日期
			String DUEBILL_STATUS = (String)reqBody.getDataValue("DUEBILL_STATUS");//借据状态
			/** 判断借据状态信息，更新借据信息（余额）、借据状态等信息 
			 * 借据状态（核心）
			 * NBAP: 未放款
			 * ACTV: 已发放
			 * SETL: 已结清
			 * 
			 * */
			Map param = new HashMap();
			if("SETL".equals(DUEBILL_STATUS)){//已结清
				param.put("status", "9");
				param.put("balance", DUEBILL_BALANCE);
				param.put("tran_date", TRAN_DATE);
				//核销贷款台账
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
						//解除合同和担保合同关系
						SqlClient.update("cancelGrtLoanGur", DUEBILL_NO, null, null, connection);
					}
				}
			}else{//未结清，状态保持正常，贷款余额更新为借据余额
				param.put("status", "1");
				param.put("balance", DUEBILL_BALANCE);
				SqlClient.update("updateAccLoanStatusAndBalanceOnly", DUEBILL_NO, param, null, connection);
			}
			EMPLog.log("TradeAheadBackLoanInform", EMPLog.INFO, 0, "【提前还款通知 】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "提前还款通知 ,业务处理失败！借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeAheadBackLoanInform", EMPLog.ERROR, 0, "【提前还款通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
