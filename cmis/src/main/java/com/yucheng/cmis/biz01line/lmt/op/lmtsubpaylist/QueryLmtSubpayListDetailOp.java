package com.yucheng.cmis.biz01line.lmt.op.lmtsubpaylist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtSubpayListDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtSubpayList";

	private final String pk_name = "pk";
	
//	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
//			if(this.updateCheck){
//				RecordRestrict recordRestrict = this.getRecordRestrict(context);
//				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
//			}
			String pk_value = null;
			try {
				pk_value = (String)context.getDataValue(pk_name);
			} catch (Exception e) {}
			if(pk_value == null || pk_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_name+"] cannot be null!");

			String subpay_bill_no = "";
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryFirst(modelId, null, "where pk = '" + pk_value + "'", connection);
			subpay_bill_no = (String)kColl.getDataValue("subpay_bill_no");
			KeyedCollection kCollAcc = dao.queryDetail("AccView", subpay_bill_no, connection);
			kColl.put("bill_amt", kCollAcc.getDataValue("bill_amt"));
			kColl.put("bill_bal", kCollAcc.getDataValue("bill_bal"));
			kColl.put("int_cumu", Double.parseDouble((String)kCollAcc.getDataValue("inner_owe_int"))+Double.parseDouble((String)kCollAcc.getDataValue("out_owe_int")));
			kColl.put("cont_no", kCollAcc.getDataValue("cont_no"));
			kColl.put("prd_id", kCollAcc.getDataValue("prd_id"));
			String[] args=new String[] { "prd_id" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[] modelForeign=new String[]{"prdid"};
			String[] fieldName=new String[]{"prdname"};
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
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
