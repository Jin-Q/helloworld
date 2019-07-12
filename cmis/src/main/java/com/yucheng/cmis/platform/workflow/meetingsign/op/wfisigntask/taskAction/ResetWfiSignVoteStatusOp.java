package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisigntask.taskAction;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.meetingsign.component.WfiSignComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

/**
 * 复位投票状态
 * 
 * @author 南部大区信贷业务部 MOHEN
 * @Email:zhouxuan@yuchengtech.com 2011-5-17下午02:24:10 TODO
 */
public class ResetWfiSignVoteStatusOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		boolean success = false;
		try {
			String voteId = "";
			try {
				connection = this.getConnection(context);
				voteId = (String) context.getDataValue("sv_vote_id");
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (!voteId.equals("")) {
				WfiSignComponent com = (WfiSignComponent) CMISComponentFactory
						.getComponentFactoryInstance().getComponentInstance(
								WorkFlowConstance.WFI_COMPONENTID4SIGN, context, connection);
				success = com.resetSignVote(voteId);
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
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
