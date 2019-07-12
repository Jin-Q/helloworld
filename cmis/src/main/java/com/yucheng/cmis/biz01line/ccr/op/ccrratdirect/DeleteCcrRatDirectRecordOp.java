package com.yucheng.cmis.biz01line.ccr.op.ccrratdirect;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.Workflow4BIZIface;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class DeleteCcrRatDirectRecordOp extends CMISOperation {

	private final String modelId = "CcrAppInfo";
	private final String modelId1 = "CcrAppDetail";
	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);

			String serno_value = null;
			try {
				serno_value = (String) context.getDataValue(serno_name);
			} catch (Exception e) {
			}
			if (serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk[" + serno_name
						+ "] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			int count = dao.deleteByPk(modelId, serno_value, connection);
			int count1 = dao.deleteByPk(modelId1, serno_value, connection);
			if (count != 1 || count1 != 1) {
				throw new EMPException("Remove Failed! Records :" + count);
			}else{
				context.addDataField("flag","success");
			}

			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance()
			.getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID); 
			wfi.wfDelInstance(serno_value, modelId, connection);
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
