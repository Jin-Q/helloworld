package com.yucheng.cmis.biz01line.psp.op.pspbatchtaskrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2015-1-13
*@description 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量）
*@version v1.0
*
 */
public class UpdatePspBatchTaskRecordOp extends CMISOperation {
	

	private final String modelId = "PspBatchTaskRel";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection kColl =  (KeyedCollection) context.getDataElement(modelId);
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection PCT = new KeyedCollection();
			PCT.setName("PspCheckTask");
			PCT.put("task_id", kColl.getDataValue("major_task_id").toString());
			PCT.put("batch_task_type", kColl.getDataValue("batch_task_type").toString());
			PCT.put("task_type", kColl.getDataValue("task_type").toString());
			PCT.put("manager_id", kColl.getDataValue("manager_id").toString());
			PCT.put("manager_br_id", kColl.getDataValue("manager_br_id").toString());
			PCT.put("check_time", kColl.getDataValue("check_time").toString());
			PCT.put("check_view", kColl.getDataValue("check_view").toString());
			dao.update(PCT, connection);
			context.addDataField("flag", "success");
		} catch (Exception e) {
			context.addDataField("flag", "falied");
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}
}
