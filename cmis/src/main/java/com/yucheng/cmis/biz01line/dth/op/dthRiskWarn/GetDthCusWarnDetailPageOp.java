package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class GetDthCusWarnDetailPageOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String value = context.getDataValue("value").toString();
			String type = context.getDataValue("type").toString();
			String sql = "";
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			IndexedCollection iColl = null;
			
			/*** 总分支行机构判断处理begin ***/
			DthPubAction cmisOp = new DthPubAction();
			String org_condition = cmisOp.judgeOrg(context.getDataValue("organNo").toString(), connection);
			/*** 总分支行机构判断处理end ***/
			
			if(type.equals("one")){
				org_condition = org_condition.replaceAll("manager_br_id", "b.main_br_id");
				sql =   " select a.cont_no,        a.bill_no,                     "
			           +"        a.cus_id,         a.prd_id,                      "
			           +"        a.loan_amt,       a.loan_balance,                "
			           +"        a.distr_date,     a.end_date,                    "
			           +"        a.five_class,     b.cust_mgr,                    "
			           +"        a.manager_br_id,  b.cus_name,                    "
			           +"        cur_type                                         "
			           +"   from cus_base b,                                      "
			           +"        (select c.cus_id, c.cus_bank_rel                 "
			           +"           from cus_com c                                "
			           +"          where c.is_ours_rela_cust in ('1', '01', '02') "
			           +"         union                                           "
			           +"         select i.cus_id, i.cus_bank_rel                 "
			           +"           from cus_indiv i                              "
			           +"          where i.is_rela_cust = '1') c,                 "
			           +"        acc_loan a                                       "
			           +"  where b.cus_id = c.cus_id(+)                           "
			           //+"    and b.cus_status = '20'                              "
			           +"    and a.cus_id = b.cus_id                              "
			           +"    and a.prd_id < '2'                                   "
			           +"    and a.acc_status = '1'                               "
			           +"    and a.loan_balance > 0                 "+org_condition
			           +"    and c.cus_bank_rel = '"+value+"'                     ";
				
				iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
				
				String[] args = new String[] {"prd_id"};
				String[] modelIds = new String[] {"PrdBasicinfo"};
				String[] modelForeign = new String[] {"prdid"};
				String[] fieldName = new String[] { "prdname"};
				String[] resultName = new String[] {"prd_type"};
			    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
				SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
				SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr" });
			} else {
				/**
				 * 根据ods提供的表结构，数据记录是按ods账号来分的。
				 * 所以信贷处理时，总行明细也是按账号明细来取，分行则按户分组
				 */
				if (type.equals("Z")) { // Z个人
					sql = " select cus_id, cust_name, month_real_avg                                 "
						 +"   from (select cus_id,                                                   "
						 +"                cust_name,                                                "
						 +"                ods_amt month_real_avg,                                   "
						 +"                case                                                      "
						 +"                  when (ods_amt between 200000 and 1000000) then          "
						 +"                   '1'                                                    "
						 +"                  when (ods_amt between 1000000 and 3000000) then         "
						 +"                   '2'                                                    "
						 +"                  when (ods_amt between 3000000 and 5000000) then         "
						 +"                   '3'                                                    "
						 +"                  when (ods_amt between 5000000 and 10000000) then        "
						 +"                   '4'                                                    "
						 +"                  when (ods_amt > 10000000) then                          "
						 +"                   '5'                                                    "
						 +"                end as ods_amt                                            "
						 +"           from (select cus_id, cust_name, sum(month_real_avg) as ods_amt "
						 +"                   from v_ods_cus_avg                                     "
						 +"                  where cus_type = 'Z'                      "+org_condition
						 +"                  group by cus_id, cust_name                              "
						 +"                 having sum(month_real_avg) >= 200000))                   "
						 +"  where ods_amt = '"+ value+ "'                                           "
						 +"  order by month_real_avg desc                                            ";
				} else { // 对公
					sql = " select cus_id, cust_name, month_real_avg                                 "
						 +"   from (select cus_id,                                                   "
						 +"                cust_name,                                                "
						 +"                ods_amt month_real_avg,                                   "
						 +"                case                                                      "
						 +"                  when (ods_amt between 500000 and 1000000) then          "
						 +"                   '1'                                                    "
						 +"                  when (ods_amt between 1000000 and 3000000) then         "
						 +"                   '2'                                                    "
						 +"                  when (ods_amt between 3000000 and 5000000) then         "
						 +"                   '3'                                                    "
						 +"                  when (ods_amt between 5000000 and 10000000) then        "
						 +"                   '4'                                                    "
						 +"                  when (ods_amt between 10000000 and 30000000) then       "
						 +"                   '5'                                                    "
						 +"                  when (ods_amt between 30000000 and 50000000) then       "
						 +"                   '6'                                                    "
						 +"                  when (ods_amt between 50000000 and 80000000) then       "
						 +"                   '7'                                                    "
						 +"                  when (ods_amt > 80000000) then                          "
						 +"                   '8'                                                    "
						 +"                end as ods_amt                                            "
						 +"           from (select cus_id, cust_name, sum(month_real_avg) as ods_amt "
						 +"                   from v_ods_cus_avg                                     "
						 +"                  where cus_type = 'F'                      "+org_condition
						 +"                    and MANAGER_BR_ID is not null                         "
						 +"                  group by cus_id, cust_name                              "
						 +"                 having sum(month_real_avg) >= 500000))                   "
						 +"  where ods_amt = '" + value + "'                                         "
						 +"  order by month_real_avg desc                                            ";
				}
				iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
			}

			iColl.setName("CusWarnDetail");			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}
	
}