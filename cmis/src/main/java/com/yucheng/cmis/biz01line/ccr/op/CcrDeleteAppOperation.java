package com.yucheng.cmis.biz01line.ccr.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.exception.AgentException;

public class CcrDeleteAppOperation extends CMISOperation {

	private final String modelId = "CcrAppInfo";
	

	private final String serno_name = "serno";
	private final String cusId_name = "cus_id";
	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
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
			String apply_status="apply_status";
			//String cusId_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			//	cusId_value = (String)context.getDataValue(cusId_name);
				apply_status=(String)context.getDataValue(apply_status);
			} catch (Exception e) {
				EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "Kcoll["+modelId+"] can't find in context");				
			}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				

			//构建业务处理类
			try{				
				CcrComponent ccrComponent = (CcrComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(CcrPubConstant.CCR_COMPONENT,context, connection);
				ccrComponent.deleteApp(serno_value);
				if(context.containsKey("is_authorize")){
					String is_authorize = (String) context.getDataValue("is_authorize");
					if("1".equals(is_authorize)){//是授信项下时需要同步删除授信申请表中的授信申请信息
						TableModelDAO dao = this.getTableModelDAO(context);
						dao.deleteByPk("LmtAppFinGuar", serno_value, connection);
					}
				}
				WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance()
				.getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID); 
				wfi.wfDelInstance(serno_value, modelId, connection);

				//异步删除标志
				context.addDataField("flag","success");
				context.addDataField("msg","");
			//	ccrComponent.approvalApp(serno_value, cusId_value);
			}catch(AgentException e){
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, e.toString());
				throw new CMISException(e);
			}
			return null;
		}catch (EMPException ee) {
			context.addDataField("flag","fail");
			context.addDataField("msg",ee.getMessage());
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}

	}

}
