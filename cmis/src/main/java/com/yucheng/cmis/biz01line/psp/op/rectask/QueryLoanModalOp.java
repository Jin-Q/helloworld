package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryLoanModalOp  extends CMISOperation {
	private final String modelId = "RscTaskInfo";
	/**
	 * 
	 * <p>
	 *	<h2>简述</h2>
	 *		<ol>查询贷款形式</ol>
	 *	<h2>功能描述</h2>
	 *		<ol>无</ol>
	 *	</p>
	 * @param context
	 * @return
	 * @throws EMPException
	 */
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String billNo = "";
			if(context.containsKey("bill_no")){
				billNo = (String)context.getDataValue("bill_no");
			}
			Map param = new HashMap();
			param.put("bill_no", billNo);
//			KeyedCollection ctrLoanCont =
//					ctrLoanContComponent.queryCtrLoanContDetailByBillNo(param, connection);
//			
			KeyedCollection ctrLoanCont = (KeyedCollection)dao.queryFirst("CtrLoanCont", null, "bill_no='"+billNo+"'", connection);
			//将处理结果放入Context中以便前端获取
			context.addDataField("loanModal", ctrLoanCont.getDataValue("loan_modal"));
		} catch (EMPException ee) {
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
