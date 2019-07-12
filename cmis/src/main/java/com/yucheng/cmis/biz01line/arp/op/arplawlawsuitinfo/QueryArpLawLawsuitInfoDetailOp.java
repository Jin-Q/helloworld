package com.yucheng.cmis.biz01line.arp.op.arplawlawsuitinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryArpLawLawsuitInfoDetailOp  extends CMISOperation {
	
	private final String modelId = "ArpLawLawsuitInfo";
	

	private final String case_no_name = "case_no";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String case_no_value = null;
			try {
				case_no_value = (String)context.getDataValue(case_no_name);
			} catch (Exception e) {}
			if(case_no_value == null || case_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+case_no_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, case_no_value, connection);
			SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" ,"input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "manager_id" ,"input_id"});
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
