package com.yucheng.cmis.biz01line.psp.op.pspbatchtaskrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
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
public class DeletePspBatchTaskRecordOp extends CMISOperation {

	private final String modelId = "PspBatchTaskRel";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String major_task_id = (String) context.getDataValue("task_id");
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.deleteByPk(modelId, major_task_id, connection);
			dao.deleteByPk("PspCheckTask", major_task_id, connection);
			context.put("flag", "success");		
		}catch (EMPException ee) {
			context.put("flag", "failed");
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
