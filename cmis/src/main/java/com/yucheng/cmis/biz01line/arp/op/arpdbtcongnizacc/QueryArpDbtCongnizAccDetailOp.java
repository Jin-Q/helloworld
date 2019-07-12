package com.yucheng.cmis.biz01line.arp.op.arpdbtcongnizacc;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryArpDbtCongnizAccDetailOp  extends CMISOperation {
	
	private final String modelId = "ArpDbtCongnizAcc";	
	private final String pk_serno_name = "pk_serno";		
	private boolean updateCheck = true;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);		
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}	
			
			String pk_serno_value = null;
			try {
				pk_serno_value = (String)context.getDataValue(pk_serno_name);
			} catch (Exception e) {}
			if(pk_serno_value == null || pk_serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_serno_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, pk_serno_value, connection);
			
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
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			SInfoUtils.addUSerName(kColl, new String[] {"input_id" });
			
			/*** 四级分类现在采用新模式，需要进行判断 ***/
			double normal_balance = Double.parseDouble(kColl.getDataValue("normal_balance").toString()) ; //正常余额
			double overdue_balance = Double.parseDouble(kColl.getDataValue("overdue_balance").toString()) ; //逾期余额
			double slack_balance = Double.parseDouble(kColl.getDataValue("slack_balance").toString()) ; //呆滞余额
			double bad_dbt_balance = Double.parseDouble(kColl.getDataValue("bad_dbt_balance").toString()) ; //呆账余额
			double inner_owe_int = Double.parseDouble(kColl.getDataValue("inner_owe_int").toString()) ; //表内欠息
			double out_owe_int = Double.parseDouble(kColl.getDataValue("out_owe_int").toString()) ; //表外欠息
			String four_class = "";
			
			if(normal_balance > 0){
				four_class = "1";
			}else if(overdue_balance > 0){
				four_class = "3";
			}else if(slack_balance > 0){
				four_class = "7";
			}else if(bad_dbt_balance > 0){
				four_class = "8";
			}
			kColl.addDataField("four_class", four_class); //四级分类标志
			kColl.addDataField("owe_int", out_owe_int+inner_owe_int); //欠息累计
			
			this.putDataElement2Context(kColl, context);			
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