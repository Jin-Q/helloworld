package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

/**
 * 
*@author fw
*@time 2015-4-7
*@description TODO 需求编号：【XD150318023】个人微贷额度发放交易
*				         
*@version v1.0
*
 */
public class Trade0200200000501 extends TranService{
	public KeyedCollection doExecute(CompositeData CD,Connection connection) throws Exception{
		EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
		Context context = factory.getContextNamed(factory.getRootContextName());
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection retKColl=TagUtil.getDefaultResultKColl();
		String agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
		String limit_code = CMISSequenceService4JXXD.querySequenceFromED("ED", "all", connection, context);
		String openday  = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
		try{
			ESBInterface esbInterface=new ESBInterfacesImple();
			KeyedCollection reqBody=esbInterface.getReqBody(CD);
			IndexedCollection client_array = (IndexedCollection) reqBody.getDataElement("CLIENT_ARRAY");//客户信息数组
			String client_no=(String)reqBody.getDataValue("CLIENT_NO");//客户码
			String ccy=(String)reqBody.getDataValue("CCY");//币种
			Double credit_limit=Double.valueOf(reqBody.getDataValue("CREDIT_LIMIT").toString().trim());//授信额度
			String guarantee_mode=(String)reqBody.getDataValue("GUARANTEE_MODE");//担保方式
			String term_type=(String)reqBody.getDataValue("TERM_TYPE");//期限类型
			Double term=Double.valueOf(reqBody.getDataValue("TERM").toString().trim());//期限
			String start_date=TagUtil.formatDate2Ten((String)reqBody.getDataValue("START_DATE"));//起始日期
			String end_date=TagUtil.formatDate2Ten((String)reqBody.getDataValue("END_DATE"));//结束日期
			String buss_type = reqBody.getDataValue("BUSS_TYPE").toString().trim();//业务类型 （001 消费类 002 经营类 对应100086、100087产品编号）

			KeyedCollection kColl4LAD=new KeyedCollection();
			kColl4LAD.put("agr_no", agr_no);
			kColl4LAD.put("cus_id",client_no);
			kColl4LAD.put("cur_type", ccy);
			kColl4LAD.put("crd_amt", credit_limit);
			kColl4LAD.put("guar_type",guarantee_mode);//担保方式
			kColl4LAD.put("term_type", term_type);
			kColl4LAD.put("term", term);
			kColl4LAD.put("start_date", start_date);
			kColl4LAD.put("end_date", end_date);
			
			kColl4LAD.put("sub_type", "01");//授信分项默认为一般授信
			kColl4LAD.put("limit_type", "01");//循环额度
			kColl4LAD.put("limit_code", limit_code);
			//001为消费性 002为经营类
			if(!"".equals(buss_type) && "001".equals(buss_type)){
				kColl4LAD.put("limit_name", "100086");//个人微贷消费贷款
			}else if(!"".equals(buss_type) && "002".equals(buss_type)){
				kColl4LAD.put("limit_name", "100087");//个人微贷经营贷款
			}
			kColl4LAD.put("prd_id", "100066,100042,100067,100068,300020,300021,100046,100047,100048,100049,100050," 
					+"100051,100052,100053,100054,500025,500020,800020,500023,500024,500026,500021,500022,500031," 
					+"500027,500028,500029,100055,100056,100057,100058,100059,100060,100062,100063,400021,700021,"
					+"700020,400024,400020,500032,200024,400022,400023");//默认适用所有品种
			kColl4LAD.put("enable_amt", credit_limit);
			kColl4LAD.put("is_pre_crd", "2");//非预授信
			kColl4LAD.put("lmt_status", "10");//额度状态默认正常
			//担保方式为准全额质押/低风险质押时，风险标志为低风险
			if("210".equals(guarantee_mode) || "220".equals(guarantee_mode)) {
				kColl4LAD.put("lrisk_type", "10");
			}else{
				kColl4LAD.put("lrisk_type", "20");
			}
			kColl4LAD.put("green_indus", "2");//非绿色产业类型
			kColl4LAD.setName("LmtAgrDetails");
			dao.insert(kColl4LAD, connection);
			
			//校验该客户是否已存在信贷系统数据库中
			KeyedCollection kColl4Cus = (KeyedCollection) SqlClient.queryFirst("queryExistsCus", client_no, null, connection);
			if(kColl4Cus ==null || kColl4Cus.size()<0){
				//生成客户信息
				if(client_array!=null&&client_array.size()>0){
					for (int i=0;i<client_array.size();i++){

						KeyedCollection kColl4Client = (KeyedCollection) client_array.get(i);
						String cus_id = kColl4Client.getDataValue("CLIENT_NO").toString().trim(); //客户号
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
						
						kColl4CusBase.put("cus_id", cus_id);           
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
						kColl4CusBase.put("main_br_id",higher_org_no);//主管机构    
					    kColl4CusBase.put("cust_mgr", acct_exec);              
						//kColl4CusBase.put("input_id", input_id);        
						//kColl4CusBase.put("input_br_id", input_br_id);       
					    kColl4CusBase.put("input_date", openday);   //生成客户信息当天营业日         
						kColl4CusBase.put("belg_line", "BL300");//微贷平台客户均为个人条线            
						//kColl4CusBase.put("guar_crd_grade", );      
						kColl4CusBase.put("last_update_date", openday);//最近一次更新日期默认为当前营业日期
						kColl4CusBase.setName("CusBase");
						dao.insert(kColl4CusBase, connection);
						
						kColl4CusIndvi.put("cus_id", cus_id);
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
			
			
			EMPLog.log("Trade0200200000501",EMPLog.INFO,0,"【微贷额度登记】交易处理完成...", null);
		}catch(Exception e){
			retKColl.setDataValue("ret_code","9999");
			retKColl.setDataValue("ret_msg", "【微贷额度登记】业务处理失败！");
			e.printStackTrace();
			EMPLog.log("outReport", EMPLog.ERROR, 0, "【微贷额度登记】交易处理失败，失败信息为："+e.getMessage(), null);

		}
		return retKColl;
	}

}
