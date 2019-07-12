package com.yucheng.cmis.biz01line.ccr.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CcrPubConstant;

public class DeleteCcrBatchAppInfoRecordOp extends CMISOperation {
	
	private final String modelId = "CcrBatchAppInfo";

	private final String serno_name = "serno";
	private final String apply_status="apply_status";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			//构建业务处理类
			CcrComponent ccrComponent = (CcrComponent)CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(CcrPubConstant.CCR_COMPONENT,context, connection);
			
			
			String serno_value = null;
			String apply_status=null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				apply_status = (String)context.getDataValue(apply_status);
			} catch (Exception e) {
				EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "Kcoll["+modelId+"] can't find in context");				
			}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			
			String count=ccrComponent.deleteBatchApp(serno_value);
			if(count.equals(CMISMessage.DEFEAT)){
				throw new EMPException("Remove Failed! Record Count: " + count);
			}
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
