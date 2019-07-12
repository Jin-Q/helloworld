package com.yucheng.cmis.biz01line.iqp.component;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class IqpCreditChangeFlowComponent extends CMISComponent {
	
	private static final String TOMODELCreditChange = "IqpCreditChangeApp";//信用证变更申请表
	private static final String TOMODELGuarantChange = "IqpGuarantChangeApp";//保函变更申请表
	private static final String TOMODELGuarChange = "IqpGuarChangeApp";//担保变更变更申请表
	private static final String TOMODELCtr = "CtrLoanCont";//合同主表表模型
	private static final String TOMODELCredit = "IqpCredit";//信用证从表表模型
	private static final String TOMODELGuarant = "IqpGuarantInfo";//保函从表表模型  
	private static final String TOMODELGrtLoan = "GrtLoanRGur";//业务担保合同关联表表模型  
	private static final String AUTHORIZEMODEL = "PvpAuthorize";//出账授权
	private static final String TOMODELBAIL = "PubBailInfo";//保证金信息表
	private static final String TOMODELAcc = "AccLoan";//台账
	private static final String TOMODELPvp = "PvpLoanApp";//出账
	
	/**  
	 * 信用证变更申请流程审批通过 
	 * @param serno 业务流水号 
	 * @throws ComponentException 
	 */
	public void doWfAgreeForCreditChange(String serno)throws ComponentException {
		Connection connection = null;
		try {
			connection = this.getConnection();
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail(TOMODELCreditChange, serno, this.getConnection());
			String cont_no = (String)kColl.getDataValue("cont_no");
			String old_serno = (String)kColl.getDataValue("old_serno");
			String bill_no = (String)kColl.getDataValue("bill_no");
			String cdt_cert_no = (String)kColl.getDataValue("cdt_cert_no");
			String cus_id = (String)kColl.getDataValue("cus_id");
			String cont_amt =  (String)kColl.getDataValue("cont_amt");
			String old_floodact_perc =  (String)kColl.getDataValue("floodact_perc");
			String new_apply_amt =  (String)kColl.getDataValue("new_apply_amt");
			String cont_cur_type =  (String)kColl.getDataValue("cont_cur_type");
			String new_cur_type =  (String)kColl.getDataValue("new_cur_type");
			BigDecimal new_security_rate =  BigDecimalUtil.replaceNull(kColl.getDataValue("new_security_rate"));
			String floodact_perc =  (String)kColl.getDataValue("new_floodact_perc");
			String shortact_perc =  (String)kColl.getDataValue("new_shortact_perc");
			String new_assure_main =  (String)kColl.getDataValue("new_assure_main");
			String new_assure_main_details =  (String)kColl.getDataValue("new_assure_main_details");
			String end_date =  (String)kColl.getDataValue("end_date");
			String manager_br_id =  (String)kColl.getDataValue("manager_br_id");
			BigDecimal security_rate =  BigDecimalUtil.replaceNull(kColl.getDataValue("security_rate"));
			String prd_id = (String) kColl.getDataValue("prd_id");
			/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			BigDecimal exchange_rate_old =  BigDecimalUtil.replaceNull(kColl.getDataValue("exchange_rate"));
			BigDecimal exchange_rate_security_old =  BigDecimalUtil.replaceNull(kColl.getDataValue("security_exchange_rate"));
			/** modified by yangzy 2015/07/14 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			/**本部分移除*/
//			/**往原合同表中回写新的数据*/
//			KeyedCollection kCollCtr = dao.queryDetail(TOMODELCtr, cont_no, this.getConnection());
//			kCollCtr.setDataValue("serno", serno);//插入新的业务流水号
//			kCollCtr.setDataValue("cont_amt", new_apply_amt);//插入新的信用证金额  
//			kCollCtr.setDataValue("security_rate", new_security_rate);//插入新的保证金比例
//			kCollCtr.setDataValue("assure_main", new_assure_main);//插入新担保方式
//			kCollCtr.setDataValue("assure_main_details", new_assure_main_details);//插入新的担保方式细分  
//			int m = dao.update(kCollCtr, this.getConnection());
//			if(m!=1){
//				throw new EMPException("往原合同表中回写新的数据失败!");
//			}
			KeyedCollection kCollBail = dao.queryFirst(TOMODELBAIL, null, "where cont_no='"+cont_no+"'", connection);
			String bail_acct_no = (String)kCollBail.getDataValue("bail_acct_no");//保证金账号
			String bail_cur_type = (String)kCollBail.getDataValue("cur_type");//保证金币种
			if(bail_cur_type==null||"".equals(bail_cur_type)){
				bail_cur_type = "CNY";
			}
			//计算信用证增减金额
			//获取实时汇率  start
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			//KeyedCollection kCollRate = service.getHLByCurrType(cont_cur_type, this.getContext(), connection);
			KeyedCollection kCollRateNew = service.getHLByCurrType(new_cur_type, this.getContext(), connection);
			//if("failed".equals(kCollRate.getDataValue("flag"))){
			//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			//}
			//BigDecimal exchange_rate_old = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
			if("failed".equals(kCollRateNew.getDataValue("flag"))){
				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			}
			BigDecimal exchange_rate_new = BigDecimalUtil.replaceNull(kCollRateNew.getDataValue("sld"));//汇率
			/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			KeyedCollection kCollRateSecurity = service.getHLByCurrType(bail_cur_type, this.getContext(), this.getConnection());
			if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			}
			BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
			//获取实时汇率  end
			/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			//更新实时汇率
			kColl.put("new_exchange_rate", exchange_rate_new);
			kColl.put("new_security_exchange_rate", exchange_rate_security);
			dao.update(kColl, this.getConnection());
			/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			
			
			BigDecimal oldAmt = new BigDecimal(cont_amt);
			BigDecimal newAmt = new BigDecimal(new_apply_amt);
			BigDecimal sprdAmt = (newAmt.multiply(exchange_rate_new).subtract(oldAmt.multiply(exchange_rate_old))).divide(exchange_rate_new, 2,BigDecimal.ROUND_HALF_EVEN);;
			//修改后的开证最大金额（开证金额*（1+溢装比例））
			BigDecimal moreAmt = (newAmt.multiply(new BigDecimal(floodact_perc).add(new BigDecimal(1.0))));
			KeyedCollection kCollAcc = dao.queryDetail(TOMODELAcc, bill_no, connection);
			
			//台账表中出账业务流水号
			String accSerno = (String)kCollAcc.getDataValue("serno");
			KeyedCollection kCollPvp = dao.queryDetail(TOMODELPvp, accSerno, connection);
			String in_acct_br_id = (String)kCollPvp.getDataValue("in_acct_br_id");
			
			//计算保证金增额
			//保证金金额=金额*保证金比例*（1+溢装比例）*合同汇率/保证金汇率  modify by zhaozq 2014-06-26
			/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			BigDecimal securityAmtOld = oldAmt.multiply(security_rate).multiply(new BigDecimal(1).add(new BigDecimal(old_floodact_perc)))
			.multiply(exchange_rate_old).divide(exchange_rate_security_old,2,BigDecimal.ROUND_HALF_EVEN);
			/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			BigDecimal securityAmtNew = newAmt.multiply(new_security_rate).multiply(new BigDecimal(1).add(new BigDecimal(floodact_perc)))
			.multiply(exchange_rate_new).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
			java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			String caculateAmtOld = String.valueOf(securityAmtOld);
			String caculateAmtNew = String.valueOf(securityAmtNew);
			securityAmtOld = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmtOld)/100)*100);
			securityAmtNew = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmtNew)/100)*100);
			String changeAmtOld = nf.format(securityAmtOld);
			String changeAmtNew = nf.format(securityAmtNew);
			securityAmtOld = BigDecimalUtil.replaceNull(changeAmtOld);
			securityAmtNew = BigDecimalUtil.replaceNull(changeAmtNew);
			
			BigDecimal addsecurity = securityAmtNew.subtract(securityAmtOld);
			
			String cus_name = "";
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			try {
				CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
				CusBase cusBase = csi.getCusBaseByCusId(cus_id,this.getContext(),connection);
				cus_name = cusBase.getCusName();
			} catch (Exception e) {
				e.printStackTrace();
				throw new EMPException("获取组织机构模块失败！");
			}
			
			/**给授权表赋值----------start---------------------------------------------------*/
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			/**生成授权编号，所有票据授权交易流水在一个授权编号下*/
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connection, this.getContext());
			/** 生成交易流水号 */
			String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connection, this.getContext());
			/** 给授权信息表赋值 */
			authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
			authorizeKColl.addDataField("serno", serno);//业务流水号（信用证变更业务流水号）
			authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
			authorizeKColl.addDataField("prd_id", prd_id);//产品编号(国内信用证)
			authorizeKColl.addDataField("cus_id", cus_id);//客户编码
			authorizeKColl.addDataField("cus_name", cus_name);//客户名称
			authorizeKColl.addDataField("cont_no", cont_no);//合同编号
			authorizeKColl.addDataField("bill_no", bill_no);//借据编号
			authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_XYZBHWH+TradeConstance.SERVICE_SCENE_XYZBHSQ);//交易码+场景
			authorizeKColl.addDataField("tran_amt", new_apply_amt);//交易金额
			//authorizeKColl.addDataField("tran_date", this.getContext().getDataValue(CMISConstance.OPENDAY));//交易日期取发送日期
			authorizeKColl.addDataField("tran_date", "");//交易日期取发送日期默认不赋值
			authorizeKColl.addDataField("send_times", "0");//发送次数
			authorizeKColl.addDataField("return_code", "");//返回编码
			authorizeKColl.addDataField("return_desc", "");//返回信息
			authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
			authorizeKColl.addDataField("status", "00");//状态
			authorizeKColl.addDataField("cur_type",new_cur_type );//币种
			
			/** 抽取发往核算系统的授权信息--start-- */
			authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编码
			authorizeKColl.addDataField("fldvalue02", "INCOME_ORG_NO@"+in_acct_br_id);//入账机构码
			authorizeKColl.addDataField("fldvalue03", "TRAN_DATE@"+"");//交易日期
			authorizeKColl.addDataField("fldvalue04", "DUEBILL_NO@"+bill_no);//借据号
			authorizeKColl.addDataField("fldvalue05", "CONSUMER_SEQ_NO@"+tranSerno);//交易流水号
			authorizeKColl.addDataField("fldvalue06", "CONTRACT_NO@"+cont_no);//合同号
			authorizeKColl.addDataField("fldvalue07", "BUSS_KIND@"+"01");//业务种类
			authorizeKColl.addDataField("fldvalue08", "LC_NO@"+cdt_cert_no);//信用证号码
			authorizeKColl.addDataField("fldvalue09", "GUARANTEE_CCY@"+bail_cur_type);//保证金币种
			authorizeKColl.addDataField("fldvalue10", "GUARANTEE_ACCT_NO@"+bail_acct_no);//保证金账号
			authorizeKColl.addDataField("fldvalue11", "ADD_OR_REDUCE_AMT@"+sprdAmt);//增减金额
			authorizeKColl.addDataField("fldvalue12", "BUSS_CCY@"+new_cur_type);//业务币种
			authorizeKColl.addDataField("fldvalue13", "GUARANTEE_PER@"+new_security_rate);//保证金比例
			authorizeKColl.addDataField("fldvalue14", "GUARANTEE_RECO_AMT@"+addsecurity);//保证金增额
			authorizeKColl.addDataField("fldvalue15", "EXE_EXPI_DATE@"+TagUtil.formatDate(end_date));//展期到期日期
			authorizeKColl.addDataField("fldvalue16", "CLIENT_NO@"+cus_id);//客户号
			authorizeKColl.addDataField("fldvalue17", "LC_OVERFLOW_RATE@"+floodact_perc);//溢装比例
			authorizeKColl.addDataField("fldvalue18", "LC_REDUCE_RATE@"+shortact_perc);//短装比例
			
			authorizeKColl.addDataField("fldvalue19", "GT_AMOUNT@"+new_apply_amt);//修改后的开证金额
			authorizeKColl.addDataField("fldvalue20", "GT_HIGHEST_AMT@"+moreAmt);//修改后的开证最大金额
			/** 抽取发往核算系统的授权信息--end-- */
			dao.insert(authorizeKColl, connection);
			/**给授权表赋值----------end---------------------------------------------------*/
			
			/**往信用证从表中插入新的数据*/
			KeyedCollection kCollCredit = dao.queryDetail(TOMODELCredit, old_serno, this.getConnection()); 
			kCollCredit.setDataValue("serno", serno);//新的业务流水号
			kCollCredit.setDataValue("floodact_perc", kColl.getDataValue("new_floodact_perc")); //溢装比例
			kCollCredit.setDataValue("shortact_perc", kColl.getDataValue("new_shortact_perc"));//短装比例
			kCollCredit.setDataValue("credit_term_type", kColl.getDataValue("new_credit_term_type"));//用证期限类型
			kCollCredit.setDataValue("fast_day", kColl.getDataValue("new_fast_day"));//远期天数
			kCollCredit.setDataValue("end_date", kColl.getDataValue("new_end_date"));//信用证效期
			kCollCredit.setDataValue("credit_type", kColl.getDataValue("new_credit_type"));//修改后信用证期限类型
			int n = dao.insert(kCollCredit, this.getConnection());
			if(n!=1){ 
				throw new EMPException("往信用证从表中插入新的数据失败!");
			} 
			/**业务担保信息修改
			 * 如果关联关系为解除的，修改原业务担保合同关联关系
			 * 如果关联关系为新增的，添加合同编号
			 * */
			doGrtLoanRGur(serno, cont_no, dao);
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	
	}
	
	/**  
	 * 保函修改申请流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForGuarantChange(String serno)throws ComponentException {
		Connection connection = this.getConnection();
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail(TOMODELGuarantChange, serno, this.getConnection());
			String cont_no = (String)kColl.getDataValue("cont_no"); 
			String old_serno = (String)kColl.getDataValue("old_serno");
			String cus_id = (String)kColl.getDataValue("cus_id");
			String bill_no = (String)kColl.getDataValue("bill_no");
			String cont_amt = (String)kColl.getDataValue("cont_amt");
			String new_cont_amt =  (String)kColl.getDataValue("new_cont_amt");
			String cont_cur_type =  (String)kColl.getDataValue("cont_cur_type");
			String new_cur_type =  (String)kColl.getDataValue("new_cur_type");
			BigDecimal new_security_rate =  BigDecimalUtil.replaceNull(kColl.getDataValue("new_security_rate"));
			String new_assure_main =  (String)kColl.getDataValue("new_assure_main");
			String new_assure_main_details =  (String)kColl.getDataValue("new_assure_main_details");
			String end_date =  (String)kColl.getDataValue("end_date");
			String manager_br_id =  (String)kColl.getDataValue("manager_br_id");
			BigDecimal security_rate =  BigDecimalUtil.replaceNull(kColl.getDataValue("security_rate"));
			String prd_id = (String) kColl.getDataValue("prd_id");
			/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			BigDecimal exchange_rate_old =  BigDecimalUtil.replaceNull(kColl.getDataValue("exchange_rate"));
			BigDecimal exchange_rate_security_old =  BigDecimalUtil.replaceNull(kColl.getDataValue("security_exchange_rate"));
			/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
//			/**往原合同表中回写新的数据*/
//			KeyedCollection kCollCtr = dao.queryDetail(TOMODELCtr, cont_no, this.getConnection());
//			kCollCtr.setDataValue("serno", serno);//插入新的业务流水号
//			kCollCtr.setDataValue("cont_amt", new_cont_amt);//插入新的保函金额  
//			kCollCtr.setDataValue("security_rate", new_security_rate);//插入新的保证金比例
//			kCollCtr.setDataValue("assure_main", new_assure_main);//插入新担保方式
//			kCollCtr.setDataValue("assure_main_details", new_assure_main_details);//插入新的担保方式细分  
//			kCollCtr.setDataValue("cont_end_date", new_cont_end_date);//保函到期日 
//			int m = dao.update(kCollCtr, this.getConnection());
//			if(m!=1){
//				throw new EMPException("往原合同表中回写新的数据失败!");
//			}
			/** modified by yangzy 2015/11/02 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			KeyedCollection kCollBail = dao.queryFirst(TOMODELBAIL, null, "where cont_no='"+cont_no+"'", connection);
			String bail_acct_no = "";
			String bail_cur_type = (String)kColl.getDataValue("security_cur_type");
			if(kCollBail!=null&&kCollBail.getDataValue("bail_acct_no")!=null&&!"".equals(kCollBail.getDataValue("bail_acct_no"))){
				bail_acct_no = (String)kCollBail.getDataValue("bail_acct_no");//保证金账号
				bail_cur_type = (String)kCollBail.getDataValue("cur_type");//保证金币种
			}
			/** modified by yangzy 2015/11/02 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
			//获取实时汇率  start
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
			//KeyedCollection kCollRate = service.getHLByCurrType(cont_cur_type, this.getContext(), connection);
			KeyedCollection kCollRateNew = service.getHLByCurrType(new_cur_type, this.getContext(), connection);
			//if("failed".equals(kCollRate.getDataValue("flag"))){
			//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			//}
			//BigDecimal exchange_rate_old = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
			if("failed".equals(kCollRateNew.getDataValue("flag"))){
				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			}
			BigDecimal exchange_rate_new = BigDecimalUtil.replaceNull(kCollRateNew.getDataValue("sld"));//汇率
			//获取实时汇率  end
			//更新实时汇率
			KeyedCollection kCollRateSecurity = service.getHLByCurrType(bail_cur_type, this.getContext(), this.getConnection());
			if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			}
			BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
			kColl.put("new_exchange_rate", exchange_rate_new);
			kColl.put("new_security_exchange_rate", exchange_rate_security);
			dao.update(kColl, this.getConnection());
			
			
			//计算信用证增减金额
			BigDecimal oldAmt = new BigDecimal(cont_amt);
			BigDecimal newAmt = new BigDecimal(new_cont_amt);
			BigDecimal sprdAmt = (newAmt.multiply(exchange_rate_new).subtract(oldAmt.multiply(exchange_rate_old))).divide(exchange_rate_new, 2,BigDecimal.ROUND_HALF_EVEN);
			/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
            KeyedCollection kCollAcc = dao.queryDetail(TOMODELAcc, bill_no, connection);
			
			//台账表中出账业务流水号
			String accSerno = (String)kCollAcc.getDataValue("serno");
			KeyedCollection kCollPvp = dao.queryDetail(TOMODELPvp, accSerno, connection);
			String in_acct_br_id = (String)kCollPvp.getDataValue("in_acct_br_id");
			
			//计算保证金增额
			BigDecimal securityAmtOld = oldAmt.multiply(security_rate).multiply(exchange_rate_old);
			BigDecimal securityAmtNew = newAmt.multiply(new_security_rate).multiply(exchange_rate_new);
			java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			String caculateAmtOld = String.valueOf(securityAmtOld);
			String caculateAmtNew = String.valueOf(securityAmtNew);
			securityAmtOld = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmtOld)/100)*100);
			securityAmtNew = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmtNew)/100)*100);
			String changeAmtOld = nf.format(securityAmtOld);
			String changeAmtNew = nf.format(securityAmtNew);
			securityAmtOld = BigDecimalUtil.replaceNull(changeAmtOld);
			securityAmtNew = BigDecimalUtil.replaceNull(changeAmtNew);
			
			BigDecimal addsecurity = securityAmtNew.subtract(securityAmtOld);			
			
			String cus_name = "";
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			try {
				CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
				CusBase cusBase = csi.getCusBaseByCusId(cus_id,this.getContext(),connection);
				cus_name = cusBase.getCusName();
			} catch (Exception e) {
				e.printStackTrace();
				throw new EMPException("获取组织机构模块失败！");
			}
			
			/**给授权表赋值----------start---------------------------------------------------*/
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			/**生成授权编号，所有票据授权交易流水在一个授权编号下*/
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connection, this.getContext());
			/** 生成交易流水号 */
			String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connection, this.getContext());
			/** 给授权信息表赋值 */
			authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
			authorizeKColl.addDataField("serno", serno);//业务流水号（信用证变更业务流水号）
			authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
			authorizeKColl.addDataField("prd_id", prd_id);//产品编号
			authorizeKColl.addDataField("cus_id", cus_id);//客户编码
			authorizeKColl.addDataField("cus_name", cus_name);//客户名称
			authorizeKColl.addDataField("cont_no", cont_no);//合同编号
			authorizeKColl.addDataField("bill_no", bill_no);//借据编号
			authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_XYZBHWH+TradeConstance.SERVICE_SCENE_XYZBHSQ);//交易码+场景
			authorizeKColl.addDataField("tran_amt", new_cont_amt);//交易金额
			//authorizeKColl.addDataField("tran_date", this.getContext().getDataValue(CMISConstance.OPENDAY));//交易日期取发送日期
			authorizeKColl.addDataField("tran_date", "");//交易日期取发送日期默认不赋值
			authorizeKColl.addDataField("send_times", "0");//发送次数
			authorizeKColl.addDataField("return_code", "");//返回编码
			authorizeKColl.addDataField("return_desc", "");//返回信息
			authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
			authorizeKColl.addDataField("status", "00");//状态
			authorizeKColl.addDataField("cur_type",new_cur_type );//币种
			
			/** 抽取发往核算系统的授权信息--start-- */
			authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@"+authSerno);//出账授权编码
			authorizeKColl.addDataField("fldvalue02", "INCOME_ORG_NO@"+in_acct_br_id);//入账机构码
			authorizeKColl.addDataField("fldvalue03", "TRAN_DATE@"+"");//交易日期
			authorizeKColl.addDataField("fldvalue04", "DUEBILL_NO@"+bill_no);//借据号
			authorizeKColl.addDataField("fldvalue05", "CONSUMER_SEQ_NO@"+tranSerno);//交易流水号
			authorizeKColl.addDataField("fldvalue06", "CONTRACT_NO@"+cont_no);//合同号
			authorizeKColl.addDataField("fldvalue07", "BUSS_KIND@"+"02");//业务种类 外汇保函
			authorizeKColl.addDataField("fldvalue08", "LC_NO@"+"");//信用证号码
			authorizeKColl.addDataField("fldvalue09", "GUARANTEE_CCY@"+bail_cur_type);//保证金币种
			authorizeKColl.addDataField("fldvalue10", "GUARANTEE_ACCT_NO@"+bail_acct_no);//保证金账号
			authorizeKColl.addDataField("fldvalue11", "ADD_OR_REDUCE_AMT@"+sprdAmt);//增减金额
			authorizeKColl.addDataField("fldvalue12", "BUSS_CCY@"+new_cur_type);//业务币种
			authorizeKColl.addDataField("fldvalue13", "GUARANTEE_PER@"+new_security_rate);//保证金比例
			authorizeKColl.addDataField("fldvalue14", "GUARANTEE_RECO_AMT@"+addsecurity);//保证金增额
			authorizeKColl.addDataField("fldvalue15", "EXE_EXPI_DATE@"+TagUtil.formatDate(end_date));//展期到期日期
			authorizeKColl.addDataField("fldvalue16", "CLIENT_NO@"+cus_id);//客户号
			authorizeKColl.addDataField("fldvalue17", "LC_OVERFLOW_RATE@"+"");//溢装比例
			authorizeKColl.addDataField("fldvalue18", "LC_REDUCE_RATE@"+"");//短装比例
			
			authorizeKColl.addDataField("fldvalue19", "GT_AMOUNT@"+cont_amt);//修改后的保函金额
			authorizeKColl.addDataField("fldvalue20", "GT_HIGHEST_AMT@"+cont_amt);//修改后的保函最大金额
			/** 抽取发往核算系统的授权信息--end-- */
			dao.insert(authorizeKColl, connection);
			/**给授权表赋值----------end---------------------------------------------------*/
			
			
			/**往保函从表中插入新的数据*/
			KeyedCollection kCollCredit = dao.queryDetail(TOMODELGuarant, old_serno, this.getConnection()); 
			kCollCredit.setDataValue("serno", serno);//新的业务流水号
			
			kCollCredit.setDataValue("guarant_type", kColl.getDataValue("new_guarant_type"));//保函种类
			kCollCredit.setDataValue("guarant_mode", kColl.getDataValue("new_guarant_mode"));//保函类型
			kCollCredit.setDataValue("is_bank_format", kColl.getDataValue("new_is_bank_format"));//是否我行标准格式
			kCollCredit.setDataValue("ben_name", kColl.getDataValue("new_ben_name"));//受益人名称
			kCollCredit.setDataValue("end_date", kColl.getDataValue("new_cont_end_date"));//修改后保函到期日
			int n = dao.insert(kCollCredit, this.getConnection());
			if(n!=1){
				throw new EMPException("往保函从表中插入新的数据失败!"); 
			} 
			/**业务担保信息修改
			 * 如果关联关系为解除的，修改原业务担保合同关联关系
			 * 如果关联关系为新增的，添加合同编号
			 * */
			doGrtLoanRGur(serno, cont_no, dao);
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
		
	}
	
	/**  
	 * 担保变更申请流程审批通过
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void doWfAgreeForGuarChange(String serno)throws ComponentException {
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail(TOMODELGuarChange, serno, this.getConnection());
			String cont_no = (String)kColl.getDataValue("cont_no");
			String new_assure_main =  (String)kColl.getDataValue("new_assure_main");
			String new_assure_main_details =  (String)kColl.getDataValue("new_assure_main_details");
			
			/**往原合同表中回写新的数据*/
			KeyedCollection kCollCtr = dao.queryDetail(TOMODELCtr, cont_no, this.getConnection());
			//kCollCtr.setDataValue("serno", serno);//插入新的业务流水号
			kCollCtr.setDataValue("assure_main", new_assure_main);//插入新担保方式
			kCollCtr.setDataValue("assure_main_details", new_assure_main_details);//插入新的担保方式细分  
			int m = dao.update(kCollCtr, this.getConnection());
			if(m!=1){
				throw new EMPException("往原合同表中回写新的数据失败!");
			}
			/**业务担保信息修改
			 * 如果关联关系为解除的，修改原业务担保合同关联关系
			 * 如果关联关系为新增的，添加合同编号
			 * */
			doGrtLoanRGur(serno, cont_no, dao);
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("流程结束业务处理异常，请检查业务处理逻辑！");
		} 
		
	}
	
	/**业务担保信息修改
	 * 如果关联关系为解除的，修改原业务担保合同关联关系
	 * 如果关联关系为新增的，添加合同编号
	 * */
	public void doGrtLoanRGur(String serno,String cont_no,TableModelDAO dao) throws ComponentException{
		try{
		String condition = "where serno='"+serno+"'";
		IndexedCollection iCollGrtLoan = dao.queryList(TOMODELGrtLoan, condition, this.getConnection());
		for(int i=0;i<iCollGrtLoan.size();i++){
			KeyedCollection kCollGrtLoan = (KeyedCollection)iCollGrtLoan.get(i);
			String corre_rel = (String)kCollGrtLoan.getDataValue("corre_rel");
			//如果关联关系为解除，则修改原业务担保合同的关联关系为已解除
			if("3".equals(corre_rel)){
				String guar_cont_no = (String)kCollGrtLoan.getDataValue("guar_cont_no");
				String conditoinStr = "where guar_cont_no='"+guar_cont_no+"' and cont_no='"+cont_no+"'";
				IndexedCollection iColl = dao.queryList(TOMODELGrtLoan, conditoinStr, this.getConnection());
				for(int m=0;m<iColl.size();m++){
					KeyedCollection kCollLoan = (KeyedCollection)iColl.get(m);
					kCollLoan.setDataValue("corre_rel", "5"); 
					dao.update(kCollLoan, this.getConnection());  
				}
				kCollGrtLoan.put("corre_rel", "5");//本条也需更新为已解除
				dao.update(kCollGrtLoan, this.getConnection());
			}else if("2".equals(corre_rel)){
				//如果关联关系为新增，则往这条复制来的业务担保合同数据插入合同编号,状态置为正常
				kCollGrtLoan.put("cont_no", cont_no); 
				kCollGrtLoan.put("corre_rel", "1"); 
				dao.update(kCollGrtLoan, this.getConnection()); 
			}else if("4".equals(corre_rel)){
				//1.如果关联关系为续作,则往原业务担保中状态置为被续作
				//2.往这条复制来的业务担保合同数据插入合同编号,状态置为正常
				String guar_cont_no = (String)kCollGrtLoan.getDataValue("guar_cont_no");
				String conditoinStr = "where guar_cont_no='"+guar_cont_no+"' and cont_no='"+cont_no+"'";
				IndexedCollection iColl = dao.queryList(TOMODELGrtLoan, conditoinStr, this.getConnection());
				for(int m=0;m<iColl.size();m++){
					KeyedCollection kCollLoan = (KeyedCollection)iColl.get(m);
					kCollLoan.setDataValue("corre_rel", "6"); 
					dao.update(kCollLoan, this.getConnection());  
				}
				kCollGrtLoan.put("cont_no", cont_no); 
				kCollGrtLoan.put("corre_rel", "1"); 
				dao.update(kCollGrtLoan, this.getConnection()); 
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("业务担保信息修改失败!"); 
		}
	}
}
