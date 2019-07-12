package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cont.pub.sequence.CMISSequenceService4Cont;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.TimeUtil;

public class TradeCreditSucInform extends ESBTranService {
	private static final Logger logger = Logger.getLogger(TradeCreditSucInform.class);
	@Override
	public KeyedCollection doExecute(KeyedCollection kColl, Connection conn) throws Exception {
		logger.info("----------------- 进入网贷放款成功通知接口  start --------------------");
		KeyedCollection retKColl = new KeyedCollection();
		KeyedCollection reqBody = (KeyedCollection)kColl.get("BODY");
		KeyedCollection appHeadBody = (KeyedCollection)kColl.get("APP_HEAD");
		KeyedCollection sysHeadBody = (KeyedCollection)kColl.get("SYS_HEAD");
		String manegeId = (String)appHeadBody.getDataValue("TlrNo");//柜员号
		String brCd = (String) appHeadBody.getDataValue("BrCd");//机构
		String txnDt = (String) sysHeadBody.getDataValue("TxnDt");//交易日期
		txnDt = txnDt.substring(0, 4) + "-" + txnDt.substring(4, 6) + "-" + txnDt.substring(6, 8);
		EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
		Context context = factory.getContextNamed(factory.getRootContextName());
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		
		//String billNo = CMISSequenceService4JXXD.querySequenceFromDB("HT", "all", conn, context);//生成借据编号
		try{
			logger.info("----------------- 获取接口入参  start --------------------");
			String crdtCtrNo = (String)reqBody.getDataValue("CrdtCtrNo");//授信合同编号
			String billNo	= (String)reqBody.getDataValue("DblNo");//借据号
			String pdTp	= (String)reqBody.getDataValue("PdTp");//产品类型
			String pdCd	= (String)reqBody.getDataValue("PdCd");//产品代码
			String drwUseAmt = (String)reqBody.getDataValue("DrwUseAmt");//支用金额
			String drwUseStdTrmTp = (String)reqBody.getDataValue("DrwUseStdTrmTp");//支用标准期限类型
			String loanPpsTp = (String)reqBody.getDataValue("LoanPpsTp");//贷款用途类型
			String repymtMd	= (String)reqBody.getDataValue("RepymtMd");//还款方式
			String loanPpsDclrDocNo	= (String)reqBody.getDataValue("LoanPpsDclrDocNo");//贷款用途声明书编号
			String loanPpsDclrTm = reqBody.getDataValue("LoanPpsDclrTm")==null?"":(String)reqBody.getDataValue("LoanPpsDclrTm");//贷款用途声明时间
			String lvyCrdtQryAuthDocNo = reqBody.getDataValue("LvyCrdtQryAuthDocNo")==null?"":(String)reqBody.getDataValue("LvyCrdtQryAuthDocNo");//征信查询授权书编号
			String lvyCrdtQryAuthTm	= (String)reqBody.getDataValue("LvyCrdtQryAuthTm");//征信查询授权时间
			String loanUsrAgrmDocNo	= (String)reqBody.getDataValue("LoanUsrAgrmDocNo");//贷款用户协议书编号
			String loanUsrAuthTm = (String)reqBody.getDataValue("LoanUsrAuthTm");//贷款用户授权时间
			String intRateAdjMd	= (String)reqBody.getDataValue("IntRateAdjMd");//利率调整方式
			String intRateAdjMoDayNum = reqBody.getDataValue("IntRateAdjMoDayNum")==null?(String)reqBody.getDataValue("IntRateAdjMoDayNum"):"";//利率调整月天数
			String clcIntFrqcy = (String)reqBody.getDataValue("ClcIntFrqcy");//计息频率
			String wthdDt = reqBody.getDataValue("WthdDt")==null?"":(String)reqBody.getDataValue("WthdDt");//扣款日期
			String fixCyc = reqBody.getDataValue("FixCyc")==null?"":(String)reqBody.getDataValue("FixCyc");//固定周期
			String pnyIntTp = reqBody.getDataValue("PnyIntTp")==null?"":(String)reqBody.getDataValue("PnyIntTp");//罚息类型
			String pnyIntFltIntRate = reqBody.getDataValue("PnyIntFltIntRate")==null?"":(String)reqBody.getDataValue("PnyIntFltIntRate");//罚息浮动利率
			String lndngAcctOpnAcctBnkNo = (String)reqBody.getDataValue("LndngAcctOpnAcctBnkNo");//放款账户开户行行号
			String lndngAcctAcctNo = (String)reqBody.getDataValue("LndngAcctAcctNo");//放款账户账号
			String repymtAcctOpnAcctBnkNo = (String)reqBody.getDataValue("RepymtAcctOpnAcctBnkNo");//还款账户开户行行号
			String repymtAcctNo = (String)reqBody.getDataValue("RepymtAcctNo");//还款账号
			String baseIntRate = (String)reqBody.getDataValue("BaseIntRate");//基准利率
			String execYrIntRate = (String)reqBody.getDataValue("ExecYrIntRate");//执行年利率
			String startdate = (String)reqBody.getDataValue("LndngBegDt");//支用起始日
			String endDate = (String)reqBody.getDataValue("LndngEndDt");//支用到期日
			if(StringUtils.isEmpty(execYrIntRate)){
				execYrIntRate = "0";
			}
			execYrIntRate = String.valueOf(Double.parseDouble(execYrIntRate)/100);
			String odueIntRate = (String)reqBody.getDataValue("OdueIntRate");//逾期利率
			if(StringUtils.isEmpty(odueIntRate)){
				odueIntRate = "0";
			}
			odueIntRate = String.valueOf(Double.parseDouble(execYrIntRate)*(1+Double.parseDouble(odueIntRate)));
			String dfltIntRate = (String)reqBody.getDataValue("DfltIntRate");//违约利率

			String prdId = "";
			if(!"".equals(pdCd) && "1001100002".equals(pdCd)){
				prdId = "100091";//贷款品种
			}else if(!"".equals(pdCd) && "1001000001".equals(pdCd)){
				prdId = "100092";//贷款品种
			}
			logger.info("----------------- 获取接口入参  end --------------------");
			
			//int term=Integer.parseInt(reqBody.getDataValue("CrdTerm").toString().trim());//期限
			//String openday  = (String)SqlClient.queryFirst("querySysInfo", null, null, conn);
			//String endDate = DateUtils.getSTDTermEndDate(txnDt,drwUseStdTrmTp);

			/*String endDate = "";
			//计算结束日期
			if(drwUseStdTrmTp.equals("001")){
				endDate = TimeUtil.ADD_YEAR(openday, term);
			}else if(drwUseStdTrmTp.equals("002")){
				endDate = TimeUtil.ADD_MONTH(openday, term);
			}else{
				endDate = TimeUtil.ADD_DAY(openday, term);
			}*/
			Map<String,Object> lmtParam = new HashMap<String,Object>();
			lmtParam.put("app_no", crdtCtrNo);
			lmtParam.put("bill_no", billNo);
			//查询贷款台账
			Map<String,Object> ctrLoanContKcoll = (Map<String, Object>) SqlClient.queryFirst("queryAccLoanByWDBillNo", lmtParam, null, conn);
			
			//查询业务申请
			Map<String,Object> accLoanKcoll = (Map<String, Object>) SqlClient.queryFirst("queryAccLoanAppByBillNo", lmtParam, null, conn);
			if(ctrLoanContKcoll==null&&accLoanKcoll==null){
				//查询客户信息
				Map<String,Object> lmtAgrDetailsKcoll = (Map<String, Object>) SqlClient.queryFirst("queryLmtAgrDetailsByAppNo", lmtParam, null, conn);

				String cusId = (String) lmtAgrDetailsKcoll.get("cusId");
				String limitCode = (String) lmtAgrDetailsKcoll.get("limitCode");
				
				logger.info("------------------------  生成业务申请  start  -----------------------");
				String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", conn, context);//生成业务流水号
				//String contNo = CMISSequenceService4JXXD.querySequenceFromDB("HT", "all", conn, context);//生成合同编号
				context.put("organNo", "9999");
				String contNo = CMISSequenceService4Cont.querySequenceFromDB("HT", "fromDate", "1", conn, context);

				KeyedCollection kColl4YWSQ=new KeyedCollection();
				kColl4YWSQ.put("serno", serno);//业务流水号
				kColl4YWSQ.put("cus_id", cusId);//客户号
				kColl4YWSQ.put("biz_type", "7");//业务模式
				kColl4YWSQ.put("prd_id", prdId);//产品编号
				kColl4YWSQ.put("is_promissory_note", "2");//是否承诺下
				kColl4YWSQ.put("assure_main", "400");//担保方式，400:信用担保
				kColl4YWSQ.put("assure_main_details", "9");//担保方式，400:信用担保
				kColl4YWSQ.put("is_trust_loan", "2");//担保方式，400:信用担保
				kColl4YWSQ.put("apply_cur_type", "CNY");//币种
				kColl4YWSQ.put("apply_amount", drwUseAmt);//贷款金额
				kColl4YWSQ.put("apply_date", startdate);//申请日期
				kColl4YWSQ.put("exchange_rate", "1");//汇率
				kColl4YWSQ.put("security_rate", "0");//保证金汇率
				kColl4YWSQ.put("security_amt", "0");//保证金金额
				kColl4YWSQ.put("same_security_amt", "0");//视同保证金
				kColl4YWSQ.put("ass_sec_multiple", "1");//担保公司保证金倍数
				kColl4YWSQ.put("flow_type", "01");//担保公司保证金倍数
				kColl4YWSQ.put("approve_status", "997");//担保公司保证金倍数
				kColl4YWSQ.put("is_rfu", "2");//担保公司保证金倍数
				kColl4YWSQ.put("is_spe_cus", "2");//担保公司保证金倍数
				kColl4YWSQ.put("security_cur_type", "CNY");//保证金金额
				kColl4YWSQ.put("risk_open_amt", drwUseAmt);//风险敞口
				kColl4YWSQ.put("risk_open_rate", "1");//敞口比率
				kColl4YWSQ.put("security_exchange_rate", "1");//敞口比率
				kColl4YWSQ.put("limit_ind", "2");//额度使用标志
				kColl4YWSQ.put("limit_acc_no", crdtCtrNo);//授信编号
				kColl4YWSQ.put("manager_br_id", brCd);//管理机构
				kColl4YWSQ.put("input_id", manegeId);//登记人
				kColl4YWSQ.put("input_br_id", brCd);//管理机构
				kColl4YWSQ.put("input_date", startdate);//登记日期
				kColl4YWSQ.put("in_acct_br_id", brCd);//入账机构
				kColl4YWSQ.put("is_limit_cont_pay", "2");//是否额度合同项下支用
				kColl4YWSQ.setName("IqpLoanApp");
				dao.insert(kColl4YWSQ, conn);
				logger.info("------------------------  生成业务申请  start  -----------------------");
	
				logger.info("------------------------  生成授信和业务关系  start  -----------------------");
				KeyedCollection kColl4SXTW=new KeyedCollection();
				kColl4SXTW.put("agr_no", limitCode);//授信编号
				kColl4SXTW.put("serno", serno);//流水号
				kColl4SXTW.put("cont_no", contNo);//合同编号
				kColl4SXTW.setName("RBusLmtInfo");
				dao.insert(kColl4SXTW, conn);
				logger.info("------------------------  生成授信和业务关系  start  -----------------------");
	
				logger.info("------------------------  生成贷款合同  start  -----------------------");
				KeyedCollection kColl4DKHT=new KeyedCollection();
				kColl4DKHT.put("serno", serno);//业务流水号
				kColl4DKHT.put("cont_no", contNo);//合同号
				kColl4DKHT.put("cn_cont_no", contNo);//中文合同号
				kColl4DKHT.put("cus_id", cusId);//客户号
				kColl4DKHT.put("biz_type", "7");//业务模式
				//kColl4DKHT.put("belong_net", "");//所属网络
				kColl4DKHT.put("rent_type", "");//租赁模式
				kColl4DKHT.put("prd_id", prdId);//产品编号
				kColl4DKHT.put("prd_details", "");//产品细分
				kColl4DKHT.put("is_promissory_note", "2");//是否承诺函下
				//kColl4DKHT.put("promissory_note", "");//承诺函
				kColl4DKHT.put("assure_main", "400");//担保方式
				kColl4DKHT.put("assure_main_details", "9");//担保方式细分
				kColl4DKHT.put("is_trust_loan", "2");//是否信托贷款
				//kColl4DKHT.put("trust_company", "");//信托公司
				kColl4DKHT.put("cont_cur_type", "CNY");//合同币种
				kColl4DKHT.put("cont_amt", drwUseAmt);//合同金额
				kColl4DKHT.put("cont_balance", "0");//合同余额
				kColl4DKHT.put("cont_start_date", startdate);//合同起始日期
				kColl4DKHT.put("cont_end_date", endDate);//合同到期日期
				kColl4DKHT.put("exchange_rate", "1");//汇率
				kColl4DKHT.put("security_rate", "0");//保证金比例
				kColl4DKHT.put("security_amt", "0");//保证金金额
				kColl4DKHT.put("same_security_amt", "0");//视同保证金
				kColl4DKHT.put("ass_sec_multiple", "1");//担保公司保证金倍数
				kColl4DKHT.put("limit_ind", "2");//授信额度使用标志
				kColl4DKHT.put("limit_acc_no", crdtCtrNo);//授信台账编号
				kColl4DKHT.put("limit_credit_no", crdtCtrNo);//第三方授信编号
				kColl4DKHT.put("cont_status", "200");//合同状态
				kColl4DKHT.put("security_exchange_rate", "1");//保证金汇率
				kColl4DKHT.put("manager_br_id", brCd);//管理机构
				kColl4DKHT.put("input_id", manegeId);//登记人
				kColl4DKHT.put("input_br_id", brCd);//登记机构
				kColl4DKHT.put("input_date", startdate);//登记日期
				kColl4DKHT.put("in_acct_br_id", brCd);//入账机构
				kColl4DKHT.put("is_limit_cont_pay", "2");//是否额度合同项下支用
				//kColl4DKHT.put("limit_cont_no", "");//额度合同编号
				//kColl4DKHT.put("cancel_date", "");//合同注销日期
				kColl4DKHT.put("ser_date", startdate);//合同签订日期
				kColl4DKHT.put("security_cur_type", "CNY");//保证金币种
				//kColl4DKHT.put("security_exchange_rate", "");//保证金汇率
				//kColl4DKHT.put("trust_pro_name", "");//信托项目名称
				//kColl4DKHT.put("is_structured_fin", "");//是否结构化融资(STD_ZX_YES_NO)
				kColl4DKHT.setName("CtrLoanCont");
				dao.insert(kColl4DKHT, conn);
				logger.info("------------------------  生成贷款合同  end  -----------------------");
				
				logger.info("------------------------  生成贷款台账  start  -----------------------");
				String dkserno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", conn, context);//生成业务流水号
				KeyedCollection kColl4DKTZ=new KeyedCollection();
				kColl4DKTZ.put("serno", dkserno);//业务流水号
				kColl4DKTZ.put("acc_day", startdate);//日期
				kColl4DKTZ.put("acc_year", TimeUtil.getCurYear(startdate));//年份
				kColl4DKTZ.put("acc_mon", startdate.substring(5, 7));//月份
				kColl4DKTZ.put("prd_id", prdId);//产品编号
				kColl4DKTZ.put("cus_id", cusId);//产品编号
				kColl4DKTZ.put("cont_no", contNo);//合同编号
				kColl4DKTZ.put("bill_no", billNo);//借据编号
				kColl4DKTZ.put("loan_amt", drwUseAmt);//贷款金额
				kColl4DKTZ.put("loan_balance", drwUseAmt);//贷款余额
				kColl4DKTZ.put("distr_date", startdate);//发放日期
				kColl4DKTZ.put("end_date", endDate);//到期日期
				kColl4DKTZ.put("ori_end_date", endDate);//到期日期
				kColl4DKTZ.put("post_count", "0");//展期次数
				kColl4DKTZ.put("overdue", "0");//逾期次数
				kColl4DKTZ.put("ruling_ir", execYrIntRate==null?"":execYrIntRate);//基准利率
				kColl4DKTZ.put("reality_ir_y", execYrIntRate==null?"":execYrIntRate);//执行年利率
				kColl4DKTZ.put("comp_int_balance", "0");//复利余额
				kColl4DKTZ.put("inner_owe_int", "0");//表内欠息
				kColl4DKTZ.put("out_owe_int", "0");//表外欠息
				kColl4DKTZ.put("rec_int_accum", "0");//应收利息累计
				kColl4DKTZ.put("recv_int_accum", "0");//实收利息累计
				kColl4DKTZ.put("normal_balance", drwUseAmt);//正常余额
				kColl4DKTZ.put("overdue_balance", "0");//逾期余额
				kColl4DKTZ.put("slack_balance", "0");//呆滞余额
				kColl4DKTZ.put("bad_dbt_balance", "0");//呆账余额
				kColl4DKTZ.put("five_class", "10");//五级分类
				kColl4DKTZ.put("overdue_rate_y", odueIntRate==null?"":odueIntRate);//逾期利率
				kColl4DKTZ.put("default_rate_y", odueIntRate==null?"":odueIntRate);//违约利率
				kColl4DKTZ.put("base_acct_no", "");//账号、卡号
				kColl4DKTZ.put("acct_seq_no", "");//账号序号
				//kColl4DKTZ.put("security_rate", "");//管理机构
				//kColl4DKTZ.put("security_amt", "");//账务机构
				kColl4DKTZ.put("acc_status", "1");//合同状态
				kColl4DKTZ.put("cur_type", "CNY");//币种
				kColl4DKTZ.put("manager_br_id", brCd);//管理机构
				kColl4DKTZ.setName("AccLoan");
				dao.insert(kColl4DKTZ, conn);
				logger.info("------------------------  生成贷款台账  end  -----------------------");
			}
			retKColl.put("RetCd", "000000");
			retKColl.put("RetInf", "【网贷放款成功通知】业务处理成功");
		}catch(Exception e){
			retKColl.put("RetCd", "999999");
			retKColl.put("RetInf", "【网贷放款成功通知】交易处理失败，错误信息："+e.getMessage());
			e.printStackTrace();
		}
		logger.info("----------------- 进入网贷放款成功通知接口  end --------------------");
		return retKColl;
	}
}
