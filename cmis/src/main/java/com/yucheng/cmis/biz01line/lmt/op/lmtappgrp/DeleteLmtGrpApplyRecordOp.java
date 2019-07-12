package com.yucheng.cmis.biz01line.lmt.op.lmtappgrp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class DeleteLmtGrpApplyRecordOp extends CMISOperation {

	private final String modelId = "LmtAppGrp";
	private final String modelIdApp = "LmtApply";

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			connection = this.getConnection(context);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);

			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0){
				context.addDataField("flag","fild");
				context.addDataField("msg","删除关联集团授信申请失败，错误描述，传入主键字段[业务编号]为空！");
				return "0";
			}

			TableModelDAO dao = this.getTableModelDAO(context);
			//删除集团授信申请信息
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				context.addDataField("flag","fild");
				context.addDataField("msg","删除关联集团授信申请失败，错误描述：未找到对应的申请记录！");
				return "0";
			}
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			String condition = " where grp_serno='"+serno_value+"'";
			IndexedCollection iColl = dao.queryList(modelIdApp, condition, connection);
			//删除成员申请分项信息
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kCollApp = (KeyedCollection) iColl.get(i);
				String sernoApp = kCollApp.getDataValue("serno").toString();
				lmtComponent.deleteLmtApplyDetailsBySerno(sernoApp);
				//变更保存时将原有授信台账复制到申请分项历史表
				if(kCollApp.containsKey("app_type") && "02".equals(kCollApp.getDataValue("app_type"))){
					Map<String, String> conditionFields = new HashMap<String, String>();
					conditionFields.put("serno", sernoApp);
					lmtComponent.deleteByField("LmtAppDetailsHis", conditionFields);
				}
			}
			//若为变更则删除协议历史
			if(context.containsKey("app_type") && "02".equals(context.getDataValue("app_type"))){
				Map<String, String> conditionFields = new HashMap<String, String>();
				conditionFields.put("serno", serno_value);
				lmtComponent.deleteByField("LmtAppGrpMemHis", conditionFields);
			}
			
			//删除成员申请主表信息
			lmtComponent.deleteLmtGrpMemberAppBySerno(serno_value);
			
			//调用流程接口删除流程实例
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance()
			.getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID); 
			wfi.wfDelInstance(serno_value, modelId, connection);
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch(Exception e){
			context.addDataField("flag","fild");
			context.addDataField("msg","删除关联集团授信申请失败，错误描述："+e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
