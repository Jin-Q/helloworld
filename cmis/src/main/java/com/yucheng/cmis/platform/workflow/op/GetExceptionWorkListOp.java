package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;

/**
 * <p>业务处理异常事项</p>
 * @author liuhw
 *
 */
public class GetExceptionWorkListOp extends CMISOperation {

	private static final String modelId = "WfiMsgQueueView";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		
		KeyedCollection queryData = null;
		try {
			queryData = (KeyedCollection) context.getDataElement(modelId);
		} catch (Exception e) {
		}
		String menuId =  (String) context.getDataValue("menuId");
		Connection connection = null;
		try {
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			connection = this.getConnection(context);
			String condition = TableModelUtil.getQueryCondition(modelId, queryData, context, false, false, false);
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
			if(condition==null || "".equals(condition)) {
				condition = "WHERE OPSTATUS='90'";  //状态为异常
			} else {
				condition += " AND OPSTATUS='90'";
			}
			if(!"exceptionbiz".equals(menuId)) {
				condition += " AND USER_ID='"+currentUserId+"'";
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection icoll = dao.queryList(modelId, null, condition, pageInfo, connection);
			WorkFlowUtil.addUserName4WF(icoll, new String[]{"user_id"});
			icoll.setName(modelId + "List");
			TableModelUtil.parsePageInfo(context, pageInfo);
			this.putDataElement2Context(icoll, context);
			
		} catch (Exception e) {
			EMPLog.log("GetExceptionWorkListOp", EMPLog.ERROR, EMPLog.ERROR, "获取业务处理异常事项出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
