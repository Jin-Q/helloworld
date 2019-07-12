package com.yucheng.cmis.biz01line.ind.op.indlib.indopt;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;	

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIndLibIndOptDetailOp  extends CMISOperation {
	
	private static final Logger logger = Logger.getLogger(QueryIndLibIndOptDetailOp.class);
	
	//扄1�7要操作的表模垄1�7
	private final String modelId = "IndOpt";

	//扄1�7要操作的表模型的主键
	private final String index_no_name = "index_no";
	private final String index_value_name = "index_value";
	
	//设置当前是否要判断修改的记录级权附1�7
	private boolean updateCheck = false;

	/**
	 * 执行查询详细信息操作
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			//查询特定丄1�7条记彄1�7
			if(this.updateCheck){
				//对修改进行记录级权限判断
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			//获得查询霄1�7要的主键信息
			String index_no_value = null;
			try {
				index_no_value = (String)context.getDataValue(index_no_name);
			} catch (Exception e) 
			{
				logger.error(e.getMessage(), e);
			}
			if(index_no_value == null || index_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+index_no_name+"] cannot be null!");
			String index_value_value = null;
			try {
				index_value_value = (String)context.getDataValue(index_value_name);
			} catch (Exception e) 
			{
				logger.error(e.getMessage(), e);
			}
			if(index_value_value == null || index_value_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+index_value_name+"] cannot be null!");
			
			//查询列表信息
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("index_no",index_no_value);
			pkMap.put("index_value",index_value_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
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
