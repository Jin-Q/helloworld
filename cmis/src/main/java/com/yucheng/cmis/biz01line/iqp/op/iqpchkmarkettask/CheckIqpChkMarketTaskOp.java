package com.yucheng.cmis.biz01line.iqp.op.iqpchkmarkettask;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckIqpChkMarketTaskOp extends CMISOperation {

	private final String modelId = "IqpMortValueAdj";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String value_no = (String)context.getDataValue("value_no");
			
			//获取批次信息
			String condition = " where value_no = '"+value_no+"' and status = '1'";
			IndexedCollection iColl = dao.queryList(modelId,condition, connection);
			
			if(iColl.size()>0){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "该货物已存在未处理的盯市任务！");
				return null;
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "校验失败！");
			throw ee;
		}  catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}