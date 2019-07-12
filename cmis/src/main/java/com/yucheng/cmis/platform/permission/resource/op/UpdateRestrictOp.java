package com.yucheng.cmis.platform.permission.resource.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class UpdateRestrictOp extends CMISOperation {
	private static final String MODELID = "SRowright";
	/**
	 * 修改当前资源的记录级权限信息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection kc = null;
			kc = (KeyedCollection)context.getDataElement(MODELID);
			if(kc == null || kc.size() == 0){
				throw new EMPException("获取记录集表单出错！");
			}
			String resourceId = (String)kc.getDataValue("resourceid");
			TableModelDAO dao = this.getTableModelDAO(context);
			List list = new ArrayList();
			list.add("pk1");
			KeyedCollection isHave = dao.queryFirst(MODELID, list, " where resourceid = '"+resourceId+"'", connection);
			if(isHave != null && isHave.size() > 0){
				String pk1 = (String)isHave.getDataValue("pk1");
				if(pk1 == null || pk1.trim().length() == 0){
					pk1 = (String)CMISSequenceService4JXXD.querySequenceFromDB("SR", "all", connection, context);
					if(pk1 == null || pk1.trim().length() == 0){
						throw new EMPException("生成主键失败！");
					}
					kc.setDataValue("pk1", pk1);
					dao.insert(kc, connection);
				}else {
					/** 修改资源记录集权限列表s_rowright */
					int count = dao.update(kc, connection);
					if(count != 1){
						throw new EMPException("修改记录失败！");
					}
				}
			}
			context.addDataField("flag", "success");
		} catch (Exception e) {
			context.addDataField("flag", "failed");
			e.printStackTrace();
			throw new EMPException("修改失败！");
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
