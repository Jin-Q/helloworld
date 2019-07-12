package com.yucheng.cmis.biz01line.esb.op.trade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 
*@author lisj
*@time 2015-4-7
*@description TODO 需求编号：【XD150318023】关于信贷系统增加循环贷类个贷 产品需求
*				         个人自助需要登记协议信息；
*				         小微和法人是客户经理直接在信贷系统登记；
*				         自助是指客户可以通过网银、手机银行自助放款申请、自助还款
*@version v1.0
*
 */
public class Trade0200200001001 extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		try {
			EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
			Context context = factory.getContextNamed(factory.getRootContextName());
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			//KeyedCollection reqSysHead = esbInterface.getReqSysHead(CD);
			String cus_id = reqBody.getDataValue("CLIENT_NO").toString().trim();//客户号
			IndexedCollection client_array = (IndexedCollection) reqBody.getDataElement("CLIENT_ARRAY");//客户信息数组
			IndexedCollection fee_array = (IndexedCollection) reqBody.getDataElement("FEE_ARRAY");//费用数组（对应信贷IQP_APPEND_TERMS表）
			IndexedCollection acct_info_array = (IndexedCollection) reqBody.getDataElement("ACCT_INFO_ARRAY");//账号信息数组（对应信贷IQP_CUS_ACCT表）
			String serno = null;
			String cont_no =null;
			String openday  = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			try {
				serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);//生成业务流水号
			} catch (Exception e) {}
			if(serno == null || "".equals(serno))
				throw new EMPJDBCException("生成业务流水号异常！");
			try {
			   cont_no = CMISSequenceService4JXXD.querySequenceFromDB("HT", "all", connection, context);//生成合同编号
			} catch (Exception e) {}
			if(cont_no == null || "".equals(cont_no))
				throw new EMPJDBCException("生成业务合同编号异常");
			
			
			context.put("OPENDAY", openday);//当前营业日
			String flag = "success";
            if(cus_id==null || "".equals(cus_id)){
            	flag = "CIDISNULL";
            }
            //added by yangzy 2015/05/25 微贷平台零售自助贷款改造 start
            BigDecimal selfcount = (BigDecimal)SqlClient.queryFirst("queryCtrLoanForSelfByCusId", cus_id, null, connection);
			int self = Integer.parseInt(selfcount.toString());
			if(self > 0){
				flag = "existCont";
			}
            if("success".equals(flag)){
				
				String cn_cont_no = reqBody.getDataValue("CONTRACT_NO").toString().trim();//中文合同编号
				String cus_card_code = reqBody.getDataValue("CARD_NO").toString().trim(); //自助卡号
				String assure_main = reqBody.getDataValue("GUARANTEE_MODE").toString().trim();//担保方式
				String assure_main_details = reqBody.getDataValue("GUARANTEE_MODE_DETAIL").toString().trim();//担保方式细分
				String cont_cur_type = reqBody.getDataValue("CCY").toString().trim();//币种
				
				Double cont_amt = Double.valueOf(reqBody.getDataValue("CONTRACT_AMT").toString().trim());//合同金额  
				String cont_start_date = TagUtil.formatDate2Ten(reqBody.getDataValue("START_DATE").toString().trim()); //合同起始日
				String cont_end_date = TagUtil.formatDate2Ten(reqBody.getDataValue("END_DATE").toString().trim());//合同到期日
				Double exchange_rate = Double.valueOf(reqBody.getDataValue("EXCHANGE_RATE").toString().trim());//汇率
				String manager_br_id = reqBody.getDataValue("MANA_ORG").toString().trim();//管理机构
				
				String input_id = reqBody.getDataValue("REGISTERED_ID").toString().trim();  //登记人
				String input_br_id = reqBody.getDataValue("REGIST_ORG_NO").toString().trim(); //登记机构
				String in_acct_br_id = reqBody.getDataValue("INCOME_ORG_NO").toString().trim();//入账机构
				String input_date = TagUtil.formatDate2Ten(reqBody.getDataValue("REGISTERED_DATE").toString().trim());//登记日期
				String cont_term = reqBody.getDataValue("TERM").toString().trim();//合同期限
				
				String term_type = reqBody.getDataValue("TERM_TYPE").toString().trim();//期限类型  
				String is_delay = reqBody.getDataValue("DEFER_FLAG").toString().trim(); //是否顺延
				Double reality_ir_y = Double.valueOf(reqBody.getDataValue("ACT_INT_RATE").toString().trim());//执行利率（年）
				Double overdue_rate_y = Double.valueOf(reqBody.getDataValue("OVERDUE_INT_RATE").toString().trim());//逾期利率（年）
				Double default_rate_y =Double.valueOf(reqBody.getDataValue("PENALTY_INT_RATE").toString().trim());//违约利率（年）
				
				String repay_type = reqBody.getDataValue("REPAY_TYPE").toString().trim();//还款方式
				String repay_date = reqBody.getDataValue("REPAY_DATE").toString().trim(); //还款日
				String five_classfiy = reqBody.getDataValue("FIVE_LEVEL_TYPE").toString().trim();//五级分类
				String spe_loan_type = reqBody.getDataValue("SPE_LOAN_TYPE").toString().trim();//特殊贷款类型
				String limit_useed_type = reqBody.getDataValue("LIMIT_USEED_TYPE").toString().trim();//额度占用类型
				
				String loan_use_type = reqBody.getDataValue("DR_USAGE").toString().trim(); //借款用途 
				String com_up_indtify = reqBody.getDataValue("FLAG").toString().trim(); //工业转型升级标识
				String loan_type = reqBody.getDataValue("LOAN_KIND").toString().trim();//贷款种类
				String agriculture_type = reqBody.getDataValue("AGRI_LOAN_KIND").toString().trim();//涉农贷款类型
				String person_loan_kind = reqBody.getDataValue("PERSON_LOAN_KIND").toString().trim();//个人经营性贷款类型
				
				String estate_adjust_type = reqBody.getDataValue("ADJUST_TYPE").toString().trim();  //产业结构调整类型 
				String strategy_new_type = reqBody.getDataValue("NEW_PRD_TYPE").toString().trim();//新兴产业类型
				String new_prd_loan = reqBody.getDataValue("NEW_PRD_LOAN").toString().trim();//新兴产业贷款
				String loan_direction = reqBody.getDataValue("LOAN_DIRECTION").toString().trim();//贷款投向
				String loan_belong1 = reqBody.getDataValue("LOAN_BELONG1").toString().trim();		
				String loan_belong2 = reqBody.getDataValue("LOAN_BELONG2").toString().trim();  
				String loan_belong3 = reqBody.getDataValue("LOAN_BELONG3").toString().trim(); 
				String loan_belong4 = reqBody.getDataValue("LOAN_BELONG4").toString().trim();
				String buss_type = reqBody.getDataValue("BUSS_TYPE").toString().trim();//业务类型 （001 消费类 002 经营类 对应100084、100085产品编号）
				String chi_cust_manager = reqBody.getDataValue("CHI_CUST_MANAGER").toString().trim();//主管客户经理
				
				//生成业务信息及合同信息（涉及IQP_LOAN_APP,IQP_LOAN_APP_SUB,CTR_LOAN_CONT,CTR_LOAN_CONT_SUB表）
				KeyedCollection kColl4ILA = new KeyedCollection("IqpLoanApp");
				KeyedCollection kColl4ILAS = new KeyedCollection("IqpLoanAppSub");
				KeyedCollection kColl4CLC = new KeyedCollection("CtrLoanCont");
				KeyedCollection kColl4CLCS = new KeyedCollection("CtrLoanContSub");
				
				//业务申请主表
				kColl4ILA.put("serno", serno);
				kColl4ILA.put("cus_id", cus_id);
				if(!"".equals(buss_type) && "001".equals(buss_type)){
					kColl4ILA.put("prd_id", "100085");
				}else if(!"".equals(buss_type) && "002".equals(buss_type)){
					kColl4ILA.put("prd_id", "100084");
				}
				kColl4ILA.put("biz_type", "7");//业务模式默认为普通
				kColl4ILA.put("assure_main", assure_main);
				kColl4ILA.put("assure_main_details", assure_main_details);
				kColl4ILA.put("apply_cur_type", cont_cur_type);//申请与合同币种一致
				kColl4ILA.put("apply_amount", cont_amt);//申请与合同金额一致
				kColl4ILA.put("is_trust_loan", "2");//非信托贷款
				kColl4ILA.put("is_promissory_note", "2");//非承诺函下
				kColl4ILA.put("is_rfu", "2");//未曾被拒绝
				kColl4ILA.put("apply_date", cont_start_date);
				kColl4ILA.put("exchange_rate", exchange_rate);
				kColl4ILA.put("manager_br_id", manager_br_id);
				kColl4ILA.put("input_id", input_id);
				kColl4ILA.put("input_br_id", input_br_id);
				kColl4ILA.put("input_date", input_date);
				kColl4ILA.put("approve_status", "997");//默认通过状态
				kColl4ILA.put("in_acct_br_id", in_acct_br_id);//入账机构
				kColl4ILA.put("limit_ind", "1");//额度使用标志
				//业务申请子表
				kColl4ILAS.put("serno", serno);
				kColl4ILAS.put("is_close_loan", "2");//非无间贷
				kColl4ILAS.put("loan_form", "1");//新增贷款
				kColl4ILAS.put("loan_nature", "1");//自营贷款（贷款性质）
				kColl4ILAS.put("conf_pay_type", "1");//确定支付方式
				kColl4ILAS.put("pay_type", "0");//自主支付
				kColl4ILAS.put("apply_term", cont_term);//申请期限
				kColl4ILAS.put("term_type", term_type);//期限类型
				kColl4ILAS.put("reality_ir_y", reality_ir_y);
				kColl4ILAS.put("overdue_rate_y", overdue_rate_y);
				kColl4ILAS.put("default_rate_y", default_rate_y);
				
				kColl4ILAS.put("repay_date", repay_date);
				kColl4ILAS.put("repay_type", repay_type);
				kColl4ILAS.put("interest_term", "2");//计息周期默认按月
				kColl4ILAS.put("repay_term", "M");//还款间隔周期默认月
				kColl4ILAS.put("repay_space", "1");//还款间隔默认1
				kColl4ILAS.put("is_term", "2");//是否期供
				kColl4ILAS.put("five_classfiy", five_classfiy);
				kColl4ILAS.put("spe_loan_type", spe_loan_type);
				kColl4ILAS.put("limit_useed_type", limit_useed_type);
				kColl4ILAS.put("loan_type", loan_type);
				kColl4ILAS.put("estate_adjust_type", estate_adjust_type);
				kColl4ILAS.put("ensure_project_loan", person_loan_kind);
				kColl4ILAS.put("com_up_indtify", com_up_indtify);
				kColl4ILAS.put("strategy_new_loan", strategy_new_type);
				kColl4ILAS.put("new_prd_loan", new_prd_loan);
				kColl4ILAS.put("agriculture_type", agriculture_type);
				kColl4ILAS.put("loan_direction", loan_direction);
				kColl4ILAS.put("loan_belong1", loan_belong1);
				kColl4ILAS.put("loan_belong2", loan_belong2);
				kColl4ILAS.put("loan_belong3", loan_belong3);
				kColl4ILAS.put("loan_belong4", loan_belong4);		
				kColl4ILAS.put("loan_use_type", loan_use_type);
				kColl4ILAS.put("is_delay", is_delay);
				kColl4ILAS.put("cus_card_code", cus_card_code);
				kColl4ILAS.put("ir_accord_type", "01");//利率依据方式
				kColl4ILAS.put("is_collect_stamp", "2");//是否收取印花税
				//业务合同主表
				kColl4CLC.put("ser_date", input_date);
				kColl4CLC.put("serno", serno);
				kColl4CLC.put("cont_no", cont_no);
				kColl4CLC.put("cn_cont_no", cn_cont_no);
				kColl4CLC.put("cus_id", cus_id);
				if(!"".equals(buss_type) && "001".equals(buss_type)){
					kColl4CLC.put("prd_id", "100085");
				}else if(!"".equals(buss_type) && "002".equals(buss_type)){
					kColl4CLC.put("prd_id", "100084");
				}
				kColl4CLC.put("biz_type", "7");//业务模式默认为普通
				kColl4CLC.put("assure_main", assure_main);
				kColl4CLC.put("assure_main_details", assure_main_details);
				kColl4CLC.put("is_trust_loan", "2");//非信托贷款
				kColl4CLC.put("is_promissory_note", "2");//非承诺函下
				kColl4CLC.put("cont_cur_type", cont_cur_type);
				kColl4CLC.put("cont_amt", cont_amt);
				kColl4CLC.put("cont_balance", cont_amt);//与合同金额一致
				kColl4CLC.put("cont_start_date", cont_start_date);			
				kColl4CLC.put("cont_end_date", cont_end_date);
				kColl4CLC.put("exchange_rate", exchange_rate);
				kColl4CLC.put("manager_br_id", manager_br_id);
				kColl4CLC.put("input_id", input_id);
				kColl4CLC.put("input_br_id", input_br_id);
				kColl4CLC.put("input_date", input_date);
				kColl4CLC.put("cont_status", "200");//默认未生效状态
				kColl4CLC.put("in_acct_br_id", in_acct_br_id);//入账机构
				kColl4CLC.put("limit_ind", "1");//额度使用标志
				//业务合同子表
				kColl4CLCS.put("cont_no", cont_no);
				kColl4CLCS.put("is_close_loan", "2");//非无间贷
				kColl4CLCS.put("loan_form", "1");//新增贷款
				kColl4CLCS.put("loan_nature", "1");//自营贷款（贷款性质）
				kColl4CLCS.put("conf_pay_type", "1");//确定支付方式
				kColl4CLCS.put("pay_type", "0");//自主支付
				kColl4CLCS.put("cont_term", cont_term);//申请期限
				kColl4CLCS.put("term_type", term_type);//期限类型
				kColl4CLCS.put("reality_ir_y", reality_ir_y);
				kColl4CLCS.put("overdue_rate_y", overdue_rate_y);
				kColl4CLCS.put("default_rate_y", default_rate_y);
				
				kColl4CLCS.put("repay_date", repay_date);//还款日
				kColl4CLCS.put("repay_type", repay_type);//还款方式
				kColl4CLCS.put("interest_term", "2");//计息周期默认按月
				kColl4CLCS.put("repay_term", "M");//还款间隔周期默认月
				kColl4CLCS.put("repay_space", "1");//还款间隔默认1
				kColl4CLCS.put("is_term", "2");//是否期供
				kColl4CLCS.put("five_classfiy", five_classfiy);
				kColl4CLCS.put("spe_loan_type", spe_loan_type);
				kColl4CLCS.put("limit_useed_type", limit_useed_type);
				kColl4CLCS.put("loan_type", loan_type);
				kColl4CLCS.put("estate_adjust_type", estate_adjust_type);
				kColl4CLCS.put("ensure_project_loan", person_loan_kind);
				kColl4CLCS.put("com_up_indtify", com_up_indtify);
				kColl4CLCS.put("strategy_new_type", strategy_new_type);
				kColl4CLCS.put("new_prd_loan", new_prd_loan);
				kColl4CLCS.put("agriculture_type", agriculture_type);
				kColl4CLCS.put("loan_direction", loan_direction);
				kColl4CLCS.put("loan_belong1", loan_belong1);
				kColl4CLCS.put("loan_belong2", loan_belong2);
				kColl4CLCS.put("loan_belong3", loan_belong3);
				kColl4CLCS.put("loan_belong4", loan_belong4);		
				kColl4CLCS.put("loan_use_type", loan_use_type);
				kColl4CLCS.put("is_delay", is_delay);
				kColl4CLCS.put("cus_card_code", cus_card_code);
				kColl4CLCS.put("ir_accord_type", "01");//利率依据方式
				kColl4CLCS.put("is_collect_stamp", "2");//是否收取印花税
				//生成账户信息
				if(acct_info_array!=null && acct_info_array.size() >0){
					for(int i=0;i < acct_info_array.size();i++){
						KeyedCollection kColl4AcctInfo = (KeyedCollection) acct_info_array.get(i);
						String acct_attr = kColl4AcctInfo.getDataValue("ACCT_CHRT").toString().trim();//账号属性
						String is_this_org_acct = kColl4AcctInfo.getDataValue("OWN_BRANCH_FLAG").toString().trim();//是否本行账户		
						String acct_no = kColl4AcctInfo.getDataValue("ACCT_NO").toString().trim();//账号
						String acct_name = kColl4AcctInfo.getDataValue("ACCT_NAME").toString().trim(); //户名
						String opac_org_no = kColl4AcctInfo.getDataValue("OPEN_ACCT_BRANCH_ID").toString().trim();//开户行行号/开户机构码（本行）			
						String opan_org_name = kColl4AcctInfo.getDataValue("OPEN_ACCT_BRANCH_NAME").toString().trim();//开户行行名/开户机构名称（本行）
						String pay_amt = kColl4AcctInfo.getDataValue("PAY_AMT").toString().trim();//支付金额		
						String acct_gl_code = kColl4AcctInfo.getDataValue("GL_CODE").toString().trim();//科目号  
						String cur_type = kColl4AcctInfo.getDataValue("CCY").toString().trim(); //币种
						String interbank_id = kColl4AcctInfo.getDataValue("C_INTERBANK_ID").toString().trim();//银联行号
						
						//生成账户信息明细
						KeyedCollection kColl4IqpCusAcct = new KeyedCollection();
						String pk_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);//生成PKID值
						kColl4IqpCusAcct.put("serno", serno);//与业务流水号一致
						kColl4IqpCusAcct.put("cont_no", cont_no);//与业务合同编号一致
						kColl4IqpCusAcct.put("acct_attr", acct_attr);
						kColl4IqpCusAcct.put("is_this_org_acct", is_this_org_acct);
						kColl4IqpCusAcct.put("acct_no", acct_no);
						kColl4IqpCusAcct.put("acct_name", acct_name);
						kColl4IqpCusAcct.put("opac_org_no", opac_org_no);
						kColl4IqpCusAcct.put("opan_org_name", opan_org_name);
						kColl4IqpCusAcct.put("pay_amt", pay_amt);
						kColl4IqpCusAcct.put("acct_gl_code", acct_gl_code);
						kColl4IqpCusAcct.put("cur_type", cur_type);
						kColl4IqpCusAcct.put("interbank_id", interbank_id);
						kColl4IqpCusAcct.put("pk_id", pk_id);
						kColl4IqpCusAcct.setName("IqpCusAcct");
						dao.insert(kColl4IqpCusAcct, connection);
					}
				}
				
				//校验该客户是否已存在信贷系统数据库中
				KeyedCollection kColl4Cus = (KeyedCollection) SqlClient.queryFirst("queryExistsCus", cus_id, null, connection);
				if(kColl4Cus ==null || kColl4Cus.size()<0){
					//生成客户信息
					if(client_array!=null&&client_array.size()>0){
						for (int i=0;i<client_array.size();i++){

							KeyedCollection kColl4Client = (KeyedCollection) client_array.get(i);
							String client_no = kColl4Client.getDataValue("CLIENT_NO").toString().trim(); //客户号
							String client_name = kColl4Client.getDataValue("CLIENT_NAME").toString().trim();  //客户名称
							String sex = kColl4Client.getDataValue("SEX").toString().trim();//性别
							String client_type = kColl4Client.getDataValue("CLIENT_TYPE").toString().trim();//客户类型
							String global_type = kColl4Client.getDataValue("GLOBAL_TYPE").toString().trim();//证件类型
							String global_id = kColl4Client.getDataValue("GLOBAL_ID").toString().trim();//证件号码
							String long_global_type = kColl4Client.getDataValue("LONG_GLOBAL_TYPE").toString().trim();//长期证件标志
							String iss_date = kColl4Client.getDataValue("ISS_DATE").toString().trim();//签发日期
							
							String global_eff_date = kColl4Client.getDataValue("GLOBAL_EFF_DATE").toString().trim();//证件有效日期
							String agri_flag = kColl4Client.getDataValue("AGRI_FLAG").toString().trim();//农户标志
							String country_code = kColl4Client.getDataValue("COUNTRY_CODE").toString().trim();//国家代码
							String nationality_code = kColl4Client.getDataValue("NATIONALITY_CODE").toString().trim();//民族代码
							String reg_perm_residence = kColl4Client.getDataValue("REG_PERM_RESIDENCE").toString().trim();//户籍所在地
							String address = kColl4Client.getDataValue("ADDRESS").toString().trim();//地址
							String birth_date = TagUtil.formatDate2Ten(kColl4Client.getDataValue("BIRTH_DATE").toString().trim());//出生日期
							String education = kColl4Client.getDataValue("EDUCATION").toString().trim();//最高学历
							String degree = kColl4Client.getDataValue("DEGREE").toString().trim();//最高学位
							String sign_date = kColl4Client.getDataValue("SIGN_DATE").toString().trim();//签约日期
							String hold_card_msg = kColl4Client.getDataValue("HOLD_CARD_MSG").toString().trim();//持卡情况
							String passport_flag = kColl4Client.getDataValue("PASSPORT_FLAG").toString().trim();//是否拥有外国护照或居住权
							
							String credit_level = kColl4Client.getDataValue("CREDIT_LEVEL").toString().trim();//信用等级
							String expiry_date = kColl4Client.getDataValue("EXPIRY_DATE").toString().trim();//信用到期日期
							String rel_client_flag = kColl4Client.getDataValue("REL_CLIENT_FLAG").toString().trim();//是否关联客户
							String own_branch_relation = kColl4Client.getDataValue("OWN_BRANCH_RELATION").toString().trim();//与我行关系
							String post = kColl4Client.getDataValue("POST").toString().trim();//职务
							String loan_card_flag = kColl4Client.getDataValue("LOAN_CARD_FLAG").toString().trim();//贷款卡标志
							String loan_card_no = kColl4Client.getDataValue("LOAN_CARD_NO").toString().trim();//贷款卡号
							String mobile = kColl4Client.getDataValue("MOBILE").toString().trim();//手机号码
							String higher_org_no = kColl4Client.getDataValue("HIGHER_ORG_NO").toString().trim();//上级机构
							String acct_exec = kColl4Client.getDataValue("ACCT_EXEC").toString().trim();//客户经理
							String open_acct_date = kColl4Client.getDataValue("OPEN_ACCT_DATE").toString().trim();//开户日期
							
							//生成客户信息(CUS_BASE,CUS_COM)
							KeyedCollection kColl4CusBase = new KeyedCollection();
							KeyedCollection kColl4CusIndvi = new KeyedCollection();
							
							kColl4CusBase.put("cus_id", client_no);           
							kColl4CusBase.put("cus_name", client_name); 
							kColl4CusBase.put("cus_type", client_type);         
							kColl4CusBase.put("cert_type", global_type);            
							kColl4CusBase.put("cert_code", global_id);           
							kColl4CusBase.put("open_date", open_acct_date);           
							kColl4CusBase.put("cus_country", country_code);           
							kColl4CusBase.put("loan_card_flg", loan_card_flag);         
							kColl4CusBase.put("loan_card_id", loan_card_no);     
						    //kColl4CusBase.put("loan_card_eff_flg",  ); //贷款卡有效标志     
							//kColl4CusBase.put("loan_card_audit_dt",  );//贷款卡最近年审日期      
							kColl4CusBase.put("cus_crd_grade", credit_level);      
							kColl4CusBase.put("cus_crd_dt", expiry_date);            
							kColl4CusBase.put("cus_status", "20");//默认为正式客户    
							kColl4CusBase.put("main_br_id",manager_br_id);//主管机构    
							kColl4CusBase.put("cust_mgr", acct_exec);              
							kColl4CusBase.put("input_id", input_id);        
							kColl4CusBase.put("input_br_id", input_br_id);          
						    kColl4CusBase.put("input_date", input_date);   //生成客户信息当天营业日         
							kColl4CusBase.put("belg_line", "BL300");//微贷平台客户均为个人条线            
							//kColl4CusBase.put("guar_crd_grade", );      
							kColl4CusBase.put("last_update_date", openday);//最近一次更新日期默认为当前营业日期
							kColl4CusBase.setName("CusBase");
							dao.insert(kColl4CusBase, connection);
							
							kColl4CusIndvi.put("cus_id", client_no);
							kColl4CusIndvi.put("indiv_sex", sex);
							kColl4CusIndvi.put("is_long_indiv", long_global_type);
							kColl4CusIndvi.put("indiv_id_start_dt", iss_date);
							kColl4CusIndvi.put("indiv_id_exp_dt", global_eff_date);
							kColl4CusIndvi.put("com_init_loan_date",sign_date);//建立信贷关系时间
							kColl4CusIndvi.put("agri_flg", agri_flag);
							kColl4CusIndvi.put("indiv_ntn", nationality_code);//民族
							kColl4CusIndvi.put("cus_id", client_no);
							kColl4CusIndvi.put("indiv_brt_place", reg_perm_residence);//籍贯
							kColl4CusIndvi.put("street", address);
							kColl4CusIndvi.put("indiv_dt_of_birth", birth_date);
							kColl4CusIndvi.put("indiv_edt", education);
							kColl4CusIndvi.put("indiv_dgr", degree);
							kColl4CusIndvi.put("hold_card", hold_card_msg);
							kColl4CusIndvi.put("passport_flg", passport_flag);
							kColl4CusIndvi.put("is_rela_cust", rel_client_flag);
							kColl4CusIndvi.put("cus_bank_rel", own_branch_relation);	
							kColl4CusIndvi.put("bank_duty", post);
							kColl4CusIndvi.put("mobile", mobile);
							kColl4CusIndvi.setName("CusIndiv");
							dao.insert(kColl4CusIndvi, connection);				
						}							
				   }
			   }
				
				//生成费用信息
				if(fee_array!=null&&fee_array.size()>0){
					for (int i=0;i<fee_array.size();i++){
						KeyedCollection  kColl4Fee = (KeyedCollection) fee_array.get(i);
						String fee_cur_type = kColl4Fee.getDataValue("CCY").toString().trim();
						Double fee_amt = Double.valueOf(kColl4Fee.getDataValue("FEE_AMOUNT").toString().trim());
						String fee_acct_no = kColl4Fee.getDataValue("ACCT_NO").toString().trim();
						String append_terms_pk = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
						KeyedCollection kColl4IAT = new KeyedCollection("IqpAppendTerms");
						kColl4IAT.put("serno", serno);
						kColl4IAT.put("fee_code", "22");
						kColl4IAT.put("fee_type", "116");
						kColl4IAT.put("fee_amt", fee_amt);
						kColl4IAT.put("fee_cur_type", fee_cur_type);
						kColl4IAT.put("fee_rate", "");
						kColl4IAT.put("fee_acct_no", fee_acct_no);
						kColl4IAT.put("is_cycle_chrg", "2");
						kColl4IAT.put("collect_type", "01");
						kColl4IAT.put("append_terms_pk", append_terms_pk);
						dao.insert(kColl4IAT, connection);
					}
				}
				dao.insert(kColl4ILA, connection);
				dao.insert(kColl4ILAS, connection);
				dao.insert(kColl4CLC, connection);
				dao.insert(kColl4CLCS, connection);
				/**授权数据采集---------------start----------------------------------*/
				this.doWfAgreeForSelfLoan(cont_no, context, connection);
				KeyedCollection retKColl2 = null;
				retKColl2 = this.trade0200100000101(cont_no, context, connection);	//调用接口
				
				if(!TagUtil.haveSuccess(retKColl2, context)){
					throw new Exception("【自助贷款协议授权】,交易失败！");
				}else{
					EMPLog.log("Trade0200200001001", EMPLog.INFO, 0, "【个人自助贷款协议发放】交易处理完成...", null);
				}
				//added by yangzy 2015/08/10 微贷平台零售自助贷款改造 start
				retKColl.put("ret_msg", cont_no);
				//added by yangzy 2015/08/10 微贷平台零售自助贷款改造 end
			}else if("existCont".equals(flag)){
				throw new Exception("存在有效的自助协议！");
			}else{
				throw new Exception("报文体不存在客户号，生成业务信息异常！");
			}
			//added by yangzy 2015/05/25 微贷平台零售自助贷款改造 end
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			retKColl.setDataValue("ret_code", "9999");
			retKColl.setDataValue("ret_msg", e.getMessage());
		}
		return retKColl;
	}
	
	/**
	 * 自助贷款协议授权信息采集
	 * @param contNo 合同编号
	 * @throws Exception
	 * added by yangzy 2015/04/07 需求编号:XD150318023,微贷平台零售自助贷款改造
	 */
	public void doWfAgreeForSelfLoan(String contNo,Context context, Connection connection)throws Exception {
		try {
			String IQPLOANAPPMODEL = "IqpLoanApp";//贷款申请
			String CTRCONTSUBMODEL = "CtrLoanContSub";//合同从表
			String AUTHORIZEMODEL = "PvpAuthorize";//出账授权
			String AUTHORIZESUBMODEL = "PvpAuthorizeSub";//授权信息从表
			String PUBBAILINFO= "PubBailInfo";//保证金信息表
			String IQPCUSACCT= "IqpCusAcct";//账户信息表
			String IQPFEE= "IqpAppendTerms";//账户信息表
			String PrdRepayMode= "PrdRepayMode";//还款方式
			
			if(contNo == null || contNo == ""){
				throw new Exception("获取业务合同编号失败！");
			}
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			/** 1.数据准备：通过业务流水号查询【合同】 */
			KeyedCollection ctrLoanKColl = dao.queryDetail("CtrLoanCont", contNo, connection);
			String Iqpserno = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("serno"));//授权交易流水号
			String prd_id = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("prd_id"));//产品编号
			String cus_id = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cus_id"));//客户编码
			String cont_no = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cont_no"));//合同编号
			BigDecimal cont_amt = BigDecimalUtil.replaceNull(ctrLoanKColl.getDataValue("cont_amt"));//协议金额
			String manager_br_id = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("manager_br_id"));//管理机构
			/**modified by lisj 2015-7-1 需求编号：XD150123005 小微自助循环贷款改造 begin**/
			String in_acct_br_id = "";//入账机构
			if("100088".equals(prd_id)){
				KeyedCollection pvpLoanKColl = dao.queryFirst("PvpLoanApp", null, "where cont_no ='"+contNo+"'", connection);
				in_acct_br_id = TagUtil.replaceNull4String(pvpLoanKColl.getDataValue("in_acct_br_id"));
			}else{
				in_acct_br_id = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("in_acct_br_id"));
			}
			/**modified by lisj 2015-7-1 需求编号：XD150123005 小微自助循环贷款改造 end**/
			String cur_type = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cont_cur_type"));//币种
			String date = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			//String input_id = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("input_id"));//登记人
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 begin */	
			if(!"100051".equals(prd_id)){
				String input_br_id = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("input_br_id"));//登记机构
				String openDay = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
				context.put("OPENDAY",openDay);
				context.put("currentUserId","admin");
				context.put("currentUserName","超级管理员");
				context.put("organNo",input_br_id);
			}
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 end */	
			/** 核算与信贷业务品种映射 START */
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			String lmPrdId = service.getPrdBasicCLPM2LM(cont_no, prd_id, context, connection);
			/** 核算与信贷业务品种映射 END */
			
			/** 自助业务申请类型 */
			String apply_type = "";
			if("100085".equals(prd_id) || "100084".equals(prd_id)){
				apply_type = "01";//个人自助
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 begin */	
			}else if("100051".equals(prd_id)){
				apply_type = "03";//法人透支
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 end */
			/*added by lisj 2015-6-30   需求编号：【XD150123005】小微自助循环贷款改造 begin */
			}else if("100088".equals(prd_id)){
				apply_type = "02";//小微自助
			}
			/*added by lisj 2015-6-30   需求编号：【XD150123005】小微自助循环贷款改造 end */
			/** 2.数据准备：通过业务流水号查询【业务申请】【合同信息】 */					
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, cont_no, connection);
			/* 需求编号：XD150825064_源泉宝法人账户透支改造  begin */
			if("100051".equals(prd_id)){
				String belgLine = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("belg_line"));
				if("BL300".equals(belgLine)){//零售
					apply_type = "04";//法人透支2
				}
			}
			/* 需求编号：XD150825064_源泉宝法人账户透支改造  end */
			String loanSerno = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("serno"));//业务申请流水号
			String ruling_ir = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir"));//基准利率（年）
			String ir_float_rate = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_rate"));//浮动比例
			String ir_float_point = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_point"));//浮动点数
			String reality_ir_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("reality_ir_y"));//执行利率（年）
			String cont_start_date = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cont_start_date"));//合同起始日期
			String cont_end_date = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cont_end_date"));//合同到期日期
			String repay_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_type"));//还款方式
			Double security_rate = TagUtil.replaceNull4Double(ctrLoanKColl.getDataValue("security_rate"));//保证金比例
			String repay_mode_type = "";//还款方式种类
			if(repay_type!=null && !"".equals(repay_type)){
				KeyedCollection prdRepayModeKColl = dao.queryDetail(PrdRepayMode, repay_type, connection);
				repay_mode_type = TagUtil.replaceNull4String(prdRepayModeKColl.getDataValue("repay_mode_type"));
			}   
			String card_no = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("cus_card_code"));//卡号
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 begin */
			if("100051".equals(prd_id) || "100088".equals(prd_id)){
				card_no="";
			}
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 end */
			//String is_collect_stamp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("is_collect_stamp"));//是否收取印花税
			//String stamp_collect_mode = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("stamp_collect_mode"));//印花税收取方式
			String pay_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("pay_type"));//支付方式
			//信贷支付方式为：0：自主支付 1：受托支付 2：跨境受托支付。核算支付方式为：1：自主支付 2：受托支付 3：跨境受托支付
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 begin */
			//if(!"100051".equals(prd_id)){
				pay_type= "1";
			//}
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 end */
			//获取委托贷款关联信息
			BigDecimal contAmt = new BigDecimal(TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cont_amt")));//合同金额
			String cont_cur_type = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cont_cur_type"));//合同币种
			Integer cont_term = TagUtil.replaceNull4Int(ctrContSubKColl.getDataValue("cont_term"));//合同期限
			String term_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("term_type"));//期限类型
			/*modified by lisj 2015-6-30   需求编号：【XD150123005】小微自助循环贷款改造 begin */
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 begin */
			if(("100051".equals(prd_id)) || ("100088".equals(prd_id))){
				 cont_term = TagUtil.replaceNull4Int(ctrContSubKColl.getDataValue("overdrawn_term"));//透支期限
				 term_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("overdrawn_type"));//透支类型
			}
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 end */
			/*mofified by lisj 2015-6-30   需求编号：【XD150123005】小微自助循环贷款改造 end */
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
			
			//获取贷款申请相关信息
			KeyedCollection iqpLoanAppKColl =  dao.queryDetail(IQPLOANAPPMODEL, loanSerno, connection);
			String apply_date = TagUtil.replaceNull4String(iqpLoanAppKColl.getDataValue("apply_date"));//业务申请日期

			//通过客户编号查询【客户信息】
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
			CusBase cusBase = csi.getCusBaseByCusId(cus_id,context,connection);
			String cus_name = TagUtil.replaceNull4String(cusBase.getCusName());//客户名称
			String cert_type = TagUtil.replaceNull4String(cusBase.getCertType());//证件类型
			String cert_code = TagUtil.replaceNull4String(cusBase.getCertCode());//证件号码
			
			//生成授权主表信息
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connection, context);//生成交易流水号
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connection, context);//生成授权编号
			authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
			authorizeKColl.addDataField("serno", Iqpserno);//业务流水号
			authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
			authorizeKColl.addDataField("prd_id", prd_id);//产品编号
			authorizeKColl.addDataField("cus_id", cus_id);//客户编码
			authorizeKColl.addDataField("cus_name", cus_name);//客户名称
			authorizeKColl.addDataField("cont_no", cont_no);//合同编号
			authorizeKColl.addDataField("bill_no", "");//借据编号
			authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_DKFFSQ + TradeConstance.SERVICE_SCENE_ZZDKFFSQ);
			authorizeKColl.addDataField("tran_amt", cont_amt);//交易金额
			authorizeKColl.addDataField("tran_date", date);//交易日期
			authorizeKColl.addDataField("send_times", "0");//发送次数
			authorizeKColl.addDataField("return_code", "");//返回编码
			authorizeKColl.addDataField("return_desc", "");//返回信息
			authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
			//modified by yangzy 2015/06/11  个人微贷自助业务授权状态改为【授权已确认】 start
			authorizeKColl.addDataField("status", "04");//状态
			//modified by yangzy 2015/06/11  个人微贷自助业务授权状态改为【授权已确认】 end
			authorizeKColl.addDataField("cur_type", cur_type);//币种
			
			//费用信息
			String conditionFee = "where serno='"+loanSerno+"'";
			IndexedCollection iqpAppendTermsIColl = dao.queryList(IQPFEE, conditionFee, connection);
			
			//计算手续费率  start
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 begin */
/*			BigDecimal chrg_rate = new BigDecimal("0.00");
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
			}*/
			/*added by wangj 2015/06/08  需求编号:XD141222087,法人账户透支需求变更 end */
			//计算手续费率  end
			
			/*其他普通贷款：授权信息组装*/

			authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编码
			authorizeKColl.addDataField("fldvalue02", "APPLY_TYPE@" + apply_type);//申请方式	
			authorizeKColl.addDataField("fldvalue03", "APPLY_NO@" + Iqpserno);//业务流水号
			authorizeKColl.addDataField("fldvalue04", "APPLY_DATE@" + TagUtil.formatDate(apply_date));//业务申请日期
			authorizeKColl.addDataField("fldvalue05", "CONTRACT_NO@" + cont_no);//合同编号
			authorizeKColl.addDataField("fldvalue06", "BRANCH_ID@" + in_acct_br_id);//入账机构
			authorizeKColl.addDataField("fldvalue07", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
			authorizeKColl.addDataField("fldvalue08", "CLIENT_NO@" + cus_id);//客户码
			authorizeKColl.addDataField("fldvalue09", "CLIENT_NAME@" + cus_name);//客户名称
			authorizeKColl.addDataField("fldvalue10", "GLOBAL_TYPE@" + cert_type);//证件类型（非必输）
			authorizeKColl.addDataField("fldvalue11", "GLOBAL_ID@" + cert_code);//证件号码（非必输）
			authorizeKColl.addDataField("fldvalue12", "ISS_CTRY@" + "CN");//发证国家
			authorizeKColl.addDataField("fldvalue13", "DEALER_CDE@" + "");//经销商代码（非必输,不填）
			authorizeKColl.addDataField("fldvalue14", "CCY@" + cont_cur_type);//币种
			authorizeKColl.addDataField("fldvalue15", "APPLY_AMOUNT@" + contAmt);//申请金额
			authorizeKColl.addDataField("fldvalue16", "DRAW_DOWN_DATE@" + TagUtil.formatDate(cont_start_date));//合同发放日期
			authorizeKColl.addDataField("fldvalue17", "LOAN_TYPE@" + lmPrdId );//贷款品种
			authorizeKColl.addDataField("fldvalue18", "CONTRACT_EXPIRY_DATE@" + TagUtil.formatDate(cont_end_date));//合同到期日
			authorizeKColl.addDataField("fldvalue19","CARD_NO@" + card_no);	//卡号
			authorizeKColl.addDataField("fldvalue20","TERM@" + cont_term);	//期限
			authorizeKColl.addDataField("fldvalue21","TERM_TYPE@" + term_type);	//期限类型
			authorizeKColl.addDataField("fldvalue22", "DEDUCT_DATE@" + repay_date);//扣款日
			
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
			authorizeKColl.addDataField("fldvalue23", "INT_RATE_MODE@" + ir_accord_type);//利率模式
			authorizeKColl.addDataField("fldvalue24", "INT_RATE_BASE@" + "Y");//利率基础
			authorizeKColl.addDataField("fldvalue25", "BASE_INT_RATE_CODE@" + ruling_ir_code);//利率类型=基准利率代码 
			authorizeKColl.addDataField("fldvalue26", "BASE_INT_RATE@" + ruling_ir);//基准利率
			authorizeKColl.addDataField("fldvalue27", "INT_FLT_RATE@" + ir_float_rate);//利率浮动比例
			authorizeKColl.addDataField("fldvalue28", "SPREAD@" + ir_float_point);//利差
			authorizeKColl.addDataField("fldvalue29", "ACT_INT_RATE@" + reality_ir_y);//执行利率
			authorizeKColl.addDataField("fldvalue30", "OD_RATE_BASE@"+"Y");//罚息利率基础
			authorizeKColl.addDataField("fldvalue31", "LOAN_OD_RATE_CODE@"+"");//罚息利率代码
			authorizeKColl.addDataField("fldvalue32", "LOAN_OD_BASE_RATE@"+"");//基准罚息利率
			authorizeKColl.addDataField("fldvalue33", "LOAN_OD_BASE_FLT_RATE@" + "");//罚息基准利率浮动比
			authorizeKColl.addDataField("fldvalue34", "LOAN_OD_ACT_RATE@" + overdue_rate_y);//罚息执行利率
			authorizeKColl.addDataField("fldvalue35", "REPAY_FREQUENCY_UNIT@" + repay_term);//还款间隔单位
			authorizeKColl.addDataField("fldvalue36", "REPAY_FREQUENCY@" + repay_space);//还款间隔
			authorizeKColl.addDataField("fldvalue37", "LOAN_REPAY_METHOD@" + repay_type);//还款方式
			authorizeKColl.addDataField("fldvalue38", "LOAN_REPAY_TYPE@" + repay_mode_type);//还款方式类型
			String buss_source = "";
			String CONSIGN_AGREE = "";
			buss_source = "NLOAN";
			authorizeKColl.addDataField("fldvalue39", "BUSS_SOURCE@" + buss_source);//业务数据来源
			authorizeKColl.addDataField("fldvalue40", "LOAN_GRACE_TYPE@" + "P");//宽限期类型
			authorizeKColl.addDataField("fldvalue41", "LOAN_GRACE_DAYS@" + "0");//宽限期天数
			authorizeKColl.addDataField("fldvalue42", "DEDUCT_METHOD@" + "AUTOPAY");//扣款方式
			authorizeKColl.addDataField("fldvalue43", "FIX_OD_RATE_FLAG@" + "N");//是否采用固定罚息利率
			authorizeKColl.addDataField("fldvalue44", "LOAN_OD_RATE_TYPE@" + "L");//罚息利率种类
			authorizeKColl.addDataField("fldvalue45", "LOAN_OD_ACT_FLT_RATE@" + default_rate);//罚息执行利率浮动比
			authorizeKColl.addDataField("fldvalue46", "LOAN_OD_COMM_PART@" + "");//计算罚息部分
			authorizeKColl.addDataField("fldvalue47", "LOAN_OD_CPD_FLAG@" + "");//是否计算罚息复利
			authorizeKColl.addDataField("fldvalue48", "NEXT_RADJ_OPT@" + ir_adjust_type);//下一次利率调整选项
			authorizeKColl.addDataField("fldvalue49", "NEXT_RADJ_FREQ@" + ir_next_adjust_term);//下一次利率调整间隔
			authorizeKColl.addDataField("fldvalue50", "NEXT_RADJ_FREQ_UNIT@" + ir_next_adjust_unit);//下一次利率调整间隔单位
			authorizeKColl.addDataField("fldvalue51", "FIRST_ADJUST_DATE@" + fir_adjust_day);//第一次调整日
			authorizeKColl.addDataField("fldvalue52", "DIVERT_FLT_RATE@" + "");//挪用利率浮动比例
			authorizeKColl.addDataField("fldvalue53", "OVERDUE_ACT_RATE@" + overdue_rate_y);//逾期执行利率
			authorizeKColl.addDataField("fldvalue54", "OVERDUE_FLT_RATE@" + overdue_rate);//逾期利率浮动比
			authorizeKColl.addDataField("fldvalue55", "ASSET_BUY_FLAG@" + "N");//是否资产买入
			authorizeKColl.addDataField("fldvalue56", "CONSIGN_AGREE@" + CONSIGN_AGREE);//委托协议
			authorizeKColl.addDataField("fldvalue57", "TERM_PAY_FLAG@" + is_term);//期供标志
			authorizeKColl.addDataField("fldvalue58", "LOAN_PROMISE_DUEBILL_NO@" + "");//贷款承诺借据号
			authorizeKColl.addDataField("fldvalue59", "PAY_METHOD@" + pay_type);	//支付类型
			/*modified by wangj   需求编号：【XD150123005】小微自助循环贷款改造 begin */
			authorizeKColl.addDataField("fldvalue60", "CONTRACT_TERM@" + TagUtil.replaceNull4Int(ctrContSubKColl.getDataValue("cont_term")));//合同期限
			authorizeKColl.addDataField("fldvalue61", "CONTRACT_TERM_TYPE@" + TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("term_type")));	//合同期限类型
			/*modified by wangj  需求编号：【XD150123005】小微自助循环贷款改造 end */
			
			//生成授权信息：保证金信息
			//查询保证金信息表获取保证金相关信息
			String condition = "where serno='"+loanSerno+"'";
			IndexedCollection iqpBailInfoIColl = dao.queryList(PUBBAILINFO, condition, connection);
			for(int i=0;i<iqpBailInfoIColl.size();i++){
				KeyedCollection iqpBailInfoKColl = (KeyedCollection)iqpBailInfoIColl.get(i);
				String bail_acct_no = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_no"));//保证金账号
				String bail_acct_name = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_name"));//保证金账号名称
				String open_org = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("open_org"));//开户机构
				String bail_acct_gl_code = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_gl_code"));//科目号
				String ccy = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("cur_type"));//币种
				String interbank_id = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("interbank_id"));//银联行号
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);

				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "CONTRACT_NO@" + cont_no);//合同号
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
				authorizeSubKColl.addDataField("fldvalue21", "OWN_BRANCH_FLAG@" + "1");//是否本行
				
				//开户行地址
				if(interbank_id!=null&&!"".equals(interbank_id)){
					KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, connection);
					if(kColl!=null){
						authorizeSubKColl.addDataField("fldvalue22", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
					}else{
						authorizeSubKColl.addDataField("fldvalue22", "ACCT_BANK_ADD@" + "");//地址
					}
				}else{
					authorizeSubKColl.addDataField("fldvalue22", "ACCT_BANK_ADD@" + "");//地址
				}
				authorizeSubKColl.addDataField("fldvalue23", "GUARANTEE_PER@" + security_rate);//保证金比例
			
				dao.insert(authorizeSubKColl, connection);
			}
			
			//生成授权信息：费用信息
			Map<String, String> feemap = new HashMap<String, String>();//定义一个账号对应map
			int feecount = 1;

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
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "CONTRACT_NO@" + cont_no);//合同号
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
				authorizeSubKColl.addDataField("fldvalue21", "BRANCH_ID@" + in_acct_br_id);//入账机构
				authorizeSubKColl.addDataField("fldvalue22", "FEE_SPAN@" + chrg_freq);//收费间隔
				dao.insert(authorizeSubKColl, connection);
			}			
			
			//生成授权信息：结算账户信息
			String conditionCusAcct = "where serno='"+loanSerno+"'";
			IndexedCollection iqpCusAcctIColl = dao.queryList(IQPCUSACCT, conditionCusAcct, connection);
			int eactcount = 0;
			for(int i=0;i<iqpCusAcctIColl.size();i++){				
				KeyedCollection iqpCusAcctKColl = (KeyedCollection)iqpCusAcctIColl.get(i);
				String acct_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_no"));//账号
				String opac_org_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opac_org_no"));//开户行行号
				String opan_org_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opan_org_name"));//开户行行名
				//String is_this_org_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行账户
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
				authorizeSubKColl.addDataField("fldvalue02", "CONTRACT_NO@" + cont_no);//合同号
				
				/*PAYM:还款账户 ACTV:放款账号 TURE:委托账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号*/
				if(!"".equals(acct_attr) && acct_attr.equals("01")){
					acct_attr = "ACTV";
					/**add by lisj 2015-6-30   需求编号：【XD150123005】小微自助循环贷款改造 begin **/
					//小微自助循环贷款新增放款账号为贷款账号，用于核心放款出账校验
					authorizeKColl.setDataValue("fldvalue19", "CARD_NO@" + acct_no);
					/**add by lisj 2015-6-30   需求编号：【XD150123005】小微自助循环贷款改造 end **/
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
				authorizeSubKColl.addDataField("fldvalue21", "OWN_BRANCH_FLAG@" + is_acct);//是否本行
				
				//开户行地址
				if(interbank_id!=null&&!"".equals(interbank_id)){
					KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, connection);
					if(kColl!=null){
						authorizeSubKColl.addDataField("fldvalue22", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
					}else{
						authorizeSubKColl.addDataField("fldvalue22", "ACCT_BANK_ADD@" + "");//地址
					}
				}else{
					authorizeSubKColl.addDataField("fldvalue22", "ACCT_BANK_ADD@" + "");//地址
				}
				dao.insert(authorizeSubKColl, connection);
			}
			dao.insert(authorizeKColl, connection);
			/**调用ESB接口，发送报文*/
			//CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			//ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			//serviceRel.trade0200100000101(contNo, context, connection);
		
		}catch (Exception e) {
			throw new Exception("自助贷款协议授权信息采集异常"+e.getMessage());
		}
	}
	
	/**
	 * 特殊字典翻译
	 * @param opttype 字典项，enname
	 * @throws Exception
	 * added by yangzy 2015/04/07 需求编号:XD150318023,微贷平台零售自助贷款改造
	 */	
	private String TransSDicForESB(String opttype,String enname)throws Exception{
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
	 * 自助贷款协议授权
	 * @param  合同流水号 contNo，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 * added by yangzy 2015/04/08 需求编号:XD150318023,微贷平台零售自助贷款改造
	 */
	public KeyedCollection trade0200100000101(String contNo,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		try{
			//获取dao
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection PvpKColl = dao.queryFirst("PvpAuthorize",null,"where cont_no='"+contNo+"'",connection);
			IndexedCollection temp  =dao.queryList("PvpAuthorize","where cont_no='"+contNo+"'",connection);
			String tran_serno = (String) PvpKColl.getDataValue("tran_serno");
			String tran_id = (String) PvpKColl.getDataValue("tran_id");
			String serviceCode = tran_id.substring(0, 11);
			String senceCode = tran_id.substring(11, 13);
			String openDay = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			context.put("OPENDAY",openDay);
			//需求编号：【XD141222087】法人账户透支需求变更   需求编号:XD150825064_源泉宝法人账户透支改造 需求编号：【XD150123005】小微自助循环贷款改造  begin
			String prd_id=TagUtil.replaceNull4String(PvpKColl.getDataValue("prd_id"));
			if(!("100051".equals(prd_id)||"100088".equals(prd_id))){
				context.put("currentUserId","admin");
				context.put("currentUserName","超级管理员");
				context.put("organNo","9350000000");
			}
			//需求编号：【XD141222087】法人账户透支需求变更  需求编号：【XD150123005】小微自助循环贷款改造 end
			
			/** 通过交易码判断所需执行的交易，以及需要准备的交易数据 */
			KeyedCollection authKColl = dao.queryDetail("PvpAuthorize", tran_serno, connection);
			String serno = (String)authKColl.getDataValue("serno");
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
			
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("APPLY_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("APPLY_TYPE"), FieldType.FIELD_STRING, 2, 0));
			bodyCD.addField("APPLY_NO", TagUtil.getEMPField(reflectKColl.getDataValue("APPLY_NO"), FieldType.FIELD_STRING, 32, 0));
			bodyCD.addField("APPLY_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("APPLY_DATE")), FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("CONTRACT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("BANK_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BANK_ID"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("CLIENT_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NAME"), FieldType.FIELD_STRING, 150, 0));
			bodyCD.addField("GLOBAL_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("GLOBAL_TYPE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("GLOBAL_ID", TagUtil.getEMPField(reflectKColl.getDataValue("GLOBAL_ID"), FieldType.FIELD_STRING, 40, 0));
			bodyCD.addField("ISS_CTRY", TagUtil.getEMPField(reflectKColl.getDataValue("ISS_CTRY"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("DEALER_CDE", TagUtil.getEMPField(reflectKColl.getDataValue("DEALER_CDE"), FieldType.FIELD_STRING, 20, 0));
			bodyCD.addField("CCY", TagUtil.getEMPField(reflectKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("APPLY_AMOUNT", TagUtil.getEMPField(reflectKColl.getDataValue("APPLY_AMOUNT"), FieldType.FIELD_DOUBLE, 16, 2));
			bodyCD.addField("DRAW_DOWN_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("DRAW_DOWN_DATE")), FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("LOAN_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_TYPE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("CONTRACT_EXPIRY_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("CARD_NO", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("CARD_NO")), FieldType.FIELD_STRING, 50, 0));
			bodyCD.addField("TERM", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("TERM")), FieldType.FIELD_STRING, 3, 0));
			bodyCD.addField("TERM_TYPE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("TERM_TYPE")), FieldType.FIELD_STRING, 3, 0));
			bodyCD.addField("DEDUCT_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("DEDUCT_DATE"), FieldType.FIELD_STRING, 2, 0));
			bodyCD.addField("INT_RATE_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("INT_RATE_MODE"), FieldType.FIELD_STRING, 2, 0));
			bodyCD.addField("INT_RATE_BASE", TagUtil.getEMPField(reflectKColl.getDataValue("INT_RATE_BASE"), FieldType.FIELD_STRING, 1, 0));
			bodyCD.addField("BASE_INT_RATE_CODE", TagUtil.getEMPField(reflectKColl.getDataValue("BASE_INT_RATE_CODE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("BASE_INT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("BASE_INT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
			bodyCD.addField("INT_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("INT_FLT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
			bodyCD.addField("SPREAD", TagUtil.getEMPField(reflectKColl.getDataValue("SPREAD"), FieldType.FIELD_DOUBLE, 16, 9));
			bodyCD.addField("ACT_INT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("ACT_INT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
			bodyCD.addField("OD_RATE_BASE", TagUtil.getEMPField(reflectKColl.getDataValue("OD_RATE_BASE"), FieldType.FIELD_STRING, 1, 0));
			bodyCD.addField("LOAN_OD_RATE_CODE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_RATE_CODE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("LOAN_OD_BASE_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_BASE_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
			bodyCD.addField("LOAN_OD_BASE_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_BASE_FLT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
			bodyCD.addField("LOAN_OD_ACT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_ACT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
			bodyCD.addField("REPAY_FREQUENCY_UNIT", TagUtil.getEMPField(reflectKColl.getDataValue("REPAY_FREQUENCY_UNIT"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("REPAY_FREQUENCY", TagUtil.getEMPField(reflectKColl.getDataValue("REPAY_FREQUENCY"), FieldType.FIELD_STRING, 2, 0));
			bodyCD.addField("LOAN_REPAY_METHOD", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_REPAY_METHOD"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("LOAN_REPAY_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_REPAY_TYPE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("BUSS_SOURCE", TagUtil.getEMPField(reflectKColl.getDataValue("BUSS_SOURCE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("LOAN_GRACE_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_GRACE_TYPE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("LOAN_GRACE_DAYS", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_GRACE_DAYS"), FieldType.FIELD_DOUBLE, 3, 0));
			bodyCD.addField("DEDUCT_METHOD", TagUtil.getEMPField(reflectKColl.getDataValue("DEDUCT_METHOD"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("FIX_OD_RATE_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("FIX_OD_RATE_FLAG"), FieldType.FIELD_STRING, 1, 0));
			bodyCD.addField("LOAN_OD_RATE_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_RATE_TYPE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("LOAN_OD_ACT_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_ACT_FLT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
			bodyCD.addField("LOAN_OD_COMM_PART", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_COMM_PART"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("LOAN_OD_CPD_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_CPD_FLAG"), FieldType.FIELD_STRING, 1, 0));
			bodyCD.addField("NEXT_RADJ_OPT", TagUtil.getEMPField(reflectKColl.getDataValue("NEXT_RADJ_OPT"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("NEXT_RADJ_FREQ", TagUtil.getEMPField(reflectKColl.getDataValue("NEXT_RADJ_FREQ"), FieldType.FIELD_DOUBLE, 2, 0));
			bodyCD.addField("NEXT_RADJ_FREQ_UNIT", TagUtil.getEMPField(reflectKColl.getDataValue("NEXT_RADJ_FREQ_UNIT"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("FIRST_ADJUST_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("FIRST_ADJUST_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("DIVERT_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("DIVERT_FLT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
			bodyCD.addField("OVERDUE_ACT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("OVERDUE_ACT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
			bodyCD.addField("OVERDUE_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("OVERDUE_FLT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
			bodyCD.addField("ASSET_BUY_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("ASSET_BUY_FLAG"), FieldType.FIELD_STRING, 1, 0));
			bodyCD.addField("CONSIGN_AGREE", TagUtil.getEMPField(reflectKColl.getDataValue("CONSIGN_AGREE"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("TERM_PAY_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("TERM_PAY_FLAG"), FieldType.FIELD_STRING, 1, 0));
			bodyCD.addField("LOAN_PROMISE_DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_PROMISE_DUEBILL_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("PAY_METHOD", TagUtil.getEMPField(reflectKColl.getDataValue("PAY_METHOD"), FieldType.FIELD_STRING, 60, 0));
			/*modified by wangj   需求编号：【XD150123005】小微自助循环贷款改造 begin */
			bodyCD.addField("CONTRACT_TERM", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_TERM"), FieldType.FIELD_STRING, 3, 0));//合同期限
			bodyCD.addField("CONTRACT_TERM_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_TERM_TYPE"), FieldType.FIELD_STRING, 10, 0));//合同期限类型
			/*modified by wangj  需求编号：【XD150123005】小微自助循环贷款改造 end */

			reqCD.addStruct("BODY", bodyCD);
			
			/** 账号信息 */
			IndexedCollection zhIColl = dao.queryList("PvpAuthorizeSub", " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_ZH+"' ", connection);
			if(zhIColl != null && zhIColl.size() > 0){
				Array zharray = new Array(); 
				for(int i=0;i<zhIColl.size();i++){
					CompositeData zhCD = new CompositeData();
					KeyedCollection zhKColl = (KeyedCollection)zhIColl.get(i);
					KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(zhKColl);
					zhCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
					zhCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 50, 0));
					zhCD.addField("AGREE_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 50, 0));
					zhCD.addField("LOAN_ACCT_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("LOAN_ACCT_TYPE"), FieldType.FIELD_STRING, 6, 0));
					zhCD.addField("ACCT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_NO"), FieldType.FIELD_STRING, 50, 0));
					zhCD.addField("CCY", TagUtil.getEMPField(reflectSubKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 3, 0));
					zhCD.addField("BANK_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("BANK_ID"), FieldType.FIELD_STRING, 30, 0));
					zhCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
					zhCD.addField("ACCT_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_NAME"), FieldType.FIELD_STRING, 150, 0));
					zhCD.addField("ISS_CTRY", TagUtil.getEMPField(reflectSubKColl.getDataValue("ISS_CTRY"), FieldType.FIELD_STRING, 10, 0));
					zhCD.addField("ACCT_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_TYPE"), FieldType.FIELD_STRING, 6, 0));
					zhCD.addField("GLOBAL_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("GLOBAL_TYPE"), FieldType.FIELD_STRING, 5, 0));
					zhCD.addField("GLOBAL_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("GLOBAL_ID"), FieldType.FIELD_STRING, 30, 0));
					zhCD.addField("REMARK", TagUtil.getEMPField(reflectSubKColl.getDataValue("REMARK"), FieldType.FIELD_STRING, 300, 0));
					zhCD.addField("CARD_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CARD_NO"), FieldType.FIELD_STRING, 20, 0));
					zhCD.addField("CA_TT_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("CA_TT_FLAG"), FieldType.FIELD_STRING, 2, 0));
					zhCD.addField("ACCT_CODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_CODE"), FieldType.FIELD_STRING, 10, 0));
					zhCD.addField("ACCT_GL_CODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_GL_CODE"), FieldType.FIELD_STRING, 10, 0));
					zhCD.addField("BALANCE", TagUtil.getEMPField(reflectSubKColl.getDataValue("BALANCE"), FieldType.FIELD_DOUBLE, 20, 2));
					zhCD.addField("COMMISSION_PAYMENT_AMOUNT", TagUtil.getEMPField(reflectSubKColl.getDataValue("COMMISSION_PAYMENT_AMOUNT"), FieldType.FIELD_DOUBLE, 20, 2));
					zhCD.addField("BANK_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("BANK_NAME"), FieldType.FIELD_STRING, 100, 0));
					zhCD.addField("OWN_BRANCH_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("OWN_BRANCH_FLAG"), FieldType.FIELD_STRING, 1, 0));
					zhCD.addField("ACCT_BANK_ADD", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_BANK_ADD"), FieldType.FIELD_STRING, 100, 0));
					if(reflectSubKColl.containsKey("GUARANTEE_PER")){
						zhCD.addField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_PER"), FieldType.FIELD_DOUBLE, 20, 7));
					}
					
					zharray.addStruct(zhCD);
				}
				bodyCD.addArray("ACCT_INFO_ARRAY", zharray);
			}
			
			/** 费用信息 */
			IndexedCollection feeIColl = dao.queryList("PvpAuthorizeSub", " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_FY+"' ", connection);
			if(feeIColl != null && feeIColl.size() > 0){
				Array feeArray = new Array(); 
				for(int i=0;i<feeIColl.size();i++){
					CompositeData feeCD = new CompositeData();
					KeyedCollection feeKColl = (KeyedCollection)feeIColl.get(i);
					KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(feeKColl);
					feeCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
					feeCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 50, 0));
					feeCD.addField("CONTRACT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 50, 0));
					feeCD.addField("FEE_CODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("FEE_CODE"), FieldType.FIELD_STRING, 10, 0));
					feeCD.addField("CCY", TagUtil.getEMPField(reflectSubKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 3, 0));
					feeCD.addField("FEE_AMOUNT", TagUtil.getEMPField(reflectSubKColl.getDataValue("FEE_AMOUNT"), FieldType.FIELD_DOUBLE, 20, 2));
					feeCD.addField("INOUT_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("INOUT_FLAG"), FieldType.FIELD_STRING, 1, 0));
					feeCD.addField("FEE_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("FEE_TYPE"), FieldType.FIELD_STRING, 10, 0));
					feeCD.addField("BASE_AMOUNT", TagUtil.getEMPField(reflectSubKColl.getDataValue("BASE_AMOUNT"), FieldType.FIELD_DOUBLE, 20, 2));
					feeCD.addField("FEE_RATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("FEE_RATE"), FieldType.FIELD_DOUBLE, 20, 7));
					feeCD.addField("DEDUCT_ACCT_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("DEDUCT_ACCT_TYPE"), FieldType.FIELD_STRING, 150, 0));
					feeCD.addField("CHARGE_PERIODICITY_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("CHARGE_PERIODICITY_FLAG"), FieldType.FIELD_STRING, 1, 0));
					feeCD.addField("CHARGE_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("CHARGE_DATE"), FieldType.FIELD_STRING, 8, 0));
					feeCD.addField("AMOR_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("AMOR_AMT"), FieldType.FIELD_DOUBLE, 20, 2));
					feeCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
					feeCD.addField("FEE_SPAN", TagUtil.getEMPField(reflectSubKColl.getDataValue("FEE_SPAN"), FieldType.FIELD_STRING, 10, 0));
					feeArray.addStruct(feeCD);
				}
				bodyCD.addArray("FEE_AUTH_INFO_ARRAY", feeArray);
			}			
			
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
		/*added by wangj 2015/09/24  需求编号:XD141222087,法人账户透支需求变更 begin */	
//			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
//				throw new Exception("自助贷款协议授权发送失败!");
//			}
			/*added by wangj 2015/09/24  需求编号:XD141222087,法人账户透支需求变更 end */
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("自助贷款协议授权发送失败"+e.getMessage());
		}
	}
	/**
	 * 解析返回结构体的系统报文头
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getRespSysHeadCD(CompositeData respCD) throws Exception {
		if(respCD == null){
			return null;
		}
		CompositeData respSysHeadCD = respCD.getStruct(TradeConstance.ESB_SYS_HEAD);
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("RET_STATUS", respSysHeadCD.getField("RET_STATUS").strValue());
		kColl.addDataField("RET_CODE",respSysHeadCD.getArray("RET").getStruct(0).getField("RET_CODE").strValue());
		kColl.addDataField("RET_MSG",respSysHeadCD.getArray("RET").getStruct(0).getField("RET_MSG").strValue());
		kColl.setName("SYS_HEAD");
		return kColl;
	}
}
