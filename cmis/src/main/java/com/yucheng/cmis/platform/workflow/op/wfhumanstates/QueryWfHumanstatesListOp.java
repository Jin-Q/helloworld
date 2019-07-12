package com.yucheng.cmis.platform.workflow.op.wfhumanstates;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryWfHumanstatesListOp extends CMISOperation {


	private final String modelId = "WfHumanstates";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String curUser = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if(conditionStr==null || "".equals(conditionStr)) {
				conditionStr = " where userid = '" + curUser + "' or vicar = '" + curUser + "'";
			} else {
				conditionStr += " and userid = '" + curUser + "' or vicar = '" + curUser + "'";
			}
			conditionStr += " order by begintime ";
			conditionStr = StringUtil.transConditionStr(conditionStr, "vicar");
			conditionStr = StringUtil.transConditionStr(conditionStr, "vicarname");
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "self", "15");
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null, conditionStr, pageInfo, connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
