package com.yucheng.cmis.biz01line.dth.op.dthLoanSubjectAnaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class GetDthIndivBadAnalyDetailPageOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String value = context.getDataValue("value").toString();
			String type = context.getDataValue("type").toString();
			String sql = "";
			
			if(type.equals("one")){
				sql = "select m.cus_id,m.prd_id,m.bill_no,m.loan_amt,m.loan_balance,m.distr_date,m.end_date,m.five_class,m.manager_br_id, c.manager_id as cust_mgr"
						+ "  from acc_loan_m m ,(select cont_no,manager_id from cus_manager where is_main_manager = '1') c "
						+ " where m.loan_balance > 0 and m.normal_balance = 0 and m.cont_no = c.cont_no(+) " 
						+ "  and his_date = (select to_char(add_months(last_day(to_date(openday, 'yyyy-mm-dd')), -1),'yyyy-mm-dd') from pub_sys_info)   "
						+ "  and prd_id in (select prdid from Prd_Basicinfo where prdowner like '%BL300%') and prd_id ='"+value+"' ";
			}else if(type.equals("two")){
				sql = "select m.cus_id,m.prd_id,m.bill_no,m.loan_amt,m.loan_balance,m.distr_date,m.end_date,m.five_class,m.manager_br_id,cust_mgr  "
						+ "  from ( select m.*,c.manager_id as cust_mgr  "
						+ "  from acc_loan_m m ,(select cont_no,manager_id from cus_manager where is_main_manager = '1') c"
						+ " where m.loan_balance>0 and m.normal_balance = 0 and m.cont_no = c.cont_no(+)" 
						+ "  and his_date = (select to_char(add_months(last_day(to_date(openday, 'yyyy-mm-dd')), -1),'yyyy-mm-dd') from pub_sys_info)   "
						+ "  and prd_id in (select prdid from Prd_Basicinfo where prdowner like '%BL300%') )m where m.cust_mgr = '"+value+"'";
			}else if(type.equals("prd")){
				String[] data = value.split(",");
				sql = "select m.cus_id,m.prd_id,m.bill_no,m.loan_amt,m.loan_balance,m.distr_date,m.end_date,m.five_class,m.manager_br_id,"
					+ " (select assure_main from ctr_loan_cont where cont_no = m.cont_no) assure_main, "
					+ "  c.manager_id as cust_mgr  "
					+ "  from acc_loan_m m,(select cont_no,manager_id from cus_manager where is_main_manager = '1') c " 
					+ " where m.loan_balance > 0 and m.cont_no = c.cont_no(+) and his_date = '"+data[1]+"' and prd_id ='"+data[0]+"' ";
			}else if(type.equals("grt")){
				String[] data = value.split(",");
				sql = "select m.cus_id,m.prd_id,m.bill_no,m.loan_amt,m.loan_balance,m.distr_date,m.end_date,m.five_class,m.manager_br_id,assure_main, "
						+ "  c.manager_id as cust_mgr  "
						+ "  from acc_loan_m m , ctr_loan_cont ht ,(select cont_no,manager_id from cus_manager where is_main_manager = '1') c"
						+ " where m.cont_no = ht.cont_no  and ht.assure_main in ('100','200','300','400') and m.cont_no = c.cont_no(+)"
					+ " and his_date = '"+data[1]+"' and assure_main ='"+data[0]+"' ";
			}
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
			
			String[] args = new String[] {"prd_id","cus_id"};
			String[] modelIds = new String[] {"PrdBasicinfo","CusBase"};
			String[] modelForeign = new String[] {"prdid","cus_id"};
			String[] fieldName = new String[] { "prdname","cus_name"};
			String[] resultName = new String[] {"prd_type","cus_name"};
		    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr" });
			iColl.setName("IndivBadAnalyDetail");
			
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