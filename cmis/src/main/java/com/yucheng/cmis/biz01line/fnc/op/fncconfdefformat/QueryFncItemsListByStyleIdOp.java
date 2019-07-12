package com.yucheng.cmis.biz01line.fnc.op.fncconfdefformat;


import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryFncItemsListByStyleIdOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "FncConfDefFmt";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */

	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
		    //获得查询的过滤数据
			String style_id = (String)context.getDataValue("style_id");
		
			KeyedCollection queryData = new KeyedCollection();
			queryData.setId(this.modelId);
			queryData.addDataField("style_id", style_id);
			
			//获得查询条件，交集、精确查询，忽略空值
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
		
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("item_id");
			list.add("item_name");
			list.add("fnc_conf_typ");
			list.add("fnc_no_flg");
			list.add("remark");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			//context.addDataElement(iColl);
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

