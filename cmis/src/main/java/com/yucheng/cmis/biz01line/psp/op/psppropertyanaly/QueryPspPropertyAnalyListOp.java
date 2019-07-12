package com.yucheng.cmis.biz01line.psp.op.psppropertyanaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryPspPropertyAnalyListOp extends CMISOperation {


	private final String modelId = "PspPropertyAnaly";
	private final String accountModelId = "PspAccountsReceivableAnaly";
	private final String analyModelId = "PspCheckAnaly";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String cus_id = (String) context.getDataValue("cus_id");
			if(cus_id==null){
				throw new EMPException("获取不到客户编号！");
			}
			String task_id = (String) context.getDataValue("task_id");
			if(task_id==null){
				throw new EMPException("获取不到任务编号！");
			}
		
			String conditionStr = " where cus_id = '"+cus_id+"'";
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			//固定资产分析
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			
			//应收账款分析
//			IndexedCollection accountIColl = dao.queryList(accountModelId, null,conditionStr,connection);
//			accountIColl.setName(accountIColl.getName()+"List");
//			this.putDataElement2Context(accountIColl, context);
			
			//检查分析说明
//			KeyedCollection analyKColl = dao.queryDetail(analyModelId, task_id, connection);
//			this.putDataElement2Context(analyKColl, context);
			
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
