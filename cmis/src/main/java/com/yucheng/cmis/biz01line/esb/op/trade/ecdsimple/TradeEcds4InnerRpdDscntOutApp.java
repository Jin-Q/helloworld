package com.yucheng.cmis.biz01line.esb.op.trade.ecdsimple;

import java.math.BigDecimal;
import java.sql.Connection;
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
 * 电子商业汇票系统内转贴现卖出申请
 * @author yangzy
 * 说明：
 * 接收电票系统发送的系统内转贴现卖出票据明细，生成系统内转贴现卖出业务明细
 * 
 */
public class TradeEcds4InnerRpdDscntOutApp extends TranService {

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
			IndexedCollection bill_array = (IndexedCollection) reqBody.getDataElement("BILL_ARRAY");                       //票据明细
			String openday  = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			context.put("OPENDAY", openday);
			String serno = "";         //申请流水
			int bill_qty = 0;          //票据数量
			String curType = "01";     //默认币种人民币
			Double TOTAL_DISCOUNT_AMT = Double.valueOf(reqBody.getDataValue("TOTAL_DISCOUNT_AMT").toString().trim());                //汇总贴现金额    
			Double TOTAL_DISCOUNT_ACT_PAY_AMT = Double.valueOf(reqBody.getDataValue("TOTAL_DISCOUNT_ACT_PAY_AMT").toString().trim());//汇总贴现实付金额
			Double TOTAL_DISCOUNT_INTEREST = Double.valueOf(reqBody.getDataValue("TOTAL_DISCOUNT_INTEREST").toString().trim());      //汇总贴现利息    
			String DISCOUNT_KIND = reqBody.getDataValue("DISCOUNT_KIND").toString().trim();                                          //贴现种类        
			String BILL_KIND = reqBody.getDataValue("BILL_KIND").toString().trim();                                                  //票据种类        
			String CCY = reqBody.getDataValue("CCY").toString().trim(); 
			if(curType.equals(CCY)){
				curType = "CNY";
			}//币种            
			String BATCH_NO = reqBody.getDataValue("BATCH_NO").toString().trim();                                                    //批次号    
			String BRANCH_ID = reqSysHead.getDataValue("BRANCH_ID").toString().trim();                                               //责任机构
			String DISCOUNTER_BANK_NAME = reqBody.getDataValue("DISCOUNTEE_BANK_NAME").toString().trim();                            //贴出行名称      
			String DISCOUNTER_BANK_BRANCH_ID = reqBody.getDataValue("DISCOUNTEE_BANK_BRANCH_ID").toString().trim();                  //贴出行行号      
			String DUTY_PERSON = reqBody.getDataValue("DUTY_PERSON").toString().trim();                                              //责任人

			String biz_type = "";
			String prd_id = "";
			String flag = "success";
			biz_type = "05";//内部转贴现
			prd_id = "300022";//内部转贴现300022
			
