package com.yucheng.cmis.biz01line.esb.op.trade.ecdsimple;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.op.trade.TranService;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 电子商业汇票银行承兑汇票撤回
 * @author yangzy
 * 说明：
 * 接收电票系统发送的撤回批次号，撤回
 * 
 */
public class TradeEcds4AcceptanceRecall extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		retKColl.put("cancel_result", "01");
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			String BATCH_NO = reqBody.getDataValue("BATCH_NO").toString().trim();//票据批次
			KeyedCollection approveInfoKColl = new KeyedCollection();
			String appTableModel = "";
			String sqlName4App = "";
			String sqlName4Cont = "";
			approveInfoKColl = (KeyedCollection)SqlClient.queryFirst("getAcceptanceApproveInfo4Ecds", BATCH_NO, null, connection);
			appTableModel = "IqpLoanApp";
			sqlName4App = "updateIqpLoanAppStatus4Ecds";
			sqlName4Cont = "updateCtrloanContStatus4Ecds";
			if(approveInfoKColl!=null&&approveInfoKColl.size()>0){
				String flag = (String)approveInfoKColl.getDataValue("flag");
				String app_serno = (String)approveInfoKColl.getDataValue("app_serno");
				String cont_no = (String)approveInfoKColl.getDataValue("cont_no");
				String pvp_serno = (String)approveInfoKColl.getDataValue("pvp_serno");
				if(flag!=null&&"1".equals(flag)){
					//申请撤销
					Map param4app = new HashMap();
					param4app.put("approve_status", "990");
					SqlClient.update(sqlName4App, app_serno, param4app, null, connection);
					//在途业务 撤销流程
					WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance()
					.getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID); 
					wfi.wfDelInstance(app_serno, appTableModel, connection);
					//担保关系撤销
					SqlClient.update("cancelGrtLoanGurBySerno", app_serno, null, null, connection);
					
				}else if(flag!=null&&"2".equals(flag)){
					//合同撤销
					Map param4cont = new HashMap();
					param4cont.put("cont_status", "700");
					SqlClient.update(sqlName4Cont, cont_no, param4cont, null, connection);
					//担保关系撤销
					SqlClient.update("cancelGrtLoanGurForDrftByContNo", cont_no, null, null, connection);
				}else if(flag!=null&&"3".equals(flag)){
					//出账撤销
					Map param4pvp = new HashMap();
					param4pvp.put("approve_status", "990");
					SqlClient.update("updatePvpLoanAppStatus4Ecds", pvp_serno, param4pvp, null, connection);
					//在途业务 删除流程
					WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance()
					.getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID); 
					wfi.wfDelInstance(pvp_serno, "PvpLoanApp", connection);
					//合同撤销
					Map param4cont = new HashMap();
					param4cont.put("cont_status", "700");
					SqlClient.update(sqlName4Cont, cont_no, param4cont, null, connection);
					//担保关系撤销
					SqlClient.update("cancelGrtLoanGurForDrftByContNo", cont_no, null, null, connection);
				}else if(flag!=null&&"4".equals(flag)){
					//授权撤销
					Map param = new HashMap();
					param.put("status", "03");
					SqlClient.update("updatePvpAuthorizeStatus4Ecds", pvp_serno, param, null, connection);
					//出账撤销
					Map param4pvp = new HashMap();
					param4pvp.put("approve_status", "990");
					SqlClient.update("updatePvpLoanAppStatus4Ecds", pvp_serno, param4pvp, null, connection);
					//合同撤销
					Map param4cont = new HashMap();
					param4cont.put("cont_status", "700");
					SqlClient.update(sqlName4Cont, cont_no, param4cont, null, connection);
					//担保关系撤销
					SqlClient.update("cancelGrtLoanGurForDrftByContNo", cont_no, null, null, connection);
					//added yangzy 2015/02/10 电票撤回增加删除未生效台账
					SqlClient.delete("deleteAccAccpForECDSCancel", cont_no, connection);
				}else if(flag!=null&&"5".equals(flag)){
					retKColl.setDataValue("ret_code", "9999");
					retKColl.setDataValue("ret_msg", "【 电子商业汇票银行承兑汇票撤回】,交易失败！");
					retKColl.setDataValue("cancel_result", "01");
				}
			}
			retKColl.setDataValue("cancel_result", "00");
			EMPLog.log("TradeEcds4AcceptanceRecall", EMPLog.INFO, 0, "【 电子商业汇票银行承兑汇票撤回】交易完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "9999");
			retKColl.setDataValue("ret_msg", "【 电子商业汇票银行承兑汇票撤回】,交易失败！");
			retKColl.setDataValue("cancel_result", "01");
			e.printStackTrace();
		}
		return retKColl;
	}

}
