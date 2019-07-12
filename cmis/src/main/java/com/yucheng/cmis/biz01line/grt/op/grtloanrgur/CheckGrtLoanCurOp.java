package com.yucheng.cmis.biz01line.grt.op.grtloanrgur;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class CheckGrtLoanCurOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "GrtLoanRGur";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String guar_cont_no ="";
			String serno ="";
			BigDecimal guar_amt = null;  
			BigDecimal thisAmt = new BigDecimal(0.00);
			try {
				 guar_cont_no = (String)context.getDataValue("guar_cont_no");
				 serno = (String)context.getDataValue("serno");
				 guar_amt = BigDecimalUtil.replaceNull(context.getDataValue("guar_amt"));
			} catch (Exception e) {} 
			if(guar_cont_no == null || guar_cont_no=="")
				throw new EMPJDBCException("The values to ["+guar_cont_no+"] cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);    
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			KeyedCollection kColl4ju = dao.queryDetail("GrtGuarCont", guar_cont_no, connection);
			
			//如果是无间贷，借新还旧，或者是置换业务  ----------------start--------------------------------------
			String cont_no_Repay = "";
			KeyedCollection kCollIqpSub = dao.queryDetail("IqpLoanAppSub", serno, connection);
			KeyedCollection kCollIqp = dao.queryDetail("IqpLoanApp", serno, connection);
			if(kCollIqpSub.containsKey("repay_bill")){
				String repay_bill = (String)kCollIqpSub.getDataValue("repay_bill");
				if(!"".equals(repay_bill) && repay_bill != null){
					String condition = "where cont_no in (select a.cont_no from acc_view a where a.bill_no='"+repay_bill+"')";
					KeyedCollection kCollCont = dao.queryFirst("CtrLoanCont",null, condition, connection);
					String sernoRepay = (String)kCollCont.getDataValue("serno");
					cont_no_Repay = (String)kCollCont.getDataValue("cont_no");
					serno = "'"+serno+"',"+"'"+sernoRepay+"'";
				}else{
					serno = "'"+serno+"'";
				}
			}else{
				serno = "'"+serno+"'";
			}
			//判断【产品代码】 为 “提贷担保” “同业代付”“信托收据贷款” “应收款买入” “汇票贴现” “福费廷” “出口议付”
			//“出口托收贷款”“出口商票融资”
			String prd_id = (String)kCollIqp.getDataValue("prd_id");
			if("500032".equals(prd_id) || "500020".equals(prd_id) || "500021".equals(prd_id)|| "500028".equals(prd_id)|| "500027".equals(prd_id)|| "500029".equals(prd_id)|| "500026".equals(prd_id)|| "500025".equals(prd_id)|| "500024".equals(prd_id)){
				//获取置换业务借据编号
				String sernoSelect = (String)context.getDataValue("serno");
				KeyedCollection kCollRes = (KeyedCollection)SqlClient.queryFirst("getReplaceBillNO", sernoSelect, null, connection);
				if(kCollRes != null){
					if(kCollRes.containsKey("rpled_serno")){
						String rpled_serno = (String)kCollRes.getDataValue("rpled_serno");
						String condition = "where cont_no in (select a.cont_no from acc_view a where a.bill_no='"+rpled_serno+"')";
						KeyedCollection kCollCont = dao.queryFirst("CtrLoanCont",null, condition, connection);
						String sernoRepay = (String)kCollCont.getDataValue("serno");
						cont_no_Repay = (String)kCollCont.getDataValue("cont_no");
						if(!"".equals(sernoRepay) && sernoRepay != null){
							serno = serno+","+"'"+sernoRepay+"'";
						}
					}
				}
			}
			//如果是无间贷，借新还旧，或者是置换业务  ----------------end--------------------------------------
			
			if(kColl4ju.containsKey("guar_cont_type")&&kColl4ju.getDataValue("guar_cont_type")!=null&&"00".equals(kColl4ju.getDataValue("guar_cont_type").toString())){
				//一般担保
				//根据合同编号和担保合同编号查询是否存在担保变更记录--start---------------
				String condtitionSelectIsChange = "where cont_no is null and corre_rel in ('2','4','3') and guar_cont_no = '"+guar_cont_no+"'";
			    IndexedCollection iCollSelectIsChange = dao.queryList("GrtLoanRGur", condtitionSelectIsChange, connection);
			    //根据合同编号和担保合同编号查询是否存在担保变更记录--end--------------------
			    String conditionStr = "";
//			    if(iCollSelectIsChange.size() > 0){
//					//新增，续作状态的
//			    	conditionStr = "where corre_rel in ('2','4','3') and guar_cont_no='"+guar_cont_no+"' and serno not in("+serno+")";
//				}else{
					//正常状态 被解除状态
					conditionStr = "where guar_cont_no='"+guar_cont_no+"' and corre_rel in('1','5') and is_add_guar='2' and serno not in("+serno+")";
				//}
				/**查询关联表中此担保合同已已经引入的金额*/
				IndexedCollection iColl =  dao.queryList("GrtLoanRGur", conditionStr, connection);
				for(int i=0;i<iColl.size();i++){
				   KeyedCollection kColl1 = (KeyedCollection)iColl.get(i);
				   String is_per_gur = (String)kColl1.getDataValue("is_per_gur");
				   if(is_per_gur != null && !"".equals(is_per_gur)){
					   String pk_id = (String)kColl1.getDataValue("pk_id");
					   String cont_no = (String)kColl1.getDataValue("cont_no");
					   if(cont_no != null && !"".equals(cont_no)){
						   String res = iqpLoanAppComponent.caculateGuarAmtSp(null, cont_no,pk_id);
						   if("2".equals(res)){
							   thisAmt = thisAmt.add(new BigDecimal(kColl1.getDataValue("guar_amt").toString()));
						   }else{
							   thisAmt = thisAmt.add(new BigDecimal(0));
						   }
					   }else{
						   String sernoSelect = (String)kColl1.getDataValue("serno");
						   String res = iqpLoanAppComponent.caculateGuarAmtSp(sernoSelect, null,pk_id);
						   if("2".equals(res)){
							   thisAmt = thisAmt.add(new BigDecimal(kColl1.getDataValue("guar_amt").toString()));
						   }else{
							   thisAmt = thisAmt.add(new BigDecimal(0));
						   }
					   }
					   
				   }
				}
			}else{
				//最高额担保
				
				//1.未生成合同部分
				String conditionStrIqp = "where guar_cont_no='"+guar_cont_no+"' and corre_rel ='1' and is_add_guar='2' and cont_no is null and serno not in("+serno+")";
				//2.生成合同部分
				String conditionStrCont = "";
				if(!"".equals(cont_no_Repay) && cont_no_Repay != null){
					conditionStrCont = "where guar_cont_no='"+guar_cont_no+"' and corre_rel not in('2','3','4','5','6') and is_add_guar='2' and serno not in (select serno from Grt_Loan_R_Gur where corre_rel = '1' and cont_no is null) and serno not in("+serno+") and cont_no !='"+cont_no_Repay+"'";
				}else{
					conditionStrCont = "where guar_cont_no='"+guar_cont_no+"' and corre_rel not in('2','3','4','5','6') and is_add_guar='2' and serno not in (select serno from Grt_Loan_R_Gur where corre_rel = '1' and cont_no is null) and serno not in("+serno+")";
				}
				//3.计算未生成合同部分
				IndexedCollection iCollIqp = dao.queryList("GrtLoanRGur", conditionStrIqp, connection);
				for(int i=0;i<iCollIqp.size();i++){
					KeyedCollection kCollIqpSelect = (KeyedCollection)iCollIqp.get(i);
					String sernoSelect = (String)kCollIqpSelect.getDataValue("serno");
					String pk_value = (String)kCollIqpSelect.getDataValue("pk_id");
				    String res = iqpLoanAppComponent.caculateGuarAmtSp(sernoSelect, null,pk_value);
				    BigDecimal guar_amt_select = new BigDecimal(0);
				    if("2".equals(res)){
				    	guar_amt_select = BigDecimalUtil.replaceNull(kCollIqpSelect.getDataValue("guar_amt"));
				    }else{
				    	guar_amt_select = new BigDecimal(0);
				    }
					//BigDecimal riskAmt = BigDecimalUtil.replaceNull(iqpLoanAppComponent.getLmtAmtBySerno(serno));
					thisAmt = thisAmt.add(guar_amt_select);
				}
				//4.计算合同未出账部分
				IndexedCollection iCollCont =  dao.queryList("GrtLoanRGur", conditionStrCont, connection);
				for(int i=0;i<iCollCont.size();i++){
					KeyedCollection kCollCont = (KeyedCollection)iCollCont.get(i);
					String cont_no = TagUtil.replaceNull4String(kCollCont.getDataValue("cont_no"));
					String pk_value = (String)kCollCont.getDataValue("pk_id");
					//根据合同编号和担保合同编号查询是否存在担保变更记录--start---------------
					String serno4Cont = "";
					String condtitionSelectIsChange = "where corre_rel in ('2','4','3') and is_add_guar='2' and guar_cont_no = '"+guar_cont_no+"'";
				    IndexedCollection iCollSelectIsChange = dao.queryList("GrtLoanRGur", condtitionSelectIsChange, connection);
				    if(iCollSelectIsChange.size() > 0 && (cont_no == null || "".equals(cont_no))){
				    	KeyedCollection kCollSelectIsChange = (KeyedCollection)iCollSelectIsChange.get(0);
				    	serno4Cont = (String)kCollSelectIsChange.getDataValue("serno");
				    	IndexedCollection iCollRes = SqlClient.queryList4IColl("selectIsChangeBySernoStr", serno4Cont, connection);
				    	if(iCollRes.size()>0){
				    		KeyedCollection kCollRes = (KeyedCollection)iCollRes.get(0);
				    		cont_no = (String)kCollRes.getDataValue("cont_no");
				    	}
				    }
				    //根据合同编号和担保合同编号查询是否存在担保变更记录--end--------------------
					IndexedCollection iCollAcc = dao.queryList("AccView", "where cont_no='"+cont_no+"'", connection);
					if(iCollAcc.size()<=0){
						String res = iqpLoanAppComponent.caculateGuarAmtSp(null, cont_no,pk_value);
						BigDecimal guar_amt_select = new BigDecimal(0);
						if("2".equals(res)){
							guar_amt_select = BigDecimalUtil.replaceNull(kCollCont.getDataValue("guar_amt"));
							BigDecimal riskAmt = BigDecimalUtil.replaceNull(iqpLoanAppComponent.getOneLmtAmtByContNo(cont_no));
							if(riskAmt.compareTo(guar_amt_select)>=0){
								thisAmt = thisAmt.add(guar_amt_select);
							}else{
								thisAmt = thisAmt.add(riskAmt);
							}
						}else{
							guar_amt_select = new BigDecimal(0);
							thisAmt = thisAmt.add(guar_amt_select);
						}
					}else{
						IndexedCollection iColl4loan = new IndexedCollection();
						iColl4loan = SqlClient.queryList4IColl("selectAccByContNo", cont_no, connection);
						if(iColl4loan!=null&&iColl4loan.size()>0){
//							for(int j=0;j<iColl4loan.size();j++){
							String res = iqpLoanAppComponent.caculateGuarAmtSp(null, cont_no,pk_value);
							
							if("2".equals(res)){
								BigDecimal loan_bal_ck = iqpLoanAppComponent.getLmtAmtByContNo(cont_no);
//								KeyedCollection kColl4loan = (KeyedCollection)iColl4loan.get(j);
//								KeyedCollection kColl4cont = dao.queryDetail("CtrLoanCont", cont_no, connection);
//								
//								BigDecimal loan_balance = new BigDecimal(kColl4loan.getDataValue("loan_balance").toString());
//								BigDecimal security_rate = new BigDecimal(kColl4cont.getDataValue("security_rate").toString());
//								BigDecimal cont_balance = BigDecimalUtil.replaceNull(kColl4cont.getDataValue("cont_balance"));
//								//获取实时汇率  start
//								String cur_type = (String) kColl4cont.getDataValue("cont_cur_type");
//								CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//								IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
//								KeyedCollection kCollRate = service.getHLByCurrType(cur_type, context, connection);
//								if("failed".equals(kCollRate.getDataValue("flag"))){
//									throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//								}
//								BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
//								//获取实时汇率  end
//								
//								BigDecimal loan_bal_ck = (loan_balance.add(cont_balance.multiply(exchange_rate))).multiply(new BigDecimal(1.00).subtract(security_rate));
								if(loan_bal_ck.compareTo(new BigDecimal(0.00))>0){
									String flag = "start";
									String conditionStr4co = "";
									//if(iCollSelectIsChange.size() > 0){
										//新增，续作状态的
									//	conditionStr4co = "where serno='"+serno4Cont+"' and corre_rel in ('2','4') and is_add_guar='2' order by guar_lvl desc";
									//}else{
										//正常状态
										conditionStr4co = "where cont_no='"+cont_no+"' and corre_rel ='1' and is_add_guar='2' order by guar_lvl desc";
									//}
									/**查询关联业务*/
									IndexedCollection iColl4co =  dao.queryList("GrtLoanRGur", conditionStr4co, connection);
									if(iColl4co!=null&&iColl4co.size()>0){
										for(int k=0;k<iColl4co.size();k++){
											if("start".equals(flag)){
												KeyedCollection kColl4co = (KeyedCollection)iColl4co.get(k);
												BigDecimal guar_amt_select = new BigDecimal(kColl4co.getDataValue("guar_amt").toString());
												String guar_cont_no4tmp = kColl4co.getDataValue("guar_cont_no").toString();
												if(guar_cont_no4tmp!=null&&guar_cont_no.equals(guar_cont_no4tmp)){
													if(loan_bal_ck.compareTo(guar_amt_select)>=0){
														thisAmt = thisAmt.add(guar_amt_select);
														flag = "end";
													}else{
														thisAmt = thisAmt.add(loan_bal_ck);
														flag = "end";
													}
												}else{
													if(loan_bal_ck.compareTo(guar_amt_select)>=0){
														loan_bal_ck = loan_bal_ck.subtract(guar_amt_select);
													}else{
														thisAmt = thisAmt.add(new BigDecimal(0.00));
														flag = "end";
													}
												}
											}
										}
									}
								}else{
									thisAmt = thisAmt.add(new BigDecimal(0.00));
								}
							}else{
								thisAmt = thisAmt.add(new BigDecimal(0));
							}
//							}
						}else{
							thisAmt = thisAmt.add(new BigDecimal(kCollCont.getDataValue("guar_amt").toString()));
						}
					}
				}	
			}
			
			/**调用担保模块接口,获取此担保合同总金融*/			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
			KeyedCollection kCollCont = service.viewGuarContInfoDetail(guar_cont_no, connection,context);
			BigDecimal Amt = new BigDecimal(0);
			if(context.containsKey("guar_cont_amt")){
				Amt = BigDecimalUtil.replaceNull(context.getDataValue("guar_cont_amt"));
			}else{
				Amt = BigDecimalUtil.replaceNull(kCollCont.getDataValue("guar_amt").toString());
			}
			//BigDecimal a = thisAmt.add(guar_amt);
			int b = Amt.compareTo(thisAmt); 
			BigDecimal canAmt = Amt.subtract(thisAmt);
			if(b>=0){
				context.addDataField("flag","success");
				context.addDataField("canAmt",thisAmt); 
			}else{
				context.addDataField("flag","error");
				context.addDataField("canAmt", canAmt); 
			} 
			context.addDataField("Amt", Amt);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
