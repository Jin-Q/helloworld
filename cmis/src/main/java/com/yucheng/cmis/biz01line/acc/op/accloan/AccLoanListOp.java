package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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

public class AccLoanListOp extends CMISOperation {

	private final String modelId = "AccLoan";
	

	public String doExecute(Context context) throws EMPException {
/*		贸易融资台账     flag=ctrLimitAcc
		保函台账         flag=guarantAcc
		委托贷款台账     flag=csgnLoanAcc
		国内保理台账     flag=interFactAcc
		银行信贷证明台账 flag=proveAcc
		贷款承诺台账     flag=promissoryAcc
		贷款意向台账     flag=adviceAcc
		信托公司贷款台账 flag=isTrustAcc
		逾期台账 flag=isTrustAcc*/
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String biz_type = null;
			String flag = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			try {
				flag = (String)context.getDataValue("flag");
			} catch (Exception e) {
				throw new Exception("参数异常，联系后台管理员!");
			}
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			conditionStr +=" order by distr_date desc";
			int size = 15;    
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();    
			list.add("serno");
			list.add("cont_no");
			list.add("prd_id");
			list.add("cus_id");
			list.add("loan_balance");
			list.add("loan_amt");
			list.add("distr_date");
			list.add("end_date");
			list.add("acc_status");
			list.add("fina_br_id");
			list.add("bill_no");
			list.add("manager_br_id");
			list.add("five_class");
			list.add("cur_type");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] {"cus_id","cus_id","prd_id" ,"cont_no","cont_no"};
			String[] modelIds=new String[]{"CusBase","CusBase","PrdBasicinfo","CtrLoanCont","CtrLoanContSub"};
			String[]modelForeign=new String[]{"cus_id","cus_id","prdid","cont_no","cont_no"};
			String[] fieldName=new String[]{"cus_name","belg_line","prdname","serno","principal_loan_typ"};
			String[] resultName=new String[]{"cus_id_displayname","belg_line","prd_id_displayname","fount_serno","principal_loan_typ"};
		    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
		    
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});  
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
