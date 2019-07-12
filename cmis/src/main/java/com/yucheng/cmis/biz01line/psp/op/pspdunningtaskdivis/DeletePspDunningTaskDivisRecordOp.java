package com.yucheng.cmis.biz01line.psp.op.pspdunningtaskdivis;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class DeletePspDunningTaskDivisRecordOp extends CMISOperation {

	private final String modelId = "PspDunningTaskDivis";
	private final String modelIdR = "PspDunningRecord";
	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String serno_value = null;
			String flagInfo = "";
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			//若该任务已登记则不能删除
			String condition = "where task_serno='"+serno_value+"'";
			IndexedCollection iCollRecord = dao.queryList(modelIdR, condition, connection);
			if(iCollRecord.size()>0){
				flagInfo = PUBConstant.EXISTS;
			}else {
				int count=dao.deleteByPk(modelId, serno_value, connection);
				if(count!=1){
					throw new EMPException("Remove Failed! Records :"+count);
				}
				flagInfo = PUBConstant.SUCCESS;
			}
			context.addDataField("flag", flagInfo);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
