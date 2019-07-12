package com.yucheng.cmis.biz01line.arp.op.arplawlawsuitdetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteArpLawLawsuitDetailRecordOp extends CMISOperation {

	private final String modelId = "ArpLawLawsuitDetail";
	

	private final String pk_serno_name = "pk_serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);


			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String pk_serno_value = null;
			try {
				pk_serno_value = (String)context.getDataValue(pk_serno_name);
			} catch (Exception e) {}
			if(pk_serno_value == null || pk_serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_serno_name+"] cannot be null!");
				
			

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail("ArpLawLawsuitDetail", pk_serno_value, connection);
			int count=dao.deleteByPk(modelId, pk_serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
			/*** 重新合计诉讼费用 ***/
			UpdateArpLawLawsuitDetailRecordOp cmisOp =new UpdateArpLawLawsuitDetailRecordOp();
			cmisOp.setLawsuitFee(kColl, connection, context);
			
			context.addDataField("flag", PUBConstant.SUCCESS);
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
