package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryRscTaskInfoSubDetailOp  extends CMISOperation {
	private final String modelId = "RscTaskInfoSub";
	private boolean updateCheck = true;
	/**
	 * <p>
	 * <h2>简述</h2>
	 *    <ol>查询详情记录</ol>
	 * <h2>功能描述</h2>
	 * 		<ol>无</ol>
	 * </p>
	* @param context EMP上下文
	 * @return String
	 * @throws EMPException
	 */
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String pkId = null;
			try {
				pkId = (String)context.getDataValue("pk_id");
			} catch (Exception e) {}
			if(pkId == null || pkId.length() == 0)
				throw new EMPJDBCException("The value of pk[pk_id] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, pkId, connection);
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
