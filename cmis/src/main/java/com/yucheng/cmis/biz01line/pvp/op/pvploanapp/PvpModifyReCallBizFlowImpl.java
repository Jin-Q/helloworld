package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 
*@author lisj
*@time 2015-8-21
*@description TODO
*@version 
* <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
*
 */
public class PvpModifyReCallBizFlowImpl extends CMISComponent implements
		BIZProcessInterface {
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑
		System.out.println("--------");
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑
		System.out.println("--------");
	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		String modify_rel_serno = "";
		String status ="";
		try {
			modify_rel_serno = wfiMsg.getPkValue();
			status = wfiMsg.getWfiStatus();//审批状态
			if("997".equals(status)){
				TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
				KeyedCollection kColl = dao.queryDetail("PvpBizModifyRel", modify_rel_serno, this.getConnection());//出账打回业务修改信息
				String pvp_serno = (String)kColl.getDataValue("biz_serno");
				String cont_no = (String)kColl.getDataValue("cont_no");
				String biz_cate  = (String)kColl.getDataValue("biz_cate");
				//获取流程发起时间，更新修改时间信息
				try {		
					KeyedCollection wfiBizCommentRecord = dao.queryFirst("WfiBizCommentRecord", null, "where instanceid='"+wfiMsg.getInstanceid()+"' and nodeid='102_a3'", this.getConnection());
					kColl.setDataValue("update_time", (String) wfiBizCommentRecord.getDataValue("op_time"));
					dao.update(kColl, this.getConnection());
				} catch (Exception e) {
					e.printStackTrace();
				}
				//更新出账信息审批退回修改权限
				if("016".equals(biz_cate)){//贷款展期
					
					//从协议缓存表中获取数据，更新协议信息
					KeyedCollection kColl4IEAT  = dao.queryFirst("IqpExtensionAgrTmp", null, " where modify_rel_serno='"+modify_rel_serno+"'", this.getConnection());
					String iea_serno = (String)kColl4IEAT.getDataValue("serno");
					try {
						kColl4IEAT.setName("IqpExtensionAgr");
						int count = dao.update(kColl4IEAT, this.getConnection());
						if(count!=1){
							throw new EMPException("更新协议信息出错，失败行数: " + count);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//更新展期出账信息
					try {
						KeyedCollection kColl4pvp = dao.queryDetail("IqpExtensionPvp", pvp_serno, this.getConnection());
						kColl4pvp.setDataValue("extension_date", kColl4IEAT.getDataValue("extension_date"));//展期日期
						kColl4pvp.setDataValue("extension_rate", kColl4IEAT.getDataValue("extension_rate"));//展期利率					
						kColl4pvp.setDataValue("approve_modify_right", "0");//1：打回可修改 0:打回不可修改
						int count = dao.update(kColl4pvp, this.getConnection());
						if(count!=1){
							throw new EMPException("更新出账信息出错，失败行数: " + count);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					//更新业务申请信息
					try {
						KeyedCollection kColl4app = dao.queryDetail("IqpExtensionApp", iea_serno, this.getConnection());
						String extension_date = (String) kColl4IEAT.getDataValue("extension_date");//展期到期日期
						String extension_rate = (String) kColl4IEAT.getDataValue("extension_rate");//展期利率
						kColl4app.setDataValue("extension_date", extension_date);
						kColl4app.setDataValue("extension_rate", extension_rate);
						int count = dao.update(kColl4app, this.getConnection());
						if(count!=1){
							throw new EMPException("更新业务申请信息出错，失败行数: " + count);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					try {
						//更新出账信息审批退回修改权限
						KeyedCollection kColl4PLA = dao.queryDetail("PvpLoanApp", pvp_serno, this.getConnection());
						kColl4PLA.setDataValue("approve_modify_right", "0");//1：打回可修改 0:打回不可修改
						int count = dao.update(kColl4PLA, this.getConnection());
						if(count!=1){
							throw new EMPException("更新出账信息出错，失败行数: " + count);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					
					//从协议缓存表中获取数据，更新合同信息
					KeyedCollection kColl4CLC  = dao.queryFirst("CtrLoanContTmp", null, " where modify_rel_serno='"+modify_rel_serno+"' and cont_no='"+cont_no+"'", this.getConnection());
					KeyedCollection kColl4CLCS  = dao.queryFirst("CtrLoanContSubTmp", null, " where modify_rel_serno='"+modify_rel_serno+"' and cont_no='"+cont_no+"'", this.getConnection());
					try {
						kColl4CLC.setName("CtrLoanCont");
						int count = dao.update(kColl4CLC, this.getConnection());
						if(count!=1){
							throw new EMPException("更新业务申请信息出错，失败行数: " + count);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						kColl4CLCS.setName("CtrLoanContSub");
						int count = dao.update(kColl4CLCS, this.getConnection());
						if(count!=1){
							throw new EMPException("更新业务申请信息出错，失败行数: " + count);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					//更新业务申请信息
					try {
						KeyedCollection kColl4IQP = dao.queryDetail("IqpLoanApp", (String) kColl4CLC.getDataValue("serno"), this.getConnection());
						KeyedCollection kColl4IQPS = dao.queryDetail("IqpLoanAppSub", (String) kColl4CLC.getDataValue("serno"), this.getConnection());
						String prd_id  = (String) kColl4CLC.getDataValue("prd_id");
						String cont_term = (String) kColl4CLCS.getDataValue("cont_term");//合同期限
						String term_type = (String) kColl4CLCS.getDataValue("term_type");//期限类型
						String repay_type = (String) kColl4CLCS.getDataValue("repay_type");//还款方式
						kColl4IQPS.setDataValue("apply_term", cont_term);
						kColl4IQPS.setDataValue("term_type", term_type);
						
						String pay_type = (String) kColl4CLCS.getDataValue("pay_type");//支付方式
						kColl4IQPS.setDataValue("pay_type",pay_type);
						
						String ir_accord_type = (String) kColl4CLCS.getDataValue("ir_accord_type");//利率依据方式 -01议价利率依据 -02 牌告利率依据 -03不计息 -04正常利率上浮动
						kColl4IQPS.setDataValue("ir_accord_type",ir_accord_type);
						
						if(!"03".equals(ir_accord_type)){
							if("01".equals(ir_accord_type)){
								kColl4IQPS.setDataValue("reality_ir_y",(String) kColl4CLCS.getDataValue("reality_ir_y"));//执行利率（年）
								kColl4IQPS.setDataValue("overdue_rate_y",(String) kColl4CLCS.getDataValue("overdue_rate_y"));//逾期利率（年）
								kColl4IQPS.setDataValue("default_rate_y",(String) kColl4CLCS.getDataValue("default_rate_y"));//违约利率（年）
							}else if("02".equals(ir_accord_type)){
								kColl4IQPS.setDataValue("ir_type", (String) kColl4CLCS.getDataValue("ir_type"));//利率种类
								kColl4IQPS.setDataValue("ir_adjust_type", (String) kColl4CLCS.getDataValue("ir_adjust_type"));//利率调整方式
								kColl4IQPS.setDataValue("ruling_ir", (String) kColl4CLCS.getDataValue("ruling_ir"));//基准利率（年）
								kColl4IQPS.setDataValue("ir_float_type", (String) kColl4CLCS.getDataValue("ir_float_type"));//正常利率浮动方式
								kColl4IQPS.setDataValue("ir_float_rate", (String) kColl4CLCS.getDataValue("ir_float_rate"));//利率浮动比
								kColl4IQPS.setDataValue("ir_float_point", (String) kColl4CLCS.getDataValue("ir_float_point"));//利率浮动点数
								kColl4IQPS.setDataValue("reality_ir_y", (String) kColl4CLCS.getDataValue("reality_ir_y"));//执行利率（年）
								kColl4IQPS.setDataValue("overdue_float_type", (String) kColl4CLCS.getDataValue("overdue_float_type"));//逾期利率浮动方式
								kColl4IQPS.setDataValue("overdue_rate", (String) kColl4CLCS.getDataValue("overdue_rate"));//逾期利率浮动比
								kColl4IQPS.setDataValue("overdue_point", (String) kColl4CLCS.getDataValue("overdue_point"));//逾期利率浮动点
								kColl4IQPS.setDataValue("overdue_rate_y", (String) kColl4CLCS.getDataValue("overdue_rate_y"));//逾期利率（年）
								kColl4IQPS.setDataValue("default_float_type", (String) kColl4CLCS.getDataValue("default_float_type"));//违约利率浮动方式
								kColl4IQPS.setDataValue("default_rate", (String) kColl4CLCS.getDataValue("default_rate"));//违约利率浮动比
								kColl4IQPS.setDataValue("default_point", (String) kColl4CLCS.getDataValue("default_point"));//违约利率浮动点
								kColl4IQPS.setDataValue("default_rate_y", (String) kColl4CLCS.getDataValue("default_rate_y"));//违约利率（年）
							}else{
								kColl4IQPS.setDataValue("ruling_ir", (String) kColl4CLCS.getDataValue("ruling_ir"));
								kColl4IQPS.setDataValue("ir_adjust_type", (String) kColl4CLCS.getDataValue("ir_adjust_type"));
								kColl4IQPS.setDataValue("ir_float_type", (String) kColl4CLCS.getDataValue("ir_float_type"));
								kColl4IQPS.setDataValue("ir_float_rate", (String) kColl4CLCS.getDataValue("ir_float_rate"));
								kColl4IQPS.setDataValue("ir_float_point", (String) kColl4CLCS.getDataValue("ir_float_point"));
								kColl4IQPS.setDataValue("reality_ir_y", (String) kColl4CLCS.getDataValue("reality_ir_y"));
								kColl4IQPS.setDataValue("overdue_float_type", (String) kColl4CLCS.getDataValue("overdue_float_type"));
								kColl4IQPS.setDataValue("overdue_rate", (String) kColl4CLCS.getDataValue("overdue_rate"));
								kColl4IQPS.setDataValue("overdue_point", (String) kColl4CLCS.getDataValue("overdue_point"));
								kColl4IQPS.setDataValue("overdue_rate_y", (String) kColl4CLCS.getDataValue("overdue_rate_y"));
								kColl4IQPS.setDataValue("default_float_type", (String) kColl4CLCS.getDataValue("default_float_type"));
								kColl4IQPS.setDataValue("default_rate", (String) kColl4CLCS.getDataValue("default_rate"));
								kColl4IQPS.setDataValue("default_point", (String) kColl4CLCS.getDataValue("default_point"));
								kColl4IQPS.setDataValue("default_rate_y", (String) kColl4CLCS.getDataValue("default_rate_y"));
							}
						}
						//更新担保
						KeyedCollection kColl4Query = new KeyedCollection();
						kColl4Query.addDataField("BIZ_SERNO", modify_rel_serno);
						kColl4Query.addDataField("CONT_NO", cont_no);
						SqlClient.executeUpd("updateGrtLoanRGurTmp", kColl4Query, null, null, this.getConnection());
						
						//更新还款计划登记信息
						if(repay_type!=null && !"".equals(repay_type) && "A001".equals(repay_type)){//自由还款法
							IndexedCollection iColl4IFPI = dao.queryList("IqpFreedomPayInfoTmp", " where modify_rel_serno='"+modify_rel_serno+"'", this.getConnection());
							
							String serno="";
							if(iColl4IFPI!=null && iColl4IFPI.size()>0){
								//清空还款计划登记信息
								SqlClient.executeUpd("deletePayPlan", (String) kColl4CLC.getDataValue("serno"), null, null, this.getConnection());//删除
								for(int i=0;i<iColl4IFPI.size();i++){
									KeyedCollection kColl4IFPIT = (KeyedCollection) iColl4IFPI.get(i);
									kColl4IFPIT.setName("IqpFreedomPayInfo");
									serno  = (String)kColl4IFPIT.getDataValue("serno");
									int num = dao.update(kColl4IFPIT, this.getConnection());
									if(num == 0){
										dao.insert(kColl4IFPIT, this.getConnection());
									}
								}
							}
							//保存后得重新设置期号
							/**if(!"".equals(serno)){
								IndexedCollection payPlanIColl = new IndexedCollection();
								payPlanIColl = dao.queryList("IqpFreedomPayInfo", " where serno = '"+serno+"' order by pay_date asc ", this.getConnection());
								for(int i=1;i<=payPlanIColl.size();i++){
									KeyedCollection kColl4PP = (KeyedCollection) payPlanIColl.get(i-1);
									kColl.setDataValue("dateno", i);
									dao.insert(kColl4PP, this.getConnection());
								}
							}**/
						}
						//更新账户信息
						IndexedCollection iColl4ICAT = dao.queryList("IqpCusAcctTmp", " where modify_rel_serno='"+modify_rel_serno+"'", this.getConnection());
						if(iColl4ICAT!=null && iColl4ICAT.size()>0){
							SqlClient.executeUpd("deleteIqpCusAcct", (String) kColl4CLC.getDataValue("serno"), null, null, this.getConnection());//清空原先账户信息删除
							KeyedCollection kColl4QrMRS = new KeyedCollection();
							kColl4QrMRS.addDataField("MODIFY_REL_SERNO", modify_rel_serno);
							SqlClient.executeUpd("createIqpCusAcctFromTmp", kColl4QrMRS, null, null, this.getConnection());//载入缓存信息
						}
						
						
						
						if(!"200024".equals(prd_id) && !"400020".equals(prd_id) && !"400021".equals(prd_id)){			
							kColl4IQPS.setDataValue("repay_type", (String) kColl4CLCS.getDataValue("repay_type"));//还款方式
							kColl4IQPS.setDataValue("interest_term", (String) kColl4CLCS.getDataValue("interest_term"));//计息周期
							kColl4IQPS.setDataValue("repay_term", (String) kColl4CLCS.getDataValue("repay_term"));//还款间隔周期
							kColl4IQPS.setDataValue("repay_space", (String) kColl4CLCS.getDataValue("repay_space"));//还款间隔
							kColl4IQPS.setDataValue("repay_date", (String) kColl4CLCS.getDataValue("repay_date"));//还款日
							kColl4IQPS.setDataValue("is_term", (String) kColl4CLCS.getDataValue("is_term"));//是否期供
						}else{
							kColl4IQP.setDataValue("security_rate", (String) kColl4CLC.getDataValue("security_rate"));//保证金比例
							kColl4IQP.setDataValue("security_amt", (String) kColl4CLC.getDataValue("security_amt"));//保证金金额
						}
						//更新银承汇票明细
						try {
							IndexedCollection iColl4IADT = dao.queryList("IqpAccpDetailTmp", "where modify_rel_serno='"+modify_rel_serno+"'", this.getConnection());
							if(iColl4IADT!=null && iColl4IADT.size()>0){
								for(int i=0;i<iColl4IADT.size();i++){
									KeyedCollection kColl4IADT = (KeyedCollection) iColl4IADT.get(i);
									String pk1 = (String) kColl4IADT.getDataValue("pk1");
									KeyedCollection kColl4IAD = dao.queryDetail("IqpAccpDetail", pk1, this.getConnection());
									kColl4IAD.setDataValue("clt_person", (String)kColl4IADT.getDataValue("clt_person"));
									kColl4IAD.setDataValue("term_type", (String)kColl4IADT.getDataValue("term_type"));
									kColl4IAD.setDataValue("term", (String)kColl4IADT.getDataValue("term"));
									int count = dao.update(kColl4IAD,this.getConnection());
									if(count!=1){
										throw new EMPException("执行更新银承汇票明细操作出错，失败行数: " + count);
									}
								}
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						//更新保证金信息
						try {
							//将保证金缓存信息插入保证金信息表中
							IndexedCollection iColl4PBIT = dao.queryList("PubBailInfoTmp", "where modify_rel_serno='"+modify_rel_serno+"'", this.getConnection());
							if(iColl4PBIT!=null && iColl4PBIT.size()>0){
								KeyedCollection delKColl = new KeyedCollection("delKColl");
								delKColl.addDataField("serno", (String) kColl4CLC.getDataValue("serno"));
								SqlClient.delete("deletePubBailInfoByS", delKColl, this.getConnection());
								for(int i=0;i<iColl4PBIT.size();i++){
									KeyedCollection temp = (KeyedCollection) iColl4PBIT.get(i);
									temp.setName("PubBailInfo");
									dao.insert(temp, this.getConnection());
								}
							}	
						} catch (Exception e) {
							e.printStackTrace();
						}
						//更新附加条款信息
						try {
							//将附加条款缓存信息插入附加条款信息表中
							IndexedCollection iColl4PBIT = dao.queryList("IqpAppendTermsTmp", "where modify_rel_serno='"+modify_rel_serno+"'", this.getConnection());
							if(iColl4PBIT!=null && iColl4PBIT.size()>0){
								KeyedCollection delKColl = new KeyedCollection("delKColl");
								delKColl.addDataField("serno", (String) kColl4CLC.getDataValue("serno"));
								SqlClient.delete("deleteIqpAppendTermsByS", delKColl, this.getConnection());
								for(int i=0;i<iColl4PBIT.size();i++){
									KeyedCollection temp = (KeyedCollection) iColl4PBIT.get(i);
									temp.setName("IqpAppendTerms");
									dao.insert(temp, this.getConnection());
								}
							}	
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int count = dao.update(kColl4IQP, this.getConnection());
							if(count!=1){
								throw new EMPException("执行业务申请信息修改（主表）操作出错，失败行数: " + count);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							int count4sub = dao.update(kColl4IQPS, this.getConnection());
							if(count4sub!=1){
								throw new EMPException("执行业务申请信息修改（从表）操作出错，失败行数: " + count4sub);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e.getMessage());
		}
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		System.out.println("--------");
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		// 审批否决时执行业务处理逻辑
		try {
			String modify_rel_serno = wfiMsg.getPkValue();
			String status = wfiMsg.getWfiStatus();//审批状态
			if("998".equals(status)){
				TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
				KeyedCollection kColl = dao.queryDetail("PvpBizModifyRel", modify_rel_serno, this.getConnection());//出账打回业务修改信息

				//获取流程发起时间，更新修改时间信息
				try {		
					KeyedCollection wfiBizCommentRecord = dao.queryFirst("WfiBizCommentRecord", null, "where instanceid='"+wfiMsg.getInstanceid()+"' and nodeid='102_a3'", this.getConnection());
					kColl.setDataValue("update_time", (String) wfiBizCommentRecord.getDataValue("op_time"));
					dao.update(kColl, this.getConnection());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("否决流程业务处理异常，请检查业务处理逻辑！");
		}
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
			String biz_cate = (String) kc.getDataValue("biz_cate");
			String manager_id ="";
			String manager_br_id="";
			if("016".equals(biz_cate)){
				KeyedCollection temp = dao.queryFirst("IqpExtensionAgr", null, "where agr_no='"+(String) kc.getDataValue("cont_no")+"'", this.getConnection());
				manager_id = (String)temp.getDataValue("manager_id");
				manager_br_id = (String)temp.getDataValue("manager_br_id");
			}else{
				KeyedCollection kColl4CLC = dao.queryDetail("CtrLoanCont", (String) kc.getDataValue("cont_no"), this.getConnection());
				String condition = "where is_main_manager='1' and serno='"+(String) kColl4CLC.getDataValue("serno")+"'";
				KeyedCollection iqpKColl = dao.queryFirst("CusManager", null, condition, this.getConnection());
				manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人
				manager_br_id = (String)kColl4CLC.getDataValue("manager_br_id");
			}
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
			String IsTeam="";
			KeyedCollection kColl4STO = new KeyedCollection();
			try {
				kColl4STO = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", manager_id, null, this.getConnection());
			} catch (SQLException e) {}
			if(kColl4STO != null && kColl4STO.getDataValue("team_no")!=null && !"".equals(kColl4STO.getDataValue("team_no"))){		
				IsTeam="yes";
			}else{
				IsTeam="no";
			}
			Map<String, String> param = new HashMap<String, String>();
			param.put("IsTeam", IsTeam);
			param.put("manager_br_id", manager_br_id);
			param.put("manager_id", manager_id);
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			return param;
		} catch (Exception e) {
			throw new EMPException("设置业务数据至流程变量之中出错，错误描述："+e.getMessage());
		}
	}
}
