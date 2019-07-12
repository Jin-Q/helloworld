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
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.base.CMISConstance;

/**
 * <p>获取待办事项列表</p>
 * @author liuhw
 *
 */
public class GetToDoWorkListOp extends CMISOperation {

	private final static String modelId = "WfiWorklistTodo";
	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		KeyedCollection queryData = null;
		String condition = null;
		String ret_val = "ret";
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
			fields.add("author");
			fields.add("prenodeid");
			fields.add("prenodename");
			fields.add("nodeid");
			fields.add("nodename");
			fields.add("nodestatus");
			fields.add("nodestarttime");
			fields.add("nodeaccepttime");
			fields.add("nodeplanendtime");
			fields.add("currentnodeuser");
			fields.add("currentnodeusers");
			fields.add("originalusers");
			fields.add("table_name");
			fields.add("pk_value");
			fields.add("cus_id");
			fields.add("cus_name");
			fields.add("cus_crd_grade");//2015-01-29 Edited by FCL 应审批官意见 增加客户信用等级一栏
			fields.add("appl_type");
			fields.add("wfi_status");
			fields.add("prd_name");
			fields.add("amt");
			/* added by yangzy 2014/12/10 需求：XD140718026_新信贷系统授信进度查询改造 start */
			fields.add("orgid");
			/* added by yangzy 2014/12/10 需求：XD140718026_新信贷系统授信进度查询改造 end */
			TableModelDAO dao = this.getTableModelDAO(context);
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			//非拟稿||已经办理  && 流转中||过期办理  && （办理人员LIKE || (必办人员LIKE&&待签收)）
			//(此处可能存在一个bug（一般不会），当出现用户号如800001、80000等情况时，所以尽量避免存在诸如此类的用户ID)
			/** 不理解为何此处用 like '%%' 2014-02-18 唐顺岩  */
			//String condition2 = "(CURRENTNODEUSER LIKE '%"+currentUserId+"%' OR (CURRENTNODEUSERS LIKE '%"+currentUserId+"%' AND NODESTATUS='3')) AND " +
			String condition2 = "(CURRENTNODEUSER = '"+currentUserId+";' OR (CURRENTNODEUSERS = '"+currentUserId+";' AND NODESTATUS='3')) AND " +
			/** END */
					" (BDRAFT='0' OR ISPROCESSED='1') AND (WFSTATUS='0' OR WFSTATUS='5') AND WFI_STATUS <>'000'";
			if(condition==null || condition.trim().equals("")) {
				condition = " WHERE " + condition2;
			} else {
				condition += " AND " + condition2;
			}
			condition += " ORDER BY NODESTARTTIME DESC";
			IndexedCollection icoll = dao.queryList(modelId, fields, condition, pageInfo, connection);
			WorkFlowUtil.addUserName4WF(icoll, new String[]{"currentnodeuser"});
			/**add by lisj 2015-3-11 需求编号：【XD150303017】关于资产证券化的信贷改造 begin**/
			if(icoll!=null && icoll.size()>0){
				for(Iterator<KeyedCollection> iterator = icoll.iterator();iterator.hasNext();){
					KeyedCollection temp = (KeyedCollection)iterator.next();
					String prd_name = (String) temp.getDataValue("prd_name");
					String table_name = (String) temp.getDataValue("table_name");
					if(prd_name!=null && !"".equals(prd_name) && prd_name.equals("资产证券化")){
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
			/* added by yangzy 2014/12/10 需求：XD140718026_新信贷系统授信进度查询改造 start */
			SInfoUtils.addSOrgName(icoll, new String[]{"orgid"});
			/* added by yangzy 2014/12/10 需求：XD140718026_新信贷系统授信进度查询改造 end */
			//如果context中包含ret参数说明为首页工具中进入   2014-06-09
			if(context.containsKey("ret")){
				ret_val = "main";
			}
		} catch (Exception e) {
			EMPLog.log("GetToDoWorkListOp", EMPLog.ERROR, EMPLog.ERROR, "获取待办事项列表出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return ret_val;
	}

}
