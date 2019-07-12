package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.base.CMISConstance;

/**
 * 获取办结事项
 * @author liuhw
 *
 */
public class GetEndWorkListOp extends CMISOperation {

	private final static String modelId = "WfiWorklistEnd";
	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		KeyedCollection queryData = null;
		String condition = null;
		try {
			queryData = (KeyedCollection) context.getDataElement(modelId);
			condition = TableModelUtil.getQueryCondition(modelId, queryData, context, false, false, false);
			condition = StringUtil.transConditionStr(condition, "cus_name");
		} catch (Exception e) {
		}
		try {
			connection = this.getConnection(context);
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
			List<String> fields = new ArrayList<String>();
			fields.add("instanceid");
			fields.add("maininstanceid");
			fields.add("wfid");
			fields.add("wfname");
			fields.add("wfsign");
			fields.add("wfjobname");
			fields.add("wfstarttime");
			fields.add("wfendtime");
			fields.add("author");
			fields.add("spstatus");
			fields.add("lastuser");
			fields.add("costtimes");
			fields.add("table_name");
			fields.add("pk_value");
			fields.add("cus_id");
			fields.add("cus_name");
			fields.add("appl_type");
			fields.add("wfi_status");
			fields.add("prd_name");
			fields.add("amt");
			TableModelDAO dao = this.getTableModelDAO(context);
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			//读者列表LIKE
			String condition2 = "(ALLREADERSLIST IS NULL OR " +
					"ALLREADERSLIST='"+currentUserId+"' OR " +
					"ALLREADERSLIST = '"+currentUserId+";' OR " +
					"ALLREADERSLIST LIKE '"+currentUserId+";%' OR " +
					"ALLREADERSLIST LIKE '%;"+currentUserId+";%' OR " +
					"ALLREADERSLIST LIKE '%;"+currentUserId+"' OR " +
					"ALLREADERSLIST LIKE '%;"+currentUserId+";') ";
			if(condition==null || condition.trim().equals("")) {
				condition = " WHERE " + condition2;
			} else {
				condition += " AND " + condition2;
			}
			condition += " ORDER BY wfendtime DESC ";
			IndexedCollection icoll = dao.queryList(modelId, fields, condition, pageInfo, connection);
			WorkFlowUtil.addUserName4WF(icoll, new String[]{"lastuser"});
			/**add by lisj 2015-3-11 需求编号：【XD150303017】关于资产证券化的信贷改造 begin**/
			if(icoll!=null && icoll.size()>0){
				for(Iterator<KeyedCollection> iterator = icoll.iterator();iterator.hasNext();){
					KeyedCollection temp = (KeyedCollection)iterator.next();
					String prd_name = (String) temp.getDataValue("prd_name");
					String table_name = (String) temp.getDataValue("table_name");
					if(prd_name!=null && !"".equals(prd_name) &&  prd_name.equals("资产证券化")){
						if(table_name!=null && table_name.equals("PvpLoanApp")){
							KeyedCollection pla = dao.queryDetail("PvpLoanApp", temp.getDataValue("pk_value").toString(), connection);
							KeyedCollection capc = dao.queryDetail("CtrAssetProCont", pla.getDataValue("cont_no").toString(), connection);
						    temp.setDataValue("cus_name", capc.getDataValue("pro_name").toString());
							temp.setDataValue("cus_id", "#");
						}else{
							KeyedCollection iapa = dao.queryDetail("IqpAssetProApp", temp.getDataValue("pk_value").toString(), connection);
						    temp.setDataValue("cus_name", iapa.getDataValue("pro_name").toString());
							temp.setDataValue("cus_id", "#");
						}
					}
				}
			}
			/**add by lisj 2015-3-11 需求编号：【XD150303017】关于资产证券化的信贷改造 end**/
			icoll.setName(modelId+"List");
			TableModelUtil.parsePageInfo(context, pageInfo);
			this.putDataElement2Context(icoll, context);
			
		} catch (Exception e) {
			EMPLog.log("GetEndWorkListOp", EMPLog.ERROR, EMPLog.ERROR, "获取办结事项列表出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
