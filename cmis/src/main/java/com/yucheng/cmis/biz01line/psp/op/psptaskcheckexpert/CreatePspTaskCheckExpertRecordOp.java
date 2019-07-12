package com.yucheng.cmis.biz01line.psp.op.psptaskcheckexpert;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.biz01line.psp.component.PspPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.util.TableModelUtil;

public class CreatePspTaskCheckExpertRecordOp extends CMISOperation {

	private final String modelId = "PspTaskCheckExpert";
	

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String task_create_mode = null;//任务生成方式
		try{
			connection = this.getConnection(context);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);
			String serno_value = null;
			String now = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				task_create_mode = (String) context.getDataValue("task_create_mode");
				now = (String) context.getDataValue("OPENDAY");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kc = dao.queryDetail(modelId, serno_value, connection);
			kc.setDataValue("task_create_date",now);
			kc.setDataValue("task_status","01");
			String condition = " where 1 = 1 ";
			String sql = "";
			if("00".equals(task_create_mode)){
				/**公司类客户*/
				condition += " and belg_line = 'BL100' ";
				//是否属集团(关联)客户
				if(kc.containsKey("is_grpmem")&&kc.getDataValue("is_grpmem")!=null&&"1".equals(kc.getDataValue("is_grpmem"))){
					condition += " and cus_id in (select cus_id from cus_com where com_grp_mode <> '9') ";
				}else if(kc.containsKey("is_grpmem")&&kc.getDataValue("is_grpmem")!=null&&"2".equals(kc.getDataValue("is_grpmem"))){
					condition += " and cus_id in (select cus_id from cus_com where com_grp_mode = '9') ";
				}
				//是否属政府融资平台
				if(kc.containsKey("is_gover_fin")&&kc.getDataValue("is_gover_fin")!=null&&"1".equals(kc.getDataValue("is_gover_fin"))){
					condition += " and cus_id in (select cus_id from cus_com where gover_fin_ter = '1') ";
				}else if(kc.containsKey("is_gover_fin")&&kc.getDataValue("is_gover_fin")!=null&&"2".equals(kc.getDataValue("is_gover_fin"))){
					condition += " and cus_id in (select cus_id from cus_com where gover_fin_ter = '2') ";
				}
				//是否属联保小组成员
				if(kc.containsKey("is_biz_area")&&kc.getDataValue("is_biz_area")!=null&&"1".equals(kc.getDataValue("is_biz_area"))){
					if(kc.containsKey("biz_area_no")&&kc.getDataValue("biz_area_no")!=null&&!"".equals(kc.getDataValue("biz_area_no"))){
						condition += " and cus_id in (select cus_id from lmt_agr_details where sub_type = '03' and lmt_status not in ('00','30') and agr_no = '"+kc.getDataValue("biz_area_no").toString()+"') ";
					}else{
						condition += " and cus_id in (select cus_id from lmt_agr_details where sub_type = '03' and lmt_status not in ('00','30')) ";
					}
				}else if(kc.containsKey("is_biz_area")&&kc.getDataValue("is_biz_area")!=null&&"2".equals(kc.getDataValue("is_biz_area"))){
					condition += " and cus_id not in (select cus_id from lmt_agr_details where sub_type = '03' and lmt_status not in ('00','30')) ";
				}
				//管理机构
				if(kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
					condition += " and manager_br_id = '"+kc.getDataValue("belg_branch").toString()+"' ";
				}
				//主管客户经理
				if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))){
					condition += " and manager_id = '"+kc.getDataValue("belg_manager_id").toString()+"' ";
				}
				//客户码
				if(kc.containsKey("cus_id")&&kc.getDataValue("cus_id")!=null&&!"".equals(kc.getDataValue("cus_id"))){
					condition += " and cus_id = '"+kc.getDataValue("cus_id").toString()+"' ";
				}
				//行业投向
				if(kc.containsKey("indus_type")&&kc.getDataValue("indus_type")!=null&&!"".equals(kc.getDataValue("indus_type"))){
					condition += " and loan_direction = '"+kc.getDataValue("indus_type").toString()+"' ";
				}
				//担保方式
				if(kc.containsKey("assure_main")&&kc.getDataValue("assure_main")!=null&&!"".equals(kc.getDataValue("assure_main"))){
					condition += " and instr('"+kc.getDataValue("assure_main").toString()+"',assure_main) > 0 ";
				}
				//业务品种
				if(kc.containsKey("biz_type")&&kc.getDataValue("biz_type")!=null&&!"".equals(kc.getDataValue("biz_type"))){
					condition += " and prd_id = '"+kc.getDataValue("biz_type").toString()+"' ";
				}
				//贷款余额开始
				if(kc.containsKey("loan_amt_begin")&&kc.getDataValue("loan_amt_begin")!=null&&!"".equals(kc.getDataValue("loan_amt_begin"))){
					condition += " and bill_bal >= "+kc.getDataValue("loan_amt_begin").toString()+" ";
				}
				//贷款余额结束
				if(kc.containsKey("loan_amt_end")&&kc.getDataValue("loan_amt_end")!=null&&!"".equals(kc.getDataValue("loan_amt_end"))){
					condition += " and bill_bal <= "+kc.getDataValue("loan_amt_end").toString()+" ";
				}
				sql = " select cus_id, count(*) as qnt,sum(bill_amt) as loan_totl_amt, sum(bill_bal) as loan_balance from psp_acc_view ";
				sql+= condition;
				sql+= " group by cus_id ";
				
				IndexedCollection icoll = TableModelUtil.buildPageData(null, this.getDataSource(context), sql);
				if(icoll!=null&&icoll.size()>0){
					for(int i=0;i<icoll.size();i++){
						KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
						KeyedCollection pspCheckTaskKc = new KeyedCollection("PspCheckTask");
						String task_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
						String sql4count = " select cus_id, count(*) as qnt,sum(bill_amt) as loan_totl_amt, sum(bill_bal) as loan_balance from psp_acc_view ";
						sql4count+= " where cus_id = '"+kcoll.getDataValue("cus_id")+"' ";
						sql4count+= " group by cus_id ";
						IndexedCollection icoll4count = TableModelUtil.buildPageData(null, this.getDataSource(context), sql4count);
						KeyedCollection kcoll4count = new KeyedCollection();
						if(icoll4count!=null&&icoll4count.size()>0){
							kcoll4count = (KeyedCollection) icoll4count.get(0);
						}
						
						pspCheckTaskKc.put("task_id",task_id);//任务编号
						pspCheckTaskKc.put("cus_id",kcoll.getDataValue("cus_id"));//客户编码
						pspCheckTaskKc.put("qnt",kcoll4count.getDataValue("qnt"));//笔数
						pspCheckTaskKc.put("loan_totl_amt",kcoll4count.getDataValue("loan_totl_amt"));//贷款总金额
						pspCheckTaskKc.put("loan_balance",kcoll4count.getDataValue("loan_balance"));//贷款总余额
						pspCheckTaskKc.put("check_type","03");//检查类型
						pspCheckTaskKc.put("task_create_date",now);//任务生成日期
						pspCheckTaskKc.put("task_request_time",kc.getDataValue("need_finish_date"));//要求完成时间
						pspCheckTaskKc.put("approve_status","000");//申请状态
						pspCheckTaskKc.put("task_type","01");//任务类型(STD_ZB_TASK_TYPE)
						KeyedCollection kcoll4cus = this.getCusInfo(kcoll.getDataValue("cus_id").toString(), context, connection);
						if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))&&kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
							pspCheckTaskKc.put("task_huser",kc.getDataValue("belg_manager_id"));//任务执行人
							pspCheckTaskKc.put("task_horg",kc.getDataValue("belg_branch"));//任务执行机构
						}else{
							pspCheckTaskKc.put("task_huser",kcoll4cus.getDataValue("cust_mgr"));//任务执行人
							pspCheckTaskKc.put("task_horg",kcoll4cus.getDataValue("main_br_id"));//任务执行机构
						}
						pspCheckTaskKc.put("manager_id",kcoll4cus.getDataValue("cust_mgr"));
						pspCheckTaskKc.put("manager_br_id",kcoll4cus.getDataValue("main_br_id"));
						pspCheckTaskKc.put("task_divis_person",kc.getDataValue("input_id"));//任务分配人
						pspCheckTaskKc.put("task_divis_org",kc.getDataValue("input_br_id"));//任务分配机构
						pspCheckTaskKc.put("check_serno",serno_value);//任务设置流水
						dao.insert(pspCheckTaskKc, connection);
					}
				}
			}else if("01".equals(task_create_mode)){
				/**小微类客户*/
				condition += " and belg_line = 'BL200' ";
				//是否属集团(关联)客户
				if(kc.containsKey("is_grpmem")&&kc.getDataValue("is_grpmem")!=null&&"1".equals(kc.getDataValue("is_grpmem"))){
					condition += " and cus_id in (select cus_id from cus_com where com_grp_mode <> '9') ";
				}else if(kc.containsKey("is_grpmem")&&kc.getDataValue("is_grpmem")!=null&&"2".equals(kc.getDataValue("is_grpmem"))){
					condition += " and cus_id in (select cus_id from cus_com where com_grp_mode = '9') ";
				}
				//是否属政府融资平台
				if(kc.containsKey("is_gover_fin")&&kc.getDataValue("is_gover_fin")!=null&&"1".equals(kc.getDataValue("is_gover_fin"))){
					condition += " and cus_id in (select cus_id from cus_com where gover_fin_ter = '1') ";
				}else if(kc.containsKey("is_gover_fin")&&kc.getDataValue("is_gover_fin")!=null&&"2".equals(kc.getDataValue("is_gover_fin"))){
					condition += " and cus_id in (select cus_id from cus_com where gover_fin_ter = '2') ";
				}
				//是否属联保小组成员
				if(kc.containsKey("is_biz_area")&&kc.getDataValue("is_biz_area")!=null&&"1".equals(kc.getDataValue("is_biz_area"))){
					if(kc.containsKey("biz_area_no")&&kc.getDataValue("biz_area_no")!=null&&!"".equals(kc.getDataValue("biz_area_no"))){
						condition += " and cus_id in (select cus_id from lmt_agr_details where sub_type = '03' and lmt_status not in ('00','30') and agr_no = '"+kc.getDataValue("biz_area_no").toString()+"') ";
					}else{
						condition += " and cus_id in (select cus_id from lmt_agr_details where sub_type = '03' and lmt_status not in ('00','30')) ";
					}
				}else if(kc.containsKey("is_biz_area")&&kc.getDataValue("is_biz_area")!=null&&"2".equals(kc.getDataValue("is_biz_area"))){
					condition += " and cus_id not in (select cus_id from lmt_agr_details where sub_type = '03' and lmt_status not in ('00','30')) ";
				}
				//是否属是否属行业成员
				if(kc.containsKey("is_indus")&&kc.getDataValue("is_indus")!=null&&"1".equals(kc.getDataValue("is_indus"))){
					if(kc.containsKey("indus_no")&&kc.getDataValue("indus_no")!=null&&!"".equals(kc.getDataValue("indus_no"))){
						condition += " and cus_id in (select t.cus_id from lmt_indus_list_mana t where exists (select 1 from lmt_indus_agr t1 where t.agr_no = t1.agr_no and t1.agr_status in ('002','004')) and agr_no = '"+kc.getDataValue("indus_no").toString()+"' and t.status = '003') ";
					}else{
						condition += " and cus_id in (select t.cus_id from lmt_indus_list_mana t where exists (select 1 from lmt_indus_agr t1 where t.agr_no = t1.agr_no and t1.agr_status in ('002','004')) and t.status = '003') ";
					}
				}else if(kc.containsKey("is_indus")&&kc.getDataValue("is_indus")!=null&&"2".equals(kc.getDataValue("is_indus"))){
					condition += " and cus_id not in (select t.cus_id from lmt_indus_list_mana t where exists (select 1 from lmt_indus_agr t1 where t.agr_no = t1.agr_no and t1.agr_status in ('002','004')) and t.status = '003') ";
				}
				//管理机构
				if(kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
					condition += " and manager_br_id = '"+kc.getDataValue("belg_branch").toString()+"' ";
				}
				//主管客户经理
				if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))){
					condition += " and manager_id = '"+kc.getDataValue("belg_manager_id").toString()+"' ";
				}
				//客户码
				if(kc.containsKey("cus_id")&&kc.getDataValue("cus_id")!=null&&!"".equals(kc.getDataValue("cus_id"))){
					condition += " and cus_id = '"+kc.getDataValue("cus_id").toString()+"' ";
				}
				//行业投向
				if(kc.containsKey("indus_type")&&kc.getDataValue("indus_type")!=null&&!"".equals(kc.getDataValue("indus_type"))){
					condition += " and loan_direction = '"+kc.getDataValue("indus_type").toString()+"' ";
				}
				//担保方式
				if(kc.containsKey("assure_main")&&kc.getDataValue("assure_main")!=null&&!"".equals(kc.getDataValue("assure_main"))){
					condition += " and instr('"+kc.getDataValue("assure_main").toString()+"',assure_main) > 0 ";
				}
				//业务品种
				if(kc.containsKey("biz_type")&&kc.getDataValue("biz_type")!=null&&!"".equals(kc.getDataValue("biz_type"))){
					condition += " and prd_id = '"+kc.getDataValue("biz_type").toString()+"' ";
				}
				//贷款余额开始
				if(kc.containsKey("loan_amt_begin")&&kc.getDataValue("loan_amt_begin")!=null&&!"".equals(kc.getDataValue("loan_amt_begin"))){
					condition += " and bill_bal >= "+kc.getDataValue("loan_amt_begin").toString()+" ";
				}
				//贷款余额结束
				if(kc.containsKey("loan_amt_end")&&kc.getDataValue("loan_amt_end")!=null&&!"".equals(kc.getDataValue("loan_amt_end"))){
					condition += " and bill_bal <= "+kc.getDataValue("loan_amt_end").toString()+" ";
				}
				sql = " select cus_id, count(*) as qnt,sum(bill_amt) as loan_totl_amt, sum(bill_bal) as loan_balance from psp_acc_view ";
				sql+= condition;
				//专项检查小微客户融资100万元以下的客户不生成贷后任务
				sql+= " and cus_id not in (select cus_id from (select cus_id, sum(bill_amt) bill_amt from psp_acc_view where belg_line = 'BL200' group by cus_id) where bill_amt < 1000000) ";
				sql+= " group by cus_id ";
				
				IndexedCollection icoll = TableModelUtil.buildPageData(null, this.getDataSource(context), sql);
				if(icoll!=null&&icoll.size()>0){
					for(int i=0;i<icoll.size();i++){
						KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
						KeyedCollection pspCheckTaskKc = new KeyedCollection("PspCheckTask");
						String task_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
						
						String sql4count = " select cus_id, count(*) as qnt,sum(bill_amt) as loan_totl_amt, sum(bill_bal) as loan_balance from psp_acc_view ";
						sql4count+= " where cus_id = '"+kcoll.getDataValue("cus_id")+"' ";
						sql4count+= " group by cus_id ";
						IndexedCollection icoll4count = TableModelUtil.buildPageData(null, this.getDataSource(context), sql4count);
						KeyedCollection kcoll4count = new KeyedCollection();
						if(icoll4count!=null&&icoll4count.size()>0){
							kcoll4count = (KeyedCollection) icoll4count.get(0);
						}
						
						pspCheckTaskKc.put("task_id",task_id);//任务编号
						pspCheckTaskKc.put("cus_id",kcoll.getDataValue("cus_id"));//客户编码
						pspCheckTaskKc.put("qnt",kcoll4count.getDataValue("qnt"));//笔数
						pspCheckTaskKc.put("loan_totl_amt",kcoll4count.getDataValue("loan_totl_amt"));//贷款总金额
						pspCheckTaskKc.put("loan_balance",kcoll4count.getDataValue("loan_balance"));//贷款总余额
						pspCheckTaskKc.put("check_type","03");//检查类型
						pspCheckTaskKc.put("task_create_date",now);//任务生成日期
						pspCheckTaskKc.put("task_request_time",kc.getDataValue("need_finish_date"));//要求完成时间
						pspCheckTaskKc.put("approve_status","000");//申请状态
						pspCheckTaskKc.put("task_type","02");//任务类型(STD_ZB_TASK_TYPE)
						KeyedCollection kcoll4cus = this.getCusInfo(kcoll.getDataValue("cus_id").toString(), context, connection);
						if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))&&kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
							pspCheckTaskKc.put("task_huser",kc.getDataValue("belg_manager_id"));//任务执行人
							pspCheckTaskKc.put("task_horg",kc.getDataValue("belg_branch"));//任务执行机构
						}else{
							pspCheckTaskKc.put("task_huser",kcoll4cus.getDataValue("cust_mgr"));//任务执行人
							pspCheckTaskKc.put("task_horg",kcoll4cus.getDataValue("main_br_id"));//任务执行机构
						}
						pspCheckTaskKc.put("manager_id",kcoll4cus.getDataValue("cust_mgr"));
						pspCheckTaskKc.put("manager_br_id",kcoll4cus.getDataValue("main_br_id"));
						pspCheckTaskKc.put("task_divis_person",kc.getDataValue("input_id"));//任务分配人
						pspCheckTaskKc.put("task_divis_org",kc.getDataValue("input_br_id"));//任务分配机构
						pspCheckTaskKc.put("check_serno",serno_value);//任务设置流水
						dao.insert(pspCheckTaskKc, connection);
					}
				}
			}else if("02".equals(task_create_mode)){
				/**个人类客户*/
				condition += " and belg_line = 'BL300' ";
				//是否属集团(关联)客户
				if(kc.containsKey("is_grpmem")&&kc.getDataValue("is_grpmem")!=null&&"1".equals(kc.getDataValue("is_grpmem"))){
					condition += " and cus_id in (select t.cus_id_rel from cus_com_manager t where t.com_mrg_typ = '01' and t.cus_id in (select cus_id from cus_com where com_grp_mode <> '9')) ";
				}else if(kc.containsKey("is_grpmem")&&kc.getDataValue("is_grpmem")!=null&&"2".equals(kc.getDataValue("is_grpmem"))){
					condition += " and cus_id not in (select t.cus_id_rel from cus_com_manager t where t.com_mrg_typ = '01' and t.cus_id in (select cus_id from cus_com where com_grp_mode <> '9')) ";
				}
				//是否属联保小组成员
				if(kc.containsKey("is_biz_area")&&kc.getDataValue("is_biz_area")!=null&&"1".equals(kc.getDataValue("is_biz_area"))){
					if(kc.containsKey("biz_area_no")&&kc.getDataValue("biz_area_no")!=null&&!"".equals(kc.getDataValue("biz_area_no"))){
						condition += " and cus_id in (select cus_id from lmt_agr_details where sub_type = '03' and lmt_status not in ('00','30') and agr_no = '"+kc.getDataValue("biz_area_no").toString()+"') ";
					}else{
						condition += " and cus_id in (select cus_id from lmt_agr_details where sub_type = '03' and lmt_status not in ('00','30')) ";
					}
				}else if(kc.containsKey("is_biz_area")&&kc.getDataValue("is_biz_area")!=null&&"2".equals(kc.getDataValue("is_biz_area"))){
					condition += " and cus_id not in (select cus_id from lmt_agr_details where sub_type = '03' and lmt_status not in ('00','30')) ";
				}
				
				//是否属圈商成员
				if(kc.containsKey("is_biz_circle")&&kc.getDataValue("is_biz_circle")!=null&&"1".equals(kc.getDataValue("is_biz_circle"))){
					if(kc.containsKey("biz_circle_no")&&kc.getDataValue("biz_circle_no")!=null&&!"".equals(kc.getDataValue("biz_circle_no"))){
						condition += " and cus_id in (select t.cus_id from lmt_name_list t where exists (select 1 from lmt_agr_biz_area t1 where t.agr_no = t1.agr_no and t1.agr_no = '"+kc.getDataValue("biz_circle_no").toString()+"' and t1.agr_status in ('002','004'))) ";
					}else{
						condition += " and cus_id in (select t.cus_id from lmt_name_list t where exists (select 1 from lmt_agr_biz_area t1 where t.agr_no = t1.agr_no and t1.agr_status in ('002','004'))) ";
					}
				}else if(kc.containsKey("is_biz_circle")&&kc.getDataValue("is_biz_circle")!=null&&"2".equals(kc.getDataValue("is_biz_circle"))){
					condition += " and cus_id not in (select t.cus_id from lmt_name_list t where exists (select 1 from lmt_agr_biz_area t1 where t.agr_no = t1.agr_no and t1.agr_status in ('002','004'))) ";
				}
				//客户编号
				if(kc.containsKey("cus_id")&&kc.getDataValue("cus_id")!=null&&!"".equals(kc.getDataValue("cus_id"))){
					condition += " and cus_id = '"+kc.getDataValue("cus_id").toString()+"' ";
				}
				//管理机构
				if(kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
					condition += " and manager_br_id = '"+kc.getDataValue("belg_branch").toString()+"' ";
				}
				//主管客户经理
				if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))){
					condition += " and manager_id = '"+kc.getDataValue("belg_manager_id").toString()+"' ";
				}
				//行业投向
				if(kc.containsKey("indus_type")&&kc.getDataValue("indus_type")!=null&&!"".equals(kc.getDataValue("indus_type"))){
					condition += " and loan_direction = '"+kc.getDataValue("indus_type").toString()+"' ";
				}
				//担保方式
				if(kc.containsKey("assure_main")&&kc.getDataValue("assure_main")!=null&&!"".equals(kc.getDataValue("assure_main"))){
					condition += " and instr('"+kc.getDataValue("assure_main").toString()+"',assure_main) > 0 ";
				}
				//业务品种
				if(kc.containsKey("biz_type")&&kc.getDataValue("biz_type")!=null&&!"".equals(kc.getDataValue("biz_type"))){
					condition += " and prd_id = '"+kc.getDataValue("biz_type").toString()+"' ";
				}
				//贷款余额开始
				if(kc.containsKey("loan_amt_begin")&&kc.getDataValue("loan_amt_begin")!=null&&!"".equals(kc.getDataValue("loan_amt_begin"))){
					condition += " and bill_bal >= "+kc.getDataValue("loan_amt_begin").toString()+" ";
				}
				//贷款余额结束
				if(kc.containsKey("loan_amt_end")&&kc.getDataValue("loan_amt_end")!=null&&!"".equals(kc.getDataValue("loan_amt_end"))){
					condition += " and bill_bal <= "+kc.getDataValue("loan_amt_end").toString()+" ";
				}
				sql = " select cus_id, count(*) as qnt,sum(bill_amt) as loan_totl_amt, sum(bill_bal) as loan_balance from psp_acc_view ";
				sql+= condition;
				//专项检查零售客户房产按揭及汽车按揭、个人经营性贷款100万元以下的贷款不生成贷后检查任务
				sql+= " and cus_id not in (select cus_id from psp_acc_view where prd_id in ('100033','100028','100029'))";
				sql+= " and cus_id not in (select cus_id from (select cus_id, sum(bill_amt) bill_amt, min(case when instr('100035,100036,100037,100038,100034,100039,100040', prd_id) > 0 then '001' else '002' end) as cus_type from psp_acc_view where belg_line = 'BL300' group by cus_id) where cus_type = '001' and bill_amt < 1000000)";
				sql+= " group by cus_id ";
				
				IndexedCollection icoll = TableModelUtil.buildPageData(null, this.getDataSource(context), sql);
				String cus_type = "";
				if(icoll!=null&&icoll.size()>0){
					for(int i=0;i<icoll.size();i++){
						KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
						KeyedCollection pspCheckTaskKc = new KeyedCollection("PspCheckTask");
						String task_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
						
						String sql4count = " select cus_id, count(*) as qnt,sum(bill_amt) as loan_totl_amt, sum(bill_bal) as loan_balance from psp_acc_view ";
						sql4count+= " where cus_id = '"+kcoll.getDataValue("cus_id")+"' ";
						sql4count+= " group by cus_id ";
						IndexedCollection icoll4count = TableModelUtil.buildPageData(null, this.getDataSource(context), sql4count);
						KeyedCollection kcoll4count = new KeyedCollection();
						if(icoll4count!=null&&icoll4count.size()>0){
							kcoll4count = (KeyedCollection) icoll4count.get(0);
						}
						
						pspCheckTaskKc.put("task_id",task_id);//任务编号
						pspCheckTaskKc.put("cus_id",kcoll.getDataValue("cus_id"));//客户编码
						cus_type = this.getCusType(kcoll.getDataValue("cus_id").toString(), context, connection);
						pspCheckTaskKc.put("psp_cus_type",cus_type);//客户类型
						pspCheckTaskKc.put("qnt",kcoll4count.getDataValue("qnt"));//笔数
						pspCheckTaskKc.put("loan_totl_amt",kcoll4count.getDataValue("loan_totl_amt"));//贷款总金额
						pspCheckTaskKc.put("loan_balance",kcoll4count.getDataValue("loan_balance"));//贷款总余额
						pspCheckTaskKc.put("check_type","03");//检查类型
						pspCheckTaskKc.put("task_create_date",now);//任务生成日期
						pspCheckTaskKc.put("task_request_time",kc.getDataValue("need_finish_date"));//要求完成时间
						pspCheckTaskKc.put("approve_status","000");//申请状态
						pspCheckTaskKc.put("task_type","03");//任务类型(STD_ZB_TASK_TYPE)
						KeyedCollection kcoll4cus = this.getCusInfo(kcoll.getDataValue("cus_id").toString(), context, connection);
						if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))&&kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
							pspCheckTaskKc.put("task_huser",kc.getDataValue("belg_manager_id"));//任务执行人
							pspCheckTaskKc.put("task_horg",kc.getDataValue("belg_branch"));//任务执行机构
						}else{
							pspCheckTaskKc.put("task_huser",kcoll4cus.getDataValue("cust_mgr"));//任务执行人
							pspCheckTaskKc.put("task_horg",kcoll4cus.getDataValue("main_br_id"));//任务执行机构
						}
						pspCheckTaskKc.put("manager_id",kcoll4cus.getDataValue("cust_mgr"));
						pspCheckTaskKc.put("manager_br_id",kcoll4cus.getDataValue("main_br_id"));
						pspCheckTaskKc.put("task_divis_person",kc.getDataValue("input_id"));//任务分配人
						pspCheckTaskKc.put("task_divis_org",kc.getDataValue("input_br_id"));//任务分配机构
						pspCheckTaskKc.put("check_serno",serno_value);//任务设置流水
						dao.insert(pspCheckTaskKc, connection);
					}
				}
			}else if("03".equals(task_create_mode)){
				/**集团（关联）客户*/
				//管理机构
				if(kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
					condition += " and manager_br_id = '"+kc.getDataValue("belg_branch").toString()+"' ";
				}
				//主管客户经理
				if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))){
					condition += " and manager_id = '"+kc.getDataValue("belg_manager_id").toString()+"' ";
				}
				//客户码
				if(kc.containsKey("grp_no")&&kc.getDataValue("grp_no")!=null&&!"".equals(kc.getDataValue("grp_no"))){
					condition += " and grp_no = '"+kc.getDataValue("grp_no").toString()+"' ";
				}
				condition += " and agr_status in ('002','004')";
				IndexedCollection icoll = dao.queryList("LmtAgrGrp", condition, connection);
				if(icoll!=null&&icoll.size()>0){
					for(int i=0;i<icoll.size();i++){
						KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
						KeyedCollection pspCheckTaskKc = new KeyedCollection("PspCheckTask");
						String task_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
						pspCheckTaskKc.put("task_id",task_id);//任务编号
						pspCheckTaskKc.put("grp_no",kcoll.getDataValue("grp_no"));//客户编码
						pspCheckTaskKc.put("check_type","03");//检查类型
						pspCheckTaskKc.put("task_create_date",now);//任务生成日期
						pspCheckTaskKc.put("task_request_time",kc.getDataValue("need_finish_date"));//要求完成时间
						pspCheckTaskKc.put("approve_status","000");//申请状态
						pspCheckTaskKc.put("task_type","04");//任务类型(STD_ZB_TASK_TYPE)
						KeyedCollection kcoll4cus = this.getCusGrpInfo(kcoll.getDataValue("grp_no").toString(), context, connection);
						if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))&&kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
							pspCheckTaskKc.put("task_huser",kc.getDataValue("belg_manager_id"));//任务执行人
							pspCheckTaskKc.put("task_horg",kc.getDataValue("belg_branch"));//任务执行机构
						}else{
							
							pspCheckTaskKc.put("task_huser",kcoll4cus.getDataValue("cust_mgr"));//任务执行人
							pspCheckTaskKc.put("task_horg",kcoll4cus.getDataValue("main_br_id"));//任务执行机构
						}
						pspCheckTaskKc.put("manager_id",kcoll4cus.getDataValue("cust_mgr"));
						pspCheckTaskKc.put("manager_br_id",kcoll4cus.getDataValue("main_br_id"));
						pspCheckTaskKc.put("task_divis_person",kc.getDataValue("input_id"));//任务分配人
						pspCheckTaskKc.put("task_divis_org",kc.getDataValue("input_br_id"));//任务分配机构
						
						
						CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
						CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
						IndexedCollection grpMemberIc = service.getGrpMemberByGrpNo(kcoll.getDataValue("grp_no").toString(), connection);
						
						IndexedCollection icoll4mem = new IndexedCollection();
						KeyedCollection kcoll4mem = new KeyedCollection();
						String sql4mem = "";
						BigDecimal qntAll = new BigDecimal(0);
						BigDecimal loanTotlAmtAll = new BigDecimal(0);
						BigDecimal loanBalanceAll = new BigDecimal(0);
						BigDecimal qnt = new BigDecimal(0);
						BigDecimal loanTotlAmt = new BigDecimal(0);
						BigDecimal loanBalance = new BigDecimal(0);
							
						for(int j=0;j<grpMemberIc.size();j++){
							KeyedCollection kcTemp  = (KeyedCollection) grpMemberIc.get(j);
							String cus_id = (String) kcTemp.getDataValue("cus_id");
							KeyedCollection tranc_kColl = new KeyedCollection("TransValue");
							PspPubComponent cmisComponent = (PspPubComponent)CMISComponentFactory
							.getComponentFactoryInstance().getComponentInstance("PspPubComponent",context,connection);
							tranc_kColl.addDataField("cus_id", cus_id);
							KeyedCollection res1 = cmisComponent.delReturnSql("checkTrueByCusId", tranc_kColl);
							boolean check = Boolean.parseBoolean(res1.getDataValue("results").toString());
							if(!check){
								continue;
							}
							
							sql4mem = " select cus_id, count(*) as qnt,sum(bill_amt) as loan_totl_amt, sum(bill_bal) as loan_balance from psp_acc_view ";
							sql4mem+= " where cus_id = '"+cus_id+"' ";
							sql4mem+= " group by cus_id ";
							icoll4mem = TableModelUtil.buildPageData(null, this.getDataSource(context), sql4mem);
							if(icoll4mem!=null&&icoll4mem.size()>0){
								kcoll4mem = (KeyedCollection) icoll4mem.get(0);
								if(kcoll4mem!=null){
									qnt = new BigDecimal(kcoll4mem.getDataValue("qnt")+"");
									loanTotlAmt = new BigDecimal(kcoll4mem.getDataValue("loan_totl_amt")+"");
									loanBalance = new BigDecimal(kcoll4mem.getDataValue("loan_balance")+"");
									qntAll = qntAll.add(qnt);
									loanTotlAmtAll = loanTotlAmtAll.add(loanTotlAmt);
									loanBalanceAll = loanBalanceAll.add(loanBalance);
								}
							}
							KeyedCollection pspCheckTaskKc1 = new KeyedCollection("PspCheckTask");
							String task_id_sub = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
							pspCheckTaskKc1.addDataField("task_id",task_id_sub);
							pspCheckTaskKc1.addDataField("cus_id",cus_id);
							pspCheckTaskKc1.addDataField("grp_task_id",task_id);
							pspCheckTaskKc1.addDataField("grp_no",kcoll.getDataValue("grp_no").toString());
							pspCheckTaskKc1.addDataField("check_type","03");
							pspCheckTaskKc1.addDataField("task_create_date",now);
							pspCheckTaskKc1.addDataField("task_request_time",kc.getDataValue("need_finish_date"));
							pspCheckTaskKc1.addDataField("approve_status","000");
							
							pspCheckTaskKc1.addDataField("task_type",this.getTaskType(cus_id, context, connection));
							pspCheckTaskKc1.put("qnt",qnt);//笔数
							pspCheckTaskKc1.put("loan_totl_amt",loanTotlAmt);//贷款总金额
							pspCheckTaskKc1.put("loan_balance",loanBalance);//贷款总余额
							if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))&&kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
								pspCheckTaskKc1.put("task_huser",kc.getDataValue("belg_manager_id"));//任务执行人
								pspCheckTaskKc1.put("task_horg",kc.getDataValue("belg_branch"));//任务执行机构
							}else{
								pspCheckTaskKc1.put("task_huser",kcoll4cus.getDataValue("cust_mgr"));//任务执行人
								pspCheckTaskKc1.put("task_horg",kcoll4cus.getDataValue("main_br_id"));//任务执行机构
							}
							pspCheckTaskKc.put("manager_id",kcoll4cus.getDataValue("cust_mgr"));
							pspCheckTaskKc.put("manager_br_id",kcoll4cus.getDataValue("main_br_id"));
							
							pspCheckTaskKc1.put("task_divis_person",kc.getDataValue("input_id"));//任务分配人
							pspCheckTaskKc1.put("task_divis_org",kc.getDataValue("input_br_id"));//任务分配机构
							pspCheckTaskKc1.put("check_serno",serno_value);//任务设置流水
							dao.insert(pspCheckTaskKc1, connection);
						}
						pspCheckTaskKc.put("qnt",qntAll);//笔数
						pspCheckTaskKc.put("loan_totl_amt",loanTotlAmtAll);//贷款总金额
						pspCheckTaskKc.put("loan_balance",loanBalanceAll);//贷款总余额
						pspCheckTaskKc.put("check_serno",serno_value);//任务设置流水
						if(qntAll.compareTo(new BigDecimal("0"))>0){
							dao.insert(pspCheckTaskKc, connection);
						}
					}
				}
			}else if("04".equals(task_create_mode)){
				/**合作方客户*/
				String coop_type = kc.getDataValue("coop_type").toString();
				if("011".equals(coop_type)){//担保公司
					//责任机构
					if(kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
						condition += " and manager_br_id = '"+kc.getDataValue("belg_branch").toString()+"' ";
					}
					//责任人
					if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))){
						condition += " and manager_id = '"+kc.getDataValue("belg_manager_id").toString()+"' ";
					}
					//合作方客户码
					if(kc.containsKey("agr_no")&&kc.getDataValue("agr_no")!=null&&!"".equals(kc.getDataValue("agr_no"))){
						condition += " and cus_id = '"+kc.getDataValue("agr_no").toString()+"' ";
					}
					//授信总额开始
					if(kc.containsKey("loan_amt_begin")&&kc.getDataValue("loan_amt_begin")!=null&&!"".equals(kc.getDataValue("loan_amt_begin"))){
						condition += " and fin_totl_limit >= "+kc.getDataValue("loan_amt_begin").toString()+" ";
					}
					//授信总额结束
					if(kc.containsKey("loan_amt_end")&&kc.getDataValue("loan_amt_end")!=null&&!"".equals(kc.getDataValue("loan_amt_end"))){
						condition += " and fin_totl_limit <= "+kc.getDataValue("loan_amt_end").toString()+" ";
					}
					condition += " and agr_status in ('002','004')";
					IndexedCollection icoll = dao.queryList("LmtAgrFinGuar", condition, connection);
					String sql4db = "";
					IndexedCollection icoll4db = new IndexedCollection();
					if(icoll!=null&&icoll.size()>0){
						for(int i=0;i<icoll.size();i++){
							KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
							KeyedCollection pspCheckTaskKc4db = new KeyedCollection("PspCheckTask");
							sql4db = " select count(*) as qnt,sum(bill_amt) as loan_totl_amt, sum(bill_bal) as loan_balance from psp_acc_view ";
							sql4db+= " where cont_no in (select t.cont_no from grt_loan_r_gur t where exists (select 1 from grt_guaranty_re a, grt_guarantee b where t.guar_cont_no = a.guar_cont_no and a.guaranty_id = b.guar_id and b.cus_id = '"+kcoll.getDataValue("cus_id")+"') and t.cont_no is not null) ";
							icoll4db = TableModelUtil.buildPageData(null, this.getDataSource(context), sql4db);
													
							if(icoll4db!=null&&icoll4db.size()>0){//担保
								KeyedCollection kcoll4db = (KeyedCollection) icoll4db.get(0);
								if(kcoll4db!=null&&kcoll4db.getDataValue("qnt")!=null&&!"0".equals(kcoll4db.getDataValue("qnt"))){
									String task_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
									pspCheckTaskKc4db.put("task_id",task_id);//任务编号
									pspCheckTaskKc4db.put("cus_id",kcoll.getDataValue("cus_id"));//客户编码
									pspCheckTaskKc4db.put("qnt",kcoll4db.getDataValue("qnt"));//笔数
									pspCheckTaskKc4db.put("loan_totl_amt",kcoll4db.getDataValue("loan_totl_amt"));//贷款总金额
									pspCheckTaskKc4db.put("loan_balance",kcoll4db.getDataValue("loan_balance"));//贷款总余额
									pspCheckTaskKc4db.put("check_type","03");//检查类型
									pspCheckTaskKc4db.put("task_create_date",now);//任务生成日期
									pspCheckTaskKc4db.put("task_request_time",kc.getDataValue("need_finish_date"));//要求完成时间
									pspCheckTaskKc4db.put("approve_status","000");//申请状态
									pspCheckTaskKc4db.put("task_type","06");//任务类型(STD_ZB_TASK_TYPE)
									KeyedCollection kcoll4cus = this.getCusInfo(kcoll.getDataValue("cus_id").toString(), context, connection);
									if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))&&kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
										pspCheckTaskKc4db.put("task_huser",kc.getDataValue("belg_manager_id"));//任务执行人
										pspCheckTaskKc4db.put("task_horg",kc.getDataValue("belg_branch"));//任务执行机构
									}else{
										
										pspCheckTaskKc4db.put("task_huser",kcoll4cus.getDataValue("cust_mgr"));//任务执行人
										pspCheckTaskKc4db.put("task_horg",kcoll4cus.getDataValue("main_br_id"));//任务执行机构
									}
									pspCheckTaskKc4db.put("manager_id",kcoll4cus.getDataValue("cust_mgr"));
									pspCheckTaskKc4db.put("manager_br_id",kcoll4cus.getDataValue("main_br_id"));
									pspCheckTaskKc4db.put("task_divis_person",kc.getDataValue("input_id"));//任务分配人
									pspCheckTaskKc4db.put("task_divis_org",kc.getDataValue("input_br_id"));//任务分配机构
									pspCheckTaskKc4db.put("check_serno",serno_value);//任务设置流水
									dao.insert(pspCheckTaskKc4db, connection);
								}
							}
						}
					}
				}else{//合作方客户
					//合作方类型
					if(kc.containsKey("coop_type")&&kc.getDataValue("coop_type")!=null&&!"".equals(kc.getDataValue("coop_type"))){
						condition += " and coop_type = '"+kc.getDataValue("coop_type").toString()+"' ";
					}				
					//责任机构
					if(kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
						condition += " and manager_br_id = '"+kc.getDataValue("belg_branch").toString()+"' ";
					}
					//责任人
					if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))){
						condition += " and manager_id = '"+kc.getDataValue("belg_manager_id").toString()+"' ";
					}
					//合作方客户码
					if(kc.containsKey("agr_no")&&kc.getDataValue("agr_no")!=null&&!"".equals(kc.getDataValue("agr_no"))){
						condition += " and cus_id = '"+kc.getDataValue("agr_no").toString()+"' ";
					}
					//授信总额开始
					if(kc.containsKey("loan_amt_begin")&&kc.getDataValue("loan_amt_begin")!=null&&!"".equals(kc.getDataValue("loan_amt_begin"))){
						condition += " and lmt_totl_amt >= "+kc.getDataValue("loan_amt_begin").toString()+" ";
					}
					//授信总额结束
					if(kc.containsKey("loan_amt_end")&&kc.getDataValue("loan_amt_end")!=null&&!"".equals(kc.getDataValue("loan_amt_end"))){
						condition += " and lmt_totl_amt <= "+kc.getDataValue("loan_amt_end").toString()+" ";
					}
					condition += " and coop_type<>'010' and agr_status in ('002','004')";
					IndexedCollection icoll = dao.queryList("LmtAgrJointCoop", condition, connection);
					String sql4hzf = "";
					IndexedCollection icoll4hzf = new IndexedCollection();
					if(icoll!=null&&icoll.size()>0){
						for(int i=0;i<icoll.size();i++){
							KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
							KeyedCollection pspCheckTaskKc4hzf = new KeyedCollection("PspCheckTask");
							sql4hzf = " select count(*) as qnt,sum(bill_amt) as loan_totl_amt, sum(bill_bal) as loan_balance from psp_acc_view ";
							sql4hzf+= " where cont_no in (select cont_no from r_bus_lmtcredit_info where agr_no = '"+kcoll.getDataValue("agr_no")+"') ";
							icoll4hzf = TableModelUtil.buildPageData(null, this.getDataSource(context), sql4hzf);
													
							if(icoll4hzf!=null&&icoll4hzf.size()>0){//合作方
								KeyedCollection kcoll4hzf = (KeyedCollection) icoll4hzf.get(0);
								if(kcoll4hzf!=null&&kcoll4hzf.getDataValue("qnt")!=null&&!"0".equals(kcoll4hzf.getDataValue("qnt"))){
									String task_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
									pspCheckTaskKc4hzf.put("task_id",task_id);//任务编号
									pspCheckTaskKc4hzf.put("cus_id",kcoll.getDataValue("cus_id"));//客户编码
									pspCheckTaskKc4hzf.put("qnt",kcoll4hzf.getDataValue("qnt"));//笔数
									pspCheckTaskKc4hzf.put("loan_totl_amt",kcoll4hzf.getDataValue("loan_totl_amt"));//贷款总金额
									pspCheckTaskKc4hzf.put("loan_balance",kcoll4hzf.getDataValue("loan_balance"));//贷款总余额
									pspCheckTaskKc4hzf.put("check_type","03");//检查类型
									pspCheckTaskKc4hzf.put("task_create_date",now);//任务生成日期
									pspCheckTaskKc4hzf.put("task_request_time",kc.getDataValue("need_finish_date"));//要求完成时间
									pspCheckTaskKc4hzf.put("approve_status","000");//申请状态
									pspCheckTaskKc4hzf.put("task_type","05");//任务类型(STD_ZB_TASK_TYPE)
									KeyedCollection kcoll4cus = this.getCusInfo(kcoll.getDataValue("cus_id").toString(), context, connection);
									if(kc.containsKey("belg_manager_id")&&kc.getDataValue("belg_manager_id")!=null&&!"".equals(kc.getDataValue("belg_manager_id"))&&kc.containsKey("belg_branch")&&kc.getDataValue("belg_branch")!=null&&!"".equals(kc.getDataValue("belg_branch"))){
										pspCheckTaskKc4hzf.put("task_huser",kc.getDataValue("belg_manager_id"));//任务执行人
										pspCheckTaskKc4hzf.put("task_horg",kc.getDataValue("belg_branch"));//任务执行机构
									}else{
										
										pspCheckTaskKc4hzf.put("task_huser",kcoll4cus.getDataValue("cust_mgr"));//任务执行人
										pspCheckTaskKc4hzf.put("task_horg",kcoll4cus.getDataValue("main_br_id"));//任务执行机构
									}
									pspCheckTaskKc4hzf.put("manager_id",kcoll4cus.getDataValue("cust_mgr"));
									pspCheckTaskKc4hzf.put("manager_br_id",kcoll4cus.getDataValue("main_br_id"));
									pspCheckTaskKc4hzf.put("task_divis_person",kc.getDataValue("input_id"));//任务分配人
									pspCheckTaskKc4hzf.put("task_divis_org",kc.getDataValue("input_br_id"));//任务分配机构
									pspCheckTaskKc4hzf.put("check_serno",serno_value);//任务设置流水
									dao.insert(pspCheckTaskKc4hzf, connection);
								}
							}
						}
					}
				}
			}
			//更新任务生成日期和任务状态两个字段。
			dao.update(kc, connection);
			context.addDataField("flag","success");
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
	
	
	protected String getTaskType(String cus_id,Context context,Connection connection){
		String task_type = null;
		//调用客户模块接口
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		CusServiceInterface service;
		try {
			service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			//从cus_base表中获得客户基本信息
			CusBase cus = service.getCusBaseByCusId(cus_id, context, connection);
			String cus_type = cus.getCusType();//客户类型
			String belg_line = cus.getBelgLine();//所属条线
			if("A2".equals(cus_type)){
				task_type="05";
			}else{
				if("BL100".equals(belg_line)){//公司
					task_type="01";
				}else if("BL200".equals(belg_line)){//小微
					task_type="02";
				}else if("BL300".equals(belg_line)){//个人
					task_type="03";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return task_type;
	}
	
	protected KeyedCollection getCusInfo(String cus_id,Context context,Connection connection){
		KeyedCollection kcoll = new KeyedCollection("CusBase");
		//调用客户模块接口
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		CusServiceInterface service;
		try {
			service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			//从cus_base表中获得客户基本信息
			CusBase cus = service.getCusBaseByCusId(cus_id, context, connection);
			String mainBrId = cus.getMainBrId();//主管机构
			String custMgr = cus.getCustMgr();//主管客户经理
			kcoll.put("main_br_id", mainBrId);
			kcoll.put("cust_mgr", custMgr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kcoll;
	}
	protected KeyedCollection getCusGrpInfo(String grp_no,Context context,Connection connection){
		KeyedCollection kcoll = new KeyedCollection("CusGrpInfo");
		//调用客户模块接口
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		CusServiceInterface service;
		try {
			service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			//从cus_base表中获得客户基本信息
			CusGrpInfo cus = service.getCusGrpInfoByGrpNo(grp_no, context, connection);
			String mainBrId = cus.getManagerBrId();//主管机构
			String custMgr = cus.getManagerId();//主管客户经理
			kcoll.put("main_br_id", mainBrId);
			kcoll.put("cust_mgr", custMgr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kcoll;
	}
	protected String getCusType(String cus_id,Context context,Connection connection) throws EMPException{
		String cus_type = "002";
		String condition = "";
		TableModelDAO dao = this.getTableModelDAO(context);
		try {
			condition += " where cus_id = '"+cus_id+"' and acc_status in ('1','6')";
			IndexedCollection icoll = dao.queryList("AccLoan", condition, connection);
			if(icoll!=null&&icoll.size()>0){
				for(int i=0;i<icoll.size();i++){
					KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
					if(kcoll!=null&&("100035".equals(kcoll.getDataValue("prd_id"))||"100036".equals(kcoll.getDataValue("prd_id"))||"100037".equals(kcoll.getDataValue("prd_id"))||"100038".equals(kcoll.getDataValue("prd_id"))||"100034".equals(kcoll.getDataValue("prd_id"))||"100039".equals(kcoll.getDataValue("prd_id"))||"100040".equals(kcoll.getDataValue("prd_id")))){
						cus_type = "001";
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cus_type;
	}
}
