package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 贷款发放成功通知 服务码：30220005 场景码：02
 * @author Pansq
 * 根据借据号更新贷款台账表的状态为1正常
 * 根据借据号更新授权表的状态为授权已确认
 * 
 */
public class TranLoanInform extends ESBTranService {
	
	/**
	 * 解析字段以ESB服务治理后的字段为准
	 */
	@Override
	public KeyedCollection doExecute(KeyedCollection kColl, Connection connection) throws Exception {
		KeyedCollection retKColl = new KeyedCollection();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = (KeyedCollection)kColl.getDataElement("BODY");
//			解析必输字段
			DUEBILL_NO = (String)reqBody.getDataValue("DblNo");//借据号
			String BRANCH_ID = (String)reqBody.getDataValue("TxnInstCd");//机构代码
			String BASE_ACCT_NO = (String)reqBody.getDataValue("LoanNo");//贷款号 
			String ACCT_SEQ_NO = (String)reqBody.getDataValue("LoanDstrbtNo");//发放号
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("AcctTxnDt"));//交易日期
//			解析非必输字段
			String ACCT_NO = (String)reqBody.getDataValue("AcctNo");//账号
			String TRAN_AMT = reqBody.getDataValue("TxnAmt")==null?"0":reqBody.getDataValue("TxnAmt").toString();//交易金额
			String DUEBILL_BALANCE = reqBody.get("DbllBal")==null?"0":reqBody.getDataValue("DbllBal").toString();//交易余额
			StringBuffer sb = new StringBuffer();
			sb.append("DUEBILL_NO=").append(DUEBILL_NO).append(";\r")
			.append("BRANCH_ID=").append(BRANCH_ID).append(";\r")
			.append("BASE_ACCT_NO=").append(BASE_ACCT_NO).append(";\r")
			.append("ACCT_SEQ_NO=").append(ACCT_SEQ_NO).append(";\r")
			.append("TRAN_DATE=").append(TRAN_DATE).append(";\r")
			.append("ACCT_NO=").append(ACCT_NO).append(";\r")
			.append("TRAN_AMT=").append(TRAN_AMT).append(";\r")
			.append("DUEBILL_BALANCE=").append(DUEBILL_BALANCE).append(";\r");
			EMPLog.log("TranLoanInform", EMPLog.INFO, 0, "【贷款发放成功通知】交易报文解析字段：\r"+sb.toString(), null);
			
			/** modified by huangtao 2019/02/26  裕民银行一期无此项业务，故注释 start */
			/**added by yangzy 2015/04/17 需求编号:XD150318023,微贷平台零售自助贷款改造 start */
//			String CONTRACT_NO = "";
//			if(reqBody.containsKey("CONTRACT_NO")&&reqBody.getDataValue("CONTRACT_NO")!=null&&!"".equals(reqBody.getDataValue("CONTRACT_NO"))){
//				CONTRACT_NO = (String)reqBody.getDataValue("CONTRACT_NO");//合同号
//			}
//			String DISTR_DATE = "";
//			//modified by wangj 需求编号:XD141222087,法人账户透支需求变更   需求编号:XD150825064_源泉宝法人账户透支改造  需求编号【XD150123005】小微自助循环贷款改造 begin
//			if(reqBody.containsKey("START_DATE")&&reqBody.getDataValue("START_DATE")!=null&&!"".equals(reqBody.getDataValue("START_DATE"))){
//				DISTR_DATE = (String)reqBody.getDataValue("START_DATE");//借据起始日
//			}
//			//modified by wangj 需求编号:XD141222087,法人账户透支需求变更   需求编号:XD150825064_源泉宝法人账户透支改造  需求编号【XD150123005】小微自助循环贷款改造  end
//			String END_DATE = "";
//			if(reqBody.containsKey("END_DATE")&&reqBody.getDataValue("END_DATE")!=null&&!"".equals(reqBody.getDataValue("END_DATE"))){
//				END_DATE = (String)reqBody.getDataValue("END_DATE");//借据到期日
//			}
//			/** 判断是否为自助贷款 */
//			BigDecimal selfcount = (BigDecimal)SqlClient.queryFirst("queryCtrLoanForSelfLoan", CONTRACT_NO, null, connection);
//			int self = Integer.parseInt(selfcount.toString());
//			if(self==1){
//				Map param = new HashMap();
//				param.put("tran_date",TRAN_DATE);
//			    param.put("bill_no",DUEBILL_NO);
//			    param.put("cont_no",CONTRACT_NO);
//			    param.put("loan_amt",TRAN_AMT);
//			    param.put("distr_date",DISTR_DATE);
//			    param.put("end_date",END_DATE);
//				//插入台账表
//				SqlClient.insert("insertAccLoanForSelfLoan",CONTRACT_NO, param, connection);
//			}
			/**added by yangzy 2015/04/17 需求编号:XD150318023,微贷平台零售自助贷款改造 end */
			
