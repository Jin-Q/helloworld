package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisigntask.taskAction;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WfiSignTask;
import com.yucheng.cmis.platform.workflow.meetingsign.component.WfiSignComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;

public class InitWfiSignTaskOp extends CMISOperation {

	private final String modelId = "WfiSignTask";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String instanceid = "";
		ComponentHelper hp = new ComponentHelper();
		try {
			connection = this.getConnection(context);
			try {
				instanceid = (String) context.getDataValue("instanceid");
			} catch (Exception e) {
				// TODO: handle exception
			}
			KeyedCollection kColl = new KeyedCollection();
			WfiSignComponent com = (WfiSignComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							WorkFlowConstance.WFI_COMPONENTID4SIGN, context, connection);
			WfiSignTask signTask = com.initSignTask(instanceid);
			kColl = hp.domain2kcol(signTask, modelId);
			this.putDataElement2Context(kColl, context);
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
