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
 * 呆账核销通知 11002000080_23
 * @author Pansq
 * 说明：呆账核销通知（核销本金、核销利息）待定。。。
 */
public class TradeBadCancelInform extends ESBTranService {

	@Override
	public KeyedCollection doExecute(KeyedCollection kColl,Connection connection)throws Exception {
		KeyedCollection retkColl = new KeyedCollection();
		String DUEBILL_NO ="";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = (KeyedCollection)kColl.get("BODY");
//			解析必输字段
			DUEBILL_NO = (String)reqBody.getDataValue("DblNo");	//借据号
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("AcctTxnDt"));	//交易日期
			String BRANCH_ID = (String)reqBody.getDataValue("TxnInstCd");//机构代码
			String VERIFY_CORPUS = reqBody.getDataValue("CnclPnpAmt").toString();// 核销本金
			String VERIFY_INTEREST = reqBody.getDataValue("CnclPnpInt").toString();// 核销利息
			String DUEBILL_STATUS = (String)reqBody.getDataValue("DbllSt");// 借据状态  （核心系统状态： -NBAP 未放款 ;-ACTV 已发放 ;-SETL 已结清）
			
			/*** 先改授权状态，确认收到通知：02(已授权) -> 04(授权已确认) ***/
			SqlClient.update("setBadCancelAuthorize", DUEBILL_NO, null, null, connection);
			
			/*** 再做业务处理：台账状态核销 ***/
			//先判断转让台账是否来自垫款台账
			BigDecimal pad_count = (BigDecimal)SqlClient.queryFirst("queryLoanBillFromAccPad", DUEBILL_NO, null, connection);
			int pad = Integer.parseInt(pad_count.toString());
			if(pad==0){//不是来自垫款，来自普通贷款
				SqlClient.update("setBadCancelAcc", DUEBILL_NO, TRAN_DATE , null, connection);
			}else{
				SqlClient.update("setBadCancelAccForPad", DUEBILL_NO, TRAN_DATE , null, connection);
			}
			
			/**更新呆账核销台账的核销日期为当前营业日*/
			SqlClient.update("setBadCancelArpDbtWriteoffAcc", DUEBILL_NO, TRAN_DATE , null, connection);
			retkColl.put("RetCd", "000000");
			retkColl.put("RetInf", "【核销成功通知】,交易成功");
			EMPLog.log("TradeDzhxtzInform", EMPLog.INFO, 0, "【呆账核销通知】交易成功！", null);
		} catch (Exception e) {
			retkColl.put("ret_code", "999999");
			retkColl.put("ret_msg", "【呆账核销通知】交易处理失败，借据编号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeDzhxtzInform", EMPLog.ERROR, 0, "【呆账核销通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retkColl;
	}

}