package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisignvote;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.meetingsign.component.WfiSignComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateWfiSignVoteRecordOp extends CMISOperation {

    private final String modelId = "WfiSignVote";

    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        boolean success = false;
        try {
            connection = this.getConnection(context);
            KeyedCollection kColl = null;
            String voteId = "";
            String result = "";
            String svAdvice = "";
            try {
                kColl = (KeyedCollection) context.getDataElement(modelId);
                voteId = (String) kColl.getDataValue("sv_vote_id");
                TableModelDAO dao = this.getTableModelDAO(context);
                KeyedCollection kCollNow = dao.queryAllDetail(modelId, voteId, connection);
                String voteStatus = (String) kCollNow.getDataValue("sv_status");
                if(!("211".equals(voteStatus) || "".equals(voteStatus))) {
                	throw new EMPException("会议已经非正常结束，不能进行投票操作！");
                }
                
                result = (String) kColl.getDataValue("sv_result");
                svAdvice = (String) kColl.getDataValue("sv_advice");
            } catch (EMPException e) {
            	e.printStackTrace();
            	throw e;
            }
            if (kColl == null || kColl.size() == 0)
                throw new EMPJDBCException("The values to update[" + modelId + "] cannot be empty!");
            WfiSignComponent com = (WfiSignComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID4SIGN, context, connection);
            success = com.submitVoteTask(voteId, result, svAdvice);

        } catch (EMPException ee) {
            ee.printStackTrace();
            throw ee;
        } finally {
            context.addDataField("success", success);
            if (connection != null)
                this.releaseConnection(context, connection);
        }
        return "0";
    }
}
