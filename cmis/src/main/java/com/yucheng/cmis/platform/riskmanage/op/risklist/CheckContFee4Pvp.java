package com.yucheng.cmis.platform.riskmanage.op.risklist;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.TimeUtil;
/**
 * 扣款账户余额及收费情况检查
*@author wangj
*@time 2015-08-04
*@description 需求编号：XD150611043,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
                           变更需求编号：XD150925071,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
*@version v1.0
*
 */
public class CheckContFee4Pvp implements RiskManageInterface {
	private static final Logger logger = Logger.getLogger(CheckContFee4Pvp.class);
	private final String clcModelId = "CtrLoanCont";//合同主表
	private final String clcsModelId = "CtrLoanContSub";//合同子表
	private final String ictModelId = "IqpCusAcct";//账户信息
	private final String iatModelId = "IqpAppendTerms";//附件费用信息
	private final String plaModelId = "PvpLoanApp";//出账申请信息
	private final String iaaModelId = "IqpAccAccp";//银行承兑汇票
	public Map<String, String> getResultMap(String tableName, String serno, Context context,Connection connection) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		Map<String,BigDecimal> retparam = new HashMap<String,BigDecimal>();
		try {
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			//合同信息
			IndexedCollection clcIColl = dao.queryList(clcModelId," where cont_no =(SELECT pla.cont_no FROM Pvp_Loan_App pla WHERE pla.serno='"+serno+"') ", connection);
			IndexedCollection clcsIColl = dao.queryList(clcsModelId," where cont_no =(SELECT pla.cont_no FROM Pvp_Loan_App pla WHERE pla.serno='"+serno+"') ", connection);
			KeyedCollection clcKcoll=null;//合同主表信息
			KeyedCollection clcsKcoll=null;//合同从表
			if(clcIColl!=null&&clcIColl.size()>0){
				clcKcoll=(KeyedCollection) clcIColl.get(0);
			}
			if(clcsIColl!=null&&clcsIColl.size()>0){
				clcsKcoll=(KeyedCollection) clcsIColl.get(0);
			}
			KeyedCollection discKcoll=null;//合同从表
			if(clcsKcoll==null){
				IndexedCollection discIColl = dao.queryList("CtrDiscCont"," where cont_no =(SELECT pla.cont_no FROM Pvp_Loan_App pla WHERE pla.serno='"+serno+"') ", connection);
				if(discIColl!=null&&discIColl.size()>0){
					discKcoll=(KeyedCollection) discIColl.get(0);
				}
			}
			//服务器
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			//
			KeyedCollection plaKColl=dao.queryDetail(plaModelId, serno, connection);
			String	prdId = (String)plaKColl.getDataValue("prd_id");
			if(prdId.equals("400020") || prdId.equals("500032") || prdId.equals("700020") || prdId.equals("700021")){
				param.put("OUT_是否通过","通过");
				param.put("OUT_提示信息","扣款账户余额检查通过！");
				return param;
			}
			//账户列表 :获取账户信息
			IndexedCollection ictIColl = dao.queryList(ictModelId," WHERE serno=(SELECT t.serno FROM Pvp_Loan_App pla,Ctr_Loan_Cont t WHERE pla.cont_no=t.cont_no and pla.serno='"+serno+"') ", connection);
			IndexedCollection acctBalanceIColl=new IndexedCollection();//查询扣款账户余额
			List<String> acctNos=new ArrayList<String>();
			if(ictIColl!=null&&ictIColl.size()>0){
				for(int i=0;i<ictIColl.size();i++){
					KeyedCollection kColl=(KeyedCollection) ictIColl.get(i);
					String acct_no=(String) kColl.getDataValue("acct_no");//账户
					if(!acctNos.contains(acct_no)){//查询不重复的账户信息
						acctNos.add(acct_no);//已经查询的账户不再查询
						acctBalanceIColl.add(kColl);
					}
				}
			}
			String out_str="";
			String out_msg="";
			//1、先查询附加信息的费用信息
			//费用列表信息
			IndexedCollection iatIColl = dao.queryList(iatModelId," where serno=(SELECT t.serno FROM Pvp_Loan_App pla,Ctr_Loan_Cont t WHERE pla.cont_no=t.cont_no and pla.serno='"+serno+"') ", connection);
			if(iatIColl!=null&&iatIColl.size()>0){
				for(int i=0;i<iatIColl.size();i++){
					KeyedCollection iatKcoll=(KeyedCollection) iatIColl.get(i);
					String fee_acct_no=(String) iatKcoll.getDataValue("fee_acct_no");//账户
					KeyedCollection feeAcctKColl=getKCollByAcctNo(acctBalanceIColl,fee_acct_no);
					String isThisOrg=(String) feeAcctKColl.getDataValue("is_this_org_acct");//是否本行账户
					if(!"1".equals(isThisOrg)){
						out_str="不通过";
						out_msg="费用账户["+fee_acct_no+"]不是本行账户,无法查询账户余额信息！";
						break;
					}
					getAcctBalance(acctBalanceIColl,fee_acct_no,serviceJndi,context,connection);//获取账户余额
					feeAcctKColl=getKCollByAcctNo(acctBalanceIColl,fee_acct_no);
					if(feeAcctKColl.containsKey("msg")){
						out_str="不通过";
						out_msg=(String) feeAcctKColl.getDataValue("msg");
						break;
					}
					BigDecimal feeAmt=BigDecimalUtil.replaceNull(iatKcoll.getDataValue("fee_amt"));//费用总额
					String is_cycle_chrg=(String) iatKcoll.getDataValue("is_cycle_chrg");//是否周期性收费
					String chrg_freq=(String) iatKcoll.getDataValue("chrg_freq");//收费频率
					if(clcsKcoll!=null&&"1".equals(is_cycle_chrg)){//周期性收费  费用总额/期数=本期所收费用
						if(chrg_freq.equals("Y")){
							chrg_freq = "12";
						}else if(chrg_freq.equals("Q")){
							chrg_freq = "3";
						}else chrg_freq = "1";
						String date = context.getDataValue(PUBConstant.OPENDAY).toString();//营业日期
						String term_type = TagUtil.replaceNull4String(clcsKcoll.getDataValue("term_type"));//期限类型
						Integer cont_term = TagUtil.replaceNull4Int(clcsKcoll.getDataValue("cont_term"));//合同期限
						String end_date = DateUtils.getEndDate(term_type, date, cont_term);//借据到期日
						int feeCnt=getFeeCnt(date,end_date,chrg_freq);
						feeAmt=feeAmt.divide(new BigDecimal(feeCnt),2,BigDecimal.ROUND_HALF_UP);//每一期的收的费用
					}
					subFeeAmt(acctBalanceIColl,fee_acct_no,feeAmt,"1",retparam);
				}
			}
			if(!"".equals(out_str)){
				param.put("OUT_是否通过",out_str);
				param.put("OUT_提示信息",out_msg);
				return param;
			}
			
			//2、收取印花税信息 IS_COLLECT_STAMP 是否收取印花税  贷款计算器(02002000002) 印花税试算(07) 接口不通 
          
			String inAcctBrId=TagUtil.replaceNull4String(plaKColl.getDataValue("in_acct_br_id"));//业务办理机构码
			String ccy=TagUtil.replaceNull4String(plaKColl.getDataValue("cur_type"));//币种
			KeyedCollection tmpKColl=clcsKcoll;
			if(tmpKColl==null) tmpKColl=discKcoll;
			if(tmpKColl!=null&&tmpKColl.containsKey("is_collect_stamp")&&"1".equals((String)tmpKColl.getDataValue("is_collect_stamp"))){
				String stampMode=TagUtil.replaceNull4String(tmpKColl.getDataValue("stamp_collect_mode"));//印花税收取方式
				BigDecimal pvpAmt=BigDecimalUtil.replaceNull(plaKColl.getDataValue("pvp_amt"));//出账金额
				String stamp1AcctNo="";//借款人印花税账号 
				String stamp2AcctNo="";//委托人印花税账号
				/*stamp_collect_mode  印花税收取方式
				 * 4           仅代扣借款人
				 * 3	全由委托人支付
				 * 2	全由借款人支付
				 * 1	双边各自支付
				 * acct_attr 账户属性
				 * 05	主办行资金归集账户
				 * 04	受托支付账号
				 * 03	收息收款账号
				 * 02	借款人印花税账号
				 * 01	放款账号
				 * 06	委托人印花税账号
				 * 07	费用账号*/
				
				if(ictIColl!=null&&ictIColl.size()>0){
					for(int i=0;i<ictIColl.size();i++){
						KeyedCollection kColl=(KeyedCollection) ictIColl.get(i);
						if("02".equals(TagUtil.replaceNull4String(kColl.getDataValue("acct_attr")))){
							stamp1AcctNo=TagUtil.replaceNull4String(kColl.getDataValue("acct_no"));
						}else if("06".equals(TagUtil.replaceNull4String(kColl.getDataValue("acct_attr")))){
							stamp2AcctNo=TagUtil.replaceNull4String(kColl.getDataValue("acct_no"));
						}
					}
				}
				
				if("1".equals(stampMode)){
					if("".equals(stamp1AcctNo)){
						out_str="不通过";
						out_msg="借款人印花税账号不存在！";
					}
					if("".equals(stamp2AcctNo)){
						out_str="不通过";
						out_msg="委托人印花税账号不存在！";
					}
					if("".equals(stamp1AcctNo)&&"".equals(stamp2AcctNo)){
						out_str="不通过";
						out_msg="委托人印花税账号和委托人印花税账号都不存在！";
					}	
				}else if("2".equals(stampMode)||"4".equals(stampMode)){
					if("".equals(stamp1AcctNo)){
						out_str="不通过";
						out_msg="借款人印花税账号不存在！";
					}
				}else if("3".equals(stampMode)){
					if("".equals(stamp2AcctNo)){
						out_str="不通过";
						out_msg="委托人印花税账号不存在！";
					}
				}else{
					out_str="不通过";
					out_msg="印花税收取方式不存在！";
				}
				
				if(!"".equals(out_str)){
					param.put("OUT_是否通过",out_str);
					param.put("OUT_提示信息",out_msg);
					return param;
				}
				//查询借款人印花税账号账户余额
				if(!"".equals(stamp1AcctNo)){
					getAcctBalance(acctBalanceIColl,stamp1AcctNo,serviceJndi,context,connection);
					KeyedCollection AcctKColl=getKCollByAcctNo(acctBalanceIColl,stamp1AcctNo);
					if(AcctKColl.containsKey("msg")){
						out_str="不通过";
						out_msg=(String) AcctKColl.getDataValue("msg");
						param.put("OUT_是否通过",out_str);
						param.put("OUT_提示信息","查询账户【"+stamp1AcctNo+"】余额查询失败！："+out_msg);
						return param;
					}
				}
				//查询委托人印花税账号余额
				if(!"".equals(stamp2AcctNo)){
					getAcctBalance(acctBalanceIColl,stamp2AcctNo,serviceJndi,context,connection);
					KeyedCollection AcctKColl=getKCollByAcctNo(acctBalanceIColl,stamp2AcctNo);
					if(AcctKColl.containsKey("msg")){
						out_str="不通过";
						out_msg=(String) AcctKColl.getDataValue("msg");
						param.put("OUT_是否通过",out_str);
						param.put("OUT_提示信息",out_msg);
						return param;
					}
				}
				//查询核心获取汇划费
				KeyedCollection kColl=new KeyedCollection();
				kColl.put("accept_branch_id",inAcctBrId);//受理机构代码ID
				kColl.put("ccy", ccy);//币种
				kColl.put("trant_amt", pvpAmt);//交易金额
				kColl.put("dr_acct_no", stamp1AcctNo);//借款人账号
				kColl.put("consign_acct_no",stamp2AcctNo);//委托人账号
				kColl.put("charge_mode",stampMode);//收费方式
				ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
				KeyedCollection retKColl=serviceRel.trade0200200000207(kColl, context, connection);
				if(TagUtil.haveSuccess(retKColl,context)){
					KeyedCollection kColl_BODY = (KeyedCollection) retKColl.getDataElement("BODY");
					BigDecimal stampTax = BigDecimalUtil.replaceNull(kColl_BODY.getDataValue("STAMP_TAX"));//借款人印花税 
					BigDecimal conStampTaxAmt = BigDecimalUtil.replaceNull(kColl_BODY.getDataValue("CON_STAMP_TAX_AMT"));//委托人印花税金额
					if(!"".equals(stamp1AcctNo))
						subFeeAmt(acctBalanceIColl, stamp1AcctNo, stampTax,"2",retparam);//扣借款人印花税 
					if(!"".equals(stamp2AcctNo))
						subFeeAmt(acctBalanceIColl, stamp2AcctNo, conStampTaxAmt,"2",retparam);//扣委托人印花税
				}
			}
			////3、受托贷款汇划费   银行费用查询(11003000016)	通用银行费用查询(02) 接口不通  prd_id.equals("400021")
			CusServiceInterface csInfo = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			String cus_id=TagUtil.replaceNull4String(plaKColl.getDataValue("cus_id"));//客户编码
			CusBase cusBaseInfo = csInfo.getCusBaseByCusId(cus_id,context,connection);
			String belgLine = TagUtil.replaceNull4String(cusBaseInfo.getBelgLine());//所属条线
			String pay_type ="";
			if(clcsKcoll!=null)
				pay_type=TagUtil.replaceNull4String(clcsKcoll.getDataValue("pay_type"));//支付方式  信贷支付方式为：0：自主支付 1：受托支付 2：跨境受托支付。核算支付方式为：1：自主支付 2：受托支付 3：跨境受托支付
			if("BL300".equals(belgLine)&&"1".equals(pay_type)&&checkPrd(serno,prdId,connection,dao)){//核算只有受托支付才收费用
				//获取收息收款账号  /*01放款账号  02印花税账号 03收息收款账号    04受托支付账号     05 主办行资金归集账户*/
				String acctP = "";
				String is_close_loan= TagUtil.replaceNull4String(clcsKcoll.getDataValue("is_close_loan"));//是否无间贷
				boolean isBwBl=false;
				if(prdId.equals("800021")){
					KeyedCollection iqpInterFact = dao.queryDetail("IqpInterFact", serno,connection);
					String fin_type = (String) iqpInterFact.getDataValue("fin_type");
					if("2".equals(fin_type)){
						isBwBl=true;
					}
				}
				if(ictIColl!=null&&ictIColl.size()>0){
					for(int i=0;i<ictIColl.size();i++){
						KeyedCollection kcoll=(KeyedCollection) ictIColl.get(i);
						if("03".equals(TagUtil.replaceNull4String(kcoll.getDataValue("acct_attr")))){
							acctP=TagUtil.replaceNull4String(kcoll.getDataValue("acct_no"));//收息收款账号
						}
					}
					//受托贷款汇划费 都是用收息账户收取的   
					for(int i=0;i<ictIColl.size();i++){
						KeyedCollection kcoll=(KeyedCollection) ictIColl.get(i);
						String acct_attr = TagUtil.replaceNull4String(kcoll.getDataValue("acct_attr"));//账户属性
						String isThisOrg=TagUtil.replaceNull4String(kcoll.getDataValue("is_this_org_acct"));//是否本行账户
						if (acct_attr.equals("04") && isThisOrg.equals("2") &&( isBwBl||(!isBwBl&&!"1".equals(is_close_loan)) ) ) {
							Double pay_amt = TagUtil.replaceNull4Double(kcoll.getDataValue("pay_amt"));//受托支付金额
							KeyedCollection kColl=new KeyedCollection();
							kColl.put("accept_branch_id",inAcctBrId);
							kColl.put("acct_no", acctP);
							kColl.put("trant_amt",pay_amt);
							kColl.put("cash_tran_flag", "2");
							kColl.put("ccy", ccy);
							kColl.put("pay_type", "0");
							ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
							KeyedCollection respBodyKColl=serviceRel.trade1100300001602(kColl, context, connection);
							if(TagUtil.haveSuccess(respBodyKColl,context)){
							KeyedCollection kColl_BODY = (KeyedCollection) respBodyKColl.getDataElement(TradeConstance.ESB_BODY);
							//String payType =TagUtil.replaceNull4String(kColl_BODY.getDataValue("PAY_TYPE"));//交费方式
							int totalRows =TagUtil.replaceNull4Int(kColl_BODY.getDataValue("TOTAL_ROWS"));//总笔数
							IndexedCollection feeArray =(IndexedCollection) kColl_BODY.getDataElement("FEE_ARRAY");//费用数组
							if(totalRows>0){
								for(int fee_i=0;fee_i<feeArray.size();fee_i++){
									KeyedCollection fee = (KeyedCollection) feeArray.get(fee_i);
									BigDecimal feeAmt=BigDecimalUtil.replaceNull(fee.getDataValue("REDUCE_FEE_AMT"));//扣费金额
									//String reduceFeeAcctNo=TagUtil.replaceNull4String(fee.getDataValue("REDUCE_FEE_ACCT_NO"));//扣费账号 核算直接扣的收息收款账号
									String feeCode=TagUtil.replaceNull4String(fee.getDataValue("FEE_CODE"));//费用代码
									/*
									 *费用代码 
									 * CCH00001  受托支付汇划费
									 *   受托支付手续费  
									 */
									KeyedCollection feeAcctKColl=getKCollByAcctNo(acctBalanceIColl,acctP);
									String isThisOrg2=(String) feeAcctKColl.getDataValue("is_this_org_acct");//是否本行账户 
									if(!"1".equals(isThisOrg2)){
										out_str="不通过";
										out_msg="收息收款账号["+acctP+"]不是本行账户,无法查询账户余额信息！";
										break;
									}
									getAcctBalance(acctBalanceIColl,acctP,serviceJndi,context,connection);//获取账户余额
									feeAcctKColl=getKCollByAcctNo(acctBalanceIColl,acctP);
									if(feeAcctKColl.containsKey("msg")){
										out_str="不通过";
										out_msg=(String) feeAcctKColl.getDataValue("msg");
										break;
									}
									subFeeAmt(acctBalanceIColl, acctP,feeAmt,"3",retparam);//
								}
							}
							if(!"".equals(out_str)){
								param.put("OUT_是否通过",out_str);
								param.put("OUT_提示信息",out_msg);
								return param;
							}
						}
							
					}
				}
			}
			}
			////3、银承工本费  每张票固定收0.48 PAYM收款收息账户收取
			//银行承兑汇票业务
			if(prdId.equals("200024")){
				IndexedCollection iaaIColl = dao.queryList(iaaModelId," WHERE serno=(SELECT t.serno FROM Pvp_Loan_App pla,Ctr_Loan_Cont t WHERE pla.cont_no=t.cont_no and pla.serno='"+serno+"') ", connection);
				KeyedCollection iaaKColl=null;
				if(iaaIColl!=null&&iaaIColl.size()>0){
					iaaKColl=(KeyedCollection) iaaIColl.get(0);
				}else
					throw new Exception("查询银行承兑汇票信息失败！");
				String is_elec_bill = TagUtil.replaceNull4String(iaaKColl.getDataValue("is_elec_bill"));//是否电子票据
				if("2".equals(is_elec_bill)){//纸票才收取工本费
					String acctP="";
					for(int i=0;i<ictIColl.size();i++){
						KeyedCollection kcoll=(KeyedCollection) ictIColl.get(i);
						if("03".equals(TagUtil.replaceNull4String(kcoll.getDataValue("acct_attr")))){
							acctP=TagUtil.replaceNull4String(kcoll.getDataValue("acct_no"));//收息收款账号
						}
					}
					KeyedCollection feeAcctKColl=getKCollByAcctNo(acctBalanceIColl,acctP);
					String isThisOrg2=(String) feeAcctKColl.getDataValue("is_this_org_acct");//是否本行账户 
					if("1".equals(isThisOrg2)){
						getAcctBalance(acctBalanceIColl,acctP,serviceJndi,context,connection);//获取账户余额
						feeAcctKColl=getKCollByAcctNo(acctBalanceIColl,acctP);
						if(feeAcctKColl.containsKey("msg")){
							out_str="不通过";
							out_msg=(String) feeAcctKColl.getDataValue("msg");
						}else{
							IndexedCollection detIColl = dao.queryList("IqpAccpDetail"," WHERE serno=(SELECT t.serno FROM Pvp_Loan_App pla,Ctr_Loan_Cont t WHERE pla.cont_no=t.cont_no and pla.serno='"+serno+"') ", connection);
							int iadCount=0;
							if(detIColl!=null&&detIColl.size()>0) iadCount=detIColl.size();
							subFeeAmt(acctBalanceIColl, acctP,new BigDecimal(0.48*iadCount),"4",retparam);//每一张票据都要收取0.48
						}
					}else{
						out_str="不通过";
						out_msg="收息收款账号["+acctP+"]不是本行账户,无法查询账户余额信息！";
					}
				}
				
				if(!"".equals(out_str)){
					param.put("OUT_是否通过",out_str);
					param.put("OUT_提示信息",out_msg);
					return param;
				}
			}
			///4、代理贴现汇划费    直贴业务prdId.equals("300021") || prdId.equals("300020")){
			IndexedCollection discIColl=dao.queryList("CtrDiscCont"," where cont_no =(SELECT pla.cont_no FROM Pvp_Loan_App pla WHERE pla.serno='"+serno+"') ", connection);
			if(discIColl!=null&&discIColl.size()>0){
				KeyedCollection descKColl=(KeyedCollection) discIColl.get(0);
				String cont_no=TagUtil.replaceNull4String(descKColl.getDataValue("cont_no"));
				String acctP="";
				String is_this_org_acct="";
				for(int i=0;i<ictIColl.size();i++){
					KeyedCollection kcoll=(KeyedCollection) ictIColl.get(i);
					if("03".equals(TagUtil.replaceNull4String(kcoll.getDataValue("acct_attr")))){
						acctP=TagUtil.replaceNull4String(kcoll.getDataValue("acct_no"));//收息收款账号
					}else if("01".equals(TagUtil.replaceNull4String(kcoll.getDataValue("acct_attr")))){//放款账户
						is_this_org_acct=TagUtil.replaceNull4String(kcoll.getDataValue("is_this_org_acct"));//放款是否是行内
					}
				}
				if("2".equals(is_this_org_acct)){//放款账户是否是本行
					String condictionStr="WHERE Iqp_Bill_Detail.porder_no in (SELECT ibbr.porder_no FROM Iqp_Batch_Bill_Rel ibbr WHERE ibbr.batch_no=(SELECT ibm.batch_no FROM Iqp_Batch_Mng ibm WHERE ibm.cont_no='"+cont_no+"'))";
					IndexedCollection ibdIColl=dao.queryList("IqpBillDetail",condictionStr,connection);
					if(ibdIColl!=null&&ibdIColl.size()>0){
						for(int i=0;i<ibdIColl.size();i++){
							KeyedCollection billDetailKColl=(KeyedCollection) ibdIColl.get(i);
							BigDecimal amt=BigDecimalUtil.replaceNull(billDetailKColl.getDataValue("drft_amt"));
							KeyedCollection kColl=new KeyedCollection();
							kColl.put("accept_branch_id",inAcctBrId);
							kColl.put("acct_no", acctP);
							kColl.put("trant_amt",amt);
							kColl.put("cash_tran_flag", "2");
							kColl.put("ccy", ccy);
							kColl.put("pay_type", "0");
							ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
							KeyedCollection respBodyKColl=serviceRel.trade1100300001602(kColl, context, connection);
							if(TagUtil.haveSuccess(respBodyKColl,context)){
								KeyedCollection kColl_BODY = (KeyedCollection) respBodyKColl.getDataElement(TradeConstance.ESB_BODY);
								//String payType =TagUtil.replaceNull4String(kColl_BODY.getDataValue("PAY_TYPE"));//交费方式
								int totalRows =TagUtil.replaceNull4Int(kColl_BODY.getDataValue("TOTAL_ROWS"));//总笔数
								IndexedCollection feeArray =(IndexedCollection) kColl_BODY.getDataElement("FEE_ARRAY");//费用数组
								if(totalRows>0){
									for(int fee_i=0;fee_i<feeArray.size();fee_i++){
										KeyedCollection fee = (KeyedCollection) feeArray.get(fee_i);
										BigDecimal feeAmt=BigDecimalUtil.replaceNull(fee.getDataValue("REDUCE_FEE_AMT"));//扣费金额
										//String reduceFeeAcctNo=TagUtil.replaceNull4String(fee.getDataValue("REDUCE_FEE_ACCT_NO"));//扣费账号 核算直接扣的收息收款账号
										String feeCode=TagUtil.replaceNull4String(fee.getDataValue("FEE_CODE"));//费用代码
										/*
										 *费用代码 
										 * CCH00001 代理贴现邮电费用
										 *   代理贴现费用  
										 */
										KeyedCollection feeAcctKColl=getKCollByAcctNo(acctBalanceIColl,acctP);
										String isThisOrg2=(String) feeAcctKColl.getDataValue("is_this_org_acct");//是否本行账户 
										if(!"1".equals(isThisOrg2)){
											out_str="不通过";
											out_msg="收息收款账号["+acctP+"]不是本行账户,无法查询账户余额信息！";
											break;
										}
										getAcctBalance(acctBalanceIColl,acctP,serviceJndi,context,connection);//获取账户余额
										feeAcctKColl=getKCollByAcctNo(acctBalanceIColl,acctP);
										if(feeAcctKColl.containsKey("msg")){
											out_str="不通过";
											out_msg=(String) feeAcctKColl.getDataValue("msg");
											break;
										}
										subFeeAmt(acctBalanceIColl, acctP,feeAmt,"5",retparam);//
									}
								}
								if(!"".equals(out_str)){
									param.put("OUT_是否通过",out_str);
									param.put("OUT_提示信息",out_msg);
									return param;
								}
							
							}
						}
					}
				}
			}
			
			//5、贴现利息:商业承兑汇票贴现/银行承兑汇票贴现
			if(prdId.equals("300020")||prdId.equals("300021")){
				String cont_no=TagUtil.replaceNull4String(plaKColl.getDataValue("cont_no"));//合同号
				String relCondition="  where batch_no in (SELECT ibm.batch_no FROM  Iqp_Batch_Mng ibm WHERE ibm.cont_no='" + cont_no + "') ";
				IndexedCollection relIColl = dao.queryList("IqpBatchBillRel", relCondition, connection);
				if(relIColl!=null&&relIColl.size()>0){
					for(int i=0;i<relIColl.size();i++){
						KeyedCollection relKColl = (KeyedCollection) relIColl.get(i);
						String porder_no = (String) relKColl.getDataValue("porder_no");//票据号
						String batch_no = (String) relKColl.getDataValue("batch_no");//票据号
						Map<String,String> incomeMap = new HashMap<String,String>();
						incomeMap.put("batch_no",batch_no);
						incomeMap.put("porder_no",porder_no);
						/**（2）收益表信息*/
						KeyedCollection incomeKColl = dao.queryDetail("IqpBillIncome", incomeMap, connection);
						String dscnt_int_pay_mode=TagUtil.replaceNull4String(incomeKColl.getDataValue("dscnt_int_pay_mode"));//贴现利息支付方式
						/*
						 * 1://卖方付息  没有IqpBillPintDetail记录 从放款账户扣
						 * 2://买方付息
						 * 3://协议付息
						 */
						if("1".equals(dscnt_int_pay_mode)){//卖方付息 
							/*BigDecimal int_amt =BigDecimalUtil.replaceNull(incomeKColl.getDataValue("int"));//利息 从放款账户扣
							String actvNo="";
							for(int k=0;k<ictIColl.size();k++){
								KeyedCollection kColl=(KeyedCollection) ictIColl.get(k);
								if("01".equals(TagUtil.replaceNull4String(kColl.getDataValue("acct_attr")))){//放款账户
									actvNo=TagUtil.replaceNull4String(kColl.getDataValue("acct_no"));
								}
							}
							
							getAcctBalance(acctBalanceIColl,actvNo,serviceJndi,context,connection);//获取账户余额
							KeyedCollection feeAcctKColl=getKCollByAcctNo(acctBalanceIColl,actvNo);
							if(feeAcctKColl.containsKey("msg")){
								out_str="不通过";
								out_msg=(String) feeAcctKColl.getDataValue("msg");
								break;
							}
							subFeeAmt(acctBalanceIColl, actvNo,int_amt,"6",retparam);//
*/							
						}else{
							String pintCondition = " where batch_no='"+batch_no+"' and porder_no = '"+porder_no+"'";
							IndexedCollection pintIColl = dao.queryList("IqpBillPintDetail", pintCondition, connection);
							for(int k=0;k<pintIColl.size();k++){
								KeyedCollection temp = (KeyedCollection)pintIColl.get(k);
								String pint_no =TagUtil.replaceNull4String(temp.getDataValue("pint_no"));//付息账户
								BigDecimal pint_amt =BigDecimalUtil.replaceNull(temp.getDataValue("pint_amt"));//付息金额
								String is_local_bank =TagUtil.replaceNull4String(temp.getDataValue("is_local_bank"));//是否本行账号
								if("1".equals(is_local_bank)){
									if(!acctNos.contains(pint_no)){//查询不重复的账户信息
										acctNos.add(pint_no);//已经查询的账户不再查询
										KeyedCollection kColl=new KeyedCollection();
										kColl.addDataField("acct_no",pint_no);//账户信息 付息信息中新增账户信息
										acctBalanceIColl.add(kColl);
									}
									getAcctBalance(acctBalanceIColl,pint_no,serviceJndi,context,connection);//获取账户余额
									KeyedCollection feeAcctKColl=getKCollByAcctNo(acctBalanceIColl,pint_no);
									if(feeAcctKColl.containsKey("msg")){
										out_str="不通过";
										out_msg=(String) feeAcctKColl.getDataValue("msg");
										break;
									}
									subFeeAmt(acctBalanceIColl, pint_no,pint_amt,"6",retparam);//
								}else{
									out_str="不通过";
									out_msg="付息账号["+pint_no+"]不是本行账户,无法查询账户余额信息！";
									break;
								}
								
							}
						}
						
						if(!"".equals(out_str)){
							param.put("OUT_是否通过",out_str);
							param.put("OUT_提示信息",out_msg);
							return param;
						}
					}
				}
			}
			
			boolean istrue=true;
			for(int i=0;i<acctBalanceIColl.size();i++){
				KeyedCollection kcoll=(KeyedCollection) acctBalanceIColl.get(i);
				if(kcoll.containsKey("balance")){
					BigDecimal balance=BigDecimalUtil.replaceNull(kcoll.getDataValue("balance"));
					if(balance.compareTo(new BigDecimal(0))<0){
						istrue=false;
						param.put("OUT_是否通过","不通过");
						if(param.containsKey("OUT_提示信息")){
							String str=param.get("OUT_提示信息");
							if(str==null) str="";
							str+="；扣款账户"+TagUtil.replaceNull4String(kcoll.getDataValue("acct_no"))+"余额不足，需缴存金额为"+balance.abs().setScale(2,BigDecimal.ROUND_HALF_UP) +"！";
							param.put("OUT_提示信息",str);
						}else
							param.put("OUT_提示信息","扣款账户"+TagUtil.replaceNull4String(kcoll.getDataValue("acct_no"))+"余额不足，需缴存金额为"+balance.abs().setScale(2,BigDecimal.ROUND_HALF_UP) );
					}
				}
			}
			
			if(istrue){
				param.put("OUT_是否通过","通过");
				param.put("OUT_提示信息","扣款账户余额检查通过！");
			}
			
			if(retparam.size()>0){
				Set<String> keys=retparam.keySet();
				for(String feeType: keys){
					if("1".equals(feeType)){
						System.out.println("信贷系统费用项目金额："+retparam.get(feeType));
					}else if("2".equals(feeType)){
						System.out.println("印花税金额："+retparam.get(feeType));
					}else if("3".equals(feeType)){
						System.out.println("受托支付汇划费："+retparam.get(feeType));
					}else if("4".equals(feeType)){
						System.out.println("银承工本费："+retparam.get(feeType));
					}else if("5".equals(feeType)){
						System.out.println("代理支付汇划费："+retparam.get(feeType));
					}else if("6".equals(feeType)){
						System.out.println("付息："+retparam.get(feeType));
					}else{
						System.out.println("费用类型不对");
					}
				}
				
			}
			
			
		} catch (Exception e) {
			logger.error("扣款账户余额及收费情况检查失败！"+e.getMessage());
			throw new EMPException(e);
		}
		return param;
	}

	/**
	 * 返回收费期数
	 * date 合同起始日
	 * end_date 到期日
	 * chrg_freq 收费频率
	 * @return
	 */
	private int getFeeCnt(String date,String end_date,String chrg_freq){
		Date startDt=TimeUtil.strToDate(date);//合同起始日
		Date endDt=TimeUtil.strToDate(end_date);
		int feeCnt=0;//收费期数
		int chrgFreq=Integer.parseInt(chrg_freq);//收费频率
		while(startDt.compareTo(endDt)<0){
			feeCnt++;
			String tmpDt=TimeUtil.ADD_MONTH(TimeUtil.dateToStr(startDt), chrgFreq);//起始日期+收费频率
			startDt=TimeUtil.strToDate(tmpDt);
		}
		return feeCnt;
	}
	/**
	 * 账户余额减去费用金额
	 * @param ictIColl
	 * @param fee_acct_no
	 * @param feeAmt
	 * @throws Exception
	 */
	private void subFeeAmt(IndexedCollection acctBalanceIColl,String fee_acct_no,BigDecimal feeAmt,String feeType,Map<String,BigDecimal> retparam) throws Exception{
		KeyedCollection kColl=getKCollByAcctNo(acctBalanceIColl,fee_acct_no);
		BigDecimal balance=BigDecimalUtil.replaceNull(kColl.getDataValue("balance"));
		/*
		 * feeType 
		 * 1 信贷手续费
		 * 2 印花税
		 * 3 受托支付汇划费
		 * 4 银承工本费
		 * 5 代理贴现汇划费
		 * 6 付息
		 */
		if(retparam.containsKey(feeType)){
			BigDecimal amt=retparam.get(feeType);
			amt=amt.add(feeAmt);
			retparam.put(feeType, amt);
		}else{
			if("1".equals(feeType)){
				retparam.put(feeType, feeAmt);
			}else if("2".equals(feeType)){
				retparam.put(feeType, feeAmt);
			}else if("3".equals(feeType)){
				retparam.put(feeType, feeAmt);
			}else if("4".equals(feeType)){
				retparam.put(feeType,feeAmt);
			}else if("5".equals(feeType)){
				retparam.put(feeType,feeAmt);
			}else if("6".equals(feeType)){
				retparam.put(feeType,feeAmt);
			}
		}
		
		kColl.setDataValue("balance", balance.subtract(feeAmt).setScale(2,BigDecimal.ROUND_HALF_UP) );
	}
	
	/**
	 * 查询核心获取账户余额
	 * @param acctBalanceIColl
	 * @param acct_no
	 * @param service
	 * @param context
	 * @param connection
	 * @throws Exception
	 */
	private void getAcctBalance(IndexedCollection acctBalanceIColl,String acct_no,CMISModualServiceFactory serviceJndi,Context context,Connection connection) throws Exception{
		ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
		KeyedCollection bKColl=getKCollByAcctNo(acctBalanceIColl,acct_no);
		if(bKColl.containsKey("balance")||bKColl.containsKey("msg")) return ;//已经查询过的跳过
		KeyedCollection retKColl = null;
		KeyedCollection BODY = new KeyedCollection("BODY");
		try{
			retKColl = service.tradeZHZH(acct_no, context, connection);
			KeyedCollection kColl=getKCollByAcctNo(acctBalanceIColl,acct_no);
			if(TagUtil.haveSuccess(retKColl, context)){//成功
				BODY = (KeyedCollection)retKColl.getDataElement("BODY");
				BigDecimal balance=BigDecimalUtil.replaceNull(BODY.getDataValue("BALANCE"));
				kColl.put("balance",balance.abs());//取绝对值
			}else{
				kColl.put("msg",(String)retKColl.getDataValue("RET_MSG"));//
			}
		}catch(Exception e){
			throw new Exception("ESB通讯接口【获取账户信息】交易失败："+e.getMessage());
		}
	}
	
	private KeyedCollection getKCollByAcctNo(IndexedCollection acctBalanceIColl,String acct_no) throws Exception{
		boolean isExist=false;
		KeyedCollection retKColl=null;
		for(int i=0;i<acctBalanceIColl.size();i++){
			KeyedCollection kColl=(KeyedCollection) acctBalanceIColl.get(i);
			if(acct_no.equals(TagUtil.replaceNull4String(kColl.getDataValue("acct_no")))){
				retKColl=kColl;
				isExist=true;
				break;
			}
		}
		if(!isExist){
			throw new Exception("账户【"+acct_no+"】不存在合同的「账户信息」中！");
		}
		return retKColl;
	}
	
	private boolean checkPrd(String serno,String prdId,Connection connection,TableModelDAO dao) throws Exception{
		if(prdId==null) return false;
		if(prdId.equals("200024")||prdId.equals("300021")||prdId.equals("300020")
				||prdId.equals("300024")||prdId.equals("300023")||prdId.equals("300022")
				||prdId.equals("400021")||prdId.equals("400022")||prdId.equals("400023")
				||prdId.equals("500029")||prdId.equals("500028")||prdId.equals("500027")
				||prdId.equals("400020")|| prdId.equals("500032")||prdId.equals("700020")
				||prdId.equals("700021")||prdId.equals("600020")||prdId.equals("600021")
				||prdId.equals("600022")) return false;
		
		return true;
	}
}
