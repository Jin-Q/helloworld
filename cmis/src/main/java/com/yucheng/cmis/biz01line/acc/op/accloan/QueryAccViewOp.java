package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryAccViewOp extends CMISOperation {
	private final String modelId = "AccView";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String cusQueryCondition = "";
	        try {
	             queryData = (KeyedCollection)context.getDataElement(this.modelId);
	        } catch (Exception e) {}
	            
	        try {
                 cusQueryCondition = (String) context.getDataValue("cusTypCondition");
            } catch (Exception e) {
               EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "没有配置查询条件", null);}
            String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);    	
	            
            if("".equals(conditionStr)||conditionStr==null){
            	conditionStr = " where 1=1 " ;
            }
            if(cusQueryCondition!=null&&!"".equals(cusQueryCondition)){
            	conditionStr = conditionStr + " and " + cusQueryCondition;
            }
            
            //资产卖出登记查询条件   modify by zhaozq 2014-07-02  start
            if(context.containsKey("from")&&"Average".equals(context.getDataValue("from"))){
            	String organNo = (String) context.getDataValue("organNo");
            	String currentUserId = (String) context.getDataValue("currentUserId");
            	//XD150918069 丰泽鲤城区域团队业务流程改造 Edited by FCL 2015-09-21  ----start
				/**
				 * 台账选择的时候，根据当前操作用户是否属于营销团队，
				 * 如果属于营销团队的话，那么就放开营销团队所挂的所有支行
				 */
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
				//XD150918069 丰泽鲤城区域团队业务流程改造 Edited by FCL 2015-09-21  ----end
            	//过滤条件：余额>0，状态正常，非表外业务
            	/**modified by lisj 2014年12月4日  删除币种，产品类型过滤条件，过滤信用证业务  begin**/
            	conditionStr = conditionStr + " and (table_model = 'AccLoan' and prd_id not in ('700020','700021') or table_model ='AccPad') and cus_id in (select cus_id from cus_base where cust_mgr = '"+currentUserId+"') and manager_br_id in ("+team_org_id+")" +
            			" and status='1' and bill_bal>0 " +
            			" and bill_no not in (select bill_no from Iqp_Average_Asset where average_status in ('1','3'))";
            }
            /**modified by lisj 2014年12月4日  删除币种，产品类型过滤条件，过滤信用证业务 end**/
            //资产卖出登记查询条件   modify by zhaozq 2014-07-02  end
            
            EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, conditionStr, null);
            int size = 15;
            PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
            IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
            
			iColl.setName("AccViewPopList"); 
			
			String[] args=new String[] {"cus_id","prd_id" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"};
			String[] fieldName=new String[]{"cus_name","prdname"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id","fina_br_id"});  
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
