package com.yucheng.cmis.biz01line.arp.op.arpdbtcongnizacc;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryArpDbtCongnizAccListOp extends CMISOperation {

	private final String modelId = "ArpDbtCongnizAcc";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"order by pk_serno desc";
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			
			String[] args = new String[] { "serno","bill_no","bill_no","bill_no","bill_no","cus_id","prd_id","cont_no","bill_no",
					"bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","bill_no" };
			String[] modelIds = new String[] { "ArpDbtCongnizApp","AccLoan","AccLoan","AccLoan","AccLoan","CusBase",
					"PrdBasicinfo","CtrLoanCont","AccLoan","AccLoan","AccLoan","AccLoan","AccLoan","AccLoan","AccLoan",
					"AccLoan","AccLoan","AccLoan","AccLoan","AccLoan"};
			String[] modelForeign = new String[] { "serno","bill_no","bill_no","bill_no","bill_no","cus_id","prdid" ,"cont_no","bill_no",
					"bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","bill_no"};
			String[] fieldName = new String[] { "input_id","prd_id","loan_amt","loan_balance","five_class","cus_name","prdname",
					"cn_cont_no","distr_date","end_date","normal_balance","overdue_balance","slack_balance","bad_dbt_balance" ,
					"inner_owe_int","out_owe_int","rec_int_accum","recv_int_accum","cur_type"};
			String[] resultName = new String[] { "input_id","prd_id","loan_amt","loan_balance","five_class","cus_name","prd_type",
					"cn_cont_no","distr_date","end_date","normal_balance","overdue_balance","slack_balance","bad_dbt_balance",
					"inner_owe_int","out_owe_int","rec_int_accum","recv_int_accum","cur_type"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			SInfoUtils.addUSerName(iColl, new String[] {"input_id" });
			
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
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