			/**如果为承诺函下，更新承诺函借据余额*/
//			SqlClient.update("updatePromissoryAccLoanBalance", DUEBILL_NO, BigDecimalUtil.replaceNull(DUEBILL_BALANCE), null, connection);
			/** modified by huangtao 2019/02/26  裕民银行一期无此项业务，故注释 end */
			
			/** 更新信贷系统本地台帐状态 */
			SqlClient.update("updateAccLoanStatus", DUEBILL_NO, "1", null, connection);
			
			/** modified by huangtao 2019/02/26  裕民银行一期无此项业务，故注释 start */
			/** 如果为资产流转或者资产证券化，同时更新资产台账状态，资产登记状态   start*/
//			BigDecimal count = (BigDecimal)SqlClient.queryFirst("queryLoanBillFromAccAssetTrans", DUEBILL_NO, null, connection);
//			int asset = Integer.parseInt(count.toString());
//			if(asset==1){
//				SqlClient.update("updateAccAssetTransStatus", DUEBILL_NO, "1", null, connection);//更新资产台账状态
//				SqlClient.update("updateIqpAssetRegiStatus", DUEBILL_NO, "03", null, connection);//更新资产登记状态
//			}
//			
//			BigDecimal count_pro = (BigDecimal)SqlClient.queryFirst("queryLoanBillFromAccAssetTransForPro", DUEBILL_NO, null, connection);
//			int asset_pro = Integer.parseInt(count_pro.toString());
//			if(asset_pro==1){
//				SqlClient.update("updateAccAssetTransStatus", DUEBILL_NO, "13", null, connection);//增加证券化转出状态，发放成功后修改  add by zhaozq 20140918
//				SqlClient.update("updateIqpAssetRegiStatus", DUEBILL_NO, "04", null, connection);//更新资产登记状态
//			}
			/** 如果为资产流转或者资产证券化，同时更新资产台账状态，资产登记状态   end*/
			/** modified by huangtao 2019/02/26  裕民银行一期无此项业务，故注释 end */
			
			//根据借据号更新授权表的状态为授权已确认
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("STATUS", "04");//状态
			param.put("BASE_ACCT_NO", BASE_ACCT_NO);
			param.put("ACCT_SEQ_NO", ACCT_SEQ_NO);
			SqlClient.update("updatePvpAuthorizeStatus4YM", DUEBILL_NO,param, null, connection); 
			retKColl.put("RetCd", "000000");
			retKColl.put("RetInf", "【贷款发放成功通知】业务处理成功");
			KeyedCollection retBody = new KeyedCollection("body");
			retKColl.addDataElement(retBody);
			EMPLog.log("TranLoanInform", EMPLog.INFO, 0, "【贷款发放成功通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.put("RetCd", "999999");
			retKColl.put("RetInf", "【贷款发放成功通知】业务处理失败！借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TranLoanInform", EMPLog.ERROR, 0, "【贷款发放成功通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
