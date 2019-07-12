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
 * 贸易融资表外业务回收通知
 * @author liqh
 * 根据结清标志判断,若为非结清标志则更新贷款台账余额为借据余额，若为结清标志则同时更新台账状态为核销，核销日期为交易日期
 * 若为结清且为信用证，则台账状态修改为信用证闭卷，同时更新核销日期为当前交易日期
 * 若为结清状态，则根据借据编号查询贷款台账获取到对应的合同编号,再通过合同编号获取所有借据
 * 若合同下借据已经全部结清且合同余额为0则注销(900)合同（合同状态，注销日期）,否则不做处理
 * 若合同注销则同时根据合同编号解除担保合同和业务的关系
 * 
 * 
 */
public class TradeMyrzbwywhsInform extends TranService {
	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
	KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
	String DUEBILL_NO = "";
	try {
		ESBInterface esbInterface = new ESBInterfacesImple();
		KeyedCollection sysHead = esbInterface.getReqSysHead(CD);
		String TRAN_DATE = TagUtil.formatDate2Ten(sysHead.getDataValue("TRAN_DATE"));//交易日期
		KeyedCollection reqBody = esbInterface.getReqBody(CD);
		DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
		String TRAN_AMT = reqBody.getDataValue("TRAN_AMT").toString();//交易金额
		String BALANCE = reqBody.getDataValue("BALANCE").toString();//借据余额
		String BUSS_KIND = (String)reqBody.getDataValue("BUSS_KIND");//业务品种
		String LC_NO = (String)reqBody.getDataValue("LC_NO");//信用证编号
		String CLEARANCE_FLAG = (String)reqBody.getDataValue("CLEARANCE_FLAG");//结清标志
		/** 
		 * 结清标志CLEARANCE_FLAG
		 * 01：已结清
		 * 02: 未结清
		 * 
		 * */
		Map param = new HashMap();
		if("01".equals(CLEARANCE_FLAG)){//已结清
			param.put("status", "10");
			param.put("balance", BALANCE);
			param.put("tran_date", TRAN_DATE);
			//结清贷款台账
			SqlClient.update("AccLoanOverForLC", DUEBILL_NO, param, null, connection);
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
			param.put("balance", BALANCE);
			SqlClient.update("updateAccLoanStatusAndBalanceOnly", DUEBILL_NO, param, null, connection);
		}
		EMPLog.log("TradeMyrzbwywhsInform", EMPLog.INFO, 0, "【贸易融资表外业务回收通知】交易处理完成...", null);
	} catch (Exception e) {
		retKColl.setDataValue("ret_code", "999999");
		retKColl.setDataValue("ret_msg", "【贸易融资表外业务回收通知】,业务处理失败！借据号为："+DUEBILL_NO);
		e.printStackTrace();
		EMPLog.log("TradeMyrzbwywhsInform", EMPLog.ERROR, 0, "【贸易融资表外业务回收通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
	}
	return retKColl;
}
}
