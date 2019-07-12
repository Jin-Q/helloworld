package com.yucheng.cmis.platform.organization.skin;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class AddOrUpdSkinOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "PubSkin";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
			
			String actorno = null;
			String skinIdNew = null;
			try {
				actorno = (String)context.getDataValue("actorno");
			} catch (Exception e) {}
			if(actorno == null || "".equals(actorno))
				throw new EMPJDBCException("The values actorno cannot be empty!");
			
			try {
				skinIdNew = (String)context.getDataValue("skinIdNew");
			} catch (Exception e) {}
			if(skinIdNew == null || "".equals(skinIdNew))
				throw new EMPJDBCException("The values skinIdNew cannot be empty!");
			
			//先删除原来选择皮肤
			SqlClient.executeUpd("deleteSkinByActno", actorno, null, null, connection);
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = new KeyedCollection(modelId);
			kColl.put("actorno", actorno);
			kColl.put("skinid", skinIdNew);
			dao.insert(kColl, connection);
			
			flag = "success";
			context.addDataField("flag", flag);
			
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
