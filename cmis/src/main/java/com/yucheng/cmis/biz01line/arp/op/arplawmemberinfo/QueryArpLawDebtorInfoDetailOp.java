package com.yucheng.cmis.biz01line.arp.op.arplawmemberinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryArpLawDebtorInfoDetailOp  extends CMISOperation {
	
	private final String modelId = "ArpLawMemberInfo";
	

	private final String pk_serno_name = "pk_serno";
	
	
	private boolean updateCheck = false;
	

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
			
			String[] args = new String[] { "cus_id","cus_id","cus_id" };
			String[] modelIds = new String[] { "CusBase","CusBase","CusBase" };
			String[] modelForeign = new String[] { "cus_id","cus_id","cus_id" };
			String[] fieldName = new String[] { "cus_name","cert_type","cert_code" };
			String[] resultName = new String[] { "cus_name","cert_type","cert_code" };
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName ,resultName);
		
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