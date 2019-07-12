package com.yucheng.cmis.biz01line.ind.op.indmodel;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;	

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIndModelListOp extends CMISOperation {
	
	private static final Logger logger = Logger.getLogger(QueryIndModelListOp.class);
	
	//扄1�7要操作的表模垄1�7
	private final String modelId = "IndModel";
	
	/**
	 * 业务逻辑执行的具体实现方泄1�7
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

				//获得查询的过滤数捄1�7
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) 
			{}
			
			//获得查询条件，交集�1�7�精确查询，忽略空�1�7�1�7
		String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
								+"";
			 conditionStr = StringUtil.transConditionStr(conditionStr, "model_name");
			int size = 10;
			//设置只在第一次查询�1�7�记录数
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
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
