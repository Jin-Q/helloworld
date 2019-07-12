package com.yucheng.cmis.biz01line.acc.op.accdrft;

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

public class QueryAccDrftDetailOp  extends CMISOperation {
	private final String modelId = "AccDrft";
	private final String bill_no_name = "bill_no";
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}  
			
			String bill_no = null;
			try {
				bill_no = (String)context.getDataValue(bill_no_name);
			} catch (Exception e) {}
			if(bill_no == null || bill_no.length() == 0)
				throw new EMPJDBCException("The value of pk["+bill_no_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, bill_no, connection);
		    
			String[] args=new String[] { "prd_id","discount_per" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[] modelForeign=new String[]{"prdid","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"};
			//详细信息翻译时调用			
		    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    
		    /** 组织机构、登记机构翻译 */
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","in_acct_br_id","fina_br_id"});
			
			
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
