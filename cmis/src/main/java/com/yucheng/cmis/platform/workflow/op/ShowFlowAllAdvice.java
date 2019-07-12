package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class ShowFlowAllAdvice extends CMISOperation {
	
	private final String modelId = "WfiWorklistTodo";
	private final String modelIdEnd = "WfiWorklistEnd";
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String serno_value = null;
			String instanceId = null;
			try {
				serno_value = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk serno cannot be null!");
		    
			TableModelDAO dao = this.getTableModelDAO(context);
			/**业务申请和出账申请分别为两个不同的流水号*/
			String condition1 = "where pk_value='"+serno_value+"'";
			
			/**查询流程实例号
			 * 首先查询待办流程列表，如果无结果则去查询办结流程列表，如果无结果不作处理
			 * */
			IndexedCollection icoll = dao.queryList(modelId, condition1, connection);
			if(icoll.size()>0){
				KeyedCollection kColl = (KeyedCollection)icoll.get(0);
				instanceId = (String)kColl.getDataValue("instanceid");
			}else{
				IndexedCollection icollEnd = dao.queryList(modelIdEnd, condition1, connection);
				if(icollEnd.size()>0){
					KeyedCollection kColl = (KeyedCollection)icollEnd.get(0);
					instanceId = (String)kColl.getDataValue("instanceid");
				}
			}
			context.addDataField("instanceId", instanceId);
			HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
			request.setAttribute("instanceId", instanceId);
 			
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
