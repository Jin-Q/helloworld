package com.yucheng.cmis.biz01line.esb.op.trade;

import java.math.BigDecimal;
import java.sql.Connection;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 票据贴现放款通知
 * @author Pansq
 * 更新借据状态，分为贴现业务和贸易融资业务，贴现业务更新票据流水台账表为出账已确认，贸易融资业务更新贷款台账表为正常
 * 更新票据状态为持有
 * 更新批次包状态为已办结
 * 根据借据号更新授权表的状态为授权已确认
 * 
 */
public class TradeBillDiscInform extends TranService {
	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		int i=0;
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			String BILL_NO = (String)reqBody.getDataValue("BILL_NO");//汇票号码
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("TRAN_DATE"));//交易日期
			String BRANCH_ID = (String)reqBody.getDataValue("BRANCH_ID");//机构代码
			//根据借据号查询票据流水台账，若存在记录则为贴现业务，否则为贸易融资业务
			BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryBillNoForAccDrft", DUEBILL_NO, null, connection);
			i = Integer.parseInt(count.toString());
			 if(i==0){
				 //贸易融资业务更新贷款台账状态
				 SqlClient.update("updateAccLoanStatus", DUEBILL_NO, "1", null, connection);
			 }else{
				 //贴现业务更新信贷系统本地票据台帐流水状态
				 SqlClient.update("updateAccDrftStatus", DUEBILL_NO, "1", null, connection);
				 //根据汇票号码更新票据状态
				 SqlClient.update("updateBillStateByPorderNo", BILL_NO, "02", null, connection);
				 
				 //判断批次包下是否存在未办理的业务，全部办结则修改批次包为已办结
				 BigDecimal acccount = (BigDecimal)SqlClient.queryFirst("queryAccDrftStsByBillNo", DUEBILL_NO, null, connection);
				 int j = Integer.parseInt(acccount.toString());
				 if(j==0){
					//根据借据号找到批次号更新票据批次包状态
					 SqlClient.update("updateBatchMngByBillNo", DUEBILL_NO, "03", null, connection);
				 }
			 }
			//根据借据号更新授权表的状态为授权已确认
			SqlClient.update("updatePvpAuthorizeStatus", DUEBILL_NO, "04", null, connection);
			 
			EMPLog.log("TradeBillDiscInform", EMPLog.INFO, 0, "【票据贴现放款通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【票据贴现放款通知】,业务逻辑处理异常！借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeBillDiscInform", EMPLog.ERROR, 0, "【票据贴现放款通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
