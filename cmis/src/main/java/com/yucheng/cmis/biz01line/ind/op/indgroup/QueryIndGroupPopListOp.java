package com.yucheng.cmis.biz01line.ind.op.indgroup;

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
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIndGroupPopListOp extends CMISOperation {
	
	 private static final Logger logger = Logger.getLogger(QueryIndGroupPopListOp.class);
	 
	//扄1�7要操作的表模垄1�7
	private final String modelId = "IndGroup";
	
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
			{
				  logger.error(e.getMessage(), e);
			}
			
			//获得查询条件，交集�1�7�精确查询，忽略空�1�7�1�7
		String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
								+"order by group_no desc";
			
			int size = 10;
			//设置只在第一次查询�1�7�记录数
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("group_no");
			list.add("group_name");
			list.add("group_kind");
			list.add("rating_rules");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
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
