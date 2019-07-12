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

public class GetDthLoanBalanceDetailPageOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String value = context.getDataValue("value").toString();
			String sql = "select m.cus_id,m.prd_id,m.bill_no,m.loan_amt,m.loan_balance,m.distr_date,m.end_date,m.five_class,m.manager_br_id,    "
					+ "   b.cus_name as ,c.manager_id as cust_mgr from acc_loan_m m ,cus_base b ,(select cont_no,manager_id from cus_manager where is_main_manager = '1') c  "
					+ "   where m.loan_balance > 0  and m.cus_id = b.cus_id and m.cont_no = c.cont_no(+)" 
					+ "and prd_id in (select prdid from Prd_Basicinfo where prdowner like '%BL300%') and his_date = '"+value+"' ";
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
			
			String[] args = new String[] {"prd_id"};
			String[] modelIds = new String[] {"PrdBasicinfo"};
			String[] modelForeign = new String[] {"prdid"};
			String[] fieldName = new String[] { "prdname"};
			String[] resultName = new String[] {"prd_type"};
		    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr" });
			iColl.setName("LoanBalanceDetail");
			
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