package com.yucheng.cmis.biz01line.psp.op.pspbatchtaskrel;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
*@author lisj
*@time 2015-1-13
*@description 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量）
*@version v1.0
*
 */
public class GetPspBatchTaskListOp extends CMISOperation {
	private final String modelId = "PspBatchTaskRel";

	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String major_task_id = "";
			try {
				major_task_id = (String)context.getDataValue("major_task_id");
			} catch (Exception e) {
				
			}
			int size = 10; 
            PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
            TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
            String condition = "where major_task_id='"+major_task_id+"'";
			IndexedCollection iColl = dao.queryList(modelId, null, condition, connection);
			BigDecimal LoanSumAmt = new BigDecimal(0);
			BigDecimal LoanSumBalance = new BigDecimal(0);
			BigDecimal sumQnt = new BigDecimal(0);
			if(iColl!=null && iColl.size()>0){
				for(int i=0;i<iColl.size();i++){
					KeyedCollection temp = (KeyedCollection) iColl.get(i);
					LoanSumAmt = LoanSumAmt.add(BigDecimalUtil.replaceNull(temp.getDataValue("loan_totl_amt")));
					LoanSumBalance = LoanSumBalance.add(BigDecimalUtil.replaceNull(temp.getDataValue("loan_balance")));
					sumQnt = sumQnt.add(BigDecimalUtil.replaceNull(temp.getDataValue("qnt")));
				}
				context.put("loan_sum_amt", LoanSumAmt);
				context.put("loan_sum_balance", LoanSumBalance);
				context.put("sum_qnt", sumQnt);
			}
			SqlClient.update("updateLoanTotlAmtByTaskId", major_task_id, LoanSumAmt, null, connection);
			SqlClient.update("updateLoanBalanceByTaskId", major_task_id, LoanSumBalance, null, connection);
			SqlClient.update("updateQntByTaskId", major_task_id, sumQnt, null, connection);
			IndexedCollection PBTL = dao.queryList(modelId, null, condition, pageInfo, connection);
			PBTL.setName("PspBatchTaskList"); 
			this.putDataElement2Context(PBTL, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			String[] args=new String[] {"cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(PBTL, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
