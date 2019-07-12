package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.acc.component.AccComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryAccLoanPop extends CMISOperation {
	private final String modelId = "AccLoan";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
        String condition = "";
        try{
        	TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
            connection = this.getConnection(context);
            KeyedCollection queryData = null;
            try {
                queryData = (KeyedCollection)context.getDataElement(this.modelId);
            } catch (Exception e) {}
            
            try {
            	condition = (String) context.getDataValue("condition");
            } catch (Exception e) {
            	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "没有配置查询条件", null);}
            String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false);
            //XD150918069 丰泽鲤城区域团队业务流程改造 Edited by FCL 2015-09-21  ----start
            String currentUserId = "";
            String team_org_id = "";
            if(context.containsKey("currentUserId") && condition.indexOf("manager_br_id")==-1){
            	currentUserId = (String)context.getDataValue("currentUserId");
            	KeyedCollection kColl = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", currentUserId, null,connection);
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
					team_org_id = "'"+(String)context.getDataValue("organNo")+"'";
				}
            }
           
            if("".equals(conditionStr)||conditionStr==null){
            	conditionStr = " where 1=1 " ;
            }
            conditionStr = StringUtil.transConditionStr(conditionStr, "cus_name");
            if(condition!=null&&!"".equals(condition)){
            	conditionStr = conditionStr + " and " + condition;
            	if(!"".equals(team_org_id)){
            		conditionStr = conditionStr + " and a.manager_br_id in ("+team_org_id+")";
            	}
            }
            //XD150918069 丰泽鲤城区域团队业务流程改造 Edited by FCL 2015-09-21  ----end
            int size = 15;
            
            PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
    
            DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
    		AccComponent accComponent = (AccComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("AccComponent", context, connection);
            
    		IndexedCollection iColl = accComponent.getPvpCont(conditionStr, dataSource, pageInfo);
            iColl.setName("AccLoanList");
            
            String[] args=new String[] {"cus_id","prd_id" };
            String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
            String[]modelForeign=new String[]{"cus_id","prdid"};
            String[] fieldName=new String[]{"cus_name","prdname"};
            //详细信息翻译时调用			
            SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
            SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
            this.putDataElement2Context(iColl, context);
            TableModelUtil.parsePageInfo(context, pageInfo);
			
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}