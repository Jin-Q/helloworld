package com.yucheng.cmis.platform.organization.sorg.op;

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
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.RestrictUtil;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QuerySOrgListOp extends CMISOperation {

	private final String modelId = "SOrg";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String manager_id="";
			String team="";
			KeyedCollection queryData = null;

			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			conditionStr = StringUtil.transConditionStr(conditionStr, "organname");
			//添加记录级权限
			if(conditionStr.indexOf("WHERE") != -1){
				conditionStr = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+" and "+conditionStr.substring(6, conditionStr.length()));
			}else {
				conditionStr  = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+conditionStr);
			}
			/*RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);*/
//			conditionStr  = RestrictUtil.getNewRestrictSelf(this.modelId, connection, context);
			if(context.containsKey("manager_id")){
				manager_id = (String) context.getDataValue("manager_id");
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
				if(!"".equals(manager_id)){
					KeyedCollection kColl = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", manager_id, null, connection);
					//判断是否存在团队
					if(kColl!=null&&kColl.getDataValue("team_no")!=null&&!"".equals(kColl.getDataValue("team_no"))){
						String team_no = (String) kColl.getDataValue("team_no");
						if(conditionStr.indexOf("WHERE") != -1){
							conditionStr +=" and organno in (select team_org_id from s_team_org s where s.team_no ='"+team_no+"')";
						}else{
							conditionStr = " where organno in (select team_org_id from s_team_org s where s.team_no ='"+team_no+"')";
						}
					}else{
						//添加记录级权限	
						if(conditionStr.indexOf("WHERE") != -1){
							conditionStr = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+" and organno in (select organno from s_deptuser where actorno = '"+manager_id+"') and"+conditionStr.substring(6, conditionStr.length()));
						}else {
							conditionStr  = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+conditionStr+" and organno in (select organno from s_deptuser where actorno = '"+manager_id+"')");
						}
					}				
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			}
			if(context.containsKey("team")){
				team = (String) context.getDataValue("team");
				if("team".equals(team)){
					//添加记录级权限	
					if(conditionStr.indexOf("WHERE") != -1){
						conditionStr = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+" and arti_organno not like '%V%' and "+conditionStr.substring(6, conditionStr.length()));
					}else {
						conditionStr  = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+conditionStr+" and arti_organno not like '%V%'");
					}
				}
			}
			
			/** modified by wangj 2015-10-10 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			if(context.containsKey("opt")){
				String opt = (String) context.getDataValue("opt");
				if("team".equals(opt)){
					if(conditionStr.indexOf("WHERE") != -1){
						conditionStr +=" and s_org.organno in ('9350503000','9350501900') ";
					}else{
						conditionStr = " where  s_org.organno in ('9350503000','9350501900')  ";
					}
				}
				
			}
			/** modified by wangj 2015-10-10 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			/** 查询分析中机构过滤：支行只能选择到自己机构本身，分行可以选择到下级机构，总行可以选择到全行机构   2014-06-11 唐顺岩 **/
			if(context.containsKey("qry")){
				String cur_org = (String)context.getDataValue("organNo");
				TableModelDAO dao = this.getTableModelDAO(context);
				KeyedCollection kColl = dao.queryDetail(modelId, cur_org, connection);
				if(kColl.containsKey("enname") && !"1".equals(kColl.getDataValue("enname"))){
					conditionStr = " WHERE (ORGANNO ='"+cur_org+"' OR SUPORGANNO='"+cur_org+"')  AND "+conditionStr.substring(6, conditionStr.length());
				}
			}
			
			/** 业务申请，出账，去除营销机构 **/
			if(context.containsKey("yewu")){
				String yewu = (String)context.getDataValue("yewu");
				if("is".equals(yewu)){
					if(conditionStr.indexOf("WHERE") != -1){
						conditionStr = conditionStr+" and organno not like '%V%'";
					}else{
						conditionStr = " where organno not like '%V%'";
					}
				}
			}
		   /** END */
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("organno");
			list.add("suporganno");
			list.add("arti_organno");
			list.add("organname");
			list.add("fincode");
			list.add("distno");
			list.add("area_dev_cate_type");
			/**add by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 ,增加机构等级字段 begin**/
			list.add("org_lvl");
			/**add by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 ,增加机构等级字段 end**/
		
			IndexedCollection iColl= dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
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
