package com.yucheng.cmis.biz01line.ind.op.indlib;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIndLibDetailOp extends CMISOperation {
	
	private static final Logger logger = Logger.getLogger(QueryIndLibDetailOp.class);
	
	//扄1�7要操作的表模垄1�7
	private final String modelId = "IndLib";
	
	//扄1�7要操作的表模型的主键
	private final String index_no_name = "index_no";
	
	/**
	 * 执行查询详细信息操作
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String condition="";

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
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, index_no_value, connection);
			this.putDataElement2Context(kColl, context);

			condition=" where index_no='"+index_no_value+"'";
			IndexedCollection iColl_IndOpt = dao.queryList("IndOpt",condition, connection);
			this.putDataElement2Context(iColl_IndOpt, context);
			
			condition=" where index_no='"+index_no_value+"'";
			IndexedCollection iColl_IndPara = dao.queryList("IndPara",condition, connection);
			this.putDataElement2Context(iColl_IndPara, context);
			
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
