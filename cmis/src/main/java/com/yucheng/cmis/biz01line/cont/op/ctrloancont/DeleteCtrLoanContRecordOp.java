package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cont.component.IqpAppendTermsComponent;
import com.yucheng.cmis.biz01line.cont.pub.ContConstant;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteCtrLoanContRecordOp extends CMISOperation {
	private final String modelId = "CtrLoanCont";
	private final String modelIdGrtLoan = "GrtLoanRGur";
	private final String modelIdPvp = "PvpLoanApp";
	private final String modelIdAcc = "AccLoan";

	private final String cont_no_name = "cont_no";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String cont_no_value = null;
			String guar_cont_no_str = "";
			try {
				cont_no_value = (String)context.getDataValue(cont_no_name);
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			DataSource datasource =(DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			//查询出账表
			//'000':'待发起', '111':'审批中', '990':'取消', '991':'重办', '992':'打回', '993':'追回', '997':'通过', '998':'否决'
			 String conditionPvp = "where cont_no='"+cont_no_value+"' and approve_status in('111','997')";
	         IndexedCollection iCollPvp = dao.queryList(modelIdPvp, conditionPvp, connection);
	         String conditionAcc = "where cont_no='"+cont_no_value+"'";
	         //查询台账表(贷款意向，信托贷款不需要出账)
	         IndexedCollection iCollAcc = dao.queryList(modelIdAcc, conditionAcc, connection);
	         if((iCollPvp.size()>0 && iCollPvp != null) || (iCollAcc.size()>0 && iCollAcc != null)){
	            context.put("flag", "error");
	            context.put("msg", "存在审批中、通过状态出账或台账记录!");
	         }else{
	 			String condition = "where cont_no='"+cont_no_value+"'";
				IndexedCollection iColl = dao.queryList(modelIdGrtLoan, condition, connection);
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
					guar_cont_no_str += "'"+guar_cont_no+"',";
				}
				if(guar_cont_no_str.length()>1){
					guar_cont_no_str = guar_cont_no_str.substring(0, guar_cont_no_str.length()-1);
					/**调用担保模块接口,跟新担保合同状态*/ 
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				    service.updateGrtGuarContSta(guar_cont_no_str,datasource, connection);
				} 
				
	            //更新业务担保合同状态
				IqpAppendTermsComponent cmisComponent = (IqpAppendTermsComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(ContConstant.IQPAPPENDTREMSCCMPONTENT, context, connection);
				cmisComponent.updateGrtLoanRGur(cont_no_value,datasource, connection);
				
				//跟新合同状态
	            KeyedCollection kColl = dao.queryDetail(modelId, cont_no_value, connection);
				kColl.setDataValue("cont_status", "700");//撤销操作把合同状态改为撤销
				dao.update(kColl, connection);
				
				//增加贴现处理
				String prd_id = (String) kColl.getDataValue("prd_id");
				String serno = (String) kColl.getDataValue("serno");
				KeyedCollection kCollForBatch = null;
				if(prd_id.equals("300021") || prd_id.equals("300020")){//判断是否贴现业务
					String conditionstr = " where serno='"+serno+"'";
					kCollForBatch=dao.queryFirst("IqpBatchMng", null, conditionstr, connection);
					if(kCollForBatch.getDataValue("batch_no")!=null&&!(kCollForBatch.getDataValue("batch_no")).equals("")){
						//kCollForBatch.setDataValue("cont_no", "");//清掉批次与合同关系
						//kCollForBatch.setDataValue("serno", "");//清掉批次与申请关系
						kCollForBatch.setDataValue("status", "04");//改为【作废】状态
						dao.update(kCollForBatch, connection);
					}
				}
				
				//如果存在出账记录，前提是 没有 '111':'审批中', '997':'通过'(前面已判断)
				//则把该出账记录改为 取消 (否决的不用改)
				String conditionStr = "where cont_no='"+cont_no_value+"' and approve_status!='998'";
		        IndexedCollection iCollPvpNeedChange = dao.queryList(modelIdPvp, conditionStr, connection);
		        for(int i=0;i<iCollPvpNeedChange.size();i++){
		        	KeyedCollection kCollPvpNeedChange = (KeyedCollection)iCollPvpNeedChange.get(i);
		        	kCollPvpNeedChange.setDataValue("approve_status", "990");
		        	//同时更新流程节点状态，防止代办事项查询到  add by zhaozq 20140928 start
		        	String pvpserno = (String) kCollPvpNeedChange.getDataValue("serno");
		        	SqlClient.executeUpd("updateWfstatusForCont", pvpserno, "4", null , connection);
		        	//同时更新流程节点状态，防止代办事项查询到  add by zhaozq 20140928 end
		        	dao.update(kCollPvpNeedChange, connection);
		        	/**add by lisj 2015-2-4 修复出账审批流程BUG，发起人做追回操作后，客户移交无法进行操作 ，于2015-2-5上线 begin**/
		        	//删除在审批流程中的数据
		        	WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance()
					.getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID); 
					wfi.wfDelInstance(pvpserno, modelIdPvp, connection);
					/**add by lisj 2015-2-4 修复出账审批流程BUG，发起人做追回操作后，客户移交无法进行操作 ，于2015-2-5上线 end**/
		        }
				context.put("flag", "success");
				context.put("msg", "合同撤销成功!");
	         }
		}catch (Exception e) {
			context.put("flag", "error");
			context.put("msg", "合同撤销失败!原因:"+e.getMessage());
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
