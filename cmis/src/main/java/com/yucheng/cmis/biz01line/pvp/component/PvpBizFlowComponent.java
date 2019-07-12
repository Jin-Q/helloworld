package com.yucheng.cmis.biz01line.pvp.component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.formula.functions.Value;
import org.apache.poi.hssf.record.formula.functions.Vlookup;

import com.dc.eai.data.CompositeData;
import com.easycon.busi.util.PackUtil;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.op.trade.TradeDkzqsq;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.iqp.agent.CatalogManaAgent;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.msiimple.IqpServiceInterfaceImple;
import com.yucheng.cmis.biz01line.pvp.op.pvploanapp.PvpBizFlowImpl;
import com.yucheng.cmis.biz01line.pvp.pvptools.PvpConstant;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.ESBUtil;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.pub.util.TimeUtil;
	
public class PvpBizFlowComponent extends CMISComponent {
	private static final String IQPLOANAPPMODEL = "IqpLoanApp";//贷款申请
	private static final String CTRCONTMODEL = "CtrLoanCont";//合同
	private static final String CTRCONTSUBMODEL = "CtrLoanContSub";//合同从表
	private static final String PVPLOANMODEL = "PvpLoanApp";//出账申请
	private static final String AUTHORIZEMODEL = "PvpAuthorize";//出账授权
	private static final String AUTHORIZESUBMODEL = "PvpAuthorizeSub";//授权信息从表
	private static final String ACCLOANMODEL = "AccLoan";//贷款台帐
	private static final String ACCVIEWLOANMODEL = "AccView";//贷款台帐视图
	private static final String PUBBAILINFO= "PubBailInfo";//保证金信息表
	private static final String IQPCUSACCT= "IqpCusAcct";//账户信息表
	private static final String IQPFEE= "IqpAppendTerms";//账户信息表
	private static final String PrdRepayMode= "PrdRepayMode";//还款方式
	private static final String IqpGuarantInfo= "IqpGuarantInfo";//保函信息
	private static final String IQPDELIVASSURE = "IqpDelivAssure";//提货担保从表
	private static final String IQPCREDIT = "IqpCredit";//信用证从表
	private static final String IQPFREEDOMPAYINFO = "IqpFreedomPayInfo";//自由还款信息
	private static final String AccTranTrustCompany = "AccTranTrustCompany";//信托公司贷款台账交易流水
	private static final Logger logger = Logger.getLogger(IqpServiceInterfaceImple.class);
	
	public String doWfAgreeForIqpLoan(String serno)throws ComponentException {
		String tranSerno=null;
		try {
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			/** 1.数据准备：通过业务流水号查询【出账申请】 */
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, this.getConnection());
			String Pvpserno = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("serno"));//授权交易流水号
			String prd_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("prd_id"));//产品编号
			String cus_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cus_id"));//客户编码
			String cont_no = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cont_no"));//合同编号
			BigDecimal pvp_amt = BigDecimalUtil.replaceNull(pvpLoanKColl.getDataValue("pvp_amt"));//出账金额
			String manager_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("manager_br_id"));//管理机构
			String in_acct_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("in_acct_br_id"));//入账机构
			String cur_type = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cur_type"));//币种
			String first_pay_date = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("first_pay_date"));//首次还款日
			String date = this.getContext().getDataValue(PUBConstant.OPENDAY).toString();//营业日期
			String input_id = this.getContext().getDataValue(PUBConstant.currentUserId).toString();//登记人
			String input_br_id = this.getContext().getDataValue(PUBConstant.organNo).toString();//登记机构
			
			/** 核算与信贷业务品种映射 START */
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			String hxPrdId = service.getPrdBasicCLPM2LM(cont_no, prd_id, this.getContext(), this.getConnection());
			/** 核算与信贷业务品种映射 END */
			
			
			/** 2.数据准备：通过业务流水号查询【业务申请】【合同信息】 */					
			KeyedCollection ctrContKColl =  dao.queryDetail(CTRCONTMODEL, cont_no, this.getConnection());
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, cont_no, this.getConnection());
			String loanSerno = TagUtil.replaceNull4String(ctrContKColl.getDataValue("serno"));//业务申请流水号
			String ruling_ir = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir"));//基准利率（年）
			String ir_float_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_type"));//浮动方式
			String ir_float_rate = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_rate"));//浮动比例
			String ir_float_point = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_point"));//浮动点数
			String reality_ir_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("reality_ir_y"));//执行利率（年）
			String overdue_rate_y_iqp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("overdue_rate_y"));//逾期利率
			String overdue_rate_iqp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("overdue_rate"));//逾期利率浮动百分比
			String default_rate_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("default_rate_y"));//违约利率
			String default_rate_iqp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("default_rate"));//违约利率浮动百分比
			String five_classfiy = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("five_classfiy"));//五级分类
			String cont_start_date = TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_start_date"));//合同起始日期
			String cont_end_date = TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_end_date"));//合同到期日期
			String repay_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_type"));//还款方式
			String interest_term = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("interest_term"));//计息周期

			//modified by yangzy 2015/07/23 保证金比例科学计算改造 start
			BigDecimal security_rate = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("security_rate"));//保证金比例
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 end
			Double security_amt = TagUtil.replaceNull4Double(ctrContKColl.getDataValue("security_amt"));//保证金金额
			String assure_main = TagUtil.replaceNull4String(ctrContKColl.getDataValue("assure_main"));//担保方式
			String repay_mode_type = "";//还款方式种类
			if(repay_type!=null && !"".equals(repay_type)){
				KeyedCollection prdRepayModeKColl = dao.queryDetail(PrdRepayMode, repay_type, this.getConnection());
				repay_mode_type = TagUtil.replaceNull4String(prdRepayModeKColl.getDataValue("repay_mode_type"));
			}   
			String is_collect_stamp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("is_collect_stamp"));//是否收取印花税
			String stamp_collect_mode = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("stamp_collect_mode"));//印花税收取方式
			String pay_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("pay_type"));//支付方式
			//信贷支付方式为：0：自主支付 1：受托支付 2：跨境受托支付。核算支付方式为：1：自主支付 2：受托支付 3：跨境受托支付
			/** modified by yangzy 20150112 受托支付需求开发(个贷特殊处理) start **/
			/** modified by yangzy 20140911 需求变化：XD140901056，内容：受托支付需求开发 start **/
			CMISModualServiceFactory jndiSer = CMISModualServiceFactory.getInstance();
			CusServiceInterface csInfo = (CusServiceInterface)jndiSer.getModualServiceById("cusServices", "cus");
			CusBase cusBaseInfo = csInfo.getCusBaseByCusId(cus_id,this.getContext(),this.getConnection());
			String hxCusId=cusBaseInfo.getHxCusId();//核心客戶号
			String belgLine = TagUtil.replaceNull4String(cusBaseInfo.getBelgLine());//所属条线
			if("BL300".equals(belgLine)){
				if(pay_type.equals("0")){
					pay_type= "1";
				}else if(pay_type.equals("1")){
					pay_type= "2";
				}else if(pay_type.equals("2")){
					pay_type= "3";
				}else if(pay_type == null || "".equals(pay_type)){
					pay_type= "";
				}else{
					throw new EMPException("未知支付方式！");
				}
			}else{
				if(pay_type.equals("0")){
					pay_type= "1";
				}else if(pay_type.equals("1")){
					//pay_type= "2";
					pay_type= "1";
				}else if(pay_type.equals("2")){
					//pay_type= "3";
					pay_type= "1";
				}else if(pay_type == null || "".equals(pay_type)){
					pay_type= "";
				}else{
					throw new EMPException("未知支付方式！");
				}
			}
			/** modified by yangzy 20140911 需求变化：XD140901056，内容：受托支付需求开发 end **/
			/** modified by yangzy 20150112 受托支付需求开发(个贷特殊处理) end **/
			//获取委托贷款关联信息
			KeyedCollection IqpCsgnLoanKColl = dao.queryDetail("IqpCsgnLoanInfo", loanSerno, this.getConnection());
			String csgn_cus_id = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_cus_id"));//委托人客户号
			Double csgn_chrg_pay_rate = TagUtil.replaceNull4Double(IqpCsgnLoanKColl.getDataValue("csgn_chrg_pay_rate"));//委托人手续费支付比例
			Double debit_chrg_pay_rate = TagUtil.replaceNull4Double(IqpCsgnLoanKColl.getDataValue("debit_chrg_pay_rate"));//借款人手续费支付比例
			BigDecimal cont_amt = new BigDecimal(TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_amt")));//合同金额
			String cont_cur_type = TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_cur_type"));//合同币种
			String repay_bill = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_bill"));//偿还借据，无间贷的原借据
			Double pad_rate_y = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("pad_rate_y"));//垫款利率
			Integer cont_term = TagUtil.replaceNull4Int(ctrContSubKColl.getDataValue("cont_term"));//合同期限
			String term_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("term_type"));//期限类型
			String repay_date = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_date"));//还款日
			String fir_repay_date = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("fir_repay_date"));//首次還款日还款日
			String ir_accord_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_accord_type"));//利率依据方式
			String ruling_ir_code = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir_code"));//基准利率代码
			Double overdue_rate_y = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("overdue_rate_y"));//罚息执行利率
			String repay_term = "";
			String repay_space = "";
			if("A005".equals(repay_type)){//利随本清传1月
				repay_term = "M";//还款间隔单位
				repay_space = "1";//还款间隔
			}else{
				repay_term = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_term"));//还款间隔单位
				repay_space = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_space"));//还款间隔
			}
			String loan_nature = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("loan_nature"));//贷款性质
			String is_promissory_note = TagUtil.replaceNull4String(ctrContKColl.getDataValue("is_promissory_note"));//是否承诺函下
			Double default_rate =  TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("default_rate"));//罚息执行利率浮动比
			String irAdjustType = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_adjust_type"));//下一次利率调整选项
			String ir_next_adjust_term = "";//下一次利率调整间隔
			String ir_next_adjust_unit = "";//下一次利率调整间隔单位
			String fir_adjust_day = "";//第一次调整日
			//20190422 chenBQ 默认利率调整方式为N,固定不变
			String ir_adjust_type = "N"; //默认为N
			//固定不变
			if("0".equals(irAdjustType)){
				ir_adjust_type = "N";
			}
			//按月调整
			else if("1".equals(irAdjustType)){
				ir_adjust_type = "R";
				ir_next_adjust_term = "1";
				//ir_next_adjust_unit = "M";
				//chenBQ 20190415 按月调整
				ir_next_adjust_unit = "M1";
				fir_adjust_day = DateUtils.getNextDate("M", date);
			}
			//按季调整
			else if("2".equals(irAdjustType)){
				ir_adjust_type = "R";
				ir_next_adjust_term = "1";
				//chenBQ 20190415 按季调整
				ir_next_adjust_unit = "M3";
				fir_adjust_day = DateUtils.getNextDate("Q", date);
			}
			//按年调整
			else if("3".equals(irAdjustType)){
				ir_adjust_type = "R";
				ir_next_adjust_term = "1";
				//ir_next_adjust_unit = "Y";
				//chenBQ 20190415 按年调整
				ir_next_adjust_unit = "Y1";
				fir_adjust_day = DateUtils.getNextDate("Y", date);
			}
			//立即生效
			else if("4".equals(irAdjustType)){
				ir_adjust_type = "A";
			}
			Double overdue_rate = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("overdue_rate"));//逾期利率浮动比
			String is_term = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("is_term"));//期供标志
			if(is_term.equals("1")){
				is_term = "Y";
			}else{
				is_term = "N";
			}
			String promissory_note = TagUtil.replaceNull4String(ctrContKColl.getDataValue("promissory_note"));//贷款承诺协议号
			String promissory_note_billno = "";
			if(promissory_note!=null&&!promissory_note.equals("")){
				//获取贷款承诺借据号
				KeyedCollection loankc = dao.queryFirst("AccLoan", null, " where cont_no = '"+promissory_note+"'", this.getConnection());
				promissory_note_billno = (String) loankc.getDataValue("bill_no");
			}
			String is_trust_loan = TagUtil.replaceNull4String(ctrContKColl.getDataValue("is_trust_loan"));//是否信托贷款
			
			//获取贷款申请相关信息
			KeyedCollection iqpLoanAppKColl =  dao.queryDetail(IQPLOANAPPMODEL, loanSerno, this.getConnection());
			String apply_date = TagUtil.replaceNull4String(iqpLoanAppKColl.getDataValue("apply_date"));//业务申请日期

			//通过客户编号查询【客户信息】
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
			CusBase cusBase = csi.getCusBaseByCusId(cus_id,this.getContext(),this.getConnection());
			String cus_name = TagUtil.replaceNull4String(cusBase.getCusName());//客户名称
			String cert_type = TagUtil.replaceNull4String(cusBase.getCertType());//证件类型
			String cert_code = TagUtil.replaceNull4String(cusBase.getCertCode());//证件号码
			String belg_line = TagUtil.replaceNull4String(cusBase.getBelgLine());//所属条线
			String hx_cus_id = TagUtil.replaceNull4String(cusBase.getHxCusId());//核心客户Id
			
			//生成借据编号
			PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
            .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), this.getConnection());
			String bill_no = cmisComponent.getBillNoByContNo(cont_no);//借据编号			
			String end_date = DateUtils.getEndDate(term_type, date, cont_term);//借据到期日
			
			//生成授权主表信息
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());//生成交易流水号
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());//生成授权编号
			authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
			authorizeKColl.addDataField("serno", Pvpserno);//业务流水号（出账编号）
			authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
			authorizeKColl.addDataField("prd_id", prd_id);//产品编号
			authorizeKColl.addDataField("cus_id", cus_id);//客户编码
			authorizeKColl.addDataField("cus_name", cus_name);//客户名称
			authorizeKColl.addDataField("cont_no", cont_no);//合同编号
			authorizeKColl.addDataField("bill_no", bill_no);//借据编号
			if(prd_id.equals("400021")){//境内保函
			   authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_BHFFSQ + TradeConstance.SERVICE_SCENE_BHFFSQ);
			}else if(prd_id.equals("400022") || prd_id.equals("400023")){//贷款承诺与信贷证明
				authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_DKCNSQ + TradeConstance.SERVICE_SCENE_DKCNSQ);
			}else{//普通贷款
			   authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_DKFFSQ + TradeConstance.SERVICE_SCENE_DKFFSQ);
			}
			authorizeKColl.addDataField("tran_amt", pvp_amt);//交易金额
			authorizeKColl.addDataField("tran_date", date);//交易日期
			authorizeKColl.addDataField("send_times", "0");//发送次数
			authorizeKColl.addDataField("return_code", "");//返回编码
			authorizeKColl.addDataField("return_desc", "");//返回信息
			authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
			authorizeKColl.addDataField("status", "00");//状态
			authorizeKColl.addDataField("cur_type", cur_type);//币种
			
			//费用信息
			String conditionFee = "where serno='"+loanSerno+"'";
			IndexedCollection iqpAppendTermsIColl = dao.queryList(IQPFEE, conditionFee, this.getConnection());
			
			//计算手续费率  start
			BigDecimal chrg_rate = new BigDecimal("0.00");
			BigDecimal commission = new BigDecimal("0.00");
			for(int fee_i=0;fee_i<iqpAppendTermsIColl.size();fee_i++){
				KeyedCollection feekc = (KeyedCollection) iqpAppendTermsIColl.get(fee_i);
				String fee_rate_str = TagUtil.replaceNull4String(feekc.getDataValue("fee_rate"));
				if(fee_rate_str==null||fee_rate_str.equals("")){
					fee_rate_str = "0";
				}
				BigDecimal fee_rate = new BigDecimal(fee_rate_str);
				chrg_rate = chrg_rate.add(fee_rate);
				
				//手续费不使用手续费率计算，固定金额直接加，防止精度丢失
				String collect_type = TagUtil.replaceNull4String(feekc.getDataValue("collect_type"));//01-按固定金额，02-按比率
				BigDecimal fee_amt = BigDecimalUtil.replaceNull(feekc.getDataValue("fee_amt"));
				if("02".equals(collect_type)){
					commission = commission.add(cont_amt.multiply(fee_rate));
				}else{
					commission = commission.add(fee_amt);
				}
			}
			//计算手续费率  end
			
			//HS141110017_保理业务改造  add by zhaozq start
			Boolean isBwBl = false;//是否表外保理
			KeyedCollection iqpInterFact = null;
			if(prd_id.equals("800021")){
				iqpInterFact = dao.queryDetail("IqpInterFact", loanSerno, this.getConnection());
				String fin_type = (String) iqpInterFact.getDataValue("fin_type");
				if("2".equals(fin_type)){
					isBwBl = true;
				}
			}
			//HS141110017_保理业务改造  add by zhaozq end
			
			//境内保函业务：授权信息组装 
			if(prd_id.equals("400021")){
				/* 查询保函信息 */
				KeyedCollection iqpGuarantInfokColl = dao.queryDetail(IqpGuarantInfo, loanSerno, this.getConnection());
				
				String ben_name = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("ben_name"));
				String guarant_mode = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("guarant_mode"));
				String guarant_type = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("guarant_type"));
				String open_type = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("open_type"));
				String ben_acct_org_no = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("ben_acct_org_no"));//受益人开户行号
				KeyedCollection orgkc = dao.queryDetail("PrdBankInfo", ben_acct_org_no, this.getConnection());
				
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编码
				authorizeKColl.addDataField("fldvalue02", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue03", "BRANCH_ID@" + in_acct_br_id);//机构代码
				authorizeKColl.addDataField("fldvalue04", "DUEBILL_NO@" + bill_no);//借据编号
				authorizeKColl.addDataField("fldvalue05", "GT_AGREE_NO@" + cont_no);//保函协议号
				authorizeKColl.addDataField("fldvalue06", "GLOBAL_TYPE@" + cert_type);//证件类型
				authorizeKColl.addDataField("fldvalue07", "GLOBAL_ID@" + cert_code);//证件号码
				authorizeKColl.addDataField("fldvalue08", "ISS_CTRY@" + "CN");//发证国家
				authorizeKColl.addDataField("fldvalue09", "CLIENT_NO@" + cus_id);//客户码
				authorizeKColl.addDataField("fldvalue10", "GT_NAME@" + cus_name);//保函所属人名称(取借款人名称)
				authorizeKColl.addDataField("fldvalue11", "CCY@" + cont_cur_type);//币种
				authorizeKColl.addDataField("fldvalue12", "LOAN_TYPE@" + hxPrdId);//贷款品种
				authorizeKColl.addDataField("fldvalue13", "GT_TYPE@" + guarant_mode);//保函类型
				authorizeKColl.addDataField("fldvalue14", "GT_AMOUNT@" + cont_amt);//保函金额
				authorizeKColl.addDataField("fldvalue15", "COMMISSION@" + commission);//手续费
				authorizeKColl.addDataField("fldvalue16", "DEDUCT_METHOD@" + "AUTOPAY");//扣款方式 可固定填AUTOPAY 自动扣款  MANUAL 手工主动还款
				authorizeKColl.addDataField("fldvalue17", "OD_INT_RATE@" + pad_rate_y);//垫款利率
				authorizeKColl.addDataField("fldvalue18", "OPEN_DATE@" + TagUtil.formatDate(date));//开办日期
				authorizeKColl.addDataField("fldvalue19", "EXPIRY_DATE@" + TagUtil.formatDate(end_date));//到期日期 
				authorizeKColl.addDataField("fldvalue20", "GT_KIND@" + guarant_type);//保函类型
				authorizeKColl.addDataField("fldvalue21", "OPEN_TYPE@" + open_type);//保函类型
				if(orgkc!=null&&orgkc.getDataValue("bank_name")!=null){
					authorizeKColl.addDataField("fldvalue22", "BENEFIT_ORG_NAME@" + orgkc.getDataValue("bank_name"));//受益人开户行名
				}else{
					authorizeKColl.addDataField("fldvalue22", "BENEFIT_ORG_NAME@" + "");//受益人开户行名
				}
				
			}
			//贷款承诺与信贷证明：授权信息组装 
			else if(prd_id.equals("400022") || prd_id.equals("400023")){
				/*查询出贷款承诺从表信息 */
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编码
				authorizeKColl.addDataField("fldvalue02", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue03", "BRANCH_ID@" + in_acct_br_id);//机构代码
				authorizeKColl.addDataField("fldvalue04", "DUEBILL_NO@" + bill_no);//借据编号
				authorizeKColl.addDataField("fldvalue05", "LOAN_PROMISE_AGREE_NO@" + cont_no);//贷款承诺协议号
				authorizeKColl.addDataField("fldvalue06", "CCY@" + cont_cur_type);//币种
				authorizeKColl.addDataField("fldvalue07", "LOAN_PROMISE_AMOUNT@" + cont_amt);//贷款承诺金额
				authorizeKColl.addDataField("fldvalue08", "COMMISSION@"+ commission);//手续费
				authorizeKColl.addDataField("fldvalue09", "GLOBAL_TYPE@" + cert_type);//证件类型
				authorizeKColl.addDataField("fldvalue10", "GLOBAL_ID@" + cert_code);//证件号码
				authorizeKColl.addDataField("fldvalue11", "ISS_CTRY@" + "CN");//发证国家
				authorizeKColl.addDataField("fldvalue12", "CLIENT_NO@" + cus_id);//客户码
				authorizeKColl.addDataField("fldvalue13", "CLIENT_NAME@" + cus_name);//客户名称
				authorizeKColl.addDataField("fldvalue14", "TERM@" + cont_term);//期限
				authorizeKColl.addDataField("fldvalue15", "TERM_TYPE@" + term_type);//期限类型
				authorizeKColl.addDataField("fldvalue16", "OPEN_DATE@" + TagUtil.formatDate(date));//开办日期
				authorizeKColl.addDataField("fldvalue17", "EXPIRY_DATE@" + TagUtil.formatDate(cont_end_date));//到期日期，合同到期日期
				authorizeKColl.addDataField("fldvalue18", "LOAN_TYPE@" + hxPrdId);//产品代码
			}else if(isBwBl){
				/*表外保理：授权信息组装*/
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编码
				authorizeKColl.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据编号
				authorizeKColl.addDataField("fldvalue03", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue04", "FACTOR_AGREE_NO@" + cont_no);//保理协议号
				authorizeKColl.addDataField("fldvalue05", "BRANCH_ID@" + in_acct_br_id);//机构代码
				authorizeKColl.addDataField("fldvalue06", "CLIENT_NO@" + cus_id);//客户码
				authorizeKColl.addDataField("fldvalue07", "CLIENT_NAME@" + cus_name);//客户名称
				authorizeKColl.addDataField("fldvalue08", "CCY@" + cont_cur_type);//币种
				authorizeKColl.addDataField("fldvalue09", "FACTOR_BALLANCE@" + cont_amt);//保理余额
				authorizeKColl.addDataField("fldvalue10", "FACTOR_DRAWDOWN_AMT@" + cont_amt);//保理发放金额
				authorizeKColl.addDataField("fldvalue11", "DRAW_DOWN_DATE@" + TagUtil.formatDate(date));//发放日期
				authorizeKColl.addDataField("fldvalue12", "EXPIRY_DATE@" + TagUtil.formatDate(cont_end_date));//到期日期
				authorizeKColl.addDataField("fldvalue13", "CLEARANCE_DATE@" + "");//结清日期
				authorizeKColl.addDataField("fldvalue14", "LOAN_TYPE@" + hxPrdId);//产品代码
				authorizeKColl.addDataField("fldvalue15", "RULE_CODE@" + "");//规则代码
				authorizeKColl.addDataField("fldvalue16", "FACTOR_STATUS@" + "NORM");//保理状态
				authorizeKColl.addDataField("fldvalue17", "REMARK@" + "");//备注
				authorizeKColl.addDataField("fldvalue18", "FEE_AMOUNT@" + commission);//手续费
			}
			/*其他普通贷款：授权信息组装*/
			else{
				authorizeKColl.addDataField("fldvalue01", "CstNo@" + hxCusId);//客户号1055559662
				authorizeKColl.addDataField("fldvalue02", "PdTp@" + hxPrdId);//产品类型01020201
				authorizeKColl.addDataField("fldvalue03", "AcctBlngInstNo@" + in_acct_br_id);//账户归属机构号
				authorizeKColl.addDataField("fldvalue04", "LoanOprtInstCd@" + in_acct_br_id);//经办机构代码
				authorizeKColl.addDataField("fldvalue05", "LoanCstNo@" + "");//贷款客户号cus_id
				authorizeKColl.addDataField("fldvalue06", "CstMgrCd@" + "CMS01");//客户经理代码
				authorizeKColl.addDataField("fldvalue07", "PrftCnrlCd@" + "");//利润中心代码
				authorizeKColl.addDataField("fldvalue08", "AcctDsc@" + "");//账户描述
				authorizeKColl.addDataField("fldvalue09", "Trm@" + valueOf(cont_term));//期限
				authorizeKColl.addDataField("fldvalue10", "TrmTp@" + getTermType(term_type));//期限类型
				authorizeKColl.addDataField("fldvalue11", "BegDt@" + valueOf(date).replace("-", ""));//起始日期/开始日期
				authorizeKColl.addDataField("fldvalue12", "ExprtnDt@" + valueOf(end_date).replace("-", ""));//到期日期
				authorizeKColl.addDataField("fldvalue13", "DstcWiOrWthtInd@" + "I");//区域内外标识
				authorizeKColl.addDataField("fldvalue14", "BlngKnd@" + "SG");//归属种类
				authorizeKColl.addDataField("fldvalue15", "DbtCrdtFlg@" + "C");//借贷标志
				authorizeKColl.addDataField("fldvalue16", "PlanMd@" + getRepayType(repay_type));//计划方式
				authorizeKColl.addDataField("fldvalue17", "IntStlmntFrqcy@" + getIntStlmntFrqcy(interest_term));//结息频率
				//根据对公对私贷款区分不同结息日及结息日期
				String intStlmntDt="21";//默认21
				//String nxtStlmntIntDt="";
				if("01010101".equals(hxPrdId)){//对公贷款
					//nxtStlmntIntDt=DateUtils.getNextJxDate(interest_term, date, intStlmntDt);//对公下一结息日期
				}else{//对私贷款repay_date
					int len=repay_date.length();
					if(len==2){
						intStlmntDt = repay_date;
					}else{
						intStlmntDt = repay_date;
					}
					//nxtStlmntIntDt=DateUtils.getNextJxDate(interest_term, date, intStlmntDt);
				}
				authorizeKColl.addDataField("fldvalue18", "NxtStlmntIntDt@" + first_pay_date.replaceAll("-", ""));//下一结息日期
				authorizeKColl.addDataField("fldvalue19", "IntStlmntDt@" + intStlmntDt);//结息日21
				authorizeKColl.addDataField("fldvalue20", "BlonLoanClcTrmTms@" + "");//气球贷计算期次
				authorizeKColl.addDataField("fldvalue21", "FrstStgTrmNum@" + "");//首段期数
				authorizeKColl.addDataField("fldvalue22", "AcrItrvTrmNum@" + "");//累进间隔期数
				authorizeKColl.addDataField("fldvalue23", "AcrVal@" + "");//累进金额
				authorizeKColl.addDataField("fldvalue24", "AcrRto@" + "");//累进比例
				authorizeKColl.addDataField("fldvalue25", "ClctPnyIntCmpdIntFlg@" + "");//收取罚息的复利标志
				authorizeKColl.addDataField("fldvalue26", "ClctSubCmpdIntFlg@" + "");//收取复利的复利标志
				authorizeKColl.addDataField("fldvalue27", "DrtnWthdFlg@" + "Y");//持续扣款标志
				authorizeKColl.addDataField("fldvalue28", "PrjNo@" + "");//项目编号
				authorizeKColl.addDataField("fldvalue29", "Ccy@" + valueOf(cont_cur_type));//币种
				authorizeKColl.addDataField("fldvalue30", "CtrOrgnlAmt@" + valueOf(pvp_amt));//合同原始金额
				authorizeKColl.addDataField("fldvalue31", "FndSrcCtyCd@" + "");//资金来源国家代码
				authorizeKColl.addDataField("fldvalue32", "FndSrcProvCd@" + "");//资金来源省份代码
				authorizeKColl.addDataField("fldvalue33", "DstrbtDdlnDt@" + "");//发放截止日期
				authorizeKColl.addDataField("fldvalue34", "ClctPnyIntFlg@" + "");//收取罚息标志
				authorizeKColl.addDataField("fldvalue35", "ClctCmpdIntFlg@" + "");//收取复利标志
				authorizeKColl.addDataField("fldvalue36", "DcnLoanIntPnyIntRate@" + "");//折扣贷款利息罚息率
				authorizeKColl.addDataField("fldvalue37", "DcnLoanElyRepymtLwsFineAmt@" + "");//折扣贷款提前还款最低罚金
				authorizeKColl.addDataField("fldvalue38", "GrcTrmDayNum@" + "");//宽限期天数
				authorizeKColl.addDataField("fldvalue39", "GrcTrmStopMoEndFlg@" + "");//宽限期至月末标志
				authorizeKColl.addDataField("fldvalue40", "AutoStlmntFlg@" + "Y");//自动结算标志
				authorizeKColl.addDataField("fldvalue41", "MtchAmtRepymtPlanModMd@" + "A");//等额还款计划变更方式
				authorizeKColl.addDataField("fldvalue42", "IntClrgWithPnpFlg@" + "");//利随本清标志
				authorizeKColl.addDataField("fldvalue43", "PpsDsc@" + "");//目的
				authorizeKColl.addDataField("fldvalue44", "GrtMd@" + getAssureMain(assure_main));//担保方式/保证方式
				authorizeKColl.addDataField("fldvalue45", "StatClFlg@" + "");//统计标志
				authorizeKColl.addDataField("fldvalue46", "StatClFlg1@" + "");//统计标志1
				authorizeKColl.addDataField("fldvalue47", "StatClFlg2@" + "");//统计标志2
				authorizeKColl.addDataField("fldvalue48", "CtrNo@" + cont_no);//合同编号
				authorizeKColl.addDataField("fldvalue49", "SyndLoanMgtBnkNo@" + "");//银团贷款管理行行号
				authorizeKColl.addDataField("fldvalue50", "SyndLoanBnkNo@" + "");//银团贷款银行行号
				authorizeKColl.addDataField("fldvalue51", "WithBrwRepymtFlg@" + "N");//随借随还标志,20190411 chenBQ 默认N
				authorizeKColl.addDataField("fldvalue52", "SbsdyIntEndDt@" + "");//贴息截止日期
				authorizeKColl.addDataField("fldvalue53", "AcctPpsCd@" + "");//账户用途代码
				authorizeKColl.addDataField("fldvalue54", "RepymtFrqcyTp@" + "");//还款频率类型valueOf(repay_term)
				authorizeKColl.addDataField("fldvalue55", "CrnMoRepymtFlg@" + "");//当月还款标志0
				authorizeKColl.addDataField("fldvalue56", "NotFullPrdMrgFlg@" + "");//不足期合并标志Y
				authorizeKColl.addDataField("fldvalue57", "DblNo@" + bill_no);//借据号
				authorizeKColl.addDataField("fldvalue58", "NeedReChkFlg@" + "Y");//需要复核标志
				String loanTrm="";
				if("001".equals(term_type)&&cont_term>1){
					loanTrm="2";
				}else if("002".equals(term_type)&&cont_term>12){
					loanTrm="2";
				}else if("003".equals(term_type)&&cont_term>360){
					loanTrm="2";
				}else{
					loanTrm="1";
				}
				
				authorizeKColl.addDataField("fldvalue59", "LoanTrm@" + loanTrm);//贷款期限
				authorizeKColl.addDataField("fldvalue60", "FullAmtWthdFlg@" + "");//足额扣款标志
				authorizeKColl.addDataField("fldvalue61", "OrgnlDblNo@" + "");//原借据号
				authorizeKColl.addDataField("fldvalue62", "EndtrmMrgFlg@" + "");//末期合并标志Y
				

				
				
			}
			dao.insert(authorizeKColl, this.getConnection());
			
			
			//生成利息信息数组
			//chenBQ 20190409判断基准利率
			String IntRateTp = "C1";//核心基准利率类型
			int termDays = 0;//贷款期限日
			if("001".equals(term_type)) {
				termDays =360*cont_term;
			} else if ("002".equals(term_type)) {
				termDays =30*cont_term;
			} else if("003".equals(term_type)) {
				termDays=cont_term;
			}
			if(termDays<=360)
				IntRateTp = "C1";
			else if(termDays>360 && termDays<=1800)
				IntRateTp = "C3";
			else if (termDays>1800)
				IntRateTp = "C5";
			KeyedCollection authorizeSubKCollForInt = new KeyedCollection(AUTHORIZESUBMODEL);
			authorizeSubKCollForInt.addDataField("auth_no", authSerno);//授权编号
			authorizeSubKCollForInt.addDataField("busi_cls", "05");//业务类别
			authorizeSubKCollForInt.addDataField("fldvalue01", "IntCtgryTp@" + "INT");//利息分类
			authorizeSubKCollForInt.addDataField("fldvalue02", "IntRateTp@" + IntRateTp);//利率类型
			authorizeSubKCollForInt.addDataField("fldvalue03", "BnkInnrIntRate@" + "");//行内利率
			authorizeSubKCollForInt.addDataField("fldvalue04", "FltIntRate@" + "");//浮动比例
			if("01".equals(ir_accord_type)||"03".equals(ir_accord_type) || "N".equals(ir_adjust_type)){//固定利率,议价利率依据,不计息，或者利率调整周期为固定不变
				authorizeSubKCollForInt.addDataField("fldvalue05", "SubsAcctLvlFltPntPct@" + "");//分户级浮动百分点
				authorizeSubKCollForInt.addDataField("fldvalue06", "SubsAcctLvlFltPct@" + "");//分户级浮动百分比
				//chenBQ 20190422 不计息时利息信息为空，核心需要传输0
				if(reality_ir_y == null || "".equals(reality_ir_y))
					reality_ir_y="0";
				authorizeSubKCollForInt.addDataField("fldvalue07", "SubsAcctLvlFixIntRate@" + valueOf(reality_ir_y)); //正常的执行利率
			}else if(("02".equals(ir_accord_type)||"04".equals(ir_accord_type)) && (!"N".equals(ir_adjust_type))){//浮动利率;牌告利率依据,正常利率上浮动,并且利率调整方式不为固定不变
				if("0".equals(ir_float_type)){//浮动方式0——加百分比，1——加点
					authorizeSubKCollForInt.addDataField("fldvalue05", "SubsAcctLvlFltPntPct@" + "");//分户级浮动百分点
					authorizeSubKCollForInt.addDataField("fldvalue06", "SubsAcctLvlFltPct@" + valueOf(ir_float_rate));//分户级浮动百分比
				}else{
					authorizeSubKCollForInt.addDataField("fldvalue05", "SubsAcctLvlFltPntPct@" + valueOf(ir_float_point));//分户级浮动百分点
					authorizeSubKCollForInt.addDataField("fldvalue06", "SubsAcctLvlFltPct@" + "");//分户级浮动百分比
				}
				authorizeSubKCollForInt.addDataField("fldvalue07", "SubsAcctLvlFixIntRate@" + "");
			}
			//authorizeSubKCollForInt.addDataField("fldvalue07", "SubsAcctLvlFixIntRate@" + "");//分户级固定利率,如果是固定利率该字段需给，如果是浮动利率该字段一定不要给
			authorizeSubKCollForInt.addDataField("fldvalue08", "ExecIntRate@" + "");//执行利率
			authorizeSubKCollForInt.addDataField("fldvalue09", "AnulBaseDayNum@" + "360");//年基准天数
			//chenBQ 20190422 等额本息月基准天数30，其他传ACT
			if("A002".equals(repay_type)) {
				authorizeSubKCollForInt.addDataField("fldvalue10", "MoBaseDaysNum@" + "30");//月基准天数
			} else {
				authorizeSubKCollForInt.addDataField("fldvalue10", "MoBaseDaysNum@" + "ACT");//月基准天数,核心自动计算
			}
			authorizeSubKCollForInt.addDataField("fldvalue11", "IntRateEnblMd@" + valueOf(ir_adjust_type));//利率启用方式
			authorizeSubKCollForInt.addDataField("fldvalue12", "PnyIntRateUseMd@" + "");//罚息利率使用方式
			authorizeSubKCollForInt.addDataField("fldvalue13", "IntClcBegDt@" + "");//利息计算起始日
			authorizeSubKCollForInt.addDataField("fldvalue14", "IntClcDdlnDt@" + "");//利息计算截止日
			authorizeSubKCollForInt.addDataField("fldvalue15", "IntRateModDt@" + valueOf(fir_adjust_day).replaceAll("-", ""));//利率变更日期
			authorizeSubKCollForInt.addDataField("fldvalue16", "IntRateModCyc@" + valueOf(ir_next_adjust_unit));//利率变更周期
			authorizeSubKCollForInt.addDataField("fldvalue17", "IntRateModDay@" + "1");//利率变更日
			authorizeSubKCollForInt.addDataField("fldvalue18", "ClcIntFlg@" + "Y");//计息标志
			authorizeSubKCollForInt.addDataField("fldvalue19", "IntRateTkEffMd@" + "N");//利率生效方式
			dao.insert(authorizeSubKCollForInt, this.getConnection());
			
			KeyedCollection authorizeSubKCollForOdp = new KeyedCollection(AUTHORIZESUBMODEL);
			authorizeSubKCollForOdp.addDataField("auth_no", authSerno);//授权编号
			authorizeSubKCollForOdp.addDataField("busi_cls", "05");//业务类别
			authorizeSubKCollForOdp.addDataField("fldvalue01", "IntCtgryTp@" + "ODP");//利息分类
			authorizeSubKCollForOdp.addDataField("fldvalue02", "IntRateTp@" + IntRateTp);//利率类型
			authorizeSubKCollForOdp.addDataField("fldvalue03", "BnkInnrIntRate@" + "");//行内利率
			authorizeSubKCollForOdp.addDataField("fldvalue04", "FltIntRate@" + "");//浮动利率
			if("01".equals(ir_accord_type)||"03".equals(ir_accord_type) || "N".equals(ir_adjust_type)){//固定利率,议价利率依据,不计息，或者利率调整周期为固定不变
				authorizeSubKCollForOdp.addDataField("fldvalue05", "SubsAcctLvlFltPntPct@" + "");//分户级浮动百分点
				authorizeSubKCollForOdp.addDataField("fldvalue06", "SubsAcctLvlFltPct@" + "");//分户级浮动百分比
				authorizeSubKCollForOdp.addDataField("fldvalue07", "SubsAcctLvlFixIntRate@" + valueOf(overdue_rate_y)); //逾期利率
			}else if(("02".equals(ir_accord_type)||"04".equals(ir_accord_type)) && (!"N".equals(ir_adjust_type))){//浮动利率;牌告利率依据,正常利率上浮动,并且利率调整方式不为固定不变
				authorizeSubKCollForOdp.addDataField("fldvalue05", "SubsAcctLvlFltPntPct@" + "");//分户级浮动百分点
				authorizeSubKCollForOdp.addDataField("fldvalue06", "SubsAcctLvlFltPct@" + valueOf(overdue_rate_iqp));//分户级浮动百分比
				authorizeSubKCollForOdp.addDataField("fldvalue07", "SubsAcctLvlFixIntRate@" + "");
			}
			authorizeSubKCollForOdp.addDataField("fldvalue08", "ExecIntRate@" + "");//执行利率
			authorizeSubKCollForOdp.addDataField("fldvalue09", "AnulBaseDayNum@" + "360");//年基准天数
			if("A002".equals(repay_type)) {
				authorizeSubKCollForOdp.addDataField("fldvalue10", "MoBaseDaysNum@" + "30");//月基准天数
			} else {
				authorizeSubKCollForOdp.addDataField("fldvalue10", "MoBaseDaysNum@" + "ACT");//月基准天数,核心自动计算
			}
			authorizeSubKCollForOdp.addDataField("fldvalue11", "IntRateEnblMd@" + valueOf(ir_adjust_type));//利率启用方式
			authorizeSubKCollForOdp.addDataField("fldvalue12", "PnyIntRateUseMd@" + "");//罚息利率使用方式
			authorizeSubKCollForOdp.addDataField("fldvalue13", "IntClcBegDt@" + "");//利息计算起始日
			authorizeSubKCollForOdp.addDataField("fldvalue14", "IntClcDdlnDt@" + "");//利息计算截止日
			authorizeSubKCollForOdp.addDataField("fldvalue15", "IntRateModDt@" + valueOf(fir_adjust_day).replaceAll("-", ""));//利率变更日期
			authorizeSubKCollForOdp.addDataField("fldvalue16", "IntRateModCyc@" + valueOf(ir_next_adjust_unit));//利率变更周期
			authorizeSubKCollForOdp.addDataField("fldvalue17", "IntRateModDay@" + "1");//利率变更日
			authorizeSubKCollForOdp.addDataField("fldvalue18", "ClcIntFlg@" + "Y");//计息标志
			authorizeSubKCollForOdp.addDataField("fldvalue19", "IntRateTkEffMd@" + "N");//利率生效方式
			dao.insert(authorizeSubKCollForOdp, this.getConnection());
			
			KeyedCollection authorizeSubKCollForOdi = new KeyedCollection(AUTHORIZESUBMODEL);
			authorizeSubKCollForOdi.addDataField("auth_no", authSerno);//授权编号
			authorizeSubKCollForOdi.addDataField("busi_cls", "05");//业务类别
			authorizeSubKCollForOdi.addDataField("fldvalue01", "IntCtgryTp@" + "ODI");//利息分类
			authorizeSubKCollForOdi.addDataField("fldvalue02", "IntRateTp@" + IntRateTp);//利率类型
			authorizeSubKCollForOdi.addDataField("fldvalue03", "BnkInnrIntRate@" + "");//行内利率
			authorizeSubKCollForOdi.addDataField("fldvalue04", "FltIntRate@" + "");//浮动利率
			if("01".equals(ir_accord_type)||"03".equals(ir_accord_type) || "N".equals(ir_adjust_type)){//固定利率,议价利率依据,不计息，或者利率调整周期为固定不变
				authorizeSubKCollForOdi.addDataField("fldvalue05", "SubsAcctLvlFltPntPct@" + "");//分户级浮动百分点
				authorizeSubKCollForOdi.addDataField("fldvalue06", "SubsAcctLvlFltPct@" + "");//分户级浮动百分比
				if(default_rate_y == null || "".equals(default_rate_y))
					default_rate_y="0";
				authorizeSubKCollForOdi.addDataField("fldvalue07", "SubsAcctLvlFixIntRate@" + valueOf(default_rate_y)); //违约利率
			}else if(("02".equals(ir_accord_type)||"04".equals(ir_accord_type)) && (!"N".equals(ir_adjust_type))){//浮动利率;牌告利率依据,正常利率上浮动,并且利率调整方式不为固定不变
				authorizeSubKCollForOdi.addDataField("fldvalue05", "SubsAcctLvlFltPntPct@" + "");//分户级浮动百分点
				authorizeSubKCollForOdi.addDataField("fldvalue06", "SubsAcctLvlFltPct@" + valueOf(default_rate_iqp));//分户级浮动百分比
				authorizeSubKCollForOdi.addDataField("fldvalue07", "SubsAcctLvlFixIntRate@" + "");
			}
			authorizeSubKCollForOdi.addDataField("fldvalue08", "ExecIntRate@" + "");//执行利率
			authorizeSubKCollForOdi.addDataField("fldvalue09", "AnulBaseDayNum@" + "360");//年基准天数
			if("A002".equals(repay_type)) {
				authorizeSubKCollForOdi.addDataField("fldvalue10", "MoBaseDaysNum@" + "30");//月基准天数
			} else {
				authorizeSubKCollForOdi.addDataField("fldvalue10", "MoBaseDaysNum@" + "ACT");//月基准天数,核心自动计算
			}
			authorizeSubKCollForOdi.addDataField("fldvalue11", "IntRateEnblMd@" + valueOf(ir_adjust_type));//利率启用方式
			authorizeSubKCollForOdi.addDataField("fldvalue12", "PnyIntRateUseMd@" + "");//罚息利率使用方式
			authorizeSubKCollForOdi.addDataField("fldvalue13", "IntClcBegDt@" + "");//利息计算起始日
			authorizeSubKCollForOdi.addDataField("fldvalue14", "IntClcDdlnDt@" + "");//利息计算截止日
			authorizeSubKCollForOdi.addDataField("fldvalue15", "IntRateModDt@" + valueOf(fir_adjust_day).replaceAll("-", ""));//利率变更日期
			authorizeSubKCollForOdi.addDataField("fldvalue16", "IntRateModCyc@" + valueOf(ir_next_adjust_unit));//利率变更周期
			authorizeSubKCollForOdi.addDataField("fldvalue17", "IntRateModDay@" + "1");//利率变更日
			authorizeSubKCollForOdi.addDataField("fldvalue18", "ClcIntFlg@" + "Y");//计息标志
			authorizeSubKCollForOdi.addDataField("fldvalue19", "IntRateTkEffMd@" + "N");//利率生效方式
			dao.insert(authorizeSubKCollForOdi, this.getConnection());
			
			//这种利息可以先不传
		/*	KeyedCollection authorizeSubKCollForOdodp = new KeyedCollection(AUTHORIZESUBMODEL);
			authorizeSubKCollForOdodp.addDataField("auth_no", authSerno);//授权编号
			authorizeSubKCollForOdodp.addDataField("busi_cls", "05");//业务类别
			authorizeSubKCollForOdodp.addDataField("fldvalue01", "IntCtgryTp@" + "ODODP");//利息分类
			authorizeSubKCollForOdodp.addDataField("fldvalue02", "IntRateTp@" + "C1");//利率类型
			authorizeSubKCollForOdodp.addDataField("fldvalue03", "BnkInnrIntRate@" + "");//行内利率
			authorizeSubKCollForOdodp.addDataField("fldvalue04", "FltIntRate@" + "");//浮动利率
			authorizeSubKCollForOdodp.addDataField("fldvalue05", "SubsAcctLvlFltPntPct@" + "");//分户级浮动百分点
			authorizeSubKCollForOdodp.addDataField("fldvalue06", "SubsAcctLvlFltPct@" + "");//分户级浮动百分比
			authorizeSubKCollForOdodp.addDataField("fldvalue07", "SubsAcctLvlFixIntRate@" + "");//分户级固定利率
			authorizeSubKCollForOdodp.addDataField("fldvalue08", "ExecIntRate@" + "");//执行利率
			authorizeSubKCollForOdodp.addDataField("fldvalue09", "AnulBaseDayNum@" + "360");//年基准天数
			authorizeSubKCollForOdodp.addDataField("fldvalue10", "MoBaseDaysNum@" + "30");//月基准天数
			authorizeSubKCollForOdodp.addDataField("fldvalue11", "IntRateEnblMd@" + valueOf(ir_adjust_type));//利率启用方式
			authorizeSubKCollForOdodp.addDataField("fldvalue12", "PnyIntRateUseMd@" + "");//罚息利率使用方式
			authorizeSubKCollForOdodp.addDataField("fldvalue13", "IntClcBegDt@" + "");//利息计算起始日
			authorizeSubKCollForOdodp.addDataField("fldvalue14", "IntClcDdlnDt@" + "");//利息计算截止日
			authorizeSubKCollForOdodp.addDataField("fldvalue15", "IntRateModDt@" + valueOf(fir_adjust_day).replaceAll("-", ""));//利率变更日期
			authorizeSubKCollForOdodp.addDataField("fldvalue16", "IntRateModCyc@" + valueOf(ir_next_adjust_unit));//利率变更周期
			authorizeSubKCollForOdodp.addDataField("fldvalue17", "IntRateModDay@" + "01");//利率变更日
			authorizeSubKCollForOdodp.addDataField("fldvalue18", "ClcIntFlg@" + "Y");//计息标志
			authorizeSubKCollForOdodp.addDataField("fldvalue19", "IntRateTkEffMd@" + "N");//利率生效方式
			dao.insert(authorizeSubKCollForOdodp, this.getConnection());*/
			
			
			//生成授权信息：结算账户信息
			String conditionCusAcct = "where serno='"+loanSerno+"'";
			IndexedCollection iqpCusAcctIColl = dao.queryList(IQPCUSACCT, conditionCusAcct, this.getConnection());
			int eactcount = 0;
			for(int i=0;i<iqpCusAcctIColl.size();i++){
				KeyedCollection iqpCusAcctKColl = (KeyedCollection)iqpCusAcctIColl.get(i);
				String acct_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_no"));//账号
				String opac_org_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opac_org_no"));//开户行行号
				String opan_org_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opan_org_name"));//开户行行名
				String is_this_org_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行账户
				String acct_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_name"));//户名
				Double pay_amt = TagUtil.replaceNull4Double(iqpCusAcctKColl.getDataValue("pay_amt"));//受托支付金额
				String acct_gl_code = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_gl_code"));//科目号
				String ccy = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("cur_type"));//币种
				String is_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行
				/*01放款账号  02印花税账号 03收息收款账号    04受托支付账号     05	主办行资金归集账户*/
				String acct_attr = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_attr"));//账户属性
				String interbank_id = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("interbank_id"));//银联行号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "StlmntBrBnkCd@" + "9901");//结算分行代码
				authorizeSubKColl.addDataField("fldvalue02", "StlmntBnkCstNo@" + "9999");//结算行客户号
				authorizeSubKColl.addDataField("fldvalue03", "StlmntAcctTp@" + getAccType(acct_attr));//结算账户类型
				authorizeSubKColl.addDataField("fldvalue04", "StlmntMd@" + "R");//结算方式
				if("AUT".equals(getAccType(acct_attr))){
					authorizeSubKColl.addDataField("fldvalue05", "ClctnPymtFlg@" + "REC");//收付款标志
					authorizeSubKColl.addDataField("fldvalue06", "AmtTp@" + "ALL");//金额类型
				}else if("PAY".equals(getAccType(acct_attr))){
					authorizeSubKColl.addDataField("fldvalue05", "ClctnPymtFlg@" + "PAY");//收付款标志
					authorizeSubKColl.addDataField("fldvalue06", "AmtTp@" + "PF");//金额类型
				}else if("TPP".equals(getAccType(acct_attr))){
					authorizeSubKColl.addDataField("fldvalue05", "ClctnPymtFlg@" + "PAY");//收付款标志
					authorizeSubKColl.addDataField("fldvalue06", "AmtTp@" + "ALL");//金额类型
				}else{
					authorizeSubKColl.addDataField("fldvalue05", "ClctnPymtFlg@" + "");//收付款标志
					authorizeSubKColl.addDataField("fldvalue06", "AmtTp@" + "");//金额类型
				}
				authorizeSubKColl.addDataField("fldvalue07", "StlmntAcctSeqNo@" + "");//结算账户序号
				authorizeSubKColl.addDataField("fldvalue08", "StlmntAcctNo@" + acct_no);//结算账号acct_no
				authorizeSubKColl.addDataField("fldvalue09", "StlmntAcctPdTp@" + "");//结算账户产品类型
				authorizeSubKColl.addDataField("fldvalue12", "StlmntCcy@" + valueOf(ccy));//结算币种
				if("AUT".equals(getAccType(acct_attr))){//还款账户
					authorizeSubKColl.addDataField("fldvalue13", "StlmntAmt@" + "");//结算金额
				}else{
					if("TPP".equals(getAccType(acct_attr))){
						//受托支付需要传受托支付金额
						authorizeSubKColl.addDataField("fldvalue13", "StlmntAmt@" + valueOf(pay_amt));//受托支付金额
					} else {
						authorizeSubKColl.addDataField("fldvalue13", "StlmntAmt@" + valueOf(pvp_amt));//结算金额
					}
				}
				authorizeSubKColl.addDataField("fldvalue16", "AutoLockInd@" + "");//自动锁定标记
				authorizeSubKColl.addDataField("fldvalue17", "PrtyLvl@" + "");//优先级别
				authorizeSubKColl.addDataField("fldvalue18", "StlmntRto@" + "");//结算权重
				authorizeSubKColl.addDataField("fldvalue19", "StlmntNo@" + "");//结算编号
				authorizeSubKColl.addDataField("fldvalue20", "IvsRto@" + "");//出资比例
				authorizeSubKColl.addDataField("fldvalue21", "StlmntAcctNm@" + "");//结算账户名称
				if("TPP".equals(getAccType(acct_attr))){
					authorizeSubKColl.addDataField("fldvalue22", "ClctnBnkNo@" + opac_org_no);//收款行行号
					authorizeSubKColl.addDataField("fldvalue23", "ClctnBnkNm@" + opan_org_name);//收款行名称
					String fdcrPymtNo = CMISSequenceService4JXXD.querySequenceFromDB("STZF", "all",this.getConnection(), this.getContext());//生成受托支付编号
					if("1".equals(is_acct)){
						authorizeSubKColl.addDataField("fldvalue24", "BnkInnrBnkOthrFlg@I");//行内
						authorizeSubKColl.addDataField("fldvalue25", "FrzPayMd@P");//支付
						authorizeSubKColl.addDataField("fldvalue26", "FdcrPymtNo@" + fdcrPymtNo);//受托支付编号
					}else{
						authorizeSubKColl.addDataField("fldvalue24", "BnkInnrBnkOthrFlg@O");//行外
						authorizeSubKColl.addDataField("fldvalue25", "FrzPayMd@B");//冻结
						authorizeSubKColl.addDataField("fldvalue26", "FdcrPymtNo@" + fdcrPymtNo);//受托支付编号
					}
				}else{
					authorizeSubKColl.addDataField("fldvalue24", "BnkInnrBnkOthrFlg@" + "");//行内行外标志
					authorizeSubKColl.addDataField("fldvalue25", "FrzPayMd@" + "");//冻结支付方式
					authorizeSubKColl.addDataField("fldvalue22", "ClctnBnkNo@" + "");//收款行行号
					authorizeSubKColl.addDataField("fldvalue23", "ClctnBnkNm@" + "");//收款行名称
					authorizeSubKColl.addDataField("fldvalue26", "FdcrPymtNo@" + "");//受托支付编号
				}
				authorizeSubKColl.addDataField("fldvalue27", "TxnTp@" + "9998");//交易类型
				authorizeSubKColl.addDataField("fldvalue28", "DvdnPrftRto@" + "");//分成利润比例				
				dao.insert(authorizeSubKColl, this.getConnection());
			}
			
			//生成授权信息：结算账户信息（委托贷款时生成委托账号授权信息）
			//个人委托贷款与企业委托贷款
			if(prd_id.equals("100063") || prd_id.equals("100065")){
				String acct_no = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_inner_dep_no"));//委托人内部存款账号
				String acct_name = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_inner_dep_name"));//委托人内部存款户名
				String csgn_acct_no = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_acct_no"));//委托人一般账号
				String csgn_acct_name = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_acct_name"));//委托人一般账号户名
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "StlmntBrBnkCd@" + "9901");//结算分行代码
				authorizeSubKColl.addDataField("fldvalue02", "StlmntBnkCstNo@" + "9999");//结算行客户号
				authorizeSubKColl.addDataField("fldvalue03", "StlmntAcctTp@" + "WTS");//结算账户类型
				authorizeSubKColl.addDataField("fldvalue04", "StlmntMd@" + "R");//结算方式
				authorizeSubKColl.addDataField("fldvalue05", "ClctnPymtFlg@" + "ALL");//收付款标志
				authorizeSubKColl.addDataField("fldvalue06", "AmtTp@" + "PF");//金额类型
				authorizeSubKColl.addDataField("fldvalue07", "StlmntAcctPrimKeyCd@" + "");//结算账户主键
				authorizeSubKColl.addDataField("fldvalue08", "StlmntAcctNo@" + "");//结算账号
				authorizeSubKColl.addDataField("fldvalue09", "StlmntAcctPdTp@" + "");//结算账户产品类型
				authorizeSubKColl.addDataField("fldvalue10", "StlmntAcctCcy@" + "");//结算账户币种
				authorizeSubKColl.addDataField("fldvalue11", "StlmntAcctSeqNo@" + "");//结算账户序号
				authorizeSubKColl.addDataField("fldvalue12", "StlmntCcy@" + valueOf(cont_cur_type));//结算币种
				authorizeSubKColl.addDataField("fldvalue13", "StlmntAmt@" + valueOf(pvp_amt));//结算金额
				authorizeSubKColl.addDataField("fldvalue14", "StlmntExgRate@" + "");//结算汇率
				authorizeSubKColl.addDataField("fldvalue15", "StlmntExgMd@" + "");//结算汇兑方式
				authorizeSubKColl.addDataField("fldvalue16", "AutoLockInd@" + "");//自动锁定标记
				authorizeSubKColl.addDataField("fldvalue17", "PrtyLvl@" + "");//优先级别
				authorizeSubKColl.addDataField("fldvalue18", "StlmntRto@" + "");//结算权重
				authorizeSubKColl.addDataField("fldvalue19", "StlmntNo@" + "");//结算编号
				authorizeSubKColl.addDataField("fldvalue20", "IvsRto@" + "");//出资比例
				authorizeSubKColl.addDataField("fldvalue21", "StlmntAcctNm@" + "");//结算账户名称
				authorizeSubKColl.addDataField("fldvalue22", "ClctnBnkNo@" + "");//收款行行号
				authorizeSubKColl.addDataField("fldvalue23", "ClctnBnkNm@" + "");//收款行名称
				authorizeSubKColl.addDataField("fldvalue24", "BnkInnrBnkOthrFlg@" + "");//行内行外标志
				authorizeSubKColl.addDataField("fldvalue25", "FrzPayMd@" + "");//冻结支付方式
				authorizeSubKColl.addDataField("fldvalue26", "FdcrPymtNo@" + "");//受托支付编号
				authorizeSubKColl.addDataField("fldvalue27", "TxnTp@" + "9998");//交易类型
				authorizeSubKColl.addDataField("fldvalue28", "DvdnPrftRto@" + "");//分成利润比例
				authorizeSubKColl.addDataField("fldvalue29", "OwnCorprtnFlg@" + "");//自营标志
				dao.insert(authorizeSubKColl, this.getConnection());
		}
			
			//生成台账信息
			KeyedCollection accLoanKColl = new KeyedCollection(ACCLOANMODEL);
			accLoanKColl.addDataField("serno", Pvpserno);//业务编号	
			accLoanKColl.addDataField("acc_day", date);//日期
			accLoanKColl.addDataField("acc_year", date.substring(0, 4));//年份
			accLoanKColl.addDataField("acc_mon", date.substring(5, 7));//月份
			accLoanKColl.addDataField("prd_id", prd_id);//产品编号
			accLoanKColl.addDataField("cus_id", cus_id);//客户编码
			accLoanKColl.addDataField("cont_no", cont_no);//合同编号
			accLoanKColl.addDataField("bill_no", bill_no);//借据编号
			accLoanKColl.addDataField("loan_amt", pvp_amt);//贷款金额
			accLoanKColl.addDataField("loan_balance", pvp_amt);//贷款余额
			accLoanKColl.addDataField("distr_date", date);//发放日期
			accLoanKColl.addDataField("end_date", end_date);//到期日期
			accLoanKColl.addDataField("ori_end_date", end_date);//原到期日期
			accLoanKColl.addDataField("post_count", "0");//展期次数
			accLoanKColl.addDataField("overdue", "0");//逾期期数
			accLoanKColl.addDataField("separate_date", "");//清分日期
			accLoanKColl.addDataField("ruling_ir", ruling_ir);//基准利率
			accLoanKColl.addDataField("reality_ir_y", reality_ir_y);//执行年利率
			accLoanKColl.addDataField("overdue_rate_y", overdue_rate_y_iqp);//逾期利率
			accLoanKColl.addDataField("default_rate_y", default_rate_y);//违约利率
			accLoanKColl.addDataField("comp_int_balance", "0");//复利余额
			accLoanKColl.addDataField("inner_owe_int", "0");//表内欠息
			accLoanKColl.addDataField("out_owe_int", "0");//表外欠息
			accLoanKColl.addDataField("rec_int_accum", "0");//应收利息累计
			accLoanKColl.addDataField("recv_int_accum", "0");//实收利息累计
			accLoanKColl.addDataField("normal_balance", pvp_amt);//正常余额
			accLoanKColl.addDataField("overdue_balance", "0");//逾期余额
			accLoanKColl.addDataField("slack_balance", "0");//呆滞余额
			accLoanKColl.addDataField("bad_dbt_balance", "0");//呆账余额
			accLoanKColl.addDataField("writeoff_date", "");//核销日期
			accLoanKColl.addDataField("paydate", "");//转垫款日
			accLoanKColl.addDataField("five_class", five_classfiy);//五级分类
			accLoanKColl.addDataField("twelve_cls_flg", "");//十二级分类
			accLoanKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			accLoanKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
			accLoanKColl.addDataField("acc_status", "0");//台帐状态
			accLoanKColl.addDataField("cur_type", cur_type);//币种
			dao.insert(accLoanKColl, this.getConnection());
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常！");
		}
		return tranSerno;
	}
	
	public void doWfAgreeForIqpLoan2(String serno)throws ComponentException {
		try {
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			/** 1.数据准备：通过业务流水号查询【出账申请】 */
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, this.getConnection());
			String Pvpserno = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("serno"));//授权交易流水号
			String prd_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("prd_id"));//产品编号
			String cus_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cus_id"));//客户编码
			String cont_no = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cont_no"));//合同编号
			BigDecimal pvp_amt = BigDecimalUtil.replaceNull(pvpLoanKColl.getDataValue("pvp_amt"));//出账金额
			String manager_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("manager_br_id"));//管理机构
			String in_acct_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("in_acct_br_id"));//入账机构
			String cur_type = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cur_type"));//币种
			String date = this.getContext().getDataValue(PUBConstant.OPENDAY).toString();//营业日期
			String input_id = this.getContext().getDataValue(PUBConstant.currentUserId).toString();//登记人
			String input_br_id = this.getContext().getDataValue(PUBConstant.organNo).toString();//登记机构
			
			/** 核算与信贷业务品种映射 START */
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			String lmPrdId = service.getPrdBasicCLPM2LM(cont_no, prd_id, this.getContext(), this.getConnection());
			/** 核算与信贷业务品种映射 END */
			
			
			/** 2.数据准备：通过业务流水号查询【业务申请】【合同信息】 */					
			KeyedCollection ctrContKColl =  dao.queryDetail(CTRCONTMODEL, cont_no, this.getConnection());
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, cont_no, this.getConnection());
			String loanSerno = TagUtil.replaceNull4String(ctrContKColl.getDataValue("serno"));//业务申请流水号
			String ruling_ir = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir"));//基准利率（年）
			String ir_float_rate = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_rate"));//浮动比例
			String ir_float_point = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_point"));//浮动点数
			String reality_ir_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("reality_ir_y"));//执行利率（年）
			String overdue_rate_y_iqp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("overdue_rate_y"));//逾期利率
			String default_rate_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("default_rate_y"));//违约利率
			String five_classfiy = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("five_classfiy"));//五级分类
			String cont_start_date = TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_start_date"));//合同起始日期
			String cont_end_date = TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_end_date"));//合同到期日期
			String repay_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_type"));//还款方式
			String interest_term = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("interest_term"));
			
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 start
			BigDecimal security_rate = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("security_rate"));//保证金比例
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 end
			Double security_amt = TagUtil.replaceNull4Double(ctrContKColl.getDataValue("security_amt"));//保证金金额
			String assure_main = TagUtil.replaceNull4String(ctrContKColl.getDataValue("assure_main"));//担保方式
			String repay_mode_type = "";//还款方式种类
			if(repay_type!=null && !"".equals(repay_type)){
				KeyedCollection prdRepayModeKColl = dao.queryDetail(PrdRepayMode, repay_type, this.getConnection());
				repay_mode_type = TagUtil.replaceNull4String(prdRepayModeKColl.getDataValue("repay_mode_type"));
			}   
			String is_collect_stamp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("is_collect_stamp"));//是否收取印花税
			String stamp_collect_mode = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("stamp_collect_mode"));//印花税收取方式
			String pay_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("pay_type"));//支付方式
			//信贷支付方式为：0：自主支付 1：受托支付 2：跨境受托支付。核算支付方式为：1：自主支付 2：受托支付 3：跨境受托支付
			/** modified by yangzy 20150112 受托支付需求开发(个贷特殊处理) start **/
			/** modified by yangzy 20140911 需求变化：XD140901056，内容：受托支付需求开发 start **/
			CMISModualServiceFactory jndiSer = CMISModualServiceFactory.getInstance();
			CusServiceInterface csInfo = (CusServiceInterface)jndiSer.getModualServiceById("cusServices", "cus");
			CusBase cusBaseInfo = csInfo.getCusBaseByCusId(cus_id,this.getContext(),this.getConnection());
			String belgLine = TagUtil.replaceNull4String(cusBaseInfo.getBelgLine());//所属条线
			if("BL300".equals(belgLine)){
				if(pay_type.equals("0")){
					pay_type= "1";
				}else if(pay_type.equals("1")){
					pay_type= "2";
				}else if(pay_type.equals("2")){
					pay_type= "3";
				}else if(pay_type == null || "".equals(pay_type)){
					pay_type= "";
				}else{
					throw new EMPException("未知支付方式！");
				}
			}else{
				if(pay_type.equals("0")){
					pay_type= "1";
				}else if(pay_type.equals("1")){
					//pay_type= "2";
					pay_type= "1";
				}else if(pay_type.equals("2")){
					//pay_type= "3";
					pay_type= "1";
				}else if(pay_type == null || "".equals(pay_type)){
					pay_type= "";
				}else{
					throw new EMPException("未知支付方式！");
				}
			}
			/** modified by yangzy 20140911 需求变化：XD140901056，内容：受托支付需求开发 end **/
			/** modified by yangzy 20150112 受托支付需求开发(个贷特殊处理) end **/
			//获取委托贷款关联信息
			KeyedCollection IqpCsgnLoanKColl = dao.queryDetail("IqpCsgnLoanInfo", loanSerno, this.getConnection());
			String csgn_cus_id = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_cus_id"));//委托人客户号
			Double csgn_chrg_pay_rate = TagUtil.replaceNull4Double(IqpCsgnLoanKColl.getDataValue("csgn_chrg_pay_rate"));//委托人手续费支付比例
			Double debit_chrg_pay_rate = TagUtil.replaceNull4Double(IqpCsgnLoanKColl.getDataValue("debit_chrg_pay_rate"));//借款人手续费支付比例
			BigDecimal cont_amt = new BigDecimal(TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_amt")));//合同金额
			String cont_cur_type = TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_cur_type"));//合同币种
			String repay_bill = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_bill"));//偿还借据，无间贷的原借据
			Double pad_rate_y = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("pad_rate_y"));//垫款利率
			Integer cont_term = TagUtil.replaceNull4Int(ctrContSubKColl.getDataValue("cont_term"));//合同期限
			String term_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("term_type"));//期限类型
			String repay_date = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_date"));//还款日
			String fir_repay_date = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("fir_repay_date"));//首次還款日还款日
			String ir_accord_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_accord_type"));//利率依据方式
			String ruling_ir_code = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir_code"));//基准利率代码
			Double overdue_rate_y = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("overdue_rate_y"));//罚息执行利率
			String repay_term = "";
			String repay_space = "";
			if(repay_type.equals("A005")){//利随本清传1月
				repay_term = "M";//还款间隔单位
				repay_space = "1";//还款间隔
			}else{
				repay_term = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_term"));//还款间隔单位
				repay_space = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_space"));//还款间隔
			}
			String loan_nature = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("loan_nature"));//贷款性质
			String is_promissory_note = TagUtil.replaceNull4String(ctrContKColl.getDataValue("is_promissory_note"));//是否承诺函下
			Double default_rate =  TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("default_rate"));//罚息执行利率浮动比
			String ir_adjust_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_adjust_type"));//下一次利率调整选项
			String ir_next_adjust_term = "";//下一次利率调整间隔
			String ir_next_adjust_unit = "";//下一次利率调整间隔单位
			String fir_adjust_day = "";//第一次调整日
			//固定不变
			if(ir_adjust_type.equals("0")){
				ir_adjust_type = "N";
			}
			//按月调整
			else if(ir_adjust_type.equals("1")){
				ir_adjust_type = "R";
				ir_next_adjust_term = "1";
				ir_next_adjust_unit = "M";
				fir_adjust_day = DateUtils.getNextDate("M", date);
			}
			//按季调整
			else if(ir_adjust_type.equals("2")){
				ir_adjust_type = "R";
				ir_next_adjust_term = "1";
				ir_next_adjust_unit = "Q";
				fir_adjust_day = DateUtils.getNextDate("Q", date);
			}
			//按年调整
			else if(ir_adjust_type.equals("3")){
				ir_adjust_type = "R";
				ir_next_adjust_term = "1";
				ir_next_adjust_unit = "Y";
				fir_adjust_day = DateUtils.getNextDate("Y", date);
			}
			//立即生效
			else if(ir_adjust_type.equals("4")){
				ir_adjust_type = "A";
			}
			Double overdue_rate = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("overdue_rate"));//逾期利率浮动比
			String is_term = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("is_term"));//期供标志
			if(is_term.equals("1")){
				is_term = "Y";
			}else{
				is_term = "N";
			}
			String promissory_note = TagUtil.replaceNull4String(ctrContKColl.getDataValue("promissory_note"));//贷款承诺协议号
			String promissory_note_billno = "";
			if(promissory_note!=null&&!promissory_note.equals("")){
				//获取贷款承诺借据号
				KeyedCollection loankc = dao.queryFirst("AccLoan", null, " where cont_no = '"+promissory_note+"'", this.getConnection());
				promissory_note_billno = (String) loankc.getDataValue("bill_no");
			}
			String is_trust_loan = TagUtil.replaceNull4String(ctrContKColl.getDataValue("is_trust_loan"));//是否信托贷款
			
			//获取贷款申请相关信息
			KeyedCollection iqpLoanAppKColl =  dao.queryDetail(IQPLOANAPPMODEL, loanSerno, this.getConnection());
			String apply_date = TagUtil.replaceNull4String(iqpLoanAppKColl.getDataValue("apply_date"));//业务申请日期

			//通过客户编号查询【客户信息】
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
			CusBase cusBase = csi.getCusBaseByCusId(cus_id,this.getContext(),this.getConnection());
			String cus_name = TagUtil.replaceNull4String(cusBase.getCusName());//客户名称
			String cert_type = TagUtil.replaceNull4String(cusBase.getCertType());//证件类型
			String cert_code = TagUtil.replaceNull4String(cusBase.getCertCode());//证件号码
			String belg_line = TagUtil.replaceNull4String(cusBase.getBelgLine());//所属条线
			String hx_cus_id = TagUtil.replaceNull4String(cusBase.getHxCusId());//核心客户Id
			
			//生成借据编号
			PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
            .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), this.getConnection());
			String bill_no = cmisComponent.getBillNoByContNo(cont_no);//借据编号			
			String end_date = DateUtils.getEndDate(term_type, date, cont_term);//借据到期日
			
			//生成授权主表信息
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());//生成交易流水号
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());//生成授权编号
			authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
			authorizeKColl.addDataField("serno", Pvpserno);//业务流水号（出账编号）
			authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
			authorizeKColl.addDataField("prd_id", prd_id);//产品编号
			authorizeKColl.addDataField("cus_id", cus_id);//客户编码
			authorizeKColl.addDataField("cus_name", cus_name);//客户名称
			authorizeKColl.addDataField("cont_no", cont_no);//合同编号
			authorizeKColl.addDataField("bill_no", bill_no);//借据编号
			if(prd_id.equals("400021")){//境内保函
			   authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_BHFFSQ + TradeConstance.SERVICE_SCENE_BHFFSQ);
			}else if(prd_id.equals("400022") || prd_id.equals("400023")){//贷款承诺与信贷证明
				authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_DKCNSQ + TradeConstance.SERVICE_SCENE_DKCNSQ);
			}else{//普通贷款
			   authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_DKFFSQ + TradeConstance.SERVICE_SCENE_DKFFSQ);
			}
			authorizeKColl.addDataField("tran_amt", pvp_amt);//交易金额
			authorizeKColl.addDataField("tran_date", date);//交易日期
			authorizeKColl.addDataField("send_times", "0");//发送次数
			authorizeKColl.addDataField("return_code", "");//返回编码
			authorizeKColl.addDataField("return_desc", "");//返回信息
			authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
			authorizeKColl.addDataField("status", "00");//状态
			authorizeKColl.addDataField("cur_type", cur_type);//币种
			
			//费用信息
			String conditionFee = "where serno='"+loanSerno+"'";
			IndexedCollection iqpAppendTermsIColl = dao.queryList(IQPFEE, conditionFee, this.getConnection());
			
			//计算手续费率  start
			BigDecimal chrg_rate = new BigDecimal("0.00");
			BigDecimal commission = new BigDecimal("0.00");
			for(int fee_i=0;fee_i<iqpAppendTermsIColl.size();fee_i++){
				KeyedCollection feekc = (KeyedCollection) iqpAppendTermsIColl.get(fee_i);
				String fee_rate_str = TagUtil.replaceNull4String(feekc.getDataValue("fee_rate"));
				if(fee_rate_str==null||fee_rate_str.equals("")){
					fee_rate_str = "0";
				}
				BigDecimal fee_rate = new BigDecimal(fee_rate_str);
				chrg_rate = chrg_rate.add(fee_rate);
				
				//手续费不使用手续费率计算，固定金额直接加，防止精度丢失
				String collect_type = TagUtil.replaceNull4String(feekc.getDataValue("collect_type"));//01-按固定金额，02-按比率
				BigDecimal fee_amt = BigDecimalUtil.replaceNull(feekc.getDataValue("fee_amt"));
				if("02".equals(collect_type)){
					commission = commission.add(cont_amt.multiply(fee_rate));
				}else{
					commission = commission.add(fee_amt);
				}
			}
			//计算手续费率  end
			
			//HS141110017_保理业务改造  add by zhaozq start
			Boolean isBwBl = false;//是否表外保理
			KeyedCollection iqpInterFact = null;
			if(prd_id.equals("800021")){
				iqpInterFact = dao.queryDetail("IqpInterFact", loanSerno, this.getConnection());
				String fin_type = (String) iqpInterFact.getDataValue("fin_type");
				if("2".equals(fin_type)){
					isBwBl = true;
				}
			}
			//HS141110017_保理业务改造  add by zhaozq end
			
			//境内保函业务：授权信息组装 
			if(prd_id.equals("400021")){
				/* 查询保函信息 */
				KeyedCollection iqpGuarantInfokColl = dao.queryDetail(IqpGuarantInfo, loanSerno, this.getConnection());
				
				String ben_name = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("ben_name"));
				String guarant_mode = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("guarant_mode"));
				String guarant_type = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("guarant_type"));
				String open_type = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("open_type"));
				String ben_acct_org_no = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("ben_acct_org_no"));//受益人开户行号
				KeyedCollection orgkc = dao.queryDetail("PrdBankInfo", ben_acct_org_no, this.getConnection());
				
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编码
				authorizeKColl.addDataField("fldvalue02", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue03", "BRANCH_ID@" + in_acct_br_id);//机构代码
				authorizeKColl.addDataField("fldvalue04", "DUEBILL_NO@" + bill_no);//借据编号
				authorizeKColl.addDataField("fldvalue05", "GT_AGREE_NO@" + cont_no);//保函协议号
				authorizeKColl.addDataField("fldvalue06", "GLOBAL_TYPE@" + cert_type);//证件类型
				authorizeKColl.addDataField("fldvalue07", "GLOBAL_ID@" + cert_code);//证件号码
				authorizeKColl.addDataField("fldvalue08", "ISS_CTRY@" + "CN");//发证国家
				authorizeKColl.addDataField("fldvalue09", "CLIENT_NO@" + cus_id);//客户码
				authorizeKColl.addDataField("fldvalue10", "GT_NAME@" + cus_name);//保函所属人名称(取借款人名称)
				authorizeKColl.addDataField("fldvalue11", "CCY@" + cont_cur_type);//币种
				authorizeKColl.addDataField("fldvalue12", "LOAN_TYPE@" + lmPrdId);//贷款品种
				authorizeKColl.addDataField("fldvalue13", "GT_TYPE@" + guarant_mode);//保函类型
				authorizeKColl.addDataField("fldvalue14", "GT_AMOUNT@" + cont_amt);//保函金额
				authorizeKColl.addDataField("fldvalue15", "COMMISSION@" + commission);//手续费
				authorizeKColl.addDataField("fldvalue16", "DEDUCT_METHOD@" + "AUTOPAY");//扣款方式 可固定填AUTOPAY 自动扣款  MANUAL 手工主动还款
				authorizeKColl.addDataField("fldvalue17", "OD_INT_RATE@" + pad_rate_y);//垫款利率
				authorizeKColl.addDataField("fldvalue18", "OPEN_DATE@" + TagUtil.formatDate(date));//开办日期
				authorizeKColl.addDataField("fldvalue19", "EXPIRY_DATE@" + TagUtil.formatDate(end_date));//到期日期 
				authorizeKColl.addDataField("fldvalue20", "GT_KIND@" + guarant_type);//保函类型
				authorizeKColl.addDataField("fldvalue21", "OPEN_TYPE@" + open_type);//保函类型
				if(orgkc!=null&&orgkc.getDataValue("bank_name")!=null){
					authorizeKColl.addDataField("fldvalue22", "BENEFIT_ORG_NAME@" + orgkc.getDataValue("bank_name"));//受益人开户行名
				}else{
					authorizeKColl.addDataField("fldvalue22", "BENEFIT_ORG_NAME@" + "");//受益人开户行名
				}
				
			}
			//贷款承诺与信贷证明：授权信息组装 
			else if(prd_id.equals("400022") || prd_id.equals("400023")){
				/*查询出贷款承诺从表信息 */
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编码
				authorizeKColl.addDataField("fldvalue02", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue03", "BRANCH_ID@" + in_acct_br_id);//机构代码
				authorizeKColl.addDataField("fldvalue04", "DUEBILL_NO@" + bill_no);//借据编号
				authorizeKColl.addDataField("fldvalue05", "LOAN_PROMISE_AGREE_NO@" + cont_no);//贷款承诺协议号
				authorizeKColl.addDataField("fldvalue06", "CCY@" + cont_cur_type);//币种
				authorizeKColl.addDataField("fldvalue07", "LOAN_PROMISE_AMOUNT@" + cont_amt);//贷款承诺金额
				authorizeKColl.addDataField("fldvalue08", "COMMISSION@"+ commission);//手续费
				authorizeKColl.addDataField("fldvalue09", "GLOBAL_TYPE@" + cert_type);//证件类型
				authorizeKColl.addDataField("fldvalue10", "GLOBAL_ID@" + cert_code);//证件号码
				authorizeKColl.addDataField("fldvalue11", "ISS_CTRY@" + "CN");//发证国家
				authorizeKColl.addDataField("fldvalue12", "CLIENT_NO@" + cus_id);//客户码
				authorizeKColl.addDataField("fldvalue13", "CLIENT_NAME@" + cus_name);//客户名称
				authorizeKColl.addDataField("fldvalue14", "TERM@" + cont_term);//期限
				authorizeKColl.addDataField("fldvalue15", "TERM_TYPE@" + term_type);//期限类型
				authorizeKColl.addDataField("fldvalue16", "OPEN_DATE@" + TagUtil.formatDate(date));//开办日期
				authorizeKColl.addDataField("fldvalue17", "EXPIRY_DATE@" + TagUtil.formatDate(cont_end_date));//到期日期，合同到期日期
				authorizeKColl.addDataField("fldvalue18", "LOAN_TYPE@" + lmPrdId);//产品代码
			}else if(isBwBl){
				/*表外保理：授权信息组装*/
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编码
				authorizeKColl.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据编号
				authorizeKColl.addDataField("fldvalue03", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue04", "FACTOR_AGREE_NO@" + cont_no);//保理协议号
				authorizeKColl.addDataField("fldvalue05", "BRANCH_ID@" + in_acct_br_id);//机构代码
				authorizeKColl.addDataField("fldvalue06", "CLIENT_NO@" + cus_id);//客户码
				authorizeKColl.addDataField("fldvalue07", "CLIENT_NAME@" + cus_name);//客户名称
				authorizeKColl.addDataField("fldvalue08", "CCY@" + cont_cur_type);//币种
				authorizeKColl.addDataField("fldvalue09", "FACTOR_BALLANCE@" + cont_amt);//保理余额
				authorizeKColl.addDataField("fldvalue10", "FACTOR_DRAWDOWN_AMT@" + cont_amt);//保理发放金额
				authorizeKColl.addDataField("fldvalue11", "DRAW_DOWN_DATE@" + TagUtil.formatDate(date));//发放日期
				authorizeKColl.addDataField("fldvalue12", "EXPIRY_DATE@" + TagUtil.formatDate(cont_end_date));//到期日期
				authorizeKColl.addDataField("fldvalue13", "CLEARANCE_DATE@" + "");//结清日期
				authorizeKColl.addDataField("fldvalue14", "LOAN_TYPE@" + lmPrdId);//产品代码
				authorizeKColl.addDataField("fldvalue15", "RULE_CODE@" + "");//规则代码
				authorizeKColl.addDataField("fldvalue16", "FACTOR_STATUS@" + "NORM");//保理状态
				authorizeKColl.addDataField("fldvalue17", "REMARK@" + "");//备注
				authorizeKColl.addDataField("fldvalue18", "FEE_AMOUNT@" + commission);//手续费
			}
			/*其他普通贷款：授权信息组装*/
			else{
				authorizeKColl.addDataField("fldvalue01", "ExecTp@" + "01");//执行类型:发放
				authorizeKColl.addDataField("fldvalue02", "AcctNoCrdNo@" + "");//账号/卡号(待处理)
				authorizeKColl.addDataField("fldvalue03", "TxnAmt@" + pvp_amt);//交易金额
				authorizeKColl.addDataField("fldvalue04", "CstNo@" + "1055549645");//核心客户号(待处理)hx_cus_id
				authorizeKColl.addDataField("fldvalue05", "DblNo@" + bill_no);//借据号

				authorizeKColl.addDataField("fldvalue06", "PdTp@" + "01020201");//产品类型，用我们自己的产品类型，交给核心处理prd_id
				authorizeKColl.addDataField("fldvalue07", "AcctBlngInstNo@" + "9901");//账户开户机构,待处理,暂时给放款申请中的入账机构in_acct_br_id
				authorizeKColl.addDataField("fldvalue08", "EstblshdInstNo@" + "9901");//开立机构号input_br_id
				authorizeKColl.addDataField("fldvalue09", "LoanCstNo@" + cus_id);//信贷客户号
				authorizeKColl.addDataField("fldvalue10", "CstMgrCd@" + "9999");//客户经理代码input_id
				authorizeKColl.addDataField("fldvalue11", "PrftCnrlCd@" + "");//利润中心代码
				authorizeKColl.addDataField("fldvalue12", "AcctDsc@" + "");//账户描述
				authorizeKColl.addDataField("fldvalue13", "Trm@" + cont_term);//期限
				authorizeKColl.addDataField("fldvalue14", "TrmTp@" + getTermType(term_type));//期限类型 
				authorizeKColl.addDataField("fldvalue15", "BegDt@" + valueOf(date).replace("-", ""));//起始日期/开始日期
				authorizeKColl.addDataField("fldvalue16", "ExprtnDt@" + end_date);//到期日期
				authorizeKColl.addDataField("fldvalue17", "DstcWiOrWthtInd@" + "I");//区域内外标识
				authorizeKColl.addDataField("fldvalue18", "BlngKnd@" + "SG");//归属种类,待处理
				authorizeKColl.addDataField("fldvalue19", "DbtCrdtFlg@" + "C");//借贷标志
				authorizeKColl.addDataField("fldvalue20", "AcctPpsCd@" + "");//账户用途代码，待处理是否 为借款用途
				authorizeKColl.addDataField("fldvalue21", "PlanMd@" + getRepayType(repay_type));//计划方式
				authorizeKColl.addDataField("fldvalue22", "IntStlmntFrqcy@" + "M1");//结息频率,待处理interest_term
				authorizeKColl.addDataField("fldvalue23", "NxtStlmntIntDt@" + valueOf(fir_repay_date).replace("-", ""));//下一结息日期，待处理
				authorizeKColl.addDataField("fldvalue24", "IntStlmntDt@" + "21");//结息日，待处理
				authorizeKColl.addDataField("fldvalue25", "BlonLoanClcTrmTms@" + "");//气球贷计算期次.置空，不存在此种还款方式
				authorizeKColl.addDataField("fldvalue26", "FrstStgTrmNum@" + "");//首段期数.置空，不存在此种还款方式
				authorizeKColl.addDataField("fldvalue27", "AcrItrvTrmNum@" + "");//累进间隔期数.置空，不存在此种还款方式
				authorizeKColl.addDataField("fldvalue28", "AcrVal@" + "");//累进金额.置空，不存在此种还款方式
				authorizeKColl.addDataField("fldvalue29", "AcrRto@" + "");//累进比例.置空，不存在此种还款方式
				
				authorizeKColl.addDataField("fldvalue30", "IntRateEnblMd@" + valueOf(ir_adjust_type));//利率启用方式，待处理
				authorizeKColl.addDataField("fldvalue31", "IntRateModDt@" + valueOf(fir_adjust_day));//利率变更日期
				authorizeKColl.addDataField("fldvalue32", "IntRateModCcy@" + valueOf(ir_next_adjust_unit));//利率变更周期
				authorizeKColl.addDataField("fldvalue33", "IntRateModDay@" + "1");//利率变更日
				authorizeKColl.addDataField("fldvalue34", "ClctPnyIntCmpdIntFlg@" + "");//收取罚息的复利标志
				authorizeKColl.addDataField("fldvalue35", "ClctSubCmpdIntFlg@" + "");//收取复利的复利标志
				authorizeKColl.addDataField("fldvalue36", "DrtnWthdFlg@" + "Y");//持续扣款标志
				authorizeKColl.addDataField("fldvalue37", "PrjNo@" + "");//项目编号
				authorizeKColl.addDataField("fldvalue38", "Ccy@" + cont_cur_type);//币种
				authorizeKColl.addDataField("fldvalue39", "CtrOrgnlAmt@" + cont_amt);//合同原始金额
				authorizeKColl.addDataField("fldvalue40", "FndSrcCtyCd@" + "");//资金来源国家代码
				authorizeKColl.addDataField("fldvalue41", "FndSrcProvCd@" + "");//资金来源省份代码
				authorizeKColl.addDataField("fldvalue42", "DstrbtDdlnDt@" + "");//发放截止日期
				authorizeKColl.addDataField("fldvalue43", "ClctPnyIntFlg@" + "");//收取罚息标志
				authorizeKColl.addDataField("fldvalue44", "ClctCmpdIntFlg@" + "");//收取复利标志
				authorizeKColl.addDataField("fldvalue45", "DcnLoanIntPnyIntRate@" + "");//折扣贷款利息罚息率
				authorizeKColl.addDataField("fldvalue46", "DcnLoanElyRepymtLwsFineAmt@" + "");//折扣贷款提前还款最低罚金
				authorizeKColl.addDataField("fldvalue47", "GrcTrmDayNum@" + "");//宽限期天数
				authorizeKColl.addDataField("fldvalue48", "GrcTrmStopMoEndFlg@" + "");//宽限期至月末标志
				authorizeKColl.addDataField("fldvalue49", "AutoStlmntFlg@" + "N");//自动结算标志
				authorizeKColl.addDataField("fldvalue50", "MtchAmtRepymtPlanModMd@" + "A");//等额还款计划变更方式
				authorizeKColl.addDataField("fldvalue51", "IntClrgWithPnpFlg@" + "");//利随本清标志
				authorizeKColl.addDataField("fldvalue52", "PpsDsc@" + "");//目的
				authorizeKColl.addDataField("fldvalue53", "GrtMd@" + "");//担保方式/保证方式
				authorizeKColl.addDataField("fldvalue54", "LoanTrm@" + "2");//贷款期限
				authorizeKColl.addDataField("fldvalue55", "StatClFlg@" + "");//统计标志
				authorizeKColl.addDataField("fldvalue56", "StatClFlg1@" + "");//统计标志
				authorizeKColl.addDataField("fldvalue57", "CtrNo@" + cont_no);//合同编号
				
				authorizeKColl.addDataField("fldvalue58", "SyndLoanMgtBnkNo@" + "");//银团贷款管理行行号
				authorizeKColl.addDataField("fldvalue59", "SyndLoanBnkNo@" + "");//银团贷款银行行号
				authorizeKColl.addDataField("fldvalue60", "WithBrwRepymtFlg@" + "");//随借随还标志
				authorizeKColl.addDataField("fldvalue61", "SbsdyIntEndDt@" + "");//贴息截止日期
				
				authorizeKColl.addDataField("fldvalue62", "IntAmt@" + "");//利息金额
				authorizeKColl.addDataField("fldvalue63", "SubPrjNo@" + "");//子项目编号
				authorizeKColl.addDataField("fldvalue64", "SmyDsc@" + "");//摘要
				authorizeKColl.addDataField("fldvalue65", "LoanFiveLvlKnd@" + "");//贷款五级分类
				
			}
			dao.insert(authorizeKColl, this.getConnection());

			
			//生成授权信息：费用信息
			Map<String, String> feemap = new HashMap<String, String>();//定义一个账号对应map
			int feecount = 1;
			if(prd_id.equals("100055")||prd_id.equals("100056")){
				//银团
				/*01	116	27503	N	委托贷款费用
				  02	117	27503	N	递延收益—保函手续费收入
				  03	117	27503	N	银行汇票业务收入
				  04	117	5110101	N	工本费收入
				  05	117	27503	N	待摊销费用
				  06	116	27503	N	印花税
				  07	116	27503	N	手续费
				  08	116	27503	N	递延收益—贷款承诺手续费收入
				  10	116	27503	N	资产买卖安排费支出
				  11	116	27503	N	资产买卖安排费
				  12	116	27503	N	代管理信贷资产手续费支出
				  13	116	27503	N	代管理信贷资产手续费收入
				  14	116	5110101	N	代理贴现费用
				  15	116	5110101	N	代理贴现邮电费用
				  16	116	27503	N	受托支付手续费
				  17	116	5110101	N	受托支付汇划费
				  18	116	27503	N	银团安排费
				  19	116	27503	N	银团承诺费
				  20	116	27503	N	银团代理费*/
				for(int i=0;i<iqpAppendTermsIColl.size();i++){				
					KeyedCollection iqpAppendTermsKColl = (KeyedCollection)iqpAppendTermsIColl.get(i);
					String fee_code = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_code"));//费用代码
					
					if("10".equals(fee_code)){//银团安排费
						fee_code = "18";
					}else if("11".equals(fee_code)){//银团承诺费
						fee_code = "19";
					}else if("12".equals(fee_code)){//银团代理费
						fee_code = "20";
					}
					
					String fee_code_hs = "";
					if(fee_code.equals("09")){//01-借款人委托贷款费用、09-委托人委托贷款费用  都对应 核算系统的  01-委托费用
						fee_code_hs = "01";
					}else{
						fee_code_hs = fee_code;
					}
					String fee_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_cur_type"));//币种
					Double fee_amt = TagUtil.replaceNull4Double(iqpAppendTermsKColl.getDataValue("fee_amt"));//费用金额
					String fee_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_type"));//费用类型
					String fee_rate = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_rate"));//费用比率
					String is_cycle_chrg = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_cycle_chrg"));//是否周期性收费标识 
					String chrg_date = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_date"));//收费日期
					String acct_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_acct_no"));//账号
					String chrg_freq = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_freq"));//账号
					if(chrg_freq.equals("Y")){
						chrg_freq = "12";
					}else if(chrg_freq.equals("Q")){
						chrg_freq = "3";
					}else if(chrg_freq.equals("M")){
						chrg_freq = "1";
					}else{
						chrg_freq = "";
					}
					KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
					authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
					authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
					authorizeSubKColl.addDataField("fldvalue01", "SvcFeeTp@" + fee_type);//服务费类型
					authorizeSubKColl.addDataField("fldvalue02", "ChrgCcy@" + fee_cur_type);//收费币种
					authorizeSubKColl.addDataField("fldvalue03", "FeeAmt@" + fee_amt);//费用金额
					authorizeSubKColl.addDataField("fldvalue04", "OrgnlSvcFeeAmt@" + "");//原始服务费金额
					authorizeSubKColl.addDataField("fldvalue05", "DcnAmt@" + "");//折扣金额
					authorizeSubKColl.addDataField("fldvalue06", "DcnTp@" + "");//折扣类型
					authorizeSubKColl.addDataField("fldvalue07", "DcnRate@" + "");//折扣率
					authorizeSubKColl.addDataField("fldvalue08", "EODFlg@" + "");//日终标志
					authorizeSubKColl.addDataField("fldvalue09", "ClctMd@" + "N");//收取方式
					authorizeSubKColl.addDataField("fldvalue10", "SvcFeeClctAcctAcctNo@" + "");//服务费收取账户账号
					authorizeSubKColl.addDataField("fldvalue11", "SvcFeeClctAcctCcy@" + "");//服务费收取账户币种			
					authorizeSubKColl.addDataField("fldvalue12", "SvcFeeClctAcctPdTp@" + "");//服务费收取账户产品类型
					authorizeSubKColl.addDataField("fldvalue13", "SvcFeeClctAcctSeqNo@" + "");//服务费收取账户序号
					authorizeSubKColl.addDataField("fldvalue14", "DrwMd@" + "");//支取方式
					authorizeSubKColl.addDataField("fldvalue15", "TxnPswdStrg@" + "");//交易密码
					authorizeSubKColl.addDataField("fldvalue16", "TaxRateTp@" + "");//税率类型
					authorizeSubKColl.addDataField("fldvalue17", "TaxRate@" + "");//税率
					authorizeSubKColl.addDataField("fldvalue18", "IntTaxAmt@" + "");//利息税金额
					authorizeSubKColl.addDataField("fldvalue19", "VchrTp@" + "");//凭证类型
					authorizeSubKColl.addDataField("fldvalue20", "PfxDsc@" + "");//前缀
					authorizeSubKColl.addDataField("fldvalue21", "VchrBegNo@" + "");//凭证起始号码
					authorizeSubKColl.addDataField("fldvalue22", "VchrEndNo@" + "");//凭证结束号码
					authorizeSubKColl.addDataField("fldvalue23", "VchrNum@" + "");//凭证数量
					authorizeSubKColl.addDataField("fldvalue24", "UnitPrcAmt@" + "");//单价
					dao.insert(authorizeSubKColl, this.getConnection());
				}
			}else{
				for(int i=0;i<iqpAppendTermsIColl.size();i++){				
					KeyedCollection iqpAppendTermsKColl = (KeyedCollection)iqpAppendTermsIColl.get(i);
					String fee_code = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_code"));//费用代码
					String fee_code_hs = "";
					if(fee_code.equals("09")){//01-借款人委托贷款费用、09-委托人委托贷款费用  都对应 核算系统的  01-委托费用
						fee_code_hs = "01";
					}else if(fee_code.equals("13")){//保理费用
						fee_code_hs = "21";
					}else{
						fee_code_hs = fee_code;
					}
					String fee_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_cur_type"));//币种
					Double fee_amt = TagUtil.replaceNull4Double(iqpAppendTermsKColl.getDataValue("fee_amt"));//费用金额
					String fee_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_type"));//费用类型
					String fee_rate = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_rate"));//费用比率
					String is_cycle_chrg = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_cycle_chrg"));//是否周期性收费标识 
					String chrg_date = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_date"));//收费日期
					String acct_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_acct_no"));//账号
					String chrg_freq = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_freq"));//账号
					if(chrg_freq.equals("Y")){
						chrg_freq = "12";
					}else if(chrg_freq.equals("Q")){
						chrg_freq = "3";
					}else if(chrg_freq.equals("M")){
						chrg_freq = "1";
					}else{
						chrg_freq = "";
					}
					KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
					authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
					authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
					authorizeSubKColl.addDataField("fldvalue01", "SvcFeeTp@" + fee_type);//服务费类型
					authorizeSubKColl.addDataField("fldvalue02", "ChrgCcy@" + fee_cur_type);//收费币种
					authorizeSubKColl.addDataField("fldvalue03", "FeeAmt@" + fee_amt);//费用金额
					authorizeSubKColl.addDataField("fldvalue04", "OrgnlSvcFeeAmt@" + "");//原始服务费金额
					authorizeSubKColl.addDataField("fldvalue05", "DcnAmt@" + "");//折扣金额
					authorizeSubKColl.addDataField("fldvalue06", "DcnTp@" + "");//折扣类型
					authorizeSubKColl.addDataField("fldvalue07", "DcnRate@" + "");//折扣率
					authorizeSubKColl.addDataField("fldvalue08", "EODFlg@" + "");//日终标志
					authorizeSubKColl.addDataField("fldvalue09", "ClctMd@" + "N");//收取方式
					authorizeSubKColl.addDataField("fldvalue10", "SvcFeeClctAcctAcctNo@" + "");//服务费收取账户账号
					authorizeSubKColl.addDataField("fldvalue11", "SvcFeeClctAcctCcy@" + "");//服务费收取账户币种			
					authorizeSubKColl.addDataField("fldvalue12", "SvcFeeClctAcctPdTp@" + "");//服务费收取账户产品类型
					authorizeSubKColl.addDataField("fldvalue13", "SvcFeeClctAcctSeqNo@" + "");//服务费收取账户序号
					authorizeSubKColl.addDataField("fldvalue14", "DrwMd@" + "");//支取方式
					authorizeSubKColl.addDataField("fldvalue15", "TxnPswdStrg@" + "");//交易密码
					authorizeSubKColl.addDataField("fldvalue16", "TaxRateTp@" + "");//税率类型
					authorizeSubKColl.addDataField("fldvalue17", "TaxRate@" + "");//税率
					authorizeSubKColl.addDataField("fldvalue18", "IntTaxAmt@" + "");//利息税金额
					authorizeSubKColl.addDataField("fldvalue19", "VchrTp@" + "");//凭证类型
					authorizeSubKColl.addDataField("fldvalue20", "PfxDsc@" + "");//前缀
					authorizeSubKColl.addDataField("fldvalue21", "VchrBegNo@" + "");//凭证起始号码
					authorizeSubKColl.addDataField("fldvalue22", "VchrEndNo@" + "");//凭证结束号码
					authorizeSubKColl.addDataField("fldvalue23", "VchrNum@" + "");//凭证数量
					authorizeSubKColl.addDataField("fldvalue24", "UnitPrcAmt@" + "");//单价
					
					dao.insert(authorizeSubKColl, this.getConnection());
				}
			}
			
			//生成利息信息数组
			KeyedCollection authorizeSubKCollForInt = new KeyedCollection(AUTHORIZESUBMODEL);
			authorizeSubKCollForInt.addDataField("auth_no", authSerno);//授权编号
			authorizeSubKCollForInt.addDataField("busi_cls", "05");//业务类别
			authorizeSubKCollForInt.addDataField("fldvalue01", "IntCtgryTp@" + "INT");//利息分类
			authorizeSubKCollForInt.addDataField("fldvalue02", "IntRateTp@" + "");//利率类型
			authorizeSubKCollForInt.addDataField("fldvalue03", "BnkInnrIntRate@" + "");//行内利率
			authorizeSubKCollForInt.addDataField("fldvalue04", "FltIntRate@" + "");//浮动利率
			authorizeSubKCollForInt.addDataField("fldvalue05", "IntRateFltPntNum@" + "");//利率浮动点数
			authorizeSubKCollForInt.addDataField("fldvalue06", "IntRateFltPct@" + "");//利率浮动百分比
			authorizeSubKCollForInt.addDataField("fldvalue07", "SubsAcctLvlFltPntPct@" + "");//分户级浮动百分点
			authorizeSubKCollForInt.addDataField("fldvalue08", "SubsAcctLvlFltPct@" + "");//分户级浮动百分比
			authorizeSubKCollForInt.addDataField("fldvalue09", "SubsAcctLvlFixIntRate@" + "");//分户级固定利率
			authorizeSubKCollForInt.addDataField("fldvalue10", "ExecIntRate@" + "");//执行利率
			authorizeSubKCollForInt.addDataField("fldvalue11", "IntStlmntFrqcy@" + "");//结息频率
			authorizeSubKCollForInt.addDataField("fldvalue12", "NxtStlmntIntDt@" + "");//下一结息日期
			authorizeSubKCollForInt.addDataField("fldvalue13", "IntStlmntDt@" + "");//结息日期
			authorizeSubKCollForInt.addDataField("fldvalue14", "AnulBaseDayNum@" + "");//年基准天数
			authorizeSubKCollForInt.addDataField("fldvalue15", "MoBaseDaysNum@" + "");//月基准天数
			authorizeSubKCollForInt.addDataField("fldvalue16", "LwsIntRate@" + "");//最低利率
			authorizeSubKCollForInt.addDataField("fldvalue17", "HestIntRate@" + "");//最高利率
			authorizeSubKCollForInt.addDataField("fldvalue18", "IntRateEnblMd@" + "N");//利率启用方式
			authorizeSubKCollForInt.addDataField("fldvalue19", "CptztnFlg@" + "");//资本化标志
			authorizeSubKCollForInt.addDataField("fldvalue20", "PnyIntRateUseMd@" + "");//罚息利率使用方式
			authorizeSubKCollForInt.addDataField("fldvalue21", "IntClcBegDt@" + "");//利息计算起始日
			authorizeSubKCollForInt.addDataField("fldvalue22", "IntClcDdlnDt@" + "");//利息计算截止日
			authorizeSubKCollForInt.addDataField("fldvalue23", "IntRateModDt@" + "");//利率变更日期
			authorizeSubKCollForInt.addDataField("fldvalue24", "IntRateModCcy@" + "");//利率变更周期
			authorizeSubKCollForInt.addDataField("fldvalue25", "IntRateModDay@" + "");//利率变更日
			authorizeSubKCollForInt.addDataField("fldvalue26", "ClcIntFlg@" + "Y");//计息标志
			authorizeSubKCollForInt.addDataField("fldvalue27", "IntRateTkEffMd@" + "N");//利率生效方式
			authorizeSubKCollForInt.addDataField("fldvalue28", "FllwExecIntFltFlg@" + "N");//随执行利率浮动标志
			dao.insert(authorizeSubKCollForInt, this.getConnection());
			
			
			//生成授权信息：结算账户信息
			String conditionCusAcct = "where serno='"+loanSerno+"'";
			IndexedCollection iqpCusAcctIColl = dao.queryList(IQPCUSACCT, conditionCusAcct, this.getConnection());
			int eactcount = 0;
			for(int i=0;i<iqpCusAcctIColl.size();i++){				
				KeyedCollection iqpCusAcctKColl = (KeyedCollection)iqpCusAcctIColl.get(i);
				String acct_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_no"));//账号
				String opac_org_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opac_org_no"));//开户行行号
				String opan_org_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opan_org_name"));//开户行行名
				String is_this_org_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行账户
				String acct_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_name"));//户名
				Double pay_amt = TagUtil.replaceNull4Double(iqpCusAcctKColl.getDataValue("pay_amt"));//受托支付金额
				String acct_gl_code = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_gl_code"));//科目号
				String ccy = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("cur_type"));//币种
				String is_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行
				/*01放款账号  02印花税账号 03收息收款账号    04受托支付账号     05	主办行资金归集账户*/
				String acct_attr = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_attr"));//账户属性
				String interbank_id = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("interbank_id"));//银联行号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				/*
				authorizeSubKColl.addDataField("fldvalue01", "StlmntBrBnkCd@" + "9901");//结算分行代码
				authorizeSubKColl.addDataField("fldvalue02", "StlmntBnkCstNo@" + "9999");//结算行客户号
				*/
				//modify by jch 20190403 StlmntBrBnkCd,StlmntBnkCstNo 不必输
				authorizeSubKColl.addDataField("fldvalue01", "StlmntBrBnkCd@" + "");//结算分行代码
				authorizeSubKColl.addDataField("fldvalue02", "StlmntBnkCstNo@" + "");//结算行客户号
				authorizeSubKColl.addDataField("fldvalue03", "StlmntAcctTp@" + getAccType(acct_attr));//结算账户类型
				authorizeSubKColl.addDataField("fldvalue04", "StlmntMd@" + "R");//结算方式
				authorizeSubKColl.addDataField("fldvalue05", "ClctnPymtFlg@" + "PAY");//收付款标志
				authorizeSubKColl.addDataField("fldvalue06", "AmtTp@" + "PF");//金额类型
				authorizeSubKColl.addDataField("fldvalue07", "StlmntAcctPrimKeyCd@" + "");//结算账户主键
				authorizeSubKColl.addDataField("fldvalue08", "StlmntAcctNo@" + acct_no);//结算账号acct_no
				authorizeSubKColl.addDataField("fldvalue09", "StlmntAcctPdTp@" + "");//结算账户产品类型
				authorizeSubKColl.addDataField("fldvalue10", "StlmntAcctCcy@" + "");//结算账户币种
				authorizeSubKColl.addDataField("fldvalue11", "StlmntAcctSeqNo@" + "");//结算账户序号
				authorizeSubKColl.addDataField("fldvalue12", "StlmntCcy@" + valueOf(ccy));//结算币种
				authorizeSubKColl.addDataField("fldvalue13", "StlmntAmt@" + "");//结算金额
				authorizeSubKColl.addDataField("fldvalue14", "StlmntExgRate@" + "");//结算汇率
				authorizeSubKColl.addDataField("fldvalue15", "StlmntExgMd@" + "");//结算汇兑方式
				authorizeSubKColl.addDataField("fldvalue16", "AutoLockInd@" + "");//自动锁定标记
				authorizeSubKColl.addDataField("fldvalue17", "PrtyLvl@" + "");//优先级别
				authorizeSubKColl.addDataField("fldvalue18", "StlmntRto@" + "");//结算权重
				authorizeSubKColl.addDataField("fldvalue19", "StlmntNo@" + "");//结算编号
				authorizeSubKColl.addDataField("fldvalue20", "IvsRto@" + "");//出资比例
				authorizeSubKColl.addDataField("fldvalue21", "StlmntAcctNm@" + "");//结算账户名称
				authorizeSubKColl.addDataField("fldvalue22", "ClctnBnkNo@" + "");//收款行行号
				authorizeSubKColl.addDataField("fldvalue23", "ClctnBnkNm@" + "");//收款行名称
				authorizeSubKColl.addDataField("fldvalue24", "BnkInnrBnkOthrFlg@" + "");//行内行外标志
				authorizeSubKColl.addDataField("fldvalue25", "FrzPayMd@" + "");//冻结支付方式
				authorizeSubKColl.addDataField("fldvalue26", "FdcrPymtNo@" + "");//受托支付编号
				authorizeSubKColl.addDataField("fldvalue27", "TxnTp@" + "");//交易类型
				authorizeSubKColl.addDataField("fldvalue28", "DvdnPrftRto@" + "");//分成利润比例
				authorizeSubKColl.addDataField("fldvalue29", "OwnCorprtnFlg@" + "");//自营标志
				dao.insert(authorizeSubKColl, this.getConnection());
			}
			
			
			
			
			//生成授权信息：结算账户信息（委托贷款时生成委托账号授权信息）
			//个人委托贷款与企业委托贷款
			if(prd_id.equals("100063") || prd_id.equals("100065")){
				String acct_no = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_inner_dep_no"));//委托人内部存款账号
				String acct_name = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_inner_dep_name"));//委托人内部存款户名
				String csgn_acct_no = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_acct_no"));//委托人一般账号
				String csgn_acct_name = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_acct_name"));//委托人一般账号户名
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "StlmntBrBnkCd@" + "9901");//结算分行代码
				authorizeSubKColl.addDataField("fldvalue02", "StlmntBnkCstNo@" + "9999");//结算行客户号
				authorizeSubKColl.addDataField("fldvalue03", "StlmntAcctTp@" + "TPP");//结算账户类型
				authorizeSubKColl.addDataField("fldvalue04", "StlmntMd@" + "R");//结算方式
				authorizeSubKColl.addDataField("fldvalue05", "ClctnPymtFlg@" + "PAY");//收付款标志
				authorizeSubKColl.addDataField("fldvalue06", "AmtTp@" + "PF");//金额类型
				authorizeSubKColl.addDataField("fldvalue07", "StlmntAcctPrimKeyCd@" + "");//结算账户主键
				authorizeSubKColl.addDataField("fldvalue08", "StlmntAcctNo@" + "");//结算账号
				authorizeSubKColl.addDataField("fldvalue09", "StlmntAcctPdTp@" + "");//结算账户产品类型
				authorizeSubKColl.addDataField("fldvalue10", "StlmntAcctCcy@" + "");//结算账户币种
				authorizeSubKColl.addDataField("fldvalue11", "StlmntAcctSeqNo@" + "");//结算账户序号
				authorizeSubKColl.addDataField("fldvalue12", "StlmntCcy@" + valueOf(cont_cur_type));//结算币种
				authorizeSubKColl.addDataField("fldvalue13", "StlmntAmt@" + "");//结算金额
				authorizeSubKColl.addDataField("fldvalue14", "StlmntExgRate@" + "");//结算汇率
				authorizeSubKColl.addDataField("fldvalue15", "StlmntExgMd@" + "");//结算汇兑方式
				authorizeSubKColl.addDataField("fldvalue16", "AutoLockInd@" + "");//自动锁定标记
				authorizeSubKColl.addDataField("fldvalue17", "PrtyLvl@" + "");//优先级别
				authorizeSubKColl.addDataField("fldvalue18", "StlmntRto@" + "");//结算权重
				authorizeSubKColl.addDataField("fldvalue19", "StlmntNo@" + "");//结算编号
				authorizeSubKColl.addDataField("fldvalue20", "IvsRto@" + "");//出资比例
				authorizeSubKColl.addDataField("fldvalue21", "StlmntAcctNm@" + "");//结算账户名称
				authorizeSubKColl.addDataField("fldvalue22", "ClctnBnkNo@" + "");//收款行行号
				authorizeSubKColl.addDataField("fldvalue23", "ClctnBnkNm@" + "");//收款行名称
				authorizeSubKColl.addDataField("fldvalue24", "BnkInnrBnkOthrFlg@" + "");//行内行外标志
				authorizeSubKColl.addDataField("fldvalue25", "FrzPayMd@" + "");//冻结支付方式
				authorizeSubKColl.addDataField("fldvalue26", "FdcrPymtNo@" + "");//受托支付编号
				authorizeSubKColl.addDataField("fldvalue27", "TxnTp@" + "");//交易类型
				authorizeSubKColl.addDataField("fldvalue28", "DvdnPrftRto@" + "");//分成利润比例
				authorizeSubKColl.addDataField("fldvalue29", "OwnCorprtnFlg@" + "");//自营标志
				dao.insert(authorizeSubKColl, this.getConnection());
			}
			
			//生成台账信息
			KeyedCollection accLoanKColl = new KeyedCollection(ACCLOANMODEL);
			accLoanKColl.addDataField("serno", Pvpserno);//业务编号	
			accLoanKColl.addDataField("acc_day", date);//日期
			accLoanKColl.addDataField("acc_year", date.substring(0, 4));//年份
			accLoanKColl.addDataField("acc_mon", date.substring(5, 7));//月份
			accLoanKColl.addDataField("prd_id", prd_id);//产品编号
			accLoanKColl.addDataField("cus_id", cus_id);//客户编码
			accLoanKColl.addDataField("cont_no", cont_no);//合同编号
			accLoanKColl.addDataField("bill_no", bill_no);//借据编号
			accLoanKColl.addDataField("loan_amt", pvp_amt);//贷款金额
			accLoanKColl.addDataField("loan_balance", pvp_amt);//贷款余额
			accLoanKColl.addDataField("distr_date", date);//发放日期
			accLoanKColl.addDataField("end_date", end_date);//到期日期
			accLoanKColl.addDataField("ori_end_date", end_date);//原到期日期
			accLoanKColl.addDataField("post_count", "0");//展期次数
			accLoanKColl.addDataField("overdue", "0");//逾期期数
			accLoanKColl.addDataField("separate_date", "");//清分日期
			accLoanKColl.addDataField("ruling_ir", ruling_ir);//基准利率
			accLoanKColl.addDataField("reality_ir_y", reality_ir_y);//执行年利率
			accLoanKColl.addDataField("overdue_rate_y", overdue_rate_y_iqp);//逾期利率
			accLoanKColl.addDataField("default_rate_y", default_rate_y);//违约利率
			accLoanKColl.addDataField("comp_int_balance", "0");//复利余额
			accLoanKColl.addDataField("inner_owe_int", "0");//表内欠息
			accLoanKColl.addDataField("out_owe_int", "0");//表外欠息
			accLoanKColl.addDataField("rec_int_accum", "0");//应收利息累计
			accLoanKColl.addDataField("recv_int_accum", "0");//实收利息累计
			accLoanKColl.addDataField("normal_balance", pvp_amt);//正常余额
			accLoanKColl.addDataField("overdue_balance", "0");//逾期余额
			accLoanKColl.addDataField("slack_balance", "0");//呆滞余额
			accLoanKColl.addDataField("bad_dbt_balance", "0");//呆账余额
			accLoanKColl.addDataField("writeoff_date", "");//核销日期
			accLoanKColl.addDataField("paydate", "");//转垫款日
			accLoanKColl.addDataField("five_class", five_classfiy);//五级分类
			accLoanKColl.addDataField("twelve_cls_flg", "");//十二级分类
			accLoanKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			accLoanKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
			accLoanKColl.addDataField("acc_status", "0");//台帐状态
			accLoanKColl.addDataField("cur_type", cur_type);//币种
			dao.insert(accLoanKColl, this.getConnection());
			
			//是否委托委托
			////王小虎注释
			/*if("1".equals(is_trust_loan)){
				String serno4AccTranTrustCompany = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", this.getConnection(), this.getContext());
				KeyedCollection accTranTrustCompanyKColl = new KeyedCollection(AccTranTrustCompany);
				accTranTrustCompanyKColl.addDataField("serno", serno4AccTranTrustCompany);//交易明细编号
				accTranTrustCompanyKColl.addDataField("bill_no", bill_no);//借据号
				accTranTrustCompanyKColl.addDataField("cont_no", cont_no);//合同号
				accTranTrustCompanyKColl.addDataField("list_type", "0");//明细类型 (发放)
				accTranTrustCompanyKColl.addDataField("cur_type", cur_type);//币种
				accTranTrustCompanyKColl.addDataField("tran_amt", pvp_amt);//交易金额
				accTranTrustCompanyKColl.addDataField("reclaim_mode", "");//回收方式
				accTranTrustCompanyKColl.addDataField("tran_date", date);//交易日期
				accTranTrustCompanyKColl.addDataField("input_id", input_id);//登记人
				accTranTrustCompanyKColl.addDataField("input_br_id", input_br_id);//登记机构
				accTranTrustCompanyKColl.addDataField("input_date", date);//登记日期
				dao.insert(accTranTrustCompanyKColl,  this.getConnection());
			}*/
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl4trade = new KeyedCollection();
//			kColl4trade.put("CLIENT_NO", cus_id);
//			kColl4trade.put("BUSS_SEQ_NO", loanSerno);
//			kColl4trade.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), this.getConnection());	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常！");
		}
	}
	/**
	 * 普通贷款出账流程审批通过
	 * @param serno 出账流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpLoanTest(String serno)throws ComponentException {
		try {
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			/** 1.数据准备：通过业务流水号查询【出账申请】 */
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, this.getConnection());
			String Pvpserno = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("serno"));//授权交易流水号
			String prd_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("prd_id"));//产品编号
			String cus_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cus_id"));//客户编码
			String cont_no = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cont_no"));//合同编号
			BigDecimal pvp_amt = BigDecimalUtil.replaceNull(pvpLoanKColl.getDataValue("pvp_amt"));//出账金额
			String manager_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("manager_br_id"));//管理机构
			String in_acct_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("in_acct_br_id"));//入账机构
			String cur_type = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cur_type"));//币种
			String date = this.getContext().getDataValue(PUBConstant.OPENDAY).toString();//营业日期
			String input_id = this.getContext().getDataValue(PUBConstant.currentUserId).toString();//登记人
			String input_br_id = this.getContext().getDataValue(PUBConstant.organNo).toString();//登记机构
			
			/** 核算与信贷业务品种映射 START */
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			String lmPrdId = service.getPrdBasicCLPM2LM(cont_no, prd_id, this.getContext(), this.getConnection());
			/** 核算与信贷业务品种映射 END */
			
			
			/** 2.数据准备：通过业务流水号查询【业务申请】【合同信息】 */					
			KeyedCollection ctrContKColl =  dao.queryDetail(CTRCONTMODEL, cont_no, this.getConnection());
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, cont_no, this.getConnection());
			String loanSerno = TagUtil.replaceNull4String(ctrContKColl.getDataValue("serno"));//业务申请流水号
			String ruling_ir = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir"));//基准利率（年）
			String ir_float_rate = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_rate"));//浮动比例
			String ir_float_point = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_point"));//浮动点数
			String reality_ir_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("reality_ir_y"));//执行利率（年）
			String overdue_rate_y_iqp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("overdue_rate_y"));//逾期利率
			String default_rate_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("default_rate_y"));//违约利率
			String five_classfiy = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("five_classfiy"));//五级分类
			String cont_start_date = TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_start_date"));//合同起始日期
			String cont_end_date = TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_end_date"));//合同到期日期
			String repay_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_type"));//还款方式
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 start
			BigDecimal security_rate = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("security_rate"));//保证金比例
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 end
			Double security_amt = TagUtil.replaceNull4Double(ctrContKColl.getDataValue("security_amt"));//保证金金额
			String assure_main = TagUtil.replaceNull4String(ctrContKColl.getDataValue("assure_main"));//担保方式
			String repay_mode_type = "";//还款方式种类
			if(repay_type!=null && !"".equals(repay_type)){
				KeyedCollection prdRepayModeKColl = dao.queryDetail(PrdRepayMode, repay_type, this.getConnection());
				repay_mode_type = TagUtil.replaceNull4String(prdRepayModeKColl.getDataValue("repay_mode_type"));
			}   
			String is_collect_stamp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("is_collect_stamp"));//是否收取印花税
			String stamp_collect_mode = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("stamp_collect_mode"));//印花税收取方式
			String pay_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("pay_type"));//支付方式
			//信贷支付方式为：0：自主支付 1：受托支付 2：跨境受托支付。核算支付方式为：1：自主支付 2：受托支付 3：跨境受托支付
			/** modified by yangzy 20150112 受托支付需求开发(个贷特殊处理) start **/
			/** modified by yangzy 20140911 需求变化：XD140901056，内容：受托支付需求开发 start **/
			CMISModualServiceFactory jndiSer = CMISModualServiceFactory.getInstance();
			CusServiceInterface csInfo = (CusServiceInterface)jndiSer.getModualServiceById("cusServices", "cus");
			CusBase cusBaseInfo = csInfo.getCusBaseByCusId(cus_id,this.getContext(),this.getConnection());
			String belgLine = TagUtil.replaceNull4String(cusBaseInfo.getBelgLine());//所属条线
			if("BL300".equals(belgLine)){
				if(pay_type.equals("0")){
					pay_type= "1";
				}else if(pay_type.equals("1")){
					pay_type= "2";
				}else if(pay_type.equals("2")){
					pay_type= "3";
				}else if(pay_type == null || "".equals(pay_type)){
					pay_type= "";
				}else{
					throw new EMPException("未知支付方式！");
				}
			}else{
				if(pay_type.equals("0")){
					pay_type= "1";
				}else if(pay_type.equals("1")){
					//pay_type= "2";
					pay_type= "1";
				}else if(pay_type.equals("2")){
					//pay_type= "3";
					pay_type= "1";
				}else if(pay_type == null || "".equals(pay_type)){
					pay_type= "";
				}else{
					throw new EMPException("未知支付方式！");
				}
			}
			/** modified by yangzy 20140911 需求变化：XD140901056，内容：受托支付需求开发 end **/
			/** modified by yangzy 20150112 受托支付需求开发(个贷特殊处理) end **/
			//获取委托贷款关联信息
			KeyedCollection IqpCsgnLoanKColl = dao.queryDetail("IqpCsgnLoanInfo", loanSerno, this.getConnection());
			String csgn_cus_id = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_cus_id"));//委托人客户号
			Double csgn_chrg_pay_rate = TagUtil.replaceNull4Double(IqpCsgnLoanKColl.getDataValue("csgn_chrg_pay_rate"));//委托人手续费支付比例
			Double debit_chrg_pay_rate = TagUtil.replaceNull4Double(IqpCsgnLoanKColl.getDataValue("debit_chrg_pay_rate"));//借款人手续费支付比例
			BigDecimal cont_amt = new BigDecimal(TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_amt")));//合同金额
			String cont_cur_type = TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_cur_type"));//合同币种
			String repay_bill = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_bill"));//偿还借据，无间贷的原借据
			Double pad_rate_y = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("pad_rate_y"));//垫款利率
			Integer cont_term = TagUtil.replaceNull4Int(ctrContSubKColl.getDataValue("cont_term"));//合同期限
			String term_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("term_type"));//期限类型
			String repay_date = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_date"));//还款日
			String ir_accord_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_accord_type"));//利率依据方式
			String ruling_ir_code = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir_code"));//基准利率代码
			Double overdue_rate_y = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("overdue_rate_y"));//罚息执行利率
			String repay_term = "";
			String repay_space = "";
			if(repay_type.equals("A005")){//利随本清传1月
				repay_term = "M";//还款间隔单位
				repay_space = "1";//还款间隔
			}else{
				repay_term = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_term"));//还款间隔单位
				repay_space = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_space"));//还款间隔
			}
			String loan_nature = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("loan_nature"));//贷款性质
			String is_promissory_note = TagUtil.replaceNull4String(ctrContKColl.getDataValue("is_promissory_note"));//是否承诺函下
			Double default_rate =  TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("default_rate"));//罚息执行利率浮动比
			String ir_adjust_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_adjust_type"));//下一次利率调整选项
			String ir_next_adjust_term = "";//下一次利率调整间隔
			String ir_next_adjust_unit = "";//下一次利率调整间隔单位
			String fir_adjust_day = "";//第一次调整日
			//固定不变
			if(ir_adjust_type.equals("0")){
				ir_adjust_type = "";
			}
			//按月调整
			else if(ir_adjust_type.equals("1")){
				ir_adjust_type = "FIX";
				ir_next_adjust_term = "1";
				ir_next_adjust_unit = "M";
				fir_adjust_day = DateUtils.getNextDate("M", date);
			}
			//按季调整
			else if(ir_adjust_type.equals("2")){
				ir_adjust_type = "FIX";
				ir_next_adjust_term = "1";
				ir_next_adjust_unit = "Q";
				fir_adjust_day = DateUtils.getNextDate("Q", date);
			}
			//按年调整
			else if(ir_adjust_type.equals("3")){
				ir_adjust_type = "FIX";
				ir_next_adjust_term = "1";
				ir_next_adjust_unit = "Y";
				fir_adjust_day = DateUtils.getNextDate("Y", date);
			}
			Double overdue_rate = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("overdue_rate"));//逾期利率浮动比
			String is_term = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("is_term"));//期供标志
			if(is_term.equals("1")){
				is_term = "Y";
			}else{
				is_term = "N";
			}
			String promissory_note = TagUtil.replaceNull4String(ctrContKColl.getDataValue("promissory_note"));//贷款承诺协议号
			String promissory_note_billno = "";
			if(promissory_note!=null&&!promissory_note.equals("")){
				//获取贷款承诺借据号
				KeyedCollection loankc = dao.queryFirst("AccLoan", null, " where cont_no = '"+promissory_note+"'", this.getConnection());
				promissory_note_billno = (String) loankc.getDataValue("bill_no");
			}
			String is_trust_loan = TagUtil.replaceNull4String(ctrContKColl.getDataValue("is_trust_loan"));//是否信托贷款
			
			//获取贷款申请相关信息
			KeyedCollection iqpLoanAppKColl =  dao.queryDetail(IQPLOANAPPMODEL, loanSerno, this.getConnection());
			String apply_date = TagUtil.replaceNull4String(iqpLoanAppKColl.getDataValue("apply_date"));//业务申请日期

			//通过客户编号查询【客户信息】
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
			CusBase cusBase = csi.getCusBaseByCusId(cus_id,this.getContext(),this.getConnection());
			String cus_name = TagUtil.replaceNull4String(cusBase.getCusName());//客户名称
			String cert_type = TagUtil.replaceNull4String(cusBase.getCertType());//证件类型
			String cert_code = TagUtil.replaceNull4String(cusBase.getCertCode());//证件号码
			String belg_line = TagUtil.replaceNull4String(cusBase.getBelgLine());//所属条线
			
			//生成借据编号
			PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
            .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), this.getConnection());
			String bill_no = cmisComponent.getBillNoByContNo(cont_no);//借据编号			
			String end_date = DateUtils.getEndDate(term_type, date, cont_term);//借据到期日
			
			//生成授权主表信息
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());//生成交易流水号
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());//生成授权编号
			authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
			authorizeKColl.addDataField("serno", Pvpserno);//业务流水号（出账编号）
			authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
			authorizeKColl.addDataField("prd_id", prd_id);//产品编号
			authorizeKColl.addDataField("cus_id", cus_id);//客户编码
			authorizeKColl.addDataField("cus_name", cus_name);//客户名称
			authorizeKColl.addDataField("cont_no", cont_no);//合同编号
			authorizeKColl.addDataField("bill_no", bill_no);//借据编号
			if(prd_id.equals("400021")){//境内保函
			   authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_BHFFSQ + TradeConstance.SERVICE_SCENE_BHFFSQ);
			}else if(prd_id.equals("400022") || prd_id.equals("400023")){//贷款承诺与信贷证明
				authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_DKCNSQ + TradeConstance.SERVICE_SCENE_DKCNSQ);
			}else{//普通贷款
			   authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_DKFFSQ + TradeConstance.SERVICE_SCENE_DKFFSQ);
			}
			authorizeKColl.addDataField("tran_amt", pvp_amt);//交易金额
			authorizeKColl.addDataField("tran_date", date);//交易日期
			authorizeKColl.addDataField("send_times", "0");//发送次数
			authorizeKColl.addDataField("return_code", "");//返回编码
			authorizeKColl.addDataField("return_desc", "");//返回信息
			authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
			authorizeKColl.addDataField("status", "00");//状态
			authorizeKColl.addDataField("cur_type", cur_type);//币种
			
			//费用信息
			String conditionFee = "where serno='"+loanSerno+"'";
			IndexedCollection iqpAppendTermsIColl = dao.queryList(IQPFEE, conditionFee, this.getConnection());
			
			//计算手续费率  start
			BigDecimal chrg_rate = new BigDecimal("0.00");
			BigDecimal commission = new BigDecimal("0.00");
			for(int fee_i=0;fee_i<iqpAppendTermsIColl.size();fee_i++){
				KeyedCollection feekc = (KeyedCollection) iqpAppendTermsIColl.get(fee_i);
				String fee_rate_str = TagUtil.replaceNull4String(feekc.getDataValue("fee_rate"));
				if(fee_rate_str==null||fee_rate_str.equals("")){
					fee_rate_str = "0";
				}
				BigDecimal fee_rate = new BigDecimal(fee_rate_str);
				chrg_rate = chrg_rate.add(fee_rate);
				
				//手续费不使用手续费率计算，固定金额直接加，防止精度丢失
				String collect_type = TagUtil.replaceNull4String(feekc.getDataValue("collect_type"));//01-按固定金额，02-按比率
				BigDecimal fee_amt = BigDecimalUtil.replaceNull(feekc.getDataValue("fee_amt"));
				if("02".equals(collect_type)){
					commission = commission.add(cont_amt.multiply(fee_rate));
				}else{
					commission = commission.add(fee_amt);
				}
			}
			//计算手续费率  end
			
			//HS141110017_保理业务改造  add by zhaozq start
			Boolean isBwBl = false;//是否表外保理
			KeyedCollection iqpInterFact = null;
			if(prd_id.equals("800021")){
				iqpInterFact = dao.queryDetail("IqpInterFact", loanSerno, this.getConnection());
				String fin_type = (String) iqpInterFact.getDataValue("fin_type");
				if("2".equals(fin_type)){
					isBwBl = true;
				}
			}
			//HS141110017_保理业务改造  add by zhaozq end
			
			//境内保函业务：授权信息组装 
			if(prd_id.equals("400021")){
				/* 查询保函信息 */
				KeyedCollection iqpGuarantInfokColl = dao.queryDetail(IqpGuarantInfo, loanSerno, this.getConnection());
				
				String ben_name = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("ben_name"));
				String guarant_mode = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("guarant_mode"));
				String guarant_type = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("guarant_type"));
				String open_type = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("open_type"));
				String ben_acct_org_no = TagUtil.replaceNull4String(iqpGuarantInfokColl.getDataValue("ben_acct_org_no"));//受益人开户行号
				KeyedCollection orgkc = dao.queryDetail("PrdBankInfo", ben_acct_org_no, this.getConnection());
				
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编码
				authorizeKColl.addDataField("fldvalue02", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue03", "BRANCH_ID@" + in_acct_br_id);//机构代码
				authorizeKColl.addDataField("fldvalue04", "DUEBILL_NO@" + bill_no);//借据编号
				authorizeKColl.addDataField("fldvalue05", "GT_AGREE_NO@" + cont_no);//保函协议号
				authorizeKColl.addDataField("fldvalue06", "GLOBAL_TYPE@" + cert_type);//证件类型
				authorizeKColl.addDataField("fldvalue07", "GLOBAL_ID@" + cert_code);//证件号码
				authorizeKColl.addDataField("fldvalue08", "ISS_CTRY@" + "CN");//发证国家
				authorizeKColl.addDataField("fldvalue09", "CLIENT_NO@" + cus_id);//客户码
				authorizeKColl.addDataField("fldvalue10", "GT_NAME@" + cus_name);//保函所属人名称(取借款人名称)
				authorizeKColl.addDataField("fldvalue11", "CCY@" + cont_cur_type);//币种
				authorizeKColl.addDataField("fldvalue12", "LOAN_TYPE@" + lmPrdId);//贷款品种
				authorizeKColl.addDataField("fldvalue13", "GT_TYPE@" + guarant_mode);//保函类型
				authorizeKColl.addDataField("fldvalue14", "GT_AMOUNT@" + cont_amt);//保函金额
				authorizeKColl.addDataField("fldvalue15", "COMMISSION@" + commission);//手续费
				authorizeKColl.addDataField("fldvalue16", "DEDUCT_METHOD@" + "AUTOPAY");//扣款方式 可固定填AUTOPAY 自动扣款  MANUAL 手工主动还款
				authorizeKColl.addDataField("fldvalue17", "OD_INT_RATE@" + pad_rate_y);//垫款利率
				authorizeKColl.addDataField("fldvalue18", "OPEN_DATE@" + TagUtil.formatDate(date));//开办日期
				authorizeKColl.addDataField("fldvalue19", "EXPIRY_DATE@" + TagUtil.formatDate(end_date));//到期日期 
				authorizeKColl.addDataField("fldvalue20", "GT_KIND@" + guarant_type);//保函类型
				authorizeKColl.addDataField("fldvalue21", "OPEN_TYPE@" + open_type);//保函类型
				if(orgkc!=null&&orgkc.getDataValue("bank_name")!=null){
					authorizeKColl.addDataField("fldvalue22", "BENEFIT_ORG_NAME@" + orgkc.getDataValue("bank_name"));//受益人开户行名
				}else{
					authorizeKColl.addDataField("fldvalue22", "BENEFIT_ORG_NAME@" + "");//受益人开户行名
				}
				
			}
			//贷款承诺与信贷证明：授权信息组装 
			else if(prd_id.equals("400022") || prd_id.equals("400023")){
				/*查询出贷款承诺从表信息 */
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编码
				authorizeKColl.addDataField("fldvalue02", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue03", "BRANCH_ID@" + in_acct_br_id);//机构代码
				authorizeKColl.addDataField("fldvalue04", "DUEBILL_NO@" + bill_no);//借据编号
				authorizeKColl.addDataField("fldvalue05", "LOAN_PROMISE_AGREE_NO@" + cont_no);//贷款承诺协议号
				authorizeKColl.addDataField("fldvalue06", "CCY@" + cont_cur_type);//币种
				authorizeKColl.addDataField("fldvalue07", "LOAN_PROMISE_AMOUNT@" + cont_amt);//贷款承诺金额
				authorizeKColl.addDataField("fldvalue08", "COMMISSION@"+ commission);//手续费
				authorizeKColl.addDataField("fldvalue09", "GLOBAL_TYPE@" + cert_type);//证件类型
				authorizeKColl.addDataField("fldvalue10", "GLOBAL_ID@" + cert_code);//证件号码
				authorizeKColl.addDataField("fldvalue11", "ISS_CTRY@" + "CN");//发证国家
				authorizeKColl.addDataField("fldvalue12", "CLIENT_NO@" + cus_id);//客户码
				authorizeKColl.addDataField("fldvalue13", "CLIENT_NAME@" + cus_name);//客户名称
				authorizeKColl.addDataField("fldvalue14", "TERM@" + cont_term);//期限
				authorizeKColl.addDataField("fldvalue15", "TERM_TYPE@" + term_type);//期限类型
				authorizeKColl.addDataField("fldvalue16", "OPEN_DATE@" + TagUtil.formatDate(date));//开办日期
				authorizeKColl.addDataField("fldvalue17", "EXPIRY_DATE@" + TagUtil.formatDate(cont_end_date));//到期日期，合同到期日期
				authorizeKColl.addDataField("fldvalue18", "LOAN_TYPE@" + lmPrdId);//产品代码
			}else if(isBwBl){
				/*表外保理：授权信息组装*/
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编码
				authorizeKColl.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据编号
				authorizeKColl.addDataField("fldvalue03", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue04", "FACTOR_AGREE_NO@" + cont_no);//保理协议号
				authorizeKColl.addDataField("fldvalue05", "BRANCH_ID@" + in_acct_br_id);//机构代码
				authorizeKColl.addDataField("fldvalue06", "CLIENT_NO@" + cus_id);//客户码
				authorizeKColl.addDataField("fldvalue07", "CLIENT_NAME@" + cus_name);//客户名称
				authorizeKColl.addDataField("fldvalue08", "CCY@" + cont_cur_type);//币种
				authorizeKColl.addDataField("fldvalue09", "FACTOR_BALLANCE@" + cont_amt);//保理余额
				authorizeKColl.addDataField("fldvalue10", "FACTOR_DRAWDOWN_AMT@" + cont_amt);//保理发放金额
				authorizeKColl.addDataField("fldvalue11", "DRAW_DOWN_DATE@" + TagUtil.formatDate(date));//发放日期
				authorizeKColl.addDataField("fldvalue12", "EXPIRY_DATE@" + TagUtil.formatDate(cont_end_date));//到期日期
				authorizeKColl.addDataField("fldvalue13", "CLEARANCE_DATE@" + "");//结清日期
				authorizeKColl.addDataField("fldvalue14", "LOAN_TYPE@" + lmPrdId);//产品代码
				authorizeKColl.addDataField("fldvalue15", "RULE_CODE@" + "");//规则代码
				authorizeKColl.addDataField("fldvalue16", "FACTOR_STATUS@" + "NORM");//保理状态
				authorizeKColl.addDataField("fldvalue17", "REMARK@" + "");//备注
				authorizeKColl.addDataField("fldvalue18", "FEE_AMOUNT@" + commission);//手续费
			}
			/*其他普通贷款：授权信息组装*/
			else{
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编码
				authorizeKColl.addDataField("fldvalue02", "APPLY_NO@" + serno);//业务流水号
				authorizeKColl.addDataField("fldvalue03", "APPLY_DATE@" + TagUtil.formatDate(apply_date));//业务申请日期
				authorizeKColl.addDataField("fldvalue04", "CONTRACT_NO@" + cont_no);//合同编号
				authorizeKColl.addDataField("fldvalue05", "BRANCH_ID@" + in_acct_br_id);//入账机构
				authorizeKColl.addDataField("fldvalue06", "DUEBILL_NO@" + bill_no);//借据编号
				authorizeKColl.addDataField("fldvalue07", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码（待修改为裕明银行代码）
				authorizeKColl.addDataField("fldvalue08", "CLIENT_NO@" + cus_id);//客户码
				authorizeKColl.addDataField("fldvalue09", "CLIENT_NAME@" + cus_name);//客户名称
				authorizeKColl.addDataField("fldvalue10", "GLOBAL_TYPE@" + cert_type);//证件类型（非必输）
				authorizeKColl.addDataField("fldvalue11", "GLOBAL_ID@" + cert_code);//证件号码（非必输）
				authorizeKColl.addDataField("fldvalue12", "ISS_CTRY@" + "CN");//发证国家
				authorizeKColl.addDataField("fldvalue13", "DEALER_CDE@" + "");//经销商代码（非必输,不填）
				authorizeKColl.addDataField("fldvalue14", "CCY@" + cont_cur_type);//币种
				authorizeKColl.addDataField("fldvalue15", "APPLY_AMOUNT@" + pvp_amt);//申请金额
				authorizeKColl.addDataField("fldvalue16", "DRAW_DOWN_DATE@" + TagUtil.formatDate(date));//发放日期
				authorizeKColl.addDataField("fldvalue17", "LOAN_TYPE@" + lmPrdId );//贷款品种
				authorizeKColl.addDataField("fldvalue18", "CONTRACT_EXPIRY_DATE@" + TagUtil.formatDate(end_date));//合同到期日
				authorizeKColl.addDataField("fldvalue19", "VALUE_DATE@" + TagUtil.formatDate(date));//起息日
				authorizeKColl.addDataField("fldvalue20", "DEDUCT_DATE@" + repay_date);//扣款日
				
				if(ir_accord_type == null || ir_accord_type.equals("") || ir_accord_type.equals("03")){
					ir_accord_type = "";
				}else if(ir_accord_type.equals("01")){
					//议价利率依据
					ir_accord_type = "FX";//FX: 固定利率
				}else{
					//牌告利率依据、正常利率上浮动
					if(ir_adjust_type == null || ir_adjust_type.equals("")){//固定不变
						ir_accord_type = "FX";//FX:固定利率
					}else{
						ir_accord_type = "RV";//RV: 浮动利率
					}
				}
				authorizeKColl.addDataField("fldvalue21", "INT_RATE_MODE@" + ir_accord_type);//利率模式
				authorizeKColl.addDataField("fldvalue22", "INT_RATE_BASE@" + "Y");//利率基础
				authorizeKColl.addDataField("fldvalue23", "BASE_INT_RATE_CODE@" + ruling_ir_code);//利率类型=基准利率代码 
				authorizeKColl.addDataField("fldvalue24", "BASE_INT_RATE@" + ruling_ir);//基准利率
				authorizeKColl.addDataField("fldvalue25", "INT_FLT_RATE@" + ir_float_rate);//利率浮动比例
				authorizeKColl.addDataField("fldvalue26", "SPREAD@" + ir_float_point);//利差
				authorizeKColl.addDataField("fldvalue27", "ACT_INT_RATE@" + reality_ir_y);//执行利率
				authorizeKColl.addDataField("fldvalue28", "OD_RATE_BASE@"+"Y");//罚息利率基础
				authorizeKColl.addDataField("fldvalue29", "LOAN_OD_RATE_CODE@"+"");//罚息利率代码
				authorizeKColl.addDataField("fldvalue30", "LOAN_OD_BASE_RATE@"+"");//基准罚息利率
				authorizeKColl.addDataField("fldvalue31", "LOAN_OD_BASE_FLT_RATE@" + "");//罚息基准利率浮动比
				authorizeKColl.addDataField("fldvalue32", "LOAN_OD_ACT_RATE@" + overdue_rate_y);//罚息执行利率
				authorizeKColl.addDataField("fldvalue33", "REPAY_FREQUENCY_UNIT@" + repay_term);//还款间隔单位
				authorizeKColl.addDataField("fldvalue34", "REPAY_FREQUENCY@" + repay_space);//还款间隔
				authorizeKColl.addDataField("fldvalue35", "LOAN_REPAY_METHOD@" + repay_type);//还款方式
				authorizeKColl.addDataField("fldvalue36", "LOAN_REPAY_TYPE@" + repay_mode_type);//还款方式类型
				String buss_source = "";
				String CONSIGN_AGREE = "";
				if(prd_id.equals("100055")||prd_id.equals("100056")){
					buss_source = "TLOAN";//银团贷款，使用委托贷款数据来源
				}else if(loan_nature.equals("2")){//是委托贷款
					buss_source = "TLOAN";
					CONSIGN_AGREE = cont_no;//传合同号
				}else if(is_promissory_note.equals("1")){//不是委托贷款，但是承诺函下
					buss_source = "DLOAN";
				}else if(prd_id.equals("800021")&&!isBwBl){
					buss_source = "SLOAN";//本息分开扣
				}else{
					buss_source = "NLOAN";
				}
				authorizeKColl.addDataField("fldvalue37", "BUSS_SOURCE@" + buss_source);//业务数据来源
				authorizeKColl.addDataField("fldvalue38", "LOAN_GRACE_TYPE@" + "P");//宽限期类型
				authorizeKColl.addDataField("fldvalue39", "LOAN_GRACE_DAYS@" + "0");//宽限期天数
				authorizeKColl.addDataField("fldvalue40", "DEDUCT_METHOD@" + "AUTOPAY");//扣款方式
				authorizeKColl.addDataField("fldvalue41", "FIX_OD_RATE_FLAG@" + "N");//是否采用固定罚息利率
				authorizeKColl.addDataField("fldvalue42", "LOAN_OD_RATE_TYPE@" + "L");//罚息利率种类
				authorizeKColl.addDataField("fldvalue43", "LOAN_OD_ACT_FLT_RATE@" + default_rate);//罚息执行利率浮动比
				authorizeKColl.addDataField("fldvalue44", "LOAN_OD_COMM_PART@" + "");//计算罚息部分
				authorizeKColl.addDataField("fldvalue45", "LOAN_OD_CPD_FLAG@" + "");//是否计算罚息复利
				authorizeKColl.addDataField("fldvalue46", "NEXT_RADJ_OPT@" + ir_adjust_type);//下一次利率调整选项
				authorizeKColl.addDataField("fldvalue47", "NEXT_RADJ_FREQ@" + ir_next_adjust_term);//下一次利率调整间隔
				authorizeKColl.addDataField("fldvalue48", "NEXT_RADJ_FREQ_UNIT@" + ir_next_adjust_unit);//下一次利率调整间隔单位
				authorizeKColl.addDataField("fldvalue49", "FIRST_ADJUST_DATE@" + fir_adjust_day);//第一次调整日
				authorizeKColl.addDataField("fldvalue50", "DIVERT_FLT_RATE@" + "");//挪用利率浮动比例
				authorizeKColl.addDataField("fldvalue51", "OVERDUE_ACT_RATE@" + overdue_rate_y);//逾期执行利率
				authorizeKColl.addDataField("fldvalue52", "OVERDUE_FLT_RATE@" + overdue_rate);//逾期利率浮动比
				String is_close_loan = (String) ctrContSubKColl.getDataValue("is_close_loan");
				if(is_close_loan.equals("1")){
					authorizeKColl.addDataField("fldvalue53", "ASSET_BUY_FLAG@" + "W");//是否资产买入，无间贷传W
				}else{
					authorizeKColl.addDataField("fldvalue53", "ASSET_BUY_FLAG@" + "N");//是否资产买入
				}
				authorizeKColl.addDataField("fldvalue54", "CONSIGN_AGREE@" + CONSIGN_AGREE);//委托协议
				authorizeKColl.addDataField("fldvalue55", "TERM_PAY_FLAG@" + is_term);//期供标志
				authorizeKColl.addDataField("fldvalue56", "LOAN_PROMISE_DUEBILL_NO@" + promissory_note_billno);//贷款承诺借据号				
				authorizeKColl.addDataField("fldvalue57","STAMP_TAX_PAY_FLAG@" + is_collect_stamp);	//是否收取印花税
				authorizeKColl.addDataField("fldvalue58","STAMP_TAX_PAY_TYPE@" + stamp_collect_mode);	//印花税收取方式
				authorizeKColl.addDataField("fldvalue59","PAY_METHOD@" + pay_type);	//支付类型
				authorizeKColl.addDataField("fldvalue60","CLIENT_TYPE@" + belg_line);	//客户类型
				authorizeKColl.addDataField("fldvalue61","CONSIGN_CLIENT_NO@" + csgn_cus_id);	//委托人客户号
				authorizeKColl.addDataField("fldvalue62","CONS_COMMI_PAY_RATE@" + csgn_chrg_pay_rate);	//委托人手续费支付比例
				authorizeKColl.addDataField("fldvalue63","DR_COMMI_PAY_RATE@" + debit_chrg_pay_rate);	//借款人手续费支付比例
				if(is_close_loan.equals("1")){
					authorizeKColl.addDataField("fldvalue64","ORI_DUEBILL_NO@" + repay_bill);	//原借据号，无间贷
				}else{
					authorizeKColl.addDataField("fldvalue64","ORI_DUEBILL_NO@" + "");	//原借据号
				}
				
				//HS141110017_保理业务改造  add by zhaozq start
				if(prd_id.equals("800021")&&!isBwBl){
					authorizeKColl.addDataField("fldvalue65","PRE_INT_FLAG@" + iqpInterFact.getDataValue("is_pay_int"));//是否预收息
				
					//add by wangj 2015-06-02 HS141110017_保理业务改造 begin
					String buy_cus_id=TagUtil.replaceNull4String(iqpInterFact.getDataValue("buy_cus_id"));//买方客户号
					String po_no=TagUtil.replaceNull4String(iqpInterFact.getDataValue("po_no"));//池编号
					KeyedCollection buyKColl =  dao.queryDetail("CusBase", buy_cus_id, this.getConnection());//客户信息
					KeyedCollection iamKColl =  dao.queryDetail("IqpActrecpoMana", po_no, this.getConnection());//保理池信息
					authorizeKColl.addDataField("fldvalue66","CREDITOR_NO@" + buy_cus_id);//债务人编号
					authorizeKColl.addDataField("fldvalue67","CREDITOR_NAME@" + buyKColl.getDataValue("cus_name"));//债务人名称
					authorizeKColl.addDataField("fldvalue68","OUGHT_ACCEPT_AMT@" + iamKColl.getDataValue("crd_rgtchg_amt"));//应收账款金额
					// add by wangj 2015-06-02 HS141110017_保理业务改造 end
				
				}
				//HS141110017_保理业务改造  add by zhaozq end
			}
			dao.insert(authorizeKColl, this.getConnection());

			//生成授权信息：保证金信息
			//查询保证金信息表获取保证金相关信息
			////王小虎注释
			/*String condition = "where serno='"+loanSerno+"'";
			IndexedCollection iqpBailInfoIColl = dao.queryList(PUBBAILINFO, condition, this.getConnection());
			for(int i=0;i<iqpBailInfoIColl.size();i++){
				KeyedCollection iqpBailInfoKColl = (KeyedCollection)iqpBailInfoIColl.get(i);
				String bail_acct_no = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_no"));//保证金账号
				String bail_cur_type = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("cur_type"));//币种
				String bail_acct_name = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_name"));//保证金账号名称
				String open_org = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("open_org"));//开户机构
				String bail_acct_gl_code = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_gl_code"));//科目号
				String ccy = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("cur_type"));//币种
				String interbank_id = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("interbank_id"));//银联行号
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				//产品为：境内保函时，保证金信息数据单独组装
				if(prd_id.equals("400021")){
					authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
					authorizeSubKColl.addDataField("busi_cls", "04");//业务类别
					authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
					authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据号
					authorizeSubKColl.addDataField("fldvalue03", "BRANCH_ID@" + open_org);//机构代码
					authorizeSubKColl.addDataField("fldvalue04", "GUARANTEE_ACCT_NO@" + bail_acct_no);//保证金账号
					authorizeSubKColl.addDataField("fldvalue05", "GUARANTEE_NO@" + i);//保证金编号（传保证金个数序号）
					authorizeSubKColl.addDataField("fldvalue06", "ACCT_TYPE@" + "" );//账户类型
					authorizeSubKColl.addDataField("fldvalue07", "GUARANTEE_EXPIRY_DATE@" + "");//保证金到期日
					authorizeSubKColl.addDataField("fldvalue08", "ACCT_CODE@" + "");//账户代码
					authorizeSubKColl.addDataField("fldvalue09", "CA_TT_FLAG@" + "");//钞汇标志
					authorizeSubKColl.addDataField("fldvalue10", "ACCT_GL_CODE@" + bail_acct_gl_code);//账号科目代码
					authorizeSubKColl.addDataField("fldvalue11", "ACCT_NAME@" + bail_acct_name);//户名
					authorizeSubKColl.addDataField("fldvalue12", "CCY@" + bail_cur_type);//币种
					authorizeSubKColl.addDataField("fldvalue13", "GUARANTEE_PER@" + security_rate);//保证金比例
					authorizeSubKColl.addDataField("fldvalue14", "GUARANTEE_AMT@" + security_amt);//保证金金额
					authorizeSubKColl.addDataField("fldvalue15", "CONTRACT_NO@" + cont_no);//协议号
					if("510".equals(assure_main)){
						authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "Y");//是否准全额
					}else{
						authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "N");//是否准全额
					}
					
				}
				//产品为：普通贷款时，保证金信息数据与结算账户一同组装
				else{
					authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
					authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
					authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
					authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据号
					authorizeSubKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "GUTR");//贷款账户类型 :GUTR 保证金账号
					authorizeSubKColl.addDataField("fldvalue04", "ACCT_NO@" + bail_acct_no);//账号
					authorizeSubKColl.addDataField("fldvalue05", "CCY@" + ccy);//币种
					authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + interbank_id);//帐号银行代码
					authorizeSubKColl.addDataField("fldvalue07", "BRANCH_ID@" + open_org);//机构代码
					authorizeSubKColl.addDataField("fldvalue08", "ACCT_NAME@" + bail_acct_name);//户名
					authorizeSubKColl.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
					authorizeSubKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "2");//账户类型:1-结算账户 2-保证金				
					authorizeSubKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue17", "ACCT_GL_CODE@" + bail_acct_gl_code);//账号科目代码（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + "");//受托支付金额
					authorizeSubKColl.addDataField("fldvalue20", "BANK_NAME@" + "");//银行名称
					authorizeSubKColl.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
					authorizeSubKColl.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
					
					//开户行地址
					if(interbank_id!=null&&!"".equals(interbank_id)){
						KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, this.getConnection());
						if(kColl!=null){
							authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
						}else{
							authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
						}
					}else{
						authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
					}
					authorizeSubKColl.addDataField("fldvalue24", "GUARANTEE_PER@" + security_rate);//保证金比例
				}
				dao.insert(authorizeSubKColl, this.getConnection());
			}*/
			//还款方式为自由还款时生成授权信息：自由还款信息
			////王小虎注释
			/*if(repay_type.equals("A001")){
				String conditionFree = "where serno='"+loanSerno+"'";
				IndexedCollection iqpFreedomPayInfoIColl = dao.queryList(IQPFREEDOMPAYINFO, conditionFree, this.getConnection());
				for(int i=0;i<iqpFreedomPayInfoIColl.size();i++){				
					KeyedCollection iqpFreedomPayInfoKColl = (KeyedCollection)iqpFreedomPayInfoIColl.get(i);
					String dateno = TagUtil.replaceNull4String(iqpFreedomPayInfoKColl.getDataValue("dateno"));//期号
					String pay_date = TagUtil.replaceNull4String(iqpFreedomPayInfoKColl.getDataValue("pay_date"));//还款日期
					Double suppose_pay_cap = TagUtil.replaceNull4Double(iqpFreedomPayInfoKColl.getDataValue("suppose_pay_cap"));//本金
					Double suppose_pay_int = TagUtil.replaceNull4Double(iqpFreedomPayInfoKColl.getDataValue("suppose_pay_int"));//利息
					KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
					authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
					authorizeSubKColl.addDataField("busi_cls", "01");//业务类别--费用信息(01 还款计划 02 费用  03账户 04保证金)
					authorizeSubKColl.addDataField("fldvalue01", "DUEBILL_NO@" + bill_no);//借据编号
					authorizeSubKColl.addDataField("fldvalue02", "PERIOD_NO@" + dateno);//期号
					authorizeSubKColl.addDataField("fldvalue03", "REPAY_DATE@" + pay_date);//还款日期			
					authorizeSubKColl.addDataField("fldvalue04", "CORPUS@" + suppose_pay_cap);//本金
					authorizeSubKColl.addDataField("fldvalue05", "INTEREST@" + suppose_pay_int);//利息
					dao.insert(authorizeSubKColl, this.getConnection());
				}
			}*/
			
			//生成授权信息：费用信息
			Map<String, String> feemap = new HashMap<String, String>();//定义一个账号对应map
			int feecount = 1;
			if(prd_id.equals("100055")||prd_id.equals("100056")){
				//银团
				/*01	116	27503	N	委托贷款费用
				  02	117	27503	N	递延收益—保函手续费收入
				  03	117	27503	N	银行汇票业务收入
				  04	117	5110101	N	工本费收入
				  05	117	27503	N	待摊销费用
				  06	116	27503	N	印花税
				  07	116	27503	N	手续费
				  08	116	27503	N	递延收益—贷款承诺手续费收入
				  10	116	27503	N	资产买卖安排费支出
				  11	116	27503	N	资产买卖安排费
				  12	116	27503	N	代管理信贷资产手续费支出
				  13	116	27503	N	代管理信贷资产手续费收入
				  14	116	5110101	N	代理贴现费用
				  15	116	5110101	N	代理贴现邮电费用
				  16	116	27503	N	受托支付手续费
				  17	116	5110101	N	受托支付汇划费
				  18	116	27503	N	银团安排费
				  19	116	27503	N	银团承诺费
				  20	116	27503	N	银团代理费*/
				for(int i=0;i<iqpAppendTermsIColl.size();i++){				
					KeyedCollection iqpAppendTermsKColl = (KeyedCollection)iqpAppendTermsIColl.get(i);
					String fee_code = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_code"));//费用代码
					
					if("10".equals(fee_code)){//银团安排费
						fee_code = "18";
					}else if("11".equals(fee_code)){//银团承诺费
						fee_code = "19";
					}else if("12".equals(fee_code)){//银团代理费
						fee_code = "20";
					}
					
					String fee_code_hs = "";
					if(fee_code.equals("09")){//01-借款人委托贷款费用、09-委托人委托贷款费用  都对应 核算系统的  01-委托费用
						fee_code_hs = "01";
					}else{
						fee_code_hs = fee_code;
					}
					String fee_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_cur_type"));//币种
					Double fee_amt = TagUtil.replaceNull4Double(iqpAppendTermsKColl.getDataValue("fee_amt"));//费用金额
					String fee_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_type"));//费用类型
					String fee_rate = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_rate"));//费用比率
					String is_cycle_chrg = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_cycle_chrg"));//是否周期性收费标识 
					String chrg_date = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_date"));//收费日期
					String acct_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_acct_no"));//账号
					String chrg_freq = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_freq"));//账号
					if(chrg_freq.equals("Y")){
						chrg_freq = "12";
					}else if(chrg_freq.equals("Q")){
						chrg_freq = "3";
					}else if(chrg_freq.equals("M")){
						chrg_freq = "1";
					}else{
						chrg_freq = "";
					}
//					String acct_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("acct_name"));//账户名
//					String opac_org_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opac_org_no"));//开户行行号
//					String opan_org_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opan_org_name"));//开户行行名
//					String acct_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("cur_type"));//账户币种
//					String is_this_org_acct = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_this_org_acct"));//是否本行账户
					KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
					authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
					authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
					authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
					authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据号
					authorizeSubKColl.addDataField("fldvalue03", "FEE_CODE@" + fee_code_hs);//费用代码				
					authorizeSubKColl.addDataField("fldvalue04", "CCY@" + fee_cur_type);//币种
					authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
				    authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + TradeConstance.is_pay_flag);//收付标志 默认传 R: 收
					authorizeSubKColl.addDataField("fldvalue07", "FEE_TYPE@" + fee_type);//费用类型
					authorizeSubKColl.addDataField("fldvalue08", "BASE_AMOUNT@" + fee_amt);//基准金额 同费用金额
					authorizeSubKColl.addDataField("fldvalue09", "FEE_RATE@" + fee_rate);//费用比率
					if(fee_code.equals("01")){
						if(feemap.containsKey(acct_no)){
							authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + feemap.get(acct_no));//扣款账户类型  默认传 FEE:收费账户
						}else{
							authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + "FEE");//扣款账户类型  默认传 FEE:收费账户
							feemap.put(acct_no, "FEE");
						}
					}else{
						if(feemap.containsKey(acct_no)){
							authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + feemap.get(acct_no));//扣款账户类型  默认传 FEE:收费账户
						}else{
							authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + "FEE"+feecount);//扣款账户类型  默认传 FEE:收费账户
							feemap.put(acct_no, "FEE"+feecount);
							feecount++;
						}
					}
					authorizeSubKColl.addDataField("fldvalue11", "CHARGE_PERIODICITY_FLAG@" + this.TransSDicForESB("STD_ZX_YES_NO",is_cycle_chrg));//是否周期性收费标识
					authorizeSubKColl.addDataField("fldvalue12", "CHARGE_DATE@" + chrg_date);//收费日期
					authorizeSubKColl.addDataField("fldvalue13", "AMOR_AMT@" + fee_amt);//摊销金额
					authorizeSubKColl.addDataField("fldvalue14", "CONTRACT_NO@" + cont_no);//协议号
//					authorizeSubKColl.addDataField("fldvalue15", "ACCT_NO@" + acct_no);//账号
//					authorizeSubKColl.addDataField("fldvalue16", "ACCT_NAME@" + acct_name);//账户名
//					authorizeSubKColl.addDataField("fldvalue17", "OPAC_ORG_NO@" + opac_org_no);//开户行行号
//					authorizeSubKColl.addDataField("fldvalue18", "OPAN_ORG_NAME@" + opan_org_name);//开户行行名
//					authorizeSubKColl.addDataField("fldvalue19", "ACCT_CCY@" + acct_cur_type);//账户币种
//					authorizeSubKColl.addDataField("fldvalue20", "IS_THIS_ORG_ACCT@" + is_this_org_acct);//是否本行账户
					authorizeSubKColl.addDataField("fldvalue21", "BRANCH_ID@" + in_acct_br_id);//入账机构
					authorizeSubKColl.addDataField("fldvalue22", "FEE_SPAN@" + chrg_freq);//收费间隔
					dao.insert(authorizeSubKColl, this.getConnection());
				}
			}else{
				for(int i=0;i<iqpAppendTermsIColl.size();i++){				
					KeyedCollection iqpAppendTermsKColl = (KeyedCollection)iqpAppendTermsIColl.get(i);
					String fee_code = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_code"));//费用代码
					String fee_code_hs = "";
					if(fee_code.equals("09")){//01-借款人委托贷款费用、09-委托人委托贷款费用  都对应 核算系统的  01-委托费用
						fee_code_hs = "01";
					}else if(fee_code.equals("13")){//保理费用
						fee_code_hs = "21";
					}else{
						fee_code_hs = fee_code;
					}
					String fee_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_cur_type"));//币种
					Double fee_amt = TagUtil.replaceNull4Double(iqpAppendTermsKColl.getDataValue("fee_amt"));//费用金额
					String fee_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_type"));//费用类型
					String fee_rate = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_rate"));//费用比率
					String is_cycle_chrg = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_cycle_chrg"));//是否周期性收费标识 
					String chrg_date = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_date"));//收费日期
					String acct_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_acct_no"));//账号
					String chrg_freq = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_freq"));//账号
					if(chrg_freq.equals("Y")){
						chrg_freq = "12";
					}else if(chrg_freq.equals("Q")){
						chrg_freq = "3";
					}else if(chrg_freq.equals("M")){
						chrg_freq = "1";
					}else{
						chrg_freq = "";
					}
//					String acct_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("acct_name"));//账户名
//					String opac_org_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opac_org_no"));//开户行行号
//					String opan_org_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opan_org_name"));//开户行行名
//					String acct_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("cur_type"));//账户币种
//					String is_this_org_acct = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_this_org_acct"));//是否本行账户
					KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
					authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
					authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
					authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
					authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据号
					authorizeSubKColl.addDataField("fldvalue03", "FEE_CODE@" + fee_code_hs);//费用代码				
					authorizeSubKColl.addDataField("fldvalue04", "CCY@" + fee_cur_type);//币种
					authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
				    authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + TradeConstance.is_pay_flag);//收付标志 默认传 R: 收
					authorizeSubKColl.addDataField("fldvalue07", "FEE_TYPE@" + fee_type);//费用类型
					authorizeSubKColl.addDataField("fldvalue08", "BASE_AMOUNT@" + fee_amt);//基准金额 同费用金额
					authorizeSubKColl.addDataField("fldvalue09", "FEE_RATE@" + fee_rate);//费用比率
					if(fee_code.equals("01")){
						if(feemap.containsKey(acct_no)){
							authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + feemap.get(acct_no));//扣款账户类型  默认传 FEE:收费账户
						}else{
							authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + "FEE");//扣款账户类型  默认传 FEE:收费账户
							feemap.put(acct_no, "FEE");
						}
					}else{
						if(feemap.containsKey(acct_no)){
							authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + feemap.get(acct_no));//扣款账户类型  默认传 FEE:收费账户
						}else{
							authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + "FEE"+feecount);//扣款账户类型  默认传 FEE:收费账户
							feemap.put(acct_no, "FEE"+feecount);
							feecount++;
						}
					}
					authorizeSubKColl.addDataField("fldvalue11", "CHARGE_PERIODICITY_FLAG@" + this.TransSDicForESB("STD_ZX_YES_NO",is_cycle_chrg));//是否周期性收费标识
					authorizeSubKColl.addDataField("fldvalue12", "CHARGE_DATE@" + chrg_date);//收费日期
					authorizeSubKColl.addDataField("fldvalue13", "AMOR_AMT@" + fee_amt);//摊销金额
					authorizeSubKColl.addDataField("fldvalue14", "CONTRACT_NO@" + cont_no);//协议号
//					authorizeSubKColl.addDataField("fldvalue15", "ACCT_NO@" + acct_no);//账号
//					authorizeSubKColl.addDataField("fldvalue16", "ACCT_NAME@" + acct_name);//账户名
//					authorizeSubKColl.addDataField("fldvalue17", "OPAC_ORG_NO@" + opac_org_no);//开户行行号
//					authorizeSubKColl.addDataField("fldvalue18", "OPAN_ORG_NAME@" + opan_org_name);//开户行行名
//					authorizeSubKColl.addDataField("fldvalue19", "ACCT_CCY@" + acct_cur_type);//账户币种
//					authorizeSubKColl.addDataField("fldvalue20", "IS_THIS_ORG_ACCT@" + is_this_org_acct);//是否本行账户
					authorizeSubKColl.addDataField("fldvalue21", "BRANCH_ID@" + in_acct_br_id);//入账机构
					authorizeSubKColl.addDataField("fldvalue22", "FEE_SPAN@" + chrg_freq);//收费间隔
					dao.insert(authorizeSubKColl, this.getConnection());
				}
			}
			
			
			//生成授权信息：结算账户信息
			String conditionCusAcct = "where serno='"+loanSerno+"'";
			IndexedCollection iqpCusAcctIColl = dao.queryList(IQPCUSACCT, conditionCusAcct, this.getConnection());
			int eactcount = 0;
			for(int i=0;i<iqpCusAcctIColl.size();i++){				
				KeyedCollection iqpCusAcctKColl = (KeyedCollection)iqpCusAcctIColl.get(i);
				String acct_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_no"));//账号
				String opac_org_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opac_org_no"));//开户行行号
				String opan_org_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opan_org_name"));//开户行行名
				String is_this_org_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行账户
				String acct_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_name"));//户名
				Double pay_amt = TagUtil.replaceNull4Double(iqpCusAcctKColl.getDataValue("pay_amt"));//受托支付金额
				String acct_gl_code = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_gl_code"));//科目号
				String ccy = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("cur_type"));//币种
				String is_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行
				/*01放款账号  02印花税账号 03收息收款账号    04受托支付账号     05	主办行资金归集账户*/
				String acct_attr = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_attr"));//账户属性
				String interbank_id = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("interbank_id"));//银联行号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据号
				
				/*PAYM:还款账户 ACTV:放款账号 TURE:委托账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号*/
				if(!"".equals(acct_attr) && acct_attr.equals("01")){
					acct_attr = "ACTV";
				}else if(!"".equals(acct_attr) && acct_attr.equals("02")){
					acct_attr = "STAMP1";
				}else if(!"".equals(acct_attr) && acct_attr.equals("03")){
					acct_attr = "PAYM";
				}else if(!"".equals(acct_attr) && acct_attr.equals("04")){
					//自主支付不要传受托支付账号  add by zhaozq 20150427 start
					if("1".equals(pay_type)){
						continue;
					}
					//自主支付不要传受托支付账号  add by zhaozq 20150427 end
					if(eactcount==0){
						acct_attr = "EACT";
					}else{
						acct_attr = "EACT"+eactcount;
					}
					eactcount++;
				}else if(!"".equals(acct_attr) && acct_attr.equals("05")){
					acct_attr = "TURE";
				}else if(!"".equals(acct_attr) && acct_attr.equals("06")){
					acct_attr = "STAMP2";
				}else if(!"".equals(acct_attr) && acct_attr.equals("07")){
					if(feemap.containsKey(acct_no)){
						acct_attr = feemap.get(acct_no);
					}else{
						continue;//找不到对应费用信息，该费用账号不传
					}
				}else{
					acct_attr = "";
				}
				authorizeSubKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + acct_attr);//贷款账户类型 				
				authorizeSubKColl.addDataField("fldvalue04", "ACCT_NO@" + acct_no);//账号
				authorizeSubKColl.addDataField("fldvalue05", "CCY@" + ccy);//币种
//				if(is_this_org_acct.equals("1")){//本行账户
//					authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
//				}else{
//					authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + opac_org_no);//帐号银行代码
//				}
				authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + interbank_id);//帐号银行代码
				authorizeSubKColl.addDataField("fldvalue07", "BRANCH_ID@" + opac_org_no);//机构代码
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_NAME@" + acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "1");//账户类型:1-结算账户 2-保证金				
				authorizeSubKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue17", "ACCT_GL_CODE@" + acct_gl_code);//账号科目代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + pay_amt);//受托支付金额
				authorizeSubKColl.addDataField("fldvalue20", "BANK_NAME@" + opan_org_name);//银行名称
				authorizeSubKColl.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
				authorizeSubKColl.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + is_acct);//是否本行
				
				//开户行地址
				if(interbank_id!=null&&!"".equals(interbank_id)){
					KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, this.getConnection());
					if(kColl!=null){
						authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
					}else{
						authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
					}
				}else{
					authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
				}
				
				dao.insert(authorizeSubKColl, this.getConnection());
			}
			
			/* 过渡户不通过信贷传   2014-04-28
			//无间贷需要系统生成放款账号信息，为过渡户
			String is_close_loan = (String) ctrContSubKColl.getDataValue("is_close_loan");
			if(is_close_loan.equals("1")){
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据号
				authorizeSubKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "ACTV");//贷款账户类型 				
				authorizeSubKColl.addDataField("fldvalue04", "ACCT_NO@" + TradeConstance.BANK_ACCT_NO);//账号
				authorizeSubKColl.addDataField("fldvalue05", "CCY@" + "CNY");//币种
				authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
				authorizeSubKColl.addDataField("fldvalue07", "BRANCH_ID@" + "");//机构代码
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_NAME@" + "过渡户");//户名
				authorizeSubKColl.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "");//账户类型:1-结算账户 2-保证金				
				authorizeSubKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue17", "ACCT_GL_CODE@" + "");//账号科目代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + "");//受托支付金额
				authorizeSubKColl.addDataField("fldvalue20", "BANK_NAME@" + "");//银行名称
				authorizeSubKColl.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
				authorizeSubKColl.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "");//是否本行
				
				//开户行地址
				authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
				
				dao.insert(authorizeSubKColl, this.getConnection());
			}*/
			
			//保理本息分开扣，需增加扣本金账号
			////王小虎注释
			/*if(prd_id.equals("800021")){
				iqpInterFact = dao.queryDetail("IqpInterFact", loanSerno, this.getConnection());
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据号
				PAYM:还款账户 ACTV:放款账号 TURE:委托账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号
				authorizeSubKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "PAYMP");//贷款账户类型 				
				authorizeSubKColl.addDataField("fldvalue04", "ACCT_NO@" + iqpInterFact.getDataValue("agent_acct_no"));//账号
				authorizeSubKColl.addDataField("fldvalue05", "CCY@" + cont_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
				authorizeSubKColl.addDataField("fldvalue07", "BRANCH_ID@" + "");//机构代码
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_NAME@" + "待处理保理业务款项");//户名
				authorizeSubKColl.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "1");//账户类型:1-结算账户 2-保证金				
				authorizeSubKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue16", "ACCT_CODE@" + "1");//账户代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue17", "ACCT_GL_CODE@" + "31511");//账号科目代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + "");//受托支付金额
				authorizeSubKColl.addDataField("fldvalue20", "BANK_NAME@" + "");//银行名称
				authorizeSubKColl.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
				authorizeSubKColl.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
				//开户行地址
				authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
				dao.insert(authorizeSubKColl, this.getConnection());
				
				//如果是预收息，需要录入收息账号
				if("1".equals(iqpInterFact.getDataValue("is_pay_int"))){
					KeyedCollection intAcctKCollAcc = new KeyedCollection(AUTHORIZESUBMODEL);
					intAcctKCollAcc.addDataField("auth_no", authSerno);
					intAcctKCollAcc.addDataField("busi_cls", "03");//账号信息
					intAcctKCollAcc.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编号
					intAcctKCollAcc.addDataField("fldvalue02", "DUEBILL_NO@"+bill_no);//借据号（无意义，暂定不传）
					intAcctKCollAcc.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "PAYMI");//保理预收息账号
					intAcctKCollAcc.addDataField("fldvalue04", "ACCT_NO@"+iqpInterFact.getDataValue("int_acct_no"));//贴现人结算账户
					intAcctKCollAcc.addDataField("fldvalue05", "CCY@"+cont_cur_type);//账户币种
					intAcctKCollAcc.addDataField("fldvalue06", "BANK_ID@"+TradeConstance.BANK_ID);//帐号银行代码
					intAcctKCollAcc.addDataField("fldvalue07", "BRANCH_ID@"+"");//帐号机构代码
					intAcctKCollAcc.addDataField("fldvalue08", "ACCT_NAME@"+"递延保理利息收入");//户名
					intAcctKCollAcc.addDataField("fldvalue09", "ISS_CTRY@"+"CN");//发证国家
					intAcctKCollAcc.addDataField("fldvalue10", "ACCT_TYPE@" + "1");//账户类型
					intAcctKCollAcc.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（非必输）
					intAcctKCollAcc.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（非必输）
					intAcctKCollAcc.addDataField("fldvalue13", "REMARK@" + "");//备注
					intAcctKCollAcc.addDataField("fldvalue14", "CARD_NO@"+"");//卡号
					intAcctKCollAcc.addDataField("fldvalue15", "CA_TT_FLAG@"+"");//钞汇标志
					intAcctKCollAcc.addDataField("fldvalue16", "ACCT_CODE@"+"1");//账户代码
					intAcctKCollAcc.addDataField("fldvalue17", "ACCT_GL_CODE@"+"27505");//账号科目代码
					intAcctKCollAcc.addDataField("fldvalue18", "BALANCE@"+"");//账户余额
					intAcctKCollAcc.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@"+"");//此交易字段存储为付息金额
					intAcctKCollAcc.addDataField("fldvalue20", "BANK_NAME@"+"");//开户行行名
					intAcctKCollAcc.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
					intAcctKCollAcc.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
					//开户行地址
					intAcctKCollAcc.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
					dao.insert(intAcctKCollAcc, this.getConnection());
				}
			}*/
			
			//生成授权信息：结算账户信息（委托贷款时生成委托账号授权信息）
			//个人委托贷款与企业委托贷款
			if(prd_id.equals("100063") || prd_id.equals("100065")){
				String acct_no = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_inner_dep_no"));//委托人内部存款账号
				String acct_name = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_inner_dep_name"));//委托人内部存款户名
				String csgn_acct_no = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_acct_no"));//委托人一般账号
				String csgn_acct_name = TagUtil.replaceNull4String(IqpCsgnLoanKColl.getDataValue("csgn_acct_name"));//委托人一般账号户名
				//委托人内部存款账号
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据号
				/*PAYM:还款账户 ACTV:放款账号 TURE:委托账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号*/
				authorizeSubKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "TURE");//贷款账户类型 				
				authorizeSubKColl.addDataField("fldvalue04", "ACCT_NO@" + acct_no);//账号
				authorizeSubKColl.addDataField("fldvalue05", "CCY@" + cont_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
				authorizeSubKColl.addDataField("fldvalue07", "BRANCH_ID@" + "");//机构代码
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_NAME@" + acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "1");//账户类型:1-结算账户 2-保证金				
				authorizeSubKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue17", "ACCT_GL_CODE@" + "");//账号科目代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + "");//受托支付金额
				authorizeSubKColl.addDataField("fldvalue20", "BANK_NAME@" + "");//银行名称
				authorizeSubKColl.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
				authorizeSubKColl.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
				
				//开户行地址
				authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
				
				dao.insert(authorizeSubKColl, this.getConnection());
				
				
				//委托人一般账号
				KeyedCollection authorizeSubKCollCsgn = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKCollCsgn.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKCollCsgn.addDataField("busi_cls", "03");//业务类别
				authorizeSubKCollCsgn.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKCollCsgn.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据号
				/*PAYM:还款账户 ACTV:放款账号 TURE:委托人内部存款账号TUREA:委托人一般账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号*/
				authorizeSubKCollCsgn.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "TUREA");//贷款账户类型 				
				authorizeSubKCollCsgn.addDataField("fldvalue04", "ACCT_NO@" + csgn_acct_no);//账号
				authorizeSubKCollCsgn.addDataField("fldvalue05", "CCY@" + cont_cur_type);//币种
				authorizeSubKCollCsgn.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
				authorizeSubKCollCsgn.addDataField("fldvalue07", "BRANCH_ID@" + "");//机构代码
				authorizeSubKCollCsgn.addDataField("fldvalue08", "ACCT_NAME@" + csgn_acct_name);//户名
				authorizeSubKCollCsgn.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
				authorizeSubKCollCsgn.addDataField("fldvalue10", "ACCT_TYPE@" + "1");//账户类型:1-结算账户 2-保证金				
				authorizeSubKCollCsgn.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue17", "ACCT_GL_CODE@" + "");//账号科目代码（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + "");//受托支付金额
				authorizeSubKCollCsgn.addDataField("fldvalue20", "BANK_NAME@" + "");//银行名称
				authorizeSubKCollCsgn.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
				authorizeSubKCollCsgn.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
				
				//开户行地址
				authorizeSubKCollCsgn.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
				
				dao.insert(authorizeSubKCollCsgn, this.getConnection());
			}
			
			//生成台账信息
			KeyedCollection accLoanKColl = new KeyedCollection(ACCLOANMODEL);
			accLoanKColl.addDataField("serno", Pvpserno);//业务编号	
			accLoanKColl.addDataField("acc_day", date);//日期
			accLoanKColl.addDataField("acc_year", date.substring(0, 4));//年份
			accLoanKColl.addDataField("acc_mon", date.substring(5, 7));//月份
			accLoanKColl.addDataField("prd_id", prd_id);//产品编号
			accLoanKColl.addDataField("cus_id", cus_id);//客户编码
			accLoanKColl.addDataField("cont_no", cont_no);//合同编号
			accLoanKColl.addDataField("bill_no", bill_no);//借据编号
			accLoanKColl.addDataField("loan_amt", pvp_amt);//贷款金额
			accLoanKColl.addDataField("loan_balance", pvp_amt);//贷款余额
			accLoanKColl.addDataField("distr_date", date);//发放日期
			accLoanKColl.addDataField("end_date", end_date);//到期日期
			accLoanKColl.addDataField("ori_end_date", end_date);//原到期日期
			accLoanKColl.addDataField("post_count", "0");//展期次数
			accLoanKColl.addDataField("overdue", "0");//逾期期数
			accLoanKColl.addDataField("separate_date", "");//清分日期
			accLoanKColl.addDataField("ruling_ir", ruling_ir);//基准利率
			accLoanKColl.addDataField("reality_ir_y", reality_ir_y);//执行年利率
			accLoanKColl.addDataField("overdue_rate_y", overdue_rate_y_iqp);//逾期利率
			accLoanKColl.addDataField("default_rate_y", default_rate_y);//违约利率
			accLoanKColl.addDataField("comp_int_balance", "0");//复利余额
			accLoanKColl.addDataField("inner_owe_int", "0");//表内欠息
			accLoanKColl.addDataField("out_owe_int", "0");//表外欠息
			accLoanKColl.addDataField("rec_int_accum", "0");//应收利息累计
			accLoanKColl.addDataField("recv_int_accum", "0");//实收利息累计
			accLoanKColl.addDataField("normal_balance", pvp_amt);//正常余额
			accLoanKColl.addDataField("overdue_balance", "0");//逾期余额
			accLoanKColl.addDataField("slack_balance", "0");//呆滞余额
			accLoanKColl.addDataField("bad_dbt_balance", "0");//呆账余额
			accLoanKColl.addDataField("writeoff_date", "");//核销日期
			accLoanKColl.addDataField("paydate", "");//转垫款日
			accLoanKColl.addDataField("five_class", five_classfiy);//五级分类
			accLoanKColl.addDataField("twelve_cls_flg", "");//十二级分类
			accLoanKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			accLoanKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
			accLoanKColl.addDataField("acc_status", "0");//台帐状态
			accLoanKColl.addDataField("cur_type", cur_type);//币种
			dao.insert(accLoanKColl, this.getConnection());
			
			//是否委托委托
			////王小虎注释
			/*if("1".equals(is_trust_loan)){
				String serno4AccTranTrustCompany = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", this.getConnection(), this.getContext());
				KeyedCollection accTranTrustCompanyKColl = new KeyedCollection(AccTranTrustCompany);
				accTranTrustCompanyKColl.addDataField("serno", serno4AccTranTrustCompany);//交易明细编号
				accTranTrustCompanyKColl.addDataField("bill_no", bill_no);//借据号
				accTranTrustCompanyKColl.addDataField("cont_no", cont_no);//合同号
				accTranTrustCompanyKColl.addDataField("list_type", "0");//明细类型 (发放)
				accTranTrustCompanyKColl.addDataField("cur_type", cur_type);//币种
				accTranTrustCompanyKColl.addDataField("tran_amt", pvp_amt);//交易金额
				accTranTrustCompanyKColl.addDataField("reclaim_mode", "");//回收方式
				accTranTrustCompanyKColl.addDataField("tran_date", date);//交易日期
				accTranTrustCompanyKColl.addDataField("input_id", input_id);//登记人
				accTranTrustCompanyKColl.addDataField("input_br_id", input_br_id);//登记机构
				accTranTrustCompanyKColl.addDataField("input_date", date);//登记日期
				dao.insert(accTranTrustCompanyKColl,  this.getConnection());
			}*/
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl4trade = new KeyedCollection();
//			kColl4trade.put("CLIENT_NO", cus_id);
//			kColl4trade.put("BUSS_SEQ_NO", loanSerno);
//			kColl4trade.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), this.getConnection());	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常！");
		}
	}
	
	/**
	 * 银承贷款出账流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpAccp(String serno)throws ComponentException {
		Connection connnection = null;
		try {
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			connnection = this.getConnection();
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			//数据准备：通过业务流水号查询【出账申请】
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, connnection);
			String prd_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("prd_id"));//产品编号
			String cus_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cus_id"));//客户码
			String cont_no = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cont_no"));//合同编号
			String manager_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("manager_br_id"));//管理机构
			String in_acct_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("in_acct_br_id"));//入账机构
			
			/** 核算与信贷业务品种映射 START */
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			String lmPrdId = service.getPrdBasicCLPM2LM(cont_no, prd_id, this.getContext(), this.getConnection());
			/** 核算与信贷业务品种映射 END */
			
			//数据准备：通过业务流水号查询【合同信息】
			KeyedCollection ctrContKColl =  dao.queryDetail(CTRCONTMODEL, cont_no, connnection);
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, cont_no, connnection);
			Double pad_rate_y = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("pad_rate_y"));//垫款利率（年）
			String five_classfiy = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("five_classfiy"));//五级分类
			String iqpserno = TagUtil.replaceNull4String(ctrContKColl.getDataValue("serno"));//业务流水号
			Double exchange_rate = TagUtil.replaceNull4Double(ctrContKColl.getDataValue("exchange_rate"));//汇率
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 start
			BigDecimal security_rate = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("security_rate"));//保证金比例
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 end			
			Double security_amt = TagUtil.replaceNull4Double(ctrContKColl.getDataValue("security_amt"));//保证金金额
			String cur_type = TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_cur_type"));//币种
			Integer cont_term = TagUtil.replaceNull4Int(ctrContSubKColl.getDataValue("cont_term"));//合同期限
			String term_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("term_type"));//期限类型
			String assure_main = TagUtil.replaceNull4String(ctrContKColl.getDataValue("assure_main"));//担保方式
			//数据准备：通过客户编号查询【客户信息】 
			String cus_name = "";//客户名称
			String cert_type = "";//证件类型
			String cert_code = "";//证件号码
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			try {
				CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
				CusBase cusBase = csi.getCusBaseByCusId(cus_id,this.getContext(),connnection);
				cus_name = cusBase.getCusName();
				cert_type = cusBase.getCertType();
				cert_code = cusBase.getCertCode();
			} catch (Exception e) {
				e.printStackTrace();
				throw new EMPException("获取客户信息失败！");
			}
			
			//数据准备：通过业务流水号查询【银票明细基本信息】
			KeyedCollection kCollIqpAccp = dao.queryDetail("IqpAccAccp", iqpserno, connnection);
			String is_elec_bill = TagUtil.replaceNull4String(kCollIqpAccp.getDataValue("is_elec_bill"));//是否电子票据
	   	    String aorg_type = TagUtil.replaceNull4String(kCollIqpAccp.getDataValue("acpt_org_type"));//承兑行类型
	   	    String aorg_no = TagUtil.replaceNull4String(kCollIqpAccp.getDataValue("actp_org_no"));//承兑行行号
	   	    String aorg_name =TagUtil.replaceNull4String(kCollIqpAccp.getDataValue("actp_org_name"));//承兑行行名
	   	    String opac_type = TagUtil.replaceNull4String(kCollIqpAccp.getDataValue("opac_type"));//签发类别
	   	    //核算类型为签发类别  0－自签 1－委签
	   	    if(opac_type.equals("01")){//自签
	   	    	opac_type = "0";
	   	    }else{
	   	    	opac_type = "1";
	   	    }
	   	    String opac_org = TagUtil.replaceNull4String(kCollIqpAccp.getDataValue("opac_org"));//代签行行名
	   	    String opac_org_name = TagUtil.replaceNull4String(kCollIqpAccp.getDataValue("opac_org_name"));//代签行行号
	   	    //数据准备：获取系统当前日期相关信息
			String date = this.getContext().getDataValue(PUBConstant.OPENDAY).toString();//营业日期
			String year = date.substring(0,4);//年
			String month = date.substring(5,7);//月
			//数据准备：通过业务流水号查询【银票明细列表信息】
			String condition = " where serno = '" + iqpserno + "'";
			IndexedCollection detIColl = dao.queryList("IqpAccpDetail", condition, connnection);
			//生成授权信息+台账信息
			//生成授权编号，所有票据授权交易流水在一个授权编号下
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connnection, this.getContext());
			
			/**费用信息**/
			String conditionFee = "where serno='"+iqpserno+"'";
			IndexedCollection iqpAppendTermsIColl = dao.queryList(IQPFEE, conditionFee, this.getConnection());
			//计算手续费率  start
			BigDecimal chrg_rate = new BigDecimal("0.00");
			for(int fee_i=0;fee_i<iqpAppendTermsIColl.size();fee_i++){
				KeyedCollection feekc = (KeyedCollection) iqpAppendTermsIColl.get(fee_i);
				String fee_rate_str = TagUtil.replaceNull4String(feekc.getDataValue("fee_rate"));
				if(fee_rate_str==null||fee_rate_str.equals("")){
					fee_rate_str = "0";
				}
				BigDecimal fee_rate = new BigDecimal(fee_rate_str);
				chrg_rate = chrg_rate.add(fee_rate);
			}
			//计算手续费率  end
			
			Map<String, String> acctmap = new HashMap<String, String>();//定义一个账号对应map
			for(int i=0;i<detIColl.size();i++){
				KeyedCollection detKColl = (KeyedCollection) detIColl.get(i);
				/** 获取借据编号*/
				PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
                .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), connnection);
				String billNo = cmisComponent.getBillNoByContNo(cont_no);
				/** 生成交易流水号 */
				String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connnection, this.getContext());
				KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
				/**数据准备：银票明细**/
				BigDecimal drft_amt = new BigDecimal(TagUtil.replaceNull4String(detKColl.getDataValue("drft_amt")));//票面金额
				String paorg_no = TagUtil.replaceNull4String(detKColl.getDataValue("paorg_no"));//收款人开户行行号
	   	    	String paorg_name = TagUtil.replaceNull4String(detKColl.getDataValue("paorg_name"));//收款人开户行行名
	   	    	String paorg_acct_no = TagUtil.replaceNull4String(detKColl.getDataValue("clt_acct_no"));//收款人账号
	   	    	String paorg_person = TagUtil.replaceNull4String(detKColl.getDataValue("clt_person"));//收款人名称
	   	    	String bill_isse_date = "";//票据出票日期
	   	    	String bill_expiry_date = "";//票据到期日期
	   	    	String bill_term_type = TagUtil.replaceNull4String(detKColl.getDataValue("term_type"));//期限类型
	   	    	Integer bill_term = TagUtil.replaceNull4Int(detKColl.getDataValue("term"));//票据期限
	   	    	//电票
	   	    	if("1".equals(is_elec_bill)){
	   	    		bill_isse_date = TagUtil.replaceNull4String(detKColl.getDataValue("bill_isse_date"));
		   	    	bill_expiry_date = TagUtil.replaceNull4String(detKColl.getDataValue("bill_expiry_date"));
	   	    	}
	   	    	//纸票
	   	    	else if("2".equals(is_elec_bill)){
	   	    		bill_isse_date = date;
	   				bill_expiry_date = DateUtils.getEndDate(bill_term_type, bill_isse_date, bill_term);
	   	    	}
	   	    	else{
	   	    		throw new EMPException("获取【是否电子票据】的值【" + is_elec_bill + "】无法识别！");
	   	    	}
	   	    	
	   	    	//计算手续费  start
	   	    	BigDecimal commission = new BigDecimal("0.00");
	   	    	for(int fee_i=0;fee_i<iqpAppendTermsIColl.size();fee_i++){
					KeyedCollection feekc = (KeyedCollection) iqpAppendTermsIColl.get(fee_i);
					String fee_rate_str = TagUtil.replaceNull4String(feekc.getDataValue("fee_rate"));
					if(fee_rate_str==null||fee_rate_str.equals("")){
						fee_rate_str = "0";
					}
					BigDecimal fee_rate = new BigDecimal(fee_rate_str);
					
					//手续费不使用手续费率计算，固定金额直接加，防止精度丢失
					String collect_type = TagUtil.replaceNull4String(feekc.getDataValue("collect_type"));//01-按固定金额，02-按比率
					BigDecimal fee_amt = BigDecimalUtil.replaceNull(feekc.getDataValue("fee_amt"));
					if("02".equals(collect_type)){
						commission = commission.add(drft_amt.multiply(fee_rate));
					}else{
						commission = commission.add(fee_amt);
					}
				}
	   	    	//计算手续费  end
	   	    	
				/** 给授权信息主表赋值 */
				authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
				authorizeKColl.addDataField("serno", serno);//业务流水号（出账编号）
				authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
				authorizeKColl.addDataField("prd_id", prd_id);//产品编号
				authorizeKColl.addDataField("cus_id", cus_id);//客户码
				authorizeKColl.addDataField("cus_name", cus_name);//客户名称
				authorizeKColl.addDataField("cont_no", cont_no);//合同编号
				authorizeKColl.addDataField("bill_no", billNo);//借据编号
				authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_YCFFSQ + TradeConstance.SERVICE_SCENE_YCFFSQ);//交易码+场景
				authorizeKColl.addDataField("tran_amt", drft_amt);//交易金额
				authorizeKColl.addDataField("tran_date", date);//交易日期:取当前营业日期
				authorizeKColl.addDataField("send_times", "0");//发送次数
				authorizeKColl.addDataField("return_code", "");//返回编码
				authorizeKColl.addDataField("return_desc", "");//返回信息
				authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
				authorizeKColl.addDataField("status", "00");//状态
				authorizeKColl.addDataField("cur_type",cur_type );//币种
				
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编码
				authorizeKColl.addDataField("fldvalue02", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue03", "BRANCH_ID@" + in_acct_br_id);//出账机构
				authorizeKColl.addDataField("fldvalue04", "LOAN_TYPE@" + lmPrdId);//贷款品种
				authorizeKColl.addDataField("fldvalue05", "DUEBILL_NO@" + billNo);//借据号
				authorizeKColl.addDataField("fldvalue06", "ACCEPT_AGREE_NO@" + cont_no);//承兑协议号（即合同号）
				authorizeKColl.addDataField("fldvalue07", "CCY@" + cur_type);//币种
				authorizeKColl.addDataField("fldvalue08", "FACE_AMT@" + drft_amt);//票面金额
				authorizeKColl.addDataField("fldvalue09", "COMMISSION@" + commission);//票面金额*手续费比例
				authorizeKColl.addDataField("fldvalue10", "GLOBAL_TYPE@" + cert_type);//出票人证件类型（非必输）
				authorizeKColl.addDataField("fldvalue11", "GLOBAL_ID@" + cert_code);//出票人证件号码（非必输）
				authorizeKColl.addDataField("fldvalue12", "ISS_CTRY@" + "CN");//出票人发证国家
				authorizeKColl.addDataField("fldvalue13", "CLIENT_NO@" + cus_id);//客户编号
				//电票
	   	    	if("1".equals(is_elec_bill)){
	   	    		authorizeKColl.addDataField("fldvalue14", "BILL_NO@" + TagUtil.replaceNull4String(detKColl.getDataValue("porder_no")));//汇票号码
	   	    	}
	   	    	//纸票
	   	    	else if("2".equals(is_elec_bill)){
	   	    		authorizeKColl.addDataField("fldvalue14", "BILL_NO@" + "");//汇票号码:授权时未生成，放空
	   	    	}
				authorizeKColl.addDataField("fldvalue15", "DRAWER_NAME@" + cus_name);//出票人姓名
				authorizeKColl.addDataField("fldvalue16", "OD_INT_RATE@" + pad_rate_y);//垫款利率（年）
				authorizeKColl.addDataField("fldvalue17", "ACCP_ISSUE_DT@" + TagUtil.formatDate(bill_isse_date));//出票日期
				authorizeKColl.addDataField("fldvalue18", "ACCP_DUE_DT@" + TagUtil.formatDate(bill_expiry_date));//到期日期
				authorizeKColl.addDataField("fldvalue19", "OPAC_TYPE@" + opac_type);//签发类型
				authorizeKColl.addDataField("fldvalue20", "OPAC_ORG@" + opac_org);//代签行行名
				authorizeKColl.addDataField("fldvalue21", "OPAC_ORG_NAME@" + opac_org_name);//代签行行号
				authorizeKColl.addDataField("fldvalue22", "PAORG_PERSON@" + paorg_person);//收款人名称
				authorizeKColl.addDataField("fldvalue23", "PAORG_ACCT_NO@" + paorg_acct_no);//收款人账号
				authorizeKColl.addDataField("fldvalue24", "PAORG_NO@" + paorg_no);//收款人开户行行号
				authorizeKColl.addDataField("fldvalue25", "PAORG_NAME@" + paorg_name);//收款人开户行行名
				authorizeKColl.addDataField("fldvalue26", "E_CDE@" + is_elec_bill);//是否电票
				dao.insert(authorizeKColl, connnection);
				
				/** 给银承台帐信息表赋值 */
				KeyedCollection AcckColl = new KeyedCollection();
	   	    	AcckColl.setId("AccAccp");	   	    	
	   	    	AcckColl.addDataField("serno",serno);//出账流水号
	       	    AcckColl.addDataField("prd_id",prd_id);//产品编号
	       	    AcckColl.addDataField("cont_no",cont_no);//合同编号
	       	    AcckColl.addDataField("bill_no",billNo);//借扰编号
	       	    AcckColl.addDataField("bill_type","100");//票据类型
	       	    //电票
	   	    	if("1".equals(is_elec_bill)){
	   	    		AcckColl.addDataField("porder_no",TagUtil.replaceNull4String(detKColl.getDataValue("porder_no")));//汇票号码
	   	    	}
	   	    	//纸票
	   	    	else if("2".equals(is_elec_bill)){
	   	    		AcckColl.addDataField("porder_no","");//汇票号码
	   	    	}
	       	    AcckColl.addDataField("utakeover_sign","");//不得转让标记
	       	    AcckColl.addDataField("is_ebill",is_elec_bill);//是否电子票据
	       	    AcckColl.addDataField("daorg_cusid",cus_id);//出票人客户码
	       	    AcckColl.addDataField("daorg_cus_name",cus_name);//出票人名称 
	       	    AcckColl.addDataField("drwr_org_code","");//出票人组织机构代码:放空
	       	    
	       	    //出票人账号信息取收息收款账号信息
	       	    KeyedCollection daorgkcoll = dao.queryFirst("IqpCusAcct", null, " where serno = '"+iqpserno+"' and acct_attr = '03' ", this.getConnection());
	       	    if(daorgkcoll!=null){
	       	    	AcckColl.addDataField("daorg_no",TagUtil.replaceNull4String(daorgkcoll.getDataValue("opac_org_no")));
		       	    AcckColl.addDataField("daorg_name",TagUtil.replaceNull4String(daorgkcoll.getDataValue("opan_org_name")));
		       	    AcckColl.addDataField("daorg_acct",TagUtil.replaceNull4String(daorgkcoll.getDataValue("acct_no")));
	       	    }else{
	       	    	AcckColl.addDataField("daorg_no","");//出票人开户行行号:放空
		       	    AcckColl.addDataField("daorg_name","");//出票人开户行行名:放空
		       	    AcckColl.addDataField("daorg_acct","");//出票人开户行账号:放空
	       	    }
	       	    AcckColl.addDataField("aorg_type",aorg_type);//承兑行类型
	       	    AcckColl.addDataField("aorg_no",aorg_no);//承兑行行号
	       	    AcckColl.addDataField("aorg_name",aorg_name);//承兑行名称
	       	    AcckColl.addDataField("pyee_name",paorg_person);//收款人名称
	       	    AcckColl.addDataField("paorg_no",paorg_no);//收款人开户行行号
	       	    AcckColl.addDataField("paorg_name",paorg_name);//收款人开户行行名
	       	    AcckColl.addDataField("paorg_acct_no",paorg_acct_no);//收款人账号
	       	    AcckColl.addDataField("exchange_rate",exchange_rate);//汇率
	       	    AcckColl.addDataField("cur_type",cur_type);//币种
	       	    AcckColl.addDataField("drft_amt",drft_amt);//票面金额
	       	    AcckColl.addDataField("bill_isse_date",date);//签发日期
		   	    AcckColl.addDataField("isse_date",bill_isse_date);//出票日期：电票从票据明细中获取
		       	AcckColl.addDataField("porder_end_date",bill_expiry_date);//到期日期：电票从票据明细中获取
	       	    AcckColl.addDataField("pad_rate",pad_rate_y);//垫款利率
	       	    AcckColl.addDataField("pad_amt",0);//垫款金额：初始为0
	       	    AcckColl.addDataField("paydate","");//转垫款日：默认为空
	       	    AcckColl.addDataField("separate_date","");//清分日期：默认为空
	       	    AcckColl.addDataField("writeoff_date","");//核销日期：默认为空
	       	    AcckColl.addDataField("five_class",five_classfiy);
	       	    AcckColl.addDataField("twelve_cls_flg","");//十二级分类标志
	       	    AcckColl.addDataField("acc_day",date);
	       	    AcckColl.addDataField("acc_year",year);
				AcckColl.addDataField("acc_mon",month);
	       	    AcckColl.addDataField("manager_br_id",manager_br_id);
	       	    AcckColl.addDataField("fina_br_id",in_acct_br_id);
	       	    AcckColl.addDataField("accp_status","0");
	       	    
	       	    dao.insert(AcckColl, connnection);
	       	    
	       	    /**10.生成授信信息：结算账户信息                      modify 按借据生成多条 **/
				String acctCondition = " where serno = '"+iqpserno+"'";
				IndexedCollection acctIColl = dao.queryList("IqpCusAcct", acctCondition, connnection);
				int feecount = 0;
				int eactcount = 0;
				for(int k=0;k<acctIColl.size();k++){
					KeyedCollection iqpCusAcctKColl = (KeyedCollection)acctIColl.get(k);
					String acct_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_no"));//账号
					String acct_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_name"));//户名
					String opac_org_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opac_org_no"));//开户行行号
					String opan_org_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opan_org_name"));//开户行行名
					String is_this_org_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行账户
					String acct_gl_code = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_gl_code"));//科目号
					String ccy = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("cur_type"));//币种
					String is_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行
					/*01放款账号  02印花税账号 03收息收款账号    04受托支付账号     05	主办行资金归集账户*/
					String acct_attr = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_attr"));//账户属性
					String interbank_id = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("interbank_id"));//银联行号
					
					KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
					/** 给授权从表赋值 */
					authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
					authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
					authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
					authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + billNo);//借据号：不需赋值
					
					
					/*PAYM:还款账户 ACTV:放款账号 TURE:委托账号 FEE:收费账户 EACT:最后付款账号 GUTR:保证金账号*/
					if(!"".equals(acct_attr) && acct_attr.equals("01")){
						acct_attr = "ACTV";
					}else if(!"".equals(acct_attr) && acct_attr.equals("02")){
						acct_attr = "STAMP1";
					}else if(!"".equals(acct_attr) && acct_attr.equals("03")){
						acct_attr = "PAYM";
					}else if(!"".equals(acct_attr) && acct_attr.equals("04")){
						if(eactcount==0){
							acct_attr = "EACT";
						}else{
							acct_attr = "EACT"+eactcount;
						}
						eactcount++;
					}else if(!"".equals(acct_attr) && acct_attr.equals("06")){
						acct_attr = "STAMP2";
					}else if(!"".equals(acct_attr) && acct_attr.equals("07")){
						if(feecount==0){
							acct_attr = "FEE";
						}else{
							acct_attr = "FEE"+feecount;
						}
						feecount++;
						if(!acctmap.containsKey(acct_no)){
							acctmap.put(acct_no, acct_attr);
						}
					}else{
						acct_attr = "";
					}
					
					authorizeSubKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + acct_attr);//贷款账户类型 字典项
					authorizeSubKColl.addDataField("fldvalue04", "ACCT_NO@" + acct_no);//账号
					authorizeSubKColl.addDataField("fldvalue05", "CCY@" + ccy);//币种：不需赋值
//					if(is_this_org_acct.equals("1")){//本行账户
//						authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
//					}else{
//						authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + opac_org_no);//帐号银行代码
//					}
					authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + interbank_id);//帐号银行代码
					authorizeSubKColl.addDataField("fldvalue07", "BRANCH_ID@" + opac_org_no);//机构代码
					authorizeSubKColl.addDataField("fldvalue08", "ACCT_NAME@" + acct_name);//户名
					authorizeSubKColl.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
					authorizeSubKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "1");//账户类型 字典项：不需赋值		
					authorizeSubKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型：不需赋值
					authorizeSubKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码：不需赋值
					authorizeSubKColl.addDataField("fldvalue13", "REMARK@" + "");//备注：不需赋值
					authorizeSubKColl.addDataField("fldvalue14", "CARD_NO@" + "");//卡号：不需赋值
					authorizeSubKColl.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志：不需赋值
					authorizeSubKColl.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码：不需赋值
					authorizeSubKColl.addDataField("fldvalue17", "ACCT_GL_CODE@" + acct_gl_code);//账号科目代码：不需赋值
					authorizeSubKColl.addDataField("fldvalue18", "BALANCE@" + "");//账户余额：不需赋值
					authorizeSubKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + "");//受托支付金额：不需赋值			
					authorizeSubKColl.addDataField("fldvalue20", "BANK_NAME@" + opan_org_name);//机构名称
					authorizeSubKColl.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
					authorizeSubKColl.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + is_acct);//是否本行
					
					//开户行地址
					if(interbank_id!=null&&!"".equals(interbank_id)){
						KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, connnection);
						if(kColl!=null){
							authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
						}else{
							authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
						}
					}else{
						authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
					}
					
					dao.insert(authorizeSubKColl, this.getConnection());
				}
			}
			
			/**8.生成授信信息：保证金信息**/
			String bailCondition = " where serno = '"+iqpserno+"'";
			IndexedCollection bailIColl = dao.queryList("PubBailInfo", bailCondition, connnection);
			for(int i=0;i<bailIColl.size();i++){
				KeyedCollection iqpBailInfoKColl = (KeyedCollection)bailIColl.get(i);
				String bail_acct_no = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_no"));//保证金账号
				String bail_cur_type = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("cur_type"));//币种
				String bail_acct_name = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_name"));//保证金账号名称
				String open_org = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("open_org"));//开户机构
				String bail_acct_gl_code = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_gl_code"));//科目号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				/** 给授权从表赋值 */
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "04");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + "");//借据号：不需赋值
				authorizeSubKColl.addDataField("fldvalue03", "BRANCH_ID@" + open_org);//机构代码
				authorizeSubKColl.addDataField("fldvalue04", "GUARANTEE_ACCT_NO@" + bail_acct_no);//保证金账号
				authorizeSubKColl.addDataField("fldvalue05", "GUARANTEE_NO@" + "");//保证金编号：不需赋值
				authorizeSubKColl.addDataField("fldvalue06", "ACCT_TYPE@" + "");//账户类型：不需赋值
				authorizeSubKColl.addDataField("fldvalue07", "GUARANTEE_EXPIRY_DATE@" + "");//保证金到期日：不需赋值
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_CODE@" + "");//账户代码：不需赋值
				authorizeSubKColl.addDataField("fldvalue09", "CA_TT_FLAG@" + "");//钞汇标志：不需赋值
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_GL_CODE@" + bail_acct_gl_code);//账号科目代码
				authorizeSubKColl.addDataField("fldvalue11", "ACCT_NAME@" + bail_acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue12", "CCY@" + bail_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue13", "GUARANTEE_PER@" + security_rate);//保证金比例
				authorizeSubKColl.addDataField("fldvalue14", "GUARANTEE_AMT@" + security_amt);//保证金金额
				authorizeSubKColl.addDataField("fldvalue15", "CONTRACT_NO@" + cont_no);//协议号
				if("510".equals(assure_main)){
					authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "Y");//是否准全额
				}else{
					authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "N");//是否准全额
				}

				dao.insert(authorizeSubKColl, this.getConnection());
			  }
			
			/**9.生成授信信息：费用信息**/
			for(int i=0;i<iqpAppendTermsIColl.size();i++){				
				KeyedCollection iqpAppendTermsKColl = (KeyedCollection)iqpAppendTermsIColl.get(i);
				String fee_code = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_code"));//费用代码
				String fee_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_cur_type"));//币种
				Double fee_amt = TagUtil.replaceNull4Double(iqpAppendTermsKColl.getDataValue("fee_amt"));//费用金额
				String fee_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_type"));//费用类型
				String fee_rate = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_rate"));//费用比率
				String is_cycle_chrg = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_cycle_chrg"));//是否周期性收费标识 
				String chrg_date = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_date"));//收费日期
				String acct_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_acct_no"));//账号
				String chrg_freq = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_freq"));//账号
				if(chrg_freq.equals("Y")){
					chrg_freq = "12";
				}else if(chrg_freq.equals("Q")){
					chrg_freq = "3";
				}else if(chrg_freq.equals("M")){
					chrg_freq = "1";
				}else{
					chrg_freq = "";
				}
//				String acct_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("acct_name"));//账户名
//				String opac_org_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opac_org_no"));//开户行行号
//				String opan_org_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opan_org_name"));//开户行行名
//				String acct_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("cur_type"));//账户币种
//				String is_this_org_acct = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_this_org_acct"));//是否本行账户
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + "");//借据号：不需赋值
				authorizeSubKColl.addDataField("fldvalue03", "FEE_CODE@" + fee_code);//费用代码				
				authorizeSubKColl.addDataField("fldvalue04", "CCY@" + fee_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
			    authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + TradeConstance.is_pay_flag);//收付标志 默认传 R: 收
				authorizeSubKColl.addDataField("fldvalue07", "FEE_TYPE@" + fee_type);//费用类型
				authorizeSubKColl.addDataField("fldvalue08", "BASE_AMOUNT@" + fee_amt);//基准金额 同费用金额
				authorizeSubKColl.addDataField("fldvalue09", "FEE_RATE@" + fee_rate);//费用比率
				authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + acctmap.get(acct_no));//扣款账户类型  默认传 FEE:收费账户			
				authorizeSubKColl.addDataField("fldvalue11", "CHARGE_PERIODICITY_FLAG@" + this.TransSDicForESB("STD_ZX_YES_NO",is_cycle_chrg));//是否周期性收费标识
				authorizeSubKColl.addDataField("fldvalue12", "CHARGE_DATE@" + chrg_date);//收费日期
				authorizeSubKColl.addDataField("fldvalue13", "AMOR_AMT@" + fee_amt);//摊销金额
				authorizeSubKColl.addDataField("fldvalue14", "CONTRACT_NO@" + cont_no);//协议号
//				authorizeSubKColl.addDataField("fldvalue15", "ACCT_NO@" + acct_no);//账号
//				authorizeSubKColl.addDataField("fldvalue16", "ACCT_NAME@" + acct_name);//账户名
//				authorizeSubKColl.addDataField("fldvalue17", "OPAC_ORG_NO@" + opac_org_no);//开户行行号
//				authorizeSubKColl.addDataField("fldvalue18", "OPAN_ORG_NAME@" + opan_org_name);//开户行行名
//				authorizeSubKColl.addDataField("fldvalue19", "ACCT_CCY@" + acct_cur_type);//账户币种
//				authorizeSubKColl.addDataField("fldvalue20", "IS_THIS_ORG_ACCT@" + is_this_org_acct);//是否本行账户
				authorizeSubKColl.addDataField("fldvalue21", "BRANCH_ID@" + in_acct_br_id);//入账机构
				authorizeSubKColl.addDataField("fldvalue22", "FEE_SPAN@" + chrg_freq);//收费间隔
				dao.insert(authorizeSubKColl, this.getConnection());
			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl4trade = new KeyedCollection();
//			kColl4trade.put("CLIENT_NO", cus_id);
//			kColl4trade.put("BUSS_SEQ_NO", iqpserno);
//			kColl4trade.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), this.getConnection());	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常！错误描述："+e.getMessage());
		}
	
	}
	
	/**
	 * 贴现贷款出账流程审批通过
	 * 出账申请审批通过执行操作：（按照票据生成多条票据流水台账）
	 * 1.生成授权信息
	 * 2.生成票据台帐信息
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpDisc(String serno)throws ComponentException {
		Connection connnection = null;
		try {
			
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			connnection = this.getConnection();
			
			/** 1.数据准备：通过业务流水号查询【出账申请】 */
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, connnection);
			String prd_id = (String)pvpLoanKColl.getDataValue("prd_id");//产品编号
			String cus_id = (String)pvpLoanKColl.getDataValue("cus_id");//客户编码
			String cont_no = (String)pvpLoanKColl.getDataValue("cont_no");//合同编号
			String manager_br_id = (String)pvpLoanKColl.getDataValue("manager_br_id");//管理机构
			String in_acct_br_id = (String)pvpLoanKColl.getDataValue("in_acct_br_id");//入账机构
			String cur_type = (String)pvpLoanKColl.getDataValue("cur_type");//币种
			
			
			
			/** 2.数据准备：通过客户编号查询【客户信息】 */
			String cus_name = "";
			String cert_type = "";
			String cert_code = "";
			String belg_line = "";
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			try {
				CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
				CusBase cusBase = csi.getCusBaseByCusId(cus_id,this.getContext(),this.getConnection());
				cus_name = cusBase.getCusName();
				cert_type = cusBase.getCertType();
				cert_code = cusBase.getCertCode();
				belg_line = TagUtil.replaceNull4String(cusBase.getBelgLine());//所属条线
			} catch (Exception e) {
				e.printStackTrace();
				throw new EMPException("获取组织机构模块失败！");
			}
			
			/** 3.数据准备：通过业务流水号查询【合同信息】 */
			KeyedCollection ctrContSubKColl =  dao.queryDetail("CtrDiscCont", cont_no, connnection);
			String five_classfiy = (String)ctrContSubKColl.getDataValue("five_classfiy");//五级分类
			
			/**4.数据准备：获取票据批次号*/
			String condition = " where cont_no ='" + cont_no + "'";
			KeyedCollection iqpBatchKColl = dao.queryFirst("IqpBatchMng", null, condition, connnection);
			String batch_no = (String) iqpBatchKColl.getDataValue("batch_no");
			String iqpserno = (String) iqpBatchKColl.getDataValue("serno");
			String biz_type = (String) iqpBatchKColl.getDataValue("biz_type");
			
			/** 5.数据准备：通过获取票据批次下的票据明细关系 */
			KeyedCollection iqpLoanKColl = dao.queryDetail(IQPLOANAPPMODEL, iqpserno, this.getConnection());
			String assure_main = TagUtil.replaceNull4String(iqpLoanKColl.getDataValue("assure_main"));//担保方式
			String relCondition = " where batch_no ='" + batch_no + "'";
			IndexedCollection relIColl = dao.queryList("IqpBatchBillRel", relCondition, connnection);
			
			String nowDate = (String)this.getContext().getDataValue(CMISConstance.OPENDAY);
			if(nowDate == null || nowDate.trim().length() == 0){
				throw new EMPException("获取系统营业时间失败！请检查系统营业时间配置是否正确！");
			}
			
			//生成授权编号，所有票据授权交易流水在一个授权编号下
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());
			
			/** 6.循环生成授权信息和台账信息*/
			Map<String, String> acctmap = new HashMap<String, String>();//定义一个账号对应map
			for(int i=0;i<relIColl.size();i++){
				/** （1）获取借据编号*/
				PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
                .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), connnection);
				String billNo = cmisComponent.getBillNoByContNo(cont_no);
				
				KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
				
				KeyedCollection relKColl = (KeyedCollection) relIColl.get(i);
				String porder_no = (String) relKColl.getDataValue("porder_no");//票据号
				Map<String,String> incomeMap = new HashMap<String,String>();
				incomeMap.put("batch_no",batch_no);
				incomeMap.put("porder_no",porder_no);
				/**（2）收益表信息*/
				KeyedCollection incomeKColl = dao.queryDetail("IqpBillIncome", incomeMap, connnection);
				String drft_amt = (String) incomeKColl.getDataValue("drft_amt");
				String fore_disc_date = (String) incomeKColl.getDataValue("fore_disc_date");
				String disc_days = (String) incomeKColl.getDataValue("disc_days");
				String adj_days = (String) incomeKColl.getDataValue("adj_days");
				String disc_rate = (String) incomeKColl.getDataValue("disc_rate");
				String int_amt = (String) incomeKColl.getDataValue("int");
				
				BigDecimal rpay_amt = new BigDecimal(drft_amt).subtract(new BigDecimal(int_amt));
				String dscnt_int_pay_mode = "";
				if(incomeKColl.containsKey("dscnt_int_pay_mode")&&incomeKColl.getDataValue("dscnt_int_pay_mode")!=null&&!"".equals(incomeKColl.getDataValue("dscnt_int_pay_mode"))){
					dscnt_int_pay_mode = (String) incomeKColl.getDataValue("dscnt_int_pay_mode");
					if(dscnt_int_pay_mode.equals("1")){//卖方付息
						dscnt_int_pay_mode = "1";
					}else if(dscnt_int_pay_mode.equals("2")){//买方付息
						dscnt_int_pay_mode = "0";
					}else if(dscnt_int_pay_mode.equals("3")){//协议付息
						dscnt_int_pay_mode = "2";
					}
				}
				
				/**（3）票据明细表信息*/
				KeyedCollection billDetailKColl = dao.queryDetail("IqpBillDetail", porder_no, connnection);
				String porder_curr = (String) billDetailKColl.getDataValue("porder_curr");
				String is_ebill = (String) billDetailKColl.getDataValue("is_ebill");
				String bill_isse_date = (String) billDetailKColl.getDataValue("bill_isse_date");
				
				/**（4）生成交易流水号 */
				String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connnection, this.getContext());
				
				/**（5）给授权信息表赋值 */
				authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
				authorizeKColl.addDataField("serno", serno);//业务流水号（出账编号）
				authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
				authorizeKColl.addDataField("prd_id", prd_id);//产品编号
				authorizeKColl.addDataField("cus_id", cus_id);//客户编码
				authorizeKColl.addDataField("cus_name", cus_name);//客户名称
				authorizeKColl.addDataField("cont_no", cont_no);//合同编号
				authorizeKColl.addDataField("bill_no", billNo);//借据编号
				authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_TXFFSQ+TradeConstance.SERVICE_SCENE_TXFFSQ);//交易码+场景
				authorizeKColl.addDataField("tran_amt", drft_amt);//交易金额
				authorizeKColl.addDataField("tran_date", nowDate);//交易日期取发送日期默认不赋值
				authorizeKColl.addDataField("send_times", "0");//发送次数
				authorizeKColl.addDataField("return_code", "");//返回编码
				authorizeKColl.addDataField("return_desc", "");//返回信息
				authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
				if(is_ebill != null && "1".equals(is_ebill)){
					authorizeKColl.addDataField("status", "05");//状态
				}else{
					authorizeKColl.addDataField("status", "00");//状态
				}
				authorizeKColl.addDataField("cur_type", cur_type);//币种
				
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编码
				authorizeKColl.addDataField("fldvalue02", "OPERATION_TYPE@"+"BUY");//操作类型（直贴，默认为买入）
				authorizeKColl.addDataField("fldvalue03", "BACK_FLAG@"+"N");//回购/返售标识（直贴，默认为否“N”）·
				authorizeKColl.addDataField("fldvalue04", "INPUT_DATE@"+TagUtil.formatDate(iqpLoanKColl.getDataValue("apply_date")));//填入日期
				authorizeKColl.addDataField("fldvalue05", "BANK_ID@"+TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue06", "DISCONT_AGREE_NO@"+cont_no);//贴现协议号
				authorizeKColl.addDataField("fldvalue07", "LOAN_TYPE@"+prd_id);//贷款种类
				authorizeKColl.addDataField("fldvalue08", "DUEBILL_NO@"+billNo);//借据号
				authorizeKColl.addDataField("fldvalue09", "BILL_NO@"+porder_no);//汇票号码
				authorizeKColl.addDataField("fldvalue10", "BILL_TYPE@"+this.TransSDicForESB("STD_DRFT_TYPE",(String)billDetailKColl.getDataValue("bill_type")));//票据类型
				authorizeKColl.addDataField("fldvalue11", "APPLYER_GLOBAL_TYPE@"+cert_type);//贴现申请人证件类型
				authorizeKColl.addDataField("fldvalue12", "APPLYER_GLOBAL_ID@"+cert_code);//贴现申请人证件号
				authorizeKColl.addDataField("fldvalue13", "APPLYER_ISS_CTRY@"+"CN");//贴现申请人发证国家
				authorizeKColl.addDataField("fldvalue14", "APPLYER_NAME@"+cus_name);//贴现申请人名称
				authorizeKColl.addDataField("fldvalue15", "APPLYER_ACCT_NO@"+"");//贴现申请人户名（目前不传）
				authorizeKColl.addDataField("fldvalue16", "BILL_BRANCH_ID@"+in_acct_br_id);//借据机构代码（取入账机构）
				authorizeKColl.addDataField("fldvalue17", "DISCOUNT_KIND@"+"NORM");//贴现种类:普通贴现
				authorizeKColl.addDataField("fldvalue18", "DISCOUNT_CCY@"+billDetailKColl.getDataValue("porder_curr"));//币种
				authorizeKColl.addDataField("fldvalue19", "DISCOUNT_AMT@"+billDetailKColl.getDataValue("drft_amt"));//贴现金额
				authorizeKColl.addDataField("fldvalue20", "DISCOUNT_DAYS@"+disc_days);//贴现天数
				authorizeKColl.addDataField("fldvalue21", "DISCOUNT_RATE@"+disc_rate);//贴现利率
				authorizeKColl.addDataField("fldvalue22", "TRANSFER_DISCOUNT_RATE@"+"");//转贴现利率
				authorizeKColl.addDataField("fldvalue23", "TRANSFER_DISCOUNT_DATE@"+"");//转贴现日期
				authorizeKColl.addDataField("fldvalue24", "DISCOUNT_INTEREST@"+int_amt);//贴现利息 
				authorizeKColl.addDataField("fldvalue25", "DISCOUNT_DATE@"+TagUtil.formatDate(fore_disc_date));//贴现日期
				authorizeKColl.addDataField("fldvalue26", "BILL_ISSUE_DATE@"+TagUtil.formatDate(billDetailKColl.getDataValue("bill_isse_date")));//出票日期
				authorizeKColl.addDataField("fldvalue27", "BILL_EXPIRY_DATE@"+TagUtil.formatDate(billDetailKColl.getDataValue("porder_end_date")));//票据到期日期
				authorizeKColl.addDataField("fldvalue28", "RETURN_DATE@"+"");//约定转回日期
				authorizeKColl.addDataField("fldvalue29", "EXPIRY_DATE@"+TagUtil.formatDate(billDetailKColl.getDataValue("porder_end_date")));//到期日期（同传票据到期日）
				authorizeKColl.addDataField("fldvalue30", "DEDUCT_METHOD@"+"AUTOPAY");//扣款方式
				authorizeKColl.addDataField("fldvalue31", "TO_BRANCH_ID@"+"");//对方机构代码（目前不传）
				
				/**（6）取票据下的付息账号信息*/
				String pintCondition = " where batch_no='"+batch_no+"' and porder_no = '"+porder_no+"'";
				IndexedCollection pintIColl = dao.queryList("IqpBillPintDetail", pintCondition, connnection);
				for(int k=0;k<pintIColl.size();k++){
					int d = k+1;//标识账户类型
					
					KeyedCollection temp = (KeyedCollection)pintIColl.get(k);
					String pint_no = (String)temp.getDataValue("pint_no");
					String pint_acct_name = (String)temp.getDataValue("pint_acct_name");
					String ccy = (String)temp.getDataValue("cur_type");
					String acc_type = (String)temp.getDataValue("acc_type");
					String acct_gl_code = (String)temp.getDataValue("acct_gl_code");
					String pint_amt = (String)temp.getDataValue("pint_amt");
					String acctsvcr = (String)temp.getDataValue("acctsvcr");
					String acctsvcrnm = (String)temp.getDataValue("acctsvcrnm");
					String interbank_id = TagUtil.replaceNull4String(temp.getDataValue("interbank_id"));//银联行号
					
					
					/** 8.生成授权信息：账户信息（取自IQP_BILL_PINT_DETAIL表）*/
					KeyedCollection pvpAcctKCollAcc = new KeyedCollection(AUTHORIZESUBMODEL);
					pvpAcctKCollAcc.addDataField("auth_no", authSerno);
					pvpAcctKCollAcc.addDataField("busi_cls", "03");//账号信息
					pvpAcctKCollAcc.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编号
					pvpAcctKCollAcc.addDataField("fldvalue02", "DUEBILL_NO@"+billNo);//借据号（无意义，暂定不传）
					pvpAcctKCollAcc.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "PAYM"+d);//系统账户类型
					pvpAcctKCollAcc.addDataField("fldvalue04", "ACCT_NO@"+pint_no);//贴现人结算账户
					pvpAcctKCollAcc.addDataField("fldvalue05", "CCY@"+ccy);//账户币种
					pvpAcctKCollAcc.addDataField("fldvalue06", "BANK_ID@"+interbank_id);//帐号银行代码
					pvpAcctKCollAcc.addDataField("fldvalue07", "BRANCH_ID@"+acctsvcr);//帐号机构代码
					pvpAcctKCollAcc.addDataField("fldvalue08", "ACCT_NAME@"+pint_acct_name);//户名
					pvpAcctKCollAcc.addDataField("fldvalue09", "ISS_CTRY@"+"CN");//发证国家
					pvpAcctKCollAcc.addDataField("fldvalue10", "ACCT_TYPE@" + acc_type);//账户类型
					pvpAcctKCollAcc.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（非必输）
					pvpAcctKCollAcc.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（非必输）
					pvpAcctKCollAcc.addDataField("fldvalue13", "REMARK@" + "");//备注
					pvpAcctKCollAcc.addDataField("fldvalue14", "CARD_NO@"+"");//卡号
					pvpAcctKCollAcc.addDataField("fldvalue15", "CA_TT_FLAG@"+"");//钞汇标志
					pvpAcctKCollAcc.addDataField("fldvalue16", "ACCT_CODE@"+"");//账户代码
					pvpAcctKCollAcc.addDataField("fldvalue17", "ACCT_GL_CODE@"+acct_gl_code);//账号科目代码
					pvpAcctKCollAcc.addDataField("fldvalue18", "BALANCE@"+"");//账户余额
					pvpAcctKCollAcc.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@"+pint_amt);//此交易字段存储为付息金额
					pvpAcctKCollAcc.addDataField("fldvalue20", "BANK_NAME@"+acctsvcrnm);//开户行行名
					pvpAcctKCollAcc.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
					pvpAcctKCollAcc.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
					
					//开户行地址
					if(interbank_id!=null&&!"".equals(interbank_id)){
						KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, connnection);
						if(kColl!=null){
							pvpAcctKCollAcc.addDataField("fldvalue23", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
						}else{
							pvpAcctKCollAcc.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
						}
					}else{
						pvpAcctKCollAcc.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
					}
										
					dao.insert(pvpAcctKCollAcc, connnection);
				}
				authorizeKColl.addDataField("fldvalue37", "DSCNT_INT_PAY_MODE@"+dscnt_int_pay_mode);//贴现付息方式
				authorizeKColl.addDataField("fldvalue38", "BRANCH_ID@"+in_acct_br_id);//取入账机构
				KeyedCollection acctKColl = dao.queryDetail("IqpDiscApp", iqpserno, connnection);
				String agent_acct_name = (String)acctKColl.getDataValue("agent_acct_name");
				String agent_acct_no = (String)acctKColl.getDataValue("agent_acct_no");
				String agent_org_no = (String)acctKColl.getDataValue("agent_org_no");
				String agent_org_name = (String)acctKColl.getDataValue("agent_org_name");
				if(agent_acct_name==null){
					agent_acct_name ="";
				}
				if(agent_acct_no==null){
					agent_acct_no ="";
				}
				if(agent_org_no==null){
					agent_org_no ="";
				}
				if(agent_org_name==null){
					agent_org_name ="";
				}
				authorizeKColl.addDataField("fldvalue39", "AGENTOR_NAME@"+agent_acct_name);//代理人名称
				authorizeKColl.addDataField("fldvalue40", "AGENTOR_ACCT_NO@"+agent_acct_no);//代理人账号
				authorizeKColl.addDataField("fldvalue41", "AGENT_ORG_NO@"+agent_org_no);//代理人开户行行号
				authorizeKColl.addDataField("fldvalue42", "AGENT_ORG_NAME@"+agent_org_name);//代理人开户行行名
				authorizeKColl.addDataField("fldvalue43", "COUNTER_OPEN_BRANCH_ID@"+"");//对手行行号
				authorizeKColl.addDataField("fldvalue44", "COUNTER_OPEN_BRANCH_NAME@"+"");//对手行行名
				String bill_type = (String) billDetailKColl.getDataValue("bill_type");
				if("100".equals(bill_type)){
					//银票
					authorizeKColl.addDataField("fldvalue45", "AA_NAME@"+TagUtil.replaceNull4String(billDetailKColl.getDataValue("aorg_name")));//承兑人名称
					authorizeKColl.addDataField("fldvalue46", "AAORG_NO@"+TagUtil.replaceNull4String(billDetailKColl.getDataValue("aorg_no")));//承兑行行号
					authorizeKColl.addDataField("fldvalue47", "AAORG_NAME@"+TagUtil.replaceNull4String(billDetailKColl.getDataValue("aorg_name")));//承兑行名称
				}else{
					//商票
					authorizeKColl.addDataField("fldvalue45", "AA_NAME@"+TagUtil.replaceNull4String(billDetailKColl.getDataValue("isse_name")));//承兑人名称
					authorizeKColl.addDataField("fldvalue46", "AAORG_NO@"+TagUtil.replaceNull4String(billDetailKColl.getDataValue("aaorg_no")));//承兑人开户行行号
					authorizeKColl.addDataField("fldvalue47", "AAORG_NAME@"+TagUtil.replaceNull4String(billDetailKColl.getDataValue("aaorg_name")));//承兑人开户行名称
				}
				authorizeKColl.addDataField("fldvalue48", "PAY_AMT@"+BigDecimalUtil.replaceNull(""));//实付金额
				authorizeKColl.addDataField("fldvalue49", "ARRANGR_DEDUCT_OPT@"+BigDecimalUtil.replaceNull(""));//预扣款项
				authorizeKColl.addDataField("fldvalue50", "APPLYER_ID@"+cus_id);//贴现申请人编号
				authorizeKColl.addDataField("fldvalue51", "E_CDE@" + is_ebill);//是否电票
				authorizeKColl.addDataField("fldvalue52", "BATCH_NO@" + batch_no);//批次号
				authorizeKColl.addDataField("fldvalue53", "CUST_TYP@" + belg_line);//客户类型取所属条线
				authorizeKColl.addDataField("fldvalue54", "DISC_OD_INT_RATE@" + "");//逾期利率
				authorizeKColl.addDataField("fldvalue55", "BILL_APP_NAME@" + TagUtil.replaceNull4String(billDetailKColl.getDataValue("isse_name")));//出票人名称
				authorizeKColl.addDataField("fldvalue56", "ACP_BANK_ACCT_NO@" + TagUtil.replaceNull4String(billDetailKColl.getDataValue("aaorg_acct_no")));//承兑人开户行账号
				authorizeKColl.addDataField("fldvalue57", "DSCNT_TYPE@" + TagUtil.replaceNull4String(biz_type));//贴现方式
				authorizeKColl.addDataField("fldvalue58", "BILL_ACCT_NO@" + TagUtil.replaceNull4String(billDetailKColl.getDataValue("daorg_acct")));//出票人账号
				authorizeKColl.addDataField("fldvalue59", "BILL_OPEN_BRANCH_ID@" + TagUtil.replaceNull4String(billDetailKColl.getDataValue("daorg_no")));//出票人开户行行号 
				authorizeKColl.addDataField("fldvalue60", "BILL_OPEN_BRANCH_NAME@" + TagUtil.replaceNull4String(billDetailKColl.getDataValue("daorg_name")));//出票人开户行行名
				
				dao.insert(authorizeKColl, this.getConnection());
				
				/**生成授权信息：结算账户信息（取自账号信息表iqp_cus_acct）**/
				String conditionCusAcct = "where serno='"+iqpserno+"'";
				IndexedCollection iqpCusAcctIColl = dao.queryList(IQPCUSACCT, conditionCusAcct, this.getConnection());
				int feecount = 0;
				int eactcount = 0;
				for(int m=0;m<iqpCusAcctIColl.size();m++){				
					KeyedCollection iqpCusAcctKColl = (KeyedCollection)iqpCusAcctIColl.get(m);
					String acct_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_no"));//账号
					String opac_org_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opac_org_no"));//开户行行号
					String opan_org_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opan_org_name"));//开户行行名
					String is_this_org_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行账户
					String acct_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_name"));//户名
					Double pay_amt = TagUtil.replaceNull4Double(iqpCusAcctKColl.getDataValue("pay_amt"));//受托支付金额
					String acct_gl_code = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_gl_code"));//科目号
					String ccy = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("cur_type"));//币种
					String is_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行
					/*01放款账号  02印花税账号 03收息收款账号    04受托支付账号     05	主办行资金归集账户*/
					String acct_attr = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_attr"));//账户属性
					String interbank_id = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("interbank_id"));//银联行号
					
					KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
					authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
					authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
					authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
					authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + billNo);//借据号
					
					/*PAYM:还款账户 ACTV:放款账号 TURE:委托账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号*/
					if(!"".equals(acct_attr) && acct_attr.equals("01")){
						acct_attr = "ACTV";
					}else if(!"".equals(acct_attr) && acct_attr.equals("02")){
						acct_attr = "STAMP1";
					}else if(!"".equals(acct_attr) && acct_attr.equals("03")){
						acct_attr = "PAYM";
					}else if(!"".equals(acct_attr) && acct_attr.equals("04")){
						if(eactcount==0){
							acct_attr = "EACT";
						}else{
							acct_attr = "EACT"+eactcount;
						}
						eactcount++;
					}else if(!"".equals(acct_attr) && acct_attr.equals("06")){
						acct_attr = "STAMP2";
					}else if(!"".equals(acct_attr) && acct_attr.equals("07")){
						if(feecount==0){
							acct_attr = "FEE";
						}else{
							acct_attr = "FEE"+feecount;
						}
						feecount++;
						acctmap.put(acct_no, acct_attr);
					}else{
						acct_attr = "";
					}
					
					authorizeSubKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + acct_attr);//贷款账户类型 				
					authorizeSubKColl.addDataField("fldvalue04", "ACCT_NO@" + acct_no);//账号
					authorizeSubKColl.addDataField("fldvalue05", "CCY@" + ccy);//币种
//					if(is_this_org_acct.equals("1")){//本行账户
//						authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
//					}else{
//						authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + opac_org_no);//帐号银行代码
//					}
					authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + interbank_id);//帐号银行代码
					authorizeSubKColl.addDataField("fldvalue07", "BRANCH_ID@" + opac_org_no);//机构代码
					authorizeSubKColl.addDataField("fldvalue08", "ACCT_NAME@" + acct_name);//户名
					authorizeSubKColl.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
					authorizeSubKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "1");//账户类型:1-结算账户 2-保证金				
					authorizeSubKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue17", "ACCT_GL_CODE@" + acct_gl_code);//账号科目代码（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
					authorizeSubKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + pay_amt);//受托支付金额
					authorizeSubKColl.addDataField("fldvalue20", "BANK_NAME@" + opan_org_name);//银行名称
					authorizeSubKColl.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
					authorizeSubKColl.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + is_acct);//是否本行
					
					//开户行地址
					if(interbank_id!=null&&!"".equals(interbank_id)){
						KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, connnection);
						if(kColl!=null){
							authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
						}else{
							authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
						}
					}else{
						authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
					}
					
					dao.insert(authorizeSubKColl, this.getConnection());
				}
				
				/**（7）生成台账信息*/
				KeyedCollection accDrftKColl = new KeyedCollection("AccDrft");//票据流水台账表
				accDrftKColl.addDataField("serno", serno);//业务编号	
				accDrftKColl.addDataField("acc_day", nowDate);//日期
				accDrftKColl.addDataField("acc_year", nowDate.substring(0, 4));//年份
				accDrftKColl.addDataField("acc_mon", nowDate.substring(5, 7));//月份
				accDrftKColl.addDataField("prd_id", prd_id);//产品编号
				accDrftKColl.addDataField("cont_no", cont_no);//合同编号
				accDrftKColl.addDataField("bill_no", billNo);//借据编号
				accDrftKColl.addDataField("dscnt_type", "07");//贴现方式（为直贴）
				accDrftKColl.addDataField("porder_no", porder_no);//汇票号码
				accDrftKColl.addDataField("discount_per", cus_id );//贴现人/交易对手
				accDrftKColl.addDataField("dscnt_date", fore_disc_date);//贴现日
				accDrftKColl.addDataField("dscnt_day", disc_days);//贴现天数
				accDrftKColl.addDataField("adjust_day", adj_days);//调整天数
				accDrftKColl.addDataField("dscnt_rate", disc_rate);//贴现利率
				accDrftKColl.addDataField("cur_type", porder_curr);//交易币种
				accDrftKColl.addDataField("dscnt_int", int_amt);//贴现利息
				accDrftKColl.addDataField("rpay_amt", rpay_amt);//实付金额
				accDrftKColl.addDataField("rebuy_date", "");//回购日期
				accDrftKColl.addDataField("rebuy_day", "");//回购天数
				accDrftKColl.addDataField("rebuy_rate", "");//回购利率
				accDrftKColl.addDataField("overdue_rebuy_rate", "");//逾期回购利率
				accDrftKColl.addDataField("rebuy_int", "");//回购利息
				accDrftKColl.addDataField("separate_date", "");//清分日期
				accDrftKColl.addDataField("writeoff_date", "");//核销日期
				accDrftKColl.addDataField("five_class", five_classfiy);//五级分类
				accDrftKColl.addDataField("twelve_cls_flg", "");//十二级分类标志
				accDrftKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				accDrftKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
				accDrftKColl.addDataField("accp_status", "0");//台账状态：出帐未确认
				dao.insert(accDrftKColl, this.getConnection());
			}
			
			/** 7.生成授权信息：费用信息*/
			String conditionFee = "where serno='"+iqpserno+"'";
			IndexedCollection iqpAppendTermsIColl = dao.queryList(IQPFEE, conditionFee, this.getConnection());
			for(int i=0;i<iqpAppendTermsIColl.size();i++){				
				KeyedCollection iqpAppendTermsKColl = (KeyedCollection)iqpAppendTermsIColl.get(i);
				String fee_code = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_code"));//费用代码
				String fee_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_cur_type"));//币种
				Double fee_amt = TagUtil.replaceNull4Double(iqpAppendTermsKColl.getDataValue("fee_amt"));//费用金额
				String fee_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_type"));//费用类型
				String fee_rate = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_rate"));//费用比率
				String is_cycle_chrg = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_cycle_chrg"));//是否周期性收费标识 
				String chrg_date = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_date"));//收费日期
				String acct_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_acct_no"));//账号
				String chrg_freq = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_freq"));//账号
				if(chrg_freq.equals("Y")){
					chrg_freq = "12";
				}else if(chrg_freq.equals("Q")){
					chrg_freq = "3";
				}else if(chrg_freq.equals("M")){
					chrg_freq = "1";
				}else{
					chrg_freq = "";
				}
//				String acct_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("acct_name"));//账户名
//				String opac_org_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opac_org_no"));//开户行行号
//				String opan_org_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opan_org_name"));//开户行行名
//				String acct_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("cur_type"));//账户币种
//				String is_this_org_acct = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_this_org_acct"));//是否本行账户
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + "");//借据号：不需赋值
				authorizeSubKColl.addDataField("fldvalue03", "FEE_CODE@" + fee_code);//费用代码				
				authorizeSubKColl.addDataField("fldvalue04", "CCY@" + fee_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
			    authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + TradeConstance.is_pay_flag);//收付标志 默认传 R: 收
				authorizeSubKColl.addDataField("fldvalue07", "FEE_TYPE@" + fee_type);//费用类型
				authorizeSubKColl.addDataField("fldvalue08", "BASE_AMOUNT@" + fee_amt);//基准金额 同费用金额
				authorizeSubKColl.addDataField("fldvalue09", "FEE_RATE@" + fee_rate);//费用比率
				authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + acctmap.get(acct_no));//扣款账户类型  默认传 FEE:收费账户			
				authorizeSubKColl.addDataField("fldvalue11", "CHARGE_PERIODICITY_FLAG@" + this.TransSDicForESB("STD_ZX_YES_NO",is_cycle_chrg));//是否周期性收费标识
				authorizeSubKColl.addDataField("fldvalue12", "CHARGE_DATE@" + chrg_date);//收费日期
				authorizeSubKColl.addDataField("fldvalue13", "AMOR_AMT@" + fee_amt);//摊销金额
				authorizeSubKColl.addDataField("fldvalue14", "CONTRACT_NO@" + cont_no);//协议号
//				authorizeSubKColl.addDataField("fldvalue15", "ACCT_NO@" + acct_no);//账号
//				authorizeSubKColl.addDataField("fldvalue16", "ACCT_NAME@" + acct_name);//账户名
//				authorizeSubKColl.addDataField("fldvalue17", "OPAC_ORG_NO@" + opac_org_no);//开户行行号
//				authorizeSubKColl.addDataField("fldvalue18", "OPAN_ORG_NAME@" + opan_org_name);//开户行行名
//				authorizeSubKColl.addDataField("fldvalue19", "ACCT_CCY@" + acct_cur_type);//账户币种
//				authorizeSubKColl.addDataField("fldvalue20", "IS_THIS_ORG_ACCT@" + is_this_org_acct);//是否本行账户
				authorizeSubKColl.addDataField("fldvalue21", "BRANCH_ID@" + in_acct_br_id);//入账机构
				authorizeSubKColl.addDataField("fldvalue22", "FEE_SPAN@" + chrg_freq);//收费间隔
				dao.insert(authorizeSubKColl, this.getConnection());
			}
			/** 8.生成授权信息：结算账户信息（取自IqpDiscApp贴现申请从表）
			KeyedCollection acctKColl = dao.queryDetail("IqpDiscApp", iqpserno, connnection);
			KeyedCollection pvpAcctKColl = new KeyedCollection(AUTHORIZESUBMODEL);
			pvpAcctKColl.addDataField("auth_no", authSerno);
			pvpAcctKColl.addDataField("busi_cls", "03");//账号信息
			pvpAcctKColl.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编号
			pvpAcctKColl.addDataField("fldvalue02", "DUEBILL_NO@"+"");//借据号（对应多笔，确认不传）
			pvpAcctKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "PAYM");//系统账户类型
			pvpAcctKColl.addDataField("fldvalue04", "ACCT_NO@"+acctKColl.getDataValue("disc_sett_acct_no"));//贴现人结算账户
			pvpAcctKColl.addDataField("fldvalue05", "CCY@"+"");//账户币种
			pvpAcctKColl.addDataField("fldvalue06", "BANK_ID@"+TradeConstance.BANK_ID);//帐号银行代码
			pvpAcctKColl.addDataField("fldvalue07", "BRANCH_ID@"+"");//帐号机构代码
			pvpAcctKColl.addDataField("fldvalue08", "ACCT_NAME@"+acctKColl.getDataValue("disc_sett_acct_name"));//户名
			pvpAcctKColl.addDataField("fldvalue09", "ISS_CTRY@"+"CN");//发证国家
			pvpAcctKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "1");//账户类型：默认 1-结算账户
			pvpAcctKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（非必输）
			pvpAcctKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（非必输）
			pvpAcctKColl.addDataField("fldvalue13", "REMARK@" + "");//备注
			pvpAcctKColl.addDataField("fldvalue14", "CARD_NO@"+"");//卡号
			pvpAcctKColl.addDataField("fldvalue15", "CA_TT_FLAG@"+"");//钞汇标志
			pvpAcctKColl.addDataField("fldvalue16", "ACCT_CODE@"+"");//账户代码
			pvpAcctKColl.addDataField("fldvalue17", "ACCT_GL_CODE@"+"");//账号科目代码
			pvpAcctKColl.addDataField("fldvalue18", "BALANCE@"+"");//账户余额
			pvpAcctKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@"+"");//受托支付发放金额
			pvpAcctKColl.addDataField("fldvalue20", "BANK_NAME@"+"");//银行名称
			pvpAcctKColl.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
			dao.insert(pvpAcctKColl, connnection);
			*/
			
			
			/**生成授信信息：保证金信息**/
			KeyedCollection ctrContKColl =  dao.queryDetail(CTRCONTMODEL, cont_no, this.getConnection());
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 start
			BigDecimal security_rate = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("security_rate"));//保证金比例
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 end
			Double security_amt = TagUtil.replaceNull4Double(ctrContKColl.getDataValue("security_amt"));//保证金金额
			String bailCondition = " where serno = '"+iqpserno+"'";
			IndexedCollection bailIColl = dao.queryList("PubBailInfo", bailCondition, connnection);
			for(int i=0;i<bailIColl.size();i++){
				KeyedCollection iqpBailInfoKColl = (KeyedCollection)bailIColl.get(i);
				String bail_acct_no = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_no"));//保证金账号
				String bail_cur_type = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("cur_type"));//币种
				String bail_acct_name = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_name"));//保证金账号名称
				String open_org = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("open_org"));//开户机构
				String bail_acct_gl_code = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_gl_code"));//科目号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				/** 给授权从表赋值 */
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "04");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + "");//借据号：不需赋值
				authorizeSubKColl.addDataField("fldvalue03", "BRANCH_ID@" + open_org);//机构代码
				authorizeSubKColl.addDataField("fldvalue04", "GUARANTEE_ACCT_NO@" + bail_acct_no);//保证金账号
				authorizeSubKColl.addDataField("fldvalue05", "GUARANTEE_NO@" + "");//保证金编号：不需赋值
				authorizeSubKColl.addDataField("fldvalue06", "ACCT_TYPE@" + "");//账户类型：不需赋值
				authorizeSubKColl.addDataField("fldvalue07", "GUARANTEE_EXPIRY_DATE@" + "");//保证金到期日：不需赋值
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_CODE@" + "");//账户代码：不需赋值
				authorizeSubKColl.addDataField("fldvalue09", "CA_TT_FLAG@" + "");//钞汇标志：不需赋值
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_GL_CODE@" + bail_acct_gl_code);//账号科目代码：不需赋值
				authorizeSubKColl.addDataField("fldvalue11", "ACCT_NAME@" + bail_acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue12", "CCY@" + bail_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue13", "GUARANTEE_PER@" + security_rate);//保证金比例
				authorizeSubKColl.addDataField("fldvalue14", "GUARANTEE_AMT@" + security_amt);//保证金金额
				authorizeSubKColl.addDataField("fldvalue15", "CONTRACT_NO@" + cont_no);//协议号
				if("510".equals(assure_main)){
					authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "Y");//是否准全额
				}else{
					authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "N");//是否准全额
				}

				dao.insert(authorizeSubKColl, this.getConnection());
			  }
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl4trade = new KeyedCollection();
//			kColl4trade.put("CLIENT_NO", cus_id);
//			kColl4trade.put("BUSS_SEQ_NO", iqpserno);
//			kColl4trade.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), this.getConnection());	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常！错误描述："+e.getMessage());
		}
	
	}
	
	/**
	 * 展期出账流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpExtend(String serno)throws ComponentException {
		Connection connection = null;
		String sql = "";
		try {
			connection = this.getConnection();			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			CatalogManaAgent catalogManaAgent = (CatalogManaAgent)this.getAgentInstance("CatalogManaAgent");
			
			KeyedCollection pvpLoanKColl = dao.queryDetail("IqpExtensionPvp", serno, connection); //展期出账表数据
			KeyedCollection tranc_kColl = new KeyedCollection(); //传值kcoll
			String fount_bill_no = pvpLoanKColl.getDataValue("fount_bill_no").toString(); //原借据编号
			String fount_cont_no = pvpLoanKColl.getDataValue("fount_cont_no").toString(); //原合同编号
			String agr_no = pvpLoanKColl.getDataValue("agr_no").toString(); //展期协议编号
			
			KeyedCollection result = null;
			result = (KeyedCollection)SqlClient.queryFirst("queryAccLoanByBillNo", fount_bill_no, null, connection);
			String base_acct_no = "";
			String acct_seq_no = "";
			String currType= "";
			if(result!=null){
				base_acct_no = (String)result.getDataValue("base_acct_no");
				acct_seq_no = (String)result.getDataValue("acct_seq_no");
			}else{
				throw new EMPException("通过借据号："+fount_bill_no+"查询台账贷款号、发放号为空！");
			}
			
			/*** 1.验证展期合同状态 ***/
			sql = "select a.status from iqp_extension_agr a where a.agr_no = '"+agr_no+"'";
			tranc_kColl.addDataField("sql", sql);
			String status = catalogManaAgent.excuteSql("selectIqpExtensionPvp", tranc_kColl).getDataValue("result").toString();
			if(!status.equals("200")){
				throw new EMPException("验证展期协议状态错误！请确认展期协议状态为正常！");
			}else{
/**更新业务信息已移到展期成功通知交易处理   start  2014-03-10*/
//				/*** 2.更新合同状态：200 -> 900 ***/
//				sql = "update iqp_extension_agr a set a.status = '900' where a.agr_no = '"+agr_no+"'";
//				tranc_kColl.setDataValue("sql", sql);
//				catalogManaAgent.excuteSql("updateIqpExtensionPvp", tranc_kColl);
//				
//				/*** 3.更新原台账中展期次数+1 ***/
//				sql = "update Acc_Loan a set a.post_count = a.post_count + 1 where a.bill_no = '"+fount_bill_no+"'";
//				tranc_kColl.setDataValue("sql", sql);
//				catalogManaAgent.excuteSql("updateIqpExtensionPvp", tranc_kColl);
/**更新业务信息已移到展期成功通知交易处理   end*/
				
				/*** 4.写入授权表***/
				KeyedCollection ctrContKColl =  dao.queryDetail(CTRCONTMODEL, fount_cont_no, this.getConnection()); //原合同表数据
				KeyedCollection iqpLoanSubKColl = dao.queryDetail(CTRCONTSUBMODEL, fount_cont_no, this.getConnection());//合同从表
				
				CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
				CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
				CusBase cusBase = csi.getCusBaseByCusId((String)pvpLoanKColl.getDataValue("cus_id"),this.getContext(),this.getConnection());
				
				String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connection, this.getContext());
				String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());
				String cus_name = cusBase.getCusName();
				String int_rate_mode = (String) iqpLoanSubKColl.getDataValue("ir_accord_type");
				
				int_rate_mode = "FX";//FX: 固定利率
				
				KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL); //授权信息kColl
				authorizeKColl.addDataField("serno",serno);  //业务编号
				authorizeKColl.addDataField("tran_serno",tranSerno);  //交易流水号
				authorizeKColl.addDataField("prd_id",(String)ctrContKColl.getDataValue("prd_id"));  //产品编号
				authorizeKColl.addDataField("cus_id",(String)pvpLoanKColl.getDataValue("cus_id"));  //客户编码
				authorizeKColl.addDataField("cus_name",cus_name);  //客户名称
				authorizeKColl.addDataField("cont_no",(String)pvpLoanKColl.getDataValue("agr_no"));  //合同编号
				authorizeKColl.addDataField("bill_no",fount_bill_no);  //借据编码
				authorizeKColl.addDataField("tran_id",TradeConstance.SERVICE_CODE_DKHKSQ+TradeConstance.SERVICE_SCENE_DKZQSQ);  //交易码
				authorizeKColl.addDataField("tran_amt",(String)pvpLoanKColl.getDataValue("extension_amt"));  //交易金额
				authorizeKColl.addDataField("tran_date",this.getContext().getDataValue(CMISConstance.OPENDAY));  //交易日期
				authorizeKColl.addDataField("send_times","0");  //发送次数
				authorizeKColl.addDataField("return_code","");  //返回编码
				authorizeKColl.addDataField("return_desc","");  //返回说明
				authorizeKColl.addDataField("manager_br_id",(String)pvpLoanKColl.getDataValue("manager_br_id"));  //管理机构
				authorizeKColl.addDataField("in_acct_br_id",(String)ctrContKColl.getDataValue("in_acct_br_id"));  //入账机构
				authorizeKColl.addDataField("status","02");  //如果成功，则直接插表
				authorizeKColl.addDataField("authorize_no",authSerno);  //授权编号
				authorizeKColl.addDataField("cur_type",(String)pvpLoanKColl.getDataValue("fount_cur_type"));  //币种
				authorizeKColl.addDataField("base_acct_no",base_acct_no);  //贷款号
				authorizeKColl.addDataField("acct_seq_no",acct_seq_no);  //贷款发放流水
				authorizeKColl.addDataField("fldvalue01","GEN_GL_NO@"+authSerno);	//授权编号
				authorizeKColl.addDataField("fldvalue02","DUEBILL_NO@"+fount_bill_no);	//借据号
				authorizeKColl.addDataField("fldvalue03","EXPIRY_DATE@"+(String)pvpLoanKColl.getDataValue("extension_date").toString());	//到期日期
				authorizeKColl.addDataField("fldvalue04","INT_RATE_MODE@"+int_rate_mode);	//利率模式
				authorizeKColl.addDataField("fldvalue05","INT_FLT_RATE@"+TagUtil.replaceNull4Double(iqpLoanSubKColl.getDataValue("ir_float_rate")));	//新利率浮动比例
				authorizeKColl.addDataField("fldvalue06","BASE_INT_RATE_CODE@"+TagUtil.replaceNull4String(iqpLoanSubKColl.getDataValue("ruling_ir_code")));//利率类型,基准利率代码 
				authorizeKColl.addDataField("fldvalue07","BASE_INT_RATE@"+TagUtil.replaceNull4Double(iqpLoanSubKColl.getDataValue("ruling_ir")));	//基准利率
				authorizeKColl.addDataField("fldvalue08","SPREAD@"+"");	//利差
				authorizeKColl.addDataField("fldvalue09","ACT_INT_RATE@"+TagUtil.replaceNull4Double(pvpLoanKColl.getDataValue("extension_rate")));	//展期利率
				BigDecimal extension_rate = BigDecimalUtil.replaceNull(pvpLoanKColl.getDataValue("extension_rate"));
				BigDecimal default_rate = BigDecimalUtil.replaceNull(iqpLoanSubKColl.getDataValue("default_rate"));
				BigDecimal overdue_rate = BigDecimalUtil.replaceNull(iqpLoanSubKColl.getDataValue("overdue_rate"));
				authorizeKColl.addDataField("fldvalue10","LOAN_OD_FLT_RATE@"+TagUtil.replaceNull4Double(iqpLoanSubKColl.getDataValue("overdue_rate")));	//罚息利率浮动比：罚息利率同传逾期利率
				authorizeKColl.addDataField("fldvalue11","LOAN_OD_ACT_RATE@"+extension_rate.multiply(overdue_rate.add(new BigDecimal(1.00))));	//罚息执行利率：罚息利率同传逾期利率
				authorizeKColl.addDataField("fldvalue12","OVERDUE_FLT_RATE@"+TagUtil.replaceNull4Double(iqpLoanSubKColl.getDataValue("overdue_rate")));	//逾期利率浮动比例
				authorizeKColl.addDataField("fldvalue13","OVERDUE_ACT_RATE@"+extension_rate.multiply(overdue_rate.add(new BigDecimal(1.00))));	//逾期执行利率
				authorizeKColl.addDataField("fldvalue14","END_DATE@"+pvpLoanKColl.getDataValue("fount_end_date").toString().replace("-", ""));	//原始到期日
				dao.insert(authorizeKColl, connection);
				//发送展期接口至核心
				TradeDkzqsq tradeDkffsq=new TradeDkzqsq();
				//Context context=this.getContext();
				//Connection connection=this.getConnection();
				this.getContext().put("tran_serno", tranSerno);
				//this.getContext().put("sence_code", "02");
				//CompositeData reqCD = tradeDkffsq.doExecute(this.getContext(), connection);
				KeyedCollection reqCD = tradeDkffsq.doYmExcute(this.getContext(), connection);
				/** 打印后台发送日志 */
				//EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(reqCD), "UTF-8"));
				/** 执行发送操作 */
				//KeyedCollection reqKcoll=TagUtil.replaceCD2KColl(reqCD);
				KeyedCollection retCD = ESBUtil.sendEsbMsg((KeyedCollection)reqCD.getDataElement("SYS_HEAD"), (KeyedCollection)reqCD.getDataElement("BODY"));
				System.out.println("****************************************");
				//System.out.println(new String(PackUtil.pack(reqCD), "UTF-8"));
				//获取报文系统头
				KeyedCollection retKColl=(KeyedCollection)retCD.getDataElement("SYS_HEAD");
				//获取报文体
				KeyedCollection respBodyKColl = (KeyedCollection)retCD.getDataElement("BODY");
				
				IndexedCollection retArr=(IndexedCollection)retKColl.getDataElement("RetInfArry");
				KeyedCollection retObj=(KeyedCollection)retArr.get(0);

				if(!"000000".equals(retObj.getDataValue("RetCd"))){//成功
					throw new EMPException("贷款展期发送核心失败，错误码："+retObj.getDataValue("RetCd")+",错误信息："+retObj.getDataValue("RetInf"));
				}else{
					if(result!=null){
						String old_end_date = (String)result.getDataValue("end_date");
						String new_end_date = (String)pvpLoanKColl.getDataValue("extension_date");
						BigDecimal count = new BigDecimal(result.getDataValue("post_count").toString());
						int i = Integer.parseInt(count.toString());
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("end_date", old_end_date);//原到期日
						param.put("count", i+1);//展期次数
						param.put("new_end_date", new_end_date);//新到期日
						param.put("base_rate", iqpLoanSubKColl.getDataValue("ruling_ir"));//展期基准利率
						param.put("extension_rate", extension_rate);//展期利率
						param.put("overdue_rate_y", extension_rate.multiply(overdue_rate.add(new BigDecimal(1.00))));//展期后逾期利率
						param.put("default_rate_y", extension_rate.multiply(default_rate.add(new BigDecimal(1.00))));//展期后违约利率
						//更新贷款台账的原到期日，到期日，展期次数
						SqlClient.update("updateAccLoanForExtend", fount_bill_no, param, null, connection);
						//根据借据号更新授权表的状态为授权已确认
						Map<String,Object> paramAccLoan = new HashMap<String,Object>();
						paramAccLoan.put("bill_no", fount_bill_no);
						paramAccLoan.put("authorize_no", authSerno);
						SqlClient.update("updatePvpAuthorizeStatusByAuthNo", paramAccLoan, "04", null, connection);
					}
				}
				
				/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//				ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//				KeyedCollection retKColl = new KeyedCollection();
//				KeyedCollection kColl4trade = new KeyedCollection();
//				KeyedCollection iqp_kColl = new KeyedCollection();
//				String sqlserno = "select a.serno from iqp_extension_agr a where a.agr_no = '"+agr_no+"'";
//				iqp_kColl.addDataField("sql", sqlserno);
//				String iqpserno = catalogManaAgent.excuteSql("selectIqpExtensionAgr", iqp_kColl).getDataValue("result").toString();
//				
//				kColl4trade.put("CLIENT_NO", (String)pvpLoanKColl.getDataValue("cus_id"));
//				kColl4trade.put("BUSS_SEQ_NO", iqpserno);
//				kColl4trade.put("TASK_ID", "");
//				try{
//					retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), connection);	//调用影像锁定接口
//				}catch(Exception e){
//					throw new Exception("影像锁定接口失败!");
//				}
//				if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//					//交易失败信息
//					throw new Exception("影像锁定接口失败!");
//				}
				/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！错误描述："+e.getMessage());
		}
	
	}
	
	/**
	 * 转帖现出账流程审批通过
	 * 出账申请审批通过执行操作：（按照票据生成多条票据流水台账）
	 * 1.生成授权信息
	 * 2.生成票据台帐信息
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpRpddscnt(String serno)throws ComponentException {
		Connection connnection = null;
		try {

			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			connnection = this.getConnection();
			
			/** 1.数据准备：通过业务流水号查询【出账申请】 */
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, connnection);
			String prd_id = (String)pvpLoanKColl.getDataValue("prd_id");//产品编号
			String cus_id = (String)pvpLoanKColl.getDataValue("cus_id");//客户编码
			String cont_no = (String)pvpLoanKColl.getDataValue("cont_no");//合同编号
			String manager_br_id = (String)pvpLoanKColl.getDataValue("manager_br_id");//管理机构
			String in_acct_br_id = (String)pvpLoanKColl.getDataValue("in_acct_br_id");//入账机构
			String cur_type = (String)pvpLoanKColl.getDataValue("cur_type");//币种
			
			/** 2.通过客户编号查询【客户信息】 */
			String cus_name = "";
			String[] args=new String[] {"cus_id" };
			String[] modelIds=new String[]{"CusSameOrg"};
			String[]modelForeign=new String[]{"same_org_no"};
			String[] fieldName=new String[]{"same_org_cnname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(pvpLoanKColl, args, SystemTransUtils.ADD, this.getContext(), modelIds,modelForeign, fieldName);
			cus_name = (String) pvpLoanKColl.getDataValue("cus_id_displayname");
			
			/** 3.数据准备：获取票据批次号*/
			String condition = " where cont_no ='" + cont_no + "'";
			KeyedCollection iqpBatchKColl = dao.queryFirst("IqpBatchMng", null, condition, connnection);
			String batch_no = (String) iqpBatchKColl.getDataValue("batch_no");
			String biz_type = (String) iqpBatchKColl.getDataValue("biz_type");
			String iqpserno = (String) iqpBatchKColl.getDataValue("serno");
			
			/** 4.数据准备：通过获取票据批次下的票据明细关系 */
			KeyedCollection IqpRpddscntKColl = dao.queryDetail("IqpRpddscnt", iqpserno, this.getConnection());
			String relCondition = " where batch_no ='"+batch_no+"'";
			IndexedCollection relIColl = dao.queryList("IqpBatchBillRel", relCondition, connnection);
			
			String nowDate = (String)this.getContext().getDataValue(CMISConstance.OPENDAY);
			if(nowDate == null || nowDate.trim().length() == 0){
				throw new EMPException("获取系统营业时间失败！请检查系统营业时间配置是否正确！");
			}
			
			//生成授权编号，所有票据授权交易流水在一个授权编号下
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());
			
			
			/**数据准备，转贴申请信息*/
			KeyedCollection acctKColl = dao.queryDetail("IqpRpddscnt", iqpserno, connnection);
			
			/** 5.循环生成授权信息和台账信息*/
			for(int i=0;i<relIColl.size();i++){
				/** （1）获取借据编号*/
				PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
                .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), connnection);
				String billNo = cmisComponent.getBillNoByContNo(cont_no);
				
				KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
				
				KeyedCollection relKColl = (KeyedCollection) relIColl.get(i);
				String porder_no = (String) relKColl.getDataValue("porder_no");//票据号
				Map<String,String> incomeMap = new HashMap<String,String>();
				incomeMap.put("batch_no",batch_no);
				incomeMap.put("porder_no",porder_no);
				/**（2）收益表信息*/
				KeyedCollection incomeKColl = dao.queryDetail("IqpBillIncome", incomeMap, connnection);
				String drft_amt = (String) incomeKColl.getDataValue("drft_amt");
				String fore_disc_date = (String) incomeKColl.getDataValue("fore_disc_date");
				String disc_days = (String) incomeKColl.getDataValue("disc_days");
				String adj_days = (String) incomeKColl.getDataValue("adj_days");
				String disc_rate = (String) incomeKColl.getDataValue("disc_rate");
				String int_amt = (String) incomeKColl.getDataValue("int");
				String rebuy_date = (String) incomeKColl.getDataValue("fore_rebuy_date");
				BigDecimal rpay_amt = new BigDecimal(drft_amt).subtract(new BigDecimal(int_amt));
				String dscnt_int_pay_mode = "";
				if(incomeKColl.containsKey("dscnt_int_pay_mode")&&incomeKColl.getDataValue("dscnt_int_pay_mode")!=null&&!"".equals(incomeKColl.getDataValue("dscnt_int_pay_mode"))){
					dscnt_int_pay_mode = (String) incomeKColl.getDataValue("dscnt_int_pay_mode");
					if(dscnt_int_pay_mode.equals("1")){//卖方付息
						dscnt_int_pay_mode = "1";
					}else if(dscnt_int_pay_mode.equals("2")){//买方付息
						dscnt_int_pay_mode = "0";
					}else if(dscnt_int_pay_mode.equals("3")){//协议付息
						dscnt_int_pay_mode = "2";
					}
				}
				
				/**（3）票据明细表信息*/
				KeyedCollection billDetailKColl = dao.queryDetail("IqpBillDetail", porder_no, connnection);
				String porder_curr = (String) billDetailKColl.getDataValue("porder_curr");
				String is_ebill = (String) billDetailKColl.getDataValue("is_ebill");
				
				/** （4）生成交易流水号 */
				String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connnection, this.getContext());
				
				/** （5）给授权信息表赋值 */
				authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
				authorizeKColl.addDataField("serno", serno);//业务流水号（出账编号）
				authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
				authorizeKColl.addDataField("prd_id", prd_id);//产品编号
				authorizeKColl.addDataField("cus_id", cus_id);//客户编码--------------------确认使用的是行号
				authorizeKColl.addDataField("cus_name", cus_name);//客户名称
				authorizeKColl.addDataField("cont_no", cont_no);//合同编号
				authorizeKColl.addDataField("bill_no", billNo);//借据编号
				authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_TXFFSQ+TradeConstance.SERVICE_SCENE_TXFFSQ);//交易码
				authorizeKColl.addDataField("tran_amt", drft_amt);//交易金额
				//authorizeKColl.addDataField("tran_date", this.getContext().getDataValue(CMISConstance.OPENDAY));//交易日期取发送日期
				authorizeKColl.addDataField("tran_date", "");//交易日期取发送日期默认不赋值
				authorizeKColl.addDataField("send_times", "0");//发送次数
				authorizeKColl.addDataField("return_code", "");//返回编码
				authorizeKColl.addDataField("return_desc", "");//返回信息
				authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
				if(is_ebill!=null&&"1".equals(is_ebill)){
					authorizeKColl.addDataField("status", "05");//状态
				}else{
					authorizeKColl.addDataField("status", "00");//状态
				}
				authorizeKColl.addDataField("cur_type",cur_type);//币种
				
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编码
				if(biz_type.equals("01")||biz_type.equals("02")){
					//买入买断\买入返售
					authorizeKColl.addDataField("fldvalue02", "OPERATION_TYPE@"+"BUY");//操作类型
				}else if(biz_type.equals("03")||biz_type.equals("04")||biz_type.equals("06")){
					//卖出卖断\卖出回购\再贴现
					authorizeKColl.addDataField("fldvalue02", "OPERATION_TYPE@"+"SELL");//操作类型
				}else{
					//内部转贴现
					authorizeKColl.addDataField("fldvalue02", "OPERATION_TYPE@"+"TSFER");//操作类型
				}
				if(biz_type.equals("02")){
					authorizeKColl.addDataField("fldvalue03", "BACK_FLAG@"+"B");//回购/返售标识(买入返售B)
				}else if(biz_type.equals("04")){
					authorizeKColl.addDataField("fldvalue03", "BACK_FLAG@"+"S");//回购/返售标识(回购卖出S)
				}else{
					authorizeKColl.addDataField("fldvalue03", "BACK_FLAG@"+"N");//回购/返售标识
				}
				authorizeKColl.addDataField("fldvalue04", "INPUT_DATE@"+TagUtil.formatDate(IqpRpddscntKColl.getDataValue("input_date")));//填入日期
				authorizeKColl.addDataField("fldvalue05", "BANK_ID@"+TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue06", "DISCONT_AGREE_NO@"+cont_no);//贴现协议号
				authorizeKColl.addDataField("fldvalue07", "LOAN_TYPE@"+prd_id);//贷款种类
				authorizeKColl.addDataField("fldvalue08", "DUEBILL_NO@"+billNo);//借据号
				authorizeKColl.addDataField("fldvalue09", "BILL_NO@"+porder_no);//汇票号码
				authorizeKColl.addDataField("fldvalue10", "BILL_TYPE@"+this.TransSDicForESB("STD_DRFT_TYPE",(String)billDetailKColl.getDataValue("bill_type")));//票据类型
				authorizeKColl.addDataField("fldvalue11", "APPLYER_GLOBAL_TYPE@"+"");//贴现申请人证件类型
				authorizeKColl.addDataField("fldvalue12", "APPLYER_GLOBAL_ID@"+"");//贴现申请人证件号
				authorizeKColl.addDataField("fldvalue13", "APPLYER_ISS_CTRY@"+"");//贴现申请人发证国家
				authorizeKColl.addDataField("fldvalue14", "APPLYER_NAME@"+"");//贴现申请人名称
				authorizeKColl.addDataField("fldvalue15", "APPLYER_ACCT_NO@"+"");//贴现申请人户名
				authorizeKColl.addDataField("fldvalue16", "BILL_BRANCH_ID@"+in_acct_br_id);//借据机构代码（取入账机构）
				if(prd_id.equals("300024")){//再贴现
					authorizeKColl.addDataField("fldvalue17", "DISCOUNT_KIND@"+"REDS");//贴现种类:再贴现
				}else{//转贴现
					authorizeKColl.addDataField("fldvalue17", "DISCOUNT_KIND@"+"DISC");//贴现种类:转贴现
				}
				authorizeKColl.addDataField("fldvalue18", "DISCOUNT_CCY@"+billDetailKColl.getDataValue("porder_curr"));//币种
				authorizeKColl.addDataField("fldvalue19", "DISCOUNT_AMT@"+billDetailKColl.getDataValue("drft_amt"));//贴现金额
				authorizeKColl.addDataField("fldvalue20", "DISCOUNT_DAYS@"+disc_days);//贴现天数
				authorizeKColl.addDataField("fldvalue21", "DISCOUNT_RATE@"+"");//贴现利率
				authorizeKColl.addDataField("fldvalue22", "TRANSFER_DISCOUNT_RATE@"+disc_rate);//转贴现利率
				authorizeKColl.addDataField("fldvalue23", "TRANSFER_DISCOUNT_DATE@"+TagUtil.formatDate(fore_disc_date));//转贴现日期
				authorizeKColl.addDataField("fldvalue24", "DISCOUNT_INTEREST@"+int_amt);//贴现利息 
				authorizeKColl.addDataField("fldvalue25", "DISCOUNT_DATE@"+"");//贴现日期
				authorizeKColl.addDataField("fldvalue26", "BILL_ISSUE_DATE@"+TagUtil.formatDate(billDetailKColl.getDataValue("bill_isse_date")));//出票日期
				authorizeKColl.addDataField("fldvalue27", "BILL_EXPIRY_DATE@"+TagUtil.formatDate(billDetailKColl.getDataValue("porder_end_date")));//票据到期日期
				authorizeKColl.addDataField("fldvalue28", "RETURN_DATE@"+TagUtil.formatDate(iqpBatchKColl.getDataValue("rebuy_date")));//约定转回日期
				authorizeKColl.addDataField("fldvalue29", "EXPIRY_DATE@"+TagUtil.formatDate(billDetailKColl.getDataValue("porder_end_date")));//到期日期（同传票据到期日）
				authorizeKColl.addDataField("fldvalue30", "DEDUCT_METHOD@"+"AUTOPAY");//扣款方式
				authorizeKColl.addDataField("fldvalue31", "TO_BRANCH_ID@"+"");//对方机构代码（目前不传）
				
				/**（6）取票据下的付息账号信息*/
				String pintCondition = " where batch_no='"+batch_no+"' and porder_no = '"+porder_no+"'";
				IndexedCollection pintIColl = dao.queryList("IqpBillPintDetail", pintCondition, connnection);
				for(int k=0;k<pintIColl.size();k++){
					int d = k+1;//标识账户类型
					KeyedCollection temp = (KeyedCollection)pintIColl.get(k);
					String pint_no = (String)temp.getDataValue("pint_no");
					String pint_acct_name = (String)temp.getDataValue("pint_acct_name");
					String ccy = (String)temp.getDataValue("cur_type");
					String acc_type = (String)temp.getDataValue("acc_type");
					String acct_gl_code = (String)temp.getDataValue("acct_gl_code");
					String pint_amt = (String)temp.getDataValue("pint_amt");
					String acctsvcr = (String)temp.getDataValue("acctsvcr");
					String acctsvcrnm = (String)temp.getDataValue("acctsvcrnm");
					String interbank_id = TagUtil.replaceNull4String(temp.getDataValue("interbank_id"));//银联行号
					
					/** 8.生成授权信息：账户信息（取自IQP_BILL_PINT_DETAIL表）*/
					KeyedCollection pvpAcctKCollAcc = new KeyedCollection(AUTHORIZESUBMODEL);
					pvpAcctKCollAcc.addDataField("auth_no", authSerno);
					pvpAcctKCollAcc.addDataField("busi_cls", "03");//账号信息
					pvpAcctKCollAcc.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编号
					pvpAcctKCollAcc.addDataField("fldvalue02", "DUEBILL_NO@"+"");//借据号（无意义，暂定不传）
					pvpAcctKCollAcc.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "PAYM"+d);//系统账户类型
					pvpAcctKCollAcc.addDataField("fldvalue04", "ACCT_NO@"+pint_no);//贴现人结算账户
					pvpAcctKCollAcc.addDataField("fldvalue05", "CCY@"+ccy);//账户币种
					pvpAcctKCollAcc.addDataField("fldvalue06", "BANK_ID@"+interbank_id);//帐号银行代码
					pvpAcctKCollAcc.addDataField("fldvalue07", "BRANCH_ID@"+acctsvcr);//帐号机构代码
					pvpAcctKCollAcc.addDataField("fldvalue08", "ACCT_NAME@"+pint_acct_name);//户名
					pvpAcctKCollAcc.addDataField("fldvalue09", "ISS_CTRY@"+"CN");//发证国家
					pvpAcctKCollAcc.addDataField("fldvalue10", "ACCT_TYPE@" + acc_type);//账户类型
					pvpAcctKCollAcc.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（非必输）
					pvpAcctKCollAcc.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（非必输）
					pvpAcctKCollAcc.addDataField("fldvalue13", "REMARK@" + "");//备注
					pvpAcctKCollAcc.addDataField("fldvalue14", "CARD_NO@"+"");//卡号
					pvpAcctKCollAcc.addDataField("fldvalue15", "CA_TT_FLAG@"+"");//钞汇标志
					pvpAcctKCollAcc.addDataField("fldvalue16", "ACCT_CODE@"+"");//账户代码
					pvpAcctKCollAcc.addDataField("fldvalue17", "ACCT_GL_CODE@"+acct_gl_code);//账号科目代码
					pvpAcctKCollAcc.addDataField("fldvalue18", "BALANCE@"+"");//账户余额
					pvpAcctKCollAcc.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@"+pint_amt);//此交易字段存储为付息金额
					pvpAcctKCollAcc.addDataField("fldvalue20", "BANK_NAME@"+acctsvcrnm);//开户行行名
					pvpAcctKCollAcc.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
					
					//开户行地址
					if(interbank_id!=null&&!"".equals(interbank_id)){
						KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, connnection);
						if(kColl!=null){
							pvpAcctKCollAcc.addDataField("fldvalue23", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
						}else{
							pvpAcctKCollAcc.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
						}
					}else{
						pvpAcctKCollAcc.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
					}
					
					dao.insert(pvpAcctKCollAcc, connnection);
				}
				authorizeKColl.addDataField("fldvalue36", "BRANCH_ID@"+in_acct_br_id);//取入账机构
				authorizeKColl.addDataField("fldvalue37", "DSCNT_INT_PAY_MODE@"+dscnt_int_pay_mode);//转贴现付息方式
				authorizeKColl.addDataField("fldvalue38", "AGENTOR_NAME@"+"");//代理人名称，转贴没有代理类型
				authorizeKColl.addDataField("fldvalue39", "AGENTOR_ACCT_NO@"+"");//代理人账号
				authorizeKColl.addDataField("fldvalue40", "AGENT_ORG_NO@"+"");//代理人开户行行号
				authorizeKColl.addDataField("fldvalue41", "AGENT_ORG_NAME@"+"");//代理人开户行行名
				if(biz_type.equals("05")){
					//内转，即转给财务部，填财务部机构号
					authorizeKColl.addDataField("fldvalue42", "COUNTER_OPEN_BRANCH_ID@"+PvpConstant.NZ_BRANCH_ID);//对手行行号
					authorizeKColl.addDataField("fldvalue43", "COUNTER_OPEN_BRANCH_NAME@"+PvpConstant.NZ_BRANCH_NAME);//对手行行名
				}else{
					authorizeKColl.addDataField("fldvalue42", "COUNTER_OPEN_BRANCH_ID@"+acctKColl.getDataValue("toorg_no"));//对手行行号
					authorizeKColl.addDataField("fldvalue43", "COUNTER_OPEN_BRANCH_NAME@"+acctKColl.getDataValue("toorg_name"));//对手行行名
				}
				String bill_type = (String) billDetailKColl.getDataValue("bill_type");
				if("100".equals(bill_type)){
					//银票
					authorizeKColl.addDataField("fldvalue45", "AA_NAME@"+"");//承兑人名称
					authorizeKColl.addDataField("fldvalue46", "AAORG_NO@"+TagUtil.replaceNull4String(billDetailKColl.getDataValue("aorg_no")));//承兑行行号
					authorizeKColl.addDataField("fldvalue47", "AAORG_NAME@"+TagUtil.replaceNull4String(billDetailKColl.getDataValue("aorg_name")));//承兑行名称
				}else{
					//商票
					authorizeKColl.addDataField("fldvalue45", "AA_NAME@"+"");//承兑人名称
					authorizeKColl.addDataField("fldvalue46", "AAORG_NO@"+TagUtil.replaceNull4String(billDetailKColl.getDataValue("aaorg_no")));//承兑人开户行行号
					authorizeKColl.addDataField("fldvalue47", "AAORG_NAME@"+TagUtil.replaceNull4String(billDetailKColl.getDataValue("aaorg_name")));//承兑人开户行名称
				}
				authorizeKColl.addDataField("fldvalue48", "PAY_AMT@"+BigDecimalUtil.replaceNull(""));//实付金额
				authorizeKColl.addDataField("fldvalue49", "ARRANGR_DEDUCT_OPT@"+BigDecimalUtil.replaceNull(""));//预扣款项
				authorizeKColl.addDataField("fldvalue50", "APPLYER_ID@"+cus_id);//贴现申请人编号
				authorizeKColl.addDataField("fldvalue51", "E_CDE@" + is_ebill);//是否电票
				authorizeKColl.addDataField("fldvalue52", "BATCH_NO@" + batch_no);//批次号
				authorizeKColl.addDataField("fldvalue53", "CUST_TYP@" + "BL400");//客户类型取所属条线
				authorizeKColl.addDataField("fldvalue54", "DISC_OD_INT_RATE@" + "");//逾期利率
				authorizeKColl.addDataField("fldvalue55", "BILL_APP_NAME@" + TagUtil.replaceNull4String(billDetailKColl.getDataValue("isse_name")));//出票人名称
				authorizeKColl.addDataField("fldvalue56", "ACP_BANK_ACCT_NO@" + TagUtil.replaceNull4String(billDetailKColl.getDataValue("aaorg_acct_no")));//承兑人开户行账号
				authorizeKColl.addDataField("fldvalue57", "DSCNT_TYPE@" + TagUtil.replaceNull4String(biz_type));//贴现方式
				authorizeKColl.addDataField("fldvalue58", "BILL_ACCT_NO@" + TagUtil.replaceNull4String(billDetailKColl.getDataValue("daorg_acct")));//出票人账号
				authorizeKColl.addDataField("fldvalue59", "BILL_OPEN_BRANCH_ID@" + TagUtil.replaceNull4String(billDetailKColl.getDataValue("daorg_no")));//出票人开户行行号 
				authorizeKColl.addDataField("fldvalue60", "BILL_OPEN_BRANCH_NAME@" + TagUtil.replaceNull4String(billDetailKColl.getDataValue("daorg_name")));//出票人开户行行名
				
				dao.insert(authorizeKColl, this.getConnection());
				
				/** 7.生成授权信息：账户信息（取自IqpRpddscnt转贴申请表）*/
//				KeyedCollection pvpAcctKColl = new KeyedCollection(AUTHORIZESUBMODEL);
//				pvpAcctKColl.addDataField("auth_no", authSerno);
//				pvpAcctKColl.addDataField("busi_cls", "03");//账号信息
//				pvpAcctKColl.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编号
//				pvpAcctKColl.addDataField("fldvalue02", "DUEBILL_NO@"+billNo);//借据号（对应多笔，确认不传）
//				pvpAcctKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@"+"ACTV");//系统账户类型
//				if(biz_type.equals("03")||biz_type.equals("04")||biz_type.equals("05")||biz_type.equals("06")){
//					//卖出卖断\卖出回购\内部转贴现\再贴现，取本行账户
//					pvpAcctKColl.addDataField("fldvalue04", "ACCT_NO@"+acctKColl.getDataValue("this_acct_no"));
//				}else{
//					//买入买断\买入返售，取交易对手行账户
//				pvpAcctKColl.addDataField("fldvalue04", "ACCT_NO@"+acctKColl.getDataValue("topp_acct_no"));//账号：默认全部传对手行账户信息
//				}
//				pvpAcctKColl.addDataField("fldvalue05", "CCY@"+"CNY");//账户币种：默认人民币
//				pvpAcctKColl.addDataField("fldvalue06", "BANK_ID@"+TradeConstance.BANK_ID);//帐号银行代码
//				pvpAcctKColl.addDataField("fldvalue07", "BRANCH_ID@"+"");//帐号机构代码
//				if(biz_type.equals("03")||biz_type.equals("04")||biz_type.equals("05")||biz_type.equals("06")){
//					//卖出卖断\卖出回购\内部转贴现\再贴现，取本行账户
//					pvpAcctKColl.addDataField("fldvalue08", "ACCT_NAME@"+acctKColl.getDataValue("this_acct_name"));//户名
//				}else{
//					//买入买断\买入返售，取交易对手行账户
//				pvpAcctKColl.addDataField("fldvalue08", "ACCT_NAME@"+acctKColl.getDataValue("topp_acct_name"));//户名：默认全部传对手行账户信息
//				}
//				pvpAcctKColl.addDataField("fldvalue09", "ISS_CTRY@"+"CN");//发证国家
//				pvpAcctKColl.addDataField("fldvalue10", "ACCT_TYPE@"+"");//账户类型
//				pvpAcctKColl.addDataField("fldvalue11", "GLOBAL_TYPE@"+"");//证件类型（非必输）
//				pvpAcctKColl.addDataField("fldvalue12", "GLOBAL_ID@"+"");//证件号码（非必输）
//				pvpAcctKColl.addDataField("fldvalue13", "REMARK@"+"");//备注
//				pvpAcctKColl.addDataField("fldvalue14", "CARD_NO@"+"");//卡号
//				pvpAcctKColl.addDataField("fldvalue15", "CA_TT_FLAG@"+"");//钞汇标志
//				pvpAcctKColl.addDataField("fldvalue16", "ACCT_CODE@"+"");//账户代码
//				pvpAcctKColl.addDataField("fldvalue17", "ACCT_GL_CODE@"+"");//账号科目代码
//				pvpAcctKColl.addDataField("fldvalue18", "BALANCE@"+"");//账户余额
//				pvpAcctKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@"+"");//受托支付发放金额
//				pvpAcctKColl.addDataField("fldvalue20", "BANK_NAME@"+"");//银行名称
//				pvpAcctKColl.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
//				pvpAcctKColl.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
//				
//				//开户行地址
//				pvpAcctKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//是否本行
//				
//				dao.insert(pvpAcctKColl, connnection);
				
				/**（7）生成台账信息*/
				KeyedCollection accDrftKColl = new KeyedCollection("AccDrft");//票据流水台账表
				accDrftKColl.addDataField("serno", serno);//业务编号	
				accDrftKColl.addDataField("acc_day", nowDate);//日期
				accDrftKColl.addDataField("acc_year", nowDate.substring(0, 4));//年份
				accDrftKColl.addDataField("acc_mon", nowDate.substring(5, 7));//月份
				accDrftKColl.addDataField("prd_id", prd_id);//产品编号
				accDrftKColl.addDataField("cont_no", cont_no);//合同编号
				accDrftKColl.addDataField("bill_no", billNo);//借据编号
				accDrftKColl.addDataField("dscnt_type", biz_type);//贴现方式
				accDrftKColl.addDataField("porder_no", porder_no);//汇票号码
				accDrftKColl.addDataField("discount_per", cus_id );//贴现人/交易对手--------------------确认使用的是行号
				accDrftKColl.addDataField("dscnt_date", fore_disc_date);//贴现日
				accDrftKColl.addDataField("dscnt_day", disc_days);//贴现天数
				accDrftKColl.addDataField("adjust_day", adj_days);//调整天数
				accDrftKColl.addDataField("dscnt_rate", disc_rate);//贴现利率
				accDrftKColl.addDataField("cur_type", porder_curr);//交易币种
				accDrftKColl.addDataField("dscnt_int", int_amt);//贴现利息
				accDrftKColl.addDataField("rpay_amt", rpay_amt);//实付金额
				accDrftKColl.addDataField("rebuy_date", rebuy_date);//回购日期
				accDrftKColl.addDataField("rebuy_day", "");//回购天数
				accDrftKColl.addDataField("rebuy_rate", "");//回购利率
				accDrftKColl.addDataField("overdue_rebuy_rate", "");//逾期回购利率
				accDrftKColl.addDataField("rebuy_int", "");//回购利息
				accDrftKColl.addDataField("separate_date", "");//清分日期
				accDrftKColl.addDataField("writeoff_date", "");//核销日期
				accDrftKColl.addDataField("five_class", "10");//五级分类
				accDrftKColl.addDataField("twelve_cls_flg", "");//十二级分类标志
				accDrftKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				accDrftKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
				accDrftKColl.addDataField("accp_status", "0");//台账状态：出帐未确认
				dao.insert(accDrftKColl, this.getConnection());
			}
			
//			/** 6.生成授权信息：费用信息*/
//			String conditionFee = "where serno='"+iqpserno+"'";
//			IndexedCollection iqpAppendTermsIColl = dao.queryList(IQPFEE, conditionFee, this.getConnection());
//			for(int i=0;i<iqpAppendTermsIColl.size();i++){				
//				KeyedCollection iqpAppendTermsKColl = (KeyedCollection)iqpAppendTermsIColl.get(i);
//				String fee_code = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_code"));//费用代码
//				String fee_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_cur_type"));//币种
//				Double fee_amt = TagUtil.replaceNull4Double(iqpAppendTermsKColl.getDataValue("fee_amt"));//费用金额
//				String fee_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_type"));//费用类型
//				String fee_rate = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_rate"));//费用比率
//				String is_cycle_chrg = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_cycle_chrg"));//是否周期性收费标识 
//				String chrg_date = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_date"));//收费日期
//				String chrg_freq = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_freq"));//账号
//				if(chrg_freq.equals("Y")){
//					chrg_freq = "12";
//				}else if(chrg_freq.equals("Q")){
//					chrg_freq = "3";
//				}else if(chrg_freq.equals("M")){
//					chrg_freq = "1";
//				}else{
//					chrg_freq = "";
//				}
//				String acct_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("acct_no"));//账号
//				String acct_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("acct_name"));//账户名
//				String opac_org_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opac_org_no"));//开户行行号
//				String opan_org_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opan_org_name"));//开户行行名
//				String acct_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("cur_type"));//账户币种
//				String is_this_org_acct = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_this_org_acct"));//是否本行账户
//				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
//				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
//				authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
//				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
//				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + "");//借据号：不需赋值
//				authorizeSubKColl.addDataField("fldvalue03", "FEE_CODE@" + fee_code);//费用代码				
//				authorizeSubKColl.addDataField("fldvalue04", "CCY@" + fee_cur_type);//币种
//				authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
//			    authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + TradeConstance.is_pay_flag);//收付标志 默认传 R: 收
//				authorizeSubKColl.addDataField("fldvalue07", "FEE_TYPE@" + fee_type);//费用类型
//				authorizeSubKColl.addDataField("fldvalue08", "BASE_AMOUNT@" + fee_amt);//基准金额 同费用金额
//				authorizeSubKColl.addDataField("fldvalue09", "FEE_RATE@" + fee_rate);//费用比率
//				authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + TradeConstance.acct_type);//扣款账户类型  默认传 FEE:收费账户			
//				authorizeSubKColl.addDataField("fldvalue11", "CHARGE_PERIODICITY_FLAG@" + this.TransSDicForESB("STD_ZX_YES_NO",is_cycle_chrg));//是否周期性收费标识
//				authorizeSubKColl.addDataField("fldvalue12", "CHARGE_DATE@" + chrg_date);//收费日期
//				authorizeSubKColl.addDataField("fldvalue13", "AMOR_AMT@" + fee_amt);//摊销金额
//				authorizeSubKColl.addDataField("fldvalue14", "CONTRACT_NO@" + cont_no);//协议号
//				authorizeSubKColl.addDataField("fldvalue15", "ACCT_NO@" + acct_no);//账号
//				authorizeSubKColl.addDataField("fldvalue16", "ACCT_NAME@" + acct_name);//账户名
//				authorizeSubKColl.addDataField("fldvalue17", "OPAC_ORG_NO@" + opac_org_no);//开户行行号
//				authorizeSubKColl.addDataField("fldvalue18", "OPAN_ORG_NAME@" + opan_org_name);//开户行行名
//				authorizeSubKColl.addDataField("fldvalue19", "ACCT_CCY@" + acct_cur_type);//账户币种
//				authorizeSubKColl.addDataField("fldvalue20", "IS_THIS_ORG_ACCT@" + is_this_org_acct);//是否本行账户
//				authorizeSubKColl.addDataField("fldvalue21", "BRANCH_ID@" + in_acct_br_id);//入账机构
//				authorizeSubKColl.addDataField("fldvalue22", "FEE_SPAN@" + chrg_freq);//收费间隔
//				dao.insert(authorizeSubKColl, this.getConnection());
//			}
			
			/**8.生成授信信息：保证金信息**/
			KeyedCollection ctrContKColl =  dao.queryDetail(CTRCONTMODEL, cont_no, this.getConnection());
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 start
			BigDecimal security_rate = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("security_rate"));//保证金比例
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 end
			Double security_amt = TagUtil.replaceNull4Double(ctrContKColl.getDataValue("security_amt"));//保证金金额
			String bailCondition = " where serno = '"+iqpserno+"'";
			IndexedCollection bailIColl = dao.queryList("PubBailInfo", bailCondition, connnection);
			for(int i=0;i<bailIColl.size();i++){
				KeyedCollection iqpBailInfoKColl = (KeyedCollection)bailIColl.get(i);
				String bail_acct_no = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_no"));//保证金账号
				String bail_cur_type = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("cur_type"));//币种
				String bail_acct_name = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_name"));//保证金账号名称
				String open_org = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("open_org"));//开户机构
				String bail_acct_gl_code = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_gl_code"));//科目号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				/** 给授权从表赋值 */
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "04");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + "");//借据号：不需赋值
				authorizeSubKColl.addDataField("fldvalue03", "BRANCH_ID@" + open_org);//机构代码
				authorizeSubKColl.addDataField("fldvalue04", "GUARANTEE_ACCT_NO@" + bail_acct_no);//保证金账号
				authorizeSubKColl.addDataField("fldvalue05", "GUARANTEE_NO@" + "");//保证金编号：不需赋值
				authorizeSubKColl.addDataField("fldvalue06", "ACCT_TYPE@" + "");//账户类型：不需赋值
				authorizeSubKColl.addDataField("fldvalue07", "GUARANTEE_EXPIRY_DATE@" + "");//保证金到期日：不需赋值
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_CODE@" + "");//账户代码：不需赋值
				authorizeSubKColl.addDataField("fldvalue09", "CA_TT_FLAG@" + "");//钞汇标志：不需赋值
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_GL_CODE@" + bail_acct_gl_code);//账号科目代码：不需赋值
				authorizeSubKColl.addDataField("fldvalue11", "ACCT_NAME@" + bail_acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue12", "CCY@" + bail_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue13", "GUARANTEE_PER@" + security_rate);//保证金比例
				authorizeSubKColl.addDataField("fldvalue14", "GUARANTEE_AMT@" + security_amt);//保证金金额
				authorizeSubKColl.addDataField("fldvalue15", "CONTRACT_NO@" + cont_no);//协议号
				authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "N");//是否准全额

				dao.insert(authorizeSubKColl, this.getConnection());
			  }
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl4trade = new KeyedCollection();
//			kColl4trade.put("CLIENT_NO", cus_id);
//			kColl4trade.put("BUSS_SEQ_NO", iqpserno);
//			kColl4trade.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), this.getConnection());	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常！错误描述："+e.getMessage());
		}
	
	}
	
	
	/**
	 * 委托贷款出账流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpCsgnLoan(String serno)throws ComponentException {
		
		try {
			this.doWfAgreeForIqpLoan(serno);
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 国结贷款出账流程审批通过，判断是否使用额度合同，
	 * 如果使用额度合同则自动生成合同、出账、台帐、授权信息等。
	 * 非额度合同下沿用贷款流程
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpTfLoan(String serno) throws ComponentException {
		
		try {
			this.doWfAgreeForIqpLoan(serno);
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 贸易融资福费廷贷款出账流程审批通过
	 * 出账申请审批通过执行操作
	 * 1.生成授权信息
	 * 2.生成台帐信息
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpTfLoanForFft(String serno)throws ComponentException {
		Connection connnection = null;
		try {
			
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			connnection = this.getConnection();
			
			/** 1.数据准备：通过业务流水号查询【出账申请】 */
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, connnection);
			String prd_id = (String)pvpLoanKColl.getDataValue("prd_id");//产品编号
			String cus_id = (String)pvpLoanKColl.getDataValue("cus_id");//客户编码
			String cont_no = (String)pvpLoanKColl.getDataValue("cont_no");//合同编号
			String manager_br_id = (String)pvpLoanKColl.getDataValue("manager_br_id");//管理机构
			String in_acct_br_id = (String)pvpLoanKColl.getDataValue("in_acct_br_id");//入账机构
			String cur_type = (String)pvpLoanKColl.getDataValue("cur_type");//币种
			BigDecimal pvp_amt = BigDecimalUtil.replaceNull(pvpLoanKColl.getDataValue("pvp_amt"));//出账金额
			String Pvpserno = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("serno"));//授权交易流水号
			
			/** 核算与信贷业务品种映射 START */
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			String lmPrdId = service.getPrdBasicCLPM2LM(cont_no, prd_id, this.getContext(), this.getConnection());
			/** 核算与信贷业务品种映射 END */
			
            /** 2.数据准备：通过客户编号查询【客户信息】 */
			String cus_name = "";
			String cert_type = "";
			String cert_code = "";
			String belg_line = "";
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			try {
				CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
				CusBase cusBase = csi.getCusBaseByCusId(cus_id,this.getContext(),this.getConnection());
				cus_name = cusBase.getCusName();
				cert_type = cusBase.getCertType();
				cert_code = cusBase.getCertCode();
				belg_line = TagUtil.replaceNull4String(cusBase.getBelgLine());//所属条线
			} catch (Exception e) {
				e.printStackTrace();
				throw new EMPException("获取组织机构模块失败！");
			}
			
			/** 3.数据准备：通过业务流水号查询【合同信息】 */
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, cont_no, connnection);
			String five_classfiy = (String)ctrContSubKColl.getDataValue("five_classfiy");//五级分类
			String term_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("term_type"));//期限类型
			Integer cont_term = TagUtil.replaceNull4Int(ctrContSubKColl.getDataValue("cont_term"));//合同期限
			String ruling_ir = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir"));//基准利率（年）
			String reality_ir_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("reality_ir_y"));//执行利率（年）
			String overdue_rate_y_iqp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("overdue_rate_y"));//逾期利率
			String default_rate_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("default_rate_y"));//违约利率
			KeyedCollection ctrKColl = dao.queryDetail(CTRCONTMODEL, cont_no, connnection);
			String assure_main = TagUtil.replaceNull4String(ctrKColl.getDataValue("assure_main"));//担保方式
			String iqpserno = (String) ctrKColl.getDataValue("serno");
			BigDecimal cont_amt = BigDecimalUtil.replaceNull(ctrKColl.getDataValue("cont_amt"));
			KeyedCollection iqpLoanKColl = dao.queryDetail(IQPLOANAPPMODEL, iqpserno, connnection);
			
			/**4.数据准备：获取从表票据信息*/
			String condition = " where serno ='" + iqpserno + "'";
			KeyedCollection iqpForftinKColl = dao.queryDetail("IqpForftin", iqpserno, connnection);
			String porder_no = (String) iqpForftinKColl.getDataValue("porder_no");
			String drftAmt = (String) iqpForftinKColl.getDataValue("drft_amt");
			String payAmt = (String) iqpForftinKColl.getDataValue("pay_amt");
			String arrangr_deduct_opt = (String) iqpForftinKColl.getDataValue("arrangr_deduct_opt");
			
			String nowDate = (String)this.getContext().getDataValue(CMISConstance.OPENDAY);
			if(nowDate == null || nowDate.trim().length() == 0){
				throw new EMPException("获取系统营业时间失败！请检查系统营业时间配置是否正确！");
			}
			
			//生成授权编号，所有票据授权交易流水在一个授权编号下
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());
			
			/** 6.循环生成授权信息和台账信息*/
			Map<String, String> acctmap = new HashMap<String, String>();//定义一个账号对应map
			/** （1）获取借据编号*/
			PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
            .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), connnection);
			String billNo = cmisComponent.getBillNoByContNo(cont_no);
			String end_date = DateUtils.getEndDate(term_type, nowDate, cont_term);//借据到期日
			
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			
			/**（4）生成交易流水号 */
			String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connnection, this.getContext());
			
			/**（5）给授权信息表赋值 */
			authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
			authorizeKColl.addDataField("serno", serno);//业务流水号（出账编号）
			authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
			authorizeKColl.addDataField("prd_id", prd_id);//产品编号
			authorizeKColl.addDataField("cus_id", cus_id);//客户编码
			authorizeKColl.addDataField("cus_name", cus_name);//客户名称
			authorizeKColl.addDataField("cont_no", cont_no);//合同编号
			authorizeKColl.addDataField("bill_no", billNo);//借据编号
			authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_TXFFSQ+TradeConstance.SERVICE_SCENE_TXFFSQ);//交易码+场景
			authorizeKColl.addDataField("tran_amt", pvp_amt);//交易金额
			authorizeKColl.addDataField("tran_date", nowDate);//交易日期取发送日期默认不赋值
			authorizeKColl.addDataField("send_times", "0");//发送次数
			authorizeKColl.addDataField("return_code", "");//返回编码
			authorizeKColl.addDataField("return_desc", "");//返回信息
			authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
			authorizeKColl.addDataField("status", "00");//状态
			authorizeKColl.addDataField("cur_type", cur_type);//币种
			
			authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编码
			authorizeKColl.addDataField("fldvalue02", "OPERATION_TYPE@"+"BUY");//操作类型（直贴，默认为买入）
			authorizeKColl.addDataField("fldvalue03", "BACK_FLAG@"+"N");//回购/返售标识（直贴，默认为否“N”）·
			authorizeKColl.addDataField("fldvalue04", "INPUT_DATE@"+TagUtil.formatDate(iqpLoanKColl.getDataValue("apply_date")));//填入日期
			authorizeKColl.addDataField("fldvalue05", "BANK_ID@"+TradeConstance.BANK_ID);//银行代码
			authorizeKColl.addDataField("fldvalue06", "DISCONT_AGREE_NO@"+cont_no);//贴现协议号
			authorizeKColl.addDataField("fldvalue07", "LOAN_TYPE@"+lmPrdId);//贷款种类
			authorizeKColl.addDataField("fldvalue08", "DUEBILL_NO@"+billNo);//借据号
			authorizeKColl.addDataField("fldvalue09", "BILL_NO@"+porder_no);//汇票号码
			authorizeKColl.addDataField("fldvalue10", "BILL_TYPE@"+"");//票据类型
			authorizeKColl.addDataField("fldvalue11", "APPLYER_GLOBAL_TYPE@"+cert_type);//贴现申请人证件类型
			authorizeKColl.addDataField("fldvalue12", "APPLYER_GLOBAL_ID@"+cert_code);//贴现申请人证件号
			authorizeKColl.addDataField("fldvalue13", "APPLYER_ISS_CTRY@"+"CN");//贴现申请人发证国家
			authorizeKColl.addDataField("fldvalue14", "APPLYER_NAME@"+cus_name);//贴现申请人名称
			authorizeKColl.addDataField("fldvalue15", "APPLYER_ACCT_NO@"+"");//贴现申请人户名（目前不传）
			authorizeKColl.addDataField("fldvalue16", "BILL_BRANCH_ID@"+in_acct_br_id);//借据机构代码（取入账机构）
			authorizeKColl.addDataField("fldvalue17", "DISCOUNT_KIND@"+"NORM");//贴现种类:普通贴现
			authorizeKColl.addDataField("fldvalue18", "DISCOUNT_CCY@"+ctrKColl.getDataValue("cont_cur_type"));//币种
			authorizeKColl.addDataField("fldvalue19", "DISCOUNT_AMT@"+ctrKColl.getDataValue("cont_amt"));//贴现金额
			authorizeKColl.addDataField("fldvalue20", "DISCOUNT_DAYS@"+iqpForftinKColl.getDataValue("disc_day"));//贴现天数
			authorizeKColl.addDataField("fldvalue21", "DISCOUNT_RATE@"+BigDecimalUtil.replaceNull(reality_ir_y));//贴现利率
			authorizeKColl.addDataField("fldvalue22", "TRANSFER_DISCOUNT_RATE@"+"");//转贴现利率
			authorizeKColl.addDataField("fldvalue23", "TRANSFER_DISCOUNT_DATE@"+"");//转贴现日期
			authorizeKColl.addDataField("fldvalue24", "DISCOUNT_INTEREST@"+(cont_amt
					.subtract(BigDecimalUtil.replaceNull(payAmt))).subtract(BigDecimalUtil.replaceNull(arrangr_deduct_opt)));//贴现利息 =合同金额-实付金额-预扣款项
			//核算确认，日期没有填当前日期
			String disc_date = (String) iqpForftinKColl.getDataValue("disc_date");
			if(disc_date!=null&&!"".equals(disc_date)){
				authorizeKColl.addDataField("fldvalue25", "DISCOUNT_DATE@"+TagUtil.formatDate(disc_date));//贴现日期
			}else{
				authorizeKColl.addDataField("fldvalue25", "DISCOUNT_DATE@"+TagUtil.formatDate(nowDate));//贴现日期
			}
			String issue_date = (String) iqpForftinKColl.getDataValue("issue_date");
			if(issue_date!=null&&!"".equals(issue_date)){
				authorizeKColl.addDataField("fldvalue26", "BILL_ISSUE_DATE@"+TagUtil.formatDate(issue_date));//出票日期
			}else{
				authorizeKColl.addDataField("fldvalue26", "BILL_ISSUE_DATE@"+TagUtil.formatDate(nowDate));//出票日期
			}
			String bill_end_date = (String) iqpForftinKColl.getDataValue("bill_end_date");
			if(bill_end_date!=null&&!"".equals(bill_end_date)){
				authorizeKColl.addDataField("fldvalue27", "BILL_EXPIRY_DATE@"+TagUtil.formatDate(bill_end_date));//票据到期日期
			}else{
				authorizeKColl.addDataField("fldvalue27", "BILL_EXPIRY_DATE@"+TagUtil.formatDate(nowDate));//票据到期日期
			}
			authorizeKColl.addDataField("fldvalue28", "RETURN_DATE@"+"");//约定转回日期
			authorizeKColl.addDataField("fldvalue29", "EXPIRY_DATE@"+TagUtil.formatDate(end_date));//到期日期
			authorizeKColl.addDataField("fldvalue30", "DEDUCT_METHOD@"+"AUTOPAY");//扣款方式
			authorizeKColl.addDataField("fldvalue31", "TO_BRANCH_ID@"+"");//对方机构代码（目前不传）
			authorizeKColl.addDataField("fldvalue37", "DSCNT_INT_PAY_MODE@"+"");//贴现付息方式
			authorizeKColl.addDataField("fldvalue38", "BRANCH_ID@"+in_acct_br_id);//取入账机构
			authorizeKColl.addDataField("fldvalue39", "AGENTOR_NAME@"+"");//代理人名称
			authorizeKColl.addDataField("fldvalue40", "AGENTOR_ACCT_NO@"+"");//代理人账号
			authorizeKColl.addDataField("fldvalue41", "AGENT_ORG_NO@"+"");//代理人开户行行号
			authorizeKColl.addDataField("fldvalue42", "AGENT_ORG_NAME@"+"");//代理人开户行行名
			authorizeKColl.addDataField("fldvalue43", "COUNTER_OPEN_BRANCH_ID@"+"");//对手行行号
			authorizeKColl.addDataField("fldvalue44", "COUNTER_OPEN_BRANCH_NAME@"+"");//对手行行名
			authorizeKColl.addDataField("fldvalue45", "AA_NAME@"+iqpForftinKColl.getDataValue("accptr_name"));//承兑人名称
			authorizeKColl.addDataField("fldvalue46", "AAORG_NO@"+"");//承兑行行号
			authorizeKColl.addDataField("fldvalue47", "AAORG_NAME@"+"");//承兑行名称
			authorizeKColl.addDataField("fldvalue48", "PAY_AMT@"+BigDecimalUtil.replaceNull(iqpForftinKColl.getDataValue("pay_amt")));//实付金额
			authorizeKColl.addDataField("fldvalue49", "ARRANGR_DEDUCT_OPT@"+BigDecimalUtil.replaceNull(iqpForftinKColl.getDataValue("arrangr_deduct_opt")));//预扣款项
			authorizeKColl.addDataField("fldvalue50", "APPLYER_ID@"+cus_id);//贴现申请人编号
			authorizeKColl.addDataField("fldvalue51", "E_CDE@" + "");//是否电票
			authorizeKColl.addDataField("fldvalue52", "BATCH_NO@" + "");//批次号
			authorizeKColl.addDataField("fldvalue53", "CUST_TYP@" + belg_line);//客户类型取所属条线
			authorizeKColl.addDataField("fldvalue54", "DISC_OD_INT_RATE@" + BigDecimalUtil.replaceNull(overdue_rate_y_iqp));//逾期利率
			authorizeKColl.addDataField("fldvalue55", "BILL_APP_NAME@" + TagUtil.replaceNull4String(iqpForftinKColl.getDataValue("drwr_name")));//出票人名称
			authorizeKColl.addDataField("fldvalue56", "ACP_BANK_ACCT_NO@" + "");//承兑人开户行账号
			authorizeKColl.addDataField("fldvalue57", "DSCNT_TYPE@" + "");//贴现方式
			authorizeKColl.addDataField("fldvalue58", "BILL_ACCT_NO@" + "");//出票人账号
			authorizeKColl.addDataField("fldvalue59", "BILL_OPEN_BRANCH_ID@" + "");//出票人开户行行号 
			authorizeKColl.addDataField("fldvalue60", "BILL_OPEN_BRANCH_NAME@" + "");//出票人开户行行名
			
			dao.insert(authorizeKColl, this.getConnection());
			
			/**生成授权信息：结算账户信息（取自账号信息表iqp_cus_acct）**/
			String conditionCusAcct = "where serno='"+iqpserno+"'";
			IndexedCollection iqpCusAcctIColl = dao.queryList(IQPCUSACCT, conditionCusAcct, this.getConnection());
			int feecount = 0;
			int eactcount = 0;
			for(int m=0;m<iqpCusAcctIColl.size();m++){
				KeyedCollection iqpCusAcctKColl = (KeyedCollection)iqpCusAcctIColl.get(m);
				String acct_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_no"));//账号
				String opac_org_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opac_org_no"));//开户行行号
				String opan_org_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opan_org_name"));//开户行行名
				String is_this_org_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行账户
				String acct_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_name"));//户名
				Double pay_amt = TagUtil.replaceNull4Double(iqpCusAcctKColl.getDataValue("pay_amt"));//受托支付金额
				String acct_gl_code = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_gl_code"));//科目号
				String ccy = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("cur_type"));//币种
				String is_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行
				/*01放款账号  02印花税账号 03收息收款账号    04受托支付账号     05	主办行资金归集账户*/
				String acct_attr = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_attr"));//账户属性
				String interbank_id = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("interbank_id"));//银联行号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + billNo);//借据号
				
				/*PAYM:还款账户 ACTV:放款账号 TURE:委托账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号*/
				if(!"".equals(acct_attr) && acct_attr.equals("01")){
					acct_attr = "ACTV";
				}else if(!"".equals(acct_attr) && acct_attr.equals("02")){
					acct_attr = "STAMP1";
				}else if(!"".equals(acct_attr) && acct_attr.equals("03")){
					acct_attr = "PAYM";
				}else if(!"".equals(acct_attr) && acct_attr.equals("04")){
					if(eactcount==0){
						acct_attr = "EACT";
					}else{
						acct_attr = "EACT"+eactcount;
					}
					eactcount++;
				}else if(!"".equals(acct_attr) && acct_attr.equals("06")){
					acct_attr = "STAMP2";
				}else if(!"".equals(acct_attr) && acct_attr.equals("07")){
					if(feecount==0){
						acct_attr = "FEE";
					}else{
						acct_attr = "FEE"+feecount;
					}
					feecount++;
					acctmap.put(acct_no, acct_attr);
				}else{
					acct_attr = "";
				}
				
				authorizeSubKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + acct_attr);//贷款账户类型 				
				authorizeSubKColl.addDataField("fldvalue04", "ACCT_NO@" + acct_no);//账号
				authorizeSubKColl.addDataField("fldvalue05", "CCY@" + ccy);//币种
//				if(is_this_org_acct.equals("1")){//本行账户
//					authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
//				}else{
//					authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + opac_org_no);//帐号银行代码
//				}
				authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + interbank_id);//帐号银行代码
				authorizeSubKColl.addDataField("fldvalue07", "BRANCH_ID@" + opac_org_no);//机构代码
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_NAME@" + acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "1");//账户类型:1-结算账户 2-保证金				
				authorizeSubKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue17", "ACCT_GL_CODE@" + acct_gl_code);//账号科目代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + pay_amt);//受托支付金额
				authorizeSubKColl.addDataField("fldvalue20", "BANK_NAME@" + opan_org_name);//银行名称
				authorizeSubKColl.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
				authorizeSubKColl.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + is_acct);//是否本行
				
				//开户行地址
				if(interbank_id!=null&&!"".equals(interbank_id)){
					KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, connnection);
					if(kColl!=null){
						authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
					}else{
						authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
					}
				}else{
					authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
				}
				
				dao.insert(authorizeSubKColl, this.getConnection());
			}
		
			/**（7）生成台账信息*/
			KeyedCollection accLoanKColl = new KeyedCollection(ACCLOANMODEL);
			accLoanKColl.addDataField("serno", Pvpserno);//业务编号	
			accLoanKColl.addDataField("acc_day", nowDate);//日期
			accLoanKColl.addDataField("acc_year", nowDate.substring(0, 4));//年份
			accLoanKColl.addDataField("acc_mon", nowDate.substring(5, 7));//月份
			accLoanKColl.addDataField("prd_id", prd_id);//产品编号
			accLoanKColl.addDataField("cus_id", cus_id);//客户编码
			accLoanKColl.addDataField("cont_no", cont_no);//合同编号
			accLoanKColl.addDataField("bill_no", billNo);//借据编号
			accLoanKColl.addDataField("loan_amt", pvp_amt);//贷款金额
			accLoanKColl.addDataField("loan_balance", pvp_amt);//贷款余额
			accLoanKColl.addDataField("distr_date", nowDate);//发放日期
			accLoanKColl.addDataField("end_date", end_date);//到期日期
			accLoanKColl.addDataField("ori_end_date", end_date);//原到期日期
			accLoanKColl.addDataField("post_count", "0");//展期次数
			accLoanKColl.addDataField("overdue", "0");//逾期期数
			accLoanKColl.addDataField("separate_date", "");//清分日期
			accLoanKColl.addDataField("ruling_ir", ruling_ir);//基准利率
			accLoanKColl.addDataField("reality_ir_y", reality_ir_y);//执行年利率
			accLoanKColl.addDataField("overdue_rate_y", overdue_rate_y_iqp);//逾期利率
			accLoanKColl.addDataField("default_rate_y", default_rate_y);//违约利率
			accLoanKColl.addDataField("comp_int_balance", "0");//复利余额
			accLoanKColl.addDataField("inner_owe_int", "0");//表内欠息
			accLoanKColl.addDataField("out_owe_int", "0");//表外欠息
			accLoanKColl.addDataField("rec_int_accum", "0");//应收利息累计
			accLoanKColl.addDataField("recv_int_accum", "0");//实收利息累计
			accLoanKColl.addDataField("normal_balance", pvp_amt);//正常余额
			accLoanKColl.addDataField("overdue_balance", "0");//逾期余额
			accLoanKColl.addDataField("slack_balance", "0");//呆滞余额
			accLoanKColl.addDataField("bad_dbt_balance", "0");//呆账余额
			accLoanKColl.addDataField("writeoff_date", "");//核销日期
			accLoanKColl.addDataField("paydate", "");//转垫款日
			accLoanKColl.addDataField("five_class", five_classfiy);//五级分类
			accLoanKColl.addDataField("twelve_cls_flg", "");//十二级分类
			accLoanKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			accLoanKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
			accLoanKColl.addDataField("acc_status", "0");//台帐状态
			accLoanKColl.addDataField("cur_type", cur_type);//币种
			dao.insert(accLoanKColl, this.getConnection());
		
			/** 7.生成授权信息：费用信息*/
			String conditionFee = "where serno='"+iqpserno+"'";
			IndexedCollection iqpAppendTermsIColl = dao.queryList(IQPFEE, conditionFee, this.getConnection());
			for(int i=0;i<iqpAppendTermsIColl.size();i++){
				KeyedCollection iqpAppendTermsKColl = (KeyedCollection)iqpAppendTermsIColl.get(i);
				String fee_code = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_code"));//费用代码
				String fee_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_cur_type"));//币种
				Double fee_amt = TagUtil.replaceNull4Double(iqpAppendTermsKColl.getDataValue("fee_amt"));//费用金额
				String fee_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_type"));//费用类型
				String fee_rate = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_rate"));//费用比率
				String is_cycle_chrg = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_cycle_chrg"));//是否周期性收费标识 
				String chrg_date = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_date"));//收费日期
				String acct_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_acct_no"));//账号
				String chrg_freq = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_freq"));//账号
				if(chrg_freq.equals("Y")){
					chrg_freq = "12";
				}else if(chrg_freq.equals("Q")){
					chrg_freq = "3";
				}else if(chrg_freq.equals("M")){
					chrg_freq = "1";
				}else{
					chrg_freq = "";
				}
//				String acct_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("acct_name"));//账户名
//				String opac_org_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opac_org_no"));//开户行行号
//				String opan_org_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opan_org_name"));//开户行行名
//				String acct_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("cur_type"));//账户币种
//				String is_this_org_acct = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_this_org_acct"));//是否本行账户
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + "");//借据号：不需赋值
				authorizeSubKColl.addDataField("fldvalue03", "FEE_CODE@" + fee_code);//费用代码				
				authorizeSubKColl.addDataField("fldvalue04", "CCY@" + fee_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
			    authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + TradeConstance.is_pay_flag);//收付标志 默认传 R: 收
				authorizeSubKColl.addDataField("fldvalue07", "FEE_TYPE@" + fee_type);//费用类型
				authorizeSubKColl.addDataField("fldvalue08", "BASE_AMOUNT@" + fee_amt);//基准金额 同费用金额
				authorizeSubKColl.addDataField("fldvalue09", "FEE_RATE@" + fee_rate);//费用比率
				authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + acctmap.get(acct_no));//扣款账户类型  默认传 FEE:收费账户			
				authorizeSubKColl.addDataField("fldvalue11", "CHARGE_PERIODICITY_FLAG@" + this.TransSDicForESB("STD_ZX_YES_NO",is_cycle_chrg));//是否周期性收费标识
				authorizeSubKColl.addDataField("fldvalue12", "CHARGE_DATE@" + chrg_date);//收费日期
				authorizeSubKColl.addDataField("fldvalue13", "AMOR_AMT@" + fee_amt);//摊销金额
				authorizeSubKColl.addDataField("fldvalue14", "CONTRACT_NO@" + cont_no);//协议号
//				authorizeSubKColl.addDataField("fldvalue15", "ACCT_NO@" + acct_no);//账号
//				authorizeSubKColl.addDataField("fldvalue16", "ACCT_NAME@" + acct_name);//账户名
//				authorizeSubKColl.addDataField("fldvalue17", "OPAC_ORG_NO@" + opac_org_no);//开户行行号
//				authorizeSubKColl.addDataField("fldvalue18", "OPAN_ORG_NAME@" + opan_org_name);//开户行行名
//				authorizeSubKColl.addDataField("fldvalue19", "ACCT_CCY@" + acct_cur_type);//账户币种
//				authorizeSubKColl.addDataField("fldvalue20", "IS_THIS_ORG_ACCT@" + is_this_org_acct);//是否本行账户
				authorizeSubKColl.addDataField("fldvalue21", "BRANCH_ID@" + in_acct_br_id);//入账机构
				authorizeSubKColl.addDataField("fldvalue22", "FEE_SPAN@" + chrg_freq);//收费间隔
				dao.insert(authorizeSubKColl, this.getConnection());
			}
		
			/**生成授信信息：保证金信息**/
			KeyedCollection ctrContKColl =  dao.queryDetail(CTRCONTMODEL, cont_no, this.getConnection());
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 start
			BigDecimal security_rate = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("security_rate"));//保证金比例
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 end
			Double security_amt = TagUtil.replaceNull4Double(ctrContKColl.getDataValue("security_amt"));//保证金金额
			String bailCondition = " where serno = '"+iqpserno+"'";
			IndexedCollection bailIColl = dao.queryList("PubBailInfo", bailCondition, connnection);
			for(int i=0;i<bailIColl.size();i++){
				KeyedCollection iqpBailInfoKColl = (KeyedCollection)bailIColl.get(i);
				String bail_acct_no = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_no"));//保证金账号
				String bail_cur_type = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("cur_type"));//币种
				String bail_acct_name = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_name"));//保证金账号名称
				String open_org = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("open_org"));//开户机构
				String bail_acct_gl_code = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_gl_code"));//科目号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				/** 给授权从表赋值 */
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "04");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + "");//借据号：不需赋值
				authorizeSubKColl.addDataField("fldvalue03", "BRANCH_ID@" + open_org);//机构代码
				authorizeSubKColl.addDataField("fldvalue04", "GUARANTEE_ACCT_NO@" + bail_acct_no);//保证金账号
				authorizeSubKColl.addDataField("fldvalue05", "GUARANTEE_NO@" + "");//保证金编号：不需赋值
				authorizeSubKColl.addDataField("fldvalue06", "ACCT_TYPE@" + "");//账户类型：不需赋值
				authorizeSubKColl.addDataField("fldvalue07", "GUARANTEE_EXPIRY_DATE@" + "");//保证金到期日：不需赋值
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_CODE@" + "");//账户代码：不需赋值
				authorizeSubKColl.addDataField("fldvalue09", "CA_TT_FLAG@" + "");//钞汇标志：不需赋值
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_GL_CODE@" + bail_acct_gl_code);//账号科目代码：不需赋值
				authorizeSubKColl.addDataField("fldvalue11", "ACCT_NAME@" + bail_acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue12", "CCY@" + bail_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue13", "GUARANTEE_PER@" + security_rate);//保证金比例
				authorizeSubKColl.addDataField("fldvalue14", "GUARANTEE_AMT@" + security_amt);//保证金金额
				authorizeSubKColl.addDataField("fldvalue15", "CONTRACT_NO@" + cont_no);//协议号
				if("510".equals(assure_main)){
					authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "Y");//是否准全额
				}else{
					authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "N");//是否准全额
				}

				dao.insert(authorizeSubKColl, this.getConnection());
			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl4trade = new KeyedCollection();
//			kColl4trade.put("CLIENT_NO", cus_id);
//			kColl4trade.put("BUSS_SEQ_NO", iqpserno);
//			kColl4trade.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), this.getConnection());	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常！");
		}
	
	}
	
	/**
	 * 贸易融资延期信用证项下应收款买入贷款出账流程审批通过
	 * 出账申请审批通过执行操作
	 * 1.生成授权信息
	 * 2.生成台帐信息
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpTfLoanForYQXYZMR(String serno)throws ComponentException {
		Connection connnection = null;
		try {
			
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			connnection = this.getConnection();
			
			/** 1.数据准备：通过业务流水号查询【出账申请】 */
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, connnection);
			String prd_id = (String)pvpLoanKColl.getDataValue("prd_id");//产品编号
			String cus_id = (String)pvpLoanKColl.getDataValue("cus_id");//客户编码
			String cont_no = (String)pvpLoanKColl.getDataValue("cont_no");//合同编号
			String manager_br_id = (String)pvpLoanKColl.getDataValue("manager_br_id");//管理机构
			String in_acct_br_id = (String)pvpLoanKColl.getDataValue("in_acct_br_id");//入账机构
			String cur_type = (String)pvpLoanKColl.getDataValue("cur_type");//币种
			BigDecimal pvp_amt = BigDecimalUtil.replaceNull(pvpLoanKColl.getDataValue("pvp_amt"));//出账金额
			String Pvpserno = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("serno"));//授权交易流水号
			
			/** 核算与信贷业务品种映射 START */
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			String lmPrdId = service.getPrdBasicCLPM2LM(cont_no, prd_id, this.getContext(), this.getConnection());
			/** 核算与信贷业务品种映射 END */
			
			/** 2.数据准备：通过客户编号查询【客户信息】 */
			String cus_name = "";
			String cert_type = "";
			String cert_code = "";
			String belg_line = "";
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			try {
				CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
				CusBase cusBase = csi.getCusBaseByCusId(cus_id,this.getContext(),this.getConnection());
				cus_name = cusBase.getCusName();
				cert_type = cusBase.getCertType();
				cert_code = cusBase.getCertCode();
				belg_line = TagUtil.replaceNull4String(cusBase.getBelgLine());//所属条线
			} catch (Exception e) {
				e.printStackTrace();
				throw new EMPException("获取组织机构模块失败！");
			}
			
			/** 3.数据准备：通过业务流水号查询【合同信息】 */
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, cont_no, connnection);
			KeyedCollection ctrContKColl =  dao.queryDetail(CTRCONTMODEL, cont_no, connnection);
			String five_classfiy = (String)ctrContSubKColl.getDataValue("five_classfiy");//五级分类
			BigDecimal cont_amt = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("cont_amt"));
			String term_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("term_type"));//期限类型
			Integer cont_term = TagUtil.replaceNull4Int(ctrContSubKColl.getDataValue("cont_term"));//合同期限
			String ruling_ir = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir"));//基准利率（年）
			String reality_ir_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("reality_ir_y"));//执行利率（年）
			String overdue_rate_y_iqp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("overdue_rate_y"));//逾期利率
			String default_rate_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("default_rate_y"));//违约利率
			KeyedCollection ctrKColl = dao.queryDetail(CTRCONTMODEL, cont_no, connnection);
			String assure_main = TagUtil.replaceNull4String(ctrKColl.getDataValue("assure_main"));//担保方式
			String iqpserno = (String) ctrKColl.getDataValue("serno");
			KeyedCollection iqpLoanKColl = dao.queryDetail(IQPLOANAPPMODEL, iqpserno, connnection);
			
			/**4.数据准备：获取从表票据信息*/
			String condition = " where serno ='" + iqpserno + "'";
			KeyedCollection iqpDelayCreditPurKColl = dao.queryDetail("IqpDelayCreditPur", iqpserno, connnection);
			String receAmt = (String) iqpDelayCreditPurKColl.getDataValue("rece_amt");
			String payAmt = (String) iqpDelayCreditPurKColl.getDataValue("pay_amt");
			String arrangr_deduct = (String) iqpDelayCreditPurKColl.getDataValue("arrangr_deduct");
			
			String nowDate = (String)this.getContext().getDataValue(CMISConstance.OPENDAY);
			if(nowDate == null || nowDate.trim().length() == 0){
				throw new EMPException("获取系统营业时间失败！请检查系统营业时间配置是否正确！");
			}
			
			//生成授权编号，所有票据授权交易流水在一个授权编号下
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());
			
			/** 6.循环生成授权信息和台账信息*/
			Map<String, String> acctmap = new HashMap<String, String>();//定义一个账号对应map
			/** （1）获取借据编号*/
			PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
            .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), connnection);
			String billNo = cmisComponent.getBillNoByContNo(cont_no);
			String end_date = DateUtils.getEndDate(term_type, nowDate, cont_term);//借据到期日
			
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			
			/**（4）生成交易流水号 */
			String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connnection, this.getContext());
			
			/**（5）给授权信息表赋值 */
			authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
			authorizeKColl.addDataField("serno", serno);//业务流水号（出账编号）
			authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
			authorizeKColl.addDataField("prd_id", prd_id);//产品编号
			authorizeKColl.addDataField("cus_id", cus_id);//客户编码
			authorizeKColl.addDataField("cus_name", cus_name);//客户名称
			authorizeKColl.addDataField("cont_no", cont_no);//合同编号
			authorizeKColl.addDataField("bill_no", billNo);//借据编号
			authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_TXFFSQ+TradeConstance.SERVICE_SCENE_TXFFSQ);//交易码+场景
			authorizeKColl.addDataField("tran_amt", pvp_amt);//交易金额
			authorizeKColl.addDataField("tran_date", nowDate);//交易日期取发送日期默认不赋值
			authorizeKColl.addDataField("send_times", "0");//发送次数
			authorizeKColl.addDataField("return_code", "");//返回编码
			authorizeKColl.addDataField("return_desc", "");//返回信息
			authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
			authorizeKColl.addDataField("status", "00");//状态
			authorizeKColl.addDataField("cur_type", cur_type);//币种
			
			authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编码
			authorizeKColl.addDataField("fldvalue02", "OPERATION_TYPE@"+"BUY");//操作类型（直贴，默认为买入）
			authorizeKColl.addDataField("fldvalue03", "BACK_FLAG@"+"N");//回购/返售标识（直贴，默认为否“N”）·
			authorizeKColl.addDataField("fldvalue04", "INPUT_DATE@"+TagUtil.formatDate(iqpLoanKColl.getDataValue("apply_date")));//填入日期
			authorizeKColl.addDataField("fldvalue05", "BANK_ID@"+TradeConstance.BANK_ID);//银行代码
			authorizeKColl.addDataField("fldvalue06", "DISCONT_AGREE_NO@"+cont_no);//贴现协议号
			authorizeKColl.addDataField("fldvalue07", "LOAN_TYPE@"+lmPrdId);//贷款种类
			authorizeKColl.addDataField("fldvalue08", "DUEBILL_NO@"+billNo);//借据号
			authorizeKColl.addDataField("fldvalue09", "BILL_NO@"+"");//汇票号码
			authorizeKColl.addDataField("fldvalue10", "BILL_TYPE@"+"");//票据类型
			authorizeKColl.addDataField("fldvalue11", "APPLYER_GLOBAL_TYPE@"+cert_type);//贴现申请人证件类型
			authorizeKColl.addDataField("fldvalue12", "APPLYER_GLOBAL_ID@"+cert_code);//贴现申请人证件号
			authorizeKColl.addDataField("fldvalue13", "APPLYER_ISS_CTRY@"+"CN");//贴现申请人发证国家
			authorizeKColl.addDataField("fldvalue14", "APPLYER_NAME@"+cus_name);//贴现申请人名称
			authorizeKColl.addDataField("fldvalue15", "APPLYER_ACCT_NO@"+"");//贴现申请人户名（目前不传）
			authorizeKColl.addDataField("fldvalue16", "BILL_BRANCH_ID@"+in_acct_br_id);//借据机构代码（取入账机构）
			authorizeKColl.addDataField("fldvalue17", "DISCOUNT_KIND@"+"NORM");//贴现种类:普通贴现
			authorizeKColl.addDataField("fldvalue18", "DISCOUNT_CCY@"+ctrKColl.getDataValue("cont_cur_type"));//币种
			authorizeKColl.addDataField("fldvalue19", "DISCOUNT_AMT@"+ctrKColl.getDataValue("cont_amt"));//贴现金额
			authorizeKColl.addDataField("fldvalue20", "DISCOUNT_DAYS@"+iqpDelayCreditPurKColl.getDataValue("fin_day"));//贴现天数
			authorizeKColl.addDataField("fldvalue21", "DISCOUNT_RATE@"+BigDecimalUtil.replaceNull(reality_ir_y));//贴现利率
			authorizeKColl.addDataField("fldvalue22", "TRANSFER_DISCOUNT_RATE@"+"");//转贴现利率
			authorizeKColl.addDataField("fldvalue23", "TRANSFER_DISCOUNT_DATE@"+"");//转贴现日期
			authorizeKColl.addDataField("fldvalue24", "DISCOUNT_INTEREST@"+(cont_amt.subtract(BigDecimalUtil.replaceNull(payAmt)))
					.subtract(BigDecimalUtil.replaceNull(arrangr_deduct)));//贴现利息 =合同金额-实付金额-预扣款项
			//核算确认，日期没有填当前日期
			authorizeKColl.addDataField("fldvalue25", "DISCOUNT_DATE@"+TagUtil.formatDate(nowDate));//贴现日期
			authorizeKColl.addDataField("fldvalue26", "BILL_ISSUE_DATE@"+TagUtil.formatDate(nowDate));//出票日期
			String rece_end_date = (String) iqpDelayCreditPurKColl.getDataValue("rece_end_date");
			if(rece_end_date!=null&&!"".equals(rece_end_date)){
				authorizeKColl.addDataField("fldvalue27", "BILL_EXPIRY_DATE@"+TagUtil.formatDate(rece_end_date));//票据到期日期
			}else{
				authorizeKColl.addDataField("fldvalue27", "BILL_EXPIRY_DATE@"+TagUtil.formatDate(nowDate));//票据到期日期
			}
			authorizeKColl.addDataField("fldvalue28", "RETURN_DATE@"+"");//约定转回日期
			authorizeKColl.addDataField("fldvalue29", "EXPIRY_DATE@"+TagUtil.formatDate(end_date));//到期日期
			authorizeKColl.addDataField("fldvalue30", "DEDUCT_METHOD@"+"AUTOPAY");//扣款方式
			authorizeKColl.addDataField("fldvalue31", "TO_BRANCH_ID@"+"");//对方机构代码（目前不传）
			authorizeKColl.addDataField("fldvalue37", "DSCNT_INT_PAY_MODE@"+"");//贴现付息方式
			authorizeKColl.addDataField("fldvalue38", "BRANCH_ID@"+in_acct_br_id);//取入账机构
			authorizeKColl.addDataField("fldvalue39", "AGENTOR_NAME@"+"");//代理人名称
			authorizeKColl.addDataField("fldvalue40", "AGENTOR_ACCT_NO@"+"");//代理人账号
			authorizeKColl.addDataField("fldvalue41", "AGENT_ORG_NO@"+"");//代理人开户行行号
			authorizeKColl.addDataField("fldvalue42", "AGENT_ORG_NAME@"+"");//代理人开户行行名
			authorizeKColl.addDataField("fldvalue43", "COUNTER_OPEN_BRANCH_ID@"+"");//对手行行号
			authorizeKColl.addDataField("fldvalue44", "COUNTER_OPEN_BRANCH_NAME@"+"");//对手行行名
			authorizeKColl.addDataField("fldvalue45", "AA_NAME@"+iqpDelayCreditPurKColl.getDataValue("promissory_pyr_name"));//承兑人名称
			authorizeKColl.addDataField("fldvalue46", "AAORG_NO@"+"");//承兑行行号
			authorizeKColl.addDataField("fldvalue47", "AAORG_NAME@"+"");//承兑行名称
			authorizeKColl.addDataField("fldvalue48", "PAY_AMT@"+BigDecimalUtil.replaceNull(iqpDelayCreditPurKColl.getDataValue("pay_amt")));//实付金额
			authorizeKColl.addDataField("fldvalue49", "ARRANGR_DEDUCT_OPT@"+BigDecimalUtil.replaceNull(iqpDelayCreditPurKColl.getDataValue("arrangr_deduct")));//预扣款项
			authorizeKColl.addDataField("fldvalue50", "APPLYER_ID@"+cus_id);//贴现申请人编号
			authorizeKColl.addDataField("fldvalue51", "E_CDE@" + "");//是否电票
			authorizeKColl.addDataField("fldvalue52", "BATCH_NO@" + "");//批次号
			authorizeKColl.addDataField("fldvalue53", "CUST_TYP@" + belg_line);//客户类型取所属条线
			authorizeKColl.addDataField("fldvalue54", "DISC_OD_INT_RATE@" + BigDecimalUtil.replaceNull(overdue_rate_y_iqp));//逾期利率
			authorizeKColl.addDataField("fldvalue55", "BILL_APP_NAME@" + "");//出票人名称
			authorizeKColl.addDataField("fldvalue56", "ACP_BANK_ACCT_NO@" + "");//承兑人开户行账号
			authorizeKColl.addDataField("fldvalue57", "DSCNT_TYPE@" + "");//贴现方式
			authorizeKColl.addDataField("fldvalue58", "BILL_ACCT_NO@" + "");//出票人账号
			authorizeKColl.addDataField("fldvalue59", "BILL_OPEN_BRANCH_ID@" + "");//出票人开户行行号 
			authorizeKColl.addDataField("fldvalue60", "BILL_OPEN_BRANCH_NAME@" + "");//出票人开户行行名
			dao.insert(authorizeKColl, this.getConnection());
			
			/**生成授权信息：结算账户信息（取自账号信息表iqp_cus_acct）**/
			String conditionCusAcct = "where serno='"+iqpserno+"'";
			IndexedCollection iqpCusAcctIColl = dao.queryList(IQPCUSACCT, conditionCusAcct, this.getConnection());
			int feecount = 0;
			int eactcount = 0;
			for(int m=0;m<iqpCusAcctIColl.size();m++){
				KeyedCollection iqpCusAcctKColl = (KeyedCollection)iqpCusAcctIColl.get(m);
				String acct_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_no"));//账号
				String opac_org_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opac_org_no"));//开户行行号
				String opan_org_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opan_org_name"));//开户行行名
				String is_this_org_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行账户
				String acct_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_name"));//户名
				Double pay_amt = TagUtil.replaceNull4Double(iqpCusAcctKColl.getDataValue("pay_amt"));//受托支付金额
				String acct_gl_code = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_gl_code"));//科目号
				String ccy = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("cur_type"));//币种
				String is_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行
				/*01放款账号  02印花税账号 03收息收款账号    04受托支付账号     05	主办行资金归集账户*/
				String acct_attr = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_attr"));//账户属性
				String interbank_id = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("interbank_id"));//银联行号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + billNo);//借据号
				
				/*PAYM:还款账户 ACTV:放款账号 TURE:委托账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号*/
				if(!"".equals(acct_attr) && acct_attr.equals("01")){
					acct_attr = "ACTV";
				}else if(!"".equals(acct_attr) && acct_attr.equals("02")){
					acct_attr = "STAMP1";
				}else if(!"".equals(acct_attr) && acct_attr.equals("03")){
					acct_attr = "PAYM";
				}else if(!"".equals(acct_attr) && acct_attr.equals("04")){
					if(eactcount==0){
						acct_attr = "EACT";
					}else{
						acct_attr = "EACT"+eactcount;
					}
					eactcount++;
				}else if(!"".equals(acct_attr) && acct_attr.equals("06")){
					acct_attr = "STAMP2";
				}else if(!"".equals(acct_attr) && acct_attr.equals("07")){
					if(feecount==0){
						acct_attr = "FEE";
					}else{
						acct_attr = "FEE"+feecount;
					}
					feecount++;
					acctmap.put(acct_no, acct_attr);
				}else{
					acct_attr = "";
				}
				
				authorizeSubKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + acct_attr);//贷款账户类型 				
				authorizeSubKColl.addDataField("fldvalue04", "ACCT_NO@" + acct_no);//账号
				authorizeSubKColl.addDataField("fldvalue05", "CCY@" + ccy);//币种
//				if(is_this_org_acct.equals("1")){//本行账户
//					authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
//				}else{
//					authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + opac_org_no);//帐号银行代码
//				}
				authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + interbank_id);//帐号银行代码
				authorizeSubKColl.addDataField("fldvalue07", "BRANCH_ID@" + opac_org_no);//机构代码
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_NAME@" + acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "1");//账户类型:1-结算账户 2-保证金				
				authorizeSubKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue17", "ACCT_GL_CODE@" + acct_gl_code);//账号科目代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + pay_amt);//受托支付金额
				authorizeSubKColl.addDataField("fldvalue20", "BANK_NAME@" + opan_org_name);//银行名称
				authorizeSubKColl.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
				authorizeSubKColl.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + is_acct);//是否本行
				
				//开户行地址
				if(interbank_id!=null&&!"".equals(interbank_id)){
					KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, connnection);
					if(kColl!=null){
						authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
					}else{
						authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
					}
				}else{
					authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
				}
				
				dao.insert(authorizeSubKColl, this.getConnection());
			}
		
			/**（7）生成台账信息*/
			KeyedCollection accLoanKColl = new KeyedCollection(ACCLOANMODEL);
			accLoanKColl.addDataField("serno", Pvpserno);//业务编号	
			accLoanKColl.addDataField("acc_day", nowDate);//日期
			accLoanKColl.addDataField("acc_year", nowDate.substring(0, 4));//年份
			accLoanKColl.addDataField("acc_mon", nowDate.substring(5, 7));//月份
			accLoanKColl.addDataField("prd_id", prd_id);//产品编号
			accLoanKColl.addDataField("cus_id", cus_id);//客户编码
			accLoanKColl.addDataField("cont_no", cont_no);//合同编号
			accLoanKColl.addDataField("bill_no", billNo);//借据编号
			accLoanKColl.addDataField("loan_amt", pvp_amt);//贷款金额
			accLoanKColl.addDataField("loan_balance", pvp_amt);//贷款余额
			accLoanKColl.addDataField("distr_date", nowDate);//发放日期
			accLoanKColl.addDataField("end_date", end_date);//到期日期
			accLoanKColl.addDataField("ori_end_date", end_date);//原到期日期
			accLoanKColl.addDataField("post_count", "0");//展期次数
			accLoanKColl.addDataField("overdue", "0");//逾期期数
			accLoanKColl.addDataField("separate_date", "");//清分日期
			accLoanKColl.addDataField("ruling_ir", ruling_ir);//基准利率
			accLoanKColl.addDataField("reality_ir_y", reality_ir_y);//执行年利率
			accLoanKColl.addDataField("overdue_rate_y", overdue_rate_y_iqp);//逾期利率
			accLoanKColl.addDataField("default_rate_y", default_rate_y);//违约利率
			accLoanKColl.addDataField("comp_int_balance", "0");//复利余额
			accLoanKColl.addDataField("inner_owe_int", "0");//表内欠息
			accLoanKColl.addDataField("out_owe_int", "0");//表外欠息
			accLoanKColl.addDataField("rec_int_accum", "0");//应收利息累计
			accLoanKColl.addDataField("recv_int_accum", "0");//实收利息累计
			accLoanKColl.addDataField("normal_balance", pvp_amt);//正常余额
			accLoanKColl.addDataField("overdue_balance", "0");//逾期余额
			accLoanKColl.addDataField("slack_balance", "0");//呆滞余额
			accLoanKColl.addDataField("bad_dbt_balance", "0");//呆账余额
			accLoanKColl.addDataField("writeoff_date", "");//核销日期
			accLoanKColl.addDataField("paydate", "");//转垫款日
			accLoanKColl.addDataField("five_class", five_classfiy);//五级分类
			accLoanKColl.addDataField("twelve_cls_flg", "");//十二级分类
			accLoanKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			accLoanKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
			accLoanKColl.addDataField("acc_status", "0");//台帐状态
			accLoanKColl.addDataField("cur_type", cur_type);//币种
			dao.insert(accLoanKColl, this.getConnection());
		
			/** 7.生成授权信息：费用信息*/
			String conditionFee = "where serno='"+iqpserno+"'";
			IndexedCollection iqpAppendTermsIColl = dao.queryList(IQPFEE, conditionFee, this.getConnection());
			for(int i=0;i<iqpAppendTermsIColl.size();i++){
				KeyedCollection iqpAppendTermsKColl = (KeyedCollection)iqpAppendTermsIColl.get(i);
				String fee_code = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_code"));//费用代码
				String fee_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_cur_type"));//币种
				Double fee_amt = TagUtil.replaceNull4Double(iqpAppendTermsKColl.getDataValue("fee_amt"));//费用金额
				String fee_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_type"));//费用类型
				String fee_rate = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_rate"));//费用比率
				String is_cycle_chrg = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_cycle_chrg"));//是否周期性收费标识 
				String chrg_date = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_date"));//收费日期
				String acct_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_acct_no"));//账号
				String chrg_freq = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_freq"));//账号
				if(chrg_freq.equals("Y")){
					chrg_freq = "12";
				}else if(chrg_freq.equals("Q")){
					chrg_freq = "3";
				}else if(chrg_freq.equals("M")){
					chrg_freq = "1";
				}else{
					chrg_freq = "";
				}
//				String acct_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("acct_name"));//账户名
//				String opac_org_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opac_org_no"));//开户行行号
//				String opan_org_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opan_org_name"));//开户行行名
//				String acct_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("cur_type"));//账户币种
//				String is_this_org_acct = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_this_org_acct"));//是否本行账户
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + "");//借据号：不需赋值
				authorizeSubKColl.addDataField("fldvalue03", "FEE_CODE@" + fee_code);//费用代码				
				authorizeSubKColl.addDataField("fldvalue04", "CCY@" + fee_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
			    authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + TradeConstance.is_pay_flag);//收付标志 默认传 R: 收
				authorizeSubKColl.addDataField("fldvalue07", "FEE_TYPE@" + fee_type);//费用类型
				authorizeSubKColl.addDataField("fldvalue08", "BASE_AMOUNT@" + fee_amt);//基准金额 同费用金额
				authorizeSubKColl.addDataField("fldvalue09", "FEE_RATE@" + fee_rate);//费用比率
				authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + acctmap.get(acct_no));//扣款账户类型  默认传 FEE:收费账户			
				authorizeSubKColl.addDataField("fldvalue11", "CHARGE_PERIODICITY_FLAG@" + this.TransSDicForESB("STD_ZX_YES_NO",is_cycle_chrg));//是否周期性收费标识
				authorizeSubKColl.addDataField("fldvalue12", "CHARGE_DATE@" + chrg_date);//收费日期
				authorizeSubKColl.addDataField("fldvalue13", "AMOR_AMT@" + fee_amt);//摊销金额
				authorizeSubKColl.addDataField("fldvalue14", "CONTRACT_NO@" + cont_no);//协议号
//				authorizeSubKColl.addDataField("fldvalue15", "ACCT_NO@" + acct_no);//账号
//				authorizeSubKColl.addDataField("fldvalue16", "ACCT_NAME@" + acct_name);//账户名
//				authorizeSubKColl.addDataField("fldvalue17", "OPAC_ORG_NO@" + opac_org_no);//开户行行号
//				authorizeSubKColl.addDataField("fldvalue18", "OPAN_ORG_NAME@" + opan_org_name);//开户行行名
//				authorizeSubKColl.addDataField("fldvalue19", "ACCT_CCY@" + acct_cur_type);//账户币种
//				authorizeSubKColl.addDataField("fldvalue20", "IS_THIS_ORG_ACCT@" + is_this_org_acct);//是否本行账户
				authorizeSubKColl.addDataField("fldvalue21", "BRANCH_ID@" + in_acct_br_id);//入账机构
				authorizeSubKColl.addDataField("fldvalue22", "FEE_SPAN@" + chrg_freq);//收费间隔
				dao.insert(authorizeSubKColl, this.getConnection());
			}
		
			/**生成授信信息：保证金信息**/
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 start
			BigDecimal security_rate = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("security_rate"));//保证金比例
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 end
			Double security_amt = TagUtil.replaceNull4Double(ctrContKColl.getDataValue("security_amt"));//保证金金额
			String bailCondition = " where serno = '"+iqpserno+"'";
			IndexedCollection bailIColl = dao.queryList("PubBailInfo", bailCondition, connnection);
			for(int i=0;i<bailIColl.size();i++){
				KeyedCollection iqpBailInfoKColl = (KeyedCollection)bailIColl.get(i);
				String bail_acct_no = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_no"));//保证金账号
				String bail_cur_type = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("cur_type"));//币种
				String bail_acct_name = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_name"));//保证金账号名称
				String open_org = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("open_org"));//开户机构
				String bail_acct_gl_code = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_gl_code"));//科目号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				/** 给授权从表赋值 */
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "04");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + "");//借据号：不需赋值
				authorizeSubKColl.addDataField("fldvalue03", "BRANCH_ID@" + open_org);//机构代码
				authorizeSubKColl.addDataField("fldvalue04", "GUARANTEE_ACCT_NO@" + bail_acct_no);//保证金账号
				authorizeSubKColl.addDataField("fldvalue05", "GUARANTEE_NO@" + "");//保证金编号：不需赋值
				authorizeSubKColl.addDataField("fldvalue06", "ACCT_TYPE@" + "");//账户类型：不需赋值
				authorizeSubKColl.addDataField("fldvalue07", "GUARANTEE_EXPIRY_DATE@" + "");//保证金到期日：不需赋值
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_CODE@" + "");//账户代码：不需赋值
				authorizeSubKColl.addDataField("fldvalue09", "CA_TT_FLAG@" + "");//钞汇标志：不需赋值
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_GL_CODE@" + bail_acct_gl_code);//账号科目代码：不需赋值
				authorizeSubKColl.addDataField("fldvalue11", "ACCT_NAME@" + bail_acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue12", "CCY@" + bail_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue13", "GUARANTEE_PER@" + security_rate);//保证金比例
				authorizeSubKColl.addDataField("fldvalue14", "GUARANTEE_AMT@" + security_amt);//保证金金额
				authorizeSubKColl.addDataField("fldvalue15", "CONTRACT_NO@" + cont_no);//协议号
				if("510".equals(assure_main)){
					authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "Y");//是否准全额
				}else{
					authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "N");//是否准全额
				}

				dao.insert(authorizeSubKColl, this.getConnection());
			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl4trade = new KeyedCollection();
//			kColl4trade.put("CLIENT_NO", cus_id);
//			kColl4trade.put("BUSS_SEQ_NO", iqpserno);
//			kColl4trade.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), this.getConnection());	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常！");
		}
	
	}
	
	/**
	 * 贸易融资远期信用证项下汇票贴现贷款出账流程审批通过
	 * 出账申请审批通过执行操作
	 * 1.生成授权信息
	 * 2.生成台帐信息
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpTfLoanForYQXYZTX(String serno)throws ComponentException {
		Connection connnection = null;
		try {
			
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			connnection = this.getConnection();
			
			/** 1.数据准备：通过业务流水号查询【出账申请】 */
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, connnection);
			String prd_id = (String)pvpLoanKColl.getDataValue("prd_id");//产品编号
			String cus_id = (String)pvpLoanKColl.getDataValue("cus_id");//客户编码
			String cont_no = (String)pvpLoanKColl.getDataValue("cont_no");//合同编号
			String manager_br_id = (String)pvpLoanKColl.getDataValue("manager_br_id");//管理机构
			String in_acct_br_id = (String)pvpLoanKColl.getDataValue("in_acct_br_id");//入账机构
			String cur_type = (String)pvpLoanKColl.getDataValue("cur_type");//币种
			BigDecimal pvp_amt = BigDecimalUtil.replaceNull(pvpLoanKColl.getDataValue("pvp_amt"));//出账金额
			String Pvpserno = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("serno"));//授权交易流水号
			
			/** 核算与信贷业务品种映射 START */
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			String lmPrdId = service.getPrdBasicCLPM2LM(cont_no, prd_id, this.getContext(), this.getConnection());
			/** 核算与信贷业务品种映射 END */
			
			/** 2.数据准备：通过客户编号查询【客户信息】 */
			String cus_name = "";
			String cert_type = "";
			String cert_code = "";
			String belg_line = "";
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			try {
				CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
				CusBase cusBase = csi.getCusBaseByCusId(cus_id,this.getContext(),this.getConnection());
				cus_name = cusBase.getCusName();
				cert_type = cusBase.getCertType();
				cert_code = cusBase.getCertCode();
				belg_line = TagUtil.replaceNull4String(cusBase.getBelgLine());//所属条线
			} catch (Exception e) {
				e.printStackTrace();
				throw new EMPException("获取组织机构模块失败！");
			}
			
			/** 3.数据准备：通过业务流水号查询【合同信息】 */
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, cont_no, connnection);
			String five_classfiy = (String)ctrContSubKColl.getDataValue("five_classfiy");//五级分类
			String term_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("term_type"));//期限类型
			Integer cont_term = TagUtil.replaceNull4Int(ctrContSubKColl.getDataValue("cont_term"));//合同期限
			String ruling_ir = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir"));//基准利率（年）
			String reality_ir_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("reality_ir_y"));//执行利率（年）
			String overdue_rate_y_iqp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("overdue_rate_y"));//逾期利率
			String default_rate_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("default_rate_y"));//违约利率
			KeyedCollection ctrKColl = dao.queryDetail(CTRCONTMODEL, cont_no, connnection);
			String assure_main = TagUtil.replaceNull4String(ctrKColl.getDataValue("assure_main"));//担保方式
			String iqpserno = (String) ctrKColl.getDataValue("serno");
			BigDecimal cont_amt = BigDecimalUtil.replaceNull(ctrKColl.getDataValue("cont_amt"));
			KeyedCollection iqpLoanKColl = dao.queryDetail(IQPLOANAPPMODEL, iqpserno, connnection);
			
			/**4.数据准备：获取从表票据信息*/
			KeyedCollection iqpFastCreditDscntKColl = dao.queryDetail("IqpFastCreditDscnt", iqpserno, connnection);
			String post_order_no = (String) iqpFastCreditDscntKColl.getDataValue("post_order_no");
			String drftAmt = (String) iqpFastCreditDscntKColl.getDataValue("drft_amt");
			String payAmt = (String) iqpFastCreditDscntKColl.getDataValue("pay_amt");
			String arrangr_deduct = (String) iqpFastCreditDscntKColl.getDataValue("arrangr_deduct");
			
			String nowDate = (String)this.getContext().getDataValue(CMISConstance.OPENDAY);
			if(nowDate == null || nowDate.trim().length() == 0){
				throw new EMPException("获取系统营业时间失败！请检查系统营业时间配置是否正确！");
			}
			
			//生成授权编号，所有票据授权交易流水在一个授权编号下
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());
			
			/** 6.循环生成授权信息和台账信息*/
			Map<String, String> acctmap = new HashMap<String, String>();//定义一个账号对应map
			/** （1）获取借据编号*/
			PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
            .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), connnection);
			String billNo = cmisComponent.getBillNoByContNo(cont_no);
			String end_date = DateUtils.getEndDate(term_type, nowDate, cont_term);//借据到期日
			
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			
			/**（4）生成交易流水号 */
			String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connnection, this.getContext());
			
			/**（5）给授权信息表赋值 */
			authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
			authorizeKColl.addDataField("serno", serno);//业务流水号（出账编号）
			authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
			authorizeKColl.addDataField("prd_id", prd_id);//产品编号
			authorizeKColl.addDataField("cus_id", cus_id);//客户编码
			authorizeKColl.addDataField("cus_name", cus_name);//客户名称
			authorizeKColl.addDataField("cont_no", cont_no);//合同编号
			authorizeKColl.addDataField("bill_no", billNo);//借据编号
			authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_TXFFSQ+TradeConstance.SERVICE_SCENE_TXFFSQ);//交易码+场景
			authorizeKColl.addDataField("tran_amt", pvp_amt);//交易金额
			authorizeKColl.addDataField("tran_date", nowDate);//交易日期取发送日期默认不赋值
			authorizeKColl.addDataField("send_times", "0");//发送次数
			authorizeKColl.addDataField("return_code", "");//返回编码
			authorizeKColl.addDataField("return_desc", "");//返回信息
			authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
			authorizeKColl.addDataField("status", "00");//状态
			authorizeKColl.addDataField("cur_type", cur_type);//币种
			
			authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编码
			authorizeKColl.addDataField("fldvalue02", "OPERATION_TYPE@"+"BUY");//操作类型（直贴，默认为买入）
			authorizeKColl.addDataField("fldvalue03", "BACK_FLAG@"+"N");//回购/返售标识（直贴，默认为否“N”）·
			authorizeKColl.addDataField("fldvalue04", "INPUT_DATE@"+TagUtil.formatDate(iqpLoanKColl.getDataValue("apply_date")));//填入日期
			authorizeKColl.addDataField("fldvalue05", "BANK_ID@"+TradeConstance.BANK_ID);//银行代码
			authorizeKColl.addDataField("fldvalue06", "DISCONT_AGREE_NO@"+cont_no);//贴现协议号
			authorizeKColl.addDataField("fldvalue07", "LOAN_TYPE@"+lmPrdId);//贷款种类
			authorizeKColl.addDataField("fldvalue08", "DUEBILL_NO@"+billNo);//借据号
			authorizeKColl.addDataField("fldvalue09", "BILL_NO@"+post_order_no);//汇票号码
			authorizeKColl.addDataField("fldvalue10", "BILL_TYPE@"+"");//票据类型
			authorizeKColl.addDataField("fldvalue11", "APPLYER_GLOBAL_TYPE@"+cert_type);//贴现申请人证件类型
			authorizeKColl.addDataField("fldvalue12", "APPLYER_GLOBAL_ID@"+cert_code);//贴现申请人证件号
			authorizeKColl.addDataField("fldvalue13", "APPLYER_ISS_CTRY@"+"CN");//贴现申请人发证国家
			authorizeKColl.addDataField("fldvalue14", "APPLYER_NAME@"+cus_name);//贴现申请人名称
			authorizeKColl.addDataField("fldvalue15", "APPLYER_ACCT_NO@"+"");//贴现申请人户名（目前不传）
			authorizeKColl.addDataField("fldvalue16", "BILL_BRANCH_ID@"+in_acct_br_id);//借据机构代码（取入账机构）
			authorizeKColl.addDataField("fldvalue17", "DISCOUNT_KIND@"+"NORM");//贴现种类:普通贴现
			authorizeKColl.addDataField("fldvalue18", "DISCOUNT_CCY@"+ctrKColl.getDataValue("cont_cur_type"));//币种
			authorizeKColl.addDataField("fldvalue19", "DISCOUNT_AMT@"+ctrKColl.getDataValue("cont_amt"));//贴现金额
			authorizeKColl.addDataField("fldvalue20", "DISCOUNT_DAYS@"+iqpFastCreditDscntKColl.getDataValue("dscnt_day"));//贴现天数
			authorizeKColl.addDataField("fldvalue21", "DISCOUNT_RATE@"+BigDecimalUtil.replaceNull(reality_ir_y));//贴现利率
			authorizeKColl.addDataField("fldvalue22", "TRANSFER_DISCOUNT_RATE@"+"");//转贴现利率
			authorizeKColl.addDataField("fldvalue23", "TRANSFER_DISCOUNT_DATE@"+"");//转贴现日期
			authorizeKColl.addDataField("fldvalue24", "DISCOUNT_INTEREST@"+(cont_amt
					.subtract(BigDecimalUtil.replaceNull(payAmt))).subtract(BigDecimalUtil.replaceNull(arrangr_deduct)));//贴现利息 =合同金额-实付金额-预扣款项
			//核算确认，日期没有填当前日期
			String dscnt_date = (String) iqpFastCreditDscntKColl.getDataValue("dscnt_date");
			if(dscnt_date!=null&&!"".equals(dscnt_date)){
				authorizeKColl.addDataField("fldvalue25", "DISCOUNT_DATE@"+TagUtil.formatDate(dscnt_date));//贴现日期
			}else{
				authorizeKColl.addDataField("fldvalue25", "DISCOUNT_DATE@"+TagUtil.formatDate(nowDate));//贴现日期
			}
			String issue_date = (String) iqpFastCreditDscntKColl.getDataValue("issue_date");
			if(issue_date!=null&&!"".equals(issue_date)){
				authorizeKColl.addDataField("fldvalue26", "BILL_ISSUE_DATE@"+TagUtil.formatDate(issue_date));//出票日期
			}else{
				authorizeKColl.addDataField("fldvalue26", "BILL_ISSUE_DATE@"+TagUtil.formatDate(nowDate));//出票日期
			}
			String bill_end_date = (String) iqpFastCreditDscntKColl.getDataValue("bill_end_date");
			if(bill_end_date!=null&&!"".equals(bill_end_date)){
				authorizeKColl.addDataField("fldvalue27", "BILL_EXPIRY_DATE@"+TagUtil.formatDate(bill_end_date));//票据到期日期
			}else{
				authorizeKColl.addDataField("fldvalue27", "BILL_EXPIRY_DATE@"+TagUtil.formatDate(nowDate));//票据到期日期
			}
			authorizeKColl.addDataField("fldvalue28", "RETURN_DATE@"+"");//约定转回日期
			authorizeKColl.addDataField("fldvalue29", "EXPIRY_DATE@"+TagUtil.formatDate(end_date));//到期日期
			authorizeKColl.addDataField("fldvalue30", "DEDUCT_METHOD@"+"AUTOPAY");//扣款方式
			authorizeKColl.addDataField("fldvalue31", "TO_BRANCH_ID@"+"");//对方机构代码（目前不传）
			authorizeKColl.addDataField("fldvalue37", "DSCNT_INT_PAY_MODE@"+"");//贴现付息方式
			authorizeKColl.addDataField("fldvalue38", "BRANCH_ID@"+in_acct_br_id);//取入账机构
			authorizeKColl.addDataField("fldvalue39", "AGENTOR_NAME@"+"");//代理人名称
			authorizeKColl.addDataField("fldvalue40", "AGENTOR_ACCT_NO@"+"");//代理人账号
			authorizeKColl.addDataField("fldvalue41", "AGENT_ORG_NO@"+"");//代理人开户行行号
			authorizeKColl.addDataField("fldvalue42", "AGENT_ORG_NAME@"+"");//代理人开户行行名
			authorizeKColl.addDataField("fldvalue43", "COUNTER_OPEN_BRANCH_ID@"+"");//对手行行号
			authorizeKColl.addDataField("fldvalue44", "COUNTER_OPEN_BRANCH_NAME@"+"");//对手行行名
			authorizeKColl.addDataField("fldvalue45", "AA_NAME@"+iqpFastCreditDscntKColl.getDataValue("accptr_name"));//承兑人名称
			authorizeKColl.addDataField("fldvalue46", "AAORG_NO@"+"");//承兑行行号
			authorizeKColl.addDataField("fldvalue47", "AAORG_NAME@"+"");//承兑行名称
			authorizeKColl.addDataField("fldvalue48", "PAY_AMT@"+BigDecimalUtil.replaceNull(iqpFastCreditDscntKColl.getDataValue("pay_amt")));//实付金额
			authorizeKColl.addDataField("fldvalue49", "ARRANGR_DEDUCT_OPT@"+BigDecimalUtil.replaceNull(iqpFastCreditDscntKColl.getDataValue("arrangr_deduct")));//预扣款项
			authorizeKColl.addDataField("fldvalue50", "APPLYER_ID@"+cus_id);//贴现申请人编号
			authorizeKColl.addDataField("fldvalue51", "E_CDE@" + "");//是否电票
			authorizeKColl.addDataField("fldvalue52", "BATCH_NO@" + "");//批次号
			authorizeKColl.addDataField("fldvalue53", "CUST_TYP@" + belg_line);//客户类型取所属条线
			authorizeKColl.addDataField("fldvalue54", "DISC_OD_INT_RATE@" + BigDecimalUtil.replaceNull(overdue_rate_y_iqp));//逾期利率
			//modified by yangzy 20151021 类型转换改造 start
			authorizeKColl.addDataField("fldvalue55", "BILL_APP_NAME@" + TagUtil.replaceNull4String(iqpFastCreditDscntKColl.getDataValue("drwr_name")));//出票人名称
			//modified by yangzy 20151021 类型转换改造 end
			authorizeKColl.addDataField("fldvalue56", "ACP_BANK_ACCT_NO@" + "");//承兑人开户行账号
			authorizeKColl.addDataField("fldvalue57", "DSCNT_TYPE@" + "");//贴现方式
			authorizeKColl.addDataField("fldvalue58", "BILL_ACCT_NO@" + "");//出票人账号
			authorizeKColl.addDataField("fldvalue59", "BILL_OPEN_BRANCH_ID@" + "");//出票人开户行行号 
			authorizeKColl.addDataField("fldvalue60", "BILL_OPEN_BRANCH_NAME@" + "");//出票人开户行行名
			dao.insert(authorizeKColl, this.getConnection());
			
			/**生成授权信息：结算账户信息（取自账号信息表iqp_cus_acct）**/
			String conditionCusAcct = "where serno='"+iqpserno+"'";
			IndexedCollection iqpCusAcctIColl = dao.queryList(IQPCUSACCT, conditionCusAcct, this.getConnection());
			int feecount = 0;
			int eactcount = 0;
			for(int m=0;m<iqpCusAcctIColl.size();m++){
				KeyedCollection iqpCusAcctKColl = (KeyedCollection)iqpCusAcctIColl.get(m);
				String acct_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_no"));//账号
				String opac_org_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opac_org_no"));//开户行行号
				String opan_org_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opan_org_name"));//开户行行名
				String is_this_org_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行账户
				String acct_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_name"));//户名
				Double pay_amt = TagUtil.replaceNull4Double(iqpCusAcctKColl.getDataValue("pay_amt"));//受托支付金额
				String acct_gl_code = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_gl_code"));//科目号
				String ccy = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("cur_type"));//币种
				String is_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行
				/*01放款账号  02印花税账号 03收息收款账号    04受托支付账号     05	主办行资金归集账户*/
				String acct_attr = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_attr"));//账户属性
				String interbank_id = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("interbank_id"));//银联行号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + billNo);//借据号
				
				/*PAYM:还款账户 ACTV:放款账号 TURE:委托账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号*/
				if(!"".equals(acct_attr) && acct_attr.equals("01")){
					acct_attr = "ACTV";
				}else if(!"".equals(acct_attr) && acct_attr.equals("02")){
					acct_attr = "STAMP1";
				}else if(!"".equals(acct_attr) && acct_attr.equals("03")){
					acct_attr = "PAYM";
				}else if(!"".equals(acct_attr) && acct_attr.equals("04")){
					if(eactcount==0){
						acct_attr = "EACT";
					}else{
						acct_attr = "EACT"+eactcount;
					}
					eactcount++;
				}else if(!"".equals(acct_attr) && acct_attr.equals("06")){
					acct_attr = "STAMP2";
				}else if(!"".equals(acct_attr) && acct_attr.equals("07")){
					if(feecount==0){
						acct_attr = "FEE";
					}else{
						acct_attr = "FEE"+feecount;
					}
					feecount++;
					acctmap.put(acct_no, acct_attr);
				}else{
					acct_attr = "";
				}
				
				authorizeSubKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + acct_attr);//贷款账户类型 				
				authorizeSubKColl.addDataField("fldvalue04", "ACCT_NO@" + acct_no);//账号
				authorizeSubKColl.addDataField("fldvalue05", "CCY@" + ccy);//币种
//				if(is_this_org_acct.equals("1")){//本行账户
//					authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
//				}else{
//					authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + opac_org_no);//帐号银行代码
//				}
				authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + interbank_id);//帐号银行代码
				authorizeSubKColl.addDataField("fldvalue07", "BRANCH_ID@" + opac_org_no);//机构代码
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_NAME@" + acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "1");//账户类型:1-结算账户 2-保证金				
				authorizeSubKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue17", "ACCT_GL_CODE@" + acct_gl_code);//账号科目代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + pay_amt);//受托支付金额
				authorizeSubKColl.addDataField("fldvalue20", "BANK_NAME@" + opan_org_name);//银行名称
				authorizeSubKColl.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
				authorizeSubKColl.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + is_acct);//是否本行
				
				//开户行地址
				if(interbank_id!=null&&!"".equals(interbank_id)){
					KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, connnection);
					if(kColl!=null){
						authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
					}else{
						authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
					}
				}else{
					authorizeSubKColl.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
				}
				
				dao.insert(authorizeSubKColl, this.getConnection());
			}
		
			/**（7）生成台账信息*/
			KeyedCollection accLoanKColl = new KeyedCollection(ACCLOANMODEL);
			accLoanKColl.addDataField("serno", Pvpserno);//业务编号	
			accLoanKColl.addDataField("acc_day", nowDate);//日期
			accLoanKColl.addDataField("acc_year", nowDate.substring(0, 4));//年份
			accLoanKColl.addDataField("acc_mon", nowDate.substring(5, 7));//月份
			accLoanKColl.addDataField("prd_id", prd_id);//产品编号
			accLoanKColl.addDataField("cus_id", cus_id);//客户编码
			accLoanKColl.addDataField("cont_no", cont_no);//合同编号
			accLoanKColl.addDataField("bill_no", billNo);//借据编号
			accLoanKColl.addDataField("loan_amt", pvp_amt);//贷款金额
			accLoanKColl.addDataField("loan_balance", pvp_amt);//贷款余额
			accLoanKColl.addDataField("distr_date", nowDate);//发放日期
			accLoanKColl.addDataField("end_date", end_date);//到期日期
			accLoanKColl.addDataField("ori_end_date", end_date);//原到期日期
			accLoanKColl.addDataField("post_count", "0");//展期次数
			accLoanKColl.addDataField("overdue", "0");//逾期期数
			accLoanKColl.addDataField("separate_date", "");//清分日期
			accLoanKColl.addDataField("ruling_ir", ruling_ir);//基准利率
			accLoanKColl.addDataField("reality_ir_y", reality_ir_y);//执行年利率
			accLoanKColl.addDataField("overdue_rate_y", overdue_rate_y_iqp);//逾期利率
			accLoanKColl.addDataField("default_rate_y", default_rate_y);//违约利率
			accLoanKColl.addDataField("comp_int_balance", "0");//复利余额
			accLoanKColl.addDataField("inner_owe_int", "0");//表内欠息
			accLoanKColl.addDataField("out_owe_int", "0");//表外欠息
			accLoanKColl.addDataField("rec_int_accum", "0");//应收利息累计
			accLoanKColl.addDataField("recv_int_accum", "0");//实收利息累计
			accLoanKColl.addDataField("normal_balance", pvp_amt);//正常余额
			accLoanKColl.addDataField("overdue_balance", "0");//逾期余额
			accLoanKColl.addDataField("slack_balance", "0");//呆滞余额
			accLoanKColl.addDataField("bad_dbt_balance", "0");//呆账余额
			accLoanKColl.addDataField("writeoff_date", "");//核销日期
			accLoanKColl.addDataField("paydate", "");//转垫款日
			accLoanKColl.addDataField("five_class", five_classfiy);//五级分类
			accLoanKColl.addDataField("twelve_cls_flg", "");//十二级分类
			accLoanKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			accLoanKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
			accLoanKColl.addDataField("acc_status", "0");//台帐状态
			accLoanKColl.addDataField("cur_type", cur_type);//币种
			dao.insert(accLoanKColl, this.getConnection());
		
			/** 7.生成授权信息：费用信息*/
			String conditionFee = "where serno='"+iqpserno+"'";
			IndexedCollection iqpAppendTermsIColl = dao.queryList(IQPFEE, conditionFee, this.getConnection());
			for(int i=0;i<iqpAppendTermsIColl.size();i++){
				KeyedCollection iqpAppendTermsKColl = (KeyedCollection)iqpAppendTermsIColl.get(i);
				String fee_code = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_code"));//费用代码
				String fee_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_cur_type"));//币种
				Double fee_amt = TagUtil.replaceNull4Double(iqpAppendTermsKColl.getDataValue("fee_amt"));//费用金额
				String fee_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_type"));//费用类型
				String fee_rate = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_rate"));//费用比率
				String is_cycle_chrg = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_cycle_chrg"));//是否周期性收费标识 
				String chrg_date = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_date"));//收费日期
				String acct_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_acct_no"));//账号
				String chrg_freq = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_freq"));//账号
				if(chrg_freq.equals("Y")){
					chrg_freq = "12";
				}else if(chrg_freq.equals("Q")){
					chrg_freq = "3";
				}else if(chrg_freq.equals("M")){
					chrg_freq = "1";
				}else{
					chrg_freq = "";
				}
//				String acct_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("acct_name"));//账户名
//				String opac_org_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opac_org_no"));//开户行行号
//				String opan_org_name = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("opan_org_name"));//开户行行名
//				String acct_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("cur_type"));//账户币种
//				String is_this_org_acct = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_this_org_acct"));//是否本行账户
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + "");//借据号：不需赋值
				authorizeSubKColl.addDataField("fldvalue03", "FEE_CODE@" + fee_code);//费用代码				
				authorizeSubKColl.addDataField("fldvalue04", "CCY@" + fee_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
			    authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + TradeConstance.is_pay_flag);//收付标志 默认传 R: 收
				authorizeSubKColl.addDataField("fldvalue07", "FEE_TYPE@" + fee_type);//费用类型
				authorizeSubKColl.addDataField("fldvalue08", "BASE_AMOUNT@" + fee_amt);//基准金额 同费用金额
				authorizeSubKColl.addDataField("fldvalue09", "FEE_RATE@" + fee_rate);//费用比率
				authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + acctmap.get(acct_no));//扣款账户类型  默认传 FEE:收费账户			
				authorizeSubKColl.addDataField("fldvalue11", "CHARGE_PERIODICITY_FLAG@" + this.TransSDicForESB("STD_ZX_YES_NO",is_cycle_chrg));//是否周期性收费标识
				authorizeSubKColl.addDataField("fldvalue12", "CHARGE_DATE@" + chrg_date);//收费日期
				authorizeSubKColl.addDataField("fldvalue13", "AMOR_AMT@" + fee_amt);//摊销金额
				authorizeSubKColl.addDataField("fldvalue14", "CONTRACT_NO@" + cont_no);//协议号
//				authorizeSubKColl.addDataField("fldvalue15", "ACCT_NO@" + acct_no);//账号
//				authorizeSubKColl.addDataField("fldvalue16", "ACCT_NAME@" + acct_name);//账户名
//				authorizeSubKColl.addDataField("fldvalue17", "OPAC_ORG_NO@" + opac_org_no);//开户行行号
//				authorizeSubKColl.addDataField("fldvalue18", "OPAN_ORG_NAME@" + opan_org_name);//开户行行名
//				authorizeSubKColl.addDataField("fldvalue19", "ACCT_CCY@" + acct_cur_type);//账户币种
//				authorizeSubKColl.addDataField("fldvalue20", "IS_THIS_ORG_ACCT@" + is_this_org_acct);//是否本行账户
				authorizeSubKColl.addDataField("fldvalue21", "BRANCH_ID@" + in_acct_br_id);//入账机构
				authorizeSubKColl.addDataField("fldvalue22", "FEE_SPAN@" + chrg_freq);//收费间隔
				dao.insert(authorizeSubKColl, this.getConnection());
			}
		
			/**生成授信信息：保证金信息**/
			KeyedCollection ctrContKColl =  dao.queryDetail(CTRCONTMODEL, cont_no, this.getConnection());
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 start
			BigDecimal security_rate = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("security_rate"));//保证金比例
			//modified by yangzy 2015/07/23 保证金比例科学计算改造 end
			Double security_amt = TagUtil.replaceNull4Double(ctrContKColl.getDataValue("security_amt"));//保证金金额
			String bailCondition = " where serno = '"+iqpserno+"'";
			IndexedCollection bailIColl = dao.queryList("PubBailInfo", bailCondition, connnection);
			for(int i=0;i<bailIColl.size();i++){
				KeyedCollection iqpBailInfoKColl = (KeyedCollection)bailIColl.get(i);
				String bail_acct_no = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_no"));//保证金账号
				String bail_cur_type = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("cur_type"));//币种
				String bail_acct_name = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_name"));//保证金账号名称
				String open_org = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("open_org"));//开户机构
				String bail_acct_gl_code = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_gl_code"));//科目号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				/** 给授权从表赋值 */
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "04");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + "");//借据号：不需赋值
				authorizeSubKColl.addDataField("fldvalue03", "BRANCH_ID@" + open_org);//机构代码
				authorizeSubKColl.addDataField("fldvalue04", "GUARANTEE_ACCT_NO@" + bail_acct_no);//保证金账号
				authorizeSubKColl.addDataField("fldvalue05", "GUARANTEE_NO@" + "");//保证金编号：不需赋值
				authorizeSubKColl.addDataField("fldvalue06", "ACCT_TYPE@" + "");//账户类型：不需赋值
				authorizeSubKColl.addDataField("fldvalue07", "GUARANTEE_EXPIRY_DATE@" + "");//保证金到期日：不需赋值
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_CODE@" + "");//账户代码：不需赋值
				authorizeSubKColl.addDataField("fldvalue09", "CA_TT_FLAG@" + "");//钞汇标志：不需赋值
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_GL_CODE@" + bail_acct_gl_code);//账号科目代码：不需赋值
				authorizeSubKColl.addDataField("fldvalue11", "ACCT_NAME@" + bail_acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue12", "CCY@" + bail_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue13", "GUARANTEE_PER@" + security_rate);//保证金比例
				authorizeSubKColl.addDataField("fldvalue14", "GUARANTEE_AMT@" + security_amt);//保证金金额
				authorizeSubKColl.addDataField("fldvalue15", "CONTRACT_NO@" + cont_no);//协议号
				if("510".equals(assure_main)){
					authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "Y");//是否准全额
				}else{
					authorizeSubKColl.addDataField("fldvalue16", "ALL_PAY_FLAG@" + "N");//是否准全额
				}

				dao.insert(authorizeSubKColl, this.getConnection());
			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl4trade = new KeyedCollection();
//			kColl4trade.put("CLIENT_NO", cus_id);
//			kColl4trade.put("BUSS_SEQ_NO", iqpserno);
//			kColl4trade.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), this.getConnection());	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常！");
		}
	
	}
	
	/**
	 * 转资产受让出账流程审批通过
	 * 出账申请审批通过执行操作：（按照资产清单生成多条资产转让台账）
	 * 1.生成授权信息
	 * 2.生成资产转让台帐信息
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpAsset(String serno)throws ComponentException {
		Connection connnection = null;
		try {
			
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			connnection = this.getConnection();
			
			/** 1.数据准备：获取出账申请信息 */
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, connnection);
			String prd_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("prd_id"));//产品编号
			String cus_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cus_id"));//客户编码
			String cont_no = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cont_no"));//合同编号
			String manager_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("manager_br_id"));//管理机构
			String in_acct_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("in_acct_br_id"));//入账机构
			String cur_type = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cur_type"));//币种
			
			/** 数据准备：获取合同信息 */
			KeyedCollection ctrLoanKColl = dao.queryDetail("CtrAssetstrsfCont", cont_no, connnection);
			
			/** 2.数据准备：查询客户信息 */
			String cus_name = "";
			String[] args = new String[] {"cus_id" };
			String[] modelIds = new String[]{"CusSameOrg"};
			String[] modelForeign = new String[]{"same_org_no"};
			String[] fieldName = new String[]{"same_org_cnname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(pvpLoanKColl, args, SystemTransUtils.ADD, this.getContext(), modelIds,modelForeign, fieldName);
			cus_name = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cus_id_displayname"));//客户名称
			
			/**3.数据准备：获取资产转受让合同信息*/
			KeyedCollection ctrKColl = dao.queryDetail("CtrAssetstrsfCont", cont_no, connnection);
			String asset_no = TagUtil.replaceNull4String(ctrKColl.getDataValue("asset_no"));//资产包编号
			String takeover_type = TagUtil.replaceNull4String(ctrKColl.getDataValue("takeover_type"));//转让方式
			String toorg_no = TagUtil.replaceNull4String(ctrKColl.getDataValue("toorg_no"));//交易对手行号
			String toorg_name = TagUtil.replaceNull4String(ctrKColl.getDataValue("toorg_name"));//交易对手行名
			String topp_acct_no = TagUtil.replaceNull4String(ctrKColl.getDataValue("topp_acct_no"));//交易对手账号
			String topp_acct_name = TagUtil.replaceNull4String(ctrKColl.getDataValue("topp_acct_name"));//交易对手户名
			String tooorg_no = TagUtil.replaceNull4String(ctrKColl.getDataValue("tooorg_no"));//交易对手开户行行号
			String tooorg_name = TagUtil.replaceNull4String(ctrKColl.getDataValue("tooorg_name"));//交易对手开户行行名	
			
			String this_acct_no = TagUtil.replaceNull4String(ctrKColl.getDataValue("this_acct_no"));//本行账户账号
			String this_acct_name = TagUtil.replaceNull4String(ctrKColl.getDataValue("this_acct_name"));//本行账户名称
			String acctsvcr_no = TagUtil.replaceNull4String(ctrKColl.getDataValue("acctsvcr_no"));//开户行行号
			String acctsvcr_name = TagUtil.replaceNull4String(ctrKColl.getDataValue("acctsvcr_name"));//开户行行名		
			
			String acct_curr = TagUtil.replaceNull4String(ctrKColl.getDataValue("acct_curr"));//转让币种
			String asset_total_amt = TagUtil.replaceNull4String(ctrKColl.getDataValue("asset_total_amt"));//资产总额
			String takeover_total_amt = TagUtil.replaceNull4String(ctrKColl.getDataValue("takeover_total_amt"));//转让总金额
			String takeover_total_int = TagUtil.replaceNull4String(ctrKColl.getDataValue("takeover_int"));//转让总利息
			String interest_type = TagUtil.replaceNull4String(ctrKColl.getDataValue("interest_type"));//收息方式
			String takeover_date = TagUtil.replaceNull4String(ctrKColl.getDataValue("takeover_date"));//转让日期			
			String trust_rate = TagUtil.replaceNull4String(ctrKColl.getDataValue("trust_rate"));//委托费率
			String takeover_qnt = TagUtil.replaceNull4String(ctrKColl.getDataValue("takeover_qnt"));//转让笔数
			String is_risk_takeover = TagUtil.replaceNull4String(ctrKColl.getDataValue("is_risk_takeover"));//风险是否转移
			
			
			/**4.数据准备：获取资产包信息*/
			KeyedCollection iqpAssetKColl = dao.queryDetail("IqpAsset", asset_no, connnection);
			String asset_type = (String) iqpAssetKColl.getDataValue("asset_type");//资产类型
			
			/**5.数据准备：获取资产清单*/
			String condition = " where asset_no = '" + asset_no + "'";
			IndexedCollection relIColl = dao.queryList("IqpAssetRel", condition, connnection);
			
			String nowDate = (String)this.getContext().getDataValue(CMISConstance.OPENDAY);
			if(nowDate == null || nowDate.trim().length() == 0){
				throw new EMPException("获取系统营业时间失败！请检查系统营业时间配置是否正确！");
			}
			
			/**6.循环生成授权和台账信息*/
			
			//生成授权编号，所有资产清单授权交易流水在一个授权编号下
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());
			
			for(int i=0;i<relIColl.size();i++){
				/**（1） 获取借据编号*/
				PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
                .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), connnection);
				String billNo = cmisComponent.getBillNoByContNo(cont_no);
				
				KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
				
				/**（2）获取资产清单明细*/
				KeyedCollection relKColl = (KeyedCollection) relIColl.get(i);				
				String ori_cus_id = TagUtil.replaceNull4String(relKColl.getDataValue("cus_id"));//原借款客户编号
				String ori_cus_name = TagUtil.replaceNull4String(relKColl.getDataValue("cus_name"));//原借款客户名称
				String ori_cont_no = TagUtil.replaceNull4String(relKColl.getDataValue("cont_no"));//原合同编号
				String ori_bill_no = TagUtil.replaceNull4String(relKColl.getDataValue("bill_no"));//原借据编号
				String ori_prd_id = TagUtil.replaceNull4String(relKColl.getDataValue("prd_id"));//原产品类型
				String guar_type = TagUtil.replaceNull4String(relKColl.getDataValue("guar_type"));//担保方式
				String guar_desc = TagUtil.replaceNull4String(relKColl.getDataValue("guar_desc"));//担保说明
				String loan_amt = TagUtil.replaceNull4String(relKColl.getDataValue("loan_amt"));//贷款金额
				String loan_bal = TagUtil.replaceNull4String(relKColl.getDataValue("loan_bal"));//贷款余额
				String latest_repay = TagUtil.replaceNull4String(relKColl.getDataValue("latest_repay"));//最近还款日
				String loan_start_date = TagUtil.replaceNull4String(relKColl.getDataValue("loan_start_date"));//原借款起始日期
				String loan_end_date = TagUtil.replaceNull4String(relKColl.getDataValue("loan_end_date"));//原借款到期日期				
				String ir_accord_type = TagUtil.replaceNull4String(relKColl.getDataValue("ir_accord_type")); //利率依据方式
				String ir_type = TagUtil.replaceNull4String(relKColl.getDataValue("ir_type"));//利率种类
				String ir_adjust_type = TagUtil.replaceNull4String(relKColl.getDataValue("ir_adjust_type"));//利率调整方式
				String ruling_ir = TagUtil.replaceNull4String(relKColl.getDataValue("ruling_ir"));//基准利率（年）				
				String ir_float_type = TagUtil.replaceNull4String(relKColl.getDataValue("ir_float_type"));//正常利率浮动方式				
				String ir_float_rate = TagUtil.replaceNull4String(relKColl.getDataValue("ir_float_rate"));//利率浮动比				
				String ir_float_point = TagUtil.replaceNull4String(relKColl.getDataValue("ir_float_point"));//利率浮动点数				
				String reality_ir_y = TagUtil.replaceNull4String(relKColl.getDataValue("reality_ir_y"));//执行利率（年）				
				String overdue_float_type = TagUtil.replaceNull4String(relKColl.getDataValue("overdue_float_type"));//逾期利率浮动方式
				String overdue_rate = TagUtil.replaceNull4String(relKColl.getDataValue("overdue_rate"));//逾期利率浮动比
				String overdue_point = TagUtil.replaceNull4String(relKColl.getDataValue("overdue_point"));//逾期利率浮动点
				String overdue_rate_y = TagUtil.replaceNull4String(relKColl.getDataValue("overdue_rate_y"));//逾期利率（年）				
				String default_float_type = TagUtil.replaceNull4String(relKColl.getDataValue("default_float_type"));//违约利率浮动方式
				String default_rate = TagUtil.replaceNull4String(relKColl.getDataValue("default_rate"));//违约利率浮动比
				String default_point = TagUtil.replaceNull4String(relKColl.getDataValue("default_point"));//违约利率浮动点
				String default_rate_y = TagUtil.replaceNull4String(relKColl.getDataValue("default_rate_y"));//违约利率（年）
				String pad_rate_y = TagUtil.replaceNull4String(relKColl.getDataValue("pad_rate_y"));//垫款利率（年）				
				String repay_type = TagUtil.replaceNull4String(relKColl.getDataValue("repay_type"));//还款方式
				String repay_term = TagUtil.replaceNull4String(relKColl.getDataValue("repay_term"));//还款间隔周期
				String repay_space = TagUtil.replaceNull4String(relKColl.getDataValue("repay_space"));//还款间隔
				String ei_type = TagUtil.replaceNull4String(relKColl.getDataValue("ei_type"));//结息方式
				String five_class = TagUtil.replaceNull4String(relKColl.getDataValue("five_class"));//五级分类
				String takeover_amt = TagUtil.replaceNull4String(relKColl.getDataValue("takeover_amt"));//转让金额
				String takeover_rate = TagUtil.replaceNull4String(relKColl.getDataValue("takeover_rate"));//转让利率
				String takeover_int = TagUtil.replaceNull4String(relKColl.getDataValue("takeover_int"));//转让利息
				String afee_type = TagUtil.replaceNull4String(relKColl.getDataValue("afee_type"));//安排计费方式
				String takeover_frate = TagUtil.replaceNull4String(relKColl.getDataValue("takeover_frate"));//转让费率
				String afee = TagUtil.replaceNull4String(relKColl.getDataValue("afee"));//安排费
				String afee_pay_type = TagUtil.replaceNull4String(relKColl.getDataValue("afee_pay_type"));//安排费支付方式
				String mfee = TagUtil.replaceNull4String(relKColl.getDataValue("mfee"));//管理费
				String int_amt = TagUtil.replaceNull4String(relKColl.getDataValue("int"));//应计利息
				String agent_asset_acct = TagUtil.replaceNull4String(relKColl.getDataValue("agent_asset_acct"));//代理资产资金账号				
				String ir_next_adjust_term = TagUtil.replaceNull4String(relKColl.getDataValue("ir_next_adjust_term")); //下一次利率调整间隔
				String ir_next_adjust_unit = TagUtil.replaceNull4String(relKColl.getDataValue("ir_next_adjust_unit")); //下一次利率调整单位
				String fir_adjust_day = TagUtil.replaceNull4String(relKColl.getDataValue("fir_adjust_day")); //第一次调整日
				String ori_manager_br_id = TagUtil.replaceNull4String(relKColl.getDataValue("manager_br_id")); //清单管理机构
				String ori_fina_br_id = TagUtil.replaceNull4String(relKColl.getDataValue("fina_br_id")); //清单账务机构
				
				/**（3） 生成交易流水号 */
				String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connnection, this.getContext());
				
				/**（4）给授权信息表赋值 */
				authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
				authorizeKColl.addDataField("serno", serno);//业务流水号（出账编号）
				authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
				authorizeKColl.addDataField("prd_id", prd_id);//产品编号
				authorizeKColl.addDataField("cus_id", cus_id);//客户编码--------------------确认使用的是行号
				authorizeKColl.addDataField("cus_name", cus_name);//客户名称
				authorizeKColl.addDataField("cont_no", cont_no);//合同编号
				authorizeKColl.addDataField("bill_no", billNo);//借据编号
				authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_ZYGL + TradeConstance.SERVICE_SCENE_ZCZRSQ);//交易码
				authorizeKColl.addDataField("tran_amt", takeover_amt);//交易金额
				authorizeKColl.addDataField("tran_date", nowDate);//交易日期
				authorizeKColl.addDataField("send_times", "0");//发送次数
				authorizeKColl.addDataField("return_code", "");//返回编码
				authorizeKColl.addDataField("return_desc", "");//返回信息
				authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
				authorizeKColl.addDataField("status", "00");//状态
				authorizeKColl.addDataField("cur_type",cur_type);//币种
				
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编号
				authorizeKColl.addDataField("fldvalue02", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue03", "ORG_NO@" + manager_br_id);//机构码
				authorizeKColl.addDataField("fldvalue04", "ASSET_NO@" + asset_no);//资产包编号
				authorizeKColl.addDataField("fldvalue05", "PRODUCT_NO@" + prd_id);//产品编号
				authorizeKColl.addDataField("fldvalue06", "TRANSFER_MODE@" + takeover_type);//转让方式
				authorizeKColl.addDataField("fldvalue07", "COUNTER_BRANCH_ID@" + toorg_no);//交易对手行号
				authorizeKColl.addDataField("fldvalue08", "COUNTER_BRANCH_NAME@" + toorg_name);//交易对手行名
				authorizeKColl.addDataField("fldvalue09", "COUNTER_ACCT_NO@" + topp_acct_no);//交易对手账号
				authorizeKColl.addDataField("fldvalue10", "COUNTER_ACCT_NAME@" + topp_acct_name);//交易对手户名
				authorizeKColl.addDataField("fldvalue11", "COUNTER_OPEN_BRANCH_ID@" + tooorg_no);//交易对手开户行行号
				authorizeKColl.addDataField("fldvalue12", "COUNTER_OPEN_BRANCH_NAME@" + tooorg_name);//交易对手开户行行名
				authorizeKColl.addDataField("fldvalue13", "ACCT_NO@" + this_acct_no);//本行账户账号
				authorizeKColl.addDataField("fldvalue14", "ACCT_NAME@" + this_acct_name);//本行账户名称
				authorizeKColl.addDataField("fldvalue15", "OPEN_BRANCH_ID@" + acctsvcr_no);//开户行行号
				authorizeKColl.addDataField("fldvalue16", "OPEN_BRANCH_NAME@" + acctsvcr_name);//开户行行名
				authorizeKColl.addDataField("fldvalue17", "CCY@" + acct_curr);//转让币种
				authorizeKColl.addDataField("fldvalue18", "ASSET_TOTAL_AMT@" + asset_total_amt);//资产总额
				authorizeKColl.addDataField("fldvalue19", "TRANSFER_TOTAL_AMT@" + takeover_total_amt);//转让总金额
				authorizeKColl.addDataField("fldvalue20", "TRANSFER_TOTAL_INT@" + takeover_total_int);//转让总利息
				authorizeKColl.addDataField("fldvalue21", "CHARGE_INTEREST_MODE@" + interest_type);//收息方式
				authorizeKColl.addDataField("fldvalue22", "TRANSFER_DATE@" + TagUtil.formatDate(takeover_date));//转让日期
				authorizeKColl.addDataField("fldvalue23", "CONSIGN_FEE_RATE@" + trust_rate);//委托费率
				authorizeKColl.addDataField("fldvalue24", "TRANSFER_CNT@" + takeover_qnt);//转让笔数
				authorizeKColl.addDataField("fldvalue25", "RISK_TRANSFER_FLAG@" + is_risk_takeover);//风险是否转移
				authorizeKColl.addDataField("fldvalue26", "LOAN_AMT_TOTL@" + "");//贷款总金额
				authorizeKColl.addDataField("fldvalue27", "LOAN_BALANCE_TOTL@" + "");//贷款总余额
				authorizeKColl.addDataField("fldvalue28", "TRANS_RATE@" + "");//转让比率
				authorizeKColl.addDataField("fldvalue29", "TRANS_ASSET_TYPE@" + "Z");//业务类型：资产转让：Z 资产流转：L
				
				//借据清单修改为：到交易发送时直接到借据表取
//				authorizeKColl.addDataField("fldvalue26", "DUEBILL_NO@" + billNo);//借据号
//				authorizeKColl.addDataField("fldvalue27", "CONTRACT_NO@" + cont_no);//合同号
//				authorizeKColl.addDataField("fldvalue28", "CLIENT_NO@" + ori_cus_id);//客户编号
//				authorizeKColl.addDataField("fldvalue29", "CLIENT_NAME@" + ori_cus_name);//客户名称
//				authorizeKColl.addDataField("fldvalue30", "ORI_CONTRACT_NO@" + ori_cont_no);//原合同编号
//				authorizeKColl.addDataField("fldvalue31", "ORI_DUEBILL_NO@" + ori_bill_no);//原借据编号
//				authorizeKColl.addDataField("fldvalue32", "LOAN_AMT@" + loan_amt);//贷款金额
//				authorizeKColl.addDataField("fldvalue33", "LOAN_BALANCE@" + loan_bal);//贷款余额
//				authorizeKColl.addDataField("fldvalue34", "LEAST_REPAY_DATE@" + TagUtil.formatDate(latest_repay));//最近还款日
//				authorizeKColl.addDataField("fldvalue35", "ORI_DR_START_DATE@" + TagUtil.formatDate(loan_start_date));//原借款起始日期
//				authorizeKColl.addDataField("fldvalue36", "ORI_DR_END_DATE@" + TagUtil.formatDate(loan_end_date));//原借款到期日期
//				authorizeKColl.addDataField("fldvalue37", "INT_ACCORD_MODE@" + ir_accord_type);//利率依据方式
//				authorizeKColl.addDataField("fldvalue38", "INT_KIND@" + ir_type);//利率种类
//				authorizeKColl.addDataField("fldvalue39", "RADJ_MODE@" + ir_adjust_type);//利率调整方式
//				authorizeKColl.addDataField("fldvalue40", "BASE_INT_RATE@" + ruling_ir);//基准利率（年）
//				authorizeKColl.addDataField("fldvalue41", "INT_FLT_MODE@" + ir_float_type);//正常利率浮动方式
//				authorizeKColl.addDataField("fldvalue42", "INT_FLT_RATE@" + ir_float_rate);//利率浮动比
//				authorizeKColl.addDataField("fldvalue43", "INT_FLT_SPREAD@" + ir_float_point);//利率浮动点数
//				authorizeKColl.addDataField("fldvalue44", "INT_RATE@" + reality_ir_y);//正常利率（年）
//				authorizeKColl.addDataField("fldvalue45", "OVERDUE_FLT_MODE@" + overdue_float_type);//逾期利率浮动方式
//				authorizeKColl.addDataField("fldvalue46", "OVERDUE_FLT_RATE@" + overdue_rate);//逾期利率浮动比
//				authorizeKColl.addDataField("fldvalue47", "OVERDUE_FLT_SPREAD@" + overdue_point);//逾期利率浮动点
//				authorizeKColl.addDataField("fldvalue48", "OVERDUE_INT_RATE@" + overdue_rate_y);//逾期利率（年）
//				authorizeKColl.addDataField("fldvalue49", "PENALTY_FLT_MODE@" + default_float_type);//违约利率浮动方式
//				authorizeKColl.addDataField("fldvalue50", "PENALTY_FLT_RATE@" + default_rate);//违约利率浮动比
//				authorizeKColl.addDataField("fldvalue51", "PENALTY_FLT_SPREAD@" + default_point);//违约利率浮动点
//				authorizeKColl.addDataField("fldvalue52", "PENALTY_INT_RATE@" + default_rate_y);//违约利率（年）
//				authorizeKColl.addDataField("fldvalue53", "OD_INT_RATE@" + pad_rate_y);//垫款利率（年）
//				authorizeKColl.addDataField("fldvalue54", "REPAY_TYPE@" + repay_type);//还款方式
//				authorizeKColl.addDataField("fldvalue55", "REPAY_FREQUENCY_FREQ@" + repay_term);//还款间隔周期
//				authorizeKColl.addDataField("fldvalue56", "REPAY_FREQUENCY@" + repay_space);//还款间隔
//				authorizeKColl.addDataField("fldvalue57", "INT_SETTLE_MODE@" + ei_type);//结息方式
//				authorizeKColl.addDataField("fldvalue58", "TRANSFER_AMT@" + takeover_amt);//转让金额
//				authorizeKColl.addDataField("fldvalue59", "TRANSFER_INT_RATE@" + takeover_rate);//转让利率
//				authorizeKColl.addDataField("fldvalue60", "TRANSFER_INT@" + takeover_int);//转让利息
//				authorizeKColl.addDataField("fldvalue61", "ARRANGE_FEE_MODE@" + afee_type);//安排计费方式
//				authorizeKColl.addDataField("fldvalue62", "TRANSFER_FEE_RATE@" + takeover_frate);//转让费率
//				authorizeKColl.addDataField("fldvalue63", "ARRANGE_COMMISSION@" + afee);//安排费
//				authorizeKColl.addDataField("fldvalue64", "ARRANGE_COMMISSION_PAY_MODE@" + afee_pay_type);//安排费支付方式
//				authorizeKColl.addDataField("fldvalue65", "MANA_FEE@" + mfee);//管理费
//				authorizeKColl.addDataField("fldvalue66", "DUE_COUNT_INTEREST@" + int_amt);//应计利息
//				authorizeKColl.addDataField("fldvalue67", "COR_ASSET_ACCT_NO@" + agent_asset_acct);//代理资产资金账号
//				authorizeKColl.addDataField("fldvalue68", "MANA_ORG@" + manager_br_id);//管理机构
//				authorizeKColl.addDataField("fldvalue69", "ACC_BRANCH_ID@" + in_acct_br_id);//账务机构
//				authorizeKColl.addDataField("fldvalue70", "NEXT_RADJ_FREQ@" + ir_next_adjust_term);//下一次利率调整间隔
//				authorizeKColl.addDataField("fldvalue71", "NEXT_RADJ_UNIT@" + ir_next_adjust_unit);//下一次利率调整单位
//				authorizeKColl.addDataField("fldvalue72", "FIRST_RADJ_DATE@" + fir_adjust_day);//第一次调整日
				
				dao.insert(authorizeKColl, this.getConnection());
				
				/**（5）生成账户信息  默认ACTV:放款账号都传过渡户31510**/
				/* 放款账号过渡户不通过信贷传   2014-04-28
				KeyedCollection pvpAcctKCollACTV = new KeyedCollection(AUTHORIZESUBMODEL);
				pvpAcctKCollACTV.addDataField("auth_no", authSerno);
				pvpAcctKCollACTV.addDataField("busi_cls", "03");//账号信息
				pvpAcctKCollACTV.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编号
				pvpAcctKCollACTV.addDataField("fldvalue02", "DUEBILL_NO@"+billNo);//借据号
				pvpAcctKCollACTV.addDataField("fldvalue03", "LOAN_ACCT_TYPE@"+"ACTV");//系统账户类型
				pvpAcctKCollACTV.addDataField("fldvalue04", "ACCT_NO@"+TradeConstance.BANK_ACCT_NO);//账号：默认全部传对手行账户信息
				pvpAcctKCollACTV.addDataField("fldvalue05", "CCY@"+"CNY");//账户币种：默认人民币
				pvpAcctKCollACTV.addDataField("fldvalue06", "BANK_ID@"+TradeConstance.BANK_ID);//帐号银行代码
				pvpAcctKCollACTV.addDataField("fldvalue07", "BRANCH_ID@"+"");//帐号机构代码
				pvpAcctKCollACTV.addDataField("fldvalue08", "ACCT_NAME@"+"过渡户");//户名：默认全部传对手行账户信息
				pvpAcctKCollACTV.addDataField("fldvalue09", "ISS_CTRY@"+"CN");//发证国家
				pvpAcctKCollACTV.addDataField("fldvalue10", "ACCT_TYPE@"+"");//账户类型
				pvpAcctKCollACTV.addDataField("fldvalue11", "GLOBAL_TYPE@"+"");//证件类型（非必输）
				pvpAcctKCollACTV.addDataField("fldvalue12", "GLOBAL_ID@"+"");//证件号码（非必输）
				pvpAcctKCollACTV.addDataField("fldvalue13", "REMARK@"+"");//备注
				pvpAcctKCollACTV.addDataField("fldvalue14", "CARD_NO@"+"");//卡号
				pvpAcctKCollACTV.addDataField("fldvalue15", "CA_TT_FLAG@"+"");//钞汇标志
				pvpAcctKCollACTV.addDataField("fldvalue16", "ACCT_CODE@"+"");//账户代码
				pvpAcctKCollACTV.addDataField("fldvalue17", "ACCT_GL_CODE@"+"");//账号科目代码
				pvpAcctKCollACTV.addDataField("fldvalue18", "BALANCE@"+"");//账户余额
				pvpAcctKCollACTV.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@"+"");//受托支付发放金额
				pvpAcctKCollACTV.addDataField("fldvalue20", "BANK_NAME@"+"");//银行名称
				pvpAcctKCollACTV.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
				pvpAcctKCollACTV.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
				
				//开户行地址
				pvpAcctKCollACTV.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//是否本行
				
				dao.insert(pvpAcctKCollACTV, connnection);
				*/
				/*
				 1、资产转让根据收息方式传不同的账号：
					资产买入：
				（1）收息方式为自主：
					客户在我行对应科目下开立结算账号，放款账号默认我行过度户账号
					放款：买入时将钱打入过度户，再通过清算系统放款
					还款：还款时直接从结算账号扣款
				（2）收息方式为代收：
					放款：买入时将钱打入过渡户，再通过清算系统放款
					还款：他行代收款项转入过渡户，到期自动从过渡户扣款

					资产卖出：
				（1）收息方式为自主：
					结算账号默认我行过度户账号
					放款：卖出时对手行将钱通过清算打入过度户，信贷放款时直接从过渡户扣款
					还款：客户到对手行直接还款
				（2）收息方式为代收：
					结算账号为客户的收息收款账号
					放款：卖出时对手行将钱通过清算打入过度户，信贷放款时直接从过渡户扣款
					还款：从结算账号扣收，再转到过度户通过清算支付给他行
					
				2014-06-12 修改
				1、卖出，我行代收，需插入TUREA为代理资金账号（信贷传），还款账号原借据账号
				2、卖出，他行自主收息，还款账号传过渡户 
				3、买入，他行代收，还款账号传过渡户
				4、买入，我行自主收息，还款账号为录入结算账号（信贷传）
				 * */
				if("01".equals(takeover_type)){//卖出
					if("2".equals(interest_type)){//代收
						//委托人一般账号
						KeyedCollection authorizeSubKCollCsgn = new KeyedCollection(AUTHORIZESUBMODEL);
						authorizeSubKCollCsgn.addDataField("auth_no", authSerno);//授权编号
						authorizeSubKCollCsgn.addDataField("busi_cls", "03");//业务类别
						authorizeSubKCollCsgn.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
						authorizeSubKCollCsgn.addDataField("fldvalue02", "DUEBILL_NO@" + billNo);//借据号
						/*PAYM:还款账户 ACTV:放款账号 TURE:委托人内部存款账号TUREA:委托人一般账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号*/
						authorizeSubKCollCsgn.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "TUREA");//贷款账户类型 				
						authorizeSubKCollCsgn.addDataField("fldvalue04", "ACCT_NO@" + agent_asset_acct);//账号
						authorizeSubKCollCsgn.addDataField("fldvalue05", "CCY@" + "CNY");//币种
						authorizeSubKCollCsgn.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
						authorizeSubKCollCsgn.addDataField("fldvalue07", "BRANCH_ID@" + "");//机构代码
						authorizeSubKCollCsgn.addDataField("fldvalue08", "ACCT_NAME@" + "代理资产资金账号");//户名
						authorizeSubKCollCsgn.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
						authorizeSubKCollCsgn.addDataField("fldvalue10", "ACCT_TYPE@" + "");//账户类型:1-结算账户 2-保证金				
						authorizeSubKCollCsgn.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
						authorizeSubKCollCsgn.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
						authorizeSubKCollCsgn.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
						authorizeSubKCollCsgn.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
						authorizeSubKCollCsgn.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
						authorizeSubKCollCsgn.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
						authorizeSubKCollCsgn.addDataField("fldvalue17", "ACCT_GL_CODE@" + "");//账号科目代码（目前无此字段）
						authorizeSubKCollCsgn.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
						authorizeSubKCollCsgn.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + "");//受托支付金额
						authorizeSubKCollCsgn.addDataField("fldvalue20", "BANK_NAME@" + "");//银行名称
						authorizeSubKCollCsgn.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
						authorizeSubKCollCsgn.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
						
						//开户行地址
						authorizeSubKCollCsgn.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
						
						dao.insert(authorizeSubKCollCsgn, this.getConnection());
					}
				}else{//买入
					if("1".equals(interest_type)){//自主收息
						KeyedCollection pvpAcctKCollPAYM = new KeyedCollection(AUTHORIZESUBMODEL);
						pvpAcctKCollPAYM.addDataField("auth_no", authSerno);
						pvpAcctKCollPAYM.addDataField("busi_cls", "03");//账号信息
						pvpAcctKCollPAYM.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编号
						pvpAcctKCollPAYM.addDataField("fldvalue02", "DUEBILL_NO@"+billNo);//借据号
						pvpAcctKCollPAYM.addDataField("fldvalue03", "LOAN_ACCT_TYPE@"+"PAYM");//系统账户类型
						pvpAcctKCollPAYM.addDataField("fldvalue04", "ACCT_NO@"+agent_asset_acct);//账号
						pvpAcctKCollPAYM.addDataField("fldvalue05", "CCY@"+"CNY");//账户币种：默认人民币
						pvpAcctKCollPAYM.addDataField("fldvalue06", "BANK_ID@"+TradeConstance.BANK_ID);//帐号银行代码
						pvpAcctKCollPAYM.addDataField("fldvalue07", "BRANCH_ID@"+"");//帐号机构代码
						pvpAcctKCollPAYM.addDataField("fldvalue08", "ACCT_NAME@"+"结算账号");//户名：默认全部传对手行账户信息
						pvpAcctKCollPAYM.addDataField("fldvalue09", "ISS_CTRY@"+"CN");//发证国家
						pvpAcctKCollPAYM.addDataField("fldvalue10", "ACCT_TYPE@"+"");//账户类型
						pvpAcctKCollPAYM.addDataField("fldvalue11", "GLOBAL_TYPE@"+"");//证件类型（非必输）
						pvpAcctKCollPAYM.addDataField("fldvalue12", "GLOBAL_ID@"+"");//证件号码（非必输）
						pvpAcctKCollPAYM.addDataField("fldvalue13", "REMARK@"+"");//备注
						pvpAcctKCollPAYM.addDataField("fldvalue14", "CARD_NO@"+"");//卡号
						pvpAcctKCollPAYM.addDataField("fldvalue15", "CA_TT_FLAG@"+"");//钞汇标志
						pvpAcctKCollPAYM.addDataField("fldvalue16", "ACCT_CODE@"+"");//账户代码
						pvpAcctKCollPAYM.addDataField("fldvalue17", "ACCT_GL_CODE@"+"");//账号科目代码
						pvpAcctKCollPAYM.addDataField("fldvalue18", "BALANCE@"+"");//账户余额
						pvpAcctKCollPAYM.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@"+"");//受托支付发放金额
						pvpAcctKCollPAYM.addDataField("fldvalue20", "BANK_NAME@"+"");//银行名称
						pvpAcctKCollPAYM.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
						pvpAcctKCollPAYM.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
						
						//开户行地址
						pvpAcctKCollPAYM.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//是否本行
						
						dao.insert(pvpAcctKCollPAYM, connnection);
					}
				}
				
				/**（6）费用信息-安排费**/
				/*
				 1、资产转让根据收息方式传不同的费用代码：
					资产买入：（资产转让买入不会出现支出）
				（1）收息方式为自主：
					收入：费用类型F19，fee_code传13
					支出：费用类型F09，fee_code传10
				（2）收息方式为代收：
					收入：费用类型F05，fee_code传11
					支出：费用类型F07，fee_code传12

					资产卖出：（不区分）
				 */
				String fee_code = "";
				if("01".equals(takeover_type)){//卖出
					fee_code = "13";//费用代码：安排费默认13，核算确认
				}else{//买入
					BigDecimal loan_bal_bg = BigDecimalUtil.replaceNull(loan_bal);
					BigDecimal takeover_amt_bg = BigDecimalUtil.replaceNull(takeover_amt);
					if("1".equals(interest_type)){//自主收息
						if(loan_bal_bg.compareTo(takeover_amt_bg)>=0){//收入
							fee_code = "13";
						}else{//支出
							fee_code = "10";
						}
					}else{//代收
						if(loan_bal_bg.compareTo(takeover_amt_bg)>=0){//收入
							fee_code = "11";
						}else{//支出
							fee_code = "12";
						}
					}
				}
				if(afee!=null&&!"".equals(afee)){
					String fee_cur_type = "CNY";//币种
					Double fee_amt = TagUtil.replaceNull4Double(afee);//费用金额
					String fee_type = "116";//费用类型
					String fee_rate = "";//费用比率
					String is_cycle_chrg = "";
					if(afee_pay_type.equals("02")){//分期支付
						is_cycle_chrg = "1";//是否周期性收费标识
					}else{
						is_cycle_chrg = "2";//是否周期性收费标识
					}
					String chrg_date = "";//收费日期
					String acct_no = "";//账号
					String acct_name = "";//账户名
					String opac_org_no = "";//开户行行号
					String opan_org_name = "";//开户行行名
					String acct_cur_type = "CNY";//账户币种
					String is_this_org_acct = "";//是否本行账户
					KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
					authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
					authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
					authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
					authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + billNo);//借据号
					authorizeSubKColl.addDataField("fldvalue03", "FEE_CODE@" + fee_code);//费用代码				
					authorizeSubKColl.addDataField("fldvalue04", "CCY@" + fee_cur_type);//币种
					if(fee_amt<0){
						fee_amt = -fee_amt;
						authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
						authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + "P");//收付标志 默认传 P: 付
					}else{
						authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
						authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + TradeConstance.is_pay_flag);//收付标志 默认传 R: 收
					}
					authorizeSubKColl.addDataField("fldvalue07", "FEE_TYPE@" + fee_type);//费用类型
					authorizeSubKColl.addDataField("fldvalue08", "BASE_AMOUNT@" + fee_amt);//基准金额 同费用金额
					authorizeSubKColl.addDataField("fldvalue09", "FEE_RATE@" + fee_rate);//费用比率
					authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + "ACTV");//资产买入都默认传ACTV类型
					authorizeSubKColl.addDataField("fldvalue11", "CHARGE_PERIODICITY_FLAG@" + this.TransSDicForESB("STD_ZX_YES_NO",is_cycle_chrg));//是否周期性收费标识
					authorizeSubKColl.addDataField("fldvalue12", "CHARGE_DATE@" + chrg_date);//收费日期
					authorizeSubKColl.addDataField("fldvalue13", "AMOR_AMT@" + fee_amt);//摊销金额
					authorizeSubKColl.addDataField("fldvalue14", "CONTRACT_NO@" + cont_no);//协议号
					authorizeSubKColl.addDataField("fldvalue15", "ACCT_NO@" + acct_no);//账号
					authorizeSubKColl.addDataField("fldvalue16", "ACCT_NAME@" + acct_name);//账户名
					authorizeSubKColl.addDataField("fldvalue17", "OPAC_ORG_NO@" + opac_org_no);//开户行行号
					authorizeSubKColl.addDataField("fldvalue18", "OPAN_ORG_NAME@" + opan_org_name);//开户行行名
					authorizeSubKColl.addDataField("fldvalue19", "ACCT_CCY@" + acct_cur_type);//账户币种
					authorizeSubKColl.addDataField("fldvalue20", "IS_THIS_ORG_ACCT@" + is_this_org_acct);//是否本行账户
					authorizeSubKColl.addDataField("fldvalue21", "BRANCH_ID@" + ori_fina_br_id);//入账机构
					authorizeSubKColl.addDataField("fldvalue22", "FEE_SPAN@" + "");//收费间隔
					dao.insert(authorizeSubKColl, this.getConnection());
				}
				
				/**（6）费用信息-管理费**/
				if(mfee!=null&&!"".equals(mfee)){
					String fee_cur_type = "CNY";//币种
					Double fee_amt = TagUtil.replaceNull4Double(mfee);//费用金额
					String fee_type = "116";//费用类型
					String fee_rate = "";//费用比率
					String is_cycle_chrg = "";//是否周期性收费标识
					String chrg_date = "";//收费日期
					String acct_no = "";//账号
					String acct_name = "";//账户名
					String opac_org_no = "";//开户行行号
					String opan_org_name = "";//开户行行名
					String acct_cur_type = "CNY";//账户币种
					String is_this_org_acct = "";//是否本行账户
					KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
					authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
					authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
					authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
					authorizeSubKColl.addDataField("fldvalue02", "DUEBILL_NO@" + billNo);//借据号
					authorizeSubKColl.addDataField("fldvalue03", "FEE_CODE@" + fee_code);//费用代码				
					authorizeSubKColl.addDataField("fldvalue04", "CCY@" + fee_cur_type);//币种
					if(fee_amt<0){
						fee_amt = -fee_amt;
						authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
					    authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + "P");//收付标志 默认传 P: 付
					}else{
						authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
					    authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + TradeConstance.is_pay_flag);//收付标志 默认传 R: 收
					}
					authorizeSubKColl.addDataField("fldvalue07", "FEE_TYPE@" + fee_type);//费用类型
					authorizeSubKColl.addDataField("fldvalue08", "BASE_AMOUNT@" + fee_amt);//基准金额 同费用金额
					authorizeSubKColl.addDataField("fldvalue09", "FEE_RATE@" + fee_rate);//费用比率
					authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + "ACTV");//资产买入都默认传ACTV类型
					authorizeSubKColl.addDataField("fldvalue11", "CHARGE_PERIODICITY_FLAG@" + this.TransSDicForESB("STD_ZX_YES_NO",is_cycle_chrg));//是否周期性收费标识
					authorizeSubKColl.addDataField("fldvalue12", "CHARGE_DATE@" + chrg_date);//收费日期
					authorizeSubKColl.addDataField("fldvalue13", "AMOR_AMT@" + fee_amt);//摊销金额
					authorizeSubKColl.addDataField("fldvalue14", "CONTRACT_NO@" + cont_no);//协议号
					authorizeSubKColl.addDataField("fldvalue15", "ACCT_NO@" + acct_no);//账号
					authorizeSubKColl.addDataField("fldvalue16", "ACCT_NAME@" + acct_name);//账户名
					authorizeSubKColl.addDataField("fldvalue17", "OPAC_ORG_NO@" + opac_org_no);//开户行行号
					authorizeSubKColl.addDataField("fldvalue18", "OPAN_ORG_NAME@" + opan_org_name);//开户行行名
					authorizeSubKColl.addDataField("fldvalue19", "ACCT_CCY@" + acct_cur_type);//账户币种
					authorizeSubKColl.addDataField("fldvalue20", "IS_THIS_ORG_ACCT@" + is_this_org_acct);//是否本行账户
					authorizeSubKColl.addDataField("fldvalue21", "BRANCH_ID@" + ori_fina_br_id);//入账机构
					authorizeSubKColl.addDataField("fldvalue22", "FEE_SPAN@" + "");//收费间隔
					dao.insert(authorizeSubKColl, this.getConnection());
				}
				
				/**（8）生成台账信息**/
				KeyedCollection accAssetKColl = new KeyedCollection("AccAssetstrsf");//资产转让台账表
				accAssetKColl.addDataField("bill_no", billNo);//借据编号
				accAssetKColl.addDataField("cont_no", cont_no);//合同编号
				accAssetKColl.addDataField("asset_no", asset_no);//资产包编号
				accAssetKColl.addDataField("toorg_no", cus_id);//交易对手行号
				accAssetKColl.addDataField("toorg_name", cus_name);//交易对手行名
				accAssetKColl.addDataField("takeover_type", takeover_type);//转让方式
				accAssetKColl.addDataField("asset_type", asset_type);//资产类型
				accAssetKColl.addDataField("is_risk_takeover", is_risk_takeover);//风险是否转移
				accAssetKColl.addDataField("takeover_date", takeover_date);//转让日期
				accAssetKColl.addDataField("cus_id", ori_cus_id);//客户编号
				accAssetKColl.addDataField("cus_name", ori_cus_name);//客户名称
				accAssetKColl.addDataField("ori_cont_no", ori_cont_no);//原合同编号
				accAssetKColl.addDataField("ori_bill_no", ori_bill_no);//原借据编号
				accAssetKColl.addDataField("guar_type", guar_type);//担保方式
				accAssetKColl.addDataField("guar_desc", guar_desc);//担保品说明
				accAssetKColl.addDataField("loan_amt", loan_amt);//贷款金额
				accAssetKColl.addDataField("loan_bal", loan_bal);//贷款余额
				accAssetKColl.addDataField("latest_repay", latest_repay);//最近还款日
				accAssetKColl.addDataField("loan_start_date", loan_start_date);//原借款起始日期
				accAssetKColl.addDataField("loan_end_date", loan_end_date);//原借款到期日期
				accAssetKColl.addDataField("ir_accord_type", ir_accord_type);//利率依据方式
				accAssetKColl.addDataField("ir_type", ir_type);//利率种类
				accAssetKColl.addDataField("ir_adjust_type", ir_adjust_type);//利率调整方式
				accAssetKColl.addDataField("ruling_ir", ruling_ir);//基准利率（年）
				accAssetKColl.addDataField("ir_float_type", ir_float_type);//正常利率浮动方式
				accAssetKColl.addDataField("ir_float_rate", ir_float_rate);//利率浮动比
				accAssetKColl.addDataField("ir_float_point", ir_float_point);//利率浮动点数
				accAssetKColl.addDataField("reality_ir_y", reality_ir_y);//正常利率（年）
				accAssetKColl.addDataField("overdue_float_type", overdue_float_type);//逾期利率浮动方式
				accAssetKColl.addDataField("overdue_rate", overdue_rate);//逾期利率浮动比
				accAssetKColl.addDataField("overdue_point", overdue_point);//逾期利率浮动点
				accAssetKColl.addDataField("overdue_rate_y", overdue_rate_y);//逾期利率（年）
				accAssetKColl.addDataField("default_float_type", default_float_type);//违约利率浮动方式
				accAssetKColl.addDataField("default_rate", default_rate);//违约利率浮动比
				accAssetKColl.addDataField("default_point", default_point);//违约利率浮动点
				accAssetKColl.addDataField("default_rate_y", default_rate_y);//违约利率（年）
				accAssetKColl.addDataField("pad_rate_y", pad_rate_y);//垫款利率（年）
				accAssetKColl.addDataField("repay_type", repay_type);//还款方式
				accAssetKColl.addDataField("repay_term", repay_term);//还款间隔周期
				accAssetKColl.addDataField("repay_space", repay_space);//还款间隔
				accAssetKColl.addDataField("ei_type", ei_type);//结息方式
				accAssetKColl.addDataField("five_class", five_class);//五级分类
				accAssetKColl.addDataField("takeover_amt", takeover_amt);//转让金额
				accAssetKColl.addDataField("takeover_rate", takeover_rate);//转让利率
				accAssetKColl.addDataField("takeover_int", takeover_int);//转让利息
				accAssetKColl.addDataField("afee_type", afee_type);//安排计费方式
				accAssetKColl.addDataField("takeover_frate", takeover_frate);//转让费率
				accAssetKColl.addDataField("afee", afee);//安排费
				accAssetKColl.addDataField("afee_pay_type", afee_pay_type);//安排费支付方式
				accAssetKColl.addDataField("mfee", mfee);//管理费
				accAssetKColl.addDataField("int", int_amt);//应计利息
				accAssetKColl.addDataField("agent_asset_acct", agent_asset_acct);//代理资产资金账号
				accAssetKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				accAssetKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
				accAssetKColl.addDataField("acc_status", "0");//台账状态：出帐未确认
				accAssetKColl.addDataField("ir_next_adjust_term", ir_next_adjust_term);//下一次利率调整间隔
				accAssetKColl.addDataField("ir_next_adjust_unit", ir_next_adjust_unit);//下一次利率调整单位
				accAssetKColl.addDataField("fir_adjust_day", fir_adjust_day);//第一次调整日
				dao.insert(accAssetKColl, this.getConnection());
				
				/**（8）同时往ACCLOAN生成台账信息**/
				if("01".equals(takeover_type)){//卖出
					if("1".equals(interest_type)){//自主收息
						//不往ACCLOAN生成台账
					}else{
						KeyedCollection accLoanKColl = new KeyedCollection(ACCLOANMODEL);
						accLoanKColl.addDataField("serno", serno);//业务编号	
						accLoanKColl.addDataField("acc_day", takeover_date);//日期
						accLoanKColl.addDataField("acc_year", takeover_date.substring(0, 4));//年份
						accLoanKColl.addDataField("acc_mon", takeover_date.substring(5, 7));//月份
						accLoanKColl.addDataField("prd_id", ori_prd_id);//产品编号
						accLoanKColl.addDataField("cus_id", ori_cus_id);//客户编码
						accLoanKColl.addDataField("cont_no", cont_no);//合同编号
						accLoanKColl.addDataField("bill_no", billNo);//借据编号
						accLoanKColl.addDataField("loan_amt", takeover_amt);//贷款金额
						accLoanKColl.addDataField("loan_balance", takeover_amt);//贷款余额
						accLoanKColl.addDataField("distr_date", takeover_date);//发放日期
						accLoanKColl.addDataField("end_date", loan_end_date);//到期日期
						accLoanKColl.addDataField("ori_end_date", loan_end_date);//原到期日期
						accLoanKColl.addDataField("post_count", "0");//展期次数
						accLoanKColl.addDataField("overdue", "0");//逾期期数
						accLoanKColl.addDataField("separate_date", "");//清分日期
						accLoanKColl.addDataField("ruling_ir", ruling_ir);//基准利率
						accLoanKColl.addDataField("reality_ir_y", reality_ir_y);//执行年利率
						accLoanKColl.addDataField("overdue_rate_y", overdue_rate_y);//逾期利率
						accLoanKColl.addDataField("default_rate_y", default_rate_y);//违约利率
						accLoanKColl.addDataField("comp_int_balance", "0");//复利余额
						accLoanKColl.addDataField("inner_owe_int", "0");//表内欠息
						accLoanKColl.addDataField("out_owe_int", "0");//表外欠息
						accLoanKColl.addDataField("rec_int_accum", "0");//应收利息累计
						accLoanKColl.addDataField("recv_int_accum", "0");//实收利息累计
						accLoanKColl.addDataField("normal_balance", takeover_amt);//正常余额
						accLoanKColl.addDataField("overdue_balance", "0");//逾期余额
						accLoanKColl.addDataField("slack_balance", "0");//呆滞余额
						accLoanKColl.addDataField("bad_dbt_balance", "0");//呆账余额
						accLoanKColl.addDataField("writeoff_date", "");//核销日期
						accLoanKColl.addDataField("paydate", "");//转垫款日
						accLoanKColl.addDataField("five_class", five_class);//五级分类
						accLoanKColl.addDataField("twelve_cls_flg", "");//十二级分类
						accLoanKColl.addDataField("manager_br_id", ori_manager_br_id);//管理机构
						accLoanKColl.addDataField("fina_br_id", ori_fina_br_id);//账务机构
						accLoanKColl.addDataField("acc_status", "0");//台帐状态
						accLoanKColl.addDataField("cur_type", cur_type);//币种
						dao.insert(accLoanKColl, this.getConnection());
					}
				}else{//买入
					KeyedCollection accLoanKColl = new KeyedCollection(ACCLOANMODEL);
					accLoanKColl.addDataField("serno", serno);//业务编号	
					accLoanKColl.addDataField("acc_day", takeover_date);//日期
					accLoanKColl.addDataField("acc_year", takeover_date.substring(0, 4));//年份
					accLoanKColl.addDataField("acc_mon", takeover_date.substring(5, 7));//月份
					accLoanKColl.addDataField("prd_id", ori_prd_id);//产品编号
					accLoanKColl.addDataField("cus_id", ori_cus_id);//客户编码
					accLoanKColl.addDataField("cont_no", cont_no);//合同编号
					accLoanKColl.addDataField("bill_no", billNo);//借据编号
					accLoanKColl.addDataField("loan_amt", takeover_amt);//贷款金额
					accLoanKColl.addDataField("loan_balance", takeover_amt);//贷款余额
					accLoanKColl.addDataField("distr_date", takeover_date);//发放日期
					accLoanKColl.addDataField("end_date", loan_end_date);//到期日期
					accLoanKColl.addDataField("ori_end_date", loan_end_date);//原到期日期
					accLoanKColl.addDataField("post_count", "0");//展期次数
					accLoanKColl.addDataField("overdue", "0");//逾期期数
					accLoanKColl.addDataField("separate_date", "");//清分日期
					accLoanKColl.addDataField("ruling_ir", ruling_ir);//基准利率
					accLoanKColl.addDataField("reality_ir_y", reality_ir_y);//执行年利率
					accLoanKColl.addDataField("overdue_rate_y", overdue_rate_y);//逾期利率
					accLoanKColl.addDataField("default_rate_y", default_rate_y);//违约利率
					accLoanKColl.addDataField("comp_int_balance", "0");//复利余额
					accLoanKColl.addDataField("inner_owe_int", "0");//表内欠息
					accLoanKColl.addDataField("out_owe_int", "0");//表外欠息
					accLoanKColl.addDataField("rec_int_accum", "0");//应收利息累计
					accLoanKColl.addDataField("recv_int_accum", "0");//实收利息累计
					accLoanKColl.addDataField("normal_balance", takeover_amt);//正常余额
					accLoanKColl.addDataField("overdue_balance", "0");//逾期余额
					accLoanKColl.addDataField("slack_balance", "0");//呆滞余额
					accLoanKColl.addDataField("bad_dbt_balance", "0");//呆账余额
					accLoanKColl.addDataField("writeoff_date", "");//核销日期
					accLoanKColl.addDataField("paydate", "");//转垫款日
					accLoanKColl.addDataField("five_class", five_class);//五级分类
					accLoanKColl.addDataField("twelve_cls_flg", "");//十二级分类
					accLoanKColl.addDataField("manager_br_id", ori_manager_br_id);//管理机构
					accLoanKColl.addDataField("fina_br_id", ori_fina_br_id);//账务机构
					accLoanKColl.addDataField("acc_status", "0");//台帐状态
					accLoanKColl.addDataField("cur_type", cur_type);//币种
					dao.insert(accLoanKColl, this.getConnection());
				}
			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl4trade = new KeyedCollection();
//			kColl4trade.put("CLIENT_NO", cus_id);
//			kColl4trade.put("BUSS_SEQ_NO", (String)ctrLoanKColl.getDataValue("serno"));
//			kColl4trade.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), this.getConnection());	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	
	/**
	 * 资产流转出账流程审批通过（参考资产转让）
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpAssetTrans(String serno)throws ComponentException {
		Connection connnection = null;
		try {
			
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			connnection = this.getConnection();
			
			/** 1.数据准备：获取出账申请信息 */
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, connnection);
			String prd_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("prd_id"));//产品编号
			String cus_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cus_id"));//客户编码
			String cont_no = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cont_no"));//合同编号
			String manager_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("manager_br_id"));//管理机构
			String in_acct_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("in_acct_br_id"));//入账机构
			String cur_type = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cur_type"));//币种
			
			/** 2.数据准备：查询客户信息 */
			String cus_name = "";
			String[] args = new String[] {"cus_id" };
			String[] modelIds = new String[]{"CusSameOrg"};
			String[] modelForeign = new String[]{"same_org_no"};
			String[] fieldName = new String[]{"same_org_cnname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(pvpLoanKColl, args, SystemTransUtils.ADD, this.getContext(), modelIds,modelForeign, fieldName);
			cus_name = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cus_id_displayname"));//客户名称
			
			/**3.数据准备：获取资产转受让合同信息*/
			KeyedCollection ctrKColl = dao.queryDetail("CtrAssetTransCont", cont_no, connnection);
			String toorg_no = TagUtil.replaceNull4String(ctrKColl.getDataValue("toorg_no"));//交易对手行号
			String loan_amt_totl = TagUtil.replaceNull4String(ctrKColl.getDataValue("loan_balance_totl"));//贷款总金额
			String loan_balance_totl = TagUtil.replaceNull4String(ctrKColl.getDataValue("loan_balance_totl"));//资产总额——贷款总余额
			String trans_amt_totl = TagUtil.replaceNull4String(ctrKColl.getDataValue("trans_amt"));//转让总金额
			String trans_date = TagUtil.replaceNull4String(ctrKColl.getDataValue("trans_date"));//转让日期		
			String trans_qnt = TagUtil.replaceNull4String(ctrKColl.getDataValue("trans_qnt"));//转让笔数
			String trans_rate = TagUtil.replaceNull4String(ctrKColl.getDataValue("trans_rate"));//转让比率
			String end_date = TagUtil.replaceNull4String(ctrKColl.getDataValue("end_date"));//到期日期
			String interest_type = TagUtil.replaceNull4String(ctrKColl.getDataValue("interest_type"));//收息方式
			
			/**5.数据准备：获取资产清单*/
			String condition = " where cont_no = '" + cont_no + "'";
			IndexedCollection relIColl = dao.queryList("IqpAssetTransList", condition, connnection);
			
			String nowDate = (String)this.getContext().getDataValue(CMISConstance.OPENDAY);
			if(nowDate == null || nowDate.trim().length() == 0){
				throw new EMPException("获取系统营业时间失败！请检查系统营业时间配置是否正确！");
			}
			
			/**6.循环生成授权和台账信息*/
			
			//生成授权编号，所有资产清单授权交易流水在一个授权编号下
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());
			
			for(int i=0;i<relIColl.size();i++){
				/**（1） 获取借据编号*/
				PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
                .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), connnection);
				String billNo = cmisComponent.getBillNoByContNo(cont_no);
				
				KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
				
				/**（2）获取资产清单明细*/
				KeyedCollection relKColl = (KeyedCollection) relIColl.get(i);
				String ori_bill_no = TagUtil.replaceNull4String(relKColl.getDataValue("bill_no"));//原借据编号
				String trans_amt = TagUtil.replaceNull4String(relKColl.getDataValue("trans_amt"));//转让总金额
				String agent_asset_acct = TagUtil.replaceNull4String(relKColl.getDataValue("agent_asset_acct"));//代理资产资金账号
				String ori_manager_br_id = TagUtil.replaceNull4String(relKColl.getDataValue("manager_br_id")); //清单管理机构
				String ori_fina_br_id = TagUtil.replaceNull4String(relKColl.getDataValue("fina_br_id")); //清单账务机构
				
				//原借据信息
				String ori_end_date = "";
				String ori_cont_no = "";
				KeyedCollection accpadKColl = dao.queryDetail("AccPad", ori_bill_no, connnection);
				KeyedCollection accloanKColl = dao.queryDetail("AccLoan", ori_bill_no, connnection);
				if(accpadKColl.getDataValue("cont_no")!=null){
					ori_cont_no = (String) accpadKColl.getDataValue("cont_no");
				}else if(accloanKColl.getDataValue("cont_no")!=null){
					ori_cont_no = (String) accloanKColl.getDataValue("cont_no");
				}else{
					ori_cont_no = cont_no;
				}
				if(accloanKColl.getDataValue("end_date")!=null){
					ori_end_date = (String) accloanKColl.getDataValue("end_date");
				}else{
					ori_end_date = end_date;
				}
				
				/**（3） 生成交易流水号 */
				String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connnection, this.getContext());
				
				/**（4）给授权信息表赋值 */
				authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
				authorizeKColl.addDataField("serno", serno);//业务流水号（出账编号）
				authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
				authorizeKColl.addDataField("prd_id", prd_id);//产品编号
				authorizeKColl.addDataField("cus_id", cus_id);//客户编码--------------------确认使用的是行号
				authorizeKColl.addDataField("cus_name", cus_name);//客户名称
				authorizeKColl.addDataField("cont_no", cont_no);//合同编号
				authorizeKColl.addDataField("bill_no", billNo);//借据编号
				authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_ZYGL + TradeConstance.SERVICE_SCENE_ZCZRSQ);//交易码
				authorizeKColl.addDataField("tran_amt", trans_amt_totl);//交易金额
				authorizeKColl.addDataField("tran_date", nowDate);//交易日期
				authorizeKColl.addDataField("send_times", "0");//发送次数
				authorizeKColl.addDataField("return_code", "");//返回编码
				authorizeKColl.addDataField("return_desc", "");//返回信息
				authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
				authorizeKColl.addDataField("status", "00");//状态
				authorizeKColl.addDataField("cur_type",cur_type);//币种
				
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编号
				authorizeKColl.addDataField("fldvalue02", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue03", "ORG_NO@" + manager_br_id);//机构码
				authorizeKColl.addDataField("fldvalue04", "ASSET_NO@" + "");//资产包编号
				authorizeKColl.addDataField("fldvalue05", "PRODUCT_NO@" + prd_id);//产品编号
				authorizeKColl.addDataField("fldvalue06", "TRANSFER_MODE@" + "01");//转让方式：资产流转默认为：转出卖断式
				authorizeKColl.addDataField("fldvalue07", "COUNTER_BRANCH_ID@" + toorg_no);//交易对手行号
				authorizeKColl.addDataField("fldvalue08", "COUNTER_BRANCH_NAME@" + cus_name);//交易对手行名
				authorizeKColl.addDataField("fldvalue09", "COUNTER_ACCT_NO@" + "");//交易对手账号
				authorizeKColl.addDataField("fldvalue10", "COUNTER_ACCT_NAME@" + "");//交易对手户名
				authorizeKColl.addDataField("fldvalue11", "COUNTER_OPEN_BRANCH_ID@" + "");//交易对手开户行行号
				authorizeKColl.addDataField("fldvalue12", "COUNTER_OPEN_BRANCH_NAME@" + "");//交易对手开户行行名
				authorizeKColl.addDataField("fldvalue13", "ACCT_NO@" + "");//本行账户账号
				authorizeKColl.addDataField("fldvalue14", "ACCT_NAME@" + "");//本行账户名称
				authorizeKColl.addDataField("fldvalue15", "OPEN_BRANCH_ID@" + "");//开户行行号
				authorizeKColl.addDataField("fldvalue16", "OPEN_BRANCH_NAME@" + "");//开户行行名
				authorizeKColl.addDataField("fldvalue17", "CCY@" + cur_type);//转让币种
				authorizeKColl.addDataField("fldvalue18", "ASSET_TOTAL_AMT@" + loan_balance_totl);//资产总额
				authorizeKColl.addDataField("fldvalue19", "TRANSFER_TOTAL_AMT@" + trans_amt_totl);//转让总金额
				authorizeKColl.addDataField("fldvalue20", "TRANSFER_TOTAL_INT@" + "");//转让总利息
				authorizeKColl.addDataField("fldvalue21", "CHARGE_INTEREST_MODE@" + interest_type);//收息方式
				authorizeKColl.addDataField("fldvalue22", "TRANSFER_DATE@" + TagUtil.formatDate(trans_date));//转让日期
				authorizeKColl.addDataField("fldvalue23", "CONSIGN_FEE_RATE@" + "");//委托费率
				authorizeKColl.addDataField("fldvalue24", "TRANSFER_CNT@" + trans_qnt);//转让笔数
				authorizeKColl.addDataField("fldvalue25", "RISK_TRANSFER_FLAG@" + "");//风险是否转移
				authorizeKColl.addDataField("fldvalue26", "LOAN_AMT_TOTL@" + loan_amt_totl);//贷款总金额
				authorizeKColl.addDataField("fldvalue27", "LOAN_BALANCE_TOTL@" + loan_balance_totl);//贷款总余额
				authorizeKColl.addDataField("fldvalue28", "TRANS_RATE@" + trans_rate);//转让比率
				authorizeKColl.addDataField("fldvalue29", "TRANS_ASSET_TYPE@" + "L");//业务类型：资产转让：Z 资产流转：L
				
				dao.insert(authorizeKColl, this.getConnection());
				
				/**（5）生成账户信息  默认ACTV:放款账号都传过渡户31510**/
				/* 放款账号过渡户不通过信贷传   2014-04-28
				KeyedCollection pvpAcctKCollACTV = new KeyedCollection(AUTHORIZESUBMODEL);
				pvpAcctKCollACTV.addDataField("auth_no", authSerno);
				pvpAcctKCollACTV.addDataField("busi_cls", "03");//账号信息
				pvpAcctKCollACTV.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编号
				pvpAcctKCollACTV.addDataField("fldvalue02", "DUEBILL_NO@"+billNo);//借据号
				pvpAcctKCollACTV.addDataField("fldvalue03", "LOAN_ACCT_TYPE@"+"ACTV");//系统账户类型
				pvpAcctKCollACTV.addDataField("fldvalue04", "ACCT_NO@"+TradeConstance.BANK_ACCT_NO);//账号：默认全部传对手行账户信息
				pvpAcctKCollACTV.addDataField("fldvalue05", "CCY@"+"CNY");//账户币种：默认人民币
				pvpAcctKCollACTV.addDataField("fldvalue06", "BANK_ID@"+TradeConstance.BANK_ID);//帐号银行代码
				pvpAcctKCollACTV.addDataField("fldvalue07", "BRANCH_ID@"+"");//帐号机构代码
				pvpAcctKCollACTV.addDataField("fldvalue08", "ACCT_NAME@"+"过渡户");//户名：默认全部传对手行账户信息
				pvpAcctKCollACTV.addDataField("fldvalue09", "ISS_CTRY@"+"CN");//发证国家
				pvpAcctKCollACTV.addDataField("fldvalue10", "ACCT_TYPE@"+"");//账户类型
				pvpAcctKCollACTV.addDataField("fldvalue11", "GLOBAL_TYPE@"+"");//证件类型（非必输）
				pvpAcctKCollACTV.addDataField("fldvalue12", "GLOBAL_ID@"+"");//证件号码（非必输）
				pvpAcctKCollACTV.addDataField("fldvalue13", "REMARK@"+"");//备注
				pvpAcctKCollACTV.addDataField("fldvalue14", "CARD_NO@"+"");//卡号
				pvpAcctKCollACTV.addDataField("fldvalue15", "CA_TT_FLAG@"+"");//钞汇标志
				pvpAcctKCollACTV.addDataField("fldvalue16", "ACCT_CODE@"+"");//账户代码
				pvpAcctKCollACTV.addDataField("fldvalue17", "ACCT_GL_CODE@"+"");//账号科目代码
				pvpAcctKCollACTV.addDataField("fldvalue18", "BALANCE@"+"");//账户余额
				pvpAcctKCollACTV.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@"+"");//受托支付发放金额
				pvpAcctKCollACTV.addDataField("fldvalue20", "BANK_NAME@"+"");//银行名称
				pvpAcctKCollACTV.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
				pvpAcctKCollACTV.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
				
				//开户行地址
				pvpAcctKCollACTV.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//是否本行
				dao.insert(pvpAcctKCollACTV, connnection);
				
				KeyedCollection pvpAcctKCollPAYM = new KeyedCollection(AUTHORIZESUBMODEL);
				pvpAcctKCollPAYM.addDataField("auth_no", authSerno);
				pvpAcctKCollPAYM.addDataField("busi_cls", "03");//账号信息
				pvpAcctKCollPAYM.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编号
				pvpAcctKCollPAYM.addDataField("fldvalue02", "DUEBILL_NO@"+billNo);//借据号
				pvpAcctKCollPAYM.addDataField("fldvalue03", "LOAN_ACCT_TYPE@"+"PAYM");//系统账户类型
				pvpAcctKCollPAYM.addDataField("fldvalue04", "ACCT_NO@"+TradeConstance.BANK_ACCT_NO);//账号
				pvpAcctKCollPAYM.addDataField("fldvalue05", "CCY@"+"CNY");//账户币种：默认人民币
				pvpAcctKCollPAYM.addDataField("fldvalue06", "BANK_ID@"+TradeConstance.BANK_ID);//帐号银行代码
				pvpAcctKCollPAYM.addDataField("fldvalue07", "BRANCH_ID@"+"");//帐号机构代码
				pvpAcctKCollPAYM.addDataField("fldvalue08", "ACCT_NAME@"+"过渡户");//户名：默认全部传对手行账户信息
				pvpAcctKCollPAYM.addDataField("fldvalue09", "ISS_CTRY@"+"CN");//发证国家
				pvpAcctKCollPAYM.addDataField("fldvalue10", "ACCT_TYPE@"+"");//账户类型
				pvpAcctKCollPAYM.addDataField("fldvalue11", "GLOBAL_TYPE@"+"");//证件类型（非必输）
				pvpAcctKCollPAYM.addDataField("fldvalue12", "GLOBAL_ID@"+"");//证件号码（非必输）
				pvpAcctKCollPAYM.addDataField("fldvalue13", "REMARK@"+"");//备注
				pvpAcctKCollPAYM.addDataField("fldvalue14", "CARD_NO@"+"");//卡号
				pvpAcctKCollPAYM.addDataField("fldvalue15", "CA_TT_FLAG@"+"");//钞汇标志
				pvpAcctKCollPAYM.addDataField("fldvalue16", "ACCT_CODE@"+"");//账户代码
				pvpAcctKCollPAYM.addDataField("fldvalue17", "ACCT_GL_CODE@"+"");//账号科目代码
				pvpAcctKCollPAYM.addDataField("fldvalue18", "BALANCE@"+"");//账户余额
				pvpAcctKCollPAYM.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@"+"");//受托支付发放金额
				pvpAcctKCollPAYM.addDataField("fldvalue20", "BANK_NAME@"+"");//银行名称
				pvpAcctKCollPAYM.addDataField("fldvalue21", "CONTRACT_NO@" + cont_no);//协议号
				pvpAcctKCollPAYM.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
				
				//开户行地址
				pvpAcctKCollPAYM.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//是否本行
				dao.insert(pvpAcctKCollPAYM, connnection);
				*/
				
				//委托人一般账号
				KeyedCollection authorizeSubKCollCsgn = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKCollCsgn.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKCollCsgn.addDataField("busi_cls", "03");//业务类别
				authorizeSubKCollCsgn.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKCollCsgn.addDataField("fldvalue02", "DUEBILL_NO@" + billNo);//借据号
				/*PAYM:还款账户 ACTV:放款账号 TURE:委托人内部存款账号TUREA:委托人一般账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号*/
				authorizeSubKCollCsgn.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "TUREA");//贷款账户类型 				
				authorizeSubKCollCsgn.addDataField("fldvalue04", "ACCT_NO@" + agent_asset_acct);//账号
				authorizeSubKCollCsgn.addDataField("fldvalue05", "CCY@" + "CNY");//币种
				authorizeSubKCollCsgn.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
				authorizeSubKCollCsgn.addDataField("fldvalue07", "BRANCH_ID@" + "");//机构代码
				authorizeSubKCollCsgn.addDataField("fldvalue08", "ACCT_NAME@" + "代理资产资金账号");//户名
				authorizeSubKCollCsgn.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
				authorizeSubKCollCsgn.addDataField("fldvalue10", "ACCT_TYPE@" + "");//账户类型:1-结算账户 2-保证金				
				authorizeSubKCollCsgn.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue17", "ACCT_GL_CODE@" + "");//账号科目代码（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + "");//受托支付金额
				authorizeSubKCollCsgn.addDataField("fldvalue20", "BANK_NAME@" + "");//银行名称
				authorizeSubKCollCsgn.addDataField("fldvalue21", "CONTRACT_NO@" + ori_cont_no);//协议号
				authorizeSubKCollCsgn.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
				
				//开户行地址
				authorizeSubKCollCsgn.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
				dao.insert(authorizeSubKCollCsgn, this.getConnection());
				
				/**（8）生成台账信息**/
				KeyedCollection accAssetKColl = new KeyedCollection("AccAssetTrans");//资产台账
				accAssetKColl.addDataField("bill_no", billNo);//借据编号
				accAssetKColl.addDataField("cont_no", cont_no);//合同编号
				accAssetKColl.addDataField("asset_type", "01");//资产类型：资产流转
				accAssetKColl.addDataField("rebuy_date", "");//赎回日期
				accAssetKColl.addDataField("acc_status", "0");//资产状态：出帐未确认
				accAssetKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				accAssetKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
				accAssetKColl.addDataField("ori_bill_no", ori_bill_no);//原借据编号
				dao.insert(accAssetKColl, this.getConnection());
				
				//往acc_loan插入新借据
				KeyedCollection accKColl = dao.queryDetail("AccLoan", ori_bill_no, connnection);
				accKColl.setDataValue("bill_no", billNo);//新主键
				accKColl.setDataValue("loan_amt", trans_amt);
				accKColl.setDataValue("loan_balance", trans_amt);
				accKColl.setDataValue("prd_id", prd_id);
				accKColl.setDataValue("acc_status", "0");//出帐未确认
				accKColl.setDataValue("distr_date", nowDate);//发放日期
				accKColl.setDataValue("end_date", ori_end_date);//到期日期
				accKColl.setDataValue("ori_end_date", ori_end_date);//原到期日期
				accKColl.setDataValue("post_count", "0");//展期次数
				accKColl.setDataValue("overdue", "0");//逾期期数
				accKColl.setDataValue("separate_date", "");//清分日期
				accKColl.setDataValue("comp_int_balance", "0");//复利余额
				accKColl.setDataValue("inner_owe_int", "0");//表内欠息
				accKColl.setDataValue("out_owe_int", "0");//表外欠息
				accKColl.setDataValue("rec_int_accum", "0");//应收利息累计
				accKColl.setDataValue("recv_int_accum", "0");//实收利息累计
				accKColl.setDataValue("normal_balance", trans_amt);//正常余额
				accKColl.setDataValue("overdue_balance", "0");//逾期余额
				accKColl.setDataValue("slack_balance", "0");//呆滞余额
				accKColl.setDataValue("bad_dbt_balance", "0");//呆账余额
				accKColl.setDataValue("writeoff_date", "");//核销日期
				accKColl.setDataValue("paydate", "");//转垫款日
				accKColl.setDataValue("twelve_cls_flg", "");//十二级分类
				accKColl.setDataValue("manager_br_id", ori_manager_br_id);//管理机构
				accKColl.setDataValue("fina_br_id", ori_fina_br_id);//账务机构
				dao.insert(accKColl, connnection);
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 资产证券化出账流程审批通过（参考资产转让）
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpAssetPro(String serno)throws ComponentException {
		Connection connnection = null;
		try {
			
			if(serno == null || serno == ""){
				throw new EMPException("流程审批结束处理类获取业务流水号失败！");
			}
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			connnection = this.getConnection();
			
			/** 1.数据准备：获取出账申请信息 */
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, connnection);
			String prd_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("prd_id"));//产品编号
//			String cus_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cus_id"));//客户编码
			String cont_no = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cont_no"));//合同编号
			String manager_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("manager_br_id"));//管理机构
			String in_acct_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("in_acct_br_id"));//入账机构
			String cur_type = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cur_type"));//币种
			
//			/** 2.数据准备：查询客户信息 */
//			String cus_name = "";
//			String[] args = new String[] {"cus_id" };
//			String[] modelIds = new String[]{"CusSameOrg"};
//			String[] modelForeign = new String[]{"same_org_no"};
//			String[] fieldName = new String[]{"same_org_cnname"};
//			//详细信息翻译时调用			
//			SystemTransUtils.dealName(pvpLoanKColl, args, SystemTransUtils.ADD, this.getContext(), modelIds,modelForeign, fieldName);
//			cus_name = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cus_id_displayname"));//客户名称
			
			/**3.数据准备：获取资产转受让合同信息*/
			KeyedCollection ctrKColl = dao.queryDetail("CtrAssetProCont", cont_no, connnection);
			String pro_amt_totl = TagUtil.replaceNull4String(ctrKColl.getDataValue("pro_amt"));//项目金额	
			String pro_qnt = TagUtil.replaceNull4String(ctrKColl.getDataValue("pro_qnt"));//笔数
			String issue_date = TagUtil.replaceNull4String(ctrKColl.getDataValue("issue_date"));//发行日期
			String end_date = TagUtil.replaceNull4String(ctrKColl.getDataValue("end_date"));//到期日期
			
			/**5.数据准备：获取资产清单*/
			String condition = " where cont_no = '" + cont_no + "'";
			IndexedCollection relIColl = dao.queryList("IqpAssetProList", condition, connnection);
			
			String nowDate = (String)this.getContext().getDataValue(CMISConstance.OPENDAY);
			if(nowDate == null || nowDate.trim().length() == 0){
				throw new EMPException("获取系统营业时间失败！请检查系统营业时间配置是否正确！");
			}
			
			/**6.循环生成授权和台账信息*/
			
			//生成授权编号，所有资产清单授权交易流水在一个授权编号下
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());
			
			for(int i=0;i<relIColl.size();i++){
				/**（1） 获取借据编号*/
				PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
                .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), connnection);
				String billNo = cmisComponent.getBillNoByContNo(cont_no);
				
				KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
				
				/**（2）获取资产清单明细*/
				KeyedCollection relKColl = (KeyedCollection) relIColl.get(i);
				String ori_bill_no = TagUtil.replaceNull4String(relKColl.getDataValue("bill_no"));//原借据编号
				KeyedCollection regikc = dao.queryFirst("IqpAssetRegiApp", null, " where bill_no = '"+ori_bill_no+"'", connnection);
				String loan_balance = TagUtil.replaceNull4String(regikc.getDataValue("loan_balance"));//资产余额
				String agent_asset_acct = TagUtil.replaceNull4String(relKColl.getDataValue("agent_asset_acct"));//代理资产资金账号
				String ori_manager_br_id = TagUtil.replaceNull4String(relKColl.getDataValue("manager_br_id")); //清单管理机构
				String ori_fina_br_id = TagUtil.replaceNull4String(relKColl.getDataValue("fina_br_id")); //清单账务机构
				
				//原借据信息
				String ori_end_date = "";
				String ori_cont_no = "";
				KeyedCollection accpadKColl = dao.queryDetail("AccPad", ori_bill_no, connnection);
				KeyedCollection accloanKColl = dao.queryDetail("AccLoan", ori_bill_no, connnection);
				if(accpadKColl.getDataValue("cont_no")!=null){
					ori_cont_no = (String) accpadKColl.getDataValue("cont_no");
				}else if(accloanKColl.getDataValue("cont_no")!=null){
					ori_cont_no = (String) accloanKColl.getDataValue("cont_no");
				}else{
					ori_cont_no = cont_no;
				}
				if(accloanKColl.getDataValue("end_date")!=null){
					ori_end_date = (String) accloanKColl.getDataValue("end_date");
				}else{
					ori_end_date = end_date;
				}
				
				/**（3） 生成交易流水号 */
				String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connnection, this.getContext());
				
				/**（4）给授权信息表赋值 */
				authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
				authorizeKColl.addDataField("serno", serno);//业务流水号（出账编号）
				authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
				authorizeKColl.addDataField("prd_id", prd_id);//产品编号
				authorizeKColl.addDataField("cus_id", "");//客户编码--------------------确认使用的是行号
				authorizeKColl.addDataField("cus_name", "");//客户名称
				authorizeKColl.addDataField("cont_no", cont_no);//合同编号
				authorizeKColl.addDataField("bill_no", billNo);//借据编号
				authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_ZYGL + TradeConstance.SERVICE_SCENE_ZCZRSQ);//交易码
				authorizeKColl.addDataField("tran_amt", pro_amt_totl);//交易金额
				authorizeKColl.addDataField("tran_date", nowDate);//交易日期
				authorizeKColl.addDataField("send_times", "0");//发送次数
				authorizeKColl.addDataField("return_code", "");//返回编码
				authorizeKColl.addDataField("return_desc", "");//返回信息
				authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
				authorizeKColl.addDataField("status", "00");//状态
				authorizeKColl.addDataField("cur_type",cur_type);//币种
				
				authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//出账授权编号
				authorizeKColl.addDataField("fldvalue02", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
				authorizeKColl.addDataField("fldvalue03", "ORG_NO@" + manager_br_id);//机构码
				authorizeKColl.addDataField("fldvalue04", "ASSET_NO@" + "");//资产包编号
				authorizeKColl.addDataField("fldvalue05", "PRODUCT_NO@" + prd_id);//产品编号
				authorizeKColl.addDataField("fldvalue06", "TRANSFER_MODE@" + "01");//转让方式：资产流转默认为：转出卖断式
				authorizeKColl.addDataField("fldvalue07", "COUNTER_BRANCH_ID@" + "");//交易对手行号
				authorizeKColl.addDataField("fldvalue08", "COUNTER_BRANCH_NAME@" + "");//交易对手行名
				authorizeKColl.addDataField("fldvalue09", "COUNTER_ACCT_NO@" + "");//交易对手账号
				authorizeKColl.addDataField("fldvalue10", "COUNTER_ACCT_NAME@" + "");//交易对手户名
				authorizeKColl.addDataField("fldvalue11", "COUNTER_OPEN_BRANCH_ID@" + "");//交易对手开户行行号
				authorizeKColl.addDataField("fldvalue12", "COUNTER_OPEN_BRANCH_NAME@" + "");//交易对手开户行行名
				authorizeKColl.addDataField("fldvalue13", "ACCT_NO@" + "");//本行账户账号
				authorizeKColl.addDataField("fldvalue14", "ACCT_NAME@" + "");//本行账户名称
				authorizeKColl.addDataField("fldvalue15", "OPEN_BRANCH_ID@" + "");//开户行行号
				authorizeKColl.addDataField("fldvalue16", "OPEN_BRANCH_NAME@" + "");//开户行行名
				authorizeKColl.addDataField("fldvalue17", "CCY@" + cur_type);//转让币种
				authorizeKColl.addDataField("fldvalue18", "ASSET_TOTAL_AMT@" + pro_amt_totl);//资产总额
				authorizeKColl.addDataField("fldvalue19", "TRANSFER_TOTAL_AMT@" + pro_amt_totl);//转让总金额
				authorizeKColl.addDataField("fldvalue20", "TRANSFER_TOTAL_INT@" + "");//转让总利息
				authorizeKColl.addDataField("fldvalue21", "CHARGE_INTEREST_MODE@" + "");//收息方式
				authorizeKColl.addDataField("fldvalue22", "TRANSFER_DATE@" + TagUtil.formatDate(issue_date));//转让日期
				authorizeKColl.addDataField("fldvalue23", "CONSIGN_FEE_RATE@" + "");//委托费率
				authorizeKColl.addDataField("fldvalue24", "TRANSFER_CNT@" + pro_qnt);//转让笔数
				authorizeKColl.addDataField("fldvalue25", "RISK_TRANSFER_FLAG@" + "");//风险是否转移
				authorizeKColl.addDataField("fldvalue26", "LOAN_AMT_TOTL@" + pro_amt_totl);//贷款总金额
				authorizeKColl.addDataField("fldvalue27", "LOAN_BALANCE_TOTL@" + pro_amt_totl);//贷款总余额
				authorizeKColl.addDataField("fldvalue28", "TRANS_RATE@" + "");//转让比率
				authorizeKColl.addDataField("fldvalue29", "TRANS_ASSET_TYPE@" + "Q");//业务类型：资产转让：Z 资产流转：L 资产证券化：Q
				
				dao.insert(authorizeKColl, this.getConnection());
				
				/**（5）生成账户信息  默认ACTV:放款账号都传过渡户31510**/
				
				//委托人一般账号
				KeyedCollection authorizeSubKCollCsgn = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKCollCsgn.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKCollCsgn.addDataField("busi_cls", "03");//业务类别
				authorizeSubKCollCsgn.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKCollCsgn.addDataField("fldvalue02", "DUEBILL_NO@" + billNo);//借据号
				/*PAYM:还款账户 ACTV:放款账号 TURE:委托人内部存款账号TUREA:委托人一般账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号*/
				authorizeSubKCollCsgn.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "TUREA");//贷款账户类型 				
				authorizeSubKCollCsgn.addDataField("fldvalue04", "ACCT_NO@" + agent_asset_acct);//账号
				authorizeSubKCollCsgn.addDataField("fldvalue05", "CCY@" + "CNY");//币种
				authorizeSubKCollCsgn.addDataField("fldvalue06", "BANK_ID@" + TradeConstance.BANK_ID);//帐号银行代码
				authorizeSubKCollCsgn.addDataField("fldvalue07", "BRANCH_ID@" + "");//机构代码
				authorizeSubKCollCsgn.addDataField("fldvalue08", "ACCT_NAME@" + "代理资产资金账号");//户名
				authorizeSubKCollCsgn.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
				authorizeSubKCollCsgn.addDataField("fldvalue10", "ACCT_TYPE@" + "");//账户类型:1-结算账户 2-保证金				
				authorizeSubKCollCsgn.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue17", "ACCT_GL_CODE@" + "");//账号科目代码（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
				authorizeSubKCollCsgn.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + "");//受托支付金额
				authorizeSubKCollCsgn.addDataField("fldvalue20", "BANK_NAME@" + "");//银行名称
				authorizeSubKCollCsgn.addDataField("fldvalue21", "CONTRACT_NO@" + ori_cont_no);//协议号
				authorizeSubKCollCsgn.addDataField("fldvalue22", "OWN_BRANCH_FLAG@" + "1");//是否本行
				
				//开户行地址
				authorizeSubKCollCsgn.addDataField("fldvalue23", "ACCT_BANK_ADD@" + "");//地址
				dao.insert(authorizeSubKCollCsgn, this.getConnection());
				
				/**（8）生成台账信息**/
				KeyedCollection accAssetKColl = new KeyedCollection("AccAssetTrans");//资产台账
				accAssetKColl.addDataField("bill_no", billNo);//借据编号
				accAssetKColl.addDataField("cont_no", cont_no);//合同编号
				accAssetKColl.addDataField("asset_type", "02");//资产类型：资产证券化
				accAssetKColl.addDataField("rebuy_date", "");//赎回日期
				accAssetKColl.addDataField("acc_status", "0");//资产状态：出帐未确认
				accAssetKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				accAssetKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
				accAssetKColl.addDataField("ori_bill_no", ori_bill_no);//原借据编号
				dao.insert(accAssetKColl, this.getConnection());
				
				//往acc_loan插入新借据
				KeyedCollection accKColl = dao.queryDetail("AccLoan", ori_bill_no, connnection);
				accKColl.setDataValue("bill_no", billNo);//新主键
				accKColl.setDataValue("cont_no", ori_cont_no);
				accKColl.setDataValue("loan_amt", loan_balance);
				accKColl.setDataValue("loan_balance", loan_balance);
				accKColl.setDataValue("prd_id", prd_id);
				accKColl.setDataValue("acc_status", "0");//出帐未确认
				accKColl.setDataValue("distr_date", nowDate);//发放日期
				accKColl.setDataValue("end_date", ori_end_date);//到期日期
				accKColl.setDataValue("ori_end_date", ori_end_date);//原到期日期
				accKColl.setDataValue("post_count", "0");//展期次数
				accKColl.setDataValue("overdue", "0");//逾期期数
				accKColl.setDataValue("separate_date", "");//清分日期
				accKColl.setDataValue("comp_int_balance", "0");//复利余额
				accKColl.setDataValue("inner_owe_int", "0");//表内欠息
				accKColl.setDataValue("out_owe_int", "0");//表外欠息
				accKColl.setDataValue("rec_int_accum", "0");//应收利息累计
				accKColl.setDataValue("recv_int_accum", "0");//实收利息累计
				accKColl.setDataValue("normal_balance", loan_balance);//正常余额
				accKColl.setDataValue("overdue_balance", "0");//逾期余额
				accKColl.setDataValue("slack_balance", "0");//呆滞余额
				accKColl.setDataValue("bad_dbt_balance", "0");//呆账余额
				accKColl.setDataValue("writeoff_date", "");//核销日期
				accKColl.setDataValue("paydate", "");//转垫款日
				accKColl.setDataValue("twelve_cls_flg", "");//十二级分类
				accKColl.setDataValue("manager_br_id", ori_manager_br_id);//管理机构
				accKColl.setDataValue("fina_br_id", ori_fina_br_id);//账务机构
				dao.insert(accKColl, connnection);
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**
	 * 贸易融资表外业务出账申请审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForIqpTfBw(String serno) throws ComponentException{
		
		try{
			if(serno == null || serno == ""){
				throw new ComponentException("流程审批结束处理类获取业务流水号失败！");
			}
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			/** 1.数据准备：通过业务流水号查询【出账申请】 */
			KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL, serno, this.getConnection());
			String Pvpserno = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("serno"));//授权交易流水号
			String prd_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("prd_id"));//产品编号
			String cus_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cus_id"));//客户编码
			String cont_no = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cont_no"));//合同编号
			BigDecimal pvp_amt = BigDecimalUtil.replaceNull(pvpLoanKColl.getDataValue("pvp_amt"));//出账金额
			String manager_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("manager_br_id"));//管理机构
			String in_acct_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("in_acct_br_id"));//入账机构
			String cur_type = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("cur_type"));//币种
			
			/** 2.数据准备：通过业务流水号查询【合同信息】 */					
			KeyedCollection ctrContKColl =  dao.queryDetail(CTRCONTMODEL, cont_no, this.getConnection());
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, cont_no, this.getConnection());
			String loanSerno = TagUtil.replaceNull4String(ctrContKColl.getDataValue("serno"));//业务申请流水号
			String ruling_ir = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir"));//基准利率（年）
			String ir_float_rate = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_rate"));//浮动比例
			String ir_float_point = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_point"));//浮动点数
			String reality_ir_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("reality_ir_y"));//执行利率（年）
			String five_classfiy = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("five_classfiy"));//五级分类
			String cont_end_date = TagUtil.replaceNull4String(ctrContKColl.getDataValue("cont_end_date"));//合同到期日期
			
			/**4.数据准备：保证金信息**/
			String condition = "where serno='" + loanSerno + "'";
			IndexedCollection iqpBailInfoIColl = dao.queryList(PUBBAILINFO, condition, this.getConnection());
			String bail_acct_no = "";//保证金账号
			String bail_cur_type = "";//保证金币种
			if(iqpBailInfoIColl != null && iqpBailInfoIColl.size() > 0){
				KeyedCollection iqpBailInfoKColl = (KeyedCollection)iqpBailInfoIColl.get(0);
				bail_acct_no = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_no"));
				bail_cur_type = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("cur_type"));
			}else{
				bail_cur_type = "CNY";
			}
			
			//计算保证金金额  start
			/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			//KeyedCollection kCollRate = service.getHLByCurrType(cur_type, this.getContext(), this.getConnection());
			//if("failed".equals(kCollRate.getDataValue("flag"))){
			//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			//}
			//KeyedCollection kCollRateSecurity = service.getHLByCurrType(bail_cur_type, this.getContext(), this.getConnection());
			//if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
			//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			//}
			//BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率，先不乘汇率
			//BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("exchange_rate"));//汇率，先不乘汇率
			BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("security_exchange_rate"));//保证金币种汇率
			/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			
			//国内信用证，进口开证
			BigDecimal floodact_perc_bg = new BigDecimal(0);//溢装比例
			if(prd_id != null &&(prd_id.equals("700020") || prd_id.equals("700021"))){
				KeyedCollection IqpCreditKColl = dao.queryDetail(IQPCREDIT, loanSerno, this.getConnection());
				if(IqpCreditKColl != null){
					floodact_perc_bg = BigDecimalUtil.replaceNull(IqpCreditKColl.getDataValue("floodact_perc"));
				}
			}
			BigDecimal security_rate = BigDecimalUtil.replaceNull(ctrContKColl.getDataValue("security_rate")); //保证金比例
			BigDecimal pvp_amt_bg = BigDecimalUtil.replaceNull(pvp_amt); //出账金额
			
			//金额*保证金比例*（1+溢装比例）*合同汇率/保证金汇率
			//计算结果进百
			//进百后乘保证金汇率
			BigDecimal securityAmt = pvp_amt_bg.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc_bg)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
			java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			String caculateAmt = String.valueOf(securityAmt);
			securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
			String changeAmt = nf.format(securityAmt);
			securityAmt = BigDecimalUtil.replaceNull(changeAmt);
			//计算保证金金额  end
			
			Double pad_rate_y = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("pad_rate_y"));//垫款利率

			/** 3.数据准备：相关从表信息 */
			String cdt_cert_no = ""; //信用证号码:从提货担保从表中获取
			String shortact_perc = "";//短装比例：从信用证从表中获取
			String floodact_perc = "";//溢装比例：从信用证从表中获取
			String is_revolv_credit = "";//循环信用证标识:从信用证从表中获取
			String is_internal_cert = "";//信用证标志：从信用证从表中获取
			String end_date ="";//效期
			//提货担保
			if(prd_id != null && prd_id.equals("500032")){
				KeyedCollection IqpDelivAssureKColl = dao.queryDetail(IQPDELIVASSURE, loanSerno, this.getConnection());
				if(IqpDelivAssureKColl != null){
					cdt_cert_no = TagUtil.replaceNull4String(IqpDelivAssureKColl.getDataValue("cdt_cert_no"));//信用证号码
				}
			}
			//国内信用证，进口开证
			if(prd_id != null &&(prd_id.equals("700020") || prd_id.equals("700021"))){
				KeyedCollection IqpCreditKColl = dao.queryDetail(IQPCREDIT, loanSerno, this.getConnection());
				if(IqpCreditKColl != null){
					shortact_perc = TagUtil.replaceNull4String(IqpCreditKColl.getDataValue("shortact_perc"));
					floodact_perc = TagUtil.replaceNull4String(IqpCreditKColl.getDataValue("floodact_perc"));
					is_revolv_credit = TagUtil.replaceNull4String(IqpCreditKColl.getDataValue("is_revolv_credit"));
					end_date = TagUtil.replaceNull4String(IqpCreditKColl.getDataValue("end_date")); 
					if(is_revolv_credit != null && is_revolv_credit.equals("1")){
						is_revolv_credit = "01";
					}else if(is_revolv_credit != null && is_revolv_credit.equals("2")){
						is_revolv_credit = "02";
					}else{
						throw new ComponentException("获取【是否循环信用证】为空！");
					}
					
					if("700020".equals(prd_id)){
						is_internal_cert = "01";
					}else if("700021".equals(prd_id)){
						is_internal_cert = "02";
					}
				}
			}
			//外汇保函
			if(prd_id != null && prd_id.equals("400020")){
				KeyedCollection IqpGuarantInfoKColl = dao.queryDetail(IqpGuarantInfo, loanSerno, this.getConnection());
				if(IqpGuarantInfoKColl != null){
					end_date = TagUtil.replaceNull4String(IqpGuarantInfoKColl.getDataValue("end_date")); 
				}
			}
			
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
			CusBase cusBase = csi.getCusBaseByCusId(cus_id,this.getContext(),this.getConnection());
			String cus_name = TagUtil.replaceNull4String(cusBase.getCusName());//客户名称
			
			
			/**5.数据准备：生成借据编号*/
			PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
            .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), this.getConnection());
			String bill_no = cmisComponent.getBillNoByContNo(cont_no);//借据编号
			String date = this.getContext().getDataValue(PUBConstant.OPENDAY).toString();//营业日期
			Integer days = TimeUtil.getBetweenDays(date, cont_end_date);//期限
			
			
			/**6.生成授权主表信息**/
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());//生成交易流水号
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());//生成授权编号
			authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
			authorizeKColl.addDataField("serno", Pvpserno);//业务流水号（出账编号）
			authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
			authorizeKColl.addDataField("prd_id", prd_id);//产品编号
			authorizeKColl.addDataField("cus_id", cus_id);//客户编码
			authorizeKColl.addDataField("cus_name", cus_name);//客户名称
			authorizeKColl.addDataField("cont_no", cont_no);//合同编号
			authorizeKColl.addDataField("bill_no", bill_no);//借据编号
			authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_GJ + TradeConstance.SERVICE_SCENE_GJSQ);
			authorizeKColl.addDataField("tran_amt", pvp_amt);//交易金额
			authorizeKColl.addDataField("tran_date", date);//交易日期
			authorizeKColl.addDataField("send_times", "0");//发送次数
			authorizeKColl.addDataField("return_code", "");//返回编码
			authorizeKColl.addDataField("return_desc", "");//返回信息
			authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
			authorizeKColl.addDataField("status", "00");//状态
			authorizeKColl.addDataField("cur_type", cur_type);//币种
			
			authorizeKColl.addDataField("fldvalue01", "DUEBILL_NO@" + bill_no);//借据号
			authorizeKColl.addDataField("fldvalue02", "CONTRACT_NO@" + cont_no);//合同号
			authorizeKColl.addDataField("fldvalue03", "CLIENT_NO@" + cus_id);//客户码
			authorizeKColl.addDataField("fldvalue04", "INCOME_ORG_NO@" + in_acct_br_id);//入账机构
			authorizeKColl.addDataField("fldvalue05", "REGIST_ORG_NO@" + manager_br_id );//登记机构（业务的管理机构）			
			authorizeKColl.addDataField("fldvalue06", "TRAN_AMT@" + pvp_amt);//交易金额
			authorizeKColl.addDataField("fldvalue07", "TRAN_CCY@" + cur_type);//交易币种			
			authorizeKColl.addDataField("fldvalue08", "GUARANTEE_PER@" + security_rate);//保证金比例
			authorizeKColl.addDataField("fldvalue09", "GUARANTEE_ACCT_NO@" + bail_acct_no);//保证金账号
			authorizeKColl.addDataField("fldvalue10", "GUARANTEE_AMT@" + securityAmt);//保证金金额
			authorizeKColl.addDataField("fldvalue11", "GUARANTEE_CCY@" + bail_cur_type);//保证金币种			
			authorizeKColl.addDataField("fldvalue12", "LC_TYPE@" + is_internal_cert);//信用证种类:01- 国内信用证 02- 国际信用证			
			authorizeKColl.addDataField("fldvalue13", "IS_CYCLE_FLAG@" + is_revolv_credit);//循环信用证标识:01- 是 02-否			
			authorizeKColl.addDataField("fldvalue14", "DR_DATE@" + TagUtil.formatDate(date));//出账日期			
			authorizeKColl.addDataField("fldvalue15", "VALUE_DATE@" + "");//起息日期			
			authorizeKColl.addDataField("fldvalue16", "EFF_PERIOD@" + TagUtil.formatDate(end_date));//有效期限			
			authorizeKColl.addDataField("fldvalue17", "TERM@" +  days);//期限:天数
			authorizeKColl.addDataField("fldvalue18", "LC_OVERFLOW_RATE@" + floodact_perc);//溢装比例：信用证业务专用，其他业务送空值。
			authorizeKColl.addDataField("fldvalue19", "LC_REDUCE_RATE@" + shortact_perc);//短装比例：信用证业务专用，其他业务送空值。			
			authorizeKColl.addDataField("fldvalue20", "LC_NO@" + cdt_cert_no);//信用证号码:提货担保和海外代付专用，其他业务送空值。			
			authorizeKColl.addDataField("fldvalue21", "SETTLE_MODE@" + "");//结算方式:海外代付专用，其他业务送空值			
			authorizeKColl.addDataField("fldvalue22", "NORMAL_INTEREST@" + "");//正常利率:海外代付专用，其他业务送空值			
			authorizeKColl.addDataField("fldvalue23", "OD_INT_RATE@" + pad_rate_y);//垫款利率			
			String buss_kind = "";//业务种类[01 信用证 02 外汇保函 03 提货担保 04 海外代付]
			if(prd_id != null && (prd_id.equals("700020") || prd_id.equals("700021"))){
				buss_kind = "01";
			}else if(prd_id != null && prd_id.equals("400020")){
				buss_kind = "02";
			}else if(prd_id != null && prd_id.equals("500032")){
				buss_kind = "03";
			}
			authorizeKColl.addDataField("fldvalue24", "BUSS_KIND@" + buss_kind);//业务种类
			authorizeKColl.addDataField("fldvalue25", "BAK_FIELD@" + "");//备用字段
			authorizeKColl.addDataField("fldvalue26", "BAK_FIELD2@" + "");//备用字段2
			dao.insert(authorizeKColl, this.getConnection());
			
			
			/**7.生成台账信息 */
			//判断是否为信用证业务
			if("700020".equals(prd_id) || "700021".equals(prd_id)){
				BigDecimal floodact = new BigDecimal(floodact_perc);
				pvp_amt = pvp_amt.multiply(new BigDecimal(1).add(floodact));
			}
			KeyedCollection accLoanKColl = new KeyedCollection(ACCLOANMODEL);
			accLoanKColl.addDataField("serno", Pvpserno);//业务编号	
			accLoanKColl.addDataField("acc_day", date);//日期
			accLoanKColl.addDataField("acc_year", date.substring(0, 4));//年份
			accLoanKColl.addDataField("acc_mon", date.substring(5, 7));//月份
			accLoanKColl.addDataField("prd_id", prd_id);//产品编号
			accLoanKColl.addDataField("cus_id", cus_id);//客户编码
			accLoanKColl.addDataField("cont_no", cont_no);//合同编号
			accLoanKColl.addDataField("bill_no", bill_no);//借据编号
			accLoanKColl.addDataField("loan_amt", pvp_amt);//贷款金额
			accLoanKColl.addDataField("loan_balance", pvp_amt);//贷款余额
			accLoanKColl.addDataField("distr_date", date);//发放日期
			accLoanKColl.addDataField("end_date", cont_end_date);//到期日期
			accLoanKColl.addDataField("ori_end_date", cont_end_date);//原到期日期
			accLoanKColl.addDataField("post_count", "0");//展期次数
			accLoanKColl.addDataField("overdue", "0");//逾期期数
			accLoanKColl.addDataField("separate_date", "");//清分日期
			accLoanKColl.addDataField("ruling_ir", ruling_ir);//基准利率
			accLoanKColl.addDataField("ir_float_rate", ir_float_rate);//利率浮动比
			accLoanKColl.addDataField("ir_float_point", ir_float_point);//利率浮动点数
			accLoanKColl.addDataField("reality_ir_y", reality_ir_y);//执行年利率
			accLoanKColl.addDataField("comp_int_balance", "0");//复利余额
			accLoanKColl.addDataField("inner_owe_int", "0");//表内欠息
			accLoanKColl.addDataField("out_owe_int", "0");//表外欠息
			accLoanKColl.addDataField("rec_int_accum", "0");//应收利息累计
			accLoanKColl.addDataField("recv_int_accum", "0");//实收利息累计
			accLoanKColl.addDataField("normal_balance", pvp_amt);//正常余额
			accLoanKColl.addDataField("overdue_balance", "0");//逾期余额
			accLoanKColl.addDataField("slack_balance", "0");//呆滞余额
			accLoanKColl.addDataField("bad_dbt_balance", "0");//呆账余额
			accLoanKColl.addDataField("writeoff_date", "");//核销日期
			accLoanKColl.addDataField("paydate", "");//转垫款日
			accLoanKColl.addDataField("five_class", five_classfiy);//五级分类
			accLoanKColl.addDataField("twelve_cls_flg", "");//十二级分类
			accLoanKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			accLoanKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
			accLoanKColl.addDataField("acc_status", "0");//台帐状态
			accLoanKColl.addDataField("cur_type", cur_type);//币种
			dao.insert(accLoanKColl, this.getConnection());
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl4trade = new KeyedCollection();
//			kColl4trade.put("CLIENT_NO", cus_id);
//			kColl4trade.put("BUSS_SEQ_NO", loanSerno);
//			kColl4trade.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), this.getConnection());	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch(Exception e){
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常！" + e.getMessage());
		}
	}
	
	
	
	private String TransSDicForESB(String opttype,String enname)throws ComponentException{
		String esbenname = "";
		
		if(opttype.equals("STD_DRFT_TYPE")){//票据类型
			if(enname.equals("100")){
				esbenname = "DD";
			}else if(enname.equals("200")){
				esbenname = "CD";
			}
		}
		
		if(opttype.equals("STD_ZX_YES_NO")){//票据类型
			if(enname.equals("1")){
				esbenname = "Y";
			}else if(enname.equals("2")){
				esbenname = "N";
			}
		}
		
		return esbenname;
	}
	
	
	/**
	 * 更新业务合同余额
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void deductContBalance(String serno) throws ComponentException{
		Double balance = 0.00;
		Double pvp_amt = 0.00;
		String cont_no = null;
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			KeyedCollection kCollPvp = dao.queryDetail(PVPLOANMODEL, serno, this.getConnection());
			cont_no = (String)kCollPvp.getDataValue("cont_no");
			pvp_amt = Double.valueOf(kCollPvp.getDataValue("pvp_amt")+"");
			
			KeyedCollection contKColl = dao.queryDetail(CTRCONTMODEL,cont_no, this.getConnection());
			String cont_balance=(String) contKColl.getDataValue("cont_balance");
			//计算合同余额
			balance = Double.valueOf(cont_balance)-Double.valueOf(pvp_amt);
			//往合同中更新合同余额
			contKColl.setDataValue("cont_balance", balance);
			int result = dao.update(contKColl, this.getConnection()); 
			if(result!=1){
				 throw new EMPException("更新合同余额失败");
			}
		} catch (EMPException e) {
			e.printStackTrace();
			throw new ComponentException("更新合同余额失败");	
		}  
	}
	
	/**
	 * 授权撤销，贷款类、境内保函、贷款承诺、银行信贷证明
	 * 交易成功后，进行如下处理：（1）将授权信息状态更新为“出账撤销”（2）将对应的台账信息删除 
	 * @param tran_serno
	 * @throws ComponentException
	 */
	public void doCancelAuthorizeForLoan(String tran_serno)throws ComponentException{
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection auKColl = dao.queryDetail(AUTHORIZEMODEL, tran_serno, this.getConnection());
			String authorize_no = (String) auKColl.getDataValue("authorize_no");
			BigDecimal tran_amt = BigDecimalUtil.replaceNull(auKColl.getDataValue("tran_amt"));
			String cont_no = (String) auKColl.getDataValue("cont_no");
			
			//更新合同余额
			SqlClient.update("updateContBalanceByContNo", cont_no, tran_amt, null, this.getConnection());
			
			SqlClient.executeUpd("updatePvpAuthorizeForCancel", authorize_no, "03", null, this.getConnection());
			
			SqlClient.delete("deleteAccLoanForCancel", authorize_no, this.getConnection());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("撤销授权，更新业务数据失败");	
		}
	}
	
	/**
	 * 授权撤销，银承类
	 * 交易成功后，进行如下处理：（1）将授权信息状态更新为“出账撤销”（2）将对应的台账信息删除 
	 * @param tran_serno
	 * @throws ComponentException
	 */
	public void doCancelAuthorizeForAccp(String tran_serno)throws ComponentException{
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection auKColl = dao.queryDetail(AUTHORIZEMODEL, tran_serno, this.getConnection());
			String authorize_no = (String) auKColl.getDataValue("authorize_no");
			
			String cont_no = (String) auKColl.getDataValue("cont_no");
			KeyedCollection ctrKColl = dao.queryDetail("CtrLoanCont", cont_no, this.getConnection());
			BigDecimal tran_amt = BigDecimalUtil.replaceNull(ctrKColl.getDataValue("cont_amt"));
			
			//更新合同余额
			SqlClient.update("updateContBalanceByContNo", cont_no, tran_amt, null, this.getConnection());
			
			
			SqlClient.executeUpd("updatePvpAuthorizeForCancel", authorize_no, "03", null, this.getConnection());
			
			SqlClient.delete("deleteAccAccpForCancel", authorize_no, this.getConnection());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("撤销授权，更新业务数据失败");	
		}
	}
	
	/**
	 * 授权撤销，贴现类
	 * 交易成功后，进行如下处理：（1）将授权信息状态更新为“出账撤销”（2）将对应的台账信息删除 
	 * @param tran_serno
	 * @throws ComponentException
	 */
	public void doCancelAuthorizeForDisc(String tran_serno)throws ComponentException{
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection auKColl = dao.queryDetail(AUTHORIZEMODEL, tran_serno, this.getConnection());
			String authorize_no = (String) auKColl.getDataValue("authorize_no");
			String cont_no = (String) auKColl.getDataValue("cont_no");
			String condition = " where cont_no = '"+cont_no+"'";
			KeyedCollection batKColl = dao.queryFirst("IqpBatchMng", null, condition, this.getConnection());
			String batch_no = (String) batKColl.getDataValue("batch_no");
			KeyedCollection ctrKColl = dao.queryDetail("CtrLoanCont", cont_no, this.getConnection());
			BigDecimal tran_amt = BigDecimalUtil.replaceNull(ctrKColl.getDataValue("cont_amt"));
			
//票据状态等都是在成功通知后去修改，因此不需去动票据状态   2014-03-10
//			//修改票据包状态,02-已引用
//			SqlClient.executeUpd("updateIqpBatchMngForCancel", batch_no, "02", null, this.getConnection());
//			
//			//修改票据状态,01-登记
//			SqlClient.executeUpd("updateIqpBillDetailForCancel", batch_no, "01", null, this.getConnection());
			
			//更新合同余额
			SqlClient.update("updateContBalanceByContNo", cont_no, tran_amt, null, this.getConnection());
			
			//修改授权表状态
			SqlClient.executeUpd("updatePvpAuthorizeForCancel", authorize_no, "03", null, this.getConnection());
			
			//删除台账信息
			SqlClient.delete("deleteAccDrftForCancel", authorize_no, this.getConnection());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("撤销授权，更新业务数据失败");	
		}
	}
	
	/**
	 * 授权撤销，转贴现类
	 * 交易成功后，进行如下处理：（1）将授权信息状态更新为“出账撤销”（2）将对应的台账信息删除 
	 * @param tran_serno
	 * @throws ComponentException
	 */
	public void doCancelAuthorizeForRpddscnt(String tran_serno)throws ComponentException{
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection auKColl = dao.queryDetail(AUTHORIZEMODEL, tran_serno, this.getConnection());
			String authorize_no = (String) auKColl.getDataValue("authorize_no");
			String cont_no = (String) auKColl.getDataValue("cont_no");
			String condition = " where cont_no = '"+cont_no+"'";
			KeyedCollection batKColl = dao.queryFirst("IqpBatchMng", null, condition, this.getConnection());
			String batch_no = (String) batKColl.getDataValue("batch_no");
			String biz_type = (String) batKColl.getDataValue("biz_type");
	
//票据状态等都是在成功通知后去修改，因此不需去动票据状态   2014-03-10
//			//修改票据包状态,02-已引用
//			SqlClient.executeUpd("updateIqpBatchMngForCancel", batch_no, "02", null, this.getConnection());
//			
//			if(biz_type.equals("01")||biz_type.equals("02")){
//				//修改票据状态,01-登记
//				SqlClient.executeUpd("updateIqpBillDetailForCancel", batch_no, "01", null, this.getConnection());
//			}else if(biz_type.equals("03")||biz_type.equals("04")||biz_type.equals("06")){
//				//修改票据状态,02-持有
//				SqlClient.executeUpd("updateIqpBillDetailForCancel", batch_no, "02", null, this.getConnection());
//			}
//			//内转不改票据状态，保持持有状态
			
			//修改授权表状态
			SqlClient.executeUpd("updatePvpAuthorizeForCancel", authorize_no, "03", null, this.getConnection());
			
			//删除台账信息
			SqlClient.delete("deleteAccDrftForCancel", authorize_no, this.getConnection());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("撤销授权，更新业务数据失败");	
		}
	}
	
	/**
	 * 授权撤销，资产转受让类
	 * 交易成功后，进行如下处理：（1）将授权信息状态更新为“出账撤销”（2）将对应的台账信息删除  （3）将对应的贷款台账信息删除  
	 * @param tran_serno
	 * @throws ComponentException
	 */
	public void doCancelAuthorizeForAsset(String tran_serno)throws ComponentException{
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection auKColl = dao.queryDetail(AUTHORIZEMODEL, tran_serno, this.getConnection());
			String authorize_no = (String) auKColl.getDataValue("authorize_no");
			
			//修改授权表状态
			SqlClient.executeUpd("updatePvpAuthorizeForCancel", authorize_no, "03", null, this.getConnection());
			
			//删除台账信息
			SqlClient.delete("deleteAccAssetstrsfForCancel", authorize_no, this.getConnection());
			
			//删除贷款台账信息
			SqlClient.delete("deleteAccLoanForCancel", authorize_no, this.getConnection());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("撤销授权，更新业务数据失败");	
		}
	}
	
	/**
	 * 授权撤销，资产流转
	 * 交易成功后，进行如下处理：（1）将授权信息状态更新为“出账撤销”（2）将对应的台账信息删除   （3）将对应的贷款台账信息删除  
	 * @param tran_serno
	 * @throws ComponentException
	 */
	public void doCancelAuthorizeForAssetTrans(String tran_serno)throws ComponentException{
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection auKColl = dao.queryDetail(AUTHORIZEMODEL, tran_serno, this.getConnection());
			String authorize_no = (String) auKColl.getDataValue("authorize_no");
			
			//修改授权表状态
			SqlClient.executeUpd("updatePvpAuthorizeForCancel", authorize_no, "03", null, this.getConnection());
			
			//删除台账信息
			SqlClient.delete("deleteAccAssetTransForCancel", authorize_no, this.getConnection());
			
			//删除贷款台账信息
			SqlClient.delete("deleteAccLoanForCancel", authorize_no, this.getConnection());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("撤销授权，更新业务数据失败");	
		}
	}
	
	/**
	 * 授权撤销，资产流转
	 * 交易成功后，进行如下处理：（1）将授权信息状态更新为“出账撤销”（2）将对应的台账信息删除   （3）将对应的贷款台账信息删除  
	 * @param tran_serno
	 * @throws ComponentException
	 */
	public void doCancelAuthorizeForAssetPro(String tran_serno)throws ComponentException{
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection auKColl = dao.queryDetail(AUTHORIZEMODEL, tran_serno, this.getConnection());
			String authorize_no = (String) auKColl.getDataValue("authorize_no");
			
			//修改授权表状态
			SqlClient.executeUpd("updatePvpAuthorizeForCancel", authorize_no, "03", null, this.getConnection());
			
			//删除台账信息
			SqlClient.delete("deleteAccAssetTransForCancel", authorize_no, this.getConnection());
			
			//删除贷款台账信息
			SqlClient.delete("deleteAccLoanForCancel", authorize_no, this.getConnection());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("撤销授权，更新业务数据失败");	
		}
	}
	
	/**
	 * 授权撤销，贷款展期类
	 * 交易成功后，进行如下处理：（1）将授权信息状态更新为“出账撤销”（2）修改到期日期和展期次数
	 * @param tran_serno
	 * @throws ComponentException
	 */
	public void doCancelAuthorizeForExtend(String tran_serno)throws ComponentException{
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection auKColl = dao.queryDetail(AUTHORIZEMODEL, tran_serno, this.getConnection());
			String authorize_no = (String) auKColl.getDataValue("authorize_no");
			String bill_no = (String) auKColl.getDataValue("bill_no");
			
			//修改授权表状态
			SqlClient.executeUpd("updatePvpAuthorizeForCancel", authorize_no, "03", null, this.getConnection());
			
//更新业务信息已移到展期成功通知交易处理   2014-03-10			
//			//修改台账信息
//			SqlClient.executeUpd("updateAccLoanForCancelExtend", bill_no, null, null, this.getConnection());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("撤销授权，更新业务数据失败");	
		}
	}
	//合同签订后生产一般贷款台账
	public void insetAccLoan4Ctr(KeyedCollection kColl,TableModelDAO dao) throws ComponentException{
		try {
			String cont_no = TagUtil.replaceNull4String(kColl.getDataValue("cont_no"));//合同编号
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, cont_no, this.getConnection());
			PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
	        .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), this.getConnection());
			/** 数据准备：通过业务流水号查询【业务申请】【合同信息】 */
			String is_trust_loan = (String)kColl.getDataValue("is_trust_loan");//是否信托贷款
			String date = this.getContext().getDataValue(PUBConstant.OPENDAY).toString();//营业日期
			String prd_id = TagUtil.replaceNull4String(kColl.getDataValue("prd_id"));//产品编号
			String cus_id = TagUtil.replaceNull4String(kColl.getDataValue("cus_id"));//客户编码
			String cur_type = TagUtil.replaceNull4String(kColl.getDataValue("cont_cur_type"));//币种
			String manager_br_id = TagUtil.replaceNull4String(kColl.getDataValue("manager_br_id"));//管理机构
			String in_acct_br_id = TagUtil.replaceNull4String(kColl.getDataValue("in_acct_br_id"));//入账机构
			
			String bill_no = cmisComponent.getBillNoByContNo(cont_no);//借据编号
			String cont_amt = TagUtil.replaceNull4String(kColl.getDataValue("cont_amt"));//合同金额
			String cont_end_date = TagUtil.replaceNull4String(kColl.getDataValue("cont_end_date"));//合同到期日期
			String ruling_ir = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir"));//基准利率（年）
			String ir_float_rate = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_rate"));//浮动比例
			String ir_float_point = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_point"));//浮动点数
			String reality_ir_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("reality_ir_y"));//执行利率（年）
			String five_classfiy = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("five_classfiy"));//五级分类
			if("1".equals(is_trust_loan) || "400024".equals(prd_id)){
				/**生成台账信息 */
				KeyedCollection accLoanKColl = new KeyedCollection(ACCLOANMODEL);
				accLoanKColl.addDataField("serno", "");//业务流水号（出账编号）
				accLoanKColl.addDataField("acc_day", date);//日期
				accLoanKColl.addDataField("acc_year", date.substring(0, 4));//年份
				accLoanKColl.addDataField("acc_mon", date.substring(5, 7));//月份
				accLoanKColl.addDataField("prd_id", prd_id);//产品编号
				accLoanKColl.addDataField("cus_id", cus_id);//客户编码
				accLoanKColl.addDataField("cont_no", cont_no);//合同编号
				accLoanKColl.addDataField("bill_no", bill_no);//借据编号
				accLoanKColl.addDataField("loan_amt", cont_amt);//贷款金额
				accLoanKColl.addDataField("loan_balance", cont_amt);//贷款余额
				accLoanKColl.addDataField("distr_date", date);//发放日期
				accLoanKColl.addDataField("end_date", cont_end_date);//到期日期
				accLoanKColl.addDataField("ori_end_date", cont_end_date);//原到期日期
				accLoanKColl.addDataField("post_count", "0");//展期次数
				accLoanKColl.addDataField("overdue", "0");//逾期期数
				accLoanKColl.addDataField("separate_date", "");//清分日期
				accLoanKColl.addDataField("ruling_ir", ruling_ir);//基准利率
				accLoanKColl.addDataField("ir_float_rate", ir_float_rate);//利率浮动比
				accLoanKColl.addDataField("ir_float_point", ir_float_point);//利率浮动点数
				accLoanKColl.addDataField("reality_ir_y", reality_ir_y);//执行年利率
				accLoanKColl.addDataField("comp_int_balance", "0");//复利余额
				accLoanKColl.addDataField("inner_owe_int", "0");//表内欠息
				accLoanKColl.addDataField("out_owe_int", "0");//表外欠息
				accLoanKColl.addDataField("rec_int_accum", "0");//应收利息累计
				accLoanKColl.addDataField("recv_int_accum", "0");//实收利息累计
				accLoanKColl.addDataField("normal_balance", cont_amt);//正常余额
				accLoanKColl.addDataField("overdue_balance", "0");//逾期余额
				accLoanKColl.addDataField("slack_balance", "0");//呆滞余额
				accLoanKColl.addDataField("bad_dbt_balance", "0");//呆账余额
				accLoanKColl.addDataField("writeoff_date", "");//核销日期
				accLoanKColl.addDataField("paydate", "");//转垫款日
				accLoanKColl.addDataField("five_class", five_classfiy);//五级分类
				accLoanKColl.addDataField("twelve_cls_flg", "");//十二级分类
				accLoanKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				accLoanKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
				accLoanKColl.addDataField("acc_status", "1");//台帐状态 正常
				accLoanKColl.addDataField("cur_type", cur_type);//币种
				dao.insert(accLoanKColl, this.getConnection());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("台账生成失败!"+e.getMessage());	
		}
	}
	//合同签订后生成银承贴现台账
    public void insetAccDrft4Ctr(KeyedCollection kColl,TableModelDAO dao) throws ComponentException{
    	try {
    		String cont_no = TagUtil.replaceNull4String(kColl.getDataValue("cont_no"));//合同编号
    		KeyedCollection ctrContSubKColl =  dao.queryDetail("CtrDiscCont", cont_no, this.getConnection());
			
			String five_classfiy = (String)ctrContSubKColl.getDataValue("five_classfiy");//五级分类
			String nowDate = (String)this.getContext().getDataValue(CMISConstance.OPENDAY);
			String prd_id = (String)kColl.getDataValue("prd_id");//产品编号
			String cus_id = (String)kColl.getDataValue("cus_id");//客户编码
			String manager_br_id = (String)kColl.getDataValue("manager_br_id");//管理机构
			String in_acct_br_id = (String)kColl.getDataValue("in_acct_br_id");//入账机构
			
			/**数据准备：获取票据批次号*/
			String condition = " where cont_no ='" + cont_no + "'";
			KeyedCollection iqpBatchKColl = dao.queryFirst("IqpBatchMng", null, condition, this.getConnection());
			String batch_no = (String) iqpBatchKColl.getDataValue("batch_no");
			
			/** 数据准备：通过获取票据批次下的票据明细关系 */
			String relCondition = " where batch_no ='" + batch_no + "'";
			IndexedCollection relIColl = dao.queryList("IqpBatchBillRel", relCondition, this.getConnection());
			
			for(int i=0;i<relIColl.size();i++){
				/** （1）获取借据编号*/
				PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
                .getComponentInstance(PvpConstant.PVPCOMPONENT, this.getContext(), this.getConnection());
				String billNo = cmisComponent.getBillNoByContNo(cont_no);
				
				KeyedCollection relKColl = (KeyedCollection) relIColl.get(i);
				String porder_no = (String) relKColl.getDataValue("porder_no");//票据号
				Map<String,String> incomeMap = new HashMap<String,String>();
				incomeMap.put("batch_no",batch_no);
				incomeMap.put("porder_no",porder_no);
				/**（2）收益表信息*/
				KeyedCollection incomeKColl = dao.queryDetail("IqpBillIncome", incomeMap, this.getConnection());
				String drft_amt = (String) incomeKColl.getDataValue("drft_amt");
				String fore_disc_date = (String) incomeKColl.getDataValue("fore_disc_date");
				String disc_days = (String) incomeKColl.getDataValue("disc_days");
				String adj_days = (String) incomeKColl.getDataValue("adj_days");
				String disc_rate = (String) incomeKColl.getDataValue("disc_rate");
				String int_amt = (String) incomeKColl.getDataValue("int");
				BigDecimal rpay_amt = new BigDecimal(drft_amt).subtract(new BigDecimal(int_amt));
				
				/**（3）票据明细表信息*/
				KeyedCollection billDetailKColl = dao.queryDetail("IqpBillDetail", porder_no, this.getConnection());
				String porder_curr = (String) billDetailKColl.getDataValue("porder_curr");
				String is_ebill = (String) billDetailKColl.getDataValue("is_ebill");
				
				
				KeyedCollection accDrftKColl = new KeyedCollection("AccDrft");//票据流水台账表
				accDrftKColl.addDataField("serno", "");//业务流水号（出账编号）
				accDrftKColl.addDataField("acc_day", nowDate);//日期
				accDrftKColl.addDataField("acc_year", nowDate.substring(0, 4));//年份
				accDrftKColl.addDataField("acc_mon", nowDate.substring(5, 7));//月份
				accDrftKColl.addDataField("prd_id", prd_id);//产品编号
				accDrftKColl.addDataField("cont_no", cont_no);//合同编号
				accDrftKColl.addDataField("bill_no", billNo);//借据编号
				accDrftKColl.addDataField("dscnt_type", "07");//贴现方式（为直贴）
				accDrftKColl.addDataField("porder_no", porder_no);//汇票号码
				accDrftKColl.addDataField("discount_per", cus_id );//贴现人/交易对手
				accDrftKColl.addDataField("dscnt_date", fore_disc_date);//贴现日
				accDrftKColl.addDataField("dscnt_day", disc_days);//贴现天数
				accDrftKColl.addDataField("adjust_day", adj_days);//调整天数
				accDrftKColl.addDataField("dscnt_rate", disc_rate);//贴现利率
				accDrftKColl.addDataField("cur_type", porder_curr);//交易币种
				accDrftKColl.addDataField("dscnt_int", int_amt);//贴现利息
				accDrftKColl.addDataField("rpay_amt", rpay_amt);//实付金额
				accDrftKColl.addDataField("rebuy_date", "");//回购日期
				accDrftKColl.addDataField("rebuy_day", "");//回购天数
				accDrftKColl.addDataField("rebuy_rate", "");//回购利率
				accDrftKColl.addDataField("overdue_rebuy_rate", "");//逾期回购利率
				accDrftKColl.addDataField("rebuy_int", "");//回购利息
				accDrftKColl.addDataField("separate_date", "");//清分日期
				accDrftKColl.addDataField("writeoff_date", "");//核销日期
				accDrftKColl.addDataField("five_class", five_classfiy);//五级分类
				accDrftKColl.addDataField("twelve_cls_flg", "");//十二级分类标志
				accDrftKColl.addDataField("manager_br_id", manager_br_id);//管理机构
				accDrftKColl.addDataField("fina_br_id", in_acct_br_id);//账务机构
				accDrftKColl.addDataField("accp_status","1");//台帐状态 正常
				dao.insert(accDrftKColl, this.getConnection());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("台账生成失败!"+e.getMessage());	
		}
	}
    /**
	 * 更新合同起止日期
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void updateContDate(String serno) throws Exception{
		String cont_no = null;
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			String date = this.getContext().getDataValue(PUBConstant.OPENDAY).toString();//营业日期
			
			KeyedCollection kCollPvp = dao.queryDetail(PVPLOANMODEL, serno, this.getConnection());
			cont_no = (String)kCollPvp.getDataValue("cont_no");
			String prd_id = (String)kCollPvp.getDataValue("prd_id");
			if("200024".equals(prd_id)){
				int rels = SqlClient.update("updateCtrDate4Accp", cont_no, null, null, this.getConnection());
				if(rels < 0){ 
					logger.error("更新银承合同起始日到期日失败！");
					throw new EMPJDBCException("更新银承合同起始日到期日失败！");  
				}
			}else{
				String condition="where cont_no='"+cont_no+"' and prd_id != '200024'";
				IndexedCollection iCollAccView = dao.queryList(ACCVIEWLOANMODEL, condition, this.getConnection());
				if(iCollAccView.size()==0){
					//普通贷款合同
					/*KeyedCollection kCollCont = dao.queryDetail(CTRCONTMODEL, cont_no, this.getConnection());
					KeyedCollection kCollContSub = dao.queryDetail(CTRCONTSUBMODEL, cont_no, this.getConnection());
					String cont_term = (String)kCollContSub.getDataValue("cont_term");
					String term_type = (String)kCollContSub.getDataValue("term_type");
					if("001".equals(term_type)){
						term_type = "Y";
					}else if("002".equals(term_type)){
						term_type = "M";
					}else if("003".equals(term_type)){
						term_type = "D";
					}	
					String endDate = DateUtils.getAddDate(term_type, date, Integer.parseInt(cont_term));
					kCollCont.put("cont_start_date", date);
					kCollCont.put("cont_end_date", endDate);
					dao.update(kCollCont, this.getConnection());*/
				}
			}
		} catch (EMPException e) {
			e.printStackTrace();
			logger.error("更新合同起止日期失败,原因："+e.getMessage());
			throw new Exception("更新合同起止日期失败,原因："+e.getMessage());	
		}  
	}
	
	public String valueOf(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
	
	//还款方式转换
	public String getRepayType(String oldRepType){
		Map<String,String> container=new HashMap<String,String>();
		container.put("A001", "5");
		container.put("A002", "1");
		container.put("A003", "2");
		container.put("A004", "4");
		container.put("A005", "3");
		return container.get(oldRepType);
	}
	
	//期限类型转换
	public String getTermType(String oldTermType){
		Map<String,String> container=new HashMap<String,String>();
		container.put("001", "Y");
		container.put("002", "M");
		container.put("003", "D");
		return container.get(oldTermType);
	}
	
	//账户类型转换
	public String getAccType(String oldAccType){
		Map<String,String> container=new HashMap<String,String>();
		container.put("01", "PAY");
		container.put("03", "AUT");
		container.put("04", "TPP");
		return container.get(oldAccType)!=null?container.get(oldAccType):"";
	}
	
	//担保方式转换
	public String getAssureMain(String oldAssureMain){
		Map<String,String> container=new HashMap<String,String>();
		container.put("100", "C104");
		container.put("200", "C103");
		container.put("210", "C103");
		container.put("220", "C103");
		container.put("300", "C102");
		container.put("400", "C101");
		container.put("500", "C106");
		container.put("510", "C106");
		return container.get(oldAssureMain);
	}
	
	//利率启用方式转换
	public String getIntRateEnblMd(String oldIntRateEnblMd){
		Map<String,String> container=new HashMap<String,String>();
		container.put("0", "N");
		container.put("1", "R");
		container.put("2", "R");
		container.put("3", "R");
		container.put("4", "A");
		return container.get(oldIntRateEnblMd);
	}
	
	//结息频率转换
	public String getIntStlmntFrqcy(String oldIntStlmntFrqcy){
		Map<String,String> container=new HashMap<String,String>();
		container.put("0", "Y1");
		container.put("1", "M3");
		container.put("2", "M1");
		return container.get(oldIntStlmntFrqcy)!=null?container.get(oldIntStlmntFrqcy):"";
	}
	
}
