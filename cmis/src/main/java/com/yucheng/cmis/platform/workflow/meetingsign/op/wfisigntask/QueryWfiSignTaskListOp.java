package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisigntask;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.msi.OrganizationCacheServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryWfiSignTaskListOp extends CMISOperation {

    private final String modelId = "WfiSignTask";

    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            KeyedCollection queryData = null;
            String  stExeUser = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
            try {
                queryData = (KeyedCollection) context.getDataElement(this.modelId);
               
            } catch (Exception e) {
            }
            String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
            if (conditionStr.length() == 0) {
                conditionStr += " WHERE ";
            } else {
                conditionStr += " AND ";
            }
            conditionStr += " ST_EXE_USER='" + stExeUser + "' ORDER BY ST_START_TIME desc";
            int size = 10;
            PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
            TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);
            List list = new ArrayList();
            list.add("st_task_id");
            list.add("st_task_name");
            list.add("st_exe_user");
            list.add("st_exe_org");
            list.add("st_result");
            list.add("st_task_status");
            list.add("serno");
            list.add("biz_type");
            list.add("st_start_time");
            list.add("st_end_time");
            IndexedCollection iColl = dao.queryList(modelId, list, conditionStr, pageInfo, connection);
            OrganizationCacheServiceInterface orgCacheMsi = (OrganizationCacheServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationCacheServices", "organization");
			orgCacheMsi.addUserName(iColl, new String[]{"st_exe_user"});
			orgCacheMsi.addOrgName(iColl, new String[]{"st_exe_org"});
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
