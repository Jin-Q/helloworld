package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class GetDthCostWarnDetailPageOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);		
			String value = context.getDataValue("value").toString();
			String type = context.getDataValue("type").toString();
			String sql = "";
			
			/*** 总分支行机构判断处理begin ***/
			DthPubAction cmisOp = new DthPubAction();
			String org_condition = cmisOp.judgeOrg(context.getDataValue("organNo").toString(), connection);
			org_condition = org_condition.replaceAll("manager_br_id", "a.manager_br_id");
			/*** 总分支行机构判断处理end ***/
			
			if(type.equals("one")){	//图1是逾期
				String condition = "";
				if(value.equals("include")){	//到期标识
					condition = " and (a.overdue_date is null or a.overdue_balance = 0) ";
				}else{
					condition = " and a.overdue_date is not null and a.overdue_balance > 0 ";
				}
				sql = "select b.cus_id,b.cus_name ,a.cont_no,a.cur_type,a.loan_amt,a.overdue_balance, "
						+ " (a.inner_owe_int+a.out_owe_int) as owe_int,a.overdue_date,a.manager_br_id,c.manager_id"
						+ " from cus_base b ,acc_loan a ,(select distinct cont_no, manager_id from cus_manager where is_main_manager = '1' ) c " 
						+ " where b.cus_id(+) = a.cus_id and c.cont_no(+) = a.cont_no and a.acc_status = '1'"+ condition+org_condition;
			}else if(type.equals("four")){	//图4到7是五级分类
				String belg_line = context.getDataValue("BL").toString();
				String condition = "";
				sql = "select b.cus_id,b.cus_name ,a.cont_no,a.cur_type,a.loan_amt,a.overdue_balance, "
					+ " (a.inner_owe_int+a.out_owe_int) as owe_int,a.overdue_date,a.manager_br_id ,c.manager_id"
					+ " from cus_base b ,acc_loan a,(select * from cus_manager where is_main_manager = '1' ) c "
					+ "  where b.cus_id(+) = a.cus_id and c.cont_no(+) = a.cont_no and a.acc_status = '1' "+org_condition ;
				if(!belg_line.equals("BLall")){
					condition = " and a.cus_id in (select cus_id from cus_base c where c.belg_line = '"+belg_line+"' ) ";
				}
				condition = condition +" and a.five_class = '"+value+"'";
				sql = sql + condition + " order by a.cus_id " ;
			}
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);			
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id" });
			iColl.setName("CostWarnDetail");
			
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