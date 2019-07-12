package com.yucheng.cmis.platform.permission.resource.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryRestrictListOp extends CMISOperation {
	private static final String MODELID = "SRowright";
	/**
	 * 获取当前资源的记录级权限信息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String resID = "";
		try {
			connection = this.getConnection(context);
			resID = (String)context.getDataValue("resourceid");
			if(resID == null || resID.trim().length() == 0){
				throw new EMPException("获取资源ID失败！");
			}
			
			/** 获取资源记录集权限列表s_rowright */
			IndexedCollection iColl = new IndexedCollection();
			TableModelDAO dao = this.getTableModelDAO(context);
			iColl = dao.queryList(MODELID, " where resourceid = '"+resID+"'", connection);
			iColl.setName("IqpBillDetailList");
			
			this.putDataElement2Context(iColl, context);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
