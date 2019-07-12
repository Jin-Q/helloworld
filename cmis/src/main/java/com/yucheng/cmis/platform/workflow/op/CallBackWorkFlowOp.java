package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.PkGeneratorSet;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.TimeUtil;

/**
 * <p>流程退回任一步（打回）</p>
 * @author liuhw
 *
 */
public class CallBackWorkFlowOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		WorkflowServiceInterface wfi = null;
		Connection connection = null;
		String currentUserId = null;
		String instanceId = null;
		String nodeId = null;
		String nextNodeId = null;
		String nextNodeUser = null;
		String callBackModel = null;
		try {
			connection = this.getConnection(context);
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			nodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NODEID);
			nextNodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NEXTNODEID);
			nextNodeUser = (String) context.getDataValue(WorkFlowConstance.ATTR_NEXTNODEUSER);
			callBackModel = (String) context.getDataValue("callBackModel");
			/**插入打回流程的打回原因  modified wangj 2015/07/28 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  begin*/
			saveCallBackDiscs(context,connection);
			/**插入打回流程的打回原因  modified wangj 2015/07/28 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求 end*/
			
			/**修改出账申请表当中打回是否可修改字段 add by lisj 2015-8-5 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求begin**/
			updateApproveOpModel(context,connection);
			/**修改出账申请表当中打回是否可修改字段 add by lisj 2015-8-5 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求end**/
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			Map paramMap = new HashMap();
			paramMap.put(WorkFlowConstance.ATTR_EMPCONTEXT, context);
			WFIVO wfiVo = wfi.wfCallBack(instanceId, nodeId, currentUserId, nextNodeId, nextNodeUser, callBackModel, paramMap, connection);
			context.put(WorkFlowConstance.WFVO_RET_NAME, wfiVo);
			
		} catch (Exception e) {
			EMPLog.log("CallBackWorkFlowOp", EMPLog.ERROR, EMPLog.ERROR, "流程退回任一步（打回）出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}
	/**插入打回流程的打回原因  modified wangj 2015/07/28 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  begin*/
	public void saveCallBackDiscs(Context context,Connection connection) throws Exception{
		if (context.containsKey("callBackDiscs")&&context.getDataValue("callBackDiscs")!=null&&!"".equals(context.getDataValue("callBackDiscs"))) {
			String callBackDiscs = (String) context.getDataValue("callBackDiscs");
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			String instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			String nodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NODEID);
			String orgId = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
			String timeStr = TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
			Map<String,String> map = new HashedMap();
			map.put("instanceid", instanceId);
			map.put("nodeid", nodeId);
			map.put("userid",currentUserId);
			KeyedCollection result = new KeyedCollection();
			IndexedCollection ic = SqlClient.queryList4IColl("selectCurrentCommId",map,connection);
			if(ic.size()>0) result = (KeyedCollection) ic.get(0);
			String commentid="";
			if(result.containsKey("commentid")) commentid=(String) result.getDataValue("commentid");
			String condition = "WHERE INSTANCEID='" + instanceId+ "' AND NODEID='" + nodeId + "' AND INPUT_ID='"+ currentUserId + "' AND COMMENTID='" + commentid + "'";
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection icoll = dao.queryList("WfiBizCommentRecord",condition, connection);
			if (icoll != null && icoll.size() > 0) {
				KeyedCollection kcoll = (KeyedCollection) icoll.get(0);
				if (kcoll != null) {
					kcoll.put("fldvalue01", callBackDiscs);
					dao.update(kcoll, connection);
				}
			} else {
				PkGeneratorSet pkservice = (PkGeneratorSet) context.getService(CMISConstance.ATTR_PRIMARYKEYSERVICE);
				UNIDGenerator pk = (UNIDGenerator) pkservice.getGenerator("UNID");
				Map<String, String> insertMap = new HashedMap();
				insertMap.put("pk1", pk.getUNID());
				insertMap.put("instanceid", instanceId);
				insertMap.put("nodeid", nodeId);
				insertMap.put("fldvalue01", callBackDiscs);
				insertMap.put("input_id", currentUserId);
				insertMap.put("input_br_id", orgId);
				insertMap.put("commentid", commentid);
				insertMap.put("op_time", timeStr);
				SqlClient.insert("insertCallBackDiscWBCR", insertMap,connection);
			}
		}
	}
	/**插入打回流程的打回原因  modified wangj 2015/07/28 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  end*/
	/**修改出账申请表当中打回是否可修改字段 add by lisj 2015-8-5 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求begin**/
	private void updateApproveOpModel(Context context, Connection connection) throws EMPException {
		if(context.containsKey("approveOpModel") && context.getDataValue("approveOpModel")!=null && !"".equals(context.getDataValue("approveOpModel"))){
			String approveOpModel = (String) context.getDataValue("approveOpModel"); //1：打回可修改 0:打回不可修改
			String instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection wfiJoin = dao.queryFirst("WfiJoin", null, "WHERE INSTANCEID='"+instanceId+"'", connection);
			String serno  = wfiJoin.getDataValue("pk_value").toString();
			IndexedCollection  PLPIColl  =  dao.queryList("PvpLoanApp", "where serno ='"+serno+"'", connection);//贷款出账
			IndexedCollection  IEPIColl  =  dao.queryList("IqpExtensionPvp", "where serno ='"+serno+"'", connection);//展期出账
			if(PLPIColl!=null && PLPIColl.size()>0){
				KeyedCollection temp = (KeyedCollection) PLPIColl.get(0);
				temp.setDataValue("approve_modify_right", approveOpModel);
				dao.update(temp, connection);
			}else if(IEPIColl!=null && IEPIColl.size()>0){
				KeyedCollection temp = (KeyedCollection) IEPIColl.get(0);
				temp.setDataValue("approve_modify_right", approveOpModel);
				dao.update(temp, connection);
			}    
		}
	}
	/**修改出账申请表当中打回是否可修改字段 add by lisj 2015-8-5 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求end**/
}
