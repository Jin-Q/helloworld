package com.yucheng.cmis.biz01line.esb.op.trade.ecdsimple;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.yucheng.cmis.biz01line.esb.op.trade.TranService;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
/**
 * 电子商业汇票提示承兑
 * @author yangzy
 * 说明：
 * 接收电票系统发送的承兑票据明细，生成银票承兑业务明细
 * 
 */
public class TradeEcds4Acceptance extends TranService {

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
			KeyedCollection reqSysHead = esbInterface.getReqSysHead4ECDS(CD);
			String BATCH_NO = reqBody.getDataValue("BATCH_NO").toString().trim();                   //票据批次
			String DUTY_PERSON = reqBody.getDataValue("DUTY_PERSON").toString().trim();             //责任人
			String BRANCH_ID = reqSysHead.getDataValue("BRANCH_ID").toString().trim();              //责任机构
			IndexedCollection bill_array = (IndexedCollection) reqBody.getDataElement("BILL_ARRAY");//票据明细
			String openday  = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			context.put("OPENDAY", openday);
			String serno = "";
			String flag = "success";
			String actorno = "";
			String organno = "";
			String curType = "01";//默认币种人民币
			String DRAWER_BRANCH_ID = "";//出票人开户行行号
			String DRAWER_ACCT_NAME = "";//出票人开户行行名
			String DRAWER_ACCT_NO = "";//出票人开户行行号
			if(DUTY_PERSON!=null&&!"".equals(DUTY_PERSON)&&BRANCH_ID!=null&&!"".equals(BRANCH_ID)){
				actorno = DUTY_PERSON;
				organno = BRANCH_ID;
			}
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("actorno", actorno);
			paraMap.put("organno", organno);
			KeyedCollection kCollSDeptuser = (KeyedCollection) SqlClient.queryFirst("querySDeptuser", paraMap, null, connection);
			if(kCollSDeptuser==null||kCollSDeptuser.size()<0){
				flag = "error";
			}
			if("success".equals(flag)){
				if(bill_array!=null&&bill_array.size()>0){
					KeyedCollection kColl4App = (KeyedCollection) bill_array.get(0);
					String CCY = kColl4App.getDataValue("CCY").toString().trim();                  //币种
					String DRAWER_ACCT_OPEN_BRANCH_ID = kColl4App.getDataValue("DRAWER_ACCT_OPEN_BRANCH_ID").toString().trim();//出票人开户行
					DRAWER_ACCT_NO = kColl4App.getDataValue("DRAWER_ACCT_NO").toString().trim();//出票人开户行账号
					DRAWER_ACCT_NAME = kColl4App.getDataValue("DRAWER_ACCT_NAME").toString().trim();//出票人开户行户名
					if(curType.equals(CCY)){
						curType = "CNY";
					}
					if("".equals(DRAWER_BRANCH_ID)){
						DRAWER_BRANCH_ID = DRAWER_ACCT_OPEN_BRANCH_ID;
					}
					
					for (int j=0;j<bill_array.size();j++){
						KeyedCollection kColl_bill_array_j = (KeyedCollection) bill_array.get(j);  //币种
						if(kColl_bill_array_j.getDataValue("CCY")==null || !CCY.equals(kColl_bill_array_j.getDataValue("CCY").toString().trim())){
							flag = "error";
						}
						if(kColl_bill_array_j.getDataValue("DRAWER_ACCT_OPEN_BRANCH_ID")==null || "".equals(kColl_bill_array_j.getDataValue("DRAWER_ACCT_OPEN_BRANCH_ID").toString().trim()) || !DRAWER_BRANCH_ID.equals(kColl_bill_array_j.getDataValue("DRAWER_ACCT_OPEN_BRANCH_ID").toString().trim())){
							flag = "error2";
						}
					}
					String DRAWER_ORG_CODE = kColl4App.getDataValue("DRAWER_ORG_CODE").toString().trim();                              //出票人组织机构代码
					KeyedCollection CusInfo = (KeyedCollection)SqlClient.queryFirst("queryCusInfo", DRAWER_ORG_CODE, null, connection);//客户码
					String cus_id = "";
					if(CusInfo!=null&&CusInfo.size()>0){
						cus_id = CusInfo.getDataValue("cus_id").toString();
					}else{
						flag = "error1";
					}
					if("success".equals(flag)){
						serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
						Double applyAmt = 0.00;
						for (int i=0;i<bill_array.size();i++){
							//获取票据明细
							KeyedCollection kColl_bill_array = (KeyedCollection) bill_array.get(i);
							//String CCY = kColl_bill_array.getDataValue("CCY").toString().trim();                                                //币种              
							String BILL_NO = kColl_bill_array.getDataValue("BILL_NO").toString().trim();                                          //票据号            
							Double BILL_AMT = Double.valueOf(kColl_bill_array.getDataValue("BILL_AMT").toString().trim());                        //票据金额          
							applyAmt += BILL_AMT;
							String BILL_ISSUE_DATE = kColl_bill_array.getDataValue("BILL_ISSUE_DATE").toString().trim();                          //出票日期          
							String BILL_EXPIRY_DATE = kColl_bill_array.getDataValue("BILL_EXPIRY_DATE").toString().trim();                        //票据到期日期      
							String PAYEE_NAME = kColl_bill_array.getDataValue("PAYEE_NAME").toString().trim();                                    //收款人名称        
							String PAYEE_ACCT_NO = kColl_bill_array.getDataValue("PAYEE_ACCT_NO").toString().trim();                              //收款人账号        
							String PAYEE_ACCT_OPEN_BRANCH_ID = kColl_bill_array.getDataValue("PAYEE_ACCT_OPEN_BRANCH_ID").toString().trim();      //收款人开户行行号  
							String PAYEE_ACCT_OPEN_BRANCH_NAME = kColl_bill_array.getDataValue("PAYEE_ACCT_OPEN_BRANCH_NAME").toString().trim();  //收款人开户行名称  
							//String DRAWER_ORG_CODE = kColl_bill_array.getDataValue("DRAWER_ORG_CODE").toString().trim();                        //出票人组织机构代码
							String DRAWER_ACCT_NAME1 = kColl_bill_array.getDataValue("DRAWER_ACCT_NAME").toString().trim();                        //出票人户名        
							String DRAWER_ACCT_NO1 = kColl_bill_array.getDataValue("DRAWER_ACCT_NO").toString().trim();                            //出票人账号        
							String DRAWER_ACCT_OPEN_BRANCH_ID1 = kColl_bill_array.getDataValue("DRAWER_ACCT_OPEN_BRANCH_ID").toString().trim();   //出票人开户行行号  
							String ACCEPTOR_BANK_NAME = kColl_bill_array.getDataValue("ACCEPTOR_BANK_NAME").toString().trim();                    //承兑行名称        
							String ACCEPTOR_BANK_ACCT_NO = kColl_bill_array.getDataValue("ACCEPTOR_BANK_ACCT_NO").toString().trim();              //承兑行帐号        
							String ACCEPTOR_ACCT_OPEN_BRANCH_ID = kColl_bill_array.getDataValue("ACCEPTOR_ACCT_OPEN_BRANCH_ID").toString().trim();//承兑人开户行行号  
							
							//生成业务申请信息iqp_accp_detail
							KeyedCollection kColl_accp_detail = new KeyedCollection();
							kColl_accp_detail.put("serno", serno);                                   //业务流水号
							kColl_accp_detail.put("clt_person", PAYEE_NAME);                         //收款人
							kColl_accp_detail.put("clt_acct_no", PAYEE_ACCT_NO);                     //收款人账号
							kColl_accp_detail.put("paorg_no", PAYEE_ACCT_OPEN_BRANCH_ID);            //收款人开户行行号
							kColl_accp_detail.put("paorg_name", PAYEE_ACCT_OPEN_BRANCH_NAME);        //收款人开户行行名
							kColl_accp_detail.put("drft_amt", BILL_AMT);                             //票面金额
							kColl_accp_detail.put("bill_isse_date", BILL_ISSUE_DATE);                //出票日
							kColl_accp_detail.put("bill_expiry_date", BILL_EXPIRY_DATE);             //到期日
							kColl_accp_detail.put("porder_no", BILL_NO);                             //票据号
							kColl_accp_detail.put("batch_no", BATCH_NO);                             //票据批次号
							
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date expiryDateHelp = sdf.parse(BILL_EXPIRY_DATE);
							Date issueDateHelp = sdf.parse(BILL_ISSUE_DATE);
							long dayNum = (expiryDateHelp.getTime()-issueDateHelp.getTime())/(24L*60L*60L*1000L);
							if(dayNum <= 0){
								dayNum = 0;
							}
							kColl_accp_detail.put("term", dayNum);
							kColl_accp_detail.put("term_type", "003");//期限类型日
							
							kColl_accp_detail.setName("IqpAccpDetail");
							dao.insert(kColl_accp_detail,connection);
						}
						//生成业务申请信息iqp_loan_app,iqp_loan_app_sub,iqp_acc_accp
						String ACCEPTOR_BANK_NAME = kColl4App.getDataValue("ACCEPTOR_BANK_NAME").toString().trim();                    //承兑行名称        
						String ACCEPTOR_ACCT_OPEN_BRANCH_ID = kColl4App.getDataValue("ACCEPTOR_ACCT_OPEN_BRANCH_ID").toString().trim();//承兑人开户行行号  
						
						
						KeyedCollection kColl_app = new KeyedCollection();
						
						KeyedCollection returnKColl = (KeyedCollection)SqlClient.queryFirst("getHLByCurrType", curType, null, connection);     //获取汇率
						kColl_app.put("serno", serno);                                           //业务流水号
						kColl_app.put("cus_id", cus_id);                                         //客户码
						kColl_app.put("apply_date", openday);					                 //申请日期
						kColl_app.put("apply_amount", applyAmt);								 //申请金额
						kColl_app.put("input_date", openday);					                 //登记日期
						kColl_app.put("prd_id", "200024");							             //产品编号
						kColl_app.put("apply_cur_type", curType);					             //申请币种
						kColl_app.put("exchange_rate", returnKColl.getDataValue("base_remit"));	 //汇率
						kColl_app.put("input_id", DUTY_PERSON);									 //登记人
						kColl_app.put("input_br_id", BRANCH_ID);								 //登记机构
						kColl_app.put("approve_status", "000");								     //审批状态
						kColl_app.put("biz_type", "7");								             //业务模式(普通)
						KeyedCollection kColl_app_sub = new KeyedCollection();
						kColl_app_sub.put("serno", serno);                                       //业务流水号
						kColl_app_sub.put("is_collect_stamp", "1");                              //是否收取印花税
						kColl_app_sub.put("stamp_collect_mode", "2");                            //印花税收取方式
						
						KeyedCollection kColl_accp = new KeyedCollection();
						kColl_accp.put("serno", serno);                                          //业务流水号
						kColl_accp.put("is_elec_bill", "1");                              		 //是否电子票据
						kColl_accp.put("bill_qty", "1");                            			 //汇票数量 
						kColl_accp.put("actp_org_no", ACCEPTOR_ACCT_OPEN_BRANCH_ID);			 //承兑行行号
						kColl_accp.put("actp_org_name", ACCEPTOR_BANK_NAME);					 //承兑行名称
						kColl_accp.put("batch_no", BATCH_NO);                                    //票据批次号
						//added by yangzy 20151022 设置银行承兑电票签发类型默认自签  start
						kColl_accp.put("opac_type", "01");                            			 //签发类型默认自签 
						//added by yangzy 20151022 设置银行承兑电票签发类型默认自签  end
						kColl_app.setName("IqpLoanApp");
						dao.insert(kColl_app, connection);
						kColl_app_sub.setName("IqpLoanAppSub");
						dao.insert(kColl_app_sub, connection);
						kColl_accp.setName("IqpAccAccp");
						dao.insert(kColl_accp, connection);
						
						KeyedCollection kColl_acct = new KeyedCollection();
						kColl_acct.put("serno", serno);                                          //业务流水号
						kColl_acct.put("acct_attr", "03");                              		 //账户属性
						kColl_acct.put("is_this_org_acct", "1");                            	 //是否本行账户 
						kColl_acct.put("acct_no", DRAWER_ACCT_NO);			                     //账号
						kColl_acct.put("acct_name", DRAWER_ACCT_NAME);					         //户名
						/* added by yangzy 2014/10/08 银行承兑电票增加科目号 begin */
						kColl_acct.put("acct_gl_code", "99999");					             //科目号
						/* added by yangzy 2014/10/08 银行承兑电票增加科目号 end */
						String daorg_name = "";
						Map<String,String> pkMap = new HashMap<String,String>();
						KeyedCollection kColl4Bank = new KeyedCollection();
						pkMap.put("bank_no",DRAWER_BRANCH_ID);
						kColl4Bank = dao.queryDetail("PrdBankInfo", pkMap, connection);
						if(kColl4Bank!=null&&kColl4Bank.getDataValue("bank_name")!=null&&!"".equals(kColl4Bank.getDataValue("bank_name"))){
							daorg_name = kColl4Bank.getDataValue("bank_name").toString();
						}
						kColl_acct.put("opac_org_no", DRAWER_BRANCH_ID);                         //开户行行号/开户机构码（本行）
						kColl_acct.put("opan_org_name", daorg_name);                             //开户行行名/开户机构名称（本行）
						String pk_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
						kColl_acct.put("pk_id", pk_id);                                          //PKID
						kColl_acct.put("cur_type", "CNY");                                       //币种
						kColl_acct.put("interbank_id", DRAWER_BRANCH_ID);                        //银联行号						
						kColl_acct.setName("IqpCusAcct");
						dao.insert(kColl_acct, connection);
						
						

						EMPLog.log("TradeEcds4Acceptance", EMPLog.INFO, 0, "【电子商业汇票提示承兑】交易处理完成...", null);
					}else{
						if("error".equals(flag)){
							retKColl.setDataValue("ret_code", "9999");
							retKColl.setDataValue("ret_msg", "【电子商业汇票提示承兑】,业务处理失败,票据批次币种不一致！");
						}else if("error1".equals(flag)){
							retKColl.setDataValue("ret_code", "9999");
							retKColl.setDataValue("ret_msg", "【电子商业汇票提示承兑】,业务处理失败,出票人未在信贷开户！");
						}else if("error2".equals(flag)){
							retKColl.setDataValue("ret_code", "9999");
							retKColl.setDataValue("ret_msg", "【电子商业汇票提示承兑】,业务处理失败,出票人开户行行号不一致！");
						}
					}
				}
			}else{
				retKColl.setDataValue("ret_code", "9999");
				retKColl.setDataValue("ret_msg", "【电子商业汇票提示承兑】,业务处理失败,责任人不在责任机构下！");
			}
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "9999");
			retKColl.setDataValue("ret_msg", "【电子商业汇票提示承兑】,业务处理失败！");
			e.printStackTrace();
		}
		return retKColl;
	}

}
