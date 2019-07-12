package com.yucheng.cmis.biz01line.esb.op.trade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 展期成功通知 11002000080_22
 * @author Pansq
 * 通过借据编号查询出贷款台账的到期日，展期次数
 * 根据借据号更新贷款台账表的原到期日为步骤1查询出的到期日，将到期日更新为新到期日，展期次数+1
 * 根据借据号更新授权表的状态为授权已确认
 * 
 */
public class TrandExtensionInform extends ESBTranService {

	@Override
	public KeyedCollection doExecute(KeyedCollection kColl, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		KeyedCollection result = null;
		String DUEBILL_NO = "";
		try { 
			KeyedCollection reqBody = (KeyedCollection)kColl.get("BODY");
//			解析必输字段
			DUEBILL_NO = (String)reqBody.getDataValue("DblNo");//借据号
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("AcctTxnDt"));//交易日期
			String BRANCH_ID = (String)reqBody.getDataValue("TxnInstCd");//机构代码
			String NEW_END_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("NewExprtnDt"));//新到期日
			String BASE_ACCT_NO = (String)reqBody.getDataValue("LoanNo");//贷款号 
			String ACCT_SEQ_NO = (String)reqBody.getDataValue("LoanDstrbtNo");//发放号
//			String NEW_END_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("NEW_EXPIRY_DATE"));//展期到期日
			
//			String GEN_GL_NO = (String)reqBody.getDataValue("GEN_GL_NO");//授权号
//			String BUSS_TYPE = (String)reqBody.getDataValue("BUSS_TYPE");//业务类型
			//根据借据号查询贷款台账表
			result = (KeyedCollection)SqlClient.queryFirst("queryAccLoanByBillNo", DUEBILL_NO, null, connection);
			if(result!=null){
				Map<String,Object> _param = new HashMap<String, Object>();
				_param.put("base_acct_no", BASE_ACCT_NO);
				_param.put("acct_seq_no", ACCT_SEQ_NO);
				KeyedCollection extendkc = (KeyedCollection)SqlClient.queryFirst("queryExtendInfoByAuthorizeNo", _param, null, connection);
				BigDecimal base_rate = (BigDecimal)extendkc.getDataValue("base_rate");//展期基准利率
				BigDecimal extension_rate = (BigDecimal)extendkc.getDataValue("extension_rate");//展期利率
				BigDecimal overdue_rate = BigDecimalUtil.replaceNull((BigDecimal)extendkc.getDataValue("overdue_rate"));//逾期利率浮动比例
				BigDecimal default_rate = BigDecimalUtil.replaceNull((BigDecimal)extendkc.getDataValue("default_rate"));//罚息利率浮动比
				
				String end_date = (String)result.getDataValue("end_date");
				BigDecimal count = (BigDecimal)result.getDataValue("post_count");
				int i = Integer.parseInt(count.toString());
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("end_date", end_date);//原到期日
				param.put("count", i+1);//展期次数
				param.put("new_end_date", NEW_END_DATE);//新到期日
				param.put("base_rate", base_rate);//展期基准利率
				param.put("extension_rate", extension_rate);//展期利率
				param.put("overdue_rate_y", extension_rate.multiply(overdue_rate.add(new BigDecimal(1.00))));//展期后逾期利率
				param.put("default_rate_y", extension_rate.multiply(default_rate.add(new BigDecimal(1.00))));//展期后违约利率
				//更新贷款台账的原到期日，到期日，展期次数
				SqlClient.update("updateAccLoanForExtend", DUEBILL_NO, param, null, connection);
			}
			//根据借据号更新授权表的状态为授权已确认
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("bill_no", DUEBILL_NO);
//			param.put("authorize_no", GEN_GL_NO);
			param.put("base_acct_no", BASE_ACCT_NO);
			param.put("acct_seq_no", ACCT_SEQ_NO);
			SqlClient.update("updatePvpAuthorizeStatusByAuthNo", param, "04", null, connection);
			retKColl.put("RetCd", "000000");
			retKColl.put("RetInf", "【展期成功通知】,交易成功");
			EMPLog.log("TrandExtensionInform", EMPLog.INFO, 0, "【展期成功通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.put("RetCd", "999999");
			retKColl.put("RetInf", "【展期成功通知】,业务逻辑处理异常！借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TrandExtensionInform", EMPLog.ERROR, 0, "【展期成功通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}
 

}
