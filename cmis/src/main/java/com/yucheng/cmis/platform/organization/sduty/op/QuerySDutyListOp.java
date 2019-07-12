package com.yucheng.cmis.platform.organization.sduty.op;

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
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QuerySDutyListOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SDuty";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String pageFalg = "true";
		
		//获得查询的过滤数据
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			if(context.containsKey("pageFalg")){
				pageFalg = context.getDataValue("pageFalg").toString();
			}
			
			//获得查询条件，交集、精确查询，忽略空值
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"order by dutyno desc";
			conditionStr = StringUtil.transConditionStr(conditionStr, "dutyname");
			int size = 15;
			//设置只在第一次查询总记录数
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("dutyno");
			list.add("dutyname");
			list.add("organno");
			list.add("depno");
			list.add("orderno");
			IndexedCollection iColl = new IndexedCollection();
			if(pageFalg.equals("false")){
				iColl = dao.queryList(modelId,list ,conditionStr,connection);
			}else{
				iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
				TableModelUtil.parsePageInfo(context, pageInfo);
			}
			
			iColl.setName(iColl.getName()+"List");
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
