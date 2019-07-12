package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
*@author yezm
*@time 2015-7-27
*@description TODO 需求编号：XD150625045 贷后管理常规检查任务改造 （否决启用历史操作查询）
*@version v1.0
*
 */ 
public class QueryPspRejectBatchLogListOp extends CMISOperation {


	private final String modelId = "PspRejectBatchLog";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String task_id ="";
		try{
			connection = this.getConnection(context);
			task_id = (String)context.getDataValue("task_id");

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if(conditionStr==null||"".equals(conditionStr)){
				conditionStr= " where 1=1 ";
			}
			conditionStr = conditionStr+" and task_id='"+task_id+"' order by input_time desc ";	
			
			int size = 10;
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		  
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
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
