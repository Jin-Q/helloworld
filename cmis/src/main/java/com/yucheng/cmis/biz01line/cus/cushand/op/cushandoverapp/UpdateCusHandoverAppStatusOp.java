package com.yucheng.cmis.biz01line.cus.cushand.op.cushandoverapp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusHandoverAppComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusHandoverApp;
import com.yucheng.cmis.biz01line.cus.cushand.component.CusHandoverCfgComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
/**
 * 客户移交  提交操作处理类
 *         拒绝操作处理类
 *         同意操作处理类‘
 *         否决操作处理类
 * @Version bsbcmis
 * @author wuming 2012-3-26 
 * Description:
 */
public class UpdateCusHandoverAppStatusOp extends CMISOperation {
	
	private final String modelId = "CusHandoverApp";
	private final String serno_name = "serno";
//	private final String approve_status_name = "approve_status";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String strReturnMessage = CMISMessage.DEFEAT;
		TableModelDAO dao = this.getTableModelDAO(context);
		String firstApproveStatus = null;
		try{
			connection = this.getConnection(context);
			
			String serno_value = null;
			KeyedCollection kColl = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			CusHandoverAppComponent cusHandoverAppComponent = (CusHandoverAppComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSHANDOVERAPP,context,connection);
			boolean hasList = cusHandoverAppComponent.hasCusTrusteeList(serno_value);
			if (hasList) {
				//从移交申请表中获取需要的参数
				KeyedCollection appKcoll = dao.queryDetail("CusHandoverApp", serno_value, connection);
				String handoverScope= (String)appKcoll.getDataValue("handover_scope");//移交范围
				String handoverMode =(String) appKcoll.getDataValue("handover_mode");//移交方式
				String handoverBrId = (String) appKcoll.getDataValue("handover_br_id");//移交机构
				String handoverId =(String)appKcoll.getDataValue("handover_id");//移交人
				String receiverBrId = (String) appKcoll.getDataValue("receiver_br_id");//接收机构
				String receiverId = (String) appKcoll.getDataValue("receiver_id");//接收人
				String orgType = (String) appKcoll.getDataValue("org_type");
				
				IndexedCollection listIcoll = dao.queryList("CusHandoverLst", "where serno = '"+serno_value+"'", connection);
				KeyedCollection kColTmp = null;
				ArrayList<String> cusList=null;
				for(Iterator it = listIcoll.iterator();it.hasNext();){
					kColTmp = (KeyedCollection)it.next();
					if(cusList == null){
						cusList = new ArrayList<String>();
					}
					//获取合并的客户号，封装为list进行操作
					cusList.add((String)kColTmp.getDataValue("cus_id"));
				}
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("handoverScope", handoverScope);
				map.put("handoverMode", handoverMode);
				map.put("receivesorg", receiverBrId);
				map.put("receiveid", receiverId);
				map.put("handoverid", handoverId);
				map.put("handoversorg", handoverBrId);
				map.put("serno", serno_value);
				map.put("cusidlist", cusList);
	            
	            CusHandoverCfgComponent cushandovercfgcomponent = (CusHandoverCfgComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSHANDOVERCFG, context,connection);
	            cushandovercfgcomponent.middle(map);//获取配置中的sql文件修改
//				for(int a = 0;a<listIcoll.size();a++){
//		              KeyedCollection listKcoll = (KeyedCollection) listIcoll.get(a);
//		              KeyedCollection kColTmp = null;
//		              ArrayList<String> cusList=null;
//		              for(Iterator it = listIcoll.iterator();it.hasNext();){
//		                kColTmp = (KeyedCollection)it.next();
//		                if(cusList == null){
//		                  cusList = new ArrayList<String>();
//		                }
//		                //获取合并的客户号，封装为list进行操作
//		                cusList.add((String)kColTmp.get("cus_id"));
//		              }
//		              HashMap<String, Object> map = new HashMap<String, Object>();
//		              map.put("handoverScope", handoverScope);
//		              map.put("handoverMode", handoverMode);
//		              map.put("receivesorg", receiverBrId);
//		              map.put("receiveid", receiverId);
//		              map.put("handoverid", handoverId);
//		              map.put("handoversorg", handoverBrId);
//		              map.put("serno", serno_value);
//		              map.put("cusidlist", cusList);
//		            
//		            CusHandoverCfgComponent cushandovercfgcomponent = (CusHandoverCfgComponent)CMISComponentFactory
//					.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSHANDOVERCFG, context,connection);
//		            cushandovercfgcomponent.middle(map);//获取配置中的sql文件修改
//				}
				// 更新状态位
				cusHandoverAppComponent.updateStatus(serno_value, "997");
				ComponentHelper cHelper = new ComponentHelper();
		        CusHandoverApp cusHandoverApp = new CusHandoverApp();
		        cHelper.kcolTOdomain(cusHandoverApp, appKcoll);
		        cusHandoverAppComponent.doReceive(cusHandoverApp);//增加日志文件
				context.put("flag", "0");
				context.put("msg", "0");
			} else {
				context.put("flag", "2");
				context.put("msg", "2");
			}
			
			
	            
//	            CusLoanRelAgent cusLoanRelAgent = (CusLoanRelAgent) this.getAgentInstance(PUBConstant.CUSLOANREL);
//	            String str =  cusLoanRelAgent.insertsCusLoanRel(map);
	            
//	            if(str.equals(CMISMessage.ADDSUCCEESS)){
//		            ComponentHelper cHelper = new ComponentHelper();
//			        CusHandoverApp cusHandoverApp = new CusHandoverApp();
//			        cHelper.kcolTOdomain(cusHandoverApp, appKcoll);
//			        CusHandoverAppComponent cusHandoverAppComponent = (CusHandoverAppComponent) this.getComponent("CusHandoverApp");
//			        cusHandoverAppComponent.doReceive(cusHandoverApp);//增加日志文件
//	           }
	          
//			String status_value = null;
//			try {
//				status_value = (String)context.getDataValue(approve_status_name);
//				firstApproveStatus = status_value;
//			} catch (Exception e) {}
//			if(status_value == null || status_value.length() == 0)
//				throw new EMPJDBCException("The value of ["+approve_status_name+"] cannot be null!");
//			
//			/**
//			 * 状态共有以下几种情况
//			 * 'STD_ZB_HAND_STATUS' : {'00':'登记', '10':'提交', '20':'同意', '30':'成功'
//			 */
//			CusHandoverAppComponent cusHandoverAppComponent = (CusHandoverAppComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSHANDOVERAPP,context,connection);	
//			
//			//客户经理录入完成  提交操作处理
//			if(CusPubConstant.CUS_CREDIT_REGISTER.equals(status_value)){
//				 //检查是否存在 移交明细
//				boolean hasList = cusHandoverAppComponent.hasCusTrusteeList(serno_value);
//				if (hasList) {
//					status_value = CusPubConstant.CUS_CREDIT_SUBMIT;
//					// 更新状态位
//				 cusHandoverAppComponent.updateStatus(serno_value, status_value);
//				 context.put("flag", "0");
//				} else {
//					context.put("flag", "2");
//				}
//			//审批操作
//			}else if(CusPubConstant.CUS_CREDIT_SUBMIT.equals(status_value)){
//				//同意还是否决的标志
//				String updateFlag = (String)context.getDataValue("updateFlag");
//				if(PUBConstant.YES.equals(updateFlag)){
//					status_value = CusPubConstant.CUS_CREDIT_AGREE;
//				}else{
//					status_value = CusPubConstant.CUS_CREDIT_REJECT;
//				}
//				try {
//					kColl = (KeyedCollection)context.getDataElement(modelId);
//				} catch (Exception e) {}
//				if(kColl == null || kColl.size() == 0)
//					throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
//				kColl.setDataValue("approve_status", status_value);
//				kColl.setDataValue("supervise_date", (String)context.getDataValue("OPENDAY"));
//				int count=dao.update(kColl, connection);
//				if(count!=1){
//					throw new EMPException("Update Failed! Record Count: " + count);
//				}
//				context.put("flag", "0");
//			//同意操作
//			}else if(status_value.equals(CusPubConstant.CUS_CREDIT_AGREE)){
//				
//				status_value = CusPubConstant.CUS_CREDIT_SUCC;
//				
//				try {
//					kColl = (KeyedCollection)context.getDataElement(modelId);
//				} catch (Exception e) {}
//				if(kColl == null || kColl.size() == 0)
//					throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
//				kColl.setDataValue("approve_status", status_value);
//				kColl.setDataValue("receive_date", (String)context.getDataValue("OPENDAY"));
//				
//				strReturnMessage = cusHandoverAppComponent.updateStatus(
//						serno_value, status_value);
//				//String  handoverScope  = (String)kColl.getDataValue("handover_scope");    //移交范围
//				//if(handoverMode.equals("1")){
//				 //仅客户资料移交
//				IndexedCollection iColl= null;
//                //- 无论哪种方式提交，需要先查询出来。因为页面只有一部分。
//                CusHandoverLstComponent cusHandoverLstComponent = (CusHandoverLstComponent) CMISComponentFactory
//                    .getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSHANDOVERLST,context,connection);
//                List<CMISDomain> cusHandoverLstList = new ArrayList<CMISDomain>();
//                cusHandoverLstList=cusHandoverLstComponent.findCusHandoverLstListBySerno((String)kColl.getDataValue("serno"));
//                ComponentHelper cHelper = new ComponentHelper();
//                iColl = cHelper.domain2icol(cusHandoverLstList,"CusHandoverLst",CMISConstance.CMIS_LIST_IND);
////                KeyedCollection kCollToDb = null;
//                for(int i=0;i<iColl.size();i++){
////                    kCollToDb = (KeyedCollection)iColl.get(i);
//                    //仅客户资料移交还是业务一起移交在updateCusLoanRel里实现
////                    String strMessage = cusHandoverAppComponent.updateCusLoanRel(businessCode, receiverBrId, receiverId, orgType, handoverMode);
////                    if(strMessage.equals(CMISMessage.ADDDEFEAT)){
////                        return strReturnMessage;
////                    }
//                }
//				//}else{
//				//客户资料和业务移交  ( 暂时不实现！)
//					//IndexedCollection iColl=(IndexedCollection)context.getDataElement("CusHandoverLstList");
//				//}
//				cHelper = new ComponentHelper();
//				CusHandoverApp cusHandoverApp = new CusHandoverApp();
//				cusHandoverApp = (CusHandoverApp) cHelper.kcolTOdomain(cusHandoverApp, kColl);
//				//新增日志
//				String strReturnInsertLog = cusHandoverAppComponent.doReceive(cusHandoverApp);
//				if(strReturnInsertLog.equals(CMISMessage.ADDDEFEAT)){
//					return strReturnMessage;
//				}
//				kColl.setDataValue("approve_status", status_value);
//				kColl.setDataValue("receive_date", (String)context.getDataValue("OPENDAY"));
//				int count=dao.update(kColl, connection);
//				if(count!=1){
//					throw new EMPException("Update Failed! Record Count: " + count);
//				}
//				context.put("flag", "0");
//			}else if(CusPubConstant.CUS_CREDIT_REFUSE.equals(status_value)){		//拒绝操作	
//				
//				cusHandoverAppComponent.updateStatus(serno_value, status_value);
//				context.put("flag", "0");
//			}
//            context.put("approve_status", firstApproveStatus);
            
		}catch (Exception ee) {
			context.put("flag", "3");
			context.put("msg", "支行内移交失败，失败原因："+ee.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ee.printStackTrace();
			}
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "", ee);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return strReturnMessage;
	}
}