			String bill_type = "";
			if("1".equals(BILL_KIND)){//电子银承
				bill_type = "100";
			}else{//电子商承
				bill_type = "200";
			}
			String actorno = "";
			String organno = "";
			if(DUTY_PERSON!=null&&!"".equals(DUTY_PERSON)&&BRANCH_ID!=null&&!"".equals(BRANCH_ID)){
				actorno = DUTY_PERSON;
				organno = BRANCH_ID;
			}
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("actorno", actorno);
			paraMap.put("organno", organno);
			KeyedCollection kCollSDeptuser = (KeyedCollection) SqlClient.queryFirst("querySDeptuser", paraMap, null, connection);
			if(kCollSDeptuser==null||kCollSDeptuser.size()<1){
				flag = "error";
			}
			Map<String,String> pkMap = new HashMap<String,String>();
			KeyedCollection kColl4Bank = new KeyedCollection();
			if("success".equals(flag)){
				if(bill_array!=null&&bill_array.size()>0){
					bill_qty = bill_array.size();
					String RATE = "";
					for (int j=0;j<bill_array.size();j++){
						KeyedCollection kColl_bill_array_j = (KeyedCollection) bill_array.get(j);    
						if(kColl_bill_array_j.getDataValue("CCY")==null || "".equals(kColl_bill_array_j.getDataValue("CCY").toString().trim()) || !CCY.equals(kColl_bill_array_j.getDataValue("CCY").toString().trim())){
							flag = "error1";
						}
						if("".equals(RATE)){
							RATE = kColl_bill_array_j.getDataValue("DISCOUNT_RATE").toString().trim();
						}
						if(kColl_bill_array_j.getDataValue("DISCOUNT_RATE")==null || "".equals(kColl_bill_array_j.getDataValue("DISCOUNT_RATE")) || BigDecimal.valueOf(Double.valueOf(RATE)).compareTo(BigDecimal.valueOf(Double.valueOf(kColl_bill_array_j.getDataValue("DISCOUNT_RATE").toString().trim())))>0){
							flag = "error2";
						}
					}
					if("success".equals(flag)){
						for (int i=0;i<bill_array.size();i++){
							//获取票据明细
							KeyedCollection kColl_bill_array = (KeyedCollection) bill_array.get(i);
							String BILL_NO = kColl_bill_array.getDataValue("BILL_NO").toString().trim();                                          //票据号            
							Double BILL_AMT = Double.valueOf(kColl_bill_array.getDataValue("BILL_AMT").toString().trim());                        //票据金额          
							//String CCY  = kColl_bill_array.getDataValue("CCY").toString().trim();                                               //币种              
							Double DISCOUNT_ACT_PAY_AMT = Double.valueOf(kColl_bill_array.getDataValue("DISCOUNT_ACT_PAY_AMT").toString().trim());//贴现实付金额      
							Double DISCOUNT_RATE = Double.valueOf(kColl_bill_array.getDataValue("DISCOUNT_RATE").toString().trim());              //贴现利率          
							String DISCOUNT_DAYS = kColl_bill_array.getDataValue("DISCOUNT_DAYS").toString().trim();                              //贴现天数          
							Double DISCOUNT_INTEREST  = Double.valueOf(kColl_bill_array.getDataValue("DISCOUNT_INTEREST").toString().trim());     //贴现利息          
							String BILL_ISSUE_DATE = kColl_bill_array.getDataValue("BILL_ISSUE_DATE").toString().trim();                          //出票日期          
							String BILL_EXPIRY_DATE = kColl_bill_array.getDataValue("BILL_EXPIRY_DATE").toString().trim();                        //票据到期日期      
							String DRAWER_ACCT_NAME = kColl_bill_array.getDataValue("DRAWER_ACCT_NAME").toString().trim();                        //出票人户名        
							String DRAWER_ACCT_OPEN_BRANCH_ID = kColl_bill_array.getDataValue("DRAWER_ACCT_OPEN_BRANCH_ID").toString().trim();    //出票人开户行行号  
							String DRAWER_ACCT_NO = kColl_bill_array.getDataValue("DRAWER_ACCT_NO").toString().trim();                            //出票人账号        
							String ACCEPTOR_ACCT_NAME = kColl_bill_array.getDataValue("ACCEPTOR_ACCT_NAME").toString().trim();                    //承兑人名称        
							String ACCEPTOR_ACCT_NO = kColl_bill_array.getDataValue("ACCEPTOR_ACCT_NO").toString().trim();                        //承兑人帐号        
							String ACCEPTOR_ACCT_OPEN_BRANCH_ID = kColl_bill_array.getDataValue("ACCEPTOR_ACCT_OPEN_BRANCH_ID").toString().trim();//承兑人开户行行号
							
							//生成票据明细信息IQP_BILL_DETAIL
							KeyedCollection kColl_bill_detail = new KeyedCollection();
							kColl_bill_detail.put("bill_type", bill_type);                  //票据种类          
							kColl_bill_detail.put("porder_no", BILL_NO);                    //汇票号码          
							kColl_bill_detail.put("porder_curr", curType);                  //汇票币种          
							kColl_bill_detail.put("drft_amt", BILL_AMT);                    //票面金额          
							kColl_bill_detail.put("is_ebill", "1");                         //是否电票          
							kColl_bill_detail.put("bill_isse_date", BILL_ISSUE_DATE);       //票据签发日        
							kColl_bill_detail.put("porder_end_date", BILL_EXPIRY_DATE);     //汇票到期日     
							kColl_bill_detail.put("isse_name", DRAWER_ACCT_NAME);           //出票人名称 
							kColl_bill_detail.put("daorg_no", DRAWER_ACCT_OPEN_BRANCH_ID);  //出票人开户行行号  
							String daorg_name = "";
							pkMap.put("bank_no",DRAWER_ACCT_OPEN_BRANCH_ID);
							kColl4Bank = dao.queryDetail("PrdBankInfo", pkMap, connection);
							if(kColl4Bank!=null&&kColl4Bank.getDataValue("bank_name")!=null&&!"".equals(kColl4Bank.getDataValue("bank_name"))){
								daorg_name = kColl4Bank.getDataValue("bank_name").toString();
							}
							kColl_bill_detail.put("daorg_name", daorg_name);  				//出票人开户行行名  
							kColl_bill_detail.put("daorg_acct", DRAWER_ACCT_NO);            //出票人开户行账号  
							
							String aaorg_name = "";
							pkMap.put("bank_no",ACCEPTOR_ACCT_OPEN_BRANCH_ID);
							kColl4Bank = dao.queryDetail("PrdBankInfo", pkMap, connection);
							if(kColl4Bank!=null&&kColl4Bank.getDataValue("bank_name")!=null&&!"".equals(kColl4Bank.getDataValue("bank_name"))){
								aaorg_name = kColl4Bank.getDataValue("bank_name").toString();
							}
							if("100".equals(bill_type)){//银行承兑汇票
								kColl_bill_detail.put("aorg_no", ACCEPTOR_ACCT_OPEN_BRANCH_ID);//承兑行行号
								kColl_bill_detail.put("aorg_name", aaorg_name);				   //承兑行名称
							}else{//商业承兑汇票
								kColl_bill_detail.put("aaorg_no", ACCEPTOR_ACCT_OPEN_BRANCH_ID);//承兑人开户行行号  
								kColl_bill_detail.put("aaorg_name", aaorg_name);				//承兑人开户行名称
								kColl_bill_detail.put("aaorg_acct_no", ACCEPTOR_ACCT_NO);       //承兑人开户行账号  
							}
							
							kColl_bill_detail.put("status", "01");                          //票据状态(01登记)
							kColl_bill_detail.setName("IqpBillDetail");
							
							//判断系统是否存在该票据
							//KeyedCollection kColl_bill_detail_his = dao.queryDetail("IqpBillDetail", BILL_NO, connection);
							//if(kColl_bill_detail_his!=null&&kColl_bill_detail_his.size()>0&&kColl_bill_detail_his.getDataValue("porder_no")!=null&&!"".equals(kColl_bill_detail_his.getDataValue("porder_no"))){
							//	dao.update(kColl_bill_detail,connection);
							//}else{
							//	dao.insert(kColl_bill_detail,connection);
							//}
							
							//生成票据利息明细信息IQP_BILL_INCOME
							KeyedCollection kColl_bill_income = new KeyedCollection();
							kColl_bill_income.put("biz_type", biz_type);                     //业务类型        
							kColl_bill_income.put("batch_no", BATCH_NO);                     //批次号          
							kColl_bill_income.put("porder_no", BILL_NO);                     //汇票号码        
							kColl_bill_income.put("fore_disc_date", openday);                //预计转/贴现日期(交易当天)     
							kColl_bill_income.put("drft_amt", BILL_AMT);                     //票面金额        
							kColl_bill_income.put("disc_days", DISCOUNT_DAYS);               //贴现天数       
							kColl_bill_income.put("disc_rate", DISCOUNT_RATE);               //转/贴现利率
							kColl_bill_income.put("int", DISCOUNT_INTEREST);                 //利息
							
							kColl_bill_income.setName("IqpBillIncome");
							dao.insert(kColl_bill_income,connection);
							//生成票据批次明细关系信息IQP_BATCH_BILL_REL
							KeyedCollection kColl_bill_rel = new KeyedCollection();
							kColl_bill_rel.put("batch_no", BATCH_NO);                        //批次号 
							kColl_bill_rel.put("porder_no", BILL_NO);                        //汇票号码   
							kColl_bill_rel.setName("IqpBatchBillRel");
							dao.insert(kColl_bill_rel,connection);
						}
						//生成流水
						serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
						//生成业务票据批次关联明细IQP_BATCH_MNG
						KeyedCollection kColl_batch_mng = new KeyedCollection();
						kColl_batch_mng.put("batch_no", BATCH_NO);                   //批次号            
						kColl_batch_mng.put("serno", serno);                         //业务编号          
						kColl_batch_mng.put("bill_type", bill_type);                 //票据种类          
						kColl_batch_mng.put("biz_type", biz_type);                   //业务类型  
						kColl_batch_mng.put("fore_disc_date", openday);              //预计转/贴现日期   
						kColl_batch_mng.put("bill_qnt", bill_qty);                   //票据数量          
						kColl_batch_mng.put("bill_total_amt", TOTAL_DISCOUNT_AMT);   //票据总金额        
						kColl_batch_mng.put("int_amt", TOTAL_DISCOUNT_INTEREST);     //利息金额          
						kColl_batch_mng.put("rpay_amt", TOTAL_DISCOUNT_ACT_PAY_AMT); //实付金额          
						kColl_batch_mng.put("status", "02");                         //状态(02引用)             
						kColl_batch_mng.put("input_date", openday);                  //登记日期
						kColl_batch_mng.put("input_id", DUTY_PERSON);				 //登记人
						kColl_batch_mng.put("input_br_id", BRANCH_ID);				 //登记机构
						kColl_batch_mng.put("opp_org_no", DISCOUNTER_BANK_BRANCH_ID);//对手行行号
						kColl_batch_mng.put("opp_org_name", DISCOUNTER_BANK_NAME);	 //对手行行名
						kColl_batch_mng.put("rate", RATE);	                         //转/再贴现利率
						
						kColl_batch_mng.setName("IqpBatchMng");
						dao.insert(kColl_batch_mng,connection);
												
						//生成业务转贴现申请主表IQP_Rpddscnt
						KeyedCollection kColl_app = new KeyedCollection();
						kColl_app.put("serno", serno);                          //业务编号
						kColl_app.put("batch_no", BATCH_NO);                    //批次号
						kColl_app.put("prd_id", prd_id);                        //产品编码
						kColl_app.put("rpddscnt_type", biz_type);               //转贴现方式
						kColl_app.put("toorg_no", DISCOUNTER_BANK_BRANCH_ID);   //交易对手行号
						kColl_app.put("toorg_name", DISCOUNTER_BANK_NAME);      //交易对手行名
						kColl_app.put("bill_type", bill_type);                  //票据种类
						kColl_app.put("bill_curr", curType);                    //票据币种
						kColl_app.put("bill_total_amt", TOTAL_DISCOUNT_AMT);    //票据总金额
						kColl_app.put("bill_qnt", bill_qty);                    //票据数量
						kColl_app.put("rpddscnt_date", openday);                //转贴现日期
						kColl_app.put("rpddscnt_rate", RATE);                   //转贴现利率
						kColl_app.put("rpddscnt_int", TOTAL_DISCOUNT_INTEREST); //总贴现利息
						kColl_app.put("rpay_amt", TOTAL_DISCOUNT_ACT_PAY_AMT);  //总实付金额
						kColl_app.put("input_id", DUTY_PERSON);                 //登记人
						kColl_app.put("input_br_id", BRANCH_ID);                //登记机构
						kColl_app.put("input_date", openday);                   //登记日期
						kColl_app.put("approve_status", "000");                 //申请状态

						kColl_app.setName("IqpRpddscnt");
						dao.insert(kColl_app, connection);
						
						EMPLog.log("TradeEcds4InnerRpdDscntOutApp", EMPLog.INFO, 0, "【电子商业汇票系统内转贴现卖出申请】交易处理完成...", null);
					}else{
						if("error1".equals(flag)){
							retKColl.setDataValue("ret_code", "9999");
							retKColl.setDataValue("ret_msg", "【电子商业汇票系统内转贴现卖出申请】,业务处理失败,票据批次币种为空/不一致！");
						}else if("error2".equals(flag)){
							retKColl.setDataValue("ret_code", "9999");
							retKColl.setDataValue("ret_msg", "【电子商业汇票系统内转贴现卖出申请】,业务处理失败,票据批次转贴现利率为空/不一致！");
						}
					}
				}
			}else{
				retKColl.setDataValue("ret_code", "9999");
				retKColl.setDataValue("ret_msg", "【电子商业汇票系统内转贴现卖出申请】,业务处理失败,责任人不在责任机构下！");
			}
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "9999");
			retKColl.setDataValue("ret_msg", "【电子商业汇票系统内转贴现卖出申请】,业务处理失败！");
			e.printStackTrace();
		}
		return retKColl;
	}

}
