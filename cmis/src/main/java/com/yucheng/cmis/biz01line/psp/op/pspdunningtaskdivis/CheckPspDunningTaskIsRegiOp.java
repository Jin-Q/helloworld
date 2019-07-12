package com.yucheng.cmis.biz01line.psp.op.pspdunningtaskdivis;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class CheckPspDunningTaskIsRegiOp extends CMISOperation {

	private final String modelId = "PspDunningRecord";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String flagInfo = null;
			String serno = null;
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(serno == null || "".equals(serno))
				throw new EMPJDBCException("催收任务编号为空，查询失败！");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = " WHERE TASK_SERNO='"+serno+"'";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl.size()>0){
				flagInfo = PUBConstant.EXISTS;
			}else{
				flagInfo = PUBConstant.NOTEXISTS;
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
