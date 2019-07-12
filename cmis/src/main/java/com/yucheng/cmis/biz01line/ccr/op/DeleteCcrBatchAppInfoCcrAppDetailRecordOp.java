package com.yucheng.cmis.biz01line.ccr.op;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CcrPubConstant;

public class DeleteCcrBatchAppInfoCcrAppDetailRecordOp extends CMISOperation {
	
	private final String modelId = "CcrAppDetail";
	
	private final String serno_name = "serno";
	private final String cus_id_name = "cus_id";

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try{
			connection = this.getConnection(context);


	                CcrComponent ccrComponent = (CcrComponent)CMISComponentFactory
	                .getComponentFactoryInstance().getComponentInstance(CcrPubConstant.CCR_COMPONENT,context, connection);
			String serno_value = null;
			String cus_id_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {
		    	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"获取serno失败");
			}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			try {
				cus_id_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"获取cus_id失败");
			}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");

			ccrComponent.deleteBatchAppDetail(serno_value, cus_id_value);
			ccrComponent.deleteBatchAppScore(serno_value, cus_id_value);
			
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
