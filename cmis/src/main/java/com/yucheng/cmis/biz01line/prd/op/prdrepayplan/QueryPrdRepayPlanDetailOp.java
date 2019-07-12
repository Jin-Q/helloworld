package com.yucheng.cmis.biz01line.prd.op.prdrepayplan;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPrdRepayPlanDetailOp  extends CMISOperation {
	
	private final String modelId = "PrdRepayPlan";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String repay_mode_id = null;
			try {
				repay_mode_id = (String)context.getDataValue("repay_mode_id");
			} catch (Exception e) {}
			if(repay_mode_id == null || repay_mode_id.length() == 0)
				throw new EMPJDBCException("The value ["+repay_mode_id+"] cannot be null!");
			
			int size = 2;  
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = "where repay_mode_id='"+repay_mode_id+"'";
			
			IndexedCollection iColl = dao.queryList(modelId,null,condition,pageInfo,connection);
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
