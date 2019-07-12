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
 * 信用证/保函撤销通知
 * @author Pansq
 * 根据借据号更新贷款台账表状态为9核销，贷款余额为0，核销日期为交易日期
 * 根据借据编号查询贷款台账获取到对应的合同编号,再通过合同编号获取所有借据，若合同下借据已经全部结清且合同余额为0则注销(900)合同（合同状态，注销日期）,否则不做处理
 * 
 */
public class TradeMyrzbwywcxInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			KeyedCollection head = esbInterface.getReqSysHead(CD);
			DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			String TRAN_DATE = TagUtil.formatDate2Ten(head.getDataValue("TRAN_DATE"));//交易日期
			Map param = new HashMap();
			//核销贷款台账
			param.put("status", "9");
			param.put("tran_date", TRAN_DATE);
			param.put("balance", 0);
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
			//根据借据号更新授权表的状态为授权已确认
			SqlClient.update("updatePvpAuthorizeStatus", DUEBILL_NO, "04", null, connection);
			EMPLog.log("TradeMyrzbwywcxInform", EMPLog.INFO, 0, "【贸易融资表外业务撤销通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【贸易融资表外业务撤销通知】 ,业务处理失败！借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeMyrzbwywcxInform", EMPLog.ERROR, 0, "【贸易融资表外业务撤销通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
