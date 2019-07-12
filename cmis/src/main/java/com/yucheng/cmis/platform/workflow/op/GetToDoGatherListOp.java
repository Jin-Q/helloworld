package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.base.CMISConstance;

/**
 * <p>会办待办事项</p>
 * @author liuhw
 *
 */
public class GetToDoGatherListOp extends CMISOperation {

	private final static String modelId = "WfInstanceGatherProperty";
	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		KeyedCollection queryData = null;
		String condition = null;
		try {
			queryData = (KeyedCollection) context.getDataElement(modelId);
			condition = TableModelUtil.getQueryCondition(modelId, queryData, context, false, true, false);
		} catch (Exception e) {
		}
		try {
			connection = this.getConnection(context);
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
			List fields = new ArrayList();
			fields.add("instanceid");
			fields.add("beforeinstanceid");
			fields.add("maininstanceid");
			fields.add("bizseqno");
			fields.add("mainnodeid");
			fields.add("mainnodename");
			fields.add("mainjobname");
			fields.add("gatherstartuserid");
			fields.add("gatherenduserid");
			fields.add("gathertitle");
			fields.add("gatherdesc");
			fields.add("currentgatheruserlist");
			fields.add("allprocessor");
			fields.add("currentgatherprocessors");
			fields.add("gatherstarttime");
			fields.add("gatherstartusername");
			fields.add("gatherendusername");
			fields.add("allprocessorname");
			TableModelDAO dao = this.getTableModelDAO(context);
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			//非拟稿||已经办理  && 流转中||过期办理  && 必办人员LIKE
			//(此处可能存在一个bug（一般不会），当出现用户号如800001、80000等情况时，所以尽量避免存在诸如此类的用户ID)
			String condition2 = "( CURRENTGATHERUSERLIST like '%"+currentUserId+";%' OR GATHERENDUSERID='"+currentUserId+"' " +
			 		"OR GATHERSTARTUSERID='"+currentUserId+"' )";
			if(condition==null || condition.trim().equals("")) {
				condition = " WHERE " + condition2;
			} else {
				condition += " AND " + condition2;
			}
			IndexedCollection icoll = dao.queryList(modelId, fields, condition, pageInfo, connection);
			icoll.setName(modelId+"List");
			TableModelUtil.parsePageInfo(context, pageInfo);
			this.putDataElement2Context(icoll, context);
			
		} catch (Exception e) {
			EMPLog.log("GetToDoGatherListOp", EMPLog.ERROR, EMPLog.ERROR, "获取会办待办事项出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
