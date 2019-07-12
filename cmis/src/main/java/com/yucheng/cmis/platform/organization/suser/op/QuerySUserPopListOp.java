package com.yucheng.cmis.platform.organization.suser.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.RestrictUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QuerySUserPopListOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SUser";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String team_no = "";
		try{
			connection = this.getConnection(context);
		
		//获得查询的过滤数据
			KeyedCollection queryData = null;
			String queryCondition1="";
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			
			try{
				if(context.containsKey("team_no")){
					//对已经添加的成员信息进行过滤
					team_no=(String) context.getDataValue("team_no");
					queryCondition1 = "WHERE "+queryCondition1+ "actorno not in (select mem_no from s_team_mem)";
				}else{
//					queryCondition1=(String) context.getDataValue("queryCondition");
					//queryCondition1=" WHERE  "+queryCondition1+"actorno in (select actorno from s_roleuser where roleno='3002')";
				}
			}catch(EMPException e){
			}

			//获得查询条件，交集、精确查询，忽略空值
			String conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false)
									+queryCondition1+"order by actorno desc";
			
			String [] conCheck=conditionStr.split("WHERE");
			
			// 添加记录集权限
			if(conditionStr.indexOf("WHERE") != -1){
				conditionStr = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+" and "+conditionStr.substring(6, conditionStr.length()));
			}else {
				conditionStr  = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+conditionStr);
			}

			if(conCheck.length==3){
				conditionStr=conCheck[0]+" WHERE "+conCheck[1]+" and "+conCheck[2];
			}
			
			int size = 20;
			//设置只在第一次查询总记录数
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("actorno");
			list.add("actorname");
			list.add("telnum");
			list.add("idcardno");
			list.add("orgid");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			SInfoUtils.addSOrgName(iColl, new String[] { "orgid" });
			
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
