package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 
*@author lisj
*@time 2015-8-6
*@description TODO 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求
*@version v1.0
* <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
*
 */
public class QueryModifyHisDetailOp  extends CMISOperation {
		
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		
		try{
			connection = this.getConnection(context);
			String modify_rel_serno ="";
			String op="";
			String cont_no ="";
			String prd_id="";
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
			}
			if(context.containsKey("op")){
				op = (String)context.getDataValue("op");
			}
			if(context.containsKey("cont_no")){
				cont_no = (String)context.getDataValue("cont_no");
			}
			if(context.containsKey("prd_id")){
				prd_id = (String)context.getDataValue("prd_id");
			}
			if(modify_rel_serno == null || modify_rel_serno.length() == 0)
				throw new EMPJDBCException("获取modify_rel_serno值失败!");
			if(op == null || op.length() == 0)
				throw new EMPJDBCException("获取op标识符失败!");
			if(cont_no == null || cont_no.length() == 0)
				throw new EMPJDBCException("获取cont_no编号失败!");
			if(prd_id == null || prd_id.length() == 0)
				throw new EMPJDBCException("获取prd_id编号失败!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl4PBMR = dao.queryDetail("PvpBizModifyRel", modify_rel_serno, connection);
			String approve_status = (String)kColl4PBMR.getDataValue("approve_status");
			
			//获取修改历史对比值
			if(!"".equals(op) && "his4iea".equals(op)){//获取展期修改历史对比值
				KeyedCollection kColl4MH = new KeyedCollection();
				kColl4MH = (KeyedCollection)SqlClient.queryFirst("queryModifyHis4IEAList", modify_rel_serno, null, connection);
				kColl4MH.setName("BizModifyHis4IEA");
				this.putDataElement2Context(kColl4MH, context);
			}else if(!"".equals(op) && "his".equals(op)){//获取贷款出账修改历史对比值

				IndexedCollection iColl4MH = new IndexedCollection("BizModifyHisList");
				KeyedCollection kColl4MH = new KeyedCollection("BizModifyHis");

				KeyedCollection  kColl4IAA = new KeyedCollection();
				IndexedCollection iColl4IAA = new IndexedCollection();
				IndexedCollection iColl4PBIH = new IndexedCollection();
				IndexedCollection iColl4PBIT = new IndexedCollection();
				IndexedCollection iColl4IATT = new IndexedCollection();
				IndexedCollection iColl4IATH = new IndexedCollection();
				
				kColl4MH = (KeyedCollection) SqlClient.queryFirst("queryModifyHisList", modify_rel_serno, null, connection);
				iColl4MH = SqlClient.queryList4IColl("queryModifyHisList", modify_rel_serno, connection);
				
				if(!"".equals(prd_id) && ("200024".equals(prd_id) ||"400020".equals(prd_id) || "400021".equals(prd_id))){
					iColl4PBIT = dao.queryList("PubBailInfoTmp", "where modify_rel_serno in (select g.modify_rel_serno from pvp_biz_modify_rel g"
							+" where g.approve_status <> '998' and g.modify_rel_serno = '"+modify_rel_serno+"')", connection);
					iColl4PBIT.setName("PubBailInfoTmpList");
					iColl4PBIH = dao.queryList("PubBailInfoHis", "where modify_rel_serno in (select g.modify_rel_serno from pvp_biz_modify_rel g"
							+" where g.approve_status <> '998' and g.modify_rel_serno = '"+modify_rel_serno+"')", connection);
					iColl4PBIH.setName("PubBailInfoHisList");
					//检查保证金信息是否修改
					String flag_PubBailInfo ="yes";
					if(iColl4PBIT!=null && iColl4PBIH!=null && iColl4PBIT.size()>0 && iColl4PBIH.size()>0){
						if(iColl4PBIT.size() == iColl4PBIH.size()){
							KeyedCollection temp1 = (KeyedCollection) iColl4PBIH.get(0);
							KeyedCollection temp2 = (KeyedCollection) iColl4PBIT.get(0);
							if(!temp1.getDataValue("bail_acct_no").toString().trim().equals(temp2.getDataValue("bail_acct_no").toString().trim())){	
								flag_PubBailInfo ="no";
							}
						}
					}
					context.put("flag_PubBailInfo", flag_PubBailInfo);
				}
				
				if(!"".equals(prd_id) && "200024".equals(prd_id)){
					IndexedCollection iColl4IAI = SqlClient.queryList4IColl("queryIqpAccpInfo", modify_rel_serno, connection);
					iColl4IAA = SqlClient.queryList4IColl("queryIqpAccpDetailInfo", modify_rel_serno, connection);//查询票据明细信息
					iColl4IAA.setName("IqpAccpDetailInfoList");
					if(iColl4IAI!=null && iColl4IAI.size()>0){
						kColl4IAA = (KeyedCollection) SqlClient.queryFirst("queryIqpAccpInfo", modify_rel_serno, null, connection);//查询汇票信息
						kColl4IAA.setName("IqpAccpInfo");
						this.putDataElement2Context(kColl4IAA, context);
					}
					if(iColl4IAA!=null && iColl4IAA.size()>0){
						this.putDataElement2Context(iColl4IAA, context);
					}
					String flag_IqpAccpDetailInfo ="no";
					if(iColl4IAA==null || iColl4IAA.size()<=0){
						flag_IqpAccpDetailInfo="yes";
					}
					context.put("flag_IqpAccpDetailInfo", flag_IqpAccpDetailInfo);
					//过滤否决的修改操作信息
					iColl4IATT = dao.queryList("IqpAppendTermsTmp", "where modify_rel_serno in (select g.modify_rel_serno from pvp_biz_modify_rel g"
									+" where g.approve_status <> '998' and g.modify_rel_serno = '"+modify_rel_serno+"')", connection);
					iColl4IATT.setName("IqpAppendTermsTmpList");
					iColl4IATH = dao.queryList("IqpAppendTermsHis", "where modify_rel_serno in (select g.modify_rel_serno from pvp_biz_modify_rel g"
									+" where g.approve_status <> '998' and g.modify_rel_serno = '"+modify_rel_serno+"')", connection);
					iColl4IATH.setName("IqpAppendTermsHisList");

					//检查附加条款信息是否修改
					String flag_AppendTerms ="yes";
					if(iColl4IATT!=null && iColl4IATH!=null && iColl4IATT.size()>0 && iColl4IATH.size()>0){
						if(iColl4IATT.size() == iColl4IATH.size()){
							KeyedCollection temp1 = (KeyedCollection) iColl4IATT.get(0);
							KeyedCollection temp2 = (KeyedCollection) iColl4IATH.get(0);
							if(!(temp1.getDataValue("fee_amt").toString().trim()).equals(temp2.getDataValue("fee_amt").toString().trim()) 
								||(!(temp1.getDataValue("collect_type").toString().trim()).equals(temp2.getDataValue("collect_type").toString().trim())) 
								||(!(temp1.getDataValue("fee_code").toString().trim()).equals(temp2.getDataValue("fee_code").toString().trim()))){
								flag_AppendTerms ="no";
							}
						}
					}
					context.put("flag_AppendTerms", flag_AppendTerms);
					
					//从数据库取业务主表申请金额
					KeyedCollection kColl4CLC = dao.queryDetail("CtrLoanCont", cont_no, connection);
					KeyedCollection kColl = dao.queryDetail("IqpLoanApp", (String) kColl4CLC.getDataValue("serno"), connection);
					String apply_amount = (String)kColl.getDataValue("apply_amount");
					
					KeyedCollection kCollCsgnLoanInfo = (KeyedCollection)dao.queryDetail("IqpCsgnLoanInfo", (String) kColl4CLC.getDataValue("serno"), connection);
					String chrg_rate = "";
					if(kCollCsgnLoanInfo != null){
						String sernoCsgnLoanInfo = (String)kCollCsgnLoanInfo.getDataValue("serno");
						if(sernoCsgnLoanInfo != null && !"".equals(sernoCsgnLoanInfo)){
							chrg_rate = (String)kCollCsgnLoanInfo.getDataValue("chrg_rate");
							
						}
					}
					context.put("chrg_rate", chrg_rate);
					context.put("apply_amount", apply_amount); 
					
				}
				if(kColl4MH!=null && iColl4MH!=null && iColl4MH.size()>0){
					kColl4MH.setName("BizModifyHis");
					iColl4MH.setName("BizModifyHisList");
					this.putDataElement2Context(iColl4MH, context);
					this.putDataElement2Context(kColl4MH, context);
				}
				
				if(!"".equals(prd_id) && ("200024".equals(prd_id) ||"400020".equals(prd_id) || "400021".equals(prd_id))){
					if(iColl4PBIT!=null && iColl4PBIT.size()>0){
						this.putDataElement2Context(iColl4PBIT, context);				
					}
					if(iColl4PBIH!=null && iColl4PBIH.size()>0){
						this.putDataElement2Context(iColl4PBIH, context);				
					}
					if(iColl4IATT!=null && iColl4IATT.size()>0){
						this.putDataElement2Context(iColl4IATT, context);				
					}
					if(iColl4IATH!=null && iColl4IATH.size()>0){
						this.putDataElement2Context(iColl4IATH, context);				
					}			
				}
				String flag_FreedomPayInfo = "";
				IndexedCollection iColl4FPPayInfo = SqlClient.queryList4IColl("queryFreedomPayInfoModifyHisList", modify_rel_serno, connection);
				if(iColl4FPPayInfo!=null&&iColl4FPPayInfo.size()>0){
					flag_FreedomPayInfo = "yes";
				}
				context.put("flag_FreedomPayInfo", flag_FreedomPayInfo);
				
				//账号信息（只查询受托支付账号）
				IndexedCollection iColl4ICAT = new IndexedCollection();
				IndexedCollection iColl4ICAH = new IndexedCollection();
				String condition4ICAT =  " where acct_attr = '04'                                               "
										+"   and modify_rel_serno = '"+modify_rel_serno+"'                      "
										+"   and acct_no in                                                     "
										+"       (select acct_no                                                "
										+"          from (select *                                              "
										+"                  from iqp_cus_acct_tmp                               "
										+"                 where acct_attr = '04'                               "
										+"                   and modify_rel_serno = '"+modify_rel_serno+"'      "
										+"                minus                                                 "
										+"                select *                                              "
										+"                  from iqp_cus_acct_his                               "
										+"                 where acct_attr = '04'                               "
										+"                   and modify_rel_serno = '"+modify_rel_serno+"'))    ";
				String condition4ICAH =  " where acct_attr = '04'                                            "
										+"   and modify_rel_serno = '"+modify_rel_serno+"'                   "
										+"   and acct_no in                                                  "
										+"       (select acct_no                                             "
										+"          from (select *                                           "
										+"                  from iqp_cus_acct_his                            "
										+"                 where acct_attr = '04'                            "
										+"                   and modify_rel_serno = '"+modify_rel_serno+"'   "
										+"                minus                                              "
										+"                select *                                           "
										+"                  from iqp_cus_acct_tmp                            "
										+"                 where acct_attr = '04'                            "
										+"                   and modify_rel_serno = '"+modify_rel_serno+"')) ";
				iColl4ICAT = dao.queryList("IqpCusAcctTmp", condition4ICAT, connection);
				iColl4ICAH = dao.queryList("IqpCusAcctHis", condition4ICAH, connection);
				
				String flag_IqpCusAcct = "no";
				if(iColl4ICAT!=null && iColl4ICAT.size()>0){
					iColl4ICAT.setName("IqpCusAcctTmpList");
					this.putDataElement2Context(iColl4ICAT, context);
					flag_IqpCusAcct = "yes";
				}
				if(iColl4ICAH!=null && iColl4ICAH.size()>0){
					iColl4ICAH.setName("IqpCusAcctHisList");
					this.putDataElement2Context(iColl4ICAH, context);	
					flag_IqpCusAcct = "yes";
				}
				context.put("flag_IqpCusAcct", flag_IqpCusAcct);
			}
			if(!"998".equals(approve_status)){
				if(!"".equals(op) && "his4iea".equals(op)){
					return "iea";
				}
			}else{
				return "reject";
			}

		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "clc";
	}
}
