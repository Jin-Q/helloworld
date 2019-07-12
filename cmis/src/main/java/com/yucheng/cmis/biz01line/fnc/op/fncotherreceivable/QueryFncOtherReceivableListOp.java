package com.yucheng.cmis.biz01line.fnc.op.fncotherreceivable;


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
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

/**
 * 
 *@Classname	QueryFncOtherReceivableListOp.java
 *@Version 1.0	
 *@Since   1.0 	Oct 8, 2008 2:44:21 PM  
 *@Copyright 	yuchengtech
 *@Author 		Administrator
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class QueryFncOtherReceivableListOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "FncOtherReceivable";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
		//获得查询的过滤数据
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			//获得查询条件，交集、精确查询，忽略空值
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"order by cus_id desc,fnc_ym desc,fnc_typ desc,seq desc";
			
			int size = 10;
			//设置只在第一次查询总记录数
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("fnc_ym");
			list.add("cus_id");
			list.add("fnc_typ");
			list.add("seq");
			list.add("fnc_con_cus_id");
			list.add("fnc_sum_amt");
			list.add("crt_usr_id");
			list.add("crt_dt");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "input_id" });
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
