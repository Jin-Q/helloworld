package com.yucheng.cmis.biz01line.cus.group.ops.cusgrpinfoapply;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpMemberApplyComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteCusGrpInfoApplyRecordOp extends CMISOperation {
	
	private final String modelId = "CusGrpInfoApply";

	private final String grp_no_name = "grp_no";
	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String grp_no_value = null;
			try {
				grp_no_value = (String)context.getDataValue(grp_no_name);
			} catch (Exception e) {}
			if(grp_no_value == null || grp_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+grp_no_name+"] cannot be null!");
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Record Count: " + count);
			}
			CusGrpMemberApplyComponent cusGrpMemberApplyComponent = (CusGrpMemberApplyComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPMEMBERAPPLYCOMPONENT,context,connection);
			int flag = cusGrpMemberApplyComponent.deteleCusGrpMemberApply(grp_no_value,serno_value);
			if("1".equals(flag)){
				throw new Exception("删除  集团客户申请成员 表失败！");
			}
			//调用流程接口删除流程实例
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance()
			.getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID); 
			wfi.wfDelInstance(serno_value, modelId, connection);

			context.addDataField("flag", PUBConstant.SUCCESS);
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
