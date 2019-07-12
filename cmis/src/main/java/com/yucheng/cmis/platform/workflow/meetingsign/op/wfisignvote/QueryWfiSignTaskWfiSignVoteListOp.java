package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisignvote;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.msi.OrganizationCacheServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryWfiSignTaskWfiSignVoteListOp extends CMISOperation {

    private final String modelId = "WfiSignVote";

    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            String st_task_id_value = (String) context.getDataValue("WfiSignTask.st_task_id");

            if (st_task_id_value == null) {
                throw new EMPException("parent primary key not found!");
            }
            String conditionStr = "where st_task_id = '" + st_task_id_value + "' order by sv_vote_id desc";
            int size = 20;
            PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
            TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);
            List list = new ArrayList();
            list.add("sv_vote_id");
            list.add("sv_exe_user");
            list.add("sv_result");
            list.add("sv_status");
            list.add("sv_start_time");
            list.add("sv_end_time");
            list.add("sv_request_time");
            IndexedCollection iColl = dao.queryList(modelId, list, conditionStr, pageInfo, connection);
            OrganizationCacheServiceInterface orgCacheMsi = (OrganizationCacheServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationCacheServices", "organization");
			orgCacheMsi.addUserName(iColl, new String[]{"sv_exe_user"});
            iColl.setName(iColl.getName() + "List");
            this.putDataElement2Context(iColl, context);
            TableModelUtil.parsePageInfo(context, pageInfo);
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
