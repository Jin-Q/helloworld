package com.yucheng.cmis.biz01line.lmt.op.lmtcurrentfundsmeasure;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddLmtCurrentFundsMeasureRecordOp extends CMISOperation {
	private final String modelId = "LmtCurrentFundsMeasure";

	public String doExecute(Context context) throws EMPException {
		EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "进入AddLmtCurrentFundsMeasureRecordOp类!");
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String operMsg = "1";
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);
			DecimalFormat df = new DecimalFormat("##########0.00");
			if(kColl.containsKey("oper") && "calculate".equals((String)kColl.getDataValue("oper"))){
				context.addDataField("operMsg", operMsg);
				double result = 0.0;
//				流动资金贷款额度=营运资金量-借款人自有资金-现有流动资金贷款-其他渠道提供的营运资金
//				current_funds_amt = operation_funds - borrower_monetary_fund - current_funds_loan  - funds_other_channels;
				//营运资金量
				double operFunds = Double.parseDouble((String)kColl.getDataValue("operation_funds"));
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "营运资金量:=="+operFunds);
				//借款人自有资金
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "借款人自有资金:=="+Double.parseDouble((String)kColl.getDataValue("borrower_monetary_fund")));
				double borrowerMoney =  Double.parseDouble((String)kColl.getDataValue("borrower_monetary_fund"));
				//现有流动资金贷款
				double currentFundsLoan = Double.parseDouble((String)kColl.getDataValue("current_funds_loan"));
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "现有流动资金贷款:=="+currentFundsLoan);
				//其他渠道提供的营运资金
				double otherFunds = Double.parseDouble((String)kColl.getDataValue("funds_other_channels"));
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "其他渠道提供的营运资金:=="+otherFunds);
				result = SInfoUtils.getAmtSubtract(operFunds, borrowerMoney);
				result = SInfoUtils.getAmtSubtract(result, currentFundsLoan);
				result = SInfoUtils.getAmtSubtract(result, otherFunds);
				
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "current_funds_amt:=="+result);
				kColl.removeDataElement("oper");
				if(result<0){
					result=0;
				}
				kColl.setDataValue("current_funds_amt", result);
				List<String> list = new ArrayList<String>();
				list.add("serno");
				String condition = " where serno = '"+ kColl.getDataValue("serno")+"'";
				IndexedCollection iColl = dao.queryList(modelId, list, condition, connection);
				int count = 0;
				if(iColl.size() > 0){
					kColl.remove("liab_fund");
					kColl.remove("lmt_amt");
					count = dao.update(kColl, connection);
				}else{
					kColl.remove("liab_fund");
					kColl.remove("lmt_amt");
					count = dao.insert(kColl, connection);
				}
				if(count != 1){
					throw new EMPException("插入表模型LmtCurrentFundsMeasure出错，serno=="+kColl.getDataValue("serno"));
				}
				
				try{
					context.addDataField("currentFundsAmt", df.format(result));
				}catch(EMPException e){
					context.setDataValue("currentFundsAmt", df.format(result));
				}
				
			}else if(kColl.containsKey("oper") && "operation".equals((String)kColl.getDataValue("oper"))){
				context.addDataField("operMsg", operMsg);
				//上年度销售收入 
				double last_year_income = Double.parseDouble((String)kColl.getDataValue("last_year_income"));
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "上年度销售收入 :=="+last_year_income);
				//上年度销售利润率
				double last_year_profit_rate = Double.parseDouble((String)kColl.getDataValue("last_year_profit_rate"));
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "上年度销售利润率 :=="+last_year_profit_rate);
				//预计销售收入年增长率
				double pre_income_rise_rate = Double.parseDouble((String)kColl.getDataValue("pre_income_rise_rate"));
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "预计销售收入年增长率:=="+pre_income_rise_rate);
				//营运资金周转次数
				double operation_turnover_count = Double.parseDouble((String)kColl.getDataValue("operation_turnover_count"));
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "营运资金周转次数:=="+operation_turnover_count);
				
				//营运资金量＝上年度销售收入×（1－上年度销售利润率）×（1＋预计销售收入年增长率%）/营运资金周转次数
				double result=SInfoUtils.getAmtMultiply(last_year_income, SInfoUtils.getAmtSubtract(1, last_year_profit_rate));
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "营运资金量＝上年度销售收入×（1－上年度销售利润率）×（1＋预计销售收入年增长率%）/营运资金周转次数");
				result=SInfoUtils.getAmtMultiply(result, SInfoUtils.getAmtAdd(1, pre_income_rise_rate));
				if(operation_turnover_count != 0){
					result=SInfoUtils.getAmtDivide(result, operation_turnover_count);
				}else{
					result = 0;
				}
				//若计算值小于0则设为0
				if(result<0){
					result=0;
				}
				context.addDataField("currentFundsAmt", df.format(result));
			}else {
				context.addDataField("operMsg", operMsg);
				List<String> list = new ArrayList<String>();
				list.add("serno");
				String condition = " where serno = '"+ kColl.getDataValue("serno")+"'";
				IndexedCollection iColl = dao.queryList(modelId, list, condition, connection);
				int count = 0;
				if(iColl.size() > 0){
					kColl.remove("pre_income_rise_rate");
					kColl.remove("current_funds_loan");
					kColl.remove("funds_other_channels");
					kColl.remove("operation_funds");
					kColl.remove("current_funds_amt");
					count = dao.update(kColl, connection);
				}else{
					kColl.remove("pre_income_rise_rate");
					kColl.remove("current_funds_loan");
					kColl.remove("funds_other_channels");
					kColl.remove("operation_funds");
					kColl.remove("current_funds_amt");
					count = dao.insert(kColl, connection);
				}
				if(count != 1){
					throw new EMPException("插入表模型LmtCurrentFundsMeasure出错，serno=="+kColl.getDataValue("serno"));
				}
				context.addDataField("currentFundsAmt", "");
			}
		}catch (EMPException ee) {
			ee.printStackTrace();
			if(context.containsKey("operMsg")){
				context.setDataValue("operMsg", ee.getMessage());
			}
		} catch(Exception e){
			e.printStackTrace();
			if(context.containsKey("operMsg")){
				context.setDataValue("operMsg", e.getMessage());
			}
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
