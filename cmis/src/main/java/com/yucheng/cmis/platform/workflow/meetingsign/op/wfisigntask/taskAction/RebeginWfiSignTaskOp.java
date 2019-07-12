package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisigntask.taskAction;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.meetingsign.component.WfiSignComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

/**
 * 重开贷审会
 * 
 * @author 南部大区信贷业务部 MOHEN
 * @Email:zhouxuan@yuchengtech.com 2011-5-17下午02:23:56 TODO
 */
public class RebeginWfiSignTaskOp extends CMISOperation {

	private final String modelId = "WfiSignTask";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		boolean success = false;
		String taskId = "", nTaskId = "";
		try {
			connection = this.getConnection(context);

			connection = this.getConnection(context);
			try {
				taskId = (String) context.getDataValue("taskId");
			} catch (Exception e) {
				// TODO: handle exception
			}
			WfiSignComponent com = (WfiSignComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							WorkFlowConstance.WFI_COMPONENTID4SIGN, context, connection);
			nTaskId = com.rebeginSignTask(taskId);
			if (nTaskId != "") {
				success = true;
			}
		} catch (EMPException ee) {
			ee.printStackTrace();
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} finally {
			context.addDataField("success", success);
			context.setDataValue("taskId", nTaskId);
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
