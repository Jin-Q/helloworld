package com.yucheng.cmis.platform.workflow.op.taskpool;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

/**
 * 设置项目池关联岗位
 * @author liuhw
 *
 */
public class SetWfTaskpoolUsersOp extends CMISOperation {

	private final String modelId = "WfTaskpoolUser";
	@Override
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		String tpid = null;
		String users = null;
		try {
			tpid = (String) context.getDataValue("tpid");
			users = (String) context.getDataValue("users");
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			if(users==null || users.trim().equals("") || users.equals("null")) {  //删除关联
				IndexedCollection icoll = dao.queryList(modelId, "WHERE TPID='"+tpid+"'", connection);
				if(icoll!=null && icoll.size()>0) {
					Map pkMap = new HashMap();
					for(int i=0; i<icoll.size(); i++) {
						KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
						pkMap.put("tpid", kcoll.getDataValue("tpid"));
						pkMap.put("userid", kcoll.getDataValue("userid"));
						int count = dao.deleteAllByPks(modelId, pkMap, connection);
					}
				}
			} else {
				String[] usersArr = users.split(";");
				for(int i=0; i<usersArr.length; i++) {
					String uid = usersArr[i];
					if(uid!=null && !"".equals(uid.trim())) {
						try {
							KeyedCollection kcoll = new KeyedCollection(modelId);
							kcoll.put("tpid", tpid);
							kcoll.put("userid", uid);
							int count =dao.insert(kcoll, connection);
						} catch (Exception e) {
						}
					}
				}
			}
			context.put("flag", 1);
			
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
