package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpBailSubDisPopListOp extends CMISOperation {
	
	private final String modelId = "CtrLoanCont";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String conditionStr = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
            
			String organNo = (String)context.getDataValue("organNo");
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
			if(conditionStr !=null && !"".equals(conditionStr)){
				conditionStr +=" and cont_status='"+200+"' and cont_no in(select a.cont_no from acc_view a where a.status='1')";//生效状态的合同 			
			}else{
				conditionStr ="where cont_status='"+200+"' and cont_no in(select a.cont_no from acc_view a where a.status='1')";//生效状态的合同 	
			}

			//modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
			String currentUserId = (String) context.getDataValue("currentUserId");
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			conditionStr += " and serno in (select serno from cus_manager where manager_id = '"+currentUserId+"' and is_main_manager = '1') ";
			KeyedCollection kColl = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", currentUserId, null,connection);
			String team_org_id = "";
			if(kColl!=null&&kColl.getDataValue("team_no")!=null&&!"".equals((String)kColl.getDataValue("team_no"))){
				String team_no = (String)kColl.getDataValue("team_no");
				IndexedCollection iColl4tm = dao.queryList("STeamOrg", "where 1=1 and team_no = '"+team_no+"'", connection);
				if(iColl4tm!=null){
					StringBuffer orgList = new StringBuffer();
					for(Object obj:iColl4tm){
						team_org_id= (String)((KeyedCollection)obj).getDataValue("team_org_id");
						orgList.append("'"+team_org_id+"',");
					}
					team_org_id = orgList.toString().substring(0, orgList.toString().length()-1);
				}
			}
			if(team_org_id==null || "".equals(team_org_id)){
				team_org_id = "'"+organNo+"'";
			}
			conditionStr+=" and manager_br_id in ("+team_org_id+")";
			//modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end
			//TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			int size=15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			IndexedCollection iColl = dao.queryList(modelId,null, conditionStr,pageInfo, connection);
			iColl.setName(iColl.getName()+"List");
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			this.putDataElement2Context(iColl, context);
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
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
