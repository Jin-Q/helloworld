package com.yucheng.cmis.platform.workflow.approverconf.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryWfiApproverUserListOp extends CMISOperation {


	private final String modelId = "WfiApproverUser";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			String confid = null;
			try {
				confid = (String) context.getDataValue("confid");
				if(confid == null || "".equals(confid))
					throw new EMPException("获取参数主表配置主键失败！");
			} catch (Exception e) {
				e.printStackTrace();
				throw new EMPException(e);
			}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false)
									+"";
			if(conditionStr.equals("")) {
				conditionStr = " WHERE confid='"+confid+"'";
			} else {
				conditionStr += " AND confid='"+confid+"'";
			}
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addUSerName(iColl, new String[]{"actorno"});
			TableModelUtil.parsePageInfo(context, pageInfo);
			this.putDataElement2Context(iColl, context);
			
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
