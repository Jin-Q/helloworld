package com.yucheng.cmis.platform.permission.resource.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteRestrictOp extends CMISOperation {
	private static final String MODELID = "SRowright";
	/**
	 * 删除当前资源的记录级权限信息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String resourceid = "";
		try {
			connection = this.getConnection(context);
			resourceid = (String)context.getDataValue("resourceid");
			if(resourceid == null || resourceid.trim().length() == 0){
			}else {
				TableModelDAO dao = this.getTableModelDAO(context);
				List list = new ArrayList();
				list.add("pk1");
				KeyedCollection rowKColl = dao.queryFirst(MODELID, list, " where resourceid='"+resourceid+"'", connection);
				String PKID = (String)rowKColl.getDataValue("pk1");
				if(PKID != null && PKID != ""){
					int count = dao.deleteByPk(MODELID, PKID, connection);
					if(count != 1){
						throw new EMPException("删除记录失败！");
					}
				}
			}
			context.addDataField("flag", "success");
		} catch (Exception e) {
			context.addDataField("flag", "failed");
			e.printStackTrace();
			throw new EMPException("删除失败！");
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
