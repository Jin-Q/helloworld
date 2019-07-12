package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtnamelist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetLmtAgrDetailsJointViewOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAgrDetails";
	private final String limit_code_name = "limit_code";
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String limit_code_value = null;
			try {
				limit_code_value = (String)context.getDataValue(limit_code_name);
			} catch (Exception e) {}
			if(limit_code_value == null || limit_code_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+limit_code_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, limit_code_value, connection);
			
			SInfoUtils.getPrdPopName(kColl, "prd_id", connection);  //翻译产品
			/**翻译额度名称**/
			String[] args=new String[] { "limit_name" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[] modelForeign=new String[]{"prdid"};
			String[] fieldName=new String[]{"prdname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			this.putDataElement2Context(kColl, context);
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
