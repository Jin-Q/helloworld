package com.yucheng.cmis.platform.workflow.op.wfhumanstates;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.msi.OrganizationCacheServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * 委托代办历史查询
 * @author liuhw
 *
 */
public class QueryWfAgentLogListOp extends CMISOperation {

	private final String modelIdDo = "WfiAgentLog";
	private final String modelIdEnd = "WfiAgentLogend";
	@Override
	public String doExecute(Context context) throws EMPException {
		
		String modelId = null;
		String flag = "1";  //1实例未办结 2办结
		try {
			flag = (String) context.getDataValue("flag");
			if(flag.equals("1")) {
				modelId = modelIdDo;
			} else {
				modelId = modelIdEnd;
			}
		} catch (Exception e) {
			throw new EMPException("获取参数[flag]失败！");
		}
		String currentUserId = null;
		Connection connection = null;
		KeyedCollection queryData = null;
		try {
			queryData = (KeyedCollection) context.getDataElement(modelIdDo);
		} catch (Exception e) {
		}
		try {
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			connection = this.getConnection(context);
			String condition = TableModelUtil.getQueryCondition(modelIdDo, queryData, context, false, false, false);
			if(condition == null || condition.trim().equals("")) {
				condition = " WHERE ORIGINALUSER='"+currentUserId+"'";
			} else {
				condition += " AND ORIGINALUSER='"+currentUserId+"'";
			}
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "self", "15");
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection icoll = dao.queryList(modelId, null, condition, pageInfo, connection);
			WorkFlowUtil.addUserName4WF(icoll, new String[]{"replacer"});
			icoll.setName(modelIdDo+"List");
			this.putDataElement2Context(icoll, context);
			TableModelUtil.parsePageInfo(context, pageInfo);			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
