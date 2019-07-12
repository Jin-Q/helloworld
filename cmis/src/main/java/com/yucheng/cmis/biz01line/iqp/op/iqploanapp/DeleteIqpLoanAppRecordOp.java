package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class DeleteIqpLoanAppRecordOp extends CMISOperation {

	private final String modelId = "IqpLoanApp";

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
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			String prd_id = (String) kColl.getDataValue("prd_id");
			KeyedCollection kCollForBatch = null;
			if(prd_id.equals("300021") || prd_id.equals("300020")){//判断是否贴现业务
				String condition = " where serno='"+serno_value+"'";
				kCollForBatch=dao.queryFirst("IqpBatchMng", null, condition, connection);
				if(kCollForBatch.getDataValue("batch_no")!=null&&!(kCollForBatch.getDataValue("batch_no")).equals("")){
					kCollForBatch.setDataValue("serno", "");//清掉批次与申请关系
					kCollForBatch.setDataValue("status", "01");//改回【登记】状态
					dao.update(kCollForBatch, connection);
				}
			}
			
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			cmisComponent.delSubTablesRecordBySerno(serno_value);
			cmisComponent.delSubTableGrtLoanRGuarBySerno(serno_value, dao, context);
			/*TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteAllByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed！Record Count: "+count);
			}*/
			
			//删除授信关系
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			iqpLoanAppComponent.deleteLmtRelation(serno_value);
			
			//调用流程接口删除流程实例
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance()
			.getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID); 
			wfi.wfDelInstance(serno_value, modelId, connection);  
			
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
