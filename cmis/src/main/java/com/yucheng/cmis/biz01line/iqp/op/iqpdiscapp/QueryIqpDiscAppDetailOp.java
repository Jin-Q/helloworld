package com.yucheng.cmis.biz01line.iqp.op.iqpdiscapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIqpDiscAppDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpDiscApp";
	private final String loanModel = "IqpLoanApp";
	
	private final String serno_name = "serno";
	private boolean updateCheck = false;
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			//如果当前票据种类为空，那么从关联的申请产品来设置票据种类。
			if("".equals(kColl.getDataValue("bill_type")) || kColl.getDataValue("bill_type") == null){
				KeyedCollection loanKcoll = dao.queryAllDetail(loanModel, serno_value, connection);
				String prd_id = (String) loanKcoll.getDataValue("prd_id");
				if("300021".equals(prd_id)){//银行承兑汇票贴现 300021  
					context.addDataField("bill_type", "100");				
				}else if("300020".equals(prd_id)){//商业承兑汇票贴现 300020
					context.addDataField("bill_type", "200");
				}
			}else{
				context.addDataField("bill_type", kColl.getDataValue("bill_type"));
			}
			
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
