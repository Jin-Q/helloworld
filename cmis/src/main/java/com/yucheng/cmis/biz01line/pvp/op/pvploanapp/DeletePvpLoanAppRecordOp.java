package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class DeletePvpLoanAppRecordOp extends CMISOperation {

	private final String modelId = "PvpLoanApp";
	private final String contModelId = "CtrLoanCont";
	private final String modelPvpLimit = "PvpLimitSet";
	

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
			String pvp_amt = null;
			String cont_no = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				pvp_amt = (String)context.getDataValue("pvp_amt");
				cont_no = (String)context.getDataValue("cont_no");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			//KeyedCollection contKColl = dao.queryDetail(contModelId,cont_no, connection);
			//String cont_balance=(String) contKColl.getDataValue("cont_balance");
			//Double balance = 0.00;
			//计算合同余额
			//balance = Double.valueOf(cont_balance)+Double.valueOf(pvp_amt);
			//往合同中更新合同余额
			//contKColl.setDataValue("cont_balance", balance); 
			//int result = dao.update(contKColl, connection);
			//if(result!=1){
		    //	 throw new EMPException("update Failed!");
			//}  
			
			//更新放款额度控制表中(当日已放额度)
			String openDay = (String)context.getDataValue(CMISConstance.OPENDAY);
			KeyedCollection kc = dao.queryFirst(modelPvpLimit,null, " where open_day = '"+openDay+"'", connection);
			String sernoHelp = (String)kc.getDataValue("serno");
			if(sernoHelp != null && sernoHelp.length() > 0){
				String out_limit_amt = (String)kc.getDataValue("out_limit_amt");
				kc.setDataValue("out_limit_amt", Double.valueOf(out_limit_amt)-Double.valueOf(pvp_amt));
				dao.update(kc, connection);
			}
			
			//调用流程接口删除流程实例
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance()
			.getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID); 
			wfi.wfDelInstance(serno_value, modelId, connection);  
			
			context.addDataField("del", "success");//异步删除返回标识
			
		}catch (EMPException ee) {
			context.addDataField("del", "error"); 
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
