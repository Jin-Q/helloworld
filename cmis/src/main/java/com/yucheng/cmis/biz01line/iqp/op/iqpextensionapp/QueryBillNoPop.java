package com.yucheng.cmis.biz01line.iqp.op.iqpextensionapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryBillNoPop extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {			
            connection = this.getConnection(context);         
            KeyedCollection queryData = null;
            try {
                queryData = (KeyedCollection)context.getDataElement("AccLoanPop");
            } catch (Exception e) {}
            String conditionStr = TableModelUtil.getQueryCondition("AccLoan", queryData, context, false, true, false);
            if(conditionStr.length()==0){
            	conditionStr = "where 1 = 1 ";
            }
            //modify by liuhongyu on 2019-04-13 
            if(conditionStr.indexOf("cont_no")!=-1){
            	conditionStr = conditionStr.replaceAll("cont_no", "a.cont_no");
            }
            
            /*** 外模块条件拼装 ***/
            String sql = "";
            //String out_condition = context.getDataValue("condition").toString();
            String out_condition = "";
            String fg = "";
            if(context.containsKey("flag")){
            	fg = (String) context.getDataValue("flag");
            	if("1".equals(fg)){
            		out_condition= " and cus_id = '"+(String)context.getDataValue("cus_id")+"' and acc_status = '1' ";
            	}else if("2".equals(fg)){
            		out_condition = " and acc_status not in ('0','9','10','11') ";
            	}else if("3".equals(fg)){
            		out_condition = " and (manager_br_id='' or manager_br_id is null or manager_br_id='"+(String)context.getDataValue("organNo")+"' or manager_br_id in (select organno from s_org where suporganno='"+(String)context.getDataValue("organNo")+"')) and a.cont_no in (select cont_no from cus_manager where manager_id = '"+(String)context.getDataValue("currentUserId")+"' and is_main_manager = '1')";
            	}
            }
            /**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
            /*modified by wangj 需求编号【XD141222087】法人透支改造 begin */
            if(context.containsKey("moduleId")){
            	String moduleId = context.getDataValue("moduleId")+"";
            	if(moduleId.equals("arp")){
       			 sql = "select a.bill_no , a.cont_no ,  a.cus_id , a.cur_type , a.five_class, (select cus_name from cus_base where cus_id=a.cus_id ) "
						+ " as cus_name ,prd_id , acc_status ,a.ruling_ir,round(a.reality_ir_y/12,6) as rate , a.distr_date as start_date , a.end_date , rec_int_accum, recv_int_accum, "
						+ " round((select base_remit from prd_rate_maintain where fount_cur_type = a.cur_type) * a.loan_amt,2)loan_amt,  "
						+ " round((select base_remit from prd_rate_maintain where fount_cur_type = a.cur_type) * a.loan_balance,2)loan_balance,  "
						+ " round((select base_remit from prd_rate_maintain where fount_cur_type = a.cur_type) * a.inner_owe_int,2)inner_owe_int,  "
						+ " round((select base_remit from prd_rate_maintain where fount_cur_type = a.cur_type) * a.out_owe_int,2)out_owe_int, "
						+ " b.manager_id as cust_mgr, a.manager_br_id main_br_id" 
						+ " from acc_loan a,cus_manager b "
 					    + conditionStr+out_condition+" and b.cont_no = a.cont_no and b.is_main_manager = '1' and a.loan_balance > 0 ";
            	}else if(moduleId.equals("extIqp")){
                	/*** 由贷款台账取借据信息，并过滤贷款台账中以下业务品种：保函、信用证、保理、贷款证明、贷款意向、融资贴现类、资产转受让、贷款承诺业务 ***/
        			sql = "select a.bill_no , a.cont_no ,  a.cus_id , a.cur_type , a.loan_amt,a.loan_balance ,a.inner_owe_int,a.out_owe_int,a.five_class, "
        					+ " (select cus_name from cus_base where cus_id=a.cus_id )as cus_name ,prd_id , acc_status , "
        					+ " a.ruling_ir,a.reality_ir_y as rate , a.distr_date as start_date , a.end_date, " 
        					+ " b.manager_id as cust_mgr, a.manager_br_id main_br_id"
        					+ " from acc_loan a,cus_manager b "
        					+ conditionStr+out_condition+" and b.cont_no = a.cont_no and b.is_main_manager = '1' and a.acc_status = '1' and a.loan_balance > 0   " 
        					+ " and prd_id not in ('400020','400021','700020','700021','800020','800021','400023','400024','500032','500027','500028','500029','600020','400022')";

                }else{
                	/*** 由贷款台账取借据信息，并过滤贷款台账中以下业务品种：保函、信用证、保理、贷款证明、贷款意向、融资贴现类、资产转受让、贷款承诺业务 ***/
        			sql = "select a.bill_no , a.cont_no ,  a.cus_id , a.cur_type , a.loan_amt,a.loan_balance ,a.inner_owe_int,a.out_owe_int,a.five_class, "
        					+ " (select cus_name from cus_base where cus_id=a.cus_id )as cus_name ,prd_id , acc_status , "
        					+ " a.ruling_ir,a.reality_ir_y as rate , a.distr_date as start_date , a.end_date, " 
        					+ " b.manager_id as cust_mgr, a.manager_br_id main_br_id"
        					+ " from acc_loan a,cus_manager b "
        					+ conditionStr+out_condition+" and b.cont_no = a.cont_no and b.is_main_manager = '1' and a.acc_status = '1' and a.loan_balance > 0 and a.end_date > (select openday from pub_sys_info) " 
        					+ " and prd_id not in ('400020','400021','700020','700021','800020','800021','400023','400024','500032','500027','500028','500029','600020','400022')";
            	}
            }
            /*modified by wangj 需求编号【XD141222087】法人透支改造 end */
            int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);

			/*** 翻译产品 ***/
			String[] args=new String[] {"prd_id" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[]modelForeign=new String[]{"prdid"};
			String[] fieldName=new String[]{"prdname"};
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr" });
            SInfoUtils.addSOrgName(iColl, new String[] { "main_br_id" });
		    /**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}