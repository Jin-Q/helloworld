package com.yucheng.cmis.platform.permission.resource.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class GetRestrictupdatePageOp extends CMISOperation {

	private static final String MODELID = "SRowright";
	/**
	 * 获得修改当前资源的记录级权限信息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			/**
			 *通过资源ID获取资源是否存在记录集权限，存在取出 
			 */
			String resId = (String)context.getDataValue("resourceid");
			if(resId == null || resId == ""){
				throw new EMPException("获取记录集权限资源ID失败！");
			}
			
			List list = new ArrayList();
			list.add("pk1");
			list.add("resourceid");
			list.add("cnname");
			list.add("readtemp");
			list.add("writetemp");
			list.add("memo");
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection rowKColl = dao.queryFirst(MODELID, list, " where resourceid='"+resId+"'", connection);
			String resourceid = (String)rowKColl.getDataValue("resourceid");
			if(resourceid == null || resourceid.trim().length() == 0){
				KeyedCollection resKColl = dao.queryDetail("SResource", resId, connection);
				rowKColl.setDataValue("resourceid", resId);
				rowKColl.setDataValue("cnname", resKColl.getDataValue("cnname"));
			}
			this.putDataElement2Context(rowKColl, context);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
