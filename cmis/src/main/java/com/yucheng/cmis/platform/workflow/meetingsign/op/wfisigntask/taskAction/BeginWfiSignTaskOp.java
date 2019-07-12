package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisigntask.taskAction;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.meetingsign.component.WfiSignComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

/**
 * 开始贷审会
 * 
 * @author 南部大区信贷业务部 MOHEN
 * @Email:zhouxuan@yuchengtech.com 2011-5-17下午02:23:16 TODO
 */
public class BeginWfiSignTaskOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String success = "false";
		try {
			connection = this.getConnection(context);
			String taskId = "";
			connection = this.getConnection(context);
			try {
				taskId = (String) context.getDataValue("taskId");
			} catch (Exception e) {
				e.printStackTrace();
			}

			WfiSignComponent com = (WfiSignComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							WorkFlowConstance.WFI_COMPONENTID4SIGN, context, connection);
			success = com.beginSignTask(taskId);

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
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
