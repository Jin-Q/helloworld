package com.yucheng.cmis.platform.permission.resource.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddRestrictOp extends CMISOperation {
	private static final String MODELID = "SRowright";
	/**
	 * 新增当前资源的记录级权限信息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String PKID = "";
		try {
			connection = this.getConnection(context);
			KeyedCollection kc = null;
			kc = (KeyedCollection)context.getDataElement(MODELID);
			if(kc == null || kc.size() == 0){
				throw new EMPException("获取记录集表单出错！");
			}
			PKID = (String)CMISSequenceService4JXXD.querySequenceFromDB("SR", "all", connection, context);
			if(PKID == null || PKID.trim().length() == 0){
				throw new EMPException("生成主键失败！");
			}
			kc.setDataValue("pk1", PKID);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kc, connection);
			context.addDataField("flag", "success");
			context.addDataField("resourceid", (String)kc.getDataValue("resourceid"));
		} catch (Exception e) {
			context.addDataField("flag", "failed");
			context.addDataField("resourceid", "");
			e.printStackTrace();
			throw new EMPException("新增失败！");
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
