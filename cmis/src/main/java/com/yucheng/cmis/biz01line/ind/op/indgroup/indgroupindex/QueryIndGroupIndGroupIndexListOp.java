package com.yucheng.cmis.biz01line.ind.op.indgroup.indgroupindex;

import java.sql.Connection;


import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.util.TableModelUtil;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIndGroupIndGroupIndexListOp extends CMISOperation {
	
	
	//扄1�7要操作的表模垄1�7
	private final String modelId = "IndGroupIndex";
	
	/**
	 * 业务逻辑执行的具体实现方泄1�7
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String group_no_value = (String)context.getDataValue("IndGroup.group_no");
			
			if(group_no_value==null){
				throw new EMPException("没有父关键字＄1�7");
			}
			
			
			String conditionStr = "where group_no = '" + group_no_value+"' ";
			
		int size = 40;
		//设置只在第一次查询�1�7�记录数
		PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		//获取可以分页的OracleDao对象
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
		IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
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
