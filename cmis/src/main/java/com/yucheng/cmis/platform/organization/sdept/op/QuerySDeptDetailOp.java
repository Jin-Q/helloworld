package com.yucheng.cmis.platform.organization.sdept.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QuerySDeptDetailOp  extends CMISOperation {
	//所要操作的表模型
	private final String modelId = "SDept";
	
	//所要操作的表模型的主键
	private final String depno_name = "depno";
	
	//设置当前是否要判断修改的记录级权限
	private boolean updateCheck = false;
	
	/**
	 * 执行查询详细信息操作
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			//查询特定一条记录
			if(this.updateCheck){
				//对修改进行记录级权限判断
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			//获得查询需要的主键信息
			String depno_value = null;
			try {
				depno_value = (String)context.getDataValue(depno_name);
			} catch (Exception e) {}
			if(depno_value == null || depno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+depno_name+"] cannot be null!");

			
			//查询列表信息
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, depno_value, connection);
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
