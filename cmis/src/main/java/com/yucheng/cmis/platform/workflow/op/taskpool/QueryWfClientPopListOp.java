package com.yucheng.cmis.platform.workflow.op.taskpool;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

/**
 * <p>查询引擎客户端提供给项目池配置时使用</p>
 * @author liuhw
 *
 */
public class QueryWfClientPopListOp extends CMISOperation {

	private final String modelId = "WfClient";
	
	@Override
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false);
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			IndexedCollection iColl = dao.queryList(modelId, null ,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			
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
