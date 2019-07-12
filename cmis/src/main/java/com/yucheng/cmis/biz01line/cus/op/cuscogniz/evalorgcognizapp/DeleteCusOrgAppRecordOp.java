package com.yucheng.cmis.biz01line.cus.op.cuscogniz.evalorgcognizapp;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteCusOrgAppRecordOp extends CMISOperation {

	private final String modelId = "CusOrgApp";

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
			
			Map<String,String> refFields = new HashMap<String,String>();
            refFields.put("app_serno", serno_value);
            CusBaseComponent cusComponent = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusBase", context,connection);
            cusComponent.deleteByField("CusOrgInfo", refFields);
            
			//调用流程接口删除流程实例
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance()
			.getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID); 
			wfi.wfDelInstance(serno_value, modelId, connection);
            
			context.addDataField("flag", PUBConstant.SUCCESS);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
