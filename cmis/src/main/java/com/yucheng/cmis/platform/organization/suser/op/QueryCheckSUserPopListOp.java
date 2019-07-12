package com.yucheng.cmis.platform.organization.suser.op;

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

public class QueryCheckSUserPopListOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SUser";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
		//获得查询的过滤数据
			KeyedCollection queryData = null;
			String queryCondition1="";
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			
			try{
				
				queryCondition1=(String) context.getDataValue("queryCondition");
				queryCondition1=" WHERE ";
				
			}catch(EMPException e){
				
			}
			
			//获得查询条件，交集、精确查询，忽略空值
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if(conditionStr==null||"".trim().equals(conditionStr)){
				conditionStr=" where 1=1 ";
			}
			KeyedCollection kcoll=(KeyedCollection)context.getDataElement("SUser");
			String organno=(String)kcoll.getDataValue("orgid");
			conditionStr+=" and exists (select b.* from S_DUTYUSER b where s_user.actorno=b.actorno and ";
			conditionStr+=" (b.dutyno  in (select a.dutyno  from s_duty a where a.dutyname like '%监交岗%'))) ";
			conditionStr+=" and orgid='"+organno+"' ";
			conditionStr+=" order by actorno desc";
			String [] conCheck=conditionStr.split("WHERE");
			
			if(conCheck.length==3){
				
				conditionStr=conCheck[0]+" WHERE "+conCheck[1]+" and "+conCheck[2];
			}
			
			int size = 20;
			//设置只在第一次查询总记录数
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("actorno");
			list.add("actorname");
			list.add("telnum");
			list.add("idcardno");
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
